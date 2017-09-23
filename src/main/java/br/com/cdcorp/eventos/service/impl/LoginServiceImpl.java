package br.com.cdcorp.eventos.service.impl;

import br.com.cdcorp.eventos.domain.model.Login;
import br.com.cdcorp.eventos.domain.repository.LoginRepository;
import br.com.cdcorp.eventos.infrastructure.encode.CriptografiaSenha;
import br.com.cdcorp.eventos.service.ConfiguracaoService;
import br.com.cdcorp.eventos.service.LoginService;
import br.com.cdcorp.eventos.service.NotificacaoService;
import br.com.cdcorp.eventos.service.exception.Notificacao;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

import static java.lang.String.format;

/**
 * Created by ceb on 06/07/17.
 */
@Service
@Transactional(readOnly = true)
public class LoginServiceImpl implements LoginService {

    private NotificacaoService notificacaoService;
    private LoginRepository loginRepository;

    private ConfiguracaoService configuracaoService;
    private CriptografiaSenha criptografiaSenha;

    @Autowired
    public LoginServiceImpl(NotificacaoService notificacaoService, LoginRepository loginRepository,
                            ConfiguracaoService configuracaoService, CriptografiaSenha criptografiaSenha) {
        this.notificacaoService = notificacaoService;
        this.loginRepository = loginRepository;
        this.configuracaoService = configuracaoService;
        this.criptografiaSenha = criptografiaSenha;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Login criarLoginUsuario(String email) {
        Login login = gerarLoginUsuario(email);
        return loginRepository.salvar(login);
    }

    protected Login gerarLoginUsuario(String email) {
        Notificacao notificacao = new Notificacao();

        validarLogin(notificacao, email);

        if (notificacao.temErro()) {
            notificacao.throwErros();
        }

        String senhaGerada = criptografiaSenha.gerarSenha();

        String assunto = configuracaoService.get("notificacao.loginGerado.assunto");
        String templateMensagem = configuracaoService.get("notificacao.loginGerado.corpo");

        String mensagemDefault = gerarMensagemDeCadastroConcluido(templateMensagem, email, senhaGerada);

        notificacaoService.enviarEmail(email, assunto, mensagemDefault);

        String senhaGeradaCriptografada = criptografiaSenha.criptografar(senhaGerada);

        Login login = new Login(email, senhaGeradaCriptografada);
        return login;
    }

    private void validarLogin(Notificacao notificacao, String email) {
        if (loginRepository.findByEmail(email) != null) notificacao.addErro("email", "registrao_ja_cadastrado", format("E-mail %s já cadastrado.", email));
    }

    protected String gerarMensagemDeCadastroConcluido(String templateMensagem, String email, String senha) {
        PebbleEngine engine = new PebbleEngine.Builder().loader(new StringLoader()).build();
        PebbleTemplate compiledTemplate = null;

        StringWriter writer = new StringWriter();

        Map<String, Object> context = new HashMap<>();
        context.put("login", email);
        context.put("senha", senha);

        try {
            compiledTemplate = engine.getTemplate(templateMensagem);
            compiledTemplate.evaluate(writer, context);
        } catch (PebbleException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return writer.toString();
    }

    @Override
    public void confirmarCadastro(String email) {
        // TODO
    }

    @Override
    public void desativarUsuario(String email) {
        // TODO
    }

    @Override
    public void trocarSenha(String email, String senha) {
        // TODO
    }
}

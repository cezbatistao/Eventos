package br.com.cdcorp.eventos.service.impl;

import br.com.cdcorp.eventos.domain.model.Login;
import br.com.cdcorp.eventos.service.LoginService;
import br.com.cdcorp.eventos.service.exception.Notificacao;
import br.com.cdcorp.eventos.service.exception.NotificacaoErro;
import br.com.cdcorp.eventos.service.exception.NotificacaoException;
import br.com.cdcorp.eventos.test.support.IntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class LoginServiceImplIntegrationTests extends IntegrationTest {

    @Autowired
    private LoginService loginService;

    @Test
    public void criarLoginComDadosValidos() {
        String email = "carlos@carlos.com.br";

        Login loginCriado = loginService.criarLoginUsuario(email);

        assertThat(loginCriado).isNotNull();
        assertThat(loginCriado.getId()).isNotNull();
        assertThat(loginCriado.getLogin()).isEqualTo(email);
        assertThat(loginCriado.getSenha()).isNotEmpty();
        assertThat(loginCriado.getAtivo()).isTrue();
        assertThat(loginCriado.getCadastroConfirmado()).isFalse();
    }

    @Test
    public void validarLoginJaCadastrado() {
        String email = "carlos@carlos.com.br";
        loginService.criarLoginUsuario(email);

        NotificacaoErro notificacaoErro= null;
        try {
            loginService.criarLoginUsuario(email);
        } catch (NotificacaoException ex) {
            List<NotificacaoErro> notificacoes = ex.getNotificacoes();
            assertThat(notificacoes).hasSize(1);
            notificacaoErro = notificacoes.get(0);
        }

        assertThat(notificacaoErro).isNotNull();
        assertThat(notificacaoErro.getErro()).isEqualTo("registrao_ja_cadastrado");
        assertThat(notificacaoErro.getMensagem()).isEqualTo(format("E-mail %s já cadastrado.", email));
    }
}

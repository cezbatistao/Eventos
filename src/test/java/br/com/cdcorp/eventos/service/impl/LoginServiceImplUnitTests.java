package br.com.cdcorp.eventos.service.impl;

import br.com.cdcorp.eventos.domain.model.Login;
import br.com.cdcorp.eventos.domain.repository.LoginRepository;
import br.com.cdcorp.eventos.infrastructure.encode.CriptografiaSenha;
import br.com.cdcorp.eventos.service.ConfiguracaoService;
import br.com.cdcorp.eventos.service.NotificacaoService;
import br.com.cdcorp.eventos.service.exception.NotificacaoErro;
import br.com.cdcorp.eventos.service.exception.NotificacaoException;
import br.com.cdcorp.eventos.test.support.UnitTest;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.*;

public class LoginServiceImplUnitTests extends UnitTest {

    private LoginRepository loginRepository;

    private LoginServiceImpl loginService;
    private NotificacaoService notificacaoService;

    private ConfiguracaoService configuracaoService;
    private CriptografiaSenha criptografiaSenha;

    @Before
    public void setup() {
        configuracaoService = mock(ConfiguracaoService.class);
        criptografiaSenha = mock(CriptografiaSenha.class);

        loginRepository = mock(LoginRepository.class);

        notificacaoService = mock(NotificacaoService.class);
        loginService = spy(new LoginServiceImpl(notificacaoService, loginRepository, configuracaoService, criptografiaSenha));
    }

    @Test
    public void validarLoginJaCriadoParaOEmailInformado() {
        String email = "carlos@carlos.com.br";

        when(loginRepository.findByEmail(email)).thenReturn(new Login(email, "qualquer senha"));

        NotificacaoErro notificacaoErro = null;
        try {
            loginService.gerarLoginUsuario(email);
        } catch (NotificacaoException ex) {
            List<NotificacaoErro> notificacoes = ex.getNotificacoes();
            assertThat(notificacoes).hasSize(1);
            notificacaoErro = notificacoes.get(0);
        }

        assertThat(notificacaoErro).isNotNull();
        assertThat(notificacaoErro.getMensagem()).isEqualTo(format("E-mail %s já cadastrado.", email));
    }

    @Test
    public void criandoUmLoginParaOEmailInformado() {
        when(configuracaoService.get("notificacao.loginGerado.assunto")).thenReturn("assuntoto");
        when(configuracaoService.get("notificacao.loginGerado.corpo")).thenReturn("corpo");
        when(criptografiaSenha.criptografar(anyString())).thenReturn("senha criptografada");

        String email = "carlos@carlos.com.br";

        doReturn("Mensagem...").when(loginService).gerarMensagemDeCadastroConcluido(anyString(), eq(email), anyString());

        Login login = loginService.gerarLoginUsuario(email);

        assertThat(login).isNotNull();
        assertThat(login.getLogin()).isEqualTo(email);
        assertThat(login.getSenha()).isNotBlank();
        assertThat(login.getAtivo()).isEqualTo(true);
        assertThat(login.getCadastroConfirmado()).isEqualTo(false);
    }
}

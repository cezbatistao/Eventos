package br.com.cdcorp.eventos.infrastructure.repository;

import br.com.cdcorp.eventos.domain.model.Login;
import br.com.cdcorp.eventos.domain.repository.LoginRepository;
import br.com.cdcorp.eventos.test.support.IntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class LoginRepositoryMysqlIntegrationTests extends IntegrationTest {

    @Autowired
    private LoginRepository loginRepository;

    @Test
    public void validarAoTentarRecuperarUmLoginQueNaoExiste() {
        Login login = loginRepository.get(3423432L);
        assertThat(login).isNull();
    }
}

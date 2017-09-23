package br.com.cdcorp.eventos.infrastructure.repository;

import br.com.cdcorp.eventos.domain.model.PessoaFisica;
import br.com.cdcorp.eventos.domain.repository.PessoaFisicaRepository;
import br.com.cdcorp.eventos.test.support.IntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class PessoaFisicaRepositoryMysqlIntegrationTests extends IntegrationTest {

    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;

    @Test
    public void validarAoTentarRecuperarUmaPessaFisicaQueNaoExiste() {
        PessoaFisica pessoaFisica = pessoaFisicaRepository.get(42343L);
        assertThat(pessoaFisica).isNull();
    }
}

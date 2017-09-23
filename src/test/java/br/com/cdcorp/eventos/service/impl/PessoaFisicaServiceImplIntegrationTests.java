package br.com.cdcorp.eventos.service.impl;

import br.com.cdcorp.eventos.domain.model.Endereco;
import br.com.cdcorp.eventos.domain.model.Pessoa;
import br.com.cdcorp.eventos.domain.model.PessoaFisica;
import br.com.cdcorp.eventos.domain.model.TipoPessoaFisica;
import br.com.cdcorp.eventos.service.PessoaFisicaService;
import br.com.cdcorp.eventos.test.support.IntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class PessoaFisicaServiceImplIntegrationTests extends IntegrationTest {

    @Autowired
    private PessoaFisicaService pessoaFisicaService;

    @Test
    public void criarUmaPessoaFisicaComDadosValidos() {
        String nome = "Carlos";
        String email = "carlos@carlos.com.br";
        LocalDate dataNascimento = LocalDate.of(1982, 10, 10);
        String celular = "(19) 99999-7777";
        String telefone = "(19) 3232-5454";
        String cpf = "350.518.412-87";
        String rg = "12.123.123-9";
        TipoPessoaFisica tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE;

        Endereco endereco = new Endereco("Rua Sem Nome", "123", "APTO 45", "12.345-678",
                "Nome do Bairro", "SP", "Campinas");
        PessoaFisica pessoaFisica = new PessoaFisica(nome, email, celular, dataNascimento, cpf, endereco, tipoPessoaFisica);
        pessoaFisica.setEndereco(endereco);
        pessoaFisica.setRg(rg);
        pessoaFisica.setTelefone(telefone);

        pessoaFisicaService.salvar(pessoaFisica);

        PessoaFisica pessoaFisicaCadastrada = pessoaFisicaService.findByCpf(cpf);

        assertThat(pessoaFisicaCadastrada).isNotNull();
        assertThat(pessoaFisicaCadastrada.getId()).isNotNull();
        assertThat(pessoaFisicaCadastrada.getNome()).isEqualTo(nome);
        assertThat(pessoaFisicaCadastrada.getEmail()).isEqualTo(email);
        assertThat(pessoaFisicaCadastrada.getCelular()).isEqualTo(celular);
        assertThat(pessoaFisicaCadastrada.getTelefone()).isEqualTo(telefone);
        assertThat(pessoaFisicaCadastrada.getDataNascimento()).isEqualTo(dataNascimento);
        assertThat(pessoaFisicaCadastrada.getCpf()).isEqualTo(cpf);
        assertThat(pessoaFisicaCadastrada.getRg()).isEqualTo(rg);
        assertThat(pessoaFisicaCadastrada.getTipoPessoaFisica()).isEqualTo(tipoPessoaFisica);
        assertThat(pessoaFisicaCadastrada.getEndereco()).isNotNull();
        assertThat(pessoaFisicaCadastrada.getEndereco().getLogradouro()).isEqualTo(endereco.getLogradouro());
        assertThat(pessoaFisicaCadastrada.getEndereco().getNumero()).isEqualTo(endereco.getNumero());
        assertThat(pessoaFisicaCadastrada.getEndereco().getComplemento()).isEqualTo(endereco.getComplemento());
        assertThat(pessoaFisicaCadastrada.getEndereco().getCep()).isEqualTo(endereco.getCep());
        assertThat(pessoaFisicaCadastrada.getEndereco().getBairro()).isEqualTo(endereco.getBairro());
        assertThat(pessoaFisicaCadastrada.getEndereco().getEstado()).isEqualTo(endereco.getEstado());
        assertThat(pessoaFisicaCadastrada.getEndereco().getCidade()).isEqualTo(endereco.getCidade());
        assertThat(pessoaFisicaCadastrada.getLogin()).isNotNull();
        assertThat(pessoaFisicaCadastrada.getLogin().getId()).isNotNull();
        assertThat(pessoaFisicaCadastrada.getLogin().getLogin()).isEqualTo(email);
        assertThat(pessoaFisicaCadastrada.getLogin().getSenha()).isNotEmpty();
        assertThat(pessoaFisicaCadastrada.getLogin().getAtivo()).isTrue();
        assertThat(pessoaFisicaCadastrada.getLogin().getCadastroConfirmado()).isFalse();
    }

    @Test
    public void criarUmaPessoaFisicaComDadosValidosENaoInformandoOsNaoObrigatorios() {
        String nome = "Carlos";
        String email = "carlos@carlos.com.br";
        LocalDate dataNascimento = LocalDate.of(1982, 10, 10);
        String celular = "(19) 99999-7777";
        String cpf = "350.518.412-87";
        TipoPessoaFisica tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE;

        Endereco endereco = new Endereco("Rua Sem Nome", "123", null, "12.345-678",
                "Nome do Bairro", "SP", "Campinas");
        PessoaFisica pessoaFisica = new PessoaFisica(nome, email, celular, dataNascimento, cpf, endereco, tipoPessoaFisica);
        pessoaFisica.setEndereco(endereco);

        pessoaFisicaService.salvar(pessoaFisica);

        PessoaFisica pessoaFisicaCadastrada = pessoaFisicaService.findByCpf(cpf);

        assertThat(pessoaFisicaCadastrada).isNotNull();
        assertThat(pessoaFisicaCadastrada.getId()).isNotNull();
        assertThat(pessoaFisicaCadastrada.getNome()).isEqualTo(nome);
        assertThat(pessoaFisicaCadastrada.getEmail()).isEqualTo(email);
        assertThat(pessoaFisicaCadastrada.getCelular()).isEqualTo(celular);
        assertThat(pessoaFisicaCadastrada.getTelefone()).isNull();
        assertThat(pessoaFisicaCadastrada.getDataNascimento()).isEqualTo(dataNascimento);
        assertThat(pessoaFisicaCadastrada.getCpf()).isEqualTo(cpf);
        assertThat(pessoaFisicaCadastrada.getRg()).isNull();
        assertThat(pessoaFisicaCadastrada.getTipoPessoaFisica()).isEqualTo(tipoPessoaFisica);
        assertThat(pessoaFisicaCadastrada.getEndereco()).isNotNull();
        assertThat(pessoaFisicaCadastrada.getEndereco().getLogradouro()).isEqualTo(endereco.getLogradouro());
        assertThat(pessoaFisicaCadastrada.getEndereco().getNumero()).isEqualTo(endereco.getNumero());
        assertThat(pessoaFisicaCadastrada.getEndereco().getComplemento()).isEqualTo(endereco.getComplemento());
        assertThat(pessoaFisicaCadastrada.getEndereco().getCep()).isEqualTo(endereco.getCep());
        assertThat(pessoaFisicaCadastrada.getEndereco().getBairro()).isEqualTo(endereco.getBairro());
        assertThat(pessoaFisicaCadastrada.getEndereco().getEstado()).isEqualTo(endereco.getEstado());
        assertThat(pessoaFisicaCadastrada.getEndereco().getCidade()).isEqualTo(endereco.getCidade());
        assertThat(pessoaFisicaCadastrada.getLogin()).isNotNull();
        assertThat(pessoaFisicaCadastrada.getLogin().getId()).isNotNull();
        assertThat(pessoaFisicaCadastrada.getLogin().getLogin()).isEqualTo(email);
        assertThat(pessoaFisicaCadastrada.getLogin().getSenha()).isNotEmpty();
        assertThat(pessoaFisicaCadastrada.getLogin().getAtivo()).isTrue();
        assertThat(pessoaFisicaCadastrada.getLogin().getCadastroConfirmado()).isFalse();
    }

    @Test
    public void atualizarOsDadosDeUmaPessoaFisicaComDadosValidosENaoInformandoOsNaoObrigatorios() {
        String nome = "Carlos";
        String email = "carlos@carlos.com.br";
        LocalDate dataNascimento = LocalDate.of(1982, 10, 10);
        String celular = "(19) 99999-7777";
        String cpf = "350.518.412-87";
        TipoPessoaFisica tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE;

        Endereco endereco = new Endereco("Rua Sem Nome", "123", null, "12.345-678",
                "Nome do Bairro", "SP", "Campinas");
        PessoaFisica pessoaFisica = new PessoaFisica(nome, email, celular, dataNascimento, cpf, endereco, tipoPessoaFisica);
        pessoaFisica.setEndereco(endereco);

        pessoaFisicaService.salvar(pessoaFisica);
        PessoaFisica pessoaFisicaCadastrada = pessoaFisicaService.findByCpf(cpf);

        String novoCelular = "(19) 99999-8888";
        String novoEmail = "carlos@corp.com";
        TipoPessoaFisica novoTipoPessoaFisica = TipoPessoaFisica.PROFISSIONAL;

        pessoaFisicaCadastrada.setCelular(novoCelular);
        pessoaFisicaCadastrada.setEmail(novoEmail);
        pessoaFisicaCadastrada.setTipoPessoaFisica(novoTipoPessoaFisica);

        pessoaFisicaService.atualizar(pessoaFisicaCadastrada);

        String sqlCount = "select count(*) from pessoa_fisica";
        Integer totalPessoasFisicasCadastradas = jdbcTemplate.queryForObject(sqlCount, Integer.class);

        assertThat(totalPessoasFisicasCadastradas).isEqualTo(1);

        PessoaFisica pessoaFisicaAtualizado = pessoaFisicaService.findByCpf(cpf);

        assertThat(pessoaFisicaCadastrada).isNotNull();
        assertThat(pessoaFisicaCadastrada.getId()).isNotNull();
        assertThat(pessoaFisicaCadastrada.getNome()).isEqualTo(nome);
        assertThat(pessoaFisicaCadastrada.getEmail()).isEqualTo(novoEmail);
        assertThat(pessoaFisicaCadastrada.getCelular()).isEqualTo(novoCelular);
        assertThat(pessoaFisicaCadastrada.getTelefone()).isNull();
        assertThat(pessoaFisicaCadastrada.getDataNascimento()).isEqualTo(dataNascimento);
        assertThat(pessoaFisicaCadastrada.getCpf()).isEqualTo(cpf);
        assertThat(pessoaFisicaCadastrada.getRg()).isNull();
        assertThat(pessoaFisicaCadastrada.getTipoPessoaFisica()).isEqualTo(novoTipoPessoaFisica);
        assertThat(pessoaFisicaCadastrada.getEndereco()).isNotNull();
        assertThat(pessoaFisicaCadastrada.getEndereco().getLogradouro()).isEqualTo(endereco.getLogradouro());
        assertThat(pessoaFisicaCadastrada.getEndereco().getNumero()).isEqualTo(endereco.getNumero());
        assertThat(pessoaFisicaCadastrada.getEndereco().getComplemento()).isEqualTo(endereco.getComplemento());
        assertThat(pessoaFisicaCadastrada.getEndereco().getCep()).isEqualTo(endereco.getCep());
        assertThat(pessoaFisicaCadastrada.getEndereco().getBairro()).isEqualTo(endereco.getBairro());
        assertThat(pessoaFisicaCadastrada.getEndereco().getEstado()).isEqualTo(endereco.getEstado());
        assertThat(pessoaFisicaCadastrada.getEndereco().getCidade()).isEqualTo(endereco.getCidade());
        assertThat(pessoaFisicaCadastrada.getLogin()).isNotNull();
        assertThat(pessoaFisicaCadastrada.getLogin().getId()).isNotNull();
        assertThat(pessoaFisicaCadastrada.getLogin().getLogin()).isEqualTo(email);
        assertThat(pessoaFisicaCadastrada.getLogin().getSenha()).isNotEmpty();
        assertThat(pessoaFisicaCadastrada.getLogin().getAtivo()).isTrue();
        assertThat(pessoaFisicaCadastrada.getLogin().getCadastroConfirmado()).isFalse();
    }
}

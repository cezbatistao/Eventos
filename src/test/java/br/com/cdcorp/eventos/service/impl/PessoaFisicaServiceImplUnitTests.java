package br.com.cdcorp.eventos.service.impl;

import br.com.cdcorp.eventos.domain.model.Endereco;
import br.com.cdcorp.eventos.domain.model.Login;
import br.com.cdcorp.eventos.domain.model.PessoaFisica;
import br.com.cdcorp.eventos.domain.model.TipoPessoaFisica;
import br.com.cdcorp.eventos.domain.repository.PessoaFisicaRepository;
import br.com.cdcorp.eventos.service.LoginService;
import br.com.cdcorp.eventos.service.NotificacaoService;
import br.com.cdcorp.eventos.service.exception.NotificacaoErro;
import br.com.cdcorp.eventos.service.exception.NotificacaoException;
import br.com.cdcorp.eventos.test.support.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.tools4j.spockito.Spockito;

import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PessoaFisicaServiceImplUnitTests extends UnitTest {

    private PessoaFisicaRepository pessoaFisicaRepository;

    private LoginService loginService;
    private NotificacaoService notificacaoService;
    private PessoaFisicaServiceImpl pessoaFisicaService;

    @Before
    public void setup() {
        pessoaFisicaRepository = mock(PessoaFisicaRepository.class);

        notificacaoService = mock(NotificacaoService.class);
        loginService = mock(LoginServiceImpl.class);

        pessoaFisicaService = new PessoaFisicaServiceImpl(loginService, notificacaoService, pessoaFisicaRepository);
    }

    @Test
    @Spockito.Name("[{row}]: com mensagem experada: {7}")
    @Spockito.Unroll({
            "| Nome     | Email                 | Data Nascimento  | Celular          | CPF             | RG            | Tipo Pessoa Fisica  | Mensagem Experada                           | ",
            "|          | carlos@carlos.com.br  | 1982-10-10       | (19) 99999-7777  | 350.518.412-87  | 33.333.333.3  | ESTUDANTE           | Nome é obrigatório.                         | ",
            "| Carlos   |                       | 1982-10-10       | (19) 99999-7777  | 350.518.412-87  | 33.333.333.3  | ESTUDANTE           | E-mail é obrigatório.                       | ",
            "| Carlos   | lfdshalefsrfse323     | 1982-10-10       | (19) 99999-7777  | 350.518.412-87  | 33.333.333.3  | ESTUDANTE           | E-mail inválido.                            | ",
            "| Carlos   | carlos@carlos.com.br  | null             | (19) 99999-7777  | 350.518.412-87  | 33.333.333.3  | ESTUDANTE           | Data de nascimento é obrigatório.           | ",
            "| Carlos   | carlos@carlos.com.br  | 1982-10-10       |                  | 350.518.412-87  | 33.333.333.3  | ESTUDANTE           | Celular é obrigatório.                      | ",
            "| Carlos   | carlos@carlos.com.br  | 1982-10-10       | (19) 343243243   | 350.518.412-87  | 33.333.333.3  | ESTUDANTE           | Celular inválido.                           | ",
            "| Carlos   | carlos@carlos.com.br  | 2015-10-10       | (19) 99999-7777  | 350.518.412-87  | 33.333.333.3  | ESTUDANTE           | Idade mínima para participar é de 18 anos.  | ",
            "| Carlos   | carlos@carlos.com.br  | 1982-10-10       | (19) 99999-7777  |                 | 33.333.333.3  | ESTUDANTE           | CPF é obrigatório.                          | ",
            "| Carlos   | carlos@carlos.com.br  | 1982-10-10       | (19) 99999-7777  | 123.789.987-78  | 33.333.333.3  | ESTUDANTE           | CPF inválido.                               | "
    })
    public void validandoCamposParaCriarPessoaFisica(String nome, String email, LocalDate dataNascimento, String celular,
                                                     String cpf, String rg, TipoPessoaFisica tipoPessoaFisica, String mensagemExperada) {
        Endereco endereco = new Endereco("Rua Sem Nome", "123", null, "12.345-678",
                "Nome do Bairro", "SP", "Campinas");
        PessoaFisica pessoaFisica = new PessoaFisica(nome, email, celular, dataNascimento, cpf, endereco, tipoPessoaFisica);
        pessoaFisica.setEndereco(endereco);

        NotificacaoErro notificacaoErro = null;
        try {
            pessoaFisicaService.salvar(pessoaFisica);
        } catch (NotificacaoException ex) {
            List<NotificacaoErro> notificacoes = ex.getNotificacoes();
            assertThat(notificacoes).hasSize(1);
            notificacaoErro = notificacoes.get(0);
        }

        assertThat(notificacaoErro).isNotNull();
        assertThat(notificacaoErro.getMensagem()).isEqualTo(mensagemExperada);
    }

    @Test
    public void erroDePessoaFisicaJaCadastrada() {
        String nome = "Carlos";
        String email = "carlos@carlos.com.br";
        LocalDate dataNascimento = LocalDate.of(1982, 10, 10);
        String celular = "(19) 99999-7777";
        String cpf = "350.518.412-87";
        TipoPessoaFisica tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE;

        Endereco endereco = new Endereco("Rua Sem Nome", "123", null, "12.345-678",
                "Nome do Bairro", "SP", "Campinas");
        PessoaFisica pessoaFisica = new PessoaFisica(nome, email, celular, dataNascimento, cpf, endereco, tipoPessoaFisica);

        when(pessoaFisicaRepository.findByCpf(cpf)).thenReturn(pessoaFisica);

        NotificacaoErro notificacaoErro = null;
        try {
            pessoaFisicaService.salvar(pessoaFisica);
        } catch (NotificacaoException ex) {
            List<NotificacaoErro> notificacoes = ex.getNotificacoes();
            assertThat(notificacoes).hasSize(1);
            notificacaoErro = notificacoes.get(0);
        }

        assertThat(notificacaoErro).isNotNull();
        assertThat(notificacaoErro.getMensagem()).isEqualTo(format("Pessoa física com CPF %s já cadastrado.", cpf));
    }

    @Test
    public void criarUmaPessoaFisicaComDadosValidos() {
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

        when(loginService.criarLoginUsuario(email)).thenReturn(mock(Login.class));

        when(pessoaFisicaRepository.salvar(argThat(new ArgumentMatcher<PessoaFisica>() {
            @Override
            public boolean matches(Object pessoaFisicaParaVerificar) {
                PessoaFisica pessoaFisicaActual = (PessoaFisica) pessoaFisicaParaVerificar;
                assertThat(pessoaFisicaActual).isNotNull();
                assertThat(pessoaFisicaActual.getEndereco()).isNotNull();
                assertThat(pessoaFisicaActual.getLogin()).isNotNull();
                return false;
            }
        }))).thenReturn(pessoaFisica);

        pessoaFisicaService.salvar(pessoaFisica);
    }

    @Test
    @Spockito.Name("[{row}]: com mensagem experada: {6}")
    @Spockito.Unroll({
            "| Logradouro    | Numero  | CEP         | Bairro          | Estado  | Cidade    | Mensagem Experada          | ",
            "|               | 45712M  | 12.345-678  | Nome do Bairro  | SP      | Campinas  | Logradouro é obrigatório.  | ",
            "| Rua Sem Nome  |         | 12.345-678  | Nome do Bairro  | SP      | Campinas  | Número é obrigatório.      | ",
            "| Rua Sem Nome  | 45712M  |             | Nome do Bairro  | SP      | Campinas  | CEP é obrigatório.         | ",
            "| Rua Sem Nome  | 45712M  | 12.345-678  |                 | SP      | Campinas  | Bairro é obrigatório.      | ",
            "| Rua Sem Nome  | 45712M  | 12.345-678  | Nome do Bairro  |         | Campinas  | Estado é obrigatório.      | ",
            "| Rua Sem Nome  | 45712M  | 12.345-678  | Nome do Bairro  | SP      |           | Cidade é obrigatório.      | "
    })
    public void validandoOsCamposDeEndereco(String logradouro, String numero, String cep, String bairro, String estado,
                                            String cidade, String mensagemExperada) {
        String nome = "Carlos";
        String email = "carlos@carlos.com.br";
        LocalDate dataNascimento = LocalDate.of(1982, 10, 10);
        String celular = "(19) 99999-7777";
        String cpf = "350.518.412-87";
        TipoPessoaFisica tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE;

        Endereco endereco = new Endereco(logradouro, numero, null, cep, bairro, estado, cidade);
        PessoaFisica pessoaFisica = new PessoaFisica(nome, email, celular, dataNascimento, cpf, endereco, tipoPessoaFisica);

        NotificacaoErro notificacaoErro = null;
        try {
            pessoaFisicaService.salvar(pessoaFisica);
        } catch (NotificacaoException ex) {
            List<NotificacaoErro> notificacoes = ex.getNotificacoes();
            assertThat(notificacoes).hasSize(1);
            notificacaoErro = notificacoes.get(0);
        }

        assertThat(notificacaoErro).isNotNull();
        assertThat(notificacaoErro.getMensagem()).isEqualTo(mensagemExperada);
    }

    @Test
    public void erroAoAtualizarPessoaFisicaSemInformarID() {
        String nome = "Carlos";
        String email = "carlos@carlos.com.br";
        LocalDate dataNascimento = LocalDate.of(1982, 10, 10);
        String celular = "(19) 99999-7777";
        String cpf = "350.518.412-87";
        TipoPessoaFisica tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE;

        Endereco endereco = new Endereco("Rua Sem Nome", "123", null, "12.345-678",
                "Nome do Bairro", "SP", "Campinas");
        PessoaFisica pessoaFisica = new PessoaFisica(nome, email, celular, dataNascimento, cpf, endereco, tipoPessoaFisica);

        NotificacaoErro notificacaoErro = null;
        try {
            pessoaFisicaService.atualizar(pessoaFisica);
        } catch (NotificacaoException ex) {
            List<NotificacaoErro> notificacoes = ex.getNotificacoes();
            assertThat(notificacoes).hasSize(1);
            notificacaoErro = notificacoes.get(0);
        }

        assertThat(notificacaoErro).isNotNull();
        assertThat(notificacaoErro.getMensagem()).isEqualTo("ID é obrigatório para atualizar.");
    }

    @Test
    public void erroAoAtualizarPessoaFisicaIDSemCadastro() {
        Long id = 23432L;
        String nome = "Carlos";
        String email = "carlos@carlos.com.br";
        LocalDate dataNascimento = LocalDate.of(1982, 10, 10);
        String celular = "(19) 99999-7777";
        String cpf = "350.518.412-87";
        TipoPessoaFisica tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE;

        Endereco endereco = new Endereco("Rua Sem Nome", "123", null, "12.345-678",
                "Nome do Bairro", "SP", "Campinas");
        PessoaFisica pessoaFisica = new PessoaFisica(nome, email, celular, dataNascimento, cpf, endereco, tipoPessoaFisica);
        pessoaFisica.setId(id);

        when(pessoaFisicaRepository.get(id)).thenReturn(null);

        NotificacaoErro notificacaoErro = null;
        try {
            pessoaFisicaService.atualizar(pessoaFisica);
        } catch (NotificacaoException ex) {
            List<NotificacaoErro> notificacoes = ex.getNotificacoes();
            assertThat(notificacoes).hasSize(1);
            notificacaoErro = notificacoes.get(0);
        }

        assertThat(notificacaoErro).isNotNull();
        assertThat(notificacaoErro.getMensagem()).isEqualTo(format("Não existe cadastro de Pessoa Fisica com o ID %s.", id));
    }

//    @Spockito.Unroll(
//            "| Nome     | Email               | Data Nascimento  | Celular          | CPF             | RG            | Tipo Pessoa Fisica  | Mensagem Experada                     | ",
//            "| Carlos   | carlos@carlos.com   | 1982-09-15       | (19) 99999-9999  | 123.123.123-12  | 11.111.111.1  | SERVIDOR            | Nome é obrigatório.                   | ",
//            "| Eduardo  | eduardo@carlos.com  | 1981-11-28       | (19) 99999-8888  | 123.321.321-31  | 22.222.222.2  | PROFISSIONAL        | E-mail é obrigatório.                 | "
//    )






    @Test
    public void AtualizarDadosDePessoaFisica() {
        Long id = 23432L;
        String nome = "Carlos";
        String email = "carlos@carlos.com.br";
        LocalDate dataNascimento = LocalDate.of(1982, 10, 10);
        String celular = "(19) 99999-7777";
        String cpf = "350.518.412-87";
        TipoPessoaFisica tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE;

        Endereco endereco = new Endereco("Rua Sem Nome", "123", null, "12.345-678", "Nome do Bairro", "SP", "Campinas");
        PessoaFisica pessoaFisica = new PessoaFisica(nome, email, celular, dataNascimento, cpf, endereco, tipoPessoaFisica);

        when(pessoaFisicaRepository.get(id)).thenReturn(pessoaFisica);

        String novoCelular = "(19) 99999-8888";
        String novoEmail = "carlos@corp.com";
        TipoPessoaFisica novoTipoPessoaFisica = TipoPessoaFisica.PROFISSIONAL;

        Endereco enderecoParaAtualizar = new Endereco("Rua Sem Nome", "123", null, "12.345-678", "Nome do Bairro", "SP", "Campinas");
        PessoaFisica pessoaFisicaExpected = new PessoaFisica(nome, email, celular, dataNascimento, cpf, endereco, tipoPessoaFisica);
        pessoaFisicaExpected.setId(id);

        pessoaFisicaExpected.setCelular(novoCelular);
        pessoaFisicaExpected.setEmail(novoEmail);
        pessoaFisicaExpected.setTipoPessoaFisica(novoTipoPessoaFisica);

        when(pessoaFisicaRepository.atualizar(argThat(new ArgumentMatcher<PessoaFisica>() {
            @Override
            public boolean matches(Object pessoaFisicaParaVerificar) {
                PessoaFisica pessoaFisicaActual = (PessoaFisica) pessoaFisicaParaVerificar;
                assertThat(pessoaFisicaActual.getNome()).isEqualToIgnoringCase(pessoaFisicaExpected.getNome());
                assertThat(pessoaFisicaActual.getEmail()).isEqualToIgnoringCase(pessoaFisicaExpected.getEmail());
                assertThat(pessoaFisicaActual.getCelular()).isEqualToIgnoringCase(pessoaFisicaExpected.getCelular());
                assertThat(pessoaFisicaActual.getTipoPessoaFisica()).isEqualTo(pessoaFisicaExpected.getTipoPessoaFisica());
                return false;
            }
        }))).thenReturn(pessoaFisica);

        pessoaFisicaService.atualizar(pessoaFisicaExpected);
    }
}

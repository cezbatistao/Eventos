package br.com.cdcorp.eventos.service.impl;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.cdcorp.eventos.domain.model.Endereco;
import br.com.cdcorp.eventos.domain.model.Login;
import br.com.cdcorp.eventos.domain.model.PessoaFisica;
import br.com.cdcorp.eventos.domain.repository.PessoaFisicaRepository;
import br.com.cdcorp.eventos.service.LoginService;
import br.com.cdcorp.eventos.service.NotificacaoService;
import br.com.cdcorp.eventos.service.PessoaFisicaService;
import br.com.cdcorp.eventos.service.exception.Notificacao;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;


/**
 * Created by ceb on 02/07/17.
 */
@Service
@Transactional(readOnly = true)
public class PessoaFisicaServiceImpl implements PessoaFisicaService {

    private LoginService loginService;
    private NotificacaoService notificacaoService;
    private PessoaFisicaRepository pessoaFisicaRepository;

    @Autowired
    public PessoaFisicaServiceImpl(LoginService loginService, NotificacaoService notificacaoService, PessoaFisicaRepository pessoaFisicaRepository) {
        this.loginService = loginService;
        this.notificacaoService = notificacaoService;
        this.pessoaFisicaRepository = pessoaFisicaRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PessoaFisica salvar(PessoaFisica pessoaFisica) {
        Notificacao notificacao = new Notificacao();
        validarPessoaFisica(notificacao, pessoaFisica);

        if (notificacao.temErro()) {
            notificacao.throwErros();
        }

        Login login = loginService.criarLoginUsuario(pessoaFisica.getEmail());
        pessoaFisica.setLogin(login);

        return pessoaFisicaRepository.salvar(pessoaFisica);
    }

    public PessoaFisica atualizar(PessoaFisica pessoaFisica)  {
        Notificacao notificacao = new Notificacao();

        if(pessoaFisica.getId() == null) notificacao.addErro("nome", "null", "ID é obrigatório para atualizar.");

        validarPessoaFisica(notificacao, pessoaFisica);

        PessoaFisica pessoaFisicaSalva = null;
        if(pessoaFisica.getId() != null) {
            pessoaFisicaSalva = pessoaFisicaRepository.get(pessoaFisica.getId());
            if(pessoaFisicaSalva == null) notificacao.addErro("pessoa_fisica_nao_cadastrada", format("Não existe cadastro de Pessoa Fisica com o ID %s.", pessoaFisica.getId()));
        }

        if(notificacao.temErro()) {
            notificacao.throwErros();
        }

        pessoaFisicaSalva.setNome(pessoaFisica.getNome());
        pessoaFisicaSalva.setEmail(pessoaFisica.getEmail());
        pessoaFisicaSalva.setCelular(pessoaFisica.getCelular());
        pessoaFisicaSalva.setTelefone(pessoaFisica.getTelefone());
        pessoaFisicaSalva.setEndereco(pessoaFisica.getEndereco());
        pessoaFisicaSalva.setTipoPessoaFisica(pessoaFisica.getTipoPessoaFisica());
        pessoaFisicaSalva.setRg(pessoaFisica.getRg());

        return pessoaFisicaRepository.atualizar(pessoaFisicaSalva);
    }

    public PessoaFisica findByCpf(String cpf) {
        return pessoaFisicaRepository.findByCpf(cpf);
    }

    private void validarPessoaFisica(Notificacao notificacao, PessoaFisica pessoaFisica) {
        if (isBlank(pessoaFisica.getNome())) notificacao.addErro("nome", "nulo_branco", "Nome é obrigatório.");

        EmailValidator emailValidator = EmailValidator.getInstance(false);
        if (isBlank(pessoaFisica.getEmail())) {
            notificacao.addErro("email", "nulo_branco", "E-mail é obrigatório.");
        } else if (!emailValidator.isValid(pessoaFisica.getEmail().trim())) {
            notificacao.addErro("email", "email_invalid", "E-mail inválido.");
        }

        if (isBlank(pessoaFisica.getCelular())) {
            notificacao.addErro("celular", "nulo_branco", "Celular é obrigatório.");
        } else if (!pessoaFisica.getCelular().trim().matches(".((10)|([1-9][1-9]).)\\s9?[6-9][0-9]{3}-[0-9]{4}")) {
            notificacao.addErro("celular", "cell_invalid", "Celular inválido.");
        }

        if (pessoaFisica.getDataNascimento() == null) {
            notificacao.addErro("dataNascimento", "nulo_branco", "Data de nascimento é obrigatório.");
        } else {
            LocalDate now = LocalDate.now();
            long idade = ChronoUnit.YEARS.between(pessoaFisica.getDataNascimento(), now);
            long idadeMinima = 18;
            if (idadeMinima > idade) {
                notificacao.addErro("dataNascimento", "dataNascimento_invalid", format("Idade mínima para participar é de %s anos.", idadeMinima));
            }
        }

        CPFValidator cpfValidator = new CPFValidator(true, true);
        if (isBlank(pessoaFisica.getCpf())) {
            notificacao.addErro("cpf", "nulo_branco", "CPF é obrigatório.");
        } else {
            try {
                cpfValidator.assertValid(pessoaFisica.getCpf().trim());
            } catch (InvalidStateException e) {
                notificacao.addErro("cpf", "cpf_invalid", "CPF inválido.");
            }
        }

        Endereco endereco = pessoaFisica.getEndereco();
        if(isBlank(endereco.getLogradouro())) notificacao.addErro("logradouro", "endereco_pessoa_fisica", "Logradouro é obrigatório.");
        if(isBlank(endereco.getNumero())) notificacao.addErro("numero", "endereco_pessoa_fisica", "Número é obrigatório.");
        if(isBlank(endereco.getCep())) notificacao.addErro("cep", "endereco_pessoa_fisica", "CEP é obrigatório.");
        if(isBlank(endereco.getBairro())) notificacao.addErro("bairro", "endereco_pessoa_fisica", "Bairro é obrigatório.");
        if(isBlank(endereco.getEstado())) notificacao.addErro("estado", "endereco_pessoa_fisica", "Estado é obrigatório.");
        if(isBlank(endereco.getCidade())) notificacao.addErro("cidade", "endereco_pessoa_fisica", "Cidade é obrigatório.");

        PessoaFisica pessoaFisicaJaCadastrada = pessoaFisicaRepository.findByCpf(pessoaFisica.getCpf());
        if(pessoaFisicaJaCadastrada != null && (pessoaFisica.getId() == null || pessoaFisica.getId() != null && !pessoaFisicaJaCadastrada.getId().equals(pessoaFisica.getId()))) {
            notificacao.addErro("pessoa_ja_cadastrada", format("Pessoa física com CPF %s já cadastrado.", pessoaFisica.getCpf()));
        }
    }
}

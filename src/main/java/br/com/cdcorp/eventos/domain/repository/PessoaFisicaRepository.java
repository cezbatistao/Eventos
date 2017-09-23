package br.com.cdcorp.eventos.domain.repository;

import br.com.cdcorp.eventos.domain.model.PessoaFisica;

/**
 * Created by ceb on 02/07/17.
 */
public interface PessoaFisicaRepository {

    PessoaFisica salvar(PessoaFisica pessoaFisica);
    PessoaFisica findByCpf(String cpf);
    PessoaFisica get(Long id);
    PessoaFisica atualizar(PessoaFisica pessoaFisica);

}

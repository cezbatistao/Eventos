package br.com.cdcorp.eventos.service;

import br.com.cdcorp.eventos.domain.model.PessoaFisica;

/**
 * Created by ceb on 02/07/17.
 */
public interface PessoaFisicaService {

    PessoaFisica salvar(PessoaFisica pessoaFisica);
    PessoaFisica atualizar(PessoaFisica pessoaFisica);
    PessoaFisica findByCpf(String cpf);

}

package br.com.cdcorp.eventos.domain.repository;

import br.com.cdcorp.eventos.domain.model.Login;

/**
 * Created by ceb on 10/07/17.
 */
public interface LoginRepository {

    Login findByEmail(String email);
    Login salvar(Login login);
    Login get(Long id);

}

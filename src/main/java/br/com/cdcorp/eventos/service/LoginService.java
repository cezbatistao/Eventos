package br.com.cdcorp.eventos.service;

import br.com.cdcorp.eventos.domain.model.Login;

/**
 * Created by ceb on 06/07/17.
 */
public interface LoginService {

    Login criarLoginUsuario(String email);
    void confirmarCadastro(String email);
    void desativarUsuario(String email);
    void trocarSenha(String email, String senha);

}

package br.com.cdcorp.eventos.domain.model;

import lombok.Data;
import lombok.NonNull;

/**
 * Created by ceb on 02/07/17.
 */
@Data
public abstract class Pessoa {

    private Long id;

    @NonNull
    private String nome;

    @NonNull
    private String email;

    @NonNull
    private String celular;

    private String telefone;

    @NonNull
    private Endereco endereco;

    private Login login;

}

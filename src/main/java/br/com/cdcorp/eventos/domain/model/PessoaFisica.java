package br.com.cdcorp.eventos.domain.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

/**
 * Created by ceb on 02/07/17.
 */
@Data
//@RequiredArgsConstructor
public class PessoaFisica extends Pessoa {

    @NonNull
    private LocalDate dataNascimento;

    @NonNull
    private String cpf;

    private String rg;

    @NonNull
    private TipoPessoaFisica tipoPessoaFisica;

    public PessoaFisica(String nome, String email, String celular, LocalDate dataNascimento, String cpf, Endereco endereco,
                        TipoPessoaFisica tipoPessoaFisica) {
        super(nome, email, celular, endereco);
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.tipoPessoaFisica = tipoPessoaFisica;
        this.setEndereco(endereco);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PessoaFisica that = (PessoaFisica) o;

        return cpf.equals(that.cpf);
    }

    @Override
    public int hashCode() {
        return cpf.hashCode();
    }

    @Override
    public String toString() {
        return "PessoaFisica{" +
                "dataNascimento=" + dataNascimento +
                ", cpf='" + cpf + '\'' +
                ", rg='" + rg + '\'' +
                ", tipoPessoaFisica=" + tipoPessoaFisica +
                "} " + super.toString();
    }
}

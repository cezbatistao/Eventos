package br.com.cdcorp.eventos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Created by ceb on 02/07/17.
 */
@Data
@AllArgsConstructor
public class Endereco {

    @NonNull
    private String logradouro;

    @NonNull
    private String numero;

    private String complemento;

    @NonNull
    private String cep;

    @NonNull
    private String bairro;

    @NonNull
    private String estado;

    @NonNull
    private String cidade;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Endereco endereco = (Endereco) o;

        if (!logradouro.equals(endereco.logradouro)) return false;
        if (!numero.equals(endereco.numero)) return false;
        if (complemento != null ? !complemento.equals(endereco.complemento) : endereco.complemento != null)
            return false;
        if (!cep.equals(endereco.cep)) return false;
        if (!bairro.equals(endereco.bairro)) return false;
        if (!estado.equals(endereco.estado)) return false;
        return cidade.equals(endereco.cidade);
    }

    @Override
    public int hashCode() {
        int result = logradouro.hashCode();
        result = 31 * result + numero.hashCode();
        result = 31 * result + (complemento != null ? complemento.hashCode() : 0);
        result = 31 * result + cep.hashCode();
        result = 31 * result + bairro.hashCode();
        result = 31 * result + estado.hashCode();
        result = 31 * result + cidade.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "logradouro='" + logradouro + '\'' +
                ", numero='" + numero + '\'' +
                ", complemento='" + complemento + '\'' +
                ", cep='" + cep + '\'' +
                ", bairro='" + bairro + '\'' +
                ", estado='" + estado + '\'' +
                ", cidade='" + cidade + '\'' +
                '}';
    }
}

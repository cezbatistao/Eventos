package br.com.cdcorp.eventos.domain.model;

import lombok.Data;
import lombok.NonNull;

/**
 * Created by ceb on 06/07/17.
 */
@Data
public class Login {

    private Long id;

    @NonNull
    private String login;

    @NonNull
    private String senha;

    private Boolean ativo = true;
    private Boolean cadastroConfirmado = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Login login1 = (Login) o;

        if (id != null ? !id.equals(login1.id) : login1.id != null) return false;
        return login.equals(login1.login);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + login.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Login{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", ativo=" + ativo +
                ", cadastroConfirmado=" + cadastroConfirmado +
                '}';
    }
}

package br.com.cdcorp.eventos.service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Created by ceb on 06/07/17.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NotificacaoErro {

    private String campo;

    @NonNull
    private String erro;

    @NonNull
    private String mensagem;

    @Override
    public String toString() {
        return "NotificacaoErro{" +
                "campo='" + campo + '\'' +
                ", erro='" + erro + '\'' +
                ", mensagem='" + mensagem + '\'' +
                '}';
    }
}

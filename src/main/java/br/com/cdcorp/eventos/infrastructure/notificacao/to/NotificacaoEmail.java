package br.com.cdcorp.eventos.infrastructure.notificacao.to;

import lombok.Data;
import lombok.NonNull;

@Data
public class NotificacaoEmail {

    @NonNull
    private String para;

    @NonNull
    private String assunto;

    @NonNull
    private String mensagem;

}

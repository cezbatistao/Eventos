package br.com.cdcorp.eventos.service.exception;

import br.com.cdcorp.eventos.infrastructure.exception.EventosException;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

/**
 * Created by ceb on 06/07/17.
 */
@Getter
public class NotificacaoException extends EventosException {

    @NonNull
    private List<NotificacaoErro> notificacoes;

    public NotificacaoException(List<NotificacaoErro> notificacoes) {
        super("Notificação de erro", null);
        this.notificacoes = notificacoes;
    }
}

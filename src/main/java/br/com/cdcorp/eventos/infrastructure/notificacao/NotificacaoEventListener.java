package br.com.cdcorp.eventos.infrastructure.notificacao;

import br.com.cdcorp.eventos.infrastructure.notificacao.to.NotificacaoEmail;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by ceb on 10/07/17.
 */
@Component
class NotificacaoEventListener {

    @Async
    @EventListener
    public void handle(NotificacaoEmail notificacaoEmail) {
        System.out.println("[${Thread.currentThread().getName()}] Notificação por email: ${notificacaoEmail}");
    }
}

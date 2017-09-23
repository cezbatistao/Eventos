package br.com.cdcorp.eventos.infrastructure.notificacao;

import br.com.cdcorp.eventos.infrastructure.notificacao.to.NotificacaoEmail;
import br.com.cdcorp.eventos.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Created by ceb on 10/07/17.
 */
@Component
public class NotificacaoEventProducer implements NotificacaoService {

    private ApplicationEventPublisher publisher;

    @Autowired
    public NotificacaoEventProducer(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void enviarEmail(String para, String assunto, String mensagem) {
        publisher.publishEvent(new NotificacaoEmail(para, assunto, mensagem));
    }
}

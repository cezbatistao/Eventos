package br.com.cdcorp.eventos.service;

/**
 * Created by ceb on 08/07/17.
 */
public interface NotificacaoService {

    void enviarEmail(String para, String assunto, String mensagem);

}

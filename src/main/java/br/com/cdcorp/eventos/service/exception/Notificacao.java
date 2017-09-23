package br.com.cdcorp.eventos.service.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ceb on 06/07/17.
 */
public class Notificacao {

    List<NotificacaoErro> notificacoes = new ArrayList<>();

    public void addErro(String erro, String mensagem) {
        notificacoes.add(new NotificacaoErro(erro, mensagem));
    }

    public void addErro(String campo, String erro, String mensagem) {
        notificacoes.add(new NotificacaoErro(campo, erro, mensagem));
    }

    public Boolean temErro() {
        return !notificacoes.isEmpty();
    }

    public void throwErros() {
        if(temErro()) throw new NotificacaoException(notificacoes);
    }
}

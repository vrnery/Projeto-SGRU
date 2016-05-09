import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Web application lifecycle listener.
 *
 * @author 10070133
 */
public class SessionListener implements HttpSessionListener {
    private final TransacaoCaixaRU transacao = new TransacaoCaixaRU();
    
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        transacao.iniciar();
        se.getSession().setAttribute("transacaoCorrente", transacao);
        
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {        
        transacao.finalizar();
    }    
}

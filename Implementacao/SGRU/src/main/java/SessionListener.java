
import br.edu.ifrs.restinga.sgru.persistencia.HibernateUtil;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.hibernate.Session;
import org.hibernate.context.internal.ManagedSessionContext;

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
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        Session sessao = HibernateUtil.getSessionFactory().openSession ();
        se.getSession().setAttribute("sessaoHibernate", sessao);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        
    }
}


import br.edu.ifrs.restinga.sgru.persistencia.HibernateUtil;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.hibernate.context.internal.ManagedSessionContext;

/**
 *
 * @author 10070133
 */
public class FiltroTransacoesHibernate implements Filter {    
    public final static String  HIBERNATE_SESSION_KEY    = "sessaoHibernate";
    //public final static String  END_OF_CONVERSATION_FLAG = "endOfConversation";    
    //public final static String  ROLLBACK_OF_CONVERSATION_FLAG = "rollbackOfConversation";
        
    private SessionFactory sf;
    
    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {                
        
        //Session currentSession;

        // Try to get a Hibernate Session from the HttpSession
        HttpSession httpSession = ((HttpServletRequest) request).getSession();
        //Session disconnectedSession = (Session) httpSession.getAttribute(HIBERNATE_SESSION_KEY);
        Session currentSession = (Session) httpSession.getAttribute(HIBERNATE_SESSION_KEY);

        try {
            // Start a new conversation or in the middle?
            /*
            if (disconnectedSession == null) {            
                currentSession = sf.openSession();
                currentSession.setFlushMode(FlushMode.MANUAL);                
            } else {            
                currentSession = disconnectedSession;
            }
            */
          
            ManagedSessionContext.bind(currentSession);                                  
            chain.doFilter(request, response);         
            currentSession = ManagedSessionContext.unbind(sf);
            
            // Se nao existe transacao ativa, entao inicia uma nova transacao
            if (!currentSession.getTransaction().isActive()) {
                currentSession.beginTransaction();
            }

            currentSession.flush();
            
            // Verifica se a transacao jah foi comitada
            if (!currentSession.getTransaction().wasCommitted()) {
                currentSession.getTransaction().commit();           
            }            
            //currentSession.close();
            /*
            // End or continue the long-running conversation?
            if (request.getAttribute(END_OF_CONVERSATION_FLAG) != null ||
                request.getParameter(END_OF_CONVERSATION_FLAG) != null) {                        
                currentSession.beginTransaction();                        
                currentSession.flush();          
                // commit only if tx still hasn't been committed yet (by hibernate)
                if (!currentSession.getTransaction().wasCommitted()) {
                    currentSession.getTransaction().commit();           
                }
                currentSession.close();            
                httpSession.setAttribute(HIBERNATE_SESSION_KEY, null);                        
            } else if (request.getAttribute(ROLLBACK_OF_CONVERSATION_FLAG) != null ||
	               request.getParameter(ROLLBACK_OF_CONVERSATION_FLAG) != null) {                    

                currentSession.getTransaction().rollback();                    
                currentSession.close();

                try {
                    httpSession.setAttribute(HIBERNATE_SESSION_KEY, null);
                }
                catch (IllegalStateException e) {
                }
                    	    
            } else {            
                httpSession.setAttribute(HIBERNATE_SESSION_KEY, currentSession);            
            }
            */
        } catch (StaleObjectStateException staleEx) {                  
            // Rollback, close everything, possibly compensate for any permanent changes
            // during the conversation, and finally restart business conversation. Maybe
            // give the user of the application a chance to merge some of his work with
            // fresh data... what you do here depends on your applications design.
            throw staleEx;
        } catch (Throwable ex) {
            // Rollback only
            try {
                if (sf.getCurrentSession().getTransaction().isActive()) {              
                    sf.getCurrentSession().getTransaction().rollback();
                }
            } catch (Throwable rbEx) {            
            } finally {            
                // Cleanup            
                currentSession = ManagedSessionContext.unbind(sf);            
                currentSession.close();            
                httpSession.setAttribute(HIBERNATE_SESSION_KEY, null);
            }

            // Let others handle it... maybe another interceptor for exceptions?
            throw new ServletException(ex);
        } 
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {        
        sf = HibernateUtil.getSessionFactory();
    }

    @Override
    public void destroy() {        
    }
}

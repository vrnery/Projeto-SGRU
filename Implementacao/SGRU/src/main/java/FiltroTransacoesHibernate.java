/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
import org.hibernate.context.internal.ManagedSessionContext;

/**
 *
 * @author 10070133
 */
public class FiltroTransacoesHibernate implements Filter {
    
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
        
        HttpSession sessaoHttp =((HttpServletRequest)request).getSession(true);
        ManagedSessionContext.bind((Session)sessaoHttp.getAttribute("sessaoHibernate"));
        
        Session sessao = HibernateUtil.getSessionFactory().getCurrentSession();
        /*
        if(sessao.getTransaction() == null)
            sessao.beginTransaction();
        chain.doFilter(request, response);
        if(sessao.getTransaction() != null)
            sessao.getTransaction().commit();
        */
        try {            
            sessao.beginTransaction();
            chain.doFilter(request, response);
            sessao.getTransaction().commit();
            ManagedSessionContext.unbind (HibernateUtil.getSessionFactory());
            //sessao.close();
        } catch (Throwable ex){
            try{
                if(sessao.getTransaction().isActive()){
                   sessao.getTransaction().rollback();
                }
            } catch (Throwable t){
                t.printStackTrace();
            }
            throw new ServletException(ex);
        }        
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {        
    }

    @Override
    public void destroy() {        
    }
}

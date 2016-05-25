package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.persistencia.HibernateUtil;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.context.internal.ManagedSessionContext;

/**
 * Encasula uma transação (envolvendo várias operações) de interface.
 * 
 * @author Leonardo
 */
public class TransacaoCaixaRU {
    private Session sessaoHibernate;

    public void iniciar() {
        sessaoHibernate = HibernateUtil.getSessionFactory().openSession();
        sessaoHibernate.setFlushMode(FlushMode.MANUAL);
    }
    
    public void finalizar() {
        if (sessaoHibernate.isOpen()) {
            sessaoHibernate.flush();
            sessaoHibernate.close();            
        }
    }
    
    public void iniciarRequisicao() {
        ManagedSessionContext.bind(sessaoHibernate);
        if(!sessaoHibernate.getTransaction().isActive()) {
            sessaoHibernate.beginTransaction();        
        }
    }
    
    public void finalizarRequisicao() {        
        if(sessaoHibernate.isOpen() && sessaoHibernate.getTransaction().isActive()) {
            sessaoHibernate.flush();
            sessaoHibernate.getTransaction().commit();
        }
        ManagedSessionContext.unbind(HibernateUtil.getSessionFactory());
    }    
}

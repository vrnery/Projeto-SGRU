/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.CaixaRU;
import br.edu.ifrs.restinga.sgru.modelo.VendaAlmoco;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author 10070133
 */
public class VendaAlmocoDAO {
    private final Session sessao;

    public VendaAlmocoDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();
        sessao.beginTransaction();
    }

    /**
     * Persiste a VendaAlmoco enviado na base de dados
     * @param vendaAlmoco A vendaAlmoco a ser salvo na base de dados
     */
    public void salvar(VendaAlmoco vendaAlmoco) {
        sessao.saveOrUpdate(vendaAlmoco);        
    }  
    
    public double carregarTotalVendidoDia(CaixaRU caixaRU) {
        return (double) sessao.createCriteria(VendaAlmoco.class)
                .createAlias("mate", "valorAlmoco", Criteria.INNER_JOIN)                
                .setProjection(Projections.sum("valorAlmoco"))
                .add(Restrictions.eq("id", caixaRU.getId()))
                .add(Restrictions.eq("dataAbertura", caixaRU.getDataAbertura())).uniqueResult();
    }
    
    /**
     * Encerra uma transação com o banco de dados. 
     * Esse método é chamado automaticamente.
     */        
    public void encerrar() {
        sessao.getTransaction().commit();
    }            
    
}

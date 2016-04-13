/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.VendaAlmoco;
import org.hibernate.Session;

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
     * Persiste o ValorAlmoco enviado na base de dados
     * @param vendaAlmoco A vendaAlmoco a ser salvo na base de dados
     */
    public void salvar(VendaAlmoco vendaAlmoco) {
        sessao.saveOrUpdate(vendaAlmoco);        
    }  
    
    /**
     * Encerra uma transação com o banco de dados. 
     * Esse método é chamado automaticamente.
     */        
    public void encerrar() {
        sessao.getTransaction().commit();
    }            
    
}

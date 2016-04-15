/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.ValorAlmoco;
import org.hibernate.Session;

/**
 *
 * @author 10070133
 */
public class ValorAlmocoDAO {
    private final Session sessao;

    public ValorAlmocoDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();
        sessao.beginTransaction();
    }

    /**
     * Persiste o ValorAlmoco enviado na base de dados
     * @param valorAlmoco O valor do almoco a ser salvo na base de dados
     */
    public void salvar(ValorAlmoco valorAlmoco) {
        sessao.saveOrUpdate(valorAlmoco);        
    }  
    
    public ValorAlmoco carregar() {        
        return (ValorAlmoco) sessao.createQuery("FROM ValorAlmoco ORDER BY dataValor DESC").setMaxResults(1).uniqueResult();
    }
    
    /**
     * Encerra uma transação com o banco de dados. 
     * Esse método é chamado automaticamente.
     */        
    public void encerrar() {
        sessao.getTransaction().commit();
    }
}

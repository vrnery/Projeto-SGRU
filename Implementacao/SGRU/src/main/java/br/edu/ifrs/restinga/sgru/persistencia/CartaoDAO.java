/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.Cartao;
import org.hibernate.Session;

/**
 *
 * @author marcelo.lima
 */
public class CartaoDAO {
    private final Session sessao;

    public CartaoDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();
        sessao.beginTransaction();
    }        
    
    /**
     * Persiste o cartão enviado na base de dados
     * @param cartao O cartão a ser persistido
     */
    public void salvar(Cartao cartao) {
        sessao.saveOrUpdate(cartao);        
    }  
    
    /**
     * Pesquisa um cartão com o id informado
     * @param id O id do cartão a ser pesquisado
     * @return Um objeto Cartao
     */
    public Cartao carregar(int id) {
        return (Cartao) sessao.load(Cartao.class, id);
    }    
    
    /**
     * Encerra uma transação com o banco de dados. 
     * Esse método é chamado automaticamente.
     */        
    public void encerrar() {
        sessao.getTransaction().commit();
    }            
}

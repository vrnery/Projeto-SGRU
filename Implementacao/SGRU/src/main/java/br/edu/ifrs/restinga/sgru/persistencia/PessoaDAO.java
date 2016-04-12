/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.Pessoa;
import org.hibernate.Session;

/**
 *
 * @author 10070133
 */
public class PessoaDAO {
    private final Session sessao;

    public PessoaDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();
        sessao.beginTransaction();
    }
    
    /**
     * Pesquisa uma pessoa baseado no id informado
     * @param id Id da pessoa
     * @return um objeto Pessoa
     */
    public Pessoa carregar(int id) {
        return (Pessoa) sessao.load(Pessoa.class, id);
    }    
    
    /**
     * Persiste um objeto Pessoa no banco de dados
     * @param pessoa A pessoa a ser cadastrada no sistema
     */
    public void salvar(Pessoa pessoa) {
        sessao.saveOrUpdate(pessoa);        
    }      
    
    /**
     * Encerra uma transação com o banco de dados. 
     * Esse método é chamado automaticamente.
     */    
    public void encerrar() {
        sessao.getTransaction().commit();
    }
}

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
    }
    
    /**
     * Pesquisa uma pessoa baseado no id informado
     * @param matricula A matricula da pessoa a ser pesquisada
     * @return um objeto Pessoa
     */
    public Pessoa carregar(String matricula) {
        return (Pessoa) sessao.createQuery("FROM Pessoa WHERE matricula=:matricula").setString("matricula", matricula).uniqueResult();
    }    
    
    /**
     * Persiste um objeto Pessoa no banco de dados
     * @param pessoa A pessoa a ser cadastrada no sistema
     */
    public void salvar(Pessoa pessoa) {
        sessao.saveOrUpdate(pessoa);        
    }      
}

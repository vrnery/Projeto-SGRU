/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.Professor;
import org.hibernate.Session;

/**
 *
 * @author 10070133
 */
public class ProfessorDAO {
    private final Session sessao;

    public ProfessorDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();
        sessao.beginTransaction();
    }
    
    /**
     * Pesquisa um professor baseado no número de matrícula enviada
     * @param matricula Número de matrícula do professor
     * @return um objeto Professor
     */
    public Professor carregar(String matricula) {        
        return (Professor) sessao.createQuery("FROM Professor WHERE matricula=:matricula").setString("matricula", matricula).uniqueResult();
    }    
    
    /**
     * Persiste um objeto Professor no banco de dados
     * @param professor O professor a ser cadastrado no sistema
     */
    public void salvar(Professor professor) {
        sessao.saveOrUpdate(professor);        
    }      
    
    /**
     * Encerra uma transação com o banco de dados. 
     * Esse método é chamado automaticamente.
     */    
    public void encerrar() {
        sessao.getTransaction().commit();
    }
}

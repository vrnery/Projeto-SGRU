/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.Aluno;
import org.hibernate.Session;

/**
 *
 * @author marcelo.lima
 */
public class AlunoDAO {
    private final Session sessao;

    public AlunoDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();
        sessao.beginTransaction();
    }
    
    /**
     * Pesquisa um aluno baseado no número de matrícula enviada
     * @param matricula Número de matrícula do aluno
     * @return um objeto Aluno
     */
    public Aluno carregar(String matricula) {        
        return (Aluno) sessao.createQuery("FROM Aluno WHERE matricula=:matricula").setString("matricula", matricula).uniqueResult();
    }    
    
    /**
     * Persiste um objeto Aluno no banco de dados
     * @param aluno O aluno a ser cadastrado no sistema
     */
    public void salvar(Aluno aluno) {
        sessao.saveOrUpdate(aluno);        
    }      
    
    /**
     * Encerra uma transação com o banco de dados. 
     * Esse método é chamado automaticamente.
     */    
    public void encerrar() {
        sessao.getTransaction().commit();
    }        
}

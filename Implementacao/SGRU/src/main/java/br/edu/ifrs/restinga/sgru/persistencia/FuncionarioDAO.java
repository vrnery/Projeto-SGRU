/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.Funcionario;
import org.hibernate.Session;

/**
 *
 * @author marcelo.lima
 */
public class FuncionarioDAO {
    private final Session sessao;

    public FuncionarioDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();        
    }    
        
    /**
     * Persiste o funcionario enviado na base de dados
     * @param funcionario O operador de caixa a ser persistido
     */
    public void salvar(Funcionario funcionario) {
        sessao.saveOrUpdate(funcionario);        
    }      
}

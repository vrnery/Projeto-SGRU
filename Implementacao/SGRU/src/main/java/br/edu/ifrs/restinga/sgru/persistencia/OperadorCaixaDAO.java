/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.OperadorCaixa;
import org.hibernate.Session;

/**
 *
 * @author marcelo.lima
 */
public class OperadorCaixaDAO {
    private final Session sessao;

    public OperadorCaixaDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();        
    }    
        
    /**
     * Persiste o operador de caixa enviado na base de dados
     * @param operadorCaixa O operador de caixa a ser persistido
     */
    public void salvar(OperadorCaixa operadorCaixa) {
        sessao.saveOrUpdate(operadorCaixa);        
    }      
}

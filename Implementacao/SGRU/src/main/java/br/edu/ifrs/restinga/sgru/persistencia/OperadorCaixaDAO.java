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
     * Pesquisa um operador de caixa com o login informado
     * @param matricula A matricula do operador de caixa
     * @return Um objeto OperadorCaixa
     */
    public OperadorCaixa carregar(String matricula) {
        return (OperadorCaixa) sessao.createQuery("FROM OperadorCaixa WHERE matricula=:matricula").setString("matricula", matricula).uniqueResult();
    }

    /**
     * Autentica o operador de caixa com o login e senha informado
     * @param login O login do operador de caixa
     * @param senha A senha do operador de caixa
     * @return Um objeto OperadorCaixa
     */
    public OperadorCaixa autenticar(String login, String senha) {
        return (OperadorCaixa) sessao.createQuery("FROM OperadorCaixa WHERE login=:login AND senha=:senha").setString("login", login).setString("senha", senha).uniqueResult();
    }

    /**
     * Persiste o operador de caixa enviado na base de dados
     * @param operadorCaixa O operador de caixa a ser persistido
     */
    public void salvar(OperadorCaixa operadorCaixa) {
        sessao.saveOrUpdate(operadorCaixa);        
    }      
}

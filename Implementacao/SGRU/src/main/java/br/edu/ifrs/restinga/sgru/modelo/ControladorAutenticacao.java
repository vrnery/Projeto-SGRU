/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.excessao.LoginInvalidoException;

/**
 *
 * @author marcelo.lima
 */
public class ControladorAutenticacao {
    private Pessoa pessoa;
    private OperadorCaixa operadorCaixa;
    
    /**
     * @return the pessoa
     */
    public Pessoa getPessoa() {
        return pessoa;
    }

    /**
     * @param pessoa the pessoa to set
     */
    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }    
    
    /**
     * @return the operadorCaixa
     */
    public OperadorCaixa getOperadorCaixa() {
        return operadorCaixa;
    }

    /**
     * @param operadorCaixa the operadorCaixa to set
     */
    public void setOperadorCaixa(OperadorCaixa operadorCaixa) {
        this.operadorCaixa = operadorCaixa;
    }    
    
    /**
     * Realiza login de um usuário no sistema     
     * @param login O login do usuário
     * @param senha A senha do usuário
     * @return Um objeto Pessoa, com o objeto correspondente ao login e senha informados
     * @throws br.edu.ifrs.restinga.sgru.excessao.LoginInvalidoException Caso os dados informados sejam inválidos
     */
    public Pessoa realizarLogin(String login, String senha) throws LoginInvalidoException {
        this.pessoa = Pessoa.validarLoginUsuario(login, senha);
        
        if (pessoa instanceof OperadorCaixa) {
            this.operadorCaixa = (OperadorCaixa) pessoa;
        }        
        return pessoa;
    }        
}

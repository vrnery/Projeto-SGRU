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
    private Funcionario funcionario;

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
     * @return the funcionario
     */
    public Funcionario getFuncionario() {
        return funcionario;
    }

    /**
     * @param funcionario the funcionario to set
     */
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
    
    
    /**
     * Autentica um usuário no sistema
     * @param login O login do usuário
     * @param senha A senha do usuário
     * @return A página a ser visualizada pelo usuário após o login          
     * @throws br.edu.ifrs.restinga.sgru.excessao.LoginInvalidoException Se o login for inválido         
     */
    public String autenticar(String login, String senha) throws LoginInvalidoException {        
        String retorno = null;        
        //this.pessoa = this.realizarLogin();
        this.pessoa = Pessoa.validarLoginUsuario(login, senha);

        if (pessoa instanceof Funcionario) {
            this.funcionario = (Funcionario)pessoa;
            switch (funcionario.getTipoFuncionario().getId()) {
                case Funcionario.OPERADOR_CAIXA:
                    retorno = "abrirCaixa";
                    break;
                case Funcionario.GERENTE:
                    retorno = "paginaGerencial";
                    break;
                default:
                    // Aqui precisa-se verificar o comportamento quando implementados os demais usuarios
                    retorno = "index";
                    break;
            }
        } else if (pessoa instanceof Cliente) {
            retorno = "paginaGerencial";
        }
        
        return retorno;
    }        
}

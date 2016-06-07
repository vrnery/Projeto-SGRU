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
     * Verifica se existe algum usuário logado no sistema     
     * @return True, caso exista algum usuário logado no sistema e false, caso contrário
     */
    public boolean isUsuarioLogado() {
        return this.pessoa != null;
    }
    
    /**
     * Verifica se o usuário logado no sistema é um gerente
     * @return True, se o usuário for gerente e false, caso contrário
     */
        public boolean isUsuarioLogadoGerente() {
        boolean retorno = false;
        if (this.pessoa instanceof Funcionario) {
            if (((Funcionario)this.pessoa).getTipoFuncionario().getCodigo().equals(Funcionario.GERENTE)) {
                retorno = true;
            }
        }
        return retorno;
    }    
    
    /**
     * Verifica se o usuário logado no sistema é um operador de caixa
     * @return True, se o usuário for um operador de caixa e false, caso contrário
     */
    public boolean isUsuarioOperadorCaixa() {
        boolean retorno = false;
        if (this.pessoa instanceof Funcionario) {
            if (((Funcionario)this.pessoa).getTipoFuncionario().getCodigo().equals(Funcionario.OPERADOR_CAIXA)) {
                retorno = true;
            }
        }
        return retorno;
    }        
    
    /**
     * Verifica se o usuário pode visualizar as rotinas administrativas, tais como emissão de relatório e edição de contra
     * @return True, caso o usuário tenha permissões para visualizar as rotinas administrativa, e false, caso contrário
     */
    public boolean vizualizarRotinasAdm() {        
        if (this.pessoa instanceof Cliente) {
            return true;
        }
        
        if (this.pessoa instanceof Funcionario) {
            if (((Funcionario)this.pessoa).getTipoFuncionario().getCodigo().equals(Funcionario.GERENTE)) {
                return true;
            }
        }
        return false;
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
            switch (funcionario.getTipoFuncionario().getCodigo()) {
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

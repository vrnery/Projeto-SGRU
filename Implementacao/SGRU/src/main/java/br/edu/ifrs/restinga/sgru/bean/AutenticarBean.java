/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.excessao.LoginInvalidoException;
import br.edu.ifrs.restinga.sgru.modelo.Cliente;
import br.edu.ifrs.restinga.sgru.modelo.ControladorAutenticacao;
import br.edu.ifrs.restinga.sgru.modelo.Funcionario;
import br.edu.ifrs.restinga.sgru.modelo.Pessoa;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author marcelo.lima
 */
@ManagedBean
@SessionScoped
public class AutenticarBean {            
    private final ControladorAutenticacao controlador;

    public AutenticarBean() {
        this.controlador = new ControladorAutenticacao();              
    }    
    
    /**
     * @return the controlador
     */
    public ControladorAutenticacao getControlador() {
        return controlador;
    }

    /**
     * Autentica um usuário no sistema
     * @param login O login do usuário
     * @param senha A senha do usuário
     * @return A página a ser visualizada pelo usuário após o login     
     */
    public String autenticar(String login, String senha) {        
        try {
            return controlador.autenticar(login, senha);
        } catch(LoginInvalidoException e) {
            enviarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
            return "index";            
        }
    }    
    
    /**
     * Realiza o logout do usuário
     * @return A próxima página a ser visualizada pelo usuário após o logout
     */
    public String realizarLogout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index.xhtml?faces-redirect=true";
    }

    /**
     * Envia à viewer uma mensagem com o status da operação
     * @param sev A severidade da mensagem
     * @param msg A mensagem a ser apresentada
     */
    private void enviarMensagem(FacesMessage.Severity sev, String msg) {
        FacesContext context = FacesContext.getCurrentInstance();        
        context.addMessage(null, new FacesMessage(sev, msg, ""));
    }                
}

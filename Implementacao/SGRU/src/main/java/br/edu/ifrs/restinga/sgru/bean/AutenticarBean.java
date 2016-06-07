/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.excessao.LoginInvalidoException;
import br.edu.ifrs.restinga.sgru.modelo.ControladorAutenticacao;
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
     * Verifica se o padrão a ser utilizado é com ou sem menu
     * @return Padrão com menu, se existir um usuário logado e padrão sem menu, caso contrário
     */
    public String setarPadraoInterno() {
        if (this.controlador.isUsuarioLogado()) {
            return "padraoInternoComMenu.xhtml";
        } else {
            return "padraoInternoSemMenu.xhtml";
        }
    }

    /**
     * Verifica se o usuário logado no sistema é um gerente
     * @return True, se o usuário for gerente e false, caso contrário
     */
    public boolean isUsuarioLogadoGerente() {
        return this.controlador.isUsuarioLogadoGerente();
    }
    
    /**
     * Verifica se o usuário logado no sistema é um operador de caixa
     * @return True, caso o usuário seja um operador de caixa e false, caso contrário
     */
    public boolean isUsuarioLogadoOperadorCaixa() {
        return this.controlador.isUsuarioOperadorCaixa();
    }
    
    /**
     * Verifica se o usuário pode visualizar as rotinas administrativas, tais como emissão de relatório e edição de contra
     * @return True, caso o usuário tenha permissões para visualizar as rotinas administrativa, e false, caso contrário
     */
    public boolean vizualizarRotinasAdm() {
        return this.controlador.vizualizarRotinasAdm();
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

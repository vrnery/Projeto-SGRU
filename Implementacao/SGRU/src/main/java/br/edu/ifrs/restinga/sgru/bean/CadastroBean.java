/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.modelo.ControladorCadastro;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author marcelo.lima
 */
@ManagedBean
@ViewScoped
public class CadastroBean {    
    private final ControladorCadastro controladorCadastro;

    public CadastroBean() {
        this.controladorCadastro = new ControladorCadastro();
    }        
    
    /**
     * retorno do controladorCadastro
     * @return controladorCadastro
     */
    public ControladorCadastro getControladorCadastro() {
        return controladorCadastro;
    }
    
    public void salvar(){
        controladorCadastro.salvar();
    }
    
    private void enviarMensagem(FacesMessage.Severity sev, String msg) {
        FacesContext context = FacesContext.getCurrentInstance();        
        context.addMessage(null, new FacesMessage(sev, msg, ""));
    }
}

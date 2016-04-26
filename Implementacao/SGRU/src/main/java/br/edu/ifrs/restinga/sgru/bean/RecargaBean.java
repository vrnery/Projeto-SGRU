/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.modelo.Recarga;
import br.edu.ifrs.restinga.sgru.persistencia.RecargaDAO;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author marcelo.lima
 */
@ManagedBean
@RequestScoped
public class RecargaBean {
    private Recarga recarga = new Recarga();
    private final RecargaDAO dao = new RecargaDAO();

    /**
     * @return the recarga
     */
    public Recarga getRecarga() {
        return recarga;
    }

    /**
     * @param recarga the recarga to set
     */
    public void setRecarga(Recarga recarga) {
        this.recarga = recarga;
    }
    
    /**
     * Solicita à camada de persistência o cadastro de uma Recarga
     */
    public void salvar() {
        dao.salvar(recarga);
        //enviarMensagem(FacesMessage.SEVERITY_INFO, "Caixa cadastrado com sucesso!");
    }
    
    /**
     * Solicita a pesquisa de uma Recarga com o id informado à camada de persistência
     * @param id O id da Recarga a ser pesquisado
     */
    public void carregar(int id) {
        recarga = dao.carregar(id);
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.modelo.VendaAlmoco;
import br.edu.ifrs.restinga.sgru.persistencia.VendaAlmocoDAO;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author 10070133
 */
@ManagedBean
@RequestScoped
public class VendaAlmocoBean {
    private VendaAlmoco vendaAlmoco = new VendaAlmoco();
    private final VendaAlmocoDAO dao = new VendaAlmocoDAO();

    /**
     * @return the vendaAlmoco
     */
    public VendaAlmoco getVendaAlmoco() {
        return vendaAlmoco;
    }

    /**
     * @param vendaAlmoco the vendaAlmoco to set
     */
    public void setVendaAlmoco(VendaAlmoco vendaAlmoco) {
        this.vendaAlmoco = vendaAlmoco;
    }
    
    /**
     * Salva uma venda de almoco no banco de dados
     * @param vendaAlmoco A venda a ser persistida no banco de dado
     */
    public void salvar(VendaAlmoco vendaAlmoco) {
        dao.salvar(vendaAlmoco);
        enviarMensagem(FacesMessage.SEVERITY_INFO, "Venda realizada com sucesso!");
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

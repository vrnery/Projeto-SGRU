/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.modelo.Aluno;
import br.edu.ifrs.restinga.sgru.modelo.CaixaRU;
import br.edu.ifrs.restinga.sgru.modelo.ValorAlmoco;
import br.edu.ifrs.restinga.sgru.modelo.VendaAlmoco;
import br.edu.ifrs.restinga.sgru.persistencia.VendaAlmocoDAO;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author 10070133
 */
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
     * Solicita à camada de persistência o cadastro de uma VendaAlmoco
     * @param aluno O aluno que está realizando a compra do almoco
     * @param valorAtualAlmoco Um objeto ValorAlmoco com o valor do almoco da tabela atual
     * @param caixa O caixa onde está sendo realizada a venda
     */    
    public void realizarVendaAlmoco(Aluno aluno, ValorAlmoco valorAtualAlmoco,
            CaixaRU caixa) {

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
    
    @PreDestroy
    public void encerrar() {
        dao.encerrar();
    }        
}

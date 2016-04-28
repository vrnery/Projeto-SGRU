/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.modelo.Cartao;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import br.edu.ifrs.restinga.sgru.persistencia.CartaoDAO;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author marcelo.lima
 */
@ManagedBean
@RequestScoped
public class CartaoBean {
    private Cartao cartao = new Cartao();
    private final CartaoDAO dao = new CartaoDAO();

    /**
     * @return the cartao
     */
    public Cartao getCartao() {
        return cartao;
    }

    /**
     * @param cartao the cartao to set
     */
    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }
    
    /**
     * Solicita à camada de persistência o cadastro de um cartão
     */
    public void salvar() {
        dao.salvar(cartao);
        enviarMensagem(FacesMessage.SEVERITY_INFO, "Cartão cadastrado com sucesso!");
    }
    
    /**
     * Solicita a pesquisa de um cartão para a camada de persistência
     * @param id O id do cartão a ser pesquisado
     */
    public void carregar(int id) {
        cartao = dao.carregar(id);
        
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

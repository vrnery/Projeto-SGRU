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
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import br.edu.ifrs.restinga.sgru.persistencia.CaixaRUDAO;
import br.edu.ifrs.restinga.sgru.persistencia.ValorAlmocoDAO;

/**
 *
 * @author marcelo.lima
 */
public class CaixaRUBean {
    private CaixaRU caixaRU = new CaixaRU();
    private  CaixaRUDAO dao;
    private final ValorAlmoco valorAtualAlmoco;    

    public CaixaRUBean() {
        // Valor atual do almoco        
        ValorAlmocoDAO daoValor = new ValorAlmocoDAO();
        valorAtualAlmoco = daoValor.carregar();
        daoValor.encerrar();                                        
        
        // Evitar nested transaction exception
        dao = new CaixaRUDAO();
    }
    
    /**
     * @return the caixaRU
     */
    public CaixaRU getCaixaRU() {
        return caixaRU;
    }

    /**
     * @param caixaRU the caixaRU to set
     */
    public void setCaixaRU(CaixaRU caixaRU) {
        this.caixaRU = caixaRU;
    }
    
    /**
     * Solicita à camada de persistência o cadastro de um CaixaRU
     */
    public void salvar() {
        dao.salvar(caixaRU);
        enviarMensagem(FacesMessage.SEVERITY_INFO, "Caixa cadastrado com sucesso!");
    }
    
    /**
     * Solicita a pesquisa de um CaixaRU com o id informado à camada de persistência
     * @param id O id do CaixaRU a ser pesquisado
     */
    public void carregar(int id) {
        caixaRU = dao.carregar(id);
    }
    
    /**
     *
     * @param aluno O aluno que está realizando a compra
     */
    public void realizarVendaAlmoco(Aluno aluno) {         
        this.getCaixaRU().setVendaAlmoco(new VendaAlmoco());
        this.getCaixaRU().getVendaAlmoco().get(0).setCartao(aluno.getCartao());                
        this.getCaixaRU().getVendaAlmoco().get(0).setValorAlmoco(valorAtualAlmoco);        
        System.out.println("Valor atual do almoco: " + valorAtualAlmoco.getValorAlmoco());
        System.out.println("Valor do almoco pago pelo aluno: " + this.getCaixaRU().getVendaAlmoco().get(0).getValorAlmoco());
        this.getCaixaRU().getVendaAlmoco().get(0).setCaixaRU(this.getCaixaRU());
        this.getCaixaRU().getVendaAlmoco().get(0).setFormaPagamento("Cartao");                
        
        dao.salvarVendaAlmoco(this.getCaixaRU().getVendaAlmoco().get(0));
        enviarMensagem(FacesMessage.SEVERITY_INFO, "Venda do Almoco cadastrada com sucesso!");        
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

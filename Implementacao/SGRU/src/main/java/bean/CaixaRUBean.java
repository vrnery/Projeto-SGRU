/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import br.edu.ifrs.restinga.sgru.modelo.CaixaRU;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import persistencia.CaixaRUDAO;

/**
 *
 * @author marcelo.lima
 */
public class CaixaRUBean {
    private CaixaRU caixaRU;
    private CaixaRUDAO dao = new CaixaRUDAO();

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

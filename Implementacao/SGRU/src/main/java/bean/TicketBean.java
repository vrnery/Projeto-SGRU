/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import br.edu.ifrs.restinga.sgru.modelo.Ticket;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import persistencia.TicketDAO;

/**
 *
 * @author marcelo.lima
 */
public class TicketBean {
    private Ticket ticket = new Ticket();
    TicketDAO dao = new TicketDAO();

    /**
     * @return the ticket
     */
    public Ticket getTicket() {
        return ticket;
    }

    /**
     * @param ticket the ticket to set
     */
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
    
    /**
     * Solicita à camada de persistência o cadastro de um ticket
     */
    public void salvar() {
        dao.salvar(ticket);
        enviarMensagem(FacesMessage.SEVERITY_INFO, "Ticket cadastrado com sucesso!");
    }
    
    /**
     * Solicita a pesquisa de um ticket para a camada de persistência
     * @param id O id do ticket a ser pesquisada
     */
    public void carregar(int id) {
        ticket = dao.carregar(id);        
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

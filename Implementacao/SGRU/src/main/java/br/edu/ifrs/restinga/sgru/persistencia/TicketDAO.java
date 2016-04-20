/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.Ticket;
import org.hibernate.Session;

/**
 *
 * @author marcelo.lima
 */
public class TicketDAO {
    private final Session sessao;

    public TicketDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();        
    }
    
    /**
     * Persiste o ticket enviado na base de dados
     * @param ticket O ticket a ser salvo na base de dados
     */
    public void salvar(Ticket ticket) {
        sessao.saveOrUpdate(ticket);        
    }  
    
    /**
     * Pesquisa um ticket com o id informado
     * @param id O id do ticket a ser pesquisado
     * @return Um objeto Ticket
     */
    public Ticket carregar(int id) {
        return (Ticket) sessao.load(Ticket.class, id);
    }

    /**
     * Consulta codigo do Ticket não utilizado
     */
    public Ticket usarTicket(int codigo) {
        return (Ticket) sessao.createQuery("FROM Ticket WHERE id=:id AND dataUtilizado = NULL").setInteger("id", codigo).uniqueResult();
    }
}

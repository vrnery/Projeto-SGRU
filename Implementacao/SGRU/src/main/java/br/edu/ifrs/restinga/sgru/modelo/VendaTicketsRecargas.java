/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.persistencia.VendaTicketsRecargasDAO;
import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author marcelo.lima
 */
@Entity
public class VendaTicketsRecargas implements Serializable {        
    @GeneratedValue
    @Id
    private int id;
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar dataVenda;
    @OneToOne
    @JoinColumn(name = "idValorAlmoco")
    private ValorAlmoco valorAlmoco;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "idTicket")
    private Ticket ticket;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "idRecarga")
    private Recarga recarga;    
    @ManyToOne
    @JoinColumn(name = "idCaixaRU")
    private CaixaRU caixaRU;        

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * @return the dataVenda
     */
    public Calendar getDataVenda() {
        return dataVenda;
    }

    /**
     * @param dataVenda the dataVenda to set
     */
    public void setDataVenda(Calendar dataVenda) {
        this.dataVenda = dataVenda;
    }    

    /**
     * @return the valorAlmoco
     */
    public ValorAlmoco getValorAlmoco() {
        return valorAlmoco;
    }

    /**
     * @param valorAlmoco the valorAlmoco to set
     */
    public void setValorAlmoco(ValorAlmoco valorAlmoco) {
        this.valorAlmoco = valorAlmoco;
    }

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
     * Realiza uma recarga feita através do caixa
     */
    public void realizarRecargaCaixa() {
        VendaTicketsRecargasDAO daoVendaTicketsRecargas = new VendaTicketsRecargasDAO();
        this.setTicket(null);
        this.setDataVenda(Calendar.getInstance());
        this.setValorAlmoco(ValorAlmoco.carregarValorAtualAlmoco());
        daoVendaTicketsRecargas.salvar(this);
    }
    
    /**
     * Realiza uma recarga feita através da página do cliente
     */
    public void realizarRecargaPaginaCliente() {
        this.setCaixaRU(null);
        this.setDataVenda(Calendar.getInstance());
        this.setTicket(null);
        this.setValorAlmoco(ValorAlmoco.carregarValorAtualAlmoco());
        
        VendaTicketsRecargasDAO daoVendaTicketsRecargas = new VendaTicketsRecargasDAO();
        daoVendaTicketsRecargas.salvar(this);
    }

    public void realizarVendaTicket() {
        this.setDataVenda(Calendar.getInstance());
        this.setValorAlmoco(this.caixaRU.getValorAtualAlmoco());
        this.setRecarga(null);
        Ticket ticket = new Ticket(this.getValorAlmoco().getValorAlmoco(), this.getDataVenda());
        this.setTicket(ticket);
        
        VendaTicketsRecargasDAO daoVendaTicketsRecargas = new VendaTicketsRecargasDAO();
        daoVendaTicketsRecargas.salvar(this);
    }
}

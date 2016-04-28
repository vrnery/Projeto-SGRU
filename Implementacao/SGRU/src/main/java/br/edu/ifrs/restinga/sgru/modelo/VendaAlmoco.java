/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.persistencia.ValorAlmocoDAO;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author marcelo.lima
 */
@Entity
public class VendaAlmoco implements Serializable {
    @Id
    @GeneratedValue
    private int id;    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)    
    private Calendar dataVenda;
    @OneToOne
    @JoinColumn(name = "idValorAlmoco")
    private ValorAlmoco valorAlmoco;
    @OneToOne
    @JoinColumn(name = "idTicket")
    private Ticket ticket;
    @OneToOne
    @JoinColumn(name = "idCartao")
    private Cartao cartao;
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
     * @param valorAlmoco the valorAlmoco to set
     */
    public void setValorAlmoco(ValorAlmoco valorAlmoco) {       
        // Inicialmente seta o valor atual do almoco
        this.valorAlmoco = valorAlmoco;
        
        // verifica a necessidade de atualizar o valor do almoco
        if (this.getCartao() != null) {
            long miliSecondsCartao = this.getCartao().getDataCredito().getTimeInMillis();            
            // Se a data do cretito no cartao for menor que a data do valor
            // do almoco atual, verifica se eh necessario atualizar valor
            if (miliSecondsCartao < valorAlmoco.getDataValor().getTimeInMillis()) {
                int numDias = (int) ((Calendar.getInstance().getTimeInMillis() - miliSecondsCartao)/86400000L);
                
                // Os creditos podem ser usados em ateh 60 dias
                if (numDias <= 60) {
                   ValorAlmocoDAO dao = new ValorAlmocoDAO();
                   this.valorAlmoco = dao.getValorAlmocoPorData(this.getCartao().getDataCredito());                   
                }
            }
        }
    }
}

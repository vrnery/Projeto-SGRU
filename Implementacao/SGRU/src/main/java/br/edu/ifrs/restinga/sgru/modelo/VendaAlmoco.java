/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author marcelo.lima
 */
@Entity
public class VendaAlmoco implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    private String formaPagamento;
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
     * @return the formaPagamento
     */
    public String getFormaPagamento() {
        return formaPagamento;
    }

    /**
     * @param formaPagamento the formaPagamento to set
     */
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
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
        if (cartao != null) {
            // verifica se a data de expiração dos creditos do cartao jah venceu
            Date dataHoje = new Date();
            // 86400000 eh equivalente a 1 dia
            int diasCredito = (int) ((dataHoje.getTime() - cartao.getDataExpiracao().getTime()) / 86400000L);
            // 60 dias é a validade dos créditos para utilizar um valor 
            if (diasCredito > 60) {
                // valor de almoco eh o valor atual da tabela
            } else {
                // verifica qual valor de almoco utilizar
            }
        }
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

}

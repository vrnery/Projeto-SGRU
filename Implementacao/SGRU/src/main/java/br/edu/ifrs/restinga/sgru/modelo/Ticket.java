/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.persistencia.TicketDAO;
import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author marcelo.lima
 */
@Entity
public class Ticket implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    private double valor;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar dataCriado;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar dataUtilizado;

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
     * @return the valor
     */
    public double getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(double valor) {
        this.valor = valor;
    }

    /**
     * @return the dataCriado
     */
    public Calendar getDataCriado() {
        return dataCriado;
    }

    /**
     * @param dataCriado the dataCriado to set
     */
    public void setDataCriado(Calendar dataCriado) {
        this.dataCriado = dataCriado;
    }

    /**
     * @return the dataUtilizado
     */
    public Calendar getDataUtilizado() {
        return dataUtilizado;
    }

    /**
     * @param dataUtilizado the dataUtilizado to set
     */
    public void setDataUtilizado(Calendar dataUtilizado) {
        this.dataUtilizado = dataUtilizado;
    }
    
    /**
     * Retorna o ticket do código informado
     * @param codigo O código do ticket apresentado
     * @return Um objeto Ticket, com o ticket encontrado ou nulo
     */
    public static Ticket carregarTicket(int codigo) {
        TicketDAO dao = new TicketDAO();
        return dao.usarTicket(codigo);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.persistencia.ValorAlmocoDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author marcelo.lima
 */
@Entity
public class CaixaRU implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar dataAbertura;
    private double valorAbertura;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar dataFechamento;
    private double valorFechamento;    
    @OneToMany(mappedBy = "caixaRU")    
    private List<VendaAlmoco> vendaAlmoco = new ArrayList();
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="idOperadorCaixa")
    private OperadorCaixa operadorCaixa;       
    // Atributo nao persistido no banco
    @Transient
    private final ValorAlmoco valorAtualAlmoco;

    public CaixaRU() {
        // Carrega o valor de almoco atual
        ValorAlmocoDAO dao = new ValorAlmocoDAO();
        valorAtualAlmoco = dao.carregarValorAtualAlmoco();
    }
    
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
     * @return the dataAbertura
     */
    public Calendar getDataAbertura() {
        return dataAbertura;
    }

    /**
     * @param dataAbertura the dataAbertura to set
     */
    public void setDataAbertura(Calendar dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    /**
     * @return the valorAbertura
     */
    public double getValorAbertura() {
        return valorAbertura;
    }

    /**
     * @param valorAbertura the valorAbertura to set
     */
    public void setValorAbertura(double valorAbertura) {
        this.valorAbertura = valorAbertura;
    }
    
    /**
     * @return the dataFechamento
     */
    public Calendar getDataFechamento() {
        return dataFechamento;
    }

    /**
     * @param dataFechamento the dataFechamento to set
     */
    public void setDataFechamento(Calendar dataFechamento) {
        this.dataFechamento = dataFechamento;
    }    

    /**
     * @return the valorFechamento
     */
    public double getValorFechamento() {
        return valorFechamento;
    }

    /**
     * @param valorFechamento the valorFechamento to set
     */
    public void setValorFechamento(double valorFechamento) {
        this.valorFechamento = valorFechamento;
    }

    /**
     * @return the vendaAlmoco
     */
    public List<VendaAlmoco> getVendaAlmoco() {
        return vendaAlmoco;
    }

    /**
     * @param vendaAlmoco the vendaAlmoco to set
     */
    public void setVendaAlmoco(VendaAlmoco vendaAlmoco) {
        this.vendaAlmoco.add(vendaAlmoco);
    }

    /**
     * @return the operadorCaixa
     */
    public OperadorCaixa getOperadorCaixa() {
        return operadorCaixa;
    }

    /**
     * @param operadorCaixa the operadorCaixa to set
     */
    public void setOperadorCaixa(OperadorCaixa operadorCaixa) {
        this.operadorCaixa = operadorCaixa;
    }

    /**
     * @return the valorAtualAlmoco
     */
    public ValorAlmoco getValorAtualAlmoco() {
        return valorAtualAlmoco;
    }
}

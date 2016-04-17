/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author marcelo.lima
 */
@Entity
public class Cartao implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataExpiracao;
    private double saldo;    
    @OneToMany(mappedBy = "cartao")
    private List<Recarga> recarga = new ArrayList();

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
     * @return the dataExpiracao
     */
    public Date getDataExpiracao() {
        return dataExpiracao;
    }

    /**
     * @param dataExpiracao the dataExpiracao to set
     */
    public void setDataExpiracao(Date dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    /**
     * @return the saldo
     */
    public double getSaldo() {
        return saldo;
    }

    /**
     * @param saldo the saldo to set
     */
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    /**
     * Descontar valor do almoço
     * @param valor the saldo to set
     */
    public void descontar(double valor) {
        this.saldo -= valor;
    }
    
    /**
     * @return the recarga
     */
    public List<Recarga> getRecarga() {
        return recarga;
    }

    /**
     * @param recarga the recarga to set
     */
    public void setRecarga(Recarga recarga) {
        // verifica se o saldo do cartao estah vazio
        // caso afirmativo, atualiza o saldo do cartao
        if (this.saldo <= 0) {
            // tranfere o valor da recarga para o cartao
            this.setSaldo(recarga.getValorRecarregado());
            // seta o valor da recarga como utilizado
            recarga.setUtilizado(true);
        }
        this.recarga.add(recarga);               
    }    
}

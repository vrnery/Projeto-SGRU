/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar dataCredito;
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
     * @return the dataCredito
     */
    public Calendar getDataCredito() {
        return dataCredito;
    }

    /**
     * @param dataCredito the dataCredito to set
     */
    public void setDataCredito(Calendar dataCredito) {
        this.dataCredito = dataCredito;
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
            // data do credito eh a mesma data da recarga
            this.setDataCredito(recarga.getDataCredito());
            // seta o valor da recarga como utilizado
            recarga.setUtilizado(true);
        }
        this.recarga.add(recarga);               
    }
    
    /**
     * Descontar o valor do almoco
     * @param valor ValorAlmoco
     */
    public void setDebitar(double valor) {
        this.saldo -= valor;
    }
}

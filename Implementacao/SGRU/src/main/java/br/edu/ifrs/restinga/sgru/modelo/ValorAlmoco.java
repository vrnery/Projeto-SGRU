/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.excessao.ValorAlmocoInvalidoException;
import br.edu.ifrs.restinga.sgru.persistencia.ValorAlmocoDAO;
import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author marcelo.lima
 */
@Entity
public class ValorAlmoco implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    private double valorAlmoco;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar dataValor;
    // O valor do credito do cartao terah validade por 60 dias, apos isso serah cobrado
    // o valor atual do almoco
    @Transient
    private static final int NUM_DIAS_VALIDADE_RECARGA = 60;

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
     * @return the valorAlmoco
     */
    public double getValorAlmoco() {        
        return valorAlmoco;
    }

    /**
     * @param valorAlmoco the valorAlmoco to set
     */
    public void setValorAlmoco(double valorAlmoco) {        
        this.valorAlmoco = valorAlmoco;                
    }

    /**
     * @return the dataValor
     */
    public Calendar getDataValor() {
        return dataValor;
    }

    /**
     * @param dataValor the dataValor to set
     */
    public void setDataValor(Calendar dataValor) {
        this.dataValor = dataValor;
    }            

    /**
     * Carrega o valor cobrado atualmente pelo almoço
     * @return Um objeto ValorAlmoco, com o valor cobrado no dia
     */
    public static ValorAlmoco carregarValorAtualAlmoco() {    
        ValorAlmocoDAO dao = new ValorAlmocoDAO();
        return dao.carregarValorAtualAlmoco();
    }
    
    /**
     * Verifica qual valor a pagar pelo almoço, com base na data informada
     * @param dataCreditoCartao A data de crédito da(s) recarga(s) no cartão
     * @param valorAtualAlmoco Um objeto ValorAlmoco com o valor praticado atualmente
     * @return Um objeto ValorAlmoco
     * @throws ValorAlmocoInvalidoException Caso não encontre um valor de almoço para a data de crédito do cartão
     */
    public static ValorAlmoco verificarValorPagarAlmoco(Calendar dataCreditoCartao,
            ValorAlmoco valorAtualAlmoco) throws ValorAlmocoInvalidoException {
        // Se a data do cretito no cartao for menor que a data do valor
        // do almoco atual, verifica se eh necessario atualizar valor            
        if (valorAtualAlmoco == null) {
            throw new ValorAlmocoInvalidoException("Nenhum valor de almoço configurado!");
        }
        if (valorAtualAlmoco.getDataValor().after(dataCreditoCartao)) {
            long miliSecondsCartao = dataCreditoCartao.getTimeInMillis();            
            int numDias = (int) ((Calendar.getInstance().getTimeInMillis() - miliSecondsCartao)/86400000L);                

            // Os creditos podem ser usados em ateh 60 dias
            if (numDias <= NUM_DIAS_VALIDADE_RECARGA) {
                ValorAlmocoDAO daoValorAlmoco = new ValorAlmocoDAO();
                return daoValorAlmoco.getValorAlmocoPorData(dataCreditoCartao);
            }
        }        
        return valorAtualAlmoco;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.excessao.RecargaNaoEncontradaException;
import br.edu.ifrs.restinga.sgru.excessao.SaldoInsuficienteException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
    private double saldoUltimaRecarga;
    @OneToMany(mappedBy = "cartao", cascade = {CascadeType.ALL})
    private List<Recarga> lstRecarga = new ArrayList();            
    @OneToOne(mappedBy = "cartao")
    private Cliente cliente;

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
     * @return the saldoUltimaRecarga
     */
    public double getSaldoUltimaRecarga() {
        return saldoUltimaRecarga;
    }

    /**
     * @param saldoUltimaRecarga the saldoUltimaRecarga to set
     */
    public void setSaldoUltimaRecarga(double saldoUltimaRecarga) {
        this.saldoUltimaRecarga = saldoUltimaRecarga;
    }    

    /**
     * Descontar valor do almoço
     * @param valor the saldo to set
     */
    public void descontar(double valor) {
        this.saldo -= valor;
    }
    
    /**
     * @return the lstRecarga
     */
    public List<Recarga> getLstRecarga() {
        return lstRecarga;
    }
    
    /**
     * Descontar o valor do almoco
     * @param valor ValorAlmoco
     */
    public void setDebitar(double valor) {
        this.saldo -= valor;
    }            
    
    /**
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }    
    
    /**
     * Verifica se o saldo da última recarga do cartão é maior que zero. Caso afirmativo, transfere o valor de recarga para o cartão, caso exista. 
     * Caso o saldo da última recarga não seja suficiente para pagar o almoço, ainda assim será cobrado o valor do almoço para aquela data de crédito da recarda do cartão do cliente.
     * @throws SaldoInsuficienteException Se o cartão não possui saldo para a compra do almoço
     */
    public void verificarSaldoCartao() throws SaldoInsuficienteException {
        try {            
            if (this.saldoUltimaRecarga <= 0) {
                // eh necessario atualizar o saldo
                transferirRecargaParaCartao();

                // Caso recarga zerada
                if (this.saldo <= 0) {
                    throw new SaldoInsuficienteException("Saldo insuficiente!");
                }
            }
        } catch(RecargaNaoEncontradaException e) {
            throw new SaldoInsuficienteException("Saldo insuficiente!");
        }
    }    
    
    /**
     * Carrega a recarga mais antiga para o cartao. Se houver mais de uma recarga realizada na mesma data, soma os valores
     * @throws br.edu.ifrs.restinga.sgru.excessao.RecargaNaoEncontradaException Se o cartão não possui recargas
     */
    public void transferirRecargaParaCartao() throws RecargaNaoEncontradaException {        
        List<Recarga> tmpLstRecargas = new ArrayList();
        
        // A lista deve conter apenas recargas ainda nao utilizadas        
        for (Recarga rec : lstRecarga) {
            if (!rec.isUtilizado()) {                
                tmpLstRecargas.add(rec);
            }
        }                
        
        // Nao ha recargas para o cartao
        //if (lstRecarga.isEmpty()) {
        if (tmpLstRecargas.isEmpty()) {
            throw new RecargaNaoEncontradaException("Nenhuma recarga encontrada para o cartão!");
        }                        
        
        // Aqui ordenamos o arraylist por data, sendo que a primeira serah
        // a mais antiga
        if (tmpLstRecargas.size() > 1) {
            Collections.sort(tmpLstRecargas, new Comparator<Recarga>() {
                @Override
                public int compare(Recarga rec1, Recarga rec2) {                
                    if ((rec1.getDataCredito() == null) || rec2.getDataCredito() == null) {
                        return 0;
                    }                
                    // Date possui um metodo compareTo para comparar as datas
                    return rec1.getDataCredito().compareTo(rec2.getDataCredito());
                }
            });
        }
        
        // Se o cliente fez mais de uma recarga no mesmo dia, entao
        // carrega todas as recargas daquele dia
        double valRecargas = 0;
        Calendar dataRecarga = tmpLstRecargas.get(0).getDataCredito();
        // Seta a data do saldo do cartao como a data da lstRecarga
        this.setDataCredito(dataRecarga);
        for (Recarga rec : tmpLstRecargas) {            
            if (rec.getDataCredito().equals(dataRecarga)) {
                // atualiza valor a ser atualizado no saldo da ultima recarga do cartao
                valRecargas += rec.getValorRecarregado();
                // seta o valor da lstRecarga como utilizado
                rec.setUtilizado(true);
            } else {
                break;
            }
        }
        // Transfere o saldo da lstRecarga para o cartao
        //this.setSaldo(valRecargas);
        this.setSaldoUltimaRecarga(this.getSaldoUltimaRecarga() + valRecargas);
    }    
}

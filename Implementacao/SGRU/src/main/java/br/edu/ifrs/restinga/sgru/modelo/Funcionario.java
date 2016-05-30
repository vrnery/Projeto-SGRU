/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;

/**
 *
 * @author marcelo.lima
 */
@Entity
@PrimaryKeyJoinColumn(name="id")
public class Funcionario extends Pessoa {
    @Transient
    public static final String GERENTE = "001";
    @Transient
    public static final String OPERADOR_CAIXA = "002";
    
    @ManyToOne
    @JoinColumn(name = "idTipoFuncionario")
    private TipoFuncionario tipoFuncionario;

    /**
     * @return the tipoFuncionario
     */
    public TipoFuncionario getTipoFuncionario() {
        return tipoFuncionario;
    }

    /**
     * @param tipoFuncionario the tipoFuncionario to set
     */
    public void setTipoFuncionario(TipoFuncionario tipoFuncionario) {
        this.tipoFuncionario = tipoFuncionario;
    }
}

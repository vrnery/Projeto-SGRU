/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author marcelo.lima
 */
@Entity
public class TipoFuncionario implements Serializable {
    @GeneratedValue
    @Id
    private int id;
    private String descricao;
    @Column(unique = true)
    private String codigo;

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
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    // Utilizado para a implementação do convert
    @Override
    public int hashCode() {
        final int primo = 11;
        int resultado = 5;
        resultado = primo * resultado + this.getId();
        return resultado;
    }

    // Utilizado para a implementação do convert
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;        
        if (getClass() != obj.getClass())
            return false;
        TipoFuncionario tipoFuncionario = (TipoFuncionario) obj;
        return this.getId() == tipoFuncionario.getId();
    }    
}

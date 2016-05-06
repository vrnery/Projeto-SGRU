/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import java.io.File;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 *
 * @author marcelo.lima
 */
@Entity
@PrimaryKeyJoinColumn(name="id")
public class Professor extends Pessoa {        
    private String caminhoFoto;       
    @JoinColumn(name="idCartao")  
    @OneToOne(cascade = {CascadeType.ALL})
    private Cartao cartao;

    /**
     * @return the caminhoFoto
     */
    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    /**
     * @param caminhoFoto the caminhoFoto to set
     */
    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
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
     * Verifica se a foto do usuário existe
     * @return True, caso a foto exista e false caso não exista
     */
    public boolean verificarExistenciaFoto() {
        File foto = new File(caminhoFoto);
        if (foto.exists()) {
            return true;
        }
        return false;
    }
}

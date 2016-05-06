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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;

/**
 *
 * @author marcelo.lima
 */
@Entity
@PrimaryKeyJoinColumn(name="id")
public class Cliente extends Pessoa {            
    private String caminhoFoto;                        
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "idCartao")
    private Cartao cartao;     
    @ManyToOne
    @JoinColumn(name = "idCodTipoCliente")
    private CodTipoCliente codTipoCliente;
    // Tipos de cliente, segundo tabela codTipoCliente
    @Transient
    public final static int ALUNO = 1;
    @Transient
    public final static int PROFESSOR = 2;

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
     * @return the codTipoCliente
     */
    public CodTipoCliente getCodTipoCliente() {
        return codTipoCliente;
    }

    /**
     * @param codTipoCliente the codTipoCliente to set
     */
    public void setCodTipoCliente(CodTipoCliente codTipoCliente) {
        this.codTipoCliente = codTipoCliente;
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

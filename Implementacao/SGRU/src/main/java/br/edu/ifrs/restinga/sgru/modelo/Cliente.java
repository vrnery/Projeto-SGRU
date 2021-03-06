/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.excessao.FotoNaoEncontradaException;
import java.io.File;
import java.io.Serializable;
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
public class Cliente extends Pessoa implements Serializable {            
    // Tipos de cliente, segundo tabela tipoCliente
    @Transient
    public final static String ALUNO = "001";
    @Transient
    public final static String PROFESSOR = "002";    
    private String caminhoFoto;                        
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "idCartao")
    private Cartao cartao;     
    @ManyToOne
    @JoinColumn(name = "idTipoCliente")
    private TipoCliente tipoCliente;    

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
     * @return the tipoCliente
     */
    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    /**
     * @param tipoCliente the tipoCliente to set
     */
    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }        
    
    /**
     * Verifica se a foto do usuário existe     
     * @throws br.edu.ifrs.restinga.sgru.excessao.FotoNaoEncontradaException Caso não encontre a foto do cliente
     */
    public void verificarExistenciaFoto() throws FotoNaoEncontradaException  {
        File foto = new File(caminhoFoto);
        if (!foto.exists()) {            
            throw new FotoNaoEncontradaException("A apresentação de um documento de identidade com foto é obrigatória!");
        }        
    }        
    
    // Utilizado para a implementação do convert
    @Override
    public int hashCode() {
        final int primo = 7;
        int resultado = 3;
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
        Cliente cliente = (Cliente) obj;
        return this.getId() == cliente.getId();
    }    
}

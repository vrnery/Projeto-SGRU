/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;

/**
 *
 * @author marcelo.lima
 */
@Entity
@PrimaryKeyJoinColumn(name="id")
public class Aluno extends Pessoa {            
    private String caminhoFoto;                    
    @JoinColumn(name="idCartao")    
    @OneToOne(cascade = {CascadeType.ALL})
    private Cartao cartao; 
    @Transient
    private byte[] foto = null;

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
     * @return the foto
     */    
    public byte[] getFoto() {
        if ((foto == null) && (getCaminhoFoto() != null)) {
            // Carrega a imagem para um array de bytes no atributo foto
            File imgFile = new File(getCaminhoFoto());
            if (!imgFile.exists()) {
                // Nao localizou a foto para a matricula informada
                imgFile = new File("c:\\imagens\\semFoto.jpg");
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {            
                BufferedImage imagem = ImageIO.read(imgFile);
                ImageIO.write(imagem, "PNG", bos);
                bos.flush();  
                foto = bos.toByteArray();
                
            } catch (IOException e) {            
            }                    
        }                        
        return foto;
    }

    /**
     * @param foto the foto to set
     */
    public void setFoto(byte[] foto) {
        this.foto = foto;
    }    
}

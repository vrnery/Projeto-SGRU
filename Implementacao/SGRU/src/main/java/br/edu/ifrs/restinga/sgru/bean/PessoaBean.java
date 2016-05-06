/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.modelo.Aluno;
import br.edu.ifrs.restinga.sgru.modelo.Pessoa;
import br.edu.ifrs.restinga.sgru.modelo.Professor;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author marcelo.lima
 */
@ManagedBean
@RequestScoped
public class PessoaBean {
    private Aluno aluno;
    private Professor professor;
    private Pessoa pessoa;
    private byte[] foto = null;
    private static final String CAMINHO_FOTO_DEFAULT = "/imagens/semFoto.png";

    public PessoaBean() {
        FacesContext context = FacesContext.getCurrentInstance();
        CaixaRUBean caixaRUBean = (CaixaRUBean) context.getExternalContext().getSessionMap().get("caixaRUBean");                
        if (caixaRUBean.getCaixaRU().ultimoAlmocoVendido().getCartao().getAluno() != null) {
            aluno = caixaRUBean.getCaixaRU().ultimoAlmocoVendido().getCartao().getAluno();
        } else {
            professor = caixaRUBean.getCaixaRU().ultimoAlmocoVendido().getCartao().getProfessor();
        }                
    }        
    
    /**
     * @return the pessoa
     */
    public Pessoa getPessoa() {
        return pessoa;
    }

    /**
     * @param pessoa the pessoa to set
     */
    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }    
    
    /**
     * @return the nome
     */
    public String getNome() {
        if (aluno != null) {
            return aluno.getNome();
        } else {
            return professor.getNome();
        }        
    }

    /**
     * @return the matricula
     */
    public String getMatricula() {
        if (aluno != null) {
            return aluno.getMatricula();
        } else {
            return professor.getMatricula();
        }
    }
    
    /**
     * Retorna uma imagem para apresentar em um componente p:graphicImage
     * @return Um objeto StreamedContent, que pode ser apresentado em um componente p:graphicImage
     */
    public StreamedContent getFoto() {
        FacesContext context = FacesContext.getCurrentInstance();
        
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }        
        
        // So, browser is requesting the image. Return a real StreamedContent with the image bytes.        
        if (aluno != null) {
            converterFoto(aluno.getCaminhoFoto());            
        } else {
            converterFoto(professor.getCaminhoFoto());                
        }
        return new DefaultStreamedContent(new ByteArrayInputStream(foto));                    
    }        
    
    /**
     * Converte um arquivo de imagem em um array de baytes     
     * @param caminhoFoto A localização do arquivo de imagem no computador     
     */
    public void converterFoto(String caminhoFoto) {                                                        
        // Carrega a imagem para um array de bytes no atributo foto        
        File imgFile = new File(caminhoFoto);
        if (!imgFile.exists()) {
            // Nao localizou a foto para a matricula informada, entao carrega a imagem default                
            // Pega o diretorio onde estah localizada a imagem default
            String realPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(CAMINHO_FOTO_DEFAULT);
            imgFile = new File(realPath);            
        }
        
        // Converte o arquivo em um array de bytes
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {            
            BufferedImage imagem = ImageIO.read(imgFile);
            ImageIO.write(imagem, "PNG", bos);
            bos.flush();  
            foto = bos.toByteArray();                
        } catch (IOException e) {
        }                            
    }    
}

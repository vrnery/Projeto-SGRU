/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.modelo.Aluno;
import br.edu.ifrs.restinga.sgru.modelo.Pessoa;
import br.edu.ifrs.restinga.sgru.modelo.Professor;
import java.io.ByteArrayInputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
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
    private String nome;
    private String matricula;

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
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
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
     * @param matricula the matricula to set
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }    
    
    public StreamedContent getImagem() {
        FacesContext context = FacesContext.getCurrentInstance();

        //if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
        //    return new DefaultStreamedContent();
        //}

        // So, browser is requesting the image. Return a real StreamedContent with the image bytes.                        
        /*
        CaixaRUBean caixaRUBean = (CaixaRUBean) context.getExternalContext().getSessionMap().get("caixaRUBean");                
        if (caixaRUBean.getCaixaRU().ultimoAlmocoVendido().getCartao().getAluno() != null) {
            Aluno aluno = caixaRUBean.getCaixaRU().ultimoAlmocoVendido().getCartao().getAluno();
            return new DefaultStreamedContent(new ByteArrayInputStream(aluno.getFoto()));            
        } else {
            Professor professor = caixaRUBean.getCaixaRU().ultimoAlmocoVendido().getCartao().getProfessor();
            return new DefaultStreamedContent(new ByteArrayInputStream(professor.getFoto()));            
        } 
        */
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        
        // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
        if (aluno != null) {
            return new DefaultStreamedContent(new ByteArrayInputStream(aluno.getFoto()));            
        } else {
            return new DefaultStreamedContent(new ByteArrayInputStream(professor.getFoto()));            
        }
    }    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.modelo.Aluno;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import br.edu.ifrs.restinga.sgru.persistencia.AlunoDAO;

/**
 *
 * @author marcelo.lima
 */
public class AlunoBean {
    private Aluno aluno = new Aluno();
    private final AlunoDAO dao = new AlunoDAO();

    /**
     * @return the aluno
     */
    public Aluno getAluno() {
        return aluno;        
    }

    /**
     * @param aluno the aluno to set
     */
    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
    
    /**
     * Solicita à camada de persistência o cadastro de um aluno
     */
    public void salvar() {
        dao.salvar(aluno);
        enviarMensagem(FacesMessage.SEVERITY_INFO, "Aluno cadastrado com sucesso!");
    }    
    
    /**
     * Solicita a pesquisa de um aluno para a camada de persistência
     * @param matricula A matricula do aluno a ser pesquisada
     */
    public void carregar(String matricula) {
        aluno = dao.carregar(matricula);        
    }
    
    /**
     * Envia à viewer uma mensagem com o status da operação
     * @param sev A severidade da mensagem
     * @param msg A mensagem a ser apresentada
     */
    private void enviarMensagem(FacesMessage.Severity sev, String msg) {
        FacesContext context = FacesContext.getCurrentInstance();        
        context.addMessage(null, new FacesMessage(sev, msg, ""));
    }        
    
    @PreDestroy
    public void encerrar() {
        dao.encerrar();
    }    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException;
import br.edu.ifrs.restinga.sgru.modelo.Professor;
import br.edu.ifrs.restinga.sgru.persistencia.ProfessorDAO;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author 10070133
 */
@ManagedBean
@RequestScoped
public class ProfessorBean {
    private Professor professor = new Professor();
    private final ProfessorDAO dao = new ProfessorDAO();

    /**
     * @return the professor
     */
    public Professor getProfessor() {
        return professor;
    }

    /**
     * @param professor the professor to set
     */
    public void setProfessor(Professor professor) {
        this.professor = professor;
    }    
    
    /**
     * Solicita à camada de persistência o cadastro de um aluno
     */
    public void salvar() {
        dao.salvar(professor);
        enviarMensagem(FacesMessage.SEVERITY_INFO, "Professor cadastrado com sucesso!");
    }    
    
    /**
     * Solicita a pesquisa de um aluno para a camada de persistência
     * @param matricula A matricula do aluno a ser pesquisada
     * @throws br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException Se a matrícula informada não for localizada
     */
    public void carregar(String matricula) throws MatriculaInvalidaException {
        Professor tmpProfessor = dao.carregar(matricula);
        
        if (tmpProfessor == null) {
            throw new MatriculaInvalidaException("Matrícula não encontrada!");
        }
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
}

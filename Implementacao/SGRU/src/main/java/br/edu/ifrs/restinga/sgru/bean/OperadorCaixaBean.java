/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException;
import br.edu.ifrs.restinga.sgru.modelo.OperadorCaixa;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import br.edu.ifrs.restinga.sgru.persistencia.OperadorCaixaDAO;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author marcelo.lima
 */
@ManagedBean
@RequestScoped
public class OperadorCaixaBean {
    private OperadorCaixa operadorCaixa = new OperadorCaixa();
    private final OperadorCaixaDAO dao = new OperadorCaixaDAO();

    /**
     * @return the operadorCaixa
     */
    public OperadorCaixa getOperadorCaixa() {
        return operadorCaixa;
    }

    /**
     * @param operadorCaixa the operadorCaixa to set
     */
    public void setOperadorCaixa(OperadorCaixa operadorCaixa) {
        this.operadorCaixa = operadorCaixa;
    }
    
    /**
     * Solicita à camada de persistência o cadastro de um operador de caixa
     */
    public void salvar() {
        dao.salvar(operadorCaixa);
        //enviarMensagem(FacesMessage.SEVERITY_INFO, "Operador de caixa cadastrado com sucesso!");
    }
    
    /**
     * Solicita a pesquisa de um operador de caixa para a camada de persistência
     * @param matricula A matricula do operador de caixa a ser pesquisado
     * @throws br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException Se a matrícula não for localizada     
     */
    public void carregar(String matricula) throws MatriculaInvalidaException {
        OperadorCaixa tmpOperadorCaixa = dao.carregar(matricula);
        if (tmpOperadorCaixa == null) {
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

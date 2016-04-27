/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.excessao.LoginInvalidoException;
import br.edu.ifrs.restinga.sgru.modelo.OperadorCaixa;
import br.edu.ifrs.restinga.sgru.modelo.Pessoa;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import br.edu.ifrs.restinga.sgru.persistencia.OperadorCaixaDAO;
import br.edu.ifrs.restinga.sgru.persistencia.PessoaDAO;
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
     * Autentica um usuário no sistema
     * @return A página a ser visualizada pelo usuário após o login     
     */
    public String autenticar() {        
        String retorno = "caixa";
        try {
            PessoaDAO daoPessoa = new PessoaDAO();
            Pessoa pessoa = daoPessoa.autenticar(operadorCaixa.getLogin(), operadorCaixa.getSenha());

            if (!(pessoa instanceof OperadorCaixa)) {
                throw new LoginInvalidoException("Usuário ou senha incorretos!");                
            }
            
            operadorCaixa = (OperadorCaixa) pessoa;
            // abre o caixa
            CaixaRUBean caixaRUBean = new CaixaRUBean();
            caixaRUBean.realizarAberturaCaixa(operadorCaixa, 0);                        
        } catch (LoginInvalidoException e) {
            operadorCaixa = new OperadorCaixa();
            retorno = "index";
            enviarMensagem(FacesMessage.SEVERITY_INFO, e.getMessage());
        }
        return retorno;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.excessao.LoginInvalidoException;
import br.edu.ifrs.restinga.sgru.modelo.CaixaRU;
import br.edu.ifrs.restinga.sgru.modelo.OperadorCaixa;
import br.edu.ifrs.restinga.sgru.modelo.Pessoa;
import br.edu.ifrs.restinga.sgru.persistencia.PessoaDAO;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author marcelo.lima
 */
@ManagedBean
@SessionScoped
public class AutenticarBean {            
    private OperadorCaixa operadorCaixa;    
    private String login;
    private String senha;
    private String nomeUsuario;
    @ManagedProperty(value="#{caixaRUBean}")
    private CaixaRUBean caixaRUBean;

    /**     
     * @param caixaRUBean The caixaRUBean to set
     */
    public void setCaixaRUBean(CaixaRUBean caixaRUBean) {
        this.caixaRUBean = caixaRUBean;
    }        
    
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
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }        

    /**
     * @return the nomeUsuario
     */
    public String getNomeUsuario() {
        return nomeUsuario;
    }

    /**
     * @param nomeUsuario the nomeUsuario to set
     */
    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }    
    
    /**
     * Autentica um usuário no sistema
     * @return A página a ser visualizada pelo usuário após o login     
     */
    public String autenticar() {        
        String retorno = "abrirCaixa";
        
        Pessoa pessoa;
        PessoaDAO daoPessoa = new PessoaDAO();
        try {                        
            pessoa = daoPessoa.autenticar(getLogin(), getSenha());
         
            // Nome do usuario para apresentacao nas demais telas
            if (pessoa != null) {
                setNomeUsuario(pessoa.getNome());
            }
            
            if (pessoa instanceof OperadorCaixa) {
                operadorCaixa = (OperadorCaixa) pessoa;
                // abre o caixa
                caixaRUBean.isCaixaAberto(operadorCaixa);
                
                if (caixaRUBean.getCaixaRU() != null) {
                    retorno = "caixa";
                } else {
                    // garante que haverah um caixaRU para setar as propriedades
                    // na abertura de caixa
                    caixaRUBean.setCaixaRU(new CaixaRU());
                }                
            }                        
        } catch (LoginInvalidoException e) {
            retorno = "index";
            enviarMensagem(FacesMessage.SEVERITY_INFO, e.getMessage());
        }        
        return retorno;
    }    
    
    public String realizarLogout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index";
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

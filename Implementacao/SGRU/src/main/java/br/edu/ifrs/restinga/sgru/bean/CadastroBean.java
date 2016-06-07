/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.excessao.DadoPessoaInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException;
import br.edu.ifrs.restinga.sgru.modelo.ControladorCadastro;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author marcelo.lima
 */
@ManagedBean
@ViewScoped
public class CadastroBean {    
    private final ControladorCadastro controladorCadastro;
    private UploadedFile file;
    
    public CadastroBean() {
        this.controladorCadastro = new ControladorCadastro();
        this.file = null;
    }        
    
    /**
     * retorno do controladorCadastro
     * @return controladorCadastro
     */
    public ControladorCadastro getControladorCadastro() {
        return controladorCadastro;
    }
    
    /**
     * @return the file
     */
    public UploadedFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public String salvar(String txtPath) {
        /*
        try {
            this.controladorCadastro.salvar();
            enviarMensagem(FacesMessage.SEVERITY_INFO, "Usuário cadastrado com sucesso!");
            return "index";
        } catch (UsuarioInvalidoException | DadoPessoaInvalidoException ex) {
            enviarMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
        return "cadastrarCliente";
        */
        InputStream inputStream = null;
        String extArquivo = null;
        try {
            // Trata a foto, caso o usuario tenha alterado            
            if ((this.file != null) && (!this.file.getFileName().isEmpty()))  {
                inputStream = this.file.getInputstream();
                String fileName = this.file.getFileName();
                extArquivo = fileName.substring(fileName.lastIndexOf(".")+1);
            }
            this.controladorCadastro.salvar(inputStream, extArquivo, txtPath);
            enviarMensagem(FacesMessage.SEVERITY_INFO, "Usuário cadastrado com sucesso!");
            return "index";                        
        } catch(IOException e) {            
            enviarMensagem(FacesMessage.SEVERITY_ERROR, "Problemas ao carregar a foto!");                    
        } catch (UsuarioInvalidoException | DadoPessoaInvalidoException ex) {
            enviarMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }    
        return null;
    }
    
    public void salvarFuncionario() {
        try {
            this.controladorCadastro.salvarFuncionario();
        } catch (UsuarioInvalidoException | DadoPessoaInvalidoException ex) {
            enviarMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
    }
    
    /**
     * Edita um usuário
     * @param txtPath Recebe a path da aplicação
     */
    public void editarUsuario(String txtPath) {         
        InputStream inputStream = null;
        String extArquivo = null;
        try {
            // Trata a foto, caso o usuario tenha alterado            
            if ((this.file != null) && (!this.file.getFileName().isEmpty())) {
                inputStream = this.file.getInputstream();
                String fileName = this.file.getFileName();
                extArquivo = fileName.substring(fileName.lastIndexOf(".")+1);
            }
            this.controladorCadastro.editarUsuario(inputStream, extArquivo, txtPath);
            enviarMensagem(FacesMessage.SEVERITY_INFO, "Usuário alterado com sucesso!");
        } catch(IOException e) {            
            enviarMensagem(FacesMessage.SEVERITY_ERROR, "Problemas ao carregar a foto!");                    
        } catch (UsuarioInvalidoException | DadoPessoaInvalidoException ex) {
            enviarMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
                
    }
    
    public void editarFuncionario() {
        try {
            this.controladorCadastro.editarFuncionario();
            enviarMensagem(FacesMessage.SEVERITY_INFO, "Usuário alterado com sucesso!");
        } catch (UsuarioInvalidoException | DadoPessoaInvalidoException ex) {
            enviarMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
    }
        
    /**
     * Exclui um usuário do sistema
     * @param idUsuario O id do usuário a ser excluído
     */
    public void excluirUsuario(int idUsuario) {
        this.controladorCadastro.excluirUsuario(idUsuario);
        enviarMensagem(FacesMessage.SEVERITY_INFO, "Usuário excluído com sucesso!");
    }
    
    private void enviarMensagem(FacesMessage.Severity sev, String msg) {
        FacesContext context = FacesContext.getCurrentInstance();        
        context.addMessage(null, new FacesMessage(sev, msg, ""));
    }   
}

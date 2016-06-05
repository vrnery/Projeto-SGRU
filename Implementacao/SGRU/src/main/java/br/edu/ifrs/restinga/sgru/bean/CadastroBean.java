/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

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
    private String codTipoCliente;
    private UploadedFile file;
    
    public CadastroBean() {
        this.controladorCadastro = new ControladorCadastro();
        this.file = null;
    }        
    
    /**
     * @return the codTipoCliente
     */
    public String getCodTipoCliente() {
        return codTipoCliente;
    }

    /**
     * @param codTipoCliente the codTipoCliente to set
     */
    public void setCodTipoCliente(String codTipoCliente) {
        this.codTipoCliente = codTipoCliente;
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
    
    public String salvar() {
        try {
            this.controladorCadastro.salvar();
            return "index";
        } catch (UsuarioInvalidoException ex) {
            enviarMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
            return "cadastrarCliente";
        }
    }
    
    public void salvarFuncionario() {
        try {
            this.controladorCadastro.salvarFuncionario();
        } catch (UsuarioInvalidoException ex) {
            enviarMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
    }
    
    /**
     * Edita um usuário
     */
    public void editarUsuario() {         
        try {
            // Trata a foto, caso o usuario tenha alterado            
            InputStream inputStream = null;
            String extArquivo = null;
            if (this.file != null) {
                inputStream = this.file.getInputstream();
                
                String fileName = this.file.getFileName();
                extArquivo = fileName.substring(fileName.lastIndexOf(".")+1);                                                        
            }
            this.controladorCadastro.editarUsuario(inputStream, extArquivo);
        } catch(IOException e) {            
            enviarMensagem(FacesMessage.SEVERITY_ERROR, "Probelmas ao carregar a foto!");        
        }
                
    }
    
    public void editarFuncionario() {
        this.controladorCadastro.editarFuncionario();
    }
        
    /**
     * Exclui um usuário do sistema
     * @param idUsuario O id do usuário a ser excluído
     */
    public void excluirUsuario(int idUsuario) {
        this.controladorCadastro.excluirUsuario(idUsuario);
    }
    
    private void enviarMensagem(FacesMessage.Severity sev, String msg) {
        FacesContext context = FacesContext.getCurrentInstance();        
        context.addMessage(null, new FacesMessage(sev, msg, ""));
    }   
}

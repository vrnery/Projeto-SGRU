/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.excessao.DadoPessoaInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException;
import br.edu.ifrs.restinga.sgru.modelo.ControladorCadastro;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author marcelo.lima
 */
@ManagedBean
@ViewScoped
public class CadastroBean {    
    // Caso nao consiga carregar foto do cliente
    private static final String CAMINHO_FOTO_DEFAULT = "/imagens/fotos/semFoto.png";
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
    
    public void salvarCliente() {
        InputStream inputStream = null;
        String extArquivo = null;
        String txtPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        
        // O separador serah colocado no controlador
        if ((txtPath.endsWith("\\")) || ((txtPath.endsWith("/")))) {
            txtPath = txtPath.substring(0, txtPath.length()-1);
        }
        try {
            // Trata a foto, caso o usuario tenha alterado            
            if ((this.file != null) && (!this.file.getFileName().isEmpty()))  {
                inputStream = this.file.getInputstream();
                String fileName = this.file.getFileName();
                extArquivo = fileName.substring(fileName.lastIndexOf(".")+1);
            }
            this.controladorCadastro.salvarCliente(inputStream, extArquivo, txtPath);
            enviarMensagem(FacesMessage.SEVERITY_INFO, "Usuário cadastrado com sucesso!");            
        } catch(IOException e) {            
            enviarMensagem(FacesMessage.SEVERITY_ERROR, "Problemas ao carregar a foto!");                    
        } catch (UsuarioInvalidoException | DadoPessoaInvalidoException ex) {
            enviarMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }                    
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
     */
    public void editarCliente() {         
        InputStream inputStream = null;
        String extArquivo = null;
        String txtPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        
        // O separador serah colocado no controlador
        if ((txtPath.endsWith("\\")) || ((txtPath.endsWith("/")))) {
            txtPath = txtPath.substring(0, txtPath.length()-1);
        }
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
            this.controladorCadastro.editarUsuario();
            enviarMensagem(FacesMessage.SEVERITY_INFO, "Usuário alterado com sucesso!");
        } catch(IOException e) {            
            enviarMensagem(FacesMessage.SEVERITY_ERROR, "Problemas ao carregar a foto!");
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
    
    /**
     * Efetua a recarga em um cartão do cliente
     * @return A próxima página a ser visualizada pelo operador
     */
    public String efetuarRecarga() {
        return null;
    }
    
    /**
     * @return the foto
     */
    public StreamedContent getFoto() {        
        FacesContext context = FacesContext.getCurrentInstance();        
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }         
        // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
        return converterFoto();                
    }        
    
    /**
     * Converte uma imagem para apresentar em um componente p:graphicImage     
     * @return Um objeto StreamedContent
     */
    public StreamedContent converterFoto() {        
        // Cria um objeto File com a foto do cliente        
        File imgFile = new File(this.controladorCadastro.getCliente().getCaminhoFoto());
        if (!imgFile.exists()) {
            // Nao localizou a foto para a matricula informada, entao carrega a imagem default                                        
            imgFile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath(CAMINHO_FOTO_DEFAULT));            
        }
        
        // Converte o arquivo em um array de bytes
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] fotoCliente = null;
        try {            
            BufferedImage imagem = ImageIO.read(imgFile);
            ImageIO.write(imagem, "PNG", bos);
            bos.flush();  
            fotoCliente = bos.toByteArray();                
        } catch (IOException e) {            
        }        
        
        try {
            return new DefaultStreamedContent(new ByteArrayInputStream(fotoCliente));
        } catch(NullPointerException e) {
            // Nao foi possivel localizar nenhuma foto ...
            return new DefaultStreamedContent();
        }        
    }    
    
    private void enviarMensagem(FacesMessage.Severity sev, String msg) {
        FacesContext context = FacesContext.getCurrentInstance();        
        context.addMessage(null, new FacesMessage(sev, msg, ""));
    }   
}

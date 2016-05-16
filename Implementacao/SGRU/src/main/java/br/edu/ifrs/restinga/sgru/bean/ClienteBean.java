/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException;
import br.edu.ifrs.restinga.sgru.modelo.Cliente;
import br.edu.ifrs.restinga.sgru.persistencia.ClienteDAO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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
public class ClienteBean {
    private Cliente cliente = new Cliente();    
    // Caso nao consiga carregar foto do cliente
    private static final String CAMINHO_FOTO_DEFAULT = "/imagens/semFoto.png";           
    
    /**
     * @return the cliente
     */
    public Cliente getCliente() {        
        return cliente;        
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;        
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

    /*
    /**
     * Solicita à camada de persistência o cadastro de um aluno
     */    
    public void salvar() {
        ClienteDAO dao = new ClienteDAO();
        dao.salvar(cliente);
        enviarMensagem(FacesMessage.SEVERITY_INFO, "Cliente cadastrado com sucesso!");
    }     
        
    /**
     * Carrega um cliente no Bean
     * @param matricula A matrícula do cliente a ser carregado
     */
    public void carregar(String matricula) {
        // Carrega dados do cliente
        ClienteDAO dao = new ClienteDAO();                
        try {
            cliente = dao.carregar(matricula);
        } catch(MatriculaInvalidaException e) {
            // essa excessao serah tratada no realizarVendaAlmocoCartao do CaixaRUBean
        }
    }
    
    /**
     * Verifica se existe um cliente setado na sessao para realizar a compra. 
     * Caso não exista, redireciona o operador para a tela do caixa
     */
    public void isClienteSet() {
        // Se o cliente nao possui nome setado, entao nao ha cliente na sessao        
        if (cliente.getNome() == null) {
            // redireciona para tela do caixa
            ConfigurableNavigationHandler nav  = 
                    (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
            
            nav.performNavigation("caixa.xhtml?faces-redirect=true");                                
        }
    }
    
    /**
     * Converte uma imagem para apresentar em um componente p:graphicImage     
     * @return Um objeto StreamedContent
     */
    public StreamedContent converterFoto() {
        // Carrega o cliente da sessao
        FacesContext context = FacesContext.getCurrentInstance();
        CaixaRUBean caixaRUBean = (CaixaRUBean) context.getExternalContext().getSessionMap().get("caixaRUBean");                
        cliente = caixaRUBean.getControlador().getCaixaRU().ultimoAlmocoVendido().getCartao().getCliente();
        
        // Cria um objeto File com a foto do cliente        
        File imgFile = new File(cliente.getCaminhoFoto());
        if (!imgFile.exists()) {
            // Nao localizou a foto para a matricula informada, entao carrega a imagem default                            
            String realPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(CAMINHO_FOTO_DEFAULT);
            // Cria um objeto File com a foto default
            imgFile = new File(realPath);            
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

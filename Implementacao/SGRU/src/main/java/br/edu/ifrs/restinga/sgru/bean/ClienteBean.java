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
    private final ClienteDAO dao = new ClienteDAO();
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
     * Solicita à camada de persistência o cadastro de um aluno
     */
    public void salvar() {
        dao.salvar(cliente);
        enviarMensagem(FacesMessage.SEVERITY_INFO, "Cliente cadastrado com sucesso!");
    }    
    
    /**
     * Solicita a pesquisa de um cliente para a camada de persistência
     * @param matricula A matricula do cliente a ser pesquisada
     * @throws br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException Se a matrícula não seja localizada
     */
    public void carregar(String matricula) throws MatriculaInvalidaException {
        dao.carregar(matricula);                        
    }
    
    /**
     * Retorna uma imagem para apresentar em um componente p:graphicImage
     * @return Um objeto StreamedContent, que pode ser apresentado em um componente p:graphicImage
     */
    public StreamedContent getFoto() {                        
        FacesContext context = FacesContext.getCurrentInstance();        
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } 
                
        // So, browser is requesting the image. Return a real StreamedContent with the image bytes.        
        // Pega o cliente da sessao
        CaixaRUBean caixaRUBean = (CaixaRUBean) context.getExternalContext().getSessionMap().get("caixaRUBean");                
        cliente = caixaRUBean.getCaixaRU().ultimoAlmocoVendido().getCartao().getCliente();
                
        // A foto convertida para o componente p:graphicImage
        byte[] foto = converterFoto(cliente.getCaminhoFoto());                
        return new DefaultStreamedContent(new ByteArrayInputStream(foto));                    
    }

    /**
     * Converte um arquivo de imagem em um array de bytes     
     * @param caminhoFoto A localização do arquivo de imagem no computador     
     * @return Um array de bytes com a representação da foto    
     */
    public byte[] converterFoto(String caminhoFoto) {                                                        
        byte[] retorno = null;
        // Carrega a imagem para um array de bytes no atributo foto        
        File imgFile = new File(caminhoFoto);
        if (!imgFile.exists()) {
            // Nao localizou a foto para a matricula informada, entao carrega a imagem default                
            // Pega o diretorio onde estah localizada a imagem default
            String realPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(CAMINHO_FOTO_DEFAULT);
            imgFile = new File(realPath);            
        }
        
        // Converte o arquivo em um array de bytes
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {            
            BufferedImage imagem = ImageIO.read(imgFile);
            ImageIO.write(imagem, "PNG", bos);
            bos.flush();  
            retorno = bos.toByteArray();                
        } catch (IOException e) {            
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

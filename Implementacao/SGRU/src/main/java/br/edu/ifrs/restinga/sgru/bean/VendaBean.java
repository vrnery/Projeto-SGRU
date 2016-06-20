/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.excessao.FotoNaoEncontradaException;
import br.edu.ifrs.restinga.sgru.excessao.ValorAlmocoInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException;
import br.edu.ifrs.restinga.sgru.excessao.PeriodoEntreAlmocosInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.SaldoInsuficienteException;
import br.edu.ifrs.restinga.sgru.excessao.TicketInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.ValorAberturaCaixaInvalido;
import br.edu.ifrs.restinga.sgru.excessao.ValorRecargaInvalidoException;
import br.edu.ifrs.restinga.sgru.modelo.ControladorVenda;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author marcelo.lima
 */
@ManagedBean
@SessionScoped
public class VendaBean {
    // Caso nao consiga carregar foto do cliente
    private static final String CAMINHO_FOTO_DEFAULT = "/imagens/fotos/semFoto.png";            
    private final ControladorVenda controlador;    

    public VendaBean() {        
        this.controlador = new ControladorVenda();
    }           
    
    /**     
     * @return the controlador
     */
    public ControladorVenda getControlador() {
        return controlador;
    }
    
    public void getValorPagoAlmoco() {
        try {
            controlador.getValorPagoAlmoco();
        } catch (ArrayIndexOutOfBoundsException | ValorAlmocoInvalidoException ex) {
            enviarMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
    }

    /**
     * Instancia um objeto recarga com a data atual e seu valor atualizado como false
     */
    public void iniciarRecarga() {
        this.controlador.iniciarRecarga();
    }
    
    /**
     * Realiza uma recarga para um cartão
     * @return A próxima página a ser exibida para o usuário
     */
    public String realizarRecarga() {
        try {
            this.controlador.realizarRecarga();
            //enviarMensagem(FacesMessage.SEVERITY_INFO, "Recarga realizada com sucesso!");
        } catch (MatriculaInvalidaException | ValorRecargaInvalidoException ex) {
            enviarMensagem(FacesMessage.SEVERITY_ERROR, ex.getMessage());
            return null;
        }
        return null;
    }
    
    /**
     * Verifica se existe um caixa setado na sessao
     */
    public void isCaixaRUSet() {        
        // Caso nao haja operador de caixa setado, entao o caixa ainda estah fechado
        if (controlador.getCaixaRU().getFuncionario() == null) {
            // O caixa ainda nao esta aberto
            ConfigurableNavigationHandler nav  = 
                    (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();            
            nav.performNavigation("abrirCaixa.xhtml?faces-redirect=true");     
        }
    }

    /**
     * Verifica se existe um ticket setado na sessao
     */
    public void isTicketSet() {        
        boolean isSet = true;
        if (controlador.getCaixaRU().getLstVendaAlmoco().isEmpty()) {
            isSet = false;            
        } else if (controlador.getCaixaRU().ultimoAlmocoVendido().getTicket() == null) {
            isSet = false;
        }
        
        if (!isSet) {
            // O ticket nao estah setado
            ConfigurableNavigationHandler nav  = 
                    (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();            
            nav.performNavigation("caixa.xhtml?faces-redirect=true");     
        }
    }
            
    /**
     * Verifica se já existe um caixa aberto, que ainda não foi fechado, para o operador naquele dia     
     */
    public void carregarCaixaAberto() {
        String proximaPagina = controlador.carregarCaixaAberto();
        if (proximaPagina != null) {                                   
            // Caixa jah aberto, redireciona para a pagina do caixa
            ConfigurableNavigationHandler nav  = 
                    (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
            
            nav.performNavigation(proximaPagina + ".xhtml?faces-redirect=true");     
        }
    }     
    
    /**
     * Carrega um caixa já aberto (com valor de fechamento zerado) ou abre um novo caixa     
     * @param valorAbertura O valor de abertura do caixa
     * @return A próxima página a ser exibida pelo usuário
     */
    public String realizarAberturaCaixa(double valorAbertura) {
        try {
            return controlador.realizarAberturaCaixa(valorAbertura);
        } catch (ValorAberturaCaixaInvalido e) {
            enviarMensagem(FacesMessage.SEVERITY_ERROR, "Valor inválido!");
            return null;
        }        
    }    
    
    /**
     * Realiza uma venda de almoco com cartao para o cliente
     * @param matricula A matricula do cliente que está realizando a compra
     * @return Retorna a próxima página a ser exibida para o operador     
     */
    public String realizarVendaAlmocoCartao(String matricula) {        
        try {
            controlador.realizarVendaAlmocoCartao(matricula);
        } catch(MatriculaInvalidaException | UsuarioInvalidoException |
                SaldoInsuficienteException | ValorAlmocoInvalidoException |
                PeriodoEntreAlmocosInvalidoException e) {            
            enviarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
            return "caixa";
        }
        
        // Verifica se o cliente possui foto
        try {
            controlador.verificarExistenciaFoto();
        } catch (FotoNaoEncontradaException e) {
            enviarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
        return "confirmarVendaCartao";
    }    
    
    /**
     * Realiza uma venda de almoço com ticket
     * @param codigo O código do ticket apresentado
     * @return  A próxima página a ser visualizada pelo operador de caixa
     */
    public String realizarVendaAlmocoTicket(int codigo) {
        String retorno = "confirmarVendaTicket";
        try {
            controlador.realizarVendaAlmocoTicket(codigo);
        } catch(TicketInvalidoException e) {
            enviarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
            retorno = "caixa";
        }
        return retorno;
    }        
    
    /**
     * Realiza a venda de um ticket para um cliente
     * @return A próxima página a ser visualizada pelo operador
     */
    public String realizarVendaTicket() {
        return null;
    }
    
    /**
     * Confirma ou não confirmar a a venda do almoço
     * @param confirmar True, para confirmar a venda, e false para não confirmar
     * @return A próxima página que o operador será redirecionado
     */
    public String finalizarAlmoco(boolean confirmar) {
        try {
            controlador.finalizarAlmoco(confirmar);
        } catch (ValorAlmocoInvalidoException e) {
            enviarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
            return "venda";
        }
        return "caixa";
    }
    
    /**
     * Realiza o fechamento de caixa. Esse método já persiste os 
     * dados do fechamento na base de dados
     * @return A página para onde o operador será redirecionado
     */
    public String realizarFechamentoCaixa() {
        try {            
            controlador.getCaixaRU().realizarFechamentoCaixa();
        } catch (ValorAlmocoInvalidoException e) {
            enviarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
            return null;
        }              
        return "index";
    }    
    
    /**
     * Carrega um cliente no controlador
     * @param matricula A matrícula do cliente a ser carregado
     */    
    public void carregarCliente(String matricula) {
        try {
            controlador.carregarCliente(matricula);
        } catch(MatriculaInvalidaException e) {
            // essa excessao serah tratada no realizarVendaAlmocoCartao do VendaBean
        }
    }    
    
/**
     * Verifica se existe um cliente setado na sessao para realizar a compra. 
     * Caso não exista, redireciona o operador para a tela do caixa
     */
    public void isClienteSet() {
        // Se cliente nao setado, entao nao ha cliente na sessao        
        if (controlador.getCliente() == null) {
            // redireciona para tela do caixa
            ConfigurableNavigationHandler nav  = 
                    (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
            
            nav.performNavigation("caixa.xhtml?faces-redirect=true");                                
        }
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
        File imgFile = new File(this.controlador.getCliente().getCaminhoFoto());
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

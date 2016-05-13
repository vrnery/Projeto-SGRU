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
import br.edu.ifrs.restinga.sgru.modelo.CaixaRU;
import br.edu.ifrs.restinga.sgru.modelo.ControladorVenda;
import br.edu.ifrs.restinga.sgru.modelo.OperadorCaixa;
import javax.faces.context.FacesContext;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author marcelo.lima
 */
@ManagedBean
@SessionScoped
public class CaixaRUBean {
    private final ControladorVenda controlador;    

    public CaixaRUBean() {        
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
            enviarMensagem(FacesMessage.SEVERITY_INFO, ex.getMessage());
        }
    }
    
    /**
     * Verifica se existe um caixa setado na sessao
     */
    public void isCaixaRUSet() {        
        // Caso nao haja operador de caixa setado, entao o caixa ainda estah fechado
        if (controlador.getCaixaRU().getOperadorCaixa() == null) {
            // O caixa ainda nao esta aberto
            ConfigurableNavigationHandler nav  = 
                    (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();            
            nav.performNavigation("abrirCaixa.xhtml?faces-redirect=true");     
        }
    }
            
    /**
     * Verifica se já existe um caixa aberto, que ainda não foi fechado, para o operador naquele dia
     * @param oper O operador de caixa
     */
    public void carregarCaixaAberto(OperadorCaixa oper) {
        if (controlador.carregarCaixaAberto(oper)) {                                   
            // Caixa jah aberto, redireciona para a pagina do caixa
            ConfigurableNavigationHandler nav  = 
                    (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
            
            nav.performNavigation("caixa.xhtml?faces-redirect=true");     
        } else {
            // garante que haverah um caixaRU para setar as propriedades
            // na abertura de caixa
            controlador.setCaixaRU(new CaixaRU());
        }
    }     
    
    /**
     * Carrega um caixa já aberto (com valor de fechamento zerado) ou abre um novo caixa
     * @param oper O operador que vai operar o caixa
     * @param valorAbertura O valor de abertura do caixa
     * @return A próxima página a ser exibida pelo usuário
     */
    public String realizarAberturaCaixa(OperadorCaixa oper, double valorAbertura) {
        try {
            controlador.realizarAberturaCaixa(oper, valorAbertura);
        } catch (ValorAberturaCaixaInvalido e) {
            enviarMensagem(FacesMessage.SEVERITY_INFO, "Valor inválido!");
            return null;
        }
        return "caixa";
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
            enviarMensagem(FacesMessage.SEVERITY_INFO, e.getMessage());
            return "caixa";
        }
        
        // Verifica se o cliente possui foto
        try {
            controlador.verificarExistenciaFoto();
        } catch (FotoNaoEncontradaException e) {
            enviarMensagem(FacesMessage.SEVERITY_INFO, e.getMessage());
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
            enviarMensagem(FacesMessage.SEVERITY_INFO, e.getMessage());
            retorno = "caixa";
        }
        return retorno;
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
            enviarMensagem(FacesMessage.SEVERITY_INFO, e.getMessage());
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
            enviarMensagem(FacesMessage.SEVERITY_INFO, e.getMessage());
            return null;
        }              
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.excessao.DataRelatorioInvalidaException;
import br.edu.ifrs.restinga.sgru.excessao.RelatorioException;
import br.edu.ifrs.restinga.sgru.modelo.Cliente;
import br.edu.ifrs.restinga.sgru.modelo.TipoCliente;
import br.edu.ifrs.restinga.sgru.modelo.ControladorRelatorio;
import br.edu.ifrs.restinga.sgru.modelo.Pessoa;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author marcelo.lima
 */
@ManagedBean
@ViewScoped
public class RelatorioBean {
    public static final int RELATORIO_COMPRAS = 1;
    public static final int RELATORIO_RECARGAS = 2;
    public static final int FORMA_PGTO_CARTAO = 1;
    public static final int FORMA_PGTO_TICKET = 2;
    
    private ControladorRelatorio controlador;
    private Date dataInicialMin;
    private Date dataInicialMax;
    private Date dataFinalMin;
    private Date dataFinalMax;
    private boolean relatorioCompras;    
    
    public RelatorioBean() {
        this. controlador = new ControladorRelatorio();
        // A data inicial maxima eh a data atual
        this.dataInicialMax = new Date();
        // A data final minima eh a data atual
        this.dataFinalMin = new Date();
        // O tipo de relatorio default eh cartao
        controlador.setTipoRelatorio(RELATORIO_COMPRAS);
        // A forma de pagamento default eh cartao
        controlador.setFormaPgto(FORMA_PGTO_CARTAO);        
        // Flag para controlar a ativacao do campo forma de pagamento
        this.relatorioCompras = true;
    }

    /**
     * @return the RELATORIO_COMPRAS
     */
    public int getRELATORIO_COMPRAS() {
        return RELATORIO_COMPRAS;
    }

    /**
     * @return the RELATORIO_RECARGAS
     */
    public int getRELATORIO_RECARGAS() {
        return RELATORIO_RECARGAS;
    }

    /**
     * @return the FORMA_PGTO_CARTAO
     */
    public int getFORMA_PGTO_CARTAO() {
        return FORMA_PGTO_CARTAO;
    }

    /**
     * @return the FORMA_PGTO_TICKET
     */
    public int getFORMA_PGTO_TICKET() {
        return FORMA_PGTO_TICKET;
    }    
    
   /**
     * @return the controlador
     */
    public ControladorRelatorio getControlador() {
        return controlador;
    }

    /**
     * @param controlador the controlador to set
     */
    public void setControlador(ControladorRelatorio controlador) {
        this.controlador = controlador;
    }    
    
    /**
     * @return the dataInicialMin
     */
    public Date getDataInicialMin() {
        return dataInicialMin;
    }

    /**
     * @return the dataInicialMax
     */
    public Date getDataInicialMax() {
        return dataInicialMax;
    }

    /**
     * @return the dataFinalMin
     */
    public Date getDataFinalMin() {
        return dataFinalMin;
    }

    /**
     * @return the dataFinalMax
     */
    public Date getDataFinalMax() {
        return dataFinalMax;
    }
    
    /**
     * @return the relatorioCompras
     */
    public boolean isRelatorioCompras() {
        return relatorioCompras;
    }

    /**
     * @param relatorioCompras the relatorioCompras to set
     */
    public void setRelatorioCompras(boolean relatorioCompras) {
        this.relatorioCompras = relatorioCompras;
    }    

    /**
     * Adapta o componente calendar dataFinal da view
     * @param evento 
     */
    public void tratarDataInicial(SelectEvent evento) {
        Date data = (Date) evento.getObject();
        this.dataFinalMin = data;
    }
    
    /**
     * Adapta o componente calendar dataInicial da view
     * @param evento 
     */
    public void tratarDataFinal(SelectEvent evento) {
        Date data = (Date) evento.getObject();
        this.dataInicialMax = data;
    }
    
    /**
     * Habilita ou não um componente na view conforme o tipo de usuário
     * @param pessoa O usuário logado no sistema
     * @return True, se for necessário habilitar o componente e false, caso contrário
     */
    public boolean habilitarComponentesGerenciais(Pessoa pessoa) {        
        return !(pessoa instanceof Cliente);
    }    
    
    /**
     * Retorna uma lista de Tipo de Clientes cadastrados no sistema
     * @return 
     */
    public List<TipoCliente> getLstTipoCliente() {        
        return controlador.getLstTipoCliente();
        
    }
    
    /**
     * Retorna uma lista de cliente do tipo desejado
     * @return Uma lista de objetos Cliente
     */
    public List<Cliente> getLstClientes() {        
        return controlador.getLstClientes();
    }    
    
    /**
     * Altera o status do atributo relatorioCompras     
     */
    public void alterarStatusFormaPgto() {
        this.relatorioCompras = !this.relatorioCompras;
    }
    
    public String emitirRelatorio() {
        String retorno = "resultado";
        try {
            if (relatorioCompras) {
                this.controlador.emitirRelatorioCompras();
            } else {
                
            }
        } catch (DataRelatorioInvalidaException | RelatorioException e) {
            enviarMensagem(FacesMessage.SEVERITY_INFO, e.getMessage());
            return null;
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

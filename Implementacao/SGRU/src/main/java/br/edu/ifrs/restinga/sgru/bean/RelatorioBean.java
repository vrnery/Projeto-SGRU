/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.modelo.Cliente;
import br.edu.ifrs.restinga.sgru.modelo.TipoCliente;
import br.edu.ifrs.restinga.sgru.modelo.ControladorRelatorio;
import br.edu.ifrs.restinga.sgru.modelo.Pessoa;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author marcelo.lima
 */
@ManagedBean
@ViewScoped
public class RelatorioBean {
    private final int RELATORIO_COMPRAS = 1;
    private final int RELATORIO_RECARGAS = 2;
    private final int FORMA_PGTO_CARTAO = 1;
    private final int FORMA_PGTO_TICKET = 2;
    
    private ControladorRelatorio controlador;
    private Date dataInicialMin;
    private Date dataInicialMax;
    private Date dataFinalMin;
    private Date dataFinalMax;
    private boolean relatorioCompras;
    private int idTipoCliente;    
    
    public RelatorioBean() {
        this. controlador = new ControladorRelatorio();
        // A data inicial maxima eh a data atual
        this.dataInicialMax = new Date();
        // A data final minima eh a data atual
        this.dataFinalMin = new Date();
        // O tipo de relatorio default eh cartao
        controlador.setTipoRelatorio(this.RELATORIO_COMPRAS);
        // A forma de pagamento default eh cartao
        controlador.setFormaPgto(this.FORMA_PGTO_CARTAO);        
        // Flag para controlar a ativacao do campo forma de pagamento
        this.relatorioCompras = true;
        // -1 para todos os clientes
        this.idTipoCliente = -1;
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
     * @return the idTipoCliente
     */
    public int getIdTipoCliente() {
        return idTipoCliente;
    }

    /**
     * @param idTipoCliente the idTipoCliente to set
     */
    public void setIdTipoCliente(int idTipoCliente) {
        this.idTipoCliente = idTipoCliente;
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
        if (pessoa instanceof Cliente) {
            return false;
        }        
        return true;
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
        return controlador.getLstClientes(this.idTipoCliente);
    }    
    
    /**
     * Altera o status do atributo relatorioCompras     
     */
    public void alterarStatusFormaPgto() {
        this.relatorioCompras = !this.relatorioCompras;
    }   
}

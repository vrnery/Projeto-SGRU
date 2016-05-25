/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.modelo.Cliente;
import br.edu.ifrs.restinga.sgru.modelo.CodTipoCliente;
import br.edu.ifrs.restinga.sgru.modelo.Pessoa;
import br.edu.ifrs.restinga.sgru.persistencia.ClienteDAO;
import br.edu.ifrs.restinga.sgru.persistencia.CodTipoClienteDAO;
import br.edu.ifrs.restinga.sgru.persistencia.RelatoriosDAO;
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
    private Date dataInicialMin;
    private Date dataInicialMax;
    private Date dataFinalMin;
    private Date dataFinalMax;
    private boolean relatorioCompras;
    private int idCodTipoCliente;
    RelatoriosDAO rel = new RelatoriosDAO();
    
    public RelatorioBean() {
        this.dataInicialMax = new Date();
        this.dataFinalMin = new Date();
        // A forma de pagamento default eh cartao
        this.relatorioCompras = true;
        // -1 para todos os clientes
        this.idCodTipoCliente = -1;
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
     * @return the idCodTipoCliente
     */
    public int getIdCodTipoCliente() {
        return idCodTipoCliente;
    }

    /**
     * @param idCodTipoCliente the idCodTipoCliente to set
     */
    public void setIdCodTipoCliente(int idCodTipoCliente) {
        this.idCodTipoCliente = idCodTipoCliente;
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
        /*
        if (pessoa instanceof Cliente) {
            return false;
        }
        */
        return true;
    }
    public void EmitirRelatorio(){
        rel.relatorio(relatorioCompras);
    }
    
    /**
     * Retorna uma lista de Tipo de Clientes cadastrados no sistema
     * @return 
     */
    public List<CodTipoCliente> getLstTipoCliente() {
        CodTipoClienteDAO dao = new CodTipoClienteDAO();
        return dao.getLstTipoClientes();
        
    }
    
    /**
     * Retorna uma lista de cliente do tipo desejado
     * @return Uma lista de objetos Cliente
     */
    public List<Cliente> getLstClientes() {
        ClienteDAO dao = new ClienteDAO();
        return dao.carregarClientesPorTipo(this.idCodTipoCliente);
    }    
    
    /**
     * Altera o status do atributo relatorioCompras     
     */
    public void alterarStatusFormaPgto() {
        this.relatorioCompras = !this.relatorioCompras;
    }   
}
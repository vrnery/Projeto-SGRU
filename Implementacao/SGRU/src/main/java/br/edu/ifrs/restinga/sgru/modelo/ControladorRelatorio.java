/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.bean.RelatorioBean;
import br.edu.ifrs.restinga.sgru.excessao.DataRelatorioInvalidaException;
import br.edu.ifrs.restinga.sgru.excessao.RelatorioException;
import br.edu.ifrs.restinga.sgru.persistencia.ClienteDAO;
import br.edu.ifrs.restinga.sgru.persistencia.RelatoriosDAO;
import br.edu.ifrs.restinga.sgru.persistencia.TipoClienteDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author marcelo.lima
 */
public class ControladorRelatorio {
    private int tipoRelatorio;
    private int formaPgto;
    private String tipoCliente;
    private int idCliente;
    private Date dataInicial;
    private Date dataFinal;

    public ControladorRelatorio() {
        // Todos os tipos de clientes
        this.tipoCliente = "-1";
    }
    
    /**
     * @return the tipoRelatorio
     */
    public int getTipoRelatorio() {
        return tipoRelatorio;
    }

    /**
     * @param tipoRelatorio the tipoRelatorio to set
     */
    public void setTipoRelatorio(int tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    /**
     * @return the formaPgto
     */
    public int getFormaPgto() {
        return formaPgto;
    }

    /**
     * @param formaPgto the formaPgto to set
     */
    public void setFormaPgto(int formaPgto) {
        this.formaPgto = formaPgto;
    }

    /**
     * @return the tipoCliente
     */
    public String getTipoCliente() {
        return tipoCliente;
    }

    /**
     * @param tipoCliente the tipoCliente to set
     */
    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }    
    
    /**
     * @return the idCliente
     */
    public int getCliente() {
        return idCliente;
    }

    /**
     * @param cliente the idCliente to set
     */
    public void setCliente(int cliente) {
        this.idCliente = cliente;
    }

    /**
     * @return the dataInicial
     */
    public Date getDataInicial() {
        return dataInicial;
    }

    /**
     * @param dataInicial the dataInicial to set
     */
    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    /**
     * @return the dataFinal
     */
    public Date getDataFinal() {
        return dataFinal;
    }

    /**
     * @param dataFinal the dataFinal to set
     */
    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }
    
    /**
     * Retorna uma lista de Tipo de Tipos de Clientes cadastrados no sistema
     * @return Uma lista TipoCliente
     */
    public List<TipoCliente> getLstTipoCliente() {
        TipoClienteDAO dao = new TipoClienteDAO();
        return dao.getLstTipoClientes();
        
    }
    
    /**
     * Retorna uma lista de idCliente do tipo desejado     
     * @return Uma lista de objetos Cliente
     */
    public List<Cliente> getLstClientes() {
        ClienteDAO dao = new ClienteDAO();
        return dao.carregarClientesPorTipo(this.tipoCliente);
    }        
    
    public List<VendaAlmoco> emitirRelatorioCompras() 
            throws DataRelatorioInvalidaException, RelatorioException {        
        // Verifica se periodo informado eh valido
        if (dataInicial.after(dataFinal)) {
            throw new DataRelatorioInvalidaException("Período inválido!");
        }
                
        // Lista com o retorno da pesquisa
        List<VendaAlmoco> lstVendaAlmoco = new ArrayList();
        RelatoriosDAO daoRelatorios = new RelatoriosDAO();
                
        if (this.formaPgto == RelatorioBean.FORMA_PGTO_CARTAO) {            
            if (this.tipoCliente.equals("-1")) {
                // todos os tipos de clientes
                lstVendaAlmoco = daoRelatorios.relatorioComprasCartao(this.dataInicial, this.dataFinal);
            } else {
                // Relatorio de um tipo especifico de idCliente
                lstVendaAlmoco = daoRelatorios.relatorioComprasCartao(this.dataInicial, this.dataFinal, this.tipoCliente);
            }
        } else if (this.formaPgto == RelatorioBean.FORMA_PGTO_TICKET) {
            lstVendaAlmoco = daoRelatorios.relatorioComprasTicket(this.dataInicial, this.dataFinal);
        } else {
            // todas as forma de pagamento
            if (this.tipoCliente.equals(Cliente.ALUNO)) {
                
            } else if (this.tipoCliente.equals(Cliente.PROFESSOR)) {
                
            } else {
                // todos os tipos de clientes
            }
        }
        return lstVendaAlmoco;
    }    
}

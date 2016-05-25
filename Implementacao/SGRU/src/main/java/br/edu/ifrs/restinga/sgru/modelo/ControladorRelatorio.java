/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.persistencia.ClienteDAO;
import br.edu.ifrs.restinga.sgru.persistencia.TipoClienteDAO;
import java.util.Date;
import java.util.List;

/**
 *
 * @author marcelo.lima
 */
public class ControladorRelatorio {
    private int tipoRelatorio;
    private int formaPgto;
    private Cliente cliente;
    private Date dataInicial;
    private Date dataFinal;

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
     * Retorna uma lista de cliente do tipo desejado
     * @param idTipoCliente O id do tipo de cliente
     * @return Uma lista de objetos Cliente
     */
    public List<Cliente> getLstClientes(int idTipoCliente) {
        ClienteDAO dao = new ClienteDAO();
        return dao.carregarClientesPorTipo(idTipoCliente);
    }        
}

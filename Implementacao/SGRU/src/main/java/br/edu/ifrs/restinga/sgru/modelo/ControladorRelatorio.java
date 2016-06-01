/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.bean.RelatorioBean;
import br.edu.ifrs.restinga.sgru.excessao.DataRelatorioInvalidaException;
import br.edu.ifrs.restinga.sgru.excessao.RecargaNaoEncontradaException;
import br.edu.ifrs.restinga.sgru.excessao.RelatorioException;
import br.edu.ifrs.restinga.sgru.persistencia.ClienteDAO;
import br.edu.ifrs.restinga.sgru.persistencia.PessoaDAO;
import br.edu.ifrs.restinga.sgru.persistencia.RelatoriosDAO;
import br.edu.ifrs.restinga.sgru.persistencia.TipoClienteDAO;
import java.util.Calendar;
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
    private List<VendaAlmoco> lstVendaAlmoco;
    private List<Recarga> lstRecargas;

    public ControladorRelatorio() {
        // Todos os tipos de clientes
        this.tipoCliente = "-1";
        // Todos os clientes
        this.idCliente = -1;
        // Inicialmentes as listas sao nulas
        this.lstVendaAlmoco = null;
        this.lstRecargas = null;
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
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * @param idCliente the idCliente to set
     */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
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
     * @return the lstVendaAlmoco
     */
    public List<VendaAlmoco> getLstVendaAlmoco() {
        return lstVendaAlmoco;
    }

    /**
     * @return the lstRecargas
     */
    public List<Recarga> getLstRecargas() {
        return lstRecargas;
    }

    /**
     * Retorna uma lista de Tipo de Tipos de Clientes cadastrados no sistema
     * @return Uma lista TipoCliente
     */
    public List<TipoCliente> getLstTipoCliente() {
        TipoClienteDAO daoTipoCliente = new TipoClienteDAO();
        return daoTipoCliente.getLstTipoClientes();
        
    }
    
    /**
     * Retorna uma lista de idCliente do tipo desejado     
     * @return Uma lista de objetos Cliente
     */
    public List<Cliente> getLstClientes() {
        ClienteDAO daoCliente = new ClienteDAO();
        return daoCliente.carregarClientesPorTipo(this.tipoCliente);
    }        
    
    /**
     * Verifica se o usuário é um cliente
     * @param idUsuario O id do usuário a ser verificado
     * @return True, se for cliente e false, caso contrário
     */
    public boolean isCliente(int idUsuario) {
        PessoaDAO daoPessoa = new PessoaDAO();
        return daoPessoa.carregar(idUsuario) instanceof Cliente;
    }
    
    /**
     * Busca os dados do relatório solicitado
     * @param idUsuarioLogado O id do usuário logado no sistema
     * @return Uma lista de objetos VendaAlmoco
     * @throws DataRelatorioInvalidaException Caso o período informado seja inválido
     * @throws RelatorioException Caso não sejam encontradas compras com os parâmetros solicitados
     */
    public List<VendaAlmoco> buscarDadosRelatorioCompras(int idUsuarioLogado) 
            throws DataRelatorioInvalidaException, RelatorioException {        
        // Verifica se periodo informado eh valido
        if (dataInicial.after(dataFinal)) {
            throw new DataRelatorioInvalidaException("Período inválido!");
        }
        
        Calendar cDataInicial = Calendar.getInstance();
        Calendar cDataFinal = Calendar.getInstance();
        
        cDataInicial.setTime(this.dataInicial);
        cDataFinal.setTime(this.dataFinal);
                
        RelatoriosDAO daoRelatorios = new RelatoriosDAO();                
        if (isCliente(idUsuarioLogado) || (this.idCliente != -1)) {
            // O cliente solicitou um relatorio, logo trata-se de um relatório para um usuario especifico,
            // ou o gerente solicitou o relatório
            lstVendaAlmoco = daoRelatorios.relatorioComprasCartao(cDataInicial, cDataFinal, 
                    this.idCliente!=-1?this.idCliente:idUsuarioLogado);
        } else {
            // Relatorios solicitados pelo gerente
            if (this.formaPgto == RelatorioBean.FORMA_PGTO_CARTAO) {            
                if (this.tipoCliente.equals("-1")) {
                    // todos os tipos de clientes
                    lstVendaAlmoco = daoRelatorios.relatorioComprasCartao(cDataInicial, cDataFinal);                
                } else {
                    // Relatorio de um tipo especifico de cliente
                    lstVendaAlmoco = daoRelatorios.relatorioComprasCartao(cDataInicial, cDataFinal, this.tipoCliente);
                }
            } else if (this.formaPgto == RelatorioBean.FORMA_PGTO_TICKET) {
                lstVendaAlmoco = daoRelatorios.relatorioComprasTicket(cDataInicial, cDataFinal);
            }
        }
        return lstVendaAlmoco;
    }    
    
    /**
     * Busca os dados do relatório solicitado
     * @param idUsuarioLogado O id do usuário logado no sistema
     * @return Uma lista de objetos Recarga
     * @throws DataRelatorioInvalidaException Caso o período informado seja inválido
     * @throws RecargaNaoEncontradaException Caso não sejam encontradas recargas com os parâmetros solicitados
     */
    public List<Recarga> buscarDadosRelatorioRecargas(int idUsuarioLogado) 
            throws DataRelatorioInvalidaException, RecargaNaoEncontradaException {        
        // Verifica se periodo informado eh valido
        if (dataInicial.after(dataFinal)) {
            throw new DataRelatorioInvalidaException("Período inválido!");
        }
        
        Calendar cDataInicial = Calendar.getInstance();
        Calendar cDataFinal = Calendar.getInstance();
        
        cDataInicial.setTime(this.dataInicial);
        cDataFinal.setTime(this.dataFinal);
                
        RelatoriosDAO daoRelatorios = new RelatoriosDAO();                
        if (isCliente(idUsuarioLogado) || (this.idCliente != -1)) {
            // O cliente solicitou um relatorio, logo trata-se de um relatório para um usuario especifico,
            // ou o gerente solicitou o relatório
            lstRecargas = daoRelatorios.relatorioRecargas(cDataInicial, cDataFinal, 
                    this.idCliente!=-1?this.idCliente:idUsuarioLogado);
        } else {
            // Relatorios solicitados pelo gerente
            if (this.tipoCliente.equals("-1")) {
                // todos os tipos de clientes
                lstRecargas = daoRelatorios.relatorioRecargas(cDataInicial, cDataFinal);                
            } else {
                // Relatorio de um tipo especifico de cliente
                lstRecargas = daoRelatorios.relatorioRecargas(cDataInicial, cDataFinal, this.tipoCliente);
            }
        }
        return lstRecargas;
    }           
}

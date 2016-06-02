/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.excessao.RecargaNaoEncontradaException;
import br.edu.ifrs.restinga.sgru.excessao.RelatorioException;
import br.edu.ifrs.restinga.sgru.modelo.Recarga;
import br.edu.ifrs.restinga.sgru.modelo.VendaAlmoco;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author 10070187
 */
public class RelatoriosDAO {
    private final Session sessao;

    public RelatoriosDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();        
    }
    
    /**
     * Lista as compras realizadas com cartão em um determinado período
     * @param dataInicial A data inicial do período desejado
     * @param dataFinal A data final do período desejado
     * @return Uma lista de objetos VendaAlmoco
     * @throws br.edu.ifrs.restinga.sgru.excessao.RelatorioException Caso não sejam encontrados dados para o relatório
     */
    public List<VendaAlmoco> gerarRelatorioComprasCartao(Calendar dataInicial, Calendar dataFinal) 
            throws RelatorioException {
        
        List<VendaAlmoco> lstVendaAlmoco = sessao.createCriteria(VendaAlmoco.class)
                .add(Restrictions.isNotNull("cartao"))
                .add(Restrictions.between("dataVenda", dataInicial, dataFinal))
                .addOrder(Order.asc("dataVenda"))
                .list();        
                
        if (lstVendaAlmoco.isEmpty()) {
            throw new RelatorioException("Não foram encontradas compras no período informado");
        }
        return lstVendaAlmoco;
    }
    
    /**
     * Lista as compras realizadas com cartão por um determinado tipo de cliente em um determinado período
     * @param dataInicial A data inicial do período desejado
     * @param dataFinal A data final do período desejado
     * @param codTipoCliente O código do tipo de cliente pesquisado (Cliente.ALUNO e Cliente.PROFESSOR)
     * @return Uma lista de objetos VendaAlmoco
     * @throws br.edu.ifrs.restinga.sgru.excessao.RelatorioException Caso não sejam encontrados dados para o relatório
     */
    public List<VendaAlmoco> gerarRelatorioComprasCartao(Calendar dataInicial, Calendar dataFinal, String codTipoCliente) 
            throws RelatorioException {
        
        List<VendaAlmoco> lstVendaAlmoco = sessao.createCriteria(VendaAlmoco.class, "venda")
                .add(Restrictions.isNotNull("venda.cartao"))
                .add(Restrictions.between("venda.dataVenda", dataInicial, dataFinal))
                .createAlias("cartao", "cartao")
                .createAlias("cartao.cliente", "cliente")
                .createAlias("cliente.tipoCliente", "tipocliente")                                
                .add(Restrictions.eq("tipocliente.codigo", codTipoCliente))
                .addOrder(Order.asc("venda.dataVenda"))
                .list();
        
        if (lstVendaAlmoco.isEmpty()) {
            throw new RelatorioException("Não foram encontradas compras no período informado");
        }
        return lstVendaAlmoco;
    }
    
    /**
     * Lista as compras realizadas com cartão por um determinado cliente em um determinado período
     * @param dataInicial A data inicial do período desejado
     * @param dataFinal A data final do período desejado     
     * @param idCliente O id do cliente a ser pesquisado    
     * @return Uma lista de objetos VendaAlmoco
     * @throws br.edu.ifrs.restinga.sgru.excessao.RelatorioException Caso não sejam encontrados dados para o relatório
     */
    public List<VendaAlmoco> gerarRelatorioComprasCartao(Calendar dataInicial, Calendar dataFinal, int idCliente) 
            throws RelatorioException {
        
        List<VendaAlmoco> lstVendaAlmoco = sessao.createCriteria(VendaAlmoco.class, "venda")
                .add(Restrictions.isNotNull("venda.cartao"))
                .add(Restrictions.between("venda.dataVenda", dataInicial, dataFinal))
                .createAlias("cartao", "cartao")
                .createAlias("cartao.cliente", "cliente")                
                .add(Restrictions.eq("cliente.id", idCliente))
                .addOrder(Order.asc("venda.dataVenda"))
                .list();
        
        if (lstVendaAlmoco.isEmpty()) {
            throw new RelatorioException("Não foram encontradas compras no período informado");
        }
        return lstVendaAlmoco;
    }    
    
    /**
     * Lista as compras realizadas com ticket em um determinado período
     * @param dataInicial A data inicial do período desejado
     * @param dataFinal A data final do período desejado
     * @return Uma lista de objetos VendaAlmoco
     * @throws br.edu.ifrs.restinga.sgru.excessao.RelatorioException Caso não sejam encontrados dados para o relatório
     */
    public List<VendaAlmoco> gerarRelatorioComprasTicket(Calendar dataInicial, Calendar dataFinal) 
            throws RelatorioException {
                
        List<VendaAlmoco> lstVendaAlmoco = sessao.createCriteria(VendaAlmoco.class)
                .add(Restrictions.isNotNull("ticket"))
                .add(Restrictions.between("dataVenda", dataInicial, dataFinal))
                .addOrder(Order.asc("dataVenda"))
                .list();        

        if (lstVendaAlmoco.isEmpty()) {
            throw new RelatorioException("Não foram encontradas compras no período informado");
        }
        return lstVendaAlmoco;
    }    
    
    /**
     * Lista as recargas realizadas em um determinado período
     * @param dataInicial A data inicial do período desejado
     * @param dataFinal A data final do período desejado
     * @return Uma lista de objetos Recarga
     * @throws RecargaNaoEncontradaException Caso não sejam encontradas recargas
     */
    public List<Recarga> gerarRelatorioRecargas(Calendar dataInicial, Calendar dataFinal) 
            throws RecargaNaoEncontradaException {
        List<Recarga> lstRecargas = sessao.createCriteria(Recarga.class)
                .add(Restrictions.between("dataCredito", dataInicial, dataFinal))
                .add(Restrictions.eq("utilizado", false))
                .addOrder(Order.asc("dataCredito"))
                .list();
        
        if (lstRecargas.isEmpty()) {
            throw new RecargaNaoEncontradaException("Não foram encontradas recargas para o período informado");
        }
        return lstRecargas;
    }
    
    /**
     * Lista as recargas realizadas em um determinado período
     * @param dataInicial A data inicial do período desejado
     * @param dataFinal A data final do período desejado
     * @param codTipoCliente O código do tipo de cliente a ser pesquisado
     * @return Uma lista de objetos Recarga
     * @throws RecargaNaoEncontradaException Caso não sejam encontradas recargas
     */
    public List<Recarga> gerarRelatorioRecargas(Calendar dataInicial, Calendar dataFinal, String codTipoCliente) 
            throws RecargaNaoEncontradaException {        
        List<Recarga> lstRecargas = sessao.createCriteria(Recarga.class)
                .add(Restrictions.between("dataCredito", dataInicial, dataFinal))
                .add(Restrictions.eq("utilizado", false))
                .createAlias("cartao", "cartao")
                .createAlias("cartao.cliente", "cliente")
                .createAlias("cliente.tipoCliente", "tipocliente")
                .add(Restrictions.eq("tipocliente.codigo", codTipoCliente))
                .addOrder(Order.asc("dataCredito"))
                .list();
        
        if (lstRecargas.isEmpty()) {
            throw new RecargaNaoEncontradaException("Não foram encontradas recargas para o período informado");
        }
        return lstRecargas;
    }    
    
    /**
     * Lista as recargas realizadas em um determinado período
     * @param dataInicial A data inicial do período desejado
     * @param dataFinal A data final do período desejado
     * @param idCliente O código do cliente a ser pesquisado
     * @return Uma lista de objetos Recarga
     * @throws RecargaNaoEncontradaException Caso não sejam encontradas recargas
     */
    public List<Recarga> gerarRelatorioRecargas(Calendar dataInicial, Calendar dataFinal, int idCliente) 
            throws RecargaNaoEncontradaException {
        List<Recarga> lstRecargas = sessao.createCriteria(Recarga.class)
                .add(Restrictions.between("dataCredito", dataInicial, dataFinal))
                .add(Restrictions.eq("utilizado", false))
                .createAlias("cartao", "cartao")
                .createAlias("cartao.cliente", "cliente")                
                .add(Restrictions.eq("cliente.id", idCliente))
                .addOrder(Order.asc("dataCredito"))
                .list();
        
        if (lstRecargas.isEmpty()) {
            throw new RecargaNaoEncontradaException("Não foram encontradas recargas para o período informado");
        }
        return lstRecargas;
    }        
}

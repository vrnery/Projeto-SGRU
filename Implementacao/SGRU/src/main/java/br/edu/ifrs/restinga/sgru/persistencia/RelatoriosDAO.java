/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.excessao.RelatorioException;
import br.edu.ifrs.restinga.sgru.modelo.VendaAlmoco;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;

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
    public List<VendaAlmoco> relatorioComprasCartao(Date dataInicial, Date dataFinal) 
            throws RelatorioException {
        List<VendaAlmoco> lstVendaAlmoco = sessao.createQuery("FROM VendaAlmoco WHERE idCartao IS NOT NULL AND dataVenda BETWEEN :dataInicial AND :dataFinal")
                .setDate("dataInicial", dataInicial)
                .setDate("dataFinal", dataFinal)
                .list();
        
        if (lstVendaAlmoco == null) {
            throw new RelatorioException("Não foram encontrados compras no período informado");
        }
        return lstVendaAlmoco;
    }
    
    /**
     * Lista as compras realizadas com cartão por um determinado tipo de cliente em um determinado período
     * @param dataInicial A data inicial do período desejado
     * @param dataFinal A data final do período desejado
     * @param codCliente O código do tipo de cliente pesquisado (Cliente.ALUNO e Cliente.PROFESSOR)
     * @return Uma lista de objetos VendaAlmoco
     * @throws br.edu.ifrs.restinga.sgru.excessao.RelatorioException Caso não sejam encontrados dados para o relatório
     */
    public List<VendaAlmoco> relatorioComprasCartao(Date dataInicial, Date dataFinal, String codCliente) 
            throws RelatorioException {
        
        List<VendaAlmoco> lstVendaAlmoco = sessao.createQuery("FROM VendaAlmoco "
                + "INNER JOIN Cartao ON VendaAlmoco.idCartao = Cartao.id "
                + "INNER JOIN Cliente ON Cliente.idCartao = Cartao.id "
                + "INNER JOIN TipoCliente ON TipoCliente.id = Cliente.idTipoCliente "
                + "WHERE idCartao IS NOT NULL "
                + "AND dataVenda BETWEEN :dataInicial AND :dataFinal "
                + "AND TipoCliente.codigo = :codCliente")
                .setString("codCliente", codCliente)
                .setDate("dataInicial", dataInicial)
                .setDate("dataFinal", dataFinal)
                .list();
        
        if (lstVendaAlmoco == null) {
            throw new RelatorioException("Não foram encontrados compras no período informado");
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
    public List<VendaAlmoco> relatorioComprasTicket(Date dataInicial, Date dataFinal) 
            throws RelatorioException {
                
        List<VendaAlmoco> lstVendaAlmoco =  sessao.createQuery("FROM VendaAlmoco WHERE idTicket IS NOT NULL AND dataVenda BETWEEN :dataInicial AND :dataFinal")
                .setDate("dataInicial", dataInicial)
                .setDate("dataFinal", dataFinal)
                .list();
        
        if (lstVendaAlmoco == null) {
            throw new RelatorioException("Não foram encontrados compras no período informado");
        }
        return lstVendaAlmoco;
    }    
}

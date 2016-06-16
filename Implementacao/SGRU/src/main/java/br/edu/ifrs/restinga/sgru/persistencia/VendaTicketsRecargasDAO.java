/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.VendaTicketsRecargas;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Session;

public class VendaTicketsRecargasDAO {
    private final Session sessao;

    public VendaTicketsRecargasDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();        
    }

    /**
     * Persiste a VendaAlmoco enviado na base de dados
     * @param vendaTicketsRecargas A vendaTicketsRecargas a ser salvo na base de dados
     */
    public void salvar(VendaTicketsRecargas vendaTicketsRecargas) {
        sessao.saveOrUpdate(vendaTicketsRecargas);        
    }  
    
    /**
     * Carrega uma lista de vendas em uma determinada data para um determinado caixa
     * @param idCaixaRU Id do caixa onde forma realizadas as vendas
     * @param dataVenda A data da venda dos almoços a serem carregados
     * @return Uma lista de vendas da data informada
     */
    public List<VendaTicketsRecargas> carregar(int idCaixaRU, Calendar dataVenda) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return (List<VendaTicketsRecargas>) sessao.createQuery("FROM VendaTicketsRecargas WHERE DATE(dataVenda)=:dataVenda AND idCaixaRU=:idCaixaRU")                
                .setString("dataVenda", f.format(dataVenda.getTime()))
                .setString("idCaixaRU", String.valueOf(idCaixaRU))
                .list();
    }    
    
    /**
     * Carrega uma lista de vendas para um determinado cartão no dia corrente
     * @param idCartao O id do cartão a ser pesquisado
     * @return Uma lista de objetos VendaAlmoco
     */
    public List<VendaTicketsRecargas> carregarAlmocosCartaoDia(int idCartao) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return (List<VendaTicketsRecargas>) sessao.createQuery("FROM VendaTicketsRecargas WHERE DATE(dataVenda)=:dataVenda AND idCartao=:idCartao")
                .setString("dataVenda", f.format(Calendar.getInstance().getTime()))
                .setString("idCartao", String.valueOf(idCartao))
                .list(); 
    }
}
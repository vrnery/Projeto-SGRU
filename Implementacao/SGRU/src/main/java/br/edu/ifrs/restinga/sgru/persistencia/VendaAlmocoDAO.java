/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.VendaAlmoco;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author 10070133
 */
public class VendaAlmocoDAO {
    private final Session sessao;

    public VendaAlmocoDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();        
    }

    /**
     * Persiste a VendaAlmoco enviado na base de dados
     * @param vendaAlmoco A vendaAlmoco a ser salvo na base de dados
     */
    public void salvar(VendaAlmoco vendaAlmoco) {
        sessao.saveOrUpdate(vendaAlmoco);        
    }  
    
    /**
     * Carrega uma lista de almoços vendidos em uma determinada data para um determinado caixa
     * @param idCaixaRU Id do caixa onde forma realizadas as vendas
     * @param dataVenda A data da venda dos almoços a serem carregados
     * @return Uma lista de almoços vendidos na data informada
     */
    public List<VendaAlmoco> carregar(int idCaixaRU, Calendar dataVenda) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return (List<VendaAlmoco>) sessao.createQuery("FROM VendaAlmoco WHERE DATE(dataVenda)=:dataVenda AND idCaixaRU=:idCaixaRU")
                .setString("dataVenda", f.format(dataVenda.getTime()))
                .setString("idCaixaRU", String.valueOf(idCaixaRU))
                .list();
    }    
    
    /**
     * Carrega uma lista de almoços pagos com um determinado cartão no dia corrente
     * @param idCartao O id do cartão a ser pesquisado
     * @return Uma lista de objetos VendaAlmoco
     */
    public List<VendaAlmoco> carregarAlmocosCartaoDia(int idCartao) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return (List<VendaAlmoco>) sessao.createQuery("FROM VendaAlmoco WHERE DATE(dataVenda)=:dataVenda AND idCartao=:idCartao")
                .setString("dataVenda", f.format(Calendar.getInstance().getTime()))
                .setString("idCartao", String.valueOf(idCartao))
                .list(); 
    }
}

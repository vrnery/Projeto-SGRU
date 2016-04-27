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
     * Carrega uma lista de almoços vendidos em uma determinada data
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
     * Executa commit, para confirmação, e rollback, para não confirmação
     * @param confirmar True, para confirmar a operação, e false para não confirmar
     */
    public void confirmarVendaAlmco(boolean confirmar) {
        if (confirmar) {
            sessao.getTransaction().commit();
        } else {
            sessao.getTransaction().rollback();
        }
    }
}

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
     * @param dataVenda A data da venda dos almoços a serem carregados
     * @return Uma lista de almoços vendidos na data informada
     */
    public List<VendaAlmoco> carregar(Calendar dataVenda) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return (List<VendaAlmoco>) sessao.createQuery("FROM VendaAlmoco WHERE DATE(dataVenda)=:dataVenda").setString("dataVenda", f.format(dataVenda.getTime())).list();        
    }
}

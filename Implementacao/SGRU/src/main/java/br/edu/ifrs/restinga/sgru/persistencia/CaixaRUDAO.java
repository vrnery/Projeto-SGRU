/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.CaixaRU;
import java.text.SimpleDateFormat;
import java.util.Calendar;
//import br.edu.ifrs.restinga.sgru.modelo.VendaAlmoco;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author marcelo.lima
 */
public class CaixaRUDAO {
    private final Session sessao;

    public CaixaRUDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();        
    }
    
    /**
     * Persiste um objeto CaixaRU no banco de dados
     * @param caixaRU O caixa a ser cadastrado no sistema
     */
    public void salvar(CaixaRU caixaRU) {
        sessao.saveOrUpdate(caixaRU);        
    }  
    
    /**
     * Procura um caixa do RU que contenha o id informado
     * @param dataAbertura A data de abertura do caixa do RU
     * @return Um objeto CaixaRU
     */
    public CaixaRU carregar(Calendar dataAbertura) {        
        //return (CaixaRU) sessao.load(CaixaRU.class, id);        
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return (CaixaRU) sessao.createQuery("FROM CaixaRU WHERE DATE(dataAbertura)=:dataAbertura").setString("dataAbertura", f.format(dataAbertura.getTime())).uniqueResult();        
    }    

    /**
     * Buscar caixa aberto pelo operador
     * @param idOperador Codigo do OperadorCaixa
     * @param data Data do dia
     * @return Objeto CaixaRU
     */
    public CaixaRU reabrirCaixa(int idOperador, Calendar data) {
        return (CaixaRU) sessao.createQuery("FROM CaixaRU WHERE idOperadorCaixa=:op AND dataFechamento = null AND dataAbertura < :data ORDER BY dataAbertura DESC").setInteger("op", idOperador).setDate("data", data.getTime()).uniqueResult();
    }        
        
    /**
     * Salva a venda do almoco no banco de dados
     * @param vendaAlmoco A venda de almoco realizada
     */
    /*
    public void salvarVendaAlmoco(VendaAlmoco vendaAlmoco) {
        sessao.saveOrUpdate(vendaAlmoco);
    }
    */
}

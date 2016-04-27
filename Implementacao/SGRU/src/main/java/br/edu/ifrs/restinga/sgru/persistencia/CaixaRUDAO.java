/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.CaixaRU;
import br.edu.ifrs.restinga.sgru.modelo.OperadorCaixa;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.hibernate.Session;

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
     * @param operador O operador de caixa que abriu o caixa anteriormente
     * @param dataAbertura A data de abertura do caixa do RU
     * @return Um objeto CaixaRU
     */
    public CaixaRU carregarCaixaAberto(OperadorCaixa operador, Calendar dataAbertura) {        
        //return (CaixaRU) sessao.load(CaixaRU.class, id);        
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return (CaixaRU) sessao.createQuery("FROM CaixaRU WHERE DATE(dataAbertura)=:dataAbertura AND idOperadorCaixa=:idOperador AND valorFechamento='0'")
                .setString("dataAbertura", f.format(dataAbertura.getTime())).setString("idOperador", String.valueOf(operador.getId())).uniqueResult();        
    }    
}

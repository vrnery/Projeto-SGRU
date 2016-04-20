/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.ValorAlmoco;
import java.util.Calendar;
import org.hibernate.Session;

/**
 *
 * @author marcelo.lima
 */
public class AlmocoDAO {
    private final Session sessao;

    public AlmocoDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();        
    }        
    
    /**     
     * Pesquisa o custo de um almoço na data enviada
     * @param dataCredito Data de crédito dos créditos do cartão
     * @return um objeto ValorAlmoco
     */
    public ValorAlmoco carregar(Calendar dataCredito) {
        return (ValorAlmoco) sessao.load(ValorAlmoco.class, dataCredito);
    }           
}

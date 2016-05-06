/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.excessao.ValorAlmocoInvalidoException;
import br.edu.ifrs.restinga.sgru.modelo.ValorAlmoco;
import java.util.Calendar;
import org.hibernate.Session;

/**
 *
 * @author 10070133
 */
public class ValorAlmocoDAO {
    private final Session sessao;

    
    public ValorAlmocoDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();        
    }

    /**
     * Persiste o ValorAlmoco enviado na base de dados
     * @param valorAlmoco O valor do almoco a ser salvo na base de dados
     */
    public void salvar(ValorAlmoco valorAlmoco) {
        sessao.saveOrUpdate(valorAlmoco);        
    }

    /**
     * Carregar o valor atual do ValorAlmoco
     * @return Objeto do tipo ValorAlmoco
     */
    public ValorAlmoco carregarValorAtualAlmoco() {        
        return (ValorAlmoco) sessao.createQuery("FROM ValorAlmoco ORDER BY dataValor DESC").setMaxResults(1).uniqueResult();
    }

    /**
     * O valor a ser cobrado pelo almoco com base na data enviada
     * @param dataCredito Data em que os creditos foram inseridos no cartao
     * @return Um objeto do tipo ValorAlmoco
     * @throws br.edu.ifrs.restinga.sgru.excessao.ValorAlmocoInvalidoException Caso não encontre valor de almoço para data informada
     */
    public ValorAlmoco getValorAlmocoPorData(Calendar dataCredito) throws ValorAlmocoInvalidoException {
        ValorAlmoco tmpValorAlmoco;
        tmpValorAlmoco = (ValorAlmoco) sessao.createQuery("FROM ValorAlmoco WHERE dataValor <= :dataCredito ORDER BY dataValor DESC").setDate("dataCredito", dataCredito.getTime()).setMaxResults(1).uniqueResult();
        if (tmpValorAlmoco == null) {
            throw new ValorAlmocoInvalidoException("Valor de almoço não encontrado para data informada!");
        }
        return tmpValorAlmoco;
    }    
}

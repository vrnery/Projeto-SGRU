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
 * @author 10070133
 */
public class ValorAlmocoDAO {
    private final Session sessao;

    
    public ValorAlmocoDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();
        sessao.beginTransaction();
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
     * Ajusta o valor a ser cobrado pelo almoço devido a data da compra do credito
     * @param expira Data de expiração do cartão
     * @return Objeto do tipo ValorAlmoco
     */
    /*
    public ValorAlmoco almocoCartao(Calendar expira) {
        long data = 86400000L * 60;
        Calendar verificar = new Date(expira.getTime() - data);
        System.out.println("Expira: " + expira + " Procura: " + verificar.toString());
        return (ValorAlmoco) sessao.createQuery("FROM ValorAlmoco WHERE dataValor >= :verifica ORDER BY dataValor").setDate("verifica", verificar).setMaxResults(1).uniqueResult();
    }
    */

    /**
     * O valor a ser cobrado pelo almoco com base na data enviada
     * @param dataCredito Data em que os creditos foram inseridos no cartao
     * @return Um objeto do tipo ValorAlmoco
     */
    public ValorAlmoco getValorAlmocoPorData(Calendar dataCredito) {
        return (ValorAlmoco) sessao.createQuery("FROM ValorAlmoco WHERE dataValor <= :dataCredito ORDER BY dataValor DESC").setDate("dataCredito", dataCredito.getTime()).setMaxResults(1).uniqueResult();
    }
    
    /**
     * Encerra uma transação com o banco de dados. 
     * Esse método é chamado automaticamente.
     */        
    public void encerrar() {
        sessao.getTransaction().commit();
    }
}

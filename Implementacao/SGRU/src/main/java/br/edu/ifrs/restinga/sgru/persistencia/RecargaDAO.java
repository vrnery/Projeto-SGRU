/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.Recarga;
import org.hibernate.Session;

/**
 *
 * @author marcelo.lima
 */
public class RecargaDAO {
    private final Session sessao;

    public RecargaDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();        
    }
    
    /**
     * Pesquisa uma recarga com o id informado
     * @param id O id da regarga a ser pesquisada
     * @return Um objeto Recarga
     */
    public Recarga carregar(int id) {
        return (Recarga) sessao.load(Recarga.class, id);
    }    
    
    /**
     * Persiste um objeto Aluno no banco de dados
     * @param recarga A recarga a ser cadastrada no sistema
     */
    public void salvar(Recarga recarga) {
        sessao.saveOrUpdate(recarga);        
    }          
}

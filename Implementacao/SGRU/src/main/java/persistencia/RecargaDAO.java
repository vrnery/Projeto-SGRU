/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

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
        sessao.beginTransaction();
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
     * Encerra uma transação com o banco de dados. 
     * Esse método é chamado automaticamente.
     */        
    public void encerrar() {
        sessao.getTransaction().commit();
    }                
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import br.edu.ifrs.restinga.sgru.modelo.CaixaRU;
import org.hibernate.Session;

/**
 *
 * @author marcelo.lima
 */
public class CaixaRUDAO {
    private final Session sessao;

    public CaixaRUDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();
        sessao.beginTransaction();
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
     * @param id O id do caixa do RU
     * @return Um objeto CaixaRU
     */
    public CaixaRU carregar(int id) {
        return (CaixaRU) sessao.load(CaixaRU.class, id);
    }    
    
    /**
     * Encerra uma transação com o banco de dados. 
     * Esse método é chamado automaticamente.
     */    
    public void encerrar() {
        sessao.getTransaction().commit();
    }            
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import br.edu.ifrs.restinga.sgru.modelo.ValorAlmoco;
import java.util.Date;
import org.hibernate.Session;

/**
 *
 * @author marcelo.lima
 */
public class AlmocoDAO {
    private final Session sessao;

    public AlmocoDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();
        sessao.beginTransaction();
    }        
    
    /**     
     * Pesquisa o custo de um almoço na data enviada
     * @param dataCredito Data de crédito dos créditos do cartão
     * @return um objeto ValorAlmoco
     */
    public ValorAlmoco carregar(Date dataCredito) {
        return (ValorAlmoco) sessao.load(ValorAlmoco.class, dataCredito);
    }    
        
    /**
     * Encerra uma transação com o banco de dados. 
     * Esse método é chamado automaticamente.
     */
    public void encerrar() {
        sessao.getTransaction().commit();
    }                
}

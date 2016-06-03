/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.TipoCliente;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author marcelo.lima
 */
public class TipoClienteDAO {
    private final Session sessao;

    public TipoClienteDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();        
    }
    
    public List<TipoCliente> getLstTipoClientes() {
        return sessao.createQuery("FROM TipoCliente").list();
    }
    
    public TipoCliente buscarCodigo(int codigo){
        return (TipoCliente) sessao.createQuery("FROM TipoCliente WHERE id=:codigo").setInteger("codigo", codigo).uniqueResult();
    }
}

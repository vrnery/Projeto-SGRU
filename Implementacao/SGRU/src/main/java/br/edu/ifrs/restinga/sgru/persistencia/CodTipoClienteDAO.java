/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.CodTipoCliente;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author marcelo.lima
 */
public class CodTipoClienteDAO {
    private final Session sessao;

    public CodTipoClienteDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();        
    }
    
    public List<CodTipoCliente> getLstTipoClientes() {
        return sessao.createQuery("FROM CodTipoCliente").list();
    }
}

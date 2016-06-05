/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.TipoFuncionario;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author cstads
 */
public class TipoFuncionarioDAO {
    private final Session sessao;

    public TipoFuncionarioDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();        
    }
    
    public List<TipoFuncionario> getLstTipoFuncionarios() {
        return sessao.createQuery("FROM TipoFuncionario").list();
    }
    
    public TipoFuncionario buscarCodigo(int codigo){
        return (TipoFuncionario) sessao.createQuery("FROM TipoFuncionario WHERE id=:codigo").setInteger("codigo", codigo).uniqueResult();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.excessao.DadoPessoaInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException;
import br.edu.ifrs.restinga.sgru.modelo.Cliente;
import br.edu.ifrs.restinga.sgru.modelo.TipoCliente;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author marcelo.lima
 */
public class ClienteDAO {
    private final Session sessao;

    public ClienteDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();        
    }
    
    /**
     * Pesquisa um cliente baseado no número de matrícula enviada
     * @param matricula Número de matrícula do cliente
     * @return um objeto Cliente
     * @throws br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException Se a matrícula não seja localizada
     */     
    public Cliente carregar(String matricula) throws MatriculaInvalidaException {        
        Cliente tmpCliente = (Cliente) sessao.createQuery("FROM Cliente WHERE matricula=:matricula").setString("matricula", matricula).uniqueResult();
        if (tmpCliente == null) {
            throw new MatriculaInvalidaException("Matrícula não encontrada!");
        }                
        return tmpCliente;
    }   
    
    /**
     * Persiste um objeto Cliente no banco de dados
     * @param cliente O cliente a ser cadastrado no sistema
     * @throws br.edu.ifrs.restinga.sgru.excessao.DadoPessoaInvalidoException Caso a matrícula ou o login informado já esteja cadastrado no sistema
     */
    public void salvar(Cliente cliente) throws DadoPessoaInvalidoException {
        try {
            sessao.saveOrUpdate(cliente);        
        } catch (ConstraintViolationException e) {            
            if (e.getSQLException().getMessage().contains("matricula")) {
                sessao.clear();
                throw new DadoPessoaInvalidoException("Matrícula já cadastrada!");
            } else if (e.getSQLException().getMessage().contains("login")) {
                sessao.clear();
                throw new DadoPessoaInvalidoException("Login já cadastrado!");
            } else {
                
            }
        }
    }          
    
    /**
     * Retorna uma lista de cliente do tipo desejado
     * @param tipoCliente O código que identifica o tipo do cliente
     * @return Uma lista de objetos Cliente
     */
    public List<Cliente> carregarClientesPorTipo(TipoCliente tipoCliente) {
        if (tipoCliente == null) {
            // Todos os tipos de cliente
            return sessao.createQuery("FROM Cliente ORDER BY nome").list();
        } else {
            return sessao.createQuery("FROM Cliente WHERE idTipoCliente=:tipoCliente ORDER BY nome")
                .setString("tipoCliente", tipoCliente.getCodigo()).list();   
        }        
    }        
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.excessao.DadoPessoaInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException;
import br.edu.ifrs.restinga.sgru.modelo.Funcionario;
import br.edu.ifrs.restinga.sgru.modelo.TipoFuncionario;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author cstads
 */
public class FuncionarioDAO {
    private final Session sessao;

    public FuncionarioDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();        
    }
    
    /**
     * Pesquisa um funcionario baseado no número de matrícula enviada
     * @param matricula
     * @return
     * @throws MatriculaInvalidaException 
     */
    public Funcionario carregar(String matricula) throws MatriculaInvalidaException {        
        Funcionario tmpFuncionario = (Funcionario) sessao.createQuery("FROM Funcionario WHERE matricula=:matricula").setString("matricula", matricula).uniqueResult();
        if (tmpFuncionario == null) {
            throw new MatriculaInvalidaException("Matrícula não encontrada!");
        }                
        return tmpFuncionario;
    }   
    
    /**
     * Persiste um objeto Funcionario no banco de dados
     * @param funcionario 
     * @throws br.edu.ifrs.restinga.sgru.excessao.DadoPessoaInvalidoException Caso a matrícula ou o login informado já esteja cadastrado no sistema
     */
    public void salvar(Funcionario funcionario) throws DadoPessoaInvalidoException {
        sessao.saveOrUpdate(funcionario);
        /*
        try {
            sessao.saveOrUpdate(funcionario);        
        } catch (ConstraintViolationException e) {            
            if (e.getSQLException().getMessage().contains("matricula")) {
                throw new DadoPessoaInvalidoException("Matrícula já cadastrada!");
            } else if (e.getSQLException().getMessage().contains("login")) {
                throw new DadoPessoaInvalidoException("Login já cadastrado!");
            } else {
                
            }
        }
        */
    }          
    
    /**
     * Retorna uma lista de funcionario do tipo desejado
     * @param tipoFuncionario
     * @return 
     */
    public List<Funcionario> carregarFuncionariosPorTipo(TipoFuncionario tipoFuncionario) {
        if (tipoFuncionario == null) {
            return sessao.createQuery("FROM Funcionario ORDER BY nome").list();
        } else {
            return sessao.createQuery("FROM Funcionario WHERE idTipoFuncionario=:tipoFuncionario ORDER BY nome")
                    .setString("tipoFuncionario", String.valueOf(tipoFuncionario.getId())).list();   
        }        
    }
}

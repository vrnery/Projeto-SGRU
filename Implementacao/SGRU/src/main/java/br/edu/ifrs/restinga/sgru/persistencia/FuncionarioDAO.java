/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException;
import br.edu.ifrs.restinga.sgru.modelo.Funcionario;
import java.util.List;
import org.hibernate.Session;

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
     */
    public void salvar(Funcionario funcionario) {
        sessao.saveOrUpdate(funcionario);        
    }          
    
    /**
     * Retorna uma lista de funcionario do tipo desejado
     * @param tipoFuncionario
     * @return 
     */
    public List<Funcionario> carregarFuncionariosPorTipo(String tipoFuncionario) {
        if (tipoFuncionario.equals("-1")) {
            return sessao.createQuery("FROM Funcionario ORDER BY nome").list();
        } else {
            return sessao.createQuery("FROM Funcionario WHERE idTipoFuncionario=:tipoFuncionario ORDER BY nome").setString("tipoFuncionario", String.valueOf(tipoFuncionario)).list();   
        }        
    }
}

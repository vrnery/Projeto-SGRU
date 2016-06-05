/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.excessao.LoginInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException;
import br.edu.ifrs.restinga.sgru.modelo.Funcionario;
import br.edu.ifrs.restinga.sgru.modelo.Pessoa;
import br.edu.ifrs.restinga.sgru.modelo.TipoFuncionario;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author 10070133
 */
public class PessoaDAO {
    private final Session sessao;

    public PessoaDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();
    }
        
    /**
     * Pesquisa uma pessoa baseado no id informado
     * @param matricula A matricula da pessoa pesquisada
     * @return um objeto Pessoa
     * @throws br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException Caso não enconte a matrícula informada
     */
    public Pessoa carregar(String matricula) throws MatriculaInvalidaException {        
        Pessoa tmpPessoa = (Pessoa) sessao.createQuery("FROM Pessoa WHERE matricula=:matricula").setString("matricula", matricula).uniqueResult();
        if (tmpPessoa == null) {
            throw new MatriculaInvalidaException("Matrícula não encontrada!");
        }        
        return tmpPessoa;
    }        
    
    /**
     * Carrega uma pessoa pelo id
     * @param id O id da pessoa pesquisada
     * @return Um objeto Pessoa
     */
    public Pessoa carregar(int id) {
        return (Pessoa) sessao.load(Pessoa.class, id);
    }    
    
    /**
     * Verifica se existem usuário e senha informados
     * @param login O login do usuário
     * @param senha A senha do usuário
     * @return Um objeto Pessoa
     * @throws LoginInvalidoException Caso o usuário e/ou a senha sejam inválidos
     */
    public Pessoa autenticar(String login, String senha) throws LoginInvalidoException {
        Pessoa tmpPessoa = (Pessoa) sessao.createQuery("FROM Pessoa WHERE login=:login AND senha=:senha").setString("login", login).setString("senha", senha).uniqueResult();
        if (tmpPessoa == null) {
            throw new LoginInvalidoException("Usuário ou senha incorretos!");
        }
        return tmpPessoa;
    }
    
    /**
     * Persiste um objeto Pessoa no banco de dados
     * @param pessoa A pessoa a ser cadastrada no sistema
     */
    public void salvar(Pessoa pessoa) {
        sessao.saveOrUpdate(pessoa);        
    }          
    
    /**
     * Remove uma pessoa da base de dados
     * @param pessoa A pessoa a ser removida
     */
    public void excluir(Pessoa pessoa) {
        sessao.delete(pessoa);
    }

    /**
     * Buscar lista de tipo de funcionarios
     * @return 
     */
    public List<TipoFuncionario> getLstTipoFuncionario() {
        return sessao.createQuery("FROM TipoFuncionario").list();
    }
    
    /**
     * Carregar tipo de funcionario
     * @param id
     * @return 
     */
    public TipoFuncionario carregarTipoFuncionarioPorCodigo(int id) {
        return (TipoFuncionario) sessao.createQuery("FROM TipoFuncionario WHERE id=:codigo").setInteger("codigo", id).uniqueResult();
    }
    
    public List<Funcionario> carregarFuncionariosPorTipo(String tipoFuncionario) {
        if (tipoFuncionario.equals("-1")) {
            return sessao.createQuery("FROM Funcionario ORDER BY nome").list();
        } else {
            return sessao.createQuery("FROM Funcionario WHERE idTipoFuncionario=:tipoFuncionario ORDER BY nome").setString("tipoFuncionario", String.valueOf(tipoFuncionario)).list();   
        }        
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.excessao.DadoPessoaInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.LoginInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException;
import br.edu.ifrs.restinga.sgru.persistencia.PessoaDAO;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author marcelo.lima
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pessoa implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private int id;
    @Column(unique = true)
    private String matricula;
    private String nome;
    private String telefone;
    private String email;
    @Column(unique = true)
    private String login;
    private String senha;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * @return the matricula
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * @param matricula the matricula to set
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }    

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set     
     */
    public void setNome(String nome) {        
        this.nome = nome;
    }

    /**
     * @return the telefone
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * @param telefone the telefone to set     
     */
    public void setTelefone(String telefone) {        
        this.telefone = telefone;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set     
     */
    public void setEmail(String email) {                
        this.email = email;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }            
    
    /**
     * Retorna um objeto Pessoa com o login e senha informados
     * @param login O login do usuário
     * @param senha A senha do usuário
     * @return Um objeto Pessoa com o login e senha informados
     * @throws LoginInvalidoException Caso os dados informados sejam inválidos
     */
    public static Pessoa validarLoginUsuario(String login, String senha) throws LoginInvalidoException {
        PessoaDAO daoPessoa = new PessoaDAO();
        return daoPessoa.autenticar(login, senha);
    }
    
    /**
     * Pesquisa uma pessoa com a matrícula informada
     * @param matricula A matrícula da pessoa a ser pesquisada
     * @return Um objeto Pessoa     
     * @throws MatriculaInvalidaException Caso a matrícula do usuário não seja encontrada
     */
    public static Pessoa carregar(String matricula) throws MatriculaInvalidaException {
        PessoaDAO pessoaDAO = new PessoaDAO();
        return pessoaDAO.carregar(matricula);
    }
    
    /**
     * Consiste os dados informados
     * @param pessoa O objeto Pessoa a ser consistido
     * @throws DadoPessoaInvalidoException Quando um ou mais dados estiverem incorretos
     */
    public static void consistirDados(Pessoa pessoa) throws DadoPessoaInvalidoException {
        // O nome nao pode ser vazio e contem apenas letras e espacos
        String regexNome = "^[a-zA-Zà-úÀ-Ú ]*$";
        if ((pessoa.nome.trim().length() == 0) ||
                !(pessoa.nome.trim().matches(regexNome))) {
            throw new DadoPessoaInvalidoException("O nome informado é inválido!");
        }
        
        // O telefone nao eh obrigatorio
        if (pessoa.telefone.length() > 0) {
            String regexTelefone = "^\\([1-9][1-9]\\) [2-9][0-9]{3}-[0-9]{4}[0-9]{0,1}?$";
            if (!pessoa.telefone.matches(regexTelefone)) {
                throw new DadoPessoaInvalidoException("O número de telefone informado é inválido!");
            }
        }    
        pessoa.telefone = pessoa.telefone.trim().replaceAll("[^0-9]()", "");        
        
        // o email nao eh obrigatorio
        if (pessoa.email.length() > 0) {
            String regexEmail = "^\\w+([-\\.]\\w+)*@\\w+([-.]\\w+)+$";
            if (!pessoa.email.matches(regexEmail)) {
                throw new DadoPessoaInvalidoException("O e-mail informado é inválido!");
            }
        }
    }
}
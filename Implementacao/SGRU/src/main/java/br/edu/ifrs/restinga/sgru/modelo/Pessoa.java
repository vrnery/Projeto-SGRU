/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.excessao.DadoPessoaInvalidoException;
import java.io.Serializable;
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
    private String matricula;
    private String nome;
    private String telefone;
    private String email;
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
     * @throws br.edu.ifrs.restinga.sgru.excessao.DadoPessoaInvalidoException Quando o nome enviado contém caracteres diferentes de letras e espaços
     */
    public void setNome(String nome) throws DadoPessoaInvalidoException {
        // O nome nao pode ser vazio e contem apenas letras e espacos
        String regexNome = "^[a-zA-Z]*$";
        if ((nome.trim().length() == 0) ||
                !(nome.trim().matches(regexNome))) {
            throw new DadoPessoaInvalidoException("O nome informado é inválido!");
        }
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
     * @throws br.edu.ifrs.restinga.sgru.excessao.DadoPessoaInvalidoException Se o telefone estiver em formato inválido
     */
    public void setTelefone(String telefone) throws DadoPessoaInvalidoException {
        // O telefone nao eh obrigatorio
        if (telefone.length() > 0) {
            String regexTelefone = "^\\([1-9][1-9]\\) [2-9][0-9]{3}-[0-9]{4}[0-9]{0,1}?$";
            if (!telefone.matches(regexTelefone)) {
                throw new DadoPessoaInvalidoException("O número de telefone informado é inválido!");
            }
        }
        // Grava apenas numeros no banco
        this.telefone = telefone.trim().replaceAll("[^0-9]()", "");
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     * @throws br.edu.ifrs.restinga.sgru.excessao.DadoPessoaInvalidoException Se o email informado for inválido
     */
    public void setEmail(String email) throws DadoPessoaInvalidoException {
        // o email nao eh obrigatorio
        if (email.length() > 0) {
            String regexEmail = "^\\w+([-\\.]\\w+)*@\\w+([-.]\\w+)+$";
            if (!email.matches(regexEmail)) {
                throw new DadoPessoaInvalidoException("O e-mail informado é inválido!");
            }
        }
        
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
}
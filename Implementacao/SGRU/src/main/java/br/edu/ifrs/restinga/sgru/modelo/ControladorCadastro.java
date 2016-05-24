/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.excessao.DadoPessoaInvalidoException;
import br.edu.ifrs.restinga.sgru.persistencia.ClienteDAO;

/**
 *
 * @author 10070124
 */
public class ControladorCadastro {
    private Cliente cliente;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ControladorCadastro() {
        this.cliente = new Cliente();
    }
    
    public void salvar(){
        ClienteDAO dao = new ClienteDAO();
        dao.salvar(cliente);
    }
}

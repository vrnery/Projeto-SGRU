/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.persistencia.ClienteDAO;
import br.edu.ifrs.restinga.sgru.persistencia.TipoClienteDAO;
import java.util.List;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author 10070124
 */
public class ControladorCadastro {
    private Cliente cliente;
    private List<TipoCliente> tipoCliente;
    private UploadedFile file;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<TipoCliente> getTipoCliente() {
        TipoClienteDAO dao = new TipoClienteDAO();
        tipoCliente = dao.getLstTipoClientes();
        return tipoCliente;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public ControladorCadastro() {
        this.cliente = new Cliente();
    }
    
    public void salvar(){
        ClienteDAO dao = new ClienteDAO();
        dao.salvar(cliente);
    }
    
    public void upload() {
        if(file != null) {
            //FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            //FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
}

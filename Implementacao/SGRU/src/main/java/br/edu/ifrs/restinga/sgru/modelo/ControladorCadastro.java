/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.excessao.FotoNaoEncontradaException;
import br.edu.ifrs.restinga.sgru.persistencia.ClienteDAO;
import br.edu.ifrs.restinga.sgru.persistencia.TipoClienteDAO;
import java.util.Calendar;
import java.util.List;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author 10070124
 */
public class ControladorCadastro {
    private Cliente cliente;
    private List<TipoCliente> tiposCliente;
    private int tipoCliente;
    private UploadedFile file;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<TipoCliente> getTiposCliente() {
        TipoClienteDAO dao = new TipoClienteDAO();
        tiposCliente = dao.getLstTipoClientes();
        return tiposCliente;
    }

    public int getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(int codTipoCliente) {
        this.tipoCliente = codTipoCliente;
        cliente.setTipoCliente(new TipoClienteDAO().buscarCodigo(this.tipoCliente));
    }
    
    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) throws FotoNaoEncontradaException {
        if(file != null){
            this.file = file;
            throw new FotoNaoEncontradaException("verificada!");
        } else{
            throw new FotoNaoEncontradaException("Não foi possivel verificar!");
        }
    }

    public ControladorCadastro() {
        this.cliente = new Cliente();
        this.cliente.setCartao(new Cartao());
    }
    
    public void salvar(){
        if(file != null) {
            cliente.setCaminhoFoto(file.getFileName());
        }
        cliente.setCartao(new Cartao());
        cliente.getCartao().setDataCredito(Calendar.getInstance());
        cliente.getCartao().setSaldo(0);
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

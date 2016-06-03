/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.excessao.CadastroInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.FotoNaoEncontradaException;
import br.edu.ifrs.restinga.sgru.persistencia.ClienteDAO;
import br.edu.ifrs.restinga.sgru.persistencia.TipoClienteDAO;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.List;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author 10070124
 */
public class ControladorCadastro {
    private Cliente cliente;
    private final List<TipoCliente> tiposCliente;
    private int tipoCliente;    
    private String codTipoCliente;
    private UploadedFile file;        
    
    public ControladorCadastro() {
        this.cliente = new Cliente();
        this.cliente.setCartao(new Cartao());
        
        // Carrega lista de clientes
        TipoClienteDAO dao = new TipoClienteDAO();
        tiposCliente = dao.getLstTipoClientes();
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {        
        this.cliente = cliente;        
    }

    public List<TipoCliente> getTiposCliente() {        
        return tiposCliente;
    }

    public int getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(int codTipoCliente) {
        this.tipoCliente = codTipoCliente;
        cliente.setTipoCliente(new TipoClienteDAO().buscarCodigo(this.tipoCliente));
    }
    
    /**
     * @return the codTipoCliente
     */
    public String getCodTipoCliente() {
        return codTipoCliente;
    }

    /**
     * @param codTipoCliente the codTipoCliente to set
     */
    public void setCodTipoCliente(String codTipoCliente) {
        this.codTipoCliente = codTipoCliente;
    }    
    
    public List<Cliente> getLstClientes() {
        ClienteDAO daoCliente = new ClienteDAO();
        // -1 carrega todos os tipos de clientes
        return daoCliente.carregarClientesPorTipo("-1");
    }
    
    public void setFile(UploadedFile file) throws FotoNaoEncontradaException {
        if(file != null){
            this.file = file;
            throw new FotoNaoEncontradaException("verificada!");
        } else{
            throw new FotoNaoEncontradaException("Não foi possivel verificar!");
        }
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
    
    public void editarUsuario(InputStream inputStream, String extArquivo) throws CadastroInvalidoException,
            IOException {        
        // Cliente alterou a foto
        if (inputStream != null) {
            String caminhoFoto = "c:\\imagens\\" + this.cliente.getMatricula() + "." + extArquivo;
            Path target = Paths.get(caminhoFoto);
            // Copia a foto para o diretorio de fotos
            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            // atualiza o caminho foto do cliente
            this.cliente.setCaminhoFoto(caminhoFoto);
        }
        
        ClienteDAO daoCliente = new ClienteDAO();
        daoCliente.salvar(cliente);
    }
    
    public void upload() {
        if(file != null) {
            //FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            //FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
}

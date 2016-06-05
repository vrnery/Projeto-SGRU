/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.excessao.FotoNaoEncontradaException;
import br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException;
import br.edu.ifrs.restinga.sgru.persistencia.ClienteDAO;
import br.edu.ifrs.restinga.sgru.persistencia.PessoaDAO;
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
    private Funcionario funcionario;
    private Pessoa pessoa;
    private final List<TipoCliente> tiposCliente;
    private final List<TipoFuncionario> tiposFuncionario;
    private List<Cliente> lstClientes;
    private List<Funcionario> lstFuncionarios;
    private int tipoCliente;
    private int tipoFuncionario;
    private String codTipoCliente;
    private UploadedFile file;
    //private String destino;
    private PessoaDAO daoPessoa;
    
    public ControladorCadastro() {
        // DAO para todas as conexões
        daoPessoa = new PessoaDAO();
        
        this.cliente = new Cliente();
        this.cliente.setCartao(new Cartao());
        this.funcionario = new Funcionario();
        
        //this.destino = null;
        // Set data corrente para novo cartão
        //this.cliente.getCartao().setDataCredito(Calendar.getInstance());
        
        // Carrega lista de clientes
        TipoClienteDAO dao = new TipoClienteDAO();
        tiposCliente = dao.getLstTipoClientes();
        
        // Carrega lista de funcionarios
        tiposFuncionario = daoPessoa.getLstTipoFuncionario();
        
        // Carrega lista de clientes
        ClienteDAO daoCliente = new ClienteDAO();
        // -1 carrega todos os tipos de clientes
        this.lstClientes = daoCliente.carregarClientesPorTipo("-1");
        
        // Carrega lista de funcionarios
        // - 1 carrega todos os tipos de funcionarios
        this.lstFuncionarios = daoPessoa.carregarFuncionariosPorTipo("-1");
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {        
        this.cliente = cliente;        
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
    
    /**
     * @return the pessoa
     */
    public Pessoa getPessoa() {
        return pessoa;
    }

    /**
     * @param pessoa the pessoa to set
     */
    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
        
        if (pessoa instanceof Cliente) {
            this.cliente = (Cliente)pessoa;
        }
    }

    public List<TipoCliente> getTiposCliente() {        
        return tiposCliente;
    }

    public List<TipoFuncionario> getTiposFuncionario() {
        return tiposFuncionario;
    }

    public List<Cliente> getLstClientes() {        
        return this.lstClientes;
    }
    
    /**
     * @param lstClientes the lstClientes to set
     */
    public void setLstClientes(List<Cliente> lstClientes) {
        this.lstClientes = lstClientes;
    }

    public List<Funcionario> getLstFuncionarios() {
        return lstFuncionarios;
    }

    public void setLstFuncionarios(List<Funcionario> lstFuncionarios) {
        this.lstFuncionarios = lstFuncionarios;
    }
    
    public int getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(int codTipoCliente) {
        this.tipoCliente = codTipoCliente;
        cliente.setTipoCliente(new TipoClienteDAO().buscarCodigo(this.tipoCliente));
    }

    public int getTipoFuncionario() {
        return tipoFuncionario;
    }

    public void setTipoFuncionario(int tipoFuncionario) {
        this.tipoFuncionario = tipoFuncionario;
        funcionario.setTipoFuncionario(daoPessoa.carregarTipoFuncionarioPorCodigo(this.tipoFuncionario));
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
    
    public void setFile(UploadedFile file) throws FotoNaoEncontradaException {
        if(file != null){
            this.file = file;
            throw new FotoNaoEncontradaException("verificada!");
        } else{
            throw new FotoNaoEncontradaException("Não foi possivel verificar!");
        }
    }

//    public String getDestino() {
//        return destino;
//    }

//    public void setDestino(String destino) {
//        this.destino = destino;
//    }
    
    public void salvar() throws UsuarioInvalidoException {
        for(Cliente cli : this.lstClientes){
            if(cli.getMatricula().equals(cliente.getMatricula()))
                throw new UsuarioInvalidoException("Matricula já cadastrada!");
        }
        ClienteDAO dao = new ClienteDAO();
        cliente.setCartao(new Cartao());
        cliente.getCartao().setDataCredito(Calendar.getInstance());
        cliente.getCartao().setSaldo(0);
        dao.salvar(cliente);
    }
    
    public void salvarFuncionario() throws UsuarioInvalidoException {
        for(Funcionario fun : this.lstFuncionarios){
            if(fun.getMatricula().equals(funcionario.getMatricula()))
                throw new UsuarioInvalidoException("Matricula já cadastrada!");
        }
        daoPessoa.salvar((Pessoa) this.funcionario);
    }
    
    /**
     * Edita um usuário
     * @param inputStream Objeto enviado pelo componente p:fileUpload
     * @param extArquivo A extensão do arquivo     
     * @throws IOException Caso não consiga copiar o arquivo
     */
    public void editarUsuario(InputStream inputStream, String extArquivo) throws IOException {        
        // Cliente alterou a foto
        if (inputStream != null) {
            String caminhoFoto = "c:\\imagens\\" + this.pessoa.getMatricula() + "." + extArquivo;
            Path target = Paths.get(caminhoFoto);
            // Copia a foto para o diretorio de fotos
            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            // atualiza o caminho foto do cliente
            if (this.pessoa instanceof Cliente) {
                this.cliente.setCaminhoFoto(caminhoFoto);
            }
        }
        
        if (this.pessoa instanceof Cliente) {
            ClienteDAO daoCliente = new ClienteDAO();
            daoCliente.salvar(cliente);
        } else {
            //PessoaDAO daoPessoa = new PessoaDAO();
            daoPessoa.salvar(pessoa);
        }
    }
    
    /**
     * Remove uma pessoa da base de dados
     * @param id O id do usuário a ser removido
     */
    public void excluirUsuario(int id) {
        //PessoaDAO daoPessoa = new PessoaDAO();
        Pessoa exPessoa = daoPessoa.carregar(id);
        daoPessoa.excluir(exPessoa); 
        
        // Atualiza a lista de clientes
        for (Cliente cli : this.lstClientes) {
            if (cli.getId() == id) {
                this.lstClientes.remove(cli);
                break;
            }
        }        
    }
    
    public void excluirFuncionario(int id) {
        //PessoaDAO daoPessoa = new PessoaDAO();
        Pessoa exPessoa = daoPessoa.carregar(id);
        daoPessoa.excluir(exPessoa); 
        
        // Atualiza a lista de clientes
        for (Funcionario fun : this.lstFuncionarios) {
            if (fun.getId() == id) {
                this.lstFuncionarios.remove(fun);
                break;
            }
        }        
    }
    
    public void upload() {
        if(file != null) {
            //FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            //FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }   
}

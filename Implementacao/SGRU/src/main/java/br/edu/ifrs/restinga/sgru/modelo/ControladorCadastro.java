/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.excessao.DadoPessoaInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.FotoNaoEncontradaException;
import br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException;
import br.edu.ifrs.restinga.sgru.persistencia.ClienteDAO;
import br.edu.ifrs.restinga.sgru.persistencia.FuncionarioDAO;
import br.edu.ifrs.restinga.sgru.persistencia.PessoaDAO;
import br.edu.ifrs.restinga.sgru.persistencia.TipoClienteDAO;
import br.edu.ifrs.restinga.sgru.persistencia.TipoFuncionarioDAO;
import java.io.File;
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
    private UploadedFile file;
    
    public ControladorCadastro() {
        this.cliente = new Cliente();
        this.cliente.setCartao(new Cartao());
        this.funcionario = new Funcionario();
        
        // Set data corrente para novo cartão
        //this.cliente.getCartao().setDataCredito(Calendar.getInstance());
        
        // Carrega lista de clientes
        TipoClienteDAO dao = new TipoClienteDAO();
        tiposCliente = dao.getLstTipoClientes();
        
        // Carrega lista de funcionarios
        TipoFuncionarioDAO daoTipoFuncionario = new TipoFuncionarioDAO();
        tiposFuncionario = daoTipoFuncionario.getLstTipoFuncionarios();
        
        // Carrega lista de clientes
        ClienteDAO daoCliente = new ClienteDAO();
        // -1 carrega todos os tipos de clientes
        this.lstClientes = daoCliente.carregarClientesPorTipo("-1");
        
        // Carrega lista de funcionarios
        FuncionarioDAO daoFuncionario = new FuncionarioDAO();
        // - 1 carrega todos os tipos de funcionarios
        this.lstFuncionarios = daoFuncionario.carregarFuncionariosPorTipo("-1");
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
        
        if (pessoa instanceof Funcionario) {
            this.funcionario = (Funcionario)pessoa;
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
        this.cliente.setTipoCliente(new TipoClienteDAO().buscarCodigo(this.tipoCliente));
    }

    public int getTipoFuncionario() {
        return tipoFuncionario;
    }

    public void setTipoFuncionario(int tipoFuncionario) {
        this.tipoFuncionario = tipoFuncionario;
        funcionario.setTipoFuncionario(new TipoFuncionarioDAO().buscarCodigo(this.tipoFuncionario));
    }
    
    public void setFile(UploadedFile file) throws FotoNaoEncontradaException {
        if(file != null){
            this.file = file;
            throw new FotoNaoEncontradaException("verificada!");
        } else{
            throw new FotoNaoEncontradaException("Não foi possivel verificar!");
        }
    }

    public void salvar() throws UsuarioInvalidoException, DadoPessoaInvalidoException {
        // Verifica se a matricula já foi cadastrada
        if(isVerificaMatricula(cliente.getMatricula()))
            throw new UsuarioInvalidoException("Matricula já cadastrada!");
        
        // Verifica se o login já foi cadastrado
        if(isVerificaLogin(cliente.getMatricula(), cliente.getLogin()))
            throw new UsuarioInvalidoException("Login já cadastrado!");
        
        ClienteDAO dao = new ClienteDAO();
        cliente.setCartao(new Cartao());
        cliente.getCartao().setDataCredito(Calendar.getInstance());
        cliente.getCartao().setSaldo(0);
        dao.salvar(cliente);
        this.lstClientes = dao.carregarClientesPorTipo("-1");
    }
    
    public void salvarFuncionario() throws UsuarioInvalidoException {
        // Verifica se a matricula já foi cadastrada
        if(isVerificaMatricula(funcionario.getMatricula()))
            throw new UsuarioInvalidoException("Matricula já cadastrada!");
        
        // Verifica se o login já foi cadastrado
        if(isVerificaLogin(funcionario.getMatricula(), funcionario.getLogin()))
            throw new UsuarioInvalidoException("Login já cadastrado!");
        
        FuncionarioDAO dao = new FuncionarioDAO();
        dao.salvar(funcionario);
        this.lstFuncionarios = dao.carregarFuncionariosPorTipo("-1");
    }
    
    /**
     * Edita um usuário
     * @param inputStream Objeto enviado pelo componente p:fileUpload
     * @param extArquivo A extensão do arquivo     
     * @param txtPath Caminho raiz da aplicação recebido
     * @throws IOException Caso não consiga copiar o arquivo
     * @throws br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException
     */
    public void editarUsuario(InputStream inputStream, String extArquivo, String txtPath) throws IOException, UsuarioInvalidoException {
        // Verifica se o login já foi cadastrado
        if(isVerificaLogin(pessoa.getMatricula(), pessoa.getLogin()) || isVerificaLogin(cliente.getMatricula(), cliente.getLogin()))
            throw new UsuarioInvalidoException("Login já cadastrado!");
        
        // Cliente alterou a foto
        if (inputStream != null) {
            //String caminhoFoto = "c:\\imagens\\" + this.pessoa.getMatricula() + "." + extArquivo;
            String caminhoFoto = txtPath + "imagens"+ File.separatorChar + "fotos" + File.separatorChar + this.pessoa.getMatricula() + "." + extArquivo;
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
            PessoaDAO daoPessoa = new PessoaDAO();
            daoPessoa.salvar(pessoa);
        }
    }
    
    public void editarFuncionario() throws UsuarioInvalidoException {
        // Verifica se o login já foi cadastrado
        if(isVerificaLogin(funcionario.getMatricula(), funcionario.getLogin()))
            throw new UsuarioInvalidoException("Login já cadastrado!");
        
        FuncionarioDAO daoFuncionario = new FuncionarioDAO();
        daoFuncionario.salvar(funcionario);
    }
    
    /**
     * Remove uma pessoa da base de dados
     * @param id O id do usuário a ser removido
     */
    public void excluirUsuario(int id) {
        PessoaDAO daoPessoa = new PessoaDAO();
        Pessoa exPessoa = daoPessoa.carregar(id);
        daoPessoa.excluir(exPessoa); 
        
        if (exPessoa instanceof Cliente) {
            // Atualiza a lista de clientes
            for (Cliente cli : this.lstClientes) {
                if (cli.getId() == id) {
                    this.lstClientes.remove(cli);
                    break;
                }
            }
        }
        
        if (exPessoa instanceof Funcionario) {
            // Atualiza a lista de funcionarios
            for (Funcionario fun : this.lstFuncionarios) {
                if (fun.getId() == id) {
                    this.lstFuncionarios.remove(fun);
                    break;
                }
            }
        }
    }
    
    // Verifica se a matricula já existe
    public boolean isVerificaMatricula(String matricula) {
        boolean retorno = false;
        for(Cliente cli : this.lstClientes){
            if(cli.getMatricula().equals(matricula)){
                retorno = true;
                break;
            }
        }
        for(Funcionario fun : this.lstFuncionarios){
            if(fun.getMatricula().equals(matricula)) {
                retorno = true;
                break;
            }
        }
        return retorno;
    }
    
    // Verifica se o login já existe e se não é da propria matricula
    public boolean isVerificaLogin(String matricula, String login) {
        boolean retorno = false;
        for(Cliente cli : this.lstClientes){
            if(cli.getLogin().equals(login) && (!cli.getMatricula().equals(matricula))){
                retorno = true;
                break;
            }
        }
        for(Funcionario fun : this.lstFuncionarios){
            if(fun.getLogin().equals(login) && (!fun.getMatricula().equals(matricula))){
                retorno = true;
                break;
            }
        }
        return retorno;
    }
    
    public void upload() {
        if(file != null) {
            //FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            //FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }   
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.excessao.DadoPessoaInvalidoException;
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

/**
 *
 * @author 10070124
 */
public class ControladorCadastro {
    private Cliente cliente;
    private Funcionario funcionario;
    private Pessoa pessoa;
    private final List<TipoCliente> lstTipoCliente;
    private final List<TipoFuncionario> lstTipoFuncionario;
    private List<Cliente> lstClientes;
    private List<Funcionario> lstFuncionarios;
    private TipoCliente tipoCliente;
    private TipoFuncionario tipoFuncionario;    
    
    public ControladorCadastro() {
        this.cliente = new Cliente();        
        this.funcionario = new Funcionario();
        
        // Carrega lista de clientes
        TipoClienteDAO dao = new TipoClienteDAO();
        lstTipoCliente = dao.getLstTipoClientes();
        
        // Carrega lista de funcionarios
        TipoFuncionarioDAO daoTipoFuncionario = new TipoFuncionarioDAO();
        lstTipoFuncionario = daoTipoFuncionario.getLstTipoFuncionarios();
        
        // Carrega lista de clientes
        ClienteDAO daoCliente = new ClienteDAO();
        // null carrega todos os tipos de clientes
        this.lstClientes = daoCliente.carregarClientesPorTipo(null);
        
        // Carrega lista de funcionarios
        FuncionarioDAO daoFuncionario = new FuncionarioDAO();
        // null carrega todos os tipos de funcionarios
        this.lstFuncionarios = daoFuncionario.carregarFuncionariosPorTipo(null);
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

    public List<TipoCliente> getLstTipoCliente() {        
        return lstTipoCliente;
    }

    public List<TipoFuncionario> getLstTipoFuncionario() {
        return lstTipoFuncionario;
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
    
    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
        this.cliente.setTipoCliente(tipoCliente);
    }

    public TipoFuncionario getTipoFuncionario() {
        return tipoFuncionario;
    }

    public void setTipoFuncionario(TipoFuncionario tipoFuncionario) {
        this.tipoFuncionario = tipoFuncionario;
        funcionario.setTipoFuncionario(tipoFuncionario);
    }
    
    /**
     * Persiste um cliente na base da dados
     * @param inputStream Objeto enviado pelo componente p:fileUpload
     * @param extArquivo A extensão do arquivo     
     * @param txtPath Caminho raiz da aplicação recebido
     * @throws IOException Caso não consiga copiar o arquivo
     * @throws br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException Caso o usuário já esteja cadastrado no sistema
     * @throws br.edu.ifrs.restinga.sgru.excessao.DadoPessoaInvalidoException Quando um ou mais dados estiverem incorretos
     */
    public void salvarCliente(InputStream inputStream, String extArquivo, String txtPath) 
            throws UsuarioInvalidoException, DadoPessoaInvalidoException, IOException {
        Pessoa.consistirDados((Pessoa)this.cliente);                        
        // Cliente alterou a foto
        if (inputStream != null) {
            //String caminhoFoto = "c:\\imagens\\" + this.cliente.getMatricula() + "." + extArquivo;
            String caminhoFoto = txtPath + File.separatorChar + "imagens"+ File.separatorChar + "fotos" + File.separatorChar + this.cliente.getMatricula() + "." + extArquivo;
            Path target = Paths.get(caminhoFoto);
            // Copia a foto para o diretorio de fotos
            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            // atualiza o caminho foto do cliente
            this.cliente.setCaminhoFoto(caminhoFoto);
        }                
                
        ClienteDAO dao = new ClienteDAO();
        cliente.setCartao(new Cartao());
        cliente.getCartao().setDataCredito(Calendar.getInstance());
        cliente.getCartao().setSaldo(0);
        dao.salvar(cliente);
        // null carrega todos os clientes
        this.lstClientes = dao.carregarClientesPorTipo(null);    
    }
    
    /**
     * Persiste um funcionário no banco de dados
     * @throws UsuarioInvalidoException Caso o usuário já esteja cadastrado no sistema
     * @throws DadoPessoaInvalidoException Quando um ou mais dados estiverem incorretos
     */    
    public void salvarFuncionario() throws UsuarioInvalidoException, DadoPessoaInvalidoException {
        Pessoa.consistirDados((Pessoa)this.funcionario);        
        
        FuncionarioDAO dao = new FuncionarioDAO();
        dao.salvar(funcionario);
        this.lstFuncionarios = dao.carregarFuncionariosPorTipo(null);
    }    
    
    /**
     * Edita um usuário
     * @throws IOException Caso não consiga copiar o arquivo
     * @throws br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException Caso o usuário já esteja cadastrado no sistema
     * @throws br.edu.ifrs.restinga.sgru.excessao.DadoPessoaInvalidoException Quando um ou mais dados estiverem incorretos
     */
    public void editarUsuario() 
            throws IOException, UsuarioInvalidoException, DadoPessoaInvalidoException {
        this.editarUsuario(null, null, null);
    }
    /**
     * Edita um usuário
     * @param inputStream Objeto enviado pelo componente p:fileUpload
     * @param extArquivo A extensão do arquivo     
     * @param txtPath Caminho raiz da aplicação recebido
     * @throws IOException Caso não consiga copiar o arquivo
     * @throws br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException Caso o usuário já esteja cadastrado no sistema
     * @throws br.edu.ifrs.restinga.sgru.excessao.DadoPessoaInvalidoException Quando um ou mais dados estiverem incorretos
     */
    public void editarUsuario(InputStream inputStream, String extArquivo, String txtPath) 
            throws IOException, UsuarioInvalidoException, DadoPessoaInvalidoException {
        Pessoa.consistirDados(this.pessoa);        
        
        // Cliente alterou a foto
        if (inputStream != null) {
            //String caminhoFoto = "c:\\imagens\\" + this.pessoa.getMatricula() + "." + extArquivo;
            String caminhoFoto = txtPath + File.separatorChar + "imagens"+ File.separatorChar + "fotos" + File.separatorChar + this.pessoa.getMatricula() + "." + extArquivo;
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
        } else if (this.pessoa instanceof Funcionario) {
            FuncionarioDAO daoFuncionario = new FuncionarioDAO();
            daoFuncionario.salvar(funcionario);
        } else if (this.pessoa != null ) {
            PessoaDAO daoPessoa = new PessoaDAO();
            daoPessoa.salvar(pessoa);
        }        
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
}

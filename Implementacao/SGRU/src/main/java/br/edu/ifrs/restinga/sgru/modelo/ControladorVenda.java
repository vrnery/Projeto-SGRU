/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.excessao.FotoNaoEncontradaException;
import br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException;
import br.edu.ifrs.restinga.sgru.excessao.PeriodoEntreAlmocosInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.SaldoInsuficienteException;
import br.edu.ifrs.restinga.sgru.excessao.TicketInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.ValorAberturaCaixaInvalido;
import br.edu.ifrs.restinga.sgru.excessao.ValorAlmocoInvalidoException;
import br.edu.ifrs.restinga.sgru.persistencia.CaixaRUDAO;
import br.edu.ifrs.restinga.sgru.persistencia.ClienteDAO;
import java.util.Calendar;

/**
 * Classe que implementa o padrão GRASP "Controlador" para as vendas realizadas no caixa.
 * @author marcelo.lima
 */
public class ControladorVenda {      
    private CaixaRU caixaRU;      
    private Cliente cliente;

    public ControladorVenda() {
        this.caixaRU = new CaixaRU();
    }        
    
    /**
     * @return the caixaRU
     */
    public CaixaRU getCaixaRU() {
        return caixaRU;
    }

    /**
     * @param caixaRU the caixaRU to set
     */
    public void setCaixaRU(CaixaRU caixaRU) {
        this.caixaRU = caixaRU;
    }
    
    /**
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }    

    /**
     * Retorna o valor a pagar pelo almoço
     * @return O valor a pagar pelo almoço
     * @throws ArrayIndexOutOfBoundsException Caso não exista almoço realizado
     * @throws ValorAlmocoInvalidoException Caso não encontre um valor de almoço cadastrado
     */
    public double getValorPagoAlmoco() throws ArrayIndexOutOfBoundsException, ValorAlmocoInvalidoException {
        return caixaRU.ultimoAlmocoVendido().getValorAlmoco().getValorAlmoco();
    }       
    
    /**
     * Verifica se o caixa está aberto
     * @param oper O operador de caixa
     * @return True, caso o caixa esteja aberto e false, caso contrário
     */
    public boolean carregarCaixaAberto(Funcionario oper) {
        CaixaRUDAO dao = new CaixaRUDAO();
        this.caixaRU = dao.carregarCaixaAberto(oper, Calendar.getInstance());
        if (this.caixaRU != null) {
            // Carrega o valor atual do almoco
            this.caixaRU.carregarValorAtualAlmoco();
            // verifica se a lista de almocos estah preenchida
            this.caixaRU.preencherListaAlmoco();
            return true;
        } else {
            // Se nao encontrar o caixa, retorna false
            return false;
        }
    }
    
    /**
     * Carrega um caixa já aberto (com valor de fechamento zerado) ou abre um novo caixa
     * @param oper O operador que vai operar o caixa
     * @param valorAbertura O valor de abertura do caixa     
     * @throws br.edu.ifrs.restinga.sgru.excessao.ValorAberturaCaixaInvalido Caso o valor de abertura informado seja zero ou negativo     
     */
    public void realizarAberturaCaixa(Funcionario oper, double valorAbertura) throws
            ValorAberturaCaixaInvalido {
        this.getCaixaRU().realizarAberturaCaixa(oper, valorAbertura);
    }
    
    /**
     * Realiza uma venda de almoco com cartao para o cliente
     * @param matricula A matrícula do cliente
     * @throws br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException Caso a matrícula não seja localizada
     * @throws br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException Caso o usuário não seja um cliente
     * @throws br.edu.ifrs.restinga.sgru.excessao.SaldoInsuficienteException Caso o cliente não possua saldo para a compra do almoço     
     * @throws br.edu.ifrs.restinga.sgru.excessao.PeriodoEntreAlmocosInvalidoException Caso o prazo para aquisição de um outro almoço não tenha expirado     
     * @throws br.edu.ifrs.restinga.sgru.excessao.ValorAlmocoInvalidoException Caso não encontre valor de almoço
     */
    public void realizarVendaAlmocoCartao(String matricula) throws MatriculaInvalidaException,
           UsuarioInvalidoException, SaldoInsuficienteException, ValorAlmocoInvalidoException,
           PeriodoEntreAlmocosInvalidoException { 
        this.getCaixaRU().realizarVendaAlmocoCartao(matricula);
    }    
    
    /**
     * Realiza uma venda de almoço com ticket
     * @param codigo O código do ticket apresentado     
     * @throws br.edu.ifrs.restinga.sgru.excessao.TicketInvalidoException Caso o ticket esteja vencido ou não seja encontrado    
     */
    public void realizarVendaAlmocoTicket(int codigo) throws TicketInvalidoException {
        this.getCaixaRU().realizarVendaAlmocoTicket(codigo);
    }    
    
    /**
     * Confirma ou não confirmar a a venda do almoço
     * @param confirmar True, para confirmar a venda, e false para não confirmar     
     * @throws br.edu.ifrs.restinga.sgru.excessao.ValorAlmocoInvalidoException Caso não encontre um valor de almoço cadastrado
     */
    public void finalizarAlmoco(boolean confirmar) throws ValorAlmocoInvalidoException {
        this.getCaixaRU().finalizarAlmoco(confirmar);
    }    
    
     /**
     * Verifica se a foto do usuário existe     
     * @throws br.edu.ifrs.restinga.sgru.excessao.FotoNaoEncontradaException Caso não encontre a foto do cliente
     */
    public void verificarExistenciaFoto() throws FotoNaoEncontradaException  {
        this.caixaRU.ultimoAlmocoVendido().getCartao().getCliente().verificarExistenciaFoto();
    }            

    /**
     * Carrega um cliente
     * @param matricula A matrícula do cliente a ser carregado
     * @throws br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException Se a matrícula não for encontrada
     */    
    public void carregarCliente(String matricula) throws MatriculaInvalidaException {
        ClienteDAO dao = new ClienteDAO();                
        cliente = dao.carregar(matricula);
   }
}

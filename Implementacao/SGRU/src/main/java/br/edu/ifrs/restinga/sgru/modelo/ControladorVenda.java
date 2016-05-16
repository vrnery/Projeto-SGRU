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

/**
 * Classe que implementa o padrão GRASP "Controlador" para as vendas realizadas no caixa.
 * @author marcelo.lima
 */
public class ControladorVenda {    
    private CaixaRU caixaRU;    
    
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
    public boolean carregarCaixaAberto(OperadorCaixa oper) {
        this.caixaRU = CaixaRU.carregarCaixaAberto(oper);
        return this.caixaRU != null;
    }
    
    /**
     * Carrega um caixa já aberto (com valor de fechamento zerado) ou abre um novo caixa
     * @param oper O operador que vai operar o caixa
     * @param valorAbertura O valor de abertura do caixa     
     * @throws br.edu.ifrs.restinga.sgru.excessao.ValorAberturaCaixaInvalido Caso o valor de abertura informado seja zero ou negativo     
     */
    public void realizarAberturaCaixa(OperadorCaixa oper, double valorAbertura) throws
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
}

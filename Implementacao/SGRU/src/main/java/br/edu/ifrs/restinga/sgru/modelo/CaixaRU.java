/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException;
import br.edu.ifrs.restinga.sgru.excessao.PeriodoEntreAlmocosInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.SaldoInsuficienteException;
import br.edu.ifrs.restinga.sgru.excessao.TicketInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.ValorAberturaCaixaInvalido;
import br.edu.ifrs.restinga.sgru.excessao.ValorAlmocoInvalidoException;
import br.edu.ifrs.restinga.sgru.persistencia.CaixaRUDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author marcelo.lima
 */
@Entity
public class CaixaRU implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar dataAbertura;
    private double valorAbertura = 0;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar dataFechamento;
    private double valorFechamento;        
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="idOperadorCaixa")
    private OperadorCaixa operadorCaixa;                  
    // Atributos nao persistidos no banco
    @Transient
    private List<VendaAlmoco> lstVendaAlmoco = new ArrayList();        
    @Transient
    private ValorAlmoco valorAtualAlmoco;
    
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
     * @return the dataAbertura
     */
    public Calendar getDataAbertura() {
        return dataAbertura;
    }

    /**
     * @param dataAbertura the dataAbertura to set
     */
    public void setDataAbertura(Calendar dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    /**
     * @return the valorAbertura
     */
    public double getValorAbertura() {
        return valorAbertura;
    }

    /**
     * @param valorAbertura the valorAbertura to set
     */
    public void setValorAbertura(double valorAbertura) {
        this.valorAbertura = valorAbertura;
    }
    
    /**
     * @return the dataFechamento
     */
    public Calendar getDataFechamento() {
        return dataFechamento;
    }

    /**
     * @param dataFechamento the dataFechamento to set
     */
    public void setDataFechamento(Calendar dataFechamento) {
        this.dataFechamento = dataFechamento;
    }    

    /**
     * @return the valorFechamento
     */
    public double getValorFechamento() {
        return valorFechamento;
    }

    /**
     * @param valorFechamento the valorFechamento to set
     */
    public void setValorFechamento(double valorFechamento) {
        this.valorFechamento = valorFechamento;
    }

    /**
     * @return the operadorCaixa
     */
    public OperadorCaixa getOperadorCaixa() {
        return operadorCaixa;
    }

    /**
     * @param operadorCaixa the operadorCaixa to set
     */
    public void setOperadorCaixa(OperadorCaixa operadorCaixa) {
        this.operadorCaixa = operadorCaixa;
    }
    
    /**
     * @return the lstVendaAlmoco
     */
    public List<VendaAlmoco> getLstVendaAlmoco() {
        return lstVendaAlmoco;
    }
    
    /**
     * 
     * @param lstVendaAlmoco the lstVendaAlmoco to set
     */
    public void setLstVendaAlmoco(List<VendaAlmoco> lstVendaAlmoco) {
        this.lstVendaAlmoco = lstVendaAlmoco;
    }    

    /**
     * @param vendaAlmoco the lstVendaAlmoco to set
     */
    public void setVendaAlmoco(VendaAlmoco vendaAlmoco) {
        this.lstVendaAlmoco.add(vendaAlmoco);
    }    

    /**
     * @return the valorAtualAlmoco
     */
    public ValorAlmoco getValorAtualAlmoco() {
        return valorAtualAlmoco;
    }

    /**
     * @param valorAtualAlmoco the valorAtualAlmoco to set
     */
    public void setValorAtualAlmoco(ValorAlmoco valorAtualAlmoco) {
        this.valorAtualAlmoco = valorAtualAlmoco;
    }    
    
    /**
     * Carrega o valor do almoço atual
     */
    public void carregarValorAtualAlmoco() {
        this.valorAtualAlmoco = ValorAlmoco.carregarValorAtualAlmoco();
    }        
    
    /**
     * Verifica se já existe um caixa aberto, que ainda não foi fechado, para o operador naquele dia
     * @param oper O operador de caixa
     * @return Um objeto CaixaRU nulo ou contendo o caixa já aberto
     */
    public static CaixaRU carregarCaixaAberto(OperadorCaixa oper) {        
        CaixaRUDAO dao = new CaixaRUDAO();
        CaixaRU tmpCaixaRU = dao.carregarCaixaAberto(oper, Calendar.getInstance());
        if (tmpCaixaRU != null) {
            // Carrega o valor atual do almoco
            tmpCaixaRU.carregarValorAtualAlmoco();
            // verifica se a lista de almocos estah preenchida
            tmpCaixaRU.preencherListaAlmoco();
            return tmpCaixaRU;
        }
        // Se nao encontrar o caixa, retorna nulo
        return null;
    }
    
    /**
     * Carrega um caixa já aberto (com valor de fechamento zerado) ou abre um novo caixa
     * @param oper O operador que vai operar o caixa
     * @param valorAbertura O valor de abertura do caixa     
     * @throws br.edu.ifrs.restinga.sgru.excessao.ValorAberturaCaixaInvalido Caso o valor de abertura informado seja zero ou negativo     
     */
    public void realizarAberturaCaixa(OperadorCaixa oper, double valorAbertura) throws
            ValorAberturaCaixaInvalido {
        if (valorAbertura < 0) {
            throw new ValorAberturaCaixaInvalido("Valor inválido!");            
        }
        
        setOperadorCaixa(oper);
        setValorAbertura(valorAbertura);
        setDataAbertura(Calendar.getInstance());
        
        CaixaRUDAO dao = new CaixaRUDAO();
        dao.salvar(this);        
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
        
        VendaAlmoco vendaAlmoco = new VendaAlmoco();        
        vendaAlmoco.setCaixaRU(this);
        
        // Procura o cliente na base
        Cliente cliente;
        try {
            cliente = (Cliente)Pessoa.carregar(matricula);        
        } catch (ClassCastException e) {
            throw new UsuarioInvalidoException("Venda de almoço não permitida para este perfil de usuário");
        }

        // verifica se o cliente pode efetuar nova aquisição de almoço naquele momento
        vendaAlmoco.validarPeriodoEntreAlmocos(cliente.getCartao().getId());
        // Verifica se o cliente tem saldo no cartao para comprar o almoco
        cliente.getCartao().verificarSaldoCartao();                        

        // seta cartao                
        vendaAlmoco.setCartao(cliente.getCartao());
        
        // o valor a ser pago pelo almoco                    
        //vendaAlmoco.setValorAlmoco(verificarValorPagarAlmoco(cliente.getCartao()));
        vendaAlmoco.setValorAlmoco(ValorAlmoco.verificarValorPagarAlmoco(cliente.getCartao().getDataCredito(),
                getValorAtualAlmoco()));
        vendaAlmoco.setDataVenda(Calendar.getInstance());
        setVendaAlmoco(vendaAlmoco);               
    }        
    
    /**
     * Realiza uma venda de almoço com ticket
     * @param codigo O código do ticket apresentado     
     * @throws br.edu.ifrs.restinga.sgru.excessao.TicketInvalidoException Caso o ticket esteja vencido ou não seja encontrado    
     */
    public void realizarVendaAlmocoTicket(int codigo) throws TicketInvalidoException {                
        Ticket ticket = Ticket.carregarTicket(codigo);
        
        if (ticket == null) {
            throw new TicketInvalidoException("Ticket não encontrado!");
        } else if (ticket.getValor() != getValorAtualAlmoco().getValorAlmoco()) {
            throw new TicketInvalidoException("Ticket vencido!");
        } else {
            // Cria o objeto VendaAlmoco
            VendaAlmoco vendaAlmoco = new VendaAlmoco();
            vendaAlmoco.setCaixaRU(this);
            
            // Coloca o ticket como utilizado
            ticket.setDataUtilizado(vendaAlmoco.getDataVenda());

            // seta o ticket
            vendaAlmoco.setTicket(ticket);
            vendaAlmoco.setValorAlmoco(getValorAtualAlmoco());
            vendaAlmoco.setDataVenda(Calendar.getInstance());
            setVendaAlmoco(vendaAlmoco);
        }
    }            
    
    /**
     * Confirma ou não confirmar a a venda do almoço
     * @param confirmar True, para confirmar a venda, e false para não confirmar     
     * @throws br.edu.ifrs.restinga.sgru.excessao.ValorAlmocoInvalidoException Caso não encontre um valor de almoço cadastrado
     */
    public void finalizarAlmoco(boolean confirmar) throws ValorAlmocoInvalidoException {
                    
        VendaAlmoco ultimoAlmocoVendido = getLstVendaAlmoco().get(getLstVendaAlmoco().size()-1);        

        if (confirmar) {
            if(ultimoAlmocoVendido.getCartao() != null){                
                // Desconta o valor do almoco do aluno
                ultimoAlmocoVendido.getCartao().setSaldo(ultimoAlmocoVendido.getCartao().getSaldo() -
                        ultimoAlmocoVendido.getValorAlmoco().getValorAlmoco());
            }else{
                ultimoAlmocoVendido.getTicket().setDataUtilizado((Calendar) ultimoAlmocoVendido.getDataVenda());                    
            }

            // Salva a venda            
            ultimoAlmocoVendido.salvarVendaAlmoco();
        } else {
            // exclui a venda da lista
            getLstVendaAlmoco().remove(ultimoAlmocoVendido);
        }            
    }        
    
    /**
     * Realiza o fechamento de caixa
     * @throws ValorAlmocoInvalidoException Caso encontre algum valor de almoco invalido
     */
    public void realizarFechamentoCaixa() throws ValorAlmocoInvalidoException {
        // calcula a soma de todos os almocos vendidos no dia e
        // seta o valor do fechamento                            
        valorFechamento = 0;
        for (VendaAlmoco vendaAlmoco : getLstVendaAlmoco()) {
            valorFechamento += vendaAlmoco.getValorAlmoco().getValorAlmoco();
        }        
        // Soma o valor de abertura no fechamento
        valorFechamento += valorAbertura;
        setDataFechamento(Calendar.getInstance());            
        
        CaixaRUDAO dao = new CaixaRUDAO();
        dao.salvar(this);        
    }
    
    /**
     * Carrega os almoços vendidos no dia para determinado caixa
     */
    public void preencherListaAlmoco() {
        if (getLstVendaAlmoco().isEmpty()) {
            // realiza a consulta para verificar se existe alguma venda para o dia            
            this.lstVendaAlmoco = VendaAlmoco.carregaListaVendasDia(id);
        }
    }        
    
    /**
     * Extrai da lista o último almoço vendido no caixa
     * @return O último almoço vendido no caixa
     */
    public VendaAlmoco ultimoAlmocoVendido() throws ArrayIndexOutOfBoundsException {
        return lstVendaAlmoco.get(lstVendaAlmoco.size()-1);
    }        
}

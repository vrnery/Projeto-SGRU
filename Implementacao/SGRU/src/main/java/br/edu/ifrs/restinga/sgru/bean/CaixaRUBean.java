/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.excessao.ValorAlmocoInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException;
import br.edu.ifrs.restinga.sgru.excessao.PeriodoEntreAlmocosInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.SaldoInsuficienteException;
import br.edu.ifrs.restinga.sgru.excessao.TicketInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException;
import br.edu.ifrs.restinga.sgru.modelo.CaixaRU;
import br.edu.ifrs.restinga.sgru.modelo.Cartao;
import br.edu.ifrs.restinga.sgru.modelo.Cliente;
import br.edu.ifrs.restinga.sgru.modelo.OperadorCaixa;
import br.edu.ifrs.restinga.sgru.modelo.Pessoa;
import br.edu.ifrs.restinga.sgru.modelo.Ticket;
import br.edu.ifrs.restinga.sgru.modelo.ValorAlmoco;
import br.edu.ifrs.restinga.sgru.modelo.VendaAlmoco;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import br.edu.ifrs.restinga.sgru.persistencia.CaixaRUDAO;
import br.edu.ifrs.restinga.sgru.persistencia.PessoaDAO;
import br.edu.ifrs.restinga.sgru.persistencia.TicketDAO;
import br.edu.ifrs.restinga.sgru.persistencia.VendaAlmocoDAO;
import java.util.Calendar;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author marcelo.lima
 */
@ManagedBean
@SessionScoped
public class CaixaRUBean {
    private CaixaRU caixaRU;
    private final CaixaRUDAO dao = new CaixaRUDAO();        
    
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
     * Solicita à camada de persistência o cadastro de um CaixaRU
     */
    public void salvar() {
        dao.salvar(caixaRU);
        enviarMensagem(FacesMessage.SEVERITY_INFO, "Caixa cadastrado com sucesso!");
    }
            
    /**
     * Verifica se já existe um caixa aberto, que ainda não foi fechado, para o operador naquele dia
     * @param oper O operador de caixa
     */
    public void isCaixaAberto(OperadorCaixa oper) {
        caixaRU = dao.carregarCaixaAberto(oper, Calendar.getInstance());
        
        if (caixaRU != null) {
            // Verifica se o valor atual do almoco estah setado
            if (caixaRU.getValorAtualAlmoco() == null) {
                caixaRU.carregarValorAtualAlmoco();
            }        

            // verifica se a lista de almocos estah preenchida
            caixaRU.preencherListaAlmoco();
        }
    }     
    
    /**
     * Carrega um caixa já aberto (com valor de fechamento zerado) ou abre um novo caixa
     * @param oper O operador que vai operar o caixa
     * @param valorAbertura O valor de abertura do caixa
     * @return A próxima página a ser exibida pelo usuário
     */
    public String realizarAberturaCaixa(OperadorCaixa oper, double valorAbertura) {
        if (valorAbertura < 0) {
            enviarMensagem(FacesMessage.SEVERITY_INFO, "Valor inválido!");
            return null;
        }
        
        caixaRU = new CaixaRU();
        caixaRU.setOperadorCaixa(oper);
        caixaRU.setValorAbertura(valorAbertura);
        caixaRU.setDataAbertura(Calendar.getInstance());
        dao.salvar(caixaRU);
        
        // Verifica se o valor atual do almoco estah setado
        if (caixaRU.getValorAtualAlmoco() == null) {
            caixaRU.carregarValorAtualAlmoco();
        }        
        
        // verifica se a lista de almocos estah preenchida
        caixaRU.preencherListaAlmoco();
        return "caixa";
    }    
    
    /**
     * Realiza uma venda de almoco com cartao para o cliente
     * @param matricula A matricula do cliente que está realizando a compra
     * @return Retorna a próxima página a ser exibida para o operador     
     */
    public String realizarVendaAlmocoCartao(String matricula) {
        String retorno = "confirmarVendaCartao";
        try {
            PessoaDAO pessoaDAO = new PessoaDAO();
            Pessoa pessoa = pessoaDAO.carregar(matricula);        

            // Apenas clientes podem almocar com cartao
            if (!(pessoa instanceof Cliente)) {
                throw new UsuarioInvalidoException("Não é permitida a venda de almoço para este usuário");            
            }        
            
            Cliente cliente = (Cliente) pessoa;                        
            // Verifica se o cliente tem saldo e está autorizado a almoçar naquele momento
            autorizarVendaAlmoco(cliente);

            // Cria o objeto VendaAlmoco            
            VendaAlmoco vendaAlmoco = new VendaAlmoco();
            vendaAlmoco.setCaixaRU(caixaRU);

            // seta cartao                
            vendaAlmoco.setCartao(cliente.getCartao());
            if (!cliente.verificarExistenciaFoto()) {
                enviarMensagem(FacesMessage.SEVERITY_WARN, 
                        "A apresentação de um documento de identidade com foto é obrigatória!");
            }

            // o valor a ser pago pelo almoco                    
            vendaAlmoco.setValorAlmoco(verificarValorPagarAlmoco(vendaAlmoco.getCartao()));
            vendaAlmoco.setDataVenda(Calendar.getInstance());
            caixaRU.setVendaAlmoco(vendaAlmoco);               
        } catch (MatriculaInvalidaException | UsuarioInvalidoException | 
                SaldoInsuficienteException | PeriodoEntreAlmocosInvalidoException e) {
            retorno = "caixa";
            enviarMensagem(FacesMessage.SEVERITY_INFO, e.getMessage());
        }
                
        return retorno;
    }    
    
    /**
     * Realiza uma venda de almoço com ticket
     * @param codigo O código do ticket apresentado
     * @return  A próxima página a ser visualizada pelo operador de caixa
     */
    public String realizarVendaAlmocoTicket(int codigo) {
        String retorno = "confirmarVendaTicket";
        TicketDAO ticketDAO = new TicketDAO();
        Ticket ticket = ticketDAO.usarTicket(codigo);
        
        try {
            if (ticket == null) {
                throw new TicketInvalidoException("Ticket não encontrado!");
            } else if (ticket.getValor() != caixaRU.getValorAtualAlmoco().getValorAlmoco()) {
                throw new TicketInvalidoException("Ticket vencido!");
            } else {
                // Cria o objeto VendaAlmoco
                VendaAlmoco vendaAlmoco = new VendaAlmoco();
                vendaAlmoco.setCaixaRU(caixaRU);
            
                // Coloca o ticket como utilizado
                ticket.setDataUtilizado(vendaAlmoco.getDataVenda());
                
                // seta o ticket
                vendaAlmoco.setTicket(ticket);
                vendaAlmoco.setValorAlmoco(caixaRU.getValorAtualAlmoco());
                vendaAlmoco.setDataVenda(Calendar.getInstance());
                caixaRU.setVendaAlmoco(vendaAlmoco);
            }
        } catch (TicketInvalidoException e) {
            enviarMensagem(FacesMessage.SEVERITY_INFO, e.getMessage());
            retorno = "caixa";
        }
        return retorno;
    }        
    
    /**
     * Verifica se o cliente possui saldo no cartão. Caso negativo, verifica se existe recarga
     * para o cartao e transfere o saldo. O almoco eh autorizado ainda que o valor do saldo do 
     * cartão seja menor que o valor do almoco. Não será autorizado, no entanto, a venda para
     * cartões que tenham saldo negativos ou ainda iguais a zero
     * @param cliente O cliente que está comprando o almoco     
     * @throws br.edu.ifrs.restinga.sgru.excessao.SaldoInsuficienteException Caso o cliente não possua saldo para a compra do almoço     
     * @throws br.edu.ifrs.restinga.sgru.excessao.PeriodoEntreAlmocosInvalidoException Caso o prazo para aquisição de um outro almoço não tenha expirado
     */    
    public void autorizarVendaAlmoco(Cliente cliente) throws SaldoInsuficienteException,
            PeriodoEntreAlmocosInvalidoException {            
        // verifica se o aluno pode efetuar nova aquisição de almoço naquele momento
        VendaAlmoco.validarPeriodoEntreAlmocos(cliente.getCartao().getId());
        // Verifica se o cliente tem saldo no cartao para comprar o almoco
        cliente.getCartao().verificarSaldoCartao();        
    }
            
    /**
     * Confirma ou não confirmar a a venda do almoço
     * @param confirmar True, para confirmar a venda, e false para não confirmar
     * @return A próxima página que o operador será redirecionado
     */
    public String finalizarAlmoco(boolean confirmar) {
        try {
            VendaAlmocoDAO daoVendaAlmoco = new VendaAlmocoDAO();            
            VendaAlmoco ultimoAlmocoVendido = this.getCaixaRU().getLstVendaAlmoco().get(this.getCaixaRU().getLstVendaAlmoco().size()-1);        

            if (confirmar) {
                if(ultimoAlmocoVendido.getCartao() != null){                
                    // Desconta o valor do almoco do aluno
                    ultimoAlmocoVendido.getCartao().setSaldo(ultimoAlmocoVendido.getCartao().getSaldo()-ultimoAlmocoVendido.getValorAlmoco().getValorAlmoco());
                }else{
                    ultimoAlmocoVendido.getTicket().setDataUtilizado((Calendar) ultimoAlmocoVendido.getDataVenda());                    
                }
                
                // Salva a venda
                daoVendaAlmoco.salvar(ultimoAlmocoVendido);
            } else {
                // exclui a venda da lista
                this.getCaixaRU().getLstVendaAlmoco().remove(ultimoAlmocoVendido);
            }            
        } catch (ValorAlmocoInvalidoException e) {
            enviarMensagem(FacesMessage.SEVERITY_INFO, e.getMessage());
            return "venda";
        }
        return "caixa";
    }
    
    /**
     * Realiza o fechamento de caixa. Esse método já persiste os 
     * dados do fechamento na base de dados
     * @return A página para onde o operador será redirecionado
     */
    public String realizarFechamentoCaixa() {
        try {            
            caixaRU.realizarFechamentoCaixa();
        } catch (ValorAlmocoInvalidoException e) {
            enviarMensagem(FacesMessage.SEVERITY_INFO, e.getMessage());
            return null;
        }              
        return "index";
    }
    
    /**
     * Verifica o valor a ser pago pelo almoço com base na data do crédito da recarga no cartão
     * @param cartao O cartão utilizado para pagar o almoço     
     * @return Um objeto ValorAlmoco com o valor a ser pago pelo almoco
     */
    public ValorAlmoco verificarValorPagarAlmoco(Cartao cartao) {
        ValorAlmoco valorAlmoco = new ValorAlmoco();
        
        if (cartao != null) {            
            try {
                // verifica a necessidade de atualizar o valor do almoco
                valorAlmoco = valorAlmoco.verificarValorPagarAlmoco(cartao.getDataCredito(),
                        caixaRU.getValorAtualAlmoco());
            } catch (ValorAlmocoInvalidoException ex) {
                // Nao encontrou o valor do almoco para a data de credito do cartao, entao
                // assume valor atual do almoco
                valorAlmoco = caixaRU.getValorAtualAlmoco();
            }
        }        
        return valorAlmoco;
    }
    
    /**
     * Envia à viewer uma mensagem com o status da operação
     * @param sev A severidade da mensagem
     * @param msg A mensagem a ser apresentada
     */
    private void enviarMensagem(FacesMessage.Severity sev, String msg) {
        FacesContext context = FacesContext.getCurrentInstance();        
        context.addMessage(null, new FacesMessage(sev, msg, ""));
    }        
}

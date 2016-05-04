/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.excessao.ValorAlmocoInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException;
import br.edu.ifrs.restinga.sgru.excessao.RecargaNaoEncontradaException;
import br.edu.ifrs.restinga.sgru.excessao.SaldoInsuficienteException;
import br.edu.ifrs.restinga.sgru.excessao.TicketInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException;
import br.edu.ifrs.restinga.sgru.modelo.Aluno;
import br.edu.ifrs.restinga.sgru.modelo.CaixaRU;
import br.edu.ifrs.restinga.sgru.modelo.OperadorCaixa;
import br.edu.ifrs.restinga.sgru.modelo.Pessoa;
import br.edu.ifrs.restinga.sgru.modelo.Professor;
import br.edu.ifrs.restinga.sgru.modelo.Ticket;
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
            if (caixaRU.getLstVendaAlmoco().isEmpty()) {
                // realiza a consulta para verificar se existe alguma venda para o dia
                VendaAlmocoDAO daoVendaAlmoco = new VendaAlmocoDAO();
                caixaRU.setLstVendaAlmoco(daoVendaAlmoco.carregar(caixaRU.getId(),Calendar.getInstance()));
            }                    
        }
    }     
    
    /**
     * Carrega um caixa já aberto (com valor de fechamento zerado) ou abre um novo caixa
     * @param oper O operador que vai operar o caixa
     * @param valorAbertura O valor de abertura do caixa
     * @return A próxima página a ser exibida pelo usuário
     */
    public String realizarAberturaCaixa(OperadorCaixa oper, double valorAbertura) {                       
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
        if (caixaRU.getLstVendaAlmoco().isEmpty()) {
            // realiza a consulta para verificar se existe alguma venda para o dia
            VendaAlmocoDAO daoVendaAlmoco = new VendaAlmocoDAO();
            caixaRU.setLstVendaAlmoco(daoVendaAlmoco.carregar(caixaRU.getId(),Calendar.getInstance()));
        }                
        return "caixa";
    }    
    
    /**
     * Realiza uma venda de almoco com cartao para o cliente
     * @param matricula A matricula do cliente que está realizando a compra
     * @return Retorna a próxima página a ser exibida para o operador     
     */
    public String realizarVendaAlmocoCartao(String matricula) {
        String retorno = "confirmarVenda";
        try {
            PessoaDAO pessoaDAO = new PessoaDAO();
            Pessoa pessoa = pessoaDAO.carregar(matricula);        

            // Apenas professores e alunos podem almocar com cartao
            if (!(pessoa instanceof Aluno) && !(pessoa instanceof Professor)) {            
                throw new UsuarioInvalidoException("Usuário não é aluno nem professor!");            
            }                

            // Verifica se o cartao do cliente tem saldo                
            boolean autorizado;
            try {
                autorizado = autorizaVendaAlmoco(pessoa);

                // Cria o objeto VendaAlmoco            
                if (autorizado) {
                    VendaAlmoco vendaAlmoco = new VendaAlmoco();
                    vendaAlmoco.setCaixaRU(caixaRU);

                    // seta cartao                
                    if (pessoa instanceof Aluno) {
                        vendaAlmoco.setCartao(((Aluno)pessoa).getCartao());                    
                    } else if (pessoa instanceof Professor) {
                        vendaAlmoco.setCartao(((Professor)pessoa).getCartao());
                    }
                    // O metodo setValorAlmoco verifica, com base na dataCredito do cartao
                    // o valor a ser pago pelo almoco
                    vendaAlmoco.setValorAlmoco(caixaRU.getValorAtualAlmoco());        
                    vendaAlmoco.setDataVenda(Calendar.getInstance());
                    caixaRU.setVendaAlmoco(vendaAlmoco);               
                } else {
                    throw new SaldoInsuficienteException("Saldo insuficiente!");
                }            
            } catch (RecargaNaoEncontradaException e) {
                throw new SaldoInsuficienteException("Saldo insuficiente!");
            }      
        } catch (MatriculaInvalidaException | UsuarioInvalidoException | 
                SaldoInsuficienteException e) {
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
        String retorno = "caixa";
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
            
                // seta o ticket
                vendaAlmoco.setTicket(ticket);
                vendaAlmoco.setValorAlmoco(caixaRU.getValorAtualAlmoco());
                vendaAlmoco.setDataVenda(Calendar.getInstance());
                caixaRU.setVendaAlmoco(vendaAlmoco);
            
                // Coloca o ticket como utilizado
                ticket.setDataUtilizado(vendaAlmoco.getDataVenda());
            }
        } catch (TicketInvalidoException e) {
            enviarMensagem(FacesMessage.SEVERITY_INFO, e.getMessage());
            retorno = "confirmarVenda";
        }
        return retorno;
    }    
    
    /**
     * Verifica se o cliente possui saldo no cartão. Caso negativo, verifica se existe recarga
     * para o cartao e transfere o saldo. O almoco eh autorizado ainda que o valor do saldo do 
     * cartão seja menor que o valor do almoco. Não será autorizado, no entanto, a venda para
     * cartões que tenham saldo negativos ou ainda iguais a zero
     * @param pessoa O cliente que está comprando o almoco
     * @return True, para almoço autorizado e false para almoço não autorizado
     * @throws RecargaNaoEncontradaException 
     */
    private boolean autorizaVendaAlmoco(Pessoa pessoa) throws 
            RecargaNaoEncontradaException {
        boolean autorizado = true;
        // primeiramente, verifica se eh necessario atualizar o saldo da
        // cartao da pessoa        
        if (pessoa instanceof Aluno) {            
            Aluno aluno = (Aluno) pessoa;
            if (aluno.getCartao().getSaldo() <= 0) {
                // eh necessario atualizar o saldo
                aluno.getCartao().transferirRecargaParaCartao();
                
                // Caso recarga zerada
                if (aluno.getCartao().getSaldo() <= 0) {
                    autorizado = false;
                }
            }
        } else if (pessoa instanceof Professor) {
            Professor professor = (Professor) pessoa;
            if (professor.getCartao().getSaldo() <= 0) {
                // eh necessario atualizar o saldo
                professor.getCartao().transferirRecargaParaCartao();
                
                // Caso recarga zerada
                if (professor.getCartao().getSaldo() <= 0) {
                    autorizado = false;
                }
            }
        }
        return autorizado;
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
                // Desconta o valor do almoco do aluno
                ultimoAlmocoVendido.getCartao().setSaldo(ultimoAlmocoVendido.getCartao().getSaldo()-ultimoAlmocoVendido.getValorAlmoco().getValorAlmoco());
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
            // calcula a soma de todos os almocos vendidos no dia e
            // seta o valor do fechamento            
            double valorFechamento = 0;
            for (VendaAlmoco vendaAlmoco : caixaRU.getLstVendaAlmoco()) {
                valorFechamento += vendaAlmoco.getValorAlmoco().getValorAlmoco();
            }
            caixaRU.setValorFechamento(valorFechamento);
            caixaRU.setDataFechamento(Calendar.getInstance());            
            dao.salvar(caixaRU);            
        } catch (ValorAlmocoInvalidoException e) {
            enviarMensagem(FacesMessage.SEVERITY_INFO, e.getMessage());
            return "";
        }                
        return "index";
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

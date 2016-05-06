/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.excessao.ValorAlmocoInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException;
import br.edu.ifrs.restinga.sgru.excessao.PeriodoEntreAlmocosInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.RecargaNaoEncontradaException;
import br.edu.ifrs.restinga.sgru.excessao.SaldoInsuficienteException;
import br.edu.ifrs.restinga.sgru.excessao.TicketInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException;
import br.edu.ifrs.restinga.sgru.modelo.Aluno;
import br.edu.ifrs.restinga.sgru.modelo.CaixaRU;
import br.edu.ifrs.restinga.sgru.modelo.Cartao;
import br.edu.ifrs.restinga.sgru.modelo.OperadorCaixa;
import br.edu.ifrs.restinga.sgru.modelo.Pessoa;
import br.edu.ifrs.restinga.sgru.modelo.Professor;
import br.edu.ifrs.restinga.sgru.modelo.Ticket;
import br.edu.ifrs.restinga.sgru.modelo.ValorAlmoco;
import br.edu.ifrs.restinga.sgru.modelo.VendaAlmoco;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import br.edu.ifrs.restinga.sgru.persistencia.CaixaRUDAO;
import br.edu.ifrs.restinga.sgru.persistencia.PessoaDAO;
import br.edu.ifrs.restinga.sgru.persistencia.TicketDAO;
import br.edu.ifrs.restinga.sgru.persistencia.ValorAlmocoDAO;
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
    private static final int NUM_MAX_MINUTOS_ULTIMO_ALMOCO = 270;
    private static final int NUM_DIAS_VALIDADE_RECARGA = 60;
    
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
        String retorno = "confirmarVendaCartao";
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
                        if (!((Aluno)pessoa).verificarExistenciaFoto()) {
                            enviarMensagem(FacesMessage.SEVERITY_WARN, 
                                    "A apresentação de um documento de identidade com foto é obrigatória!");
                        }
                    } else if (pessoa instanceof Professor) {
                        vendaAlmoco.setCartao(((Professor)pessoa).getCartao());
                        if (!((Professor)pessoa).verificarExistenciaFoto()) {
                            enviarMensagem(FacesMessage.SEVERITY_WARN, 
                                    "A apresentação de um documento de identidade com foto é obrigatória!");
                        }                        
                    }
                    
                    // o valor a ser pago pelo almoco                    
                    vendaAlmoco.setValorAlmoco(verificarValorPagarAlmoco(vendaAlmoco.getCartao()));
                    vendaAlmoco.setDataVenda(Calendar.getInstance());
                    caixaRU.setVendaAlmoco(vendaAlmoco);               
                } else {
                    throw new SaldoInsuficienteException("Saldo insuficiente!");
                }            
            } catch (RecargaNaoEncontradaException e) {
                throw new SaldoInsuficienteException("Saldo insuficiente!");
            }      
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
     * @param pessoa O cliente que está comprando o almoco
     * @return True, para almoço autorizado e false para almoço não autorizado
     * @throws RecargaNaoEncontradaException 
     */
    private boolean autorizaVendaAlmoco(Pessoa pessoa) throws RecargaNaoEncontradaException, 
            PeriodoEntreAlmocosInvalidoException, PeriodoEntreAlmocosInvalidoException {
        boolean autorizado = true;
        // primeiramente, verifica se eh necessario atualizar o saldo da
        // cartao da pessoa        
        if (pessoa instanceof Aluno) {            
            Aluno aluno = (Aluno) pessoa;
            // Verifica se o aluno já fez alguma compra em um determinado 
            // intervalo de tempo
            // ultimo almoco do aluno realizado naquele dia
            VendaAlmoco ultimoAlmocoAluno = null;
            for (VendaAlmoco vendaAlmoco : getCaixaRU().getLstVendaAlmoco()) {                
                // Procura almoco vendido para o aluno
                try {
                    if (vendaAlmoco.getCartao().getAluno().getId() == aluno.getId()) {                    
                        if (ultimoAlmocoAluno == null) {                        
                            ultimoAlmocoAluno = vendaAlmoco;
                        } else {
                            // Ja havia encontrado almoco anteriormente na lista
                            if (ultimoAlmocoAluno.getDataVenda().after(vendaAlmoco.getDataVenda())) {
                                ultimoAlmocoAluno = vendaAlmoco;
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    // o almoco foi vendido para um professor
                }
            }
            
            // Se o aluno jah almocou, verifica se pode comprar novamente
            if (ultimoAlmocoAluno != null) {
                int numMinutos = (int) ((Calendar.getInstance().getTimeInMillis() - ultimoAlmocoAluno.getDataVenda().getTimeInMillis())* 0.0000166667);
                if (numMinutos <= NUM_MAX_MINUTOS_ULTIMO_ALMOCO) {
                    throw new PeriodoEntreAlmocosInvalidoException("Período entre almoços menor que " + NUM_MAX_MINUTOS_ULTIMO_ALMOCO + " minutos!");
                }
            }
            
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
            // Verifica se o aluno já fez alguma compra em um determinado 
            // intervalo de tempo
            // ultimo almoco do aluno realizado naquele dia
            VendaAlmoco ultimoAlmocoProfessor = null;
            for (VendaAlmoco vendaAlmoco : getCaixaRU().getLstVendaAlmoco()) {
                try {
                    // Procura almoco vendido para o aluno
                    if (vendaAlmoco.getCartao().getProfessor().getId() == professor.getId()) {                    
                        if (ultimoAlmocoProfessor == null) {                        
                            ultimoAlmocoProfessor = vendaAlmoco;
                        } else {
                            // Ja havia encontrado almoco anteriormente na lista
                            if (ultimoAlmocoProfessor.getDataVenda().after(vendaAlmoco.getDataVenda())) {
                                ultimoAlmocoProfessor = vendaAlmoco;
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    // o almoco foi vendido para um aluno
                }
            }
            
            // Se o professor jah almocou, verifica se pode comprar novamente
            if (ultimoAlmocoProfessor != null) {
                int numMinutos = (int) ((Calendar.getInstance().getTimeInMillis() - ultimoAlmocoProfessor.getDataVenda().getTimeInMillis())*0.0000166667);
                if (numMinutos <= NUM_MAX_MINUTOS_ULTIMO_ALMOCO) {
                    throw new PeriodoEntreAlmocosInvalidoException("Período entre almoços menor que " + NUM_MAX_MINUTOS_ULTIMO_ALMOCO + " minutos!");
                }
            }            
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
        ValorAlmoco valorAlmoco = null;
        // verifica a necessidade de atualizar o valor do almoco
        if (cartao != null) {            
            // Se a data do cretito no cartao for menor que a data do valor
            // do almoco atual, verifica se eh necessario atualizar valor            
            if (caixaRU.getValorAtualAlmoco().getDataValor().after(cartao.getDataCredito())) {
                long miliSecondsCartao = cartao.getDataCredito().getTimeInMillis();            
                int numDias = (int) ((Calendar.getInstance().getTimeInMillis() - miliSecondsCartao)/86400000L);                
                
                // Os creditos podem ser usados em ateh 60 dias
                if (numDias <= NUM_DIAS_VALIDADE_RECARGA) {
                   ValorAlmocoDAO daoValorAlmoco = new ValorAlmocoDAO();
                   valorAlmoco = daoValorAlmoco.getValorAlmocoPorData(cartao.getDataCredito());                   
                }
            }
        }
        
        // Se nao encontrar valor de almoco para a data enviada, seta o valor do almoco
        // com o valor atual
        if (valorAlmoco == null) {
            // Inicialmente seta o valor atual do almoco
            valorAlmoco = caixaRU.getValorAtualAlmoco();
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

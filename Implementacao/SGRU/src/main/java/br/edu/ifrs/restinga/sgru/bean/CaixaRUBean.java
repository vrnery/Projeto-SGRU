/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException;
import br.edu.ifrs.restinga.sgru.excessao.RecargaNaoEncontradaException;
import br.edu.ifrs.restinga.sgru.excessao.SaldoInsuficienteException;
import br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException;
import br.edu.ifrs.restinga.sgru.modelo.Aluno;
import br.edu.ifrs.restinga.sgru.modelo.CaixaRU;
import br.edu.ifrs.restinga.sgru.modelo.Pessoa;
import br.edu.ifrs.restinga.sgru.modelo.Professor;
import br.edu.ifrs.restinga.sgru.modelo.VendaAlmoco;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import br.edu.ifrs.restinga.sgru.persistencia.CaixaRUDAO;
import br.edu.ifrs.restinga.sgru.persistencia.PessoaDAO;
import br.edu.ifrs.restinga.sgru.persistencia.VendaAlmocoDAO;
import java.util.Calendar;

/**
 *
 * @author marcelo.lima
 */
public class CaixaRUBean {
    private CaixaRU caixaRU = new CaixaRU();
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
        //enviarMensagem(FacesMessage.SEVERITY_INFO, "Caixa cadastrado com sucesso!");
    }
    
    /**
     * Solicita a pesquisa de um CaixaRU com o id informado à camada de persistência
     * @param dataAbertura A data de abertura do CaixaRU a ser pesquisado
     */
    public void carregar(Calendar dataAbertura) {
        caixaRU = dao.carregar(dataAbertura);
    }        
    
    /**
     * Realiza uma venda de almoco com cartao para o cliente
     * @param matricula A matricula do cliente que está realizando a compra
     * @throws br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException Se não encontrar matrícula cadastrada     
     * @throws UsuarioInvalidoException Caso o cliente não seja nem aluno nem professor
     * @throws br.edu.ifrs.restinga.sgru.excessao.SaldoInsuficienteException Se o cliente não possuir saldo    
     */
    public void realizarVendaAlmocoCartao(String matricula) throws MatriculaInvalidaException,
            UsuarioInvalidoException, SaldoInsuficienteException {
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

                System.out.println("Valor atual do almoco: " + caixaRU.getValorAtualAlmoco().getValorAlmoco());
                System.out.println("Valor do almoco pago pelo cliente: " + vendaAlmoco.getValorAlmoco().getValorAlmoco());        
                
                // Desconta o valor do almoco do saldo do cliente
                if (pessoa instanceof Aluno) {
                    ((Aluno)pessoa).getCartao().setSaldo(((Aluno)pessoa).getCartao().getSaldo() - vendaAlmoco.getValorAlmoco().getValorAlmoco());
                } else if (pessoa instanceof Professor) {
                    ((Professor)pessoa).getCartao().setSaldo(((Professor)pessoa).getCartao().getSaldo() - vendaAlmoco.getValorAlmoco().getValorAlmoco());
                }
            } else {
                throw new SaldoInsuficienteException("Saldo insuficiente!");
            }            
        } catch (RecargaNaoEncontradaException e) {
            throw new SaldoInsuficienteException("Saldo insuficiente!");
        }        
    }
    
    // Apos execucao, o metodo realizarVendaAlmocoCartao deve retornar para o operador
    // de caixa a foto, o nome completo, o saldo e o valor do almoco a pagar pelo
    // aluno. O operador, entao, confirma ou nao a venda. Caso confirme, da-se um 
    // commit na transaco, caso nao confirme, da-se um rollback na transacao
    public void finalizarAlmoco(boolean confirmar) {
        if (confirmar) {            
            // commit na transacao
            VendaAlmocoDAO daoVendaAlmoco = new VendaAlmocoDAO();
            daoVendaAlmoco.salvar(this.getCaixaRU().getLstVendaAlmoco().get(this.getCaixaRU().getLstVendaAlmoco().size()-1));
        } else {
            // rollback na trasacao
            this.getCaixaRU().setVendaAlmoco(null);
        }
    }
    
    /**
     * Verifica se o cliente possuir saldo no cartão. Caso negativo, verifica se existe recarga
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
     * Realiza o fechamento de caixa. Esse método já persiste os 
     * dados do fechamento na base de dados
     */
    public void realizarFechamentoCaixa() {
        // calcula a soma de todos os almocos vendidos no dia
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

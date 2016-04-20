/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.modelo.Aluno;
import br.edu.ifrs.restinga.sgru.modelo.CaixaRU;
import br.edu.ifrs.restinga.sgru.modelo.VendaAlmoco;
import br.edu.ifrs.restinga.sgru.persistencia.AlunoDAO;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import br.edu.ifrs.restinga.sgru.persistencia.CaixaRUDAO;

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
     * @param id O id do CaixaRU a ser pesquisado
     */
    public void carregar(int id) {
        caixaRU = dao.carregar(id);
    }        
    
    /**
     * Realiza uma venda de almoco com cartao para o cliente
     * @param matricula A matricula do cliente que está realizando a compra
     * @throws NullPointerException Se não encontrar matrícula cadastrada
     */
    public void realizarVendaAlmocoCartao(String matricula) throws NullPointerException {
        AlunoDAO alunoDAO = new AlunoDAO();
        Aluno aluno = alunoDAO.carregar(matricula);        
        
        if (aluno == null) {
            // Criar classe excessao CartaoNaoEncontradoException?
            throw new NullPointerException("Matrícula não encontrada");
        }
        
        // Cria o objeto VendaAlmoco
        VendaAlmoco vendaAlmoco = new VendaAlmoco();
        vendaAlmoco.setCaixaRU(caixaRU);
        vendaAlmoco.setCartao(aluno.getCartao());
        // Seta o valor atual do almoco por default
        // No metodo set da classe ValorAlmoco verifica se o 
        // valor deve ser atualizado
        vendaAlmoco.setValorAlmoco(caixaRU.getValorAtualAlmoco());        
        caixaRU.setVendaAlmoco(vendaAlmoco);
        
        System.out.println("Valor atual do almoco: " + caixaRU.getValorAtualAlmoco().getValorAlmoco());
        System.out.println("Valor do almoco pago pelo aluno: " + vendaAlmoco.getValorAlmoco().getValorAlmoco());        
    }
    
    // Apos execucao, o metodo realizarVendaAlmocoCartao deve retornar para o operador
    // de caixa a foto, o nome completo, o saldo e o valor do almoco a pagar pelo
    // aluno. O operador, entao, confirma ou nao a venda. Caso confirme, da-se um 
    // commit na transaco, caso nao confirme, da-se um rollback na transacao
    public void finalizarAlmoco(boolean confirmar) {
        if (confirmar) {
            // commit na transacao
        } else {
            // rollback na trasacao
        }
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

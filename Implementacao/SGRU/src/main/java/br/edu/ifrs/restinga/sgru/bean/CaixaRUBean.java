/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.modelo.Aluno;
import br.edu.ifrs.restinga.sgru.modelo.CaixaRU;
import br.edu.ifrs.restinga.sgru.modelo.VendaAlmoco;
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
     * Realiza uma venda de almoco com cartao para o aluno
     * @param aluno O aluno que está realizando a compra
     */
    public void realizarVendaAlmocoCartao(Aluno aluno) {        
        // Cria o objeto VendaAlmoco
        VendaAlmoco vendaAlmoco = new VendaAlmoco();
        vendaAlmoco.setCaixaRU(caixaRU);
        vendaAlmoco.setCartao(aluno.getCartao());
        // Seta o valor atual do almoco por default
        // No metodo set da classe ValorAlmoco verifica se o 
        // valor deve ser atualizado
        vendaAlmoco.setValorAlmoco(caixaRU.getValorAtualAlmoco());
        
        System.out.println("Valor atual do almoco: " + caixaRU.getValorAtualAlmoco());
        System.out.println("Valor do almoco pago pelo aluno: " + vendaAlmoco.getValorAlmoco().getValorAlmoco());        
    }
    
    /**
     * Realiza o fechamento de caixa. Esse método já persiste os 
     * dados do fechamento na base de dados
     */
    public void realizarFechamentoCaixa() {
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

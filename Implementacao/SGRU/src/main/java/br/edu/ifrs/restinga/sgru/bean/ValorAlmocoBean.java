/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.modelo.ValorAlmoco;
import br.edu.ifrs.restinga.sgru.persistencia.ValorAlmocoDAO;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author marcelo.lima
 */
public class ValorAlmocoBean {
    private ValorAlmoco valorAlmoco = new ValorAlmoco();
    private final ValorAlmocoDAO dao = new ValorAlmocoDAO();

    /**
     * @return the valorAlmoco
     */
    public ValorAlmoco getValorAlmoco() {
        return valorAlmoco;
    }

    /**
     * @param valorAlmoco the valorAlmoco to set
     */
    public void setValorAlmoco(ValorAlmoco valorAlmoco) {
        this.valorAlmoco = valorAlmoco;
    }
    
    /**
     * Solicita à camada de persistência o cadastro de um valor de almoco
     */
    public void salvar() {
        dao.salvar(valorAlmoco);
        enviarMensagem(FacesMessage.SEVERITY_INFO, "Valor do almoço cadastrado com sucesso!");
    }    
    
    /**
     * Solicita a pesquisa de um aluno para a camada de persistência     
     */
    public void carregarValorAtualAlmoco() {
        valorAlmoco = dao.carregarValorAtualAlmoco();        
    }    
    
    /**
     * Solicita o valor do almoco conforme a data informada
     * @param valorAtualAlmocoBean Um objeto ValorAlmocoBean com o valor atual do almoco
     * @param dataCredito A data do crédito dos créditos do cartão     
     */
    public void getValorAlmocoPorData(ValorAlmocoBean valorAtualAlmocoBean,
            Calendar dataCredito) {
        // Inicia com o valor atual do almoco
        valorAlmoco = valorAtualAlmocoBean.getValorAlmoco();
        
        // Se a data do credito do cartao for menor que a data
        // do valor do almoco, entao verifica se eh necessario atualizar o valor do almoco        
        if (dataCredito.getTimeInMillis() < valorAtualAlmocoBean.getValorAlmoco().getDataValor().getTimeInMillis()) {            
            // verifica se a data de expiração dos creditos do cartao jah venceu
            Calendar dataHoje = new GregorianCalendar();
            // 86400000 eh equivalente a 1 dia            
            int diasCredito = (int) ((dataHoje.getTimeInMillis() - valorAlmoco.getDataValor().getTimeInMillis()) / 86400000L);
            // 60 dias é a validade dos créditos para utilizar um valor 
            if (diasCredito < 60) {
                // verifica qual valor de almoco utilizar
                valorAlmoco = dao.getValorAlmocoPorData(dataCredito);
            }            
        }
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
        
    @PreDestroy
    public void encerrar() {
        dao.encerrar();
    }                   
}

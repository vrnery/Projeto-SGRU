/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.excessao.PeriodoEntreAlmocosInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.ValorAlmocoInvalidoException;
import br.edu.ifrs.restinga.sgru.persistencia.VendaAlmocoDAO;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author marcelo.lima
 */
@Entity
public class VendaAlmoco implements Serializable {
    @Id
    @GeneratedValue
    private int id;    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)    
    private Calendar dataVenda;
    @OneToOne
    @JoinColumn(name = "idValorAlmoco")
    private ValorAlmoco valorAlmoco;
    @OneToOne
    @JoinColumn(name = "idTicket")
    private Ticket ticket;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "idCartao")
    private Cartao cartao;
    @ManyToOne
    @JoinColumn(name = "idCaixaRU")
    private CaixaRU caixaRU;   
    @Transient
    // Intervalo mínimo entre compra de almoco por cliente
    private static final int NUM_MAX_MINUTOS_ULTIMO_ALMOCO = 270;

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
     * @return the dataVenda
     */
    public Calendar getDataVenda() {
        return dataVenda;
    }

    /**
     * @param dataVenda the dataVenda to set
     */
    public void setDataVenda(Calendar dataVenda) {
        this.dataVenda = dataVenda;
    }    

    /**
     * @return the valorAlmoco
     * @throws br.edu.ifrs.restinga.sgru.excessao.ValorAlmocoInvalidoException Caso não encontre um valor de almoço cadastrado
     */
    public ValorAlmoco getValorAlmoco() throws ValorAlmocoInvalidoException  {
        if (valorAlmoco == null) {
            throw new ValorAlmocoInvalidoException("Valor de almoço não cadastrado!");
        }
        return valorAlmoco;
    }
    
    /**
     * @return the ticket
     */
    public Ticket getTicket() {
        return ticket;
    }

    /**
     * @param ticket the ticket to set
     */
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    /**
     * @return the cartao
     */
    public Cartao getCartao() {
        return cartao;
    }

    /**
     * @param cartao the cartao to set
     */
    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
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
     * @param valorAlmoco the valorAlmoco to set
     */
    public void setValorAlmoco(ValorAlmoco valorAlmoco) {                       
        this.valorAlmoco = valorAlmoco;
    }
    
    /**
     * Verifica o prazo para aquisição de um novo almoço é válido
     * @param idCartao O id do cartao que está senado utilizado na compra do almoço     
     * @throws PeriodoEntreAlmocosInvalidoException Caso o prazo para aquisição de um outro almoço não tenha expirado
     */
    public void validarPeriodoEntreAlmocos(int idCartao) throws PeriodoEntreAlmocosInvalidoException {
        // Primeiramente carrega a lista de almocos vendidos para o cliente no dia
        // Nao podemos utilizar a lista de almocos do CaixaRU, porque essa lista contem
        // apenas almocos para um determinado id de caixa
        VendaAlmocoDAO daoVendaAlmoco = new VendaAlmocoDAO();
        List<VendaAlmoco> lstVendaAlmocoCartao = daoVendaAlmoco.carregarAlmocosCartaoDia(idCartao);
        
        if (!lstVendaAlmocoCartao.isEmpty()) {
            // o ultimo almoco vendido para o cliente eh o ultimo da lista
            VendaAlmoco ultimoAlmocoCartao = lstVendaAlmocoCartao.get(lstVendaAlmocoCartao.size()-1);

            // Calcula o numero de minutos necessarios para a proxima compra
            int numMinutos = NUM_MAX_MINUTOS_ULTIMO_ALMOCO - 
                    ((int) ((Calendar.getInstance().getTimeInMillis() - ultimoAlmocoCartao.getDataVenda().getTimeInMillis())* 0.0000166667));
            
            // Cria um ojjeto GregorianCalendar com a data atual do sistema
            Calendar horaPermitidaAlmoco = new GregorianCalendar();
            // Adiciona os minutos ao horario atual
            horaPermitidaAlmoco.add(Calendar.MINUTE, numMinutos);
            // Verifica se o cliente pode efetuar a compra
            /*            
            if (numMinutos <= NUM_MAX_MINUTOS_ULTIMO_ALMOCO) {
                //throw new PeriodoEntreAlmocosInvalidoException("Compra permitida somente após " + (NUM_MAX_MINUTOS_ULTIMO_ALMOCO-numMinutos) + " minutos!");
                SimpleDateFormat fmt = new SimpleDateFormat("HH'h'mm");
                throw new PeriodoEntreAlmocosInvalidoException("Compra permitida somente a partir das " + fmt.format(horaPermitidaAlmoco.getTime()));
            } 
            */
        }
    }    
}

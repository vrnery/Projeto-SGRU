/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.excessao.DataRelatorioInvalidaException;
import br.edu.ifrs.restinga.sgru.excessao.RecargaNaoEncontradaException;
import br.edu.ifrs.restinga.sgru.excessao.RelatorioException;
import br.edu.ifrs.restinga.sgru.modelo.Cliente;
import br.edu.ifrs.restinga.sgru.modelo.TipoCliente;
import br.edu.ifrs.restinga.sgru.modelo.ControladorRelatorio;
import br.edu.ifrs.restinga.sgru.modelo.Pessoa;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author marcelo.lima
 */
@ManagedBean
@SessionScoped
public class RelatorioBean implements Serializable {
    private static Font catFont = new Font(Font.TIMES_ROMAN, 18,Font.BOLD);
    public static final int RELATORIO_COMPRAS = 1;
    public static final int RELATORIO_RECARGAS = 2;
    public static final int FORMA_PGTO_CARTAO = 1;
    public static final int FORMA_PGTO_TICKET = 2;
    
    private final ControladorRelatorio controlador;
    private Date dataInicialMin;
    private Date dataInicialMax;
    private Date dataFinalMin;
    private Date dataFinalMax;
    private boolean relatorioCompras;    
    private boolean relatorioCartao;
    
    public RelatorioBean() {
        this. controlador = new ControladorRelatorio();
        // A data inicial maxima eh a data atual
        this.dataInicialMax = new Date();
        // A data final minima eh a data atual
        this.dataFinalMin = new Date();
        // O tipo de relatorio default eh compras
        controlador.setTipoRelatorio(RELATORIO_COMPRAS);
        // A forma de pagamento default eh cartao
        controlador.setFormaPgto(FORMA_PGTO_CARTAO);        
        // Flag para controlar a ativacao do campo forma de pagamento
        this.relatorioCompras = true;
        // Flag para controlar a ativacao dos campos tipo de usuario e cliente
        this.relatorioCartao = true;
    }

    /**
     * @return the RELATORIO_COMPRAS
     */
    public int getRELATORIO_COMPRAS() {
        return RELATORIO_COMPRAS;
    }

    /**
     * @return the RELATORIO_RECARGAS
     */
    public int getRELATORIO_RECARGAS() {
        return RELATORIO_RECARGAS;
    }

    /**
     * @return the FORMA_PGTO_CARTAO
     */
    public int getFORMA_PGTO_CARTAO() {
        return FORMA_PGTO_CARTAO;
    }

    /**
     * @return the FORMA_PGTO_TICKET
     */
    public int getFORMA_PGTO_TICKET() {
        return FORMA_PGTO_TICKET;
    }    
    
   /**
     * @return the controlador
     */
    public ControladorRelatorio getControlador() {
        return controlador;
    }

    /**
     * @return the dataInicialMin
     */
    public Date getDataInicialMin() {
        return dataInicialMin;
    }

    /**
     * @return the dataInicialMax
     */
    public Date getDataInicialMax() {
        return dataInicialMax;
    }

    /**
     * @return the dataFinalMin
     */
    public Date getDataFinalMin() {
        return dataFinalMin;
    }

    /**
     * @return the dataFinalMax
     */
    public Date getDataFinalMax() {
        return dataFinalMax;
    }
    
    /**
     * @return the relatorioCompras
     */
    public boolean isRelatorioCompras() {
        return relatorioCompras;
    }
        
    /**
     * @param relatorioCompras the relatorioCompras to set
     */
    public void setRelatorioCompras(boolean relatorioCompras) {
        this.relatorioCompras = relatorioCompras;
    }    

    /**
     * @return the relatorioCartao
     */
    public boolean isRelatorioCartao() {
        return relatorioCartao;
    }

    /**
     * @param relatorioCartao the relatorioCartao to set
     */
    public void setRelatorioCartao(boolean relatorioCartao) {
        this.relatorioCartao = relatorioCartao;
    }    
    
    /**
     * Adapta o componente calendar dataFinal da view
     * @param evento 
     */
    public void tratarDataInicial(SelectEvent evento) {
        Date data = (Date) evento.getObject();
        this.dataFinalMin = data;
    }
    
    /**
     * Adapta o componente calendar dataInicial da view
     * @param evento 
     */
    public void tratarDataFinal(SelectEvent evento) {
        Date data = (Date) evento.getObject();
        this.dataInicialMax = data;
    }
    
    /**
     * Habilita ou não um componente na view conforme o tipo de usuário
     * @param pessoa O usuário logado no sistema
     * @return True, se for necessário habilitar o componente e false, caso contrário
     */
    public boolean habilitarComponentesGerenciais(Pessoa pessoa) {        
        return !(pessoa instanceof Cliente);
    }    
    
    /**
     * Retorna uma lista de Tipo de Clientes cadastrados no sistema
     * @return 
     */
    public List<TipoCliente> getLstTipoCliente() {        
        return controlador.getLstTipoCliente();
        
    }
    
    /**
     * Retorna uma lista de cliente do tipo desejado
     * @return Uma lista de objetos Cliente
     */
    public List<Cliente> getLstClientes() {        
        return controlador.getLstClientes();
    }    
    
    public String getDataInicialFormatada() {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        return fmt.format(controlador.getDataInicial().getTime());
    }
    
    public String getDataFinalFormatada() {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        return fmt.format(controlador.getDataFinal().getTime());
    }    
    
    /**
     * Altera o status do atributo relatorioCompras     
     */
    public void alterarStatusRelatorioCompras() {
        this.relatorioCompras = !this.relatorioCompras;
    }
    
    /**
     * Altera o status do atributo relatorioCartao
     */
    public void alterarStatusRelatorioCartao() {
        this.relatorioCartao = !this.relatorioCartao;
    }    
    
    public String emitirRelatorio(int idUsuarioLogado) {
        String retorno;
        try {            
            if (relatorioCompras) {
                this.controlador.buscarDadosRelatorioCompras(idUsuarioLogado);
                retorno = "relatorioCompras";
            } else {                
                this.controlador.buscarDadosRelatorioRecargas(idUsuarioLogado);
                retorno = "relatorioRecargas";
            }
        } catch (DataRelatorioInvalidaException | RelatorioException | 
                RecargaNaoEncontradaException e) {
            enviarMensagem(FacesMessage.SEVERITY_INFO, e.getMessage());
            return null;
        }
        return retorno;
    }
    
    public boolean isRelatorioComprasSet() {
        return !(this.controlador.getLstVendaAlmoco() == null);
    }
    
    public boolean isRelatorioRecargasSet() {
        return !(this.controlador.getLstRecargas() == null);
    }
    
    /**
     * Cria um cabeçalho para o documento PDF
     * @param document
     * @throws IOException
     * @throws BadElementException
     * @throws DocumentException 
     */
    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;
        pdf.open();
        pdf.setPageSize(PageSize.A4); 
        
        // Cabecalho do PDF
        String cabecalho;
        if (this.isRelatorioComprasSet()) {
            cabecalho = "Compras realizadas no período de " + this.getDataInicialFormatada() +
                    " a " + this.getDataFinalFormatada();
        } else {
            cabecalho = "Recargas realizadas no período de " + this.getDataInicialFormatada() +
                    " a " + this.getDataFinalFormatada();
        }
        //pdf.addTitle(cabecalho);        
        Paragraph preface = new Paragraph();
        preface.setAlignment(Element.ALIGN_CENTER);
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph(cabecalho, catFont));
        addEmptyLine(preface, 1);
        
        pdf.add(preface);
    }
    
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
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
}

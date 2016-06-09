/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.excessao.DataRelatorioInvalidaException;
import br.edu.ifrs.restinga.sgru.excessao.RecargaNaoEncontradaException;
import br.edu.ifrs.restinga.sgru.excessao.RelatorioException;
import br.edu.ifrs.restinga.sgru.modelo.ControladorRelatorio;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author marcelo.lima
 */
@ManagedBean
@SessionScoped
public class RelatorioBean implements Serializable {
    private static final Font CAT_FONT = new Font(Font.TIMES_ROMAN,
            18,Font.BOLD);
    private static final Font SMALL_BOLD = new Font(Font.TIMES_ROMAN,
            12,Font.BOLD);
    public static final int RELATORIO_COMPRAS = 1;
    public static final int RELATORIO_RECARGAS = 2;
    public static final int FORMA_PGTO_CARTAO = 1;
    public static final int FORMA_PGTO_TICKET = 2;
    
    private final ControladorRelatorio controlador;
    private Date dataInicialMin;
    private Date dataInicialMax;
    private Date dataFinalMin;
    private final Date dataFinalMax;
    private boolean relatorioCompras;    
    private boolean relatorioCartao;
    
    public RelatorioBean() {
        this. controlador = new ControladorRelatorio();
        // A data inicial maxima eh a data atual
        this.dataInicialMax = new Date();
        // A data final minima eh a data atual
        this.dataFinalMin = new Date();
        // A data final maxima sempre sera a data atual
        this.dataFinalMax = new Date();
        // O tipo de relatorio default eh compras
        this.controlador.setTipoRelatorio(RELATORIO_COMPRAS);
        // A forma de pagamento default eh cartao
        this.controlador.setFormaPgto(FORMA_PGTO_CARTAO);        
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
     * @return O nome do arquivo para o relatorio
     */
    public String getNomeArquivoRelatorio() {
        return this.controlador.getNomeArquivoRelatorio();
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
     * @return the relatorioCartao
     */
    public boolean isRelatorioCartao() {
        return relatorioCartao;
    }
        
    /**
     * Formata a a data inicial (this.controlador.dataInicial) para o formato dd/mm/aaaa
     * @return Um string com a data formatada
     */
    public String getDataInicialFormatada() {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        return fmt.format(controlador.getDataInicial().getTime());
    }
    
    /**
     * Formata a a data final (this.controlador.dataFinal) para o formato dd/mm/aaaa
     * @return Um string com a data formatada
     */
    public String getDataFinalFormatada() {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        return fmt.format(controlador.getDataFinal().getTime());
    }    

    /**
     * Verifica se a lista this.controlador.lstVendaAlmoco está preenchida. 
     * Caso não esteja, redireciona o usuário para a página gerencial.
     */
    public void isRelatorioComprasSet() {        
        // Caso nao haja lista de venda setada, entao retorna para a pagina gerencial
        if (this.controlador.getLstVendaAlmoco() == null) {
            // O caixa ainda nao esta aberto
            ConfigurableNavigationHandler nav  = 
                    (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();            
            nav.performNavigation("paginaGerencial.xhtml?faces-redirect=true");     
        }
    }
    
    /**
     * Verifica se a lista this.controlador.lstRecargas está preenchida.
     * Caso não esteja, redireciona o usuário para a página gerencial.
     */
    public void isRelatorioRecargasSet() {        
        // Caso nao haja lista de venda setada, entao retorna para a pagina gerencial
        if (this.controlador.getLstRecargas() == null) {
            // O caixa ainda nao esta aberto
            ConfigurableNavigationHandler nav  = 
                    (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();            
            nav.performNavigation("paginaGerencial.xhtml?faces-redirect=true");     
        }
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
     * @return True, se for necessário habilitar o componente e false, caso contrário
     */
    public boolean habilitarComponentesGerenciais() {
        return this.controlador.isUsuarioLogadoGerente();
    }        
    
    public boolean habilitarNomeCliente() {
        // Nao ha necessidade de mostrar a coluna nome cliente quando a solicitacao
        // parte de um cliente
        if (!this.habilitarComponentesGerenciais()) {
            return false;
        }
        // Ticket nao tem cliente associado
        return !(this.controlador.getFormaPgto() == FORMA_PGTO_TICKET);
    }
    
    /**
     * Altera o status dos atributos relatorioCompras, relatorioCartao, 
     * tipo de usuário, cliente e forma de pagamento
     */
    public void alterarStatusRelatorioCompras() {
        this.relatorioCompras = this.controlador.getTipoRelatorio() == RELATORIO_COMPRAS;
        
        // Se recarga, altera os campos do cliente e tipo cliente para todos        
        this.relatorioCartao = true;                
        this.controlador.setCliente(null);
        this.controlador.setTipoCliente(null);
        this.controlador.setFormaPgto(FORMA_PGTO_CARTAO);
    }
    
    /**
     * Altera o status do atributo relatorioCartao, tipo de usuário e cliente
     */
    public void alterarStatusRelatorioCartao() {
        this.relatorioCartao = this.controlador.getFormaPgto() == FORMA_PGTO_CARTAO;
                
        this.controlador.setCliente(null);
        this.controlador.setTipoCliente(null);            
    }    
    
    /**
     * Solicita a emissão de um relatório     
     * @return A próxima página a ser visitada pelo usuário
     */
    public String emitirRelatorio() {
        String retorno;
        try {            
            if (relatorioCompras) {
                this.controlador.buscarDadosRelatorioCompras();
                retorno = "relatorioCompras";
            } else {                
                this.controlador.buscarDadosRelatorioRecargas();
                retorno = "relatorioRecargas";
            }
        } catch (DataRelatorioInvalidaException | RelatorioException | 
                RecargaNaoEncontradaException e) {
            enviarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
            return null;
        }
        return retorno;
    }    
    
    /**
     * Cria um cabeçalho para o documento PDF
     * @param document
     */
    public void preProcessPDF(Object document) {
        try {                                    
            Document pdf = (Document) document;    
            pdf.setPageSize(PageSize.A4);                         
            pdf.open();                        

            Paragraph preface = new Paragraph();
            
            // Titulo
            String titulo;
            if (this.relatorioCompras) {
                titulo = "Compras realizadas no período de " + this.getDataInicialFormatada() +
                        " a " + this.getDataFinalFormatada();
            } else {
                titulo = "Recargas realizadas no período de " + this.getDataInicialFormatada() +
                        " a " + this.getDataFinalFormatada();
            }                                    
            // Titulo centralizado
            preface.setAlignment(Element.ALIGN_CENTER);            
            // We add one empty line
            addEmptyLine(preface, 1);                        
            // Lets write a big header
            preface.add(new Paragraph(titulo, CAT_FONT));            
            addEmptyLine(preface, 1);                                    
            
            // O restante serah alinhado a a esquerda
            preface.setAlignment(Element.ALIGN_LEFT);
            // Nome de quem requisitou o relatorio
            SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy 'às' HH'h'mm");
            String dataSolicitacao = fmt.format(Calendar.getInstance().getTime());
            preface.add(new Paragraph("Relatório gerado por " + this.controlador.getUsuarioLogado().getNome() 
                    + " em " + dataSolicitacao, SMALL_BOLD));                        
            
            // Forma de pagamento, Tipo de Cliente e Nome do Cliente            
            if (this.controlador.isUsuarioLogadoGerente()) {                
                // Opcao para relatorio de compras apenas
                if (this.relatorioCompras) {
                    String formaPgto;
                    if (this.controlador.getFormaPgto() == FORMA_PGTO_CARTAO) {
                        formaPgto = "Cartão";
                    } else {
                        formaPgto = "Ticket";
                    }                
                    preface.add(new Paragraph("Forma de Pagamento: " + formaPgto, SMALL_BOLD));                
                }
                
                // Tipo de cliente
                //String tipoUsuario = this.controlador.getDescricaoCodigoCliente();
                String tipoCliente = "Todos";
                if (this.controlador.getTipoCliente() != null) {
                    tipoCliente = this.controlador.getTipoCliente().getDescricao();
                }
                
                // Tipo de usuario e cliente nao serah impresso para ticket
                if ((this.controlador.getFormaPgto() != FORMA_PGTO_TICKET)) {
                    // Tipo de usuario
                    preface.add(new Paragraph("Tipo de Usuário: " + tipoCliente, SMALL_BOLD));                

                    // Cliente           
                    String nomeCliente = "Todos";
                    if (this.controlador.getCliente() != null) {
                        nomeCliente = this.controlador.getCliente().getNome();
                    }
                    preface.add(new Paragraph("Nome do Cliente: " + nomeCliente, SMALL_BOLD));
                        
                }
            }                        
            
            addEmptyLine(preface, 1);
            pdf.add(preface);
        } catch(DocumentException e) {
            enviarMensagem(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }
    
    /**
     * Adiciona uma linha no arquivo PDF
     * @param paragraph
     * @param number 
     */
    private void addEmptyLine(Paragraph paragraph, int number) {
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

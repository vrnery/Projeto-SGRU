/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import br.edu.ifrs.restinga.sgru.excessao.FotoNaoEncontradaException;
import br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException;
import br.edu.ifrs.restinga.sgru.excessao.PeriodoEntreAlmocosInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.SaldoInsuficienteException;
import br.edu.ifrs.restinga.sgru.excessao.TicketInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.ValorAberturaCaixaInvalido;
import br.edu.ifrs.restinga.sgru.excessao.ValorAlmocoInvalidoException;
import br.edu.ifrs.restinga.sgru.excessao.ValorRecargaInvalidoException;
import br.edu.ifrs.restinga.sgru.persistencia.CaixaRUDAO;
import br.edu.ifrs.restinga.sgru.persistencia.ClienteDAO;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfDocument;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Classe que implementa o padrão GRASP "Controlador" para as vendas realizadas
 * no caixa.
 *
 * @author marcelo.lima
 */
public class ControladorVenda {

    private final int FORMA_PGTO_PAYPAL = 1;

    private CaixaRU caixaRU;
    private Cliente cliente;
    private Funcionario operador;
    private Recarga recarga;
    private int formaPgto;
    private int quantidade = 1;
    private double valorTickets = this.quantidade * ValorAlmoco.carregarValorAtualAlmoco().getValorAlmoco();

    public ControladorVenda() {
        this.formaPgto = this.FORMA_PGTO_PAYPAL;
    }

    /**
     * @return the FORMA_PGTO_PAYPAL
     */
    public int getFORMA_PGTO_PAYPAL() {
        return FORMA_PGTO_PAYPAL;
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
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the operador
     */
    public Funcionario getOperador() {
        return operador;
    }

    /**
     * @param operador the operador to set
     */
    public void setOperador(Funcionario operador) {
        this.operador = operador;
    }

    /**
     * Retorna o valor a pagar pelo almoço
     *
     * @return O valor a pagar pelo almoço
     * @throws ArrayIndexOutOfBoundsException Caso não exista almoço realizado
     * @throws ValorAlmocoInvalidoException Caso não encontre um valor de almoço
     * cadastrado
     */
    public double getValorPagoAlmoco() throws ArrayIndexOutOfBoundsException, ValorAlmocoInvalidoException {
        return caixaRU.ultimoAlmocoVendido().getValorAlmoco().getValorAlmoco();
    }

    /**
     * @return the recarga
     */
    public Recarga getRecarga() {
        return recarga;
    }

    /**
     * @param recarga the recarga to set
     */
    public void setRecarga(Recarga recarga) {
        this.recarga = recarga;
    }

    /**
     * Seta o cliente para a recarga e, caso haja operador logado, seta o
     * operador
     *
     * @param pessoa A pessoa logada no sistema
     */
    public void setClienteRecarga(Pessoa pessoa) {
        if (pessoa instanceof Funcionario) {
            this.operador = (Funcionario) pessoa;
            // O cliente nao estah logado no sistema, mas precisa ser instanciado
            this.cliente = new Cliente();
        } else {
            // Cliente logado no sistema
            this.cliente = (Cliente) pessoa;
        }
    }

    /**
     * @return the formaPgto
     */
    public int getFormaPgto() {
        return formaPgto;
    }

    /**
     * @param formaPgto the formaPgto to set
     */
    public void setFormaPgto(int formaPgto) {
        this.formaPgto = formaPgto;
    }

    /**
     * @return
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * @param quantidade
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorTickets() {
        return valorTickets;
    }

    public void setValorTickets(double valorTickets) {
        this.valorTickets = valorTickets;
    }

    /**
     * Instancia um objeto recarga com a data atual e seu valor atualizado como
     * false
     */
    public void iniciarRecarga() {
        this.recarga = new Recarga();
        this.recarga.setDataCredito(Calendar.getInstance());
        this.recarga.setUtilizado(false);
    }

    /**
     * Realiza a recarga para o cliente
     *
     * @throws br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException
     * Caso a matrícula informada não esteja cadastrada
     * @throws br.edu.ifrs.restinga.sgru.excessao.ValorRecargaInvalidoException
     * Caso o valor da recarga seja inválido
     */
    public void realizarRecarga() throws MatriculaInvalidaException,
            ValorRecargaInvalidoException {
        // Verifica valores invalidos para recarga
        if (this.recarga.getValorRecarregado() <= 0) {
            throw new ValorRecargaInvalidoException("Valor inválido de recarga!");
        }
        // Recarga realizada no caixa
        if (this.operador != null) {
            ClienteDAO daoCliente = new ClienteDAO();
            this.cliente = daoCliente.carregar(this.cliente.getMatricula());
            // Seta o cartao da recarga
            this.recarga.setCartao(this.cliente.getCartao());
            // Atualiza o saldo do cartao
            this.recarga.getCartao().setSaldo(this.recarga.getCartao().getSaldo() + this.recarga.getValorRecarregado());
            // Caso o valor da ultima regarga seja menor ou igual a zero, deve-se atualizar o valor
            if (this.recarga.getCartao().getSaldoUltimaRecarga() <= 0) {
                this.recarga.getCartao().setSaldoUltimaRecarga(this.recarga.getCartao().getSaldoUltimaRecarga()
                        + this.recarga.getValorRecarregado());
                this.recarga.setUtilizado(true);
            }
            VendaTicketsRecargas vendaTicketsRecargas = new VendaTicketsRecargas();
            vendaTicketsRecargas.setCaixaRU(this.caixaRU);
            vendaTicketsRecargas.setRecarga(recarga);
            vendaTicketsRecargas.realizarRecargaCaixa();
            this.caixaRU.atualizarLstVendaTicketsRecargas(vendaTicketsRecargas);
        } else {
            // Recarga realizada na pagina do cliente
            this.recarga.setCartao(this.cliente.getCartao());
            // Atualiza o saldo do cartao
            this.recarga.getCartao().setSaldo(this.recarga.getCartao().getSaldo() + this.recarga.getValorRecarregado());
            // Caso o valor da ultima regarga seja menor ou igual a zero, deve-se atualizar o valor
            if (this.recarga.getCartao().getSaldoUltimaRecarga() <= 0) {
                this.recarga.getCartao().setSaldoUltimaRecarga(this.recarga.getCartao().getSaldoUltimaRecarga()
                        + this.recarga.getValorRecarregado());
                this.recarga.setUtilizado(true);
            }
            VendaTicketsRecargas vendaTicketsRecargas = new VendaTicketsRecargas();
            vendaTicketsRecargas.setRecarga(recarga);
            vendaTicketsRecargas.realizarRecargaPaginaCliente();
        }
    }

    /**
     * Verifica se o caixa está aberto
     *
     * @return A próxima página a ser visualizada pelo operador e Null, caso o
     * caixa esteja fechado
     */
    public String carregarCaixaAberto() {
        CaixaRUDAO dao = new CaixaRUDAO();
        this.caixaRU = dao.carregarCaixaAberto(this.operador, Calendar.getInstance());
        if (this.caixaRU != null) {
            // Carrega o valor atual do almoco
            this.caixaRU.carregarValorAtualAlmoco();

            if (this.operador.getTipoFuncionario().getCodigo().equals(Funcionario.OPERADOR_CAIXA)) {
                // verifica se a lista de almocos estah preenchida
                this.caixaRU.preencherListaAlmoco();
                return "caixa";
            } else if (this.operador.getTipoFuncionario().getCodigo().equals(Funcionario.OPERADOR_SISTEMA)) {
                // verifica se a lista de venda ticket/recargas estah preenchida
                this.caixaRU.preencherListaVendaTicketsRecargas();
                return "recarregarCartao";
            }
        }

        // Se nao encontrar o caixa, retorna null
        this.caixaRU = new CaixaRU();

        // Verifica qual lista instanciar: lstVendaAlmoco, para venda de almocos;
        // e lstVendaTicketsRecargas, para venda de recargas
        if (this.operador.getTipoFuncionario().getCodigo().equals(Funcionario.OPERADOR_CAIXA)) {
            this.caixaRU.setLstVendaAlmoco(new ArrayList());
        } else if (this.operador.getTipoFuncionario().getCodigo().equals(Funcionario.OPERADOR_SISTEMA)) {
            this.caixaRU.setLstVendaTicketsRecargas(new ArrayList());
        }

        return null;
    }

    /**
     * Carrega um caixa já aberto (com valor de fechamento zerado) ou abre um
     * novo caixa
     *
     * @param valorAbertura O valor de abertura do caixa
     * @return A próxima página a ser visitada pelo operador
     * @throws br.edu.ifrs.restinga.sgru.excessao.ValorAberturaCaixaInvalido
     * Caso o valor de abertura informado seja zero ou negativo
     */
    public String realizarAberturaCaixa(double valorAbertura) throws
            ValorAberturaCaixaInvalido {
        this.getCaixaRU().realizarAberturaCaixa(this.operador, valorAbertura);

        if (this.operador.getTipoFuncionario().getCodigo().equals(Funcionario.OPERADOR_CAIXA)) {
            return "caixa";
        } else if (this.operador.getTipoFuncionario().getCodigo().equals(Funcionario.OPERADOR_SISTEMA)) {
            return "recarregarCartao";
        }
        return null;
    }

    /**
     * Realiza uma venda de almoco com cartao para o cliente
     *
     * @param matricula A matrícula do cliente
     * @throws br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException
     * Caso a matrícula não seja localizada
     * @throws br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException Caso
     * o usuário não seja um cliente
     * @throws br.edu.ifrs.restinga.sgru.excessao.SaldoInsuficienteException
     * Caso o cliente não possua saldo para a compra do almoço
     * @throws
     * br.edu.ifrs.restinga.sgru.excessao.PeriodoEntreAlmocosInvalidoException
     * Caso o prazo para aquisição de um outro almoço não tenha expirado
     * @throws br.edu.ifrs.restinga.sgru.excessao.ValorAlmocoInvalidoException
     * Caso não encontre valor de almoço
     */
    public void realizarVendaAlmocoCartao(String matricula) throws MatriculaInvalidaException,
            UsuarioInvalidoException, SaldoInsuficienteException, ValorAlmocoInvalidoException,
            PeriodoEntreAlmocosInvalidoException {
        this.getCaixaRU().realizarVendaAlmocoCartao(matricula);
    }

    /**
     * Realiza uma venda de almoço com ticket
     *
     * @param codigo O código do ticket apresentado
     * @throws br.edu.ifrs.restinga.sgru.excessao.TicketInvalidoException Caso o
     * ticket esteja vencido ou não seja encontrado
     */
    public void realizarVendaAlmocoTicket(int codigo) throws TicketInvalidoException {
        this.getCaixaRU().realizarVendaAlmocoTicket(codigo);
    }

    /**
     * Calcular o valor total da venda de tickets
     */
    public void calcularVendaTicket() {
        this.valorTickets = this.quantidade * ValorAlmoco.carregarValorAtualAlmoco().getValorAlmoco();
    }

    /**
     * Realiza a venda de um ticket para um cliente
     *
     * @throws br.edu.ifrs.restinga.sgru.excessao.TicketInvalidoException
     */
    public void realizarVendaTicket() throws TicketInvalidoException {
        if (this.quantidade <= 0) {
            throw new TicketInvalidoException("Venda de Quantidade invalida!");
        }

//        try {
//            PdfDocument pdfTicket = new PdfDocument();
//            pdfTicket.setPageSize(PageSize.A4);
//            pdfTicket.open();
//
//            Paragraph preface = new Paragraph();
//            preface.setAlignment(Element.ALIGN_CENTER);

            for (int i = 0; i < this.quantidade; i++) {
                VendaTicketsRecargas vendaTicketsRecarga = new VendaTicketsRecargas();
                vendaTicketsRecarga.setCaixaRU(this.caixaRU);
                vendaTicketsRecarga.realizarVendaTicket();
//                preface.add(new Paragraph("TICKET"));
//                preface.add(new Paragraph(String.valueOf(vendaTicketsRecarga.getTicket().getId())));
//                preface.add(new Paragraph(String.valueOf(vendaTicketsRecarga.getTicket().getValor())));
//                preface.add(new Paragraph(vendaTicketsRecarga.getTicket().getDataCriado().getTime().toString()));
                this.caixaRU.atualizarLstVendaTicketsRecargas(vendaTicketsRecarga);
            }

//            addEmptyLine(preface, 1);
//            pdfTicket.add(preface);
//        } catch (DocumentException ex) {
//            throw new TicketInvalidoException(ex.getMessage());
//        }
    }

    /**
     * Confirma ou não confirmar a a venda do almoço
     *
     * @param confirmar True, para confirmar a venda, e false para não confirmar
     * @throws br.edu.ifrs.restinga.sgru.excessao.ValorAlmocoInvalidoException
     * Caso não encontre um valor de almoço cadastrado
     */
    public void finalizarAlmoco(boolean confirmar) throws ValorAlmocoInvalidoException {
        this.getCaixaRU().finalizarAlmoco(confirmar);
    }

    /**
     * Verifica se a foto do usuário existe
     *
     * @throws br.edu.ifrs.restinga.sgru.excessao.FotoNaoEncontradaException
     * Caso não encontre a foto do cliente
     */
    public void verificarExistenciaFoto() throws FotoNaoEncontradaException {
        this.caixaRU.ultimoAlmocoVendido().getCartao().getCliente().verificarExistenciaFoto();
    }

    /**
     * Carrega um cliente
     *
     * @param matricula A matrícula do cliente a ser carregado
     * @throws br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException Se
     * a matrícula não for encontrada
     */
    public void carregarCliente(String matricula) throws MatriculaInvalidaException {
        ClienteDAO dao = new ClienteDAO();
        cliente = dao.carregar(matricula);
    }

    /**
     * Adiciona uma linha no arquivo PDF
     *
     * @param paragraph
     * @param number
     */
    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}

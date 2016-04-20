
import br.edu.ifrs.restinga.sgru.modelo.CaixaRU;
import br.edu.ifrs.restinga.sgru.modelo.OperadorCaixa;
import br.edu.ifrs.restinga.sgru.modelo.Ticket;
import br.edu.ifrs.restinga.sgru.modelo.ValorAlmoco;
import br.edu.ifrs.restinga.sgru.persistencia.CaixaRUDAO;
import br.edu.ifrs.restinga.sgru.persistencia.OperadorCaixaDAO;
import br.edu.ifrs.restinga.sgru.persistencia.TicketDAO;
import br.edu.ifrs.restinga.sgru.persistencia.ValorAlmocoDAO;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cstads
 
public class NewClass1 {
    public static void main(String[] args) {
        OperadorCaixaDAO daoOper = new OperadorCaixaDAO();
        OperadorCaixa oper = daoOper.carregar("oper12");        
        
        ValorAlmoco valorAtual;
        ValorAlmocoDAO daoValor = new ValorAlmocoDAO();
        valorAtual = daoValor.carregarValorAtualAlmoco();        
        
        Ticket tic = new Ticket();
        TicketDAO daoTic = new TicketDAO();
        Calendar data = new GregorianCalendar();
        tic.setDataCriado(data);
        tic.setValor(valorAtual.getValorAlmoco());
        daoTic.salvar(tic);        
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(NewClass1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        CaixaRU caixa = new CaixaRU();
        CaixaRUDAO daoCaixa = new CaixaRUDAO();
        
        data = new GregorianCalendar();
        caixa.setDataAbertura(data);
        caixa.setOperadorCaixa(oper);
        caixa.setValorAbertura(0);
        
        /*
        List<VendaAlmoco> listaVenda = new ArrayList();
        caixa.setVendaAlmoco(listaVenda);
        caixa.getVendaAlmoco().add(new VendaAlmoco());
        caixa.getVendaAlmoco().get(0).setTicket(tic);
        caixa.getVendaAlmoco().get(0).setValorAlmoco(valorAtual);
        caixa.getVendaAlmoco().get(0).setCaixaRU(caixa);
        caixa.getVendaAlmoco().get(0).setFormaPagamento("Ticket");
        
        daoCaixa.salvar(caixa);
        daoCaixa.encerrar();
        
        daoTic = new TicketDAO();
        tic.setDataUtilizado(new Date());
        daoTic.salvar(tic);
        daoTic.encerrar();
        
        System.out.println("Data: " + tic.getDataCriado() + " Utilizado: " + tic.getDataUtilizado());
    }
}
*/

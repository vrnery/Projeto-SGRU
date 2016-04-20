
import br.edu.ifrs.restinga.sgru.modelo.Aluno;
import br.edu.ifrs.restinga.sgru.modelo.CaixaRU;
import br.edu.ifrs.restinga.sgru.modelo.OperadorCaixa;
import br.edu.ifrs.restinga.sgru.modelo.Ticket;
import br.edu.ifrs.restinga.sgru.modelo.ValorAlmoco;
import br.edu.ifrs.restinga.sgru.modelo.VendaAlmoco;
import br.edu.ifrs.restinga.sgru.persistencia.AlunoDAO;
import br.edu.ifrs.restinga.sgru.persistencia.CaixaRUDAO;
import br.edu.ifrs.restinga.sgru.persistencia.OperadorCaixaDAO;
import br.edu.ifrs.restinga.sgru.persistencia.TicketDAO;
import br.edu.ifrs.restinga.sgru.persistencia.ValorAlmocoDAO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author cstads
 
public class NewClass2 {

    public static void main(String[] args) {
        //Simula login no sistema
        OperadorCaixa op = new OperadorCaixa();
        op.setLogin("oper13");
        op.setSenha("oper");

        OperadorCaixaDAO daoOp = new OperadorCaixaDAO();
        op = daoOp.autenticar(op.getLogin(), op.getSenha());        

        System.out.println("Operador: " + op.getNome() + " Código: " + op.getId());

        //Faz a busca do caixa ja aberto (exemplo de retorno do horario de almoço do OperadorCaixa)
        CaixaRUDAO daoCaixa = new CaixaRUDAO();
        Calendar data = new GregorianCalendar();
        CaixaRU caixa = daoCaixa.reabrirCaixa(op.getId(), data);        

        System.out.println("Caixa: " + caixa.getId() + " Data Abertura: " + caixa.getDataAbertura() + " Operador: " + caixa.getOperadorCaixa().getId());

        //Faz a busca do valor do almoço
        ValorAlmocoDAO daoValor = new ValorAlmocoDAO();
        ValorAlmoco valor = daoValor.carregarValorAtualAlmoco();        

        System.out.println("Data: " + valor.getDataValor() + " Valor do Almoço:" + valor.getValorAlmoco());

        //Escolher a forma de pagamento
        Scanner ler = new Scanner(System.in);
        String txtForma = "";
        Aluno aluno = new Aluno();
        AlunoDAO daoAluno;
        Ticket ticket = new Ticket();
        TicketDAO daoTicket;
        int forma;

        do {
            System.out.println("Forma de Pagamento\r 1 - Cartão\t 2 - Ticket");
            forma = ler.nextInt();

            System.out.println("Código de barra");
            int codigo = ler.nextInt();

            if (forma == 1) {
                //pesquisa o aluno pela matricula
                daoAluno = new AlunoDAO();
                aluno = daoAluno.carregar(String.valueOf(codigo));                

                //verifica o valor correto de acordo com a data da recarga
                daoValor = new ValorAlmocoDAO();
                valor = daoValor.almocoCartao(aluno.getCartao().getDataCredito());                

                //verifica se tem saldo no cartão
                if (valor.getValorAlmoco() < aluno.getCartao().getSaldo()) {
                    txtForma = "Cartão";

                    //debita do saldo o valor correto do almoço
                    daoAluno = new AlunoDAO();
                    aluno.getCartao().setDebitar(valor.getValorAlmoco());
                    daoAluno.salvar(aluno);                    
                }
            } else {
                //perquisa o ticket pelo codigo
                daoTicket = new TicketDAO();
                ticket = daoTicket.usarTicket(codigo);                

                //verifica se retornou um ticket valido
                if (ticket != null) {
                    txtForma = "Ticket";

                    //registra que o ticket foi utilizado na data
                    daoTicket = new TicketDAO();
                    data = new GregorianCalendar();
                    ticket.setDataUtilizado(data);
                    daoTicket.salvar(ticket);                    
                }
            }
        } while (txtForma.equals("")); //verifica se tem alguma forma de pagamento

        //Realizar a venda do almoço
        /*
        List<VendaAlmoco> venda = new ArrayList();
        caixa.setVendaAlmoco(venda);
        caixa.getVendaAlmoco().add(new VendaAlmoco());
        caixa.getVendaAlmoco().get(0).setFormaPagamento(txtForma);
        if (forma == 1) {
            caixa.getVendaAlmoco().get(0).setCartao(aluno.getCartao());
        } else {
            caixa.getVendaAlmoco().get(0).setTicket(ticket);
        }

        caixa.getVendaAlmoco().get(0).setValorAlmoco(valor);
        caixa.getVendaAlmoco().get(0).setCaixaRU(caixa);
        System.out.println("Valor para cartão: " + caixa.getVendaAlmoco().get(0).getValorAlmoco().getValorAlmoco());
        daoCaixa = new CaixaRUDAO();
        daoCaixa.salvar(caixa);
        daoCaixa.encerrar();

        //Emitir nota
        System.out.println("\r------------------------------\r\r");
        System.out.println("   Sistema Gerenciamento RU");
        System.out.println("Rua Rua do IFRS");
        System.out.println("CEP: " + 1234 + "\tFone: " + op.getTelefone());
        System.out.println("CNPJ: " + 4321 + "\tIE: " + 5678);
        System.out.println(new Date() + "\tCodigo: " + venda.get(0).getId());
        System.out.println("\tCupom Fiscal");
        System.out.println("Item\t\t\tValor");
        System.out.println("Almoço\t\t\t R$ " + valor.getValorAlmoco());
        System.out.println("------------------------------");
        System.out.println("Total\t\t\t R$ " + valor.getValorAlmoco());
        System.out.println(venda.get(0).getFormaPagamento() + "\t R$ " + valor.getValorAlmoco());
        if (aluno != null) {
            System.out.println("Cliente: " + aluno.getNome());
        } else {
            System.out.println("Cliente: Visitante");
        }

        System.out.println("Operador: " + op.getId() + " " + op.getNome());
        System.out.println(new Date());
        
    }
}
*/

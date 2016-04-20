
import br.edu.ifrs.restinga.sgru.bean.AlunoBean;
import br.edu.ifrs.restinga.sgru.bean.CaixaRUBean;
import br.edu.ifrs.restinga.sgru.bean.OperadorCaixaBean;
import br.edu.ifrs.restinga.sgru.persistencia.HibernateUtil;
import java.util.Calendar;
import org.hibernate.Session;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 10070133
 */
public class NewClass {
    public static void main(String[] args) {
        Session sessao = HibernateUtil.getSessionFactory().getCurrentSession();
        sessao.beginTransaction();
        
        // Operador de caixa
        OperadorCaixaBean operadorCaixaBean = new OperadorCaixaBean();
        operadorCaixaBean.carregar("oper1");        
       
        // abre o caixa        
        CaixaRUBean caixaRUBean = new CaixaRUBean();               
        caixaRUBean.getCaixaRU().setDataAbertura(Calendar.getInstance());
        caixaRUBean.getCaixaRU().setOperadorCaixa(operadorCaixaBean.getOperadorCaixa());
        caixaRUBean.getCaixaRU().setValorAbertura(0);
        
        // Simula uma venda
        
        /* *******************************
        *       1 - Consulta aluno
        *  ******************************* */                 
        AlunoBean alunoBean = new AlunoBean();                
        alunoBean.carregar("123456");        
        
        // Solicita o valor do almoco para o aluno
        /*
        ValorAlmocoBean valorAlmocoAlunoBean = new ValorAlmocoBean();
        valorAlmocoAlunoBean.getValorAlmocoPorData(valorAtualAlmocoBean, alunoBean.getAluno().getCartao().getDataCredito());        
        */
         // O frente de caixa apresenta a foto, o nome completo do usuario, o saldo
         // e o valor do almoco para o operador, e solicita a confirmacao

         /************************************/
         /*       PARA TESTE APENAS          */
         /************************************/
         //CaixaRUBean caixaRUBean = new CaixaRUBean();
         //caixaRUBean.carregar(1);         
         /************************************/         
         
        // Se confirmado, inicia a venda do almoco 
        /*
        VendaAlmocoBean vendaAlmocoBean = new VendaAlmocoBean();
        // seta o objeto VendaAlmoco do bean
        vendaAlmocoBean.getVendaAlmoco().setCaixaRU(caixaRUBean.getCaixaRU());
        vendaAlmocoBean.getVendaAlmoco().setCartao(alunoBean.getAluno().getCartao());
        vendaAlmocoBean.getVendaAlmoco().setValorAlmoco(valorAlmocoAlunoBean.getValorAlmoco());        
        vendaAlmocoBean.getVendaAlmoco().setFormaPagamento("Cartão");
        System.out.println("Valor atual do almoco: " + valorAtualAlmocoBean.getValorAlmoco().getValorAlmoco());
        System.out.println("Valor do almoco pago pelo aluno: " + valorAlmocoAlunoBean.getValorAlmoco().getValorAlmoco());
        vendaAlmocoBean.salvar(vendaAlmocoBean.getVendaAlmoco());        
        */
        
        // Encerramento do caixa                
        caixaRUBean.getCaixaRU().setDataFechamento(Calendar.getInstance());
        //caixaRUBean.realizarFechamentoCaixa();
        caixaRUBean.getCaixaRU().setValorFechamento(15);
        caixaRUBean.salvar();        
        /*
        daoAluno = new AlunoDAO();
        aluno.getCartao().descontar(valorAtualAlmoco.getValorAlmoco());
        daoAluno.salvar(aluno);
        daoAluno.encerrar();
        
        System.out.println("Data: " + aluno.getCartao().getDataCredito() + " Saldo: " + aluno.getCartao().getSaldo());
        */
        
        sessao.getTransaction().commit();
    }
}

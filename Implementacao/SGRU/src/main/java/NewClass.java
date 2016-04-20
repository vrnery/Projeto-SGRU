
import br.edu.ifrs.restinga.sgru.bean.CaixaRUBean;
import br.edu.ifrs.restinga.sgru.bean.OperadorCaixaBean;
import br.edu.ifrs.restinga.sgru.modelo.VendaAlmoco;
import br.edu.ifrs.restinga.sgru.persistencia.HibernateUtil;
import br.edu.ifrs.restinga.sgru.persistencia.VendaAlmocoDAO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
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
        sessao.getTransaction().commit();
       
        // Carrega o caixa      
        sessao = HibernateUtil.getSessionFactory().getCurrentSession(); 
        sessao.beginTransaction();
        CaixaRUBean caixaRUBean = new CaixaRUBean();               
        caixaRUBean.carregar(1);
        sessao.getTransaction().commit();
        
        // Carrega valor atual do almoco. Esse carregamento deve ficar no
        // construtor da classe CaixaRU. No entanto o Hibernate, aparentemente,
        // instancia toda classe Entity, e isso está causando problemas no 
        // construtor da classe CaixaRU
        sessao = HibernateUtil.getSessionFactory().getCurrentSession(); 
        sessao.beginTransaction();
        caixaRUBean.getCaixaRU().carregarValorAtualAlmoco();
        sessao.getTransaction().commit();
        
         // O frente de caixa apresenta a foto, o nome completo do usuario, o saldo
         // e o valor do almoco para o operador, e solicita a confirmacao
        sessao = HibernateUtil.getSessionFactory().getCurrentSession(); 
        sessao.beginTransaction();
        try {
            caixaRUBean.realizarVendaAlmocoCartao("123456");
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        System.out.printf("Confirmar almoco?\n1 - Sim\n2 - Não\nOpção: ");
        Scanner ler = new Scanner(System.in);
        int opcao = ler.nextInt();
        
        /*****************************/
        /* Este codigo deve ficar em */  
        /* finalizarAlmoco da classe */
        /* CaixaRUBean               */
        /*****************************/
        VendaAlmocoDAO daoVendaAlmoco = new VendaAlmocoDAO();        
        List<VendaAlmoco> lstVendaAlmoco = caixaRUBean.getCaixaRU().getVendaAlmoco();        
        if (opcao == 1) {
            // salva ultimo almoco vendido
            daoVendaAlmoco.salvar(lstVendaAlmoco.get(lstVendaAlmoco.size()-1));
            sessao.getTransaction().commit();
        } else {
            // remove almoco da lista de almocos            
            caixaRUBean.getCaixaRU().getVendaAlmoco().remove(lstVendaAlmoco.size()-1);
            sessao.getTransaction().rollback();
        }
        
        sessao = HibernateUtil.getSessionFactory().getCurrentSession(); 
        sessao.beginTransaction();
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

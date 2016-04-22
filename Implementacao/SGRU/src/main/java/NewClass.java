
import br.edu.ifrs.restinga.sgru.bean.CaixaRUBean;
import br.edu.ifrs.restinga.sgru.bean.OperadorCaixaBean;
import br.edu.ifrs.restinga.sgru.excessao.MatriculaInvalidaException;
import br.edu.ifrs.restinga.sgru.excessao.SaldoInsuficienteException;
import br.edu.ifrs.restinga.sgru.excessao.UsuarioInvalidoException;
import br.edu.ifrs.restinga.sgru.persistencia.HibernateUtil;
import java.util.Calendar;
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
        try {
            operadorCaixaBean.carregar("987654");        
        } catch (MatriculaInvalidaException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
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
        } catch (MatriculaInvalidaException | UsuarioInvalidoException |
                SaldoInsuficienteException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        
        System.out.printf("Confirmar almoco?\n1 - Sim\n2 - Não\nOpção: ");
        Scanner ler = new Scanner(System.in);
        int opcao = ler.nextInt();
                
        /*****************************************************************/
        /*  Verificar com professor como realizar commit e rollback aqui */
        /*****************************************************************/                
        if (opcao == 1) {
            // descontar valor do saldo do cartao            
            caixaRUBean.finalizarAlmoco(true);
            sessao.getTransaction().commit();
        } else {
            caixaRUBean.finalizarAlmoco(false);
            sessao.getTransaction().rollback();
        }
        
        sessao = HibernateUtil.getSessionFactory().getCurrentSession(); 
        sessao.beginTransaction();
        // Encerramento do caixa                
        caixaRUBean.getCaixaRU().setDataFechamento(Calendar.getInstance());
        //caixaRUBean.realizarFechamentoCaixa();
        caixaRUBean.getCaixaRU().setValorFechamento(15);
        caixaRUBean.salvar();        
        sessao.getTransaction().commit();        
        /*
        daoAluno = new AlunoDAO();
        aluno.getCartao().descontar(valorAtualAlmoco.getValorAlmoco());
        daoAluno.salvar(aluno);
        daoAluno.encerrar();
        
        System.out.println("Data: " + aluno.getCartao().getDataCredito() + " Saldo: " + aluno.getCartao().getSaldo());
        */               
    }
}

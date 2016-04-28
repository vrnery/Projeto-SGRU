
import br.edu.ifrs.restinga.sgru.excessao.DadoPessoaInvalidoException;
import br.edu.ifrs.restinga.sgru.modelo.Aluno;
import br.edu.ifrs.restinga.sgru.modelo.Cartao;
import br.edu.ifrs.restinga.sgru.modelo.OperadorCaixa;
import br.edu.ifrs.restinga.sgru.modelo.Recarga;
import br.edu.ifrs.restinga.sgru.modelo.ValorAlmoco;
import br.edu.ifrs.restinga.sgru.persistencia.AlunoDAO;
import br.edu.ifrs.restinga.sgru.persistencia.HibernateUtil;
import br.edu.ifrs.restinga.sgru.persistencia.OperadorCaixaDAO;
import br.edu.ifrs.restinga.sgru.persistencia.RecargaDAO;
import br.edu.ifrs.restinga.sgru.persistencia.ValorAlmocoDAO;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.hibernate.Session;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marcelo.lima
 */
public class Cadastros {
    public static void main (String[] args) {
        /* *******************************
        *       Cadastros - Inicio
        *  ******************************* */ 
        Session sessao  = HibernateUtil.getSessionFactory().getCurrentSession();
        sessao.beginTransaction();
        Calendar data;
        
        OperadorCaixa oper = new OperadorCaixa();
        OperadorCaixaDAO daoOper = new OperadorCaixaDAO();
        try {
            oper.setNome("Operador");
            oper.setMatricula("987654");
            oper.setEmail("operador@email.com.br");
            oper.setTelefone("(51) 5555-5555");
        } catch (DadoPessoaInvalidoException e) {
            System.out.println(e);
        }
        oper.setLogin("oper1");
        oper.setSenha("oper");
        daoOper.salvar(oper);        
        
        /*
        data = new GregorianCalendar(2016, 3, 17);
        CaixaRU caixaRU = new CaixaRU();
        CaixaRUDAO daoCaixaRU = new CaixaRUDAO();
        caixaRU.setDataAbertura(data);
        caixaRU.setOperadorCaixa(oper);
        caixaRU.setValorAbertura(0);
        daoCaixaRU.salvar(caixaRU);        
        
        caixaRU = new CaixaRU();
        daoCaixaRU = new CaixaRUDAO();
        caixaRU.setDataAbertura(Calendar.getInstance());
        caixaRU.setOperadorCaixa(oper);
        caixaRU.setValorAbertura(0);
        daoCaixaRU.salvar(caixaRU);
        */
                
        Aluno aluno = new Aluno();
        AlunoDAO daoAluno = new AlunoDAO();
        
        Recarga recarga = new Recarga();
        RecargaDAO daoRecarga = new RecargaDAO();
        
        /*********************************************/
        /*              Cassiane                     */
        /*********************************************/
        try {
            aluno.setNome("Cassiane");
            aluno.setEmail("cassiane@email.com.br");
            aluno.setCaminhoFoto("C:\\Fotos");
            aluno.setLogin("cassiane");
            aluno.setSenha("cassiane");
            aluno.setMatricula("147");
            aluno.setTelefone("(51) 3333-3333");
        } catch (DadoPessoaInvalidoException e) {
            System.out.println(e);
        }
        aluno.setCartao(new Cartao());        
        aluno.getCartao().setDataCredito(Calendar.getInstance());
        aluno.getCartao().setSaldo(0);        
        daoAluno.salvar(aluno);        
        
        data = new GregorianCalendar(2016, 3, 14);
        recarga.setDataCredito(data);
        recarga.setUtilizado(false);
        recarga.setValorRecarregado(100);
        recarga.setCartao(aluno.getCartao());
        daoRecarga.salvar(recarga);        
        
        recarga = new Recarga();        
        data = new GregorianCalendar(2016, 3, 14);
        recarga.setDataCredito(data);
        recarga.setUtilizado(false);
        recarga.setValorRecarregado(200);
        recarga.setCartao(aluno.getCartao());
        daoRecarga.salvar(recarga);                
        
        recarga = new Recarga();        
        data = new GregorianCalendar(2016, 3, 15);
        recarga.setDataCredito(data);
        recarga.setUtilizado(false);
        recarga.setValorRecarregado(300);
        recarga.setCartao(aluno.getCartao());
        daoRecarga.salvar(recarga);                        
        
        recarga = new Recarga();        
        data = new GregorianCalendar(2016, 3, 16);
        recarga.setDataCredito(data);
        recarga.setUtilizado(false);
        recarga.setValorRecarregado(400);
        recarga.setCartao(aluno.getCartao());
        daoRecarga.salvar(recarga);                                
                
        /*********************************************/
        /*              Marcelo                      */
        /*********************************************/        
        aluno = new Aluno();
        try {
            aluno.setNome("Marcelo");
            aluno.setEmail("marcelo@email.com.br");
            aluno.setCaminhoFoto("C:\\Fotos");
            aluno.setLogin("marcelo");
            aluno.setSenha("marcelo");
            aluno.setMatricula("258");
            aluno.setTelefone("(51) 4444-4444");
        } catch (DadoPessoaInvalidoException e) {
            System.out.println(e);
        }
        aluno.setCartao(new Cartao());        
        aluno.getCartao().setDataCredito(Calendar.getInstance());
        aluno.getCartao().setSaldo(0);        
        daoAluno.salvar(aluno);                
        
        data = new GregorianCalendar(2016, 3, 19);
        recarga.setDataCredito(data);
        recarga.setUtilizado(false);
        recarga.setValorRecarregado(100);
        recarga.setCartao(aluno.getCartao());
        daoRecarga.salvar(recarga);        
        
        recarga = new Recarga();        
        data = new GregorianCalendar(2016, 3, 17);
        recarga.setDataCredito(data);
        recarga.setUtilizado(false);
        recarga.setValorRecarregado(200);
        recarga.setCartao(aluno.getCartao());
        daoRecarga.salvar(recarga);                
        
        recarga = new Recarga();        
        data = new GregorianCalendar(2016, 3, 18);
        recarga.setDataCredito(data);
        recarga.setUtilizado(false);
        recarga.setValorRecarregado(300);
        recarga.setCartao(aluno.getCartao());
        daoRecarga.salvar(recarga);                        
        
        recarga = new Recarga();        
        data = new GregorianCalendar(2016, 3, 16);
        recarga.setDataCredito(data);
        recarga.setUtilizado(false);
        recarga.setValorRecarregado(400);
        recarga.setCartao(aluno.getCartao());
        daoRecarga.salvar(recarga);                                        
        
        /*********************************************/
        /*              Vanderson                    */
        /*********************************************/        
        
        aluno = new Aluno();
        try {
            aluno.setNome("Vanderson");
            aluno.setEmail("vanderson@email.com.br");
            aluno.setCaminhoFoto("C:\\Fotos");
            aluno.setLogin("vanderson");
            aluno.setSenha("vanderson");
            aluno.setMatricula("369");
            aluno.setTelefone("(51) 5555-5555");
        } catch (DadoPessoaInvalidoException e) {
            System.out.println(e);
        }
        aluno.setCartao(new Cartao());        
        aluno.getCartao().setDataCredito(Calendar.getInstance());
        aluno.getCartao().setSaldo(0);        
        daoAluno.salvar(aluno);                                                        
        
        recarga.setDataCredito(Calendar.getInstance());
        recarga.setUtilizado(false);
        recarga.setValorRecarregado(100);
        recarga.setCartao(aluno.getCartao());
        daoRecarga.salvar(recarga);        
        
        recarga = new Recarga();                
        recarga.setDataCredito(Calendar.getInstance());
        recarga.setUtilizado(false);
        recarga.setValorRecarregado(200);
        recarga.setCartao(aluno.getCartao());
        daoRecarga.salvar(recarga);                
        
        recarga = new Recarga();                
        recarga.setDataCredito(Calendar.getInstance());
        recarga.setUtilizado(false);
        recarga.setValorRecarregado(300);
        recarga.setCartao(aluno.getCartao());
        daoRecarga.salvar(recarga);                        
        
        recarga = new Recarga();                
        recarga.setDataCredito(Calendar.getInstance());
        recarga.setUtilizado(false);
        recarga.setValorRecarregado(400);
        recarga.setCartao(aluno.getCartao());
        daoRecarga.salvar(recarga);                                                
        /****************************************************/
                
        // Salva quatro valores para teste
        ValorAlmoco valor = new ValorAlmoco();
        ValorAlmocoDAO daoValor = new ValorAlmocoDAO();
                        
        data = new GregorianCalendar(2016,3,14);
        valor.setDataValor(data);
        valor.setValorAlmoco(1);
        daoValor.salvar(valor);        
        
        daoValor = new ValorAlmocoDAO();
        valor = new ValorAlmoco();
        data = new GregorianCalendar(2016,3,15);
        valor.setDataValor(data);
        valor.setValorAlmoco(1.2);
        daoValor.salvar(valor);        
        
        daoValor = new ValorAlmocoDAO();
        valor = new ValorAlmoco();
        data = new GregorianCalendar(2016,3,16);
        valor.setDataValor(data);
        valor.setValorAlmoco(1.3);
        daoValor.salvar(valor);        
        
        daoValor = new ValorAlmocoDAO();
        valor = new ValorAlmoco();
        data = new GregorianCalendar(2016,3,18);
        valor.setDataValor(data);
        valor.setValorAlmoco(1.4);
        daoValor.salvar(valor);   
        sessao.getTransaction().commit();
        
        /* *******************************
        *       Cadastros - Fim
        *  ******************************* */                         
    }
}

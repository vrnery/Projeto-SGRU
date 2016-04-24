
import br.edu.ifrs.restinga.sgru.modelo.Aluno;
import br.edu.ifrs.restinga.sgru.modelo.CaixaRU;
import br.edu.ifrs.restinga.sgru.modelo.Cartao;
import br.edu.ifrs.restinga.sgru.modelo.OperadorCaixa;
import br.edu.ifrs.restinga.sgru.modelo.Recarga;
import br.edu.ifrs.restinga.sgru.modelo.ValorAlmoco;
import br.edu.ifrs.restinga.sgru.persistencia.AlunoDAO;
import br.edu.ifrs.restinga.sgru.persistencia.CaixaRUDAO;
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
        oper.setNome("Operador");
        oper.setMatricula("987654");
        oper.setEmail("operador@email");
        oper.setTelefone("55555555");
        oper.setLogin("oper1");
        oper.setSenha("oper");
        daoOper.salvar(oper);        
        
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
                
        Aluno aluno = new Aluno();
        AlunoDAO daoAluno = new AlunoDAO();
        
        aluno.setNome("Aluno");
        aluno.setEmail("aluno@email");
        aluno.setCaminhoFoto("C:\\Fotos");
        aluno.setLogin("aluno");
        aluno.setSenha("senha");
        aluno.setMatricula("123456");
        aluno.setTelefone("66666666");
        aluno.setCartao(new Cartao());        
        aluno.getCartao().setDataCredito(Calendar.getInstance());
        aluno.getCartao().setSaldo(0);        
        daoAluno.salvar(aluno);        
        
        Recarga recarga = new Recarga();
        RecargaDAO daoRecarga = new RecargaDAO();
                
        data = new GregorianCalendar(2016, 3, 17);
        recarga.setDataCredito(data);
        recarga.setUtilizado(false);
        recarga.setValorRecarregado(100);
        recarga.setCartao(aluno.getCartao());
        daoRecarga.salvar(recarga);        
        
        recarga = new Recarga();        
        data = new GregorianCalendar(2016, 3, 23);
        recarga.setDataCredito(data);
        recarga.setUtilizado(false);
        recarga.setValorRecarregado(200);
        recarga.setCartao(aluno.getCartao());
        daoRecarga.salvar(recarga);                
        
        recarga = new Recarga();        
        data = new GregorianCalendar(2016, 3, 22);
        recarga.setDataCredito(data);
        recarga.setUtilizado(false);
        recarga.setValorRecarregado(300);
        recarga.setCartao(aluno.getCartao());
        daoRecarga.salvar(recarga);                        
        
        recarga = new Recarga();        
        data = new GregorianCalendar(2016, 3, 17);
        recarga.setDataCredito(data);
        recarga.setUtilizado(false);
        recarga.setValorRecarregado(400);
        recarga.setCartao(aluno.getCartao());
        daoRecarga.salvar(recarga);                        
        
        // Adiciona a recarga ao cartao do aluno
        //daoAluno = new AlunoDAO();
        //aluno.getCartao().setRecarga(recarga);        
        //daoAluno.salvar(aluno);        
                
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

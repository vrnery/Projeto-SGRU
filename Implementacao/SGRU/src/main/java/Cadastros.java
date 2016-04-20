
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
        Calendar data = new GregorianCalendar();
        
        OperadorCaixa oper = new OperadorCaixa();
        OperadorCaixaDAO daoOper = new OperadorCaixaDAO();
        oper.setNome("Operador");
        oper.setEmail("operador@email");
        oper.setTelefone("55555555");
        oper.setLogin("oper1");
        oper.setSenha("oper");
        daoOper.salvar(oper);        
                
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
                
        data.set(2016, 04, 17);
        recarga.setDataCredito(data);
        recarga.setUtilizado(false);
        recarga.setValorRecarregado(100);
        recarga.setCartao(aluno.getCartao());
        daoRecarga.salvar(recarga);        
        
        // Adiciona a recarga ao cartao do aluno
        daoAluno = new AlunoDAO();
        aluno.getCartao().setRecarga(recarga);        
        daoAluno.salvar(aluno);        
                
        // Salva quatro valores para teste
        ValorAlmoco valor = new ValorAlmoco();
        ValorAlmocoDAO daoValor = new ValorAlmocoDAO();
                        
        data.set(2016,04,14);
        valor.setDataValor(data);
        valor.setValorAlmoco(1);
        daoValor.salvar(valor);        
        
        daoValor = new ValorAlmocoDAO();
        valor = new ValorAlmoco();
        data.set(2016,04,15);
        valor.setDataValor(data);
        valor.setValorAlmoco(1.2);
        daoValor.salvar(valor);        
        
        daoValor = new ValorAlmocoDAO();
        valor = new ValorAlmoco();
        data.set(2016,04,16);
        valor.setDataValor(data);
        valor.setValorAlmoco(1.3);
        daoValor.salvar(valor);        
        
        daoValor = new ValorAlmocoDAO();
        valor = new ValorAlmoco();
        data.set(2016,04,18);
        valor.setDataValor(data);
        valor.setValorAlmoco(1.4);
        daoValor.salvar(valor);   
        sessao.getTransaction().commit();
        
        /* *******************************
        *       Cadastros - Fim
        *  ******************************* */                         
    }
}

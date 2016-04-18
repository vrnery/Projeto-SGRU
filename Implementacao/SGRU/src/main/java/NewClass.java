
import br.edu.ifrs.restinga.sgru.bean.AlunoBean;
import br.edu.ifrs.restinga.sgru.bean.CaixaRUBean;
import br.edu.ifrs.restinga.sgru.modelo.Aluno;
import br.edu.ifrs.restinga.sgru.modelo.CaixaRU;
import br.edu.ifrs.restinga.sgru.modelo.Cartao;
import br.edu.ifrs.restinga.sgru.modelo.OperadorCaixa;
import br.edu.ifrs.restinga.sgru.modelo.Recarga;
import br.edu.ifrs.restinga.sgru.modelo.ValorAlmoco;
import br.edu.ifrs.restinga.sgru.modelo.VendaAlmoco;
import br.edu.ifrs.restinga.sgru.persistencia.AlunoDAO;
import br.edu.ifrs.restinga.sgru.persistencia.CaixaRUDAO;
import br.edu.ifrs.restinga.sgru.persistencia.OperadorCaixaDAO;
import br.edu.ifrs.restinga.sgru.persistencia.RecargaDAO;
import br.edu.ifrs.restinga.sgru.persistencia.ValorAlmocoDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        
        /* *******************************
        *       Cadastros - Inicio
        *  ******************************* */ 
        OperadorCaixa oper = new OperadorCaixa();
        OperadorCaixaDAO daoOper = new OperadorCaixaDAO();
        oper.setNome("Operador");
        oper.setEmail("operador@email");
        oper.setTelefone("55555555");
        oper.setLogin("oper1");
        oper.setSenha("oper");
        daoOper.salvar(oper);
        daoOper.encerrar();
                
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
        aluno.getCartao().setDataCredito(new Date());
        aluno.getCartao().setSaldo(0);        
        daoAluno.salvar(aluno);
        daoAluno.encerrar();
        
        Recarga recarga = new Recarga();
        RecargaDAO daoRecarga = new RecargaDAO();
        
        Date dataCredito = new Date(2016,04,17);
        recarga.setDataCredito(dataCredito);
        recarga.setUtilizado(false);
        recarga.setValorRecarregado(100);
        recarga.setCartao(aluno.getCartao());
        daoRecarga.salvar(recarga);
        daoRecarga.encerrar();
        
        // Adiciona a recarga ao cartao do aluno
        daoAluno = new AlunoDAO();
        aluno.getCartao().setRecarga(recarga);        
        daoAluno.salvar(aluno);
        daoAluno.encerrar();
                
        // Salva quatro valores para teste
        ValorAlmoco valor = new ValorAlmoco();
        ValorAlmocoDAO daoValor = new ValorAlmocoDAO();
                
        valor.setDataValor(new Date(2016,04,14));
        valor.setValorAlmoco(1);
        daoValor.salvar(valor);
        daoValor.encerrar();
        
        daoValor = new ValorAlmocoDAO();
        valor = new ValorAlmoco();
        valor.setDataValor(new Date(2016,04,15));
        valor.setValorAlmoco(1.2);
        daoValor.salvar(valor);
        daoValor.encerrar();
        
        daoValor = new ValorAlmocoDAO();
        valor = new ValorAlmoco();
        valor.setDataValor(new Date(2016,04,16));
        valor.setValorAlmoco(1.3);
        daoValor.salvar(valor);
        daoValor.encerrar();
        
        daoValor = new ValorAlmocoDAO();
        valor = new ValorAlmoco();
        valor.setDataValor(new Date(2016,04,18));
        valor.setValorAlmoco(1.4);
        daoValor.salvar(valor);
        daoValor.encerrar();                        
        
        /* *******************************
        *       Cadastros - Fim
        *  ******************************* */                 
       
        // O aluno deveria ser carregado apos a abertura do caixa
        // mas uma excecao nested transactio eh lancada, pois
        // teriamo duas sessoes abertas ao mesmo tempo
        // Eh preciso ver com o professor como resolver isso
        AlunoBean alunoBean = new AlunoBean();        
        alunoBean.carregar("123456");          
        alunoBean.encerrar();
        
        // abre o caixa
        CaixaRUBean caixaBean = new CaixaRUBean();       
        
        caixaBean.getCaixaRU().setDataAbertura(new Date());
        caixaBean.getCaixaRU().setOperadorCaixa(oper);
        caixaBean.getCaixaRU().setValorAbertura(0);
        
        // Simula uma venda
        
        /* *******************************
        *       1 - Consulta aluno
        *  ******************************* */ 
        // carrega aluno        
        //AlunoBean alunoBean = new AlunoBean();        
        //alunoBean.carregar("123456");
        //alunoBean.encerrar();
        
        // Neste ponto, aguarda-se pela confirmacao do operador
        // de caixa
        caixaBean.realizarVendaAlmoco(alunoBean.getAluno());
        
        // Encerramento do caixa
        CaixaRUDAO daoCaixa = new CaixaRUDAO();
        daoCaixa.salvar(caixaBean.getCaixaRU());
        daoCaixa.encerrar();

        /*
        daoAluno = new AlunoDAO();
        aluno.getCartao().descontar(valorAtualAlmoco.getValorAlmoco());
        daoAluno.salvar(aluno);
        daoAluno.encerrar();
        
        System.out.println("Data: " + aluno.getCartao().getDataCredito() + " Saldo: " + aluno.getCartao().getSaldo());
        */
    }
}

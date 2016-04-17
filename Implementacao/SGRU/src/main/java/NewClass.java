
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
        aluno.getCartao().setDataExpiracao(new Date());
        aluno.getCartao().setSaldo(0);        
        daoAluno.salvar(aluno);
        daoAluno.encerrar();
        
        Recarga recarga = new Recarga();
        RecargaDAO daoRecarga = new RecargaDAO();
        
        recarga.setDataCredito(new Date());
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
                
        ValorAlmoco valor = new ValorAlmoco();
        ValorAlmocoDAO daoValor = new ValorAlmocoDAO();
        
        valor.setDataValor(new Date());
        valor.setValorAlmoco(1.3);
        daoValor.salvar(valor);
        daoValor.encerrar();
        
        // simula o carregamento do valor atual do almoco
        ValorAlmoco valorAtual;
        daoValor = new ValorAlmocoDAO();
        valorAtual = daoValor.carregar();
        daoValor.encerrar();
        
        System.out.println("Valor atual do almoco: " + valorAtual.getValorAlmoco());
        
        CaixaRU caixa = new CaixaRU();
        CaixaRUDAO daoCaixa = new CaixaRUDAO();
        
        caixa.setDataAbertura(new Date());
        caixa.setOperadorCaixa(oper);
        caixa.setValorAbertura(0);
        
        List<VendaAlmoco> listaVenda = new ArrayList();
        caixa.setVendaAlmoco(listaVenda);
        caixa.getVendaAlmoco().add(new VendaAlmoco());
        caixa.getVendaAlmoco().get(0).setCartao(aluno.getCartao());
        caixa.getVendaAlmoco().get(0).setValorAlmoco(valorAtual);        
        caixa.getVendaAlmoco().get(0).setCaixaRU(caixa);
        caixa.getVendaAlmoco().get(0).setFormaPagamento("Cartao");
        
        daoCaixa.salvar(caixa);
        daoCaixa.encerrar();

        daoAluno = new AlunoDAO();
        aluno.getCartao().descontar(valorAtual.getValorAlmoco());
        daoAluno.salvar(aluno);
        daoAluno.encerrar();
        
        System.out.println("Data: " + aluno.getCartao().getDataExpiracao() + " Saldo: " + aluno.getCartao().getSaldo());
    }
}

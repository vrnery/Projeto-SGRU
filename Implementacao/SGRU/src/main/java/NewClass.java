
import br.edu.ifrs.restinga.sgru.modelo.Aluno;
import br.edu.ifrs.restinga.sgru.modelo.Cartao;
import br.edu.ifrs.restinga.sgru.modelo.Pessoa;
import br.edu.ifrs.restinga.sgru.persistencia.AlunoDAO;
import br.edu.ifrs.restinga.sgru.persistencia.PessoaDAO;
import java.util.Date;

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
        
        Aluno aluno = new Aluno();
        AlunoDAO dao = new AlunoDAO();
        
        aluno.setNome("Lima");
        aluno.setEmail("lima@email");
        aluno.setCaminhoFoto("C:\\Fotos");
        aluno.setLogin("lima");
        aluno.setSenha("limasecreto");
        aluno.setMatricula("sor10");
        aluno.setTelefone("LimaTel");
        
        dao.salvar(aluno);
        dao.encerrar();
        
        dao = new AlunoDAO();
                
        aluno = dao.carregar("sor10");        
        aluno.setCartao(new Cartao());        
        aluno.getCartao().setDataExpiracao(new Date());
        aluno.getCartao().setSaldo(0);
        dao.salvar(aluno);
        dao.encerrar();
    }
}

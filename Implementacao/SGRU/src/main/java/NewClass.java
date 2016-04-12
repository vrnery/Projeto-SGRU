
import br.edu.ifrs.restinga.sgru.modelo.Aluno;
import br.edu.ifrs.restinga.sgru.modelo.Pessoa;
import br.edu.ifrs.restinga.sgru.persistencia.AlunoDAO;
import br.edu.ifrs.restinga.sgru.persistencia.PessoaDAO;

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
        /*
        Aluno aluno = new Aluno();
        AlunoDAO dao = new AlunoDAO();
        
        aluno.setNome("Lima");
        aluno.setEmail("lima@email");
        aluno.setCaminhoFoto("c:\foto");
        aluno.setLogin("lima");
        aluno.setSenha("limasecreto");
        aluno.setMatricula("Lima123");
        aluno.setTelefone("LimaTel");
        
        dao.salvar(aluno);
        */
        Pessoa pessoa = new Pessoa();
        PessoaDAO dao = new PessoaDAO();
        pessoa.setEmail("email");
        pessoa.setLogin("login");
        pessoa.setNome("nome");
        pessoa.setSenha("senha");
        pessoa.setTelefone("telefone");
        dao.salvar(pessoa);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.persistencia;

import br.edu.ifrs.restinga.sgru.modelo.Cliente;
import br.edu.ifrs.restinga.sgru.modelo.Recarga;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author 10070187
 */
public class RelatoriosDAO {
    private final Session sessao;

    public RelatoriosDAO() {
        sessao = HibernateUtil.getSessionFactory().getCurrentSession();        
    }
    public List<Recarga> relatorio(boolean relatorioCompras) {
        if (relatorioCompras == true){
        }
        else{
            String sql;
            sql = "FROM Recarga rec";
            /*
            if (pessoa instanceof Cliente) {
                return false;
            }else{
               sql = "FROM Recarga 
                      inner join cartao crt on rec.idCartao = crt.id
                      inner join cliente c on crt.id = c.idCartao"
                if (idCodTipoCliente <> null){
                    sql = sql+"where c.idCodTipoCliente = ?"
                }
                if (){
                }
            }
                
                    
            }
               
            }
            
            return sessao.createQuery("FROM Recarga WHERE idCodTipoCliente=:codTipoCliente ORDER BY nome")
                .setString("codTipoCliente", String.valueOf(codTipoCliente)).list();   */
        }        
        /*
        if (codTipoCliente == -1) {
            return sessao.createQuery("FROM Cliente ORDER BY nome").list();
        } else {
            return sessao.createQuery("FROM Cliente WHERE idCodTipoCliente=:codTipoCliente ORDER BY nome")
                .setString("codTipoCliente", String.valueOf(codTipoCliente)).list();   
        } */       
        return null;
    }
}

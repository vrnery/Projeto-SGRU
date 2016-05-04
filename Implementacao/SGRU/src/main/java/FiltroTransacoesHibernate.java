/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Leonardo
 */
public class FiltroTransacoesHibernate implements Filter {           
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession sessaoHTTP = req.getSession(true);
        
        TransacaoCaixaRU transacaoCorrente = null;        

        if(sessaoHTTP != null) {
            transacaoCorrente = (TransacaoCaixaRU) sessaoHTTP.getAttribute("transacaoCorrente");
            if(transacaoCorrente != null) {
                transacaoCorrente.iniciarRequisicao();
            }
        }

        chain.doFilter(request, response);
        if(sessaoHTTP != null && transacaoCorrente != null) {
            transacaoCorrente.finalizarRequisicao();
        }
    }

    @Override
    public void destroy() {
       
    }
    
}

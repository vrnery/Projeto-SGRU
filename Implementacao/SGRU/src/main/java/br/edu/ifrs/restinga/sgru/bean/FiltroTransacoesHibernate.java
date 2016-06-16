package br.edu.ifrs.restinga.sgru.bean;

import br.edu.ifrs.restinga.sgru.modelo.Pessoa;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Leonardo
 */
public class FiltroTransacoesHibernate implements Filter {
    private final static String FILTER_APPLIED = "_security_filter_applied";
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession sessaoHTTP = req.getSession(true);
        req.getPathInfo();
        String paginaAtual = new String(req.getRequestURL());
        
        // dont filter index.xhtml because otherwise an endless loop.	
        if ((request.getAttribute(FILTER_APPLIED) == null) && (!paginaAtual.endsWith("index.xhtml")) && (!paginaAtual.endsWith("cadastrarCliente.xhtml"))) {
            request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
            // If the session bean is not null get the session bean property
            // username.
            Pessoa user = null;
            if ((sessaoHTTP.getAttribute("autenticarBean")) != null) {
                AutenticarBean bean = ((br.edu.ifrs.restinga.sgru.bean.AutenticarBean)(sessaoHTTP.getAttribute("autenticarBean")));
                user = bean.getControlador().getPessoa();
            }
            if ((user == null) || (user.getLogin().isEmpty())) {
                // redireciona para a pagina de login
                resp.sendRedirect("/SGRU/faces/index.xhtml");
                return;
            }            
        }                        

        TransacaoCaixaRU transacaoCorrente = (TransacaoCaixaRU) sessaoHTTP.getAttribute("transacaoCorrente");
        if(transacaoCorrente != null) {
            transacaoCorrente.iniciarRequisicao();
        }
        
        chain.doFilter(request, response);
        if(transacaoCorrente != null) {
            transacaoCorrente.finalizarRequisicao();
        }                      
    }

    @Override
    public void destroy() {
       
    }
    
}

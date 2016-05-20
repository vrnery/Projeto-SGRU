/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import java.util.Calendar;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author marcelo.lima
 */
@ManagedBean
@RequestScoped
public class RelatorioBean {
    public Calendar maxDate(Calendar data) {
        return Calendar.getInstance();
    }
    
    public Calendar minDate(Calendar data) {
        return Calendar.getInstance();
    }    
}

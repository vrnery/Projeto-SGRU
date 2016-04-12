/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 *
 * @author marcelo.lima
 */
@Entity
@PrimaryKeyJoinColumn(name="id")
public class OperadorCaixa extends Pessoa {            
}

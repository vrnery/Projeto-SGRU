/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function calcular() {
    var quantidade = $('#txtQuantidade').val();
    var valor = $('#txtValor').val().replace( '.', ' ' ).replace( ',', '.' );
    valor = valor * quantidade;
    $('#txtTotal').val(valor);

};

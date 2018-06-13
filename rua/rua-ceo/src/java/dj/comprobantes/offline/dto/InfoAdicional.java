/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dj.comprobantes.offline.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author djacome
 */
public class InfoAdicional {

    private String nombre;
    private String valor;
    private Comprobante comprobante;

    public InfoAdicional(ResultSet resultado) {
        try {
            this.nombre = resultado.getString("nombre_srina");
            this.valor = resultado.getString("valor_srina");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Comprobante getComprobante() {
        return comprobante;
    }

    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

}

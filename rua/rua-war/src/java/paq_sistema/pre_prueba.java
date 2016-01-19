/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sistema;

import componentes.FacturaCxC;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_prueba extends Pantalla {

    private FacturaCxC fac = new FacturaCxC();

    public pre_prueba() {
        fac.setId("fac");
        fac.setFacturaCxC("");
        agregarComponente(fac);
    }

    @Override
    public void insertar() {
        fac.dibujar();
    }

    @Override
    public void guardar() {

    }

    @Override
    public void eliminar() {

    }

    public FacturaCxC getFac() {
        return fac;
    }

    public void setFac(FacturaCxC fac) {
        this.fac = fac;
    }

}

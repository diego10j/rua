/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sistema;

import componentes.CuentaCxc;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_prueba extends Pantalla {

    private CuentaCxc fac = new CuentaCxc();

    public pre_prueba() {
        fac.setId("fac");
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

    public CuentaCxc getFac() {
        return fac;
    }

    public void setFac(CuentaCxc fac) {
        this.fac = fac;
    }

}

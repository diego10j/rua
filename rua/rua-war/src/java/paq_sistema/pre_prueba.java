/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sistema;

import componentes.AsientoContable;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_prueba extends Pantalla {

    private AsientoContable asi = new AsientoContable();

    public pre_prueba() {

        asi.setId("asi");
        agregarComponente(asi);

    }

    @Override
    public void insertar() {

    }

    @Override
    public void guardar() {
        asi.dibujar();
    }

    @Override
    public void eliminar() {

    }

    public AsientoContable getAsi() {
        return asi;
    }

    public void setAsi(AsientoContable asi) {
        this.asi = asi;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gestion;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.logging.Logger;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author CONTABILIDAD
 */
public class pre_empleado_rubro extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private AutoCompletar aut_empleado = new AutoCompletar();

    public pre_empleado_rubro() {

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("gth_empleado_rubro", "ide_gtemr", 0);

        tab_tabla.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setId("pat_tabla");
        pat_panel.setPanelTabla(tab_tabla);
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);

        agregarComponente(div_division);

        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        aut_empleado.setId("aut_empleado");
        aut_empleado.setAutoCompletar("select IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, APELLIDO_PATERNO_GTEMP, APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP  from GTH_EMPLEADO");
        aut_empleado.setMetodoChange("filtrarEmpleado");

        bar_botones.agregarComponente(aut_empleado);
        bar_botones.agregarBoton(bot_limpiar);
    }

    @Override
    public void insertar() {

        tab_tabla.insertar();

    }

    @Override
    public void guardar() {

        tab_tabla.guardar();
        guardarPantalla();

    }

    @Override
    public void eliminar() {

        tab_tabla.eliminar();

    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public AutoCompletar getAut_empleado() {
        return aut_empleado;
    }

    public void setAut_empleado(AutoCompletar aut_empleado) {
        this.aut_empleado = aut_empleado;
    }

}

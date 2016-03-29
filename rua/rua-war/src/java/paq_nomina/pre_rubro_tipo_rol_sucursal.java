/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_rubro_tipo_rol_sucursal extends Pantalla {

    
    private Tabla tab_tabla = new Tabla();

    public pre_rubro_tipo_rol_sucursal() {        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("NRH_RUBRO_TIPO_SUCURSAL", "IDE_NRRTS", 1);
		tab_tabla.getColumna("IDE_SUCU").setCombo("SIS_SUCURSAL", "IDE_SUCU", "NOM_SUCU","");
		tab_tabla.getColumna("IDE_NRRUB").setCombo("select IDE_NRRUB,DETALLE_NRRUB,DETALLE_GTTEM from NRH_RUBRO RUB " +
				"left join GTH_TIPO_EMPLEADO tem on TEM.IDE_GTTEM=RUB.IDE_GTTEM");
		tab_tabla.getColumna("IDE_GTTCO").setCombo("GTH_TIPO_CONTRATO", "IDE_GTTCO", "DETALLE_GTTCO","");
		tab_tabla.setMostrarcampoSucursal(true);
        tab_tabla.getColumna("ACTIVO_NRRTS").setCheck();
        tab_tabla.getColumna("ACTIVO_NRRTS").setValorDefecto("true");
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
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
}

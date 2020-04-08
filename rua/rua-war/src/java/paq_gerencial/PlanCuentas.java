/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gerencial;

import pkg_contabilidad.*;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import persistencia.Conexion;
import servicios.contabilidad.ServicioContabilidadGeneral;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Louis
 * @version 2.0
 */
public class PlanCuentas extends Pantalla {

    Tabla tab_tabla1 = new Tabla();

    private Division div_division = new Division();
    private Arbol arb_arbol = new Arbol();

    public PlanCuentas() {



        //Configurar tabla2        
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setCampoPadre("con_ide_cndpc");
        tab_tabla1.setCampoNombre("nombre_cndpc");
        tab_tabla1.setTabla("con_det_plan_cuen", "ide_cndpc", 1);
        tab_tabla1.agregarArbol(arb_arbol);

        tab_tabla1.dibujar();

        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setId("pat_panel1");
        pat_panel1.setPanelTabla(tab_tabla1);

        arb_arbol.setId("arb_arbol");

        arb_arbol.dibujar();         

        div_division.setId("div_division");
        div_division.dividir2(arb_arbol, pat_panel1, "50%", "V");

        agregarComponente(div_division);

    }

    @Override
    public void insertar() {
        tab_tabla1.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        //guardarPantalla();        
    }

    @Override
    public void eliminar() {
        tab_tabla1.eliminar();
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }


    public Arbol getArb_arbol() {
        return arb_arbol;
    }

    public void setArb_arbol(Arbol arb_arbol) {
        this.arb_arbol = arb_arbol;
    }

}
                     

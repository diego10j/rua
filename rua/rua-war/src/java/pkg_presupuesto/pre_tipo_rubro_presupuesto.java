/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_presupuesto;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import org.primefaces.event.NodeSelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_tipo_rubro_presupuesto extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Division div_division = new Division();
    private Arbol arb_arbol = new Arbol();

    public pre_tipo_rubro_presupuesto() {

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("pre_tipo_rubro_presu", "ide_prtrp", 1);

        tab_tabla1.getColumna("nivel_prtrp").setCombo(utilitario.getListaNiveles());
        tab_tabla1.setCampoNombre("nombre_prtrp"); //Para que se configure el arbol
        tab_tabla1.setCampoPadre("pre_ide_prtrp"); //Para que se configure el arbol
        tab_tabla1.agregarArbol(arb_arbol); //Para que se configure el arbol        
//        tab_tabla1.setTipoFormulario(true);        
//        tab_tabla1.getGrid().setColumns(2);




        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);

        arb_arbol.setId("arb_arbol");

        arb_arbol.dibujar();

        div_division.setId("div_division");
        div_division.dividir2(arb_arbol, pat_panel, "21%", "V");


        agregarComponente(div_division);


    }

    public void seleccionar_arbol(NodeSelectEvent evt) {
        arb_arbol.seleccionarNodo(evt);
        tab_tabla1.setValorPadre(arb_arbol.getValorSeleccionado());
        tab_tabla1.ejecutarSql();
    }

    @Override
    public void insertar() {
        tab_tabla1.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla1.eliminar();
    }

    public Arbol getArb_arbol() {
        return arb_arbol;
    }

    public void setArb_arbol(Arbol arb_arbol) {
        this.arb_arbol = arb_arbol;
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }
}

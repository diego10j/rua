/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_inventario;

import framework.componentes.Tabla;
import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.event.NodeSelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_bodega extends Pantalla{

    private Tabla tab_tabla1 = new Tabla();
    private Division div_division = new Division();
    private Arbol arb_arbol = new Arbol();

    public pre_bodega() {
    
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("inv_bodega", "ide_inbod", 1);

        tab_tabla1.getColumna("nivel_inbod").setCombo(utilitario.getListaNiveles());
        tab_tabla1.setCampoNombre("nombre_inbod"); //Para que se configure el arbol
        tab_tabla1.setCampoPadre("inv_ide_inbod"); //Para que se configure el arbol
        tab_tabla1.agregarArbol(arb_arbol); //Para que se configure el arbol
        
        List lista = new ArrayList();
        Object fila1[] = {
            "true", "SI"
        };
        Object fila2[] = {
            "false", "NO"
        };
        lista.add(fila1);
        lista.add(fila2);
        tab_tabla1.getColumna("hace_asient_inbod").setRadio(lista, "false");     
           List lista1 = new ArrayList();
        Object fila11[] = {
            "true", "SI"
        };
        Object fila22[] = {
            "false", "NO"
        };
        lista1.add(fila11);
        lista1.add(fila22);
        tab_tabla1.getColumna("permi_factu_inbod").setRadio(lista1, "false");
        

        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        
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

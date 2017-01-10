/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_activos;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.event.NodeSelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_tipos_activos_fijos extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();

    private Arbol arb_arbol = new Arbol();

    public pre_tipos_activos_fijos() {

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("inv_articulo", "ide_inarti", 1);
        tab_tabla1.getColumna("nivel_inarti").setCombo(utilitario.getListaNiveles());
        tab_tabla1.setCampoNombre("nombre_inarti"); //Para que se configure el arbol
        tab_tabla1.setCampoPadre("inv_ide_inarti"); //Para que se configure el arbol
        tab_tabla1.agregarArbol(arb_arbol); //Para que se configure el arbol
        tab_tabla1.getColumna("ide_infab").setCombo("inv_fabricante", "ide_infab", "nombre_infab", "");
        tab_tabla1.getColumna("ide_inmar").setCombo("inv_marca", "ide_inmar", "nombre_invmar", "");
        tab_tabla1.getColumna("ide_inuni").setCombo("inv_unidad", "ide_inuni", "nombre_inuni", "");
        tab_tabla1.getColumna("ide_intpr").setCombo("inv_tipo_producto", "ide_intpr", "nombre_intpr", "");
        tab_tabla1.getColumna("ide_inepr").setCombo("inv_estado_produc", "ide_inepr", "nombre_inepr", "");
        tab_tabla1.getColumna("ide_intpr").setValorDefecto(utilitario.getVariable("p_act_tipo_activo_fijo"));
        tab_tabla1.getColumna("ide_intpr").setVisible(false);
        tab_tabla1.getColumna("ide_GEORG").setVisible(false);
        tab_tabla1.getColumna("es_combo_inarti").setVisible(false);
        tab_tabla1.getColumna("es_combo_inarti").setVisible(false);
        tab_tabla1.getColumna("ice_inarti").setVisible(false);
        tab_tabla1.getColumna("hace_kardex_inarti").setVisible(false);
        

        List lista = new ArrayList();
        Object fila1[] = {
            "1", "SI"
        };
        Object fila2[] = {
            "-1", "NO"
        };
        Object fila3[] = {
            "0", "NO  OBJETO"
        };
        lista.add(fila1);
        lista.add(fila2);
        lista.add(fila3);
        tab_tabla1.getColumna("iva_inarti").setRadio(lista, "1");
        tab_tabla1.getColumna("iva_inarti").setRadioVertical(true);
        tab_tabla1.setCondicion("ide_inarti=53");
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.dibujar();
        tab_tabla1.setCondicion("");
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);

        arb_arbol.setId("arb_arbol");
        arb_arbol.setCondicion("ide_inarti=53");        
        arb_arbol.dibujar();

        Division div_division = new Division();
        div_division.dividir2(arb_arbol, pat_panel, "24%", "V");
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_inventario;

import paq_activos.*;
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
public class pre_articulo_asiento extends Pantalla {
    
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Arbol arb_arbol = new Arbol();
    
    public pre_articulo_asiento() {
        
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("inv_articulo", "ide_inarti", 1);
        tab_tabla1.getColumna("ide_infab").setVisible(false);
        tab_tabla1.getColumna("ide_inmar").setVisible(false);
        tab_tabla1.getColumna("ide_inuni").setVisible(false);
        tab_tabla1.getColumna("ide_intpr").setVisible(false);
        tab_tabla1.getColumna("ide_inepr").setVisible(false);
        tab_tabla1.getColumna("ide_intpr").setVisible(false);
        tab_tabla1.getColumna("ide_GEORG").setVisible(false);
        tab_tabla1.getColumna("es_combo_inarti").setVisible(false);
        tab_tabla1.getColumna("es_combo_inarti").setVisible(false);
        tab_tabla1.getColumna("ice_inarti").setVisible(false);
        tab_tabla1.getColumna("hace_kardex_inarti").setVisible(false);
        //tab_tabla1.getColumna("inv_ide_inarti").setVisible(false);
        //tab_tabla1.getColumna("inv_ide_inarti").setValorDefecto("53");
        tab_tabla1.getColumna("iva_inarti").setVisible(false);
        tab_tabla1.getColumna("iva_inarti").setValorDefecto("1");
        tab_tabla1.setCondicion("ide_intpr=0 and inv_ide_inarti is not null");
        //tab_tabla1.getColumna("nivel_inarti").setVisible(false);
        tab_tabla1.getColumna("nivel_inarti").setValorDefecto("HIJO");
        tab_tabla1.getColumna("codigo_inarti").setMascara("000");
        tab_tabla1.getColumna("codigo_inarti").setRequerida(true);
        tab_tabla1.getColumna("codigo_inarti").setAncho(3);
        tab_tabla1.getColumna("codigo_inarti").setFiltro(true);
        tab_tabla1.getColumna("codigo_inarti").setLongitud(3);
        tab_tabla1.getColumna("nombre_inarti").setFiltro(true);
        tab_tabla1.setCondicion("nivel_inarti='HIJO'");
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();
        tab_tabla1.setRows(15);
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        

       tab_tabla2.setId("tab_tabla2");
		tab_tabla2.setTabla("INV_ASIENTO_INVENTARIO", "IDE_INASI", 2);
                tab_tabla2.getColumna("IDE_GELUA").setCombo("con_lugar_aplicac", "ide_cnlap", "nombre_cnlap", "");

		//tab_tabla2.getColumna("ide_cocac").setCombo(ser_contabilidad.servicioCatalogoCuentaCombo());
                tab_tabla2.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc||' '||nombre_cndpc", "");
                tab_tabla2.getColumna("ide_cndpc").setAutoCompletar();
                tab_tabla2.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setMensajeWarn("RUBROS PARA ASIENTO CONTABLE");		
		pat_panel2.setPanelTabla(tab_tabla2);
       
        
        Division div_tabla1 = new Division();
        div_tabla1.setId("div_tabla1");
        div_tabla1.dividir2(pat_panel, pat_panel2, "50%", "H");
        agregarComponente(div_tabla1);

        
    }
    
    @Override
    public void insertar() {
        tab_tabla2.insertar();
    }
    
    @Override
    public void guardar() {
        tab_tabla2.guardar();
        guardarPantalla();
    }
    
    @Override
    
    public void eliminar() {
        tab_tabla2.eliminar();
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

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }
    
}

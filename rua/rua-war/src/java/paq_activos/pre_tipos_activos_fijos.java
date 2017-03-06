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
    
    public pre_tipos_activos_fijos() {
        
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("inv_articulo", "ide_inarti", 1);
        tab_tabla1.getColumna("ide_infab").setVisible(false);
        tab_tabla1.getColumna("ide_inmar").setVisible(false);
        tab_tabla1.getColumna("ide_inuni").setVisible(false);
        tab_tabla1.getColumna("ide_intpr").setVisible(false);
        tab_tabla1.getColumna("ide_inepr").setVisible(false);
        tab_tabla1.getColumna("ide_intpr").setValorDefecto(utilitario.getVariable("p_act_tipo_activo_fijo"));
        tab_tabla1.getColumna("ide_intpr").setVisible(false);
        tab_tabla1.getColumna("ide_GEORG").setVisible(false);
        tab_tabla1.getColumna("es_combo_inarti").setVisible(false);
        tab_tabla1.getColumna("es_combo_inarti").setVisible(false);
        tab_tabla1.getColumna("ice_inarti").setVisible(false);
        tab_tabla1.getColumna("hace_kardex_inarti").setVisible(false);
        tab_tabla1.getColumna("inv_ide_inarti").setVisible(false);
        tab_tabla1.getColumna("inv_ide_inarti").setValorDefecto("53");
        tab_tabla1.getColumna("iva_inarti").setVisible(false);
        tab_tabla1.getColumna("iva_inarti").setValorDefecto("1");
        tab_tabla1.setCondicion("ide_intpr=0 and inv_ide_inarti is not null");
        tab_tabla1.getColumna("nivel_inarti").setVisible(false);
        tab_tabla1.getColumna("nivel_inarti").setValorDefecto("HIJO");
        tab_tabla1.getColumna("codigo_inarti").setMascara("000");
        tab_tabla1.getColumna("codigo_inarti").setRequerida(true);
        tab_tabla1.getColumna("codigo_inarti").setAncho(3);
        tab_tabla1.getColumna("codigo_inarti").setMascara("999");
        tab_tabla1.getColumna("codigo_inarti").setLongitud(3);
        
        tab_tabla1.dibujar();
        tab_tabla1.setCondicion("");
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        
        Division div_division = new Division();
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
        
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
    
    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }
    
    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }
    
}

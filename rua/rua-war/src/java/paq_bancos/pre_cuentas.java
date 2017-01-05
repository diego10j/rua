/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bancos;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import servicios.contabilidad.ServicioContabilidadGeneral;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_cuentas extends Pantalla {
    
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);
    
    public pre_cuentas() {
        
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("tes_banco", "ide_teban", 1);
        
        tab_tabla1.getColumna("es_caja_teban").setVisible(false);
        tab_tabla1.getColumna("es_caja_teban").setValorDefecto("false");
        tab_tabla1.setCampoOrden("es_caja_teban desc, nombre_teban");        
        tab_tabla1.agregarRelacion(tab_tabla2);
        
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);
        
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("tes_cuenta_banco", "ide_tecba", 2);
        tab_tabla2.setCondicionSucursal(true);
        tab_tabla2.getColumna("ide_tecba").setVisible(false);
        tab_tabla2.getColumna("ide_tetcb").setCombo("tes_tip_cuen_banc", "ide_tetcb", "nombre_tetcb", "");
        tab_tabla2.getColumna("ide_cndpc").setCombo(ser_contabilidad.getSqlCuentasHijas());
        tab_tabla2.getColumna("ide_cndpc").setAutoCompletar();
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        
        Division div_1 = new Division();
        div_1.dividir2(pat_panel1, pat_panel2, "40%", "H");
        
        agregarComponente(div_1);
        
    }
    
    @Override
    public void insertar() {
        
        utilitario.getTablaisFocus().insertar();
        
    }
    
    @Override
    public void guardar() {
        if (tab_tabla1.guardar()) {
            if (tab_tabla1.guardar()) {
                if (tab_tabla2.guardar()) {
                    utilitario.getConexion().guardarPantalla();
                }
            }
        }
    }
    
    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }
    
    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
    }
    
    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }
    
    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }
    
    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }
    
    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_contabilidad;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import servicios.contabilidad.ServicioContabilidadGeneral;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class pre_gasto extends Pantalla {
    
    public Tabla tab_tabla1 = new Tabla();
    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);
    
    public pre_gasto() {
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setHeader("GASTOS");
        tab_tabla1.setTabla("inv_articulo", "ide_inarti", -1);
        tab_tabla1.getColumna("nivel_inarti").setVisible(false);
        tab_tabla1.getColumna("ide_infab").setVisible(false);
        tab_tabla1.getColumna("ide_inmar").setVisible(false);
        tab_tabla1.getColumna("ide_inuni").setVisible(false);
        tab_tabla1.getColumna("ide_intpr").setVisible(false);
        tab_tabla1.getColumna("ide_intpr").setValorDefecto("2");
        tab_tabla1.getColumna("ide_inepr").setVisible(false);
        tab_tabla1.getColumna("nivel_inarti").setValorDefecto("HIJO");
        tab_tabla1.getColumna("hace_kardex_inarti").setValorDefecto("false");
        tab_tabla1.getColumna("hace_kardex_inarti").setVisible(false);
        tab_tabla1.getColumna("es_combo_inarti").setValorDefecto("false");
        tab_tabla1.getColumna("nombre_inarti").setRequerida(true);
        tab_tabla1.getColumna("iva_inarti").setVisible(false);
        tab_tabla1.getColumna("iva_inarti").setValorDefecto("1");
        tab_tabla1.getColumna("INV_IDE_INARTI").setVisible(false);
        tab_tabla1.getColumna("ice_inarti").setVisible(true);
        tab_tabla1.getColumna("ide_cndpc").setCombo(ser_contabilidad.getSqlCuentas());
        tab_tabla1.getColumna("ide_cndpc").setVisible(true);
        tab_tabla1.getColumna("ide_cndpc").setRequerida(true);
        tab_tabla1.getColumna("ide_georg").setVisible(false);
        tab_tabla1.getColumna("es_combo_inarti").setVisible(false);
        tab_tabla1.getColumna("observacion_inarti").setControl("Texto");
        tab_tabla1.getColumna("ide_cndpc").setVisible(true); //cuenta contable
        tab_tabla1.getColumna("ide_cndpc").setCombo(ser_contabilidad.getSqlCuentasHijas());
        tab_tabla1.getColumna("ide_cndpc").setAutoCompletar();
        tab_tabla1.setCondicion("ide_intpr=2");
        tab_tabla1.getColumna("INV_IDE_INARTI").setValorDefecto("2");
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        
        Division div = new Division();
        div.dividir1(pat_panel);
        agregarComponente(div);
    }
    
    @Override
    public void insertar() {
        tab_tabla1.insertar();
    }
    
    @Override
    public void guardar() {
        if (tab_tabla1.guardar()) {
            guardarPantalla();
        }
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

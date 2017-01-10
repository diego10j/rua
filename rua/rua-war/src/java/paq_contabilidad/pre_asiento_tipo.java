/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_contabilidad;

import sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import servicios.contabilidad.ServicioComprobanteContabilidad;
import servicios.contabilidad.ServicioContabilidadGeneral;

/**
 *
 * @author Diego
 */
public class pre_asiento_tipo extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();

    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);

    @EJB
    private final ServicioComprobanteContabilidad ser_comprobante = (ServicioComprobanteContabilidad) utilitario.instanciarEJB(ServicioComprobanteContabilidad.class);

    public pre_asiento_tipo() {

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("cont_nombre_asiento_contable", "ide_conac", 1);
        tab_tabla1.getColumna("ide_modu").setCombo("sis_modulo", "ide_modu", "nom_modu", "");
        tab_tabla1.getColumna("ide_cntcm").setCombo(ser_comprobante.getSqlTiposComprobante());
        tab_tabla1.getColumna("detalle_conac").setControl("Texto");
        tab_tabla1.setCondicionSucursal(true);
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("cont_asiento_tipo", "ide_coast", 2);
        tab_tabla2.getColumna("ide_cndpc").setCombo(ser_contabilidad.getSqlCuentas());
        tab_tabla2.getColumna("ide_cndpc").setAutoCompletar();
        tab_tabla2.getColumna("ide_cndpc").setRequerida(true);
        tab_tabla2.getColumna("ide_cnlap").setCombo(ser_comprobante.getSqlLugarAplica());
        tab_tabla2.getColumna("ide_cnlap").setPermitirNullCombo(false);
        tab_tabla2.getColumna("ide_cnlap").setLongitud(10);
        tab_tabla2.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "");
        tab_tabla2.getColumna("ide_inarti").setAutoCompletar();
        tab_tabla2.getColumna("ide_prcla").setCombo("pre_clasificador", "ide_prcla", "codigo_clasificador_prcla,descripcion_clasificador_prcla", "");
        tab_tabla2.getColumna("ide_prcla").setAutoCompletar();
        tab_tabla2.getColumna("ide_prmop").setCombo("pre_movimiento_presupuestario", "ide_prmop", "detalle_prmop", "");
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, pat_panel2, "50%", "H");
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {

        if (tab_tabla1.guardar()) {
            if (tab_tabla2.guardar()) {
                guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
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

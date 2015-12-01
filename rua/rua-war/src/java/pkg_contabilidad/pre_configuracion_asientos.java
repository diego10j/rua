/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_contabilidad;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.List;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_configuracion_asientos extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    private Division div_division = new Division();

    public pre_configuracion_asientos() {

        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");
        if (lis_plan != null) {

            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setTabla("con_cab_conf_asie", "ide_cncca", 1);
            tab_tabla1.agregarRelacion(tab_tabla2);
            tab_tabla1.setRows(6);
            tab_tabla1.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla1);

            tab_tabla2.setId("tab_tabla2");
            tab_tabla2.setTabla("con_vig_conf_asie", "ide_cnvca", 2);
            tab_tabla2.setCampoForanea("ide_cncca");
            tab_tabla2.agregarRelacion(tab_tabla3);
            tab_tabla2.setRows(3);
            tab_tabla2.dibujar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_tabla2);

            tab_tabla3.setId("tab_tabla3");
            tab_tabla3.setTabla("con_det_conf_asie", "ide_cndca", 3);
            tab_tabla3.setCampoForanea("ide_cnvca");
            tab_tabla3.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "ide_cncpc=" + lis_plan.get(0));
            tab_tabla3.getColumna("ide_cndpc").setAutoCompletar();

            tab_tabla3.getColumna("ide_intti").setCombo("inv_tip_tran_inve", "ide_intti", "nombre_intti", "");
            tab_tabla3.getColumna("ide_cncim").setCombo("con_cabece_impues", "ide_cncim", "nombre_cncim,casillero_cncim", "");
            tab_tabla3.getColumna("ide_cnpim").setCombo("con_porcen_impues", "ide_cnpim", "nombre_cnpim", "");
            tab_tabla3.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "");
            tab_tabla3.getColumna("ide_inarti").setAutoCompletar();
            tab_tabla3.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "");
            tab_tabla3.getColumna("ide_geper").setAutoCompletar();
            tab_tabla3.getColumna("ide_cndpc").setRequerida(true);
            tab_tabla3.getColumna("ide_acttr").setCombo("act_tipo_transaccion", "ide_acttr", "nombre_acttr", "");
            tab_tabla3.setCampoOrden("ide_cndca desc");
            tab_tabla3.setRows(5);
            tab_tabla3.dibujar();
            PanelTabla pat_panel3 = new PanelTabla();
            pat_panel3.setPanelTabla(tab_tabla3);

            div_division.setId("div_division");
            div_division.dividir3(pat_panel1, pat_panel2, pat_panel3, "35%", "50%", "H");

            agregarComponente(div_division);

        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        tab_tabla2.guardar();
        tab_tabla3.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
    }

    public void seleccionar_tabla2(SelectEvent evt) {
        tab_tabla2.seleccionarFila(evt);
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

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }
}

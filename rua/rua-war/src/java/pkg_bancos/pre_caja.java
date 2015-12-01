/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_bancos;


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
public class pre_caja extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Division div_division = new Division();

    public pre_caja() {

        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");

        if (lis_plan != null && !lis_plan.isEmpty()) {

            bar_botones.getBot_insertar().setUpdate("tab_tabla1,tab_tabla2");
            bar_botones.getBot_guardar().setUpdate("tab_tabla1,tab_tabla2");
            bar_botones.getBot_eliminar().setUpdate("tab_tabla1,tab_tabla2");

            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setTabla("tes_banco", "ide_teban", 1);
            tab_tabla1.getColumna("es_caja_teban").setVisible(false);
            tab_tabla1.setCondicion("es_caja_teban=true");
            tab_tabla1.getColumna("es_caja_teban").setValorDefecto("true");
            tab_tabla1.agregarRelacion(tab_tabla2);
            tab_tabla1.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla1);

            tab_tabla2.setId("tab_tabla2");
            tab_tabla2.setTabla("tes_cuenta_banco", "ide_tecba", 2);
            tab_tabla2.getColumna("ide_tetcb").setCombo("tes_tip_cuen_banc", "ide_tetcb", "nombre_tetcb", "");
            tab_tabla2.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "nombre_cndpc,codig_recur_cndpc", "nivel_cndpc='HIJO' and ide_cncpc=" + lis_plan.get(0));
            tab_tabla2.getColumna("ide_cndpc").setAutoCompletar();
            tab_tabla2.setCampoForanea("ide_teban");
            tab_tabla2.setCondicionSucursal(true);
            tab_tabla2.dibujar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_tabla2);


            div_division.setId("div_division");
            div_division.dividir2(pat_panel1, pat_panel2, "50%", "H");

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
        utilitario.getConexion().guardarPantalla();
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_bancos;


import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.List;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_bancos extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    private Division div_division = new Division();

    public pre_bancos() {

        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");

        if (lis_plan != null && !lis_plan.isEmpty()) {


            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setTabla("tes_banco", "ide_teban", 1);
            tab_tabla1.getColumna("es_caja_teban").setVisible(false);
            tab_tabla1.setCondicion("es_caja_teban=false");
            tab_tabla1.getColumna("es_caja_teban").setValorDefecto("false");
            tab_tabla1.agregarRelacion(tab_tabla2);
            tab_tabla1.agregarRelacion(tab_tabla3);
            tab_tabla1.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla1);

            tab_tabla2.setId("tab_tabla2");
            tab_tabla2.setTabla("tes_cuenta_banco", "ide_tecba", 2);
            tab_tabla2.getColumna("ide_tetcb").setCombo("tes_tip_cuen_banc", "ide_tetcb", "nombre_tetcb", "");
            tab_tabla2.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "nivel_cndpc='HIJO' and ide_cncpc=" + lis_plan.get(0));
            tab_tabla2.getColumna("ide_cndpc").setAutoCompletar();
            tab_tabla2.dibujar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_tabla2);

            tab_tabla3.setId("tab_tabla3");
            tab_tabla3.setTabla("tes_configura_conciliacion", "ide_tecco", 3);
            tab_tabla3.dibujar();
            PanelTabla pat_panel3 = new PanelTabla();
            pat_panel3.setPanelTabla(tab_tabla3);

            Etiqueta eti_tit = new Etiqueta();
            eti_tit.setStyle("font-size: 13px;color: black;font-weight: bold");
            eti_tit.setValue("CONFIGURACION PARA CONCILIACION");

            div_division.setId("div_division");

            Division div_conciliacion = new Division();
            div_conciliacion.setFooter(eti_tit, pat_panel3, "20%");

            Division div_1 = new Division();
            div_1.dividir2(pat_panel1, div_conciliacion, "50%", "V");
            div_division.dividir2(div_1, pat_panel2, "45%", "H");

            agregarComponente(div_division);

        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }

    }

    @Override
    public void insertar() {
        if (tab_tabla3.isFocus()) {
            if (tab_tabla3.getTotalFilas() == 0) {
                tab_tabla3.insertar();
            }
        } else {
            utilitario.getTablaisFocus().insertar();
        }
    }

    public boolean validarIngresoConfConciliacion() {
        if (tab_tabla3.getValor("num_colum_doc_tecco") == null || tab_tabla3.getValor("num_colum_doc_tecco").isEmpty()) {
            utilitario.agregarMensajeError("No se puede guardar los datos", "El numero de columna de documento no puede ser null o vacio");
            return false;
        }
        if (tab_tabla3.getValor("num_colum_monto_tecco") == null || tab_tabla3.getValor("num_colum_monto_tecco").isEmpty()) {
            utilitario.agregarMensajeError("No se puede guardar los datos", "El numero de columna del monto no puede ser null o vacio");
            return false;
        }
        if (Integer.parseInt(tab_tabla3.getValor("num_colum_doc_tecco")) <= 0) {
            utilitario.agregarMensajeError("No se puede guardar los datos", "El numero de columna de documento no puede cero o numero negativo");
            return false;
        }
        if (Integer.parseInt(tab_tabla3.getValor("num_colum_monto_tecco")) <= 0) {
            utilitario.agregarMensajeError("No se puede guardar los datos", "El numero de columna del monto no puede cero o numero negativo");
            return false;
        }
        return true;
    }

    @Override
    public void guardar() {
        if (tab_tabla3.isFilaInsertada() || tab_tabla3.isFilaModificada()) {
            if (validarIngresoConfConciliacion()) {
                tab_tabla3.guardar();
                utilitario.getConexion().guardarPantalla();
            }
        } else {
            if (tab_tabla1.isFocus()) {
                tab_tabla1.guardar();
                utilitario.getConexion().guardarPantalla();
            } else {
                if (tab_tabla2.isFocus()) {
                    System.out.println("ide_cndpc "+tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_cndpc"));
                    if (tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_cndpc") != null && !tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_cndpc").isEmpty()) {
                        if (tab_tabla2.getValor("nombre_tecba") != null && !tab_tabla2.getValor("nombre_tecba").isEmpty()) {
                            tab_tabla2.guardar();
                            utilitario.getConexion().guardarPantalla();
                        } else {
                            utilitario.agregarMensajeInfo("no se puede guardar", "No se adminte null en el campo nombre");
                        }
                    } else {
                        utilitario.agregarMensajeInfo("no se puede guardar", "No se adminte null en el campo cuenta");
                    }
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

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }
}

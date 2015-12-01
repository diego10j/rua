/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_rrhh;

import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.List;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 *
 */
public class pre_rubros extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Division div_division = new Division();
    private Boton bot_ver_formula = new Boton();
    private Dialogo dia_formula = new Dialogo();
    private Etiqueta eti_formula = new Etiqueta();
    private Etiqueta eti_mensaje = new Etiqueta();

    public pre_rubros() {

        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");

        if (lis_plan != null && !lis_plan.isEmpty()) {

            bot_ver_formula.setValue("Ver Formula");
            bot_ver_formula.setMetodo("verFormula");
            bar_botones.agregarBoton(bot_ver_formula);
            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setTabla("reh_cab_rubro", "ide_rhcru", 1);
            tab_tabla1.agregarRelacion(tab_tabla2);
            tab_tabla1.getColumna("ide_rhtru").setCombo("reh_tipo_rubro", "ide_rhtru", "nombre_rhtru", "");
            tab_tabla1.getColumna("ide_rhfca").setCombo("reh_forma_calculo", "ide_rhfca", "nombre_rhfca", "");
            tab_tabla1.getColumna("ide_rheru").setCombo("reh_estado_rubro", "ide_rheru", "nombre_rheru", "");
            tab_tabla1.getColumna("order_imprime_rhcru").setVisible(false);
            tab_tabla1.getColumna("nombre_rhcru").setFiltro(true);
            tab_tabla1.setRows(15);
           tab_tabla1.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla1);

            tab_tabla2.setId("tab_tabla2");
            tab_tabla2.setTabla("reh_detalle_rubro", "ide_rhdru", 2);
            tab_tabla2.setCampoForanea("ide_rhcru");
            tab_tabla2.getColumna("ide_cnlap").setCombo("con_lugar_aplicac", "ide_cnlap", "nombre_cnlap", "");
            tab_tabla2.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "nivel_cndpc='HIJO' and ide_cncpc=" + lis_plan.get(0));
            tab_tabla2.getColumna("ide_cndpc").setAutoCompletar();
            tab_tabla2.getColumna("ide_rhtas").setCombo("reh_tipo_asiento", "ide_rhtas", "nombre_rhtas", "");
            tab_tabla2.getColumna("ide_georg").setCombo("gen_organigrama", "ide_georg", "nombre_georg", "");
            tab_tabla2.getColumna("ide_georg").setAutoCompletar();
            tab_tabla2.dibujar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_tabla2);

            div_division.setId("div_division");
            div_division.dividir2(pat_panel1, pat_panel2, "70%", "H");
            agregarComponente(div_division);

            dia_formula.setId("dia_formula");
            dia_formula.setTitle("Visualizador de Formula");
            dia_formula.setWidth("40%");
            dia_formula.setHeight("18%");
            dia_formula.setModal(false);
            Grupo gru_cuerpo = new Grupo();
            eti_mensaje.setValue("Formula del Rubro: ");
            eti_mensaje.setStyle("font-size: 15px;border: none;text-shadow: 0px 2px 3px #ccc;background: none;");
            eti_formula.setStyle("font-size: 18px;");
            Grid gri_clave = new Grid();
            gri_clave.setWidth("100%");
            gri_clave.setStyle("text-align: center;");
            gri_clave.getChildren().add(eti_formula);
            gru_cuerpo.getChildren().add(eti_mensaje);
            gru_cuerpo.getChildren().add(gri_clave);
            dia_formula.getBot_aceptar().setMetodo("aceptar_formula");
            dia_formula.setDialogo(gru_cuerpo);
            agregarComponente(dia_formula);
//

        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }


    }

    public void aceptar_formula() {
        dia_formula.cerrar();
    }

    public void verFormula() {
        cls_nomina n = new cls_nomina();
        String formula = n.obtenerParametroRubros("formula_rhcru", tab_tabla1.getValor("ide_rhcru"));
        if (formula != null && !formula.isEmpty()) {
            if (formula.substring(0, 1).equalsIgnoreCase("=")) {
                eti_mensaje.setValue("Formula del Rubro: " + n.obtenerParametroRubros("nombre_rhcru", tab_tabla1.getValor("ide_rhcru")));
                eti_formula.setValue(n.obtenerFormulaReemplazada(formula,false));
                dia_formula.dibujar();
            } else {
                utilitario.agregarMensajeInfo("Atencion", "El rubro seleccionado no tiene formula de calculo");
            }
        } else {
            utilitario.agregarMensajeInfo("Atencion", "El rubro seleccionado no tiene formula de calculo");
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

    public Dialogo getDia_formula() {
        return dia_formula;
    }

    public void setDia_formula(Dialogo dia_formula) {
        this.dia_formula = dia_formula;
    }
}

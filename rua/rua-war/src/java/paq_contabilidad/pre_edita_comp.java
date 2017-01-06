/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_contabilidad;

import pkg_contabilidad.*;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.MarcaAgua;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.List;
import javax.faces.event.AjaxBehaviorEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 *
 */
public class pre_edita_comp extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Etiqueta eti_suma_debe = new Etiqueta();
    private Etiqueta eti_suma_haber = new Etiqueta();
    private Etiqueta eti_suma_diferencia = new Etiqueta();
    //Parametros del sistema
    private String p_con_lugar_debe = utilitario.getVariable("p_con_lugar_debe");
    private String p_con_lugar_haber = utilitario.getVariable("p_con_lugar_haber");
    cls_contabilidad con = new cls_contabilidad();
    private Texto tex_num_transaccion = new Texto();
    private Boton bot_buscar_transaccion = new Boton();

    public pre_edita_comp() {
        //Recuperar el plan de cuentas activo
        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");

        if (lis_plan != null && !lis_plan.isEmpty()) {
            bar_botones.quitarBotonsNavegacion();

            tex_num_transaccion.setId("tex_num_transaccion");
            tex_num_transaccion.setSoloEnteros();
            tex_num_transaccion.setSize(15);
            bot_buscar_transaccion.setTitle("Buscar");
            bot_buscar_transaccion.setIcon("ui-icon-search");
            bot_buscar_transaccion.setMetodo("buscarTransaccion");
            bar_botones.agregarComponente(new Etiqueta("NUM. ASIENTO: "));
            bar_botones.agregarComponente(tex_num_transaccion);
            bar_botones.agregarBoton(bot_buscar_transaccion);

            MarcaAgua maa_marca = new MarcaAgua();
            maa_marca.setValue("Num. Asiento");
            maa_marca.setFor("tex_num_transaccion");
            agregarComponente(maa_marca);

            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setTabla("con_cab_comp_cont", "ide_cnccc", 1);
            tab_tabla1.setCondicionSucursal(true);
            tab_tabla1.getColumna("ide_cneco").setCombo("con_estado_compro", "ide_cneco", "nombre_cneco", "");
            tab_tabla1.getColumna("fecha_siste_cnccc").setVisible(false);
            tab_tabla1.getColumna("numero_cnccc").setEtiqueta();
            tab_tabla1.getColumna("numero_cnccc").setEstilo("font-size:11px;font-weight: bold");
            tab_tabla1.getColumna("fecha_siste_cnccc").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("fecha_trans_cnccc").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("hora_sistem_cnccc").setVisible(false);
            tab_tabla1.getColumna("hora_sistem_cnccc").setValorDefecto(utilitario.getHoraActual());
            tab_tabla1.getColumna("ide_cntcm").setVisible(false);
            tab_tabla1.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "");
            tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_tabla1.getColumna("ide_usua").setLectura(true);
            tab_tabla1.getColumna("ide_modu").setCombo("sis_modulo", "ide_modu", "nom_modu", "");
            tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "");
            tab_tabla1.getColumna("ide_geper").setAutoCompletar();
            tab_tabla1.getColumna("ide_geper").setRequerida(true);
            tab_tabla1.agregarRelacion(tab_tabla2);
            tab_tabla1.setCondicion("ide_cntcm=-1");
            tab_tabla1.setValidarInsertar(true);
            tab_tabla1.setTipoFormulario(true);
            tab_tabla1.getGrid().setColumns(6);
            tab_tabla1.getColumna("ide_cneco").setValorDefecto(utilitario.getVariable("p_con_estado_comprobante_normal"));
            tab_tabla1.getColumna("ide_cneco").setLectura(true);
            tab_tabla1.setCampoOrden("ide_cnccc desc");
            tab_tabla1.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla1);
            pat_panel1.getMenuTabla().getItem_eliminar().setRendered(false);
            pat_panel1.getMenuTabla().getItem_insertar().setRendered(false);

            tab_tabla2.setId("tab_tabla2");
            tab_tabla2.setTabla("con_det_comp_cont", "ide_cndcc", 2);
            tab_tabla2.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "ide_cncpc=" + lis_plan.get(0));
            tab_tabla2.getColumna("ide_cndpc").setAutoCompletar();
            tab_tabla2.getColumna("ide_cndpc").setRequerida(true);
            tab_tabla2.getColumna("ide_cnlap").setCombo("con_lugar_aplicac", "ide_cnlap", "nombre_cnlap", "");
            tab_tabla2.getColumna("ide_cnlap").setPermitirNullCombo(false);
            tab_tabla2.getColumna("ide_cnlap").setMetodoChange("cambioLugarAplica");
            tab_tabla2.setCampoOrden("ide_cnlap desc");
            tab_tabla2.getColumna("valor_cndcc").setMetodoChange("ingresaCantidad");
            tab_tabla2.setRows(10);
            tab_tabla2.dibujar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_tabla2);

            Grid gri_totales = new Grid();
            gri_totales.setId("gri_totales");

            gri_totales.setColumns(3);
            eti_suma_debe.setValue("TOTAL DEBE : 0");
            eti_suma_debe.setStyle("font-size: 14px;font-weight: bold");
            eti_suma_haber.setValue("TOTAL HABER : 0");
            eti_suma_haber.setStyle("font-size: 14px;font-weight: bold");
            eti_suma_diferencia.setValue("DIFERENCIA : 0");
            eti_suma_diferencia.setStyle("font-size: 14px;font-weight: bold");
            gri_totales.setWidth("100%");
            gri_totales.getChildren().add(eti_suma_diferencia);
            gri_totales.getChildren().add(eti_suma_debe);
            gri_totales.getChildren().add(eti_suma_haber);
            Division div_division = new Division();
            div_division.setId("div_division");
            Division div_detalle = new Division();
            div_detalle.setFooter(pat_panel2, gri_totales, "85%");

            div_division.dividir2(pat_panel1, div_detalle, "40%", "H");

            gru_pantalla.getChildren().add(bar_botones);
            gru_pantalla.getChildren().add(div_division);
        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }
    }

    public void buscarTransaccion() {
        if (tex_num_transaccion.getValue() != null && !tex_num_transaccion.getValue().toString().isEmpty()) {

            tab_tabla1.setCondicion("ide_cnccc=" + tex_num_transaccion.getValue());
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            if (tab_tabla1.getTotalFilas() > 0) {
                calcularTotal();
            } else {
                utilitario.agregarMensajeInfo("Atencion", "El numero de Asiento no existe");
            }
            utilitario.addUpdate("tab_tabla1,tab_tabla2,gri_totales");
        }
    }

    @Override
    public void insertar() {
        if (tab_tabla2.isFocus()) {
            tab_tabla2.insertar();
        }
    }

    @Override
    public void guardar() {
        if (validar()) {
            TablaGenerica tab_persona = utilitario.consultar("select * from gen_persona where ide_geper=" + tab_tabla1.getValor("ide_geper"));
            utilitario.getConexion().agregarSqlPantalla("update tes_cab_libr_banc set beneficiari_teclb='" + tab_persona.getValor("nom_geper") + "' where ide_cnccc=" + tab_tabla1.getValor("ide_cnccc"));
            tab_tabla1.guardar();
            tab_tabla2.guardar();
            utilitario.getConexion().guardarPantalla();
        }

    }

    public void cambioLugarAplica(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    public boolean validar() {
        if (calcularTotal()) {
            return true;
        } else {
            utilitario.agregarMensajeInfo("La suma de los detalles del DEBE tiene que ser igual al del HABER", "");
        }
        return false;
    }

    public boolean calcularTotal() {
        double dou_debe = 0;
        double dou_haber = 0;
        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {

            try {
                if (tab_tabla2.getValor(i, "ide_cnlap").equals(p_con_lugar_debe)) {
                    dou_debe += Double.parseDouble(tab_tabla2.getValor(i, "valor_cndcc"));
                } else if (tab_tabla2.getValor(i, "ide_cnlap").equals(p_con_lugar_haber)) {
                    dou_haber += Double.parseDouble(tab_tabla2.getValor(i, "valor_cndcc"));
                }
            } catch (Exception e) {
            }
        }
        eti_suma_debe.setValue("TOTAL DEBE : " + utilitario.getFormatoNumero(dou_debe));
        eti_suma_haber.setValue("TOTAL HABER : " + utilitario.getFormatoNumero(dou_haber));

        double dou_diferencia = Double.parseDouble(utilitario.getFormatoNumero(dou_debe)) - Double.parseDouble(utilitario.getFormatoNumero(dou_haber));
        eti_suma_diferencia.setValue("DIFERENCIA : " + utilitario.getFormatoNumero(dou_diferencia));
        if (dou_diferencia != 0.0) {
            eti_suma_diferencia.setStyle("font-size: 14px;font-weight: bold;color:red");
        } else {
            eti_suma_diferencia.setStyle("font-size: 14px;font-weight: bold");
            return true;
        }
        return false;
    }

    public void ingresaCantidad(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    @Override
    public void eliminar() {
        if (tab_tabla2.isFocus()) {
            tab_tabla2.eliminar();
        }
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

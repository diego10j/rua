/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_inversiones_prestamos;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Consulta;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.DateSelectEvent;
import pkg_bancos.cls_bancos;
import pkg_contabilidad.VistaAsiento;
import pkg_contabilidad.cls_cab_comp_cont;
import pkg_contabilidad.cls_contabilidad;
import pkg_contabilidad.cls_det_comp_cont;
import pkg_cuentas_x_pagar.cls_cuentas_x_pagar;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_generar_inversion extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Division div_division = new Division();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private String str_p_con_tipo_comprobante_egreso = utilitario.getVariable("p_con_tipo_comprobante_egreso");
    private String str_p_con_tipo_comprobante_ingreso = utilitario.getVariable("p_con_tipo_comprobante_ingreso");
    private String p_est_com_normal = utilitario.getVariable("p_con_estado_comprobante_normal");
    private Combo com_tipo_inversion = new Combo();
    private Etiqueta eti_tipo_inversion = new Etiqueta();
    cls_contabilidad conta = new cls_contabilidad();
    cls_cab_comp_cont cab_com_con;
    List<cls_det_comp_cont> lista_detalles = new ArrayList();
    private VistaAsiento via_asiento = new VistaAsiento();
    private Dialogo dia_tipo_inversion = new Dialogo();
    private Radio rad_tipo_inversion = new Radio();
//    private Boton bot_anular_inversion = new Boton();
    private Boton bot_generar_asiento_inversion = new Boton();
    private Boton bot_generar_asiento_inversion_interes = new Boton();
    private Dialogo dia_banco = new Dialogo();
    private Texto tex_num_cheque = new Texto();
    private Combo com_banco = new Combo();
    private Combo com_cuenta_banco = new Combo();
    private Combo com_tipo_tansaccion = new Combo();
    private Texto tex_monto_cheque = new Texto();
    private Etiqueta eti_banc = new Etiqueta();
    private Etiqueta eti_cue_banc = new Etiqueta();
    private Etiqueta eti_num_cheque = new Etiqueta();
    private Etiqueta eti_monto_cheque = new Etiqueta();
    private Etiqueta eti_fecha_cheque = new Etiqueta();
    private Etiqueta eti_tipo_transaccion = new Etiqueta();
    private Calendario cal_fecha_cheque = new Calendario();
    private String banco_actual = "-1";
    private String cuenta_bancaria_asiento = "";
    private Boton bot_ver_inversiones_vencidas = new Boton();
    private Consulta con_inversion_vencida = new Consulta();

    public pre_generar_inversion() {
        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");
        if (lis_plan != null && !lis_plan.isEmpty()) {

            bar_botones.getBot_insertar().setUpdate("tab_tabla");
            bar_botones.getBot_guardar().setUpdate("tab_tabla");
            //  bar_botones.setConfirmarGuardar("tab_tabla");
            bar_botones.getBot_eliminar().setUpdate("tab_tabla");
            bar_botones.agregarReporte();
            bar_botones.agregarComponente(eti_tipo_inversion);
            bar_botones.agregarComponente(com_tipo_inversion);
//            bar_botones.agregarBoton(bot_anular_inversion);
            bar_botones.agregarBoton(bot_generar_asiento_inversion);
            bar_botones.agregarBoton(bot_generar_asiento_inversion_interes);
            bar_botones.agregarBoton(bot_ver_inversiones_vencidas);

            eti_tipo_inversion.setId("eti_tipo_inversion");
            eti_tipo_inversion.setValue("Tipo de Inversión");
            com_tipo_inversion.setId("com_tipo_inversion");
            com_tipo_inversion.setMetodo("cargarTipoInversion");

            rep_reporte.setId("rep_reporte");
            rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
            sel_rep.setId("sel_rep");

            tab_tabla.setId("tab_tabla");
            tab_tabla.setTabla("iyp_certificado", "ide_ipcer", 1);
            tab_tabla.getColumna("ide_ipcin").setCombo("iyp_clase_inversion", "ide_ipcin", "nombre_ipcin", "");
            tab_tabla.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "ide_cntcu=" + utilitario.getVariable("p_iyp_pasivo"));
            tab_tabla.getColumna("ide_cndpc").setAutoCompletar();
            //tab_tabla.getColumna("ide_cndpc").setVisible(false);
            tab_tabla.getColumna("ide_ipein").setCombo("iyp_estado_inversion", "ide_ipein", "nombre_ipein", "");
            tab_tabla.getColumna("ide_ipein").setValorDefecto(utilitario.getVariable("p_iyp_estado_activo_inversion"));
//            tab_tabla.getColumna("con_ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "ide_cntcu=" + utilitario.getVariable("p_iyp_activo"));
//            tab_tabla.getColumna("con_ide_cndpc").setAutoCompletar();
            tab_tabla.getColumna("con_ide_cndpc").setVisible(false);
            tab_tabla.getColumna("ide_cnmod").setCombo("con_moneda", "ide_cnmod", "nombre_cnmod", "");
            tab_tabla.getColumna("ide_cnmod").setPermitirNullCombo(false);
            tab_tabla.getColumna("capital_ipcer").setMetodoChange("calcularInteres");
            tab_tabla.getColumna("tasa_ipcer").setMetodoChange("calcularInteres");
            tab_tabla.getColumna("plazo_ipcer").setMetodoChange("calcularInteres");
            tab_tabla.getColumna("interes_ipcer").setEtiqueta();
            tab_tabla.getColumna("interes_ipcer").setEstilo("font-size:13px;font-weight: bold;");
            tab_tabla.getColumna("valor_a_pagar_ipcer").setEtiqueta();
            tab_tabla.getColumna("valor_a_pagar_ipcer").setEstilo("font-size:13px;font-weight: bold;");
          //  tab_tabla.getColumna("num_certificado_ipcer").setLectura(true);
            tab_tabla.getColumna("fecha_emision_ipcer").setValorDefecto(utilitario.getFechaActual());
            tab_tabla.getColumna("fecha_emision_ipcer").setMetodoChange("obtenetFechaVencimiento");
            tab_tabla.getColumna("fecha_vence_ipcer").setLectura(true);
            tab_tabla.getColumna("ide_ipcin").setPermitirNullCombo(false);
            tab_tabla.getColumna("es_inver_banco_ipcer").setVisible(false);
            tab_tabla.setCondicion("es_inver_banco_ipcer is null");
            tab_tabla.getColumna("es_inver_banco_ipcer").setValorDefecto("true");
            tab_tabla.getColumna("ide_cnccc").setLectura(true);
            tab_tabla.getColumna("ide_cnccc_interes").setLectura(true);
            tab_tabla.getColumna("ide_cnccc_terminacion").setLectura(true);
            tab_tabla.getColumna("ide_teclb").setLectura(true);
            tab_tabla.getColumna("ide_cpdtr").setLectura(true);
            tab_tabla.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_tabla.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "");
            tab_tabla.getColumna("ide_usua").setLectura(true);
            tab_tabla.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "nivel_geper='HIJO'");
            tab_tabla.getColumna("ide_geper").setAutoCompletar();
            tab_tabla.getColumna("fecha_sistema_ipcer").setValorDefecto(utilitario.getFechaActual());
            tab_tabla.getColumna("hora_sistema_ipcer").setValorDefecto(utilitario.getHoraActual());
            tab_tabla.getColumna("fecha_sistema_ipcer").setVisible(false);
            tab_tabla.getColumna("hora_sistema_ipcer").setVisible(false);
            tab_tabla.getColumna("ide_geper").setRequerida(true);
            tab_tabla.getColumna("ide_ipein").setRequerida(true);
//            tab_tabla.getColumna("ide_cndpc").setRequerida(true);
            tab_tabla.getColumna("observacion_ipcer").setRequerida(true);
            tab_tabla.getColumna("ide_cnmod").setRequerida(true);
            tab_tabla.getColumna("ide_ipcin").setRequerida(true);
            tab_tabla.getColumna("es_renovacion_ipcer").setValorDefecto("false");
            tab_tabla.getColumna("hace_asiento_ipcer").setValorDefecto("false");
            tab_tabla.getColumna("hace_asiento_ipcer").setVisible(false);
            tab_tabla.getColumna("ide_tecba").setCombo("select tes_cuenta_banco.ide_tecba,tes_banco.nombre_teban,tes_cuenta_banco.nombre_tecba from  tes_banco,tes_cuenta_banco,sis_empresa where tes_banco.ide_teban=tes_cuenta_banco.ide_teban and sis_empresa.ide_empr=" + utilitario.getVariable("ide_empr") + " and tes_cuenta_banco.ide_sucu=" + utilitario.getVariable("ide_sucu"));

            List lista = new ArrayList();
            Object fila1[] = {
                "true", "BANCOS"
            };
            Object fila2[] = {
                "false", "SALESIANOS"
            };
            lista.add(fila1);
            lista.add(fila2);
            com_tipo_inversion.setCombo(lista);
            tab_tabla.setTipoFormulario(true);
            tab_tabla.getGrid().setColumns(4);
            //tab_tabla.setRecuperarLectura(true);
            tab_tabla.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla);

            div_division.setId("div_division");
            div_division.dividir1(pat_panel);

            via_asiento.setId("via_asiento");
            via_asiento.getBot_aceptar().setMetodo("aceptarVistaAsiento");
            via_asiento.getBot_cancelar().setMetodo("cancelarDialogo");
            gru_pantalla.getChildren().add(via_asiento);

            rad_tipo_inversion.setId("rad_tipo_inversion");
            dia_tipo_inversion.setId("dia_tipo_inversion");
            dia_tipo_inversion.setTitle("T I P O   I N V E R S I O N");
            dia_tipo_inversion.setWidth("25%");
            dia_tipo_inversion.setHeight("20%");
            dia_tipo_inversion.getBot_aceptar().setMetodo("aceptarReporte");
            Grid grid_tipo_inversion = new Grid();
            grid_tipo_inversion.setColumns(2);
            grid_tipo_inversion.getChildren().add(new Etiqueta("Escoja Opcion"));
            grid_tipo_inversion.getChildren().add(rad_tipo_inversion);
            grid_tipo_inversion.setStyle("width:" + (dia_tipo_inversion.getAnchoPanel() - 5) + "px;height:" + dia_tipo_inversion.getAltoPanel() + "px;overflow: auto;display: block;");
            List lista1 = new ArrayList();
            Object fila11[] = {
                "true", "BANCOS"
            };
            Object fila22[] = {
                "false", "SALESIANOS"
            };
            lista1.add(fila11);
            lista1.add(fila22);
            rad_tipo_inversion.setRadio(lista1);
            dia_tipo_inversion.setDialogo(grid_tipo_inversion);

//            bot_anular_inversion.setId("bot_anular_inversion");
//            bot_anular_inversion.setValue("ANULAR INVERSION");
//            bot_anular_inversion.setMetodo("anularInversion");


            tex_num_cheque.setId("tex_num_cheque");
            com_banco.setCombo("select ide_teban,nombre_teban from tes_banco where ide_empr=" + utilitario.getVariable("ide_empr"));
            com_banco.setMetodo("cargar_cuentas");
            com_cuenta_banco.setId("com_cuenta_banco");
            com_cuenta_banco.setCombo(new ArrayList());
            com_cuenta_banco.setMetodo("cargar_num_cheque");
            com_tipo_tansaccion.setId("com_tipo_tansaccion");
            com_tipo_tansaccion.setCombo("SELECT ide_tettb,nombre_tettb FROM tes_tip_tran_banc WHERE signo_tettb>0 AND ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_tettb  in(" + utilitario.getVariable("p_tes_tran_deposito_caja") + "," + utilitario.getVariable("p_tes_tran_deposito") + "," + utilitario.getVariable("p_tes_tran_transferencia_mas") + ")");
            com_tipo_tansaccion.setMetodo("cargar_num_cheque");


            eti_banc.setValue("Banco: ");
            eti_cue_banc.setValue("Cuenta Banco: ");
            eti_num_cheque.setValue("Numero Cheque: ");
            eti_monto_cheque.setValue("Monto Cheque: ");
            eti_fecha_cheque.setValue("Fecha: ");
            eti_tipo_transaccion.setValue("Tipo Transaccion");
            Grid grid_bancos = new Grid();
            grid_bancos.setColumns(2);
            grid_bancos.getChildren().add(eti_tipo_transaccion);
            grid_bancos.getChildren().add(com_tipo_tansaccion);
            grid_bancos.getChildren().add(eti_banc);
            grid_bancos.getChildren().add(com_banco);
            grid_bancos.getChildren().add(eti_cue_banc);
            grid_bancos.getChildren().add(com_cuenta_banco);
            grid_bancos.getChildren().add(eti_num_cheque);
            grid_bancos.getChildren().add(tex_num_cheque);
            grid_bancos.getChildren().add(eti_monto_cheque);
            grid_bancos.getChildren().add(tex_monto_cheque);
            grid_bancos.getChildren().add(eti_fecha_cheque);
            grid_bancos.getChildren().add(cal_fecha_cheque);

            dia_banco.setId("dia_banco");
            dia_banco.setTitle("Forma de Pago");
            dia_banco.setWidth("30%");
            dia_banco.setHeight("40%");
            dia_banco.setDialogo(grid_bancos);
            dia_banco.setDynamic(false);
            grid_bancos.setStyle("width:" + (dia_banco.getAnchoPanel() - 5) + "px;height:" + dia_banco.getAltoPanel() + "px;overflow: auto;display: block;");
            dia_banco.getBot_aceptar().setMetodo("generarAsiento");
            //dia_banco.getBot_aceptar().setMetodo("generarInversion");

            bot_generar_asiento_inversion.setId("bot_generar_asiento_inversion");
            bot_generar_asiento_inversion.setValue("GENERAR ASIENTO");
            bot_generar_asiento_inversion.setMetodo("generarAsientoInicial");

            bot_generar_asiento_inversion_interes.setId("bot_generar_asiento_inversion_interes");
            bot_generar_asiento_inversion_interes.setValue("GENERAR ASIENTO INTERES");
            bot_generar_asiento_inversion_interes.setMetodo("generarAsientoInteres");

            bot_ver_inversiones_vencidas.setId("bot_ver_inversiones_vencidas");
            bot_ver_inversiones_vencidas.setValue("VER INVERSIONES VENCIDAS");
            bot_ver_inversiones_vencidas.setMetodo("dibujarDialogoVencidas");

            con_inversion_vencida.setId("con_inversion_vencida");
            con_inversion_vencida.setTitle("Inversiones Vencidas");
            con_inversion_vencida.setHeight("50%");
            con_inversion_vencida.setWidth("50%");
            con_inversion_vencida.setConsulta("SELECT ide_ipcer as ide_certificado,num_certificado_ipcer,fecha_vence_ipcer,capital_ipcer,interes_ipcer,valor_a_pagar_ipcer,es_inver_banco_ipcer FROM iyp_certificado   "
                    + "WHERE ide_sucu=" + utilitario.getVariable("ide_sucu") + "  "
                    + "AND ide_empr=" + utilitario.getVariable("ide_empr") + "  "
                    + "AND fecha_vence_ipcer<='" + utilitario.getFechaActual() + "'  "
                    + "AND es_renovacion_ipcer=FALSE  "
                    + "AND ide_ipein=" + utilitario.getVariable("p_iyp_estado_activo_inversion") + "  "
                    + "ORDER BY es_inver_banco_ipcer,ide_ipcer", "ide_certificado");
            con_inversion_vencida.getBot_aceptar().setRendered(false);
            con_inversion_vencida.getTab_consulta_dialogo().getColumna("ide_certificado").setNombreVisual("Codigo");
            con_inversion_vencida.getTab_consulta_dialogo().getColumna("num_certificado_ipcer").setNombreVisual("Certificado");
            con_inversion_vencida.getTab_consulta_dialogo().getColumna("fecha_vence_ipcer").setNombreVisual("Vence");
            con_inversion_vencida.getTab_consulta_dialogo().getColumna("capital_ipcer").setNombreVisual("Capital");
            con_inversion_vencida.getTab_consulta_dialogo().getColumna("interes_ipcer").setNombreVisual("Interes");
            con_inversion_vencida.getTab_consulta_dialogo().getColumna("valor_a_pagar_ipcer").setNombreVisual("Valor a Pagar");
            con_inversion_vencida.getTab_consulta_dialogo().getColumna("es_inver_banco_ipcer").setNombreVisual("Tipo");

            agregarComponente(dia_banco);
            agregarComponente(dia_tipo_inversion);
            agregarComponente(div_division);
            agregarComponente(rep_reporte);
            agregarComponente(sel_rep);
            agregarComponente(con_inversion_vencida);
            dibujarDialogoVencidas();

////            //llenarsecuencial();
        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }
    }

    public void dibujarDialogoVencidas() {
        con_inversion_vencida.dibujar();
    }

    public void cargar_num_cheque() {
//        if (com_tipo_tansaccion.getValue().equals(utilitario.getVariable("p_tes_tran_cheque"))) {//combo es cheque
        cls_bancos banco = new cls_bancos();
        tex_num_cheque.setValue(banco.obtenerNumMaximoTran(com_cuenta_banco.getValue() + "", com_tipo_tansaccion.getValue() + ""));
//        } else {
//            tex_num_cheque.setValue("");
//        }
        utilitario.addUpdate("tex_num_cheque");
    }

    public void cargar_cuentas() {
        if (com_banco.getValue() != null) {
            banco_actual = com_banco.getValue().toString();
            com_cuenta_banco.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + banco_actual);
            tex_num_cheque.setValue("");
        } else {
            com_cuenta_banco.setValue("");
            tex_num_cheque.setValue("");
        }
        utilitario.addUpdate("com_cuenta_banco,tex_num_cheque");
    }

    public boolean validarDiaGenerarAsiento() {  /// hacer el dialogo para que valide cuando escoje  el combo del banco  
        if (com_tipo_tansaccion.getValue() == null) {
            utilitario.agregarMensajeInfo("No se puede realizar la terminación de la inversion", "Debe seleccionar un tipo de transacción");
            return false;
        }
        if (com_banco.getValue() == null) {
            utilitario.agregarMensajeInfo("No se puede realizar la terminación de la inversion", "Debe seleccionar un banco");
            return false;
        }
        if (com_cuenta_banco.getValue() == null) {
            utilitario.agregarMensajeInfo("No se puede realizar la terminación de la inversion", "Debe seleccionar un numero de cuenta bancaria");
            return false;
        }
        if (tex_num_cheque.getValue() == null || tex_num_cheque.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede realizar la terminación de la inversion", "Debe ingresar un numero de cheque");
            return false;
        }
        if (tex_monto_cheque.getValue() == null || tex_monto_cheque.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede realizar la terminación de la inversion", "Debe ingresar un monto");
            return false;
        }
        if (cal_fecha_cheque.getValue() == null || cal_fecha_cheque.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede realizar la terminación de la inversion", "Debe seleccionar un calendario");
            return false;
        }
        return true;
    }

    public void generarInversion() {
        if (com_tipo_inversion.getValue() != null) {

            if (tab_tabla.getTotalFilas() > 0) {
                TablaGenerica tab_terminar_inversion = utilitario.consultar("SELECT * FROM iyp_certificado WHERE ide_ipcer=" + tab_tabla.getValor("ide_ipcer") + "");
                if (dia_banco.isVisible()) {
                    if (validarDiaGenerarAsiento()) {
                        dia_banco.cerrar();
                        System.out.println("si ingresa al terminar inversion");
                        if (tab_terminar_inversion.getTotalFilas() > 0) {
                            ///////////////generarAsientoTerminoInversion(tab_terminar_inversion);
                        }
                    }
                } else {
                    tex_monto_cheque.setValue(tab_terminar_inversion.getValor("valor_a_pagar_ipcer"));
                    dia_banco.dibujar();
                }
            } else {
                utilitario.agregarMensajeInfo("No se puede terminar la inversión", "Debe tener datos en la tabla");
            }
        } else {
            utilitario.agregarMensajeInfo("No se puede terminar la inversión", "Debe seleccionar un tipo de inversión");
        }
    }

    public void generarAsientoInicial() {
        if (com_tipo_inversion.getValue() != null) {
            if (tab_tabla.getTotalFilas() > 0) {
                if (tab_tabla.getValor("ide_cnccc") == null || tab_tabla.getValor("ide_cnccc").isEmpty()) {
                    if (tab_tabla.getValor("ide_cndpc") != null && !tab_tabla.getValor("ide_cndpc").isEmpty()) {
                        if (tab_tabla.getValor("ide_tecba") != null && !tab_tabla.getValor("ide_tecba").isEmpty()) {
                            System.out.println("ide_tecba " + tab_tabla.getValor("ide_tecba"));
                            TablaGenerica tab_cuenta_banco = utilitario.consultar("SELECT * FROM tes_banco where ide_teban in ( "
                                    + "SELECT ide_teban FROM tes_cuenta_banco WHERE ide_tecba=" + tab_tabla.getValor("ide_tecba") + ")");
                            if (tab_cuenta_banco.getTotalFilas() > 0) {
                                com_banco.setValue(tab_cuenta_banco.getValor(0, "ide_teban"));
                                com_cuenta_banco.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + tab_cuenta_banco.getValor(0, "ide_teban"));
                                com_cuenta_banco.setValue(tab_tabla.getValor("ide_tecba"));
                                com_cuenta_banco.setDisabled(true);
                                com_banco.setDisabled(true);
                                tex_monto_cheque.setValue(tab_tabla.getValor("capital_ipcer"));
                                tex_monto_cheque.setDisabled(true);
                                if (tab_cuenta_banco.getValor(0, "es_caja_teban").equals("true")) {
                                    com_tipo_tansaccion.setCombo("SELECT ide_tettb,nombre_tettb FROM tes_tip_tran_banc WHERE signo_tettb>0 AND ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_tettb  in(" + utilitario.getVariable("p_tes_tran_deposito_caja") + ")");
                                } else {
                                    com_tipo_tansaccion.setCombo("SELECT ide_tettb,nombre_tettb FROM tes_tip_tran_banc WHERE signo_tettb>0 AND ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_tettb  in(" + utilitario.getVariable("p_tes_tran_deposito") + "," + utilitario.getVariable("p_tes_tran_transferencia_mas") + ")");
                                }
                            }
                            dia_banco.dibujar();
                        } else {
                            /////rex..................................                            
                            System.out.println("ingresa sin datos del banco");
                            generarAsiento();
                        }
                    } else {
                        utilitario.agregarMensajeInfo("No puede generar el asiento", "Debe escoger la cuenta del pasivo");
                    }

                } else {
                    utilitario.agregarMensajeInfo("No puede generar el asiento", "La tabla ya tiene generado el asiento inicial");
                }
            } else {
                utilitario.agregarMensajeInfo("No puede generar el asiento", "La tabla no posee registros");
            }
        } else {
            utilitario.agregarMensajeInfo("No puede generar el asiento", "Debe seleccionar un tipo de Inversión");
        }


    }

    public void generarAsiento() {
        if (com_tipo_inversion.getValue() != null) {
            if (tab_tabla.getValor("ide_ipein").equals(utilitario.getVariable("p_iyp_estado_activo_inversion"))) {
                if (com_tipo_inversion.getValue().equals("true")) {
                    System.out.println("Si ingresa al gereara asiento inversion ,,, BANCOS");
                } else {
                    if (tab_tabla.getTotalFilas() > 0) {
//                        if (tab_tabla.getValor("ide_tecba") != null && !tab_tabla.getValor("ide_tecba").isEmpty()) {
                        if (tab_tabla.getValor("ide_cnccc") != null && !tab_tabla.getValor("ide_cnccc").isEmpty()) {
                            utilitario.agregarMensajeInfo("No puede generar el asiento", "Ya tiene realizado el Asiento");
                        } else {
                            System.out.println("Si ingresa al gereara asiento inversion salesianos");
                            generarAsientoInversion();
                        }
//                        } else {
//                            utilitario.agregarMensajeInfo("No puede generar el asiento", "Debe seleccionar una cuenta Bancaria");
//                        }
                    } else {
                        utilitario.agregarMensajeInfo("No puede generar el asiento", "La tabla no posee registros");
                    }
                }
            } else {
                utilitario.agregarMensajeInfo("No puede generar el asiento", "El periodo debe de estar activo");
            }
        } else {
            utilitario.agregarMensajeInfo("No puede generar el asiento", "Debe seleccionar un tipo de inversión");
        }
    }

    public void generarAsientoInteres() {
        if (com_tipo_inversion.getValue() != null) {
            if (com_tipo_inversion.getValue().equals("true")) {
                System.out.println("Si ingresa al gereara asiento inversion ,,, BANCOS");
            } else {
                if (tab_tabla.getTotalFilas() > 0) {
//////                    if (tab_tabla.getValor("ide_tecba") != null && !tab_tabla.getValor("ide_tecba").isEmpty()) {
                    if (tab_tabla.getValor("ide_cnccc_interes") == null || tab_tabla.getValor("ide_cnccc_interes").isEmpty()) {
                        System.out.println("Si ingresa al gereara asiento inversion salesianos");
                        generarAsientoInversionInteres();
                    } else {
                        utilitario.agregarMensajeInfo("No puede generar el asiento", "Ya tiene realizado el Asiento");
                    }
//////                    } else {
//////                        utilitario.agregarMensajeInfo("No puede generar el asiento", "Debe seleccionar una cuenta Bancaria");
//////                    }
                } else {
                    utilitario.agregarMensajeInfo("No puede generar el asiento", "La tabla no posee registros");
                }
            }
        } else {
            utilitario.agregarMensajeInfo("No puede generar el asiento", "Debe seleccionar un tipo de inversión");
        }
    }

//    public void anularInversion() {
//        System.out.println("si ingresa al anular inversion");
//        if (com_tipo_inversion.getValue() != null) {
//            try {
//                if (tab_tabla.getValor("ide_ipein").equals(utilitario.getVariable("p_iyp_estado_activo_inversion"))) {
//                    cls_contabilidad conta = new cls_contabilidad();
//                    cls_bancos banco = new cls_bancos();
//                    cls_cuentas_x_pagar cxp = new cls_cuentas_x_pagar();
//                    if (tab_tabla.getValor("ide_cnccc") != null && !tab_tabla.getValor("ide_cnccc").isEmpty()) {
//                        utilitario.getConexion().agregarSqlPantalla("UPDATE iyp_certificado SET ide_ipein=" + utilitario.getVariable("p_iyp_estado_anulada") + " WHERE ide_ipcer=" + tab_tabla.getValor("ide_ipcer"));
//                        conta.anular(tab_tabla.getValor("ide_cnccc"));
//                        Tabla tab_banco = utilitario.consultar("SELECT * FROM iyp_certificado WHERE ide_ipcer=" + tab_tabla.getValor("ide_ipcer"));
//                        banco.reversar(tab_banco.getValor("ide_teclb"), tab_banco.getValor("observacion_ipcer"));
//                        cxp.reversar(tab_tabla.getValor("ide_cnccc"), null, tab_tabla.getValor("observacion_ipcer"), tab_tabla.getValor("ide_cpdtr"));
//                    } else {
//                        System.out.println("ingresafsdfsd");
//                    }
//                }
//            } catch (Exception e) {
//                utilitario.agregarMensajeInfo("No se puede anular la inversión", "La inversion no de encuentra activa");
//            }
//            utilitario.getConexion().guardarPantalla();
//            tab_tabla.ejecutarSql();
//        } else {
//            utilitario.agregarMensajeInfo("No se puede anular la inversión", "Debe seleccionar un tipo de inversión");
//        }
//    }
    public void cargarTipoInversion() {
        System.out.println("Si entra al cargar tipo inversion");
        if (com_tipo_inversion.getValue() != null) {
            System.out.println("valor del combo............ " + com_tipo_inversion.getValue());
            tab_tabla.setCondicion("es_inver_banco_ipcer=" + com_tipo_inversion.getValue());
            tab_tabla.getColumna("es_inver_banco_ipcer").setValorDefecto(com_tipo_inversion.getValue() + "");
            if (!bot_generar_asiento_inversion.isDisabled()) {

                String str = utilitario.getConexion().ejecutarSql("update sis_campo set visible_camp=TRUE where ide_tabl in ( "
                        + "SELECT ide_tabl from sis_tabla tabla "
                        + "left join sis_opcion opcion on opcion.ide_opci=tabla.ide_opci "
                        + "where tabla.tabla_tabl like 'iyp_certificado' "
                        + "and lower(opcion.nom_opci) like 'generar inversion' "
                        + "group by tabla.ide_tabl,opcion.nom_opci,opcion.ide_opci "
                        + "HAVING tabla.ide_opci is not NULL "
                        + ")and lower(nom_camp) like 'ide_tecba'");
//                if (str.isEmpty()) {
//                    utilitario.getConexion().commit();
//                }
                tab_tabla.getColumna("ide_tecba").setVisible(true);
            } else {
                String str = utilitario.getConexion().ejecutarSql("update sis_campo set visible_camp=FALSE where ide_tabl in ( "
                        + "SELECT ide_tabl from sis_tabla tabla "
                        + "left join sis_opcion opcion on opcion.ide_opci=tabla.ide_opci "
                        + "where tabla.tabla_tabl like 'iyp_certificado' "
                        + "and lower(opcion.nom_opci) like 'generar inversion' "
                        + "group by tabla.ide_tabl,opcion.nom_opci,opcion.ide_opci "
                        + "HAVING tabla.ide_opci is not NULL "
                        + ")and lower(nom_camp) like 'ide_tecba'");
//                if (str.isEmpty()) {
//                    utilitario.getConexion().commit();
//                }
                tab_tabla.getColumna("ide_tecba").setVisible(false);
            }
            tab_tabla.ejecutarSql();
            tab_tabla.dibujar();
            utilitario.addUpdate("tab_tabla");
        } else {
            tab_tabla.limpiar();
        }
    }

    public void insertar() {
        if (via_asiento.getTab_det_comp_cont_vasiento().isFocus()) {
            via_asiento.insertar();
        } else if (com_tipo_inversion.getValue() != null) {
            if (tab_tabla.isFocus()) {
                tab_tabla.insertar();
            }
        } else {
            utilitario.agregarMensajeInfo("No se puede Insertar", "Debe seleccionar un tipo de Inversión");
        }
    }

    public boolean validar() {
//        if (tab_tabla.getValor("ide_cndpc") == null || tab_tabla.getValor("ide_cndpc").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "debe seleccionar la la cuenta del pasivo");
//            return false;
//        }
        if (tab_tabla.getValor("capital_ipcer") == null || tab_tabla.getValor("capital_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "debe ingresar el capital");
            return false;
        }
        if (tab_tabla.getValor("plazo_ipcer") == null || tab_tabla.getValor("plazo_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "debe ingresar el plazo");
            return false;
        }

        if (tab_tabla.getValor("tasa_ipcer") == null || tab_tabla.getValor("tasa_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "debe ingresar la tasa de interes");
            return false;
        }

        if (tab_tabla.getValor("fecha_emision_ipcer") == null || tab_tabla.getValor("fecha_emision_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la fecha de emision");
            return false;
        }
        if (tab_tabla.getValor("observacion_ipcer") == null || tab_tabla.getValor("observacion_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la observación");
            return false;
        }
        if (tab_tabla.getValor("ide_geper") == null || tab_tabla.getValor("ide_geper").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar el Beneficiario");
            return false;
        }
        if (tab_tabla.getValor("ide_cnmod") == null || tab_tabla.getValor("ide_cnmod").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la Moneda");
            return false;
        }
        if (tab_tabla.getValor("ide_ipein") == null || tab_tabla.getValor("ide_ipein").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la el estado");
            return false;
        }
        if (tab_tabla.getValor("ide_ipcin") == null || tab_tabla.getValor("ide_ipcin").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la Clase de Inversión");
            return false;
        }
        return true;
    }

    public String getNumMaxTransaccion() {
        TablaGenerica tab_cer = utilitario.consultar("select max (ide_ipcer) as maximo from iyp_certificado where ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + "");
        if (tab_cer.getTotalFilas() > 0) {
            try {
                int numMax = Integer.parseInt(tab_cer.getValor(0, "maximo"));
                return (numMax + 1) + "";
            } catch (Exception e) {
                return null;
            }

        } else {
            return "1";
        }
    }

    @Override
    public void guardar() {
        if (tab_tabla.isFilaInsertada()) {
            if (tab_tabla.getValor("hace_asiento_ipcer").equals("true")) {
                if (validar()) {
                    if (tab_tabla.getValor("cod_captacion_ipcer") != null && !tab_tabla.getValor("cod_captacion_ipcer").isEmpty()) {
                        if (tab_tabla.getValor("con_ide_cndpc") != null && !tab_tabla.getValor("con_ide_cndpc").isEmpty()) {
                            generarAsientoInversion();
                        } else {
                            utilitario.agregarMensajeError("No se puede guardar", "Ingrese una cuenta del banco");
                        }
                    } else {
                        utilitario.agregarMensajeError("No se puede guardar", "Ingrese un codigo de captación");
                    }
                }
            } else {
                if (validar()) {
                    tab_tabla.setValor("num_certificado_ipcer", secuenciaCertificado1());
                    tab_tabla.guardar();
                    utilitario.getConexion().guardarPantalla();
                }
            }
        } else {
            if (validar()) {
                tab_tabla.setValor("num_certificado_ipcer", secuenciaCertificado1());
                tab_tabla.guardar();
                utilitario.getConexion().guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        if (via_asiento.getTab_det_comp_cont_vasiento().isFocus()) {
            via_asiento.eliminar();
        } else {
            utilitario.getTablaisFocus().eliminar();
        }
    }

    public void calcularInteres(AjaxBehaviorEvent evt) {
        tab_tabla.modificar(evt);
        calculainteres();
    }

    public void calculainteres() {
        double capital = 0;
        double tasa = 0;
        double plazo = 0;
        double interes = 0;
        double valortotal = 0;
        if (tab_tabla.getValor(tab_tabla.getFilaActual(), "capital_ipcer") != null && !tab_tabla.getValor(tab_tabla.getFilaActual(), "capital_ipcer").isEmpty()) {
            try {
                capital = Double.parseDouble(tab_tabla.getValor(tab_tabla.getFilaActual(), "capital_ipcer"));
            } catch (Exception e) {
                capital = 0;
            }
        }
        if (tab_tabla.getValor(tab_tabla.getFilaActual(), "tasa_ipcer") != null && !tab_tabla.getValor(tab_tabla.getFilaActual(), "tasa_ipcer").isEmpty()) {
            try {
                tasa = Double.parseDouble(tab_tabla.getValor(tab_tabla.getFilaActual(), "tasa_ipcer"));

            } catch (Exception e) {
                tasa = 0;
            }
        }
        if (tab_tabla.getValor(tab_tabla.getFilaActual(), "plazo_ipcer") != null && !tab_tabla.getValor(tab_tabla.getFilaActual(), "plazo_ipcer").isEmpty()) {
            plazo = Double.parseDouble(tab_tabla.getValor(tab_tabla.getFilaActual(), "plazo_ipcer"));
            obtenetFechaVencimiento();
        } else {
            plazo = 0;
            obtenetFechaVencimiento();
        }
        interes = (capital * tasa * plazo) / 36000;
        valortotal = capital + interes;
        tab_tabla.setValor(tab_tabla.getFilaActual(), "interes_ipcer", utilitario.getFormatoNumero(interes, 2));
        tab_tabla.setValor(tab_tabla.getFilaActual(), "valor_a_pagar_ipcer", utilitario.getFormatoNumero(valortotal, 2));
        utilitario.addUpdateTabla(tab_tabla, "interes_ipcer,valor_a_pagar_ipcer", "");
    }

    public boolean obtenetFechaVencimiento() {
        if (tab_tabla.getValor("plazo_ipcer") != null && !tab_tabla.getValor("plazo_ipcer").isEmpty()) {
            System.out.println("SI ENTRA..." + utilitario.sumarDiasFecha(utilitario.getFecha(tab_tabla.getValor("fecha_emision_ipcer")), Integer.parseInt(tab_tabla.getValor("plazo_ipcer"))));
            System.out.println("SI ENTRA..." + utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_tabla.getValor("fecha_emision_ipcer")), Integer.parseInt(tab_tabla.getValor("plazo_ipcer")))));
            tab_tabla.setValor(tab_tabla.getFilaActual(), "fecha_vence_ipcer", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_tabla.getValor("fecha_emision_ipcer")), Integer.parseInt(tab_tabla.getValor("plazo_ipcer")))));
            utilitario.addUpdateTabla(tab_tabla, "fecha_vence_ipcer", "");
            return true;
        } else {
            tab_tabla.setValor(tab_tabla.getFilaActual(), "fecha_vence_ipcer", "");
            utilitario.addUpdateTabla(tab_tabla, "fecha_vence_ipcer", "");
            return false;
        }
    }

    public void obtenetFechaVencimiento(DateSelectEvent evt) {
//        tab_renovacion_inversion.modificar(evt);
        if (obtenetFechaVencimiento()) {
            calculainteres();
        }
    }

    public void secuenciaCertificado() {
        tab_tabla.setValor("num_certificado_ipcer", secuenciaCertificado1());
        utilitario.addUpdate("tab_tabla");
    }

    public String secuenciaCertificado1() {
        List list = utilitario.getConexion().consultar("SELECT max(num_certificado_ipcer)  FROM iyp_certificado WHERE es_inver_banco_ipcer=" + tab_tabla.getValor("es_inver_banco_ipcer") + " AND ide_empr=" + utilitario.getVariable("ide_empr") + " AND ide_sucu=" + utilitario.getVariable("ide_sucu") + "");
        String str_maximo = "0";
        if (list != null && !list.isEmpty()) {
            if (list.get(0) != null) {
                if (!list.get(0).toString().isEmpty()) {
                    str_maximo = list.get(0) + "";
                }
            }
        }
        String num_max = String.valueOf(Integer.parseInt(str_maximo) + 1);
        String ceros = utilitario.generarCero(10 - num_max.length());
        str_maximo = ceros.concat(num_max);
        return str_maximo;
    }

    public void generarAsientoInversion() {
        conta.limpiar();
        if (com_tipo_inversion.getValue().toString().equalsIgnoreCase("true")) {
            System.out.println("INGRESO SI INGRESA AL TIPO DE INVERSION BANCOS........... " + com_tipo_inversion.getValue().toString().equalsIgnoreCase("true"));
            cab_com_con = new cls_cab_comp_cont(str_p_con_tipo_comprobante_ingreso, p_est_com_normal, "9", tab_tabla.getValor("ide_geper"), tab_tabla.getValor("fecha_emision_ipcer"), tab_tabla.getValor("observacion_ipcer"));
            lista_detalles.clear();
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), tab_tabla.getValor("con_ide_cndpc"), Double.parseDouble(tab_tabla.getValor("capital_ipcer")), ""));
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), tab_tabla.getValor("ide_cndpc"), Double.parseDouble(tab_tabla.getValor("capital_ipcer")), ""));
            cab_com_con.setDetalles(lista_detalles);
            via_asiento.setVistaAsiento(cab_com_con);
            via_asiento.dibujar();
            utilitario.addUpdate("via_asiento");
        } else {
            System.out.println("INGRESO SI INGRESA INVERSION SALESIANOS................ " + com_tipo_inversion.getValue().toString().equalsIgnoreCase("true"));
            cab_com_con = new cls_cab_comp_cont(str_p_con_tipo_comprobante_egreso, p_est_com_normal, "9", tab_tabla.getValor("ide_geper"), tab_tabla.getValor("fecha_emision_ipcer"), tab_tabla.getValor("observacion_ipcer"));
            lista_detalles.clear();
            String str_ide_cndpc_cxp = conta.buscarCuentaPersona("CUENTA POR PAGAR", tab_tabla.getValor("ide_geper"));
            if (str_ide_cndpc_cxp != null && !str_ide_cndpc_cxp.isEmpty()) {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), str_ide_cndpc_cxp, Double.parseDouble(tab_tabla.getValor("capital_ipcer")), ""));
            } else {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), tab_tabla.getValor("ide_cndpc"), Double.parseDouble(tab_tabla.getValor("capital_ipcer")), ""));
            }
            cls_bancos banco = new cls_bancos();
            String str_ide_cndpc_banco = banco.obtenerParametroCuentaBanco("ide_cndpc", tab_tabla.getValor("ide_tecba"));
            if (str_ide_cndpc_banco != null && !str_ide_cndpc_banco.isEmpty()) {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), str_ide_cndpc_banco, Double.parseDouble(tab_tabla.getValor("capital_ipcer")), ""));
            } else {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), "", Double.parseDouble(tab_tabla.getValor("capital_ipcer")), ""));
            }
            cab_com_con.setDetalles(lista_detalles);
            via_asiento.setVistaAsiento(cab_com_con);
            via_asiento.dibujar();
            dia_banco.cerrar();
            utilitario.addUpdate("via_asiento");
        }
    }
    boolean boo_hizo_asiento_interes = false;

    public void generarAsientoInversionInteres() {
        conta.limpiar();
        boo_hizo_asiento_interes = true;
        if (com_tipo_inversion.getValue().toString().equalsIgnoreCase("true")) {
            System.out.println("INGRESO SI INGRESA AL TIPO DE INVERSION BANCOS........... " + com_tipo_inversion.getValue().toString().equalsIgnoreCase("true"));
            cab_com_con = new cls_cab_comp_cont(str_p_con_tipo_comprobante_ingreso, p_est_com_normal, "9", tab_tabla.getValor("ide_geper"), tab_tabla.getValor("fecha_emision_ipcer"), tab_tabla.getValor("observacion_ipcer"));
            lista_detalles.clear();
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), tab_tabla.getValor("con_ide_cndpc"), Double.parseDouble(tab_tabla.getValor("capital_ipcer")), ""));
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), tab_tabla.getValor("ide_cndpc"), Double.parseDouble(tab_tabla.getValor("capital_ipcer")), ""));
            cab_com_con.setDetalles(lista_detalles);
            via_asiento.setVistaAsiento(cab_com_con);
            via_asiento.dibujar();
            utilitario.addUpdate("via_asiento");
        } else {
            System.out.println("INGRESO SI INGRESA INVERSION SALESIANOS................ " + com_tipo_inversion.getValue().toString().equalsIgnoreCase("true"));
            cab_com_con = new cls_cab_comp_cont(str_p_con_tipo_comprobante_egreso, p_est_com_normal, "9", tab_tabla.getValor("ide_geper"), tab_tabla.getValor("fecha_emision_ipcer"), tab_tabla.getValor("observacion_ipcer"));
            lista_detalles.clear();
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), utilitario.getVariable("p_iyp_cuenta_interes_por_pagar_cyo_inversiones"), Double.parseDouble(tab_tabla.getValor("interes_ipcer")), ""));
            cls_bancos banco = new cls_bancos();
            String str_ide_cndpc_banco = banco.obtenerParametroCuentaBanco("ide_cndpc", tab_tabla.getValor("ide_tecba"));
            if (tab_tabla.getValor("ide_tecba") != null && !tab_tabla.getValor("ide_tecba").isEmpty()) {
                if (str_ide_cndpc_banco != null && !str_ide_cndpc_banco.isEmpty()) {
////////                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), str_ide_cndpc_banco, Double.parseDouble(tab_tabla.getValor("interes_ipcer")), ""));
                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), utilitario.getVariable("p_iyp_cuenta_gasto_casas_obras_inv_interes"), Double.parseDouble(tab_tabla.getValor("interes_ipcer")), ""));
                } else {
                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), "", Double.parseDouble(tab_tabla.getValor("interes_ipcer")), ""));
                }
            } else {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), utilitario.getVariable("p_iyp_cuenta_gasto_casas_obras_inv_interes"), Double.parseDouble(tab_tabla.getValor("interes_ipcer")), ""));
            }
            cab_com_con.setDetalles(lista_detalles);
            via_asiento.setVistaAsiento(cab_com_con);
            via_asiento.dibujar();
            utilitario.addUpdate("via_asiento");
        }
    }

    public void aceptarVistaAsiento() {
        if (via_asiento.validarComprobante()) {
            cab_com_con.setObservacion_cnccc(via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"));
            cab_com_con.setIde_geper(via_asiento.getTab_cab_comp_cont_vasiento().getValor("ide_geper"));
            cab_com_con.setFecha_trans_cnccc(via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"));
            lista_detalles.clear();
            for (int i = 0; i < via_asiento.getTab_det_comp_cont_vasiento().getTotalFilas(); i++) {
                lista_detalles.add(new cls_det_comp_cont(via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "ide_cnlap"), via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "ide_cndpc"), Double.parseDouble(via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "valor_cndcc")), via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "observacion_cndcc")));
            }
            cab_com_con.setDetalles(lista_detalles);
            conta.generarAsientoContable(cab_com_con);
            String ide_cnccc = conta.getTab_cabecera().getValor("ide_cnccc");
            if (ide_cnccc != null) {
                via_asiento.cerrar();
                utilitario.addUpdate("via_asiento");
                tab_tabla.setValor("ide_cnccc", ide_cnccc);
                tab_tabla.setValor("num_certificado_ipcer", secuenciaCertificado1());
                cls_bancos bancos = new cls_bancos();
                cls_cuentas_x_pagar cxp = new cls_cuentas_x_pagar();
                if (com_tipo_inversion.getValue().equals("true")) {
                    bancos.generarLibroBanco(tab_tabla.getValor("ide_geper"), tab_tabla.getValor("fecha_emision_ipcer"), utilitario.getVariable("p_tes_tran_transferencia_menos"), tab_tabla.getValor("ide_tecba"), ide_cnccc, Double.parseDouble(tab_tabla.getValor("capital_ipcer")), tab_tabla.getValor("observacion_ipcer"), tex_num_cheque.getValue().toString());
                    tab_tabla.setValor("ide_teclb", bancos.getTab_cab_libro_banco().getValor("ide_teclb"));
                    System.out.println("valor de libro banco true " + bancos.getTab_cab_libro_banco().getValor("ide_teclb"));
                } else {
                    if (boo_hizo_asiento_interes) {
                        System.out.print("valor del numero de certificado:antes de guardar de la tablas............. " + tab_tabla.getValor("num_certificado_ipcer"));
                        System.out.println("si ingresa al metodo booleano -- asiento de interes ");
                        System.out.println("valor del booleano  " + boo_hizo_asiento_interes);
                        utilitario.getConexion().agregarSqlPantalla("UPDATE iyp_certificado set ide_cnccc_interes=" + ide_cnccc + " where ide_ipcer=" + tab_tabla.getValor(tab_tabla.getFilaActual(), "ide_ipcer"));
                        cxp.generarCabeceraTransaccionCxP(tab_tabla.getValor("ide_geper"), utilitario.getVariable("p_cxp_tipo_trans_inversion_interes"), via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"), null, tab_tabla.getValor("observacion_ipcer"), false, null);
                        cxp.getTab_det_tran_cxp().limpiar();
                        System.out.print("valor del numero de certificado:antes de guardar " + tab_tabla.getValor("num_certificado_ipcer"));
                        cxp.generarDetalleTransaccionCxP(cxp.getTab_cab_tran_cxp().getValor("ide_cpctr"), utilitario.getVariable("p_cxp_tipo_trans_inversion_interes"), via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"), null, via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"), Double.parseDouble(tab_tabla.getValor("interes_ipcer")), tab_tabla.getValor("num_certificado_ipcer"), ide_cnccc, bancos.getTab_cab_libro_banco().getValor("ide_teclb"));
                        cxp.getTab_det_tran_cxp().guardar();
                        System.out.print("valor del numero de certificado: guardardado " + tab_tabla.getValor("num_certificado_ipcer"));
                    } else {
                        System.out.println("si ingresa aasiento de inversion de capital");
                        bancos.generarLibroBanco(tab_tabla.getValor("ide_geper"), via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"), utilitario.getVariable("p_tes_tran_deposito"), tab_tabla.getValor("ide_tecba"), ide_cnccc, Double.parseDouble(tab_tabla.getValor("capital_ipcer")), via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"), tex_num_cheque.getValue().toString());
                        tab_tabla.setValor("ide_teclb", bancos.getTab_cab_libro_banco().getValor("ide_teclb"));
                        System.out.println("ide_cpdtr--------- " + cxp.getTab_det_tran_cxp().getValor("ide_cpdtr"));
                        cxp.generarCabeceraTransaccionCxP(tab_tabla.getValor("ide_geper"), utilitario.getVariable("p_cxp_tipo_trans_inversion"), via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"), null, tab_tabla.getValor("observacion_ipcer"), false, null);
                        cxp.getTab_det_tran_cxp().limpiar();
                        cxp.generarDetalleTransaccionCxP(cxp.getTab_cab_tran_cxp().getValor("ide_cpctr"), utilitario.getVariable("p_cxp_tipo_trans_inversion"), via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"), null, via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"), Double.parseDouble(tab_tabla.getValor("capital_ipcer")), tab_tabla.getValor("num_certificado_ipcer"), ide_cnccc, bancos.getTab_cab_libro_banco().getValor("ide_teclb"));
                        cxp.getTab_det_tran_cxp().guardar();
                        tab_tabla.setValor("ide_cpdtr", cxp.getTab_det_tran_cxp().getValor("ide_cpdtr"));
                        System.out.println("ide_cpdtr--------- " + cxp.getTab_det_tran_cxp().getValor("ide_cpdtr"));
                        System.out.println("valor de libro banco false " + bancos.getTab_cab_libro_banco().getValor("ide_teclb"));
                    }
                }
                tab_tabla.guardar();
                utilitario.getConexion().guardarPantalla();
            }
        }
    }

    public void cancelarDialogo() {
        if (via_asiento.isVisible()) {
            via_asiento.cerrar();
            utilitario.addUpdate("via_asiento");
        }
        //cancela todo lo que haya tenido hasta ese momento
        //utilitario.getConexion().rollback();*******
        utilitario.getConexion().getSqlPantalla().clear();
    }

    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra 
        rep_reporte.dibujar();

    }
    Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
//Se ejecuta cuando se selecciona un reporte de la lista

        if (rep_reporte.getReporteSelecionado().equals("Certificados de Inversiones")) {
            if (rep_reporte.isVisible()) {
                if (com_tipo_inversion.getValue() != null) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    TablaGenerica tab_cab_con = utilitario.consultar("select * from iyp_certificado where ide_ipcer=" + tab_tabla.getValor("ide_ipcer"));
                    if (tab_cab_con.getTotalFilas() > 0) {
                        if (tab_cab_con.getValor(0, "ide_cnccc") != null && !tab_cab_con.getValor(0, "ide_cnccc").isEmpty()) {
                            parametro.put("ide_cnccc", tab_cab_con.getValor(0, "ide_cnccc") + "");
                        } else {
                            parametro.put("ide_cnccc", "-1");
                        }
                    } else {
                        parametro.put("ide_cnccc", "-1");
                    }
                    parametro.put("ide_ipcer", Long.parseLong(tab_tabla.getValor("ide_ipcer")));
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene Certificados de Inversion");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante Contabilidad")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla.getValor("ide_cnccc") != null) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cnccc", Long.parseLong(tab_tabla.getValor("ide_cnccc")));
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene comprobante de contabilidad");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Inversiones Realizadas")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                dia_tipo_inversion.dibujar();
                utilitario.addUpdate("rep_reporte,dia_tipo_inversion");
            } else if (dia_tipo_inversion.isVisible()) {
                if (rad_tipo_inversion.getValue().equals("true")) {
                    dia_tipo_inversion.cerrar();
                    parametro.put("es_inver_banco_ipcer", true);
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    dia_tipo_inversion.cerrar();
                    parametro.put("es_inver_banco_ipcer", false);
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                }
            } else {
                utilitario.agregarMensajeInfo("No se puede generar el reporte", "Escoja solo un tipo de opcion");
                dia_tipo_inversion.cerrar();
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante Contabilidad Interes")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla.getValor("ide_cnccc_interes") != null) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cnccc", Long.parseLong(tab_tabla.getValor("ide_cnccc_interes")));
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene comprobante de contabilidad de interes");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante Contabilidad Termino Inversión")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla.getValor("ide_cnccc_terminacion") != null) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cnccc", Long.parseLong(tab_tabla.getValor("ide_cnccc_terminacion")));
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene comprobante de contabilidad de termino de inversión");
                }
            }
        }
    }

    public void cargarCuentaContableBanco(AjaxBehaviorEvent evt) {
        tab_tabla.modificar(evt);
        TablaGenerica tab_cuenta_banco = utilitario.consultar("SELECT * FROM tes_cuenta_banco WHERE ide_tecba=" + tab_tabla.getValor("ide_tecba"));
        if (tab_cuenta_banco.getTotalFilas() > 0) {
            tab_tabla.setValor(tab_tabla.getFilaActual(), "con_ide_cndpc", tab_cuenta_banco.getValor(0, "ide_cndpc"));
        }
        utilitario.addUpdateTabla(tab_tabla, "con_ide_cndpc", "");
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public SeleccionFormatoReporte getSel_rep() {
        return sel_rep;
    }

    public void setSel_rep(SeleccionFormatoReporte sel_rep) {
        this.sel_rep = sel_rep;
    }

    public VistaAsiento getVia_asiento() {
        return via_asiento;
    }

    public void setVia_asiento(VistaAsiento via_asiento) {
        this.via_asiento = via_asiento;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public Dialogo getDia_tipo_inversion() {
        return dia_tipo_inversion;
    }

    public void setDia_tipo_inversion(Dialogo dia_tipo_inversion) {
        this.dia_tipo_inversion = dia_tipo_inversion;
    }

    public Radio getRad_tipo_inversion() {
        return rad_tipo_inversion;
    }

    public void setRad_tipo_inversion(Radio rad_tipo_inversion) {
        this.rad_tipo_inversion = rad_tipo_inversion;
    }

//    public Boton getBot_anular_inversion() {
//        return bot_anular_inversion;
//    }
//
//    public void setBot_anular_inversion(Boton bot_anular_inversion) {
//        this.bot_anular_inversion = bot_anular_inversion;
//    }
    public Boton getBot_generar_asiento_inversion() {
        return bot_generar_asiento_inversion;
    }

    public void setBot_generar_asiento_inversion(Boton bot_generar_asiento_inversion) {
        this.bot_generar_asiento_inversion = bot_generar_asiento_inversion;
    }

    public Boton getBot_generar_asiento_inversion_interes() {
        return bot_generar_asiento_inversion_interes;
    }

    public void setBot_generar_asiento_inversion_interes(Boton bot_generar_asiento_inversion_interes) {
        this.bot_generar_asiento_inversion_interes = bot_generar_asiento_inversion_interes;
    }

    public Dialogo getDia_banco() {
        return dia_banco;
    }

    public void setDia_banco(Dialogo dia_banco) {
        this.dia_banco = dia_banco;
    }

    public Consulta getCon_inversion_vencida() {
        return con_inversion_vencida;
    }

    public void setCon_inversion_vencida(Consulta con_inversion_vencida) {
        this.con_inversion_vencida = con_inversion_vencida;
    }
}

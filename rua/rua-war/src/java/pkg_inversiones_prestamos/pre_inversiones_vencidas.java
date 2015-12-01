/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.  banco tipo trans
 */
package pkg_inversiones_prestamos;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
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
public class pre_inversiones_vencidas extends Pantalla {

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
    private Boton bot_terminar_inversion = new Boton();
    private Boton bot_renovar_inversion = new Boton();
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
    private Dialogo dia_certificado_inversion = new Dialogo();
    private Tabla tab_renovacion_inversion = new Tabla();
    private Etiqueta eti_adicional_inversion = new Etiqueta();
    private Texto tex_adicional_inversion = new Texto();
    private AutoCompletar aut_buscar_inversion = new AutoCompletar();
    private Boton bot_clean = new Boton();
    private Dialogo dia_renovacion_capital_interes = new Dialogo();
    private Radio rad_capital_interes = new Radio();

    public pre_inversiones_vencidas() {
        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");
        if (lis_plan != null && !lis_plan.isEmpty()) {

            bar_botones.getBot_insertar().setUpdate("tab_tabla");
            bar_botones.getBot_guardar().setUpdate("tab_tabla");
            //  bar_botones.setConfirmarGuardar("tab_tabla");
            bar_botones.getBot_eliminar().setUpdate("tab_tabla");
            bar_botones.agregarReporte();
            bar_botones.agregarComponente(eti_tipo_inversion);
            bar_botones.agregarComponente(com_tipo_inversion);
            bar_botones.agregarBoton(bot_terminar_inversion);
            bar_botones.agregarBoton(bot_renovar_inversion);
            bar_botones.agregarComponente(new Etiqueta("Buscar Inversión"));
            bar_botones.agregarComponente(aut_buscar_inversion);

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
            tab_tabla.getColumna("ide_ipein").setCombo("iyp_estado_inversion", "ide_ipein", "nombre_ipein", "");
            tab_tabla.getColumna("con_ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "ide_cntcu=" + utilitario.getVariable("p_iyp_activo"));
            tab_tabla.getColumna("con_ide_cndpc").setAutoCompletar();
            tab_tabla.getColumna("ide_cnmod").setCombo("con_moneda", "ide_cnmod", "nombre_cnmod", "");
            tab_tabla.getColumna("interes_ipcer").setEtiqueta();
            tab_tabla.getColumna("interes_ipcer").setEstilo("font-size:13px;font-weight: bold;");
            tab_tabla.getColumna("valor_a_pagar_ipcer").setEtiqueta();
            tab_tabla.getColumna("valor_a_pagar_ipcer").setEstilo("font-size:13px;font-weight: bold;");
            tab_tabla.getColumna("ide_ipcin").setPermitirNullCombo(false);
            tab_tabla.getColumna("es_inver_banco_ipcer").setVisible(false);
            tab_tabla.setCondicion("es_inver_banco_ipcer is null");
            tab_tabla.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "");
            tab_tabla.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "nivel_geper='HIJO'");
            tab_tabla.getColumna("ide_geper").setAutoCompletar();
            tab_tabla.getColumna("fecha_sistema_ipcer").setVisible(false);
            tab_tabla.getColumna("hora_sistema_ipcer").setVisible(false);
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
            tab_tabla.setLectura(true);
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

            bot_terminar_inversion.setId("bot_terminar_inversion");
            bot_terminar_inversion.setValue("TERMINAR");
            bot_terminar_inversion.setMetodo("terminarInversion");

            bot_renovar_inversion.setId("bot_renovar_inversion");
            bot_renovar_inversion.setValue("RENOVAR");
//            bot_renovar_inversion.setMetodo("renovarInversion");
            bot_renovar_inversion.setMetodo("renovarCapitaInteres");

            tex_num_cheque.setId("tex_num_cheque");
            com_banco.setCombo("select ide_teban,nombre_teban from tes_banco where ide_empr=" + utilitario.getVariable("ide_empr"));
            com_banco.setMetodo("cargar_cuentas");
            com_cuenta_banco.setId("com_cuenta_banco");
            com_cuenta_banco.setCombo(new ArrayList());
            com_cuenta_banco.setMetodo("cargar_num_cheque");
            com_tipo_tansaccion.setId("com_tipo_tansaccion");
            com_tipo_tansaccion.setCombo("SELECT ide_tettb,nombre_tettb FROM tes_tip_tran_banc WHERE signo_tettb<0 AND ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_tettb not in(" + utilitario.getVariable("p_tes_nota_debito") + "," + utilitario.getVariable("p_tes_tran_reversa_menos") + ")");

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
            dia_banco.getBot_aceptar().setMetodo("aceptarDialogoBanco");
            //dia_banco.getBot_cancelar().setMetodo("cancelarDialogo");

            tab_renovacion_inversion.setId("tab_renovacion_inversion");
            tab_renovacion_inversion.setTabla("iyp_certificado", "ide_ipcer", -1);
            utilitario.buscarNombresVisuales(tab_renovacion_inversion);

            tab_renovacion_inversion.getColumna("ide_ipcin").setCombo("iyp_clase_inversion", "ide_ipcin", "nombre_ipcin", "");
            tab_renovacion_inversion.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "ide_cntcu=" + utilitario.getVariable("p_iyp_pasivo"));
            tab_renovacion_inversion.getColumna("ide_cndpc").setAutoCompletar();
            tab_renovacion_inversion.getColumna("ide_ipein").setCombo("iyp_estado_inversion", "ide_ipein", "nombre_ipein", "");
            tab_renovacion_inversion.getColumna("ide_ipein").setValorDefecto(utilitario.getVariable("p_iyp_estado_activo_inversion"));
            tab_renovacion_inversion.getColumna("con_ide_cndpc").setVisible(false);
            tab_renovacion_inversion.getColumna("ide_cnmod").setCombo("con_moneda", "ide_cnmod", "nombre_cnmod", "");
            tab_renovacion_inversion.getColumna("ide_cnmod").setPermitirNullCombo(false);
            tab_renovacion_inversion.getColumna("capital_ipcer").setMetodoChange("calcularInteres");
            tab_renovacion_inversion.getColumna("tasa_ipcer").setMetodoChange("calcularInteres");
            tab_renovacion_inversion.getColumna("plazo_ipcer").setMetodoChange("calcularInteres");
            tab_renovacion_inversion.getColumna("interes_ipcer").setEtiqueta();
            tab_renovacion_inversion.getColumna("interes_ipcer").setEstilo("font-size:13px;font-weight: bold;");
            tab_renovacion_inversion.getColumna("valor_a_pagar_ipcer").setEtiqueta();
            tab_renovacion_inversion.getColumna("valor_a_pagar_ipcer").setEstilo("font-size:13px;font-weight: bold;");
            tab_renovacion_inversion.getColumna("num_certificado_ipcer").setLectura(true);
            tab_renovacion_inversion.getColumna("fecha_emision_ipcer").setValorDefecto(utilitario.getFechaActual());
            tab_renovacion_inversion.getColumna("fecha_emision_ipcer").setMetodoChange("obtenetFechaVencimiento");
            tab_renovacion_inversion.getColumna("fecha_vence_ipcer").setLectura(true);
            tab_renovacion_inversion.getColumna("ide_ipcin").setPermitirNullCombo(false);
            tab_renovacion_inversion.getColumna("es_inver_banco_ipcer").setVisible(false);
            tab_renovacion_inversion.setCondicion("es_inver_banco_ipcer is null");
            tab_renovacion_inversion.getColumna("es_inver_banco_ipcer").setValorDefecto("true");
            tab_renovacion_inversion.getColumna("ide_cnccc").setVisible(false);
            tab_renovacion_inversion.getColumna("ide_cnccc_interes").setVisible(false);
            tab_renovacion_inversion.getColumna("ide_cnccc_terminacion").setVisible(false);
            tab_renovacion_inversion.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_renovacion_inversion.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "");
            tab_renovacion_inversion.getColumna("ide_usua").setLectura(true);
            tab_renovacion_inversion.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "nivel_geper='HIJO'");
            tab_renovacion_inversion.getColumna("ide_geper").setAutoCompletar();
            tab_renovacion_inversion.getColumna("fecha_sistema_ipcer").setValorDefecto(utilitario.getFechaActual());
            tab_renovacion_inversion.getColumna("hora_sistema_ipcer").setValorDefecto(utilitario.getHoraActual());
            tab_renovacion_inversion.getColumna("fecha_sistema_ipcer").setVisible(false);
            tab_renovacion_inversion.getColumna("hora_sistema_ipcer").setVisible(false);
            tab_renovacion_inversion.getColumna("ide_geper").setRequerida(true);
            tab_renovacion_inversion.getColumna("ide_ipein").setRequerida(true);
            tab_renovacion_inversion.getColumna("observacion_ipcer").setRequerida(true);
            tab_renovacion_inversion.getColumna("ide_cnmod").setRequerida(true);
            tab_renovacion_inversion.getColumna("ide_ipcin").setRequerida(true);
            tab_renovacion_inversion.getColumna("hace_asiento_ipcer").setValorDefecto("false");
            tab_renovacion_inversion.getColumna("hace_asiento_ipcer").setVisible(false);
            tab_renovacion_inversion.getColumna("ide_tecba").setCombo("select tes_cuenta_banco.ide_tecba,tes_banco.nombre_teban,tes_cuenta_banco.nombre_tecba from  tes_banco,tes_cuenta_banco,sis_empresa where tes_banco.ide_teban=tes_cuenta_banco.ide_teban and sis_empresa.ide_empr=" + utilitario.getVariable("ide_empr") + " and tes_cuenta_banco.ide_sucu=" + utilitario.getVariable("ide_sucu"));
            tab_renovacion_inversion.getColumna("ide_teclb").setVisible(false);
            tab_renovacion_inversion.getColumna("ide_cpdtr").setVisible(false);
            tab_renovacion_inversion.getColumna("ide_tecba").setVisible(false);
            tab_renovacion_inversion.getColumna("es_renovacion_ipcer").setVisible(false);
            tab_renovacion_inversion.setTipoFormulario(true);
            tab_renovacion_inversion.getGrid().setColumns(4);
            utilitario.buscarNombresVisuales(tab_renovacion_inversion);
            tab_renovacion_inversion.dibujar();

            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_renovacion_inversion);

            dia_renovacion_capital_interes.setId("dia_renovacion_capital_interes");
            dia_renovacion_capital_interes.setTitle("Renovación Capital e Interes");
            dia_renovacion_capital_interes.setDynamic(false);
            dia_renovacion_capital_interes.setWidth("30%");
            dia_renovacion_capital_interes.setHeight("30%");

            List lista2 = new ArrayList();
            Object fila21[] = {
                "0", "CAPITAL + INTERES"
            };
            Object fila31[] = {
                "1", "SOLO CAPITAL"
            };
            lista2.add(fila21);
            lista2.add(fila31);

            rad_capital_interes.setRadio(lista2);
            rad_capital_interes.setVertical();

            Grid gri_interes_capital = new Grid();
            gri_interes_capital.getChildren().add(rad_capital_interes);
            gri_interes_capital.setColumns(2);

            dia_renovacion_capital_interes.setDialogo(gri_interes_capital);
            dia_renovacion_capital_interes.getBot_aceptar().setMetodo("aceptarRenovacionInversion");

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            dia_certificado_inversion.setId("dia_certificado_inversion");
            dia_certificado_inversion.setTitle("CERTIFICADO DE REINVERSIÓN");
            dia_certificado_inversion.setDynamic(false);
            dia_certificado_inversion.setWidth("88%");
            dia_certificado_inversion.setHeight("76%");
            dia_certificado_inversion.getBot_aceptar().setMetodo("aceptarCertificadoReinversion");

            Grid gri_renovacion_inversion = new Grid();
            gri_renovacion_inversion.setColumns(1);
            gri_renovacion_inversion.getChildren().add(pat_panel2);
            dia_certificado_inversion.setDialogo(gri_renovacion_inversion);
            gri_renovacion_inversion.setStyle("width:" + (dia_certificado_inversion.getAnchoPanel() - 5) + "px;height:" + dia_certificado_inversion.getAltoPanel() + "px;overflow: auto;display: block;");

            aut_buscar_inversion.setId("aut_buscar_inversion");
            aut_buscar_inversion.setAutoCompletar("SELECT cer.ide_ipcer,gp.nom_geper,cer.num_certificado_ipcer FROM iyp_certificado cer "
                    + "LEFT JOIN gen_persona gp ON cer.ide_geper=gp.ide_geper "
                    + "WHERE cer.ide_empr=" + utilitario.getVariable("ide_empr") + "  "
                    + "AND cer.ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
                    + "AND fecha_vence_ipcer<='" + utilitario.getFechaActual() + "' "
                    + "AND ide_ipein=" + utilitario.getVariable("p_iyp_estado_activo_inversion") + " "
                    + "AND es_inver_banco_ipcer=" + com_tipo_inversion.getValue() + "");
            aut_buscar_inversion.setDisabled(true);
            aut_buscar_inversion.setMetodoChange("buscarInversion");
            bot_clean.setIcon("ui-icon-cancel");
            bot_clean.setTitle("Limpiar");
            bot_clean.setMetodo("limpiar");
            bar_botones.agregarComponente(bot_clean);

            agregarComponente(dia_banco);
            agregarComponente(dia_tipo_inversion);
            agregarComponente(div_division);
            agregarComponente(rep_reporte);
            agregarComponente(sel_rep);
            agregarComponente(dia_certificado_inversion);
            agregarComponente(dia_renovacion_capital_interes);
        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }
    }

    public void limpiar() {
        if (aut_buscar_inversion.getValue() != null) {
            aut_buscar_inversion.setValue(null);
            tab_tabla.setCondicion("fecha_vence_ipcer<='" + utilitario.getFechaActual() + "' and ide_ipein=" + utilitario.getVariable("p_iyp_estado_activo_inversion") + " and es_inver_banco_ipcer=" + com_tipo_inversion.getValue());
            tab_tabla.ejecutarSql();
            utilitario.addUpdate("tab_tabla,aut_buscar_inversion");
        }
    }

    public void renovarCapitaInteres() {
        if (com_tipo_inversion.getValue() != null) {
            if (tab_tabla.getValor("ide_cnccc") != null && !tab_tabla.getValor("ide_cnccc").isEmpty()) {
                if (tab_tabla.getValor("ide_cnccc_interes") != null && !tab_tabla.getValor("ide_cnccc_interes").isEmpty()) {
                    if (tab_tabla.getValor("es_renovacion_ipcer") != null && !tab_tabla.getValor("es_renovacion_ipcer").isEmpty()) {
                        if (tab_tabla.getValor("es_renovacion_ipcer").toString().equals("false")) {
                            System.out.println("VALOR DE ES_RENOVACION_IPCER DESPUES DEL IF::  " + tab_tabla.getValor("es_renovacion_ipcer"));
                            System.out.println("INGRESA AL CAPITAL MAS INTERES");
                            dia_renovacion_capital_interes.dibujar();
                        } else {
                            utilitario.agregarMensajeInfo("No se puede renovar", "La inversion ya se encuentra renovada");
                        }
                    } else {
                        utilitario.agregarMensajeInfo("No se puede renovar", "El campo renovacion tiene el registro nulo, consulte con su administrador");
                    }

                } else {
                    utilitario.agregarMensajeInfo("No se puede renovar", "La inversion no tiene realizado el segundo asiento del interes");
                }
            } else {
                utilitario.agregarMensajeInfo("No se puede renovar", "La inversion no tiene realizado el primer asiento del capital");
            }
        } else {
            utilitario.agregarMensajeInfo("No se puede renovar", "Debe seleccionar un tipo de inversión");
        }
    }

    public void aceptarRenovacionInversion() {
        if (tab_tabla.getTotalFilas() > 0) {
            if (rad_capital_interes.getValue().toString().equals("0")) {
                utilitario.getConexion().agregarSqlPantalla("UPDATE iyp_certificado SET es_renovacion_ipcer=TRUE WHERE ide_ipcer=" + tab_tabla.getValor("ide_ipcer"));
                generarAsientoRenovacionInversionCapitalMasInteres(tab_tabla);
                dia_certificado_inversion.cerrar();
                utilitario.addUpdate("dia_certificado_inversion");
            } else {
                System.out.println("SI INGRESO AL RENOVAR SOLO CAPITAL");
                com_tipo_tansaccion.setValue(null);
                com_banco.setValue(null);
                com_cuenta_banco.setValue(null);
                tex_num_cheque.setValue(null);
                cal_fecha_cheque.setValue(utilitario.getFecha(utilitario.getFechaActual()));
                tex_monto_cheque.setValue(tab_tabla.getValor("interes_ipcer"));
                tex_monto_cheque.setDisabled(true);
                dia_banco.dibujar();
            }
        } else {
            utilitario.agregarMensajeError("No Puede Guardar", "Debe seleccionar una cuenta bancaria");
        }
    }

    public void terminarInversion() {
        if (com_tipo_inversion.getValue() != null) {
            if (tab_tabla.getTotalFilas() > 0) {
                if (tab_tabla.getValor("ide_cnccc") != null && !tab_tabla.getValor("ide_cnccc").isEmpty()) {
                    if (tab_tabla.getValor("ide_cnccc_interes") != null && !tab_tabla.getValor("ide_cnccc_interes").isEmpty()) {
                        if (tab_tabla.getValor("ide_ipein") != null && !tab_tabla.getValor("ide_ipein").isEmpty()) {
                            if (tab_tabla.getValor("ide_ipein").equals(utilitario.getVariable("p_iyp_estado_activo_inversion"))) {
                                if (tab_tabla.getValor("ide_cnccc_terminacion") == null || tab_tabla.getValor("ide_cnccc_terminacion").isEmpty()) {
                                    if (tab_tabla.getValor("es_renovacion_ipcer") == null || tab_tabla.getValor("es_renovacion_ipcer").equals("false")) {
                                        System.out.println("SI INGRESO AL TERMINAR INVERSION ");
                                        com_tipo_tansaccion.setValue(null);
                                        com_banco.setValue(null);
                                        com_cuenta_banco.setValue(null);
                                        tex_num_cheque.setValue(null);
                                        cal_fecha_cheque.setValue(utilitario.getFecha(utilitario.getFechaActual()));
                                        tex_monto_cheque.setValue(tab_tabla.getValor("valor_a_pagar_ipcer"));
                                        tex_monto_cheque.setDisabled(true);
                                        dia_banco.dibujar();
                                    } else {
                                        utilitario.agregarMensajeInfo("No se puede Terminar la Inversión", "La inversión ya se encuentra renovada");
                                    }

                                } else {
                                    utilitario.agregarMensajeInfo("No se puede Terminar la Inversión", "La inversión ya tiene el asiento de terminación de Inversión");
                                }
                            } else {
                                utilitario.agregarMensajeInfo("No se puede Terminar la Inversión", "La inversión no esta activa");
                            }
                        } else {
                            utilitario.agregarMensajeInfo("No se puede Terminar la Inversión", "La inversión no tiene el estado activo");
                        }
                    } else {
                        utilitario.agregarMensajeInfo("No se puede Terminar la Inversión", "La inversión no tiene el segundo asiento del interes");
                    }
                } else {
                    utilitario.agregarMensajeInfo("No se puede Terminar la Inversión", "La inversión no tiene el primer asiento del capital");
                }
            } else {
                utilitario.agregarMensajeInfo("No se puede Terminar la Inversión", "La inversión no posee registros");
            }

        } else {
        }
    }

    public void buscarInversion() {
        if (aut_buscar_inversion.getValue() != null) {
            System.out.println("si ingresa al buscar inversion " + aut_buscar_inversion.getValor());
            tab_tabla.setCondicion("fecha_vence_ipcer<='" + utilitario.getFechaActual() + "' and ide_ipein=" + utilitario.getVariable("p_iyp_estado_activo_inversion") + " and es_inver_banco_ipcer=" + com_tipo_inversion.getValue());
            tab_tabla.ejecutarSql();
            tab_tabla.setFilaActual(aut_buscar_inversion.getValor());
            utilitario.addUpdate("tab_tabla");
        } else {
            utilitario.agregarMensajeInfo("No se puede buscar", "Debe seleccionar un tipo de inversión");
        }
    }

    public void cargar_num_cheque() {
//        if (com_tipo_tansaccion.getValue().equals(utilitario.getVariable("p_tes_tran_cheque"))) {//combo es cheque
        cls_bancos banco = new cls_bancos();
        tex_num_cheque.setValue(banco.obtenerNumMaximoTran(com_cuenta_banco.getValue() + "", utilitario.getVariable("p_tes_tran_cheque")));
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

    public boolean validarDialogoBanco() {
        if (com_tipo_tansaccion.getValue() != null) {
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
    boolean boo_es_asiento_pago_interes = false;

    public void generarAsientoPagoInteres() {
        conta.limpiar();
        boo_es_asiento_pago_interes = true;
        if (com_tipo_inversion.getValue().toString().equals("true")) {
            System.out.println("INGRESA AL TRUE DE BANCOS");
        } else {
            System.out.println("INGRESA AL VISTA ASIENTO DEL CAPITAL ");
            cab_com_con = new cls_cab_comp_cont(str_p_con_tipo_comprobante_egreso, p_est_com_normal, "9", tab_tabla.getValor("ide_geper"), utilitario.getFechaActual(), tab_tabla.getValor("observacion_ipcer"));
            lista_detalles.clear();
            cls_bancos banco = new cls_bancos();
            if (com_tipo_tansaccion.getValue() != null) {
                String cuenta_contable_banco = banco.obtenerParametroCuentaBanco("ide_cndpc", com_cuenta_banco.getValue().toString());
                if (cuenta_contable_banco != null) {
                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), cuenta_contable_banco, Double.parseDouble(tab_tabla.getValor("interes_ipcer")), ""));
                }
            } else {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), "", Double.parseDouble(tab_tabla.getValor("interes_ipcer")), ""));
            }
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), utilitario.getVariable("p_iyp_cuenta_interes_por_pagar_cyo_inversiones"), Double.parseDouble(tab_tabla.getValor("interes_ipcer")), ""));
            cab_com_con.setDetalles(lista_detalles);
            via_asiento.setVistaAsiento(cab_com_con);
            via_asiento.dibujar();
            utilitario.addUpdate("via_asiento");
        }
    }

    public void aceptarDialogoBanco() {
        if (rad_capital_interes.getValue() != null) {
            if (validarDialogoBanco()) {
                dia_banco.cerrar();
                generarAsientoPagoInteres();
            }
        } else {
            if (com_tipo_inversion.getValue() != null) {
                if (tab_tabla.getTotalFilas() > 0) {
                    System.out.println("valor de ide_ipein: " + tab_tabla.getValor("ide_ipein"));
                    if (tab_tabla.getValor("ide_ipein").equals(utilitario.getVariable("p_iyp_estado_activo_inversion"))) {
                        TablaGenerica tab_terminar_inversion = utilitario.consultar("SELECT * FROM iyp_certificado WHERE ide_ipcer=" + tab_tabla.getValor("ide_ipcer") + "");
                        if (dia_banco.isVisible()) {
                            if (validarDialogoBanco()) {
                                dia_banco.cerrar();
                                System.out.println("si ingresa al terminar inversion");
                                if (tab_terminar_inversion.getTotalFilas() > 0) {
                                    generarAsientoTerminoInversion(tab_terminar_inversion);
                                }
                            }
                        } else {
                            tex_monto_cheque.setValue(tab_terminar_inversion.getValor("valor_a_pagar_ipcer"));
                            dia_banco.dibujar();
                        }
                    } else {
                        utilitario.agregarMensajeInfo("No se puede terminar la inversión", "La Inversion ya se encuentra terminada");
                    }

                } else {
                    utilitario.agregarMensajeInfo("No se puede terminar la inversión", "Debe tener datos en la tabla");
                }
            } else {
                utilitario.agregarMensajeInfo("No se puede terminar la inversión", "Debe seleccionar un tipo de inversión");
            }
        }
    }

    public void renovarInversion() {
        if (com_tipo_inversion.getValue() != null) {
            if (tab_tabla.getTotalFilas() > 0) {
                System.out.println("si ingresa al renovar la inversion");
                if (tab_tabla.getValor("es_renovacion_ipcer").equals("null") || tab_tabla.getValor("es_renovacion_ipcer").isEmpty() || tab_tabla.getValor("es_renovacion_ipcer").equals("false")) {
                    tab_renovacion_inversion.insertar();
                    tab_renovacion_inversion.setValor("ide_usua", tab_tabla.getValor("ide_usua"));
                    tab_renovacion_inversion.setValor("hace_asiento_ipcer", "true");
                    tab_renovacion_inversion.setValor("ide_geper", tab_tabla.getValor("ide_geper"));
                    tab_renovacion_inversion.setValor("ide_ipcin", tab_tabla.getValor("ide_ipcin"));
                    tab_renovacion_inversion.setValor("ide_cnmod", tab_tabla.getValor("ide_cnmod"));
                    tab_renovacion_inversion.setValor("ide_cndpc", tab_tabla.getValor("ide_cndpc"));
                    tab_renovacion_inversion.setValor("ide_ipein", tab_tabla.getValor("ide_ipein"));
                    tab_renovacion_inversion.setValor("fecha_emision_ipcer", tab_tabla.getValor("fecha_emision_ipcer"));
                    tab_renovacion_inversion.setValor("detalle_plazo_ipcer", tab_tabla.getValor("detalle_plazo_ipcer"));
                    tab_renovacion_inversion.setValor("observacion_ipcer", tab_tabla.getValor("observacion_ipcer"));
                    tab_renovacion_inversion.setValor("ide_tecba", tab_tabla.getValor("ide_tecba"));
                    tab_renovacion_inversion.setValor("es_inver_banco_ipcer", tab_tabla.getValor("es_inver_banco_ipcer"));
                    tab_renovacion_inversion.setValor("aux_renova_ide_ipcer", tab_tabla.getValor("ide_ipcer"));
                    tab_renovacion_inversion.setValor("ide_cnccc", "");
                    tab_renovacion_inversion.setValor("ide_cnccc_interes", "");
                    tab_renovacion_inversion.setValor("ide_cndpc", tab_tabla.getValor("ide_cndpc"));
                    tab_renovacion_inversion.setValor("es_renovacion_ipcer", "false");
                    if (rad_capital_interes.getValue().equals("0")) {
                        tab_renovacion_inversion.setValor("capital_ipcer", tab_tabla.getValor("valor_a_pagar_ipcer"));
                        dia_certificado_inversion.dibujar();
                    }
                    if (rad_capital_interes.getValue().equals("1")) {
                        tab_renovacion_inversion.setValor("capital_ipcer", tab_tabla.getValor("capital_ipcer"));
                        dia_certificado_inversion.dibujar();
                    }
                } else {
                    utilitario.agregarMensajeInfo("No se puede renovar la inversión", "La inversión actual ya se encuentra realizada");
                }

            } else {
                utilitario.agregarMensajeInfo("No se puede renovar la inversión", "No tiene ningun registro para renovar");
            }
        } else {
            utilitario.agregarMensajeInfo("No se puede renovar la inversión", "Debe seleccionar un tipo de inversión");
        }
    }

    public void cargarTipoInversion() {
        System.out.println("Si entra al cargar tipo inversion");
        if (com_tipo_inversion.getValue() != null) {
            aut_buscar_inversion.setDisabled(false);
            aut_buscar_inversion.setValue(null);
            aut_buscar_inversion.setAutoCompletar("SELECT cer.ide_ipcer,gp.nom_geper,cer.num_certificado_ipcer FROM iyp_certificado cer "
                    + "LEFT JOIN gen_persona gp ON cer.ide_geper=gp.ide_geper "
                    + "WHERE cer.ide_empr=" + utilitario.getVariable("ide_empr") + "  "
                    + "AND cer.ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
                    + "AND fecha_vence_ipcer<='" + utilitario.getFechaActual() + "' "
                    + "AND ide_ipein=" + utilitario.getVariable("p_iyp_estado_activo_inversion") + " "
                    + "AND es_inver_banco_ipcer=" + com_tipo_inversion.getValue() + "");
            System.out.println("valor del combo............ " + com_tipo_inversion.getValue());
            tab_tabla.setCondicion("fecha_vence_ipcer<='" + utilitario.getFechaActual() + "' and ide_ipein=" + utilitario.getVariable("p_iyp_estado_activo_inversion") + " and es_inver_banco_ipcer=" + com_tipo_inversion.getValue());
            tab_tabla.getColumna("es_inver_banco_ipcer").setValorDefecto(com_tipo_inversion.getValue() + "");
            tab_tabla.ejecutarSql();
            utilitario.addUpdate("aut_buscar_inversion");
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
        if (tab_tabla.getValor("ide_cndpc") == null || tab_tabla.getValor("ide_cndpc").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "debe seleccionar la la cuenta del pasivo");
            return false;
        }
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

    public boolean validarRenovacionInversion() {
        if (tab_renovacion_inversion.getValor("ide_cndpc") == null || tab_renovacion_inversion.getValor("ide_cndpc").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "debe seleccionar la la cuenta del pasivo");
            return false;
        }
        if (tab_renovacion_inversion.getValor("capital_ipcer") == null || tab_renovacion_inversion.getValor("capital_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "debe ingresar el capital");
            return false;
        }
        if (tab_renovacion_inversion.getValor("plazo_ipcer") == null || tab_renovacion_inversion.getValor("plazo_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "debe ingresar el plazo");
            return false;
        }
        if (tab_renovacion_inversion.getValor("tasa_ipcer") == null || tab_renovacion_inversion.getValor("tasa_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "debe ingresar la tasa de interes");
            return false;
        }
        if (tab_renovacion_inversion.getValor("fecha_emision_ipcer") == null || tab_renovacion_inversion.getValor("fecha_emision_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la fecha de emision");
            return false;
        }
        if (tab_renovacion_inversion.getValor("observacion_ipcer") == null || tab_renovacion_inversion.getValor("observacion_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la observación");
            return false;
        }
        if (tab_renovacion_inversion.getValor("ide_geper") == null || tab_renovacion_inversion.getValor("ide_geper").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar el Beneficiario");
            return false;
        }
        if (tab_renovacion_inversion.getValor("ide_cnmod") == null || tab_renovacion_inversion.getValor("ide_cnmod").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la Moneda");
            return false;
        }
        if (tab_renovacion_inversion.getValor("ide_ipein") == null || tab_renovacion_inversion.getValor("ide_ipein").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la el estado");
            return false;
        }
        if (tab_renovacion_inversion.getValor("ide_ipcin") == null || tab_renovacion_inversion.getValor("ide_ipcin").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la Clase de Inversión");
            return false;
        }
        if (tab_renovacion_inversion.getValor("observacion_ipcer") == null || tab_renovacion_inversion.getValor("observacion_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar una observación");
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
    }

    @Override
    public void eliminar() {
    }

    public void obtenetFechaVencimiento(AjaxBehaviorEvent evt) {
        tab_tabla.modificar(evt);
        System.out.println("SI ENTRA..." + utilitario.sumarDiasFecha(utilitario.getFecha(tab_tabla.getValor("fecha_emision_ipcer")), Integer.parseInt(tab_tabla.getValor("plazo_ipcer"))));
        System.out.println("SI ENTRA..." + utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_tabla.getValor("fecha_emision_ipcer")), Integer.parseInt(tab_tabla.getValor("plazo_ipcer")))));

        tab_tabla.setValor(tab_tabla.getFilaActual(), "fecha_vence_ipcer", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_tabla.getValor("fecha_emision_ipcer")), Integer.parseInt(tab_tabla.getValor("plazo_ipcer")))));
        utilitario.addUpdateTabla(tab_tabla, "fecha_vence_ipcer", "");
        calcularInteres(evt);
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

    public void calcularInteres(AjaxBehaviorEvent evt) {
        tab_renovacion_inversion.modificar(evt);
        double capital = 0;
        double tasa = 0;
        double plazo = 0;
        double interes = 0;
        double valortotal = 0;
        if (tab_renovacion_inversion.getValor(tab_renovacion_inversion.getFilaActual(), "capital_ipcer") != null && !tab_renovacion_inversion.getValor(tab_renovacion_inversion.getFilaActual(), "capital_ipcer").isEmpty()) {
            try {
                capital = Double.parseDouble(tab_renovacion_inversion.getValor(tab_renovacion_inversion.getFilaActual(), "capital_ipcer"));
            } catch (Exception e) {
                capital = 0;
            }
        }
        if (tab_renovacion_inversion.getValor(tab_renovacion_inversion.getFilaActual(), "tasa_ipcer") != null && !tab_renovacion_inversion.getValor(tab_renovacion_inversion.getFilaActual(), "tasa_ipcer").isEmpty()) {
            try {
                tasa = Double.parseDouble(tab_renovacion_inversion.getValor(tab_renovacion_inversion.getFilaActual(), "tasa_ipcer"));

            } catch (Exception e) {
                tasa = 0;
            }
        }
        if (tab_renovacion_inversion.getValor(tab_renovacion_inversion.getFilaActual(), "plazo_ipcer") != null && !tab_renovacion_inversion.getValor(tab_renovacion_inversion.getFilaActual(), "plazo_ipcer").isEmpty()) {
            try {
                plazo = Double.parseDouble(tab_renovacion_inversion.getValor(tab_renovacion_inversion.getFilaActual(), "plazo_ipcer"));
                obtenetFechaVencimiento();
            } catch (Exception e) {
                plazo = 0;
                obtenetFechaVencimiento();
            }
        }
        interes = (capital * tasa * plazo) / 36000;
        valortotal = capital + interes;
        tab_renovacion_inversion.setValor("interes_ipcer", utilitario.getFormatoNumero(interes, 2));
        tab_renovacion_inversion.setValor("valor_a_pagar_ipcer", utilitario.getFormatoNumero(valortotal, 2));
        utilitario.addUpdateTabla(tab_renovacion_inversion, "interes_ipcer,valor_a_pagar_ipcer", "");
    }

    public boolean obtenetFechaVencimiento() {
        if (tab_renovacion_inversion.getValor("plazo_ipcer") != null && !tab_renovacion_inversion.getValor("plazo_ipcer").isEmpty()) {
            System.out.println("SI ENTRA..." + utilitario.sumarDiasFecha(utilitario.getFecha(tab_renovacion_inversion.getValor("fecha_emision_ipcer")), Integer.parseInt(tab_renovacion_inversion.getValor("plazo_ipcer"))));
            System.out.println("SI ENTRA..." + utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_renovacion_inversion.getValor("fecha_emision_ipcer")), Integer.parseInt(tab_renovacion_inversion.getValor("plazo_ipcer")))));
            tab_renovacion_inversion.setValor(tab_renovacion_inversion.getFilaActual(), "fecha_vence_ipcer", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_renovacion_inversion.getValor("fecha_emision_ipcer")), Integer.parseInt(tab_renovacion_inversion.getValor("plazo_ipcer")))));
            utilitario.addUpdateTabla(tab_renovacion_inversion, "fecha_vence_ipcer", "");
            return true;
        } else {
            tab_renovacion_inversion.setValor(tab_renovacion_inversion.getFilaActual(), "fecha_vence_ipcer", "");
            utilitario.addUpdateTabla(tab_renovacion_inversion, "fecha_vence_ipcer", "");
            return false;
        }
    }

    public void secuenciaCertificadoRenovacion() {
        tab_renovacion_inversion.setValor("num_certificado_ipcer", secuenciaCertificado1Renovacion());
        utilitario.addUpdate("tab_renovacion_inversion");
    }

    public String secuenciaCertificado1Renovacion() {
        List list = utilitario.getConexion().consultar("SELECT max(num_certificado_ipcer)  FROM iyp_certificado WHERE es_inver_banco_ipcer=" + tab_renovacion_inversion.getValor("es_inver_banco_ipcer") + " AND ide_empr=" + utilitario.getVariable("ide_empr") + " AND ide_sucu=" + utilitario.getVariable("ide_sucu") + "");
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
    boolean boo_es_asiento_termino_inversion = false;

    public void generarAsientoTerminoInversion(TablaGenerica tab_iypcertificado) {
        conta.limpiar();
        boo_es_asiento_termino_inversion = true;
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
            cab_com_con = new cls_cab_comp_cont(str_p_con_tipo_comprobante_egreso, p_est_com_normal, "9", tab_tabla.getValor("ide_geper"), utilitario.getFechaActual(), tab_tabla.getValor("observacion_ipcer"));
            lista_detalles.clear();
            cls_bancos banco = new cls_bancos();
            if (com_tipo_tansaccion.getValue() != null) {
                String cuenta_contable_banco = banco.obtenerParametroCuentaBanco("ide_cndpc", com_cuenta_banco.getValue().toString());
                if (cuenta_contable_banco != null) {
                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), cuenta_contable_banco, Double.parseDouble(tab_iypcertificado.getValor("valor_a_pagar_ipcer")), ""));
                }
            } else {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), "", Double.parseDouble(tab_iypcertificado.getValor("valor_a_pagar_ipcer")), ""));
            }

            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), tab_iypcertificado.getValor("ide_cndpc"), Double.parseDouble(tab_iypcertificado.getValor("capital_ipcer")), ""));
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), utilitario.getVariable("p_iyp_cuenta_interes_por_pagar_cyo_inversiones"), Double.parseDouble(tab_iypcertificado.getValor("interes_ipcer")), ""));
            cab_com_con.setDetalles(lista_detalles);
            via_asiento.setVistaAsiento(cab_com_con);
            via_asiento.dibujar();
            utilitario.addUpdate("via_asiento");
        }
    }
    boolean boo_hizo_asiento_renovacion_inv_cap_mas_interes = false;

    public void generarAsientoRenovacionInversionCapitalMasInteres(Tabla tab_iypcertificado) {
        conta.limpiar();
        boo_hizo_asiento_renovacion_inv_cap_mas_interes = true;
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
            cab_com_con = new cls_cab_comp_cont(str_p_con_tipo_comprobante_egreso, p_est_com_normal, "9", tab_tabla.getValor("ide_geper"), utilitario.getFechaActual(), tab_tabla.getValor("observacion_ipcer"));
            lista_detalles.clear();
            double dou_capital_anterior = 0;
            double dou_capital_renovacion = Double.parseDouble(tab_tabla.getValor("capital_ipcer"));
            double dou_interes_renovacion = Double.parseDouble(tab_tabla.getValor("interes_ipcer"));
            double dou_interes_anterior = 0;
            double dou_total_renovacion_cinteres = 0;
            cls_bancos banco = new cls_bancos();
            System.out.println("INGRESA AL VISTA ASIENTO DEL CAPITAL MAS EL INTERES");
            System.out.println("valor del debe: " + dou_total_renovacion_cinteres);
            System.out.println("valor del haber1: " + dou_interes_anterior);
            System.out.println("valor del haber2: " + dou_interes_renovacion);
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), utilitario.getVariable("p_iyp_cuenta_interes_por_pagar_cyo_inversiones"), Double.parseDouble(tab_tabla.getValor("interes_ipcer")), ""));
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), tab_tabla.getValor("ide_cndpc"), Double.parseDouble(tab_tabla.getValor("interes_ipcer")), ""));
            //lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), "", dou_interes_renovacion, ""));
            cab_com_con.setDetalles(lista_detalles);
            via_asiento.setVistaAsiento(cab_com_con);
            via_asiento.dibujar();
            utilitario.addUpdate("via_asiento");
        }
    }

    public void aceptarCertificadoReinversion() {
        System.out.println("si ingresa al aceptar  certificado reinversion capital e interes");
        if (rad_capital_interes.getValue().equals("0")) {
            tab_renovacion_inversion.setValor("num_certificado_ipcer", secuenciaCertificado1());
            tab_renovacion_inversion.setValor("ide_cnccc", ide_cnccc_capital);
            tab_renovacion_inversion.guardar();
            dia_certificado_inversion.cerrar();
            dia_renovacion_capital_interes.cerrar();
            utilitario.getConexion().agregarSqlPantalla("UPDATE iyp_certificado SET ES_RENOVACION_IPCER=TRUE WHERE ide_ipcer=" + tab_tabla.getValor("ide_ipcer"));
            guardarPantalla();
            generarAsientoInteresesRenovacion();
        }
        if (rad_capital_interes.getValue().equals("1")) {
            tab_renovacion_inversion.setValor("num_certificado_ipcer", secuenciaCertificado1());
            tab_renovacion_inversion.setValor("ide_cnccc", ide_cnccc_pago_interes);
            tab_renovacion_inversion.guardar();
            dia_certificado_inversion.cerrar();
            dia_renovacion_capital_interes.cerrar();
            utilitario.getConexion().agregarSqlPantalla("UPDATE iyp_certificado SET ES_RENOVACION_IPCER=TRUE WHERE ide_ipcer=" + tab_tabla.getValor("ide_ipcer"));
            guardarPantalla();
            generarAsientoInteresesRenovacion();
        }
    }
    boolean boo_hizo_asiento_interes_renovacion = false;

    public void generarAsientoInteresesRenovacion() {
        conta.limpiar();
        boo_hizo_asiento_interes_renovacion = true;
        if (com_tipo_inversion.getValue().toString().equalsIgnoreCase("true")) {
        } else {
            System.out.println("INGRESO SI INGRESA INVERSION SALESIANOS................ " + com_tipo_inversion.getValue().toString().equalsIgnoreCase("true"));
            cab_com_con = new cls_cab_comp_cont(str_p_con_tipo_comprobante_egreso, p_est_com_normal, "9", tab_tabla.getValor("ide_geper"), utilitario.getFechaActual(), tab_tabla.getValor("observacion_ipcer"));
            lista_detalles.clear();
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), utilitario.getVariable("p_iyp_cuenta_gasto_casas_obras_inv_interes"), Double.parseDouble(tab_renovacion_inversion.getValor("interes_ipcer")), ""));
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), utilitario.getVariable("p_iyp_cuenta_interes_por_pagar_cyo_inversiones"), Double.parseDouble(tab_renovacion_inversion.getValor("interes_ipcer")), ""));

            cab_com_con.setDetalles(lista_detalles);
            via_asiento.setVistaAsiento(cab_com_con);
            via_asiento.dibujar();
            utilitario.addUpdate("via_asiento");
        }
    }

    public void generarCertificadoInversion() {
        renovarInversion();
    }
    String ide_cnccc_capital = "";
    String ide_cnccc_interes = "";
    String ide_cnccc_pago_interes = "";
    String ide_cnccc_termino_inversion = "";
    boolean ban_hizo_cxp = false;

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
            cls_cuentas_x_pagar cxp = new cls_cuentas_x_pagar();
            cls_bancos bancos = new cls_bancos();
            if (ide_cnccc != null) {
                if (boo_hizo_asiento_renovacion_inv_cap_mas_interes) {
                    boo_hizo_asiento_renovacion_inv_cap_mas_interes = false;
                    via_asiento.cerrar();
                    utilitario.addUpdate("via_asiento");
                    ide_cnccc_capital = ide_cnccc;
                    generarCertificadoInversion();
                }
                if (boo_hizo_asiento_interes_renovacion) {
                    boo_hizo_asiento_interes_renovacion = false;
                    via_asiento.cerrar();
                    utilitario.addUpdate("via_asiento");
                    ide_cnccc_interes = ide_cnccc;
                    tab_renovacion_inversion.setValor("ide_cnccc_interes", ide_cnccc_interes);
                    tab_renovacion_inversion.guardar();
                    utilitario.getConexion().agregarSqlPantalla("UPDATE iyp_certificado SET ide_cnccc_interes=" + ide_cnccc_interes + " WHERE ide_ipcer=" + tab_renovacion_inversion.getValor("ide_ipcer"));
                    if (!ban_hizo_cxp) {
                        cxp.generarCabeceraTransaccionCxP(tab_tabla.getValor("ide_geper"), utilitario.getVariable("p_cxp_tipo_trans_renova_interes_reinver"), via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"), null, tab_tabla.getValor("observacion_ipcer"), false, null);
                        cxp.getTab_det_tran_cxp().limpiar();
                        System.out.print("valor del numero de certificado:antes de guardar " + tab_tabla.getValor("num_certificado_ipcer"));
                        cxp.generarDetalleTransaccionCxP(cxp.getTab_cab_tran_cxp().getValor("ide_cpctr"), utilitario.getVariable("p_cxp_tipo_trans_renova_interes_reinver"), via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"), null, via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"), Double.parseDouble(tab_renovacion_inversion.getValor("interes_ipcer")), tab_renovacion_inversion.getValor("num_certificado_ipcer"), ide_cnccc, null);
                        cxp.getTab_det_tran_cxp().guardar();
                    } else {
                        cxp.generarCabeceraTransaccionCxP(tab_tabla.getValor("ide_geper"), utilitario.getVariable("p_cxp_tipo_trans_renova_interes_reinver"), via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"), null, tab_tabla.getValor("observacion_ipcer"), false, null);
                        cxp.getTab_det_tran_cxp().limpiar();
                        cxp.generarDetalleTransaccionCxP(cxp.getTab_cab_tran_cxp().getValor("ide_cpctr"), utilitario.getVariable("p_cxp_tipo_trans_renova_interes_reinver"), via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"), null, via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"), Double.parseDouble(tab_renovacion_inversion.getValor("interes_ipcer")), tab_renovacion_inversion.getValor("num_certificado_ipcer"), ide_cnccc, null);
                        cxp.getTab_det_tran_cxp().guardar();
                    }
                    guardarPantalla();
                }
                if (boo_es_asiento_pago_interes) {
                    boo_es_asiento_pago_interes = false;
                    via_asiento.cerrar();
                    utilitario.addUpdate("via_asiento");
                    ide_cnccc_pago_interes = ide_cnccc;
                    cxp.generarCabeceraTransaccionCxP(tab_tabla.getValor("ide_geper"), utilitario.getVariable("p_cxp_tipo_trans_pago_interes_inversion"), via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"), null, tab_tabla.getValor("observacion_ipcer"), false, null);
                    cxp.getTab_det_tran_cxp().limpiar();
                    if (com_tipo_tansaccion.getValue() != null) {
                        bancos.generarLibroBanco(tab_tabla.getValor("ide_geper"), cal_fecha_cheque.getFecha(), com_tipo_tansaccion.getValue() + "", com_cuenta_banco.getValue() + "", ide_cnccc, Double.parseDouble(tex_monto_cheque.getValue() + ""), via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"), tex_num_cheque.getValue() + "");
                        cxp.generarDetalleTransaccionCxP(cxp.getTab_cab_tran_cxp().getValor("ide_cpctr"), utilitario.getVariable("p_cxp_tipo_trans_pago_interes_inversion"), via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"), null, via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"), Double.parseDouble(tab_tabla.getValor("interes_ipcer")), tab_tabla.getValor("num_certificado_ipcer"), ide_cnccc, bancos.getTab_cab_libro_banco().getValor("ide_teclb"));
                        cxp.getTab_det_tran_cxp().guardar();
                    } else {
                        cxp.generarDetalleTransaccionCxP(cxp.getTab_cab_tran_cxp().getValor("ide_cpctr"), utilitario.getVariable("p_cxp_tipo_trans_pago_interes_inversion"), via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"), null, via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"), Double.parseDouble(tab_tabla.getValor("interes_ipcer")), tab_tabla.getValor("num_certificado_ipcer"), ide_cnccc, null);
                        cxp.getTab_det_tran_cxp().guardar();
                    }
                    ban_hizo_cxp = true;
                    generarCertificadoInversion();
                }
                if (boo_es_asiento_termino_inversion) {
                    boo_es_asiento_termino_inversion = false;
                    via_asiento.cerrar();
                    utilitario.addUpdate("via_asiento");
                    ide_cnccc_termino_inversion = ide_cnccc;
                    cxp.generarCabeceraTransaccionCxP(tab_tabla.getValor("ide_geper"), utilitario.getVariable("p_cxp_tipo_trans_pago_inversion"), via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"), null, tab_tabla.getValor("observacion_ipcer"), false, null);
                    cxp.getTab_det_tran_cxp().limpiar();
                    if (com_tipo_tansaccion.getValue() != null) {
                        bancos.generarLibroBanco(tab_tabla.getValor("ide_geper"), utilitario.getFechaActual(), com_tipo_tansaccion.getValue() + "", com_cuenta_banco.getValue() + "", ide_cnccc, Double.parseDouble(tex_monto_cheque.getValue().toString()), via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"), tex_num_cheque.getValue() + "");
                        cxp.generarDetalleTransaccionCxP(cxp.getTab_cab_tran_cxp().getValor("ide_cpctr"), utilitario.getVariable("p_cxp_tipo_trans_pago_inversion"), via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"), null, via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"), Double.parseDouble(tab_tabla.getValor("interes_ipcer")), tab_tabla.getValor("num_certificado_ipcer"), ide_cnccc, bancos.getTab_cab_libro_banco().getValor("ide_teclb"));
                        cxp.getTab_det_tran_cxp().guardar();
                    } else {
                        cxp.generarDetalleTransaccionCxP(cxp.getTab_cab_tran_cxp().getValor("ide_cpctr"), utilitario.getVariable("p_cxp_tipo_trans_pago_inversion"), via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"), null, via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"), Double.parseDouble(tab_tabla.getValor("interes_ipcer")), tab_tabla.getValor("num_certificado_ipcer"), ide_cnccc, null);
                        cxp.getTab_det_tran_cxp().guardar();
                    }
                    utilitario.getConexion().agregarSqlPantalla("UPDATE iyp_certificado SET ide_ipein=" + utilitario.getVariable("p_iyp_estado_cancelado") + ", ide_cnccc_terminacion=" + ide_cnccc_termino_inversion + " WHERE ide_ipcer=" + tab_tabla.getValor("ide_ipcer"));
                    utilitario.getConexion().guardarPantalla();
                }
            }
        }
    }

    public void cancelarDialogo() {
        if (via_asiento.isVisible()) {
            via_asiento.cerrar();
            utilitario.addUpdate("via_asiento");
        }
        //cancela todo lo que haya tenido hasta ese momento
        //utilitario.getConexion().rollback();***
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

    public void cargarCuentaContableBanco2(AjaxBehaviorEvent evt) {
        tab_renovacion_inversion.modificar(evt);
        TablaGenerica tab_cuenta_banco = utilitario.consultar("SELECT * FROM tes_cuenta_banco WHERE ide_tecba=" + tab_renovacion_inversion.getValor("ide_tecba"));
        if (tab_cuenta_banco.getTotalFilas() > 0) {
            tab_renovacion_inversion.setValor(tab_renovacion_inversion.getFilaActual(), "con_ide_cndpc", tab_cuenta_banco.getValor(0, "ide_cndpc"));
        }
        utilitario.addUpdateTabla(tab_renovacion_inversion, "con_ide_cndpc", "");
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

    public Dialogo getDia_banco() {
        return dia_banco;
    }

    public void setDia_banco(Dialogo dia_banco) {
        this.dia_banco = dia_banco;
    }

    public Dialogo getDia_certificado_inversion() {
        return dia_certificado_inversion;
    }

    public void setDia_certificado_inversion(Dialogo dia_certificado_inversion) {
        this.dia_certificado_inversion = dia_certificado_inversion;
    }

    public Tabla getTab_renovacion_inversion() {
        return tab_renovacion_inversion;
    }

    public void setTab_renovacion_inversion(Tabla tab_renovacion_inversion) {
        this.tab_renovacion_inversion = tab_renovacion_inversion;
    }

    public AutoCompletar getAut_buscar_inversion() {
        return aut_buscar_inversion;
    }

    public void setAut_buscar_inversion(AutoCompletar aut_buscar_inversion) {
        this.aut_buscar_inversion = aut_buscar_inversion;
    }

    public Dialogo getDia_renovacion_capital_interes() {
        return dia_renovacion_capital_interes;
    }

    public void setDia_renovacion_capital_interes(Dialogo dia_renovacion_capital_interes) {
        this.dia_renovacion_capital_interes = dia_renovacion_capital_interes;
    }
}

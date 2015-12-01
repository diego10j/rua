/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_inversiones_prestamos;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
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
public class pre_inversiones extends Pantalla {

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
    private Boton bot_anular_inversion = new Boton();

    public pre_inversiones() {
        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");
        if (lis_plan != null && !lis_plan.isEmpty()) {

            bar_botones.getBot_insertar().setUpdate("tab_tabla");
            bar_botones.getBot_guardar().setUpdate("tab_tabla");
            //  bar_botones.setConfirmarGuardar("tab_tabla");
            bar_botones.getBot_eliminar().setUpdate("tab_tabla");
            bar_botones.agregarReporte();
            bar_botones.agregarComponente(eti_tipo_inversion);
            bar_botones.agregarComponente(com_tipo_inversion);
            bar_botones.agregarBoton(bot_anular_inversion);

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
            tab_tabla.getColumna("ide_ipein").setValorDefecto(utilitario.getVariable("p_iyp_estado_activo_inversion"));
            tab_tabla.getColumna("con_ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "ide_cntcu=" + utilitario.getVariable("p_iyp_activo"));
            tab_tabla.getColumna("con_ide_cndpc").setAutoCompletar();
            tab_tabla.getColumna("ide_cnmod").setCombo("con_moneda", "ide_cnmod", "nombre_cnmod", "");
            tab_tabla.getColumna("ide_cnmod").setPermitirNullCombo(false);
            tab_tabla.getColumna("capital_ipcer").setMetodoChange("calcularInteres");
            tab_tabla.getColumna("tasa_ipcer").setMetodoChange("calcularInteres");
            tab_tabla.getColumna("plazo_ipcer").setMetodoChange("calcularInteres");
            tab_tabla.getColumna("interes_ipcer").setEtiqueta();
            tab_tabla.getColumna("interes_ipcer").setEstilo("font-size:13px;font-weight: bold;");
            tab_tabla.getColumna("valor_a_pagar_ipcer").setEtiqueta();
            tab_tabla.getColumna("valor_a_pagar_ipcer").setEstilo("font-size:13px;font-weight: bold;");
            tab_tabla.getColumna("num_certificado_ipcer").setLectura(true);
            tab_tabla.getColumna("fecha_emision_ipcer").setValorDefecto(utilitario.getFechaActual());
            tab_tabla.getColumna("fecha_emision_ipcer").setMetodoChange("obtenetFechaVencimiento");
            tab_tabla.getColumna("fecha_vence_ipcer").setLectura(true);
            tab_tabla.getColumna("ide_ipcin").setPermitirNullCombo(false);
            tab_tabla.getColumna("es_inver_banco_ipcer").setVisible(false);
            tab_tabla.setCondicion("es_inver_banco_ipcer is null");
            tab_tabla.getColumna("es_inver_banco_ipcer").setValorDefecto("true");
            tab_tabla.getColumna("ide_cnccc").setLectura(true);
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
            tab_tabla.getColumna("ide_cndpc").setRequerida(true);
            tab_tabla.getColumna("observacion_ipcer").setRequerida(true);
            tab_tabla.getColumna("ide_cnmod").setRequerida(true);
            tab_tabla.getColumna("ide_ipcin").setRequerida(true);
            tab_tabla.getColumna("hace_asiento_ipcer").setValorDefecto("true");
            tab_tabla.getColumna("ide_tecba").setCombo("select tes_cuenta_banco.ide_tecba,tes_banco.nombre_teban,tes_cuenta_banco.nombre_tecba from  tes_banco,tes_cuenta_banco,sis_empresa where tes_banco.ide_teban=tes_cuenta_banco.ide_teban and sis_empresa.ide_empr=" + utilitario.getVariable("ide_empr") + " and tes_cuenta_banco.ide_sucu=" + utilitario.getVariable("ide_sucu"));
            tab_tabla.getColumna("ide_tecba").setMetodoChange("cargarCuentaContableBanco");
            tab_tabla.setRecuperarLectura(true);
            //tab_tabla.getColumna("cod_captacion_ipcer").setRequerida(true);

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

            bot_anular_inversion.setId("bot_anular_inversion");
            bot_anular_inversion.setValue("ANULAR INVERSION");
            bot_anular_inversion.setMetodo("anularInversion");

            agregarComponente(dia_tipo_inversion);
            agregarComponente(div_division);
            agregarComponente(rep_reporte);
            agregarComponente(sel_rep);


////            //llenarsecuencial();
        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }
    }
//////    public  void llenarsecuencial(){
//////        Tabla tab_certificado=utilitario.consultar("SELECT * FROM iyp_certificado");
//////        for (int i = 0; i < tab_certificado.getTotalFilas(); i++) {
//////            String str_num_cer=tab_certificado.getValor(i, "num_certificado_ipcer");
//////            int int_num=Integer.parseInt(str_num_cer);
//////            String str_ceros=utilitario.generarCero(10-String.valueOf(int_num).length());
//////            str_num_cer=str_ceros.concat(String.valueOf(int_num));
//////            System.out.println("numero:  "+str_num_cer);
//////            utilitario.getConexion().agregarSqlPantalla("UPDATE  iyp_certificado SET num_certificado_ipcer='"+str_num_cer+"' WHERE ide_ipcer="+tab_certificado.getValor(i, "ide_ipcer"));
//////            
//////        }
//////        utilitario.getConexion().guardarPantalla();
//////    }      

    public void anularInversion() {
        System.out.println("si ingresa al anular inversion");
        if (com_tipo_inversion.getValue() != null) {
            try {
                if (tab_tabla.getValor("ide_ipein").equals(utilitario.getVariable("p_iyp_estado_activo_inversion"))) {
                    cls_contabilidad conta = new cls_contabilidad();
                    cls_bancos banco = new cls_bancos();
                    cls_cuentas_x_pagar cxp = new cls_cuentas_x_pagar();
                    if (tab_tabla.getValor("ide_cnccc") != null && !tab_tabla.getValor("ide_cnccc").isEmpty()) {
                        utilitario.getConexion().agregarSqlPantalla("UPDATE iyp_certificado SET ide_ipein=" + utilitario.getVariable("p_iyp_estado_anulada") + " WHERE ide_ipcer=" + tab_tabla.getValor("ide_ipcer"));
                        conta.anular(tab_tabla.getValor("ide_cnccc"));
                        TablaGenerica tab_banco = utilitario.consultar("SELECT * FROM iyp_certificado WHERE ide_ipcer=" + tab_tabla.getValor("ide_ipcer"));
                        banco.reversar(tab_banco.getValor("ide_teclb"), tab_banco.getValor("observacion_ipcer"));
                        cxp.reversar(tab_tabla.getValor("ide_cnccc"), null, tab_tabla.getValor("observacion_ipcer"), tab_tabla.getValor("ide_cpdtr"));
                    } else {
                        System.out.println("ingresafsdfsd");
                    }
                }
            } catch (Exception e) {
                utilitario.agregarMensajeInfo("No se puede anular la inversión", "La inversion no de encuentra activa");
            }
            utilitario.getConexion().guardarPantalla();
            tab_tabla.ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("No se puede anular la inversión", "Debe seleccionar un tipo de inversión");
        }
    }

    public void cargarTipoInversion() {
        System.out.println("Si entra al cargar tipo inversion");
        if (com_tipo_inversion.getValue() != null) {
            System.out.println("valor del combo............ " + com_tipo_inversion.getValue());
            tab_tabla.setCondicion("es_inver_banco_ipcer=" + com_tipo_inversion.getValue());
            tab_tabla.getColumna("es_inver_banco_ipcer").setValorDefecto(com_tipo_inversion.getValue() + "");
            tab_tabla.ejecutarSql();
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
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), tab_tabla.getValor("ide_cndpc"), Double.parseDouble(tab_tabla.getValor("valor_a_pagar_ipcer")), ""));
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), tab_tabla.getValor("con_ide_cndpc"), Double.parseDouble(tab_tabla.getValor("capital_ipcer")), ""));
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), "", Double.parseDouble(tab_tabla.getValor("interes_ipcer")), ""));
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

                if (com_tipo_inversion.getValue().equals("true")) {
                    cls_bancos bancos = new cls_bancos();
                    bancos.generarLibroBanco(tab_tabla.getValor("ide_geper"), tab_tabla.getValor("fecha_emision_ipcer"), utilitario.getVariable("p_tes_tran_transferencia_menos"), tab_tabla.getValor("ide_tecba"), ide_cnccc, Double.parseDouble(tab_tabla.getValor("capital_ipcer")), tab_tabla.getValor("observacion_ipcer"), tab_tabla.getValor("cod_captacion_ipcer"));
                    tab_tabla.setValor("ide_teclb", bancos.getTab_cab_libro_banco().getValor("ide_teclb"));
                    System.out.println("valor de libro banco true " + bancos.getTab_cab_libro_banco().getValor("ide_teclb"));
                } else {
                    cls_bancos bancos = new cls_bancos();
                    bancos.generarLibroBanco(tab_tabla.getValor("ide_geper"), tab_tabla.getValor("fecha_emision_ipcer"), utilitario.getVariable("p_tes_tran_deposito"), tab_tabla.getValor("ide_tecba"), ide_cnccc, Double.parseDouble(tab_tabla.getValor("capital_ipcer")), tab_tabla.getValor("observacion_ipcer"), tab_tabla.getValor("cod_captacion_ipcer"));
                    cls_cuentas_x_pagar cxp = new cls_cuentas_x_pagar();
                    tab_tabla.setValor("ide_cpdtr", cxp.getTab_det_tran_cxp().getValor("ide_cpdtr"));
                    tab_tabla.setValor("ide_teclb", bancos.getTab_cab_libro_banco().getValor("ide_teclb"));                    
                    cxp.generarCxP(tab_tabla.getValor("ide_geper"), utilitario.getVariable("p_cxp_tipo_trans_inversion"), tab_tabla.getValor("fecha_emision_ipcer"), null, tab_tabla.getValor("observacion_ipcer"), Double.parseDouble(tab_tabla.getValor("valor_a_pagar_ipcer")), tab_tabla.getValor("num_certificado_ipcer"), ide_cnccc);
                    tab_tabla.setValor("ide_teclb", bancos.getTab_cab_libro_banco().getValor("ide_teclb"));
                    tab_tabla.setValor("ide_cpdtr", cxp.getTab_det_tran_cxp().getValor("ide_cpdtr"));
                    System.out.println("valor de libro banco false " + bancos.getTab_cab_libro_banco().getValor("ide_teclb"));
                    System.out.println("valor de cxp detalle trans false " + bancos.getTab_cab_libro_banco().getValor("ide_teclb"));
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
        //utilitario.getConexion().rollback();*****
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

    public Boton getBot_anular_inversion() {
        return bot_anular_inversion;
    }

    public void setBot_anular_inversion(Boton bot_anular_inversion) {
        this.bot_anular_inversion = bot_anular_inversion;
    }
}

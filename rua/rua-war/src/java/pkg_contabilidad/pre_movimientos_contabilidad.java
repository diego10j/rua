package pkg_contabilidad;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.event.SelectEvent;
import pkg_bancos.cls_bancos;
import sistema.aplicacion.Pantalla;

public class pre_movimientos_contabilidad extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Division div_division = new Division();
    private AutoCompletar aut_filtro_tipo_cuenta = new AutoCompletar();
    private Boton bot_clean = new Boton();
    private String p_con_lugar_debe = utilitario.getVariable("p_con_lugar_debe");
    private String p_con_lugar_haber = utilitario.getVariable("p_con_lugar_haber");
    private Texto tex_saldo_cant = new Texto();
    private Texto tex_saldo_valor = new Texto();
    private Texto tex_saldo_debe = new Texto();
    private Texto tex_saldo_haber = new Texto();
    private Texto tex_saldo_total = new Texto();
    private Grid gri_totales = new Grid();
    private Etiqueta eti_suma_debe = new Etiqueta();
    private Etiqueta eti_suma_haber = new Etiqueta();
    private Etiqueta eti_saldo_total = new Etiqueta();
    private String tipo_cuenta = "-1";
    private SeleccionTabla sel_tab = new SeleccionTabla();
    private PanelTabla pat_panel1 = new PanelTabla();
    private SeleccionCalendario sec_calendario = new SeleccionCalendario();
    private Etiqueta eti_cuenta = new Etiqueta("CUENTA CONTABLE: ");
    private Etiqueta eti_rango_fechas = new Etiqueta("DESDE: 0/0/0       HASTA: 0/0/0 ");
    private Etiqueta eti_saldo_anterior = new Etiqueta("SALDO ANTERIOR: ");
    private Grid gri_titulo = new Grid();
    private VistaAsiento via_asiento = new VistaAsiento();
    private Boton bot_generar_asiento = new Boton();
    cls_contabilidad conta = new cls_contabilidad();
    cls_cab_comp_cont cab_com_con;
    List<cls_det_comp_cont> lista_detalles = new ArrayList();
    private VisualizarPDF vpdf_ver = new VisualizarPDF();
    private Boton bot_ver_asiento = new Boton();
    private Texto tex_num_doc_banco = new Texto();
    private Combo com_tip_tran_bancaria = new Combo();

    public pre_movimientos_contabilidad() {
        bar_botones.agregarCalendario();
        aut_filtro_tipo_cuenta.setId("aut_filtro_tipo_cuenta");
        aut_filtro_tipo_cuenta.setAutoCompletar("select ide_cndpc,codig_recur_cndpc,nombre_cndpc from con_det_plan_cuen");
        aut_filtro_tipo_cuenta.setMetodoChange("filtrar_tipo_cuenta");
        sec_calendario.setId("sec_calendario");
        //por defecto friltra un mes
        sec_calendario.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -31));
        sec_calendario.setFecha2(utilitario.getDate());
        agregarComponente(sec_calendario);
        sec_calendario.getBot_aceptar().setMetodo("aceptarRango");

        bar_botones.agregarComponente(new Etiqueta("Tipo Cuenta: "));
        bar_botones.agregarComponente(aut_filtro_tipo_cuenta);
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql("SELECT CAB.fecha_trans_cnccc as FECHA,CAB.ide_cnccc as ASIENTO,CAB.observacion_cnccc as OBSERVACION, "
                + "LUGAR.ide_cnlap,'' as DEBE, '' as HABER, "
                + "(DETA.valor_cndcc * sc.signo_cnscu) as valor_cndcc,'' as SALDO,PERSO.nom_geper as BENEFICIARIO, "
                + "CUENTA.nombre_cndpc as NOMBRE_CUENTA "
                + "from con_cab_comp_cont CAB "
                + "left join gen_persona PERSO on CAB.ide_geper=PERSO.ide_geper "
                + "inner join  con_det_comp_cont DETA on CAB.ide_cnccc=DETA.ide_cnccc "
                + "inner join con_det_plan_cuen CUENTA on  CUENTA.ide_cndpc = DETA.ide_cndpc "
                + "inner join con_lugar_aplicac LUGAR on  LUGAR.ide_cnlap=DETA.ide_cnlap "
                + "inner join con_tipo_cuenta tc on CUENTA.ide_cntcu=tc.ide_cntcu "
                + "inner join con_signo_cuenta sc on tc.ide_cntcu=sc.ide_cntcu and DETA.ide_cnlap=sc.ide_cnlap "
                + "WHERE CUENTA.ide_cndpc=-1 and fecha_trans_cnccc BETWEEN '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "' "
                + "and ide_cneco in (" + utilitario.getVariable("p_con_estado_comp_inicial") + "," + utilitario.getVariable("p_con_estado_comprobante_normal") + "," + utilitario.getVariable("p_con_estado_comp_final") + ") "
                + "and cab.ide_sucu=" + utilitario.getVariable("ide_sucu") + " ORDER BY CAB.fecha_trans_cnccc,cab.ide_cnccc asc");
        tab_tabla1.setCampoPrimaria("ASIENTO");
        tab_tabla1.getColumna("ide_cnlap").setVisible(false);
        tab_tabla1.getColumna("valor_cndcc").setVisible(false);
        tab_tabla1.setScrollRows(20);
        tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);

        eti_cuenta.setStyle("font-size: 14px;font-weight: bold;text-align: center");
        eti_rango_fechas.setStyle("font-size: 14px;font-weight: bold;text-align: center");
        eti_saldo_anterior.setStyle("font-size: 14px;font-weight: bold;text-align: center");
        gri_titulo.setId("gri_titulo");
        gri_titulo.setColumns(2);
        gri_titulo.setWidth("80%");
        Espacio esp1 = new Espacio("10", "10");
        gri_titulo.getChildren().add(eti_cuenta);
        gri_titulo.getChildren().add(esp1);
        gri_titulo.getChildren().add(eti_rango_fechas);
        gri_titulo.getChildren().add(eti_saldo_anterior);

        Division div_1 = new Division();
        div_1.dividir2(gri_titulo, pat_panel, "15%", "H");
        div_division.setId("div_division");
        gri_totales.setId("gri_totales");
        gri_totales.setColumns(6);
        eti_suma_debe.setValue("TOTAL DEBE :");
        eti_suma_debe.setStyle("font-size: 14px;font-weight: bold");
        eti_suma_haber.setValue("TOTAL HABER :");
        eti_suma_haber.setStyle("font-size: 14px;font-weight: bold");
        eti_saldo_total.setValue("SALDO ACTUAL:");
        eti_saldo_total.setStyle("font-size: 14px;font-weight: bold");
        tex_saldo_debe.setDisabled(true);
        tex_saldo_haber.setDisabled(true);
        tex_saldo_total.setDisabled(true);
        gri_totales.setWidth("100%");
        gri_totales.getChildren().add(eti_suma_debe);
        gri_totales.getChildren().add(tex_saldo_debe);
        gri_totales.getChildren().add(eti_suma_haber);
        gri_totales.getChildren().add(tex_saldo_haber);
        gri_totales.getChildren().add(eti_saldo_total);
        gri_totales.getChildren().add(tex_saldo_total);
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarComponente(bot_clean);
        div_division.setFooter(div_1, gri_totales, "90%");
        agregarComponente(div_division);

//  CONFIGURACION COMPROBANTE CONTABILIDAD
        via_asiento.setId("via_asiento");
        via_asiento.getBot_aceptar().setMetodo("aceptarComprobanteContabilidad");
        via_asiento.getBot_cancelar().setMetodo("cancelarDialogo");
        via_asiento.setDynamic(false);

        agregarComponente(via_asiento);

        bot_generar_asiento.setTitle("Generar Comprobante");
        bot_generar_asiento.setValue("Generar Comprobante");
        bot_generar_asiento.setMetodo("generarAsientoContable");
        bar_botones.agregarBoton(bot_generar_asiento);

        bot_ver_asiento.setValue("Ver Comprobante");
        bot_ver_asiento.setMetodo("verComprobante");
        bar_botones.agregarBoton(bot_ver_asiento);
        vpdf_ver.setId("vpdf_ver");
        vpdf_ver.setTitle("Comprobante de Contabilidad");
        agregarComponente(vpdf_ver);

        com_tip_tran_bancaria.setId("com_tip_tran_bancaria");
        com_tip_tran_bancaria.setCombo("select ide_tettb,nombre_tettb,sigla_tettb from tes_tip_tran_banc");

    }

    public void verComprobante() {
        if (tab_tabla1.getTotalFilas() > 0) {
            Map parametros = new HashMap();
            parametros.put("ide_cnccc", Long.parseLong(tab_tabla1.getValor(tab_tabla1.getFilaActual(), "asiento")));
            parametros.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
            parametros.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
            vpdf_ver.setVisualizarPDF("rep_contabilidad/rep_comprobante_contabilidad.jasper", parametros);
            vpdf_ver.dibujar();
            utilitario.addUpdate("vpdf_ver");
        } else {
            utilitario.agregarMensajeInfo("No hay ningun comprobante seleccionado", "");
        }
    }

    public String getTipoTransaccion(String ide_tecba, String ide_cnlap) {
        TablaGenerica tab_tipo_cuenta = utilitario.consultar("select * from tes_cuenta_banco where ide_tecba=" + ide_tecba);
        if (tab_tipo_cuenta.getTotalFilas() > 0) {
            if (tab_tipo_cuenta.getValor(0, "ide_tetcb") != null && !tab_tipo_cuenta.getValor(0, "ide_tetcb").isEmpty()) {
                // 6 -tipo de cuenta virtual "cajas"
                if (tab_tipo_cuenta.getValor(0, "ide_tetcb").equals("6")) {
                    if (ide_cnlap.equals(utilitario.getVariable("p_con_lugar_debe"))) {
                        return utilitario.getVariable("p_tes_tran_deposito_caja");
                    } else {
                        return utilitario.getVariable("p_tes_tran_retiro_caja");
                    }
                } else {
                    if (ide_cnlap.equals(utilitario.getVariable("p_con_lugar_debe"))) {
                        return utilitario.getVariable("p_tes_tran_deposito");
                    } else {
                        return utilitario.getVariable("p_tes_tran_cheque");
                    }
                }
            }
        }
        return null;
    }
    Map parametro = new HashMap();

    public void aceptarComprobanteContabilidad() {
        if (via_asiento.validarComprobante()) {
            via_asiento.cerrar();
            cab_com_con.setObservacion_cnccc(via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"));
            cab_com_con.setIde_geper(via_asiento.getTab_cab_comp_cont_vasiento().getValor("ide_geper"));
            cab_com_con.setFecha_trans_cnccc(via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"));
            cab_com_con.setIde_cntcm(via_asiento.getTab_cab_comp_cont_vasiento().getValor("ide_cntcm"));
            lista_detalles.clear();
            for (int i = 0; i < via_asiento.getTab_det_comp_cont_vasiento().getTotalFilas(); i++) {
                lista_detalles.add(new cls_det_comp_cont(via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "ide_cnlap"), via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "ide_cndpc"), Double.parseDouble(via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "valor_cndcc")), via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "observacion_cndcc")));
            }
            cab_com_con.setDetalles(lista_detalles);
            String ide_cnccc = conta.generarAsientoContable(cab_com_con);
            if (ide_cnccc != null && !ide_cnccc.isEmpty()) {
                cls_bancos banco = new cls_bancos();
                banco.getTab_cab_libro_banco().limpiar();
                for (int i = 0; i < via_asiento.getTab_det_comp_cont_vasiento().getTotalFilas(); i++) {
                    String ide_tecba = banco.getParametroCuentaBanco(via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "ide_cndpc"), "ide_cndpc", "ide_tecba");
                    System.out.println("ide tecba " + ide_tecba);
                    if (ide_tecba != null) {
                        String ide_geper = via_asiento.getTab_cab_comp_cont_vasiento().getValor(0, "ide_geper");
                        String fecha_trans = via_asiento.getTab_cab_comp_cont_vasiento().getValor(0, "fecha_trans_cnccc");
                        String ide_tettb = getTipoTransaccion(ide_tecba, via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "ide_cnlap"));
                        System.out.println("ide tettb " + ide_tettb);
                        if (ide_tettb == null) {
                            ide_tettb = "";
                        }
                        double valor = Double.parseDouble(via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "valor_cndcc"));
                        String observacion = via_asiento.getTab_cab_comp_cont_vasiento().getValor(0, "observacion_cnccc");
                        String num_doc = tex_num_doc_banco.getValue() + "";
                        banco.generarVariosLibrosBancos(ide_geper, fecha_trans, ide_tettb, ide_tecba, ide_cnccc, valor, observacion, num_doc);
                    }
                }
                banco.getTab_cab_libro_banco().guardar();
                utilitario.getConexion().guardarPantalla();

                if (com_tip_tran_bancaria.getValue().equals(utilitario.getVariable("p_tes_tran_cheque"))) {
                    TablaGenerica tab_lib_banc = utilitario.consultar("select * from tes_cab_libr_banc where ide_cnccc=" + ide_cnccc);
                    parametro = new HashMap();
                    parametro.put("beneficiario", tab_lib_banc.getValor("beneficiari_teclb") + "");
                    parametro.put("monto", tab_lib_banc.getValor("valor_teclb") + "");
                    parametro.put("anio", utilitario.getAnio(tab_lib_banc.getValor("fecha_trans_teclb")) + "");
                    parametro.put("mes", utilitario.getMes(tab_lib_banc.getValor("fecha_trans_teclb")) + "");
                    parametro.put("dia", utilitario.getDia(tab_lib_banc.getValor("fecha_trans_teclb")) + "");
                    parametro.put("monto_letras", banco.agregarAsteriscosCheque(utilitario.getLetrasDolarNumero(tab_lib_banc.getValor("valor_teclb"))));
                    parametro.put("ide_cnccc", Long.parseLong(tab_lib_banc.getValor("ide_cnccc")));
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                    parametro.put("p_num_cheque", tab_lib_banc.getValor("numero_teclb") + "");
                    parametro.put("p_num_trans", tab_lib_banc.getValor("ide_teclb") + "");
                    List lis_geper = utilitario.getConexion().consultar("select identificac_geper from gen_persona where ide_geper=(select ide_geper from con_cab_comp_cont where ide_cnccc =" + tab_lib_banc.getValor("ide_cnccc") + ")");
                    if (lis_geper.size() > 0) {
                        parametro.put("p_identificacion", lis_geper.get(0) + "");
                    } else {
                        parametro.put("p_identificacion", "");
                    }
                    vpdf_ver.setVisualizarPDF("rep_bancos/rep_cheque.jasper", parametro);
                    vpdf_ver.dibujar();
                } else {
                    parametro = new HashMap();
                    parametro.put("ide_cnccc", Long.parseLong(ide_cnccc));
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                    vpdf_ver.setVisualizarPDF("rep_contabilidad/rep_comprobante_contabilidad.jasper", parametro);
                    vpdf_ver.dibujar();
                }
            }
        }

    }

    public void generarAsientoContable() {
        if (tab_tabla1.getTotalFilas() > 0) {
            conta.limpiar();
            cab_com_con = new cls_cab_comp_cont(utilitario.getVariable("p_con_tipo_comprobante_diario"), utilitario.getVariable("p_con_estado_comprobante_normal"), "0", "", utilitario.getFechaActual(), "");
            lista_detalles.clear();
            if (saldo_actual < 0) {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), aut_filtro_tipo_cuenta.getValor(), (saldo_actual + (-2 * saldo_actual)), ""));
            } else {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), aut_filtro_tipo_cuenta.getValor(), saldo_actual, ""));
            }
            cab_com_con.setDetalles(lista_detalles);
            via_asiento.getTab_cab_comp_cont_vasiento().getColumna("ide_geper").setLectura(false);
            via_asiento.getTab_cab_comp_cont_vasiento().getColumna("ide_cntcm").setVisible(true);
            via_asiento.getTab_cab_comp_cont_vasiento().getColumna("ide_cntcm").setValorDefecto(utilitario.getVariable("p_con_tipo_comprobante_egreso"));
            via_asiento.setVistaAsiento(cab_com_con);
            tex_num_doc_banco.setValue(null);
            com_tip_tran_bancaria.setValue(utilitario.getVariable("p_tes_tran_cheque"));
            Grid gri_bancos = new Grid();
            gri_bancos.setColumns(2);
            gri_bancos.getChildren().add(new Etiqueta("Num Documento Bancario: "));
            gri_bancos.getChildren().add(tex_num_doc_banco);
            gri_bancos.getChildren().add(new Etiqueta("Tipo Transaccion Bancaria: "));
            gri_bancos.getChildren().add(com_tip_tran_bancaria);
            if (via_asiento.getGri_cuerpo_vasiento().getChildren().size() > 3) {
                via_asiento.getGri_cuerpo_vasiento().getChildren().remove(3);
            }
            via_asiento.getGri_cuerpo_vasiento().getChildren().add(gri_bancos);
            via_asiento.dibujar();
            utilitario.addUpdate("via_asiento");
        }

    }

    public void cancelarDialogo() {
        if (via_asiento.isVisible()) {
            via_asiento.cerrar();
            utilitario.addUpdate("via_asiento");
        }
        //utilitario.getConexion().rollback(); *****
        utilitario.getConexion().getSqlPantalla().clear();
    }

    public void aceptarRango() {
        if (sec_calendario.isFechasValidas()) {
            sec_calendario.cerrar();
            tab_tabla1.setSql("SELECT CAB.fecha_trans_cnccc as FECHA,CAB.ide_cnccc as ASIENTO,CAB.observacion_cnccc as OBSERVACION, "
                    + "LUGAR.ide_cnlap,'' as DEBE, '' as HABER, "
                    + "(DETA.valor_cndcc * sc.signo_cnscu) as valor_cndcc,'' as SALDO,PERSO.nom_geper as BENEFICIARIO, "
                    + "CUENTA.nombre_cndpc as NOMBRE_CUENTA "
                    + "from con_cab_comp_cont CAB "
                    + "left join gen_persona PERSO on CAB.ide_geper=PERSO.ide_geper "
                    + "inner join  con_det_comp_cont DETA on CAB.ide_cnccc=DETA.ide_cnccc "
                    + "inner join con_det_plan_cuen CUENTA on  CUENTA.ide_cndpc = DETA.ide_cndpc "
                    + "inner join con_lugar_aplicac LUGAR on  LUGAR.ide_cnlap=DETA.ide_cnlap "
                    + "inner join con_tipo_cuenta tc on CUENTA.ide_cntcu=tc.ide_cntcu "
                    + "inner join con_signo_cuenta sc on tc.ide_cntcu=sc.ide_cntcu and DETA.ide_cnlap=sc.ide_cnlap "
                    + "WHERE CUENTA.ide_cndpc=" + aut_filtro_tipo_cuenta.getValor() + " and fecha_trans_cnccc BETWEEN '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "' "
                    + "and ide_cneco in (" + utilitario.getVariable("p_con_estado_comp_inicial") + "," + utilitario.getVariable("p_con_estado_comprobante_normal") + "," + utilitario.getVariable("p_con_estado_comp_final") + ") "
                    + "and cab.ide_sucu=" + utilitario.getVariable("ide_sucu") + " ORDER BY CAB.fecha_trans_cnccc,cab.ide_cnccc asc");
            tab_tabla1.ejecutarSql();
            utilitario.addUpdate("sec_calendario,tab_tabla1");
            cls_contabilidad conta = new cls_contabilidad();
            double saldo_anterior = conta.obtenerSaldoInicialCuenta(aut_filtro_tipo_cuenta.getValor(), sec_calendario.getFecha1String());
            saldo_inicial = saldo_anterior;
            cargarTitulo(aut_filtro_tipo_cuenta.getValor(), saldo_anterior + "");
            double saldo_actual = 0;
            for (int i = 0; i < tab_tabla1.getTotalFilas(); i++) {
                if (tab_tabla1.getValor(i, "ide_cnlap").equals(utilitario.getVariable("p_con_lugar_debe"))) {
                    tab_tabla1.setValor(i, "debe", tab_tabla1.getValor(i, "valor_cndcc"));
                } else {
                    tab_tabla1.setValor(i, "haber", tab_tabla1.getValor(i, "valor_cndcc"));
                }
                saldo_actual = saldo_anterior + Double.parseDouble(tab_tabla1.getValor(i, "valor_cndcc"));
                tab_tabla1.setValor(i, "saldo", utilitario.getFormatoNumero(saldo_actual, 3));
                saldo_anterior = saldo_actual;
            }
            if (tab_tabla1.getTotalFilas() == 0) {
                utilitario.agregarMensajeInfo("Atencion", "No existe Movimientos con esta Cuenta ");
                tex_saldo_cant.limpiar();
                tex_saldo_valor.limpiar();
                utilitario.addUpdate("div_division");
            }
            utilitario.addUpdate("tab_tabla1");
            calcularTotal();
        } else {
            utilitario.agregarMensajeInfo("Fechas no válidas", "");
        }
    }

    @Override
    public void abrirRangoFecha() {
        if (aut_filtro_tipo_cuenta.getValue() != null) {
            sec_calendario.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una cuenta contable", "");
        }
    }

    public String retornar_mes_letras(int mes) {
        String mes1 = "";
        if (mes == 1) {
            mes1 = "Enero";
        }
        if (mes == 2) {
            mes1 = "Febrero";
        }
        if (mes == 3) {
            mes1 = "Marzo";
        }
        if (mes == 4) {
            mes1 = "Abril";
        }
        if (mes == 5) {
            mes1 = "Mayo";
        }
        if (mes == 6) {
            mes1 = "Junio";
        }
        if (mes == 7) {
            mes1 = "Julio";
        }
        if (mes == 8) {
            mes1 = "Agosto";
        }
        if (mes == 9) {
            mes1 = "Septiembre";
        }
        if (mes == 10) {
            mes1 = "Octubre";
        }
        if (mes == 11) {
            mes1 = "Noviembre";
        }
        if (mes == 12) {
            mes1 = "Diciembre";
        }
        return mes1;
    }

    public String getFormatoFecha(String fecha) {
        String mes = retornar_mes_letras(utilitario.getMes(fecha));
        String dia = utilitario.getDia(fecha) + "";
        String anio = utilitario.getAnio(fecha) + "";
        String fecha_formato = dia + " de " + mes + " del " + anio;
        return fecha_formato;
    }

    public void cargarTitulo(String cuenta, String saldo_anterior) {
        eti_rango_fechas.setValue("Desde: " + getFormatoFecha(sec_calendario.getFecha1String()) + "    ----     Hasta: " + getFormatoFecha(sec_calendario.getFecha2String()));
        eti_saldo_anterior.setValue("SALDO ANTERIOR: " + saldo_anterior);
        TablaGenerica tab_plan_cuenta = utilitario.consultar("select * from con_det_plan_cuen where ide_cndpc=" + cuenta);
        if (tab_plan_cuenta.getTotalFilas() > 0) {
            try {
                eti_cuenta.setValue("CUENTA CONTABLE: " + tab_plan_cuenta.getValor(0, "codig_recur_cndpc") + " " + tab_plan_cuenta.getValor(0, "nombre_cndpc"));
            } catch (Exception e) {
            }
        }
        utilitario.addUpdate("gri_titulo");
    }
    double saldo_inicial = 0;

    public void filtrar_tipo_cuenta(SelectEvent evt) {
        aut_filtro_tipo_cuenta.onSelect(evt);
        tipo_cuenta = aut_filtro_tipo_cuenta.getValor();
        tab_tabla1.setSql("SELECT CAB.fecha_trans_cnccc as FECHA,CAB.ide_cnccc as ASIENTO,CAB.observacion_cnccc as OBSERVACION, "
                + "LUGAR.ide_cnlap,'' as DEBE, '' as HABER, "
                + "(DETA.valor_cndcc * sc.signo_cnscu) as valor_cndcc,'' as SALDO,PERSO.nom_geper as BENEFICIARIO, "
                + "CUENTA.nombre_cndpc as NOMBRE_CUENTA "
                + "from con_cab_comp_cont CAB "
                + "left join gen_persona PERSO on CAB.ide_geper=PERSO.ide_geper "
                + "inner join  con_det_comp_cont DETA on CAB.ide_cnccc=DETA.ide_cnccc "
                + "inner join con_det_plan_cuen CUENTA on  CUENTA.ide_cndpc = DETA.ide_cndpc "
                + "inner join con_lugar_aplicac LUGAR on  LUGAR.ide_cnlap=DETA.ide_cnlap "
                + "inner join con_tipo_cuenta tc on CUENTA.ide_cntcu=tc.ide_cntcu "
                + "inner join con_signo_cuenta sc on tc.ide_cntcu=sc.ide_cntcu and DETA.ide_cnlap=sc.ide_cnlap "
                + "WHERE CUENTA.ide_cndpc=" + aut_filtro_tipo_cuenta.getValor() + " "
                + "and fecha_trans_cnccc BETWEEN '" + sec_calendario.getFecha1String() + "' "
                + "and '" + sec_calendario.getFecha2String() + "' "
                + "and ide_cneco in (" + utilitario.getVariable("p_con_estado_comp_inicial") + "," + utilitario.getVariable("p_con_estado_comprobante_normal") + "," + utilitario.getVariable("p_con_estado_comp_final") + ") "
                + "and cab.ide_sucu=" + utilitario.getVariable("ide_sucu") + " ORDER BY CAB.fecha_trans_cnccc,cab.ide_cnccc asc");
        tab_tabla1.ejecutarSql();
        utilitario.addUpdate("tab_tabla1");
        cls_contabilidad conta = new cls_contabilidad();
        double saldo_anterior = conta.obtenerSaldoInicialCuenta(aut_filtro_tipo_cuenta.getValor(), sec_calendario.getFecha1String());
        saldo_inicial = saldo_anterior;
        cargarTitulo(aut_filtro_tipo_cuenta.getValor(), saldo_anterior + "");
        double saldo_actual = 0;

        for (int i = 0; i < tab_tabla1.getTotalFilas(); i++) {
            if (tab_tabla1.getValor(i, "ide_cnlap").equals(utilitario.getVariable("p_con_lugar_debe"))) {
                tab_tabla1.setValor(i, "debe", tab_tabla1.getValor(i, "valor_cndcc"));
            } else {
                tab_tabla1.setValor(i, "haber", tab_tabla1.getValor(i, "valor_cndcc"));
            }
            saldo_actual = saldo_anterior + Double.parseDouble(tab_tabla1.getValor(i, "valor_cndcc"));
            tab_tabla1.setValor(i, "saldo", utilitario.getFormatoNumero(saldo_actual, 3));

            saldo_anterior = saldo_actual;
        }
        if (tab_tabla1.getTotalFilas() == 0) {
            utilitario.agregarMensajeInfo("Atencion", "No existe Movimientos con esta Cuenta ");
            tex_saldo_cant.limpiar();
            tex_saldo_valor.limpiar();
            utilitario.addUpdate("div_division");
        }
        utilitario.addUpdate("tab_tabla1");
        calcularTotal();
    }
    double saldo_actual = 0;

    public void calcularTotal() {
        double dou_debe = 0;
        double dou_haber = 0;
        for (int i = 0; i < tab_tabla1.getTotalFilas(); i++) {
            try {
                if (tab_tabla1.getValor(i, "debe") != null && !tab_tabla1.getValor(i, "debe").isEmpty()) {
                    dou_debe += Double.parseDouble(tab_tabla1.getValor(i, "debe"));
                }
                if (tab_tabla1.getValor(i, "haber") != null && !tab_tabla1.getValor(i, "haber").isEmpty()) {
                    dou_haber += Double.parseDouble(tab_tabla1.getValor(i, "haber"));
                }
            } catch (Exception e) {
            }
        }
        tex_saldo_debe.setValue("" + utilitario.getFormatoNumero(dou_debe));
        tex_saldo_haber.setValue("" + utilitario.getFormatoNumero(dou_haber));

        double dou_diferencia = Double.parseDouble(utilitario.getFormatoNumero(saldo_inicial)) + Double.parseDouble(utilitario.getFormatoNumero(dou_debe)) + Double.parseDouble(utilitario.getFormatoNumero(dou_haber));
        saldo_actual = dou_diferencia;
        tex_saldo_total.setValue("" + utilitario.getFormatoNumero(dou_diferencia));
        tex_saldo_total.setStyle("font-size: 14px;font-weight: bold");
        utilitario.addUpdate("gri_totales");
    }

    public void limpiar() {
        aut_filtro_tipo_cuenta.limpiar();
        tab_tabla1.limpiar();
        tex_saldo_debe.limpiar();
        tex_saldo_haber.limpiar();
        tex_saldo_total.limpiar();
        eti_cuenta.setValue("CUENTA CONTABLE:");
        eti_rango_fechas.setValue("DESDE: 0/0/0       HASTA: 0/0/0 ");
        eti_saldo_anterior.setValue("SALDO ANTERIOR: ");

        utilitario.addUpdate("aut_filtro_tipo_cuenta,tab_tabla1,div_division,gri_totales,gri_titulo");
    }

    @Override
    public void insertar() {
        if (via_asiento.getTab_det_comp_cont_vasiento().isFocus()) {
            via_asiento.insertar();
        }

    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
        if (via_asiento.getTab_det_comp_cont_vasiento().isFocus()) {
            via_asiento.eliminar();
        }
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public AutoCompletar getAut_filtro_tipo_cuenta() {
        return aut_filtro_tipo_cuenta;
    }

    public void setAut_filtro_tipo_cuenta(AutoCompletar aut_filtro_tipo_cuenta) {
        this.aut_filtro_tipo_cuenta = aut_filtro_tipo_cuenta;
    }

    public SeleccionCalendario getSec_calendario() {
        return sec_calendario;
    }

    public void setSec_calendario(SeleccionCalendario sec_calendario) {
        this.sec_calendario = sec_calendario;
    }

    public VistaAsiento getVia_asiento() {
        return via_asiento;
    }

    public void setVia_asiento(VistaAsiento via_asiento) {
        this.via_asiento = via_asiento;
    }

    public VisualizarPDF getVpdf_ver() {
        return vpdf_ver;
    }

    public void setVpdf_ver(VisualizarPDF vpdf_ver) {
        this.vpdf_ver = vpdf_ver;
    }

}

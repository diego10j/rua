/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_contabilidad;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 *
 */
public class pre_comprobante_conta_editable extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Division div_division = new Division();
    private Boton bot_copiar = new Boton();
    private Combo com_tipo_comprobante = new Combo();
    private SeleccionCalendario sec_calendario = new SeleccionCalendario();
    private Etiqueta eti_suma_debe = new Etiqueta();
    private Etiqueta eti_suma_haber = new Etiqueta();
    private Etiqueta eti_suma_diferencia = new Etiqueta();
    private Boton bot_ver = new Boton();
    private VisualizarPDF vpdf_ver = new VisualizarPDF();
    //Parametros del sistema
    private String p_con_lugar_debe = utilitario.getVariable("p_con_lugar_debe");
    private String p_con_lugar_haber = utilitario.getVariable("p_con_lugar_haber");
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private SeleccionTabla sel_tab = new SeleccionTabla();
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();
    cls_contabilidad con = new cls_contabilidad();
    private Texto tex_num_transaccion = new Texto();
    private Boton bot_buscar_transaccion = new Boton();

    public pre_comprobante_conta_editable() {
        //Recuperar el plan de cuentas activo
        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");

        if (lis_plan != null && !lis_plan.isEmpty()) {
            bar_botones.agregarReporte();

            bar_botones.getBot_inicio().setMetodo("inicio");
            bar_botones.getBot_fin().setMetodo("fin");
            bar_botones.getBot_atras().setMetodo("atras");
            bar_botones.getBot_siguiente().setMetodo("siguiente");
            bar_botones.agregarCalendario();

            tex_num_transaccion.setId("tex_num_transaccion");
            tex_num_transaccion.setSoloEnteros();
            bot_buscar_transaccion.setTitle("Buscar Transaccion");
            bot_buscar_transaccion.setIcon("ui-icon-search");
            bot_buscar_transaccion.setMetodo("buscarTransaccion");
            bar_botones.agregarComponente(new Etiqueta("Num Transaccion: "));
            bar_botones.agregarComponente(tex_num_transaccion);
            bar_botones.agregarBoton(bot_buscar_transaccion);

            Etiqueta eti = new Etiqueta();
            eti.setValue("Tipo de Comprobante :");
            bar_botones.agregarComponente(eti);

            com_tipo_comprobante.setId("com_tipo_comprobante");
            com_tipo_comprobante.setTitle("Tipo de Comprobate");
            com_tipo_comprobante.setCombo("select ide_cntcm,nombre_cntcm from con_tipo_comproba where ide_empr=" + utilitario.getVariable("ide_empr"));
            com_tipo_comprobante.setMetodo("seleccionTipoComprobante");
            bar_botones.agregarComponente(com_tipo_comprobante);

            Espacio esp = new Espacio();
            esp.setHeight("1");
            esp.setWidth("20");
            bar_botones.agregarComponente(esp);

            bot_ver.setValue("Ver Comprobante");
            bot_ver.setMetodo("verComprobante");
            bot_ver.setUpdate("vpdf_ver");
            //       bar_botones.agregarBoton(bot_ver);

            bot_copiar.setValue("Copiar");
            bot_copiar.setMetodo("copiarComprobante");

            bot_copiar.setIcon("ui-icon-copy");
            bar_botones.agregarBoton(bot_copiar);

            //seleccion de las cuentas para reporte libro mayor

            sel_tab.setId("sel_tab");
            sel_tab.setSeleccionTabla("SELECT ide_cndpc,codig_recur_cndpc,nombre_cndpc FROM con_det_plan_cuen WHERE ide_cncpc=(SELECT ide_cncpc FROM con_cab_plan_cuen WHERE activo_cncpc is TRUE) AND ide_empr=" + utilitario.getVariable("ide_empr") + " AND nivel_cndpc='HIJO'", "ide_cndpc");
            sel_tab.getTab_seleccion().getColumna("nombre_cndpc").setFiltro(true);
            gru_pantalla.getChildren().add(sel_tab);

            rep_reporte.setId("rep_reporte");
            rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");

            sel_tab.getBot_aceptar().setMetodo("aceptarReporte");
            sel_tab.getBot_aceptar().setUpdate("sel_tab,sec_calendario ");


            gru_pantalla.getChildren().add(rep_reporte);

            sec_rango_reporte.setId("sec_rango_reporte");
            sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");
            sec_rango_reporte.setMultiple(false);
            gru_pantalla.getChildren().add(sec_rango_reporte);

            sel_rep.setId("sel_rep");
            gru_pantalla.getChildren().add(sel_rep);



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

            div_division.setId("div_division");
            Division div_detalle = new Division();
            div_detalle.setFooter(pat_panel2, gri_totales, "85%");

            div_division.dividir2(pat_panel1, div_detalle, "40%", "H");

            gru_pantalla.getChildren().add(bar_botones);
            gru_pantalla.getChildren().add(div_division);

            sec_calendario.setId("sec_calendario");
            //por defecto friltra un mes
            sec_calendario.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -31));
            sec_calendario.setFecha2(utilitario.getDate());
            gru_pantalla.getChildren().add(sec_calendario);
            sec_calendario.getBot_aceptar().setMetodo("aceptarRango");
            vpdf_ver.setTitle("Comprobante de Contabilidad");
            vpdf_ver.setId("vpdf_ver");
            agregarComponente(vpdf_ver);


        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }
    }

    public void buscarTransaccion() {

        if (tex_num_transaccion.getValue() != null && !tex_num_transaccion.getValue().toString().isEmpty()) {

            tab_tabla1.setCondicion("fecha_trans_cnccc >='" + con.obtenerFechaInicialPeriodoActivo() + "' and ide_cnccc=" + tex_num_transaccion.getValue());
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            if (tab_tabla1.getTotalFilas() > 0) {
                if (tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cntcm") != null && !tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cntcm").isEmpty()) {
                    com_tipo_comprobante.setValue(tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cntcm"));
                }
                calcularTotal();
            } else {
                utilitario.agregarMensajeInfo("Atencion", "El numero de transaccion no existe");
                com_tipo_comprobante.setValue(null);
            }
            utilitario.addUpdate("tab_tabla1,tab_tabla2,gri_totales,com_tipo_comprobante");
        }
    }

    public void inicio() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.inicio();
        }
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    public void fin() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.fin();
        }
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    public void atras() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.atras();
        }
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    public void siguiente() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.siguiente();
        }
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    public void copiarComprobante() {
        if (!tab_tabla1.isFilaInsertada()) {

            TablaGenerica tab_cabecera = utilitario.consultar("SELECT * From con_cab_comp_cont where ide_cnccc=" + tab_tabla1.getValorSeleccionado());
            TablaGenerica tab_detalle = utilitario.consultar("SELECT * From con_det_comp_cont where ide_cnccc=" + tab_tabla1.getValorSeleccionado());

            tab_tabla1.insertar();
            tab_tabla1.setValor("IDE_MODU", tab_cabecera.getValor("IDE_MODU"));
            tab_tabla1.setValor("IDE_GEPER", tab_cabecera.getValor("IDE_GEPER"));
            tab_tabla1.setValor("OBSERVACION_CNCCC", tab_cabecera.getValor("OBSERVACION_CNCCC"));

            for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
                tab_tabla2.insertar();
                tab_tabla2.setValor("IDE_CNLAP", tab_detalle.getValor(i, "IDE_CNLAP"));
                tab_tabla2.setValor("IDE_CNDPC", tab_detalle.getValor(i, "IDE_CNDPC"));
                tab_tabla2.setValor("VALOR_CNDCC", tab_detalle.getValor(i, "VALOR_CNDCC"));
                tab_tabla2.setValor("OBSERVACION_CNDCC", tab_detalle.getValor(i, "OBSERVACION_CNDCC"));
            }
            calcularTotal();
            utilitario.addUpdate("tab_tabla1,tab_tabla2,gri_totales");
        } else {
            utilitario.agregarMensajeInfo("No se puede copiar el comprobante de contabilidad", "El comprobante seleccionado no se encuentra grabado");
        }
    }

    public void verComprobante() {
        if (tab_tabla1.getValorSeleccionado() != null) {
            if (!tab_tabla1.isFilaInsertada()) {
                Map parametros = new HashMap();
                parametros.put("ide_cnccc", Long.parseLong(tab_tabla1.getValorSeleccionado()));
                parametros.put("ide_cnlap_debe", p_con_lugar_debe);
                parametros.put("ide_cnlap_haber", p_con_lugar_haber);
                vpdf_ver.setVisualizarPDF("rep_contabilidad/rep_comprobante_contabilidad.jasper", parametros);
                vpdf_ver.dibujar();
            } else {
                utilitario.agregarMensajeInfo("Debe guardar el comprobante", "");
            }

        } else {
            utilitario.agregarMensajeInfo("No hay ningun comprobante seleccionado", "");
        }
    }

    public void aceptarRango() {
        if (sec_calendario.isFechasValidas()) {
            sec_calendario.cerrar();
            tab_tabla1.setCondicion("fecha_trans_cnccc between '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "' and ide_cntcm=" + com_tipo_comprobante.getValue());
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            utilitario.addUpdate("sec_calendario");
            calcularTotal();
            utilitario.addUpdate("gri_totales");
        } else {
            utilitario.agregarMensajeInfo("Fechas no válidas", "");
        }
    }

    @Override
    public void abrirRangoFecha() {
        if (com_tipo_comprobante.getValue() != null) {
            sec_calendario.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un tipo de comprobante", "");
        }

    }

    public void seleccionTipoComprobante() {
        if (com_tipo_comprobante.getValue() != null) {
            tex_num_transaccion.setValue(null);
            tab_tabla1.setCondicion("fecha_trans_cnccc between '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "' and ide_cntcm=" + com_tipo_comprobante.getValue());
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
        } else {
            tex_num_transaccion.setValue(null);
            tab_tabla1.setCondicion("ide_cnccc=-1");
            tab_tabla1.ejecutarSql();
            tab_tabla2.setCondicion("ide_cndcc=-1");
            tab_tabla2.ejecutarSql();
        }
        calcularTotal();
        utilitario.addUpdate("gri_totales,tex_num_transaccion");
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
            TablaGenerica tab_persona=utilitario.consultar("select * from gen_persona where ide_geper="+tab_tabla1.getValor("ide_geper"));
            utilitario.getConexion().agregarSqlPantalla("update tes_cab_libr_banc set beneficiari_teclb='"+tab_persona.getValor("nom_geper") +"' where ide_cnccc="+tab_tabla1.getValor("ide_cnccc"));
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

    private String generarSecuencial() {
        //GENERA el número secuencial de la cabecera del comprobante
        String str_numero = null;
        String str_fecha = tab_tabla1.getValor("FECHA_TRANS_CNCCC");
        String str_ano = utilitario.getAnio(str_fecha) + "";
        String str_mes = utilitario.getMes(str_fecha) + "";
        String str_ide_sucu = utilitario.getVariable("ide_sucu");
        //SELECCIONA EL MAXIMO SEGUN EL MES Y EL AÑO 
        TablaGenerica tab_max = utilitario.consultar("SELECT count(NUMERO_CNCCC) as cod,max(cast( substr(NUMERO_CNCCC,7) as NUMERIC)) AS MAXIMO FROM CON_CAB_COMP_CONT WHERE extract(year from FECHA_TRANS_CNCCC) ='" + str_ano + "' AND extract(month from FECHA_TRANS_CNCCC) ='" + str_mes + "' AND IDE_SUCU=" + str_ide_sucu);

        String str_maximo = "0";
        if (tab_max.getTotalFilas() > 0) {
            str_maximo = tab_max.getValor("MAXIMO");
            if (str_maximo == null || str_maximo.isEmpty()) {
                str_maximo = "0";
            }
            int lint_siguiente = (Integer.parseInt(str_maximo)) + 1;
            str_maximo = lint_siguiente + "";
        }
        str_maximo = utilitario.generarCero(8 - str_maximo.length()) + str_maximo;
        str_numero = str_ano + str_mes + str_ide_sucu + str_maximo;
        return str_numero;
    }

    @Override
    public void eliminar() {
        if (tab_tabla2.isFocus()) {
            tab_tabla2.eliminar();
        }
    }

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }
    Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
//Se ejecuta cuando se selecciona un reporte de la lista        
//        if (rep_reporte.getReporteSelecionado().equals("Libro Diario")) {
//            if (rep_reporte.isVisible()) {
//                parametro = new HashMap();
//                rep_reporte.cerrar();
//                sec_rango_reporte.setMultiple(true);
//                sec_rango_reporte.dibujar();
//                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
//
//            } else if (sec_rango_reporte.isVisible()) {
//                String estado = "" + utilitario.getVariable("p_con_estado_comprobante_normal") + "," + utilitario.getVariable("p_con_estado_comp_inicial") + "," + utilitario.getVariable("p_con_estado_comp_final");
//                parametro.put("fecha_inicio", sec_rango_reporte.getFecha1());
//                parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
//                parametro.put("ide_cneco", estado);
//                parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
//                parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
//                sec_rango_reporte.cerrar();
//                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
//                sel_rep.dibujar();
//                utilitario.addUpdate("sel_rep,sec_rango_reporte");
//            }
//
//
//
//        } else if (rep_reporte.getReporteSelecionado().equals("Balance General")) {
//            if (rep_reporte.isVisible()) {
//                parametro = new HashMap();
//                rep_reporte.cerrar();
//                sec_rango_reporte.setMultiple(false);
//                sec_rango_reporte.dibujar();
//                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
//
//            } else {
//                if (sec_rango_reporte.isVisible()) {
//                    String fecha_fin = sec_rango_reporte.getFecha1String();
//                    System.out.println("fecha fin " + fecha_fin);
//                    sec_rango_reporte.cerrar();
//
//                    parametro.put("p_activo", utilitario.getVariable("p_con_tipo_cuenta_activo"));
//                    parametro.put("p_pasivo", utilitario.getVariable("p_con_tipo_cuenta_pasivo"));
//                    parametro.put("p_patrimonio", utilitario.getVariable("p_con_tipo_cuenta_patrimonio"));
//                    Tabla tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
//                    if (tab_datos.getTotalFilas() > 0) {
//                        parametro.put("logo", tab_datos.getValor(0, "logo_empr"));
//                        parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
//                        parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
//                        parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
//                        parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
//                        parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));
//
//                    }
//                    Tabla tab_balance = con.generarBalanceGeneral(fecha_fin,7);
//                    if (tab_balance.getTotalFilas() > 0) {
//                        List lis_totales = con.obtenerTotalesBalanceGeneral(fecha_fin);
//                        double tot_activo = Double.parseDouble(lis_totales.get(0) + "");
//                        double tot_pasivo = Double.parseDouble(lis_totales.get(1) + "");
//                        double tot_patrimonio = Double.parseDouble(lis_totales.get(2) + "");
//                        double utilidad_perdida = tot_activo - (tot_pasivo + tot_patrimonio);
//                        double total = tot_pasivo + tot_patrimonio + utilidad_perdida;
//
//                        parametro.put("p_tot_activo", tot_activo);
//                        parametro.put("p_total", total);
//                        parametro.put("p_utilidad_perdida", utilidad_perdida);
//                    }
//                    ReporteDataSource data = new ReporteDataSource(tab_balance);
//                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath(), data);
//                    sel_rep.dibujar();
//                    utilitario.addUpdate("sel_rep,sec_rango_reporte");
//
//                }
//            }
//
//        } else if (rep_reporte.getReporteSelecionado().equals("Estado de Resultados")) {
//            if (rep_reporte.isVisible()) {
//                parametro = new HashMap();
//                rep_reporte.cerrar();
//                sec_rango_reporte.setMultiple(false);
//                sec_rango_reporte.dibujar();
//                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
//
//            } else {
//                if (sec_rango_reporte.isVisible()) {
//                    String fecha_fin = sec_rango_reporte.getFecha1String();
//                    System.out.println("fecha fin " + fecha_fin);
//                    sec_rango_reporte.cerrar();
//
//                    parametro.put("p_ingresos", utilitario.getVariable("p_con_tipo_cuenta_ingresos"));
//                    parametro.put("p_gastos", utilitario.getVariable("p_con_tipo_cuenta_gastos"));
//                    parametro.put("p_costos", utilitario.getVariable("p_con_tipo_cuenta_costos"));
//                    Tabla tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
//                    if (tab_datos.getTotalFilas() > 0) {
//                        parametro.put("logo", tab_datos.getValor(0, "logo_empr"));
//                        parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
//                        parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
//                        parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
//                        parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
//                        parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));
//                    }
//                    Tabla tab_estado = con.generarEstadoResultados(fecha_fin,5);
//                    if (tab_estado.getTotalFilas() > 0) {
//                        List lis_totales = con.obtenerTotalesEstadoResultados(fecha_fin);
//                        double tot_ingresos = Double.parseDouble(lis_totales.get(0) + "");
//                        double tot_gastos = Double.parseDouble(lis_totales.get(1) + "");
//                        double tot_costos = Double.parseDouble(lis_totales.get(2) + "");
//                        double utilidad_perdida = tot_ingresos - (tot_gastos + tot_costos);
//                        parametro.put("p_utilidad", utilidad_perdida);
//                    }
//                    ReporteDataSource data = new ReporteDataSource(tab_estado);
//                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath(), data);
//                    sel_rep.dibujar();
//                    utilitario.addUpdate("sel_rep,sec_rango_reporte");
//
//                }
//            }
//
//        } else if (rep_reporte.getReporteSelecionado().equals("Libro Mayor")) {
//            if (rep_reporte.isVisible()) {
//                parametro = new HashMap();
//                rep_reporte.cerrar();
//                sel_tab.dibujar();
//                utilitario.addUpdate("rep_reporte,sel_tab");
//            } else {
//                if (sel_tab.isVisible()) {
//                    sel_tab.cerrar();
//                    parametro.put("ide_cndpc", sel_tab.getSeleccionados());//lista sel                     
//                    String estado = "" + utilitario.getVariable("p_con_estado_comprobante_normal") + "," + utilitario.getVariable("p_con_estado_comp_inicial") + "," + utilitario.getVariable("p_con_estado_comp_final");
//                    parametro.put("ide_cneco", estado);
//                    sec_rango_reporte.setMultiple(true);
//                    sec_rango_reporte.dibujar();
//                    utilitario.addUpdate("sel_tab,sec_rango_reporte");
//                } else if (sec_rango_reporte.isVisible()) {
//                    parametro.put("fecha_inicio", sec_rango_reporte.getFecha1());
//                    parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
//                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
//                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
//                    sec_rango_reporte.cerrar();
//                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
//                    sel_rep.dibujar();
//                    utilitario.addUpdate("sel_rep,sec_rango_reporte");
//                }
//            }
//        } else if (rep_reporte.getReporteSelecionado().equals("Balance de Comprobacion")) {
//            if (rep_reporte.isVisible()) {
//                parametro = new HashMap();
//                rep_reporte.cerrar();
//                sec_rango_reporte.setMultiple(false);
//                sec_rango_reporte.dibujar();
//                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
//
//            } else {
//                if (sec_rango_reporte.isVisible()) {
//                    String fecha_fin = sec_rango_reporte.getFecha1String();
//                    System.out.println("fecha fin " + fecha_fin);
//                    sec_rango_reporte.cerrar();
//
//                    Tabla tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
//                    if (tab_datos.getTotalFilas() > 0) {
//                        parametro.put("logo", tab_datos.getValor(0, "logo_empr"));
//                        parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
//                        parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
//                        parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
//                        parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
//                        parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));
//                    }
//                    Tabla tab_bal = con.generarBalanceComprobacion(fecha_fin);
//                    double suma_debe = 0;
//                    double suma_haber = 0;
//                    double suma_deudor = 0;
//                    double suma_acreedor = 0;
//                    for (int i = 0; i < tab_bal.getTotalFilas() - 1; i++) {
//                        suma_debe = Double.parseDouble(tab_bal.getValor(i, "debe")) + suma_debe;
//                        suma_haber = Double.parseDouble(tab_bal.getValor(i, "haber")) + suma_haber;
//                        suma_deudor = Double.parseDouble(tab_bal.getValor(i, "deudor")) + suma_deudor;
//                        suma_acreedor = Double.parseDouble(tab_bal.getValor(i, "acreedor")) + suma_acreedor;
//                    }
//                    parametro.put("tot_debe", suma_debe);
//                    parametro.put("tot_haber", suma_haber);
//                    parametro.put("tot_deudor", suma_deudor);
//                    parametro.put("tot_acreedor", suma_acreedor);
//                    ReporteDataSource data = new ReporteDataSource(tab_bal);
//                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath(), data);
//                    sel_rep.dibujar();
//                    utilitario.addUpdate("sel_rep,sec_rango_reporte");
//
//                }
//            }
//
//        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante Contabilidad")) {
//            if (rep_reporte.isVisible()) {
//                if (tab_tabla1.getValor("ide_cnccc") != null) {
//                    parametro = new HashMap();
//                    rep_reporte.cerrar();
//                    parametro.put("ide_cnccc", Long.parseLong(tab_tabla1.getValor("ide_cnccc")));
//                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
//                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
//                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
//                    sel_rep.dibujar();
//                    utilitario.addUpdate("rep_reporte,sel_rep");
//                } else {
//                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene compraqbante de contabilidad");
//                }
//
//
//            }
//        }
    }

    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra 
        rep_reporte.dibujar();

    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSel_rep() {
        return sel_rep;
    }

    public void setSel_rep(SeleccionFormatoReporte sel_rep) {
        this.sel_rep = sel_rep;
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

    public SeleccionCalendario getSec_calendario() {
        return sec_calendario;
    }

    public void setSec_calendario(SeleccionCalendario sec_calendario) {
        this.sec_calendario = sec_calendario;
    }

    public VisualizarPDF getVpdf_ver() {
        return vpdf_ver;
    }

    public void setVpdf_ver(VisualizarPDF vpdf_ver) {
        this.vpdf_ver = vpdf_ver;
    }

    public SeleccionTabla getSel_tab() {
        return sel_tab;
    }

    public void setSel_tab(SeleccionTabla sel_tab) {
        this.sel_tab = sel_tab;
    }

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gerencial;

import componentes.AsientoContable;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.BotonesCombo;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.ItemMenu;
import framework.componentes.Link;
import framework.componentes.ListaSeleccion;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionArbol;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.VisualizarPDF;
import framework.componentes.graficos.GraficoCartesiano;
import framework.componentes.graficos.GraficoPastel;
import framework.reportes.ReporteDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;
import paq_gerencial.ejb.ServicioGerencial;
import persistencia.Conexion;
import pkg_contabilidad.cls_contabilidad;
import servicios.contabilidad.ServicioContabilidadGeneral;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Luis Toapanta
 */
public class EstadosFinancieros extends Pantalla {

    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);
    @EJB
    private final ServicioGerencial ser_gerencial = (ServicioGerencial) utilitario.instanciarEJB(ServicioGerencial.class);

    private final MenuPanel mep_menu = new MenuPanel();
    private AutoCompletar aut_cuenta;
    private AutoCompletar aut_persona;
    private Conexion conPersistencia = new Conexion();
    private Etiqueta eti_anio_estado = new Etiqueta();
    private Etiqueta eti_obra = new Etiqueta();
    private Etiqueta eti_tip_balance = new Etiqueta();
    private Etiqueta eti_mes = new Etiqueta();
    private Etiqueta eti_estado_detalle = new Etiqueta();
    private Tabla tab_consulta;
    private Tabla tab_tabla;
    private Tabla tab_tabla3 = new Tabla();
    private Tabla tab_tabla_traspaso = new Tabla();
    private Tabla tab_tabla_traspaso_cabecera = new Tabla();
    private Tabla tab_tabla_cuenta_traspaso = new Tabla();
    private cls_contabilidad con = new cls_contabilidad();
    private GraficoCartesiano gca_utilidad;
    private GraficoPastel gpa_utilidad;
    //Consultas
    private Calendario cal_fecha_inicio;
    private Calendario cal_fecha_fin;
    private Radio rad_niveles;

    //Reportes
    private final String p_con_lugar_debe = utilitario.getVariable("p_con_lugar_debe");
    private final String p_con_lugar_haber = utilitario.getVariable("p_con_lugar_haber");
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private SeleccionTabla sel_casa_obras = new SeleccionTabla();
    private Map parametro = new HashMap();
    private String fecha_fin;
    private String fecha_inicio;
    private VisualizarPDF vipdf_mayor = new VisualizarPDF();
    private AsientoContable asc_asiento = new AsientoContable();
    private Dialogo dia_cerrar_periodo = new Dialogo();
    private Combo com_estado_anio_fiscal = new Combo();
    private Combo com_estado_mes_fiscal = new Combo();
    private Combo com_mes = new Combo();
    private Combo com_tipo_balance = new Combo();
    private Combo com_periodo;
    private Combo com_periodo_financiero;
    private Combo com_periodo_grafico;
    private ListaSeleccion lis_tipo_balance = new ListaSeleccion();
    private ListaSeleccion lis_meses = new ListaSeleccion();
    private Radio rad_nivel_inicial = new Radio();
    private Radio rad_nivel_final = new Radio();
    private Etiqueta eti_casa = new Etiqueta();
    private Dialogo dia_cerrar_balance = new Dialogo();
    private Etiqueta eti_cerrar_balance = new Etiqueta();
    String ide_gebame = "-1";
    String str_impresion = "-1";
    private Conexion conPostgres = new Conexion();

    public EstadosFinancieros() {

        conPostgres.setUnidad_persistencia("rua_gerencial");
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        
        tab_tabla_cuenta_traspaso.setId("tab_tabla_cuenta_traspaso");
        tab_tabla_cuenta_traspaso.setConexion(conPostgres);
        tab_tabla_cuenta_traspaso.setHeader("CEDULA CABECERA");
        tab_tabla_cuenta_traspaso.setSql("select ide_cndpc,codig_recur_cndpc,nombre_cndpc from con_det_plan_cuen where codig_recur_cndpc='0' ");
//        tab_tabla_cuenta_traspaso.setGenerarPrimaria(false);
        tab_tabla_cuenta_traspaso.dibujar();
        
        tab_tabla_traspaso.setId("tab_tabla_traspaso");
        tab_tabla_traspaso.setConexion(conPostgres);
        tab_tabla_traspaso.setHeader("CEDULA CABECERA");
        tab_tabla_traspaso.setTabla("ger_balance_detalle", "ide_gebade", 10);
        tab_tabla_traspaso.setGenerarPrimaria(false);
        tab_tabla_traspaso.setCondicion("ide_gebade=-1");
        tab_tabla_traspaso.dibujar();
        //agregarComponente(tab_tabla_traspaso);

        tab_tabla_traspaso_cabecera.setId("tab_tabla_traspaso_cabecera");
        tab_tabla_traspaso_cabecera.setConexion(conPostgres);
        tab_tabla_traspaso_cabecera.setHeader("¡¡¡ VERIFICAR LOS DATOS DE LA OBRA Y BALANCES A TRANSFERIR ¡¡¡");
        tab_tabla_traspaso_cabecera.setTabla("ger_balance_mensual", "ide_gebame", 11);
        tab_tabla_traspaso_cabecera.setCondicion("ide_gebame=-1");
        tab_tabla_traspaso_cabecera.getColumna("ide_gecobc").setCombo("select ide_gecobc,nombre_gerobr||' PERIODO CONTABLE: '||nom_geani from  ger_cont_balance_cabecera a, ger_obra b,gen_anio c where a.ide_gerobr =b.ide_gerobr and a.ide_geani= c.ide_geani");
        tab_tabla_traspaso_cabecera.getColumna("ide_getiba").setCombo("select ide_getiba,detalle_getiba from ger_tipo_balance");
        tab_tabla_traspaso_cabecera.getColumna("ide_gerest").setCombo("select ide_gerest,detalle_gerest from ger_estado");
        tab_tabla_traspaso_cabecera.getColumna("ide_gemes").setCombo("select ide_gemes,nombre_gemes from gen_mes");
        tab_tabla_traspaso_cabecera.getColumna("ide_gecobc").setNombreVisual("Obra");
        tab_tabla_traspaso_cabecera.getColumna("ide_getiba").setNombreVisual("Tipo Balance");
        tab_tabla_traspaso_cabecera.getColumna("ide_gerest").setNombreVisual("Estado");
        tab_tabla_traspaso_cabecera.getColumna("ide_gemes").setNombreVisual("Mes");
        tab_tabla_traspaso_cabecera.getColumna("observacion_gebame").setNombreVisual("Detalle");
        tab_tabla_traspaso_cabecera.getColumna("responsable_gebame").setVisible(false);
        tab_tabla_traspaso_cabecera.getColumna("fecha_apert_gebame").setVisible(false);
        tab_tabla_traspaso_cabecera.getColumna("fecha_cierre_gebame").setVisible(false);
        tab_tabla_traspaso_cabecera.getColumna("ide_gecobc").setLectura(true);
        tab_tabla_traspaso_cabecera.getColumna("ide_getiba").setLectura(true);
        tab_tabla_traspaso_cabecera.getColumna("ide_gerest").setLectura(true);
        tab_tabla_traspaso_cabecera.getColumna("observacion_gebame").setLectura(true);
        tab_tabla_traspaso_cabecera.getColumna("ide_gemes").setLectura(true);
        tab_tabla_traspaso_cabecera.setTipoFormulario(true);
        tab_tabla_traspaso_cabecera.getGrid().setColumns(4);
        tab_tabla_traspaso_cabecera.dibujar();

        bar_botones.limpiar();

        // Etiquetas de la obra
        eti_anio_estado.setStyle("font-size:14px;font-weight: bold;color:green");
        eti_obra.setStyle("font-size:14px;font-weight: bold;color:green");
        eti_tip_balance.setStyle("font-size:14px;font-weight: bold;color:green");
        eti_cerrar_balance.setStyle("font-size:18px;font-weight: bold;color:red");
        eti_mes.setStyle("font-size:14px;font-weight: bold;color:green");
        eti_estado_detalle.setStyle("font-size:14px;font-weight: bold;color:green");

        mep_menu.setMenuPanel("ESTADOS FINANCIEROS CONTABLES CONSOLIDADOS", "20%");
        mep_menu.agregarItem("Transferir Balance", "dibujarTransferirBalance", "ui-icon-bookmark"); //1
        mep_menu.agregarItem("Estados Financieros", "dibujarEstadosFinancieros", "ui-icon-bookmark");//2
        mep_menu.agregarSubMenu("GRAFICOS");
        mep_menu.agregarItem("Gráfico Balance", "dibujarGrafico", "ui-icon-clock");
        agregarComponente(mep_menu);
        tipoImpresion();//Inicializo el tipo de impresión

        vipdf_mayor.setId("vipdf_mayor");
        agregarComponente(vipdf_mayor);
        //SELECCION DE OBRAS
        sel_casa_obras.setId("sel_casa_obras");
        sel_casa_obras.setTitle("SELECCIONE DE OBRAS");
        sel_casa_obras.setSeleccionTabla(ser_gerencial.getCasaObraPeriodoFiscal("-1", "-1", "-1", "-1"), "ide_gecobc");
        sel_casa_obras.getBot_aceptar().setMetodo("actualizarGraficos");
        //
        agregarComponente(sel_casa_obras);

        //Dialogo para importar 
        dia_cerrar_balance.setId("dia_cerrar_balance");
        dia_cerrar_balance.setTitle("CONFIRMACIÓN DE BLOQUEO Y TRANSFERECIA DE BALANCES");
        //dia_importar.setPosition("left");
        dia_cerrar_balance.setWidth("50%");
        dia_cerrar_balance.setHeight("50%");
        eti_cerrar_balance.setValue("Está seguro de bloquear y cerrar el presente balance");
        //dia_cerrar_balance.setDialogo(eti_cerrar_balance);
        dia_cerrar_balance.getBot_aceptar().setMetodo("bloquearTransferir");
        agregarComponente(dia_cerrar_balance);
    }

    public void dibujarGrafico() {
        Grupo grupo = new Grupo();

        gca_utilidad = new GraficoCartesiano();
        gca_utilidad.setId("gca_utilidad");

        gpa_utilidad = new GraficoPastel();
        gpa_utilidad.setId("gpa_utilidad");
        gpa_utilidad.setShowDataLabels(true);
        gpa_utilidad.setStyle("width:300px;");

        com_periodo_grafico = new Combo();
        com_periodo_grafico.setCombo(ser_gerencial.getAnio("-1", "-1", "-1"));
        com_periodo_grafico.eliminarVacio();

        Boton bot_imprimir_cuentas = new Boton();
        bot_imprimir_cuentas.setIcon("ui-icon-print");
        bot_imprimir_cuentas.setValue("FILTRAR OBRAS SALESIANAS");
        bot_imprimir_cuentas.setMetodo("abrirObras");
        eti_casa.setStyle("font-size:10px;font-weight: bold;");

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setHeader(eti_casa);
        tab_tabla.setSql(ser_gerencial.getResultadoBalanceGeneral("-1", "-1", utilitario.getVariable("p_ger_cuenta_activo"), utilitario.getVariable("p_ger_cuenta_pasivo"), utilitario.getVariable("p_ger_cuenta_patrimonio")));
        tab_tabla.setLectura(true);
        tab_tabla.getColumna("nombre_gemes").setNombreVisual("MES");
        tab_tabla.setColumnaSuma("activo,pasivo,patrimonio");
        tab_tabla.getColumna("activo").alinearDerecha();
        tab_tabla.getColumna("pasivo").alinearDerecha();
        tab_tabla.getColumna("patrimonio").alinearDerecha();
        tab_tabla.dibujar();

        Grid gri_opciones = new Grid();
        gri_opciones.setColumns(3);
        gri_opciones.getChildren().add(new Etiqueta("<strong>PERÍODO :</strong>"));
        gri_opciones.getChildren().add(com_periodo_grafico);
        gri_opciones.getChildren().add(bot_imprimir_cuentas);

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.getChildren().add(gri_opciones);
        pat_panel.setPanelTabla(tab_tabla);

        Grid gri = new Grid();
        gri.setWidth("100%");
        gri.setColumns(2);
        gpa_utilidad.agregarSerie(tab_tabla, "nombre_gemes", "activo");
        gri.getChildren().add(pat_panel);
        gri.getChildren().add(gpa_utilidad);
        grupo.getChildren().add(gri);

        gca_utilidad.setTitulo("RESULTADO EJERCICIO MENSUAL");
        gca_utilidad.agregarSerie(tab_tabla, "nombre_gemes", "activo", "TOTAL " + String.valueOf(com_periodo_grafico.getValue()));
        grupo.getChildren().add(gca_utilidad);

        mep_menu.dibujar(3, "fa fa-bar-chart", "Gráficos estadísticos Resultado del Ejercicio Mensual", grupo, true);
    }

    public void abrirObras() {
        sel_casa_obras.getTab_seleccion().setSql(ser_gerencial.getCasaObraPeriodoFiscal(utilitario.getVariable("p_ger_estado_activo") + "," + utilitario.getVariable("p_ger_estado_cerrado"), com_periodo_grafico.getValue().toString(), "1", "-1"));
        sel_casa_obras.getTab_seleccion().ejecutarSql();
        sel_casa_obras.dibujar();
    }

    public void actualizarGraficos() {
        if (mep_menu.getOpcion() == 3) {
            String seleccionados = sel_casa_obras.getSeleccionados();
            if (seleccionados != null) {

                tab_tabla.setSql(ser_gerencial.getResultadoBalanceGeneral(com_periodo_grafico.getValue().toString(), seleccionados, utilitario.getVariable("p_ger_cuenta_activo"), utilitario.getVariable("p_ger_cuenta_pasivo"), utilitario.getVariable("p_ger_cuenta_patrimonio")));
                tab_tabla.ejecutarSql();
                gca_utilidad.limpiar();
                gca_utilidad.agregarSerie(tab_tabla, "nombre_gemes", "activo", "total " + String.valueOf(com_periodo_grafico.getValue()));
                gpa_utilidad.limpiar();
                gpa_utilidad.agregarSerie(tab_tabla, "nombre_gemes", "activo");
                TablaGenerica tab_obras = utilitario.consultar(ser_gerencial.getObrasHorizontal(seleccionados));
                eti_casa.setValue(tab_obras.getValor("obra"));
                sel_casa_obras.cerrar();
                utilitario.addUpdate("gca_utilidad,gpa_utilidad,eti_casa");
            } else {
                utilitario.agregarMensajeError("Seleccione Obra", "Debe seleccionar al menos una obra");
            }
        }
    }

    public void dibujarTransferirBalance() {
        com_periodo = new Combo();
        com_periodo.setCombo(ser_gerencial.getAnio("true,false", "1", ""));
        com_periodo.eliminarVacio();
        com_periodo.setMetodo("seleccionaPeriodo");
        com_periodo.setStyle("width: 150px;");

        Grupo gru_grupo = new Grupo();
        TablaGenerica tab_casa_obra = utilitario.consultar(ser_gerencial.getCasaObraScursal(utilitario.getVariable("ide_sucu"), com_periodo.getValue().toString(), "1"));
        if (tab_casa_obra.getTotalFilas() > 0) {
            eti_obra.setValue(tab_casa_obra.getValor("nombre_gercas") + " OBRA: " + tab_casa_obra.getValor("nombre_gerobr"));
        } else {
            eti_obra.setValue("No existe obras habilitadas para el periódo físcal seleccionado");
        }

        com_estado_anio_fiscal.setId("com_estado_anio_fiscal");
        com_estado_anio_fiscal.setCombo(ser_gerencial.getEstado());
        com_estado_anio_fiscal.setValue(tab_casa_obra.getValor("ide_gerest"));
        com_estado_anio_fiscal.setDisabled(true);
        com_estado_anio_fiscal.setStyle("width: 100px;");

        com_estado_mes_fiscal.setId("com_estado_mes_fiscal");
        com_estado_mes_fiscal.setValue("");
        com_estado_mes_fiscal.setCombo(ser_gerencial.getEstado());
        com_estado_mes_fiscal.setDisabled(true);
        com_estado_mes_fiscal.setStyle("width: 100px;");

        com_tipo_balance.setId("com_tipo_balance");
        com_tipo_balance.setValue("");
        com_tipo_balance.setCombo(ser_gerencial.getTipoBalance("1", "-1"));
        com_tipo_balance.setMetodo("actualizarEstadoBalance");
        //com_tipo_balance.setDisabled(true);
        com_tipo_balance.setStyle("width: 100px;");
        com_mes.setId("com_mes");
        com_mes.setCombo(ser_gerencial.getMes("-1", ""));
        //com_mes.setDisabled(true);
        com_mes.setValue("");
        com_mes.setMetodo("actualizarTipoBalance");
        com_mes.setStyle("width: 100px;");

        Etiqueta eti_des_a = new Etiqueta("Estado Año Contable:");
        Etiqueta eti_des_c = new Etiqueta("Casa-Obra:");
        Etiqueta eti_des_t = new Etiqueta("Tipo Balance:");
        Etiqueta eti_des_esta_ba = new Etiqueta("Mes:");
        Etiqueta eti_des_s = new Etiqueta("Estado Mes Contable:");
        Etiqueta eti_anio = new Etiqueta("Año Contable:");
        eti_anio.setStyle("font-size:14px;font-weight: bold;color:black");
        eti_des_a.setStyle("font-size:14px;font-weight: bold;color:black");
        eti_des_c.setStyle("font-size:14px;font-weight: bold;color:black");
        eti_des_t.setStyle("font-size:14px;font-weight: bold;color:black");
        eti_des_esta_ba.setStyle("font-size:14px;font-weight: bold;color:black");
        eti_des_s.setStyle("font-size:14px;font-weight: bold;color:black");

        Fieldset fis_consulta = new Fieldset();
        Grid gr_dato_titulo = new Grid();
        gr_dato_titulo.setColumns(2);
        gr_dato_titulo.getChildren().add(eti_anio);
        gr_dato_titulo.getChildren().add(com_periodo);
        gr_dato_titulo.getChildren().add(eti_des_c);
        gr_dato_titulo.getChildren().add(eti_obra);

        Grid gr_dato_obra = new Grid();
        gr_dato_obra.setColumns(4);
        gr_dato_obra.getChildren().add(eti_des_a);
        gr_dato_obra.getChildren().add(com_estado_anio_fiscal);
        gr_dato_obra.getChildren().add(eti_des_esta_ba);
        gr_dato_obra.getChildren().add(com_mes);
        gr_dato_obra.getChildren().add(eti_des_t);
        gr_dato_obra.getChildren().add(com_tipo_balance);
        gr_dato_obra.getChildren().add(eti_des_s);
        gr_dato_obra.getChildren().add(com_estado_mes_fiscal);

        Grid gp = new Grid();
        gp.setColumns(3);

        Boton bot_imprimir_cuentas = new Boton();
        bot_imprimir_cuentas.setIcon("ui-icon-print");
        bot_imprimir_cuentas.setTitle("Imprimir");
        bot_imprimir_cuentas.setValue("Importar Balance General");
        bot_imprimir_cuentas.setMetodo("transferirBalance");

        Boton bot_loquear = new Boton();
        bot_loquear.setIcon("ui-icon-print");
        bot_loquear.setValue("Bloquear y Transferir");
        bot_loquear.setMetodo("bloquearTransferir");

        fis_consulta.getChildren().add(gr_dato_titulo);
        fis_consulta.getChildren().add(gr_dato_obra);
        gp.getChildren().add(bot_imprimir_cuentas);
        gp.getChildren().add(bot_loquear); //comentado omentaeamente luis t habilitar para cerrar
        gp.getChildren().add(new Etiqueta(""));

        fis_consulta.getChildren().add(gp);
        // combo de fechas
        Grid gri_fechas = new Grid();
        gri_fechas.setColumns(5);
        gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA DESDE :</strong>"));
        cal_fecha_inicio = new Calendario();
        cal_fecha_inicio.setId("cal_fecha_inicio");
        //cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));        
        gri_fechas.getChildren().add(cal_fecha_inicio);
        gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA HASTA :</strong>"));
        cal_fecha_fin = new Calendario();
        cal_fecha_fin.setId("cal_fecha_fin");
        gri_fechas.getChildren().add(cal_fecha_fin);
        fis_consulta.getChildren().add(gri_fechas);
        Separator separar = new Separator();
        fis_consulta.getChildren().add(separar);

        gru_grupo.getChildren().add(fis_consulta);

        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setTabla("ger_balance_detalle", "ide_gebade", 3);
        tab_tabla3.setGenerarPrimaria(false);
        tab_tabla3.getColumna("ide_cndpc").setCombo(ser_gerencial.getPlanCuentas());
        tab_tabla3.setCondicion("ide_gebame=-1");
        tab_tabla3.getColumna("ide_gebame").setVisible(false);
        tab_tabla3.getColumna("ide_cndpc").setAutoCompletar();
        tab_tabla3.getColumna("ide_cndpc").setLectura(true);
        tab_tabla3.getColumna("ide_gebade").setLectura(true);
        tab_tabla3.getColumna("valor_debe_gebade").setLectura(true);
        tab_tabla3.getColumna("valor_haber_gebade").setLectura(true);
        tab_tabla3.getColumna("ide_gebade").setNombreVisual("CODIGO");
        tab_tabla3.getColumna("ide_cndpc").setNombreVisual("CUENTA CONTABLE");
        tab_tabla3.getColumna("valor_debe_gebade").setNombreVisual("VALOR DEBE");
        tab_tabla3.getColumna("valor_haber_gebade").setNombreVisual("VALOR HABER");
        tab_tabla3.setRows(15);
        tab_tabla3.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla3);
        gru_grupo.getChildren().add(pat_panel);
        mep_menu.dibujar(1, "TRANSFERENCIA DEL BALANCE GENERAL", gru_grupo);
    }

    public void transferirBalance() {
        if (cal_fecha_inicio.getFecha() == null || cal_fecha_fin.getFecha() == null) {
            utilitario.agregarMensajeError("Fechas no válidas", "Necesita seleccionar la fecha inicial o fecha final");
            return;
        }
        if (!utilitario.isFechasValidas(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha())) {
            utilitario.agregarMensajeError("Fechas no válidas", "Las fecha final del reporte es mayor a la fecha inicial");
            return;
        }
        if (com_estado_mes_fiscal.getValue().toString().equals(utilitario.getVariable("p_ger_estado_activo"))) {
            String estado_normal = utilitario.getVariable("p_con_estado_comprobante_normal");
            String estado_inicial = utilitario.getVariable("p_con_estado_comp_inicial");
            String estado = "-1";
            if (com_tipo_balance.getValue().toString().equals("1")) {  //Inicial
                estado = estado_inicial;
            } else if (com_tipo_balance.getValue().toString().equals("2")) {  //mensual
                estado = estado_normal;
            }
            TablaGenerica tab_cuenta = utilitario.consultar(ser_gerencial.getTranseferirAsientos(estado, utilitario.getVariable("ide_sucu"), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            utilitario.getConexion().ejecutarSql(ser_gerencial.deleteTempBalance(utilitario.getVariable("ide_usua")));
            if (tab_cuenta.getTotalFilas() > 0) {
                for (int i = 0; i < tab_cuenta.getTotalFilas(); i++) {
                    //System.out.println("primer insert i "+i);
                    utilitario.getConexion().ejecutarSql(ser_gerencial.insertTempBalance(tab_cuenta.getValor(i, "ide_cndpc"), tab_cuenta.getValor(i, "debe"), tab_cuenta.getValor(i, "haber"), utilitario.getVariable("ide_usua"), tab_cuenta.getValor(i, "ide_cnncu")));
                }
                int nivel_nuevo = 0;
                for (int j = 7; j > 2; j--) {
                    nivel_nuevo = j - 1;
                    //System.out.println("j  " + j);
                    TablaGenerica tab_temporal = utilitario.consultar(ser_gerencial.getCalTemBalance(utilitario.getVariable("ide_usua"), j + ""));
                    for (int k = 0; k < tab_temporal.getTotalFilas(); k++) {
                        // System.out.println("segundo insert k "+k);
                        utilitario.getConexion().ejecutarSql(ser_gerencial.insertTempBalance(tab_temporal.getValor(k, "con_ide_cndpc"), tab_temporal.getValor(k, "debe"), tab_temporal.getValor(k, "haber"), utilitario.getVariable("ide_usua"), nivel_nuevo + ""));
                    }

                }
                TablaGenerica tab_temporal_insert = utilitario.consultar(ser_gerencial.getCalTemBalance(utilitario.getVariable("ide_usua"), "2,3,4,5,6"));
                utilitario.getConexion().ejecutarSql(ser_gerencial.deleteBalanceDetalle(ide_gebame));
                for (int k = 0; k < tab_temporal_insert.getTotalFilas(); k++) {
                    tab_tabla3.insertar();
                    tab_tabla3.setValor("ide_cndpc", tab_temporal_insert.getValor(k, "con_ide_cndpc"));
                    tab_tabla3.setValor("ide_gebame", ide_gebame);
                    tab_tabla3.setValor("valor_debe_gebade", tab_temporal_insert.getValor(k, "debe"));
                    tab_tabla3.setValor("valor_haber_gebade", tab_temporal_insert.getValor(k, "haber"));
                }
            }
            tab_tabla3.guardar();
            guardarPantalla();
            tab_tabla3.setCondicion("ide_gebame=" + ide_gebame);
            tab_tabla3.ejecutarSql();
        } else {
            utilitario.agregarMensajeError("Periódo no habilitado", "NO se puede realizar la transferencia, el período se encuentra inhabilitado");
        }

    }

    public void bloquearTransferir() {
        if (dia_cerrar_balance.isVisible()) {

            if (utilitario.getVariable("p_ger_casa_principal").equals("true")) { // si es la casa principal solo cerramos el asiento, caso contrario hacemos el traspaso
                utilitario.getConexion().ejecutarSql(ser_gerencial.UpdateEstadoBalanceCerrado(ide_gebame, utilitario.getVariable("p_ger_estado_cerrado")));
                actualizarTipoBalance();
            } else {
               

                for (int i = 0; i < tab_tabla3.getTotalFilas(); i++) {
                    TablaGenerica tab_nombre_cuenta=utilitario.consultar("select ide_cndpc,codig_recur_cndpc,nombre_cndpc from con_det_plan_cuen where ide_cndpc= "+tab_tabla3.getValor(i, "ide_cndpc"));
                     tab_tabla_cuenta_traspaso.setSql("select ide_cndpc,codig_recur_cndpc,nombre_cndpc from con_det_plan_cuen where codig_recur_cndpc='"+tab_nombre_cuenta.getValor("codig_recur_cndpc")+"' ");
                    tab_tabla_cuenta_traspaso.ejecutarSql();
                     tab_tabla_traspaso.insertar();
                    tab_tabla_traspaso.setValor("ide_cndpc",tab_tabla_cuenta_traspaso.getValor("ide_cndpc") );
                    tab_tabla_traspaso.setValor("ide_gebame", tab_tabla_traspaso_cabecera.getValor("ide_gebame"));
                    tab_tabla_traspaso.setValor("valor_debe_gebade", tab_tabla3.getValor(i, "valor_debe_gebade"));
                    tab_tabla_traspaso.setValor("valor_haber_gebade", tab_tabla3.getValor(i, "valor_haber_gebade"));
                }
                if(tab_tabla_traspaso.guardar()){
                conPostgres.guardarPantalla();
                utilitario.getConexion().ejecutarSql(ser_gerencial.UpdateEstadoBalanceCerrado(ide_gebame, utilitario.getVariable("p_ger_estado_cerrado")));
                //actualizarTipoBalance();
                }
            }
            utilitario.agregarMensaje("Balance Tranferido", "Se cerro el balance y fue tranferido con éxito");
            dia_cerrar_balance.cerrar();
        } else {
            if (tab_tabla3.getTotalFilas() > 0) {
                if (com_estado_mes_fiscal.getValue().equals(utilitario.getVariable("p_ger_estado_activo"))) {
                    if (utilitario.getVariable("p_ger_casa_principal").equals("true")) {
                        dia_cerrar_balance.setDialogo(eti_cerrar_balance);
                    } else {
                        TablaGenerica anio_vigente = utilitario.consultar("select * from gen_anio where ide_geani=" + com_periodo.getValue());
                        if (anio_vigente.getTotalFilas() > 0) {
                            TablaGenerica tab_mes = utilitario.consultar("select * from gen_mes where ide_gemes=" + com_mes.getValue());
                            if (tab_mes.getTotalFilas() > 0) {
                                TablaGenerica tab_tipo_balance = utilitario.consultar("select ide_getiba,detalle_getiba from ger_tipo_balance where ide_getiba= " + com_tipo_balance.getValue());
                                if (tab_tipo_balance.getTotalFilas() > 0) {
                                    if (com_estado_mes_fiscal.getValue().equals("1")) {
                                                TablaGenerica tab_casa_obra = utilitario.consultar(ser_gerencial.getCasaObraScursal(utilitario.getVariable("ide_sucu"), com_periodo.getValue().toString(), "1"));
                                                if(tab_casa_obra.getTotalFilas()>0){
                                        tab_tabla_traspaso_cabecera.setCondicion("ide_gebame in (select ide_gebame from ger_balance_mensual where ide_gecobc in (select ide_gecobc from ger_cont_balance_cabecera where ide_geani in (select ide_geani from gen_anio where nom_geani='" + anio_vigente.getValor("nom_geani") + "') and ide_gerobr in (select ide_gerobr from ger_obra where codigo_gerobr='"+tab_casa_obra.getValor("codigo_gerobr")+"'))) and ide_gemes=" + com_mes.getValue() + " and ide_getiba=" + com_tipo_balance.getValue());
                                        tab_tabla_traspaso_cabecera.ejecutarSql();
                                        dia_cerrar_balance.setDialogo(tab_tabla_traspaso_cabecera);
                                        } else {
                                        utilitario.agregarMensajeError("La Casa y Obra no se encuentran habilitados para transferir los balances", "Contactese con Casa Inspectorial");
                                    }
                                    } else {
                                        utilitario.agregarMensajeError("El estado del balnce debe encontrarse ACTIVO ", "Contactese con Casa Inspectorial");
                                    }
                                } else {
                                    utilitario.agregarMensajeError("No existe aperturado el Tipo Balance " + tab_tipo_balance.getValor("detalle_getiba"), "Contactese con Casa Inspectorial");
                                }
                            } else {
                                utilitario.agregarMensajeError("No existe aperturado el Mes Contable", "Contactese con Casa Inspectorial");
                            }
                        } else {
                            utilitario.agregarMensajeError("No existe aperturado el Año Contable", "Contactese con Casa Inspectorial");
                        }
                    }
                    dia_cerrar_balance.dibujar();
                } else {
                    utilitario.agregarMensajeError("No se puede continuar", "El mes contable debe encontrarse en estado activo para poder continuar");
                }
            } else {
                utilitario.agregarMensajeError("No existe datos", "No existen datos no puede cerrar el balance general");
            }
        }
    }

    public void actualizarEstadoBalance() {
        TablaGenerica tab_casa_obra = utilitario.consultar(ser_gerencial.getCasaObraScursal(utilitario.getVariable("ide_sucu"), com_periodo.getValue().toString(), "1"));
        TablaGenerica tab_detalle = utilitario.consultar("select * from ger_balance_mensual  where ide_gecobc=" + tab_casa_obra.getValor("ide_gecobc") + " and ide_getiba=" + com_tipo_balance.getValue()+" and ide_gemes="+com_mes.getValue());
        //System.out.println("sql_tabla ");
        //tab_detalle.imprimirSql();
        com_estado_mes_fiscal.setValue(tab_detalle.getValor("ide_gerest"));
        ide_gebame = tab_detalle.getValor("ide_gebame");
        tab_tabla3.setCondicion("ide_gebame=" + ide_gebame);
        tab_tabla3.ejecutarSql();
        utilitario.addUpdate("com_estado_mes_fiscal");

    }

    public void actualizarTipoBalance() {
        com_tipo_balance.setCombo(ser_gerencial.getTipoBalance("1", com_mes.getValue().toString()));
        com_estado_mes_fiscal.setValue("-1");
        utilitario.addUpdate("com_tipo_balance,com_estado_mes_fiscal");
    }

    public void seleccionaPeriodo() {
        TablaGenerica tab_casa_obra = utilitario.consultar(ser_gerencial.getCasaObraScursal(utilitario.getVariable("ide_sucu"), com_periodo.getValue().toString(), "1"));
        com_estado_mes_fiscal.setValue("");
        ide_gebame = "-1";
        com_tipo_balance.setValue("");
        com_mes.setValue("");
        com_estado_anio_fiscal.setValue(tab_casa_obra.getValor("ide_gerest"));
        eti_obra.setValue("No existe obra habilitada para el periódo físcal seleccionado");
        tab_tabla3.setCondicion("ide_gebame=" + ide_gebame);
        tab_tabla3.ejecutarSql();
        utilitario.addUpdate("com_estado_mes_fiscal,com_tipo_balance,com_mes,com_estado_anio_fiscal,eti_obra");
    }

    public void dibujarEstadosFinancieros() {

        // BOTONES REPORTES
        Boton bot_imp_balance_general = new Boton();
        bot_imp_balance_general.setIcon("ui-icon-print");
        bot_imp_balance_general.setValue("Balance General");
        bot_imp_balance_general.setMetodo("imprimirBalanceGeneral");
        
        Boton bot_imp_estado_resultado = new Boton();
        bot_imp_estado_resultado.setIcon("ui-icon-print");
        bot_imp_estado_resultado.setValue("Estado Resultados");
        bot_imp_estado_resultado.setMetodo("imprimirEstadoResultado");

        Boton bot_imp_bal_comprobacion = new Boton();
        bot_imp_bal_comprobacion.setIcon("ui-icon-print");
        bot_imp_bal_comprobacion.setValue("Balance Comprobación");
        bot_imp_bal_comprobacion.setMetodo("imprimirBalanceComprobacion");

        Grupo gru_grupo = new Grupo();

        Fieldset fis_consulta = new Fieldset();
        Grid gri_fechas = new Grid();
        gri_fechas.setColumns(6);

        com_periodo_financiero = new Combo();
        com_periodo_financiero.setCombo(ser_gerencial.getAnio("true,false", "1", ""));
        com_periodo_financiero.eliminarVacio();
        com_periodo_financiero.setMetodo("seleccionaPeriodoReporte");
        gri_fechas.getChildren().add(new Etiqueta("<strong>AÑO FÍSCAL </strong>"));
        //gri_fechas.getChildren().add(new Espacio("1", "1"));
        gri_fechas.getChildren().add(new Etiqueta("<strong>TIPO BALANCE </strong>"));
       // gri_fechas.getChildren().add(new Espacio("1", "1"));
        gri_fechas.getChildren().add(bot_imp_balance_general);
        gri_fechas.getChildren().add(bot_imp_estado_resultado);        
        gri_fechas.getChildren().add(bot_imp_bal_comprobacion);
         gri_fechas.getChildren().add(new Espacio("1", "1"));
        gri_fechas.getChildren().add(com_periodo_financiero);
        //GENERANDO LISTA TIPO BALANCE
        lis_tipo_balance.setId("lis_tipo_balance");
        lis_tipo_balance.setListaSeleccion(ser_gerencial.getTipoBalance("-1", "-1"));
        //gri_fechas.getChildren().add(new Espacio("1", "1"));
        gri_fechas.getChildren().add(lis_tipo_balance);
        gri_fechas.getChildren().add(new Espacio("1", "1"));
        gri_fechas.getChildren().add(new Espacio("1", "1"));
        gri_fechas.getChildren().add(new Espacio("1", "1"));
        gri_fechas.getChildren().add(new Espacio("1", "1"));
        gri_fechas.getChildren().add(new Etiqueta("<strong>NIVEL INICIAL  </strong>"));

        //NIVEL INICIAL
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "1"
        };
        Object fila2[] = {
            "2", "2"
        };
        Object fila3[] = {
            "3", "3"
        };
        Object fila4[] = {
            "4", "4"
        };
        Object fila5[] = {
            "5", "5"
        };
        lista.add(fila1);
        lista.add(fila2);
        lista.add(fila3);
        lista.add(fila4);
        lista.add(fila5);
        rad_nivel_inicial.setId("rad_nivel_inicial");
        rad_nivel_inicial.setRadio(lista);
        gri_fechas.getChildren().add(rad_nivel_inicial);
        gri_fechas.getChildren().add(new Espacio("1", "1"));
        gri_fechas.getChildren().add(new Espacio("1", "1"));
        gri_fechas.getChildren().add(new Espacio("1", "1"));
        gri_fechas.getChildren().add(new Espacio("1", "1"));
        gri_fechas.getChildren().add(new Etiqueta("<strong>NIVEL FINAL </strong>"));
        rad_nivel_final.setId("rad_nivel_final");
        rad_nivel_final.setRadio(lista);
        gri_fechas.getChildren().add(rad_nivel_final);
        fis_consulta.getChildren().add(gri_fechas);

        Separator separar_mes = new Separator();
        fis_consulta.getChildren().add(separar_mes);
        //GENERANDO MESES
        Grid gri_mes = new Grid();
        gri_mes.setColumns(1);
        lis_meses.setId("lis_meses");
        lis_meses.setListaSeleccion(ser_gerencial.getMes("-1", "-1"));
        gri_mes.getChildren().add(new Etiqueta("<strong>SELECCIONAR MES </strong>"));
        gri_mes.getChildren().add(lis_meses);
        fis_consulta.getChildren().add(gri_mes);
        // SEPARAR BOTN SLEECCION INVERSA
        Separator separar_sel = new Separator();
        fis_consulta.getChildren().add(separar_sel);
        //BOTON SELECCION INVERSA
        BotonesCombo boc_seleccion_inversa = new BotonesCombo();
        ItemMenu itm_todas = new ItemMenu();
        ItemMenu itm_niguna = new ItemMenu();

        boc_seleccion_inversa.setValue("Selección Inversa");
        boc_seleccion_inversa.setIcon("ui-icon-circle-check");
        boc_seleccion_inversa.setMetodo("seleccinarInversa");
        boc_seleccion_inversa.setUpdate("tab_consulta");
        itm_todas.setValue("Seleccionar Todo");
        itm_todas.setIcon("ui-icon-check");
        itm_todas.setMetodo("seleccionarTodas");
        itm_todas.setUpdate("tab_consulta");
        boc_seleccion_inversa.agregarBoton(itm_todas);
        itm_niguna.setValue("Seleccionar Ninguna");
        itm_niguna.setIcon("ui-icon-minus");
        itm_niguna.setMetodo("seleccionarNinguna");
        itm_niguna.setUpdate("tab_consulta");
        boc_seleccion_inversa.agregarBoton(itm_niguna);
        fis_consulta.getChildren().add(boc_seleccion_inversa);
        // HASTA AQUI BOTON SELECCION INVERSA
        Separator separar = new Separator();
        fis_consulta.getChildren().add(separar);
        gru_grupo.getChildren().add(fis_consulta);

        tab_consulta = new Tabla();
        tab_consulta.setId("tab_consulta");
        tab_consulta.setHeader("CASAS SALESIANAS REGISTRADAS EN EL PERIODO FISCAL");
        tab_consulta.setSql(ser_gerencial.getCasaObraScursal(utilitario.getVariable("ide_sucu"), com_periodo_financiero.getValue().toString(), str_impresion));
        tab_consulta.getColumna("ide_gerest").setCombo(ser_gerencial.getEstado());
        tab_consulta.getColumna("ide_gerest").setLongitud(30);
        tab_consulta.getColumna("ide_gecobc").setNombreVisual("IDE");
        tab_consulta.getColumna("ide_gerest").setNombreVisual("ESTADO");
        tab_consulta.getColumna("nombre_gercas").setNombreVisual("CASA SALESIANAS");
        tab_consulta.getColumna("nombre_gerobr").setNombreVisual("OBRA SALESIANA");
        tab_consulta.getColumna("codigo_gerobr").setNombreVisual("CODIGO OBRA");
        tab_consulta.setTipoSeleccion(true);
        tab_consulta.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_consulta);
        gru_grupo.getChildren().add(pat_panel);
        mep_menu.dibujar(2, "REPORTE DE ESTADOS FINANCIEROS", gru_grupo);
    }

    public void tipoImpresion() {
        if (utilitario.getVariable("p_ger_imprimir_admin").equals("false")) {
            str_impresion = "1";
        }
    }

    public void seleccionaPeriodoReporte() {
        tab_consulta.setSql(ser_gerencial.getCasaObraScursal(utilitario.getVariable("ide_sucu"), com_periodo_financiero.getValue().toString(), str_impresion));
        tab_consulta.ejecutarSql();
    }

    public void imprimirBalanceGeneral() {
        if (lis_tipo_balance.getSeleccionados() == "") {
            utilitario.agregarMensajeError("Tipo Balance", "Seleccione el tipo de balance que desea incluir en la impresión del Balance General");
            return;
        }
        if (rad_nivel_inicial.getValue() == null) {
            utilitario.agregarMensajeError("Nivel Inicial", "Seleccione el nivel inicial");
            return;
        }
        if (rad_nivel_final.getValue() == null) {
            utilitario.agregarMensajeError("Nivel Final", "Seleccione el nivel final");
            return;
        }
        if (Integer.parseInt(rad_nivel_inicial.getValue().toString()) > Integer.parseInt(rad_nivel_final.getValue().toString())) {
            utilitario.agregarMensajeError("Diferencia de Niveles", "El nivel inicial es superior al nivel final");
            return;
        }
        if (lis_meses.getSeleccionados() == "") {
            utilitario.agregarMensajeError("Meses", "Seleccione los meses que desea incluir en la impresión del Balance General");
            return;
        }
        String seleccionado = tab_consulta.getFilasSeleccionadas().toString();
        if (seleccionado.equals("null") || seleccionado.isEmpty()) {
            utilitario.agregarMensajeError("Obras y Casas", "Debe seleccionar al menos una Obra Salesiana");
            return;
        }
        //Extraer Casas y obras
        TablaGenerica tab_obras = utilitario.consultar(ser_gerencial.getObrasHorizontal(seleccionado));
        // Extraer mes inicial
        TablaGenerica tab_mes_min = utilitario.consultar(ser_gerencial.getResumenMes(lis_meses.getSeleccionados(), " min(ide_gemes) ", " where 1=1 "));
        TablaGenerica tab_mesi_desc = utilitario.consultar(ser_gerencial.getMes("0", tab_mes_min.getValor("valor")));
        // Extraer mes final
        TablaGenerica tab_mes_max = utilitario.consultar(ser_gerencial.getResumenMes(lis_meses.getSeleccionados(), " max(ide_gemes) ", " where 1=1 "));
        TablaGenerica tab_mesf_desc = utilitario.consultar(ser_gerencial.getMes("0", tab_mes_max.getValor("valor")));
        // Extraer periodo
        TablaGenerica tab_periodo = utilitario.consultar(ser_gerencial.getAnio("true,false", "2", com_periodo_financiero.getValue().toString()));
        //Extraer Valores Totales
        TablaGenerica tab_totales = utilitario.consultar(ser_gerencial.getBalanceGeneral(com_periodo_financiero.getValue().toString(), seleccionado, lis_tipo_balance.getSeleccionados(), lis_meses.getSeleccionados(), "1", "1", "-1", "-1"));

        double tot_activo = 0;
        double tot_pasivo = 0;
        double tot_patrimonio = 0;
        if (tab_totales.getTotalFilas() > 0) {
            for (int i = 0; i < tab_totales.getTotalFilas(); i++) {
                if (tab_totales.getValor(i, "ide_cndpc").equals(utilitario.getVariable("p_ger_cuenta_activo"))) {
                    tot_activo = Double.parseDouble(tab_totales.getValor(i, "debe"));
                }
                if (tab_totales.getValor(i, "ide_cndpc").equals(utilitario.getVariable("p_ger_cuenta_pasivo"))) {
                    tot_pasivo = Double.parseDouble(tab_totales.getValor(i, "haber"));
                }
                if (tab_totales.getValor(i, "ide_cndpc").equals(utilitario.getVariable("p_ger_cuenta_patrimonio"))) {
                    tot_patrimonio = Double.parseDouble(tab_totales.getValor(i, "haber"));
                }
            }
        }
        double utilidad_perdida = tot_activo - tot_pasivo - tot_patrimonio;
        double total = tot_pasivo + tot_patrimonio + utilidad_perdida;
        parametro = new HashMap();
        TablaGenerica tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
        if (tab_datos.getTotalFilas() > 0) {
            parametro.put("logo", utilitario.getLogoEmpresa().getStream());
            parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
            parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
            parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
            parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
            parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));
        }

        parametro.put("titulo", "BALANCE GENERAL CONSOLIDADO");
        parametro.put("pmeses", lis_meses.getSeleccionados());
        parametro.put("pmes_inicial", tab_mesi_desc.getValor("nombre_gemes"));
        parametro.put("pmes_final", tab_mesf_desc.getValor("nombre_gemes"));
        parametro.put("pnivel_inicial", Integer.parseInt(rad_nivel_inicial.getValue().toString()));
        parametro.put("pnivel_final", Integer.parseInt(rad_nivel_final.getValue().toString()));
        parametro.put("pperiodo", tab_periodo.getValor("nom_geani"));
        parametro.put("ptipo_balance", lis_tipo_balance.getSeleccionados());
        parametro.put("pusuario", utilitario.getVariable("nick"));
        parametro.put("pobra", tab_obras.getValor("obra"));
        parametro.put("pide_obra", seleccionado);
        parametro.put("pide_geani", Integer.parseInt(com_periodo_financiero.getValue().toString()));
        parametro.put("p_tot_activo", tot_activo);
        parametro.put("p_total", total);
        parametro.put("p_utilidad_perdida", utilidad_perdida);
        parametro.put("p_tot_pasivo", tot_pasivo);
        parametro.put("p_tot_patrimonio", (tot_patrimonio));
        parametro.put("pfirma1", utilitario.getVariable("p_ger_nom_firma1"));
        parametro.put("pcargo1", utilitario.getVariable("p_ger_cargo_firma1"));
        parametro.put("pfirma2", utilitario.getVariable("p_ger_nom_firma2"));
        parametro.put("pcargo2", utilitario.getVariable("p_ger_cargo_firma2"));
        vipdf_mayor.setVisualizarPDF("rep_gerencial/rep_balance_general_ger.jasper", parametro);
        vipdf_mayor.dibujar();

    }
public void imprimirEstadoResultado() {
        if (lis_tipo_balance.getSeleccionados() == "") {
            utilitario.agregarMensajeError("Tipo Balance", "Seleccione el tipo de balance que desea incluir en la impresión del Estado de Resultados");
            return;
        }
        if (rad_nivel_inicial.getValue() == null) {
            utilitario.agregarMensajeError("Nivel Inicial", "Seleccione el nivel inicial");
            return;
        }
        if (rad_nivel_final.getValue() == null) {
            utilitario.agregarMensajeError("Nivel Final", "Seleccione el nivel final");
            return;
        }
        if (Integer.parseInt(rad_nivel_inicial.getValue().toString()) > Integer.parseInt(rad_nivel_final.getValue().toString())) {
            utilitario.agregarMensajeError("Diferencia de Niveles", "El nivel inicial es superior al nivel final");
            return;
        }
        if (lis_meses.getSeleccionados() == "") {
            utilitario.agregarMensajeError("Meses", "Seleccione los meses que desea incluir en la impresión del Balance General");
            return;
        }
        String seleccionado = tab_consulta.getFilasSeleccionadas().toString();
        if (seleccionado.equals("null") || seleccionado.isEmpty()) {
            utilitario.agregarMensajeError("Obras y Casas", "Debe seleccionar al menos una Obra Salesiana");
            return;
        }
        //Extraer Casas y obras
        TablaGenerica tab_obras = utilitario.consultar(ser_gerencial.getObrasHorizontal(seleccionado));
        // Extraer mes inicial
        TablaGenerica tab_mes_min = utilitario.consultar(ser_gerencial.getResumenMes(lis_meses.getSeleccionados(), " min(ide_gemes) ", " where 1=1 "));
        TablaGenerica tab_mesi_desc = utilitario.consultar(ser_gerencial.getMes("0", tab_mes_min.getValor("valor")));
        // Extraer mes final
        TablaGenerica tab_mes_max = utilitario.consultar(ser_gerencial.getResumenMes(lis_meses.getSeleccionados(), " max(ide_gemes) ", " where 1=1 "));
        TablaGenerica tab_mesf_desc = utilitario.consultar(ser_gerencial.getMes("0", tab_mes_max.getValor("valor")));
        // Extraer periodo
        TablaGenerica tab_periodo = utilitario.consultar(ser_gerencial.getAnio("true,false", "2", com_periodo_financiero.getValue().toString()));
        //Extraer Valores Totales
        TablaGenerica tab_totales = utilitario.consultar(ser_gerencial.getBalanceGeneral(com_periodo_financiero.getValue().toString(), seleccionado, lis_tipo_balance.getSeleccionados(), lis_meses.getSeleccionados(), "1", "1", "-1", "-1"));

        double tot_activo = 0;
        double tot_pasivo = 0;
        double tot_patrimonio = 0;
        double tot_ingresos = 0;
        double tot_costos = 0;
        if (tab_totales.getTotalFilas() > 0) {
            for (int i = 0; i < tab_totales.getTotalFilas(); i++) {
                if (tab_totales.getValor(i, "ide_cndpc").equals(utilitario.getVariable("p_ger_cuenta_activo"))) {
                    tot_activo = Double.parseDouble(tab_totales.getValor(i, "debe"));
                }
                if (tab_totales.getValor(i, "ide_cndpc").equals(utilitario.getVariable("p_ger_cuenta_pasivo"))) {
                    tot_pasivo = Double.parseDouble(tab_totales.getValor(i, "haber"));
                }
                if (tab_totales.getValor(i, "ide_cndpc").equals(utilitario.getVariable("p_ger_cuenta_patrimonio"))) {
                    tot_patrimonio = Double.parseDouble(tab_totales.getValor(i, "haber"));
                }
                if (tab_totales.getValor(i, "ide_cndpc").equals(utilitario.getVariable("p_ger_cuenta_ingreso"))) {
                    tot_ingresos = Double.parseDouble(tab_totales.getValor(i, "haber"));
                }
                if (tab_totales.getValor(i, "ide_cndpc").equals(utilitario.getVariable("p_ger_cuenta_costo"))) {
                    tot_costos = Double.parseDouble(tab_totales.getValor(i, "debe"));
                   // System.out.println("costos"+tab_totales.getValor(i, "debe")+" cvv "+tab_totales.getValor(i, "haber"));
                }
            }
        }
        double utilidad_perdida = tot_activo - tot_pasivo - tot_patrimonio;
        double total = tot_pasivo + tot_patrimonio + utilidad_perdida;
        parametro = new HashMap();
        TablaGenerica tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
        if (tab_datos.getTotalFilas() > 0) {
            parametro.put("logo", utilitario.getLogoEmpresa().getStream());
            parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
            parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
            parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
            parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
            parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));
        }

        parametro.put("titulo", "ESTADO DE RESULTADOS CONSOLIDADO");
        parametro.put("pmeses", lis_meses.getSeleccionados());
        parametro.put("pmes_inicial", tab_mesi_desc.getValor("nombre_gemes"));
        parametro.put("pmes_final", tab_mesf_desc.getValor("nombre_gemes"));
        parametro.put("pnivel_inicial", Integer.parseInt(rad_nivel_inicial.getValue().toString()));
        parametro.put("pnivel_final", Integer.parseInt(rad_nivel_final.getValue().toString()));
        parametro.put("pperiodo", tab_periodo.getValor("nom_geani"));
        parametro.put("ptipo_balance", lis_tipo_balance.getSeleccionados());
        parametro.put("pusuario", utilitario.getVariable("nick"));
        parametro.put("pobra", tab_obras.getValor("obra"));
        parametro.put("pide_obra", seleccionado);
        parametro.put("pide_geani", Integer.parseInt(com_periodo_financiero.getValue().toString()));
        parametro.put("p_tot_activo", tot_activo);
        parametro.put("p_total", total);
        parametro.put("p_utilidad_perdida", utilidad_perdida);
        parametro.put("p_tot_pasivo", tot_pasivo);
        parametro.put("p_tot_patrimonio", (tot_patrimonio));
        parametro.put("p_ingresos", (tot_ingresos));
        parametro.put("p_gastos", (tot_costos)); 
        parametro.put("pfirma1", utilitario.getVariable("p_ger_nom_firma1"));
        parametro.put("pcargo1", utilitario.getVariable("p_ger_cargo_firma1"));
        parametro.put("pfirma2", utilitario.getVariable("p_ger_nom_firma2"));
        parametro.put("pcargo2", utilitario.getVariable("p_ger_cargo_firma2"));
        vipdf_mayor.setVisualizarPDF("rep_gerencial/rep_estado_resultado.jasper", parametro);
        vipdf_mayor.dibujar();

    }

    public void imprimirBalanceComprobacion() {
        if (lis_tipo_balance.getSeleccionados() == "") {
            utilitario.agregarMensajeError("Tipo Balance", "Seleccione el tipo de balance que desea incluir en la impresión del Balance General");
            return;
        }
        if (rad_nivel_inicial.getValue() == null) {
            utilitario.agregarMensajeError("Nivel Inicial", "Seleccione el nivel inicial");
            return;
        }
        if (rad_nivel_final.getValue() == null) {
            utilitario.agregarMensajeError("Nivel Final", "Seleccione el nivel final");
            return;
        }
        if (Integer.parseInt(rad_nivel_inicial.getValue().toString()) > Integer.parseInt(rad_nivel_final.getValue().toString())) {
            utilitario.agregarMensajeError("Diferencia de Niveles", "El nivel inicial es superior al nivel final");
            return;
        }
        if (lis_meses.getSeleccionados() == "") {
            utilitario.agregarMensajeError("Meses", "Seleccione los meses que desea incluir en la impresión del Balance General");
            return;
        }
        String seleccionado = tab_consulta.getFilasSeleccionadas().toString();
        if (seleccionado.equals("null") || seleccionado.isEmpty()) {
            utilitario.agregarMensajeError("Obras y Casas", "Debe seleccionar al menos una Obra Salesiana");
            return;
        }
        //Extraer Casas y obras
        TablaGenerica tab_obras = utilitario.consultar(ser_gerencial.getObrasHorizontal(seleccionado));
        // Extraer mes inicial
        TablaGenerica tab_mes_min = utilitario.consultar(ser_gerencial.getResumenMes(lis_meses.getSeleccionados(), " min(ide_gemes) ", " where 1=1 "));
        TablaGenerica tab_mesi_desc = utilitario.consultar(ser_gerencial.getMes("0", tab_mes_min.getValor("valor")));
        // Extraer mes final
        TablaGenerica tab_mes_max = utilitario.consultar(ser_gerencial.getResumenMes(lis_meses.getSeleccionados(), " max(ide_gemes) ", " where 1=1 "));
        TablaGenerica tab_mesf_desc = utilitario.consultar(ser_gerencial.getMes("0", tab_mes_max.getValor("valor")));
        // Extraer periodo
        TablaGenerica tab_periodo = utilitario.consultar(ser_gerencial.getAnio("true,false", "2", com_periodo_financiero.getValue().toString()));
        //Extraer tipo Balance
        String bal_inicial = "-1";
        String bal_mensual = "-2";
        TablaGenerica tab_lista = utilitario.consultar(ser_gerencial.getTipoBalance("2", lis_tipo_balance.getSeleccionados()));
        for (int i = 0; i < tab_lista.getTotalFilas(); i++) {
            if (tab_lista.getValor(i, "ide_getiba").equals("1")) {
                bal_inicial = "1";
            }
            if (tab_lista.getValor(i, "ide_getiba").equals("2")) {
                bal_mensual = "2";
            }
        }
        //Extraer Valores Totales
        TablaGenerica tab_totales = utilitario.consultar(ser_gerencial.getTotalBalanceComprobacion(com_periodo_financiero.getValue().toString(), bal_inicial, bal_mensual, seleccionado, lis_meses.getSeleccionados(), lis_tipo_balance.getSeleccionados(), tab_mes_max.getValor("valor")));

        parametro = new HashMap();
        TablaGenerica tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
        if (tab_datos.getTotalFilas() > 0) {
            parametro.put("logo", utilitario.getLogoEmpresa().getStream());
            parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
            parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
            parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
            parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
            parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));
        }

        parametro.put("titulo", "BALANCE COMPROBACIÓN CONSOLIDADO");
        parametro.put("pmeses", lis_meses.getSeleccionados());
        parametro.put("pmes_inicial", tab_mesi_desc.getValor("nombre_gemes"));
        parametro.put("pmes_final", tab_mesf_desc.getValor("nombre_gemes"));
        parametro.put("pnivel_inicial", Integer.parseInt(rad_nivel_inicial.getValue().toString()));
        parametro.put("pnivel_final", Integer.parseInt(rad_nivel_final.getValue().toString()));
        parametro.put("pperiodo", tab_periodo.getValor("nom_geani"));
        parametro.put("ptipo_balance", lis_tipo_balance.getSeleccionados());
        parametro.put("pbal_inicial", bal_inicial);
        parametro.put("pbal_mes", bal_mensual);
        parametro.put("pmes_periodo", tab_mes_max.getValor("valor"));
        parametro.put("pusuario", utilitario.getVariable("nick"));
        parametro.put("pobra", tab_obras.getValor("obra"));
        parametro.put("pide_obra", seleccionado);
        parametro.put("pide_geani", Integer.parseInt(com_periodo_financiero.getValue().toString()));
        parametro.put("pfirma1", utilitario.getVariable("p_ger_nom_firma1"));
        parametro.put("pcargo1", utilitario.getVariable("p_ger_cargo_firma1"));
        parametro.put("pfirma2", utilitario.getVariable("p_ger_nom_firma2"));
        parametro.put("pcargo2", utilitario.getVariable("p_ger_cargo_firma2"));
        parametro.put("pdebe_ini", Double.parseDouble(tab_totales.getValor("tot_debe_inicial")));
        parametro.put("phaber_ini", Double.parseDouble(tab_totales.getValor("tot_haber_incial")));
        parametro.put("pdebe_per", Double.parseDouble(tab_totales.getValor("tot_debe_per")));
        parametro.put("phaber_per", Double.parseDouble(tab_totales.getValor("tot_haber_per")));
        parametro.put("pdebe_acum", Double.parseDouble(tab_totales.getValor("tot_debe_acum")));
        parametro.put("phaber_acum", Double.parseDouble(tab_totales.getValor("tot_haber_acum")));
        parametro.put("pdebe_sal", Double.parseDouble(tab_totales.getValor("tot_debe_sal")));
        parametro.put("phaber_sal", Double.parseDouble(tab_totales.getValor("tot_haber_sal")));
        vipdf_mayor.setVisualizarPDF("rep_gerencial/rep_balance_compro_ger.jasper", parametro);
        vipdf_mayor.dibujar();

    }

    public void seleccionarTodas() {
        tab_consulta.setSeleccionados(null);
        Fila seleccionados[] = new Fila[tab_consulta.getTotalFilas()];
        for (int i = 0; i < tab_consulta.getFilas().size(); i++) {
            seleccionados[i] = tab_consulta.getFilas().get(i);
        }
        tab_consulta.setSeleccionados(seleccionados);
        //calculoTotal();

    }

    public void seleccinarInversa() {
        if (tab_consulta.getSeleccionados() == null) {
            seleccionarTodas();
        } else if (tab_consulta.getSeleccionados().length == tab_consulta.getTotalFilas()) {
            seleccionarNinguna();
        } else {
            Fila seleccionados[] = new Fila[tab_consulta.getTotalFilas() - tab_consulta.getSeleccionados().length];
            int cont = 0;
            for (int i = 0; i < tab_consulta.getFilas().size(); i++) {
                boolean boo_selecionado = false;
                for (int j = 0; j < tab_consulta.getSeleccionados().length; j++) {
                    if (tab_consulta.getSeleccionados()[j].equals(tab_consulta.getFilas().get(i))) {
                        boo_selecionado = true;
                        break;
                    }
                }
                if (boo_selecionado == false) {
                    seleccionados[cont] = tab_consulta.getFilas().get(i);
                    cont++;
                }
            }
            tab_consulta.setSeleccionados(seleccionados);
        }
        //calculoTotal();
    }

    public void seleccionarNinguna() {
        tab_consulta.setSeleccionados(null);

    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }

    public AutoCompletar getAut_cuenta() {
        return aut_cuenta;
    }

    public void setAut_cuenta(AutoCompletar aut_cuenta) {
        this.aut_cuenta = aut_cuenta;
    }

    public Tabla getTab_consulta() {
        return tab_consulta;
    }

    public void setTab_consulta(Tabla tab_consulta) {
        this.tab_consulta = tab_consulta;
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

    public AsientoContable getAsc_asiento() {
        return asc_asiento;
    }

    public void setAsc_asiento(AsientoContable asc_asiento) {
        this.asc_asiento = asc_asiento;
    }

    public Dialogo getDia_cerrar_periodo() {
        return dia_cerrar_periodo;
    }

    public void setDia_cerrar_periodo(Dialogo dia_cerrar_periodo) {
        this.dia_cerrar_periodo = dia_cerrar_periodo;
    }

    public AutoCompletar getAut_persona() {
        return aut_persona;
    }

    public void setAut_persona(AutoCompletar aut_persona) {
        this.aut_persona = aut_persona;
    }

    public VisualizarPDF getVipdf_mayor() {
        return vipdf_mayor;
    }

    public void setVipdf_mayor(VisualizarPDF vipdf_mayor) {
        this.vipdf_mayor = vipdf_mayor;
    }

    public Conexion getConPersistencia() {
        return conPersistencia;
    }

    public void setConPersistencia(Conexion conPersistencia) {
        this.conPersistencia = conPersistencia;
    }

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }

    public ListaSeleccion getLis_tipo_balance() {
        return lis_tipo_balance;
    }

    public void setLis_tipo_balance(ListaSeleccion lis_tipo_balance) {
        this.lis_tipo_balance = lis_tipo_balance;
    }

    public ListaSeleccion getLis_meses() {
        return lis_meses;
    }

    public void setLis_meses(ListaSeleccion lis_meses) {
        this.lis_meses = lis_meses;
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public GraficoCartesiano getGca_utilidad() {
        return gca_utilidad;
    }

    public void setGca_utilidad(GraficoCartesiano gca_utilidad) {
        this.gca_utilidad = gca_utilidad;
    }

    public GraficoPastel getGpa_utilidad() {
        return gpa_utilidad;
    }

    public void setGpa_utilidad(GraficoPastel gpa_utilidad) {
        this.gpa_utilidad = gpa_utilidad;
    }

    public SeleccionTabla getSel_casa_obras() {
        return sel_casa_obras;
    }

    public void setSel_casa_obras(SeleccionTabla sel_casa_obras) {
        this.sel_casa_obras = sel_casa_obras;
    }

    public Dialogo getDia_cerrar_balance() {
        return dia_cerrar_balance;
    }

    public void setDia_cerrar_balance(Dialogo dia_cerrar_balance) {
        this.dia_cerrar_balance = dia_cerrar_balance;
    }

    public Tabla getTab_tabla_traspaso() {
        return tab_tabla_traspaso;
    }

    public void setTab_tabla_traspaso(Tabla tab_tabla_traspaso) {
        this.tab_tabla_traspaso = tab_tabla_traspaso;
    }

    public Tabla getTab_tabla_traspaso_cabecera() {
        return tab_tabla_traspaso_cabecera;
    }

    public void setTab_tabla_traspaso_cabecera(Tabla tab_tabla_traspaso_cabecera) {
        this.tab_tabla_traspaso_cabecera = tab_tabla_traspaso_cabecera;
    }

    public Tabla getTab_tabla_cuenta_traspaso() {
        return tab_tabla_cuenta_traspaso;
    }

    public void setTab_tabla_cuenta_traspaso(Tabla tab_tabla_cuenta_traspaso) {
        this.tab_tabla_cuenta_traspaso = tab_tabla_cuenta_traspaso;
    }

}

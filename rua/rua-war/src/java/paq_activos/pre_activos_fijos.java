/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_activos;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionArbol;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import servicios.activos.ServicioActivosFijos;
import servicios.pensiones.ServicioPensiones;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class pre_activos_fijos extends Pantalla {

    private final MenuPanel mep_menu = new MenuPanel();

    private VisualizarPDF vipdf_acta = new VisualizarPDF();
    private final Calendario cal_fecha = new Calendario();
    private final Calendario cal_fecha_depreciacion = new Calendario();

    @EJB
    private final ServicioActivosFijos ser_activos = (ServicioActivosFijos) utilitario.instanciarEJB(ServicioActivosFijos.class);

    @EJB
    private ServicioPensiones ser_pensiones = (ServicioPensiones) utilitario.instanciarEJB(ServicioPensiones.class);
    
    private Tabla tab_tabla;
    private Tabla tab_tabla2;
    private Tabla tab_tabla3;
    private Tabla tab_tabla4;
    private Tabla tab_tabla5;
    private Tabla tab_tabla6;
    private AutoCompletar aut_custodio;

    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private SeleccionArbol sel_arb = new SeleccionArbol();
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();

    private final Etiqueta eti_cod_barras = new Etiqueta();
    private StreamedContent stcCodigoBarra;
    private int cantidad = 1;
    private Texto txt_cod;
    private final Tabulador tab_tabulador = new Tabulador();

    private SeleccionTabla set_selecciona = new SeleccionTabla();
    private SeleccionTabla sel_clase_activos = new SeleccionTabla();

    private Confirmar con_confirma = new Confirmar();

    private Dialogo dia_foto = new Dialogo();
    private Tabla tab_foto = new Tabla();
    
    private double valor_compra = 0;
    private double valor_reposicion = 0;
    private double valor_total = 0;
    private double valor_pendiente_depre = 0;
    private double valor_total_depre = 0;
    private String p_con_por_valor_residual = utilitario.getVariable("p_act_porcen_valor_residual");
    String ide_acafi = "";
    String fecha_compra = "";
    String fecha_depreciacion = "";
    private int vida_util = 0;
    private int dias_año = 365;
    private int numero_dias = 0;
    private double valor_deprecia_dias = 0;
    private double valor_depreciado = 0;
    private double resultado_sub = 0;
    private double valor_recidual = 0;
    private double valor_depreciado_final = 0;
    
    
    public pre_activos_fijos() {
        bar_botones.agregarReporte();

        mep_menu.setMenuPanel("OPCIONES ACTIVOS FIJOS", "21%");
        mep_menu.agregarItem("Listado de Activos", "dibujarListadosActivos", "ui-icon-note");//1                

        mep_menu.agregarItem("Listado Bienes de Control", "dibujarListadosBienesC", "ui-icon-note");//1       

        mep_menu.agregarItem("Depreciar Activos", "dibujarDepreciar", "ui-icon-clock");//6
        mep_menu.agregarItem("Dar de Baja Activos", "dibujarDardeBaja", "ui-icon-cancel");//7
        mep_menu.agregarSubMenu("ACTAS");
        mep_menu.agregarItem("Nueva Acta Constatación Física", "dibujarAsignarActivos", "ui-icon-document");//5
        mep_menu.agregarItem("Listado Actas Constatación Física", "dibujarListadoActasConstata", "ui-icon-note");//10
        mep_menu.agregarItem("Acta Entrega Recepción", "dibujarActa", "ui-icon-note");//9
        mep_menu.agregarSubMenu("INFORMES");
        mep_menu.agregarItem("Consultar por Código de Barras", "dibujarConsultarPorCodigoB", "ui-icon-contact");//11
        mep_menu.agregarItem("Consultar Activos Asignados", "dibujarConsultarAsignados", "ui-icon-contact");//3
        mep_menu.agregarItem("Activos Dados de Baja", "dibujarActDadosBaja", "ui-icon-note");//8        
        agregarComponente(mep_menu);
        dibujarListadosActivos();

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);

        sec_rango_reporte.setId("sec_rango_reporte");
        sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sec_rango_reporte.setMultiple(false);
        agregarComponente(sec_rango_reporte);

        sel_rep.setId("sel_rep");
        agregarComponente(sel_rep);
        sel_arb.setId("sel_arb");
        sel_arb.setSeleccionArbol("inv_articulo", "ide_inarti", "nombre_inarti", "inv_ide_inarti");
        sel_arb.getArb_seleccion().setCondicion("ide_inarti=" + utilitario.getVariable("p_inv_articulo_activo_fijo"));
        sel_arb.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(sel_arb);

        vipdf_acta.setId("vipdf_acta");
        vipdf_acta.setTitle("ACTA ENTREGA - RECEPCIÓN");
        agregarComponente(vipdf_acta);

        set_selecciona.setId("set_selecciona");
        set_selecciona.setSeleccionTabla(ser_activos.getSqlListaActivosFijos("-1"), "ide_acafi");//carga vacia
        set_selecciona.getTab_seleccion().getColumna("codigo_barras_acafi").setFiltroContenido();
        set_selecciona.getTab_seleccion().getColumna("TIPO_ACTIVO").setFiltroContenido();
        set_selecciona.getTab_seleccion().getColumna("ESTADO").setFiltroContenido();
        set_selecciona.getTab_seleccion().getColumna("CASA").setFiltroContenido();
        set_selecciona.getTab_seleccion().getColumna("OBRA").setFiltroContenido();
        set_selecciona.getTab_seleccion().getColumna("CLASE").setFiltroContenido();
        set_selecciona.getTab_seleccion().getColumna("AREA_UBICACION").setFiltroContenido();
        set_selecciona.getTab_seleccion().getColumna("codigo_barras_acafi").setFiltroContenido();
        set_selecciona.getTab_seleccion().getColumna("act_ide_acafi").setVisible(false);
        set_selecciona.setWidth("80%");
        set_selecciona.setHeight("70%");
        set_selecciona.setHeader("SELECCIONAR ACTIVO FIJO");
        agregarComponente(set_selecciona);
        
        sel_clase_activos.setId("sel_clase_activos");
        sel_clase_activos.setSeleccionTabla("select ide_accla,nombre_accla,codigo_accla from act_clase_activo order by codigo_accla", "ide_accla");
        sel_clase_activos.setWidth("40%");
        sel_clase_activos.setHeight("60%");
        sel_clase_activos.setHeader("CLASES DE ACTIVOS FIJOS");
        sel_clase_activos.getBot_aceptar().setMetodo("calcularValorGrupal");
        agregarComponente(sel_clase_activos);
        

        con_confirma.setId("con_confirma");
        con_confirma.setMessage("Está seguro que desea eliminar el Activo Fijo Seleccionado ?");
        con_confirma.setTitle("ELIMINAR ACTIVO FIJO");
        con_confirma.getBot_aceptar().setValue("Si");
        con_confirma.getBot_cancelar().setValue("No");
        agregarComponente(con_confirma);

        dia_foto.setId("dia_foto");
        dia_foto.setTitle("AGREGAR FOTO");
        dia_foto.setWidth("45%");
        dia_foto.setHeight("50%");
        agregarComponente(dia_foto);

        tab_foto.setId("tab_foto");
        tab_foto.setTabla("act_foto_activo", "ide_acfac", 99);
        tab_foto.setCondicion("ide_acafi=-1");
        tab_foto.getColumna("ide_acafi").setVisible(false);
        tab_foto.getColumna("ide_acfac").setVisible(false);
        tab_foto.setTipoFormulario(true);
        tab_foto.setMostrarNumeroRegistros(false);
        tab_foto.getColumna("foto_acfac").setUpload();
        tab_foto.getColumna("foto_acfac").setImagen("200", "200");
        tab_foto.getColumna("foto_acfac").setNombreVisual("FOTO");
        tab_foto.getColumna("foto_acfac").setRequerida(true);
        tab_foto.dibujar();
        PanelTabla patpanel = new PanelTabla();
        patpanel.setPanelTabla(tab_foto);
        patpanel.setStyle("overflow: hidden;");
        patpanel.getMenuTabla().setRendered(false);
        Grid gf = new Grid();
        gf.getChildren().add(patpanel);
        gf.setStyle("width:" + (dia_foto.getAnchoPanel() - 5) + "px;height:" + (dia_foto.getAltoPanel() - 10) + "px;overflow: hidden;display: block;");
        dia_foto.setDialogo(gf);
        dia_foto.getBot_aceptar().setMetodo("aceptarGuardarFoto");
        
        
    }

    public void aceptarGuardarFoto() {
        if (tab_foto.guardar()) {
            if (guardarPantalla().isEmpty()) {
                dia_foto.cerrar();
                tab_tabla4.actualizar();
            }
        }
    }

    public void abrirDialogoAgregarFoto() {
        dia_foto.dibujar();
        tab_foto.setCondicion("ide_acafi=-1");
        tab_foto.insertar();
        tab_foto.setValor("ide_acafi", tab_tabla.getValor("ide_acafi"));
    }

    public void dibujarConsultarPorCodigoB() {

        Grid gr = new Grid();
        gr.setColumns(2);
        gr.getChildren().add(new Etiqueta("<strong>CÓDIGO DE BARRAS :</strong>"));
        txt_cod = new Texto();
        txt_cod.setStyle("font-size:18px");
        txt_cod.setSize(50);
        gr.getChildren().add(txt_cod);

        Boton bot_generar = new Boton();
        bot_generar.setValue("Buscar");
        bot_generar.setIcon("ui-icon-search");
        bot_generar.setMetodo("cargaActivoFijoPorCodigo");
        gr.setFooter(bot_generar);
        mep_menu.dibujar(11, "BUSCAR ACTIVO FIJO POR CÓDIGO DE BARRAS", gr);
    }

    public void cargaActivoFijoPorCodigo() {
        if (txt_cod.getValue() == null) {
            utilitario.agregarMensajeError("Debe Ingresar el código de barras", "");
        }

        String ide_acafi = txt_cod.getValue().toString();
        if (ser_activos.isExisteCodBarras(ide_acafi)) {

            dibujarActivoFijo();
            tab_tabla.setCondicion("codigo_barras_acafi='" + ide_acafi + "'");
            tab_tabla.ejecutarSql();
            tab_tabla2.setSql(ser_activos.getSqlHistoriaAsignacionActivo(tab_tabla.getValor("ide_acafi")));
            tab_tabla2.ejecutarSql();
            tab_tabla3.setSql(ser_activos.getSqlTransaccionesActivo(tab_tabla.getValor("ide_acafi")));
            tab_tabla3.ejecutarSql();
            tab_tabla2.setRendered(true);
            tab_tabla3.setRendered(true);
            tab_tabla5.setSql(ser_activos.getSqlActivosHijoMasivo(tab_tabla.getValor("ide_acafi")));
            tab_tabla5.ejecutarSql();
            cargarCodigoBarras();
        } else {
            utilitario.agregarMensajeError("El código de barras no existe", "");
        }
    }

    public void dibujarActa() {
        Grid gru = new Grid();
        Grid gr = new Grid();
        gr.setColumns(2);
        gr.getChildren().add(new Etiqueta("<strong>CUSTODIO :</strong>"));
        aut_custodio = new AutoCompletar();
        aut_custodio.setId("aut_custodio");
        aut_custodio.setAutoCompletar("select ide_geper, nom_geper,identificac_geper from gen_persona where es_empleado_geper=true and ide_empr=" + utilitario.getVariable("ide_empr"));
        aut_custodio.setAutocompletarContenido();
        aut_custodio.setSize(70);

        gr.getChildren().add(aut_custodio);
        cal_fecha.setFechaActual();
        gr.getChildren().add(new Etiqueta("<strong>FECHA DEL ACTA :</strong>"));
        gr.getChildren().add(cal_fecha);
        gru.getChildren().add(gr);

        Boton bot_generar = new Boton();
        bot_generar.setValue("Generar Acta");
        bot_generar.setIcon("ui-icon-print");
        bot_generar.setMetodo("generarActa");
        gru.setFooter(bot_generar);
        mep_menu.dibujar(9, "ACTA DE ENTREGA - RECEPCIÓN", gru);
    }

    public void generarActa() {
        if (cal_fecha.getValue() == null) {
            utilitario.agregarMensajeInfo("Seleccione una fecha para generar el Acta", "");
            return;
        }
        if (aut_custodio.getValor() != null) {
            Map parametros_rep = new HashMap();
            parametros_rep.put("ide_geper", Integer.parseInt(aut_custodio.getValor()));
            parametros_rep.put("ide_usua", Integer.parseInt(utilitario.getVariable("ide_usua")));
            parametros_rep.put("dia", String.valueOf(utilitario.getDia(cal_fecha.getFecha())));
            parametros_rep.put("mes", utilitario.getNombreMes(utilitario.getMes(cal_fecha.getFecha())));
            parametros_rep.put("ano", String.valueOf(utilitario.getAnio(cal_fecha.getFecha())));
            vipdf_acta.setVisualizarPDF("rep_activos/rep_acta-entrega.jasper", parametros_rep);
            vipdf_acta.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Seleccione un custodio para generar el Acta", "");
        }
    }

    public void dibujarListadoActasConstata() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();

        Boton bot_ver = new Boton();
        bot_ver.setValue("Imprimir Acta");
        bot_ver.setMetodo("imprimirActaC");
        bot_ver.setIcon("ui-icon-print");
        bar_menu.agregarComponente(bot_ver);

        bar_menu.agregarSeparador();
        Boton bot_e = new Boton();
        bot_e.setValue("Editar Acta");
        bot_e.setMetodo("modificarActaConstata");
        bot_e.setIcon("ui-icon-pencil");
        bar_menu.agregarComponente(bot_e);

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_activos.getSqlListadoActasConstatacion());
        tab_tabla.setCampoPrimaria("ide_acact");
        tab_tabla.getColumna("ide_acact").setVisible(false);
        tab_tabla.setLectura(true);
        tab_tabla.setRows(20);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(10, "LISTADO DE ACTAS DE CONSTATCIÓN FÍSICA DE ACTIVOS FIJOS", gru);
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Activos Fijos")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sel_arb.dibujar();
                utilitario.addUpdate("rep_reporte,sel_arb");
            } else if (sel_arb.isVisible()) {
                System.out.println("ides " + sel_arb.getSeleccionados());
                parametro.put("ide_inarti", sel_arb.getSeleccionados());
                sel_arb.cerrar();
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("sel_arb,sel_rep");
            }

        } else if (rep_reporte.getReporteSelecionado().equals("Depreciacion Activos Fijos")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(false);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                parametro.put("fecha_fin", sec_rango_reporte.getFecha1());
                sec_rango_reporte.cerrar();
                sel_arb.dibujar();
                utilitario.addUpdate("rep_reporte,sel_arb");
            } else if (sel_arb.isVisible()) {
                System.out.println("ides " + sel_arb.getSeleccionados());
                parametro.put("ide_inarti", sel_arb.getSeleccionados());
                sel_arb.cerrar();
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("sel_arb,sel_rep");
            }

        } else if (rep_reporte.getReporteSelecionado().equals("Detalle Depreciacion Activos")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                parametro.put("fecha_ini", sec_rango_reporte.getFecha1());
                parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
                sec_rango_reporte.cerrar();
                sel_arb.dibujar();
                utilitario.addUpdate("rep_reporte,sel_arb");
            } else if (sel_arb.isVisible()) {
                System.out.println("ides " + sel_arb.getSeleccionados());
                parametro.put("ide_inarti", sel_arb.getSeleccionados());
                sel_arb.cerrar();
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("sel_arb,sel_rep");
            }
        }
    }

    public void dibujarDepreciar() {
        Grid grm = new Grid();
        grm.setColumns(3);
        grm.setStyle("font-size:14px;color:black;text-align:left;");
        grm.setMensajeInfo("Seleccione los parámetros para depreciar los activos");
        grm.getChildren().add(new Etiqueta("Fecha Valoración: "));
        cal_fecha_depreciacion.setId("cal_fecha_depreciacion");
        cal_fecha_depreciacion.setFechaActual();
        cal_fecha_depreciacion.setTipoBoton(true);
        Boton bot_valorar = new Boton();
        bot_valorar.setIcon("ui-icon-print");
        bot_valorar.setValue("Valorar Depreciación");
        bot_valorar.setMetodo("dibujarSeleccionClaseActivos");
        
        grm.getChildren().add(cal_fecha_depreciacion);
        grm.getChildren().add(bot_valorar);
        mep_menu.dibujar(6, "DEPRECIAR ACTIVOS FIJOS", grm);
    }
    
    public void calcularValorGrupal(){
        String valores_seleccionados = sel_clase_activos.getSeleccionados();
        TablaGenerica tab_depreciaciones_consulta = utilitario.consultar("select ide_acafi, ide_aceaf, ide_accla, deprecia_acafi, valor_compra_acafi, fecha_compra_acafi,vida_util_acafi, valor_reposicion_acafi, recidual_acafi from act_activo_fijo where ide_accla in ("+valores_seleccionados+") "
                                                                       + "and ide_aceaf in("+utilitario.getVariable("p_act_estado_activo_valora_deprec")+") and deprecia_acafi = false ");
        
        try{
            for (int i=0; i<tab_depreciaciones_consulta.getTotalFilas() ; i++){
            ide_acafi = tab_depreciaciones_consulta.getValor(i,"ide_acafi");
            fecha_compra = tab_depreciaciones_consulta.getValor(i, "fecha_compra_acafi");
            vida_util = Integer.parseInt(tab_depreciaciones_consulta.getValor(i, "vida_util_acafi"));
            valor_compra = Double.parseDouble(tab_depreciaciones_consulta.getValor(i, "valor_compra_acafi"));
            valor_reposicion = Double.parseDouble(tab_depreciaciones_consulta.getValor(i, "valor_reposicion_acafi"));
            valor_total = valor_compra + valor_reposicion;
            ser_activos.calcularTotalResidual(valor_total, p_con_por_valor_residual, ide_acafi);
            valor_recidual = Double.parseDouble(tab_depreciaciones_consulta.getValor(i, "recidual_acafi"));
            calculaDepreciacion();
        }
        sel_clase_activos.cerrar();
        utilitario.agregarMensaje("Se ha depreciado correctamente", "");
        }
        catch(Exception e){
         utilitario.agregarMensajeError("No se puedo guardar", "Faltan Asignar parámetros"+e);   
        }
    }
    public void calculaDepreciacion(){
        valor_pendiente_depre = 0;
        TablaGenerica tab_depreciacion = utilitario.consultar("select ide_acdepr, ide_acafi, fecha_acdepr, valor_acdepr, valor_compra_acdepr, validado_depre_acdepr \n" +
                                                              "from act_depreciacion \n" +
                                                              "where validado_depre_acdepr = true \n" +
                                                              "and ide_acafi in ("+ide_acafi+")");
        TablaGenerica tab_fecha = utilitario.consultar("select ide_acafi, max(fecha_acdepr) as fecha_maxima from act_depreciacion "
                                                        + "where ide_acafi = "+ide_acafi+" group by ide_acafi");
        TablaGenerica codigo_maximo = utilitario.consultar(ser_pensiones.getCodigoMaximoTabla("act_depreciacion", "ide_acdepr"));
        String maximo = codigo_maximo.getValor("maximo");
        String fecha_maxima = tab_fecha.getValor("fecha_maxima");
       // if (tab_depreciacion.getTotalFilas() > 0){
        String valor_acdepr = tab_depreciacion.getValor("valor_acdepr");
        String fecha_deprecia = tab_depreciacion.getValor("fecha_acdepr");
        if (valor_acdepr == null){
            valor_total_depre = 0;
        } else {
            valor_total_depre = Double.parseDouble(valor_acdepr);
        }
        if (fecha_deprecia == null){
            fecha_depreciacion = fecha_compra;
        } else {
            fecha_depreciacion = fecha_maxima;   
        }
        try{
            valor_pendiente_depre = valor_total - valor_total_depre;
            int num_dias = utilitario.getDiferenciasDeFechas(utilitario.getFecha(fecha_depreciacion), utilitario.getFecha(cal_fecha_depreciacion.getFecha()));
            numero_dias = vida_util * dias_año;
            valor_deprecia_dias = valor_total / numero_dias;
            valor_depreciado = valor_deprecia_dias * num_dias;
            resultado_sub = valor_depreciado + valor_pendiente_depre;
            if (resultado_sub >= valor_recidual){
               valor_depreciado_final = valor_depreciado;
            } else {
                valor_depreciado_final = valor_recidual;
            }
           /* System.out.println("valor_recidual "+valor_recidual);
            System.out.println("dias "+num_dias);
            System.out.println("numero_dias "+numero_dias);
            System.out.println("valor_total "+valor_total);
            System.out.println("valor_deprecia_dias "+valor_deprecia_dias);
            System.out.println("valor_depreciado "+valor_depreciado);
            System.out.println("resultado "+resultado_sub);
            System.out.println("valor_depreciado_final "+valor_depreciado_final);
            System.out.println("cal_fecha_depreciacion "+fecha_depreciacion);*/
            utilitario.getConexion().ejecutarSql("INSERT INTO act_depreciacion (ide_acdepr, ide_acafi, fecha_acdepr, valor_acdepr, valor_reposicion_acdepr, valor_residual_acdepr, valor_compra_acdepr, validado_depre_acdepr)\n" +
                                                 "VALUES ("+maximo+", "+ide_acafi+", '"+cal_fecha_depreciacion.getFecha()+"', "+valor_depreciado_final+", "+valor_reposicion+", "+valor_recidual+", "+valor_compra+", false );");
        } catch(Exception e){
        }
        /*System.out.println("valor_depreciacion "+valor_total_depre);
        System.out.println("fecha_depreciacion "+fecha_depreciacion);
        System.out.println("valor_pendiente_depre "+valor_pendiente_depre);
        System.out.println("fecha_compra "+fecha_compra);
        System.out.println("fecha_maxima "+fecha_maxima);*/
      /*  } else{
            
        }*/
    }
    public void dibujarSeleccionClaseActivos(){
        sel_clase_activos.dibujar();
    }
    
    public void dibujarDardeBaja() {
        Grupo gru = new Grupo();

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("act_transaccion", "ide_actra", -1);
        tab_tabla.getColumna("ide_actra").setVisible(false);
        tab_tabla.getColumna("ide_acafi").setVisible(false);
        tab_tabla.getColumna("ide_usua").setVisible(false);
        tab_tabla.getColumna("ide_cnccc").setVisible(false);
        tab_tabla.getColumna("acumulado_actra").setVisible(false);
        tab_tabla.getColumna("valor_actra").setVisible(false);
        tab_tabla.getColumna("valor_activo_actra").setVisible(false);
        tab_tabla.getColumna("cantidad_actra").setVisible(false);
        tab_tabla.getColumna("ide_acttr").setCombo("act_tipo_transaccion", "ide_acttr", "nombre_acttr", "ide_acttr=1");
        tab_tabla.getColumna("ide_acttr").setValorDefecto("1"); //Dar de baja
        tab_tabla.getColumna("ide_acttr").setLectura(true);
        tab_tabla.getColumna("ide_acttr").setNombreVisual("TIPO TRANSACCION");
        tab_tabla.getColumna("fecha_actra").setNombreVisual("FECHA");
        tab_tabla.getColumna("fecha_actra").setValorDefecto(utilitario.getFechaActual());
        tab_tabla.getColumna("observacion_actra").setNombreVisual("OBSERVACION");
        //tab_tabla.getColumna("valor_actra").setNombreVisual("VALOR TRANSACCION");
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.setMostrarNumeroRegistros(false);
        tab_tabla.dibujar();
        tab_tabla.insertar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        pat_panel.getMenuTabla().setRendered(false);
        gru.getChildren().add(pat_panel);
        gru.getChildren().add(new Separator());

        tab_tabla2 = new Tabla();

        tab_tabla2.setId("tab_seleccion");
        tab_tabla2.setSql(ser_activos.getSqlListaActivosFijosDarBaja());
        tab_tabla2.setCampoPrimaria("ide_acafi");
        tab_tabla2.getColumna("ide_acafi").setVisible(false);
        tab_tabla2.getColumna("act_ide_acafi").setVisible(false);
        tab_tabla2.getColumna("nom_geper").setFiltroContenido();
        //tab_tabla.getColumna("codigo_barras_acafi").setFiltroContenido(); 
        tab_tabla2.getColumna("nom_geper").setNombreVisual("CUSTODIO");
        tab_tabla2.getColumna("TIPO_ACTIVO").setFiltroContenido();
        tab_tabla2.getColumna("ESTADO").setFiltroContenido();
        tab_tabla2.getColumna("CASA").setFiltroContenido();
        tab_tabla2.getColumna("OBRA").setFiltroContenido();
        tab_tabla2.getColumna("CLASE").setFiltroContenido();
        tab_tabla2.getColumna("anos_uso_acafi").alinearDerecha();
        tab_tabla2.getColumna("anos_uso_acafi").setNombreVisual("AÑOS DE USO");
        tab_tabla2.getColumna("vida_util_acafi").alinearDerecha();
        tab_tabla2.getColumna("cantidad").alinearDerecha();
        tab_tabla2.getColumna("vida_util_acafi").setNombreVisual("VIDA UTIL");
        tab_tabla2.getColumna("valor_compra_acafi").alinearDerecha();
        tab_tabla2.getColumna("valor_comercial_acafi").alinearDerecha();
        tab_tabla2.getColumna("valor_remate_acafi").alinearDerecha();
        tab_tabla2.setTipoSeleccion(true);
        tab_tabla2.setSeleccionTabla("multiple");
        tab_tabla2.setRows(10);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        Grid grm = new Grid();
        grm.setMensajeInfo("Seleccionar los activos que se van a dar de baja");
        gru.getChildren().add(grm);

        gru.getChildren().add(pat_panel2);

        mep_menu.dibujar(7, "DAR DE BAJA ACTIVOS FIJOS", gru);
    }

    public void dibujarActDadosBaja() {
        //8
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_activos.getSqlActivosDadosdeBaja());
        tab_tabla.setCampoPrimaria("baja_ide_acafi");
        tab_tabla.getColumna("baja_ide_acafi").setVisible(false);
        tab_tabla.getColumna("descripcion").setFiltroContenido();
        tab_tabla.getColumna("serie").setFiltroContenido();
        tab_tabla.setRows(20);
        tab_tabla.setLectura(true);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        Grupo gru = new Grupo();

        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(8, "ACTIVOS FIJOS DADOS DE BAJA", gru);
    }

    public void dibujarConsultarAsignados() {
        //3        
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_activos.getSqlActivosAsignados("-1"));
        tab_tabla.setCampoPrimaria("baja_ide_acafi");
        tab_tabla.getColumna("baja_ide_acafi").setVisible(false);
        tab_tabla.getColumna("descripcion").setFiltroContenido();
        tab_tabla.getColumna("serie").setFiltroContenido();
        tab_tabla.setRows(20);
        tab_tabla.setLectura(true);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        Grupo gru = new Grupo();

        Grid gr = new Grid();
        gr.setColumns(2);
        gr.getChildren().add(new Etiqueta("<strong>CUSTODIO :</strong>"));
        aut_custodio = new AutoCompletar();
        aut_custodio.setId("aut_custodio");
        aut_custodio.setAutoCompletar("select ide_geper, nom_geper,identificac_geper from gen_persona where es_empleado_geper=true and ide_empr=" + utilitario.getVariable("ide_empr"));
        aut_custodio.setAutocompletarContenido();
        aut_custodio.setMetodoChange("cargarActivosAsignados");
        gr.getChildren().add(aut_custodio);
        gru.getChildren().add(gr);
        gru.getChildren().add(new Separator());
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(3, "CONSULTAR ACTIVOS ASIGNADOS", gru);

    }

    public void cargarActivosAsignados(SelectEvent evt) {
        aut_custodio.onSelect(evt);
        if (aut_custodio.getValor() != null) {
            tab_tabla.setSql(ser_activos.getSqlActivosAsignados(aut_custodio.getValor()));
            tab_tabla.ejecutarSql();

        }

    }

    public void dibujarAsignarActivos() {
        Grupo gru = new Grupo();
        strActivosAsignados = "";
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("act_acta_constata", "ide_acact", 13);
        tab_tabla.getColumna("ide_acact").setVisible(false);
        tab_tabla.getColumna("ide_usua").setVisible(false);
        tab_tabla.getColumna("secuencial_acact").setVisible(false);
        tab_tabla.getColumna("codigo_acact").setLectura(true);
        tab_tabla.getColumna("codigo_acact").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_tabla.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_empleado_geper=true");
        tab_tabla.getColumna("ide_geper").setAutoCompletar();
        tab_tabla.getColumna("ide_geper").setRequerida(true);
        tab_tabla.getColumna("fecha_asigna_acact").setValorDefecto(utilitario.getFechaActual());
        tab_tabla.getColumna("fecha_asigna_acact").setRequerida(true);
        tab_tabla.getColumna("ide_gecas").setCombo("select ide_gecas,nombre_gecas,codigo_gecas from gen_casa order by codigo_gecas");
        tab_tabla.getColumna("ide_gecas").setRequerida(true);
        tab_tabla.getColumna("ide_geobr").setCombo(" select ide_geobr,nombre_geobr,codigo_geobr from  gen_obra order by codigo_geobr");
        tab_tabla.getColumna("ide_geobr").setRequerida(true);
        tab_tabla.getColumna("ide_acuba").setCombo("select ide_acuba,nombre_acuba,codigo_acuba from act_ubicacion_activo order by codigo_acuba");
        tab_tabla.getColumna("ide_acuba").setRequerida(true);
        tab_tabla.getColumna("observacion_acact").setControl("Texto");
        tab_tabla.getColumna("observacion_acact").setRequerida(true);
        tab_tabla.setCondicion("ide_acact=-1");
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.setMostrarNumeroRegistros(false);
        tab_tabla.dibujar();
        tab_tabla.insertar();
        tab_tabla.setValor("secuencial_acact", String.valueOf(ser_activos.getSecuencialActaConstatacion()));
        tab_tabla.setValor("codigo_acact", "ACT-" + String.valueOf(ser_activos.getSecuencialActaConstatacion()));
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        pat_panel.getMenuTabla().getItem_insertar().setRendered(false);
        pat_panel.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        gru.getChildren().add(pat_panel);
        //gru.getChildren().add(new Separator());

        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();

        Boton bot_ver = new Boton();
        bot_ver.setValue("Agregar Activo Fijo");
        bot_ver.setMetodo("abrirAgregarActivo");
        bot_ver.setIcon("ui-icon-plusthick");
        bar_menu.agregarComponente(bot_ver);
        gru.getChildren().add(bar_menu);
        bar_menu.agregarSeparador();
        Boton bot_q = new Boton();
        bot_q.setValue("Eliminar Activo Fijo");
        bot_q.setMetodo("quitarActivoFijo");
        bot_q.setIcon("ui-icon-minusthick");
        bar_menu.agregarComponente(bot_q);
        bar_menu.agregarSeparador();
        Boton bot_r = new Boton();
        bot_r.setValue("Reasignar");
        bot_r.setMetodo("reasignarActivoFijo");
        bot_r.setIcon("ui-icon-arrowrefresh-1-s");
        bar_menu.agregarComponente(bot_r);

        tab_tabla2 = new Tabla();

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setSql(ser_activos.getSqlListaActivosFijos("-1"));
        tab_tabla2.setCampoPrimaria("ide_acafi");
        tab_tabla2.getColumna("ide_acafi").setVisible(false);
        tab_tabla2.getColumna("act_ide_acafi").setVisible(false);
        tab_tabla2.getColumna("anos_uso_acafi").alinearDerecha();
        tab_tabla2.getColumna("anos_uso_acafi").setNombreVisual("AÑOS DE USO");
        tab_tabla2.getColumna("vida_util_acafi").alinearDerecha();
        tab_tabla2.getColumna("cantidad").alinearDerecha();
        tab_tabla2.getColumna("vida_util_acafi").setNombreVisual("VIDA UTIL");
        tab_tabla2.getColumna("valor_compra_acafi").alinearDerecha();
        tab_tabla2.getColumna("valor_comercial_acafi").alinearDerecha();
        tab_tabla2.getColumna("valor_remate_acafi").alinearDerecha();
        tab_tabla2.setLectura(true);
        tab_tabla2.setScrollable(true);
        tab_tabla2.setScrollHeight(300);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        gru.getChildren().add(pat_panel2);

        mep_menu.dibujar(5, "ACTA DE CONSTATACION FÍSICA DE ACTIVOS FIJOS", gru);
    }

    public void modificarActaConstata() {
        String ide_acact = tab_tabla.getValor("ide_acact");
        if (ide_acact != null) {
            dibujarAsignarActivos();
            tab_tabla.setCondicion("ide_acact=" + ide_acact);
            tab_tabla.ejecutarSql();
            TablaGenerica tg = utilitario.consultar("select ide_acaaf,ide_acafi,ide_acact from act_asignacion_activo where ide_acact=" + ide_acact);
            if (tg.isEmpty() == false) {
                tab_tabla2.setSql(ser_activos.getSqlListaActivosFijos(tg.getStringColumna("ide_acafi")));
                tab_tabla2.ejecutarSql();
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione una Acta", "");
        }

    }

    public void abrirAgregarActivo() {
        set_selecciona.getBot_aceptar().setMetodo("agregarActivoAsigna");
        boolean bienes = false;
        if (tab_tabla.getValor("bienes_control_acact").equals("true")) {
            set_selecciona.setHeader("SELECCIONAR BIEN DE CONTROL");
            bienes = true;
        } else {
            set_selecciona.setHeader("SELECCIONAR ACTIVO FIJO");
        }
        set_selecciona.getTab_seleccion().setSql(ser_activos.getSqlListaActivosFijosSinCustodio(bienes));
        set_selecciona.getTab_seleccion().ejecutarSql();
        set_selecciona.dibujar();

    }

    public void reasignarActivoFijo() {
        set_selecciona.getBot_aceptar().setMetodo("agregarActivoAsigna");
        boolean bienes = false;
        if (tab_tabla.getValor("bienes_control_acact").equals("true")) {
            set_selecciona.setHeader("SELECCIONAR BIEN DE CONTROL");
            bienes = true;
        } else {
            set_selecciona.setHeader("SELECCIONAR ACTIVO FIJO");
        }
        set_selecciona.getTab_seleccion().setSql(ser_activos.getSqlListaActivosFijosReasignar(bienes));
        set_selecciona.getTab_seleccion().ejecutarSql();
        set_selecciona.dibujar();
    }

    private String strActivosAsignados = "";

    public void agregarActivoAsigna() {
        if (set_selecciona.getSeleccionados() != null) {
            if (tab_tabla.isFilaInsertada() == false) {
                strActivosAsignados = tab_tabla2.getStringColumna("ide_acafi");
                if (strActivosAsignados == null) {
                    strActivosAsignados = "";
                }
            }
            if (strActivosAsignados.isEmpty() == false) {
                strActivosAsignados += ",";
            }
            strActivosAsignados += set_selecciona.getSeleccionados();
            tab_tabla2.setSql(ser_activos.getSqlListaActivosFijos(strActivosAsignados));
            tab_tabla2.ejecutarSql();
            set_selecciona.cerrar();
        } else {
            utilitario.agregarMensajeError("Seleccione un activo fijo", "");
        }
    }

    public void quitarActivoFijo() {
        if (tab_tabla2.getFilaSeleccionada() != null) {
            if (tab_tabla.isFilaInsertada() == false) {
                utilitario.getConexion().agregarSqlPantalla("update act_activo_fijo set ide_geper=null where ide_acafi =" + tab_tabla2.getValor("ide_acafi"));
            }
            tab_tabla2.getFilas().remove(tab_tabla2.getFilaActual());
            utilitario.addUpdate("tab_tabla2");

        } else {
            utilitario.agregarMensajeError("Seleccione un activo fijo", "");
        }
    }

    public void dibujarListadosBienesC() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();

        Boton bot_ver = new Boton();
        bot_ver.setValue("Datos Bien de Control");
        bot_ver.setIcon("ui-icon-search");
        bot_ver.setMetodo("cargarActivoFijo");
        bar_menu.agregarComponente(bot_ver);
        bar_menu.agregarSeparador();
        Boton bot_elimina = new Boton();
        bot_elimina.setValue("Eliminar Bien de Control");
        bot_elimina.setIcon("ui-icon-cancel");
        bot_elimina.setMetodo("abrirEliminarActivoFijo");
        bar_menu.agregarComponente(bot_elimina);

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_activos.getSqlListaBienesControl());
        tab_tabla.setCampoPrimaria("ide_acafi");
        tab_tabla.getColumna("ide_acafi").setVisible(true);
        tab_tabla.getColumna("ide_acafi").setNombreVisual("CODIGO");
        tab_tabla.getColumna("ide_acafi").setFiltro(true);
        tab_tabla.getColumna("act_ide_acafi").setVisible(false);
        tab_tabla.getColumna("nom_geper").setFiltroContenido();
        tab_tabla.getColumna("codigo_barras_acafi").setFiltroContenido();
        tab_tabla.getColumna("nom_geper").setNombreVisual("CUSTODIO");
        tab_tabla.getColumna("TIPO_ACTIVO").setFiltroContenido();
        tab_tabla.getColumna("ESTADO").setFiltroContenido();
        tab_tabla.getColumna("CASA").setFiltroContenido();
        tab_tabla.getColumna("OBRA").setFiltroContenido();
        tab_tabla.getColumna("CLASE").setFiltroContenido();
        tab_tabla.getColumna("AREA_UBICACION").setFiltroContenido();
        tab_tabla.getColumna("SECUENCIAL").setFiltro(true);
        tab_tabla.getColumna("SECUENCIAL").setAncho(2);
        tab_tabla.getColumna("SECUENCIAL").setLongitud(2);
        tab_tabla.getColumna("anos_uso_acafi").alinearDerecha();
        tab_tabla.getColumna("anos_uso_acafi").setNombreVisual("AÑOS DE USO");
        tab_tabla.getColumna("vida_util_acafi").alinearDerecha();
        tab_tabla.getColumna("cantidad").alinearDerecha();
        tab_tabla.getColumna("vida_util_acafi").setNombreVisual("VIDA UTIL");
        tab_tabla.getColumna("valor_compra_acafi").alinearDerecha();
        tab_tabla.getColumna("valor_comercial_acafi").alinearDerecha();
        tab_tabla.getColumna("valor_remate_acafi").alinearDerecha();
        tab_tabla.setLectura(true);
        tab_tabla.setRows(20);

        //COLOR verde cantidad diferente de  1
        tab_tabla.setValueExpression("rowStyleClass", "fila.campos[5] eq '1'  ? null : 'text-green'");
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);

        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(20, "LISTADO BIENES DE CONTROL", gru);
    }

    public void dibujarListadosActivos() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();

        Boton bot_ver = new Boton();
        bot_ver.setValue("Datos Activo Fijo");
        bot_ver.setIcon("ui-icon-search");
        bot_ver.setMetodo("cargarActivoFijo");
        bar_menu.agregarComponente(bot_ver);
        bar_menu.agregarSeparador();
        Boton bot_elimina = new Boton();
        bot_elimina.setValue("Eliminar Activo Fijo");
        bot_elimina.setIcon("ui-icon-cancel");
        bot_elimina.setMetodo("abrirEliminarActivoFijo");
        bar_menu.agregarComponente(bot_elimina);

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_activos.getSqlListaActivosFijos());
        tab_tabla.setCampoPrimaria("ide_acafi");
        tab_tabla.getColumna("ide_acafi").setVisible(true);
        tab_tabla.getColumna("ide_acafi").setNombreVisual("CODIGO");
        tab_tabla.getColumna("ide_acafi").setFiltro(true);
        tab_tabla.getColumna("act_ide_acafi").setVisible(false);
        tab_tabla.getColumna("nom_geper").setFiltroContenido();
        tab_tabla.getColumna("codigo_barras_acafi").setFiltroContenido();
        tab_tabla.getColumna("nom_geper").setNombreVisual("CUSTODIO");
        tab_tabla.getColumna("TIPO_ACTIVO").setFiltroContenido();
        tab_tabla.getColumna("ESTADO").setFiltroContenido();
        tab_tabla.getColumna("CASA").setFiltroContenido();
        tab_tabla.getColumna("OBRA").setFiltroContenido();
        tab_tabla.getColumna("CLASE").setFiltroContenido();
        tab_tabla.getColumna("AREA_UBICACION").setFiltroContenido();
        tab_tabla.getColumna("SECUENCIAL").setFiltro(true);
        tab_tabla.getColumna("SECUENCIAL").setAncho(2);
        tab_tabla.getColumna("SECUENCIAL").setLongitud(2);
        tab_tabla.getColumna("anos_uso_acafi").alinearDerecha();
        tab_tabla.getColumna("anos_uso_acafi").setNombreVisual("AÑOS DE USO");
        tab_tabla.getColumna("vida_util_acafi").alinearDerecha();
        tab_tabla.getColumna("cantidad").alinearDerecha();
        tab_tabla.getColumna("vida_util_acafi").setNombreVisual("VIDA UTIL");
        tab_tabla.getColumna("valor_compra_acafi").alinearDerecha();
        tab_tabla.getColumna("valor_comercial_acafi").alinearDerecha();
        tab_tabla.getColumna("valor_remate_acafi").alinearDerecha();
        tab_tabla.setLectura(true);
        tab_tabla.setRows(20);

        //COLOR verde cantidad diferente de  1
        tab_tabla.setValueExpression("rowStyleClass", "fila.campos[5] eq '1'  ? null : 'text-green'");
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);

        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(4, "LISTADO DE ACTIVOS FIJOS", gru);
    }

    public void dibujarActivoFijo() {
        cantidad = 1;
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("act_activo_fijo", "ide_acafi", 1);
        tab_tabla.getColumna("ide_acafi").setNombreVisual("CODIGO");
        tab_tabla.getColumna("ide_inarti").setCombo("select ide_inarti,nombre_inarti,codigo_inarti from  inv_articulo  where ide_intpr=0 order by codigo_inarti"); //SOLO ACTIVOS FIJOS
        tab_tabla.getColumna("ide_inarti").setMetodoChange("generarCodigoBarras");
        tab_tabla.getColumna("gen_ide_geper").setVisible(false);
        tab_tabla.getColumna("ide_rheor").setVisible(false);
        tab_tabla.getColumna("alterno_acafi").setVisible(false);
        tab_tabla.getColumna("fo_acafi").setVisible(false);
        tab_tabla.getColumna("ide_geubi").setCombo("gen_ubicacion", "ide_geubi", "nombre_geubi", "nivel_geubi='HIJO'");
        tab_tabla.getColumna("ide_usua").setVisible(false);
        tab_tabla.getColumna("ide_aceaf").setCombo("act_estado_activo_fijo", "ide_aceaf", "nombre_aceaf", "");
        tab_tabla.getColumna("ide_aceaf").setRequerida(true);
        tab_tabla.getColumna("ide_inmar").setCombo("inv_marca", "ide_inmar", "nombre_invmar", "");
        // tab_tabla.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_empleado_geper=true");
        //tab_tabla.getColumna("ide_geper").setLectura(true);
        //tab_tabla.getColumna("ide_geper").setAutoCompletar();
        tab_tabla.getColumna("ide_geper").setVisible(false);
        tab_tabla.getColumna("ide_acuba").setCombo("select ide_acuba,nombre_acuba,codigo_acuba from act_ubicacion_activo order by codigo_acuba");
        tab_tabla.getColumna("ide_acuba").setRequerida(true);
        tab_tabla.getColumna("ide_acuba").setMetodoChange("generarCodigoBarras");
        tab_tabla.getColumna("serie_acafi").setRequerida(false);///antes true
        tab_tabla.getColumna("nombre_acafi").setRequerida(false);
        tab_tabla.getColumna("ide_inarti").setRequerida(true);
        tab_tabla.getColumna("nombre_acafi").setVisible(false);
        tab_tabla.getColumna("vida_util_acafi").setLectura(false);
        tab_tabla.getColumna("valor_compra_acafi").setLectura(false);
        tab_tabla.getColumna("recidual_acafi").setVisible(true);
        tab_tabla.getColumna("credi_tribu_acafi").setVisible(false);
        tab_tabla.getColumna("fecha_compra_acafi").setLectura(false);
        tab_tabla.getColumna("fecha_acafi").setLectura(true);
        tab_tabla.getColumna("fecha_acafi").setValorDefecto(utilitario.getFechaActual());
        tab_tabla.getColumna("VALOR_REPOSICION_ACAFI").setNombreVisual("VALOR DE REPOSICION");
        tab_tabla.getColumna("VALOR_REPOSICION_ACAFI").alinearDerecha();
        tab_tabla.getColumna("VALOR_REPOSICION_ACAFI").setValorDefecto("0");
        tab_tabla.getColumna("deprecia_acafi").setValorDefecto("false");
        tab_tabla.getColumna("deprecia_acafi").setLectura(true);
        tab_tabla.getColumna("recidual_acafi").setLectura(true);

        tab_tabla.getColumna("ide_actac").setVisible(true);
        tab_tabla.getColumna("ide_actac").setCombo("act_tipo_adquisicion", "ide_actac", "nombre_actac", "");

        tab_tabla.getColumna("act_ide_acafi").setVisible(false);///SI ES ASIGNACION MASIVA

        tab_tabla.getColumna("ide_gecas").setCombo("select ide_gecas,nombre_gecas,codigo_gecas from gen_casa order by codigo_gecas");
        tab_tabla.getColumna("ide_gecas").setRequerida(true);
        tab_tabla.getColumna("ide_gecas").setMetodoChange("generarCodigoBarras");
        tab_tabla.getColumna("ide_geobr").setCombo(" select ide_geobr,nombre_geobr,codigo_geobr from  gen_obra order by codigo_geobr");
        tab_tabla.getColumna("ide_geobr").setRequerida(true);
        tab_tabla.getColumna("ide_geobr").setMetodoChange("generarCodigoBarras");
        tab_tabla.getColumna("ide_accla").setCombo("select ide_accla,nombre_accla,codigo_accla from act_clase_activo order by codigo_accla");
        tab_tabla.getColumna("ide_accla").setRequerida(true);
        tab_tabla.getColumna("ide_accla").setMetodoChange("generarCodigoBarras");
        tab_tabla.getColumna("cantidad_acafi").setValorDefecto("1");
        tab_tabla.getColumna("cantidad_acafi").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla.getColumna("cantidad_acafi").setMetodoChange("cambioCantidad");

        tab_tabla.getColumna("ano_actual_acafi").setVisible(false);
        tab_tabla.getColumna("ano_actual_acafi").setValorDefecto(utilitario.getFechaActual());

        tab_tabla.getColumna("codigo_barras_acafi").setVisible(false);
        tab_tabla.getColumna("codigo_barras_acafi").setRequerida(true);

        tab_tabla.getColumna("fd_acafi").setVisible(false);
        tab_tabla.getColumna("fecha_fabrica_acafi").setVisible(false);
        tab_tabla.getColumna("mediadas_acafi").setVisible(false);
        tab_tabla.getColumna("custodio_tmp").setVisible(false);
        tab_tabla.getColumna("cuenta_ant_sistema").setVisible(false);
        tab_tabla.getColumna("descripcion1_acafi").setVisible(false);
        tab_tabla.getColumna("color_acafi").setVisible(false);
        tab_tabla.getColumna("codigo_recu_acafi").setVisible(false);
        tab_tabla.getColumna("sec_masivo_acafi").setVisible(false);
        tab_tabla.getColumna("ide_cpcfa").setVisible(false);
//        tab_tabla.agregarRelacion(tab_tabla6);

        tab_tabla.getColumna("ide_accls").setCombo("act_clasificacion", "ide_accls", "nombre_accls", "");

        tab_tabla.getGrid().setColumns(4);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.setCondicion("ide_acafi=-1");
        tab_tabla.setMostrarNumeroRegistros(false);
        tab_tabla.dibujar();
        tab_tabla.setValidarInsertar(false);
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla);

        tab_tabulador.getChildren().clear();
        tab_tabulador.setTransient(true);
        tab_tabulador.setId("tab_tabulador");

        Grid gri_cb = new Grid();
        gri_cb.setId("gri_cb");
        gri_cb.setWidth("98%");
        gri_cb.setStyle("width: 98%;");
        gri_cb.getChildren().add(new Etiqueta("<div align='center'>"));
        eti_cod_barras.setStyle("font-size: 13px;font-weight: bold;");
        eti_cod_barras.setValue(tab_tabla.getValor("codigo_barras_acafi"));
        gri_cb.getChildren().add(eti_cod_barras);
        Imagen ima_barra = new Imagen();
        ima_barra.setValueExpression("value", "pre_index.clase.stcCodigoBarra");
        gri_cb.getChildren().add(ima_barra);
        tab_tabulador.agregarTab("CODIGO DE BARRAS", gri_cb);

        Boton bot_imprimir = new Boton();
        bot_imprimir.setValue("Imprimir");
        bot_imprimir.setIcon("ui-icon-print");
        bot_imprimir.setMetodo("imprimirCodigoBarra");
        gri_cb.getChildren().add(bot_imprimir);

        gri_cb.getChildren().add(new Etiqueta("</div>"));
        
        
       
        
        
        
        
        tab_tabla4 = new Tabla();
        tab_tabla4.setId("tab_tabla4");
        tab_tabla4.setIdCompleto("tab_tabulador:tab_tabla4");
        tab_tabla4.setTabla("act_foto_activo", "ide_acfac", 10);
        tab_tabla4.setCondicion("ide_acafi=-1");
        tab_tabla4.getColumna("ide_acafi").setVisible(false);
        tab_tabla4.getColumna("ide_acfac").setVisible(false);
        tab_tabla4.setTipoFormulario(true);
        tab_tabla4.getColumna("foto_acfac").setUpload();
        tab_tabla4.getColumna("foto_acfac").setImagen("120", "120");
        tab_tabla4.getColumna("foto_acfac").setNombreVisual("FOTO");
        tab_tabla4.setLectura(true);
        tab_tabla4.dibujar();

        Boton bot_agregar = new Boton();
        bot_agregar.setMetodo("abrirDialogoAgregarFoto");
        bot_agregar.setValue("Agregar Foto");
        bot_agregar.setIcon("ui-icon-plus");

        Boton bot_eliminar = new Boton();
        bot_eliminar.setMetodo("eliminarFoto");
        bot_eliminar.setValue("Eliminar Foto");
        bot_eliminar.setIcon("ui-icon-trash");

        Grid gri1 = new Grid();
        gri1.setColumns(3);
        gri1.getChildren().add(bot_agregar);
        gri1.getChildren().add(bot_eliminar);

        PanelTabla pat_panel4 = new PanelTabla();
        pat_panel4.setHeader(gri1);
        pat_panel4.setPanelTabla(tab_tabla4);
        pat_panel4.getMenuTabla().getItem_eliminar().setRendered(false);
        tab_tabulador.agregarTab("FOTOS", pat_panel4);
        
        

        tab_tabla2 = new Tabla();
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setIdCompleto("tab_tabulador:tab_tabla2");
        tab_tabla2.setSql(ser_activos.getSqlHistoriaAsignacionActivo("-1"));
        tab_tabla2.setCampoPrimaria("ide_acaaf");
        tab_tabla2.getColumna("ide_acaaf").setVisible(false);
        tab_tabla2.setLectura(true);
        tab_tabla2.setRows(10);
        tab_tabla2.dibujar();
        tab_tabla2.setRendered(false);
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        tab_tabulador.agregarTab("HISTORIAL ASIGNACIONES", pat_panel2);

        tab_tabla3 = new Tabla();
        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setIdCompleto("tab_tabulador:tab_tabla3");
        tab_tabla3.setSql(ser_activos.getSqlTransaccionesActivo("-1"));
        tab_tabla3.setCampoPrimaria("ide_actra");
        tab_tabla3.getColumna("ide_actra").setVisible(false);
        tab_tabla3.setLectura(true);
        tab_tabla3.setRows(10);
        tab_tabla3.setRendered(false);
        tab_tabla3.dibujar();

        PanelTabla pat_panel5 = new PanelTabla();
        pat_panel5.setPanelTabla(tab_tabla3);
        tab_tabulador.agregarTab("TRANSACCIONES", pat_panel5);
        
        

        tab_tabla5 = new Tabla();
        tab_tabla5.setId("tab_tabla5");
        tab_tabla5.setIdCompleto("tab_tabulador:tab_tabla5");
        tab_tabla5.setSql(ser_activos.getSqlActivosHijoMasivo("-1"));
        tab_tabla5.setCampoPrimaria("ide_acafi");
        tab_tabla5.getColumna("ide_acafi").setVisible(false);
        tab_tabla5.setLectura(true);
        tab_tabla5.setRows(10);

        tab_tabla5.dibujar();
         
   
        
        PanelTabla pat_panel6 = new PanelTabla();
        pat_panel6.setPanelTabla(tab_tabla5);
        tab_tabulador.agregarTab("DETALLE CANTIDAD", pat_panel6);
        
        tab_tabla6 = new Tabla();
        tab_tabla6.setId("tab_tabla6");
        tab_tabla6.setIdCompleto("tab_tabulador:tab_tabla6");
        tab_tabla6.setTabla("ACT_DEPRECIACION", "IDE_ACDEPR", 11);
       // tab_tabla6.setCampoPrimaria("ide_acdepr");
      //  tab_tabla6.getColumna("ide_acdepr").setVisible(false);
        //tab_tabla6.setLectura(true);
        //tab_tabla6.setRows(10);
        tab_tabla6.setLectura(true);
        tab_tabla6.dibujar();

        PanelTabla pat_panel7 = new PanelTabla();
        pat_panel7.setPanelTabla(tab_tabla6);
        tab_tabulador.agregarTab("DEPRECIACIÓN", pat_panel7);

        Grupo gr = new Grupo();
        gr.setTransient(true);
        gr.getChildren().add(pat_panel1);
        gr.getChildren().add(tab_tabulador);
        mep_menu.dibujar(2, "FICHA ACTIVO FIJO", gr);
        mep_menu.getDivision2().setTransient(true);
        mep_menu.getDivision2().getChildren().get(0).setTransient(true);
        gr.getParent().setTransient(true);
        gr.getParent().getParent().setTransient(true);
        tab_tabulador.getParent().setTransient(true);
        tab_tabulador.getParent().getParent().setTransient(true);
    }

    public void eliminarFoto() {
        tab_tabla4.setLectura(false);
        if (tab_tabla4.eliminar()) {
            guardarPantalla();
        }
    }

    public void cambioCantidad() {
        //Valida si que sea mayor q 1

        try {
            cantidad = Integer.parseInt(tab_tabla.getValor("cantidad_acafi"));
        } catch (Exception e) {
            cantidad = 0;
        }
        if (cantidad <= 0) {
            utilitario.agregarMensajeError("La cantidad debe ser mayor o igual que 1", "");
            tab_tabla.setValor("cantidad_acafi", "1");
            utilitario.addUpdateTabla(tab_tabla, "cantidad_acafi", "");
            cantidad = 1;
        } else if (cantidad > 1) {
            //Si es mayor q 1 es ingreso masivo
            utilitario.agregarMensajeInfo("Se crearán " + cantidad + " activos fijos del mismo tipo", "");
        }
        generarCodigoBarras();
    }

    public void abrirEliminarActivoFijo() {
        if (tab_tabla.getValor("ide_acafi") != null) {
            con_confirma.getBot_aceptar().setMetodo("eliminarActivoFijo");
            con_confirma.dibujar();
        } else {
            utilitario.agregarMensajeError("Debe seleccionar un Activo Fijo", "");
        }
    }

    public void eliminarActivoFijo() {
        String ide_acafi = tab_tabla.getValor("ide_acafi");
        String act_ide_acafi = tab_tabla.getValor("act_ide_acafi");
        if (ide_acafi != null) {
            con_confirma.cerrar();
            //borra los hijos primero 
            utilitario.getConexion().agregarSql("DELETE FROM act_activo_fijo WHERE act_ide_acafi=" + ide_acafi);
            //borra activo
            utilitario.getConexion().agregarSql("DELETE FROM act_activo_fijo WHERE ide_acafi=" + ide_acafi);

            if (act_ide_acafi != null) {
                //Modifica la cantidad si elimino un hijo
                utilitario.getConexion().agregarSql("UPDATE act_activo_fijo SET cantidad_acafi= cantidad_acafi-1 WHERE ide_acafi=" + act_ide_acafi);
            }
            if (guardarPantalla().isEmpty()) {
                tab_tabla.actualizar();
            }

        } else {
            utilitario.agregarMensaje("Debe seleccionar un Activo", "");
        }
    }

    public void cargarActivoFijo() {
        String ide_acafi = tab_tabla.getValor("ide_acafi");
        if (ide_acafi != null) {
            dibujarActivoFijo();
            tab_tabla.setCondicion("ide_acafi=" + ide_acafi);
            tab_tabla.ejecutarSql();
            tab_tabla.modificar(tab_tabla.getFilaActual()); //dfj 
            tab_tabla2.setSql(ser_activos.getSqlHistoriaAsignacionActivo(tab_tabla.getValor("ide_acafi")));
            tab_tabla2.ejecutarSql();
            tab_tabla3.setSql(ser_activos.getSqlTransaccionesActivo(tab_tabla.getValor("ide_acafi")));
            tab_tabla3.ejecutarSql();
            tab_tabla2.setRendered(true);
            tab_tabla3.setRendered(true);
            tab_tabla5.setSql(ser_activos.getSqlActivosHijoMasivo(tab_tabla.getValor("ide_acafi")));
            tab_tabla5.ejecutarSql();
            tab_tabla.getColumna("cantidad_acafi").setLectura(true); ///BLOQUEA PARA QUE NO PUEDAN MODIFICAR CANTIDAD

            tab_tabla4.setCondicion("ide_acafi=" + tab_tabla.getValor("ide_acafi"));
            tab_tabla4.ejecutarSql();
            tab_tabla6.setCondicion("ide_acafi=" + tab_tabla.getValor("ide_acafi"));
            tab_tabla6.ejecutarSql();

            cargarCodigoBarras();
        } else {
            utilitario.agregarMensaje("Debe seleccionar un Activo", "");
        }
    }

    public void imprimirCodigoBarra() {
        System.out.println("***-*-*");
        if (tab_tabla.isFilaInsertada() == false) {
            if (cantidad == 1) {
                cagarReporteCodigoBarras(tab_tabla.getStringColumna("ide_acafi"));
            }
            if (cantidad > 1) {
                if (tab_tabla5.isEmpty() == false) {
                    cagarReporteCodigoBarras(tab_tabla5.getStringColumna("ide_acafi"));
                }
            }
        } else {
            utilitario.agregarMensajeInfo("Debe guardar el activo fijo", "");
        }
    }

    private void cagarReporteCodigoBarras(String ide_acafi) {
        if (ide_acafi != null && ide_acafi.isEmpty() == false) {
            Map parametros_rep = new HashMap();
            parametros_rep.put("ide_acafi", ide_acafi);
            vipdf_acta.setTitle("CÓDIGO DE BARRAS");
            vipdf_acta.setVisualizarPDF("rep_activos/rep_cod_barras.jasper", parametros_rep);
            vipdf_acta.dibujar();
        } else {
            utilitario.agregarMensajeError("Seleccione un activo fijo para imprimir el Código de Barras", "");
        }
    }

    public void imprimirActaC() {
        if (tab_tabla.getValor("ide_acact") != null) {
            Map parametros_rep = new HashMap();
            parametros_rep.put("ide_acact", Long.parseLong(tab_tabla.getValor("ide_acact")));
            vipdf_acta.setTitle("ACTA DE CONSTATACIÓN FÍSICA");
            vipdf_acta.setVisualizarPDF("rep_activos/rep_acta_constata.jasper", parametros_rep);
            vipdf_acta.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Seleccione una Acta", "");
        }
    }

    public void generarCodigoBarras() {
        StringBuilder codigoB = new StringBuilder();
        String codCasa = tab_tabla.getValor("ide_gecas") == null ? "00" : tab_tabla.getValorArreglo("ide_gecas", 2);
        if (codCasa != null && codCasa.length() < 2) {
            codCasa = String.format("%02d", new Object[]{Integer.parseInt(codCasa)}).trim();
        }
        String codObra = tab_tabla.getValor("ide_geobr") == null ? "000" : tab_tabla.getValorArreglo("ide_geobr", 2);
        if (codObra != null && codObra.length() < 3) {
            codObra = String.format("%03d", new Object[]{Integer.parseInt(codObra)}).trim();
        }
        String codUbiAct = tab_tabla.getValor("ide_acuba") == null ? "00" : tab_tabla.getValorArreglo("ide_acuba", 2);
        if (codUbiAct != null && codUbiAct.length() < 2) {
            codUbiAct = String.format("%02d", new Object[]{Integer.parseInt(codUbiAct)}).trim();
        }
        String codClasAct = tab_tabla.getValor("ide_accla") == null ? "00" : tab_tabla.getValorArreglo("ide_accla", 2);
        if (codClasAct != null && codClasAct.length() < 2) {
            codClasAct = String.format("%02d", new Object[]{Integer.parseInt(codClasAct)}).trim();
        }
        String codTipoAct = tab_tabla.getValor("ide_inarti") == null ? "000" : tab_tabla.getValorArreglo("ide_inarti", 2);
        if (codTipoAct != null && codTipoAct.length() < 3) {
            codTipoAct = String.format("%03d", new Object[]{Integer.parseInt(codTipoAct)}).trim();
        }
        codigoB.append(codCasa).append(" ").append(codObra).append(" ").append(codUbiAct).append(" ").append(codClasAct).append(" ").append(codTipoAct);
        tab_tabla.setValor("codigo_barras_acafi", codigoB.toString().trim());

        if (cantidad == 1) {
            eti_cod_barras.setValue(codigoB.toString());
            try {
                File barcodeFile = new File("dynamicbarcode");
                BarcodeImageHandler.saveJPEG(
                        BarcodeFactory.createCode128(codigoB.toString()), barcodeFile);
                stcCodigoBarra = new DefaultStreamedContent(
                        new FileInputStream(barcodeFile), "image/jpeg");
            } catch (Exception e) {
            }
        } else {
            eti_cod_barras.setValue(null);
            stcCodigoBarra = null;
        }
        utilitario.addUpdate("tab_tabulador:0:gri_cb");
    }

    public void cargarCodigoBarras() {

        String codigoB = tab_tabla.getValor("codigo_barras_acafi");
        try {
            cantidad = Integer.parseInt(tab_tabla.getValor("cantidad_acafi"));
        } catch (Exception e) {
            cantidad = 0;
        }
        if (codigoB != null) {
            eti_cod_barras.setValue(codigoB);
            try {
                File barcodeFile = new File("dynamicbarcode");
                BarcodeImageHandler.saveJPEG(
                        BarcodeFactory.createCode128(codigoB), barcodeFile);
                stcCodigoBarra = new DefaultStreamedContent(
                        new FileInputStream(barcodeFile), "image/jpeg");
            } catch (Exception e) {
            }
        } else {
            eti_cod_barras.setValue(null);
            stcCodigoBarra = null;
        }
        utilitario.addUpdate("tab_tabulador:0:gri_cb");
    }

    @Override
    public void insertar() {
        if (mep_menu.getOpcion() == 2) {
            tab_tabla.limpiar();
            tab_tabla.insertar();
            tab_tabla2.limpiar();
            tab_tabla3.limpiar();
            eti_cod_barras.setValue(tab_tabla.getValor("codigo_barras_acafi"));
            cantidad = 0;
        } else {
            mep_menu.limpiar();
            dibujarActivoFijo();
            tab_tabla.insertar();
        }

    }

    @Override
    public void guardar() {
        if (mep_menu.getOpcion() == 2) {
            TablaGenerica tab_masivo = new TablaGenerica();
            generarCodigoBarras();
            if (tab_tabla.guardar()) {
                if (cantidad > 1) {
                    if (tab_tabla.isFilaInsertada()) {
                        tab_masivo.setTabla("act_activo_fijo", "ide_acafi");
                        tab_masivo.setCondicion("ide_acafi=-1");
                        tab_masivo.ejecutarSql();
                        for (int i = 0; i < cantidad; i++) {
                            tab_masivo.insertar();
                            //recorre todas las columnas de la tabla principal
                            for (int j = 0; j < tab_tabla.getTotalColumnas(); j++) {
                                String strColumna = tab_tabla.getColumnas()[j].getNombre();
                                if (!strColumna.equalsIgnoreCase("ide_acafi")) {
                                    tab_masivo.setValor(strColumna, tab_tabla.getValor(strColumna));
                                }
                            }
                            tab_masivo.setValor("act_ide_acafi", tab_tabla.getValor("ide_acafi"));
                            tab_masivo.setValor("sec_masivo_acafi", String.valueOf((i + 1)));
                            tab_masivo.setValor("cantidad_acafi", "1");
                        }
                        tab_masivo.guardar();
                    } else if (tab_tabla.isFilaModificada()) {
                        //Actualiza caracteriasticas en lote
                        tab_masivo.setTabla("act_activo_fijo", "ide_acafi");
                        tab_masivo.setCondicion("act_ide_acafi=" + tab_tabla.getValor("ide_acafi"));
                        tab_masivo.ejecutarSql();
                        for (int j = 0; j < tab_masivo.getTotalFilas(); j++) {
                            tab_masivo.modificar(j);
                            tab_masivo.setValor(j, "ide_aceaf", tab_tabla.getValor("ide_aceaf"));
                            tab_masivo.setValor(j, "ide_geubi", tab_tabla.getValor("ide_geubi"));
                            tab_masivo.setValor(j, "ide_inarti", tab_tabla.getValor("ide_inarti"));
                            tab_masivo.setValor(j, "nombre_acafi", tab_tabla.getValor("nombre_acafi"));
                            tab_masivo.setValor(j, "vida_util_acafi", tab_tabla.getValor("vida_util_acafi"));
                            tab_masivo.setValor(j, "valor_compra_acafi", tab_tabla.getValor("valor_compra_acafi"));
                            tab_masivo.setValor(j, "deprecia_acafi", tab_tabla.getValor("deprecia_acafi"));
                            tab_masivo.setValor(j, "recidual_acafi", tab_tabla.getValor("recidual_acafi"));
                            tab_masivo.setValor(j, "serie_acafi", tab_tabla.getValor("serie_acafi"));
                            tab_masivo.setValor(j, "observacion_acafi", tab_tabla.getValor("observacion_acafi"));
                            tab_masivo.setValor(j, "numero_factu_acafi", tab_tabla.getValor("numero_factu_acafi"));
                            tab_masivo.setValor(j, "fecha_compra_acafi", tab_tabla.getValor("fecha_compra_acafi"));
                            tab_masivo.setValor(j, "anos_uso_acafi", tab_tabla.getValor("anos_uso_acafi"));
                            tab_masivo.setValor(j, "valor_comercial_acafi", tab_tabla.getValor("valor_comercial_acafi"));
                            tab_masivo.setValor(j, "valor_remate_acafi", tab_tabla.getValor("valor_remate_acafi"));
                            tab_masivo.setValor(j, "modelo_acafi", tab_tabla.getValor("modelo_acafi"));
                            tab_masivo.setValor(j, "ide_inmar", tab_tabla.getValor("ide_inmar"));
                            tab_masivo.setValor(j, "ide_acuba", tab_tabla.getValor("ide_acuba"));
                            tab_masivo.setValor(j, "color_acafi", tab_tabla.getValor("color_acafi"));
                            tab_masivo.setValor(j, "mediadas_acafi", tab_tabla.getValor("mediadas_acafi"));
                            tab_masivo.setValor(j, "ide_gecas", tab_tabla.getValor("ide_gecas"));
                            tab_masivo.setValor(j, "ide_geobr", tab_tabla.getValor("ide_geobr"));
                            tab_masivo.setValor(j, "ide_accla", tab_tabla.getValor("ide_accla"));
                        }
                        tab_masivo.guardar();
                    }
                }
                if (guardarPantalla().isEmpty()) {
                    tab_tabla5.setSql(ser_activos.getSqlActivosHijoMasivo(tab_tabla.getValor("ide_acafi")));
                    tab_tabla5.ejecutarSql();
                    if (cantidad == 1) {
                        //solo si no es masivo actualiza el codigo de barras
                        if (tab_tabla.getValor("act_ide_acafi") == null) {
                            utilitario.getConexion().ejecutarSql("UPDATE act_activo_fijo set codigo_barras_acafi='" + tab_tabla.getValor("codigo_barras_acafi") + "'||' '||ide_acafi||' 1' where ide_acafi=" + tab_tabla.getValor("ide_acafi"));
                        } else {
                            utilitario.getConexion().ejecutarSql("UPDATE act_activo_fijo set codigo_barras_acafi='" + tab_tabla.getValor("codigo_barras_acafi") + "'||' '||act_ide_acafi||' '||sec_masivo_acafi where ide_acafi=" + tab_tabla.getValor("ide_acafi"));
                        }
                    }
                    if (cantidad > 1) {
                        utilitario.getConexion().ejecutarSql("UPDATE act_activo_fijo set codigo_barras_acafi='" + tab_tabla.getValor("codigo_barras_acafi") + "'||' '||ide_acafi where ide_acafi=" + tab_tabla.getValor("ide_acafi"));
                        utilitario.getConexion().ejecutarSql("UPDATE act_activo_fijo set codigo_barras_acafi='" + tab_tabla.getValor("codigo_barras_acafi") + "'||' '||'" + tab_tabla.getValor("ide_acafi") + "'||' '||sec_masivo_acafi where ide_acafi in(" + tab_tabla5.getStringColumna("ide_acafi") + ") and  act_ide_acafi=" + tab_tabla.getValor("ide_acafi"));
                        utilitario.agregarMensaje("Se generaron " + cantidad + " activos fijos", "");
                        cantidad = 1;
                    }
                    tab_tabla.setCondicion("ide_acafi=" + tab_tabla.getValor("ide_acafi"));
                    tab_tabla.ejecutarSql();
                    cargarCodigoBarras();
                    //cantidad = 0;
                }
            }
        } else if (mep_menu.getOpcion() == 5) {
            String seleccionadas = tab_tabla2.getStringColumna("ide_acafi");
            if (tab_tabla2.getTotalFilas() > 0) {
                if (tab_tabla.isFilaInsertada()) {
                    tab_tabla.setValor("secuencial_acact", String.valueOf(ser_activos.getSecuencialActaConstatacion()));
                    tab_tabla.setValor("codigo_acact", "ACT-" + String.valueOf(ser_activos.getSecuencialActaConstatacion()));
                } else {
                    //elimino asignaciones ya realizadas
                    utilitario.getConexion().agregarSqlPantalla("delete from act_asignacion_activo where ide_acact =" + tab_tabla.getStringColumna("ide_acact"));
                    utilitario.getConexion().agregarSqlPantalla("update act_activo_fijo set ide_geper=null where ide_acafi in (" + seleccionadas + ")");
                }
                if (tab_tabla.guardar()) {
                    String ide_geper = tab_tabla.getValor("ide_geper");
                    TablaGenerica tab_asig = new TablaGenerica();
                    tab_asig.setTabla("act_asignacion_activo", "ide_acaaf");
                    tab_asig.setCondicion("ide_acaaf=-1");
                    tab_asig.ejecutarSql();
                    String[] act = seleccionadas.split(",");
                    for (String ide_acafi : act) {
                        tab_asig.insertar();
                        tab_asig.setValor("ide_acafi", ide_acafi);
                        tab_asig.setValor("ide_usua", utilitario.getVariable("ide_usua"));
                        tab_asig.setValor("ide_geper", ide_geper);
                        tab_asig.setValor("fecha_acaaf", utilitario.getFechaActual());
                        tab_asig.setValor("ide_acact", tab_tabla.getValor("ide_acact"));
                    }
                    utilitario.getConexion().agregarSqlPantalla("update act_activo_fijo set ide_geper=" + ide_geper + " where ide_acafi in (" + seleccionadas + ")");
                    if (tab_asig.guardar()) {
                        if (guardarPantalla().isEmpty()) {
                            dibujarListadoActasConstata();
                        }
                    }
                }

            } else {
                //  utilitario.agregarMensajeError("Seleccione activos para asignar", "");
                guardarPantalla();
            }
        } else if (mep_menu.getOpcion() == 7) {
            System.out.println("xxx " + tab_tabla2.getSeleccionados());
            if (tab_tabla2.getSeleccionados() != null) {
                TablaGenerica tag = new TablaGenerica();
                tag.setTabla("act_transaccion", "ide_actra", -1);
                tag.setCondicion("ide_actra=-1");

                for (Fila filaActual : tab_tabla2.getSeleccionados()) {
                    tag.insertar();
                    tag.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
                    tag.setValor("ide_empr", utilitario.getVariable("ide_empr"));
                    tag.setValor("ide_usua", utilitario.getVariable("ide_usua"));
                    tag.setValor("valor_actra", "0");
                    tag.setValor("observacion_actra", tab_tabla.getValor("observacion_actra"));
                    tag.setValor("ide_acttr", tab_tabla.getValor("ide_acttr"));
                    tag.setValor("fecha_actra", tab_tabla.getValor("fecha_actra"));
                    tag.setValor("ide_acafi", String.valueOf(filaActual.getCampos()[tab_tabla2.getNumeroColumna("ide_acafi")]));
                    utilitario.getConexion().agregarSql("UPDATE act_activo_fijo set ide_aceaf=" + utilitario.getVariable("p_act_estado_dado_de_baja") + " where ide_acafi=" + String.valueOf(filaActual.getCampos()[tab_tabla2.getNumeroColumna("ide_acafi")]));
                }
                if (tag.guardar()) {
                    if (utilitario.getConexion().guardarPantalla().isEmpty()) {
                        tab_tabla.limpiar();
                        tab_tabla.insertar();
                        tab_tabla2.actualizar();
                    }
                }

            } else {
                utilitario.agregarMensajeInfo("Seleccione uno o varios Activos fijos", "");
            }

        }
    }

    @Override
    public void eliminar() {

    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
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

    public Tabla getTab_seleccion() {
        return tab_tabla2;
    }

    public void setTab_seleccion(Tabla tab_tabla) {
        this.tab_tabla2 = tab_tabla;
    }

    public AutoCompletar getAut_custodio() {
        return aut_custodio;
    }

    public void setAut_custodio(AutoCompletar aut_custodio) {
        this.aut_custodio = aut_custodio;
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

    public SeleccionArbol getSel_arb() {
        return sel_arb;
    }

    public void setSel_arb(SeleccionArbol sel_arb) {
        this.sel_arb = sel_arb;
    }

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }

    public VisualizarPDF getVipdf_acta() {
        return vipdf_acta;
    }

    public void setVipdf_acta(VisualizarPDF vipdf_acta) {
        this.vipdf_acta = vipdf_acta;
    }

    public StreamedContent getStcCodigoBarra() {
        return stcCodigoBarra;
    }

    public void setStcCodigoBarra(StreamedContent stcCodigoBarra) {
        this.stcCodigoBarra = stcCodigoBarra;
    }

    public Tabla getTab_tabla4() {
        return tab_tabla4;
    }

    public void setTab_tabla4(Tabla tab_tabla4) {
        this.tab_tabla4 = tab_tabla4;
    }

    public Tabla getTab_tabla5() {
        return tab_tabla5;
    }

    public void setTab_tabla5(Tabla tab_tabla5) {
        this.tab_tabla5 = tab_tabla5;
    }

    public SeleccionTabla getSet_selecciona() {
        return set_selecciona;
    }

    public void setSet_selecciona(SeleccionTabla set_selecciona) {
        this.set_selecciona = set_selecciona;
    }

    public Confirmar getCon_confirma() {
        return con_confirma;
    }

    public void setCon_confirma(Confirmar con_confirma) {
        this.con_confirma = con_confirma;
    }

    public Dialogo getDia_foto() {
        return dia_foto;
    }

    public void setDia_foto(Dialogo dia_foto) {
        this.dia_foto = dia_foto;
    }

    public Tabla getTab_foto() {
        return tab_foto;
    }

    public void setTab_foto(Tabla tab_foto) {
        this.tab_foto = tab_foto;
    }

    public Tabla getTab_tabla6() {
        return tab_tabla6;
    }

    public void setTab_tabla6(Tabla tab_tabla6) {
        this.tab_tabla6 = tab_tabla6;
    }

    public SeleccionTabla getSel_clase_activos() {
        return sel_clase_activos;
    }

    public void setSel_clase_activos(SeleccionTabla sel_clase_activos) {
        this.sel_clase_activos = sel_clase_activos;
    }

}

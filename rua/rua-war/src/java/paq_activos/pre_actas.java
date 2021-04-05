/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_activos;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.VisualizarPDF;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import paq_produccion.ejb.ServicioProduccion;
import servicios.activos.ServicioActivosFijos;

/**
 *
 */
public class pre_actas extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_acta_entrega = new Tabla();
    private Combo com_tipo_acta = new Combo();
    private Dialogo dia_activos = new Dialogo();
    private SeleccionTabla set_activos = new SeleccionTabla();
    private SeleccionTabla set_tipo_acta = new SeleccionTabla();
    private Dialogo dia_actas = new Dialogo();
    private Dialogo dia_anulado = new Dialogo();
    private Dialogo dia_radio = new Dialogo();
    private Radio rad_bloqueado = new Radio();
    private VisualizarPDF vipdf_actas = new VisualizarPDF();
    private Confirmar con_confirma = new Confirmar();
    private Etiqueta eti_acta = new Etiqueta();
    private Calendario cal_baja= new Calendario();
    private AreaTexto are_baja = new AreaTexto();

    @EJB
    private ServicioActivosFijos ser_activos = (ServicioActivosFijos) utilitario.instanciarEJB(ServicioActivosFijos.class);
    @EJB
    private ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    @EJB
    private final ServicioProduccion ser_valtiempo = (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class);

    String condicion = "";

    ///reporte
    private Map p_parametros = new HashMap();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();

    public pre_actas() {

        ///reporte
        rep_reporte.setId("rep_reporte"); //id
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
        agregarComponente(rep_reporte);//agrega el componente a la pantalla
        //bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
        self_reporte.setId("self_reporte"); //id
        //agregarComponente(self_reporte);
        com_tipo_acta.setId("com_tipo_acta");
        com_tipo_acta.setCombo(ser_activos.getTipoActa());
        com_tipo_acta.setMetodo("seleccionarTipoActa");
        bar_botones.agregarComponente(new Etiqueta("Seleccione El Tipo de Acta:"));
        bar_botones.agregarComponente(com_tipo_acta);

        Boton bot_clean = new Boton();
        bot_clean.setId("bot_clean");
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_clean);

        Boton bot_agrega_activo = new Boton();
        bot_agrega_activo.setIcon("ui-icon-search");
        bot_agrega_activo.setValue("BUSCAR ACTIVOS");
        bot_agrega_activo.setMetodo("importarActivos");
        bar_botones.agregarBoton(bot_agrega_activo);

        Boton bot_aprobar_activo = new Boton();
        bot_aprobar_activo.setIcon("ui-icon-search");
        bot_aprobar_activo.setValue("APROBAR ACTA");
        bot_aprobar_activo.setMetodo("dibujarConfirm");
        bar_botones.agregarBoton(bot_aprobar_activo);

        Boton bot_anular_activo = new Boton();
        bot_anular_activo.setIcon("ui-icon-search");
        bot_anular_activo.setValue("ANULAR ACTA");
        bot_anular_activo.setMetodo("abrirDialogoAnular");
        bar_botones.agregarBoton(bot_anular_activo);

        Boton bot_imprime_acta = new Boton();
        bot_imprime_acta.setIcon("ui-icon-print");
        bot_imprime_acta.setValue("IMPRIMIR ACTAS");
        bot_imprime_acta.setMetodo("generarPDF");
        bar_botones.agregarBoton(bot_imprime_acta);
        
        eti_acta.setId("eti_acta");
        
        con_confirma.setId("con_confirma");
        con_confirma.setMessage("Está seguro que desea aprobar el acta: "+eti_acta);
        con_confirma.setTitle("APROBAR ACTA");
        con_confirma.getBot_aceptar().setValue("Si");
        con_confirma.getBot_cancelar().setValue("No");
        agregarComponente(con_confirma);        

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("act_acta_constata", "ide_acact", 1);
        tab_tabla.setCondicion("ide_actia=-1");
        tab_tabla.setCampoOrden("ide_acact desc");
        tab_tabla.getColumna("ide_gecas").setCombo(ser_activos.getCasa());
        tab_tabla.getColumna("ide_geobr").setCombo(ser_activos.getObras());
        tab_tabla.getColumna("ide_acuba").setCombo(ser_activos.getUbicacionActivo());
        tab_tabla.getColumna("act_ide_acuba").setCombo(ser_activos.getUbicacionActivo());
        tab_tabla.getColumna("ide_geper").setCombo(ser_adquisiciones.getDatosProveedor());
        tab_tabla.getColumna("gen_ide_geper").setCombo(ser_adquisiciones.getDatosProveedor());
        tab_tabla.getColumna("ide_gecas").setAutoCompletar();
        tab_tabla.getColumna("ide_geobr").setAutoCompletar();
        tab_tabla.getColumna("ide_acuba").setAutoCompletar();
        tab_tabla.getColumna("ide_geper").setAutoCompletar();
        tab_tabla.getColumna("gen_ide_geper").setAutoCompletar();
        tab_tabla.getColumna("ide_geper").setRequerida(true);
        tab_tabla.getColumna("secuencial_acact").setRequerida(true);
        tab_tabla.getColumna("anulado_acact").setLectura(true);
        tab_tabla.getColumna("bloqueado_acact").setLectura(true);
        tab_tabla.getColumna("bloqueado_acact").setValorDefecto("false");
        tab_tabla.getColumna("secuencial_acact").setEtiqueta();
        tab_tabla.getColumna("secuencial_acact").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
        tab_tabla.getColumna("ide_actia").setVisible(false);
        tab_tabla.agregarRelacion(tab_tabla2);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(6);
        tab_tabla.setLectura(true);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("act_movimiento", "ide_acmov", 2);
        tab_tabla2.getColumna("ide_aceaf").setCombo(ser_activos.getEstadoActivo());
        tab_tabla2.getColumna("ide_acafi").setCombo(ser_activos.getDatoActivoBasico());
        tab_tabla2.getColumna("ide_aceaf").setAutoCompletar();
        tab_tabla2.getColumna("ide_acafi").setAutoCompletar();

        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel, pat_panel2, "30%", "H");
        agregarComponente(div_division);

        set_activos.setId("set_activos");
        set_activos.setTitle("SELECCIONE LOS ACTIVOS FIJOS");
        set_activos.setSeleccionTabla(ser_activos.getSqlListaActivosFijos("1", "-1", "0", "-1", "1"), "ide_acafi");
        set_activos.getTab_seleccion().getColumna("observacion_acafi").setFiltro(true);
        set_activos.getTab_seleccion().getColumna("TIPO_ACTIVO").setFiltro(true);
        set_activos.getTab_seleccion().getColumna("SECUENCIAL").setFiltro(true);
        set_activos.getTab_seleccion().getColumna("observacion_acafi").setOrden(1);
        set_activos.getTab_seleccion().getColumna("TIPO_ACTIVO").setOrden(2);
        set_activos.getTab_seleccion().getColumna("SECUENCIAL").setOrden(3);
        set_activos.getBot_aceptar().setMetodo("aceptarActivo");
        agregarComponente(set_activos);

        dia_actas = new Dialogo();
        dia_actas.setTitle("DATOS PARA EL ACTA");
        dia_actas.setId("dia_actas");
        dia_actas.setHeight("45%");
        dia_actas.setWidth("80%");
        dia_actas.getBot_aceptar().setMetodo("guardarDialogo");
        agregarComponente(dia_actas);

        List lista = new ArrayList();
        Object fila1[] = {
            "1", "Bloquear Actas"
        };
        Object fila2[] = {
            "2", "Previsualizar"
        };
        lista.add(fila1);
        lista.add(fila2);
        rad_bloqueado = new Radio();
        rad_bloqueado.setRadio(lista);
        rad_bloqueado.setValue("2"); //Por defecto beneficiario

        dia_radio = new Dialogo();
        dia_radio.setTitle("SELECCIONE LA OPCION PARA IMPRIMIR EL ACTA");
        dia_radio.setId("dia_radio");
        dia_radio.setHeight("25%");
        dia_radio.setWidth("25%");
        dia_radio.setDialogo(rad_bloqueado);
        dia_radio.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(dia_radio);

        vipdf_actas.setId("vipdf_actas");
        vipdf_actas.setTitle("REPORTE DE ACTAS");
        agregarComponente(vipdf_actas);
        
        // dialogo para anulacion
        dia_anulado = new Dialogo();
        dia_anulado.setTitle("DATOS PARA EL ACTA");
        dia_anulado.setId("dia_anulado");
        dia_anulado.setHeight("45%");
        dia_anulado.setWidth("40%");
        dia_anulado.getBot_aceptar().setMetodo("anularActa");
        Grid  grid_anulacion= new Grid();
        grid_anulacion.setColumns(2);
        cal_baja.setId("cal_baja");
        cal_baja.setFechaActual();
        are_baja.setId("are_baja");
        are_baja.setAutoResize(true);
        grid_anulacion.getChildren().add(new Etiqueta("Fecha Anulación"));
        grid_anulacion.getChildren().add(cal_baja);
        grid_anulacion.getChildren().add(new Etiqueta("Razón Anulación"));
        grid_anulacion.getChildren().add(are_baja);
        dia_anulado.setDialogo(grid_anulacion);
        agregarComponente(dia_anulado);
    }
    public void dibujarConfirm(){
        if (tab_tabla.getValor("ide_acact") != null) {
            eti_acta.setValue(tab_tabla.getValor("secuencial_acact"));            
            con_confirma.getBot_aceptar().setMetodo("aprobarActa");
            utilitario.addUpdate("eti_acta,con_confirma");
            con_confirma.dibujar();
        } else {
            utilitario.agregarMensajeError("Debe seleccionar un Acta", "");
        }
    }
    public void aprobarActa() {
        utilitario.getConexion().ejecutarSql("update act_acta_constata set bloqueado_acact=true where ide_acact=" + tab_tabla.getValor("ide_acact"));
        TablaGenerica tab_detalle_activo = utilitario.consultar("select ide_acmov,ide_acafi from act_movimiento where ide_acact=" + tab_tabla.getValor("ide_acact"));

        if (valor_combo.equals(utilitario.getVariable("p_act_acta_entrega_recep")) || valor_combo.equals(utilitario.getVariable("p_act_acta_cambio"))) {

            if (tab_detalle_activo.getTotalFilas() > 0) {
                for (int i = 0; i < tab_detalle_activo.getTotalFilas(); i++) {
                    utilitario.getConexion().ejecutarSql("update act_activo_fijo set ide_geper =" + tab_tabla.getValor("gen_ide_geper") + ", ide_acact="+tab_tabla.getValor("ide_acact")+",ide_gecas = "+tab_tabla.getValor("ide_gecas")+" ,ide_geobr="+tab_tabla.getValor("ide_geobr")+" ,ide_acuba= "+tab_tabla.getValor("ide_acuba")+", act_ide_acuba="+tab_tabla.getValor("act_ide_acuba")+" where ide_acafi =" + tab_detalle_activo.getValor(i, "ide_acafi"));
                }
            }
        }

        if (valor_combo.equals(utilitario.getVariable("p_act_acta_baja"))) {
            if (tab_detalle_activo.getTotalFilas() > 0) {
                for (int i = 0; i < tab_detalle_activo.getTotalFilas(); i++) {
                    utilitario.getConexion().agregarSql("UPDATE act_activo_fijo set ide_aceaf=" + utilitario.getVariable("p_act_estado_dado_de_baja") + " where ide_acafi=" + tab_detalle_activo.getValor(i, "ide_acafi"));
                }
                
            }
        }
            utilitario.agregarMensaje("Acta Aprobada", "Se ha aprobado el acta");        
            con_confirma.cerrar();
            tab_tabla.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla.getValorSeleccionado());

        }
    
    public void abrirDialogoAnular(){
        if (tab_tabla.getValor("ide_acact") != null) {

            dia_anulado.dibujar();
        } else {
            utilitario.agregarMensajeError("Debe seleccionar un Acta", "");
        }
    }
    public void anularActa() {
        utilitario.getConexion().ejecutarSql("update act_acta_constata set anulado_acact=true,fecha_anulado_acact= '"+cal_baja.getFecha()+"' ,razon_anulado_acact='"+are_baja.getValue()+"' where ide_acact=" + tab_tabla.getValor("ide_acact"));
        dia_anulado.cerrar();
        tab_tabla.ejecutarSql();
        tab_tabla2.ejecutarValorForanea(tab_tabla.getValorSeleccionado());

    }

    public void actaEntregaRecepcion() {

        if (com_tipo_acta.getValue() == null) {
            utilitario.agregarMensajeError("No se puede insertar", "Debe Seleccionar un tipo de acta");
            return;

        }
        tab_acta_entrega.limpiar();
        dia_actas.getGri_cuerpo().getChildren().clear();
        valor_combo = com_tipo_acta.getValue().toString();

        if (valor_combo.equals(utilitario.getVariable("p_act_acta_entrega_recep"))) {
            sec_modulo = utilitario.getVariable("p_act_secuecial_entrega_recepcion");
        } else if (valor_combo.equals(utilitario.getVariable("p_act_acta_constatacion"))) {
            sec_modulo = utilitario.getVariable("p_act_secuecial_constatacion");
        } else if (valor_combo.equals(utilitario.getVariable("p_act_acta_baja"))) {
            sec_modulo = utilitario.getVariable("p_act_secuecial_baja");
        } else if (valor_combo.equals(utilitario.getVariable("p_act_acta_cambio"))) {
            sec_modulo = utilitario.getVariable("p_act_secuecial_cambio");
        }
        TablaGenerica tab_secuencial = utilitario.consultar(ser_valtiempo.getSecuencialModulo(sec_modulo));

        tab_acta_entrega = new Tabla();
        tab_acta_entrega.setId("tab_acta_entrega");
        tab_acta_entrega.setTabla("act_acta_constata", "ide_acact", 3);
        tab_acta_entrega.setCondicion("ide_acact=-1");
        tab_acta_entrega.getColumna("ide_acact").setVisible(false);
        tab_acta_entrega.getColumna("ide_gecas").setCombo(ser_activos.getCasa());
        tab_acta_entrega.getColumna("ide_geobr").setCombo(ser_activos.getObras());
        tab_acta_entrega.getColumna("ide_acuba").setCombo(ser_activos.getUbicacionActivo());
        tab_acta_entrega.getColumna("act_ide_acuba").setCombo(ser_activos.getUbicacionActivo());
        tab_acta_entrega.getColumna("ide_geper").setCombo(ser_adquisiciones.getDatosProveedor());
        tab_acta_entrega.getColumna("gen_ide_geper").setCombo(ser_adquisiciones.getDatosProveedor());
        tab_acta_entrega.getColumna("ide_gecas").setAutoCompletar();
        tab_acta_entrega.getColumna("ide_geobr").setAutoCompletar();
        tab_acta_entrega.getColumna("ide_acuba").setAutoCompletar();
        tab_acta_entrega.getColumna("ide_geper").setAutoCompletar();
        tab_acta_entrega.getColumna("gen_ide_geper").setAutoCompletar();
        tab_acta_entrega.getColumna("ide_gecas").setNombreVisual("CASA");
        tab_acta_entrega.getColumna("ide_geobr").setNombreVisual("OBRA");
        tab_acta_entrega.getColumna("ide_acuba").setNombreVisual("AREA");
        tab_acta_entrega.getColumna("act_ide_acuba").setNombreVisual("DEPARTAMENTO");
        tab_acta_entrega.getColumna("ide_geper").setNombreVisual("CUSTODIO ACTUAL");
        tab_acta_entrega.getColumna("observacion_acact").setNombreVisual("OBSERVACIONES");
        tab_acta_entrega.getColumna("observacion_acact").setMayusculas(true);
        tab_acta_entrega.getColumna("fecha_asigna_acact").setNombreVisual("FECHA ACTA");
        tab_acta_entrega.getColumna("razon_anulado_acact").setVisible(false);
        tab_acta_entrega.getColumna("fecha_anulado_acact").setVisible(false);
        if (valor_combo.equals(utilitario.getVariable("p_act_acta_constatacion"))) {
            tab_acta_entrega.getColumna("ide_geobr").setVisible(false);
            tab_acta_entrega.getColumna("ide_gecas").setVisible(false);
            tab_acta_entrega.getColumna("ide_acuba").setVisible(false);
            tab_acta_entrega.getColumna("act_ide_acuba").setVisible(false);
            tab_acta_entrega.getColumna("ide_geper").setVisible(false);
            tab_acta_entrega.getColumna("ide_geper").setValorDefecto(utilitario.getVariable("p_act_custodio"));
            tab_acta_entrega.getColumna("gen_ide_geper").setNombreVisual("CUSTODIO");
        }
        else if (valor_combo.equals(utilitario.getVariable("p_act_acta_baja"))) {
            tab_acta_entrega.getColumna("ide_geobr").setVisible(false);
            tab_acta_entrega.getColumna("ide_gecas").setVisible(false);
            tab_acta_entrega.getColumna("ide_acuba").setVisible(false);
            tab_acta_entrega.getColumna("act_ide_acuba").setVisible(false);
            tab_acta_entrega.getColumna("ide_geper").setVisible(false);
            tab_acta_entrega.getColumna("ide_geper").setValorDefecto(utilitario.getVariable("p_act_custodio"));
            tab_acta_entrega.getColumna("gen_ide_geper").setNombreVisual("CUSTODIO");

        }
        else{
            tab_acta_entrega.getColumna("gen_ide_geper").setNombreVisual("CUSTODIO NUEVO");
        }
        tab_acta_entrega.getColumna("ide_geper").setRequerida(true);
        tab_acta_entrega.getColumna("secuencial_acact").setRequerida(true);
        tab_acta_entrega.getColumna("secuencial_acact").setValorDefecto(tab_secuencial.getValor("nuevo_secuencial"));
        tab_acta_entrega.getColumna("bloqueado_acact").setLectura(true);
        tab_acta_entrega.getColumna("bloqueado_acact").setValorDefecto("false");
        tab_acta_entrega.getColumna("secuencial_acact").setEtiqueta();
        tab_acta_entrega.getColumna("secuencial_acact").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
        tab_acta_entrega.getColumna("ide_actia").setVisible(false);
        tab_acta_entrega.getColumna("ide_actia").setValorDefecto(com_tipo_acta.getValue().toString());
        tab_acta_entrega.getColumna("bloqueado_acact").setVisible(false);
        tab_acta_entrega.getColumna("anulado_acact").setValorDefecto("false");
        tab_acta_entrega.getColumna("anulado_acact").setVisible(false);
        tab_acta_entrega.setTipoFormulario(true);
        tab_acta_entrega.getGrid().setColumns(4);
        tab_acta_entrega.dibujar();
        tab_acta_entrega.insertar();
        PanelTabla pat_panel3 = new PanelTabla();
        pat_panel3.setPanelTabla(tab_acta_entrega);
        pat_panel3.getMenuTabla().setRendered(false);
        pat_panel3.setStyle("overflow:auto");

        dia_actas.setDialogo(pat_panel3);
        dia_actas.dibujar();

    }

    public void guardarDialogo() {
        tab_acta_entrega.guardar();
        guardarPantalla();
         valor_combo = com_tipo_acta.getValue().toString();

        if (valor_combo.equals(utilitario.getVariable("p_act_acta_entrega_recep"))) {
            sec_modulo = utilitario.getVariable("p_act_secuecial_entrega_recepcion");
        } else if (valor_combo.equals(utilitario.getVariable("p_act_acta_constatacion"))) {
            sec_modulo = utilitario.getVariable("p_act_secuecial_constatacion");
        } else if (valor_combo.equals(utilitario.getVariable("p_act_acta_baja"))) {
            sec_modulo = utilitario.getVariable("p_act_secuecial_baja");
        } else if (valor_combo.equals(utilitario.getVariable("p_act_acta_cambio"))) {
            sec_modulo = utilitario.getVariable("p_act_secuecial_cambio");
        }
        utilitario.getConexion().ejecutarSql(ser_valtiempo.getActualizarSecuencial(sec_modulo));
        dia_actas.cerrar();
        tab_tabla.ejecutarSql();
        tab_tabla2.ejecutarValorForanea(tab_tabla.getValorSeleccionado());
    }
    public boolean validaActas(){
        boolean val=false;
        if (valor_combo.equals(utilitario.getVariable("p_act_acta_entrega_recep"))) {

        } else if (valor_combo.equals(utilitario.getVariable("p_act_acta_constatacion"))) {

        } else if (valor_combo.equals(utilitario.getVariable("p_act_acta_baja"))) {

        } else if (valor_combo.equals(utilitario.getVariable("p_act_acta_cambio"))) {

        }
        return val;
    }
    public void importarActivos() {
        if (com_tipo_acta.getValue() == null) {
            utilitario.agregarMensajeError("No se puede insertar", "Debe Seleccionar un tipo de acta");
            return;

        }
        set_activos.limpiarSeleccionados();
        String custodio_actual = tab_tabla.getValor("ide_geper");
        String custodio_constatacion = tab_tabla.getValor("gen_ide_geper");
        String estado_activo = utilitario.getVariable("p_act_estado_activo_valora_deprec");
        String clase_activo = "1,2"; //indica los tipo de ACTIVO FIJO, Y BIENES DE CONTROL sie n la tabla cambian los codigos o generan nuevos se debe agregar aqui
        valor_combo = com_tipo_acta.getValue().toString();
                    System.out.println("valorr "+valor_combo);

        if (valor_combo.equals(utilitario.getVariable("p_act_acta_entrega_recep"))) {
            set_activos.getTab_seleccion().setSql(ser_activos.getSqlListaActivosFijos("1", custodio_actual, "1", estado_activo, clase_activo));
            set_activos.getTab_seleccion().ejecutarSql();
            set_activos.dibujar();
        } else if (valor_combo.equals(utilitario.getVariable("p_act_acta_constatacion"))) {
            TablaGenerica tab_activos_custodio = utilitario.consultar(ser_activos.getSqlListaActivosFijos("1", custodio_constatacion, "1", estado_activo, clase_activo));
            if (tab_activos_custodio.getTotalFilas() > 0) {
                for (int i = 0; i < tab_activos_custodio.getTotalFilas(); i++) {
                    tab_tabla2.insertar();
                    tab_tabla2.setValor(i, "ide_acafi", tab_activos_custodio.getValor(i, "ide_acafi"));
                    tab_tabla2.setValor(i, "ide_aceaf", tab_activos_custodio.getValor(i, "ide_aceaf"));
                }
                utilitario.addUpdate("tab_tabla2");
            } else {
                utilitario.agregarMensajeInfo("No Registra Activos", "El custodio consultado no registra bienes a su cargo revise por favor");
            }
        } else if (valor_combo.equals(utilitario.getVariable("p_act_acta_baja"))) {
            //System.out.println("etre a imprimir el acta de baja");            
            set_activos.getTab_seleccion().setSql(ser_activos.getSqlListaActivosFijos("1", custodio_constatacion, "1", estado_activo, clase_activo));
            set_activos.getTab_seleccion().ejecutarSql();
            set_activos.dibujar();

        }
        else if (valor_combo.equals(utilitario.getVariable("p_act_acta_cambio"))) {
            //System.out.println("etre a imprimir el acta de baja");
            set_activos.getTab_seleccion().setSql(ser_activos.getSqlListaActivosFijos("1", custodio_actual, "1", estado_activo, clase_activo));
            set_activos.getTab_seleccion().ejecutarSql();
            set_activos.dibujar();

        }

    }

    public void aceptarActivo() {

        String str_seleccionado = set_activos.getSeleccionados();
        if (str_seleccionado != null) {
        TablaGenerica tabla_activos = utilitario.consultar("select * from act_activo_fijo where ide_acafi in (" + str_seleccionado + ")");

            for (int i = 0; i < tabla_activos.getTotalFilas(); i++) {
                tab_tabla2.insertar();
                tab_tabla2.setValor("ide_acafi", tabla_activos.getValor(i, "ide_acafi"));
                tab_tabla2.setValor("ide_aceaf", tabla_activos.getValor(i, "ide_aceaf"));
            }
            utilitario.addUpdate("tab_tabla2");

        } else {
            utilitario.agregarMensajeInfo("SELECCIONE OPCION", "Seleccione un registro");
        }

    }

    public void seleccionarTipoActa() {
        if (com_tipo_acta.getValue() != null) {
            tab_tabla.setCondicion("ide_actia=" + com_tipo_acta.getValue());
            tab_tabla.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla.getValorSeleccionado());

        } else {
            tab_tabla.setCondicion("ide_actia=-1");
            tab_tabla.ejecutarSql();
        }
        utilitario.addUpdate("tab_tabla");
    }

    public void actualizarCampos() {
        tab_tabla.getColumna("observacion_acact").setLectura(true);
        utilitario.addUpdate("tab_tabla");
    }

    public void limpiar() {
        com_tipo_acta.limpiar();
        tab_tabla.limpiar();
        tab_tabla2.limpiar();
    }
    String valor_combo = "";
    String sec_modulo = "";

    @Override
    public void insertar() {

        actaEntregaRecepcion();

    }
    //reporte

    public void generarPDF() {
        valor_combo = com_tipo_acta.getValue().toString();
        
        if (com_tipo_acta.getValue() != null) {
            
            TablaGenerica tab_empleado= utilitario.consultar("select ide_geper,nom_geper from gen_persona where ide_geper ="+utilitario.getVariable("p_act_custodio"));
            ///////////AQUI ABRE EL REPORTE
            Map parametros = new HashMap();
            p_parametros.put("titulo", "CERTIFICACION PRESUPUESTARIA");
            p_parametros.put("ide_acact", Integer.parseInt(tab_tabla.getValor("ide_acact")));
            p_parametros.put("nombre", utilitario.getVariable("NICK"));
            p_parametros.put("pres_activos", tab_empleado.getValor("nom_geper"));
            p_parametros.put("pciudad", utilitario.getVariable("p_con_ciudad"));
            p_parametros.put("porganizacion", utilitario.getVariable("p_con_organizacion"));
            String reporte = "";

            if (valor_combo.equals(utilitario.getVariable("p_act_acta_entrega_recep"))) {
                reporte = "rep_activos/rep_acta_entrega.jasper";
            } else if (valor_combo.equals(utilitario.getVariable("p_act_acta_constatacion"))) {
                reporte = "rep_activos/rep_acta_constata.jasper";
            } else if (valor_combo.equals(utilitario.getVariable("p_act_acta_baja"))) {
                reporte = "rep_activos/rep_acta_baja.jasper";
            } else if (valor_combo.equals(utilitario.getVariable("p_act_acta_cambio"))) {
                reporte = "rep_activos/rep_acta_cambio.jasper";
            }

            //System.out.println(" " + p_parametros + " reportes " + reporte);
            vipdf_actas.setVisualizarPDF(reporte, p_parametros);
            vipdf_actas.dibujar();
            utilitario.addUpdate("vipdf_actas");
        } else {
            utilitario.agregarMensajeInfo("Seleccione el Tipo de Acta", "Seleccione el Tipo de Acta para poder continuar");
        }
    }

    public void abrirListaReportes() {

        if (com_tipo_acta.getValue() == null) {
            utilitario.agregarMensajeInfo("Seleccione el Tipo de Acta", "Seleccione el Tipo de Acta para poder continuar");

        } else {
            // TODO Auto-generated method stub
            /*
        if(tab_tabla.getValor("bloqueado_acact").equals("false")){
            dia_radio.dibujar();
        }
        else {
                if(dia_radio.isVisible())
                {
                    if(rad_bloqueado.getValue().equals("1")){  //cerrar actas
                        tab_tabla.setValor("bloqueado_acact", "true");
                        utilitario.addUpdateTabla(tab_tabla, "bloqueado_acact", "");
                        guardar();
                    }
                    dia_radio.cerrar();
                }*/
            rep_reporte.dibujar();
            //}
        }
    }

    public void aceptarReporte() {

        if (rep_reporte.getReporteSelecionado().equals("Acta Entrega Recepcion")) {
            if (rep_reporte.isVisible()) {
                p_parametros = new HashMap();
                rep_reporte.cerrar();
                p_parametros.put("titulo", "CERTIFICACION PRESUPUESTARIA");
                p_parametros.put("ide_acact", tab_tabla.getValor("ide_acact"));
                p_parametros.put("nombre", utilitario.getVariable("NICK"));
                self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                self_reporte.dibujar();

            } else {
                utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");

            }
        } else if (rep_reporte.getReporteSelecionado().equals("Acta Constatacion Fisica")) {
            if (rep_reporte.isVisible()) {

                rep_reporte.cerrar();

                p_parametros = new HashMap();
                rep_reporte.cerrar();
                p_parametros.put("titulo", "COMPROMISO PRESUPUESTARIA");
                p_parametros.put("nombre", utilitario.getVariable("NICK"));
                p_parametros.put("ide_acact", tab_tabla.getValor("ide_acact"));
                System.out.println("paso parametrios " + p_parametros);
                self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                self_reporte.dibujar();
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Acta de Bajas")) {
            if (rep_reporte.isVisible()) {

                rep_reporte.cerrar();

                p_parametros = new HashMap();
                rep_reporte.cerrar();
                p_parametros.put("titulo", "COMPROMISO PRESUPUESTARIA");
                p_parametros.put("nombre", utilitario.getVariable("NICK"));
                p_parametros.put("ide_acact", tab_tabla.getValor("ide_acact"));
                //System.out.println("paso parametrios "+p_parametros);
                self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                self_reporte.dibujar();
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Acta Cambio de Custodio")) {
            if (rep_reporte.isVisible()) {

                rep_reporte.cerrar();

                p_parametros = new HashMap();
                rep_reporte.cerrar();
                p_parametros.put("titulo", "COMPROMISO PRESUPUESTARIA");
                p_parametros.put("nombre", utilitario.getVariable("NICK"));
                p_parametros.put("ide_acact", tab_tabla.getValor("ide_acact"));
                p_parametros.put("pres_activos", utilitario.getVariable("p_act_custodio"));
                //System.out.println("paso parametrios "+p_parametros);
                self_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                self_reporte.dibujar();
            }
        }

    }

    @Override
    public void guardar() {
        valor_combo = com_tipo_acta.getValue().toString();
        if (tab_tabla.getValor("bloqueado_acact").equals("true") || tab_tabla.getValor("anulado_acact").equals("true")) {
            utilitario.agregarMensajeError("No se puede guardar", "El acta se encuentra validado y bloqueado, no se puede proceder con la modificación");
        } else {
            if (tab_tabla.guardar()) {
                if (tab_tabla2.guardar()) {
                    guardarPantalla();
                    // damos d ebaja a todos los actovos que se dan de baja en elacta
                    if (valor_combo.equals(utilitario.getVariable("p_act_acta_baja"))&&tab_tabla.getValor("bloqueado_acact").equals("true")) {
                        utilitario.getConexion().ejecutarSql("update act_activo_fijo set ide_aceaf=4 where ide_acafi in (select ide_acafi from act_movimiento where ide_acact= "+tab_tabla.getValor("ide_acact")+")");
                    }
                }
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
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

    public Dialogo getDia_activos() {
        return dia_activos;
    }

    public void setDia_activos(Dialogo dia_activos) {
        this.dia_activos = dia_activos;
    }

    public SeleccionTabla getSet_activos() {
        return set_activos;
    }

    public void setSet_activos(SeleccionTabla set_activos) {
        this.set_activos = set_activos;
    }

    public SeleccionTabla getSet_tipo_acta() {
        return set_tipo_acta;
    }

    public void setSet_tipo_acta(SeleccionTabla set_tipo_acta) {
        this.set_tipo_acta = set_tipo_acta;
    }

    public Tabla getTab_acta_entrega() {
        return tab_acta_entrega;
    }

    public void setTab_acta_entrega(Tabla tab_acta_entrega) {
        this.tab_acta_entrega = tab_acta_entrega;
    }

    public Dialogo getDia_actas() {
        return dia_actas;
    }

    public void setDia_actas(Dialogo dia_actas) {
        this.dia_actas = dia_actas;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSelf_reporte() {
        return self_reporte;
    }

    public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
        this.self_reporte = self_reporte;
    }

    public Dialogo getDia_radio() {
        return dia_radio;
    }

    public void setDia_radio(Dialogo dia_radio) {
        this.dia_radio = dia_radio;
    }

    public VisualizarPDF getVipdf_actas() {
        return vipdf_actas;
    }

    public void setVipdf_actas(VisualizarPDF vipdf_actas) {
        this.vipdf_actas = vipdf_actas;
    }

    public Confirmar getCon_confirma() {
        return con_confirma;
    }

    public void setCon_confirma(Confirmar con_confirma) {
        this.con_confirma = con_confirma;
    }

    public Dialogo getDia_anulado() {
        return dia_anulado;
    }

    public void setDia_anulado(Dialogo dia_anulado) {
        this.dia_anulado = dia_anulado;
    }

}

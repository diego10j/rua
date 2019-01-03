/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pensiones;

/**
 *
 */
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.Boton;
import framework.componentes.BotonesCombo;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import paq_contabilidad.ejb.ServicioContabilidad;
import servicios.pensiones.ServicioPensiones;

import sistema.aplicacion.Pantalla;

public class pre_alumno_periodo extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Combo com_periodo_academico = new Combo();
    private Combo com_cursos = new Combo();
    private Combo com_paralelos = new Combo();
    private Combo com_especialidad = new Combo();
    private Combo com_mes = new Combo();
    private Combo com_estado = new Combo();
    private SeleccionTabla sel_tab_alumno = new SeleccionTabla();
    private SeleccionTabla sel_tab_representante = new SeleccionTabla();
    private SeleccionTabla sel_periodo_academico = new SeleccionTabla();
    private SeleccionTabla sel_especialidades = new SeleccionTabla();
    private SeleccionTabla sel_cursos = new SeleccionTabla();
    private SeleccionTabla sel_paralelos = new SeleccionTabla();
    private Dialogo dia_emision = new Dialogo();
    private Calendario fechaInicio = new Calendario();
    private Calendario fechaFin = new Calendario();
    private Calendario fechaDescuento = new Calendario();
    private Combo com_conceptos = new Combo();
    private AreaTexto area_dialogo = new AreaTexto();
    private Dialogo dia_retiro = new Dialogo();
    private Calendario fecha_retiro = new Calendario();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private VisualizarPDF vipdf_alumnos_matriculados = new VisualizarPDF();
    private Map parametro = new HashMap();
    private Radio rad_descuento = new Radio();
    private Dialogo dia_descuento= new Dialogo();
    private Texto txt_val_descuento=new Texto();
    private Dialogo dia_email= new Dialogo();
    private Texto txt_email=new Texto();
    String titulo_alumno = "";
    String periodo_academico = "";
    String especialidad = "";
    String curso = "";
    String paralelo = "";

    @EJB
    private ServicioPensiones ser_pensiones = (ServicioPensiones) utilitario.instanciarEJB(ServicioPensiones.class);
    @EJB
    private ServiciosAdquisiones ser_adqusiiones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    @EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);

    public pre_alumno_periodo() {

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);
        bar_botones.agregarReporte();
        sel_rep.setId("sel_rep");
        agregarComponente(sel_rep);
        agregarComponente(rep_reporte);

        Boton bot_al_matri = new Boton();
        bot_al_matri.setIcon("ui-icon-print");
        bot_al_matri.setValue("IMPRIMIR ALUMNOS MATRICULADOS");
        bot_al_matri.setMetodo("generarPDF");
        //bot_fac_elec.setMetodo(ser_pensiones.generarFacturaElectronica("-1"));

        com_periodo_academico.setId("cmb_periodo_academico");
        com_periodo_academico.setCombo(ser_pensiones.getPeriodoAcademico("true"));

        com_cursos.setId("com_cursos");
        com_cursos.setCombo(ser_pensiones.getCursos("true,false"));

        com_paralelos.setId("com_paralelos");
        com_paralelos.setCombo(ser_pensiones.getParalelos("true,false"));

        com_especialidad.setId("com_especialidad");
        com_especialidad.setCombo(ser_pensiones.getEspecialidad("true"));

        com_estado.setId("com_estado");
        com_estado.setCombo(ser_pensiones.estado_estudiante());

        Grid grup_titulo = new Grid();
        grup_titulo.setColumns(10);
        grup_titulo.setWidth("100%");
        grup_titulo.setId("grup_titulo");
        grup_titulo.getChildren().add(new Etiqueta("Periódo Académico"));
        grup_titulo.getChildren().add(com_periodo_academico);
        grup_titulo.getChildren().add(new Etiqueta("Curso"));
        grup_titulo.getChildren().add(com_cursos);
        grup_titulo.getChildren().add(new Etiqueta("Paralelo"));
        grup_titulo.getChildren().add(com_paralelos);
        grup_titulo.getChildren().add(new Etiqueta("Especialidad"));
        grup_titulo.getChildren().add(com_especialidad);
        grup_titulo.getChildren().add(new Etiqueta("Estado"));
        grup_titulo.getChildren().add(com_estado);

        // boton seleccion inversa
        BotonesCombo boc_seleccion_inversa = new BotonesCombo();
        ItemMenu itm_todas = new ItemMenu();
        ItemMenu itm_niguna = new ItemMenu();

        boc_seleccion_inversa.setValue("Selección Inversa");
        boc_seleccion_inversa.setIcon("ui-icon-circle-check");
        boc_seleccion_inversa.setMetodo("seleccinarInversa");
        boc_seleccion_inversa.setUpdate("tab_tabla1");
        itm_todas.setValue("Seleccionar Todo");
        itm_todas.setIcon("ui-icon-check");
        itm_todas.setMetodo("seleccionarTodas");
        itm_todas.setUpdate("tab_tabla1");
        boc_seleccion_inversa.agregarBoton(itm_todas);
        itm_niguna.setValue("Seleccionar Ninguna");
        itm_niguna.setIcon("ui-icon-minus");
        itm_niguna.setMetodo("seleccionarNinguna");
        itm_niguna.setUpdate("tab_tabla1");
        boc_seleccion_inversa.agregarBoton(itm_niguna);

        tab_tabla1.setId("tab_tabla1");   //identificador
        tab_tabla1.setTabla("REC_ALUMNO_PERIODO", "IDE_RECALP", 1);
        tab_tabla1.getColumna("descripcion_recalp").setVisible(false);
        tab_tabla1.getColumna("ide_repea").setVisible(false);
        tab_tabla1.getColumna("ide_repar").setCombo(ser_pensiones.getParalelos("true,false"));
        tab_tabla1.getColumna("ide_recur").setCombo(ser_pensiones.getCursos("true,false"));
        tab_tabla1.getColumna("ide_reces").setCombo(ser_pensiones.getEspecialidad("true,false"));
        //tab_tabla1.getColumna("ide_repar").setAutoCompletar();
        //tab_tabla1.getColumna("ide_recur").setAutoCompletar();
        //tab_tabla1.getColumna("ide_reces").setAutoCompletar();
        tab_tabla1.getColumna("ide_repar").setLectura(true);
        tab_tabla1.getColumna("ide_recur").setLectura(true);
        tab_tabla1.getColumna("ide_reces").setLectura(true);
        tab_tabla1.getColumna("ide_geper").setLectura(true);
        tab_tabla1.getColumna("retirado_recalp").setLectura(true);
        tab_tabla1.getColumna("retirado_recalp").setLectura(true);
        tab_tabla1.getColumna("retirado_recalp").setValorDefecto("FALSE");
        tab_tabla1.getColumna("descuento_recalp").setValorDefecto("false");
        tab_tabla1.getColumna("activo_recalp").setValorDefecto("true");
        tab_tabla1.getColumna("activo_recalp").setLectura(true);
        tab_tabla1.getColumna("detalle_retiro_recalp").setLectura(true);
        tab_tabla1.getColumna("fecha_retiro_recalp").setLectura(true);
        tab_tabla1.getColumna("valor_descuento_recalp").setValorDefecto("0");
        tab_tabla1.getColumna("ide_geper").setFiltroContenido();
        tab_tabla1.getColumna("ide_geper").setCombo(ser_pensiones.getListaAlumnos("2", ""));
        tab_tabla1.getColumna("gen_ide_geper").setCombo(ser_pensiones.getListaAlumnos("2", ""));
        tab_tabla1.getColumna("ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("gen_ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("ide_repar").setAncho(20);
        tab_tabla1.getColumna("ide_geper").setEstilo("width:20 px");
        tab_tabla1.setCondicion("IDE_RECALP = -1");
        tab_tabla1.setTipoSeleccion(true);
        tab_tabla1.dibujar();
        PanelTabla pat_tabla1 = new PanelTabla();
        pat_tabla1.setId("pat_tabla1");
        pat_tabla1.getChildren().add(boc_seleccion_inversa);
        pat_tabla1.setPanelTabla(tab_tabla1);
        Division div_tabla1 = new Division();
        div_tabla1.setId("div_tabla1");
        div_tabla1.dividir1(pat_tabla1);

        Division div_padre = new Division();
        div_padre.dividir2(grup_titulo, div_tabla1, "8%", "H");
        agregarComponente(div_padre);

        Boton bot_filtro_consulta = new Boton();
        bot_filtro_consulta.setIcon("ui-icon-search");
        bot_filtro_consulta.setValue("CONSULTAR ALUMNOS");
        bot_filtro_consulta.setMetodo("filtroAlumno");
        bar_botones.agregarBoton(bot_filtro_consulta);

        bar_botones.agregarBoton(bot_al_matri);

        //     bar_botones.agregarBoton(bot_filtro_alumno);
        Boton bot_imp_alumno = new Boton();
        bot_imp_alumno.setIcon("ui-icon-person");
        bot_imp_alumno.setValue("IMPORTAR ALUMNOS");
        bot_imp_alumno.setMetodo("abrirDialogoAlumno");
        bar_botones.agregarBoton(bot_imp_alumno);

        Boton bot_imp_repre = new Boton();
        bot_imp_repre.setIcon("ui-icon-person");
        bot_imp_repre.setValue("AGREGAR REPRESENTANTE");
        bot_imp_repre.setMetodo("abrirDialogoRepresentante");
        bar_botones.agregarBoton(bot_imp_repre);

        Boton bot_emision = new Boton();
        bot_emision.setIcon("ui-icon-person");
        bot_emision.setValue("REALIZAR LA EMISION DE PENSIONES");
        bot_emision.setMetodo("abrirDialogoConcepto");
        bar_botones.agregarBoton(bot_emision);

        sel_tab_alumno.setId("sel_tab_alumno");
        sel_tab_alumno.setTitle("ALUMNOS");
        sel_tab_alumno.setSeleccionTabla(ser_pensiones.getListaAlumnos("1", "1"), "ide_geper");
        sel_tab_alumno.setWidth("80%");
        sel_tab_alumno.setHeight("70%");
        sel_tab_alumno.getBot_aceptar().setMetodo("aceptarAlumno");
        sel_tab_alumno.getTab_seleccion().getColumna("identificac_geper").setFiltroContenido();
        sel_tab_alumno.getTab_seleccion().getColumna("nom_geper").setFiltroContenido();
        agregarComponente(sel_tab_alumno);

        sel_tab_representante.setId("sel_tab_representante");
        sel_tab_representante.setTitle("REPRESENTANTE DEL ALUMNO: " + titulo_alumno);
        sel_tab_representante.setSeleccionTabla(ser_pensiones.getListaAlumnos("1", "0"), "ide_geper");
        sel_tab_representante.setWidth("80%");
        sel_tab_representante.setHeight("70%");
        sel_tab_representante.setRadio();
        sel_tab_representante.getTab_seleccion().getColumna("identificac_geper").setFiltroContenido();
        sel_tab_representante.getTab_seleccion().getColumna("nom_geper").setFiltroContenido();
        sel_tab_representante.getBot_aceptar().setMetodo("aceptarRepresentante");
        agregarComponente(sel_tab_representante);

        List lista = new ArrayList();
        Object dato1[] = {
            "1", "Si"
        };
        Object dato2[] = {
            "2", "No"
        };
        lista.add(dato1);
        lista.add(dato2);
        // creo dialogo para crear modalidad
        dia_emision.setId("dia_emision");
        dia_emision.setTitle("Seleccion los parámetros");
        dia_emision.setWidth("25%");
        dia_emision.setHeight("30%");
        dia_emision.getBot_aceptar().setMetodo("generarConceptos");
        dia_emision.setResizable(false);

        com_conceptos.setId("com_conceptos");
        com_conceptos.setCombo(ser_pensiones.getSqlConceptos());

        //select ide_gemes, nombre_gemes from gen_mes order by ide_gemes
        com_mes.setId("com_mes");
        com_mes.setCombo(ser_contabilidad.getMes("true, false"));

        Grid gru_cuerpo = new Grid();
        gru_cuerpo.setColumns(2);

        Etiqueta eti_concepto = new Etiqueta();
        eti_concepto.setValue("Seleccione el concepto                                             ");
        eti_concepto.setStyle("font-size: 13px;border: none;text-shadow: 0px 2px 3px #ccc;background: none;");

        gru_cuerpo.getChildren().add(eti_concepto);
        gru_cuerpo.getChildren().add(com_conceptos);

        Etiqueta eti_mes = new Etiqueta();
        eti_mes.setValue("Seleccione el mes                                             ");
        eti_mes.setStyle("font-size: 13px;border: none;text-shadow: 0px 2px 3px #ccc;background: none;");

        gru_cuerpo.getChildren().add(eti_mes);
        gru_cuerpo.getChildren().add(com_mes);

        gru_cuerpo.getChildren().add(new Etiqueta("Fecha Emisión: "));
        fechaInicio.setId("fechaInicio");
        fechaInicio.setFechaActual();
        fechaInicio.setTipoBoton(true);
        gru_cuerpo.getChildren().add(fechaInicio);

        gru_cuerpo.getChildren().add(new Etiqueta("Fecha Vencimiento: "));
        fechaFin.setId("fechaFin");
        fechaFin.setFechaActual();
        fechaFin.setTipoBoton(true);
        gru_cuerpo.getChildren().add(fechaFin);

        gru_cuerpo.getChildren().add(new Etiqueta("Aplicar Descuento: "));
        rad_descuento.setId("rad_descuento");
        rad_descuento.setLocalValueSet(true);
        rad_descuento.setValue("1");
        rad_descuento.setRadio(lista);

        gru_cuerpo.getChildren().add(rad_descuento);

        dia_emision.setDialogo(gru_cuerpo);
        agregarComponente(dia_emision);

        Boton bot_retiro_alumno = new Boton();
        bot_retiro_alumno.setIcon("ui-icon-person");
        bot_retiro_alumno.setValue("RETIRAR ALUMNO");
        bot_retiro_alumno.setMetodo("abrirDialogoRetiro");
        bar_botones.agregarBoton(bot_retiro_alumno);
        


        dia_retiro.setId("dia_retiro");
        dia_retiro.setTitle("Ingrese la información");
        dia_retiro.setWidth("39%");
        dia_retiro.setHeight("24%");
        dia_retiro.getBot_aceptar().setMetodo("aceptarDescuento");
        dia_retiro.setResizable(false);

        Grid gri3 = new Grid();
        gri3.setColumns(1);
        Grid gri4 = new Grid();
        gri4.setColumns(2);
        //Etiqueta eti_fecha = new Etiqueta();
        //eti_fecha.setValue("FECHA DE RETIRO:                                             ");
        //eti_fecha.setStyle("font-size: 11px;border: none;text-shadow: 0px 2px 3px #ccc;background: none;");
        gri4.getChildren().add(new Etiqueta("<strong>FECHA DE RETIRO: </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        fecha_retiro.setId("fecha_retiro");
        fecha_retiro.setFechaActual();
        fecha_retiro.setTipoBoton(true);
        //gri4.getChildren().add(eti_fecha);
        gri4.getChildren().add(fecha_retiro);
        gri3.getChildren().add(gri4);

        area_dialogo = new AreaTexto();
        area_dialogo.setCols(90);
        area_dialogo.setMaxlength(190);
        area_dialogo.setRows(2);
        gri3.getChildren().add(new Etiqueta("<strong>MOTIVO DE RETIRO DEL ALUMNO: </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri3.getChildren().add(area_dialogo);

        dia_retiro.setDialogo(gri3);
        agregarComponente(dia_retiro);

        
        Boton bot_aplica_descuento = new Boton();
        bot_aplica_descuento.setIcon("ui-icon-person");
        bot_aplica_descuento.setValue("APLICAR DESCUENTO");
        bot_aplica_descuento.setMetodo("abrirDialogoDescuento");
        bar_botones.agregarBoton(bot_aplica_descuento);
        
        dia_descuento.setId("dia_descuento");
        dia_descuento.setTitle("Ingrese el Descuento");
        dia_descuento.setWidth("39%");
        dia_descuento.setHeight("24%");
        dia_descuento.getBot_aceptar().setMetodo("aceptarDialogoDescuento");
        dia_descuento.setResizable(false);

        Grid gri5 = new Grid();
        gri5.setColumns(2);
         gri5.getChildren().add(new Etiqueta("<strong>Valor Descuento: </strong> <span style='color:red;font-weight: bold;'>*</span>"));
         txt_val_descuento.setId("txt_val_descuento");
         txt_val_descuento.setSoloNumeros();
        gri5.getChildren().add(txt_val_descuento);
        
        fechaDescuento.setId("fechaDescuento");
        fechaDescuento.setFechaActual();
        fechaDescuento.setTipoBoton(true);
         gri5.getChildren().add(new Etiqueta("<strong>Fecha Descuento: </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri5.getChildren().add(fechaDescuento);

        dia_descuento.setDialogo(gri5);
        agregarComponente(dia_descuento);
        
        // dialogo para modificar email
        Boton bot_email = new Boton();
        bot_email.setIcon("ui-icon-person");
        bot_email.setValue("ACTUALIZAR CORREO");
        bot_email.setMetodo("abrirDialogoEmail");
        bar_botones.agregarBoton(bot_email);
        
        dia_email.setId("dia_email");
        dia_email.setTitle("Ingrese el EMAIL");
        dia_email.setWidth("39%");
        dia_email.setHeight("24%");
        dia_email.getBot_aceptar().setMetodo("aceptarDialogoEmail");
        dia_email.setResizable(false);

        Grid gri6 = new Grid();
        gri6.setColumns(2);
        gri6.getChildren().add(new Etiqueta("<strong>Email: </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        txt_email.setId("txt_email");
        gri6.getChildren().add(txt_email);
        
        dia_email.setDialogo(gri6);
        agregarComponente(dia_email);

        
        sel_periodo_academico.setId("sel_periodo_academico");
        sel_periodo_academico.setSeleccionTabla(ser_pensiones.getPeriodoAcademico("true, false"), "ide_repea");
        sel_periodo_academico.setWidth("60%");
        sel_periodo_academico.setHeight("40%");
        sel_periodo_academico.setRadio();
        sel_periodo_academico.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(sel_periodo_academico);

        sel_especialidades.setId("sel_especialidades");
        sel_especialidades.setSeleccionTabla(ser_pensiones.getEspecialidad("true, false"), "ide_reces");
        sel_especialidades.setWidth("40%");
        sel_especialidades.setHeight("40%");
        sel_especialidades.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(sel_especialidades);

        sel_cursos.setId("sel_cursos");
        sel_cursos.setSeleccionTabla(ser_pensiones.getCursos("true, false"), "ide_recur");
        sel_cursos.setWidth("40%");
        sel_cursos.setHeight("40%");
        sel_cursos.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(sel_cursos);

        sel_paralelos.setId("sel_paralelos");
        sel_paralelos.setSeleccionTabla(ser_pensiones.getParalelos("true, false"), "ide_repar");
        sel_paralelos.setWidth("40%");
        sel_paralelos.setHeight("40%");
        sel_paralelos.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(sel_paralelos);

        vipdf_alumnos_matriculados.setId("vipdf_alumnos_matriculados");
        vipdf_alumnos_matriculados.setTitle("ALUMNOS MATRICULADOS");
        agregarComponente(vipdf_alumnos_matriculados);

    }

    public void abrirDialogoRetiro() {
        //if(tab_tabla1.){

        //}
        if (tab_tabla1.getTotalFilas() > 0) {
            if (tab_tabla1.getValorSeleccionado().isEmpty()) {
                utilitario.agregarMensajeError("Debe seleccionar al menos un estudiante para continuar", "");
            } else {
                dia_retiro.dibujar();
            }
        } else {
            utilitario.agregarMensajeError("Debe consultar los alumnos para continuar", "");
        }
    }

    public void aceptarRetiroAlumno() {
        try {
            String alumno_selec = tab_tabla1.getFilasSeleccionadas();
            utilitario.getConexion().ejecutarSql("update rec_alumno_periodo\n"
                    + "set retirado_recalp = true, detalle_retiro_recalp = '" + area_dialogo.getValue() + "',activo_recalp = false, fecha_retiro_recalp = '" + fecha_retiro.getValue() + "'\n"
                    + "where ide_recalp in (" + alumno_selec + ")");
            dia_retiro.cerrar();
            utilitario.agregarMensaje("Se ha guardado correctamente", "");
            tab_tabla1.actualizar();
            area_dialogo.limpiar();
            utilitario.addUpdate("tab_tabla1");
        } catch (Exception e) {

        }
    }
        public void aceptarDialogoEmail() {
        try {
            String alumno_selec = tab_tabla1.getFilasSeleccionadas();
            utilitario.getConexion().ejecutarSql("update rec_alumno_periodo\n"
                    + "set correo_recalp = '"+txt_email.getValue()+"' \n"
                    + "where ide_recalp in (" + alumno_selec + ")");
            dia_email.cerrar();
            utilitario.agregarMensaje("Se ha guardado correctamente", "");
            tab_tabla1.actualizar();
            txt_email.limpiar();
            utilitario.addUpdate("tab_tabla1");
        } catch (Exception e) {

        }
    }    
        public void aceptarDialogoDescuento() {
        try {
            String alumno_selec = tab_tabla1.getFilasSeleccionadas();
            utilitario.getConexion().ejecutarSql("update rec_alumno_periodo\n"
                    + "set descuento_recalp = true, valor_descuento_recalp = " + txt_val_descuento.getValue() + ", fecha_descuento_recalp='"+fechaDescuento.getFecha()+"' \n"
                    + "where ide_recalp in (" + alumno_selec + ")");
            dia_descuento.cerrar();
            utilitario.agregarMensaje("Se ha guardado correctamente", "");
            tab_tabla1.actualizar();
            txt_val_descuento.limpiar();
            utilitario.addUpdate("tab_tabla1");
        } catch (Exception e) {

        }
    }

    public void abrirDialogoConcepto() {
        if (tab_tabla1.getFilasSeleccionadas() != "") {
            String alumnos_seleccionados = tab_tabla1.getFilasSeleccionadas();
            TablaGenerica tab_cons_alumperiodo = utilitario.consultar("select * from rec_alumno_periodo where ide_recalp in (" + alumnos_seleccionados + ")");
            if (!tab_cons_alumperiodo.getValor("retirado_recalp").equals("true")) {
                dia_emision.dibujar();
            } else {
                utilitario.agregarMensajeError("No se puede continuar", "Existen alumnos retirados seleccionados, realice el filtro solo estudiantes activos");
            }

        } else {
            utilitario.agregarMensajeError("No se puede continuar", "Debe seleccionar al menos un registro para generar los rubros");
        }

    }

    public void generarConceptos() {
        String maximo = "";
        String maximo_detalle = "";
        String alumnos_seleccionados = tab_tabla1.getFilasSeleccionadas();
        TablaGenerica tab_cons_alumperiodo = utilitario.consultar("select * from rec_alumno_periodo where ide_recalp in (" + alumnos_seleccionados + ")");
        TablaGenerica tab_mes=utilitario.consultar("select ide_gemes,nombre_gemes from gen_mes where ide_gemes ="+com_mes.getValue());
        TablaGenerica tab_anio=utilitario.consultar("select ide_repea,nom_geani from rec_periodo_academico a,gen_anio b where a.ide_geani = b.ide_geani and ide_repea="+com_periodo_academico.getValue());
        TablaGenerica tab_detalle_concepto=utilitario.consultar("select ide_concepto_recon,des_concepto_recon from rec_concepto where ide_concepto_recon="+com_conceptos.getValue());        
        String opcion_descuento = rad_descuento.getValue().toString();
        String valor_descuento="0";
        String fecha_descuento;
        String aplica_descuento;

        
        for (int i = 0; i < tab_cons_alumperiodo.getTotalFilas(); i++) {
            TablaGenerica cod_max = utilitario.consultar(ser_pensiones.getCodigoMaximoTabla("rec_valores", "ide_titulo_recval"));
            maximo = cod_max.getValor("maximo");
            TablaGenerica tab_concepto = utilitario.consultar("select * from rec_forma_impuesto where ide_concepto_recon =" + com_conceptos.getValue());
                    if(opcion_descuento.equals("1")){
                        if(tab_cons_alumperiodo.getValor(i, "descuento_recalp").equals("true")){
                        valor_descuento=tab_cons_alumperiodo.getValor(i, "valor_descuento_recalp");
                        fecha_descuento="'"+tab_cons_alumperiodo.getValor(i, "fecha_descuento_recalp")+"'";
                        aplica_descuento=tab_cons_alumperiodo.getValor(i, "descuento_recalp");
                        }
                        else{
                           valor_descuento="0";
                        fecha_descuento="null";
                        aplica_descuento="false"; 
                        }
                    }
                    else{
                        valor_descuento="0";
                        fecha_descuento="null";
                        aplica_descuento="false";
                    }
            
            utilitario.getConexion().ejecutarSql("INSERT INTO rec_valores (ide_titulo_recval, ide_recalp, ide_sucu, ide_empr, ide_geper, gen_ide_geper, ide_recest, fecha_emision_recva, fecha_vence_recva, IDE_CONCEPTO_RECON"
                    + "                           , ide_gemes, valor_imponible_recva,valor_descuento_recva,aplica_total_descuento_recva,generado_fact_recva,detalle_recva,fecha_descuento_recva )\n"
                    + "VALUES (" + maximo + ", " + tab_cons_alumperiodo.getValor(i, "IDE_RECALP") + ", " + utilitario.getVariable("ide_sucu") + ", " + utilitario.getVariable("ide_empr") + " "
                    + ", " + tab_cons_alumperiodo.getValor(i, "ide_geper") + "," + tab_cons_alumperiodo.getValor(i, "gen_ide_geper") + ", " + utilitario.getVariable("p_pen_deuda_activa") + ", '" + fechaInicio.getFecha() + "', '" + fechaFin.getFecha()+ "', " + com_conceptos.getValue() + ",   "
                    + " " + com_mes.getValue() + ", 0," + valor_descuento + "," + aplica_descuento + ",false ,'"+tab_detalle_concepto.getValor("des_concepto_recon")+" CORRESPONDIENTE AL MES: "+tab_mes.getValor("nombre_gemes")+" DEL "+tab_anio.getValor("nom_geani")+"',"+fecha_descuento+" );");

            for (int j = 0; j < tab_concepto.getTotalFilas(); j++) {
                TablaGenerica tab_impuesto = utilitario.consultar("select * from rec_impuesto where ide_impuesto_reimp = " + tab_concepto.getValor(j, "ide_impuesto_reimp") + "");
                String valor_ide = tab_concepto.getValor(j, "ide_impuesto_reimp");
                TablaGenerica cod_max_detalle = utilitario.consultar(ser_pensiones.getCodigoMaximoTabla("rec_valor_detalle", "ide_valdet_revad"));
                maximo_detalle = cod_max_detalle.getValor("maximo");
                utilitario.getConexion().ejecutarSql("INSERT INTO rec_valor_detalle (ide_valdet_revad,ide_titulo_recval, ide_impuesto_reimp, cantidad_revad, precio_revad, total_revad, iva_inarti_revad, valor_descuento_revad, porcentaje_descuento_revad,detalle_revad)\n"
                        + "VALUES (" + maximo_detalle + ", " + maximo + ", " + tab_impuesto.getValor("ide_impuesto_reimp") + ", " + "1" + ", " + tab_impuesto.getValor("valor_reimp") + ", " + tab_impuesto.getValor("valor_reimp") + "-" + valor_descuento + ", " + "0" + "," + valor_descuento + ", " + "0" + ",'" + tab_impuesto.getValor("des_impuesto_reimp") + "'    );");
                // IDE_impuesto_revad, cantidad_revad, precio_revad, total_revad, iva_inarti_revad, valoor_desceunto_revad, porcentaje_desceunto_revad

            }
            TablaGenerica tab_suma_valores = utilitario.consultar("select 1 as codigo, sum(total_revad) as suma_total from rec_valor_detalle where IDE_TITULO_RECVAL  = " + maximo + "");
            utilitario.getConexion().ejecutarSql("update rec_valores set TOTAL_RECVA = " + tab_suma_valores.getValor("suma_total") + ", valor_imponible_recva = " + tab_suma_valores.getValor("suma_total") + ",base_no_objeto_iva_recva=0,base_tarifa0_recva=" + tab_suma_valores.getValor("suma_total") + ",base_grabada_recva=0,valor_iva_recva=0,tarifa_iva_recva=12  where IDE_TITULO_RECVAL = " + maximo + "");
        }
        dia_emision.cerrar();
        utilitario.agregarMensaje("Se ha recaudado correctamente", "");

    }

    public void filtroAlumno() {
        String cm_per_aca = "";
        String cm_cur = com_cursos.getValue() + "";
        String cm_par = com_paralelos.getValue() + "";
        String cm_esp = com_especialidad.getValue() + "";
        String cm_est = com_estado.getValue() + "";
        System.out.println("entre al filtro del alumno");
        String condicion = "";
        if (com_periodo_academico.getValue() == null) {
            utilitario.agregarMensajeError("Seleccione Registro", "Para consultar listado de alumnos debe seleccionar un periodo academico");

        } else {
            condicion += " ide_repea= " + com_periodo_academico.getValue();
            if (!cm_cur.equals("null")) {
                condicion += " and ide_recur= " + cm_cur;
            }
            if (!cm_par.equals("null")) {
                condicion += " and ide_repar= " + cm_par;
            }
            if (!cm_esp.equals("null")) {
                condicion += " and ide_reces= " + cm_esp;
            }
            if (!cm_est.equals("null")) {
                if (cm_est.equals("1")) {
                    condicion += " and activo_recalp=true ";
                } else if (cm_est.equals("2")) {
                    condicion += " and retirado_recalp=true ";
                }
            }
            tab_tabla1.setCondicion(condicion);
            tab_tabla1.ejecutarSql();
            tab_tabla1.imprimirSql();
            utilitario.addUpdate("tab_tabla1");
        }
    }

    public void filtroComboPeriodoAcademnico() {

        tab_tabla1.setCondicion("ide_repea=" + com_periodo_academico.getValue().toString());
        tab_tabla1.ejecutarSql();
        utilitario.addUpdate("tab_tabla1");

    }

    public void filtroComboCursos() {

        tab_tabla1.setCondicion("ide_recur=" + com_cursos.getValue().toString());
        tab_tabla1.ejecutarSql();
        utilitario.addUpdate("tab_tabla1");
    }

    public void filtroComboParalelos() {

        tab_tabla1.setCondicion("ide_repar=" + com_paralelos.getValue().toString());
        tab_tabla1.ejecutarSql();
        utilitario.addUpdate("tab_tabla1");
    }

    public void filtroComboEspecialidad() {

        tab_tabla1.setCondicion("ide_reces=" + com_especialidad.getValue().toString());
        tab_tabla1.ejecutarSql();
        utilitario.addUpdate("tab_tabla1");
    }

    public void abrirDialogoRepresentante() {
        if (tab_tabla1.getTotalFilas() > 0) {
            TablaGenerica tab_consulta = utilitario.consultar("select ide_geper,nom_geper from gen_persona where ide_geper=" + tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_geper"));
            titulo_alumno = tab_consulta.getValor("nom_geper");
            sel_tab_representante.setTitle("REPRSENTANTE DEL ALUMNO: " + titulo_alumno);
            sel_tab_representante.dibujar();
        } else {
            utilitario.agregarMensajeError("No existen registros", "No existen registros de alumnos para asignar un representante");
        }

    }
        public void abrirDialogoEmail() {
        if (tab_tabla1.getFilasSeleccionadas() != "") {
            
            String alumnos_seleccionados = tab_tabla1.getFilasSeleccionadas();
            TablaGenerica tab_cons_alumperiodo = utilitario.consultar("select * from rec_alumno_periodo where ide_recalp in (" + alumnos_seleccionados + ")");
            if (!tab_cons_alumperiodo.getValor("retirado_recalp").equals("true")) {
                dia_email.dibujar();
            } else {
                utilitario.agregarMensajeError("No se puede continuar", "Existen alumnos retirados seleccionados, realice el filtro solo estudiantes activos");
            }
       
        } else {
            utilitario.agregarMensajeError("No se puede continuar", "Debe seleccionar al menos un registro para generar los rubros");
        }

    }
    public void abrirDialogoDescuento() {
        if (tab_tabla1.getFilasSeleccionadas() != "") {
            String alumnos_seleccionados = tab_tabla1.getFilasSeleccionadas();
            TablaGenerica tab_cons_alumperiodo = utilitario.consultar("select * from rec_alumno_periodo where ide_recalp in (" + alumnos_seleccionados + ")");
            if (!tab_cons_alumperiodo.getValor("retirado_recalp").equals("true")) {
                dia_descuento.dibujar();
            } else {
                utilitario.agregarMensajeError("No se puede continuar", "Existen alumnos retirados seleccionados, realice el filtro solo estudiantes activos");
            }
       
        } else {
            utilitario.agregarMensajeError("No se puede continuar", "Debe seleccionar al menos un registro para generar los rubros");
        }

    }
    public void abrirDialogoAlumno() {
        if (com_periodo_academico.getValue() == null) {
            utilitario.agregarMensajeError("No se puede Continuar", "Debe seleccionar el Periodo Académico");
        } else if (com_cursos.getValue() == null) {
            utilitario.agregarMensajeError("No se puede Continuar", "Debe seleccionar el Curso");
        } else if (com_paralelos.getValue() == null) {
            utilitario.agregarMensajeError("No se puede Continuar", "Debe seleccionar el Paralelo");
        } else if (com_especialidad.getValue() == null) {
            utilitario.agregarMensajeError("No se puede Continuar", "Debe seleccionar la especialidad");
        } else {

            sel_tab_alumno.dibujar();
        }
    }

    public void aceptarRepresentante() {
        
                    String alumno_selec = tab_tabla1.getFilasSeleccionadas();
                     String str_repre = sel_tab_representante.getValorSeleccionado();

            utilitario.getConexion().ejecutarSql("update rec_alumno_periodo\n"
                    + "set gen_ide_geper = " + str_repre + " \n"
                    + "where ide_recalp in (" + alumno_selec + ")");

        sel_tab_representante.cerrar();
        tab_tabla1.actualizar();
        utilitario.addUpdate("tab_tabla1");
    }

    public void aceptarAlumno() {
        String str_alum = sel_tab_alumno.getSeleccionados();
        TablaGenerica tab_dat_alum = utilitario.consultar("select ide_geper, identificac_geper, nom_geper  from gen_persona  where ide_vgtcl = 1 and ide_geper = " + str_alum + "order by nom_geper");
        for (int i = 0; i < tab_dat_alum.getTotalFilas(); i++) {
            if (tab_tabla1.isFilaInsertada() == false) {
                tab_tabla1.insertar();
            }
            tab_tabla1.setValor("ide_geper", tab_dat_alum.getValor(i, "ide_geper"));
        }
        tab_tabla1.setValor("ide_repea", com_periodo_academico.getValue().toString());
        tab_tabla1.setValor("ide_recur", com_cursos.getValue().toString());
        tab_tabla1.setValor("ide_repar", com_paralelos.getValue().toString());
        tab_tabla1.setValor("ide_reces", com_especialidad.getValue().toString());
        utilitario.addUpdateTabla(tab_tabla1, "ide_repea,ide_recur,ide_repar,ide_reces", "");

        sel_tab_alumno.cerrar();
        utilitario.addUpdate("tab_tabla1");
    }

    @Override
    public void insertar() {
        if (com_periodo_academico.getValue() == null) {
            utilitario.agregarMensajeError("No se puede Continuar", "Debe seleccionar el Periodo Académico");
        } else if (com_cursos.getValue() == null) {
            utilitario.agregarMensajeError("No se puede Continuar", "Debe seleccionar el Curso");
        } else if (com_paralelos.getValue() == null) {
            utilitario.agregarMensajeError("No se puede Continuar", "Debe seleccionar el Paralelo");
        } else if (com_especialidad.getValue() == null) {
            utilitario.agregarMensajeError("No se puede Continuar", "Debe seleccionar la especialidad");
        } else {
            tab_tabla1.insertar();
            tab_tabla1.setValor("ide_repea", com_periodo_academico.getValue().toString());
            tab_tabla1.setValor("ide_recur", com_cursos.getValue().toString());
            tab_tabla1.setValor("ide_repar", com_paralelos.getValue().toString());
            tab_tabla1.setValor("ide_reces", com_especialidad.getValue().toString());
            utilitario.addUpdateTabla(tab_tabla1, "ide_repea", "");
            utilitario.addUpdateTabla(tab_tabla1, "ide_recur", "");
            utilitario.addUpdateTabla(tab_tabla1, "ide_repar", "");
            utilitario.addUpdateTabla(tab_tabla1, "ide_reces", "");
            utilitario.addUpdate("tab_tabla1");
        }

    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla1.eliminar();
    }

    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra 
        rep_reporte.dibujar();

    }

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Listado de Alumnos")) {
            if (rep_reporte.isVisible()) {
                rep_reporte.cerrar();
                sel_periodo_academico.dibujar();
            } else if (sel_periodo_academico.isVisible()) {
                periodo_academico = sel_periodo_academico.getValorSeleccionado() + "";
                sel_periodo_academico.cerrar();
                sel_especialidades.dibujar();
            } else if (sel_especialidades.isVisible()) {
                especialidad = sel_especialidades.getSeleccionados() + "";
                sel_especialidades.cerrar();
                sel_cursos.dibujar();
            } else if (sel_cursos.isVisible()) {
                curso = sel_cursos.getSeleccionados() + "";
                sel_cursos.cerrar();
                sel_paralelos.dibujar();
            } else if (sel_paralelos.isVisible()) {
                //curso = sel_cursos.getSeleccionados();
                parametro = new HashMap();
                parametro.put("pide_especialidad", especialidad);
                parametro.put("pide_paralelo", sel_paralelos.getSeleccionados() + "");
                parametro.put("pide_curso", curso);
                parametro.put("pide_periodo", periodo_academico);
                parametro.put("nombre", utilitario.getVariable("NICK"));
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_paralelos.cerrar();
                sel_rep.dibujar();
                utilitario.addUpdate("sel_rep");

            }
        } else if (rep_reporte.getReporteSelecionado().equals("Listado de Alumnos Retirados")) {
            if (rep_reporte.isVisible()) {
                rep_reporte.cerrar();
                sel_periodo_academico.dibujar();
            } else if (sel_periodo_academico.isVisible()) {
                periodo_academico = sel_periodo_academico.getValorSeleccionado() + "";
                sel_periodo_academico.cerrar();
                sel_especialidades.dibujar();
            } else if (sel_especialidades.isVisible()) {
                especialidad = sel_especialidades.getSeleccionados() + "";
                sel_especialidades.cerrar();
                sel_cursos.dibujar();
            } else if (sel_cursos.isVisible()) {
                curso = sel_cursos.getSeleccionados() + "";
                sel_cursos.cerrar();
                sel_paralelos.dibujar();
            } else if (sel_paralelos.isVisible()) {
                //curso = sel_cursos.getSeleccionados();
                parametro = new HashMap();
                parametro.put("pide_especialidad", especialidad);
                parametro.put("pide_paralelo", sel_paralelos.getSeleccionados() + "");
                parametro.put("pide_curso", curso);
                parametro.put("pide_periodo", periodo_academico);
                parametro.put("nombre", utilitario.getVariable("NICK"));
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_paralelos.cerrar();
                sel_rep.dibujar();
                utilitario.addUpdate("sel_rep");

            }
        } else if (rep_reporte.getReporteSelecionado().equals("Listado de Becados")) {
            if (rep_reporte.isVisible()) {
                rep_reporte.cerrar();
                sel_periodo_academico.dibujar();
            } else if (sel_periodo_academico.isVisible()) {
                periodo_academico = sel_periodo_academico.getValorSeleccionado() + "";
                sel_periodo_academico.cerrar();
                sel_especialidades.dibujar();
            } else if (sel_especialidades.isVisible()) {
                especialidad = sel_especialidades.getSeleccionados() + "";
                sel_especialidades.cerrar();
                sel_cursos.dibujar();
            } else if (sel_cursos.isVisible()) {
                curso = sel_cursos.getSeleccionados() + "";
                sel_cursos.cerrar();
                sel_paralelos.dibujar();
            } else if (sel_paralelos.isVisible()) {
                //curso = sel_cursos.getSeleccionados();
                parametro = new HashMap();
                parametro.put("pide_especialidad", especialidad);
                parametro.put("pide_paralelo", sel_paralelos.getSeleccionados() + "");
                parametro.put("pide_curso", curso);
                parametro.put("pide_periodo", periodo_academico);
                parametro.put("nombre", utilitario.getVariable("NICK"));
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_paralelos.cerrar();
                sel_rep.dibujar();
                utilitario.addUpdate("sel_rep");

            }

        } else if (rep_reporte.getReporteSelecionado().equals("Numero de Alumnos Matriculados")) {
            if (rep_reporte.isVisible()) {
                rep_reporte.cerrar();
                sel_periodo_academico.dibujar();
            } else if (sel_periodo_academico.isVisible()) {
                periodo_academico = sel_periodo_academico.getValorSeleccionado() + "";
                sel_periodo_academico.cerrar();
                sel_especialidades.dibujar();
            } else if (sel_especialidades.isVisible()) {
                especialidad = sel_especialidades.getSeleccionados() + "";
                sel_especialidades.cerrar();
                sel_cursos.dibujar();
            } else if (sel_cursos.isVisible()) {
                curso = sel_cursos.getSeleccionados() + "";
                sel_cursos.cerrar();
                sel_paralelos.dibujar();
            } else if (sel_paralelos.isVisible()) {
                //curso = sel_cursos.getSeleccionados();
                parametro = new HashMap();
                parametro.put("pide_especialidad", especialidad);
                parametro.put("pide_paralelo", sel_paralelos.getSeleccionados() + "");
                parametro.put("pide_curso", curso);
                parametro.put("pide_periodo", periodo_academico);
                parametro.put("nombre", utilitario.getVariable("NICK"));
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_paralelos.cerrar();
                sel_rep.dibujar();
                utilitario.addUpdate("sel_rep");

            }
        } 
        else if (rep_reporte.getReporteSelecionado().equals("Alumno Deudas")) {
            if (rep_reporte.isVisible()) {
                rep_reporte.cerrar();
            
                //curso = sel_cursos.getSeleccionados();
                parametro = new HashMap();

                parametro.put("nombre", utilitario.getVariable("NICK"));
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_paralelos.cerrar();
                sel_rep.dibujar();
                utilitario.addUpdate("sel_rep");

            }
        } 
        else {
            utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun registro");
        }
    }

    public void generarPDF() {
        if (com_periodo_academico.getValue() != null) {
            ///////////AQUI ABRE EL REPORTE
            Map parametros = new HashMap();
            parametros.put("pide_periodo", com_periodo_academico.getValue() + "");
            parametros.put("nombre", utilitario.getVariable("NICK"));

            //System.out.println(" " + str_titulos);
            vipdf_alumnos_matriculados.setVisualizarPDF("rep_escuela_colegio/rep_lista_alumnos_total.jasper", parametros);
            vipdf_alumnos_matriculados.dibujar();
            utilitario.addUpdate("vipdf_alumnos_matriculados");
        } else {
            utilitario.agregarMensajeInfo("Seleccione el periodo académico", "");
        }
    }

    public void seleccionarTodas() {
        tab_tabla1.setSeleccionados(null);
        Fila seleccionados[] = new Fila[tab_tabla1.getTotalFilas()];
        for (int i = 0; i < tab_tabla1.getFilas().size(); i++) {
            seleccionados[i] = tab_tabla1.getFilas().get(i);
        }
        tab_tabla1.setSeleccionados(seleccionados);
        //calculoTotal();

    }

    public void seleccinarInversa() {
        if (tab_tabla1.getSeleccionados() == null) {
            seleccionarTodas();
        } else if (tab_tabla1.getSeleccionados().length == tab_tabla1.getTotalFilas()) {
            seleccionarNinguna();
        } else {
            Fila seleccionados[] = new Fila[tab_tabla1.getTotalFilas() - tab_tabla1.getSeleccionados().length];
            int cont = 0;
            for (int i = 0; i < tab_tabla1.getFilas().size(); i++) {
                boolean boo_selecionado = false;
                for (int j = 0; j < tab_tabla1.getSeleccionados().length; j++) {
                    if (tab_tabla1.getSeleccionados()[j].equals(tab_tabla1.getFilas().get(i))) {
                        boo_selecionado = true;
                        break;
                    }
                }
                if (boo_selecionado == false) {
                    seleccionados[cont] = tab_tabla1.getFilas().get(i);
                    cont++;
                }
            }
            tab_tabla1.setSeleccionados(seleccionados);
        }
        //calculoTotal();
    }

    public void seleccionarNinguna() {
        tab_tabla1.setSeleccionados(null);

    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Combo getCom_periodo_academico() {
        return com_periodo_academico;
    }

    public void setCom_periodo_academico(Combo com_periodo_academico) {
        this.com_periodo_academico = com_periodo_academico;
    }

    public Combo getCom_cursos() {
        return com_cursos;
    }

    public void setCom_cursos(Combo com_cursos) {
        this.com_cursos = com_cursos;
    }

    public Combo getCom_paralelos() {
        return com_paralelos;
    }

    public void setCom_paralelos(Combo com_paralelos) {
        this.com_paralelos = com_paralelos;
    }

    public Combo getCom_especialidad() {
        return com_especialidad;
    }

    public void setCom_especialidad(Combo com_especialidad) {
        this.com_especialidad = com_especialidad;
    }

    public SeleccionTabla getSel_tab_alumno() {
        return sel_tab_alumno;
    }

    public void setSel_tab_alumno(SeleccionTabla sel_tab_alumno) {
        this.sel_tab_alumno = sel_tab_alumno;
    }

    public ServicioPensiones getSer_pensiones() {
        return ser_pensiones;
    }

    public void setSer_pensiones(ServicioPensiones ser_pensiones) {
        this.ser_pensiones = ser_pensiones;
    }

    public SeleccionTabla getSel_tab_representante() {
        return sel_tab_representante;
    }

    public void setSel_tab_representante(SeleccionTabla sel_tab_representante) {
        this.sel_tab_representante = sel_tab_representante;
    }

    public Dialogo getDia_emision() {
        return dia_emision;
    }

    public void setDia_emision(Dialogo dia_emision) {
        this.dia_emision = dia_emision;
    }

    public Calendario getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Calendario fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Calendario getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Calendario fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Combo getCom_conceptos() {
        return com_conceptos;
    }

    public void setCom_conceptos(Combo com_conceptos) {
        this.com_conceptos = com_conceptos;
    }

    public AreaTexto getArea_dialogo() {
        return area_dialogo;
    }

    public void setArea_dialogo(AreaTexto area_dialogo) {
        this.area_dialogo = area_dialogo;
    }

    public Dialogo getDia_retiro() {
        return dia_retiro;
    }

    public void setDia_retiro(Dialogo dia_retiro) {
        this.dia_retiro = dia_retiro;
    }

    public Calendario getFecha_retiro() {
        return fecha_retiro;
    }

    public void setFecha_retiro(Calendario fecha_retiro) {
        this.fecha_retiro = fecha_retiro;
    }

    public SeleccionTabla getSel_periodo_academico() {
        return sel_periodo_academico;
    }

    public void setSel_periodo_academico(SeleccionTabla sel_periodo_academico) {
        this.sel_periodo_academico = sel_periodo_academico;
    }

    public SeleccionTabla getSel_especialidades() {
        return sel_especialidades;
    }

    public void setSel_especialidades(SeleccionTabla sel_especialidades) {
        this.sel_especialidades = sel_especialidades;
    }

    public SeleccionTabla getSel_cursos() {
        return sel_cursos;
    }

    public void setSel_cursos(SeleccionTabla sel_cursos) {
        this.sel_cursos = sel_cursos;
    }

    public SeleccionTabla getSel_paralelos() {
        return sel_paralelos;
    }

    public void setSel_paralelos(SeleccionTabla sel_paralelos) {
        this.sel_paralelos = sel_paralelos;
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

    public Map getParametro() {
        return parametro;
    }

    public void setParametro(Map parametro) {
        this.parametro = parametro;
    }

    public String getTitulo_alumno() {
        return titulo_alumno;
    }

    public void setTitulo_alumno(String titulo_alumno) {
        this.titulo_alumno = titulo_alumno;
    }

    public String getPeriodo_academico() {
        return periodo_academico;
    }

    public void setPeriodo_academico(String periodo_academico) {
        this.periodo_academico = periodo_academico;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getParalelo() {
        return paralelo;
    }

    public void setParalelo(String paralelo) {
        this.paralelo = paralelo;
    }

    public ServiciosAdquisiones getSer_adqusiiones() {
        return ser_adqusiiones;
    }

    public void setSer_adqusiiones(ServiciosAdquisiones ser_adqusiiones) {
        this.ser_adqusiiones = ser_adqusiiones;
    }

    public Combo getCom_mes() {
        return com_mes;
    }

    public void setCom_mes(Combo com_mes) {
        this.com_mes = com_mes;
    }

    public ServicioContabilidad getSer_contabilidad() {
        return ser_contabilidad;
    }

    public void setSer_contabilidad(ServicioContabilidad ser_contabilidad) {
        this.ser_contabilidad = ser_contabilidad;
    }

    public VisualizarPDF getVipdf_alumnos_matriculados() {
        return vipdf_alumnos_matriculados;
    }

    public void setVipdf_alumnos_matriculados(VisualizarPDF vipdf_alumnos_matriculados) {
        this.vipdf_alumnos_matriculados = vipdf_alumnos_matriculados;
    }

    public Dialogo getDia_descuento() {
        return dia_descuento;
    }

    public void setDia_descuento(Dialogo dia_descuento) {
        this.dia_descuento = dia_descuento;
    }

    public Calendario getFechaDescuento() {
        return fechaDescuento;
    }

    public void setFechaDescuento(Calendario fechaDescuento) {
        this.fechaDescuento = fechaDescuento;
    }

    public Dialogo getDia_email() {
        return dia_email;
    }

    public void setDia_email(Dialogo dia_email) {
        this.dia_email = dia_email;
    }

}

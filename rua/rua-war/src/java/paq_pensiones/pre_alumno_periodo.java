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
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import servicios.pensiones.ServicioPensiones;

import sistema.aplicacion.Pantalla;

public class pre_alumno_periodo extends Pantalla{
    private Tabla tab_tabla1 = new Tabla();
    private Combo com_periodo_academico = new Combo();
    private Combo com_cursos = new Combo();
    private Combo com_paralelos = new Combo();
    private Combo com_especialidad = new Combo();
    private SeleccionTabla sel_tab_alumno = new SeleccionTabla();
    private SeleccionTabla sel_tab_representante = new SeleccionTabla();
    String titulo_alumno="";
    private Dialogo dia_emision = new Dialogo();
    private Calendario fechaInicio = new Calendario();
    private Calendario fechaFin = new Calendario();
    private Combo com_conceptos = new Combo();
    
    @EJB
    private ServicioPensiones ser_pensiones = (ServicioPensiones) utilitario.instanciarEJB(ServicioPensiones.class);
    @EJB
    private ServiciosAdquisiones ser_adqusiiones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    
    public pre_alumno_periodo(){
        
        
        
        com_periodo_academico.setId("cmb_periodo_academico");
        com_periodo_academico.setCombo(ser_pensiones.getPeriodoAcademico("true"));

        com_cursos.setId("com_cursos");
        com_cursos.setCombo(ser_pensiones.getCursos("true,false"));
        
       
        com_paralelos.setId("com_paralelos");
        com_paralelos.setCombo(ser_pensiones.getParalelos("true,false"));
            
        com_especialidad.setId("com_especialidad");
        com_especialidad.setCombo(ser_pensiones.getEspecialidad("true"));
        
        Grid grup_titulo = new Grid();
        grup_titulo.setColumns(8);
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
        
        tab_tabla1.setId("tab_tabla1");   //identificador
        tab_tabla1.setTabla("REC_ALUMNO_PERIODO", "IDE_RECALP", 1);
        tab_tabla1.getColumna("descripcion_recalp").setVisible(false);
        tab_tabla1.getColumna("ide_repea").setVisible(false);
        tab_tabla1.getColumna("ide_repar").setCombo(ser_pensiones.getParalelos("true,false"));
        tab_tabla1.getColumna("ide_recur").setCombo(ser_pensiones.getCursos("true,false"));
        tab_tabla1.getColumna("ide_reces").setCombo(ser_pensiones.getEspecialidad("true,false"));
        tab_tabla1.getColumna("ide_repar").setAutoCompletar();
        tab_tabla1.getColumna("ide_recur").setAutoCompletar();
        tab_tabla1.getColumna("ide_reces").setAutoCompletar();
        tab_tabla1.getColumna("ide_repar").setLectura(true);
        tab_tabla1.getColumna("ide_recur").setLectura(true);
        tab_tabla1.getColumna("ide_reces").setLectura(true);
        tab_tabla1.getColumna("ide_geper").setLectura(true);
        tab_tabla1.getColumna("gen_ide_geper").setLectura(true);
        tab_tabla1.getColumna("ide_geper").setFiltroContenido();
        tab_tabla1.getColumna("ide_geper").setCombo(ser_pensiones.getListaAlumnos("2",""));
        tab_tabla1.getColumna("gen_ide_geper").setCombo(ser_pensiones.getListaAlumnos("2",""));
        tab_tabla1.getColumna("ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("gen_ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("ide_repar").setAncho(20);
        tab_tabla1.getColumna("ide_geper").setEstilo("width:20 px");
        tab_tabla1.setCondicion("IDE_RECALP = -1");
        tab_tabla1.dibujar();
        PanelTabla pat_tabla1 = new PanelTabla();
        pat_tabla1.setId("pat_tabla1");
        pat_tabla1.setPanelTabla(tab_tabla1);
        Division div_tabla1 = new Division();
        div_tabla1.setId("div_tabla1");
        div_tabla1.dividir1(pat_tabla1);
        
        
        Division div_padre = new Division();
        div_padre.dividir2(grup_titulo, div_tabla1, "8%","H");
        agregarComponente(div_padre);

        Boton bot_filtro_consulta = new Boton();
        bot_filtro_consulta.setIcon("ui-icon-search");
        bot_filtro_consulta.setValue("CONSULTAR ALUMNOS");
        bot_filtro_consulta.setMetodo("filtroAlumno");
        bar_botones.agregarBoton(bot_filtro_consulta);
        

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
        sel_tab_alumno.setSeleccionTabla(ser_pensiones.getListaAlumnos("1","1"), "ide_geper");
        sel_tab_alumno.setWidth("80%");
        sel_tab_alumno.setHeight("70%");
        sel_tab_alumno.getBot_aceptar().setMetodo("aceptarAlumno");
        sel_tab_alumno.getTab_seleccion().getColumna("identificac_geper").setFiltroContenido();
        sel_tab_alumno.getTab_seleccion().getColumna("nom_geper").setFiltroContenido();
        agregarComponente(sel_tab_alumno);
        
        sel_tab_representante.setId("sel_tab_representante");
        sel_tab_representante.setTitle("REPRESENTANTE DEL ALUMNO: "+titulo_alumno);
        sel_tab_representante.setSeleccionTabla(ser_pensiones.getListaAlumnos("1","0"), "ide_geper");
        sel_tab_representante.setWidth("80%");
        sel_tab_representante.setHeight("70%");
        sel_tab_representante.setRadio();
        sel_tab_representante.getTab_seleccion().getColumna("identificac_geper").setFiltroContenido();
        sel_tab_representante.getTab_seleccion().getColumna("nom_geper").setFiltroContenido();
        sel_tab_representante.getBot_aceptar().setMetodo("aceptarRepresentante");
        agregarComponente(sel_tab_representante);
        
        // creo dialogo para crear modalidad
        dia_emision.setId("dia_emision");
        dia_emision.setTitle("Seleccion los parámetros");
        dia_emision.setWidth("25%");
        dia_emision.setHeight("30%");
        dia_emision.getBot_aceptar().setMetodo("generarConceptos");
        dia_emision.setResizable(false);
        
        com_conceptos.setId("com_conceptos");
        com_conceptos.setCombo(ser_pensiones.getSqlConceptos());
    
        
        Grid gru_cuerpo = new Grid();
        gru_cuerpo.setColumns(2);
        Etiqueta eti_mensaje = new Etiqueta();
        eti_mensaje.setValue("Seleccione el concepto                                             ");
        eti_mensaje.setStyle("font-size: 13px;border: none;text-shadow: 0px 2px 3px #ccc;background: none;");
        
        gru_cuerpo.getChildren().add(eti_mensaje);
        gru_cuerpo.getChildren().add(com_conceptos);
        
        
        gru_cuerpo.getChildren().add(new Etiqueta("FECHA INICIAL: "));
        fechaInicio.setId("fechaInicio");
        fechaInicio.setFechaActual();
        fechaInicio.setTipoBoton(true);
        gru_cuerpo.getChildren().add(fechaInicio);
        
        

        gru_cuerpo.getChildren().add(new Etiqueta("FECHA FINAL: "));
        fechaFin.setId("fechaFin");
        fechaFin.setFechaActual();
        fechaFin.setTipoBoton(true);
        gru_cuerpo.getChildren().add(fechaFin);
        
        dia_emision.setDialogo(gru_cuerpo);
        agregarComponente(dia_emision);
    
    }
    public void abrirDialogoConcepto(){
        if (tab_tabla1.getValorSeleccionado() != null){
        dia_emision.dibujar();
        }
        else {
            utilitario.agregarMensajeError("No se puede continuar", "Debe filtrar los alumnos a los que desea generar los rubros");
        }
    }
    public void generarConceptos(){
        
          int l_alumno = tab_tabla1.getTotalFilasVisibles();
          for (int i=0; i<tab_tabla1.getTotalFilasVisibles(); i++){
//              System.out.println("alum "+alumnos);
          }
        
    }
    public void filtroAlumno(){
        String cm_per_aca = "";
        String cm_cur = com_cursos.getValue()+"";
        String cm_par = com_paralelos.getValue()+"";
        String cm_esp = com_especialidad.getValue()+"";
        
        String condicion="";
        if(com_periodo_academico.getValue()==null){
                       utilitario.agregarMensajeError("Seleccione Registro", "Para consultar listado de alumnos debe seleccionar un periodo academico");

        }
        else {
         condicion+=" ide_repea= "+com_periodo_academico.getValue();
           if(!cm_cur.equals("null")) {
               condicion+=" and ide_recur= "+cm_cur;
           }
           if(!cm_par.equals("null")) {
               condicion+=" and ide_repar= "+cm_par;
           }
           if(!cm_esp.equals("null")) {
               condicion+=" and ide_reces= "+cm_esp;
           }
           System.out.println("condicion "+condicion);
           tab_tabla1.setCondicion(condicion);
           tab_tabla1.ejecutarSql();
           utilitario.addUpdate("tab_tabla1");
        }
    }
    
    public void filtroComboPeriodoAcademnico(){
        
        tab_tabla1.setCondicion("ide_repea="+com_periodo_academico.getValue().toString());
        tab_tabla1.ejecutarSql();
        utilitario.addUpdate("tab_tabla1");
        
     
    }
    public void filtroComboCursos(){
        
        tab_tabla1.setCondicion("ide_recur="+com_cursos.getValue().toString());
        tab_tabla1.ejecutarSql();
        utilitario.addUpdate("tab_tabla1");
    }
    public void filtroComboParalelos(){
        
        tab_tabla1.setCondicion("ide_repar="+com_paralelos.getValue().toString());
        tab_tabla1.ejecutarSql();
        utilitario.addUpdate("tab_tabla1");
    }
    public void filtroComboEspecialidad(){
        
        tab_tabla1.setCondicion("ide_reces="+com_especialidad.getValue().toString());
        tab_tabla1.ejecutarSql();
        utilitario.addUpdate("tab_tabla1");
    }
    public void abrirDialogoRepresentante(){
        if(tab_tabla1.getTotalFilas()>0){
                    TablaGenerica tab_consulta = utilitario.consultar("select ide_geper,nom_geper from gen_persona where ide_geper="+tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_geper"));
        titulo_alumno=tab_consulta.getValor("nom_geper");
        sel_tab_representante.setTitle("REPRSENTANTE DEL ALUMNO: "+titulo_alumno);
        sel_tab_representante.dibujar();
        }
        else {
            utilitario.agregarMensajeError("No existen registros", "No existen registros de alumnos para asignar un representante");
        }
        
    }    
    public void abrirDialogoAlumno(){
        if (com_periodo_academico.getValue() == null){
            utilitario.agregarMensajeError("No se puede Continuar", "Debe seleccionar el Periodo Académico");       
        }
            else if (com_cursos.getValue() == null){
                utilitario.agregarMensajeError("No se puede Continuar", "Debe seleccionar el Curso");    
                    }
                    else if (com_paralelos.getValue() == null){
                utilitario.agregarMensajeError("No se puede Continuar", "Debe seleccionar el Paralelo");    
                    }
                    else if (com_especialidad.getValue() == null){
                utilitario.agregarMensajeError("No se puede Continuar", "Debe seleccionar la especialidad");    
                    }
        
        else{

        sel_tab_alumno.dibujar();
        }
    }
    public void aceptarRepresentante(){
        String str_repre = sel_tab_representante.getValorSeleccionado();     
        System.out.println("repre "+str_repre);
            tab_tabla1.setValor("gen_ide_geper",str_repre);        
             sel_tab_representante.cerrar();
             tab_tabla1.modificar(tab_tabla1.getFilaActual());
             tab_tabla1.guardar();
             guardarPantalla();
	     utilitario.addUpdate("tab_tabla1");
    }
    public void aceptarAlumno(){
        String str_alum = sel_tab_alumno.getSeleccionados();
        TablaGenerica tab_dat_alum = utilitario.consultar("select ide_geper, identificac_geper, nom_geper  from gen_persona  where ide_vgtcl = 1 and ide_geper = "+str_alum+"order by nom_geper");
        for (int i=0;i<tab_dat_alum.getTotalFilas();i++){
            if(tab_tabla1.isFilaInsertada()==false){
                tab_tabla1.insertar();
            }
            tab_tabla1.setValor("ide_geper",tab_dat_alum.getValor(i,"ide_geper"));
        }
             tab_tabla1.setValor("ide_repea", com_periodo_academico.getValue().toString());
             tab_tabla1.setValor("ide_recur", com_cursos.getValue().toString());
             tab_tabla1.setValor("ide_repar", com_paralelos.getValue().toString());
             tab_tabla1.setValor("ide_reces", com_especialidad.getValue().toString());
             utilitario.addUpdateTabla(tab_tabla1, "ide_repea", "");
             utilitario.addUpdateTabla(tab_tabla1, "ide_recur", "");
             utilitario.addUpdateTabla(tab_tabla1, "ide_repar", "");
             utilitario.addUpdateTabla(tab_tabla1, "ide_reces", "");
             sel_tab_alumno.cerrar();
	     utilitario.addUpdate("tab_tabla1");
    }
    @Override
    public void insertar() {
        if (com_periodo_academico.getValue() == null){
            utilitario.agregarMensajeError("No se puede Continuar", "Debe seleccionar el Periodo Académico");       
        }
            else if (com_cursos.getValue() == null){
                utilitario.agregarMensajeError("No se puede Continuar", "Debe seleccionar el Curso");    
                    }
                    else if (com_paralelos.getValue() == null){
                utilitario.agregarMensajeError("No se puede Continuar", "Debe seleccionar el Paralelo");    
                    }
                    else if (com_especialidad.getValue() == null){
                utilitario.agregarMensajeError("No se puede Continuar", "Debe seleccionar la especialidad");    
                    }
        
        else{
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


    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pensiones;

/**
 *
 * @author ANDRES REDROBAN
 */

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import servicios.pensiones.ServicioPensiones;

import sistema.aplicacion.Pantalla;

public class pre_alumno_periodo extends Pantalla{
    private Tabla tab_tabla1 = new Tabla();
    private Combo com_periodo_academico = new Combo();
    private Combo com_cursos = new Combo();
    private Combo com_paralelos = new Combo();
    private Combo com_especialidad = new Combo();
    private SeleccionTabla sel_tab_alumno = new SeleccionTabla();
    
    @EJB
    private ServicioPensiones ser_pensiones = (ServicioPensiones) utilitario.instanciarEJB(ServicioPensiones.class);
    
    public pre_alumno_periodo(){
        tab_tabla1.setId("tab_tabla1");   //identificador
        tab_tabla1.setTabla("REC_ALUMNO_PERIODO", "IDE_RECALP", 1);
        tab_tabla1.getColumna("ide_repea").setVisible(false);
        tab_tabla1.getColumna("ide_repar").setVisible(false);
        tab_tabla1.getColumna("ide_recur").setVisible(false);
        tab_tabla1.getColumna("ide_reces").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setFiltro(true);
        tab_tabla1.getColumna("ide_geper").setCombo(ser_pensiones.getListaAlumnos());
        tab_tabla1.getColumna("ide_geper").setAutoCompletar();
        tab_tabla1.setCondicion("IDE_RECALP = -1");
        tab_tabla1.dibujar();
        PanelTabla pat_tabla1 = new PanelTabla();
        pat_tabla1.setId("pat_tabla1");
        pat_tabla1.setPanelTabla(tab_tabla1);
        Division div_tabla1 = new Division();
        div_tabla1.setId("div_tabla1");
        div_tabla1.dividir1(pat_tabla1);
        agregarComponente(div_tabla1);
        
        com_periodo_academico.setId("cmb_periodo_academico");
        com_periodo_academico.setCombo(ser_pensiones.getPeriodoAcademico("true"));
        com_periodo_academico.setMetodo("filtroComboPeriodoAcademnico");
        
        bar_botones.agregarComponente(new Etiqueta("Periodo Academico: "));
        bar_botones.agregarComponente(com_periodo_academico);
        
        com_cursos.setId("com_cursos");
        com_cursos.setCombo(ser_pensiones.getCursos("true,false"));
        com_cursos.setMetodo("filtroComboCursos");
        
        bar_botones.agregarComponente(new Etiqueta("Curso: "));
        bar_botones.agregarComponente(com_cursos);
        
        com_paralelos.setId("com_paralelos");
        com_paralelos.setCombo(ser_pensiones.getParalelos("true,false"));
        com_paralelos.setMetodo("filtroComboParalelos");
        
        bar_botones.agregarComponente(new Etiqueta("Paralelo: "));
        bar_botones.agregarComponente(com_paralelos);
        
        com_especialidad.setId("com_especialidad");
        com_especialidad.setCombo(ser_pensiones.getEspecialidad("true"));
        com_especialidad.setMetodo("filtroComboEspecialidad");
        
        bar_botones.agregarComponente(new Etiqueta("Especialidad: "));
        bar_botones.agregarComponente(com_especialidad);
        
        Boton bot_filtro_alumno = new Boton();
        bot_filtro_alumno.setIcon("ui-icon-search");
        bot_filtro_alumno.setValue("Filtrar");
        bot_filtro_alumno.setMetodo("filtroAlumno");
        agregarComponente(bot_filtro_alumno);
   //     bar_botones.agregarBoton(bot_filtro_alumno);
        
        Boton bot_imp_alumno = new Boton();
        bot_imp_alumno.setIcon("ui-icon-search");
        bot_imp_alumno.setValue("Importar Alumno");
        bot_imp_alumno.setMetodo("abrirDialogoAlumno");
        agregarComponente(bot_imp_alumno);
        bar_botones.agregarBoton(bot_imp_alumno);
        
        Boton bot_bus_alumno = new Boton();
        bot_bus_alumno.setIcon("ui-icon-search");
        bot_bus_alumno.setValue("Filtrar Alumno");
        bot_bus_alumno.setMetodo("filtroAlumno");
        agregarComponente(bot_bus_alumno);
    //    bar_botones.agregarBoton(bot_bus_alumno);
        
        
        sel_tab_alumno.setId("sel_tab_alumno");
        sel_tab_alumno.setTitle("ALUMNOS");
        sel_tab_alumno.setSeleccionTabla(ser_pensiones.getListaAlumnos(), "ide_geper");
        sel_tab_alumno.setWidth("80%");
        sel_tab_alumno.setHeight("70%");
        sel_tab_alumno.getTab_seleccion().getColumna("identificac_geper").setFiltroContenido();
        sel_tab_alumno.getTab_seleccion().getColumna("nom_geper").setFiltroContenido();
        sel_tab_alumno.getBot_aceptar().setMetodo("aceptarAlumno");
        agregarComponente(sel_tab_alumno);
        
    }
    public void filtroAlumno(){
        String cm_per_aca = com_periodo_academico.getValue().toString();
        String cm_cur = com_cursos.getValue().toString();
        String cm_par = com_paralelos.getValue().toString();
        String cm_esp = com_especialidad.getValue().toString();
        
        if (com_periodo_academico.getValue().toString() != null){
            tab_tabla1.setCondicion("ide_repea="+com_periodo_academico.getValue().toString());
            tab_tabla1.ejecutarSql();
            utilitario.addUpdate("tab_tabla1");
        }
        else if (com_cursos.getValue().toString() != null){
            tab_tabla1.setCondicion("ide_recur="+com_cursos.getValue().toString());
            tab_tabla1.ejecutarSql();
            utilitario.addUpdate("tab_tabla1");
        }
        else if (com_paralelos.getValue().toString() != null){
            tab_tabla1.setCondicion("ide_repar="+com_paralelos.getValue().toString());
            tab_tabla1.ejecutarSql();
            utilitario.addUpdate("tab_tabla1");
        }
        else if (com_especialidad.getValue().toString() != null){
            tab_tabla1.setCondicion("ide_reces="+com_especialidad.getValue().toString());
           tab_tabla1.ejecutarSql();
           utilitario.addUpdate("tab_tabla1");
    }
        else {
            tab_tabla1.limpiar();
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


    
}

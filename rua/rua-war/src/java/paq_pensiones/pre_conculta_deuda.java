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

import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import servicios.pensiones.ServicioPensiones;
import sistema.aplicacion.Pantalla;

public class pre_conculta_deuda extends Pantalla{
    private Tabla tab_tabla1 = new Tabla();
    private Combo com_estados = new Combo();
    private Calendario fechaInicio = new Calendario();
    private Calendario fechaFin = new Calendario();
    private Dialogo dia_emision = new Dialogo();
    @EJB
    private ServicioPensiones ser_pensiones = (ServicioPensiones) utilitario.instanciarEJB(ServicioPensiones.class);
    
    public pre_conculta_deuda(){
        
        com_estados.setId("com_estados");
        com_estados.setCombo("select * from rec_estados");
        //com_estados.setMetodo("actualizar deuda");
        //bar_botones.agregarComponente(com_estados);
        
        Boton bot_convenio = new Boton();
        bot_convenio.setValue("Crear Convenio");
        bot_convenio.setMetodo("abrirDialogoConcepto");
        bar_botones.agregarBoton(bot_convenio);
               
        
         // creo dialogo para crear modalidad
        dia_emision.setId("dia_emision");
        dia_emision.setTitle("Seleccion los parámetros");
        dia_emision.setWidth("25%");
        dia_emision.setHeight("30%");
        dia_emision.getBot_aceptar().setMetodo("generarConvenio");
        dia_emision.setResizable(false);
        
        Grid gru_cuerpo = new Grid();
        gru_cuerpo.setColumns(2);
        Etiqueta eti_mensaje = new Etiqueta();
      
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
        

        
        
        tab_tabla1.setId("tab_tabla1");   //identificador
        tab_tabla1.setSql(ser_pensiones.getAlumnosDeudaConsultaTotal("1"));
        tab_tabla1.setCampoPrimaria("ide_titulo_recval");
        tab_tabla1.getColumna("alumno").setFiltro(true);
        tab_tabla1.setLectura(true);
        tab_tabla1.setTipoSeleccion(true);
        tab_tabla1.dibujar();
        tab_tabla1.setRows(20);
        PanelTabla pat_tabla1 = new PanelTabla();
        pat_tabla1.setId("pat_tabla1");
        pat_tabla1.setPanelTabla(tab_tabla1);
        Division div_tabla1 = new Division();
        div_tabla1.setId("div_tabla1");
        div_tabla1.dividir1(pat_tabla1);
        agregarComponente(div_tabla1);
    }
    public void generarConvenio(){
        String titulos_seleccionados=tab_tabla1.getFilasSeleccionadas();
        utilitario.getConexion().ejecutarSql("update rec_valores set aplica_convenio_recva=true, fecha_iniconve_recva='"+fechaInicio.getFecha()+"', fecha_finconve_recva='"+fechaFin.getFecha()+"' where ide_titulo_recval in ("+titulos_seleccionados+")");
        dia_emision.cerrar();
        utilitario.agregarMensaje("Se ha realizado con exito", "Registrado convenio con exito");
        utilitario.addUpdate("tab_tabla1");
    }
    public void abrirDialogoConcepto(){
        if (tab_tabla1.getValorSeleccionado() != null){
        dia_emision.dibujar();
        }
        else {
            utilitario.agregarMensajeError("No se puede continuar", "Debe filtrar los alumnos a los que desea generar el convenio");
        }
    }
    
    @Override
    public void insertar() {
        tab_tabla1.insertar();
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

    public Dialogo getDia_emision() {
        return dia_emision;
    }

    public void setDia_emision(Dialogo dia_emision) {
        this.dia_emision = dia_emision;
    }


    
}

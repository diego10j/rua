/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gerencial;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.List;
import javax.ejb.EJB;
import paq_gerencial.ejb.ServicioGerencial;
import persistencia.Conexion;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author LUIS TOAPANTA
 */
public class ActivarPeriodoFiscal extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    private Combo com_anio = new Combo();
    private SeleccionTabla sel_casa_obra = new SeleccionTabla();
    private SeleccionTabla sel_casa_obra_cierre = new SeleccionTabla();
    private Dialogo dia_dialogo = new Dialogo();
    private Dialogo dia_dialogo_cierre = new Dialogo();
    private Calendario cal_fecha_emision = new Calendario();
    private Calendario cal_fecha_cierre = new Calendario();
    private Texto txt_resolucion = new Texto();
    String str_selecccionados = "";
    String str_fecha="";
    String str_observacion="";
    String str_fecha_cierre="";
    @EJB
    private final ServicioGerencial ser_gerencial = (ServicioGerencial) utilitario.instanciarEJB(ServicioGerencial.class);

    public ActivarPeriodoFiscal() {

        com_anio.setCombo(ser_gerencial.getAnio("true,false"));
        com_anio.setMetodo("seleccionaElAnio");
        bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
        bar_botones.agregarComponente(com_anio);

        Boton bot_agregar = new Boton();
        bot_agregar.setValue("Apertura Periodo Fiscal");
        bot_agregar.setMetodo("aceptarObra");
        bar_botones.agregarBoton(bot_agregar);

        Boton bot_cierre = new Boton();
        bot_cierre.setValue("Cierre Periodo Fiscal");
        bot_cierre.setMetodo("cerrarObra");
        bar_botones.agregarBoton(bot_cierre);
        
        //Permite crear la tabla1 
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setHeader("CASAS SALESIANAS REGISTRADAS EN EL PERIODO FISCAL");
        tab_tabla1.setTabla("ger_cont_balance_cabecera", "ide_gecobc", 1);
        tab_tabla1.setCondicion("ide_geani=-1");
        tab_tabla1.getColumna("ide_gerest").setCombo(ser_gerencial.getEstado());
        tab_tabla1.getColumna("ide_geani").setVisible(false);
        tab_tabla1.getColumna("ide_gerobr").setCombo(ser_gerencial.getObra());
        tab_tabla1.getColumna("responsable_gecobc").setEtiqueta();
        tab_tabla1.getColumna("responsable_gecobc").setNombreVisual("RESPONSABLE");
        tab_tabla1.getColumna("fecha_apert_gecobc").setNombreVisual("FECHA APERTURA");
        tab_tabla1.getColumna("fecha_cierre_gecobc").setNombreVisual("FECHA CIERRE");
        tab_tabla1.getColumna("observacion_gecobc").setNombreVisual("OBSERVACION");
        tab_tabla1.dibujar();

        //Es el contenedor de la tabla
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setId("pat_panel1");
        pat_panel1.setPanelTabla(tab_tabla1);

        //Permite la dision de la pantalla
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel1);
        agregarComponente(div_division);

        sel_casa_obra.setId("sel_casa_obra");
        sel_casa_obra.getTab_seleccion();
        sel_casa_obra.setTitle("SELECCIONE UNA OBRA SALESIANA");
        sel_casa_obra.setSeleccionTabla(ser_gerencial.getCasaObra("2", ""), "ide_gerobr");
        sel_casa_obra.getTab_seleccion().getColumna("ide_gercas").setVisible(false);
        sel_casa_obra.getTab_seleccion().getColumna("nombre_gercas").setNombreVisual("Casa");
        sel_casa_obra.getTab_seleccion().getColumna("nombre_gerobr").setNombreVisual("Obra");
        sel_casa_obra.getBot_aceptar().setMetodo("agregarObra");
        agregarComponente(sel_casa_obra);

        //Dialogo
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setTitle("REGISTRO DE INFORMACION PARA APERTURA DE PERIODO FISCAL CONTABLE");
        dia_dialogo.setWidth("40%");
        dia_dialogo.setHeight("30%");
        dia_dialogo.setResizable(false);

        Grid gri_cuerpo = new Grid();
        gri_cuerpo.setColumns(2);
        gri_cuerpo.setWidth("100%");
        gri_cuerpo.setStyle("width:100%;overflow: auto;display: block;");
        gri_cuerpo.getChildren().clear();
        gri_cuerpo.getChildren().add(new Etiqueta("FECHA DE APERTURA"));
        cal_fecha_emision.limpiar();
        gri_cuerpo.getChildren().add(cal_fecha_emision);
        gri_cuerpo.getChildren().add(new Etiqueta("OBSERVACION"));
        txt_resolucion.setId("txt_resolucion");
        txt_resolucion.setSize(50);
        txt_resolucion.limpiar();
        txt_resolucion.setMaxlength(50);
        gri_cuerpo.getChildren().add(txt_resolucion);
        dia_dialogo.getBot_aceptar().setMetodo("agregarObra");
        dia_dialogo.setDialogo(gri_cuerpo);
        agregarComponente(dia_dialogo);
        
        // CIERRE DE PERIODO FISCAL
        sel_casa_obra_cierre.setId("sel_casa_obra_cierre");
        sel_casa_obra_cierre.getTab_seleccion();
        sel_casa_obra_cierre.setTitle("SELECCIONE UNA OBRA SALESIANA");
        sel_casa_obra_cierre.setSeleccionTabla(ser_gerencial.getCasaObraPeriodoFiscal("-1", "-1","-1","-1"), "ide_gecobc");
        sel_casa_obra_cierre.getTab_seleccion().getColumna("nombre_gercas").setNombreVisual("Casa");
        sel_casa_obra_cierre.getTab_seleccion().getColumna("nombre_gerobr").setNombreVisual("Obra");
        sel_casa_obra_cierre.getTab_seleccion().getColumna("nombre_gerobr").setFiltro(true);
        sel_casa_obra_cierre.getBot_aceptar().setMetodo("agregarCerrarObra");
        agregarComponente(sel_casa_obra_cierre);

        //Dialogo CIERRE
        dia_dialogo_cierre.setId("dia_dialogo_cierre");
        dia_dialogo_cierre.setTitle("REGISTRO DE INFORMACION PARA EL CIERRE DE PERIODO FISCAL CONTABLE");
        dia_dialogo_cierre.setWidth("40%");
        dia_dialogo_cierre.setHeight("30%");
        dia_dialogo_cierre.setResizable(false);
        Grid gri_cuerpo2 = new Grid();
        gri_cuerpo2.setColumns(2);
        gri_cuerpo2.setWidth("100%");
        gri_cuerpo2.setStyle("width:100%;overflow: auto;display: block;");
        gri_cuerpo2.getChildren().clear();
        gri_cuerpo2.getChildren().add(new Etiqueta("FECHA DE CIERRE"));
        cal_fecha_cierre.limpiar();
        gri_cuerpo2.getChildren().add(cal_fecha_cierre);        
        dia_dialogo_cierre.getBot_aceptar().setMetodo("agregarCerrarObra");
        dia_dialogo_cierre.setDialogo(gri_cuerpo2);
        agregarComponente(dia_dialogo_cierre);
    }

    public void aceptarObra() {
        //si no selecciono ningun valor en el combo
        if (com_anio.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
            return;
        } else {
            dia_dialogo.dibujar();
        }
    }
    public void cerrarObra() {
        //si no selecciono ningun valor en el combo
        if (com_anio.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
            return;
        } else {
            dia_dialogo_cierre.dibujar();
        }
    }
    public void agregarObra() {

        if (cal_fecha_emision.getValue() == null || cal_fecha_emision.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("ADVERTENCIA,", "Ingrese la Fecha de Apertura");
            return;
        } else if (txt_resolucion.getValue() == null || txt_resolucion.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("ADVERTENCIA,", "Ingrese alguna Observación");
            return;
        } else {
            if (dia_dialogo.isVisible()) {
                str_fecha=cal_fecha_emision.getFecha();
                str_observacion=txt_resolucion.getValue().toString();
                dia_dialogo.cerrar();
                
                sel_casa_obra.dibujar();
            } else if (sel_casa_obra.isVisible()) {
                sel_casa_obra.cerrar();
                str_selecccionados = sel_casa_obra.getSeleccionados();
                TablaGenerica tab_obra = new TablaGenerica();
                List list_obras = tab_obra.getConexion().consultar(ser_gerencial.getCasaObra("1", str_selecccionados));
                if (list_obras.size() > 0) {
                    for (Object li : list_obras) {
                        Object[] fila = (Object[]) li;
                        tab_tabla1.insertar();
                        tab_tabla1.setValor("ide_gerobr", String.valueOf(fila[0]));
                        tab_tabla1.setValor("responsable_gecobc", utilitario.getVariable("NICK"));
                        tab_tabla1.setValor("ide_geani", com_anio.getValue() + "");
                        tab_tabla1.setValor("ide_gerest", utilitario.getVariable("p_ger_estado_activo"));
                        tab_tabla1.setValor("fecha_apert_gecobc", str_fecha);
                        tab_tabla1.setValor("observacion_gecobc",str_observacion);
                    }
                }
            }
        }
    }
public void agregarCerrarObra() {

        if (cal_fecha_cierre.getValue() == null || cal_fecha_cierre.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("ADVERTENCIA,", "Ingrese la Fecha de Cierre");
            return;
        }  else {
            if (dia_dialogo_cierre.isVisible()) {
                str_fecha_cierre=cal_fecha_cierre.getFecha();
                dia_dialogo_cierre.cerrar();
                sel_casa_obra_cierre.getTab_seleccion().setSql(ser_gerencial.getCasaObraPeriodoFiscal(utilitario.getVariable("p_ger_estado_activo"), com_anio.getValue().toString(),"1","1"));
		sel_casa_obra_cierre.getTab_seleccion().ejecutarSql();
		sel_casa_obra_cierre.dibujar();
            } else if (sel_casa_obra_cierre.isVisible()) {
                sel_casa_obra_cierre.cerrar();
                str_selecccionados = sel_casa_obra_cierre.getSeleccionados();
                TablaGenerica tab_obra = new TablaGenerica(); 
                List list_obras = tab_obra.getConexion().consultar(ser_gerencial.getCasaObraPeriodoFiscal("-1","-1","2", str_selecccionados));
                if (list_obras.size() > 0) {
                    for (Object li : list_obras) {
                        Object[] fila = (Object[]) li;
                        utilitario.getConexion().setUnidad_persistencia("rua_gerencial");
                        utilitario.getConexion().ejecutarSql("update ger_cont_balance_cabecera set fecha_cierre_gecobc='"+str_fecha_cierre+"' where ide_gecobc="+String.valueOf(fila[0]));
                    }
                    tab_tabla1.ejecutarSql();
                    utilitario.addUpdate("tab_tabla1");
                }
            }
        }
    }
    public void seleccionaElAnio() {
        if (com_anio.getValue() != null) {
            tab_tabla1.setCondicion(" ide_geani=" + com_anio.getValue());
            tab_tabla1.ejecutarSql();

        } else {
            utilitario.agregarMensajeInfo("Selecione un Año", "");

        }
    }

    @Override
    public void insertar() {
        if (tab_tabla1.isFocus()) {
            tab_tabla1.insertar();
                }
    }

    @Override
    public void guardar() {
        if (tab_tabla1.guardar()) {
            if (tab_tabla2.guardar()) {
                if (tab_tabla3.guardar()) {
                    guardarPantalla();
                    //guardarPantalla();
                }
            }
        }
    }

    @Override
    public void eliminar() {
        if (tab_tabla1.isFocus()) {
            tab_tabla1.eliminar();
        } else if (tab_tabla2.isFocus()) {
            tab_tabla2.eliminar();
        } else if (tab_tabla3.isFocus()) {
            tab_tabla3.eliminar();
        }
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

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }

    public SeleccionTabla getSel_casa_obra() {
        return sel_casa_obra;
    }

    public void setSel_casa_obra(SeleccionTabla sel_casa_obra) {
        this.sel_casa_obra = sel_casa_obra;
    }

    public Dialogo getDia_dialogo() {
        return dia_dialogo;
    }

    public void setDia_dialogo(Dialogo dia_dialogo) {
        this.dia_dialogo = dia_dialogo;
    }

    public Calendario getCal_fecha_emision() {
        return cal_fecha_emision;
    }

    public void setCal_fecha_emision(Calendario cal_fecha_emision) {
        this.cal_fecha_emision = cal_fecha_emision;
    }

    public SeleccionTabla getSel_casa_obra_cierre() {
        return sel_casa_obra_cierre;
    }

    public void setSel_casa_obra_cierre(SeleccionTabla sel_casa_obra_cierre) {
        this.sel_casa_obra_cierre = sel_casa_obra_cierre;
    }

    public Dialogo getDia_dialogo_cierre() {
        return dia_dialogo_cierre;
    }

    public void setDia_dialogo_cierre(Dialogo dia_dialogo_cierre) {
        this.dia_dialogo_cierre = dia_dialogo_cierre;
    }

}

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

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import paq_contabilidad.ejb.ServicioContabilidad;
import servicios.pensiones.ServicioPensiones;
import sistema.aplicacion.Pantalla;

public class pre_alumno_periodo_edita extends Pantalla{
    private Tabla tab_tabla1 = new Tabla();
    @EJB
    private ServicioPensiones ser_pensiones = (ServicioPensiones) utilitario.instanciarEJB(ServicioPensiones.class);
    @EJB
    private ServiciosAdquisiones ser_adqusiiones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    @EJB
    private ServicioContabilidad ser_contabilidad = (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);
    
    
    public pre_alumno_periodo_edita(){

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
        //tab_tabla1.getColumna("retirado_recalp").setLectura(true);
        //tab_tabla1.getColumna("retirado_recalp").setLectura(true);
        tab_tabla1.getColumna("retirado_recalp").setValorDefecto("FALSE");
        tab_tabla1.getColumna("descuento_recalp").setValorDefecto("false");
        tab_tabla1.getColumna("activo_recalp").setValorDefecto("true");
        tab_tabla1.getColumna("activo_recalp").setLectura(true);
        //tab_tabla1.getColumna("detalle_retiro_recalp").setLectura(true);
        //tab_tabla1.getColumna("fecha_retiro_recalp").setLectura(true);
        tab_tabla1.getColumna("valor_descuento_recalp").setValorDefecto("0");
        tab_tabla1.getColumna("ide_geper").setFiltroContenido();
        tab_tabla1.getColumna("ide_geper").setCombo(ser_pensiones.getListaAlumnos("2", ""));
        tab_tabla1.getColumna("gen_ide_geper").setCombo(ser_pensiones.getListaAlumnos("2", ""));
        tab_tabla1.getColumna("ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("gen_ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("ide_repar").setAncho(20);
        tab_tabla1.getColumna("ide_geper").setEstilo("width:20 px");
        tab_tabla1.dibujar();
        PanelTabla pat_tabla1 = new PanelTabla();
        pat_tabla1.setId("pat_tabla1");
        pat_tabla1.setPanelTabla(tab_tabla1);
        Division div_tabla1 = new Division();
        div_tabla1.setId("div_tabla1");
        div_tabla1.dividir1(pat_tabla1);
        agregarComponente(div_tabla1);
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


    
}

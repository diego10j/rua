/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pensiones;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import servicios.pensiones.ServicioPensiones;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author djacome
 */
public class pre_alumnos extends Pantalla {
    
    @EJB
    private final ServicioPensiones ser_pensiones = (ServicioPensiones) utilitario.instanciarEJB(ServicioPensiones.class);
    
    private Tabla tab_tabla = new Tabla();
    private Tabla tab_tabla1 = new Tabla();
    
    public pre_alumnos() {
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTipoFormulario(true);
        tab_tabla.setTabla("gen_persona", "ide_geper", 1);
        //OCULTA TODAS LAS COLUMNAS
        for (int i = 0; i < tab_tabla.getTotalColumnas(); i++) {
            tab_tabla.getColumnas()[i].setVisible(false);
            tab_tabla.getColumnas()[i].setLectura(true);
        }
        
        tab_tabla.getColumna("codigo_geper").setVisible(true);
        tab_tabla.getColumna("codigo_geper").setRequerida(true);
        tab_tabla.getColumna("identificac_geper").setVisible(true);
        tab_tabla.getColumna("identificac_geper").setRequerida(true);
        tab_tabla.getColumna("nom_geper").setVisible(true);
        tab_tabla.getColumna("nom_geper").setRequerida(true);
        tab_tabla.getColumna("rep_ide_geper").setVisible(true);
        tab_tabla.getColumna("rep_ide_geper").setRequerida(true);
        tab_tabla.getColumna("direccion_geper").setVisible(true);
        tab_tabla.getColumna("ide_getid").setVisible(true);
        tab_tabla.getColumna("telefono_geper").setVisible(true);
        tab_tabla.getColumna("correo_geper").setVisible(true);
        tab_tabla.getColumna("ide_vgtcl").setVisible(true);
        tab_tabla.getColumna("ide_vgecl").setVisible(true);
        tab_tabla.getColumna("ide_vgecl").setValorDefecto("0"); // ACTIVO
        tab_tabla.getColumna("gen_ide_geper").setValorDefecto("3");//3 = ALUMNOS  
        tab_tabla.getColumna("ide_cntco").setValorDefecto("2");// PERSONA NATURAL
        tab_tabla.getColumna("ide_vgtcl").setValorDefecto("1"); // ALUMNOS   
        tab_tabla.setValor("fecha_ingre_geper", utilitario.getFechaActual());
        tab_tabla.getColumna("ide_getid").setCombo("gen_tipo_identifi", "ide_getid", "nombre_getid", "ide_getid=0");
        tab_tabla.getColumna("ide_getid").setPermitirNullCombo(false);
        tab_tabla.getColumna("repre_legal_geper").setLectura(true);
        tab_tabla.getColumna("nivel_geper").setValorDefecto("HIJO");
        tab_tabla.getColumna("es_cliente_geper").setValorDefecto("true");
        tab_tabla.getColumna("identificac_geper").setUnico(true);
        tab_tabla.getColumna("ide_vgtcl").setCombo("ven_tipo_cliente", "ide_vgtcl", "nombre_vgtcl", "ide_vgtcl=1");
        tab_tabla.getColumna("ide_vgecl").setCombo("ven_estado_client", "ide_vgecl", "nombre_vgecl", "");
        tab_tabla.getColumna("rep_ide_geper").setCombo("select ide_geper,nom_geper from gen_persona where nivel_geper ='HIJO' and ide_vgtcl=0 and es_cliente_geper=true order by nom_geper");
        tab_tabla.getColumna("rep_ide_geper").setAutoCompletar();
        tab_tabla.getColumna("ide_vgecl").setPermitirNullCombo(false);
        
        tab_tabla.setMostrarNumeroRegistros(false);
        tab_tabla.getGrid().setColumns(6);
        tab_tabla.setValidarInsertar(true);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setLectura(true);
        tab_tabla1.setSql(ser_pensiones.getSqlAlumnos());
        tab_tabla1.setNumeroTabla(2);
        tab_tabla1.setHeader("LISTADO DE ALUMNOS");
        tab_tabla1.setCampoPrimaria("ide_geper");
        tab_tabla1.getColumna("CODIGO").setFiltroContenido();
        tab_tabla1.getColumna("CEDULA").setFiltroContenido();
        tab_tabla1.getColumna("APELLIDOS_Y_NOMBRES").setFiltroContenido();
        tab_tabla1.getColumna("REPRESENTANTE").setFiltroContenido();
        tab_tabla1.onSelect("seleccionaAlumno");
        tab_tabla1.dibujar();
        
        seleccionaAlumno();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel, pat_panel1, "60%", "H");
        agregarComponente(div_division);
        
    }
    
    public void seleccionaAlumno(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        seleccionaAlumno();
    }
    
    private void seleccionaAlumno() {
        tab_tabla.setCondicion("ide_geper=" + tab_tabla1.getValorSeleccionado());
        tab_tabla.ejecutarSql();
    }
    
    @Override
    public void insertar() {
        tab_tabla.insertar();
    }
    
    @Override
    public void inicio() {
        tab_tabla1.inicio();
        seleccionaAlumno();
    }
    
    @Override
    public void siguiente() {
        tab_tabla1.siguiente();
        seleccionaAlumno();
    }
    
    @Override
    public void atras() {
        tab_tabla1.atras();
        seleccionaAlumno();
    }
    
    @Override
    public void fin() {
        tab_tabla1.fin();
        seleccionaAlumno();
    }
    
    @Override
    public void guardar() {
        tab_tabla.setValor("nombre_compl_geper", tab_tabla.getValor("nom_geper"));
        if (tab_tabla.guardar()) {
            guardarPantalla();
            tab_tabla1.actualizar();
            tab_tabla1.setFilaActual(tab_tabla.getValorSeleccionado());
        }
    }
    
    @Override
    public void eliminar() {
        if (tab_tabla.eliminar()) {
            tab_tabla1.getFilas().remove(tab_tabla1.getFilaActual());
        }
    }
    
    public Tabla getTab_tabla() {
        return tab_tabla;
    }
    
    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
    
    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }
    
    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }
}

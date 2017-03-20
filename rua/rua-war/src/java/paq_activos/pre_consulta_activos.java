/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_activos;

import framework.aplicacion.Columna;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import servicios.activos.ServicioActivosFijos;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author djacome
 */
public class pre_consulta_activos extends Pantalla {
    
    @EJB
    private final ServicioActivosFijos ser_activos = (ServicioActivosFijos) utilitario.instanciarEJB(ServicioActivosFijos.class);
    
    private Tabla tab_consulta = new Tabla();
    
    public pre_consulta_activos() {
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();
        Boton bot_exportar = new Boton();
        bot_exportar.setValue("Exportar a Excel");
        bot_exportar.setMetodo("exportar");
        bot_exportar.setAjax(false);
        bot_exportar.setIcon("ui-icon-transferthick-e-w");
        bar_botones.agregarBoton(bot_exportar);
        
        tab_consulta.setSql(ser_activos.getSqlConsultaActivos());
        tab_consulta.setNumeroTabla(1);
        //filtros en todos los campos tipo String
        for (Columna colActual : tab_consulta.getColumnas()) {
            if (colActual.getTipoJava().equals("java.lang.String")) {
                colActual.setFiltro(true);
            }
        }
        tab_consulta.setId("tab_consulta");
        tab_consulta.getColumna("ide_acact").setVisible(false);
        tab_consulta.getColumna("act_ide_acafi").setVisible(false);        
        tab_consulta.setLectura(true);
        tab_consulta.setRows(20);
        tab_consulta.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_consulta);
        
        Division div_division = new Division();
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }
    
    public void exportar() {
        tab_consulta.exportarXLS();
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
    
    public Tabla getTab_consulta() {
        return tab_consulta;
    }
    
    public void setTab_consulta(Tabla tab_consulta) {
        this.tab_consulta = tab_consulta;
    }
    
}

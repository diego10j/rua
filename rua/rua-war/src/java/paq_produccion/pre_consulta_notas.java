/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_produccion;

import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Mauricio
 */
public class pre_consulta_notas extends Pantalla {

    private Tabla tab_consulta = new Tabla();
    private Calendario cal_fecha_inicio = new Calendario();
    private Calendario cal_fecha_final = new Calendario();
     private Combo com_tipo_trans = new Combo();

    public pre_consulta_notas() {
         
        
        bar_botones.agregarComponente(new Etiqueta("FECHA DESDE :"));
        cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
        bar_botones.agregarComponente(cal_fecha_inicio);

        bar_botones.agregarComponente(new Etiqueta("FECHA HASTA :"));
        cal_fecha_final.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_final);
        
        com_tipo_trans.setId("com_tipo_trans");
        com_tipo_trans.setCombo("select ide_intti,nombre_intti from inv_tip_tran_inve");
        bar_botones.agregarComponente(new Etiqueta("TIPO TRANSACCIÓN"));
        bar_botones.agregarComponente(com_tipo_trans);
        
        Boton bot_filtro_consulta = new Boton();
        bot_filtro_consulta.setIcon("ui-icon-search");
        bot_filtro_consulta.setValue("CONSULTAR");
        bot_filtro_consulta.setMetodo("consultar");
        bar_botones.agregarBoton(bot_filtro_consulta);

        tab_consulta.setId("tab_consulta");
        tab_consulta.setTabla("inv_cab_comp_inve", "ide_incci", 1);
        tab_consulta.setCondicion("ide_incci = -1");
        tab_consulta.getColumna("ide_incci").setNombreVisual("CODIGO");
        tab_consulta.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper", "");
        tab_consulta.getColumna("ide_geper").setFiltroContenido();
        tab_consulta.getColumna("ide_geper").setLongitud(100);
        tab_consulta.getColumna("ide_geper").setAutoCompletar();
        tab_consulta.getColumna("ide_usua").setVisible(false);
        tab_consulta.getColumna("ide_intti").setNombreVisual("TIPO TRANSACCIÓN");
        tab_consulta.getColumna("ide_intti").setCombo("inv_tip_tran_inve", "ide_intti", "nombre_intti", "");
        tab_consulta.getColumna("ide_intti").setLongitud(40);
        tab_consulta.getColumna("ide_geper").setLectura(true);
        tab_consulta.getColumna("ide_intti").setLectura(true);
        tab_consulta.getColumna("numero_incci").setNombreVisual("SECUENCIAL");
        tab_consulta.getColumna("numero_incci").setFiltroContenido();
        tab_consulta.getColumna("observacion_incci").setNombreVisual("OBSERVACION");
        tab_consulta.getColumna("observacion_incci").setFiltroContenido();
        tab_consulta.getColumna("fecha_trans_incci").setFiltroContenido();
        tab_consulta.getColumna("codigo_documento_incci").setNombreVisual("No FACTURA");
        tab_consulta.getColumna("codigo_documento_incci").setFiltroContenido();
        tab_consulta.getColumna("ide_cnccc").setVisible(false);
        tab_consulta.getColumna("sis_ide_usua").setVisible(false);
        tab_consulta.getColumna("ide_inbod").setVisible(false);
        tab_consulta.getColumna("fec_cam_est_incci").setVisible(false);
        tab_consulta.getColumna("fecha_efect_incci").setVisible(false);
        tab_consulta.getColumna("fecha_siste_incci").setVisible(false);
        tab_consulta.getColumna("hora_sistem_incci").setVisible(false);
        tab_consulta.getColumna("ide_georg").setVisible(false);
        tab_consulta.getColumna("gth_ide_gtemp").setVisible(false);
        tab_consulta.getColumna("referencia_incci").setVisible(false);
        tab_consulta.getColumna("gth_ide_gtemp2").setVisible(false);
        tab_consulta.getColumna("ide_gtemp").setVisible(false);
        tab_consulta.getColumna("gth_ide_gtemp3").setVisible(false);
        tab_consulta.getColumna("maquina_incci").setVisible(false);
        tab_consulta.getColumna("ide_inepi").setVisible(false);
        tab_consulta.setLectura(true);
        tab_consulta.setRows(20);
        tab_consulta.dibujar();
        PanelTabla pat_consulta = new PanelTabla();
        pat_consulta.setId("pat_consulta");
        pat_consulta.setPanelTabla(tab_consulta);

        Division div_consulta = new Division();
        div_consulta.setId("div_consulta");
        div_consulta.dividir1(pat_consulta);
        agregarComponente(div_consulta);

    }

    public void consultar() {
        String cm_tipo = com_tipo_trans.getValue() + "";
        String condicion = "";
        if (cal_fecha_inicio.getValue() == null || cal_fecha_final.getValue() == null) {
            condicion +="1=1";
        } else {
            condicion += " fecha_trans_incci BETWEEN '"+cal_fecha_inicio.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' ";
            if (!cm_tipo.equals("null")) {
                condicion += " and ide_intti= " + cm_tipo;
            }
            tab_consulta.setCondicion(condicion);
            tab_consulta.ejecutarSql();
            utilitario.addUpdate("tab_consulta");
        }
    }

    
    @Override
    public void insertar() {
        tab_consulta.insertar();
    }

    @Override
    public void guardar() {
        tab_consulta.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_consulta.eliminar();
    }

    public Tabla getTab_consulta() {
        return tab_consulta;
    }

    

    public void setTab_consulta(Tabla tab_consulta) {
        this.tab_consulta = tab_consulta;
    }

}

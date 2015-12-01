/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_contabilidad;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author byron
 */
public class pre_movimientos_realizados extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Division div_division = new Division();
    private SeleccionCalendario sec_calendario = new SeleccionCalendario();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private Reporte rep_reporte = new Reporte();
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();

    public pre_movimientos_realizados() {

        bar_botones.agregarCalendario();
        bar_botones.agregarReporte();
        sec_calendario.setId("sec_calendario");
        //por defecto friltra un mes
        sec_calendario.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -31));
        sec_calendario.setFecha2(utilitario.getDate());
        sec_calendario.getBot_aceptar().setMetodo("aceptarRango");
        agregarComponente(sec_calendario);
        tab_tabla.setId("tab_tabla");
            tab_tabla.setSql("select ccc.ide_cnccc,"
                    + "ccc.fecha_trans_cnccc,"
                    + "ccc.fecha_siste_cnccc,"
                    + "nombre_cneco,"
                    + "nombre_cntcm,"
                    + "per.nom_geper,"
                    + "sum (dcc.valor_cndcc) as valor,"
                    + "usua.nom_usua,"
                    + "modu.nom_modu, "
                    + "suc.nom_sucu "
                    + "from con_cab_comp_cont ccc "
                    + "left join con_det_comp_cont dcc on dcc.ide_cnccc=ccc.ide_cnccc "
                    + "left join con_tipo_comproba tcm on tcm.ide_cntcm=ccc.ide_cntcm "
                    + "left join con_estado_compro ecm on ecm.ide_cneco=ccc.ide_cneco "
                    + "left join sis_usuario usua on usua.ide_usua=ccc.ide_usua "
                    + "left join gen_persona per on per.ide_geper=ccc.ide_geper "
                    + "left join sis_modulo modu on modu.ide_modu=ccc.ide_modu "
                    + "left join sis_sucursal suc on suc.ide_sucu=ccc.ide_sucu "
                    + "where ccc.fecha_trans_cnccc BETWEEN '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "' and dcc.ide_cnlap=" + utilitario.getVariable("p_con_lugar_debe") + " "
                    + "and ccc.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                    + "GROUP BY ccc.ide_cnccc,nombre_cneco,tcm.nombre_cntcm,per.nom_geper,usua.nom_usua,modu.nom_modu,suc.nom_sucu "
                    + "ORDER BY ccc.fecha_trans_cnccc DESC,ccc.ide_cnccc DESC");
        tab_tabla.setCampoPrimaria("ide_cnccc");
        tab_tabla.setLectura(true);
        tab_tabla.setRows(20);
        tab_tabla.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        div_division.setId("div_division");
        div_division.dividir1(pat_panel);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);

        sec_rango_reporte.setId("sec_rango_reporte");
        sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sec_rango_reporte.setMultiple(false);
        sel_rep.setId("sel_rep");
        agregarComponente(sec_rango_reporte);
        agregarComponente(sel_rep);
        agregarComponente(div_division);

    }

    @Override
    public void abrirRangoFecha() {
        sec_calendario.dibujar();
    }

    public void aceptarRango() {
        if (sec_calendario.isFechasValidas()) {
            sec_calendario.cerrar();
            tab_tabla.setSql("select ccc.ide_cnccc,"
                    + "ccc.fecha_trans_cnccc,"
                    + "ccc.fecha_siste_cnccc,"
                    + "nombre_cneco,"
                    + "nombre_cntcm,"
                    + "per.nom_geper,"
                    + "sum (dcc.valor_cndcc) as valor,"
                    + "usua.nom_usua,"
                    + "modu.nom_modu, "
                    + "suc.nom_sucu "
                    + "from con_cab_comp_cont ccc "
                    + "left join con_det_comp_cont dcc on dcc.ide_cnccc=ccc.ide_cnccc "
                    + "left join con_tipo_comproba tcm on tcm.ide_cntcm=ccc.ide_cntcm "
                    + "left join con_estado_compro ecm on ecm.ide_cneco=ccc.ide_cneco "
                    + "left join sis_usuario usua on usua.ide_usua=ccc.ide_usua "
                    + "left join gen_persona per on per.ide_geper=ccc.ide_geper "
                    + "left join sis_modulo modu on modu.ide_modu=ccc.ide_modu "
                    + "left join sis_sucursal suc on suc.ide_sucu=ccc.ide_sucu "
                    + "where ccc.fecha_trans_cnccc BETWEEN '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "' and dcc.ide_cnlap=" + utilitario.getVariable("p_con_lugar_debe") + " "
                    + "and ccc.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                    + "GROUP BY ccc.ide_cnccc,nombre_cneco,tcm.nombre_cntcm,per.nom_geper,usua.nom_usua,modu.nom_modu,suc.nom_sucu "
                    + "ORDER BY ccc.fecha_trans_cnccc DESC,ccc.ide_cnccc DESC");
            tab_tabla.ejecutarSql();
            utilitario.addUpdate("tab_tabla");
        } else {
            utilitario.agregarMensajeInfo("Fechas no válidas", "");
        }
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

    
    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra 
        sec_rango_reporte.getCal_fecha1().setValue(null);
        sec_rango_reporte.getCal_fecha2().setValue(null);
        rep_reporte.dibujar();
    }
    Map parametro = new HashMap();
    String fecha_fin;
    String fecha_inicio;

    @Override
    public void aceptarReporte() {
//Se ejecuta cuando se selecciona un reporte de la lista        
        if (rep_reporte.getReporteSelecionado().equals("Movimientos realizados")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {                
                parametro.put("fecha_inicio", sec_rango_reporte.getFecha1());
                parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
                parametro.put("ide_cnlap", Integer.parseInt(utilitario.getVariable("p_con_lugar_debe")));
                
                System.out.println("fecha inicio : " +sec_rango_reporte.getFecha1());
                System.out.println("fecha fin : " + sec_rango_reporte.getFecha1());
                System.out.println("ide_cnlap: " + Integer.parseInt(utilitario.getVariable("p_con_lugar_debe")));
                sec_rango_reporte.cerrar();
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("sel_rep,sec_rango_reporte");
            }
        }
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public SeleccionCalendario getSec_calendario() {
        return sec_calendario;
    }

    public void setSec_calendario(SeleccionCalendario sec_calendario) {
        this.sec_calendario = sec_calendario;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }

    public SeleccionFormatoReporte getSel_rep() {
        return sel_rep;
    }

    public void setSel_rep(SeleccionFormatoReporte sel_rep) {
        this.sel_rep = sel_rep;
    }
}


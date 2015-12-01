/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sistema;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import java.util.HashMap;
import java.util.Map;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_permisos extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    private Tabla tab_tabla4 = new Tabla();
    private Tabla tab_tabla5 = new Tabla();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private Map map_parametros = new HashMap();
    private SeleccionTabla set_perfiles = new SeleccionTabla();

    public pre_permisos() {
        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("SIS_PERFIL", "IDE_PERF", 1);
        tab_tabla1.getColumna("activo_perf").setCheck();
        tab_tabla1.getColumna("perm_util_perf").setCheck();
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.agregarRelacion(tab_tabla3);
        tab_tabla1.agregarRelacion(tab_tabla4);
        tab_tabla1.agregarRelacion(tab_tabla5);
        tab_tabla1.setRows(20);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setIdCompleto("tab_tabulador:tab_tabla2");
        tab_tabla2.setTabla("SIS_PERFIL_REPORTE", "IDE_PERE", 2);        
        tab_tabla2.getColumna("IDE_REPO").setUnico(true);
        tab_tabla2.getColumna("IDE_PERF").setUnico(true);
        tab_tabla2
                .getColumna("IDE_REPO")
                .setCombo(
                "select repo.ide_repo,nom_repo,nom_opci from sis_reporte repo inner join sis_opcion opcion on repo.ide_opci=opcion.ide_opci order by nom_repo,nom_opci");
        tab_tabla2.getColumna("IDE_REPO").setAutoCompletar();
        tab_tabla2.setRows(20);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setIdCompleto("tab_tabulador:tab_tabla3");
        tab_tabla3.setTabla("SIS_PERFIL_OPCION", "IDE_PEOP", 3);        
        tab_tabla3.getColumna("IDE_OPCI").setCombo("select a.ide_opci,a.NOM_OPCI,"
                + "( case when b.sis_ide_opci is null then 'PANTALLA' else 'MENU' end ) as nuevo "
                + "from SIS_OPCION a left join ( "
                + "select DISTINCT sis_ide_opci   from SIS_OPCION  where sis_ide_opci  in (  "
                + "select ide_opci from SIS_OPCION ) ) b on a.IDE_OPCI=b.SIS_IDE_OPCI order by a.NOM_OPCI");
        tab_tabla3.getColumna("LECTURA_PEOP").setCheck();
        tab_tabla3.getColumna("IDE_OPCI").setAutoCompletar();
        tab_tabla3.getColumna("IDE_OPCI").setUnico(true);
        tab_tabla3.getColumna("IDE_PERF").setUnico(true);
        tab_tabla3.setRows(20);
        tab_tabla3.dibujar();
        PanelTabla pat_panel3 = new PanelTabla();
        pat_panel3.setPanelTabla(tab_tabla3);

        tab_tabla4.setId("tab_tabla4");
        tab_tabla4.setIdCompleto("tab_tabulador:tab_tabla4");
        tab_tabla4.setTabla("SIS_PERFIL_OBJETO", "IDE_PEOB", 4);
        tab_tabla4.getColumna("IDE_OBOP").setCombo("SELECT IDE_OBOP,NOM_OBOP || ' '|| ID_OBOP,NOM_OPCI FROM SIS_OBJETO_OPCION,SIS_OPCION WHERE SIS_OPCION.IDE_OPCI = SIS_OBJETO_OPCION.IDE_OPCI ORDER BY NOM_OPCI,NOM_OBOP");
        tab_tabla4.getColumna("IDE_OBOP").setAutoCompletar();
        tab_tabla4.getColumna("VISIBLE_PEOB").setCheck();
        tab_tabla4.getColumna("LECTURA_PEOB").setCheck();
        tab_tabla4.getColumna("VISIBLE_PEOB").setValorDefecto("true");
        tab_tabla4.getColumna("IDE_PEOB").setUnico(true);
        tab_tabla4.getColumna("IDE_PERF").setUnico(true);
        tab_tabla4.setRows(20);
        tab_tabla4.dibujar();
        PanelTabla pat_panel4 = new PanelTabla();
        pat_panel4.setPanelTabla(tab_tabla4);

        tab_tabla5.setId("tab_tabla5");
        tab_tabla5.setIdCompleto("tab_tabulador:tab_tabla5");
        tab_tabla5.setTabla("SIS_PERFIL_CAMPO", "IDE_PECA", 5);
        tab_tabla5.getColumna("IDE_CAMP").setCombo("SELECT IDE_CAMP,NOM_CAMP,TABLA_TABL FROM SIS_CAMPO,SIS_TABLA WHERE SIS_CAMPO.IDE_TABL = SIS_TABLA.IDE_TABL ORDER BY TABLA_TABL,NOM_CAMP");
        tab_tabla5.getColumna("IDE_CAMP").setAutoCompletar();
        tab_tabla5.getColumna("VISIBLE_PECA").setValorDefecto("true");
        tab_tabla5.getColumna("VISIBLE_PECA").setCheck();
        tab_tabla5.getColumna("LECTURA_PECA").setCheck();        
        tab_tabla5.getColumna("IDE_PERF").setUnico(true);
        tab_tabla5.getColumna("IDE_CAMP").setUnico(true);
        tab_tabla5.setRows(20);
        tab_tabla5.dibujar();
        PanelTabla pat_panel5 = new PanelTabla();
        pat_panel5.setPanelTabla(tab_tabla5);

        tab_tabulador.agregarTab("OPCIONES", pat_panel3);
        tab_tabulador.agregarTab("REPORTES", pat_panel2);
        tab_tabulador.agregarTab("OBJETOS COMPONENTES", pat_panel4);
        tab_tabulador.agregarTab("CAMPOS", pat_panel5);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, tab_tabulador, "30%", "H");
        agregarComponente(div_division);

        bar_botones.agregarReporte();
        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);

        sel_rep.setId("sel_rep");
        agregarComponente(sel_rep);

        set_perfiles.setId("set_perfiles");
        set_perfiles.setSeleccionTabla("select IDE_PERF,upper(NOM_PERF) as Perfil,DESCRIPCION_PERF from SIS_PERFIL", "ide_perf");
        set_perfiles.getBot_aceptar().setMetodo("aceptarReporte");
        set_perfiles.setTitle("SELECCION DE PERFILES");
        agregarComponente(set_perfiles);
    }

    @Override
    public void aceptarReporte() {
        // TODO Auto-generated method stub
        switch (rep_reporte.getReporteSelecionado()) {
            case "Perfiles":
                if (rep_reporte.isVisible()) {
                    map_parametros = new HashMap();
                    rep_reporte.cerrar();
                    map_parametros.put("titulo", "PERFILES DE USUARIO");
                    sel_rep.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("sel_rep,rep_reporte");
                }   break;
            case "Usuarios Por Perfil":
                if (rep_reporte.isVisible()) {
                    map_parametros = new HashMap();
                    rep_reporte.cerrar();
                    set_perfiles.dibujar();
                    //	utilitario.addUpdate("rep_reporte,set_perfiles");
                } else if (set_perfiles.isVisible()) {
                    if (set_perfiles.getListaSeleccionados().size() > 0) {
                        map_parametros.put("ide_perf", set_perfiles.getSeleccionados());
                        set_perfiles.cerrar();
                        map_parametros.put("titulo", "USUARIOS POR PERFIL");
                        sel_rep.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
                        sel_rep.dibujar();
                        //	utilitario.addUpdate("sel_rep,set_perfiles");
                    } else {
                        utilitario.agregarMensajeInfo("Atencion", "Debe seleccionar al menos un perfil de usuario");
                    }
                }   break;
            case "Recursos por Perfil":
                if (rep_reporte.isVisible()) {
                    map_parametros = new HashMap();
                    rep_reporte.cerrar();
                    set_perfiles.dibujar();
                    //	utilitario.addUpdate("rep_reporte,set_perfiles");
                } else if (set_perfiles.isVisible()) {
                    if (set_perfiles.getListaSeleccionados().size() > 0) {
                        map_parametros.put("ide_perf", set_perfiles.getSeleccionados());
                        set_perfiles.cerrar();
                        map_parametros.put("titulo", "PERFILES POR RECURSOS");
                        sel_rep.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
                        sel_rep.dibujar();
                        //	utilitario.addUpdate("sel_rep,set_perfiles");
                    } else {
                        utilitario.agregarMensajeInfo("Atencion", "Debe seleccionar al menos un perfil de usuario");
                }
            }   break;
        }
    }

    @Override
    public void abrirListaReportes() {
        // TODO Auto-generated method stub
        rep_reporte.dibujar();
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tab_tabla1.guardar()) {
            if (tab_tabla2.guardar()) {
                if (tab_tabla3.guardar()) {
                    if (tab_tabla4.guardar()) {
                        if (tab_tabla5.guardar()) {
                            guardarPantalla();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_tabla5() {
        return tab_tabla5;
    }

    public void setTab_tabla5(Tabla tab_tabla5) {
        this.tab_tabla5 = tab_tabla5;
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

    public Tabla getTab_tabla4() {
        return tab_tabla4;
    }

    public void setTab_tabla4(Tabla tab_tabla4) {
        this.tab_tabla4 = tab_tabla4;
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

    public SeleccionTabla getSet_perfiles() {
        return set_perfiles;
    }

    public void setSet_perfiles(SeleccionTabla set_perfiles) {
        this.set_perfiles = set_perfiles;
    }
}

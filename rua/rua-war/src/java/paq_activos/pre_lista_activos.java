/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_activos;

import framework.aplicacion.Fila;
import framework.componentes.BotonesCombo;
import framework.componentes.Division;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla; 
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author HP_PRO
 */
public class pre_lista_activos extends Pantalla {

    private Tabla tab_lista = new Tabla();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();

    public pre_lista_activos() {
        bar_botones.getBot_insertar().setRendered(false);
        
        bar_botones.agregarReporte();
        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);
        
        sel_rep.setId("sel_rep");
        agregarComponente(sel_rep);
        
        // boton seleccion inversa
        BotonesCombo boc_seleccion_inversa = new BotonesCombo();
        ItemMenu itm_todas = new ItemMenu();
        ItemMenu itm_niguna = new ItemMenu();

        boc_seleccion_inversa.setValue("Selección Inversa");
        boc_seleccion_inversa.setIcon("ui-icon-circle-check");
        boc_seleccion_inversa.setMetodo("seleccinarInversa");
        boc_seleccion_inversa.setUpdate("tab_lista");
        itm_todas.setValue("Seleccionar Todo");
        itm_todas.setIcon("ui-icon-check");
        itm_todas.setMetodo("seleccionarTodas");
        itm_todas.setUpdate("tab_lista");
        boc_seleccion_inversa.agregarBoton(itm_todas);
        itm_niguna.setValue("Seleccionar Ninguna");
        itm_niguna.setIcon("ui-icon-minus");
        itm_niguna.setMetodo("seleccionarNinguna");
        itm_niguna.setUpdate("tab_lista");
        boc_seleccion_inversa.agregarBoton(itm_niguna);


        tab_lista.setId("tab_lista");
        tab_lista.setTabla("act_activo_fijo","ide_acafi",1);
        tab_lista.getColumna("ide_acafi").setNombreVisual("CODIGO");
        tab_lista.getColumna("ide_inarti").setCombo("select ide_inarti,nombre_inarti,codigo_inarti from  inv_articulo  where ide_intpr=0 order by codigo_inarti"); //SOLO ACTIVOS FIJOS
        tab_lista.getColumna("ide_inarti").setMetodoChange("generarCodigoBarras");
        tab_lista.getColumna("ide_inarti").setFiltroContenido();
        tab_lista.getColumna("ide_inarti").setLongitud(70);
        tab_lista.getColumna("ide_geper").setCombo("select ide_geper,identificac_geper,nom_geper from gen_persona where es_empleado_geper=true");
        tab_lista.getColumna("ide_geper").setFiltroContenido();
        tab_lista.getColumna("ide_geper").setLongitud(90);
        tab_lista.getColumna("cod_anterior_acafi").setFiltroContenido();
        tab_lista.getColumna("ide_rheor").setVisible(false);
        tab_lista.getColumna("alterno_acafi").setVisible(false);
        tab_lista.getColumna("fo_acafi").setVisible(false);
        tab_lista.getColumna("ide_geubi").setCombo("gen_ubicacion", "ide_geubi", "nombre_geubi", "nivel_geubi='HIJO'");
        tab_lista.getColumna("ide_geubi").setAutoCompletar();
        tab_lista.getColumna("ide_geubi").setFiltroContenido();
        tab_lista.getColumna("ide_usua").setVisible(false);
        tab_lista.getColumna("ide_aceaf").setCombo("act_estado_activo_fijo", "ide_aceaf", "nombre_aceaf", "");
        tab_lista.getColumna("ide_aceaf").setRequerida(true);
        tab_lista.getColumna("ide_inmar").setCombo("inv_marca", "ide_inmar", "nombre_invmar", "");
        tab_lista.getColumna("ide_inmar").setFiltro(true);
        tab_lista.getColumna("ide_acuba").setCombo("act_ubicacion_activo","ide_acuba","nombre_acuba","");
        tab_lista.getColumna("ide_acuba").setFiltroContenido();
        tab_lista.getColumna("ide_gecas").setCombo("gen_casa","ide_gecas","nombre_gecas","");
        tab_lista.getColumna("ide_gecas").setFiltroContenido();
        tab_lista.getColumna("ide_gecas").setLongitud(60);
        tab_lista.getColumna("ide_geobr").setCombo("gen_obra","ide_geobr","nombre_geobr","");
        tab_lista.getColumna("ide_geobr").setFiltroContenido();
        tab_lista.getColumna("ide_geobr").setLongitud(100);
        tab_lista.getColumna("ide_cndpc").setCombo("select ide_cndpc,codig_recur_cndpc,nombre_cndpc from  con_det_plan_cuen order by codig_recur_cndpc");
        tab_lista.getColumna("ide_cndpc").setFiltroContenido();
        tab_lista.getColumna("ide_cndpc").setLongitud(150);
        tab_lista.getColumna("inv_ide_inarti").setCombo("inv_articulo","ide_inarti","nombre_inarti","");
        tab_lista.getColumna("inv_ide_inarti").setLongitud(70);
        tab_lista.getColumna("inv_ide_inarti").setFiltroContenido();
        tab_lista.getColumna("act_ide_acuba").setCombo("act_ubicacion_activo","ide_acuba","nombre_acuba","");
        tab_lista.getColumna("act_ide_acuba").setLongitud(70);
        tab_lista.getColumna("act_ide_acuba").setFiltroContenido();
        tab_lista.getColumna("ide_actac").setCombo("act_tipo_adquisicion","ide_actac","nombre_actac","");
        tab_lista.getColumna("ide_actac").setAutoCompletar();
        tab_lista.getColumna("ide_accls").setCombo("act_clasificacion","ide_accls","nombre_accls","");
        tab_lista.getColumna("ide_accls").setLongitud(50);
        tab_lista.getColumna("proveedor_acafi").setFiltroContenido();
        tab_lista.getColumna("ide_accla").setCombo("act_clase_activo","ide_accla","nombre_accla","");
        tab_lista.getColumna("ide_accla").setAutoCompletar();
        tab_lista.getColumna("act_ide_acafi").setVisible(false);
        tab_lista.getColumna("gen_ide_geper").setVisible(false);
        tab_lista.getColumna("nombre_acafi").setVisible(false);
        tab_lista.getColumna("deprecia_acafi").setVisible(false);
        tab_lista.getColumna("recidual_acafi").setLectura(true);
        tab_lista.getColumna("credi_tribu_acafi").setVisible(false);
        tab_lista.getColumna("anos_uso_acafi").setVisible(false);
        tab_lista.getColumna("descripcion1_acafi").setVisible(false);
        tab_lista.getColumna("cuenta_ant_sistema").setVisible(false);
        tab_lista.getColumna("color_acafi").setVisible(false);
        tab_lista.getColumna("custodio_tmp").setVisible(false);
        tab_lista.getColumna("mediadas_acafi").setVisible(false);
        tab_lista.getColumna("fecha_fabrica_acafi").setVisible(false);
        tab_lista.getColumna("fd_acafi").setVisible(false);
        tab_lista.getColumna("sec_masivo_acafi").setVisible(false);
        tab_lista.getColumna("ide_cpcfa").setVisible(false);
        tab_lista.getColumna("codigo_recu_acafi").setVisible(false);
        tab_lista.setLectura(true);
        tab_lista.setTipoSeleccion(true);
        tab_lista.dibujar(); 
        tab_lista.setRows(15);
        PanelTabla pat_lista = new PanelTabla();
        pat_lista.setId("pat_lista");
        pat_lista.getChildren().add(boc_seleccion_inversa);
        pat_lista.setPanelTabla(tab_lista);
        
        Division div_lista = new Division();
        div_lista.setId("div_lista");
        div_lista.dividir1(pat_lista);
        agregarComponente(div_lista);

        
        
        
        
    }
public void seleccionarTodas() {
        tab_lista.setSeleccionados(null);
        Fila seleccionados[] = new Fila[tab_lista.getTotalFilas()];
        for (int i = 0; i < tab_lista.getFilas().size(); i++) {
            seleccionados[i] = tab_lista.getFilas().get(i);
        }
        tab_lista.setSeleccionados(seleccionados);
        //calculoTotal();

    }

    public void seleccinarInversa() {
        if (tab_lista.getSeleccionados() == null) {
            seleccionarTodas();
        } else if (tab_lista.getSeleccionados().length == tab_lista.getTotalFilas()) {
            seleccionarNinguna();
        } else {
            Fila seleccionados[] = new Fila[tab_lista.getTotalFilas() - tab_lista.getSeleccionados().length];
            int cont = 0;
            for (int i = 0; i < tab_lista.getFilas().size(); i++) {
                boolean boo_selecionado = false;
                for (int j = 0; j < tab_lista.getSeleccionados().length; j++) {
                    if (tab_lista.getSeleccionados()[j].equals(tab_lista.getFilas().get(i))) {
                        boo_selecionado = true;
                        break;
                    }
                }
                if (boo_selecionado == false) {
                    seleccionados[cont] = tab_lista.getFilas().get(i);
                    cont++;
                }
            }
            tab_lista.setSeleccionados(seleccionados);
        }
        //calculoTotal();
    }

    public void seleccionarNinguna() {
        tab_lista.setSeleccionados(null);

    }

    public Tabla getTab_lista() {
        return tab_lista;
    }

    public void setTab_lista(Tabla tab_lista) {
        this.tab_lista = tab_lista;
    }
@Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Codigo Barras")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
               
                parametro.put("ide_acafi", tab_lista.getFilasSeleccionadas());
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("sel_rep");
            }        
        }
    }
    @Override
    public void insertar() {
        tab_lista.insertar();
    }

    @Override
    public void guardar() {
       tab_lista.guardar();
       guardarPantalla();
    }

    @Override
    public void eliminar() {
     tab_lista.eliminar();
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
    
}

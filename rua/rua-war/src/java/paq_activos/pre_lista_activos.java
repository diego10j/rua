/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_activos;

import framework.componentes.Division;
import framework.componentes.PanelTabla; 
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author HP_PRO
 */
public class pre_lista_activos extends Pantalla {

    private Tabla tab_lista = new Tabla();

    public pre_lista_activos() {

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
        tab_lista.dibujar(); 
        tab_lista.setRows(15);
        PanelTabla pat_lista = new PanelTabla();
        pat_lista.setId("pat_lista");
        pat_lista.setPanelTabla(tab_lista);
        
        Division div_lista = new Division();
        div_lista.setId("div_lista");
        div_lista.dividir1(pat_lista);
        agregarComponente(div_lista);

        
        
        
        
    }

    public Tabla getTab_lista() {
        return tab_lista;
    }

    public void setTab_lista(Tabla tab_lista) {
        this.tab_lista = tab_lista;
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
    
}

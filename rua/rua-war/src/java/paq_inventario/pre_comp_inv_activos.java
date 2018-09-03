/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_inventario;

/**
 *
 * @author ANDRES
 */

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import servicios.pensiones.ServicioPensiones;
import sistema.aplicacion.Pantalla;

public class pre_comp_inv_activos extends Pantalla{
    
    private Tabla tab_tabla = new Tabla();
    private SeleccionTabla sel_cabece_compra = new SeleccionTabla();
    private SeleccionTabla sel_detalle_compra = new SeleccionTabla();
    String factura="";
    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    
    public pre_comp_inv_activos(){
        tab_tabla.setId("tab_tabla");   //identificador
        tab_tabla.setTabla("act_activo_fijo", "ide_acafi", 1);
       // tab_tabla.setCondicion("1=1 limit 25");
     //   tab_tabla.setSql("select * from act_activo_fijo order by ide_acafi desc limit 25");
        tab_tabla.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "");
        tab_tabla.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "codigo_inarti,nombre_inarti", "");
        tab_tabla.getColumna("ide_geper").setLectura(true);
        tab_tabla.getColumna("ide_inarti").setLectura(true);
        tab_tabla.getColumna("cantidad_acafi").setLectura(true);
        tab_tabla.getColumna("valor_compra_acafi").setLectura(true);
        tab_tabla.getColumna("numero_factu_acafi").setLectura(true);
        tab_tabla.getColumna("fecha_compra_acafi").setLectura(true);
        
        tab_tabla.getColumna("ide_aceaf").setVisible(false);
        tab_tabla.getColumna("ide_usua").setVisible(false);
        tab_tabla.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla.getColumna("ide_geubi").setVisible(false);
        tab_tabla.getColumna("gen_ide_geper").setVisible(false);
        tab_tabla.getColumna("ide_rheor").setVisible(false);
        tab_tabla.getColumna("nombre_acafi").setVisible(false);
        tab_tabla.getColumna("vida_util_acafi").setVisible(false);
        tab_tabla.getColumna("deprecia_acafi").setVisible(false);
        tab_tabla.getColumna("recidual_acafi").setVisible(false);
        tab_tabla.getColumna("serie_acafi").setVisible(false);
        tab_tabla.getColumna("observacion_acafi").setVisible(false);
        tab_tabla.getColumna("credi_tribu_acafi").setVisible(false);
        tab_tabla.getColumna("alterno_acafi").setVisible(false);
        tab_tabla.getColumna("codigo_recu_acafi").setVisible(false);
        tab_tabla.getColumna("fecha_acafi").setVisible(false);
        tab_tabla.getColumna("ano_actual_acafi").setVisible(false);
        tab_tabla.getColumna("anos_uso_acafi").setVisible(false);
        tab_tabla.getColumna("valor_comercial_acafi").setVisible(false);
        tab_tabla.getColumna("valor_remate_acafi").setVisible(false);
        tab_tabla.getColumna("fo_acafi").setVisible(false);
        tab_tabla.getColumna("modelo_acafi").setVisible(false);
        tab_tabla.getColumna("ide_inmar").setVisible(false);
        tab_tabla.getColumna("ide_acuba").setVisible(true);
        tab_tabla.getColumna("ide_acuba").setCombo("act_ubicacion_activo", "ide_acuba", "nombre_acuba", "");
        tab_tabla.getColumna("ide_acuba").setLectura(true);
        tab_tabla.getColumna("descripcion1_acafi").setVisible(false);
        tab_tabla.getColumna("cuenta_ant_sistema").setVisible(false);
        tab_tabla.getColumna("color_acafi").setVisible(false);
        tab_tabla.getColumna("custodio_tmp").setVisible(false);
        tab_tabla.getColumna("mediadas_acafi").setVisible(false);
        tab_tabla.getColumna("fecha_fabrica_acafi").setVisible(false);
        tab_tabla.getColumna("fd_acafi").setVisible(false);
        tab_tabla.getColumna("codigo_barras_acafi").setVisible(false);
        tab_tabla.getColumna("ide_gecas").setVisible(true);
        tab_tabla.getColumna("ide_gecas").setLectura(true);
        tab_tabla.getColumna("ide_gecas").setCombo("gen_casa", "ide_gecas", "nombre_gecas", "");
        tab_tabla.getColumna("ide_geobr").setVisible(true);
        tab_tabla.getColumna("ide_geobr").setLectura(true);
        tab_tabla.getColumna("ide_geobr").setCombo("gen_obra", "ide_geobr", "nombre_geobr", "");
        tab_tabla.getColumna("ide_accla").setVisible(true);
        tab_tabla.getColumna("ide_accla").setLectura(true);
        tab_tabla.getColumna("ide_accla").setCombo("act_clase_activo", "ide_accla", "nombre_accla", "");
        tab_tabla.getColumna("act_ide_acafi").setVisible(false);
        tab_tabla.getColumna("sec_masivo_acafi").setVisible(false);
        tab_tabla.getColumna("cod_anterior_acafi").setVisible(false); 
        tab_tabla.getColumna("ide_actac").setVisible(false); 
        tab_tabla.getColumna("ide_accls").setVisible(false); 
        
        tab_tabla.setLimite(25);
        tab_tabla.dibujar();
        PanelTabla pat_tabla = new PanelTabla();
        pat_tabla.setId("pat_tabla");
        pat_tabla.setPanelTabla(tab_tabla);
        Division div_tabla1 = new Division();
        div_tabla1.setId("div_tabla1");
        div_tabla1.dividir1(pat_tabla);
        agregarComponente(div_tabla1);
        
        Boton bot_anular = new Boton();
        bot_anular.setIcon("ui-icon-search");
        bot_anular.setValue("BUSCAR FACTURA");
        bot_anular.setMetodo("dibujaFacturas");
        bar_botones.agregarBoton(bot_anular);
        
        sel_cabece_compra.setId("sel_cabece_compra");
        sel_cabece_compra.setTitle("SELECCIONE UNA FACTURA");
        sel_cabece_compra.setSeleccionTabla("select ide_cpcfa, fecha_emisi_cpcfa, b.identificac_geper, b.nom_geper, total_cpcfa\n" +
                                            "from cxp_cabece_factur a\n" +
                                            "left join gen_persona b on a.ide_geper = b.ide_geper\n" +
                                            "left join adq_compra c on a.ide_adcomp = c.ide_adcomp\n" +
                                            "where a.ide_adcomp = c.ide_adcomp\n" +
                                            "and recibido_compra_cpcfa = false\n" +
                                            "and c.ingreso_adcomp = 2", "ide_cpcfa");
        sel_cabece_compra.setWidth("80%");
        sel_cabece_compra.setHeight("70%");
        sel_cabece_compra.setRadio();
        sel_cabece_compra.getBot_aceptar().setMetodo("aceptarSolicitud");
        agregarComponente(sel_cabece_compra);
        
        sel_detalle_compra.setId("sel_detalle_compra");
        sel_detalle_compra.setTitle("SELECCIONA EL DETALLE DE LA FACTURA");
        sel_detalle_compra.setSeleccionTabla(ser_adquisiciones.getdetalleFacturaCompra("1", ""), "ide_cpdfa");
        sel_detalle_compra.setWidth("80%");
        sel_detalle_compra.setHeight("70%");
        //sel_tab_detalle_compra.setRadio();
        sel_detalle_compra.getBot_aceptar().setMetodo("ingresaValores");
        agregarComponente(sel_detalle_compra);
    }
    
    public void dibujaFacturas(){
        sel_cabece_compra.dibujar();
    }
    public void aceptarSolicitud(){
        factura = sel_cabece_compra.getValorSeleccionado();
        sel_cabece_compra.cerrar();
        sel_detalle_compra.getTab_seleccion().setSql(ser_adquisiciones.getdetalleFacturaCompra("2", factura));
        sel_detalle_compra.getTab_seleccion().ejecutarSql();   
        sel_detalle_compra.dibujar();
    }
    
    public void ingresaValores(){
        int numero = 0;
        int cantidad = 1;
        String productos = sel_detalle_compra.getSeleccionados();
        TablaGenerica tab_pro_fac = utilitario.consultar("select a.ide_cpcfa, b.ide_inarti, ide_geper,ide_accla, total_cpcfa, cantidad_cpdfa, observacion_cpdfa, a.fecha_emisi_cpcfa\n" +
                                                         "from cxp_cabece_factur a\n" +
                                                         "left join cxp_detall_factur b on a.ide_cpcfa = b.ide_cpcfa\n" +
                                                         "left join (select a.ide_accla, a.ide_inarti, nombre_inarti, b.nombre_accla from ACT_CLASE_ARTICULO a\n" +
                                                         "left join ACT_CLASE_ACTIVO b on a.ide_accla = b.ide_accla\n" +
                                                         "left join inv_articulo c on a.ide_inarti = c.ide_inarti) c on b.ide_inarti = c.ide_inarti\n" +
                                                         "where b.ide_cpdfa in ("+productos+")");
        //System.out.println("pro "+productos);
        TablaGenerica pro_gen = utilitario.consultar("select ide_cpdfa, cast(cantidad_cpdfa as integer)  from cxp_detall_factur where ide_cpdfa in ("+productos+")");
        
        numero = Integer.parseInt(pro_gen.getValor("cantidad_cpdfa"));
         for (int i=0; i < tab_pro_fac.getTotalFilas(); i++ ){
            for (int j=0; j < numero; j++ ){
                tab_tabla.insertar();
                tab_tabla.setValor("ide_geper",tab_pro_fac.getValor(i, "ide_geper"));
                tab_tabla.setValor("ide_inarti",tab_pro_fac.getValor(i, "ide_inarti"));
                tab_tabla.setValor("cantidad_acafi", "1");
                tab_tabla.setValor("valor_compra_acafi",tab_pro_fac.getValor(i, "total_cpcfa"));
                tab_tabla.setValor("numero_factu_acafi",tab_pro_fac.getValor(i, "ide_cpcfa"));
                tab_tabla.setValor("ide_cpcfa",tab_pro_fac.getValor(i, "ide_cpcfa"));
                tab_tabla.setValor("observacion_acafi",tab_pro_fac.getValor(i, "observacion_cpdfa"));
                tab_tabla.setValor("fecha_compra_acafi",tab_pro_fac.getValor(i, "fecha_emisi_cpcfa"));
                tab_tabla.setValor("ide_acuba",utilitario.getVariable("p_act_area_ubicacion"));
                tab_tabla.setValor("ide_gecas",utilitario.getVariable("p_act_casa"));
                tab_tabla.setValor("ide_geobr",utilitario.getVariable("p_act_obra"));
                tab_tabla.setValor("ide_accla",tab_pro_fac.getValor(i, "ide_accla"));
                
                
              //  utilitario.getConexion().ejecutarSql("update cxp_detall_factur set recibido_compra_cpdfa = true where ide_cpdfa in ("+productos+")");
            }
         }
        /*TablaGenerica tab_con_recibido = utilitario.consultar("select ide_cpdfa, recibido_compra_cpdfa \n" +
                                                               "from cxp_detall_factur where ide_cpcfa = "+factura+" \n" +
                                                               "and recibido_compra_cpdfa = false");
         if (tab_con_recibido.getTotalFilas()> 0){
             
         } else {
             utilitario.getConexion().ejecutarSql("update cxp_cabece_factur set recibido_compra_cpcfa = true where ide_cpcfa = "+factura+"");
         }*/
         
        sel_detalle_compra.cerrar();
      //  tab_tabla.guardar();
      //  guardarPantalla();
	utilitario.addUpdate("tab_tabla");
    }
    
    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
       tab_tabla.eliminar();
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public SeleccionTabla getSel_cabece_compra() {
        return sel_cabece_compra;
    }

    public void setSel_cabece_compra(SeleccionTabla sel_cabece_compra) {
        this.sel_cabece_compra = sel_cabece_compra;
    }

    public SeleccionTabla getSel_detalle_compra() {
        return sel_detalle_compra;
    }

    public void setSel_detalle_compra(SeleccionTabla sel_detalle_compra) {
        this.sel_detalle_compra = sel_detalle_compra;
    }
    
}

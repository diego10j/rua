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
        tab_tabla.setSql("select * from act_activo_fijo order by ide_acafi desc limit 100");
        tab_tabla.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "");
        tab_tabla.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "codigo_inarti,nombre_inarti", "");
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
        String productos = sel_detalle_compra.getSeleccionados();
        TablaGenerica tab_pro_fac = utilitario.consultar("select a.ide_cpcfa, b.ide_inarti, ide_geper, valor_cpdfa, cantidad_cpdfa\n" +
                                                         "from cxp_cabece_factur a\n" +
                                                         "left join cxp_detall_factur b on a.ide_cpcfa = b.ide_cpcfa\n" +
                                                         "where a.ide_cpcfa = "+factura+"");
        System.out.println("pro "+productos);
        TablaGenerica pro_gen = utilitario.consultar("select ide_cpdfa, cast(cantidad_cpdfa as integer)  from cxp_detall_factur where ide_cpdfa in ("+productos+")");
        
        numero = Integer.parseInt(pro_gen.getValor("cantidad_cpdfa"));
         for (int i=0; i < tab_pro_fac.getTotalFilas(); i++ ){
            for (int j=0; j < numero; j++ ){
                tab_tabla.insertar();
                tab_tabla.setValor("ide_geper",tab_pro_fac.getValor(i, "ide_geper"));
                tab_tabla.setValor("ide_inarti",tab_pro_fac.getValor(i, "ide_inarti"));
                tab_tabla.setValor("cantidad_acafi", null);
                tab_tabla.setValor("valor_compra_acafi",tab_pro_fac.getValor(i, "valor_cpdfa"));
            }
         }
        
        sel_detalle_compra.cerrar();
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

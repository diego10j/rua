/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_activos_fijos;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionArbol;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.event.NodeSelectEvent;
import pkg_contabilidad.VistaAsiento;
import pkg_contabilidad.cls_cab_comp_cont;
import pkg_contabilidad.cls_contabilidad;
import pkg_contabilidad.cls_det_comp_cont;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_activos_fijos extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_act_asignacion_activo = new Tabla();
    private Division div_division = new Division();
    private Arbol arb_arbol = new Arbol();
    private Boton bot_depreciar = new Boton();
    private Boton bot_reasignar = new Boton();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private SeleccionArbol sel_arb = new SeleccionArbol();
    private Tabulador tab_tabulador = new Tabulador();
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();
    private Dialogo dia_asignacion_activo = new Dialogo();
    private Tabla tab_datos_reasignacion = new Tabla();
    private Boton bot_dar_baja = new Boton();
    private Dialogo dia_dar_baja = new Dialogo();
    private Tabla tab_activo_dar_baja = new Tabla();
    private Tabla tab_act_tran = new Tabla();
    private Dialogo dia_num_factura = new Dialogo();
    private AutoCompletar aut_num_factura = new AutoCompletar();
    private Dialogo dia_ingreso_activo = new Dialogo();
    private Tabla tab_ingreso_activo = new Tabla();
    private Tabla tab_historial_asignacion = new Tabla();
    private Tabla tab_arti = new Tabla();
    private String p_activos_fijos = utilitario.getVariable("p_inv_articulo_activo_fijo");
    private VistaAsiento via_comprobante_conta = new VistaAsiento();
    cls_contabilidad conta = new cls_contabilidad();
    cls_cab_comp_cont cab_com_con;
    List<cls_det_comp_cont> lista_detalles = new ArrayList();
    private String p_estado_comprobante_normal = utilitario.getVariable("p_con_estado_comprobante_normal");
    //
    cls_activos_fijos cls_activo_fijo = new cls_activos_fijos();
    private VisualizarPDF vp = new VisualizarPDF();
    private Dialogo dia_item_activo = new Dialogo();
    private Texto tex_item_activo = new Texto();
    private Grid gri_item_activo = new Grid();
    private Dialogo dia_tipo_ingreso_activo = new Dialogo();
    private Radio rad_tipo_ingreso = new Radio();

    public pre_activos_fijos() {

        bar_botones.agregarReporte();
        bar_botones.agregarBoton(bot_depreciar);

        bar_botones.getBot_siguiente().setMetodo("siguiente");
        bar_botones.getBot_atras().setMetodo("atras");
        bar_botones.getBot_fin().setMetodo("fin");
        bar_botones.getBot_inicio().setMetodo("inicio");

        bot_depreciar.setMetodo("depreciarActivo");
        bot_depreciar.setValue("Depreciar");
        bot_depreciar.setUpdate("tab_tabla2");

//        bot_reasignar.setMetodo("reasignarActivo");
//        bot_reasignar.setValue("Reasignar Activo");
//        bar_botones.agregarComponente(bot_reasignar);
        bot_dar_baja.setMetodo("dardeBajaActivo");
        bot_dar_baja.setValue("Dar de Baja");
        bar_botones.agregarComponente(bot_dar_baja);

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("act_activo_fijo", "ide_acafi", 1);
        tab_tabla1.setRows(15);
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.agregarRelacion(tab_historial_asignacion);
        tab_tabla1.getColumna("ide_aceaf").setCombo("act_estado_activo_fijo", "ide_aceaf", "nombre_aceaf", "");
        tab_tabla1.getColumna("ide_rheor").setCombo("reh_estruc_organi", "ide_rheor", "nombre_rheor", "");
        tab_tabla1.getColumna("ide_inmar").setCombo("inv_marca", "ide_inmar", "nombre_invmar", "");
        tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "");
        tab_tabla1.getColumna("ide_geper").setLectura(true);
        tab_tabla1.getColumna("ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("ide_acuba").setCombo("act_ubicacion_activo", "ide_acuba", "nombre_acuba", "");
        tab_tabla1.getColumna("gen_ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "");
        tab_tabla1.getColumna("gen_ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "");
        tab_tabla1.getColumna("ide_geubi").setCombo("gen_ubicacion", "ide_geubi", "nombre_geubi", "");
        tab_tabla1.getColumna("ide_geubi").setVisible(false);
        tab_tabla1.getColumna("ide_inarti").setVisible(false);
        tab_tabla1.getColumna("ide_rheor").setVisible(false);
        tab_tabla1.getColumna("gen_ide_geper").setVisible(false);
        tab_tabla1.getColumna("valor_compra_acafi").setVisible(false);
        tab_tabla1.getColumna("recidual_acafi").setVisible(false);
        tab_tabla1.getColumna("anos_uso_acafi").setVisible(false);
        tab_tabla1.getColumna("valor_remate_acafi").setVisible(false);
        tab_tabla1.getColumna("cantidad_acafi").setVisible(false);
        tab_tabla1.getColumna("vida_util_acafi").setVisible(false);
        tab_tabla1.getColumna("deprecia_acafi").setVisible(false);
        tab_tabla1.getColumna("alterno_acafi").setVisible(false);
        tab_tabla1.getColumna("ano_actual_acafi").setVisible(false);
        tab_tabla1.getColumna("valor_comercial_acafi").setVisible(false);
        tab_tabla1.getColumna("numero_factu_acafi").setVisible(false);
        tab_tabla1.getColumna("credi_tribu_acafi").setVisible(false);
        tab_tabla1.getColumna("fo_acafi").setVisible(false);
        tab_tabla1.getColumna("foto_acafi").setVisible(false);
        tab_tabla1.getColumna("ide_usua").setVisible(false);
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.setCampoOrden("ide_acafi desc");
        tab_tabla1.setIdCompleto("tab_tabulador:tab_tabla1");
        tab_tabla1.setRecuperarLectura(true);
        tab_tabla1.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);

        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setTabla("act_activo_fijo", "ide_acafi", 3);
        tab_tabla3.setRows(15);
        tab_tabla3.agregarRelacion(tab_tabla2);
        tab_tabla3.agregarRelacion(tab_act_asignacion_activo);
        tab_tabla3.agregarRelacion(tab_historial_asignacion);
        tab_tabla3.getColumna("ide_geubi").setVisible(false);
        tab_tabla3.getColumna("ide_acuba").setVisible(false);
        tab_tabla3.getColumna("ide_geper").setVisible(false);
        tab_tabla3.getColumna("ide_rheor").setVisible(false);
        tab_tabla3.getColumna("serie_acafi").setVisible(false);
        tab_tabla3.getColumna("alterno_acafi").setVisible(false);
        tab_tabla3.getColumna("codigo_recu_acafi").setVisible(false);
        tab_tabla3.getColumna("fecha_acafi").setVisible(false);
        tab_tabla3.getColumna("foto_acafi").setVisible(false);
        tab_tabla3.getColumna("fo_acafi").setVisible(false);
        tab_tabla3.getColumna("ide_aceaf").setVisible(false);
        tab_tabla3.getColumna("ide_inarti").setVisible(false);
        tab_tabla3.getColumna("gen_ide_geper").setVisible(false);
        tab_tabla3.getColumna("observacion_acafi").setVisible(false);
        tab_tabla3.getColumna("ide_inmar").setVisible(false);
        tab_tabla3.getColumna("modelo_acafi").setVisible(false);
        tab_tabla3.setTipoFormulario(true);
        tab_tabla3.getGrid().setColumns(4);
        tab_tabla3.setCampoOrden("ide_acafi desc");
        tab_tabla3.setIdCompleto("tab_tabulador:tab_tabla3");
        tab_tabla3.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla3);

        tab_historial_asignacion.setId("tab_historial_asignacion");
        tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                + "where aa.ide_acafi=-1 "
                + "order by aa.fecha_acaaf DESC");
        tab_historial_asignacion.setCampoPrimaria("has_ide_acaaf");
        tab_historial_asignacion.getColumna("has_ide_acaaf").setVisible(false);
        tab_historial_asignacion.setLectura(true);
        tab_historial_asignacion.setRows(10);
        tab_historial_asignacion.setNumeroTabla(9);

//        tab_historial_asignacion.setIdCompleto("tab_tabulador:tab_historial_asignacion");
        tab_historial_asignacion.dibujar();
        PanelTabla pat_panel_historial = new PanelTabla();
        pat_panel_historial.setPanelTabla(tab_historial_asignacion);

        tab_tabulador.setId("tab_tabulador");
        tab_tabulador.agregarTab("ACTIVOS FIJOS", pat_panel);
        tab_tabulador.agregarTab("DATOS COMPRA", pat_panel1);
//        tab_tabulador.agregarTab("HISTORIAL ASIGNACION", pat_panel_historial);

        tab_arti.setId("tab_arti");
        tab_arti.setTabla("inv_articulo", "ide_inarti", -1);
        tab_arti.setCampoNombre("nombre_inarti"); //Para que se configure el arbol
        tab_arti.setCampoPadre("inv_ide_inarti"); //Para que se configure el arbol
        tab_arti.agregarArbol(arb_arbol); //Para que se configure el arbol
        tab_arti.dibujar();
        arb_arbol.setId("arb_arbol");
        arb_arbol.setCondicion("ide_inarti=" + p_activos_fijos);
        arb_arbol.onSelect("seleccionar_arbol");

        arb_arbol.dibujar();

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("act_transaccion", "ide_actra", 2);
        tab_tabla2.getColumna("ide_acttr").setValorDefecto("0");
        tab_tabla2.getColumna("ide_acttr").setCombo("act_tipo_transaccion", "ide_acttr", "nombre_acttr", "");
        tab_tabla2.setCampoOrden("acumulado_actra desc");
        tab_tabla2.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_tabla2.setRecuperarLectura(true);
        tab_tabla2.setCampoOrden("ide_actra desc");
        tab_tabla2.dibujar();

        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        Division div_2 = new Division();
        div_2.dividir2(tab_tabulador, pat_panel2, "65%", "H");

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);

        sec_rango_reporte.setId("sec_rango_reporte");
        sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sec_rango_reporte.setMultiple(false);
        agregarComponente(sec_rango_reporte);

        sel_rep.setId("sel_rep");
        agregarComponente(sel_rep);

        sel_rep.setId("sel_rep");
        agregarComponente(sel_rep);
        sel_arb.setId("sel_arb");
        sel_arb.setSeleccionArbol("inv_articulo", "ide_inarti", "nombre_inarti", "inv_ide_inarti");

        sel_arb.getArb_seleccion().setCondicion("ide_inarti=" + p_activos_fijos);
        sel_arb.getBot_aceptar().setMetodo("aceptarReporte");

        agregarComponente(sel_arb);

        sel_arb.getBot_aceptar().setUpdate("sel_arb");

        div_division.setId("div_division");
        div_division.dividir2(arb_arbol, div_2, "21%", "V");

        agregarComponente(div_division);

        tab_act_asignacion_activo.setId("tab_act_asignacion_activo");
        tab_act_asignacion_activo.setTabla("act_asignacion_activo", "ide_acaaf", 4);
        tab_act_asignacion_activo.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "");
        tab_act_asignacion_activo.getColumna("ide_geper").setAutoCompletar();
        tab_act_asignacion_activo.getColumna("ide_empr").setValorDefecto(utilitario.getVariable("ide_empr"));
        tab_act_asignacion_activo.getColumna("ide_empr").setVisible(false);
        tab_act_asignacion_activo.getColumna("ide_sucu").setValorDefecto(utilitario.getVariable("ide_sucu"));
        tab_act_asignacion_activo.getColumna("ide_sucu").setVisible(false);
        tab_act_asignacion_activo.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_act_asignacion_activo.getColumna("ide_usua").setVisible(false);
        tab_act_asignacion_activo.getColumna("fecha_acaaf").setValorDefecto(utilitario.getFechaActual());
        tab_act_asignacion_activo.setCampoOrden("ide_acaaf desc");
        tab_act_asignacion_activo.setTipoFormulario(true);
        tab_act_asignacion_activo.getGrid().setColumns(2);
        tab_act_asignacion_activo.dibujar();
        PanelTabla pat_panel3 = new PanelTabla();
        pat_panel3.setPanelTabla(tab_act_asignacion_activo);

        tab_datos_reasignacion.setId("tab_datos_reasignacion");
        tab_datos_reasignacion.setSql("select af.ide_acafi as sql_ide_acafi,nom_geper as custodio_actual,nombre_acuba as ubicacion,nombre_acafi as descripcion,serie_acafi as serie,modelo_acafi as modelo from act_activo_fijo af "
                + "left join gen_persona per on per.ide_geper = af.ide_geper "
                + "left join inv_articulo arti on arti.ide_inarti = af.ide_inarti "
                + "left join inv_articulo arti1 on arti.inv_ide_inarti = arti1.ide_inarti "
                + "left join act_ubicacion_activo acubi on acubi.ide_acuba=af.ide_acuba "
                + "where ide_acafi =-1");
        tab_datos_reasignacion.setCampoPrimaria("sql_ide_acafi");
        tab_datos_reasignacion.setNumeroTabla(5);
        tab_datos_reasignacion.getColumna("sql_ide_acafi").setVisible(false);
        tab_datos_reasignacion.setLectura(true);
        tab_datos_reasignacion.dibujar();

        dia_asignacion_activo.setId("dia_asignacion_activo");
        dia_asignacion_activo.setWidth("55%");
        dia_asignacion_activo.setHeight("60%");
        dia_asignacion_activo.getBot_aceptar().setMetodo("aceptarAsignacion");
        dia_asignacion_activo.getBot_cancelar().setMetodo("cancelarDialogo");

        Grid gri_datos_asignacion = new Grid();
        gri_datos_asignacion.setStyle("width:" + (dia_asignacion_activo.getAnchoPanel() - 5) + "px;height:" + dia_asignacion_activo.getAltoPanel() + "px;overflow: auto;display: block;");
        gri_datos_asignacion.getChildren().add(tab_datos_reasignacion);
        gri_datos_asignacion.getChildren().add(pat_panel3);
        dia_asignacion_activo.setDialogo(gri_datos_asignacion);
        agregarComponente(dia_asignacion_activo);

        dia_dar_baja.setId("dia_dar_baja");
        dia_dar_baja.setWidth("55%");
        dia_dar_baja.setHeight("60%");
        dia_dar_baja.getBot_aceptar().setMetodo("aceptarDarBajaActivo");
        dia_dar_baja.getBot_cancelar().setMetodo("cancelarDialogo");

        tab_activo_dar_baja.setId("tab_activo_dar_baja");
        tab_activo_dar_baja.setSql("select af.ide_acafi as baja_ide_acafi,nombre_acuba as ubicacion,nombre_acafi as descripcion,serie_acafi as serie,modelo_acafi as modelo,nom_geper as custodio_actual from act_activo_fijo af "
                + "left join gen_persona per on per.ide_geper = af.ide_geper "
                + "left join inv_articulo arti on arti.ide_inarti = af.ide_inarti "
                + "left join inv_articulo arti1 on arti.inv_ide_inarti = arti1.ide_inarti "
                + "left join act_ubicacion_activo acubi on acubi.ide_acuba=af.ide_acuba "
                + "where ide_acafi =-1");
        tab_activo_dar_baja.setCampoPrimaria("baja_ide_acafi");
        tab_activo_dar_baja.setNumeroTabla(6);
        tab_activo_dar_baja.getColumna("baja_ide_acafi").setVisible(false);
        tab_activo_dar_baja.setLectura(true);
        tab_activo_dar_baja.dibujar();

        tab_act_tran.setId("tab_act_tran");
        tab_act_tran.setSql("SELECT ide_actra as sql_ide_actra,ide_acttr,fecha_actra,valor_actra ,observacion_actra "
                + "from act_transaccion "
                + "where ide_actra=-1");
        tab_act_tran.setCampoPrimaria("sql_ide_actra");
        tab_act_tran.setNumeroTabla(7);
        tab_act_tran.getColumna("ide_acttr").setCombo("act_tipo_transaccion", "ide_acttr", "nombre_acttr", "");
        tab_act_tran.getColumna("ide_acttr").setValorDefecto("1");
        tab_act_tran.getColumna("ide_acttr").setLectura(true);
        tab_act_tran.getColumna("ide_acttr").setNombreVisual("TIPO TRANSACCION");
        tab_act_tran.getColumna("fecha_actra").setNombreVisual("FECHA");
        tab_act_tran.getColumna("fecha_actra").setValorDefecto(utilitario.getFechaActual());
        tab_act_tran.getColumna("observacion_actra").setNombreVisual("OBSERVACION");
        tab_act_tran.getColumna("valor_actra").setNombreVisual("VALOR TRANSACCION");
        tab_act_tran.getColumna("sql_ide_actra").setVisible(false);
        tab_act_tran.setTipoFormulario(true);
        tab_act_tran.dibujar();

        Grid gri_datos_dar_baja = new Grid();
        gri_datos_dar_baja.setStyle("width:" + (dia_dar_baja.getAnchoPanel() - 5) + "px;height:" + dia_dar_baja.getAltoPanel() + "px;overflow: auto;display: block;");
        gri_datos_dar_baja.getChildren().add(tab_activo_dar_baja);
        gri_datos_dar_baja.getChildren().add(tab_act_tran);
        dia_dar_baja.setDialogo(gri_datos_dar_baja);
        agregarComponente(dia_dar_baja);

        // dialogo tipo de ingreso de activo por factura o por donacion
        dia_tipo_ingreso_activo.setId("dia_tipo_ingreso_activo");
        dia_tipo_ingreso_activo.setWidth("25%");
        dia_tipo_ingreso_activo.setHeight("25%");
        dia_tipo_ingreso_activo.getBot_aceptar().setMetodo("aceptarTipoIngresoActivo");
        dia_tipo_ingreso_activo.getBot_cancelar().setMetodo("cancelarDialogo");
        dia_tipo_ingreso_activo.setTitle("SELECCION TIPO DE INGRESO");
        dia_tipo_ingreso_activo.setDynamic(false);
        List lista = new ArrayList();
        Object fila1[] = {
            "0", "FACTURA"
        };
        Object fila2[] = {
            "1", "DONACION"
        };
        lista.add(fila1);
        lista.add(fila2);
        rad_tipo_ingreso.setId("rad_tipo_ingreso");
        rad_tipo_ingreso.setRadio(lista);
        rad_tipo_ingreso.setVertical();

        Grid gri_tipo_ingreso_act = new Grid();
        gri_tipo_ingreso_act.setColumns(1);
        gri_tipo_ingreso_act.setStyle("width:" + (dia_tipo_ingreso_activo.getAnchoPanel() - 5) + "px;height:" + dia_tipo_ingreso_activo.getAltoPanel() + "px;overflow: auto;display: block;");
        gri_tipo_ingreso_act.getChildren().add(rad_tipo_ingreso);
        dia_tipo_ingreso_activo.setDialogo(gri_tipo_ingreso_act);
        agregarComponente(dia_tipo_ingreso_activo);

//****************************************************************************************************************************
        // DIALOGO CON NUMERO DE FACTURA PARA EL INGRESO DE ACTIVOS
        dia_num_factura.setId("dia_num_factura");
        dia_num_factura.setWidth("35%");
        dia_num_factura.setHeight("20%");
        dia_num_factura.getBot_aceptar().setMetodo("aceptarNumeroFactura");
        dia_num_factura.getBot_cancelar().setMetodo("cancelarDialogo");

        aut_num_factura.setId("aut_num_factura");
        aut_num_factura.setAutoCompletar("select ide_cpcfa,numero_cpcfa,nom_geper,total_cpcfa from cxp_cabece_factur cf "
                + "left join gen_persona per on per.ide_geper=cf.ide_geper "
                + "where cf.ide_empr=" + utilitario.getVariable("ide_empr") + "");

        Grid gri_ingreso_num_fac = new Grid();
        gri_ingreso_num_fac.setColumns(2);
        gri_ingreso_num_fac.setStyle("width:" + (dia_num_factura.getAnchoPanel() - 5) + "px;height:" + dia_num_factura.getAltoPanel() + "px;overflow: auto;display: block;");
        gri_ingreso_num_fac.getChildren().add(new Etiqueta("Numero Factura: "));
        gri_ingreso_num_fac.getChildren().add(aut_num_factura);
        dia_num_factura.setDialogo(gri_ingreso_num_fac);
        agregarComponente(dia_num_factura);

// DIALOGO CON NUMERO DE FACTURA PARA EL INGRESO DE ACTIVOS
        dia_item_activo.setId("dia_item_activo");
        dia_item_activo.setWidth("35%");
        dia_item_activo.setHeight("20%");
        dia_item_activo.getBot_aceptar().setMetodo("aceptarItemActivo");
        //dia_item_activo.getBot_cancelar().setMetodo("cancelarDialogo");

        tex_item_activo.setId("tex_item_activo");

        gri_item_activo.setColumns(2);
        gri_item_activo.setStyle("width:" + (dia_item_activo.getAnchoPanel() - 5) + "px;height:" + dia_item_activo.getAltoPanel() + "px;overflow: auto;display: block;");
        gri_item_activo.getChildren().add(new Etiqueta("Item: "));
        gri_item_activo.getChildren().add(tex_item_activo);
        dia_item_activo.setDialogo(gri_item_activo);
        agregarComponente(dia_item_activo);

// ********************************************************************************
        dia_ingreso_activo.setId("dia_ingreso_activo");
        dia_ingreso_activo.setWidth("75%");
        dia_ingreso_activo.setHeight("60%");
        dia_ingreso_activo.setDynamic(false);
        dia_ingreso_activo.getBot_aceptar().setMetodo("aceptarIngresoActivo");
        dia_ingreso_activo.getBot_cancelar().setMetodo("cancelarDialogo");

        tab_ingreso_activo.setId("tab_ingreso_activo");
        tab_ingreso_activo.setSql("SELECT ide_acafi as tab_ide_acafi, "
                + "codigo_recu_acafi,"
                + "fecha_acafi,"
                + "nombre_acafi,"
                + "observacion_acafi,"
                + "ide_inmar,"
                + "modelo_acafi,"
                + "serie_acafi,"
                + "ide_acuba,"
                + "ide_geper,"
                + "ide_aceaf,"
                + "fecha_compra_acafi,"
                + "vida_util_acafi,"
                + "valor_compra_acafi,"
                + "valor_comercial_acafi,"
                + "recidual_acafi, "
                + "numero_factu_acafi "
                + "from act_activo_fijo where ide_acafi=-1");
        tab_ingreso_activo.setCampoPrimaria("tab_ide_acafi");
        tab_ingreso_activo.getColumna("tab_ide_acafi").setVisible(false);
        tab_ingreso_activo.getColumna("codigo_recu_acafi").setNombreVisual("ITEM");
        tab_ingreso_activo.getColumna("fecha_acafi").setNombreVisual("FECHA INVENTARIO");
        tab_ingreso_activo.getColumna("fecha_acafi").setValorDefecto(utilitario.getFechaActual());
        tab_ingreso_activo.getColumna("nombre_acafi").setNombreVisual("DESCRIPCION");
        tab_ingreso_activo.getColumna("observacion_acafi").setNombreVisual("DESCRIPCION 2");
        tab_ingreso_activo.getColumna("ide_inmar").setNombreVisual("MARCA");
        tab_ingreso_activo.getColumna("ide_inmar").setCombo("inv_marca", "ide_inmar", "nombre_invmar", "");
        tab_ingreso_activo.getColumna("modelo_acafi").setNombreVisual("MODELO");
        tab_ingreso_activo.getColumna("serie_acafi").setNombreVisual("SERIE");
        tab_ingreso_activo.getColumna("ide_acuba").setCombo("act_ubicacion_activo", "ide_acuba", "nombre_acuba", "");
        tab_ingreso_activo.getColumna("ide_acuba").setNombreVisual("UBICACION");
        tab_ingreso_activo.getColumna("ide_geper").setNombreVisual("CUSTODIO");
        tab_ingreso_activo.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "");
        tab_ingreso_activo.getColumna("ide_geper").setAutoCompletar();
        tab_ingreso_activo.getColumna("ide_aceaf").setNombreVisual("ESTADO");
        tab_ingreso_activo.getColumna("ide_aceaf").setCombo("act_estado_activo_fijo", "ide_aceaf", "nombre_aceaf", "ide_aceaf!=4");
        tab_ingreso_activo.getColumna("fecha_compra_acafi").setNombreVisual("FECHA COMPRA");
        tab_ingreso_activo.getColumna("vida_util_acafi").setNombreVisual("VIDA UTIL TECNICA");
        tab_ingreso_activo.getColumna("valor_compra_acafi").setNombreVisual("VALOR A NUEVO");
        tab_ingreso_activo.getColumna("valor_compra_acafi").setLectura(false);
        tab_ingreso_activo.getColumna("valor_comercial_acafi").setNombreVisual("VALOR COMERCIAL");
        tab_ingreso_activo.getColumna("recidual_acafi").setNombreVisual("VALOR REMANENTE");
        tab_ingreso_activo.getColumna("numero_factu_acafi").setVisible(false);
        tab_ingreso_activo.setNumeroTabla(8);
        tab_ingreso_activo.setTipoFormulario(true);
        tab_ingreso_activo.getGrid().setColumns(4);
        tab_ingreso_activo.dibujar();

        Grid gri_ingreso_activo = new Grid();
        gri_ingreso_activo.setStyle("width:" + (dia_ingreso_activo.getAnchoPanel() - 5) + "px;height:" + dia_ingreso_activo.getAltoPanel() + "px;overflow: auto;display: block;");
        gri_ingreso_activo.getChildren().add(tab_ingreso_activo);
        dia_ingreso_activo.setDialogo(gri_ingreso_activo);
        agregarComponente(dia_ingreso_activo);

// ************* PARA EL VISUALIZADOR DE COMPROBANTE DE CONTABILIDAD
        via_comprobante_conta.setId("via_comprobante_conta");
        via_comprobante_conta.getBot_aceptar().setMetodo("aceptarComprobanteContabilidad");
        via_comprobante_conta.getBot_cancelar().setMetodo("cancelarDialogo");
        via_comprobante_conta.setDynamic(false);

        agregarComponente(via_comprobante_conta);
        vp.setId("vp");
        agregarComponente(vp);

        //        cargarArticulosenActivos();
        //crearMarcar();
        //crearActivo();
        //llenarTablaActivosAisgnados();
        //llenarSerieModelo();
        //llenaractUbi();
        //llenarAniosUso();
        //llenarconesa();
        //ingresoActivos();
    }

    public void ingresoActivos() {
        TablaGenerica tab_activos_fijos = utilitario.consultar("select * from act_activo_fijo order by ide_acafi ASC");
        Tabla tab_act_transaccion = new Tabla();
        tab_act_transaccion.setTabla("act_transaccion", "ide_actra", -1);
        tab_act_transaccion.ejecutarSql();
        for (int i = 0; i < tab_activos_fijos.getTotalFilas(); i++) {
            tab_act_transaccion.insertar();
            tab_act_transaccion.setValor("ide_empr", utilitario.getVariable("ide_empr"));
            tab_act_transaccion.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
            tab_act_transaccion.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_act_transaccion.setValor("ide_acafi", tab_activos_fijos.getValor(i, "ide_acafi"));
            tab_act_transaccion.setValor("ide_acttr", "2");
            tab_act_transaccion.setValor("fecha_actra", tab_activos_fijos.getValor(i, "fecha_compra_acafi"));
            tab_act_transaccion.setValor("acumulado_actra", "0");
            tab_act_transaccion.setValor("valor_activo_actra", tab_activos_fijos.getValor(i, "valor_compra_acafi"));
            tab_act_transaccion.setValor("valor_actra", tab_activos_fijos.getValor(i, "valor_compra_acafi"));
            tab_act_transaccion.setValor("observacion_actra", "transaccion de ingreso del activo al sistema");
        }
        tab_act_transaccion.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    public boolean validarIngresoActivo() {
        if (tab_ingreso_activo.getValor("fecha_acafi") == null || tab_ingreso_activo.getValor("fecha_acafi").isEmpty()) {
            utilitario.agregarMensajeError("Atencion", "Debe ingresar la fecha de inventario");
            return false;
        }

        if (tab_ingreso_activo.getValor("nombre_acafi") == null || tab_ingreso_activo.getValor("nombre_acafi").isEmpty()) {
            utilitario.agregarMensajeError("Atencion", "Debe ingresar la descripcion del Activo");
            return false;
        }
        if (tab_ingreso_activo.getValor("ide_acuba") == null || tab_ingreso_activo.getValor("ide_acuba").isEmpty()) {
            utilitario.agregarMensajeError("Atencion", "Debe ingresar la ubicacion del activo");
            return false;
        }

        if (tab_ingreso_activo.getValor("fecha_compra_acafi") == null || tab_ingreso_activo.getValor("fecha_compra_acafi").isEmpty()) {
            utilitario.agregarMensajeError("Atencion", "Debe ingresar la fecha de compra");
            return false;
        }

        if (tab_ingreso_activo.getValor("vida_util_acafi") == null || tab_ingreso_activo.getValor("vida_util_acafi").isEmpty()) {
            utilitario.agregarMensajeError("Atencion", "Debe ingresar la vida util tecnica");
            return false;
        }

        if (tab_ingreso_activo.getValor("valor_compra_acafi") == null || tab_ingreso_activo.getValor("valor_compra_acafi").isEmpty()) {
            utilitario.agregarMensajeError("Atencion", "Debe ingresar el valor a nuevo");
            return false;
        }

        return true;
    }

    public void aceptarItemActivo() {
        if (tex_item_activo.getValue() != null && !tex_item_activo.toString().isEmpty()) {
            List lis_items = new ArrayList();
            lis_items.add(tab_ingreso_activo.getValor("codigo_recu_acafi") + "");
            for (int i = 0; i < gri_item_activo.getChildren().size(); i++) {
                try {
                    String tex_item = ((Texto) (gri_item_activo.getChildren().get(i))).getValue() + "";
                    lis_items.add(tex_item);
                } catch (Exception e) {
                }
            }
            List lis_ide_acafi = new ArrayList();
            cls_activo_fijo.generarIngresoActivo(tab_ingreso_activo, str_ide_inarti, lis_items);
            for (int i = 0; i < cls_activo_fijo.getTab_act_activo_fijo().getTotalFilas(); i++) {
                lis_ide_acafi.add(cls_activo_fijo.getTab_act_activo_fijo().getValor(i, "ide_acafi") + "");
            }
            cls_activo_fijo.generarTransaccionActivo("2", str_ide_cnccc_compra_activo, lis_ide_acafi, 0, Double.parseDouble(tab_ingreso_activo.getValor("valor_compra_acafi")), Double.parseDouble(tab_ingreso_activo.getValor("valor_compra_acafi")), "ingreso del activo con fact num: " + tab_ingreso_activo.getValor("numero_factu_acafi"));
            if (tab_ingreso_activo.getValor("ide_geper") != null && !tab_ingreso_activo.getValor("ide_geper").isEmpty()) {
                cls_activo_fijo.generarAsignacionActivo(cls_activo_fijo.getTab_act_activo_fijo().getValor("ide_acafi"), tab_ingreso_activo.getValor("ide_geper"), "Asignacion del activo", lis_items);
            }
            dia_item_activo.cerrar();
            utilitario.getConexion().guardarPantalla();
            tab_tabla1.setCondicion("ide_inarti in (" + str_ide_inarti + ")");
            tab_tabla1.setFilaActual(cls_activo_fijo.getTab_act_activo_fijo().getValor("ide_acafi"));
            tab_tabla1.ejecutarSql();
            tab_tabla1.setCondicion("ide_inarti in (" + str_ide_inarti + ")");
            tab_tabla3.setFilaActual(cls_activo_fijo.getTab_act_activo_fijo().getValor("ide_acafi"));
            tab_tabla3.ejecutarSql();
            tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                    + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                    + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                    + "where aa.ide_acafi=" + tab_tabla1.getValor("ide_acafi") + " "
                    + "order by aa.fecha_acaaf DESC");
            tab_historial_asignacion.ejecutarSql();
            utilitario.addUpdate("tab_tabla1,tab_tabla3,tab_tabla2,tab_historial_asignacion");

        }
    }

    public void aceptarIngresoActivo() {
        if (validarIngresoActivo()) {
            dia_ingreso_activo.cerrar();
            if (Double.parseDouble(str_cantidad_activos) > 1) {
                gri_item_activo.getChildren().clear();
                for (int i = 0; i < Double.parseDouble(str_cantidad_activos) - 1; i++) {
                    gri_item_activo.getChildren().add(new Etiqueta("Item:" + (i + 2)));
                    tex_item_activo = new Texto();
                    gri_item_activo.getChildren().add(tex_item_activo);
                }
                dia_item_activo.dibujar();
            } else {
                cls_activo_fijo.generarIngresoActivo(tab_ingreso_activo, str_ide_inarti);
                cls_activo_fijo.generarTransaccionActivo("2", str_ide_cnccc_compra_activo, cls_activo_fijo.getTab_act_activo_fijo().getValor("ide_acafi"), 0, Double.parseDouble(tab_ingreso_activo.getValor("valor_compra_acafi")), Double.parseDouble(tab_ingreso_activo.getValor("valor_compra_acafi")), "ingreso del activo con fact num: " + tab_ingreso_activo.getValor("numero_factu_acafi"));
                if (tab_ingreso_activo.getValor("ide_geper") != null && !tab_ingreso_activo.getValor("ide_geper").isEmpty()) {
                    cls_activo_fijo.generarAsignacionActivo(cls_activo_fijo.getTab_act_activo_fijo().getValor("ide_acafi"), tab_ingreso_activo.getValor("ide_geper"), "Asignacion del activo");
                }
                utilitario.getConexion().guardarPantalla();
                tab_tabla1.setCondicion("ide_inarti in (" + str_ide_inarti + ")");
                tab_tabla1.setFilaActual(cls_activo_fijo.getTab_act_activo_fijo().getValor("ide_acafi"));
                tab_tabla1.ejecutarSql();
                tab_tabla1.setCondicion("ide_inarti in (" + str_ide_inarti + ")");
                tab_tabla3.setFilaActual(cls_activo_fijo.getTab_act_activo_fijo().getValor("ide_acafi"));
                tab_tabla3.ejecutarSql();
                tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                        + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                        + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                        + "where aa.ide_acafi=" + tab_tabla1.getValor("ide_acafi") + " "
                        + "order by aa.fecha_acaaf DESC");
                tab_historial_asignacion.ejecutarSql();
                utilitario.addUpdate("tab_tabla1,tab_tabla3,tab_tabla2,tab_historial_asignacion");
            }
        }
    }
    String str_ide_cnccc_compra_activo;
    String str_cantidad_activos = "";

    public void aceptarNumeroFactura() {
        if (rad_tipo_ingreso.getValue().equals("1")) {// tipo donacion
            str_cantidad_activos = "1";

            tab_ingreso_activo.insertar();
            str_ide_cnccc_compra_activo = null;
//            if (Double.parseDouble(str_cantidad_activos) > 1) {
//                double valor_compra = Double.parseDouble(tab_cxp_cab_factura.getValor(0, "total_cpcfa")) / Double.parseDouble(str_cantidad_activos);
//                tab_ingreso_activo.setValor("valor_compra_acafi", utilitario.getFormatoNumero(valor_compra, 4));
//            } else {
//                tab_ingreso_activo.setValor("valor_compra_acafi", tab_cxp_cab_factura.getValor(0, "total_cpcfa"));
//            }
            dia_ingreso_activo.setTitle("INGRESO DE ACTIVOS");
            dia_ingreso_activo.dibujar();
        } else {
            if (aut_num_factura.getValue() != null) {
                if (!aut_num_factura.getValor().isEmpty()) {
                    TablaGenerica tab_cxp_cab_factura = utilitario.consultar("select * from cxp_cabece_factur where ide_cpcfa=" + aut_num_factura.getValor());
                    TablaGenerica tab_cxp_det_factura = utilitario.consultar("select * from cxp_detall_factur where ide_cpcfa=" + aut_num_factura.getValor());
                    if (tab_cxp_det_factura.getTotalFilas() > 0) {
                        str_cantidad_activos = tab_cxp_det_factura.getValor(0, "cantidad_cpdfa");
                    }
                    if (tab_cxp_cab_factura.getTotalFilas() > 0) {
                        dia_num_factura.cerrar();
                        tab_ingreso_activo.getColumna("valor_compra_acafi").setLectura(false);
                        tab_ingreso_activo.insertar();
                        str_ide_cnccc_compra_activo = tab_cxp_cab_factura.getValor(0, "ide_cnccc");
                        if (Double.parseDouble(str_cantidad_activos) > 1) {
                            double valor_compra = Double.parseDouble(tab_cxp_cab_factura.getValor(0, "total_cpcfa")) / Double.parseDouble(str_cantidad_activos);
                            tab_ingreso_activo.setValor("valor_compra_acafi", utilitario.getFormatoNumero(valor_compra, 4));
                        } else {
                            tab_ingreso_activo.setValor("valor_compra_acafi", tab_cxp_cab_factura.getValor(0, "total_cpcfa"));
                        }
                        tab_ingreso_activo.setValor("fecha_compra_acafi", tab_cxp_cab_factura.getValor(0, "fecha_emisi_cpcfa"));
                        tab_ingreso_activo.setValor("numero_factu_acafi", tab_cxp_cab_factura.getValor(0, "numero_cpcfa"));
                        dia_ingreso_activo.setTitle("INGRESO DE ACTIVOS");
                        dia_ingreso_activo.dibujar();
                    } else {
                        utilitario.agregarMensajeError("Atencion, El numero de factura ingresado no existe en el sistema", "Favor ingresar la Factura de compra de activo en el modulo Cuentas x Pagar");
                    }
                } else {
                    utilitario.agregarMensajeError("Atencion", "Debe ingresar el numero de factura");
                }
            } else {
                utilitario.agregarMensajeError("Atencion", "Debe ingresar el numero de factura");
            }
        }
    }

    public boolean validarDarBaja() {
        if (tab_act_tran.getValor("valor_actra") == null || tab_act_tran.getValor("valor_actra").isEmpty()) {
            utilitario.agregarMensajeError("Atencion", "Debe ingresar el valor de la transaccion");
            return false;
        }
        if (tab_act_tran.getValor("observacion_actra") == null || tab_act_tran.getValor("observacion_actra").isEmpty()) {
            utilitario.agregarMensajeError("Atencion", "Debe ingresar la observacion para dar la baja");
            return false;
        }
        return true;
    }

    public void generarAsientoDarBaja() {

        conta.limpiar();
        cab_com_con = new cls_cab_comp_cont("0", p_estado_comprobante_normal, "", "", utilitario.getFechaActual(), "");
        lista_detalles.clear();
        if (Double.parseDouble(tab_act_tran.getValor("valor_actra")) != 0) {
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), "", Double.parseDouble(utilitario.getFormatoNumero(tab_act_tran.getValor("valor_actra"), 2)), ""));
        }
        cab_com_con.setDetalles(lista_detalles);
        via_comprobante_conta.setVistaAsiento(cab_com_con);
        via_comprobante_conta.dibujar();
        utilitario.addUpdate("via_comprobante_conta");
    }

    public synchronized void aceptarComprobanteContabilidad() {
        if (via_comprobante_conta.validarComprobante()) {
            cab_com_con.setObservacion_cnccc(via_comprobante_conta.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"));
            cab_com_con.setIde_geper(via_comprobante_conta.getTab_cab_comp_cont_vasiento().getValor("ide_geper"));
            lista_detalles.clear();
            for (int i = 0; i < via_comprobante_conta.getTab_det_comp_cont_vasiento().getTotalFilas(); i++) {
                lista_detalles.add(new cls_det_comp_cont(via_comprobante_conta.getTab_det_comp_cont_vasiento().getValor(i, "ide_cnlap"), via_comprobante_conta.getTab_det_comp_cont_vasiento().getValor(i, "ide_cndpc"), Double.parseDouble(via_comprobante_conta.getTab_det_comp_cont_vasiento().getValor(i, "valor_cndcc")), via_comprobante_conta.getTab_det_comp_cont_vasiento().getValor(i, "observacion_cndcc")));
            }
            cab_com_con.setDetalles(lista_detalles);
            conta.generarAsientoContable(cab_com_con);
            String ide_cnccc = conta.getTab_cabecera().getValor("ide_cnccc");
            if (ide_cnccc != null) {
                cls_activo_fijo.generarTransaccionActivo("1", ide_cnccc, tab_tabla1.getValor("ide_acafi"), 0, Double.parseDouble(tab_act_tran.getValor("valor_actra")), Double.parseDouble(tab_act_tran.getValor("valor_actra")), tab_act_tran.getValor("observacion_actra"));
                utilitario.getConexion().agregarSqlPantalla("update act_activo_fijo set ide_aceaf=4 where ide_acafi=" + tab_tabla1.getValor("ide_acafi"));
                via_comprobante_conta.cerrar();
                utilitario.getConexion().guardarPantalla();
                tab_tabla1.setCondicion("ide_inarti in (" + str_ide_inarti + ")");
                tab_tabla1.setFilaActual(tab_tabla1.getValor("ide_acafi"));
                tab_tabla1.ejecutarSql();
                tab_tabla1.setCondicion("ide_inarti in (" + str_ide_inarti + ")");
                tab_tabla3.setFilaActual(tab_tabla1.getValor("ide_acafi"));
                tab_tabla3.ejecutarSql();
                tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                        + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                        + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                        + "where aa.ide_acafi=" + tab_tabla1.getValor("ide_acafi") + " "
                        + "order by aa.fecha_acaaf DESC");
                tab_historial_asignacion.ejecutarSql();
                utilitario.addUpdate("tab_tabla1,tab_tabla3,tab_tabla2,tab_historial_asignacion");
            }
        }
    }

    public void aceptarDarBajaActivo() {

        if (validarDarBaja()) {
            dia_dar_baja.cerrar();
            generarAsientoDarBaja();
        }
    }

    public void dardeBajaActivo() {
        if (tab_tabla1.getValor("ide_acafi") != null && !tab_tabla1.getValor("ide_acafi").isEmpty()) {
            if (!tab_tabla1.getValor("ide_aceaf").equals(utilitario.getVariable("p_act_estado_dado_de_baja"))) {
                if (tab_tabla2.getTotalFilas() > 0) {
                    tab_activo_dar_baja.setSql("select af.ide_acafi as baja_ide_acafi,nombre_acuba as ubicacion,nombre_acafi as descripcion,serie_acafi as serie,modelo_acafi as modelo,nom_geper as custodio_actual from act_activo_fijo af "
                            + "left join gen_persona per on per.ide_geper = af.ide_geper "
                            + "left join inv_articulo arti on arti.ide_inarti = af.ide_inarti "
                            + "left join inv_articulo arti1 on arti.inv_ide_inarti = arti1.ide_inarti "
                            + "left join act_ubicacion_activo acubi on acubi.ide_acuba=af.ide_acuba "
                            + "where ide_acafi =" + tab_tabla1.getValor("ide_acafi") + "");
                    tab_activo_dar_baja.ejecutarSql();
                    tab_act_tran.insertar();
                    if (tab_tabla2.getValor(0, "valor_activo_actra") != null && !tab_tabla2.getValor(0, "valor_activo_actra").isEmpty()) {
                        tab_act_tran.setValor("valor_actra", tab_tabla2.getValor(0, "valor_activo_actra"));
                    }
                    dia_dar_baja.dibujar();
                } else {
                    utilitario.agregarMensajeInfo("Atencion", "el activo no tiene transacciones de ingreso ni de depreciacion");
                }
            } else {
                utilitario.agregarMensajeInfo("Atencion", "el activo seleccionado ya esta dado de baja");
            }
        }
    }

//    public void llenarconesa() {
//        Tabla tab_activos = utilitario.consultar("select * from activos "
//                + "order by item");
//        for (int i = 0; i < tab_activos.getTotalFilas(); i++) {
//            utilitario.getConexion().agregarSqlPantalla("update act_activo_fijo set valor_comercial_acafi= " + tab_activos.getValor(i, "valor_comercial") + ",valor_compra_acafi=" + tab_activos.getValor(i, "valor_nuevo") + ",recidual_acafi=" + tab_activos.getValor(i, "valor_remate") + " where codigo_recu_acafi like '" + tab_activos.getValor(i, "item") + "'");
//        }
//        utilitario.getConexion().guardarPantalla();
//    }
    public void cancelarDialogo() {
        if (dia_asignacion_activo.isVisible()) {
            dia_asignacion_activo.cerrar();
        }
        if (dia_dar_baja.isVisible()) {
            dia_dar_baja.cerrar();
        }
        if (dia_num_factura.isVisible()) {
            dia_num_factura.cerrar();
        }
        if (dia_ingreso_activo.isVisible()) {
            dia_ingreso_activo.cerrar();
        }
        if (via_comprobante_conta.isVisible()) {
            via_comprobante_conta.cerrar();
        }
        if (dia_tipo_ingreso_activo.isVisible()) {
            dia_tipo_ingreso_activo.cerrar();
        }

        //utilitario.getConexion().rollback();*********
        utilitario.getConexion().getSqlPantalla().clear();

    }

    public void aceptarAsignacion() {
        if (tab_act_asignacion_activo.getValor("ide_geper") != null && !tab_act_asignacion_activo.getValor("ide_geper").isEmpty()) {
            if (tab_act_asignacion_activo.getValor("observacion_acaaf") != null && !tab_act_asignacion_activo.getValor("observacion_acaaf").isEmpty()) {
                dia_asignacion_activo.cerrar();
                utilitario.getConexion().agregarSqlPantalla("update act_activo_fijo set ide_geper=" + tab_act_asignacion_activo.getValor("ide_geper") + " where ide_acafi=" + tab_act_asignacion_activo.getValor("ide_acafi"));
                tab_act_asignacion_activo.guardar();
                utilitario.getConexion().guardarPantalla();
                tab_tabla1.setFilaActual(str_ide_acafi_reasignado);
                tab_tabla1.ejecutarSql();
                tab_tabla3.setFilaActual(str_ide_acafi_reasignado);
                tab_tabla3.ejecutarSql();
                tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                        + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                        + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                        + "where aa.ide_acafi=" + str_ide_acafi_reasignado + " "
                        + "order by aa.fecha_acaaf DESC");
                tab_historial_asignacion.ejecutarSql();
                utilitario.addUpdate("tab_tabla1,tab_tabla3,tab_tabla2,tab_historial_asignacion");
            } else {
                utilitario.agregarMensajeError("Atencion", "La observacion de la reaignacion es obligatorio");
            }
        } else {
            utilitario.agregarMensajeError("Atencion", "No ha seleccionado un nuevo custodio");
        }

    }
    String str_ide_acafi_reasignado;

    public void reasignarActivo() {
        tab_datos_reasignacion.setSql("select af.ide_acafi as sql_ide_acafi,nom_geper as custodio_actual,nombre_acuba as ubicacion,nombre_acafi as descripcion,serie_acafi as serie,modelo_acafi as modelo from act_activo_fijo af "
                + "left join gen_persona per on per.ide_geper = af.ide_geper "
                + "left join inv_articulo arti on arti.ide_inarti = af.ide_inarti "
                + "left join inv_articulo arti1 on arti.inv_ide_inarti = arti1.ide_inarti "
                + "left join act_ubicacion_activo acubi on acubi.ide_acuba=af.ide_acuba "
                + "where ide_acafi =" + tab_tabla1.getValor("ide_acafi") + "");
        str_ide_acafi_reasignado = tab_tabla1.getValor("ide_acafi");
        tab_datos_reasignacion.ejecutarSql();
        tab_act_asignacion_activo.insertar();
        dia_asignacion_activo.setTitle("REASIGNACION DE ACTIVOS FIJOS");
        dia_asignacion_activo.dibujar();
    }

//    public void llenarAniosUso() {
//        Tabla tab_act_fijos = utilitario.consultar("select * from act_activo_fijo");
//        for (int i = 0; i < tab_act_fijos.getTotalFilas(); i++) {
//            calcularAniosUso(tab_act_fijos.getValor(i, "ide_acafi"));
//        }
//        utilitario.getConexion().guardarPantalla();
//    }
//
    public void llenaractUbi() {
        TablaGenerica tab_geor = utilitario.consultar("select * from act_activo_fijo");
        for (int i = 0; i < tab_geor.getTotalFilas(); i++) {
            if (tab_geor.getValor(i, "ide_georg") != null && !tab_geor.getValor(i, "ide_georg").isEmpty()) {
                utilitario.getConexion().agregarSqlPantalla("update act_activo_fijo set ide_acuba=" + tab_geor.getValor(i, "ide_georg") + " where ide_acafi=" + tab_geor.getValor(i, "ide_acafi"));
            }
        }
        utilitario.getConexion().guardarPantalla();
    }
//
//    public void llenarSerieModelo() {
//        Tabla tab_excel = utilitario.consultar("select * from activos");
//        Tabla tab_act_fijo = utilitario.consultar("select * from act_activo_fijo");
//        String item;
//        for (int i = 0; i < tab_excel.getTotalFilas(); i++) {
//            item = tab_excel.getValor(i, "item");
//            for (int j = 0; j < tab_act_fijo.getTotalFilas(); j++) {
//                if (tab_act_fijo.getValor(j, "codigo_recu_acafi").equals(item)) {
////                    utilitario.getConexion().agregarSqlPantalla("update act_activo_fijo set valor_comercial_acafi='"+tab_excel.getValor(i, "valor_comercial")+"', modelo_acafi='" + tab_excel.getValor(i, "modelo") + "',serie_acafi='" + tab_excel.getValor(i, "serie") + "' where ide_acafi=" + tab_act_fijo.getValor(j, "ide_acafi"));
//                    utilitario.getConexion().agregarSqlPantalla("update act_activo_fijo set valor_remate_acafi=" + tab_excel.getValor(i, "valor_remate") + " where ide_acafi=" + tab_act_fijo.getValor(j, "ide_acafi"));
//                    break;
//                }
//            }
//        }
//        utilitario.getConexion().guardarPantalla();
//    }
//

    public void llenarTablaActivosAisgnados() {
        TablaGenerica tab_act_fij = utilitario.consultar("select * from act_activo_fijo");
        Tabla act_asig = new Tabla();
        act_asig.setTabla("act_asignacion_activo", "ide_acaaf", -1);
        act_asig.ejecutarSql();
        for (int i = 0; i < tab_act_fij.getTotalFilas(); i++) {
            act_asig.insertar();
            act_asig.setValor("ide_acafi", tab_act_fij.getValor(i, "ide_acafi"));
            act_asig.setValor("ide_geper", tab_act_fij.getValor(i, "ide_geper"));
            act_asig.setValor("ide_empr", utilitario.getVariable("ide_empr"));
            act_asig.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
            act_asig.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            act_asig.setValor("fecha_acaaf", utilitario.getFechaActual());
            act_asig.setValor("observacion_acaaf", "Asignacion de equipos inicial");
        }
        act_asig.guardar();
        utilitario.getConexion().guardarPantalla();

    }

    public String buscarEstado(String nombre) {
        TablaGenerica tab_org = utilitario.consultar("select * from act_estado_activo_fijo");
        String ide_aceaf = "";
        for (int i = 0; i < tab_org.getTotalFilas(); i++) {
            if (nombre.equalsIgnoreCase(tab_org.getValor(i, "nombre_aceaf"))) {
                ide_aceaf = tab_org.getValor(i, "ide_aceaf") + "";
                break;
            }
        }
        if (ide_aceaf.isEmpty()) {
            return null;
        } else {
            return ide_aceaf;
        }
    }

    public String buscarMarca(String nombre) {
        TablaGenerica tab_marca = utilitario.consultar("select * from inv_marca");
        String ide_inmar = "";
        for (int i = 0; i < tab_marca.getTotalFilas(); i++) {
            if (nombre.equalsIgnoreCase(tab_marca.getValor(i, "nombre_invmar"))) {
                ide_inmar = tab_marca.getValor(i, "ide_inmar") + "";
                break;
            }
        }
        if (ide_inmar.isEmpty()) {
            return null;
        } else {
            return ide_inmar;
        }
    }

    public String buscarArticulo(String nombre) {
        TablaGenerica tab_articulo = utilitario.consultar("select *from inv_articulo");
        String ide_inarti = "";
        for (int i = 0; i < tab_articulo.getTotalFilas(); i++) {
            if (nombre.indexOf(" ") != -1) {
                nombre = nombre.replace(" ", "");
            }
            if (nombre.indexOf("  ") != -1) {
                nombre = nombre.replace("  ", "");
            }
            if (nombre.equalsIgnoreCase(tab_articulo.getValor(i, "nombre_inarti"))) {
                ide_inarti = tab_articulo.getValor(i, "ide_inarti") + "";
                break;
            }
        }
        if (ide_inarti.isEmpty()) {
            return null;
        } else {
            return ide_inarti;
        }
    }

    public String buscarIdeGeper(String custodio) {
        String nom1 = "";
        String nom2 = "";
        String nom3 = "";
        String nom4 = "";
        if (custodio.indexOf(" ") != -1) {
            nom1 = custodio.substring(0, custodio.indexOf(" "));
            custodio = custodio.substring(custodio.indexOf(" ") + 1, custodio.length());
            if (custodio.indexOf(" ") != -1) {
                nom2 = custodio.substring(0, custodio.indexOf(" "));
                custodio = custodio.substring(custodio.indexOf(" ") + 1, custodio.length());
                if (custodio.indexOf(" ") != -1) {
                    nom3 = custodio.substring(0, custodio.indexOf(" "));
                    custodio = custodio.substring(custodio.indexOf(" ") + 1, custodio.length());
                    if (custodio.indexOf(" ") != -1) {
                        nom4 = custodio.substring(0, custodio.indexOf(" "));
                    } else {
                        nom4 = custodio;
                    }
                } else {
                    nom3 = custodio;
                }
            } else {
                nom2 = custodio;
            }
        } else {
            nom1 = custodio;
        }
        String sql = "";
        if (!nom1.isEmpty() && !nom2.isEmpty() && !nom3.isEmpty() && !nom4.isEmpty()) {
            sql = "select * from gen_persona where upper(nom_geper) like '%" + nom1 + "%' AND upper(nom_geper) like '%" + nom2 + "%' AND upper(nom_geper) like '%" + nom3 + "%' AND upper(nom_geper) like '%" + nom4 + "%'";
        }
        if (!nom1.isEmpty() && !nom2.isEmpty() && !nom3.isEmpty()) {
            sql = "select * from gen_persona where upper(nom_geper) like '%" + nom1 + "%' AND upper(nom_geper) like '%" + nom2 + "%' AND upper(nom_geper) like '%" + nom3 + "%'";
        }
        if (!nom1.isEmpty() && !nom2.isEmpty()) {
            sql = "select * from gen_persona where upper(nom_geper) like '%" + nom1 + "%' AND upper(nom_geper) like '%" + nom2 + "%'";
        }
        if (!nom1.isEmpty()) {
            sql = "select * from gen_persona where upper(nom_geper) like '%" + nom1 + "%'";
        }

        TablaGenerica gen_per = utilitario.consultar(sql);
        if (gen_per.getTotalFilas() > 0) {
            return gen_per.getValor(0, "ide_geper");
        } else {
            return null;
        }
    }

    public void crearActivo() {
        Tabla tab_activo = new Tabla();
        tab_activo.setId("tab_activo");
        tab_activo.setTabla("act_activo_fijo", "ide_acafi", -1);
        tab_activo.setCondicion("ide_acafi=-1");
        tab_activo.ejecutarSql();

//        Tabla tab_prueba = utilitario.consultar("SELECT * from pruebas_activos where cuenta_contable like 'VEHICULO%'");
        TablaGenerica tab_prueba = utilitario.consultar("SELECT * from pruebas_activos");
        for (int i = 0; i < tab_prueba.getTotalFilas(); i++) {
            tab_activo.insertar();
            tab_activo.setValor("ide_geper", buscarIdeGeper(tab_prueba.getValor(i, "custodio")));
            tab_activo.setValor("ide_aceaf", buscarEstado(tab_prueba.getValor(i, "estado")));
            tab_activo.setValor("ide_inarti", buscarArticulo(tab_prueba.getValor(i, "descripcion")));
            tab_activo.setValor("nombre_acafi", tab_prueba.getValor(i, "descripcion"));
            tab_activo.setValor("cantidad_acafi", "1");
            tab_activo.setValor("fecha_acafi", tab_prueba.getValor(i, "fecha_inventario"));
            tab_activo.setValor("vida_util_acafi", tab_prueba.getValor(i, "vida_util_tecnica"));
            tab_activo.setValor("valor_compra_acafi", tab_prueba.getValor(i, "valor_a_nuevo"));
            tab_activo.setValor("recidual_acafi", tab_prueba.getValor(i, "valor_remanence"));
            tab_activo.setValor("serie_acafi", tab_prueba.getValor(i, "serie"));
            tab_activo.setValor("observacion_acafi", tab_prueba.getValor(i, "descripcion2"));
            tab_activo.setValor("codigo_recu_acafi", tab_prueba.getValor(i, "ITEM"));
            tab_activo.setValor("fecha_compra_acafi", tab_prueba.getValor(i, "fecha_de_compra"));
            tab_activo.setValor("anos_uso_acafi", tab_prueba.getValor(i, "anios_de_uso"));
            tab_activo.setValor("modelo_acafi", tab_prueba.getValor(i, "modelo"));
            tab_activo.setValor("ide_inmar", buscarMarca(tab_prueba.getValor(i, "marca")));

        }

        tab_activo.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    public void crearMarcar() {
        Tabla tab_marcas = new Tabla();
        tab_marcas.setId("tab_marcas");
        tab_marcas.setTabla("inv_marca", "ide_inmar", -1);
        tab_marcas.setCondicion("ide_inmar=-1");
        tab_marcas.ejecutarSql();
        TablaGenerica tab_marcas_p = utilitario.consultar("select count(marca),marca from pruebas_activos group by marca");
        for (int i = 0; i < tab_marcas_p.getTotalFilas(); i++) {
            System.out.println("vv " + tab_marcas_p.getValor(i, "marca"));
            if (tab_marcas_p.getValor(i, "marca") != null && !tab_marcas_p.getValor(i, "marca").isEmpty()) {
                tab_marcas.insertar();
                tab_marcas.setValor("nombre_invmar", tab_marcas_p.getValor(i, "marca"));
            }
        }
        tab_marcas.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra 
        rep_reporte.dibujar();

    }

    Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Activos Fijos")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sel_arb.dibujar();
                utilitario.addUpdate("rep_reporte,sel_arb");
            } else if (sel_arb.isVisible()) {
                System.out.println("ides " + sel_arb.getSeleccionados());
                parametro.put("ide_inarti", sel_arb.getSeleccionados());
                sel_arb.cerrar();
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("sel_arb,sel_rep");
            }

        } else if (rep_reporte.getReporteSelecionado().equals("Depreciacion Activos Fijos")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(false);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                parametro.put("fecha_fin", sec_rango_reporte.getFecha1());
                sec_rango_reporte.cerrar();
                sel_arb.dibujar();
                utilitario.addUpdate("rep_reporte,sel_arb");
            } else if (sel_arb.isVisible()) {
                System.out.println("ides " + sel_arb.getSeleccionados());
                parametro.put("ide_inarti", sel_arb.getSeleccionados());
                sel_arb.cerrar();
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("sel_arb,sel_rep");
            }

        } else if (rep_reporte.getReporteSelecionado().equals("Detalle Depreciacion Activos")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                parametro.put("fecha_ini", sec_rango_reporte.getFecha1());
                parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
                sec_rango_reporte.cerrar();
                sel_arb.dibujar();
                utilitario.addUpdate("rep_reporte,sel_arb");
            } else if (sel_arb.isVisible()) {
                System.out.println("ides " + sel_arb.getSeleccionados());
                parametro.put("ide_inarti", sel_arb.getSeleccionados());
                sel_arb.cerrar();
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("sel_arb,sel_rep");
            }

        }

    }

//    public void cargarArticulosenActivos() {
//        Tabla tab_consulta = utilitario.consultar("select arti.ide_inarti,af.ide_acafi from act_activo_fijo af,inv_articulo arti "
//                + "where af.nombre_acafi = arti.nombre_inarti");
//        for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {
//            utilitario.getConexion().agregarSqlPantalla("update act_activo_fijo set ide_inarti=" + tab_consulta.getValor(i, "ide_inarti") + " where ide_acafi=" + tab_consulta.getValor(i, "ide_acafi"));
//        }
//        utilitario.getConexion().guardarPantalla();
//
//    }
    public void siguiente() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.siguiente();
        }
        if (!tab_tabla1.isFocus()) {
            tab_tabla1.siguiente();
            tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                    + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                    + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                    + "where aa.ide_acafi=" + tab_tabla1.getValor("ide_acafi") + " "
                    + "order by aa.fecha_acaaf DESC");
            tab_historial_asignacion.ejecutarSql();
            utilitario.addUpdate("tab_historial_asignacion");
        }
        if (!tab_tabla3.isFocus()) {
            tab_tabla3.siguiente();
            tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                    + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                    + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                    + "where aa.ide_acafi=" + tab_tabla1.getValor("ide_acafi") + " "
                    + "order by aa.fecha_acaaf DESC");
            tab_historial_asignacion.ejecutarSql();
            utilitario.addUpdate("tab_historial_asignacion");
        }
    }

    public void atras() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.atras();
        }
        if (!tab_tabla1.isFocus()) {
            tab_tabla1.atras();
            tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                    + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                    + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                    + "where aa.ide_acafi=" + tab_tabla1.getValor("ide_acafi") + " "
                    + "order by aa.fecha_acaaf DESC");
            tab_historial_asignacion.ejecutarSql();
            utilitario.addUpdate("tab_historial_asignacion");
        }
        if (!tab_tabla3.isFocus()) {
            tab_tabla3.atras();
            tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                    + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                    + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                    + "where aa.ide_acafi=" + tab_tabla1.getValor("ide_acafi") + " "
                    + "order by aa.fecha_acaaf DESC");
            tab_historial_asignacion.ejecutarSql();
            utilitario.addUpdate("tab_historial_asignacion");
        }
    }

    public void fin() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.fin();
        }
        if (!tab_tabla1.isFocus()) {
            tab_tabla1.fin();
            tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                    + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                    + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                    + "where aa.ide_acafi=" + tab_tabla1.getValor("ide_acafi") + " "
                    + "order by aa.fecha_acaaf DESC");
            tab_historial_asignacion.ejecutarSql();
            utilitario.addUpdate("tab_historial_asignacion");
        }
        if (!tab_tabla3.isFocus()) {
            tab_tabla3.fin();
            tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                    + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                    + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                    + "where aa.ide_acafi=" + tab_tabla1.getValor("ide_acafi") + " "
                    + "order by aa.fecha_acaaf DESC");
            tab_historial_asignacion.ejecutarSql();
            utilitario.addUpdate("tab_historial_asignacion");
        }
    }

    public void inicio() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.inicio();
        }
        if (!tab_tabla1.isFocus()) {
            tab_tabla1.inicio();
            tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                    + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                    + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                    + "where aa.ide_acafi=" + tab_tabla1.getValor("ide_acafi") + " "
                    + "order by aa.fecha_acaaf DESC");
            tab_historial_asignacion.ejecutarSql();
            utilitario.addUpdate("tab_historial_asignacion");
        }
        if (!tab_tabla3.isFocus()) {
            tab_tabla3.inicio();
            tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                    + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                    + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                    + "where aa.ide_acafi=" + tab_tabla1.getValor("ide_acafi") + " "
                    + "order by aa.fecha_acaaf DESC");
            tab_historial_asignacion.ejecutarSql();
            utilitario.addUpdate("tab_historial_asignacion");
        }

    }

//    public void depreciar(String fecha_comp, int vid_util, double valor_comp, double residual, String ide_acafi) {
//
//        if (fecha_comp != null && !fecha_comp.isEmpty()
//                && vid_util > 0
//                && valor_comp > 0
//                && residual >= 0
//                && ide_acafi != null && !ide_acafi.isEmpty()) {
//
//            List lis_depre_acu = new ArrayList();
//            List lis_periodo = new ArrayList();
//            List lis_valor_libros = new ArrayList();
//
//            int vida_util = vid_util;
//            double costo_activo = valor_comp;
//            String fecha_compra = fecha_comp;
//            int anio_compra = utilitario.getAnio(fecha_compra);
//            int mes_compra = utilitario.getMes(fecha_compra);
//            int anio_actual = utilitario.getAnio(utilitario.getFechaActual());
//            int mes_actual = utilitario.getMes(utilitario.getFechaActual());
//            String fecha_tope_depreciacion = utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(fecha_compra), vid_util * 365));
//            int diferencia_dias = utilitario.getDiferenciasDeFechas(utilitario.getFecha(utilitario.getFechaActual()), utilitario.getFecha(fecha_tope_depreciacion));
//
//            double dp;
//            double depre_acu;
//            double libro;
//            int num_meses_faltantes;
//
//            List lis_fecha_max = utilitario.getConexion().consultar("select max(fecha_actra) from act_transaccion where ide_acafi=" + ide_acafi);
//            if (lis_fecha_max.get(0) != null) {
//                System.out.println("si entra");
//
//                int mes_max_depreciado = utilitario.getMes(lis_fecha_max.get(0).toString());
//                if (mes_max_depreciado == mes_actual) {
////                    utilitario.agregarMensajeInfo("Activos Fijos", "El activo seleccionado ya tiene Depreciacion para este mes");
//                } else {
//                    mes_compra = mes_max_depreciado;
//                    if (diferencia_dias >= -31) {
//                        int anio_depreciacion = anio_actual - anio_compra;
//                        if (mes_compra < mes_actual) {
//                            anio_depreciacion = anio_depreciacion;
//                            num_meses_faltantes = mes_actual - mes_compra;
//                        } else {
//                            if (mes_compra == mes_actual) {
//                                anio_depreciacion = anio_depreciacion;
//                                num_meses_faltantes = 0;
//                            } else {
//                                num_meses_faltantes = (12 - mes_compra) + mes_actual;
//                                if (anio_depreciacion != 0) {
//                                    anio_depreciacion = anio_depreciacion - 1;
//                                }
//                            }
//                        }
//
//                        Tabla tab_depreciacion_activo = utilitario.consultar("select ide_acafi,MAX(acumulado_actra) as acumulado,min(valor_activo_actra) as valor_activo,valor_actra from act_transaccion "
//                                + "where ide_acafi=" + ide_acafi + " group by ide_acafi,valor_actra");
//
//                        double valor_depreciar = Double.parseDouble(utilitario.getFormatoNumero(tab_depreciacion_activo.getValor(0, "valor_actra")));
//                        double depre1 = Double.parseDouble(utilitario.getFormatoNumero(tab_depreciacion_activo.getValor(0, "acumulado"))) + valor_depreciar;
//                        double libro1 = Double.parseDouble(utilitario.getFormatoNumero(tab_depreciacion_activo.getValor(0, "valor_activo"))) - valor_depreciar;
//
//                        String fecha_actra;
//                        int mes_depreciado = mes_compra + 1;
//                        int anio_depreciado = anio_actual;
//                        int periodo = anio_depreciacion;
//
//                        for (int i = 0; i < num_meses_faltantes; i++) {
//                            tab_tabla2.insertar();
//                            fecha_actra = (anio_depreciado - 1) + "-" + mes_depreciado + "-" + utilitario.getDia(utilitario.getFechaActual());
//                            System.out.println("fecha: " + utilitario.getFormatoFecha(utilitario.getFecha(fecha_actra)));
//
//                            tab_tabla2.setValor("ide_acafi", ide_acafi);
//                            tab_tabla2.setValor("fecha_actra", utilitario.getFormatoFecha(utilitario.getFecha(fecha_actra)));
//                            tab_tabla2.setValor("acumulado_actra", utilitario.getFormatoNumero(depre1) + "");
//                            tab_tabla2.setValor("valor_actra", utilitario.getFormatoNumero(valor_depreciar) + "");
//                            tab_tabla2.setValor("valor_activo_actra", utilitario.getFormatoNumero(libro1) + "");
//                            depre1 = depre1 + valor_depreciar;
//                            libro1 = libro1 - valor_depreciar;
//                            if (mes_depreciado < 12) {
//                                tab_tabla2.setValor("observacion_actra", getMesenLetras(mes_depreciado) + " - " + (anio_depreciado - 1) + " del Periodo " + (periodo));
//                                mes_depreciado = mes_depreciado + 1;
//                            } else {
//                                tab_tabla2.setValor("observacion_actra", getMesenLetras(mes_depreciado) + " - " + (anio_depreciado - 1) + " del Periodo " + (periodo));
//                                mes_depreciado = 1;
//                                anio_depreciado = anio_depreciado + 1;
//                                periodo = periodo + 1;
//                            }
//                        }
//                    }
//
//                }
//            } else {
//                if (diferencia_dias >= -31) {
//                    int anio_depreciacion = anio_actual - anio_compra;
//                    if (residual > 0) {
//                        dp = (costo_activo - residual) / vida_util;
//                    } else {
//                        dp = costo_activo / vida_util;
//                    }
//                    double valor_depreciar = dp / 12;
//                    valor_depreciar = Double.parseDouble(utilitario.getFormatoNumero(valor_depreciar));
//                    if (mes_compra < mes_actual) {
//                        anio_depreciacion = anio_depreciacion;
//                        num_meses_faltantes = mes_actual - mes_compra;
//                    } else {
//                        if (mes_compra == mes_actual) {
//                            anio_depreciacion = anio_depreciacion;
//                            num_meses_faltantes = 0;
//                        } else {
//                            num_meses_faltantes = (12 - mes_compra) + mes_actual;
//                            if (anio_depreciacion != 0) {
//                                anio_depreciacion = anio_depreciacion - 1;
//                            }
//                        }
//                    }
//                    depre_acu = (valor_depreciar * 12) * anio_depreciacion;
//                    libro = costo_activo - depre_acu;
//                    lis_depre_acu.add(depre_acu);
//                    lis_valor_libros.add(libro);
////                for (int i = 0; i < anio_depreciacion; i++) {
////                    depre_acu = dp + depre_acu;
////                    libro = costo_activo - depre_acu;
////                    lis_depre_acu.add(depre_acu);
////                    lis_valor_libros.add(costo_activo - depre_acu);
////                }
//                    String fecha_actra;
//
//                    if (anio_depreciacion > 0) {
//                        tab_tabla2.insertar();
//                        fecha_actra = (anio_actual - 1) + "-" + mes_compra + "-" + utilitario.getDia(utilitario.getFechaActual());
//
//                        System.out.println("fecha 1: " + utilitario.getFormatoFecha(utilitario.getFecha(fecha_actra)));
//                        tab_tabla2.setValor("ide_acafi", ide_acafi);
//                        tab_tabla2.setValor("fecha_actra", utilitario.getFormatoFecha(utilitario.getFecha(fecha_actra)));
//                        tab_tabla2.setValor("acumulado_actra", utilitario.getFormatoNumero(lis_depre_acu.get(0)) + "");
//                        tab_tabla2.setValor("valor_actra", utilitario.getFormatoNumero(lis_depre_acu.get(0)) + "");
//                        tab_tabla2.setValor("valor_activo_actra", utilitario.getFormatoNumero(lis_valor_libros.get(0)) + "");
//                        tab_tabla2.setValor("observacion_actra", getMesenLetras(mes_compra) + " - " + utilitario.getAnio(fecha_compra) + " a " + getMesenLetras(mes_compra) + " - " + (anio_actual - 1));
//                    }
//
//                    double depre1 = depre_acu + valor_depreciar;
//                    double libro1 = libro - valor_depreciar;
//
//                    int mes_depreciado = mes_compra + 1;
//                    int anio_depreciado = anio_actual;
//                    int periodo = anio_depreciacion;
//                    for (int i = 0; i < num_meses_faltantes; i++) {
//                        tab_tabla2.insertar();
//                        fecha_actra = (anio_depreciado - 1) + "-" + mes_depreciado + "-" + utilitario.getDia(utilitario.getFechaActual());
//                        System.out.println("fecha: " + utilitario.getFormatoFecha(utilitario.getFecha(fecha_actra)));
//
//                        tab_tabla2.setValor("ide_acafi", ide_acafi);
//                        tab_tabla2.setValor("fecha_actra", utilitario.getFormatoFecha(utilitario.getFecha(fecha_actra)));
//                        tab_tabla2.setValor("acumulado_actra", utilitario.getFormatoNumero(depre1) + "");
//                        tab_tabla2.setValor("valor_actra", utilitario.getFormatoNumero(valor_depreciar) + "");
//                        tab_tabla2.setValor("valor_activo_actra", utilitario.getFormatoNumero(libro1) + "");
//                        depre1 = depre1 + valor_depreciar;
//                        libro1 = libro1 - valor_depreciar;
//                        if (mes_depreciado < 12) {
//                            tab_tabla2.setValor("observacion_actra", getMesenLetras(mes_depreciado) + " - " + (anio_depreciado - 1) + " del Periodo " + (periodo));
//                            mes_depreciado = mes_depreciado + 1;
//                        } else {
//                            tab_tabla2.setValor("observacion_actra", getMesenLetras(mes_depreciado) + " - " + (anio_depreciado - 1) + " del Periodo " + (periodo));
//                            mes_depreciado = 1;
//                            anio_depreciado = anio_depreciado + 1;
//                            periodo = periodo + 1;
//                        }
//                    }
//                }
//            }
//        }
//
//    }
    public void depreciarActivo() {
        if (tab_tabla1.getTotalFilas() > 0 && tab_tabla3.getTotalFilas() > 0) {
            List list_ides = utilitario.getConexion().consultar("select ide_inarti from inv_articulo where inv_ide_inarti in (" + arb_arbol.getValorSeleccionado() + ")");
            TablaGenerica tab_activos_a_depreciar;
            if (list_ides.size() > 0) {
                tab_activos_a_depreciar = utilitario.consultar("select * from act_activo_fijo where ide_inarti in ( "
                        + "select ide_inarti from inv_articulo where inv_ide_inarti in (" + arb_arbol.getValorSeleccionado() + ")) and ide_aceaf!=" + utilitario.getVariable("p_act_estado_dado_de_baja"));
            } else {
                tab_activos_a_depreciar = utilitario.consultar("select * from act_activo_fijo where ide_inarti in (" + arb_arbol.getValorSeleccionado() + ") and ide_aceaf!=" + utilitario.getVariable("p_act_estado_dado_de_baja"));
            }

            String ide_cnccc = cls_activo_fijo.depreciarActivos(tab_activos_a_depreciar);
            if (!ide_cnccc.isEmpty()) {
                parametro = new HashMap();
                parametro.put("ide_cnccc", Long.parseLong(ide_cnccc));
                parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                vp.setVisualizarPDF("rep_contabilidad/rep_comprobante_contabilidad.jasper", parametro);
                vp.dibujar();
                utilitario.addUpdate("vp");
            }

            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValor("ide_acafi"));
        }
    }

    public void aceptarTipoIngresoActivo() {
        if (rad_tipo_ingreso.getValue() != null) {
            if (rad_tipo_ingreso.getValue().equals("0")) {
                dia_tipo_ingreso_activo.cerrar();
                dia_num_factura.setTitle("SELECCION NUMERO DE FACTURA");
                dia_num_factura.dibujar();
            } else {
                dia_tipo_ingreso_activo.cerrar();
                aceptarNumeroFactura();
            }
        }
    }
    String str_ide_inarti;

    @Override
    public void insertar() {
        if (tab_tabla1.isFocus()) {
            str_ide_inarti = arb_arbol.getValorSeleccionado();
            TablaGenerica tab_articulo = utilitario.consultar("select * from inv_articulo where ide_inarti=" + str_ide_inarti);
            if (tab_articulo.getTotalFilas() > 0) {
                if (tab_articulo.getValor(0, "nivel_inarti") != null && !tab_articulo.getValor(0, "nivel_inarti").isEmpty()) {
                    if (tab_articulo.getValor(0, "nivel_inarti").equalsIgnoreCase("HIJO")) {
                        dia_tipo_ingreso_activo.dibujar();
//                        dia_num_factura.setTitle("SELECCION NUMERO DE FACTURA");
//                        dia_num_factura.dibujar();
                    } else {
                        utilitario.agregarMensajeInfo("Atencion", "Seleccione un grupo de activos mas interno");
                    }
                } else {
                    utilitario.agregarMensajeInfo("Atencion", "Seleccione un grupo de activos mas interno");
                }
            }
        } else if (via_comprobante_conta.getTab_det_comp_cont_vasiento().isFocus()) {
            via_comprobante_conta.insertar();
        }

    }

    @Override
    public void guardar() {
        if (tab_tabla1.isFilaInsertada() || tab_tabla1.isFilaModificada()) {
            if (!tab_tabla1.getValor("ide_aceaf").equals(utilitario.getVariable("p_act_estado_dado_de_baja"))) {
                tab_tabla1.guardar();
            } else {
                utilitario.agregarMensajeInfo("Atencion", "No puede cambiar el estado a Dado de Baja");
            }
        }
        if (tab_tabla3.isFilaInsertada() || tab_tabla3.isFilaModificada()) {
            tab_tabla3.guardar();
        } else if (tab_tabla3.isFilaModificada()) {
            TablaGenerica tab_deprecia_activo = utilitario.consultar("select * from act_transaccion "
                    + "where ide_acttr=0 and ide_acafi=" + tab_tabla3.getValor("ide_acafi") + "");
            if (tab_deprecia_activo.getTotalFilas() == 0) {
                tab_tabla3.guardar();
            } else {
                utilitario.agregarMensajeInfo("No se puede guardar", "No se puede cambiar el valor remanente o valor comercial ya que el activo tiene realizado depreciaciones");
            }
        }
        tab_tabla2.guardar();
        utilitario.getConexion().guardarPantalla();

    }

    @Override
    public void eliminar() {
        if (tab_tabla2.isFocus()) {
            System.out.println("fila actual " + tab_tabla2.getFilaActual());
            System.out.println("ide_acttr " + tab_tabla2.getValor("ide_acttr"));
            if (tab_tabla2.getFilaActual() == 0) {
                if (Integer.parseInt(tab_tabla2.getValor("ide_acttr")) == 0) {
                    if (tab_tabla2.getValor("ide_cnccc") == null || tab_tabla2.getValor("ide_cnccc").isEmpty()) {
                        tab_tabla2.setRecuperarLectura(false);
                        tab_tabla2.eliminar();
                    } else {
                        utilitario.agregarMensajeInfo("No se puede eliminar la transaccion", "La transacion ya tiene asiento contable");
                    }
                } else {
                    utilitario.agregarMensajeInfo("No se puede eliminar la transaccion", "El tipo de trasaccion seleccionado no se puede eliminar");
                }
            } else {
                utilitario.agregarMensajeInfo("No se puede eliminar la transaccion", "Debe seleccionar la primera fila");
            }
        }
    }

    public String obtenerNivelArticulo(String ide_inarti) {
        List lis_ide_inarti = utilitario.getConexion().consultar("select ide_inarti from inv_articulo where inv_ide_inarti in (" + ide_inarti + ")");
        if (!lis_ide_inarti.isEmpty()) {
            return "PADRE";
        } else {
            return "HIJO";
        }
    }

    public int verificarHijos(String hijos) {
        TablaGenerica tab_actvivos = utilitario.consultar("select *from act_activo_fijo where ide_inarti in ("
                + "select ide_inarti from inv_articulo where inv_ide_inarti in (" + hijos + "))");
        if (tab_actvivos.getTotalFilas() > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public String obtenerHijosActivo(List lis_ide_inarti) {
        String ide_inarti = "'";
        for (int i = 0; i < lis_ide_inarti.size(); i++) {
            ide_inarti = ide_inarti.concat(lis_ide_inarti.get(i).toString().concat("','"));
        }
        ide_inarti = ide_inarti.substring(0, ide_inarti.length() - 2);
        System.out.println("ide inarti " + ide_inarti);
        return ide_inarti;
    }

    public void seleccionar_arbol(NodeSelectEvent evt) {
        arb_arbol.seleccionarNodo(evt);
//        
        System.out.println("arbol " + arb_arbol.getValorSeleccionado());

        if (arb_arbol.getValorSeleccionado() != null) {
            String nivel = obtenerNivelArticulo(arb_arbol.getValorSeleccionado());
            if (nivel.equals("PADRE")) {
                List list_ides = utilitario.getConexion().consultar("select ide_inarti from inv_articulo where inv_ide_inarti in (" + arb_arbol.getValorSeleccionado() + ")");
                String hijos = obtenerHijosActivo(list_ides);
                int bandera = verificarHijos(hijos);
                if (bandera == 1) {
                    tab_tabla1.setCondicion("ide_inarti in (select ide_inarti from inv_articulo where inv_ide_inarti in (" + hijos + "))");
                    tab_tabla1.ejecutarSql();
                    tab_tabla3.setCondicion("ide_inarti in (select ide_inarti from inv_articulo where inv_ide_inarti in (" + hijos + "))");
                    tab_tabla3.ejecutarSql();
                    tab_tabla2.ejecutarSql();
                    tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                            + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                            + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                            + "where aa.ide_acafi=" + tab_tabla1.getValor("ide_acafi") + " "
                            + "order by aa.fecha_acaaf DESC");
                    tab_historial_asignacion.ejecutarSql();
                } else {
                    tab_tabla1.setCondicion("ide_inarti in (" + hijos + ")");
                    tab_tabla1.ejecutarSql();
                    tab_tabla3.setCondicion("ide_inarti in (" + hijos + ")");
                    tab_tabla3.ejecutarSql();
                    tab_tabla2.ejecutarSql();
                    tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                            + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                            + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                            + "where aa.ide_acafi=" + tab_tabla1.getValor("ide_acafi") + " "
                            + "order by aa.fecha_acaaf DESC");
                    tab_historial_asignacion.ejecutarSql();
                }
            }
            if (nivel.equals("HIJO")) {
                tab_tabla1.setCondicion("ide_inarti=" + arb_arbol.getValorSeleccionado());
                tab_tabla1.ejecutarSql();
                tab_tabla3.setCondicion("ide_inarti=" + arb_arbol.getValorSeleccionado());
                tab_tabla3.ejecutarSql();
                if (tab_tabla1.getTotalFilas() > 0) {
                    tab_tabla2.ejecutarSql();
                } else {
                    tab_tabla2.limpiar();
                }
                tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                        + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                        + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                        + "where aa.ide_acafi=" + tab_tabla1.getValor("ide_acafi") + " "
                        + "order by aa.fecha_acaaf DESC");
                tab_historial_asignacion.ejecutarSql();
            }
        } else {
            List list_ides = utilitario.getConexion().consultar("select ide_inarti from inv_articulo where inv_ide_inarti in (" + p_activos_fijos + ")");
            String hijos = obtenerHijosActivo(list_ides);
            tab_tabla1.setCondicion("ide_inarti in (select ide_inarti from inv_articulo where inv_ide_inarti in (" + hijos + "))");
            tab_tabla1.ejecutarSql();
            tab_tabla3.setCondicion("ide_inarti in (select ide_inarti from inv_articulo where inv_ide_inarti in (" + hijos + "))");
            tab_tabla3.ejecutarSql();
            tab_tabla2.ejecutarSql();
            tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                    + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                    + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                    + "where aa.ide_acafi=" + tab_tabla1.getValor("ide_acafi") + " "
                    + "order by aa.fecha_acaaf DESC");
            tab_historial_asignacion.ejecutarSql();
        }

        utilitario.addUpdate("tab_tabla1,tab_tabla3,tab_tabla2,tab_historial_asignacion");
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Arbol getArb_arbol() {
        return arb_arbol;
    }

    public void setArb_arbol(Arbol arb_arbol) {
        this.arb_arbol = arb_arbol;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

    public SeleccionArbol getSel_arb() {
        return sel_arb;
    }

    public void setSel_arb(SeleccionArbol sel_arb) {
        this.sel_arb = sel_arb;
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

    public Tabulador getTab_tabulador() {
        return tab_tabulador;
    }

    public void setTab_tabulador(Tabulador tab_tabulador) {
        this.tab_tabulador = tab_tabulador;
    }

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }

    public Tabla getTab_act_asignacion_activo() {
        return tab_act_asignacion_activo;
    }

    public void setTab_act_asignacion_activo(Tabla tab_act_asignacion_activo) {
        this.tab_act_asignacion_activo = tab_act_asignacion_activo;
    }

    public Dialogo getDia_asignacion_activo() {
        return dia_asignacion_activo;
    }

    public void setDia_asignacion_activo(Dialogo dia_asignacion_activo) {
        this.dia_asignacion_activo = dia_asignacion_activo;
    }

    public Tabla getTab_datos_reasignacion() {
        return tab_datos_reasignacion;
    }

    public void setTab_datos_reasignacion(Tabla tab_datos_reasignacion) {
        this.tab_datos_reasignacion = tab_datos_reasignacion;
    }

    public Dialogo getDia_dar_baja() {
        return dia_dar_baja;
    }

    public void setDia_dar_baja(Dialogo dia_dar_baja) {
        this.dia_dar_baja = dia_dar_baja;
    }

    public Tabla getTab_activo_dar_baja() {
        return tab_activo_dar_baja;
    }

    public void setTab_activo_dar_baja(Tabla tab_activo_dar_baja) {
        this.tab_activo_dar_baja = tab_activo_dar_baja;
    }

    public Tabla getTab_act_tran() {
        return tab_act_tran;
    }

    public void setTab_act_tran(Tabla tab_act_tran) {
        this.tab_act_tran = tab_act_tran;
    }

    public Dialogo getDia_ingreso_activo() {
        return dia_ingreso_activo;
    }

    public void setDia_ingreso_activo(Dialogo dia_ingreso_activo) {
        this.dia_ingreso_activo = dia_ingreso_activo;
    }

    public Dialogo getDia_num_factura() {
        return dia_num_factura;
    }

    public void setDia_num_factura(Dialogo dia_num_factura) {
        this.dia_num_factura = dia_num_factura;
    }

    public Tabla getTab_ingreso_activo() {
        return tab_ingreso_activo;
    }

    public void setTab_ingreso_activo(Tabla tab_ingreso_activo) {
        this.tab_ingreso_activo = tab_ingreso_activo;
    }

    public Tabla getTab_historial_asignacion() {
        return tab_historial_asignacion;
    }

    public void setTab_historial_asignacion(Tabla tab_historial_asignacion) {
        this.tab_historial_asignacion = tab_historial_asignacion;
    }

    public Tabla getTab_arti() {
        return tab_arti;
    }

    public void setTab_arti(Tabla tab_arti) {
        this.tab_arti = tab_arti;
    }

    public VistaAsiento getVia_comprobante_conta() {
        return via_comprobante_conta;
    }

    public void setVia_comprobante_conta(VistaAsiento via_comprobante_conta) {
        this.via_comprobante_conta = via_comprobante_conta;
    }

    public VisualizarPDF getVp() {
        return vp;
    }

    public void setVp(VisualizarPDF vp) {
        this.vp = vp;
    }

    public AutoCompletar getAut_num_factura() {
        return aut_num_factura;
    }

    public void setAut_num_factura(AutoCompletar aut_num_factura) {
        this.aut_num_factura = aut_num_factura;
    }

    public Dialogo getDia_item_activo() {
        return dia_item_activo;
    }

    public void setDia_item_activo(Dialogo dia_item_activo) {
        this.dia_item_activo = dia_item_activo;
    }

    public Dialogo getDia_tipo_ingreso_activo() {
        return dia_tipo_ingreso_activo;
    }

    public void setDia_tipo_ingreso_activo(Dialogo dia_tipo_ingreso_activo) {
        this.dia_tipo_ingreso_activo = dia_tipo_ingreso_activo;
    }
}

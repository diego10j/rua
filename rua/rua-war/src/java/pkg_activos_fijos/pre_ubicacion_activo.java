/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_activos_fijos;

import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Efecto;
import framework.componentes.Grid;
import framework.componentes.MarcaAgua;
import framework.componentes.PanelAcordion;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.TablaGrid;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import org.primefaces.component.panel.Panel;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_ubicacion_activo extends Pantalla {

    private Tabla tab_tabla2 = new Tabla();
    private Division div_division = new Division();
    private PanelAcordion pac_acordion = new PanelAcordion();
    private Panel pan_emple = new Panel();
    private TablaGrid tag_empleados = new TablaGrid();
    private Texto tex_busca = new Texto();
    private String p_reh_estado_activo_empleado = utilitario.getVariable("p_reh_estado_activo_empleado");
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_formato = new SeleccionFormatoReporte();
    private Boton bot_reasignar_activo = new Boton();
    private Tabla tab_datos_reasignacion = new Tabla();
    private Tabla tab_act_asignacion_activo = new Tabla();
    private Dialogo dia_asignacion_activo = new Dialogo();
    private Tabla tab_datos_activo = new Tabla();
    private Tabla tab_datos_imagen = new Tabla();
    private Tabla tab_historial_asignacion = new Tabla();

    public pre_ubicacion_activo() {
        bar_botones.quitarBotonsNavegacion();
        pac_acordion.setId("pac_acordion");
        tab_datos_activo.setId("tab_datos_activo");
        tab_datos_activo.setIdCompleto("pac_acordion:tab_datos_activo");
        tab_datos_activo.setTabla("act_activo_fijo", "ide_acafi", 1);
        tab_datos_activo.agregarRelacion(tab_tabla2);
        tab_datos_activo.getColumna("ide_inarti").setVisible(false);
        tab_datos_activo.getColumna("gen_ide_geper").setVisible(false);
        tab_datos_activo.getColumna("ide_rheor").setVisible(false);
        tab_datos_activo.getColumna("alterno_acafi").setVisible(false);
        tab_datos_activo.getColumna("ano_actual_acafi").setVisible(false);
        tab_datos_activo.getColumna("fo_acafi").setVisible(false);
        tab_datos_activo.getColumna("ide_geubi").setVisible(false);
        tab_datos_activo.getColumna("ide_usua").setVisible(false);
        tab_datos_activo.getColumna("foto_acafi").setUpload("upload/activos");
        tab_datos_activo.getColumna("foto_acafi").setValorDefecto("upload/activos/empleado.png");
        tab_datos_activo.getColumna("foto_acafi").setImagen("120", "120");
        tab_datos_activo.getColumna("ide_aceaf").setCombo("act_estado_activo_fijo", "ide_aceaf", "nombre_aceaf", "");
        tab_datos_activo.getColumna("ide_inmar").setCombo("inv_marca", "ide_inmar", "nombre_invmar", "");
        tab_datos_activo.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "");
        tab_datos_activo.getColumna("ide_geper").setLectura(true);
        tab_datos_activo.getColumna("ide_geper").setAutoCompletar();
        tab_datos_activo.getColumna("ide_acuba").setCombo("act_ubicacion_activo", "ide_acuba", "nombre_acuba", "");
        tab_datos_activo.getColumna("ide_acuba").setAutoCompletar();
        tab_datos_activo.getColumna("cantidad_acafi").setVisible(false);
        tab_datos_activo.getColumna("vida_util_acafi").setLectura(true);
        tab_datos_activo.getColumna("valor_compra_acafi").setLectura(true);
        tab_datos_activo.getColumna("deprecia_acafi").setVisible(false);
        tab_datos_activo.getColumna("recidual_acafi").setVisible(true);
        tab_datos_activo.getColumna("credi_tribu_acafi").setVisible(false);
        tab_datos_activo.getColumna("anos_uso_acafi").setVisible(false);
        tab_datos_activo.getColumna("valor_comercial_acafi").setVisible(true);
        tab_datos_activo.getColumna("valor_remate_acafi").setVisible(false);
        tab_datos_activo.getColumna("fecha_compra_acafi").setLectura(true);
        tab_datos_activo.getColumna("fecha_acafi").setLectura(true);
        tab_datos_activo.getGrid().setColumns(4);
        tab_datos_activo.setTipoFormulario(true);
        tab_datos_activo.dibujar();

        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_datos_activo);

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
        tab_historial_asignacion.dibujar();

        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_historial_asignacion);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("act_transaccion", "ide_actra", 2);
        tab_tabla2.getColumna("ide_acttr").setValorDefecto("0");
        tab_tabla2.getColumna("ide_acttr").setCombo("act_tipo_transaccion", "ide_acttr", "nombre_acttr", "");
        tab_tabla2.setCampoOrden("acumulado_actra desc");
        tab_tabla2.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_tabla2.setRecuperarLectura(true);
        tab_tabla2.setCampoOrden("ide_actra desc");
        tab_tabla2.setLectura(true);
        tab_tabla2.dibujar();

        PanelTabla pat_panel5 = new PanelTabla();
        pat_panel5.setPanelTabla(tab_tabla2);

        pac_acordion.agregarPanel("DATOS DEL ACTIVO", pat_panel1);
        pac_acordion.agregarPanel("HISTORIAL ASIGNACIONES", pat_panel2);
        pac_acordion.agregarPanel("TRANSACCIONES", pat_panel5);
        pac_acordion.setRendered(false);
        pan_emple.setId("pan_emple");
        pan_emple.setHeader("Listado de Activos");

        // Grid gri_bus = new Grid();
        // gri_bus.setColumns(3);
        tex_busca.setId("tex_busca");
        tex_busca.setMetodoKeyPress("buscarActivo");
        tex_busca.setSize(50);
        bar_botones.agregarComponente(tex_busca);

        MarcaAgua maa_marca = new MarcaAgua();
        maa_marca.setValue("Buscar Activo Fijo");
        maa_marca.setFor("tex_busca");
        agregarComponente(maa_marca);

        //gri_bus.getChildren().add(tex_busca);
        Boton bot_ficha = new Boton();
        bot_ficha.setIcon("ui-icon-calculator");
        bot_ficha.setMetodo("dibujarAcordeon");
        bot_ficha.setTitle("Vista Ficha");

        //gri_bus.getChildren().add(bot_ficha);
        bar_botones.agregarComponente(bot_ficha);

        Boton bot_grid = new Boton();
        bot_grid.setIcon("ui-icon-contact");
        bot_grid.setMetodo("dibujarGridEmpleados");
        bot_grid.setTitle("Vista Grid");
        bar_botones.agregarComponente(bot_grid);
        //gri_bus.getChildren().add(bot_grid); 

        pan_emple.getChildren().add(pac_acordion);

        Efecto efecto = new Efecto();
        efecto.setType("drop");
        efecto.setSpeed(150);
        efecto.setPropiedad("mode", "'show'");
        efecto.setEvent("load");
        pan_emple.getChildren().add(efecto);

        tab_datos_imagen.setId("tab_datos_imagen");
        tab_datos_imagen.setSql("select ide_acafi,nombre_acafi,observacion_acafi,nombre_acuba,nom_geper,foto_acafi,codigo_recu_acafi from act_activo_fijo af "
                + "left join gen_persona per on per.ide_geper=af.ide_geper "
                + "left join act_ubicacion_activo uac on uac.ide_acuba=af.ide_acuba "
                + "where af.ide_empr=" + utilitario.getVariable("ide_empr") + "");

        tab_datos_imagen.ejecutarSql();
        tag_empleados.setId("tag_empleados");
        tag_empleados.setTablaGrid(tab_datos_imagen);
        tag_empleados.setColumns(3);
        tag_empleados.setMostrarColumnas("codigo_recu_acafi,nombre_acafi,observacion_acafi,nombre_acuba,nom_geper", false);
        tag_empleados.setImagen("foto_acafi", "55", "55", "H");
        tag_empleados.setSeleccion("seleccionoActivo");
        tag_empleados.dibujar();
        pan_emple.getChildren().add(tag_empleados);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sel_formato.setId("sel_formato");
        agregarComponente(sel_formato);
        agregarComponente(rep_reporte);
        bar_botones.agregarReporte();

        div_division.setId("div_division");
        div_division.dividir1(pan_emple);

        // BOTON REASIGNAR ACTIVO
        bot_reasignar_activo.setId("bot_reasignar_activo");
        bot_reasignar_activo.setValue("Reaignar Activo");
        bot_reasignar_activo.setMetodo("reasignarActivo");
        bot_reasignar_activo.setDisabled(true);
        bar_botones.agregarBoton(bot_reasignar_activo);

        tab_datos_reasignacion.setId("tab_datos_reasignacion");
        tab_datos_reasignacion.setSql("select af.ide_acafi as sql_ide_acafi,nom_geper as custodio_actual,nombre_acuba as ubicacion,nombre_acafi as descripcion,serie_acafi as serie,modelo_acafi as modelo from act_activo_fijo af "
                + "left join gen_persona per on per.ide_geper = af.ide_geper "
                + "left join inv_articulo arti on arti.ide_inarti = af.ide_inarti "
                + "left join inv_articulo arti1 on arti.inv_ide_inarti = arti1.ide_inarti "
                + "left join act_ubicacion_activo acubi on acubi.ide_acuba=af.ide_acuba "
                + "where ide_acafi =-1");
        tab_datos_reasignacion.setCampoPrimaria("sql_ide_acafi");
        tab_datos_reasignacion.setNumeroTabla(2);
        tab_datos_reasignacion.getColumna("sql_ide_acafi").setVisible(false);
        tab_datos_reasignacion.setLectura(true);
        tab_datos_reasignacion.dibujar();

        tab_act_asignacion_activo.setId("tab_act_asignacion_activo");
        tab_act_asignacion_activo.setTabla("act_asignacion_activo", "ide_acaaf", 3);
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

        agregarComponente(div_division);
    }

    public void aceptarAsignacion() {
        if (tab_act_asignacion_activo.getValor("ide_geper") != null && !tab_act_asignacion_activo.getValor("ide_geper").isEmpty()) {
            if (tab_act_asignacion_activo.getValor("observacion_acaaf") != null && !tab_act_asignacion_activo.getValor("observacion_acaaf").isEmpty()) {
                dia_asignacion_activo.cerrar();
                utilitario.getConexion().agregarSqlPantalla("update act_activo_fijo set ide_geper=" + tab_act_asignacion_activo.getValor("ide_geper") + " where ide_acafi=" + tab_act_asignacion_activo.getValor("ide_acafi"));
                String ide_acafi = tab_act_asignacion_activo.getValor("ide_acafi");
                tab_act_asignacion_activo.guardar();
                utilitario.getConexion().guardarPantalla();
                tab_datos_activo.setCondicion("ide_acafi>0");
                tab_datos_activo.ejecutarSql();
                tab_datos_activo.setFilaActual(ide_acafi);
                utilitario.addUpdate("tab_datos_activo");
            } else {
                utilitario.agregarMensajeError("Atencion", "La observacion de la reaignacion es obligatorio");
            }
        } else {
            utilitario.agregarMensajeError("Atencion", "No ha seleccionado un nuevo custodio");
        }

    }

    public void cancelarDialogo() {
        if (dia_asignacion_activo.isVisible()) {
            dia_asignacion_activo.cerrar();
        }
        //utilitario.getConexion().rollback(); **********
        utilitario.getConexion().getSqlPantalla().clear();

    }
    String str_ide_acafi_reasignado;

    public void reasignarActivo() {
        tab_datos_reasignacion.setSql("select af.ide_acafi as sql_ide_acafi,nom_geper as custodio_actual,nombre_acuba as ubicacion,nombre_acafi as descripcion,serie_acafi as serie,modelo_acafi as modelo from act_activo_fijo af "
                + "left join gen_persona per on per.ide_geper = af.ide_geper "
                + "left join inv_articulo arti on arti.ide_inarti = af.ide_inarti "
                + "left join inv_articulo arti1 on arti.inv_ide_inarti = arti1.ide_inarti "
                + "left join act_ubicacion_activo acubi on acubi.ide_acuba=af.ide_acuba "
                + "where ide_acafi =" + tab_datos_activo.getValor("ide_acafi") + "");
        str_ide_acafi_reasignado = tab_datos_activo.getValor("ide_acafi");
        tab_datos_reasignacion.ejecutarSql();
        tab_act_asignacion_activo.insertar();
        tab_act_asignacion_activo.setValor("ide_acafi", tab_datos_activo.getValor("ide_acafi"));
        dia_asignacion_activo.setTitle("REASIGNACION DE ACTIVOS FIJOS");
        dia_asignacion_activo.dibujar();
    }

    public void buscarActivo() {
        if (tex_busca.getValue() != null && !tex_busca.getValue().toString().isEmpty()) {
            tab_datos_activo.setCondicion("LOWER(nombre_acafi) LIKE '%" + tex_busca.getValue().toString().toLowerCase() + "%' or LOWER(codigo_recu_acafi) LIKE '%" + tex_busca.getValue().toString().toLowerCase() + "%'");
            tab_datos_imagen.setSql("select ide_acafi,nombre_acafi,observacion_acafi,nombre_acuba,nom_geper,foto_acafi,codigo_recu_acafi from act_activo_fijo af "
                    + "left join gen_persona per on per.ide_geper=af.ide_geper "
                    + "left join act_ubicacion_activo uac on uac.ide_acuba=af.ide_acuba "
                    + "where af.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                    + "and LOWER(nombre_acafi) LIKE '%" + tex_busca.getValue().toString().toLowerCase() + "%' "
                    + "or LOWER(codigo_recu_acafi) LIKE '%" + tex_busca.getValue().toString().toLowerCase() + "%' "
                    + "or LOWER(nombre_acuba) LIKE '%" + tex_busca.getValue().toString().toLowerCase() + "%'");

        } else {
            tab_datos_activo.setCondicion("ide_acafi>0");
            tab_datos_imagen.setSql("select ide_acafi,nombre_acafi,observacion_acafi,nombre_acuba,nom_geper,foto_acafi,codigo_recu_acafi from act_activo_fijo af "
                    + "left join gen_persona per on per.ide_geper=af.ide_geper "
                    + "left join act_ubicacion_activo uac on uac.ide_acuba=af.ide_acuba "
                    + "where af.ide_empr=" + utilitario.getVariable("ide_empr") + " ");
        }
        tab_datos_activo.ejecutarSql();
        utilitario.addUpdate("tab_datos_activo");
        if (tab_datos_activo.getTotalFilas() == 1 || tab_datos_activo.getTotalFilas() == 0) {
            tab_datos_activo.setCondicion("ide_acafi>0");
            tab_datos_activo.ejecutarSql();
            utilitario.addUpdate("tab_datos_activo");
        }
        tab_datos_imagen.ejecutarSql();
        if (pac_acordion.isRendered()) {
            bot_reasignar_activo.setDisabled(false);
        } else {
            bot_reasignar_activo.setDisabled(true);
        }
        utilitario.addUpdate("tag_empleados,bot_reasignar_activo");
    }

    public void seleccionoActivo() {
        System.out.println("ide_seleccionado " + tag_empleados.getValorSeleccionado());
        if (tab_datos_activo.getTotalFilas() == 1 || tab_datos_activo.getTotalFilas() == 0) {
            tab_datos_activo.setCondicion("ide_acafi>0");
            tab_datos_activo.ejecutarSql();
            utilitario.addUpdate("tab_datos_activo");
        }
        pac_acordion.setRendered(true);
        tab_datos_activo.setFilaActual(tag_empleados.getValorSeleccionado());
        tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                + "where aa.ide_acafi=" + tag_empleados.getValorSeleccionado() + " "
                + "order by aa.fecha_acaaf DESC");
        tab_historial_asignacion.ejecutarSql();
        utilitario.addUpdate("tab_historial_asignacion");
        bot_reasignar_activo.setDisabled(false);
        tag_empleados.setRendered(false);
        utilitario.addUpdate("pan_emple,bot_reasignar_activo");
    }

    public TablaGrid getTag_empleados() {
        return tag_empleados;
    }

    public void setTag_empleados(TablaGrid tag_empleados) {
        this.tag_empleados = tag_empleados;
    }

    public void dibujarAcordeon() {
        pac_acordion.setRendered(true);
        if (tag_empleados.getValorSeleccionado() != null) {
            tab_datos_activo.setFilaActual(tag_empleados.getValorSeleccionado());
            if (tex_busca.getValue() != null && !tex_busca.toString().isEmpty()) {
                tab_datos_imagen.setSql("select ide_acafi,nombre_acafi,observacion_acafi,nombre_acuba,nom_geper,foto_acafi,codigo_recu_acafi from act_activo_fijo af "
                        + "left join gen_persona per on per.ide_geper=af.ide_geper "
                        + "left join act_ubicacion_activo uac on uac.ide_acuba=af.ide_acuba "
                        + "where af.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                        + "and LOWER(nombre_acafi) LIKE '%" + tex_busca.getValue().toString().toLowerCase() + "%' "
                        + "or LOWER(codigo_recu_acafi) LIKE '%" + tex_busca.getValue().toString().toLowerCase() + "%' "
                        + "or LOWER(nombre_acuba) LIKE '%" + tex_busca.getValue().toString().toLowerCase() + "%'");
            } else {
                tab_datos_imagen.setSql("select ide_acafi,nombre_acafi,observacion_acafi,nombre_acuba,nom_geper,foto_acafi,codigo_recu_acafi from act_activo_fijo af "
                        + "left join gen_persona per on per.ide_geper=af.ide_geper "
                        + "left join act_ubicacion_activo uac on uac.ide_acuba=af.ide_acuba "
                        + "where af.ide_empr=" + utilitario.getVariable("ide_empr") + " ");
            }
            tab_datos_imagen.ejecutarSql();

            tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                    + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                    + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                    + "where aa.ide_acafi=" + tag_empleados.getValorSeleccionado() + " "
                    + "order by aa.fecha_acaaf DESC");
            tab_historial_asignacion.ejecutarSql();
            utilitario.addUpdate("tab_historial_asignacion");
        } else {
            tab_historial_asignacion.setSql("select ide_acaaf as has_ide_acaaf,ide_acafi,nom_usua,nom_geper,fecha_acaaf,observacion_acaaf from act_asignacion_activo aa "
                    + "left join gen_persona per on per.ide_geper=aa.ide_geper "
                    + "left join sis_usuario u on u.ide_usua=aa.ide_usua "
                    + "where aa.ide_acafi=" + tab_datos_activo.getValor("ide_acafi") + " "
                    + "order by aa.fecha_acaaf DESC");
            tab_historial_asignacion.ejecutarSql();
            utilitario.addUpdate("tab_historial_asignacion");

        }
        tag_empleados.setRendered(false);
        bot_reasignar_activo.setDisabled(false);
        utilitario.addUpdate("pan_emple,bot_reasignar_activo");
    }

    public void dibujarGridEmpleados() {

        pac_acordion.setRendered(false);
        if (tex_busca.getValue() != null && !tex_busca.toString().isEmpty()) {
            tab_datos_imagen.setSql("select ide_acafi,nombre_acafi,observacion_acafi,nombre_acuba,nom_geper,foto_acafi,codigo_recu_acafi from act_activo_fijo af "
                    + "left join gen_persona per on per.ide_geper=af.ide_geper "
                    + "left join act_ubicacion_activo uac on uac.ide_acuba=af.ide_acuba "
                    + "where af.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                    + "and LOWER(nombre_acafi) LIKE '%" + tex_busca.getValue().toString().toLowerCase() + "%' "
                    + "or LOWER(codigo_recu_acafi) LIKE '%" + tex_busca.getValue().toString().toLowerCase() + "%' "
                    + "or LOWER(nombre_acuba) LIKE '%" + tex_busca.getValue().toString().toLowerCase() + "%'");
        } else {
            tab_datos_imagen.setSql("select ide_acafi,nombre_acafi,observacion_acafi,nombre_acuba,nom_geper,foto_acafi,codigo_recu_acafi from act_activo_fijo af "
                    + "left join gen_persona per on per.ide_geper=af.ide_geper "
                    + "left join act_ubicacion_activo uac on uac.ide_acuba=af.ide_acuba "
                    + "where af.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                    + "and af.ide_acafi=" + tag_empleados.getValorSeleccionado());
        }
        tab_datos_imagen.ejecutarSql();
        bot_reasignar_activo.setDisabled(true);
        tag_empleados.setRendered(true);
        utilitario.addUpdate("pan_emple,bot_reasignar_activo");
    }

    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra 
        rep_reporte.dibujar();

    }
    Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
//Se ejecuta cuando se selecciona un reporte de la lista
        if (rep_reporte.getReporteSelecionado().equals("Empleados por Departamento")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                parametro.put("ide_rheem", Integer.parseInt(tab_datos_activo.getValor("ide_rheem")));
                System.out.println("Si parametro..." + parametro + "----->" + Integer.parseInt(utilitario.getVariable("p_reh_estado_activo_empleado")));
                sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_formato.dibujar();
                utilitario.addUpdate("rep_reporte,sel_formato");
            }
        }
    }

    @Override
    public void insertar() {
        if (pac_acordion.isRendered() == false) {
            dibujarAcordeon();
//            tab_tabla1.insertar();
        } else {
            //utilitario.getTablaisFocus().insertar();
        }

    }

    @Override
    public void guardar() {
        if (tab_datos_activo.isFilaModificada()) {
            System.out.println("foto " + tab_datos_activo.getValor("foto_acafi"));
            tab_datos_activo.guardar();
            guardarPantalla();
        }

    }

    @Override
    public void eliminar() {
        if (utilitario.getTablaisFocus() != null) {
            utilitario.getTablaisFocus().eliminar();
        }

    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSel_formato() {
        return sel_formato;
    }

    public void setSel_formato(SeleccionFormatoReporte sel_formato) {
        this.sel_formato = sel_formato;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

    public Dialogo getDia_asignacion_activo() {
        return dia_asignacion_activo;
    }

    public void setDia_asignacion_activo(Dialogo dia_asignacion_activo) {
        this.dia_asignacion_activo = dia_asignacion_activo;
    }

    public Tabla getTab_act_asignacion_activo() {
        return tab_act_asignacion_activo;
    }

    public void setTab_act_asignacion_activo(Tabla tab_act_asignacion_activo) {
        this.tab_act_asignacion_activo = tab_act_asignacion_activo;
    }

    public Tabla getTab_datos_reasignacion() {
        return tab_datos_reasignacion;
    }

    public void setTab_datos_reasignacion(Tabla tab_datos_reasignacion) {
        this.tab_datos_reasignacion = tab_datos_reasignacion;
    }

    public Tabla getTab_datos_activo() {
        return tab_datos_activo;
    }

    public void setTab_datos_activo(Tabla tab_datos_activo) {
        this.tab_datos_activo = tab_datos_activo;
    }

    public Tabla getTab_datos_imagen() {
        return tab_datos_imagen;
    }

    public void setTab_datos_imagen(Tabla tab_datos_imagen) {
        this.tab_datos_imagen = tab_datos_imagen;
    }

    public Tabla getTab_historial_asignacion() {
        return tab_historial_asignacion;
    }

    public void setTab_historial_asignacion(Tabla tab_historial_asignacion) {
        this.tab_historial_asignacion = tab_historial_asignacion;
    }
}

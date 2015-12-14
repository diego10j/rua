/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sistema;

import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Hora;
import framework.componentes.ListaSeleccion;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionHora;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import servicios.sistema.ServicioSeguridad;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_auditoria extends Pantalla {

    private final Calendario cal_fecha_inicial = new Calendario();
    private final Calendario cal_fecha_final = new Calendario();
    private final Hora hor_inicial = new Hora();
    private final Hora hor_final = new Hora();
    private Tabla tab_acceso = new Tabla();
    private Tabla tab_auditoria = new Tabla();
    private Confirmar con_confirmar = new Confirmar();
    private SeleccionTabla set_usu_conectados = new SeleccionTabla();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();
    private SeleccionTabla sel_tab_accion_auditoria = new SeleccionTabla();
    private SeleccionTabla sel_tab_usuarios = new SeleccionTabla();
    private Map map_parametros = new HashMap();
    private SeleccionTabla set_tab_recursos = new SeleccionTabla();
    private final ListaSeleccion lis_accion_auditoria = new ListaSeleccion();
    private Dialogo dia_accion_auditoria = new Dialogo();
    private SeleccionHora seh_rango_hora_reporte = new SeleccionHora();
    @EJB
    private final ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario
            .instanciarEJB(ServicioSeguridad.class);

    public pre_auditoria() {
        bar_botones.limpiar();
        bar_botones.agregarReporte();

        bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
        cal_fecha_inicial.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_inicial);

        bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
        cal_fecha_final.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_final);

        bar_botones.agregarComponente(new Etiqueta("Desde :"));
        hor_inicial.setValue(utilitario.getHora(utilitario
                .getFormatoHora("07:00:00")));
        bar_botones.agregarComponente(hor_inicial);

        bar_botones.agregarComponente(new Etiqueta("Hasta :"));
        hor_final.setValue(utilitario.getHora(utilitario
                .getFormatoHora("18:00:00")));
        bar_botones.agregarComponente(hor_final);

        Boton bot_filtrar = new Boton();
        bot_filtrar.setValue("Actualizar");
        bot_filtrar.setMetodo("actualizarAuditoria");
        bot_filtrar.setIcon("ui-icon-refresh");
        bar_botones.agregarBoton(bot_filtrar);

        Boton bot_borrar = new Boton();
        bot_borrar.setValue("Borrar Auditoria");
        bot_borrar.setIcon("ui-icon-trash");
        bot_borrar.setMetodo("borrarAuditoria");
        bar_botones.agregarBoton(bot_borrar);

        Boton bot_usu_activo = new Boton();
        bot_usu_activo.setValue("Usuarios Conectados");
        bot_usu_activo.setMetodo("abrirUsuariosConectados");
        bot_usu_activo.setIcon("ui-icon-person");
        bar_botones.agregarBoton(bot_usu_activo);

        tab_acceso.setId("tab_acceso");
        tab_acceso.setTabla("sis_auditoria_acceso", "ide_auac", 1);
        tab_acceso.getColumna("ide_acau").setCombo("sis_accion_auditoria",
                "ide_acau", "nom_acau", "");
        tab_acceso.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua",
                "nom_usua", "");
        tab_acceso.getColumna("ide_usua").setFiltro(true);
        tab_acceso.getColumna("ide_acau").setFiltro(true);
        tab_acceso.getColumna("ip_auac").setFiltro(true);
        tab_acceso.getColumna("fin_auac").setCheck();
        tab_acceso.getColumna("sis_ide_usua").setCombo(
                tab_acceso.getColumna("ide_usua").getListaCombo());
        tab_acceso.setRows(20);
        tab_acceso.setLectura(true);
        tab_acceso.setCondicion(getFiltrosAcceso());
        tab_acceso.setCampoOrden("IDE_AUAC desc");
        tab_acceso.dibujar();
        PanelTabla pat_acceso = new PanelTabla();
        tab_acceso.setHeader("Actividades de Acceso al sistema");
        pat_acceso.setPanelTabla(tab_acceso);

        tab_auditoria.setId("tab_auditoria");
        tab_auditoria.setTabla("sis_auditoria", "ide_audi", 2);
        tab_auditoria.setLectura(true);
        tab_auditoria.getColumna("ide_usua").setCombo("sis_usuario",
                "ide_usua", "nom_usua", "");
        tab_auditoria.getColumna("ide_opci").setCombo("sis_opcion", "ide_opci",
                "nom_opci", "");
        tab_auditoria.setRows(20);
        tab_auditoria.getColumna("ide_usua").setFiltro(true);
        tab_auditoria.getColumna("ip_audi").setFiltro(true);
        tab_auditoria.getColumna("ide_opci").setFiltro(true);
        tab_auditoria.getColumna("sql_audi").setLongitud(300);
        tab_auditoria.getColumna("accion_audi").setFiltro(true);
        tab_auditoria.getColumna("tabla_audi").setFiltro(true);
        tab_auditoria.setCondicion(getFiltrosAuditoria());
        tab_auditoria.setCampoOrden("ide_audi desc");
        tab_auditoria.dibujar();

        PanelTabla pat_auditoria = new PanelTabla();
        tab_auditoria.setHeader("Transacciones realizadas en el sistema");
        pat_auditoria.setPanelTabla(tab_auditoria);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_acceso, pat_auditoria, "50%", "H");
        agregarComponente(div_division);

        con_confirmar.setId("con_confirmar");
        con_confirmar.setTitle("BORRAR TABLAS DE AUDITORIA");
        con_confirmar
                .setMessage("Está seguro que desea eliminar todos los registros de auditoría del sistema");
        con_confirmar.getBot_aceptar().setMetodo("aceptarBorrarAuditoria");
        agregarComponente(con_confirmar);

        set_usu_conectados.setId("set_usu_conectados");
        set_usu_conectados.setTitle("USUARIOS CONECTADOS AL SISTEMA");
        set_usu_conectados.setWidth("80%");
        set_usu_conectados.setHeight("70%");
        set_usu_conectados.setSeleccionTabla(ser_seguridad.getSqlUsuariosConectados(), "IDE_AUAC");
        set_usu_conectados.getBot_aceptar().setValue(
                "Desconectar Usuarios Seleccionados");
        set_usu_conectados.getBot_aceptar().setMetodo("desconectarUsuarios");
        agregarComponente(set_usu_conectados);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);

        sel_rep.setId("sel_rep");
        agregarComponente(sel_rep);

        sec_rango_reporte.setId("sec_rango_reporte");
        sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(sec_rango_reporte);

        seh_rango_hora_reporte.setId("seh_rango_hora_reporte");
        seh_rango_hora_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(seh_rango_hora_reporte);

        sel_tab_accion_auditoria.setId("sel_tab_accion_auditoria");
        sel_tab_accion_auditoria.setSeleccionTabla(
                "select IDE_ACAU,NOM_ACAU from SIS_ACCION_AUDITORIA",
                "ide_acau");
        sel_tab_accion_auditoria.getBot_aceptar().setMetodo("aceptarReporte");
        sel_tab_accion_auditoria.setTitle("SELECCIONE ACCION DE AUDITORIA DE ACCESO");
        agregarComponente(sel_tab_accion_auditoria);

        sel_tab_usuarios.setId("sel_tab_usuarios");
        sel_tab_usuarios.setSeleccionTabla("SELECT IDE_USUA,NICK_USUA,NOM_USUA FROM SIS_USUARIO",
                "ide_usua");
        sel_tab_usuarios.getBot_aceptar().setMetodo("aceptarReporte");
        sel_tab_usuarios.setTitle("SELECCION DE USUARIOS");

        agregarComponente(sel_tab_usuarios);

        set_tab_recursos.setId("set_tab_recursos");
        set_tab_recursos.setSeleccionTabla(
                "select IDE_OPCI,NOM_OPCI as RECURSO from SIS_OPCION "
                + "GROUP BY IDE_OPCI,NOM_OPCI,SIS_IDE_OPCI "
                + "HAVING SIS_IDE_OPCI is not NULL "
                + "ORDER BY NOM_OPCI ASC", "ide_opci");
        set_tab_recursos.getBot_aceptar().setMetodo("aceptarReporte");
        set_tab_recursos.setTitle("SELECCION DE RECURSOS");
        agregarComponente(set_tab_recursos);

        List lista = new ArrayList();
        Object fila1[] = {"ELIMINAR", "ELIMINAR"};
        Object fila2[] = {"INSERTAR", "INSERTAR"};
        Object fila3[] = {"MODIFICAR", "MODIFICAR"};
        lista.add(fila1);
        lista.add(fila2);
        lista.add(fila3);
        lis_accion_auditoria.setListaSeleccion(lista);
        lis_accion_auditoria.setVertical();
        Grid gri_accion_audi = new Grid();
        gri_accion_audi.getChildren().add(lis_accion_auditoria);
        dia_accion_auditoria.setId("dia_accion_auditoria");
        dia_accion_auditoria.setTitle("Accion Auditoria");
        dia_accion_auditoria.setWidth("20%");
        dia_accion_auditoria.setHeight("20%");
        dia_accion_auditoria.setDialogo(gri_accion_audi);
        dia_accion_auditoria.setDynamic(false);
        gri_accion_audi.setStyle("width:"
                + (dia_accion_auditoria.getAnchoPanel() - 5) + "px;height:"
                + dia_accion_auditoria.getAltoPanel()
                + "px;overflow: auto;display: block;");
        dia_accion_auditoria.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(dia_accion_auditoria);

    }

    @Override
    public void abrirListaReportes() {
        sec_rango_reporte.getCal_fecha1().setValue(null);
        sec_rango_reporte.getCal_fecha2().setValue(null);

        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        // TODO Auto-generated method stub
        if (rep_reporte.getReporteSelecionado().equals("Actividades de Acceso al Sistema")) {
            if (rep_reporte.isVisible()) {
                map_parametros = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.setFecha1(null);
                sec_rango_reporte.setFecha2(null);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                if (sec_rango_reporte.isFechasValidas()) {
                    map_parametros.put("fecha_ini", sec_rango_reporte.getFecha1());
                    map_parametros.put("fecha_fin", sec_rango_reporte.getFecha2());
                    sec_rango_reporte.cerrar();
                    seh_rango_hora_reporte.setHora1(null);
                    seh_rango_hora_reporte.setHora2(null);
                    seh_rango_hora_reporte.dibujar();
                    utilitario.addUpdate("seh_rango_hora_reporte,sec_rango_reporte");
                } else {
                    utilitario.agregarMensajeInfo("Atencion", "El rango de fechas seleccionado es incorrecto");
                }
            } else if (seh_rango_hora_reporte.isVisible()) {
                if (seh_rango_hora_reporte.isHorasValidas()) {
                    map_parametros.put("hora_ini", seh_rango_hora_reporte.getTime1());
                    map_parametros.put("hora_fin", seh_rango_hora_reporte.getTime2());
                    seh_rango_hora_reporte.cerrar();
                    sel_tab_accion_auditoria.dibujar();
                    utilitario.addUpdate("seh_rango_hora_reporte,sel_tab_accion_auditoria");
                } else {
                    utilitario.agregarMensajeInfo("Atencion", "El rango de horas seleccionado es incorrecto");
                }
            } else if (sel_tab_accion_auditoria.isVisible()) {
                if (sel_tab_accion_auditoria.getListaSeleccionados().size() > 0) {
                    map_parametros.put("ide_acau", sel_tab_accion_auditoria.getSeleccionados());
                    sel_tab_accion_auditoria.cerrar();
                    sel_tab_usuarios.dibujar();
                    utilitario.addUpdate("sel_tab_usuarios,sel_tab_accion_auditoria");
                } else {
                    utilitario.agregarMensajeInfo("Atención", "Debe seleccionar al menos una accion de auditoria");
                }
            } else if (sel_tab_usuarios.isVisible()) {
                if (sel_tab_usuarios.getListaSeleccionados().size() > 0) {
                    map_parametros.put("ide_usua", sel_tab_usuarios.getSeleccionados());
                    sel_tab_usuarios.cerrar();
                    map_parametros.put("titulo", "ACTIVIDADES DE ACCESO AL SISTEMA");
                    sel_rep.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("sel_rep,sel_tab_usuarios");
                } else {
                    utilitario.agregarMensajeInfo("Atencion", "Debe seleccionar al menos un usuario");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Transacciones Realizadas en el Sistema")) {
            if (rep_reporte.isVisible()) {
                map_parametros = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.setFecha1(null);
                sec_rango_reporte.setFecha2(null);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                if (sec_rango_reporte.getFecha1String() != null && !sec_rango_reporte.getFecha1String().isEmpty()) {
                    //if (sec_rango_reporte.isFechasValidas()){
                    map_parametros.put("fecha_ini", sec_rango_reporte.getFecha1String());
                    map_parametros.put("fecha_fin", sec_rango_reporte.getFecha2String());
                    sec_rango_reporte.cerrar();
                    seh_rango_hora_reporte.setHora1(null);
                    seh_rango_hora_reporte.setHora2(null);
                    seh_rango_hora_reporte.dibujar();
                    utilitario.addUpdate("seh_rango_hora_reporte,sec_rango_reporte");
                } else {
                    utilitario.agregarMensajeInfo("Atencion", "El rango de fechas seleccionado es incorrecto");
                }
            } else if (seh_rango_hora_reporte.isVisible()) {
                if (seh_rango_hora_reporte.getHora1String() != null && !seh_rango_hora_reporte.getHora1String().isEmpty()
                        && seh_rango_hora_reporte.getHora2String() != null && !seh_rango_hora_reporte.getHora2String().isEmpty()) {

                    //				if (seh_rango_hora_reporte.isHorasValidas()){
                    map_parametros.put("hora_ini", seh_rango_hora_reporte.getHora1String());
                    map_parametros.put("hora_fin", seh_rango_hora_reporte.getHora2String());
                    seh_rango_hora_reporte.cerrar();
                    lis_accion_auditoria.setValue(null);
                    dia_accion_auditoria.dibujar();
                    utilitario.addUpdate("seh_rango_hora_reporte,dia_accion_auditoria");
                } else {
                    utilitario.agregarMensajeInfo("Atencion", "El rango de horas seleccionado es incorrecto");
                }
            } else if (dia_accion_auditoria.isVisible()) {
                if (lis_accion_auditoria.getSeleccionados() != null && !lis_accion_auditoria.getSeleccionados().isEmpty()) {
                    map_parametros.put("tipo_accion", lis_accion_auditoria.getSeleccionados());
                    System.out.println("accion auditoria " + lis_accion_auditoria.getSeleccionados());
                    dia_accion_auditoria.cerrar();
                    set_tab_recursos.dibujar();
                    utilitario.addUpdate("dia_accion_auditoria,set_tab_recursos");
                } else {
                    utilitario.agregarMensajeInfo("Atencion", "Debe seleccionar al menos una accion de auditoria");
                }
            } else if (set_tab_recursos.isVisible()) {
                if (set_tab_recursos.getSeleccionados() != null && !set_tab_recursos.getSeleccionados().isEmpty()) {
                    map_parametros.put("ide_opci", set_tab_recursos.getSeleccionados());
                    System.out.println("accion recurso " + set_tab_recursos.getSeleccionados());
                    set_tab_recursos.cerrar();
                    sel_tab_usuarios.dibujar();
                    utilitario.addUpdate("set_tab_recursos,sel_tab_usuarios");
                } else {
                    utilitario.agregarMensajeInfo("Atencion", "Debe seleccionar al menos un recurso del sistema");
                }
            } else if (sel_tab_usuarios.isVisible()) {
                map_parametros.put("ide_usua", sel_tab_usuarios.getSeleccionados());
                System.out.println("accion usuarios " + sel_tab_usuarios.getSeleccionados());
                sel_tab_usuarios.cerrar();
                map_parametros.put("titulo", "TRANSACCIONES REALIZADAS EN EL SISTEMA");
                sel_rep.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("sel_rep,sel_tab_usuarios");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Usuarios Conectados")) {
            if (rep_reporte.isVisible()) {
                map_parametros = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("sec_rango_reporte,rep_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                if (sec_rango_reporte.isFechasValidas()) {
                    map_parametros.put("fecha_ini", sec_rango_reporte.getFecha1String());
                    map_parametros.put("fecha_fin", sec_rango_reporte.getFecha2String());
                    sec_rango_reporte.cerrar();
                    map_parametros.put("titulo", "REPORTE USUARIOS CONECTADOS");
                    sel_rep.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("sel_rep,sec_rango_reporte");
                } else {
                    utilitario.agregarMensajeInfo("Atencion", "El rango de fechas seleccionado es incorrecto");
                }

            }

        }

    }

    public void desconectarUsuarios() {
        if (set_usu_conectados.getNumeroSeleccionados() > 0) {
            ser_seguridad.desconectarUsuarios(set_usu_conectados
                    .getSeleccionados());
            set_usu_conectados.cerrar();
            utilitario.agregarMensaje("Se desconectaron a "
                    + set_usu_conectados.getNumeroSeleccionados()
                    + " usuario(s)", "");
        } else {
            utilitario.agregarMensajeInfo(
                    "Debe seleccionar al menos un registro ", "");
        }
    }

    public void abrirUsuariosConectados() {
        set_usu_conectados.setSql("SELECT IDE_AUAC,NOM_USUA,IP_AUAC,FECHA_AUAC,HORA_AUAC FROM SIS_AUDITORIA_ACCESO au "
                + "INNER JOIN SIS_USUARIO us on au.IDE_USUA = us.IDE_USUA "
                + "WHERE IDE_ACAU="
                + ser_seguridad.P_SIS_INGRESO_USUARIO
                + " AND FIN_AUAC=false");
        set_usu_conectados.dibujar();
    }

    public void aceptarBorrarAuditoria() {
        con_confirmar.cerrar();
        ser_seguridad.borrarAuditoria();
        tab_acceso.limpiar();
        tab_auditoria.limpiar();
        utilitario.addUpdate("tab_acceso,tab_auditoria");
        utilitario.agregarMensaje("Se eliminaron los registros de auditoría",
                "");
    }

    private String getFiltrosAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (cal_fecha_inicial.getValue() != null
                && cal_fecha_final.getValue() != null
                && hor_inicial.getValue() != null
                && hor_final.getValue() != null) {

            str_filtros = " FECHA_AUAC BETWEEN "
                    + utilitario.getFormatoFechaSQL(cal_fecha_inicial
                            .getFecha()) + " AND "
                    + utilitario.getFormatoFechaSQL(cal_fecha_final.getFecha());
            str_filtros += " AND HORA_AUAC BETWEEN "
                    + utilitario.getFormatoHoraSQL(hor_inicial.getHora())
                    + " AND "
                    + utilitario.getFormatoHoraSQL(hor_final.getHora());

        } else {
            utilitario.agregarMensajeInfo("Filtros no válidos",
                    "Debe ingresar los fitros de fechas y horas");
        }
        return str_filtros;
    }

    private String getFiltrosAuditoria() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (cal_fecha_inicial.getValue() != null
                && cal_fecha_final.getValue() != null
                && hor_inicial.getValue() != null
                && hor_final.getValue() != null) {

            str_filtros = " FECHA_AUDI BETWEEN "
                    + utilitario.getFormatoFechaSQL(cal_fecha_inicial
                            .getFecha()) + " AND "
                    + utilitario.getFormatoFechaSQL(cal_fecha_final.getFecha());
            str_filtros += " AND HORA_AUDI BETWEEN "
                    + utilitario.getFormatoHoraSQL(hor_inicial.getHora())
                    + " AND "
                    + utilitario.getFormatoHoraSQL(hor_final.getHora());

        } else {
            utilitario.agregarMensajeInfo("Filtros no válidos",
                    "Debe ingresar los fitros de fechas y horas");
        }
        return str_filtros;
    }

    public void borrarAuditoria() {
        con_confirmar.dibujar();
    }

    public void actualizarAuditoria() {
        if (!getFiltrosAcceso().isEmpty()) {
            tab_acceso.setCondicion(getFiltrosAcceso());
            tab_auditoria.setCondicion(getFiltrosAuditoria());
            tab_acceso.ejecutarSql();
            tab_auditoria.ejecutarSql();
            utilitario.addUpdate("tab_acceso,tab_auditoria");
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

    public Tabla getTab_acceso() {
        return tab_acceso;
    }

    public void setTab_acceso(Tabla tab_acceso) {
        this.tab_acceso = tab_acceso;
    }

    public Tabla getTab_auditoria() {
        return tab_auditoria;
    }

    public void setTab_auditoria(Tabla tab_auditoria) {
        this.tab_auditoria = tab_auditoria;
    }

    public Confirmar getCon_confirmar() {
        return con_confirmar;
    }

    public void setCon_confirmar(Confirmar con_confirmar) {
        this.con_confirmar = con_confirmar;
    }

    public SeleccionTabla getSet_usu_conectados() {
        return set_usu_conectados;
    }

    public void setSet_usu_conectados(SeleccionTabla set_usu_conectados) {
        this.set_usu_conectados = set_usu_conectados;
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

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }

    public SeleccionTabla getSel_tab_accion_auditoria() {
        return sel_tab_accion_auditoria;
    }

    public void setSel_tab_accion_auditoria(
            SeleccionTabla sel_tab_accion_auditoria) {
        this.sel_tab_accion_auditoria = sel_tab_accion_auditoria;
    }

    public SeleccionTabla getSel_tab_usuarios() {
        return sel_tab_usuarios;
    }

    public void setSel_tab_usuarios(SeleccionTabla sel_tab_usuarios) {
        this.sel_tab_usuarios = sel_tab_usuarios;
    }

    public SeleccionTabla getSet_tab_recursos() {
        return set_tab_recursos;
    }

    public void setSet_tab_recursos(SeleccionTabla set_tab_recursos) {
        this.set_tab_recursos = set_tab_recursos;
    }

    public Dialogo getDia_accion_auditoria() {
        return dia_accion_auditoria;
    }

    public void setDia_accion_auditoria(Dialogo dia_accion_auditoria) {
        this.dia_accion_auditoria = dia_accion_auditoria;
    }

    public SeleccionHora getSeh_rango_hora_reporte() {
        return seh_rango_hora_reporte;
    }

    public void setSeh_rango_hora_reporte(SeleccionHora seh_rango_hora_reporte) {
        this.seh_rango_hora_reporte = seh_rango_hora_reporte;
    }

}

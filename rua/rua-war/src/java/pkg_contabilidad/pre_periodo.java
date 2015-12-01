package pkg_contabilidad;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.VisualizarPDF;
import java.util.HashMap;
import java.util.Map;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_periodo extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Division div_division = new Division();
    private Boton bot_cerrar_periodo = new Boton();
    private Boton bot_estado_sit_inicial = new Boton();
    private VisualizarPDF vp = new VisualizarPDF();
    private Dialogo dia_cambio_fecha = new Dialogo();
    private Calendario cal_fecha_actual = new Calendario();
    private Calendario cal_fecha = new Calendario();
    private Dialogo dia_cerrar_periodo = new Dialogo();

    public pre_periodo() {
        bar_botones.agregarBoton(bot_cerrar_periodo);
        bar_botones.agregarBoton(bot_estado_sit_inicial);

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("con_periodo", "ide_cnper", 1);
        tab_tabla.getColumna("estado_cnper").setLectura(true);
        tab_tabla.getColumna("cerrado_cnper").setLectura(true);
        tab_tabla.getColumna("fecha_inicio_cnper").setRequerida(true);
        tab_tabla.getColumna("fecha_inicio_cnper").setLectura(true);
        tab_tabla.getColumna("fecha_fin_cnper").setRequerida(true);
        tab_tabla.getColumna("nombre_cnper").setRequerida(true);
        tab_tabla.getColumna("fecha_fin_cnper").setLectura(true);
        tab_tabla.setCondicionSucursal(true);
        tab_tabla.setValidarInsertar(true);
        tab_tabla.dibujar();

        bot_cerrar_periodo.setValue("Cerrar Periodo");
        bot_cerrar_periodo.setMetodo("cerrarPeriodo");

        bot_estado_sit_inicial.setValue("Estado Situacion Inicial");
        bot_estado_sit_inicial.setMetodo("mostrarEstadoInicial");

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        ItemMenu itm_cambiar_fecha_fin = new ItemMenu();
        itm_cambiar_fecha_fin.setValue("Cambiar Fecha Fin");
        itm_cambiar_fecha_fin.setMetodo("modificarFechaFinPeriodo");
        pat_panel.getMenuTabla().getChildren().add(itm_cambiar_fecha_fin);

        dia_cambio_fecha.setId("dia_cambio_fecha");
        dia_cambio_fecha.setTitle("CAMBIO FECHA FIN DE PERIODO");
        dia_cambio_fecha.setWidth("30%");
        dia_cambio_fecha.setHeight("30%");
        dia_cambio_fecha.setDynamic(false);

        Etiqueta eti_fecha_actual = new Etiqueta();
        eti_fecha_actual.setValue("Fecha Fin Actual: ");
        eti_fecha_actual.setStyle("font-size: 14px;font-weight: bold");

        Etiqueta eti_fecha = new Etiqueta();
        eti_fecha.setValue("Fecha Fin: ");
        eti_fecha.setStyle("font-size: 14px;font-weight: bold");

        cal_fecha_actual.setDisabled(true);
        Grid grid_dialogo_cambio_doc = new Grid();
        grid_dialogo_cambio_doc.setColumns(2);
        grid_dialogo_cambio_doc.setStyle("width:" + (dia_cambio_fecha.getAnchoPanel() - 5) + "px; height:" + dia_cambio_fecha.getAltoPanel() + "px;overflow:auto;display:block;");
        grid_dialogo_cambio_doc.getChildren().add(eti_fecha_actual);
        grid_dialogo_cambio_doc.getChildren().add(cal_fecha_actual);
        grid_dialogo_cambio_doc.getChildren().add(eti_fecha);
        grid_dialogo_cambio_doc.getChildren().add(cal_fecha);

        dia_cambio_fecha.setDialogo(grid_dialogo_cambio_doc);
        dia_cambio_fecha.getBot_aceptar().setMetodo("aceptarDialogoCambioFechaFin");
        agregarComponente(dia_cambio_fecha);

        div_division.setId("div_division");
        div_division.dividir1(pat_panel);

        agregarComponente(div_division);

        vp.setId("vp");
        agregarComponente(vp);

        dia_cerrar_periodo.setId("dia_cerrar_periodo");
        dia_cerrar_periodo.setTitle("CONFIRMACION CIERRE DE PERIODO");
        dia_cerrar_periodo.setWidth("45%");
        dia_cerrar_periodo.setHeight("30%");

        Grid gri = new Grid();
        gri.setColumns(1);
        Etiqueta eti_1 = new Etiqueta("ATENCION SE REALIZARA LAS SIGUIENTES ACCIONES: ");
        eti_1.setStyle("font-size: 13px;font-weight:bold");
        Etiqueta eti_2 = new Etiqueta("1.- SE CERRARA EL PERIODO ACTUAL Y QUEDARA INACTIVO ");
        eti_2.setStyle("font-size: 11px;font-weight:text-decoration: underline");
        Etiqueta eti_3 = new Etiqueta("2.- SE VA A GENERAR UN NUEVO PERIODO AUTOMATICAMENTE SIN FECHA DE CIERRE ");
        eti_3.setStyle("font-size: 11px;font-weight:text-decoration: underline");
        Etiqueta eti_4 = new Etiqueta("3.- SE GENERARA UN ASIENTO DE APERTURA DE CUENTAS ");
        eti_4.setStyle("font-size: 11px;font-weight:text-decoration: underline");
        Etiqueta eti_5 = new Etiqueta("ESTA SEGURO DE CONTINUAR CON EL CIERRE DE PERIODO: ");
        eti_5.setStyle("font-size: 13px;font-weight:bold");
        gri.getChildren().add(eti_1);
        gri.getChildren().add(new Etiqueta(""));
        gri.getChildren().add(new Etiqueta(""));
        gri.getChildren().add(eti_2);
        gri.getChildren().add(eti_3);
        gri.getChildren().add(eti_4);
        gri.getChildren().add(new Etiqueta(""));
        gri.getChildren().add(new Etiqueta(""));
        gri.getChildren().add(eti_5);
        gri.setStyle("width:" + (dia_cerrar_periodo.getAnchoPanel() - 5) + "px; height:" + dia_cerrar_periodo.getAltoPanel() + "px;overflow:auto;display:block;");
        dia_cerrar_periodo.setDialogo(gri);
        dia_cerrar_periodo.getBot_aceptar().setMetodo("aceptarCierrePeriodo");
        dia_cerrar_periodo.getBot_cancelar().setMetodo("cancelarCierrePeriodo");

        agregarComponente(dia_cerrar_periodo);

    }

    public void aceptarDialogoCambioFechaFin() {
        if (cal_fecha.getValue() != null && !cal_fecha.getValue().toString().isEmpty()) {
            TablaGenerica tab_periodo = utilitario.consultar("select * from con_periodo "
                    + "where '" + cal_fecha.getValue().toString() + "' BETWEEN fecha_inicio_cnper and fecha_fin_cnper "
                    + "and ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
                    + "and ide_cnper not in (" + tab_tabla.getValor("ide_cnper") + ") ");
            if (tab_periodo.getTotalFilas() == 0) {
                if (utilitario.isFechaMayor(utilitario.getFecha(utilitario.getFormatoFecha(cal_fecha.getValue())), utilitario.getFecha(tab_tabla.getValor("fecha_inicio_cnper")))) {
                    utilitario.getConexion().agregarSqlPantalla("update con_periodo set fecha_fin_cnper='" + cal_fecha.getValue().toString() + "' where ide_cnper=" + tab_tabla.getValor("ide_cnper"));
                    guardarPantalla();
                    tab_tabla.ejecutarSql();
                    dia_cambio_fecha.cerrar();
                    utilitario.addUpdate("tab_tabla");
                } else {
                    utilitario.agregarMensajeInfo("No se puede Cambiar la Fecha Fin de Periodo", "La fecha ingresada no puede ser menor que la fecha inicio");
                }
            } else {
                utilitario.agregarMensajeInfo("No se puede Cambiar la Fecha Fin de Periodo", "La fecha ingresada se encuentra en otro Periodo");
            }
        } else {
            utilitario.agregarMensajeInfo("No se puede Cambiar la Fecha Fin de Periodo", "La fecha ingresada no es valida");
        }

    }

    public void modificarFechaFinPeriodo() {
        if (tab_tabla.getTotalFilas() > 0) {
            if (tab_tabla.getValor("estado_cnper").equals("true")) {
                cal_fecha_actual.setValue(utilitario.getFecha(tab_tabla.getValor("fecha_fin_cnper")));
                cal_fecha.limpiar();
                dia_cambio_fecha.dibujar();
            } else {
                utilitario.agregarMensajeInfo("No se puede Cambiar La fecha Fin de Periodo", "El periodo seleccionado no se encuentra activo");
            }
        }
    }
    Map parametro = new HashMap();

    public void mostrarEstadoInicial() {
        if (tab_tabla.getTotalFilas() > 0) {
            TablaGenerica tab_ide_cnccc_inicial = utilitario.consultar("select * from con_cab_comp_cont where fecha_trans_cnccc >= '" + tab_tabla.getValor("fecha_inicio_cnper") + "' "
                    + "order by fecha_trans_cnccc asc,observacion_cnccc ASC,hora_sistem_cnccc asc  "
                    + "limit 1");
            if (tab_ide_cnccc_inicial.getTotalFilas() > 0) {
                parametro = new HashMap();
                parametro.put("ide_cnccc", Long.parseLong(tab_ide_cnccc_inicial.getValor(0, "ide_cnccc")));
                parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                vp.setVisualizarPDF("rep_contabilidad/rep_comprobante_contabilidad.jasper", parametro);
                vp.dibujar();
            }
        }
    }

    public void cancelarCierrePeriodo() {
        if (dia_cerrar_periodo.isVisible()) {
            dia_cerrar_periodo.cerrar();
            utilitario.addUpdate("dia_cerrar_periodo");
        }
        //utilitario.getConexion().rollback(); ****
        utilitario.getConexion().getSqlPantalla().clear();

    }

    public void aceptarCierrePeriodo() {
        dia_cerrar_periodo.cerrar();
        utilitario.addUpdate("dia_cerrar_periodo");
        cls_contabilidad con = new cls_contabilidad();
        String str_ide_cnper_nuevo = con.cerrarPeriodoContable(tab_tabla.getValor("ide_cnper"), tab_tabla.getValor("fecha_inicio_cnper"), tab_tabla.getValor("fecha_fin_cnper"));
        String ide_cnccc = generarAsientoAperturaCuentas(tab_tabla.getValor("fecha_inicio_cnper"), tab_tabla.getValor("fecha_fin_cnper"));
        if (ide_cnccc != null) {
            utilitario.getConexion().agregarSqlPantalla("update con_periodo set cerrado_cnper=true where ide_cnper=" + tab_tabla.getValor("ide_cnper"));
            utilitario.getConexion().agregarSqlPantalla("update con_periodo set fecha_fin_cnper=null where ide_cnper=" + str_ide_cnper_nuevo);
            guardarPantalla();
            utilitario.agregarMensaje("Atencion", "El perido se cerro correctamente y se realizo un asiento de apertura de cuentas para el nuevo periodo ");
            tab_tabla.ejecutarSql();
            utilitario.addUpdate("tab_tabla");
            parametro = new HashMap();
            parametro.put("ide_cnccc", Long.parseLong(ide_cnccc));
            parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
            parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
            vp.setVisualizarPDF("rep_contabilidad/rep_comprobante_contabilidad.jasper", parametro);
            vp.dibujar();
        } else {
            utilitario.getConexion().getSqlPantalla().clear();
            if (utilitario.getConexion().ejecutarSql("delete from con_periodo where ide_cnper=" + str_ide_cnper_nuevo).isEmpty()) {
                utilitario.getConexion().ejecutarSql("update con_periodo set estado_cnper=true where ide_cnper=" + tab_tabla.getValor("ide_cnper"));
                // utilitario.getConexion().commit(); ****
                tab_tabla.ejecutarSql();
                utilitario.addUpdate("tab_tabla");
            }
        }

    }

    private String generarAsientoAperturaCuentas(String fecha_inicio, String fecha_fin) {
        if (fecha_inicio != null && !fecha_inicio.isEmpty()
                && fecha_fin != null && !fecha_fin.isEmpty()) {
            cls_contabilidad con = new cls_contabilidad();
            return con.generarAsientoAperturaCuentas(fecha_inicio, fecha_fin);
        } else {
            return null;
        }
    }

    public void cerrarPeriodo() {
        if (utilitario.getVariable("p_con_deficit_ejercicio_anterior") != null
                && utilitario.getVariable("p_con_superavit_ejercicio_anterior") != null
                && utilitario.getVariable("p_con_patrimonio") != null) {
            if (tab_tabla.getValor("fecha_fin_cnper") != null && !tab_tabla.getValor("fecha_fin_cnper").isEmpty()) {
                if (tab_tabla.getValor("estado_cnper").equalsIgnoreCase("true")) {
                    dia_cerrar_periodo.dibujar();
                    utilitario.addUpdate("dia_cerrar_periodo");
                } else {
                    utilitario.agregarMensajeError("No se puede cerrar el periodo", "El periodo tiene estado Inactivo");
                }
            } else {
                utilitario.agregarMensajeError("No se puede cerrar el periodo", "El periodo no tiene fecha fin ");
            }

        } else {
            utilitario.agregarMensajeInfo("No se puede Cerrar el Periodo", "No se han importado los paramteros necesarios para el proceso");
        }
    }

    public void aceptarRegistro() {
        tab_tabla.getColumna("estado_cnper");
    }

    @Override
    public void insertar() {
        if (tab_tabla.getTotalFilas() == 0) {
            tab_tabla.getColumna("fecha_inicio_cnper").setLectura(false);
            tab_tabla.getColumna("fecha_fin_cnper").setLectura(false);
            tab_tabla.insertar();
            tab_tabla.setValor("estado_cnper", "true");
        } else {
            utilitario.agregarMensajeInfo("No se puede insertar", "Debe cerrar el Periodo Activo");
        }
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        if (tab_tabla.getTotalFilas() > 0) {
            if (!tab_tabla.getValor("estado_cnper").equals("true")) {
                if (!tab_tabla.getValor("cerrado_cnper").equals("true")) {
                    tab_tabla.eliminar();
                } else {
                    utilitario.agregarMensajeInfo("No se puede eliminar", "El periodo seleccionado ya se encuentra Cerrado");
                }
            } else {
                utilitario.agregarMensajeInfo("No se puede eliminar", "El periodo seleccionado se encuentra en estado Activo");
            }
        }
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public VisualizarPDF getVp() {
        return vp;
    }

    public void setVp(VisualizarPDF vp) {
        this.vp = vp;
    }

    public Dialogo getDia_cambio_fecha() {
        return dia_cambio_fecha;
    }

    public void setDia_cambio_fecha(Dialogo dia_cambio_fecha) {
        this.dia_cambio_fecha = dia_cambio_fecha;
    }

    public Dialogo getDia_cerrar_periodo() {
        return dia_cerrar_periodo;
    }

    public void setDia_cerrar_periodo(Dialogo dia_cerrar_periodo) {
        this.dia_cerrar_periodo = dia_cerrar_periodo;
    }
}

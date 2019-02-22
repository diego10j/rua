/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bancos;

import componentes.AsientoContable;
import framework.aplicacion.Columna;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.ItemMenu;
import framework.componentes.Link;
import framework.componentes.MenuPanel;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.Upload;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import org.apache.commons.io.IOUtils;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import servicios.contabilidad.ServicioComprobanteContabilidad;
import servicios.cuentas_x_cobrar.ServicioCliente;
import servicios.cuentas_x_cobrar.ServicioCuentasCxC;
import servicios.cuentas_x_cobrar.ServicioFacturaCxC;
import servicios.cuentas_x_pagar.ServicioCuentasCxP;
import servicios.cuentas_x_pagar.ServicioProveedor;
import servicios.tesoreria.ServicioTesoreria;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author dfjacome
 */
public class pre_libro_bancos extends Pantalla {

    private final MenuPanel mep_menu = new MenuPanel();
    private AutoCompletar aut_cuentas = new AutoCompletar();
    @EJB
    private final ServicioTesoreria ser_tesoreria = (ServicioTesoreria) utilitario.instanciarEJB(ServicioTesoreria.class);
    private final Calendario cal_fecha_inicio = new Calendario();
    private final Calendario cal_fecha_fin = new Calendario();
    private Tabla tab_tabla1;
    private Radio rad_cliente;
    private AsientoContable asc_asiento = new AsientoContable();

    @EJB
    private final ServicioComprobanteContabilidad ser_comp_conta = (ServicioComprobanteContabilidad) utilitario.instanciarEJB(ServicioComprobanteContabilidad.class);

    ///Cuentas por Cobrar
    ////CLIENTES
    @EJB
    private final ServicioCliente ser_cliente = (ServicioCliente) utilitario.instanciarEJB(ServicioCliente.class);
    ////CXC
    @EJB
    private final ServicioFacturaCxC ser_factura = (ServicioFacturaCxC) utilitario.instanciarEJB(ServicioFacturaCxC.class);

    ///Cuentas por Pagar
    ////PROVEEDORES
    @EJB
    private final ServicioProveedor ser_proveedor = (ServicioProveedor) utilitario.instanciarEJB(ServicioProveedor.class);
    ////CXP
    @EJB
    private final ServicioCuentasCxP ser_cuentas_cxp = (ServicioCuentasCxP) utilitario.instanciarEJB(ServicioCuentasCxP.class);
    ////CXC
    @EJB
    private final ServicioCuentasCxC ser_cuentas_cxc = (ServicioCuentasCxC) utilitario.instanciarEJB(ServicioCuentasCxC.class);

    private AutoCompletar aut_persona;
    private Calendario cal_fecha_pago;
    private AutoCompletar aut_cuenta;
    private AutoCompletar aut_cuenta1;
    private Combo com_tip_tran;
    private Texto tex_num;
    private Texto tex_diferencia;
    private Texto tex_valor_pagar;
    private Texto tex_beneficiario;
    private Texto tex_identificacion;
    private Combo com_tipo_identificacion;
    private AreaTexto ate_observacion;
    private String str_ide_geper;

    private Dialogo dia_modifica = new Dialogo();
    private Tabla tab_tabla2;

    private Radio rad_hace_asiento;
    private Texto tex_num_asiento;
    private Etiqueta eti_num_asiento;
    private Texto tex_num_comprobante;

    private Upload upl_importa;
    private Texto tex_separa;
    private Combo com_colDocumento;
    private Combo com_colValor;
    private Confirmar con_confirma = new Confirmar();

    private SeleccionTabla sel_conciliados = new SeleccionTabla();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_formato = new SeleccionFormatoReporte();

    private SeleccionCalendario sel_fechas = new SeleccionCalendario();
    private SeleccionTabla set_bancos = new SeleccionTabla();

    private SeleccionTabla set_tipo_transaccion = new SeleccionTabla();

    private Combo com_anticipos_anteriores;

    public pre_libro_bancos() {

        mep_menu.setMenuPanel("CONSULTAS", "20%");
        mep_menu.agregarItem("Posición Consolidada", "dibujarPosicion", "ui-icon-note");//1
        mep_menu.agregarItem("Consulta de Movimientos", "dibujarMovimienots", "ui-icon-note");//2
        mep_menu.agregarItem("Movimientos Anulados", "dibujarAnulados", "ui-icon-cancel");//11
        mep_menu.agregarSubMenu("TRANSACCIONES");
        mep_menu.agregarItem("Cuentas por Cobrar", "dibujarCxC", "ui-icon-contact");//3
        mep_menu.agregarItem("Cuentas por Cobrar Grupo", "dibujarCxCGrupo", "ui-icon-star");//15
        mep_menu.agregarItem("Cuentas por Pagar", "dibujarCxP", "ui-icon-contact");//4
        mep_menu.agregarItem("Anticipos a Proveedores", "dibujarAnticipo", "ui-icon-contact");//9
        mep_menu.agregarItem("Anticipos a Empleados", "dibujarAnticipoEmpleados", "ui-icon-contact");//13
        mep_menu.agregarItem("Anticipos de Clientes", "dibujarAnticipoClientes", "ui-icon-contact");//14
        mep_menu.agregarItem("Otras Transacciones", "dibujarOtros", "ui-icon-contact");//5
        mep_menu.agregarItem("Transferencias entre Cuentas", "dibujarTransferencias", "ui-icon-contact");//6
        mep_menu.agregarSubMenu("HERRAMIENTAS");
        mep_menu.agregarItem("Conciliación Manual", "dibujarConciliarM", "ui-icon-pencil"); //7
        mep_menu.agregarItem("Conciliación Automática", "dibujarConciliarA", "ui-icon-calculator");//10

        agregarComponente(mep_menu);

        aut_cuentas.setId("aut_cuentas");
        aut_cuentas.setAutocompletarContenido();
        aut_cuentas.setDropdown(true);
        aut_cuentas.setAutoCompletar(ser_tesoreria.getSqlComboCuentas());
        aut_cuentas.setMetodoChange("actualizarMovimientos");
        aut_cuentas.setGlobal(true);
        aut_cuentas.setValue(null);
        aut_cuentas.setGlobal(true);
        aut_cuentas.setMaxResults(25);

        bar_botones.limpiar();

        bar_botones.agregarReporte();

        bar_botones.agregarComponente(new Etiqueta("CUENTA :"));
        bar_botones.agregarComponente(aut_cuentas);
        bar_botones.agregarSeparador();
        bar_botones.agregarComponente(new Etiqueta("FECHA DESDE :"));

        cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
        bar_botones.agregarComponente(cal_fecha_inicio);
        bar_botones.agregarComponente(new Etiqueta("FECHA HASTA :"));

        cal_fecha_fin.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_fin);

        Boton bot_consultar = new Boton();
        bot_consultar.setMetodo("actualizarMovimientos");
        bot_consultar.setIcon("ui-icon-search");

        bar_botones.agregarBoton(bot_consultar);
        dibujarPosicion();

        asc_asiento.setId("asc_asiento");
        asc_asiento.getBot_aceptar().setMetodo("guardar");
        asc_asiento.getBot_cancelar().setMetodo("cerrarAsiento");
        agregarComponente(asc_asiento);

        dia_modifica.setId("dia_modifica");
        dia_modifica.setHeight("50%");
        dia_modifica.setWidth("40%");
        dia_modifica.setTitle("MODIFICAR MOVIMIENTO");
        dia_modifica.getBot_aceptar().setMetodo("aceptarModificar");
        agregarComponente(dia_modifica);
        con_confirma.setId("con_confirma");
        con_confirma.setMessage("Está seguro de Anular el Movimiento Seleccionado ?");
        con_confirma.setTitle("ANULAR MOVIMIENTO");
        con_confirma.getBot_aceptar().setValue("Si");
        con_confirma.getBot_cancelar().setValue("No");
        agregarComponente(con_confirma);

        sel_conciliados.setId("sel_conciliados");
        sel_conciliados.setSeleccionTabla(ser_tesoreria.getSqlTransaccionesEncontradasConciliarCuenta("-1"), "ide_teclb");
        sel_conciliados.setTitle("MOVIMIENTOS ENCONTRADOS");
        sel_conciliados.getBot_aceptar().setMetodo("aceptarSeleccionadosConciliar");
        sel_conciliados.setWidth("65%");
        sel_conciliados.setHeight("65");
        agregarComponente(sel_conciliados);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sel_formato.setId("sel_formato");
        agregarComponente(rep_reporte);
        agregarComponente(sel_formato);

        asc_asiento.setId("asc_asiento");
        asc_asiento.getBot_aceptar().setMetodo("guardar");
        agregarComponente(asc_asiento);

        sel_fechas.setId("sel_fechas");
        sel_fechas.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(sel_fechas);

        set_bancos.setId("set_bancos");
        set_bancos.setSeleccionTabla(ser_tesoreria.getSqlComboCuentas(), "ide_tecba");
        set_bancos.setWidth("50%");
        set_bancos.getBot_aceptar().setMetodo("aceptarReporte");
        set_bancos.setHeight("65%");
        set_bancos.setTitle("CUENTAS CAJAS - BANCOS");
        agregarComponente(set_bancos);

        set_tipo_transaccion.setId("set_tipo_transaccion");
        set_tipo_transaccion.setSeleccionTabla(ser_tesoreria.getSqlTipoTransaccion(), "ide_tettb");
        set_tipo_transaccion.setWidth("50%");
        set_tipo_transaccion.getBot_aceptar().setMetodo("aceptarReporte");
        set_tipo_transaccion.setHeight("65%");
        set_tipo_transaccion.setTitle("TIPOS DE TRANSACCION - BANCOS");
        agregarComponente(set_tipo_transaccion);

    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Movimientos Bancarios")) {
            if (rep_reporte.isVisible()) {
                //abre calendario                 
                rep_reporte.cerrar();
                parametro = new HashMap();
                set_bancos.dibujar();
            } else if (set_bancos.isVisible()) {
                if (set_bancos.getSeleccionados() == null || set_bancos.getSeleccionados().isEmpty()) {
                    utilitario.agregarMensajeInfo("Seleccione una cuenta caja o banco", "");
                    return;
                }
                parametro.put("ide_tecba", set_bancos.getSeleccionados());
                set_bancos.cerrar();
                sel_fechas.dibujar();
            } else if (sel_fechas.isVisible()) {
                if (sel_fechas.isFechasValidas() == true) {
                    parametro.put("fecha_inicio", sel_fechas.getFecha1String());
                    parametro.put("fecha_fin", sel_fechas.getFecha2String());

                    sel_fechas.cerrar();
                    sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_formato.dibujar();
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Movimientos Conciliados")) {
            if (rep_reporte.isVisible()) {
                //abre calendario                 
                rep_reporte.cerrar();
                parametro = new HashMap();
                set_bancos.dibujar();
            } else if (set_bancos.isVisible()) {
                if (set_bancos.getSeleccionados() == null || set_bancos.getSeleccionados().isEmpty()) {
                    utilitario.agregarMensajeInfo("Seleccione una cuenta caja o banco", "");
                    return;
                }
                parametro.put("ide_tecba", set_bancos.getSeleccionados());
                set_bancos.cerrar();
                set_tipo_transaccion.dibujar();
            } else if (set_tipo_transaccion.isVisible()) {
                if (set_tipo_transaccion.getSeleccionados() != null) {
                    parametro.put("ide_tettb", set_tipo_transaccion.getSeleccionados());
                    set_tipo_transaccion.cerrar();
                    sel_fechas.dibujar();
                }
            } else if (sel_fechas.isVisible()) {
                if (sel_fechas.isFechasValidas() == true) {
                    parametro.put("fecha_inicio", sel_fechas.getFecha1String());
                    parametro.put("fecha_fin", sel_fechas.getFecha2String());
                    sel_fechas.cerrar();
                    sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_formato.dibujar();
                }
            }

        }

    }

    public void aceptarSeleccionadosConciliar() {
        if (sel_conciliados.getSeleccionados() != null) {
            ser_tesoreria.conciliarMovimientos(sel_conciliados.getSeleccionados());
            sel_conciliados.cerrar();
        } else {
            utilitario.agregarMensajeError("Seleccione los movimientos para ser conciliados", "");
        }
    }

    public void cerrarAsiento() {
        //limpia sql guardados
        utilitario.getConexion().getSqlPantalla().clear();
        asc_asiento.cerrar();
    }

    public void abrirAnular() {
        if (tab_tabla1.getValor("ide_teclb") != null) {
            con_confirma.getBot_aceptar().setMetodo("anularMovimiento");
            con_confirma.dibujar();
        } else {
            utilitario.agregarMensajeError("Debe seleccionar un Movimiento", "");
        }
    }

    public void anularMovimiento() {
        if (tab_tabla1.getValor("ide_teclb") != null) {
            ser_tesoreria.anularMovimiento(tab_tabla1.getValor("ide_teclb"));
            if (guardarPantalla().isEmpty()) {
                con_confirma.cerrar();
                tab_tabla1.actualizar();
                actualizarSaldos();
            }
        } else {
            utilitario.agregarMensajeError("Debe seleccionar un Movimiento", "");
        }
    }

    public void dibujarAnulados() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_tesoreria.getSqlTransaccionesAnuladasCuenta(aut_cuentas.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla1.setRows(15);
        tab_tabla1.setLectura(true);
        tab_tabla1.setOrdenar(false);
        tab_tabla1.getColumna("ide_teclb").setVisible(false);
        tab_tabla1.setCampoPrimaria("ide_teclb");
        tab_tabla1.getColumna("VALOR").setLongitud(25);
        tab_tabla1.getColumna("VALOR").alinearDerecha();
        tab_tabla1.getColumna("VALOR").setEstilo("font-weight: bold;");
        tab_tabla1.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("numero_teclb").setFiltroContenido();
        tab_tabla1.getColumna("beneficiari_teclb").setFiltroContenido();
        tab_tabla1.getColumna("nombre_tettb").setFiltroContenido();
        tab_tabla1.setColumnaSuma("VALOR");
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);

        mep_menu.dibujar(11, "MOVIMIENTOS ANULADOS ", pat_panel);
    }

    public void dibujarConciliarA() {

//        Grid gri_archivo = new Grid();
//        gri_archivo.setWidth("95%");
//        gri_archivo.setColumns(3);
        upl_importa = new Upload();
        Grid gri_matriz = new Grid();
        gri_matriz.setId("grid");
        gri_matriz.setStyle("width:100%;");
        gri_matriz.setColumns(2);

        //gri_archivo.getChildren().add(gri_matriz);
        Panel pn = new Panel();
        pn.setTitle("SELECCIONAR ARCHIVO CON EXTENSIÓN .CSV ");
        Grid gri_valida = new Grid();
        gri_valida.setId("gri_valida");
        gri_valida.setColumns(3);
        Etiqueta eti_valida = new Etiqueta();
        eti_valida.setValueExpression("value", "pre_index.clase.upl_importa.nombreReal");
        eti_valida.setValueExpression("rendered", "pre_index.clase.upl_importa.nombreReal != null");
        gri_valida.getChildren().add(eti_valida);

        Imagen ima_valida = new Imagen();
        ima_valida.setWidth("22");
        ima_valida.setHeight("22");
        ima_valida.setValue("/imagenes/im_csv.png");
        ima_valida.setValueExpression("rendered", "pre_index.clase.upl_importa.nombreReal != null");
        gri_valida.getChildren().add(ima_valida);

        upl_importa.setId("upl_importa");
        upl_importa.setUpdate("gri_valida");
        upl_importa.setAllowTypes("/(\\.|\\/)(csv)$/");
        upl_importa.setMetodo("seleccionarArchivo");
        //upl_importa.setProcess("@all");
        upl_importa.setUploadLabel("Validar Archivo .csv");
        upl_importa.setAuto(false);

        Grid g2 = new Grid();
        g2.getChildren().add(new Etiqueta("<strong>SEPARADOR DE COLUMNAS: </strong><span style='color:red;font-weight: bold;'>*</span>"));
        tex_separa = new Texto();
        tex_separa.setSize(5);
        tex_separa.setValue(",");
        g2.getChildren().add(tex_separa);
        g2.setFooter(gri_valida);

        gri_matriz.getChildren().add(g2);
        gri_matriz.getChildren().add(upl_importa);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql("SELECT '' COLUMNA1,'' COLUMNA2,'' COLUMNA3 from sis_empresa WHERE IDE_EMPR=1");
        tab_tabla1.setLectura(true);
        tab_tabla1.setRows(15);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        //grid.getChildren().add(pat_panel);
        com_colDocumento = new Combo();
        com_colDocumento.setStyle("width: 210px;");
        com_colValor = new Combo();
        com_colValor.setStyle("width: 210px;");
        Grid g1 = new Grid();
        g1.getChildren().add(new Etiqueta("<strong>COLUMNA NUM. DOCUMENTO :</strong><span style='color:red;font-weight: bold;'>*</span>"));
        g1.getChildren().add(com_colDocumento);
        g1.getChildren().add(new Etiqueta("<strong>COLUMNA VALOR :</strong><span style='color:red;font-weight: bold;'>*</span>"));
        g1.getChildren().add(com_colValor);
        g1.getChildren().add(new Etiqueta("<strong>CUENTA BANCARIA : </strong> <span style='color:red;font-weight: bold;'>*</span>"));

        aut_cuenta = new AutoCompletar();
        aut_cuenta.setId("aut_cuenta");
        aut_cuenta.setAutoCompletar(ser_tesoreria.getSqlComboCuentasBancarias());
        aut_cuenta.setDropdown(true);
        aut_cuenta.setAutocompletarContenido();
        aut_cuenta.setSize(30);
        aut_cuenta.setMaxResults(25);

        g1.getChildren().add(aut_cuenta);

        Boton bot_procesar = new Boton();
        bot_procesar.setValue("Conciliar");
        bot_procesar.setMetodo("conciliacionAutmaitica");

        g1.getChildren().add(bot_procesar);

        gri_matriz.getChildren().add(g1);
        gri_matriz.getChildren().add(pat_panel);

        pn.getChildren().add(gri_matriz);
        mep_menu.dibujar(10, "CONCILIACIÓN AUTOMÁTICA", pn);

    }

    public void conciliacionAutmaitica() {
        if (com_colDocumento.getValue() == null) {
            utilitario.agregarMensajeError("Seleccione la COLUMNA NUM. DOCUMENTO", "");
            return;
        }
        if (com_colValor.getValue() == null) {
            utilitario.agregarMensajeError("Seleccione la COLUMNA VALOR", "");
            return;
        }
        if (tab_tabla1.getTotalFilas() <= 0) {
            utilitario.agregarMensajeError("No hay movimientos para Conciliar", "");
            return;
        }
        if (aut_cuenta.getValor() == null) {
            utilitario.agregarMensajeError("Debe seleccionar la CUENTA BANCARIA", "");
            return;
        }

        TablaGenerica tab_mov = utilitario.consultar(ser_tesoreria.getSqlTransaccionesConciliarCuenta(aut_cuenta.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));

//reccore la tabla 
        String str_econtrados = "";
        for (int i = 0; i < tab_tabla1.getTotalFilas(); i++) {
            double dou_valorC = 0;
            long lon_docuC = 0;
            try {
                String valorC = tab_tabla1.getValor(i, String.valueOf(com_colValor.getValue()));
                valorC = valorC.replace(",", "");
                dou_valorC = Double.parseDouble(utilitario.getFormatoNumero(valorC));
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            try {
                String num_documento = tab_tabla1.getValor(i, String.valueOf(com_colDocumento.getValue()));
                lon_docuC = Long.parseLong(num_documento);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            boolean encontro = false;
            //busca en los movimientos

            for (int j = 0; j < tab_mov.getTotalFilas(); j++) {
                double dou_valor = 0;
                long lon_docu = 0;

                try {
                    String val = tab_mov.getValor(j, "valor_teclb");
                    val = val.replace(",", "");
                    dou_valor = Double.parseDouble(utilitario.getFormatoNumero(val));
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                try {
                    String doc = tab_mov.getValor(j, "numero_teclb");
                    lon_docu = Long.parseLong(doc);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                //        System.out.println("-- " + dou_valorC + "   ...  " + dou_valor + "   ===  " + lon_docuC + " === " + lon_docu);
                if (dou_valor == dou_valorC) {
                    if (lon_docu == lon_docuC) {
                        encontro = true;
                        if (str_econtrados.isEmpty() == false) {
                            str_econtrados += ",";
                        }
                        str_econtrados += tab_mov.getValor(j, "ide_teclb");
                        break;
                    }
                }

            }

            System.out.println("---" + encontro);
            if (encontro) {
                tab_tabla1.setValor(i, "ENCONTRO", "true");
            } else {
                tab_tabla1.setValor(i, "ENCONTRO", "false");
            }

        }
        //  System.out.println("=== " + str_econtrados);
        if (str_econtrados.isEmpty() == false) {
            sel_conciliados.getTab_seleccion().setSql(ser_tesoreria.getSqlTransaccionesEncontradasConciliarCuenta(str_econtrados));
            sel_conciliados.getTab_seleccion().ejecutarSql();
            sel_conciliados.dibujar();
            sel_conciliados.seleccionarTodas();
        } else {
            utilitario.agregarMensajeError("No se ecnontraron movimientos", "El archivo no contiene movimientos de la cuenta bancaria seleccionada");
        }

        // sel_conciliados.setSeleccionTabla(ser_tesoreria.getSqlTransaccionesEncontradasConciliarCuenta("-1"), "ide_teclb");
    }

    public void seleccionarArchivo(FileUploadEvent event) {
        if (tex_separa.getValue() == null || tex_separa.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeError("Ingrese el separador de columnas del archivo", "");
            return;
        }
        try {
            final File tempFile = File.createTempFile("archivoConcilia", "csv");
            tempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                IOUtils.copy(event.getFile().getInputstream(), out);
            }
            List<String> lines = Files.readAllLines(tempFile.toPath(),
                    StandardCharsets.ISO_8859_1);
            int intFila = 0;
            //Columnas            
            String strColumnas = "";
            List listaColumnas = new ArrayList();
            if (lines.isEmpty() == false) {
                String columnas = lines.get(0);
                columnas = columnas.replace("\"", "");
                String[] array = columnas.split(tex_separa.getValue() + "");
                for (String array1 : array) {

                    if (strColumnas.isEmpty() == false) {
                        strColumnas += ",";
                    }
                    array1 = array1.replace("\"", "");
                    if (array1.contains("FECHA")) {
                        strColumnas += "'' FECHA_MOVIMIENTO";
                        Object fila[] = {"FECHA_MOVIMIENTO", "FECHA_MOVIMIENTO"};
                        listaColumnas.add(fila);
                    } else {
                        array1 = array1.replace(" ", "");
                        strColumnas += "'' " + array1;
                        Object fila[] = {array1.replace("\"", ""), array1.replace("\"", "")};
                        listaColumnas.add(fila);
                    }
                }
            }
            com_colDocumento.setCombo(listaColumnas);
            com_colValor.setCombo(listaColumnas);
            UIComponent padre = null;
            try {
                padre = tab_tabla1.getParent();
                padre.getChildren().remove(tab_tabla1);
            } catch (Exception e) {
            }

            tab_tabla1 = new Tabla();
            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setSql("SELECT 0 as ENCONTRO, " + strColumnas + " from sis_empresa WHERE IDE_EMPR=1");
            tab_tabla1.setLectura(true);
            tab_tabla1.setRows(10);
            for (Columna col : tab_tabla1.getColumnas()) {
                col.setFiltroContenido();
                col.setLongitud(30);
                col.setAncho(30);
            }
            tab_tabla1.getColumna("ENCONTRO").setVisible(false);
            tab_tabla1.setValueExpression("rowStyleClass", "fila.campos[0] eq 'false' ? 'text-red' : fila.campos[1] eq 'true'  ? 'text-green' : null");

            tab_tabla1.dibujar();
            tab_tabla1.setLectura(false);
            try {
                padre.getChildren().add(tab_tabla1);
            } catch (Exception e) {
            }

            //Filas
            for (String line : lines) {
                if (intFila == 0) {
                    intFila++;
                    continue;
                }
                tab_tabla1.insertar();

                line = line.replace("\"", "");
                String[] array = line.split(tex_separa.getValue() + "");
                int intColumna = 1;
                for (String array1 : array) {
                    tab_tabla1.setValor(tab_tabla1.getColumnas()[intColumna].getNombre(), array1.replace("\"", ""));
                    intColumna++;
                }
                intFila++;
            }
            upl_importa.setNombreReal(event.getFile().getFileName());
            utilitario.addUpdate("gri_valida,grid");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dibujarPosicion() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_tesoreria.getSqlPosicionConsolidada());
        tab_tabla1.setLectura(true);
        tab_tabla1.setRows(50);
        tab_tabla1.getColumna("ide_tecba").setVisible(false);
        tab_tabla1.getColumna("ide_cndpc").setVisible(false);
        tab_tabla1.setCampoPrimaria("ide_tecba");
        tab_tabla1.getColumna("nombre_teban").setNombreVisual("BANCO");
        tab_tabla1.getColumna("nombre_teban").setLongitud(55);
        tab_tabla1.getColumna("nombre_tecba").setNombreVisual("CUENTA");
        //tab_tabla1.getColumna("nombre_tecba").setLink();
        //tab_tabla1.getColumna("nombre_tecba").setMetodoChange("cargarMovimientosCuenta");
        tab_tabla1.getColumna("nombre_tetcb").setNombreVisual("TIPO");
        tab_tabla1.getColumna("saldo_contable").setNombreVisual("SALDO CONTABLE");
        tab_tabla1.getColumna("saldo_contable").setLongitud(25);
        tab_tabla1.getColumna("saldo_contable").alinearDerecha();
        tab_tabla1.getColumna("saldo_contable").setTipoJava("java.lang.Number");
        tab_tabla1.getColumna("saldo_disponible").setNombreVisual("SALDO DISPONIBLE");
        tab_tabla1.getColumna("saldo_disponible").alinearDerecha();
        tab_tabla1.getColumna("saldo_disponible").setLongitud(25);
        tab_tabla1.getColumna("saldo_disponible").setTipoJava("java.lang.Number");
        tab_tabla1.setHeader("CUENTAS");
        tab_tabla1.dibujar();

        if (tab_tabla1.isEmpty() == false) {
            aut_cuentas.setValor(tab_tabla1.getValor("ide_tecba"));
        }

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.setMensajeInfo(utilitario.getFechaLarga(utilitario.getFechaActual()));
        mep_menu.dibujar(1, "POSICIÓN CONSOLIDADA", pat_panel);
    }

    public void aceptarModificar() {
        tab_tabla2.modificar(0);
        if (tab_tabla2.guardar()) {
            if (utilitario.getConexion().guardarPantalla().isEmpty()) {
                dia_modifica.cerrar();
                tab_tabla1.actualizar();
            }
        }

    }

    public void dibujarMovimienots() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_tesoreria.getSqlTransaccionesCuenta(aut_cuentas.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla1.setRows(15);
        tab_tabla1.setLectura(true);
        tab_tabla1.setOrdenar(false);
        tab_tabla1.getColumna("ide_teclb").setVisible(false);
        tab_tabla1.setCampoPrimaria("ide_teclb");
        tab_tabla1.getColumna("EGRESOS").alinearDerecha();
        tab_tabla1.getColumna("EGRESOS").setLongitud(25);
        tab_tabla1.getColumna("INGRESOS").alinearDerecha();
        tab_tabla1.getColumna("INGRESOS").setLongitud(25);
        tab_tabla1.getColumna("SALDO").alinearDerecha();
        tab_tabla1.getColumna("SALDO").setLongitud(25);
        tab_tabla1.getColumna("SALDO").alinearDerecha();
        tab_tabla1.getColumna("SALDO").setSuma(false);
        tab_tabla1.getColumna("SALDO").setEstilo("font-weight: bold;");
        tab_tabla1.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("numero_teclb").setFiltroContenido();
        tab_tabla1.getColumna("beneficiari_teclb").setFiltroContenido();
        tab_tabla1.getColumna("nombre_tettb").setFiltroContenido();
        tab_tabla1.setColumnaSuma("INGRESOS,EGRESOS,SALDO");
        tab_tabla1.dibujar();
        actualizarSaldos();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);

        ItemMenu itemedita = new ItemMenu();
        itemedita.setValue("Modificar");
        itemedita.setIcon("ui-icon-pencil");
        itemedita.setMetodo("abrirModificar");
        pat_panel.getMenuTabla().getChildren().add(itemedita);

        ItemMenu itemanula = new ItemMenu();
        itemanula.setValue("Anular");
        itemanula.setIcon("ui-icon-cancel");
        itemanula.setMetodo("abrirAnular");
        pat_panel.getMenuTabla().getChildren().add(itemanula);

        tab_tabla2 = new Tabla();
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("tes_cab_libr_banc", "ide_teclb", 10);
        tab_tabla2.setCondicion("ide_teclb=-1");
        tab_tabla2.setTipoFormulario(true);
        tab_tabla2.setMostrarNumeroRegistros(false);
        tab_tabla2.getGrid().setColumns(2);
        //oculta todas las columnas
        for (Columna columna : tab_tabla2.getColumnas()) {
            columna.setVisible(false);
        }
        tab_tabla2.getColumna("ide_tecba").setVisible(true);
        tab_tabla2.getColumna("ide_tecba").setNombreVisual("CUENTA");
        tab_tabla2.getColumna("ide_tecba").setCombo(aut_cuentas.getLista());

        tab_tabla2.getColumna("beneficiari_teclb").setVisible(true);
        tab_tabla2.getColumna("beneficiari_teclb").setNombreVisual("BENEFICIARIO");
        tab_tabla2.getColumna("fecha_trans_teclb").setVisible(true);
        tab_tabla2.getColumna("fecha_trans_teclb").setNombreVisual("FECHA");
        tab_tabla2.getColumna("ide_tettb").setVisible(true);
        tab_tabla2.getColumna("ide_tettb").setNombreVisual("TIPO DE TRANSACCIÓN");
        tab_tabla2.getColumna("ide_tettb").setCombo("tes_tip_tran_banc", "ide_tettb", "nombre_tettb", "");
        tab_tabla2.getColumna("valor_teclb").setVisible(true);
        tab_tabla2.getColumna("valor_teclb").setNombreVisual("VALOR");
        tab_tabla2.getColumna("numero_teclb").setVisible(true);
        tab_tabla2.getColumna("numero_teclb").setNombreVisual("NUM. DOCUMENTO");
        tab_tabla2.getColumna("observacion_teclb").setVisible(true);
        tab_tabla2.getColumna("observacion_teclb").setNombreVisual("OBSERVACIÓN");

        tab_tabla2.dibujar();
        PanelTabla pt = new PanelTabla();
        pt.setPanelTabla(tab_tabla2);
        pt.getMenuTabla().setRendered(false);
        dia_modifica.getGri_cuerpo().getChildren().clear();
        dia_modifica.setDialogo(tab_tabla2);

        mep_menu.dibujar(2, "CONSULTA DE MOVIMIENTOS", pat_panel);
    }

    public void abrirModificar() {
        if (tab_tabla1.getFilaSeleccionada() != null) {
            tab_tabla2.setCondicion("ide_teclb=" + tab_tabla1.getFilaSeleccionada().getCampos()[tab_tabla1.getNumeroColumna("ide_teclb")]);
            tab_tabla2.ejecutarSql();
            dia_modifica.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un Moviento", "");
        }
    }

    public void dibujarCxCGrupo() {
        Grid contenido = new Grid();

        Grid gri1 = new Grid();
        gri1.setColumns(3);

        gri1.getChildren().add(new Etiqueta("<strong>&nbsp;&nbsp;&nbsp;FECHA : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta());
        gri1.getChildren().add(new Etiqueta());

        aut_persona = new AutoCompletar();
        aut_persona.setId("aut_persona");
        aut_persona.setMetodoChange("cargarCuentasporCobrar");
        aut_persona.setAutocompletarContenido();
        aut_persona.setAutoCompletar(ser_cliente.getSqlComboClientes());
        aut_persona.setSize(70);
        aut_persona.setValor(asc_asiento.getBeneficiarioEmpresa());
        cal_fecha_pago = new Calendario();
        cal_fecha_pago.setFechaActual();
        gri1.getChildren().add(cal_fecha_pago);

        Boton bt_busca = new Boton();
        bt_busca.setValue("Buscar Transacciones");
        bt_busca.setMetodo("cargarCuentasporCobrarGrupo");

        gri1.getChildren().add(bt_busca);
        gri1.getChildren().add(new Etiqueta());

        gri1.getChildren().add(new Etiqueta("<strong>A LA CUENTA : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta("<strong>TRANSACCIÓN : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta("<strong>NUM. DOCUMENTO : </strong>"));
        aut_cuenta = new AutoCompletar();
        aut_cuenta.setId("aut_cuenta");
        aut_cuenta.setMetodoChange("cambioCuenta");
        aut_cuenta.setAutoCompletar(aut_cuentas.getLista());
        aut_cuenta.setDropdown(true);
        aut_cuenta.setAutocompletarContenido();
        aut_cuenta.setSize(66);
        aut_cuenta.setMaxResults(25);

        gri1.getChildren().add(aut_cuenta);

        com_tip_tran = new Combo();
        com_tip_tran.setMetodo("cambioTipoTransBanco");
        com_tip_tran.setCombo(ser_tesoreria.getSqlTipoTransaccionPositivo());
        gri1.getChildren().add(com_tip_tran);

        tex_num = new Texto();
        tex_num.setId("tex_num");
        tex_num.setSoloEnteros();
        gri1.getChildren().add(tex_num);

        contenido.getChildren().add(gri1);

        Grid gri3 = new Grid();
        gri3.setColumns(1);
        ate_observacion = new AreaTexto();
        ate_observacion.setCols(90);
        ate_observacion.setMaxlength(190);
        ate_observacion.setRows(2);
        gri3.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri3.getChildren().add(ate_observacion);
        contenido.getChildren().add(gri3);
        contenido.getChildren().add(new Separator());

        PanelGrid gri4 = new PanelGrid();
        gri4.setColumns(4);
        Etiqueta eti_valor_cobrar = new Etiqueta();
        Etiqueta eti_diferencia_cxc = new Etiqueta();
        eti_valor_cobrar.setValue("VALOR A COBRAR $:");
        tex_diferencia = new Texto();
        tex_diferencia.setId("tex_diferencia");
        eti_diferencia_cxc.setStyle("font-size: 14px;font-weight: bold;padding-left:10px;");
        tex_diferencia.setStyle("font-size: 14px;font-weight: bold");
        tex_diferencia.setDisabled(true);
        tex_diferencia.setSoloNumeros();
        eti_diferencia_cxc.setValue("DIFERENCIA $: ");
        tex_valor_pagar = new Texto();
        // tex_valor_pagar.setSoloNumeros();
        tex_valor_pagar.setId("tex_valor_pagar");
        tex_valor_pagar.setMetodoKeyPress("CalcularDiferenciaCxC");
        tex_valor_pagar.setMetodoChange("CalcularDiferenciaCxC");
        eti_valor_cobrar.setStyle("font-size: 14px;font-weight: bold;");
        tex_valor_pagar.setStyle("font-size: 14px;font-weight: bold");
        gri4.getChildren().add(eti_valor_cobrar);
        gri4.getChildren().add(tex_valor_pagar);
        gri4.getChildren().add(eti_diferencia_cxc);
        gri4.getChildren().add(tex_diferencia);

        contenido.getChildren().add(gri4);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_seleccion");
        tab_tabla1.setSql(ser_cliente.getSqlCuentasPorCobrar(aut_cuenta.getValor()));
        tab_tabla1.getColumna("saldo_x_pagar").setEstilo("font-size: 15px;font-weight: bold;");
        tab_tabla1.getColumna("saldo_x_pagar").alinearDerecha();
        tab_tabla1.getColumna("saldo_x_pagar").setLongitud(25);
        tab_tabla1.getColumna("total_cccfa").setLongitud(25);
        tab_tabla1.getColumna("total_cccfa").alinearDerecha();
        tab_tabla1.getColumna("ide_cccfa").setVisible(false);
        tab_tabla1.getColumna("secuencial_cccfa").setLongitud(25);
        tab_tabla1.getColumna("fecha_emisi_cccfa").setNombreVisual("FECHA");
        tab_tabla1.getColumna("secuencial_cccfa").setNombreVisual("NUM. FACTURA");
        tab_tabla1.getColumna("total_cccfa").setNombreVisual("TOTAL");
        tab_tabla1.getColumna("saldo_x_pagar").setNombreVisual("SALDO");
        tab_tabla1.getColumna("observacion_cccfa").setNombreVisual("OBSERVACIÓN");

        tab_tabla1.setScrollable(true);
        tab_tabla1.setScrollHeight(utilitario.getAltoPantalla() - 380);
        tab_tabla1.setCampoPrimaria("ide_ccctr");
        tab_tabla1.setLectura(true);
        tab_tabla1.setTipoSeleccion(true);
        tab_tabla1.setCondicion("ide_ccctr=-1");
        tab_tabla1.setColumnaSuma("saldo_x_pagar");

        tab_tabla1.onSelectCheck("seleccionaFacturaCxC");
        tab_tabla1.onUnselectCheck("deseleccionaFacturaCxC");
        tab_tabla1.dibujar();

        contenido.getChildren().add(tab_tabla1);
        contenido.getChildren().add(new Separator());
        Boton bot_aceptar = new Boton();
        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setMetodo("aceptarCxC");
        contenido.getChildren().add(bot_aceptar);
        mep_menu.dibujar(5, "CUENTAS POR COBRAR EN GRUPO", contenido);
    }

    public void dibujarCxC() {
        Grid contenido = new Grid();

        Grid gri1 = new Grid();
        gri1.setColumns(3);
        gri1.getChildren().add(new Etiqueta("<strong>CLIENTE : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta("<strong>&nbsp;&nbsp;&nbsp;FECHA : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta());

        aut_persona = new AutoCompletar();
        aut_persona.setId("aut_persona");
        aut_persona.setMetodoChange("cargarCuentasporCobrar");
        aut_persona.setAutocompletarContenido();
        aut_persona.setAutoCompletar(ser_cliente.getSqlComboClientes());
        aut_persona.setSize(70);
        gri1.getChildren().add(aut_persona);

        cal_fecha_pago = new Calendario();
        cal_fecha_pago.setFechaActual();
        gri1.getChildren().add(cal_fecha_pago);
        gri1.getChildren().add(new Etiqueta());

        gri1.getChildren().add(new Etiqueta("<strong>A LA CUENTA : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta("<strong>TRANSACCIÓN : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta("<strong>NUM. DOCUMENTO : </strong>"));
        aut_cuenta = new AutoCompletar();
        aut_cuenta.setId("aut_cuenta");
        aut_cuenta.setMetodoChange("cambioCuenta");
        aut_cuenta.setAutoCompletar(aut_cuentas.getLista());
        aut_cuenta.setDropdown(true);
        aut_cuenta.setAutocompletarContenido();
        aut_cuenta.setSize(66);
        aut_cuenta.setMaxResults(25);

        gri1.getChildren().add(aut_cuenta);

        com_tip_tran = new Combo();
        com_tip_tran.setMetodo("cambioTipoTransBanco");
        com_tip_tran.setCombo(ser_tesoreria.getSqlTipoTransaccionPositivo());
        gri1.getChildren().add(com_tip_tran);

        tex_num = new Texto();
        tex_num.setId("tex_num");
        tex_num.setSoloEnteros();
        gri1.getChildren().add(tex_num);

        contenido.getChildren().add(gri1);

        Grid gri3 = new Grid();
        gri3.setColumns(1);
        ate_observacion = new AreaTexto();
        ate_observacion.setCols(90);
        ate_observacion.setMaxlength(190);
        ate_observacion.setRows(2);
        gri3.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri3.getChildren().add(ate_observacion);
        contenido.getChildren().add(gri3);
        contenido.getChildren().add(new Separator());

        PanelGrid gri4 = new PanelGrid();
        gri4.setColumns(4);
        Etiqueta eti_valor_cobrar = new Etiqueta();
        Etiqueta eti_diferencia_cxc = new Etiqueta();
        eti_valor_cobrar.setValue("VALOR A COBRAR $:");
        tex_diferencia = new Texto();
        tex_diferencia.setId("tex_diferencia");
        eti_diferencia_cxc.setStyle("font-size: 14px;font-weight: bold;padding-left:10px;");
        tex_diferencia.setStyle("font-size: 14px;font-weight: bold");
        tex_diferencia.setDisabled(true);
        tex_diferencia.setSoloNumeros();
        eti_diferencia_cxc.setValue("DIFERENCIA $: ");
        tex_valor_pagar = new Texto();
        // tex_valor_pagar.setSoloNumeros();
        tex_valor_pagar.setId("tex_valor_pagar");
        tex_valor_pagar.setMetodoKeyPress("CalcularDiferenciaCxC");
        tex_valor_pagar.setMetodoChange("CalcularDiferenciaCxC");
        eti_valor_cobrar.setStyle("font-size: 14px;font-weight: bold;");
        tex_valor_pagar.setStyle("font-size: 14px;font-weight: bold");
        gri4.getChildren().add(eti_valor_cobrar);
        gri4.getChildren().add(tex_valor_pagar);
        gri4.getChildren().add(eti_diferencia_cxc);
        gri4.getChildren().add(tex_diferencia);

        contenido.getChildren().add(gri4);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_seleccion");
        tab_tabla1.setSql(ser_cliente.getSqlCuentasPorCobrar(aut_cuenta.getValor()));
        tab_tabla1.getColumna("saldo_x_pagar").setEstilo("font-size: 15px;font-weight: bold;");
        tab_tabla1.getColumna("saldo_x_pagar").alinearDerecha();
        tab_tabla1.getColumna("saldo_x_pagar").setLongitud(25);
        tab_tabla1.getColumna("total_cccfa").setLongitud(25);
        tab_tabla1.getColumna("total_cccfa").alinearDerecha();
        tab_tabla1.getColumna("ide_cccfa").setVisible(false);
        tab_tabla1.getColumna("secuencial_cccfa").setLongitud(25);
        tab_tabla1.getColumna("fecha_emisi_cccfa").setNombreVisual("FECHA");
        tab_tabla1.getColumna("secuencial_cccfa").setNombreVisual("NUM. FACTURA");
        tab_tabla1.getColumna("total_cccfa").setNombreVisual("TOTAL");
        tab_tabla1.getColumna("saldo_x_pagar").setNombreVisual("SALDO");
        tab_tabla1.getColumna("observacion_cccfa").setNombreVisual("OBSERVACIÓN");

        tab_tabla1.setScrollable(true);
        tab_tabla1.setScrollHeight(utilitario.getAltoPantalla() - 380);
        tab_tabla1.setCampoPrimaria("ide_ccctr");
        tab_tabla1.setLectura(true);
        tab_tabla1.setTipoSeleccion(true);
        tab_tabla1.setCondicion("ide_ccctr=-1");
        tab_tabla1.setColumnaSuma("saldo_x_pagar");

        tab_tabla1.onSelectCheck("seleccionaFacturaCxC");
        tab_tabla1.onUnselectCheck("deseleccionaFacturaCxC");
        tab_tabla1.dibujar();

        contenido.getChildren().add(tab_tabla1);
        contenido.getChildren().add(new Separator());
        Boton bot_aceptar = new Boton();
        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setMetodo("aceptarCxC");
        contenido.getChildren().add(bot_aceptar);
        mep_menu.dibujar(5, "CUENTAS POR COBRAR A CLIENTES", contenido);
    }

    public void dibujarCxP() {
        Grid contenido = new Grid();
        Grid gri1 = new Grid();
        gri1.setColumns(3);
        gri1.getChildren().add(new Etiqueta("<strong>PROVEEDOR : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta("<strong>FECHA : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta());

        aut_persona = new AutoCompletar();
        aut_persona.setId("aut_persona");
        aut_persona.setMetodoChange("cargarCuentasporPagar");
        aut_persona.setAutocompletarContenido();
        aut_persona.setAutoCompletar(ser_proveedor.getSqlComboProveedor());
        aut_persona.setSize(70);
        gri1.getChildren().add(aut_persona);
        cal_fecha_pago = new Calendario();
        cal_fecha_pago.setFechaActual();
        gri1.getChildren().add(cal_fecha_pago);
        gri1.getChildren().add(new Etiqueta());

        gri1.getChildren().add(new Etiqueta("<strong>DE LA CUENTA : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta("<strong>TRANSACCIÓN : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta("<strong>NUM. DOCUMENTO : </strong>"));

        aut_cuenta = new AutoCompletar();
        aut_cuenta.setId("aut_cuenta");
        aut_cuenta.setMetodoChange("cambioCuenta");
        aut_cuenta.setAutoCompletar(aut_cuentas.getLista());
        aut_cuenta.setDropdown(true);
        aut_cuenta.setSize(66);
        aut_cuenta.setAutocompletarContenido();
        aut_cuenta.setMaxResults(25);

        gri1.getChildren().add(aut_cuenta);
        com_tip_tran = new Combo();
        com_tip_tran.setMetodo("cambioTipoTransBanco");
        com_tip_tran.setCombo(ser_tesoreria.getSqlTipoTransaccionNegativo());
        gri1.getChildren().add(com_tip_tran);
        tex_num = new Texto();
        tex_num.setId("tex_num");
        tex_num.setSoloEnteros();
        gri1.getChildren().add(tex_num);
        contenido.getChildren().add(gri1);

        Grid gri3 = new Grid();
        gri3.setColumns(1);
        ate_observacion = new AreaTexto();
        ate_observacion.setCols(90);
        ate_observacion.setMaxlength(190);
        ate_observacion.setRows(2);
        gri3.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri3.getChildren().add(ate_observacion);
        contenido.getChildren().add(gri3);
        contenido.getChildren().add(new Separator());

        PanelGrid gri4 = new PanelGrid();
        gri4.setColumns(4);
        Etiqueta eti_valor_cobrar = new Etiqueta();
        Etiqueta eti_diferencia = new Etiqueta();
        eti_valor_cobrar.setValue("VALOR A PAGAR $:");
        tex_diferencia = new Texto();
        tex_diferencia.setId("tex_diferencia");
        eti_diferencia.setStyle("font-size: 14px;font-weight: bold;padding-left:10px;");
        tex_diferencia.setStyle("font-size: 14px;font-weight: bold");
        tex_diferencia.setDisabled(true);
        tex_diferencia.setSoloNumeros();
        eti_diferencia.setValue("DIFERENCIA $: ");
        tex_valor_pagar = new Texto();
        tex_valor_pagar.setId("tex_valor_pagar");
        tex_valor_pagar.setMetodoKeyPress("calcularDiferenciaCxP");
        tex_valor_pagar.setMetodoChange("calcularDiferenciaCxP");
        eti_valor_cobrar.setStyle("font-size: 14px;font-weight: bold;");
        tex_valor_pagar.setStyle("font-size: 14px;font-weight: bold");
        gri4.getChildren().add(eti_valor_cobrar);
        gri4.getChildren().add(tex_valor_pagar);
        gri4.getChildren().add(eti_diferencia);
        gri4.getChildren().add(tex_diferencia);

        contenido.getChildren().add(gri4);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_seleccion");
        tab_tabla1.setSql(ser_proveedor.getSqlCuentasPorPagar(aut_cuenta.getValor()));
        tab_tabla1.getColumna("saldo_x_pagar").setEstilo("font-size: 15px;font-weight: bold;");
        tab_tabla1.getColumna("saldo_x_pagar").alinearDerecha();
        tab_tabla1.getColumna("saldo_x_pagar").setLongitud(25);
        tab_tabla1.getColumna("total_cpcfa").setLongitud(25);
        tab_tabla1.getColumna("total_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla1.getColumna("numero_cpcfa").setLongitud(25);
        tab_tabla1.getColumna("fecha_emisi_cpcfa").setNombreVisual("FECHA");
        tab_tabla1.getColumna("numero_cpcfa").setNombreVisual("NUM. FACTURA");
        tab_tabla1.getColumna("total_cpcfa").setNombreVisual("TOTAL");
        tab_tabla1.getColumna("saldo_x_pagar").setNombreVisual("SALDO");
        tab_tabla1.getColumna("observacion_cpcfa").setNombreVisual("OBSERVACIÓN");
        tab_tabla1.setScrollable(true);
        tab_tabla1.setScrollHeight(utilitario.getAltoPantalla() - 380);
        tab_tabla1.setCampoPrimaria("ide_cpctr");
        tab_tabla1.setLectura(true);
        tab_tabla1.setTipoSeleccion(true);
        tab_tabla1.setCondicion("ide_cpctr=-1");
        tab_tabla1.setColumnaSuma("saldo_x_pagar");
        tab_tabla1.onSelectCheck("seleccionaFacturaCxP");
        tab_tabla1.onUnselectCheck("deseleccionaFacturaCxP");
        tab_tabla1.dibujar();
        contenido.getChildren().add(tab_tabla1);
        contenido.getChildren().add(new Separator());
        Boton bot_aceptar = new Boton();
        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setMetodo("aceptarCxP");
        contenido.getChildren().add(bot_aceptar);
        mep_menu.dibujar(4, "CUENTAS POR PAGAR A PROVEEDORES", contenido);
    }

    public void dibujarAnticipo() {
        Grid contenido = new Grid();
        Grid gri1 = new Grid();
        gri1.setColumns(3);
        gri1.getChildren().add(new Etiqueta("<strong>PROVEEDOR : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta("<strong>FECHA : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta());

        aut_persona = new AutoCompletar();
        aut_persona.setId("aut_persona");
        aut_persona.setAutocompletarContenido();
        aut_persona.setAutoCompletar(ser_proveedor.getSqlComboProveedor());
        aut_persona.setSize(70);
        gri1.getChildren().add(aut_persona);
        cal_fecha_pago = new Calendario();
        cal_fecha_pago.setFechaActual();
        gri1.getChildren().add(cal_fecha_pago);
        gri1.getChildren().add(new Etiqueta());

        gri1.getChildren().add(new Etiqueta("<strong>DE LA CUENTA : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta("<strong>TRANSACCIÓN : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta("<strong>NUM. DOCUMENTO : </strong>"));

        aut_cuenta = new AutoCompletar();
        aut_cuenta.setId("aut_cuenta");
        aut_cuenta.setMetodoChange("cambioCuenta");
        aut_cuenta.setAutoCompletar(aut_cuentas.getLista());
        aut_cuenta.setDropdown(true);
        aut_cuenta.setSize(66);
        aut_cuenta.setAutocompletarContenido();
        aut_cuenta.setMaxResults(25);

        gri1.getChildren().add(aut_cuenta);
        com_tip_tran = new Combo();
        com_tip_tran.setMetodo("cambioTipoTransBanco");
        com_tip_tran.setCombo(ser_tesoreria.getSqlTipoTransaccionNegativo());
        gri1.getChildren().add(com_tip_tran);
        tex_num = new Texto();
        tex_num.setId("tex_num");
        tex_num.setSoloEnteros();
        gri1.getChildren().add(tex_num);
        contenido.getChildren().add(gri1);
        PanelGrid gri4 = new PanelGrid();
        gri4.setColumns(2);
        Etiqueta eti_valor_cobrar = new Etiqueta();
        eti_valor_cobrar.setValue("VALOR $:");
        tex_valor_pagar = new Texto();
        tex_valor_pagar.setId("tex_valor_pagar");
        tex_valor_pagar.setSoloNumeros();
        eti_valor_cobrar.setStyle("font-size: 14px;font-weight: bold;");
        tex_valor_pagar.setStyle("font-size: 14px;font-weight: bold");
        gri4.getChildren().add(eti_valor_cobrar);
        gri4.getChildren().add(tex_valor_pagar);
        contenido.getChildren().add(new Separator());
        contenido.getChildren().add(gri4);

        Grid gri3 = new Grid();
        gri3.setColumns(1);
        ate_observacion = new AreaTexto();
        ate_observacion.setCols(90);
        ate_observacion.setMaxlength(190);
        gri3.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri3.getChildren().add(ate_observacion);
        contenido.getChildren().add(gri3);

        contenido.getChildren().add(new Separator());
        Boton bot_aceptar = new Boton();
        bot_aceptar.setId("bot_aceptar");
        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setMetodo("aceptarAnticipo");
        contenido.getChildren().add(bot_aceptar);
        mep_menu.dibujar(9, "ANTICIPO A PROVEEDORES", contenido);
        //metodo of mauricio
        utilitario.buscarPermisosObjetos();
    }

    public void dibujarAnticipoEmpleados() {
        Grid contenido = new Grid();
        Grid gri1 = new Grid();
        gri1.setColumns(3);
        gri1.getChildren().add(new Etiqueta("<strong>BENEFICIARIO / EMPLEADO : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta("<strong>FECHA : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta());

        aut_persona = new AutoCompletar();
        aut_persona.setId("aut_persona");
        aut_persona.setAutocompletarContenido();
        aut_persona.setAutoCompletar(ser_cliente.getSqlComboClientes());
        aut_persona.setMetodoChange("cargarAnticiposAnteriores");
        aut_persona.setSize(70);
        gri1.getChildren().add(aut_persona);
        cal_fecha_pago = new Calendario();
        cal_fecha_pago.setFechaActual();
        gri1.getChildren().add(cal_fecha_pago);
        gri1.getChildren().add(new Etiqueta());

        gri1.getChildren().add(new Etiqueta("<strong>DE LA CUENTA : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta("<strong>TRANSACCIÓN : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta("<strong>NUM. DOCUMENTO : </strong>"));

        aut_cuenta = new AutoCompletar();
        aut_cuenta.setId("aut_cuenta");
        aut_cuenta.setMetodoChange("cambioCuenta");
        aut_cuenta.setAutoCompletar(aut_cuentas.getLista());
        aut_cuenta.setDropdown(true);
        aut_cuenta.setSize(66);
        aut_cuenta.setAutocompletarContenido();
        aut_cuenta.setMaxResults(25);

        gri1.getChildren().add(aut_cuenta);
        com_tip_tran = new Combo();
        com_tip_tran.setMetodo("cambioTipoTransBanco");
        com_tip_tran.setCombo(ser_tesoreria.getSqlTipoTransaccionNegativo());
        gri1.getChildren().add(com_tip_tran);
        tex_num = new Texto();
        tex_num.setId("tex_num");
        tex_num.setSoloEnteros();
        gri1.getChildren().add(tex_num);

        gri1.getChildren().add(new Etiqueta("<strong>ACUMULAR ANTICIPOS ANTERIORES : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta(""));
        gri1.getChildren().add(new Etiqueta(""));

        com_anticipos_anteriores = new Combo();
        com_anticipos_anteriores.setId("com_anticipos_anteriores");
        gri1.getChildren().add(com_anticipos_anteriores);

        contenido.getChildren().add(gri1);
        PanelGrid gri4 = new PanelGrid();
        gri4.setColumns(2);
        Etiqueta eti_valor_cobrar = new Etiqueta();
        eti_valor_cobrar.setValue("VALOR $:");
        tex_valor_pagar = new Texto();
        tex_valor_pagar.setId("tex_valor_pagar");
        tex_valor_pagar.setSoloNumeros();
        eti_valor_cobrar.setStyle("font-size: 14px;font-weight: bold;");
        tex_valor_pagar.setStyle("font-size: 14px;font-weight: bold");
        gri4.getChildren().add(eti_valor_cobrar);
        gri4.getChildren().add(tex_valor_pagar);
        contenido.getChildren().add(new Separator());
        contenido.getChildren().add(gri4);

        Grid gri3 = new Grid();
        gri3.setColumns(1);
        ate_observacion = new AreaTexto();
        ate_observacion.setCols(90);
        ate_observacion.setMaxlength(190);
        gri3.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri3.getChildren().add(ate_observacion);
        contenido.getChildren().add(gri3);

        contenido.getChildren().add(new Separator());
        Boton bot_aceptar = new Boton();
        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setMetodo("aceptarAnticipoEmpleado");
        bot_aceptar.setIcon("ui-icon-check");
        contenido.getChildren().add(bot_aceptar);
        mep_menu.dibujar(13, "ANTICIPO A EMPLEADOS", contenido);
        //metodo of mauricio
        utilitario.buscarPermisosObjetos();
    }

    public void cargarAnticiposAnteriores(SelectEvent evt) {
        aut_persona.onSelect(evt);
        if (aut_persona.getValor() != null) {
            com_anticipos_anteriores.setCombo(ser_factura.getSqlAnticiposEmpleado(aut_persona.getValor()));
        } else {
            com_anticipos_anteriores.setCombo(ser_factura.getSqlAnticiposEmpleado("-1"));
        }
        utilitario.addUpdate("com_anticipos_anteriores");
    }

    public void dibujarOtros() {
        str_ide_geper = null;
        Grid contenido = new Grid();
        contenido.setWidth("100%");
        contenido.setId("gri_conte_otros");
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "BENEFICIARIO"
        };
        Object fila2[] = {
            "2", "BENEFICIARIO EXTERNO"
        };
        lista.add(fila1);
        lista.add(fila2);
        rad_cliente = new Radio();
        rad_cliente.setRadio(lista);
        rad_cliente.setValue("1"); //Por defecto beneficiario
        rad_cliente.setMetodoChange("cambioTipoBeneficiario");
        contenido.getChildren().add(rad_cliente);

        Grid grid2 = new Grid();
        grid2.setColumns(2);

        grid2.getChildren().add(new Etiqueta("<strong>IDENTIFICACIÓN : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        grid2.getChildren().add(new Etiqueta("<strong>TIPO DE IDENTIFICACIÓN : </strong><span style='color:red;font-weight: bold;'>*</span>"));

        tex_identificacion = new Texto();
        tex_identificacion.setId("tex_identificacion");
        tex_identificacion.setSize(15);
        tex_identificacion.setMetodoChange("buscarPersona");
        grid2.getChildren().add(tex_identificacion);

        com_tipo_identificacion = new Combo();
        com_tipo_identificacion.setId("com_tipo_identificacion");
        com_tipo_identificacion.setCombo(ser_tesoreria.getSqlComboTipoIdentificacion());
        grid2.getChildren().add(com_tipo_identificacion);
        grid2.setRendered(false);

        Grid grid1 = new Grid();
        grid1.setColumns(3);
        grid1.getChildren().add(new Etiqueta("<strong>BENEFICIARIO : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        grid1.getChildren().add(new Etiqueta("<strong>FECHA : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        grid1.getChildren().add(new Etiqueta());

        tex_beneficiario = new Texto();
        tex_beneficiario.setId("tex_beneficiario");
        tex_beneficiario.setSize(70);
        tex_beneficiario.setRendered(false);
        grid1.getChildren().add(tex_beneficiario);

        aut_persona = new AutoCompletar();
        aut_persona.setId("aut_persona");
        aut_persona.setAutocompletarContenido();
        aut_persona.setAutoCompletar(ser_tesoreria.getSqlComboBeneficiario());
        aut_persona.setSize(70);
        grid1.getChildren().add(aut_persona);

        cal_fecha_pago = new Calendario();
        cal_fecha_pago.setFechaActual();
        grid1.getChildren().add(cal_fecha_pago);
        grid1.getChildren().add(new Etiqueta());

        grid1.getChildren().add(new Etiqueta("<strong>CUENTA : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        grid1.getChildren().add(new Etiqueta("<strong>TRANSACCIÓN : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        grid1.getChildren().add(new Etiqueta("<strong>NUM. DOCUMENTO : </strong>"));

        aut_cuenta = new AutoCompletar();
        aut_cuenta.setId("aut_cuenta");
        aut_cuenta.setMetodoChange("cambioCuenta");
        aut_cuenta.setAutoCompletar(aut_cuentas.getLista());
        aut_cuenta.setDropdown(true);
        aut_cuenta.setAutocompletarContenido();
        aut_cuenta.setSize(66);
        aut_cuenta.setMaxResults(25);

        grid1.getChildren().add(aut_cuenta);

        com_tip_tran = new Combo();
        com_tip_tran.setMetodo("cambioTipoTransBanco");
        com_tip_tran.setCombo(ser_tesoreria.getSqlTipoTransaccion());
        grid1.getChildren().add(com_tip_tran);

        tex_num = new Texto();
        tex_num.setId("tex_num");
        tex_num.setSoloEnteros();
        grid1.getChildren().add(tex_num);

        Grid gri3 = new Grid();
        gri3.setColumns(1);
        ate_observacion = new AreaTexto();
        ate_observacion.setCols(90);
        ate_observacion.setMaxlength(190);
        gri3.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri3.getChildren().add(ate_observacion);

        Grid gri = new Grid();
        gri.setId("gri");
        gri.setColumns(2);
        gri.getChildren().add(new Etiqueta("<div style='font-size:12px;font-weight: bold;'> <img src='imagenes/im_pregunta.gif' />  GENERAR NUEVO ASIENTO CONTABLE ? </div>"));
        rad_hace_asiento = new Radio();
        rad_hace_asiento.setRadio(utilitario.getListaPregunta());
        rad_hace_asiento.setValue(true);
        rad_hace_asiento.setMetodoChange("cambiaHaceAsiento");
        gri.getChildren().add(rad_hace_asiento);
        gri3.setFooter(gri);

        tex_num_asiento = new Texto();
        tex_num_asiento.setId("tex_num_asiento");
        tex_num_asiento.setSoloEnteros();
        tex_num_asiento.setRendered(false);

        eti_num_asiento = new Etiqueta();
        eti_num_asiento.setRendered(false);
        eti_num_asiento.setId("eti_num_asiento");
        eti_num_asiento.setValue("<strong>NÚMERO DE ASIENTO : :</strong>");

        gri.getChildren().add(eti_num_asiento);
        gri.getChildren().add(tex_num_asiento);

        PanelGrid gri4 = new PanelGrid();
        gri4.setColumns(2);
        Etiqueta eti_valor_cobrar = new Etiqueta();
        eti_valor_cobrar.setValue("VALOR $: ");
        tex_valor_pagar = new Texto();
        eti_valor_cobrar.setStyle("font-size: 14px;font-weight: bold;padding-left:10px;");
        tex_valor_pagar.setStyle("font-size: 14px;font-weight: bold");
        tex_valor_pagar.setSoloNumeros();
        gri4.getChildren().add(eti_valor_cobrar);
        gri4.getChildren().add(tex_valor_pagar);

        contenido.getChildren().add(grid2);
        contenido.getChildren().add(grid1);
        contenido.getChildren().add(gri4);
        contenido.getChildren().add(gri3);
        contenido.getChildren().add(new Separator());
        Boton bot_aceptar = new Boton();
        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setMetodo("aceptarOtros");
        contenido.getChildren().add(bot_aceptar);
        mep_menu.dibujar(5, "OTRAS TRANSACCIONES", contenido);
        //metodo of mauricio
        utilitario.buscarPermisosObjetos();
    }

    public void cambiaHaceAsiento() {
        if (rad_hace_asiento.getValue().equals("true")) {
            tex_num_asiento.setRendered(false);
            eti_num_asiento.setRendered(false);
        } else {
            tex_num_asiento.setRendered(true);
            eti_num_asiento.setRendered(true);
        }
        utilitario.addUpdate("gri");
    }

    public void cambioTipoBeneficiario() {
        if (rad_cliente.getValue().equals("1")) {
            tex_identificacion.getParent().setRendered(false);
            tex_beneficiario.setRendered(false);
            aut_persona.setRendered(true);
        } else {
            tex_identificacion.getParent().setRendered(true);
            tex_beneficiario.setRendered(true);
            aut_persona.setRendered(false);
        }
        utilitario.addUpdate("gri_conte_otros");
    }

    private boolean validarCedula() {
        if (com_tipo_identificacion.getValue() != null && tex_identificacion.getValue() != null) {
            if (com_tipo_identificacion.getValue().toString().equals(utilitario.getVariable("p_gen_tipo_identificacion_cedula"))) {
                if (utilitario.validarCedula(tex_identificacion.getValue().toString()) == false) {
                    utilitario.agregarMensajeError("El número de cédula no es válido", "");
                    return false;
                }
            } else if (com_tipo_identificacion.getValue().toString().equals(utilitario.getVariable("p_gen_tipo_identificacion_ruc"))) {
                if (utilitario.validarRUC(tex_identificacion.getValue().toString()) == false) {
                    utilitario.agregarMensajeError("El número de RUC no es válido", "");
                    return false;
                }
            }
        }
        return true;
    }

    public void buscarPersona() {
        str_ide_geper = null;
        if (tex_identificacion.getValue() != null) {
            if (tex_identificacion.getValue().toString().trim().isEmpty() == false) {
                TablaGenerica tab_per = ser_tesoreria.getPersonaporIdentificacion(tex_identificacion.getValue().toString().trim());
                if (tab_per.isEmpty() == false) {
                    tex_beneficiario.setValue(tab_per.getValor("nom_geper"));
                    com_tipo_identificacion.setValue(tab_per.getValor("ide_getid"));
                    str_ide_geper = tab_per.getValor("ide_geper");
                } else {
                    tex_beneficiario.limpiar();
                    com_tipo_identificacion.limpiar();
                }
                utilitario.addUpdate("tex_beneficiario,com_tipo_identificacion");
            }
        }
    }

    public void dibujarTransferencias() {
        Grid contenido = new Grid();
        contenido.setWidth("100%");

        Grid gri1 = new Grid();
        gri1.setColumns(1);
        gri1.getChildren().add(new Etiqueta("<strong>DE LA CUENTA : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        aut_cuenta = new AutoCompletar();
        aut_cuenta.setId("aut_cuenta");
        aut_cuenta.setAutoCompletar(aut_cuentas.getLista());
        //aut_cuenta.setMetodoChange("cambioCuenta");        
        aut_cuenta.setDropdown(true);
        aut_cuenta.setAutocompletarContenido();
        aut_cuenta.setSize(66);
        aut_cuenta.setMaxResults(25);
        gri1.getChildren().add(aut_cuenta);
        gri1.getChildren().add(new Etiqueta("<strong>A LA CUENTA : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        aut_cuenta1 = new AutoCompletar();
        aut_cuenta1.setId("aut_cuenta1");
        //aut_cuenta1.setMetodoChange("cambioCuenta");
        aut_cuenta1.setAutoCompletar(ser_tesoreria.getSqlComboCuentasTodas());
        aut_cuenta1.setDropdown(true);
        aut_cuenta1.setAutocompletarContenido();
        aut_cuenta1.setSize(66);
        aut_cuenta1.setMaxResults(25);
        gri1.getChildren().add(aut_cuenta1);

        cal_fecha_pago = new Calendario();
        cal_fecha_pago.setFechaActual();
        gri1.getChildren().add(new Etiqueta("<strong>FECHA : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(cal_fecha_pago);

        Grid gri2 = new Grid();
        gri2.setColumns(2);
        gri2.getChildren().add(new Etiqueta("<strong>TRANSACCIÓN : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri2.getChildren().add(new Etiqueta("<strong>NUMERO : </strong><span style='color:red;font-weight: bold;'>*</span>"));

        com_tip_tran = new Combo();
        com_tip_tran.setCombo(ser_tesoreria.getSqlTipoTransaccion());
        com_tip_tran.setValue(utilitario.getVariable("p_tes_tran_transferencia_menos"));
        com_tip_tran.setDisabled(true);
        gri2.getChildren().add(com_tip_tran);

        tex_num = new Texto();
        tex_num.setId("tex_num");
        tex_num.setSoloEnteros();
        gri2.getChildren().add(tex_num);

        PanelGrid gri4 = new PanelGrid();
        gri4.setColumns(2);
        Etiqueta eti_valor_cobrar = new Etiqueta();
        eti_valor_cobrar.setValue("VALOR $: ");
        tex_valor_pagar = new Texto();
        eti_valor_cobrar.setStyle("font-size: 14px;font-weight: bold;padding-left:10px;");
        tex_valor_pagar.setStyle("font-size: 14px;font-weight: bold");
        tex_valor_pagar.setSoloNumeros();
        gri4.getChildren().add(eti_valor_cobrar);
        gri4.getChildren().add(tex_valor_pagar);

        Grid gri3 = new Grid();
        gri3.setColumns(1);
        ate_observacion = new AreaTexto();
        ate_observacion.setCols(90);
        ate_observacion.setMaxlength(190);
        gri3.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri3.getChildren().add(ate_observacion);

        contenido.getChildren().add(gri1);
        contenido.getChildren().add(gri2);
        contenido.getChildren().add(gri4);
        contenido.getChildren().add(gri3);
        contenido.getChildren().add(new Separator());
        Boton bot_aceptar = new Boton();
        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setMetodo("aceptarTransferencia");
        contenido.getChildren().add(bot_aceptar);
        mep_menu.dibujar(6, "TRANSFERENCIAS ENTRE CUENTAS", contenido);
    }

    public void dibujarConciliarM() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_seleccion");
        tab_tabla1.setSql(ser_tesoreria.getSqlTransaccionesCuentaNoConciliado(String.valueOf(aut_cuentas.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla1.setRows(1105);
        tab_tabla1.setHeader("MOVIMIENTOS NO CONCILIADOS");
        tab_tabla1.setLectura(true);
        tab_tabla1.setOrdenar(false);
        tab_tabla1.getColumna("ide_teclb").setVisible(false);
        tab_tabla1.setCampoPrimaria("ide_teclb");
        tab_tabla1.getColumna("EGRESOS").alinearDerecha();
        tab_tabla1.getColumna("fecha_trans_teclb").setNombreVisual("FECHA");
        tab_tabla1.getColumna("numero_teclb").setNombreVisual("NUM. TRAN");
        tab_tabla1.getColumna("nombre_tettb").setNombreVisual("TRANSACCIÓN");
        tab_tabla1.getColumna("beneficiari_teclb").setNombreVisual("BENEFICIARIO");
        tab_tabla1.getColumna("beneficiari_teclb").setLongitud(50);
        tab_tabla1.getColumna("observacion_teclb").setLongitud(50);
        tab_tabla1.getColumna("observacion_teclb").setNombreVisual("OBSERVACIÓN");
        tab_tabla1.getColumna("EGRESOS").setLongitud(25);
        tab_tabla1.getColumna("INGRESOS").alinearDerecha();
        tab_tabla1.getColumna("INGRESOS").setLongitud(25);
//        tab_tabla1.getColumna("ide_cnccc").setFiltroContenido();
//        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
//        tab_tabla1.getColumna("IDE_CNCCC").setLink();
//        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
//        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("IDE_CNCCC").setVisible(false);
        tab_tabla1.setColumnaSuma("INGRESOS,EGRESOS");
        tab_tabla1.getColumna("numero_teclb").setFiltroContenido();
        tab_tabla1.getColumna("beneficiari_teclb").setFiltroContenido();
        tab_tabla1.getColumna("nombre_tettb").setFiltroContenido();
        tab_tabla1.setScrollable(true);
        tab_tabla1.setScrollHeight(utilitario.getAltoPantalla() - 300);
        tab_tabla1.setLectura(true);
        tab_tabla1.setTipoSeleccion(true);

        tab_tabla1.onSelectCheck("seleccionaConciliaM");
        tab_tabla1.onUnselectCheck("deSeleccionaConciliaM");
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);

        Etiqueta eti_saldoEstado = new Etiqueta();
        eti_saldoEstado.setStyle("font-size: 12px;font-weight: bold;padding-left:10px;");
        eti_saldoEstado.setValue("SALDO ESTADO DE CUENTA$:");

        tex_identificacion = new Texto();
        tex_identificacion.setId("tex_identificacion");
        tex_identificacion.setStyle("font-size: 12px;font-weight: bold");
        tex_identificacion.setSize(10);
        tex_identificacion.setDisabled(true);

        PanelGrid gs = new PanelGrid();
        gs.setColumns(2);
        gs.getChildren().add(eti_saldoEstado);
        gs.getChildren().add(tex_identificacion);

        pat_panel.setHeader(gs);

        Boton bot_conciliar = new Boton();
        bot_conciliar.setValue("Conciliar Selccionados");
        bot_conciliar.setMetodo("conciliarM");
        bot_conciliar.setIcon("ui-icon-check");

        PanelGrid g = new PanelGrid();
        g.setColumns(8);

        Etiqueta eti_saldoInicial = new Etiqueta();
        eti_saldoInicial.setStyle("font-size: 12px;font-weight: bold;padding-left:10px;");
        eti_saldoInicial.setValue("SALDO ANTERIOR CONCILIADO$:");

        Etiqueta eti_ingresos = new Etiqueta();
        eti_ingresos.setStyle("font-size: 12px;font-weight: bold;padding-left:10px;");
        eti_ingresos.setValue("DEPÓSITOS / CRÉDITOS $:");

        Etiqueta eti_egresos = new Etiqueta();
        eti_egresos.setStyle("font-size: 12px;font-weight: bold;padding-left:10px;");
        eti_egresos.setValue("CHEQUES / DÉBITOS $:");

        Etiqueta eti_saldo = new Etiqueta();
        eti_saldo.setStyle("font-size: 12px;font-weight: bold;padding-left:10px;");
        eti_saldo.setValue("SALDO ACTUAL$:");

        tex_num = new Texto();
        tex_num.setId("tex_num");
        tex_num.setStyle("font-size: 12px;font-weight: bold");
        tex_num.setSize(10);
        tex_num.setDisabled(true);

        tex_valor_pagar = new Texto();
        tex_valor_pagar.setId("tex_valor_pagar");
        tex_valor_pagar.setStyle("font-size: 12px;font-weight: bold");
        tex_valor_pagar.setSize(10);
        tex_valor_pagar.setDisabled(true);
        tex_diferencia = new Texto();
        tex_diferencia.setStyle("font-size: 12px;font-weight: bold");
        tex_diferencia.setId("tex_diferencia");
        tex_diferencia.setSize(10);
        tex_diferencia.setDisabled(true);

        tex_num_comprobante = new Texto();
        tex_num_comprobante.setStyle("font-size: 12px;font-weight: bold");
        tex_num_comprobante.setId("tex_num_comprobante");
        tex_num_comprobante.setSize(10);
        tex_num_comprobante.setDisabled(true);

        g.getChildren().add(eti_saldoInicial);
        g.getChildren().add(tex_num);
        g.getChildren().add(eti_ingresos);
        g.getChildren().add(tex_valor_pagar);
        g.getChildren().add(eti_egresos);
        g.getChildren().add(tex_diferencia);
        g.getChildren().add(eti_saldo);
        g.getChildren().add(tex_num_comprobante);

        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel);

        gru.getChildren().add(g);
        gru.getChildren().add(bot_conciliar);

        mep_menu.dibujar(7, "CONCILIAZIÓN MANUAL", gru);
    }

    public void seleccionaConciliaM(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        calculaConciliaM();
    }

    public void deSeleccionaConciliaM(UnselectEvent evt) {

        calculaConciliaM();
    }

    private void calculaConciliaM() {

        double ingresos = 0;
        double egresos = 0;
        double saldoAnterior = 0;
        if (tab_tabla1.getSeleccionados() != null) {
            for (Fila actual : tab_tabla1.getSeleccionados()) {
                double in = 0;
                try {
                    in = Double.parseDouble(actual.getCampos()[4] + "");
                } catch (Exception e) {
                }
                double eg = 0;
                try {
                    eg = Double.parseDouble(actual.getCampos()[5] + "");
                } catch (Exception e) {
                }
                ingresos += in;
                egresos += eg;
            }
        }

        try {
            saldoAnterior = Double.parseDouble(tex_num.getValue() + "");
        } catch (Exception e) {
        }
        tex_valor_pagar.setValue(utilitario.getFormatoNumero(ingresos));
        tex_diferencia.setValue(utilitario.getFormatoNumero(egresos));
        tex_num_comprobante.setValue(utilitario.getFormatoNumero((saldoAnterior + ingresos) - egresos));
        utilitario.addUpdate("tex_valor_pagar,tex_diferencia,tex_num_comprobante");
    }

    public void conciliarM() {

        if (tab_tabla1.getFilasSeleccionadas() != null && tab_tabla1.getFilasSeleccionadas().isEmpty() == false) {
            ser_tesoreria.conciliarMovimientos(tab_tabla1.getFilasSeleccionadas());
            utilitario.agregarMensaje("Se Guardo correctamente", tab_tabla1.getFilasSeleccionadas());
            tab_tabla1.setSql(ser_tesoreria.getSqlTransaccionesCuentaNoConciliado(aut_cuentas.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla1.ejecutarSql();
        } else {
            utilitario.agregarMensajeError("Debe seleccionar Movimientos", "");
        }

    }

    private void generarAsiento(String ide_teclb) {
        asc_asiento.nuevoAsiento();
        asc_asiento.dibujar();
        if (ate_observacion != null) {
            if (ate_observacion.getValue() != null) {
                asc_asiento.getTab_cabe_asiento().setValor("observacion_cnccc", ate_observacion.getValue().toString());
            }
        }
        asc_asiento.setAsientoLibroBancos(ide_teclb);

        //Todos tienen tipo transaccion y cuenta
        if (mep_menu.getOpcion() != 6 && mep_menu.getOpcion() != 10) {
            asc_asiento.getTab_deta_asiento().insertar();
            if (ser_tesoreria.getSignoTransaccion(com_tip_tran.getValue().toString()).equals("1")) {
                //DEBE                
                asc_asiento.getTab_deta_asiento().setValor("ide_cnlap", asc_asiento.getLugarAplicaDebe());
                asc_asiento.getTab_cabe_asiento().setValor("ide_cntcm", asc_asiento.getTipoComprobanteIngreso());
            } else {
                //HABER
                asc_asiento.getTab_deta_asiento().setValor("ide_cnlap", asc_asiento.getLugarAplicaHaber());
                asc_asiento.getTab_cabe_asiento().setValor("ide_cntcm", asc_asiento.getTipoComprobanteEgreso());
            }
            //////////
            if (str_ide_geper != null) {
                try {
                    Object obj_persona[] = new Object[3];
                    obj_persona[0] = str_ide_geper;
                    obj_persona[1] = tex_beneficiario.getValue().toString();
                    obj_persona[2] = tex_identificacion.getValue().toString();
                    asc_asiento.getTab_cabe_asiento().getColumna("ide_geper").getListaCombo().add(obj_persona);
                    asc_asiento.getTab_cabe_asiento().setValor("ide_geper", str_ide_geper);
                } catch (Exception e) {
                    asc_asiento.getTab_cabe_asiento().setValor("ide_geper", utilitario.getVariable("p_con_beneficiario_empresa"));//sociedad salesianos                
                }

            } else if (aut_persona != null) {
                asc_asiento.getTab_cabe_asiento().setValor("ide_geper", aut_persona.getValor());
            } else {
                asc_asiento.getTab_cabe_asiento().setValor("ide_geper", utilitario.getVariable("p_con_beneficiario_empresa"));//sociedad salesianos                
            }

            if (asc_asiento.getTab_cabe_asiento().getValor("ide_geper") == null) {
                asc_asiento.getTab_cabe_asiento().setValor("ide_geper", utilitario.getVariable("p_con_beneficiario_empresa"));//sociedad salesianos                
            }

            asc_asiento.getTab_deta_asiento().setValor("ide_cndpc", ser_tesoreria.getCuentaContable(aut_cuenta.getValor()));

            if (mep_menu.getOpcion() == 5 && (tab_tabla2 != null && tab_tabla2.getSumaColumna("valor_ccdtr") > 0)) {
                double valor_adicional = tab_tabla2.getSumaColumna("valor_ccdtr");
                double valor_pagar = Double.parseDouble(tex_valor_pagar.getValue().toString());
                asc_asiento.getTab_deta_asiento().setValor("valor_cndcc", utilitario.getFormatoNumero(valor_adicional + valor_pagar));
            } else {
                asc_asiento.getTab_deta_asiento().setValor("valor_cndcc", utilitario.getFormatoNumero(tex_valor_pagar.getValue().toString()));
            }
            /////////////DETALLE ASIENTO
            //si es cuenta por cobrar
            if (mep_menu.getOpcion() == 5) {
                asc_asiento.getTab_deta_asiento().insertar();
                asc_asiento.getTab_deta_asiento().setValor("ide_cnlap", asc_asiento.getLugarAplicaHaber());
                asc_asiento.getTab_deta_asiento().setValor("ide_cndpc", ser_cliente.getCuentaCliente(aut_persona.getValor()));
                if (tab_tabla2 != null && tab_tabla2.getSumaColumna("valor_ccdtr") < 0) {
                    double valor_adicional = tab_tabla2.getSumaColumna("valor_ccdtr");
                    double valor_pagar = Double.parseDouble(tex_valor_pagar.getValue().toString());
                    asc_asiento.getTab_deta_asiento().setValor("valor_cndcc", utilitario.getFormatoNumero(Math.abs(valor_adicional) + valor_pagar));
                } else {
                    asc_asiento.getTab_deta_asiento().setValor("valor_cndcc", utilitario.getFormatoNumero(tex_valor_pagar.getValue().toString()));
                }
                if (tab_tabla2 != null) {
                    for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
                        double valor_ad = 0;
                        try {
                            valor_ad = Double.parseDouble(tab_tabla2.getValor(i, "valor_ccdtr"));
                        } catch (Exception ex) {

                        }
                        if (valor_ad != 0) {
                            asc_asiento.getTab_deta_asiento().insertar();
                            if (valor_ad > 0) {
                                asc_asiento.getTab_deta_asiento().setValor("ide_cnlap", asc_asiento.getLugarAplicaHaber());
                            } else {
                                asc_asiento.getTab_deta_asiento().setValor("ide_cnlap", asc_asiento.getLugarAplicaDebe());
                            }
                            asc_asiento.getTab_deta_asiento().setValor("ide_cndpc", tab_tabla2.getValor(i, "ide_cnccc"));
                            asc_asiento.getTab_deta_asiento().setValor("valor_cndcc", utilitario.getFormatoNumero(Math.abs(valor_ad)));
                        }

                    }
                }
            }

            //si es cuenta por pagar
            if (mep_menu.getOpcion() == 4) {
                asc_asiento.getTab_deta_asiento().insertar();
                asc_asiento.getTab_deta_asiento().setValor("ide_cnlap", asc_asiento.getLugarAplicaDebe());
                asc_asiento.getTab_deta_asiento().setValor("ide_cndpc", ser_proveedor.getCuentaProveedor(aut_persona.getValor()));
                asc_asiento.getTab_deta_asiento().setValor("valor_cndcc", utilitario.getFormatoNumero(tex_valor_pagar.getValue().toString()));
            }

            asc_asiento.calcularTotal();
            if (com_tip_tran.getValue().equals(utilitario.getVariable("p_tes_tran_cheque"))) {
                asc_asiento.setReporteCheque();
            } else {
                asc_asiento.setReporteComprobante();
            }
        } else {
            //asiento de transferencias entre cuentas y depositos de cajas
            asc_asiento.getTab_cabe_asiento().setValor("ide_geper", utilitario.getVariable("p_con_beneficiario_empresa"));//sociedad salesianos                
            asc_asiento.getTab_cabe_asiento().setValor("ide_cntcm", asc_asiento.getTipoComprobanteDiario());
            asc_asiento.getTab_deta_asiento().insertar();
            asc_asiento.getTab_deta_asiento().setValor("ide_cnlap", asc_asiento.getLugarAplicaHaber());
            asc_asiento.getTab_deta_asiento().setValor("ide_cndpc", ser_tesoreria.getCuentaContable(aut_cuenta.getValor()));
            asc_asiento.getTab_deta_asiento().setValor("valor_cndcc", utilitario.getFormatoNumero(tex_valor_pagar.getValue().toString()));
            asc_asiento.getTab_deta_asiento().insertar();
            asc_asiento.getTab_deta_asiento().setValor("ide_cnlap", asc_asiento.getLugarAplicaDebe());
            asc_asiento.getTab_deta_asiento().setValor("ide_cndpc", ser_tesoreria.getCuentaContable(aut_cuenta1.getValor()));
            asc_asiento.getTab_deta_asiento().setValor("valor_cndcc", utilitario.getFormatoNumero(tex_valor_pagar.getValue().toString()));
            asc_asiento.calcularTotal();
        }

        asc_asiento.getTab_cabe_asiento().setValor("fecha_trans_cnccc", cal_fecha_pago.getFecha()); //dfj

    }

    private void actualizarSaldos() {
        if (aut_cuentas.getValor() != null) {
            double saldo_anterior = ser_tesoreria.getSaldoInicialCuenta(aut_cuentas.getValor(), cal_fecha_inicio.getFecha());
            double dou_saldo_inicial = saldo_anterior;
            double dou_saldo_actual = 0;
            for (int i = 0; i < tab_tabla1.getTotalFilas(); i++) {
                if (tab_tabla1.getValor(i, "ingresos") != null) {
                    dou_saldo_actual = saldo_anterior + Double.parseDouble(tab_tabla1.getValor(i, "ingresos"));
                } else {
                    dou_saldo_actual = saldo_anterior - Double.parseDouble(tab_tabla1.getValor(i, "egresos"));
                }
                tab_tabla1.setValor(i, "saldo", utilitario.getFormatoNumero(dou_saldo_actual));
                saldo_anterior = dou_saldo_actual;
            }
            if (tab_tabla1.isEmpty()) {
                dou_saldo_actual = dou_saldo_inicial;
                tab_tabla1.setEmptyMessage("No existen Transacciones en el rango de fechas seleccionado");
            }
            //INSERTA PRIMERA FILA SALDO INICIAL
            if (dou_saldo_inicial != 0) {
                tab_tabla1.setLectura(false);
                tab_tabla1.insertar();
                tab_tabla1.setValor("saldo", utilitario.getFormatoNumero(dou_saldo_inicial));
                tab_tabla1.setValor("NOMBRE_TETTB", "SALDO INICIAL");
                tab_tabla1.setValor("OBSERVACION_TECLB", "SALDO INICIAL AL " + cal_fecha_inicio.getFecha());
                tab_tabla1.setValor("FECHA_TRANS_TECLB", cal_fecha_inicio.getFecha());
                tab_tabla1.setLectura(true);
            }
            utilitario.addUpdate("tex_saldo_inicial,tex_saldo_final");
            //ASIGNA SALDOS FINALES
            tab_tabla1.getColumna("saldo").setTotal(dou_saldo_actual);
        }
    }

    public void actualizarMovimientos(SelectEvent evt) {
        aut_cuentas.onSelect(evt);
        actualizarMovimientos();
    }

    public void actualizarMovimientos() {
        if (mep_menu.getOpcion() == 2) {
            if (aut_cuentas.getValor() != null) {
                tab_tabla1.setSql(ser_tesoreria.getSqlTransaccionesCuenta(aut_cuentas.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
                tab_tabla1.ejecutarSql();
            } else {
                tab_tabla1.limpiar();
                utilitario.agregarMensajeInfo("Seleccione una Cuenta", "Debe seleccionar una 'CUENTA'");
            }
            actualizarSaldos();
        } else if (mep_menu.getOpcion() == 7) {
            tab_tabla1.setSql(ser_tesoreria.getSqlTransaccionesCuentaNoConciliado(String.valueOf(aut_cuentas.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla1.ejecutarSql();
            tex_num.setValue(utilitario.getFormatoNumero(ser_tesoreria.getSaldoInicialConciliadoCuenta(String.valueOf(aut_cuentas.getValue()), cal_fecha_fin.getFecha())));
            tex_diferencia.setValue(utilitario.getFormatoNumero("0"));
            tex_valor_pagar.setValue(utilitario.getFormatoNumero("0"));
            tex_num_comprobante.setValue(utilitario.getFormatoNumero("0"));
            tex_identificacion.setValue(utilitario.getFormatoNumero(ser_tesoreria.getSaldoInicialEstadoCuenta(String.valueOf(aut_cuentas.getValue()), cal_fecha_fin.getFecha())));
            utilitario.addUpdate("tex_num,tex_diferencia,tex_valor_pagar,tex_num_comprobante,tex_identificacion");

        } else if (mep_menu.getOpcion() == 11) {
            if (aut_cuentas.getValor() != null) {
                tab_tabla1.setSql(ser_tesoreria.getSqlTransaccionesAnuladasCuenta(aut_cuentas.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
                tab_tabla1.ejecutarSql();
            } else {
                tab_tabla1.limpiar();
                utilitario.agregarMensajeInfo("Seleccione una Cuenta", "Debe seleccionar una 'CUENTA'");
            }
        } else {
            dibujarMovimienots();
        }
    }

    @Override
    public void insertar() {

    }

    @Override
    public void guardar() {
        if (asc_asiento.isVisible()) {
            asc_asiento.guardar();
            if (asc_asiento.isVisible() == false) {
                utilitario.agregarMensaje("Se Guardo correctamente", "");
            }
        }
    }

    @Override
    public void eliminar() {

    }

    @Override
    public void actualizar() {
        actualizarMovimientos();
    }

    //////////////CXP
    /**
     * Carga las facturas por Cobrar cuando se selecciona un cliente del
     * autocompletar
     *
     * @param evt
     */
    public void cargarCuentasporPagar(SelectEvent evt) {
        aut_persona.onSelect(evt);
        tab_tabla1.setSql(ser_proveedor.getSqlCuentasPorPagar(aut_persona.getValor()));
        tab_tabla1.ejecutarSql();
        tex_diferencia.setValue(utilitario.getFormatoNumero(0));
        tex_valor_pagar.setValue(utilitario.getFormatoNumero(0));
        if (tab_tabla1.isEmpty()) {
            utilitario.agregarMensajeError("El Proveedor seleccionado no tiene cuentas por pagar", "");
        }
    }

    public void deseleccionaFacturaCxP(UnselectEvent evt) {
        double total = 0;
        for (Fila actual : tab_tabla1.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[5] + "") + total;
        }
        tex_valor_pagar.setValue(utilitario.getFormatoNumero(total));
        utilitario.addUpdate("tex_valor_pagar");
        calcularDiferenciaCxP();
    }

    public void seleccionaFacturaCxP(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        double total = 0;
        for (Fila actual : tab_tabla1.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[5] + "") + total;
        }
        tex_valor_pagar.setValue(utilitario.getFormatoNumero(total));
        utilitario.addUpdate("tex_valor_pagar");
        calcularDiferenciaCxP();
    }

    public void calcularDiferenciaCxP() {
        double diferencia = 0;
        if (tex_valor_pagar.getValue() != null) {
            if (!tex_valor_pagar.getValue().toString().isEmpty()) {
                if (tab_tabla1.getTotalFilas() > 0) {
                    try {
                        diferencia = tab_tabla1.getSumaColumna("saldo_x_pagar") - Double.parseDouble(tex_valor_pagar.getValue().toString());
                    } catch (Exception e) {
                        tex_valor_pagar.setValue(utilitario.getFormatoNumero("0"));
                        utilitario.addUpdate("tex_valor_pagar");
                    }
                    tex_diferencia.setValue(utilitario.getFormatoNumero(diferencia));
                }
            }
        } else if (tab_tabla1.getTotalFilas() > 0) {
            diferencia = tab_tabla1.getSumaColumna("saldo_x_pagar");
            tex_diferencia.setValue(utilitario.getFormatoNumero(diferencia));
        }
        utilitario.addUpdate("tex_diferencia");
    }

    public void aceptarCxP() {
        if (validarCxP()) {
            String ide_teclb = cargarPagoCxP(Double.parseDouble(tex_valor_pagar.getValue().toString()));
            //11/02/2019 Control por si genera error al guardar la transacción
            if (ide_teclb == null) {
                ide_teclb = "";
            }
            int num = -1;
            try {
                num = Integer.parseInt(ide_teclb);
            } catch (Exception e) {
                num = -1;
            }

            if (num > 0) {
                generarAsiento(ide_teclb);
                dibujarCxP();
            } else {
                utilitario.agregarMensajeError("Error al guardar la transacción", "Cuentas por Pagar");
            }
        }
    }

    public void aceptarOtros() {
        if (validarOtros()) {
            if (aut_persona.isRendered() == false) {
                if (str_ide_geper == null) {
                    //Crea el beneficiario
                    str_ide_geper = ser_tesoreria.crearBeneficiario(tex_identificacion.getValue().toString(), com_tipo_identificacion.getValue().toString(), tex_beneficiario.getValue().toString());
                }
            } else {
                tex_beneficiario.setValue(aut_persona.getValorArreglo(2));
                tex_identificacion.setValue(aut_persona.getValorArreglo(1));
            }

            String ide_teclb = ser_tesoreria.generarLibroBanco(tex_beneficiario.getValue().toString(), cal_fecha_pago.getFecha(),
                    com_tip_tran.getValue().toString(), aut_cuenta.getValor(), Double.parseDouble(tex_valor_pagar.getValue().toString()), String.valueOf(ate_observacion.getValue()), String.valueOf(tex_num.getValue()), String.valueOf(tex_num_asiento.getValue()));
            if (rad_hace_asiento.getValue().toString().equalsIgnoreCase("true")) {
                //11/02/2019 Control por si genera error al guardar la transacción
                if (ide_teclb != null) {
                    generarAsiento(ide_teclb);
                    dibujarOtros();
                } else {
                    utilitario.agregarMensajeError("Error al guardar la transacción", "Otras transacciones");
                }
            } else {
                guardarPantalla();
                dibujarOtros();
            }
        }
    }

    public void aceptarAnticipoEmpleado() {
        if (validarAnticipoEmpleado()) {
            TablaGenerica tab_libro = ser_tesoreria.generarTablaLibroBanco(aut_persona.getValorArreglo(2), cal_fecha_pago.getFecha(),
                    com_tip_tran.getValue().toString(), aut_cuenta.getValor(), Double.parseDouble(tex_valor_pagar.getValue().toString()), String.valueOf(ate_observacion.getValue()), String.valueOf(tex_num.getValue()), null);
            //Generar transaccion anticipo cxc 
            ser_factura.generarTransaccionAnticipo(aut_persona.getValor(), tab_libro, String.valueOf(com_anticipos_anteriores.getValue()));
            generarAsiento(tab_libro.getValor("ide_teclb"));
        }
    }

    public void aceptarAnticipo() {
        if (validarAnticipo()) {
            TablaGenerica tab_libro = ser_tesoreria.generarTablaLibroBanco(aut_persona.getValorArreglo(2), cal_fecha_pago.getFecha(),
                    com_tip_tran.getValue().toString(), aut_cuenta.getValor(), Double.parseDouble(tex_valor_pagar.getValue().toString()), String.valueOf(ate_observacion.getValue()), String.valueOf(tex_num.getValue()), null);
            //Generar transaccion anticipo cxc 
            ser_cuentas_cxp.generarTransaccionAnticipo(aut_persona.getValor(), tab_libro);
            generarAsiento(tab_libro.getValor("ide_teclb"));
        }
    }

    public void aceptarTransferencia() {
        if (validarTransferencia()) {
            String ide_teclb = ser_tesoreria.generarLibroBancoTransferir(cal_fecha_pago.getFecha(),
                    com_tip_tran.getValue().toString(), aut_cuenta.getValor(), aut_cuenta1.getValor(), Double.parseDouble(tex_valor_pagar.getValue().toString()), ate_observacion.getValue().toString(), tex_num.getValue().toString());
            generarAsiento(ide_teclb);
            dibujarTransferencias();
        }
    }

    public String cargarPagoCxP(double total_a_pagar) {

        List lis_fact_pagadas = new ArrayList();
        for (int i = 0; i < tab_tabla1.getListaFilasSeleccionadas().size(); i++) {
            double monto_sobrante = 0;
            double valor_x_pagar = Double.parseDouble(tab_tabla1.getListaFilasSeleccionadas().get(i).getCampos()[5] + "");
            if (valor_x_pagar > 0) {
                if (total_a_pagar >= valor_x_pagar) {
                    Object fila[] = {tab_tabla1.getListaFilasSeleccionadas().get(i).getCampos()[1], tab_tabla1.getListaFilasSeleccionadas().get(i).getRowKey(), tab_tabla1.getListaFilasSeleccionadas().get(i).getCampos()[5]};
                    lis_fact_pagadas.add(fila);
                    monto_sobrante = total_a_pagar - valor_x_pagar;
                    if (tab_tabla1.getListaFilasSeleccionadas().get(i).getCampos()[1] != null) {
                        //ACTUALIZA LA FACTURA A PAGADA
                        utilitario.getConexion().agregarSqlPantalla(ser_cuentas_cxp.getSqlActualizaPagoDocumento(String.valueOf(tab_tabla1.getListaFilasSeleccionadas().get(i).getCampos()[1])));
                    }
                } else {
                    monto_sobrante = total_a_pagar - valor_x_pagar;
                    if (monto_sobrante <= valor_x_pagar) {
                        System.out.println(monto_sobrante + "    *** BREAK  " + valor_x_pagar + "  VALOR A PAGAR  " + total_a_pagar);
                        Object fila[] = {tab_tabla1.getListaFilasSeleccionadas().get(i).getCampos()[1], tab_tabla1.getListaFilasSeleccionadas().get(i).getRowKey(), utilitario.getFormatoNumero(total_a_pagar)};
                        lis_fact_pagadas.add(fila);
                        break;
                    }
                }
                total_a_pagar = monto_sobrante;
            } else {
                break;
            }
        }
        //ORDENA DE MENOR A MAYOR SALDO
        for (int i = 0; i < lis_fact_pagadas.size(); i++) {
            for (int j = (i + 1); j < lis_fact_pagadas.size(); j++) {
                Object[] obj_filai = (Object[]) lis_fact_pagadas.get(i);
                Object[] obj_filaj = (Object[]) lis_fact_pagadas.get(j);
                double dou_saldoi = Double.parseDouble(String.valueOf(obj_filai[2]));
                double dou_saldoj = Double.parseDouble(String.valueOf(obj_filaj[2]));
                if (dou_saldoj < dou_saldoi) {
                    Object[] obj_aux = obj_filai;
                    lis_fact_pagadas.set(i, obj_filaj);
                    lis_fact_pagadas.set(j, obj_aux);
                }
            }
        }
        String ide_teclb = ser_tesoreria.generarLibroBanco(aut_persona.getValorArreglo(2), cal_fecha_pago.getFecha(),
                com_tip_tran.getValue().toString(), aut_cuenta.getValor(), Double.parseDouble(tex_valor_pagar.getValue().toString()), String.valueOf(ate_observacion.getValue()), String.valueOf(tex_num.getValue()), null);

        for (Object lis_fact_pagada : lis_fact_pagadas) {
            Object[] obj_fila = (Object[]) lis_fact_pagada;
            System.out.println("ide_cpcfa " + obj_fila[0] + " ide_cpctr " + obj_fila[1] + " valor " + obj_fila[2]);
            if (obj_fila[0] != null) {
                //Actualiza cxp_detall_transa libro banco generado
                utilitario.getConexion().agregarSqlPantalla("UPDATE cxp_detall_transa SET ide_teclb=" + ide_teclb + " WHERE ide_cpcfa =" + obj_fila[0] + " and ide_teclb is null");
            }
            String ide_ccctr = String.valueOf(obj_fila[1]);
            //TRANSACCION EN TESORERIA y TRANSACCION CXP
            TablaGenerica tab_cabecera = utilitario.consultar(ser_cuentas_cxp.getSqlCabeceraDocumento(String.valueOf(obj_fila[0])));
            ser_cuentas_cxp.generarTransaccionPago(tab_cabecera, ide_ccctr, ide_teclb, Double.parseDouble(String.valueOf(obj_fila[2])), String.valueOf(ate_observacion.getValue()), String.valueOf(tex_num.getValue()));
        }
        // utilitario.getConexion().setImprimirSqlConsola(true);
        //utilitario.getConexion().guardarPantalla();
        return ide_teclb;
    }

    /**
     * Validaciones Otras Transacciones
     *
     * @return
     */
    public boolean validarOtros() {
        if (com_tip_tran.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un 'TIPO DE TRANSACCIÓN' ", "");
            return false;
        }

        if (aut_cuenta.getValor() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar una 'CUENTA' ", "");
            return false;
        }

        if (tex_beneficiario.isRendered()) {
            if (tex_identificacion.getValue() == null || tex_identificacion.getValue().toString().isEmpty()) {
                utilitario.agregarMensajeInfo("Debe ingresar la 'IDENTIFICACIÓN' ", "");
                return false;
            }

            if (com_tipo_identificacion.getValue() == null) {
                utilitario.agregarMensajeInfo("Debe seleccionar una 'TIPO DE IDENTIFICACIÓN' ", "");
                return false;
            }

            if (tex_beneficiario.getValue() == null || tex_beneficiario.getValue().toString().isEmpty()) {
                utilitario.agregarMensajeInfo("Debe ingresar un 'BENEFICIARIO' ", "");
                return false;
            }
        } else if (aut_persona.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un 'BENEFICIARIO' ", "");
            return false;
        }

        if (ate_observacion.getValue() == null || ate_observacion.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar una 'OBSERVACIÓN' ", "");
            return false;
        }

        if (validarCedula() == false) {
            return false;
        }

        if (tex_valor_pagar.getValue() == null || tex_valor_pagar.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar un 'VALOR'", "");
            return false;
        } else {
            try {
                if (Double.parseDouble(tex_valor_pagar.getValue().toString()) <= 0) {
                    utilitario.agregarMensajeError("El 'VALOR' no es válido", "");
                    return false;
                }
            } catch (Exception e) {
                utilitario.agregarMensajeError("El 'VALOR' no es válido", "");
                return false;
            }
        }

        if (tex_num_asiento.isRendered()) {
            if (tex_num_asiento.getValue() != null) {
                if (tex_num_asiento.getValue().toString().isEmpty() == false) {
                    TablaGenerica tab_asiento = ser_comp_conta.getCabeceraComprobante(tex_num_asiento.getValue().toString());
                    if (tab_asiento.isEmpty()) {
                        utilitario.agregarMensajeError("El asiento contable Num. " + tex_num_asiento.getValue() + " no existe", "");
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Validaciones de la Transferencia
     *
     * @return
     */
    public boolean validarTransferencia() {
        if (aut_cuenta.getValor() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar una 'CUENTA ORIGEN' ", "");
            return false;
        }

        if (aut_cuenta1.getValor() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar una 'CUENTA DESTINO' ", "");
            return false;
        }

        if (ate_observacion.getValue() == null || ate_observacion.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar una 'OBSERVACIÓN' ", "");
            return false;
        }

        if (tex_valor_pagar.getValue() == null || tex_valor_pagar.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar un 'VALOR'", "");
            return false;
        } else {
            try {
                if (Double.parseDouble(tex_valor_pagar.getValue().toString()) <= 0) {
                    utilitario.agregarMensajeError("El 'VALOR' no es válido", "");
                    return false;
                }
            } catch (Exception e) {
                utilitario.agregarMensajeError("El 'VALOR' no es válido", "");
                return false;
            }
        }
        return true;
    }

    /**
     * Validaciones de la Transaccion CXC de Pago
     *
     * @return
     */
    public boolean validarCxP() {
        if (com_tip_tran.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un 'TIPO DE TRANSACCIÓN' ", "");
            return false;
        }
        if (ate_observacion.getValue() == null || ate_observacion.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar una 'OBSERVACIÓN' ", "");
            return false;
        }

        if (aut_cuenta.getValor() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar una 'CUENTA' ", "");
            return false;
        }

        if (aut_persona.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un 'PROVEEDOR' ", "");
            return false;
        }

        if (tab_tabla1.isEmpty()) {
            utilitario.agregarMensajeInfo("El Proveedor seleccionado no tiene Cuentas por Pagar ", "");
            return false;
        }

        if (tab_tabla1.getListaFilasSeleccionadas().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un Documento por Pagar", "");
            return false;
        }
        if (tex_valor_pagar.getValue() == null || tex_valor_pagar.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar el 'VALOR A PAGAR'", "");
            return false;
        } else {
            try {
                if (Double.parseDouble(tex_valor_pagar.getValue().toString()) <= 0) {
                    utilitario.agregarMensajeError("El 'VALOR A PAGAR' no es válido", "");
                    return false;
                }
            } catch (Exception e) {
                utilitario.agregarMensajeError("El 'VALOR A PAGAR' no es válido", "");
                return false;
            }
        }

        if (tex_diferencia.getValue() != null) {
            try {
                if (Double.parseDouble(tex_diferencia.getValue().toString()) < 0) {
                    utilitario.agregarMensajeError("El 'VALOR A PAGAR' es mayor al saldo total de la cuenta por cobrar", "");
                    return false;
                }
            } catch (Exception e) {
                utilitario.agregarMensajeError("El 'VALOR A PAGAR' no es válido", "");
                return false;
            }
        }

        double total = 0;
        for (Fila actual : tab_tabla1.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[5] + "") + total;
        }
        total = Double.parseDouble(utilitario.getFormatoNumero(total));
        double valor_a_pagar = Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(tex_valor_pagar.getValue() + "")));
        System.out.println("total " + total + " valor a pagar " + valor_a_pagar);
        if (total < valor_a_pagar) {
            utilitario.agregarMensajeError("El 'VALOR A PAGAR' es menor que el saldo de las Facturas Seleccionadas", "");
            return false;
        }
        return true;
    }

    public boolean validarAnticipo() {
        if (com_tip_tran.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un 'TIPO DE TRANSACCIÓN' ", "");
            return false;
        }
        if (ate_observacion.getValue() == null || ate_observacion.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar una 'OBSERVACIÓN' ", "");
            return false;
        }

        if (aut_cuenta.getValor() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar una 'CUENTA' ", "");
            return false;
        }

        if (aut_persona.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un 'PROVEEDOR' ", "");
            return false;
        }

        if (tex_valor_pagar.getValue() == null || tex_valor_pagar.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar el 'VALOR A PAGAR'", "");
            return false;
        } else {
            try {
                if (Double.parseDouble(tex_valor_pagar.getValue().toString()) <= 0) {
                    utilitario.agregarMensajeError("El 'VALOR A PAGAR' no es válido", "");
                    return false;
                }
            } catch (Exception e) {
                utilitario.agregarMensajeError("El 'VALOR A PAGAR' no es válido", "");
                return false;
            }
        }

        return true;
    }

    public boolean validarAnticipoEmpleado() {
        if (com_tip_tran.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un 'TIPO DE TRANSACCIÓN' ", "");
            return false;
        }
        if (ate_observacion.getValue() == null || ate_observacion.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar una 'OBSERVACIÓN' ", "");
            return false;
        }

        if (aut_cuenta.getValor() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar una 'CUENTA' ", "");
            return false;
        }

        if (aut_persona.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un 'BENEFICIARIO' ", "");
            return false;
        }

        if (tex_valor_pagar.getValue() == null || tex_valor_pagar.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar el 'VALOR'", "");
            return false;
        } else {
            try {
                if (Double.parseDouble(tex_valor_pagar.getValue().toString()) <= 0) {
                    utilitario.agregarMensajeError("El 'VALOR' no es válido", "");
                    return false;
                }
            } catch (Exception e) {
                utilitario.agregarMensajeError("El 'VALOR' no es válido", "");
                return false;
            }
        }

        return true;
    }

    ////////////CXC
    /**
     * Carga las facturas por Cobrar cuando se selecciona un cliente del
     * autocompletar
     *
     * @param evt
     */
    public void cargarCuentasporCobrar(SelectEvent evt) {
        aut_persona.onSelect(evt);
        tab_tabla1.setSql(ser_cliente.getSqlCuentasPorCobrar(aut_persona.getValor()));
        tab_tabla1.ejecutarSql();
        tex_diferencia.setValue(utilitario.getFormatoNumero(0));
        tex_valor_pagar.setValue(utilitario.getFormatoNumero(0));
        if (tab_tabla1.isEmpty()) {
            utilitario.agregarMensajeError("El cliente seleccionado no tiene cuentas por cobrar", "");
        }
    }

    /**
     * Carga facturas pendientes de pago en una fecha
     */
    public void cargarCuentasporCobrarGrupo() {

        tab_tabla1.setSql(ser_cliente.getSqlCuentasPorCobrarGrupo(cal_fecha_pago.getFecha()));
        tab_tabla1.ejecutarSql();
        tex_diferencia.setValue(utilitario.getFormatoNumero(0));
        tex_valor_pagar.setValue(utilitario.getFormatoNumero(0));
        if (tab_tabla1.isEmpty()) {
            utilitario.agregarMensajeError("No existen cuentas por cobrar", "");
        }
    }

    public void deseleccionaFacturaCxC(UnselectEvent evt) {
        double total = 0;
        for (Fila actual : tab_tabla1.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[5] + "") + total;
        }
        tex_valor_pagar.setValue(utilitario.getFormatoNumero(total));
        utilitario.addUpdate("tex_valor_pagar");
        CalcularDiferenciaCxC();
    }

    public void seleccionaFacturaCxC(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        double total = 0;
        for (Fila actual : tab_tabla1.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[5] + "") + total;
        }
        tex_valor_pagar.setValue(utilitario.getFormatoNumero(total));
        utilitario.addUpdate("tex_valor_pagar");
        CalcularDiferenciaCxC();
    }

    public void CalcularDiferenciaCxC() {
        double diferencia = 0;
        if (tex_valor_pagar.getValue() != null) {
            if (!tex_valor_pagar.getValue().toString().isEmpty()) {
                if (tab_tabla1.getTotalFilas() > 0) {
                    try {
                        diferencia = tab_tabla1.getSumaColumna("saldo_x_pagar") - Double.parseDouble(tex_valor_pagar.getValue().toString());
                    } catch (Exception e) {
                        tex_valor_pagar.setValue(utilitario.getFormatoNumero("0"));
                        utilitario.addUpdate("tex_valor_pagar");
                    }
                    tex_diferencia.setValue(utilitario.getFormatoNumero(diferencia));
                }
            }
        } else if (tab_tabla1.getTotalFilas() > 0) {
            diferencia = tab_tabla1.getSumaColumna("saldo_x_pagar");
            tex_diferencia.setValue(utilitario.getFormatoNumero(diferencia));
        }
        utilitario.addUpdate("tex_diferencia");
    }

    public void aceptarCxC() {
        if (validarCxC()) {
            String ide_teclb = cargarPagoCxC(Double.parseDouble(tex_valor_pagar.getValue().toString()));
            //11/02/2019 Control por si genera error al guardar la transacción
            if (ide_teclb == null) {
                ide_teclb = "";
            }
            int num = -1;
            try {
                num = Integer.parseInt(ide_teclb);
            } catch (Exception e) {
                num = -1;
            }

            if (num > 0) {
                generarAsiento(ide_teclb);
                dibujarCxC();
            } else {
                utilitario.agregarMensajeError("Error al guardar la transacción", "Cuentas por Cobrar");
            }

        }
    }

    public void cambioCuenta(SelectEvent evt) {
        aut_cuenta.onSelect(evt);
        cambioTipoTransBanco();
    }

    public void cambioTipoTransBanco() {
//        CAMBIE
        if (com_tip_tran.getValue() != null) {
            if (aut_cuenta.getValor() != null) {
                tex_num.setValue(ser_tesoreria.getNumMaximoTipoTransaccion(aut_cuenta.getValor() + "", com_tip_tran.getValue() + ""));
            } else {
                aut_cuenta.limpiar();
                tex_num.limpiar();
            }
        }
        utilitario.addUpdate("tex_num,aut_cuenta");
    }

    private String cargarPagoCxC(double total_a_pagar) {

        List lis_fact_pagadas = new ArrayList();

        for (int i = 0; i < tab_tabla1.getListaFilasSeleccionadas().size(); i++) {
            double monto_sobrante = 0;

            double valor_x_pagar = Double.parseDouble(utilitario.getFormatoNumero(tab_tabla1.getListaFilasSeleccionadas().get(i).getCampos()[5]));
            if (valor_x_pagar > 0) {
                if (total_a_pagar >= valor_x_pagar) {
                    Object fila[] = {tab_tabla1.getListaFilasSeleccionadas().get(i).getCampos()[1], tab_tabla1.getListaFilasSeleccionadas().get(i).getRowKey(), utilitario.getFormatoNumero(tab_tabla1.getListaFilasSeleccionadas().get(i).getCampos()[5])};

                    lis_fact_pagadas.add(fila);

                    monto_sobrante = total_a_pagar - valor_x_pagar;

                    if (tab_tabla1.getListaFilasSeleccionadas().get(i).getCampos()[1] != null) {
                        //ACTUALIZA LA FACTURA A PAGADA
                        utilitario.getConexion().agregarSqlPantalla(ser_factura.getSqlActualizaPagoFactura(String.valueOf(tab_tabla1.getListaFilasSeleccionadas().get(i).getCampos()[1])));
                    }
                } else {
                    //System.out.println(monto_sobrante + " ----- " + valor_x_pagar + "  VALOR A PAGAR  " + total_a_pagar);

                    monto_sobrante = total_a_pagar - valor_x_pagar;
                    if (monto_sobrante <= valor_x_pagar) {
                        System.out.println(monto_sobrante + "    *** BREAK  " + valor_x_pagar + "  VALOR A PAGAR  " + total_a_pagar);
                        Object fila[] = {tab_tabla1.getListaFilasSeleccionadas().get(i).getCampos()[1], tab_tabla1.getListaFilasSeleccionadas().get(i).getRowKey(), utilitario.getFormatoNumero(total_a_pagar)};
                        lis_fact_pagadas.add(fila);
                        break;
                    }
                }
                total_a_pagar = monto_sobrante;
            }
        }

        //ORDENA DE MENOR A MAYOR SALDO
        for (int i = 0; i < lis_fact_pagadas.size(); i++) {
            for (int j = (i + 1); j < lis_fact_pagadas.size(); j++) {
                Object[] obj_filai = (Object[]) lis_fact_pagadas.get(i);
                Object[] obj_filaj = (Object[]) lis_fact_pagadas.get(j);
                double dou_saldoi = Double.parseDouble(String.valueOf(obj_filai[2]));
                double dou_saldoj = Double.parseDouble(String.valueOf(obj_filaj[2]));
                if (dou_saldoj < dou_saldoi) {
                    Object[] obj_aux = obj_filai;
                    lis_fact_pagadas.set(i, obj_filaj);
                    lis_fact_pagadas.set(j, obj_aux);
                }
            }
        }

        String ide_teclb = ser_tesoreria.generarLibroBanco(aut_persona.getValorArreglo(2), cal_fecha_pago.getFecha(),
                com_tip_tran.getValue().toString(), aut_cuenta.getValor(), Double.parseDouble(tex_valor_pagar.getValue().toString()), String.valueOf(ate_observacion.getValue()), String.valueOf(tex_num.getValue()), null);

        for (Object lis_fact_pagada : lis_fact_pagadas) {
            Object[] obj_fila = (Object[]) lis_fact_pagada;
            System.out.println("ide_cccfa " + obj_fila[0] + " ide_ccctr " + obj_fila[1] + "*** valor " + obj_fila[2]);
            if (obj_fila[0] != null) {
                //Actualiza cxc_detall_transa libro banco generado
                utilitario.getConexion().agregarSqlPantalla("UPDATE cxc_detall_transa SET ide_teclb=" + ide_teclb + " WHERE ide_cccfa =" + obj_fila[0] + " and ide_teclb is null");
            }

            String ide_ccctr = String.valueOf(obj_fila[1]);
            //TRANSACCION EN TESORERIA y TRANSACCION CXC
            TablaGenerica tab_cabecera = utilitario.consultar(ser_factura.getSqlCabeceraFactura(String.valueOf(obj_fila[0])));

            ser_factura.generarTransaccionPago(tab_cabecera, ide_ccctr, ide_teclb, Double.parseDouble(String.valueOf(obj_fila[2])), String.valueOf(ate_observacion.getValue()), String.valueOf(tex_num.getValue()));
        }
        return ide_teclb;
        //utilitario.getConexion().setImprimirSqlConsola(true);
        //utilitario.getConexion().guardarPantalla();
    }

    /**
     * Validaciones de la Transaccion CXC de Pago
     *
     * @return
     */
    private boolean validarCxC() {

        if (com_tip_tran.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar el 'TIPO DE TRANSACCIÓN'", "");
            return false;
        }

        if (ate_observacion.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe ingresar una 'OBSERVACIÓN' ", "");
            return false;
        }
        if (aut_cuenta.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar una 'CUENTA' ", "");
            return false;
        }

        if (aut_persona.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un 'CLIENTE' ", "");
            return false;
        }
        if (tab_tabla1.isEmpty()) {
            utilitario.agregarMensajeInfo("El Cliente seleccionado no tiene Cuentas por Cobrar ", "");
            return false;
        }
        if (tab_tabla1.getListaFilasSeleccionadas() == null || tab_tabla1.getListaFilasSeleccionadas().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos una Factura", "");
            return false;
        }

        if (tex_valor_pagar.getValue() == null || tex_valor_pagar.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar el 'VALOR A COBRAR'", "");
            return false;
        } else {
            try {
                if (Double.parseDouble(tex_valor_pagar.getValue().toString()) <= 0) {
                    utilitario.agregarMensajeError("El 'VALOR A COBRAR' no es válido", "");
                    return false;
                }
            } catch (Exception e) {
                utilitario.agregarMensajeError("El 'VALOR A COBRAR' no es válido", "");
                return false;
            }
        }

        if (tex_diferencia.getValue() != null) {
            try {
                if (Double.parseDouble(tex_diferencia.getValue().toString()) < 0) {
                    utilitario.agregarMensajeError("El 'VALOR A COBRAR' es mayor al saldo total de la cuenta por cobrar", "");
                    return false;
                }
            } catch (Exception e) {
                utilitario.agregarMensajeError("El 'VALOR A COBRAR' no es válido", "");
                return false;
            }
        }

        //Valida que el monto a pagar ingresado sea superior al monto de las facturas seleccionadas
        if (tab_tabla1.getListaFilasSeleccionadas().size() > 1) { //si se a seleccionado mas de una factura
            // double dou_suma_saldo_fact = 0;//SUMA EL SALDO DE LAS FACTURAS SELECCIONADAS
            double dou_saldo_menor = 0;  //ALMACENA EL VALOR DE MENOR PAGO
            for (int i = 0; i < tab_tabla1.getListaFilasSeleccionadas().size(); i++) {
                double dou_saldo_actual = Double.parseDouble(String.valueOf(tab_tabla1.getListaFilasSeleccionadas().get(i).getCampos()[5]));
                //      dou_suma_saldo_fact += dou_saldo_actual;
                if (i == 0) {
                    dou_saldo_menor = dou_saldo_actual;
                }

                if (dou_saldo_actual < dou_saldo_menor) {
                    dou_saldo_menor = dou_saldo_actual;
                }

            }
            if ((Double.parseDouble(tex_valor_pagar.getValue().toString())) < dou_saldo_menor) {
                utilitario.agregarMensajeError("El 'VALOR A PAGAR' es menor que el saldo de las Facturas Seleccionadas, el valor mínimo a pagar es: " + utilitario.getFormatoNumero(dou_saldo_menor), "");
                return false;
            }
        }

        return true;
    }

    /**
     * Abre el asiento contable seleccionado
     *
     * @param evt
     */
    public void abrirAsiento(ActionEvent evt) {
        Link lin_ide_cnccc = (Link) evt.getComponent();
        asc_asiento.setAsientoContable(lin_ide_cnccc.getValue().toString());
        tab_tabla1.setFilaActual(lin_ide_cnccc.getDir());
        asc_asiento.dibujar();
    }

    public void cargarMovimientosCuenta(ActionEvent evt) {
        Link lin_ide_tecba = (Link) evt.getComponent();
        tab_tabla1.setFilaActual(lin_ide_tecba.getDir());
        aut_cuentas.setValor(tab_tabla1.getValor("ide_tecba"));
        utilitario.addUpdate("aut_cuentas");
        dibujarMovimienots();
    }

    public void dibujarAnticipoClientes() {
        Grid contenido = new Grid();
        Grid gri1 = new Grid();
        gri1.setColumns(3);
        gri1.getChildren().add(new Etiqueta("<strong>CLIENTE : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta("<strong>FECHA : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta());

        aut_persona = new AutoCompletar();
        aut_persona.setId("aut_persona");
        aut_persona.setAutocompletarContenido();
        aut_persona.setAutoCompletar(ser_cliente.getSqlComboClientes());
        aut_persona.setSize(70);
        gri1.getChildren().add(aut_persona);
        cal_fecha_pago = new Calendario();
        cal_fecha_pago.setFechaActual();
        gri1.getChildren().add(cal_fecha_pago);
        gri1.getChildren().add(new Etiqueta());

        gri1.getChildren().add(new Etiqueta("<strong>A LA CUENTA : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta("<strong>TRANSACCIÓN : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri1.getChildren().add(new Etiqueta("<strong>NUM. DOCUMENTO : </strong>"));

        aut_cuenta = new AutoCompletar();
        aut_cuenta.setId("aut_cuenta");
        aut_cuenta.setMetodoChange("cambioCuenta");
        aut_cuenta.setAutoCompletar(aut_cuentas.getLista());
        aut_cuenta.setDropdown(true);
        aut_cuenta.setSize(66);
        aut_cuenta.setAutocompletarContenido();
        aut_cuenta.setMaxResults(25);

        gri1.getChildren().add(aut_cuenta);
        com_tip_tran = new Combo();
        com_tip_tran.setMetodo("cambioTipoTransBanco");
        com_tip_tran.setCombo(ser_tesoreria.getSqlTipoTransaccionPositivo());
        gri1.getChildren().add(com_tip_tran);
        tex_num = new Texto();
        tex_num.setId("tex_num");
        tex_num.setSoloEnteros();
        gri1.getChildren().add(tex_num);

        contenido.getChildren().add(gri1);
        PanelGrid gri4 = new PanelGrid();
        gri4.setColumns(2);
        Etiqueta eti_valor_cobrar = new Etiqueta();
        eti_valor_cobrar.setValue("VALOR $:");
        tex_valor_pagar = new Texto();
        tex_valor_pagar.setId("tex_valor_pagar");
        tex_valor_pagar.setSoloNumeros();
        eti_valor_cobrar.setStyle("font-size: 14px;font-weight: bold;");
        tex_valor_pagar.setStyle("font-size: 14px;font-weight: bold");
        gri4.getChildren().add(eti_valor_cobrar);
        gri4.getChildren().add(tex_valor_pagar);
        contenido.getChildren().add(new Separator());
        contenido.getChildren().add(gri4);

        Grid gri3 = new Grid();
        gri3.setColumns(1);
        ate_observacion = new AreaTexto();
        ate_observacion.setCols(90);
        ate_observacion.setMaxlength(190);
        gri3.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri3.getChildren().add(ate_observacion);
        contenido.getChildren().add(gri3);

        contenido.getChildren().add(new Separator());
        Boton bot_aceptar = new Boton();
        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setMetodo("aceptarAnticipoCliente");
        bot_aceptar.setIcon("ui-icon-check");
        contenido.getChildren().add(bot_aceptar);
        mep_menu.dibujar(14, "ANTICIPO DE CLIENTES", contenido);
        //metodo of mauricio
        utilitario.buscarPermisosObjetos();
    }

    public void aceptarAnticipoCliente() {
        if (validarAnticipo()) {
            TablaGenerica tab_libro = ser_tesoreria.generarTablaLibroBanco(aut_persona.getValorArreglo(2), cal_fecha_pago.getFecha(),
                    com_tip_tran.getValue().toString(), aut_cuenta.getValor(), Double.parseDouble(tex_valor_pagar.getValue().toString()), String.valueOf(ate_observacion.getValue()), String.valueOf(tex_num.getValue()), null);
            //Generar transaccion anticipo cxc 
            ser_cuentas_cxc.generarTransaccionAnticipo(aut_persona.getValor(), tab_libro);
            generarAsiento(tab_libro.getValor("ide_teclb"));
        }
    }

    public AutoCompletar getAut_cuentas() {
        return aut_cuentas;
    }

    public void setAut_cuentas(AutoCompletar aut_cuentas) {
        this.aut_cuentas = aut_cuentas;
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public AutoCompletar getAut_persona() {
        return aut_persona;
    }

    public void setAut_persona(AutoCompletar aut_persona) {
        this.aut_persona = aut_persona;
    }

    public AutoCompletar getAut_cuenta() {
        return aut_cuenta;
    }

    public void setAut_cuenta(AutoCompletar aut_cuenta) {
        this.aut_cuenta = aut_cuenta;
    }

    public Tabla getTab_seleccion() {
        return tab_tabla1;
    }

    public void setTab_seleccion(Tabla tab_tabla) {
        this.tab_tabla1 = tab_tabla;
    }

    public AutoCompletar getAut_cuenta1() {
        return aut_cuenta1;
    }

    public void setAut_cuenta1(AutoCompletar aut_cuenta1) {
        this.aut_cuenta1 = aut_cuenta1;
    }

    public AsientoContable getAsc_asiento() {
        return asc_asiento;
    }

    public void setAsc_asiento(AsientoContable asc_asiento) {
        this.asc_asiento = asc_asiento;
    }

    public Dialogo getDia_modifica() {
        return dia_modifica;
    }

    public void setDia_modifica(Dialogo dia_modifica) {
        this.dia_modifica = dia_modifica;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

    public Upload getUpl_importa() {
        return upl_importa;
    }

    public void setUpl_importa(Upload upl_importa) {
        this.upl_importa = upl_importa;
    }

    public Confirmar getCon_confirma() {
        return con_confirma;
    }

    public void setCon_confirma(Confirmar con_confirma) {
        this.con_confirma = con_confirma;
    }

    public SeleccionTabla getSel_conciliados() {
        return sel_conciliados;
    }

    public void setSel_conciliados(SeleccionTabla sel_conciliados) {
        this.sel_conciliados = sel_conciliados;
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

    public SeleccionCalendario getSel_fechas() {
        return sel_fechas;
    }

    public void setSel_fechas(SeleccionCalendario sel_fechas) {
        this.sel_fechas = sel_fechas;
    }

    public SeleccionTabla getSet_bancos() {
        return set_bancos;
    }

    public void setSet_bancos(SeleccionTabla set_bancos) {
        this.set_bancos = set_bancos;
    }

    public SeleccionTabla getSet_tipo_transaccion() {
        return set_tipo_transaccion;
    }

    public void setSet_tipo_transaccion(SeleccionTabla set_tipo_transaccion) {
        this.set_tipo_transaccion = set_tipo_transaccion;
    }

}

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
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.Link;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import servicios.cuentas_x_cobrar.ServicioCliente;
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

    public pre_libro_bancos() {

        mep_menu.setMenuPanel("CONSULTAS", "20%");
        mep_menu.agregarItem("Posición Consolidada", "dibujarPosicion", "ui-icon-note");//1
        mep_menu.agregarItem("Consulta de Movimientos", "dibujarMovimienots", "ui-icon-note");//2
        mep_menu.agregarSubMenu("TRANSACCIONES");
        mep_menu.agregarItem("Cuentas por Cobrar", "dibujarCxC", "ui-icon-contact");//3
        mep_menu.agregarItem("Cuentas por Pagar", "dibujarCxP", "ui-icon-contact");//4
        mep_menu.agregarItem("Anticipos a Proveedores", "dibujarAnticipo", "ui-icon-contact");//9
        mep_menu.agregarItem("Otras Transacciones", "dibujarOtros", "ui-icon-contact");//5
        mep_menu.agregarItem("Transferencias entre Cuentas", "dibujarTransferencias", "ui-icon-contact");//6
        mep_menu.agregarSubMenu("HERRAMIENTAS");
        mep_menu.agregarItem("Conciliación Manual", "dibujarConciliarM", "ui-icon-pencil"); //7
        mep_menu.agregarItem("Conciliación Automática", "dibujarConciliarA", "ui-icon-calculator");//8

        agregarComponente(mep_menu);

        aut_cuentas.setId("aut_cuentas");
        aut_cuentas.setAutocompletarContenido();
        aut_cuentas.setDropdown(true);
        aut_cuentas.setAutoCompletar(ser_tesoreria.getSqlComboCuentas());
        aut_cuentas.setMetodoChange("actualizarMovimientos");
        aut_cuentas.setGlobal(true);
        aut_cuentas.setValue(null);
        aut_cuentas.setGlobal(true);
        aut_cuentas.setMaxResults(15);

        bar_botones.limpiar();
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
        agregarComponente(asc_asiento);

        dia_modifica.setId("dia_modifica");
        dia_modifica.setHeight("50%");
        dia_modifica.setWidth("40%");
        dia_modifica.setTitle("MODIFICAR MOVIMIENTO");
        dia_modifica.getBot_aceptar().setMetodo("aceptarModificar");
        agregarComponente(dia_modifica);

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
        gri1.getChildren().add(aut_cuenta);

        com_tip_tran = new Combo();
        com_tip_tran.setMetodo("cambioTipoTransBanco");
        com_tip_tran.setCombo(ser_tesoreria.getSqlTipoTransaccionPositivo());
        gri1.getChildren().add(com_tip_tran);

        tex_num = new Texto();
        tex_num.setId("tex_num");
        gri1.getChildren().add(tex_num);

        contenido.getChildren().add(gri1);

        Grid gri3 = new Grid();
        gri3.setColumns(1);
        ate_observacion = new AreaTexto();
        ate_observacion.setCols(90);
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
        gri1.getChildren().add(aut_cuenta);
        com_tip_tran = new Combo();
        com_tip_tran.setMetodo("cambioTipoTransBanco");
        com_tip_tran.setCombo(ser_tesoreria.getSqlTipoTransaccionNegativo());
        gri1.getChildren().add(com_tip_tran);
        tex_num = new Texto();
        tex_num.setId("tex_num");
        gri1.getChildren().add(tex_num);
        contenido.getChildren().add(gri1);

        Grid gri3 = new Grid();
        gri3.setColumns(1);
        ate_observacion = new AreaTexto();
        ate_observacion.setCols(90);
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
        gri1.getChildren().add(aut_cuenta);
        com_tip_tran = new Combo();
        com_tip_tran.setMetodo("cambioTipoTransBanco");
        com_tip_tran.setCombo(ser_tesoreria.getSqlTipoTransaccionNegativo());
        gri1.getChildren().add(com_tip_tran);
        tex_num = new Texto();
        tex_num.setId("tex_num");
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
        gri3.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri3.getChildren().add(ate_observacion);
        contenido.getChildren().add(gri3);

        contenido.getChildren().add(new Separator());
        Boton bot_aceptar = new Boton();
        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setMetodo("aceptarAnticipo");
        contenido.getChildren().add(bot_aceptar);
        mep_menu.dibujar(9, "ANTICIPO A PROVEEDORES", contenido);
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
        aut_persona.setMetodoChange("cargarCuentasporPagar");
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
        grid1.getChildren().add(aut_cuenta);

        com_tip_tran = new Combo();
        com_tip_tran.setMetodo("cambioTipoTransBanco");
        com_tip_tran.setCombo(ser_tesoreria.getSqlTipoTransaccion());
        grid1.getChildren().add(com_tip_tran);

        tex_num = new Texto();
        tex_num.setId("tex_num");
        grid1.getChildren().add(tex_num);

        Grid gri3 = new Grid();
        gri3.setColumns(1);
        ate_observacion = new AreaTexto();
        ate_observacion.setCols(90);
        gri3.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri3.getChildren().add(ate_observacion);

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
        gri1.getChildren().add(aut_cuenta);
        gri1.getChildren().add(new Etiqueta("<strong>A LA CUENTA : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        aut_cuenta1 = new AutoCompletar();
        aut_cuenta1.setId("aut_cuenta1");
        //aut_cuenta1.setMetodoChange("cambioCuenta");
        aut_cuenta1.setAutoCompletar(ser_tesoreria.getSqlComboCuentasTodas());
        aut_cuenta1.setDropdown(true);
        aut_cuenta1.setAutocompletarContenido();
        aut_cuenta1.setSize(66);
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
        tab_tabla1.setSql(ser_tesoreria.getSqlTransaccionesCuentaNoConciliado(aut_cuentas.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla1.setRows(15);
        tab_tabla1.setHeader("MOVIMIENTOS NO CONCILIADOS");
        tab_tabla1.setLectura(true);
        tab_tabla1.setOrdenar(false);
        tab_tabla1.getColumna("ide_teclb").setVisible(false);
        tab_tabla1.setCampoPrimaria("ide_teclb");
        tab_tabla1.getColumna("EGRESOS").alinearDerecha();
        tab_tabla1.getColumna("EGRESOS").setLongitud(25);
        tab_tabla1.getColumna("INGRESOS").alinearDerecha();
        tab_tabla1.getColumna("INGRESOS").setLongitud(25);
        tab_tabla1.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("numero_teclb").setFiltroContenido();
        tab_tabla1.getColumna("beneficiari_teclb").setFiltroContenido();
        tab_tabla1.getColumna("nombre_tettb").setFiltroContenido();
        tab_tabla1.setLectura(true);
        tab_tabla1.setTipoSeleccion(true);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);

        Boton bot_conciliar = new Boton();
        bot_conciliar.setValue("Conciliar Selccionados");
        bot_conciliar.setMetodo("conciliarM");
        pat_panel.setFooter(bot_conciliar);

        mep_menu.dibujar(7, "CONCILIAZIÓN MANUAL", pat_panel);
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
        if (mep_menu.getOpcion() != 6) {
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

            } else {
                if (aut_persona != null) {
                    asc_asiento.getTab_cabe_asiento().setValor("ide_geper", aut_persona.getValor());
                } else {
                    asc_asiento.getTab_cabe_asiento().setValor("ide_geper", utilitario.getVariable("p_con_beneficiario_empresa"));//sociedad salesianos                
                }
            }

            asc_asiento.getTab_deta_asiento().setValor("ide_cndpc", ser_tesoreria.getCuentaContable(aut_cuenta.getValor()));
            asc_asiento.getTab_deta_asiento().setValor("valor_cndcc", utilitario.getFormatoNumero(tex_valor_pagar.getValue().toString()));
            asc_asiento.calcularTotal();

            if (com_tip_tran.getValue().equals(utilitario.getVariable("p_tes_tran_cheque"))) {
                asc_asiento.setReporteCheque();
            } else {
                asc_asiento.setReporteComprobante();
            }
        } else {
            //asiento de transferencias entre cuentas
            asc_asiento.getTab_cabe_asiento().setValor("ide_geper", utilitario.getVariable("p_con_beneficiario_empresa"));//sociedad salesianos                
            asc_asiento.getTab_cabe_asiento().setValor("ide_cntcm", asc_asiento.getTipoComprobanteDiario());
            asc_asiento.getTab_deta_asiento().insertar();
            asc_asiento.getTab_deta_asiento().setValor("ide_cnlap", asc_asiento.getLugarAplicaHaber());
            asc_asiento.getTab_deta_asiento().setValor("ide_cndpc", ser_tesoreria.getCuentaContable(aut_cuenta.getValor()));
            asc_asiento.getTab_deta_asiento().setValor("valor_cndcc", utilitario.getFormatoNumero(tex_valor_pagar.getValue().toString()));
            asc_asiento.getTab_deta_asiento().insertar();
            asc_asiento.getTab_deta_asiento().setValor("ide_cnlap", asc_asiento.getLugarAplicaDebe());
            asc_asiento.getTab_deta_asiento().setValor("ide_cndpc", ser_tesoreria.getCuentaContable(aut_cuenta.getValor()));
            asc_asiento.getTab_deta_asiento().setValor("valor_cndcc", utilitario.getFormatoNumero(tex_valor_pagar.getValue().toString()));
            asc_asiento.calcularTotal();
        }

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
            generarAsiento(ide_teclb);
            dibujarCxP();
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
                    com_tip_tran.getValue().toString(), aut_cuenta.getValor(), Double.parseDouble(tex_valor_pagar.getValue().toString()), ate_observacion.getValue().toString(), tex_num.getValue().toString());
            generarAsiento(ide_teclb);
            dibujarOtros();
        }
    }

    public void aceptarAnticipo() {
        if (validarAnticipo()) {
            TablaGenerica tab_libro = ser_tesoreria.generarTablaLibroBanco(aut_persona.getValorArreglo(2), cal_fecha_pago.getFecha(),
                    com_tip_tran.getValue().toString(), aut_cuenta.getValor(), Double.parseDouble(tex_valor_pagar.getValue().toString()), ate_observacion.getValue().toString(), tex_num.getValue().toString());
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
                com_tip_tran.getValue().toString(), aut_cuenta.getValor(), Double.parseDouble(tex_valor_pagar.getValue().toString()), ate_observacion.getValue().toString(), tex_num.getValue().toString());

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
            if (tex_identificacion.getValue() == null) {
                utilitario.agregarMensajeInfo("Debe ingresar la 'IDENTIFICACIÓN' ", "");
                return false;
            }

            if (com_tipo_identificacion.getValue() == null) {
                utilitario.agregarMensajeInfo("Debe seleccionar una 'TIPO DE IDENTIFICACIÓN' ", "");
                return false;
            }

            if (tex_beneficiario.getValue() == null) {
                utilitario.agregarMensajeInfo("Debe ingresar un 'BENEFICIARIO' ", "");
                return false;
            }
        } else {
            if (aut_persona.getValue() == null) {
                utilitario.agregarMensajeInfo("Debe seleccionar un 'BENEFICIARIO' ", "");
                return false;
            }
        }

        if (ate_observacion.getValue() == null) {
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

        if (ate_observacion.getValue() == null) {
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
        if (ate_observacion.getValue() == null) {
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
        if (ate_observacion.getValue() == null) {
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
            generarAsiento(ide_teclb);
            dibujarCxC();
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
                com_tip_tran.getValue().toString(), aut_cuenta.getValor(), Double.parseDouble(tex_valor_pagar.getValue().toString()), ate_observacion.getValue().toString(), tex_num.getValue().toString());

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

}

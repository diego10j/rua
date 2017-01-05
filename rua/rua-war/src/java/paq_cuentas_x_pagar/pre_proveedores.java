/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_pagar;

import componentes.AsientoContable;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Link;
import framework.componentes.MenuPanel;
import framework.componentes.PanelArbol;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.graficos.GraficoCartesiano;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.event.SelectEvent;
import servicios.contabilidad.ServicioComprobanteContabilidad;
import servicios.contabilidad.ServicioContabilidadGeneral;
import servicios.cuentas_x_pagar.ServicioCuentasCxP;
import servicios.cuentas_x_pagar.ServicioProveedor;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author dfjacome
 */
public class pre_proveedores extends Pantalla {

    @EJB
    private final ServicioProveedor ser_proveedor = (ServicioProveedor) utilitario.instanciarEJB(ServicioProveedor.class);

    private final MenuPanel mep_menu = new MenuPanel();
    private AutoCompletar aut_proveedor = new AutoCompletar();

    //Consultas
    private Calendario cal_fecha_inicio;
    private Calendario cal_fecha_fin;

    private Tabla tab_tabla;
    private Arbol arb_estructura;// Estructura Gerarquica de proveedores

    /*CONTABILIDAD*/
    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);
    private AutoCompletar aut_cuentas;
    private AsientoContable asc_asiento = new AsientoContable();

    @EJB
    private final ServicioComprobanteContabilidad ser_comp_conta = (ServicioComprobanteContabilidad) utilitario.instanciarEJB(ServicioComprobanteContabilidad.class);

    @EJB
    private final ServicioCuentasCxP ser_cuentaCXP = (ServicioCuentasCxP) utilitario.instanciarEJB(ServicioCuentasCxP.class);

    /*INFOMRES*/
    private GraficoCartesiano gca_grafico;
    private Combo com_periodo;

    private Combo com_fac_pendientes;
    private Texto tex_num_asiento;

    public pre_proveedores() {

        bar_botones.quitarBotonsNavegacion();
        bar_botones.agregarComponente(new Etiqueta("PROVEEDOR :"));

        aut_proveedor.setId("aut_proveedor");
        aut_proveedor.setAutoCompletar(ser_proveedor.getSqlComboProveedor());
        aut_proveedor.setSize(75);
        aut_proveedor.setAutocompletarContenido(); // no startWith para la busqueda
        aut_proveedor.setMetodoChangeRuta("pre_index.clase.seleccionarProveedor");
        bar_botones.agregarComponente(aut_proveedor);
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_clean);

        mep_menu.setMenuPanel("OPCIONES PROVEEDOR", "20%");
        mep_menu.agregarItem("Información Proveedor", "dibujarProveedor", "ui-icon-person");
        mep_menu.agregarItem("Clasificación Proveedores", "dibujarEstructura", "ui-icon-arrow-4-diag");
        mep_menu.agregarSubMenu("TRANSACCIONES");
        mep_menu.agregarItem("Transacciones Proveedor", "dibujarTransacciones", "ui-icon-contact");
        mep_menu.agregarItem("Ingresar Transacción", "dibujarIngresarTransacciones", "ui-icon-contact");
        mep_menu.agregarItem("Productos Proveedor", "dibujarProductos", "ui-icon-cart");
        mep_menu.agregarItem("Documentos Por Pagar", "dibujarFacturas", "ui-icon-calculator");
        mep_menu.agregarSubMenu("CONTABILIDAD");
        mep_menu.agregarItem("Configura Cuenta Contable", "dibujarConfiguraCuenta", "ui-icon-wrench");
        mep_menu.agregarItem("Movimientos Contables", "dibujarMovimientos", "ui-icon-note");
        mep_menu.agregarSubMenu("INFORMES");
        mep_menu.agregarItem("Gráfico de Compras", "dibujarGrafico", "ui-icon-bookmark");
        mep_menu.agregarItem("Reporte Cuentas por Pagar", "dibujarReporteCxP", "ui-icon-bookmark");
        mep_menu.agregarItem("Productos Comprados", "dibujarProductosComprados", "ui-icon-cart");

        agregarComponente(mep_menu);
        asc_asiento.setId("asc_asiento");
        agregarComponente(asc_asiento);
    }

    /**
     * Selecciona un Proveedor en el autocompletar
     *
     * @param evt
     */
    public void seleccionarProveedor(SelectEvent evt) {
        aut_proveedor.onSelect(evt);
        if (aut_proveedor != null) {
            switch (mep_menu.getOpcion()) {
                case 1:
                    dibujarProveedor();
                    break;
                case 2:
                    dibujarTransacciones();
                    break;
                case 3:
                    dibujarProductos();
                    break;
                case 4:
                    dibujarConfiguraCuenta();
                    break;
                case 5:
                    dibujarMovimientos();
                    break;
                case 6:
                    dibujarFacturas();
                    break;
                case 7:
                    dibujarEstructura();
                    break;
                case 8:
                    dibujarGrafico();
                    break;
                case 9:
                    dibujarProductosComprados();
                    break;
                case 10:
                    dibujarIngresarTransacciones();
                    break;
                case 11:
                    dibujarReporteCxP();
                    break;
                default:
                    dibujarProveedor();
            }
        } else {
            limpiar();
        }
    }

    public void dibujarReporteCxP() {
//12
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_cuentaCXP.getSqlTransaccionesCxP());
        tab_tabla.setCampoPrimaria("ide_geper");
        tab_tabla.getColumna("ide_geper").setVisible(false);
        tab_tabla.getColumna("PROVEEDOR").setLongitud(190);
        tab_tabla.getColumna("PROVEEDOR").setFiltroContenido();
        tab_tabla.getColumna("IDENTIFICACION").setFiltroContenido();
        tab_tabla.setLectura(true);
        tab_tabla.setColumnaSuma("SALDO");
        tab_tabla.setRows(20);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        mep_menu.dibujar(12, "REPORTE  CUENTAS POR PAGAR", pat_panel);

    }

    /**
     * Dibuja el formulario de datos del Proveedor, osigna opcion 1
     */
    public void dibujarProveedor() {
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        ser_proveedor.configurarTablaProveedor(tab_tabla);
        tab_tabla.setTabla("gen_persona", "ide_geper", 1);
        tab_tabla.setCondicion("ide_geper=" + aut_proveedor.getValor());
        tab_tabla.setMostrarNumeroRegistros(false);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        mep_menu.dibujar(1, "DATOS DEL PROVEEDOR", pat_panel);
    }

    public void dibujarIngresarTransacciones() {
        Grupo gru_grupo = new Grupo();
        if (isProveedorSeleccionado()) {
            tab_tabla = new Tabla();
            tab_tabla.setId("tab_tabla");
            tab_tabla.setTabla("cxp_detall_transa", "ide_cpdtr", 998);
            tab_tabla.setTipoFormulario(true);
            tab_tabla.getGrid().setColumns(2);
            tab_tabla.getColumna("ide_cpdtr").setVisible(false);
            tab_tabla.setMostrarNumeroRegistros(false);
            tab_tabla.getColumna("ide_teclb").setVisible(false);
            tab_tabla.getColumna("ide_cpttr").setCombo("cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr", "");
            tab_tabla.getColumna("ide_cpttr").setNombreVisual("TIPO DE TRANSACCIÓN");
            tab_tabla.getColumna("ide_cpttr").setOrden(1);
            tab_tabla.getColumna("ide_cpttr").setRequerida(true);

            tab_tabla.getColumna("fecha_trans_cpdtr").setValorDefecto(utilitario.getFechaActual());
            tab_tabla.getColumna("fecha_trans_cpdtr").setNombreVisual("FECHA");
            tab_tabla.getColumna("fecha_trans_cpdtr").setOrden(2);
            tab_tabla.getColumna("fecha_trans_cpdtr").setRequerida(true);

            tab_tabla.getColumna("valor_cpdtr").setNombreVisual("VALOR");
            tab_tabla.getColumna("valor_cpdtr").setRequerida(true);
            tab_tabla.getColumna("valor_cpdtr").setOrden(3);

            tab_tabla.getColumna("fecha_venci_cpdtr").setVisible(false);
            tab_tabla.getColumna("numero_pago_cpdtr").setVisible(false);
            tab_tabla.getColumna("numero_pago_cpdtr").setValorDefecto("0");

            tab_tabla.getColumna("docum_relac_cpdtr").setNombreVisual("NUM. DOCUMENTO");
            tab_tabla.getColumna("docum_relac_cpdtr").setOrden(4);

            tab_tabla.getColumna("observacion_cpdtr").setNombreVisual("OBSERVACIÓN");
            tab_tabla.getColumna("observacion_cpdtr").setRequerida(true);
            tab_tabla.getColumna("observacion_cpdtr").setOrden(5);
            tab_tabla.getColumna("observacion_cpdtr").setControl("AreaTexto");

            tab_tabla.getColumna("ide_cpcfa").setVisible(false);
            tab_tabla.getColumna("ide_cpdno").setVisible(false);
            tab_tabla.getColumna("ide_cnccc").setVisible(false);
            tab_tabla.getColumna("ide_cpctr").setVisible(false);
            tab_tabla.getColumna("ide_usua").setVisible(false);
            tab_tabla.getColumna("valor_anticipo_cpdtr").setVisible(false);
            tab_tabla.getColumna("valor_anticipo_cpdtr").setValorDefecto("0");
            tab_tabla.getColumna("anticipo_activo").setVisible(false);

            tab_tabla.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));

            tab_tabla.dibujar();
            tab_tabla.insertar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_tabla);
            pat_panel2.getMenuTabla().setRendered(false);

            com_fac_pendientes = new Combo();
            com_fac_pendientes.setCombo(ser_proveedor.getSqlComboFacturasPorPagar(aut_proveedor.getValor()));
            Grid gris = new Grid();
            gris.setColumns(2);
            gris.getChildren().add(new Etiqueta("<strong> CUENTA POR PAGAR : </strong>"));
            gris.getChildren().add(com_fac_pendientes);

            tex_num_asiento = new Texto();
            tex_num_asiento.setSize(7);
            tex_num_asiento.setSoloEnteros();
            gris.getChildren().add(new Etiqueta("<strong>NÚMERO DE ASIENTO : </strong>"));
            gris.getChildren().add(tex_num_asiento);

            gru_grupo.getChildren().add(gris);
            gru_grupo.getChildren().add(pat_panel2);
        }
        mep_menu.dibujar(10, "INGRESAR TRANSACCIÓN", gru_grupo);
    }

    /**
     * Abre el asiento contable seleccionado
     *
     * @param evt
     */
    public void abrirAsiento(ActionEvent evt) {
        Link lin_ide_cnccc = (Link) evt.getComponent();
        asc_asiento.setAsientoContable(lin_ide_cnccc.getValue().toString());
        tab_tabla.setFilaActual(lin_ide_cnccc.getDir());
        asc_asiento.dibujar();
    }

    public void dibujarTransacciones() {
        Grupo gru_grupo = new Grupo();
        if (isProveedorSeleccionado()) {

            Fieldset fis_consulta = new Fieldset();
            fis_consulta.setLegend("Detalle de la Consulta");

            Grid gri_fechas = new Grid();
            gri_fechas.setColumns(5);
            gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA DESDE :</strong>"));
            cal_fecha_inicio = new Calendario();
            cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
            gri_fechas.getChildren().add(cal_fecha_inicio);
            gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA HASTA :</strong>"));
            cal_fecha_fin = new Calendario();
            cal_fecha_fin.setFechaActual();
            gri_fechas.getChildren().add(cal_fecha_fin);
            fis_consulta.getChildren().add(gri_fechas);

            Boton bot_consultar = new Boton();
            bot_consultar.setValue("Consultar");
            bot_consultar.setMetodo("actualizarTransacciones");
            bot_consultar.setIcon("ui-icon-search");

            gri_fechas.getChildren().add(bot_consultar);

            fis_consulta.getChildren().add(gri_fechas);
            gru_grupo.getChildren().add(fis_consulta);

            tab_tabla = new Tabla();
            tab_tabla.setNumeroTabla(2);
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_proveedor.getSqlTransaccionesProveedor(aut_proveedor.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.setCampoPrimaria("ide_cpdtr");
            tab_tabla.getColumna("IDE_TECLB").setVisible(false);
            tab_tabla.getColumna("fecha_trans_cpdtr").setNombreVisual("FECHA");
            tab_tabla.getColumna("ide_cpdtr").setVisible(false);
            tab_tabla.getColumna("numero_pago_cpdtr").setVisible(false);
            tab_tabla.getColumna("INGRESOS").alinearDerecha();
            tab_tabla.getColumna("EGRESOS").alinearDerecha();
            tab_tabla.getColumna("IDE_CNCCC").setFiltro(true);
            tab_tabla.getColumna("IDE_CNCCC").setNombreVisual("N. ASIENTO");
            tab_tabla.getColumna("IDE_CNCCC").setLink();
            tab_tabla.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
            tab_tabla.getColumna("TRANSACCION").setFiltroContenido();
            tab_tabla.getColumna("docum_relac_cpdtr").setFiltroContenido();
            tab_tabla.getColumna("docum_relac_cpdtr").setNombreVisual("N. DOCUMENTO");
            tab_tabla.getColumna("INGRESOS").alinearDerecha();
            tab_tabla.getColumna("EGRESOS").alinearDerecha();
            tab_tabla.getColumna("INGRESOS").setLongitud(20);
            tab_tabla.getColumna("EGRESOS").setLongitud(20);
            tab_tabla.getColumna("saldo").setLongitud(25);
            tab_tabla.getColumna("saldo").alinearDerecha();
            tab_tabla.getColumna("saldo").setEstilo("font-weight: bold;");
            tab_tabla.setColumnaSuma("INGRESOS,EGRESOS,saldo");
            tab_tabla.getColumna("INGRESOS").setSuma(false);
            tab_tabla.getColumna("EGRESOS").setSuma(false);
            tab_tabla.getColumna("saldo").setSuma(false);
            tab_tabla.setOrdenar(false);
            tab_tabla.setLectura(true);
            tab_tabla.setScrollable(true);
            tab_tabla.setRows(20);
            tab_tabla.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla);
            gru_grupo.getChildren().add(pat_panel);

            actualizarSaldoxPagar();
        }
        mep_menu.dibujar(2, "TRANSACCIONES DEL PROVEEDOR", gru_grupo);
    }

    public void dibujarProductos() {
        Grupo gru_grupo = new Grupo();
        if (isProveedorSeleccionado()) {
            Fieldset fis_consulta = new Fieldset();
            fis_consulta.setLegend("Detalle de la Consulta");

            Grid gri_fechas = new Grid();
            gri_fechas.setColumns(5);
            gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA DESDE :</strong>"));
            cal_fecha_inicio = new Calendario();
            cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
            gri_fechas.getChildren().add(cal_fecha_inicio);
            gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA HASTA :</strong>"));
            cal_fecha_fin = new Calendario();
            cal_fecha_fin.setFechaActual();
            gri_fechas.getChildren().add(cal_fecha_fin);
            fis_consulta.getChildren().add(gri_fechas);

            Boton bot_consultar = new Boton();
            bot_consultar.setValue("Consultar");
            bot_consultar.setMetodo("actualizarProducots");
            bot_consultar.setIcon("ui-icon-search");

            gri_fechas.getChildren().add(bot_consultar);

            fis_consulta.getChildren().add(gri_fechas);
            gru_grupo.getChildren().add(fis_consulta);
            tab_tabla = new Tabla();
            tab_tabla.setNumeroTabla(3);
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_proveedor.getSqlProductosProveedor(aut_proveedor.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.getColumna("ide_cpdfa").setVisible(false);

            tab_tabla.getColumna("fecha_emisi_cpcfa").setNombreVisual("FECHA");
            tab_tabla.getColumna("numero_cpcfa").setNombreVisual("N. FACTURA");
            tab_tabla.getColumna("nombre_inarti").setNombreVisual("ARTICULO");
            tab_tabla.getColumna("cantidad_cpdfa").setNombreVisual("CANTIDAD");
            tab_tabla.getColumna("precio_cpdfa").setNombreVisual("PRECIO UNI.");
            tab_tabla.getColumna("valor_cpdfa").setNombreVisual("TOTAL");
            tab_tabla.getColumna("precio_cpdfa").alinearDerecha();
            tab_tabla.getColumna("valor_cpdfa").alinearDerecha();
            tab_tabla.getColumna("valor_cpdfa").setEstilo("font-weight: bold");
            tab_tabla.getColumna("nombre_inarti").setFiltroContenido();
            tab_tabla.getColumna("numero_cpcfa").setFiltroContenido();

            tab_tabla.setLectura(true);
            tab_tabla.setScrollable(true);
            tab_tabla.setScrollHeight(300);
            tab_tabla.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla);
            gru_grupo.getChildren().add(pat_panel);
        }

        mep_menu.dibujar(3, "PRODUCTOS COMPRADOS POR EL PROVEEDOR", gru_grupo);
    }

    public void dibujarConfiguraCuenta() {
        Grupo gru_grupo = new Grupo();
        if (isProveedorSeleccionado()) {
            aut_cuentas = new AutoCompletar();
            aut_cuentas.setId("aut_cuentas");
            aut_cuentas.setAutoCompletar(ser_contabilidad.getSqlCuentasPasivos());
            aut_cuentas.setSize(75);
            aut_cuentas.setDisabled(true);
            aut_cuentas.setMetodoChange("seleccionarCuentaContable");
            aut_cuentas.setValor(ser_proveedor.getCuentaProveedor(aut_proveedor.getValor()));
            aut_cuentas.setAutocompletarContenido();

            Grid gri_contenido = new Grid();
            gri_contenido.setColumns(3);
            gri_contenido.getChildren().add(new Etiqueta("<strong>CUENTA CONTABLE : </strong>"));
            gri_contenido.getChildren().add(aut_cuentas);
            Boton bot_cambia = new Boton();
            bot_cambia.setValue("Cambiar");
            bot_cambia.setIcon("ui-icon-refresh");
            bot_cambia.setMetodo("activaCambiarCuenta");
            gri_contenido.getChildren().add(bot_cambia);
            gru_grupo.getChildren().add(gri_contenido);
            gru_grupo.getChildren().add(new Etiqueta("<p style='padding-top:10px;'><strong >NOTA: </strong> La cuenta contable seleccionada se relacionará a los movimientos-transacciones que realice el Proveedor, a partir que se <strong>guarde </strong> el cambio. </p>"));
        }
        mep_menu.dibujar(4, "CONFIGURACIÓN DE CUENTA CONTABLE", gru_grupo);
    }

    public void dibujarMovimientos() {
        Grupo gru_grupo = new Grupo();
        if (isProveedorSeleccionado()) {
            TablaGenerica tab_cuenta = ser_contabilidad.getCuenta(ser_proveedor.getCuentaProveedor(aut_proveedor.getValor()));
            if (!tab_cuenta.isEmpty()) {

                Fieldset fis_consulta = new Fieldset();
                fis_consulta.setLegend("Detalle de la Consulta");
                fis_consulta.getChildren().add(new Etiqueta("<p style='font-size:16px;padding-bottom:5px;'> <strong>" + tab_cuenta.getValor("codig_recur_cndpc") + "</strong> &nbsp; " + tab_cuenta.getValor("nombre_cndpc") + "</p>"));

                Grid gri_fechas = new Grid();
                gri_fechas.setColumns(5);
                gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA DESDE :</strong>"));
                cal_fecha_inicio = new Calendario();
                cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
                gri_fechas.getChildren().add(cal_fecha_inicio);
                gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA HASTA :</strong>"));
                cal_fecha_fin = new Calendario();
                cal_fecha_fin.setFechaActual();
                gri_fechas.getChildren().add(cal_fecha_fin);
                fis_consulta.getChildren().add(gri_fechas);

                Boton bot_consultar = new Boton();
                bot_consultar.setValue("Consultar");
                bot_consultar.setMetodo("actualizarMovimientos");
                bot_consultar.setIcon("ui-icon-search");

                gri_fechas.getChildren().add(bot_consultar);

                gru_grupo.getChildren().add(fis_consulta);

                tab_tabla = new Tabla();
                tab_tabla.setNumeroTabla(4);
                tab_tabla.setId("tab_tabla");
                tab_tabla.setSql(ser_contabilidad.getSqlMovimientosCuentaPersona(ser_proveedor.getCuentaProveedor(aut_proveedor.getValor()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), aut_proveedor.getValor()));
                tab_tabla.setLectura(true);
                tab_tabla.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
                tab_tabla.getColumna("fecha_trans_cnccc").setNombreVisual("FECHA");
                tab_tabla.getColumna("ide_cnlap").setVisible(false);
                tab_tabla.getColumna("debe").setLongitud(20);
                tab_tabla.getColumna("haber").setLongitud(20);
                tab_tabla.getColumna("saldo").setLongitud(25);
                tab_tabla.getColumna("debe").alinearDerecha();
                tab_tabla.getColumna("haber").alinearDerecha();
                tab_tabla.getColumna("saldo").alinearDerecha();
                tab_tabla.getColumna("saldo").setEstilo("font-weight: bold;");
                tab_tabla.getColumna("valor_cndcc").setVisible(false);
                tab_tabla.getColumna("debe").setSuma(false);
                tab_tabla.getColumna("haber").setSuma(false);
                tab_tabla.getColumna("saldo").setSuma(false);
                tab_tabla.setColumnaSuma("debe,haber,saldo");
                tab_tabla.setRows(20);
                tab_tabla.setOrdenar(false);
                tab_tabla.dibujar();
                PanelTabla pat_panel = new PanelTabla();
                pat_panel.setPanelTabla(tab_tabla);
                gru_grupo.getChildren().add(pat_panel);
                actualizarSaldosContable();
            } else {
                utilitario.agregarMensajeInfo("No se encontro Cuenta Contable", "El proveedor seleccionado no tiene asociado una cuenta contable");
            }
        }
        mep_menu.dibujar(5, "MOVIMIENTOS CONTABLES", gru_grupo);
    }

    public void dibujarFacturas() {
        Grupo gru_grupo = new Grupo();
        if (isProveedorSeleccionado()) {
            tab_tabla = new Tabla();
            tab_tabla.setNumeroTabla(6);
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_proveedor.getSqlFacturasPorPagar(aut_proveedor.getValor()));
            tab_tabla.getColumna("saldo_x_pagar").setEstilo("font-size: 13px;font-weight: bold");
            tab_tabla.getColumna("saldo_x_pagar").alinearDerecha();
            tab_tabla.setCampoPrimaria("ide_cpctr");
            tab_tabla.getColumna("ide_cpctr").setVisible(false);
            tab_tabla.getColumna("ide_cpcfa").setVisible(false);
            tab_tabla.getColumna("fecha").setVisible(true);
            tab_tabla.getColumna("numero_cpcfa").setNombreVisual("N. FACTURA");
            tab_tabla.getColumna("numero_cpcfa").setFiltroContenido();
            tab_tabla.getColumna("saldo_x_pagar").setNombreVisual("SALDO");
            tab_tabla.getColumna("total_cpcfa").setNombreVisual("TOTAL");
            tab_tabla.getColumna("total_cpcfa").setEstilo("font-size: 13px;");
            tab_tabla.getColumna("total_cpcfa").alinearDerecha();
            tab_tabla.setLectura(true);
            tab_tabla.setColumnaSuma("saldo_x_pagar");
            tab_tabla.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla);
            gru_grupo.getChildren().add(pat_panel);

            if (tab_tabla.isEmpty()) {
                tab_tabla.setEmptyMessage("No tiene facturas por pagar al proveedor seleccionado");
            }
        }
        mep_menu.dibujar(6, "FACTURAS POR PAGAR AL PROVEEDOR", gru_grupo);
    }

    public void dibujarEstructura() {
        Grupo gru_grupo = new Grupo();
        arb_estructura = new Arbol();
        arb_estructura.setId("arb_estructura");
        arb_estructura.setArbol("gen_persona", "ide_geper", "nom_geper", "gen_ide_geper");
        arb_estructura.setCondicion("es_proveedo_geper=true");
        arb_estructura.dibujar();
        //Selecciona el nodo
        if (aut_proveedor.getValor() != null) {
            arb_estructura.seleccinarNodo(aut_proveedor.getValor());
            arb_estructura.getNodoSeleccionado().setExpanded(true);
            arb_estructura.getNodoSeleccionado().getParent().setExpanded(true);
        }
        PanelArbol paa_panel = new PanelArbol();
        paa_panel.setPanelArbol(arb_estructura);
        gru_grupo.getChildren().add(paa_panel);
        mep_menu.dibujar(7, "CLASIFICACIÓN DE PROVEEDORES", gru_grupo);
    }

    public void dibujarGrafico() {
        Grupo gru_grupo = new Grupo();
        if (isProveedorSeleccionado()) {
            gca_grafico = new GraficoCartesiano();
            gca_grafico.setId("gca_grafico");

            com_periodo = new Combo();
            com_periodo.setMetodo("actualizarGrafico");
            com_periodo.setCombo(ser_proveedor.getSqlAniosCompras());
            com_periodo.eliminarVacio();
            // com_periodo.setValue(utilitario.getAnio(utilitario.getFechaActual()));
            tab_tabla = new Tabla();
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_proveedor.getSqlTotalComprasMensualesProveedor(aut_proveedor.getValor(), String.valueOf(com_periodo.getValue())));
            tab_tabla.setLectura(true);
            tab_tabla.setColumnaSuma("num_facturas,ventas12,ventas0,iva,total");
            tab_tabla.getColumna("num_facturas").alinearDerecha();
            tab_tabla.getColumna("ventas12").alinearDerecha();
            tab_tabla.getColumna("ventas0").alinearDerecha();
            tab_tabla.getColumna("iva").alinearDerecha();
            tab_tabla.getColumna("total").alinearDerecha();
            tab_tabla.dibujar();

            Grid gri_opciones = new Grid();
            gri_opciones.setColumns(2);
            gri_opciones.getChildren().add(new Etiqueta("<strong>PERÍODO :</strong>"));
            gri_opciones.getChildren().add(com_periodo);
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.getChildren().add(gri_opciones);
            pat_panel.setPanelTabla(tab_tabla);

            gca_grafico.setTitulo("COMPRAS MENSUALES");
            gca_grafico.agregarSerie(tab_tabla, "nombre_gemes", "total", "COMPRAS " + String.valueOf(com_periodo.getValue()));

            gru_grupo.getChildren().add(pat_panel);
            gru_grupo.getChildren().add(gca_grafico);
        }
        mep_menu.dibujar(8, "GRAFICO DE COMPRAS", gru_grupo);
    }

    public void dibujarProductosComprados() {
        Grupo gru_grupo = new Grupo();
        if (isProveedorSeleccionado()) {
            tab_tabla = new Tabla();
            tab_tabla.setId("tab_tabla");
            tab_tabla.setSql(ser_proveedor.getSqlProductosComprados(aut_proveedor.getValor()));
            tab_tabla.getColumna("ide_inarti").setVisible(false);
            tab_tabla.setRows(25);
            tab_tabla.getColumna("nombre_inarti").setNombreVisual("PRODUCTO");
            tab_tabla.getColumna("nombre_inarti").setFiltroContenido();
            tab_tabla.getColumna("nombre_inarti").setLongitud(200);
            tab_tabla.getColumna("fecha_ultima_compra").setNombreVisual("FECHA ULTIMA COMPRA");
            tab_tabla.getColumna("fecha_ultima_compra").setLongitud(100);
            tab_tabla.getColumna("ultimo_precio").setNombreVisual("PRECIO ULTIMA COMPRA");
            tab_tabla.getColumna("ultimo_precio").setFormatoNumero(2);
            tab_tabla.getColumna("ultimo_precio").alinearDerecha();
            tab_tabla.getColumna("ultimo_precio").setLongitud(100);
            tab_tabla.getColumna("ultimo_precio").setEstilo("font-weight: bold;font-size:14px");
            tab_tabla.setLectura(true);
            tab_tabla.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla);
            gru_grupo.getChildren().add(pat_panel1);

        }
        mep_menu.dibujar(9, "PRODUCTOS COMPRADOS", gru_grupo);
    }

    /**
     * Actualiza el grafico de compras segun el combo de periodo
     */
    public void actualizarGrafico() {
        tab_tabla.setSql(ser_proveedor.getSqlTotalComprasMensualesProveedor(aut_proveedor.getValor(), String.valueOf(com_periodo.getValue())));
        tab_tabla.ejecutarSql();
        gca_grafico.limpiar();
        gca_grafico.agregarSerie(tab_tabla, "nombre_gemes", "total", "COMPRAS " + String.valueOf(com_periodo.getValue()));
        utilitario.addUpdate("gca_grafico");
    }

    /**
     * Actualiza los movmientos contables segun las fechas selecionadas
     */
    public void actualizarMovimientos() {
        tab_tabla.setSql(ser_contabilidad.getSqlMovimientosCuentaPersona(ser_proveedor.getCuentaProveedor(aut_proveedor.getValor()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), aut_proveedor.getValor()));
        tab_tabla.ejecutarSql();
        actualizarSaldosContable();
    }

    /**
     * Actualiza las transacciones de un Proveedor segun las fechas selecionadas
     */
    public void actualizarTransacciones() {
        tab_tabla.setSql(ser_proveedor.getSqlTransaccionesProveedor(aut_proveedor.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla.ejecutarSql();
        actualizarSaldoxPagar();
    }

    /**
     * Actualiza los productos comprados del Proveedor segun las fechas
     * selecionadas
     */
    public void actualizarProducots() {
        tab_tabla.setSql(ser_proveedor.getSqlProductosProveedor(aut_proveedor.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla.ejecutarSql();
        if (tab_tabla.isEmpty()) {

            tab_tabla.setEmptyMessage("No Productos en el rango de fechas seleccionado");
        }
    }

    /**
     * Actualiza los solados que se visualizan en pantalla
     */
    private void actualizarSaldosContable() {

        double saldo_anterior = ser_contabilidad.getSaldoInicialCuenta(ser_proveedor.getCuentaProveedor(aut_proveedor.getValor()), cal_fecha_inicio.getFecha());
        double dou_saldo_inicial = saldo_anterior;
        double dou_saldo_actual = 0;
        double dou_debe = 0;
        double dou_haber = 0;
        String p_con_lugar_debe = utilitario.getVariable("p_con_lugar_debe");

        for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
            if (tab_tabla.getValor(i, "ide_cnlap").equals(p_con_lugar_debe)) {
                tab_tabla.setValor(i, "debe", utilitario.getFormatoNumero(Math.abs(Double.parseDouble(tab_tabla.getValor(i, "valor_cndcc")))));
                dou_debe += Double.parseDouble(tab_tabla.getValor(i, "debe"));

            } else {
                tab_tabla.setValor(i, "haber", utilitario.getFormatoNumero(tab_tabla.getValor(i, "valor_cndcc")));
                dou_haber += Double.parseDouble(tab_tabla.getValor(i, "haber"));
            }
            dou_saldo_actual = saldo_anterior + Double.parseDouble(tab_tabla.getValor(i, "valor_cndcc"));
            tab_tabla.setValor(i, "saldo", utilitario.getFormatoNumero(dou_saldo_actual));

            saldo_anterior = dou_saldo_actual;
        }
        if (tab_tabla.isEmpty()) {
            dou_saldo_actual = dou_saldo_inicial;
            tab_tabla.setEmptyMessage("No existen Movimientos Contables en el rango de fechas seleccionado");
        }
        //INSERTA PRIMERA FILA SALDO INICIAL
        if (dou_saldo_inicial != 0) {
            tab_tabla.setLectura(false);
            tab_tabla.insertar();
            tab_tabla.setValor("saldo", utilitario.getFormatoNumero(dou_saldo_inicial));
            tab_tabla.setValor("OBSERVACION", "SALDO INICIAL AL " + cal_fecha_inicio.getFecha());
            tab_tabla.setValor("fecha_trans_cnccc", cal_fecha_inicio.getFecha());
            tab_tabla.setLectura(true);
        }
        //ASIGNA SALDOS FINALES
        tab_tabla.getColumna("saldo").setTotal(dou_saldo_actual);
        tab_tabla.getColumna("debe").setTotal(dou_debe);
        tab_tabla.getColumna("haber").setTotal(dou_haber);
    }

    /**
     * *
     * Activa el Autocompletar para cambiar la cuenta contable
     */
    public void activaCambiarCuenta() {
        if (isProveedorSeleccionado()) {
            aut_cuentas.setDisabled(false);
            utilitario.addUpdate("aut_cuentas");
        }
    }

    /**
     * Selecciona una nueva cuenta contable, y agrega el SQL para que pueda ser
     * guardado
     *
     * @param evt
     */
    public void seleccionarCuentaContable(SelectEvent evt) {
        aut_cuentas.onSelect(evt);
        if (isProveedorSeleccionado()) {
            if (aut_cuentas.getValor() != null) {
                utilitario.getConexion().getSqlPantalla().clear();
                if (ser_proveedor.isTieneCuentaConfiguradaProveedor(aut_proveedor.getValor()) == false) {
                    //nueva
                    utilitario.getConexion().agregarSqlPantalla(ser_proveedor.getSqlInsertarCuentaProveedor(aut_proveedor.getValor(), aut_cuentas.getValor()));
                } else {
                    //modifica
                    utilitario.getConexion().agregarSqlPantalla(ser_proveedor.getSqlActualizarCuentaProveedor(aut_proveedor.getValor(), aut_cuentas.getValor()));
                }
            }
        }

    }

    /**
     * Validacion para que se seleccione un Proveedor del Autocompletar
     *
     * @return
     */
    private boolean isProveedorSeleccionado() {
        if (aut_proveedor.getValor() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Proveedor", "Seleccione un proveedor de la lista del Autocompletar");
            return false;
        }
        return true;
    }

    /**
     * Calcula el saldo por cobrar del Proveedor
     *
     * @return
     */
    private void actualizarSaldoxPagar() {
        double saldo_anterior = ser_proveedor.getSaldoInicialProveedor(aut_proveedor.getValor(), cal_fecha_inicio.getFecha());
        double dou_saldo_inicial = saldo_anterior;
        double dou_saldo_actual = 0;
        double dou_ingresos = 0;
        double dou_egresos = 0;

        for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
            if (tab_tabla.getValor(i, "ingresos") != null) {
                dou_ingresos += Double.parseDouble(tab_tabla.getValor(i, "ingresos"));
                dou_saldo_actual = saldo_anterior + Double.parseDouble(tab_tabla.getValor(i, "ingresos"));
            } else {
                dou_egresos += Double.parseDouble(tab_tabla.getValor(i, "egresos"));
                dou_saldo_actual = saldo_anterior - Double.parseDouble(tab_tabla.getValor(i, "egresos"));
            }

            tab_tabla.setValor(i, "saldo", utilitario.getFormatoNumero(dou_saldo_actual));
            saldo_anterior = dou_saldo_actual;
        }
        if (tab_tabla.isEmpty()) {
            dou_saldo_actual = dou_saldo_inicial;
            tab_tabla.setEmptyMessage("No existen Transacciones en el rango de fechas seleccionado");
        }
        //INSERTA PRIMERA FILA SALDO INICIAL
        if (dou_saldo_inicial != 0) {
            tab_tabla.setLectura(false);
            tab_tabla.insertar();
            tab_tabla.setValor("saldo", utilitario.getFormatoNumero(dou_saldo_inicial));
            tab_tabla.setValor("OBSERVACION", "SALDO INICIAL AL " + cal_fecha_inicio.getFecha());
            tab_tabla.setValor("fecha_trans_cpdtr", cal_fecha_inicio.getFecha());
            tab_tabla.setLectura(true);
        }
        //ASIGNA SALDOS FINALES
        tab_tabla.getColumna("saldo").setTotal(dou_saldo_actual);
        tab_tabla.getColumna("INGRESOS").setTotal(dou_ingresos);
        tab_tabla.getColumna("EGRESOS").setTotal(dou_egresos);

    }

    /**
     * Limpia la pantalla y el autocompletar
     */
    public void limpiar() {
        aut_proveedor.limpiar();
        mep_menu.limpiar();
    }

    @Override
    public void insertar() {
        if (mep_menu.getOpcion() == -1) {
            //PANTALLA LIMPIA
            dibujarProveedor();
        }

        if (mep_menu.getOpcion() == 1) {
            //FORMULARIO Proveedor
            aut_proveedor.limpiar();
            tab_tabla.insertar();
        }
    }

    @Override
    public void guardar() {
        if (mep_menu.getOpcion() == 1) {
            //FORMULARIO Proveedor
            if (ser_proveedor.validarProveedor(tab_tabla)) {
                tab_tabla.guardar();
                if (guardarPantalla().isEmpty()) {
                    //Actualiza el autocompletar
                    aut_proveedor.actualizar();
                    aut_proveedor.setSize(75);
                    aut_proveedor.setValor(tab_tabla.getValorSeleccionado());
                    utilitario.addUpdate("aut_proveeedor");
                }
            }
        } else if (mep_menu.getOpcion() == 4) {
            //Cambiar Cuenta Contable
            if (guardarPantalla().isEmpty()) {
                aut_cuentas.setDisabled(true);
            }
        } else if (mep_menu.getOpcion() == 7) {
            //Classificacion de Proveedores
            guardarPantalla();
        } else if (mep_menu.getOpcion() == 10) {
            String ide_cnccc = null;
            if (tex_num_asiento.getValue() != null) {
                if (tex_num_asiento.getValue().toString().isEmpty() == false) {
                    TablaGenerica tab_asiento = ser_comp_conta.getCabeceraComprobante(tex_num_asiento.getValue().toString());
                    if (tab_asiento.isEmpty()) {
                        utilitario.agregarMensajeError("El asiento contable Num. " + tex_num_asiento.getValue() + " no existe", "");
                        return;
                    } else {
                        ide_cnccc = tex_num_asiento.getValue().toString();
                    }
                }
            }

            String ide_cpctr = null;
            String ide_cpcfa = null;
            if (com_fac_pendientes.getValue() == null) {
                TablaGenerica tab_cab = new TablaGenerica();
                tab_cab.setTabla("cxp_cabece_transa", "ide_cpctr");
                tab_cab.setCondicion("ide_cpctr=-1");
                tab_cab.ejecutarSql();
                tab_cab.insertar();
                tab_cab.setValor("fecha_trans_cpctr", tab_tabla.getValor("fecha_trans_cpdtr"));
                tab_cab.setValor("ide_geper", aut_proveedor.getValor());
                tab_cab.setValor("observacion_cpctr", tab_tabla.getValor("observacion_cpdtr"));
                tab_cab.setValor("ide_cpttr", tab_tabla.getValor("ide_cpttr"));
                tab_cab.guardar();
                ide_cpctr = tab_cab.getValor("ide_cpctr");
            } else {
                ide_cpctr = com_fac_pendientes.getValue().toString();
                ide_cpcfa = utilitario.consultar("select ide_cpctr,ide_cpcfa from cxp_detall_transa where ide_cpctr=" + ide_cpctr + " and numero_pago_cpdtr=0 order by ide_cpctr").getValor("ide_cpcfa");
                tab_tabla.setValor("numero_pago_cpdtr", "1");
                tab_tabla.setValor("ide_cpcfa", ide_cpcfa);
            }

            if (ide_cnccc != null) {
                //Asigna num de asiento a documento cxp y a transaccion cxp
                utilitario.getConexion().agregarSqlPantalla("UPDATE cxp_cabece_factur set ide_cnccc=" + ide_cnccc + " WHERE ide_cpcfa=" + ide_cpcfa);
                if (ide_cpcfa != null) {
                    utilitario.getConexion().agregarSqlPantalla("UPDATE cxp_detall_transa set ide_cnccc=" + ide_cnccc + " WHERE ide_cpctr=" + ide_cpctr + " and ide_cnccc is null");
                }
            }

            tab_tabla.setValor("fecha_venci_cpdtr", tab_tabla.getValor("fecha_trans_cpdtr"));
            tab_tabla.setValor("ide_cpctr", ide_cpctr);
            tab_tabla.guardar();
            if (guardarPantalla().isEmpty()) {
                dibujarTransacciones();
            }
        }
    }

    @Override
    public void eliminar() {
        if (mep_menu.getOpcion() == 1) {
            //FORMULARIO Proveedor
            tab_tabla.eliminar();
        }
    }

    @Override
    public void actualizar() {
        super.actualizar();
        if (mep_menu.getOpcion() == 2) {
            actualizarSaldoxPagar();
        } else if (mep_menu.getOpcion() == 5) {
            actualizarSaldosContable();
        }
    }

    public AutoCompletar getAut_proveedor() {
        return aut_proveedor;
    }

    public void setAut_proveedor(AutoCompletar aut_proveedor) {
        this.aut_proveedor = aut_proveedor;
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public AutoCompletar getAut_cuentas() {
        return aut_cuentas;
    }

    public void setAut_cuentas(AutoCompletar aut_cuentas) {
        this.aut_cuentas = aut_cuentas;
    }

    public Arbol getArb_estructura() {
        return arb_estructura;
    }

    public void setArb_estructura(Arbol arb_estructura) {
        this.arb_estructura = arb_estructura;
    }

    public GraficoCartesiano getGca_grafico() {
        return gca_grafico;
    }

    public void setGca_grafico(GraficoCartesiano gca_grafico) {
        this.gca_grafico = gca_grafico;
    }

    public AsientoContable getAsc_asiento() {
        return asc_asiento;
    }

    public void setAsc_asiento(AsientoContable asc_asiento) {
        this.asc_asiento = asc_asiento;
    }

}

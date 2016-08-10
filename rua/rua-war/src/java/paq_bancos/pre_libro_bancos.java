/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bancos;

import componentes.CuentaCxc;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Etiqueta;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import servicios.contabilidad.ServicioContabilidadGeneral;
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
    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);
    private Tabla tab_tabla1;

    ///Cuentas por Cobrar
    private CuentaCxc cxc_cuenta = new CuentaCxc();

    public pre_libro_bancos() {

        mep_menu.setMenuPanel("CONSULTAS", "20%");
        mep_menu.agregarItem("Posición Consolidada", "dibujarPosicion", "ui-icon-note");
        mep_menu.agregarItem("Consulta de Movimientos", "dibujarMovimienots", "ui-icon-note");
        mep_menu.agregarSubMenu("TRANSACCIONES");
        mep_menu.agregarItem("Cuentas por Cobrar", "dibujarCxC", "ui-icon-calculator");
        mep_menu.agregarItem("Cuentas por Pagar", "dibujarCxP", "ui-icon-calculator");
        mep_menu.agregarItem("Otras Transacciones", "dibujarOtros", "ui-icon-calculator");
        mep_menu.agregarItem("Transferencias entre Cuentas", "dibujarTransferencias", "ui-icon-calculator");
        mep_menu.agregarSubMenu("HERRAMIENTAS");
        mep_menu.agregarItem("Conciliar", "dibujarConciliar", "ui-icon-pencil");      

        agregarComponente(mep_menu);

        aut_cuentas.setId("aut_cuentas");
        aut_cuentas.setAutocompletarContenido();
        aut_cuentas.setDropdown(true);
        aut_cuentas.setAutoCompletar(ser_tesoreria.getSqlComboCuentas());
        aut_cuentas.setMetodoChange("actualizarMovimientos");

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

        cxc_cuenta.setId("cxc_cuenta");
        agregarComponente(cxc_cuenta);
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
        tab_tabla1.agregarColumna("seleccionarCuenta", "/imagenes/im_go.png", "Consultar Movimientos");
        if (tab_tabla1.isEmpty() == false) {
            aut_cuentas.setValor(tab_tabla1.getValor("ide_tecba"));
        }

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.setMensajeInfo(utilitario.getFechaLarga(utilitario.getFechaActual()));
        mep_menu.dibujar(1, "POSICIÓN CONSOLIDADA", pat_panel);
    }

    public void seleccionarCuenta() {
        aut_cuentas.setValor(tab_tabla1.getValor("ide_tecba"));
        utilitario.addUpdate("aut_cuentas");
        dibujarMovimienots();
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
        tab_tabla1.getColumna("IDE_CNCCC").setFiltroContenido();
        tab_tabla1.getColumna("numero_teclb").setFiltroContenido();
        tab_tabla1.getColumna("beneficiari_teclb").setFiltroContenido();
        tab_tabla1.getColumna("nombre_tettb").setFiltroContenido();
        tab_tabla1.setColumnaSuma("INGRESOS,EGRESOS,SALDO");
        tab_tabla1.dibujar();
        actualizarSaldos();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        mep_menu.dibujar(2, "CONSULTA DE MOVIMIENTOS", pat_panel);
    }

    public void dibujarCxC() {
        cxc_cuenta.dibujar();
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

    @Override
    public void actualizar() {
        actualizarMovimientos();
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

    public CuentaCxc getCxc_cuenta() {
        return cxc_cuenta;
    }

    public void setCxc_cuenta(CuentaCxc cxc_cuenta) {
        this.cxc_cuenta = cxc_cuenta;
    }

}

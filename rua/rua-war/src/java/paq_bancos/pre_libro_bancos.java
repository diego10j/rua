/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bancos;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import servicios.tesoreria.ServicioTesoreria;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author dfjacome
 */
public class pre_libro_bancos extends Pantalla {

    private AutoCompletar aut_cuentas = new AutoCompletar();
    @EJB
    private final ServicioTesoreria ser_tesoreria = (ServicioTesoreria) utilitario.instanciarEJB(ServicioTesoreria.class);
    private final Calendario cal_fecha_inicio = new Calendario();
    private final Calendario cal_fecha_fin = new Calendario();

    private Tabla tab_movimientos = new Tabla();

    public pre_libro_bancos() {
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
        bot_consultar.setValue("Consultar");
        bot_consultar.setMetodo("actualizarMovimientos");
        bot_consultar.setIcon("ui-icon-search");

        bar_botones.agregarBoton(bot_consultar);

        tab_movimientos.setId("tab_movimientos");
        tab_movimientos.setSql(ser_tesoreria.getSqlTransaccionesCuenta(aut_cuentas.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_movimientos.setRows(15);
        tab_movimientos.setLectura(true);
        tab_movimientos.setOrdenar(false);
        tab_movimientos.getColumna("ide_teclb").setVisible(false);
        tab_movimientos.setCampoPrimaria("ide_teclb");
        tab_movimientos.getColumna("EGRESOS").alinearDerecha();
        tab_movimientos.getColumna("EGRESOS").setLongitud(25);
        tab_movimientos.getColumna("INGRESOS").alinearDerecha();
        tab_movimientos.getColumna("INGRESOS").setLongitud(25);
        tab_movimientos.getColumna("SALDO").alinearDerecha();
        tab_movimientos.getColumna("SALDO").setLongitud(25);
        tab_movimientos.getColumna("SALDO").alinearDerecha();
        tab_movimientos.getColumna("SALDO").setSuma(false);
        tab_movimientos.getColumna("SALDO").setEstilo("font-weight: bold;");
        tab_movimientos.getColumna("IDE_CNCCC").setFiltroContenido();
        tab_movimientos.getColumna("numero_teclb").setFiltroContenido();
        tab_movimientos.getColumna("beneficiari_teclb").setFiltroContenido();
        tab_movimientos.getColumna("nombre_tettb").setFiltroContenido();
        tab_movimientos.setColumnaSuma("INGRESOS,EGRESOS,SALDO");

        tab_movimientos.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_movimientos);

        Division div = new Division();
        div.dividir1(pat_panel);

        agregarComponente(div);

    }

    private void actualizarSaldos() {
        if (aut_cuentas.getValor() != null) {
            double saldo_anterior = ser_tesoreria.getSaldoInicialCuenta(aut_cuentas.getValor(), cal_fecha_inicio.getFecha());
            double dou_saldo_inicial = saldo_anterior;
            double dou_saldo_actual = 0;

            for (int i = 0; i < tab_movimientos.getTotalFilas(); i++) {
                if (tab_movimientos.getValor(i, "ingresos") != null) {
                    dou_saldo_actual = saldo_anterior + Double.parseDouble(tab_movimientos.getValor(i, "ingresos"));
                } else {
                    dou_saldo_actual = saldo_anterior - Double.parseDouble(tab_movimientos.getValor(i, "egresos"));
                }
                tab_movimientos.setValor(i, "saldo", utilitario.getFormatoNumero(dou_saldo_actual));
                saldo_anterior = dou_saldo_actual;
            }
            if (tab_movimientos.isEmpty()) {
                dou_saldo_actual = dou_saldo_inicial;
                tab_movimientos.setEmptyMessage("No existen Transacciones en el rango de fechas seleccionado");
            }

            //INSERTA PRIMERA FILA SALDO INICIAL
            if (dou_saldo_inicial != 0) {
                tab_movimientos.setLectura(false);
                tab_movimientos.insertar();
                tab_movimientos.setValor("saldo", utilitario.getFormatoNumero(dou_saldo_inicial));
                tab_movimientos.setValor("NOMBRE_TETTB", "SALDO INICIAL");
                tab_movimientos.setValor("OBSERVACION_TECLB", "SALDO INICIAL AL " + cal_fecha_inicio.getFecha());
                tab_movimientos.setValor("FECHA_TRANS_TECLB", cal_fecha_inicio.getFecha());
                tab_movimientos.setLectura(true);
            }
            utilitario.addUpdate("tex_saldo_inicial,tex_saldo_final");
            //ASIGNA SALDOS FINALES
            tab_movimientos.getColumna("saldo").setTotal(dou_saldo_actual);

        }

    }

    public void actualizarMovimientos(SelectEvent evt) {
        aut_cuentas.onSelect(evt);
        actualizarMovimientos();
    }

    public void actualizarMovimientos() {
        if (aut_cuentas.getValor() != null) {
            tab_movimientos.setSql(ser_tesoreria.getSqlTransaccionesCuenta(aut_cuentas.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_movimientos.ejecutarSql();
        } else {
            tab_movimientos.limpiar();
            utilitario.agregarMensajeInfo("Seleccione una Cuenta", "Debe seleccionar una 'CUENTA'");
        }
        actualizarSaldos();
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

    public Tabla getTab_movimientos() {
        return tab_movimientos;
    }

    public void setTab_movimientos(Tabla tab_movimientos) {
        this.tab_movimientos = tab_movimientos;
    }

}

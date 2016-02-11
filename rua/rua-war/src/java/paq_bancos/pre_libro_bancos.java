/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bancos;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import javax.ejb.EJB;
import org.primefaces.component.fieldset.Fieldset;
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
    private Texto tex_saldo_inicial = new Texto();
    private Texto tex_saldo_final = new Texto();

    private Tabla tab_movimientos = new Tabla();

    public pre_libro_bancos() {
        aut_cuentas.setId("aut_cuentas");
        aut_cuentas.setAutocompletarContenido();
        aut_cuentas.setDropdown(true);
        aut_cuentas.setAutoCompletar(ser_tesoreria.getSqlComboCuentas());
        aut_cuentas.setMetodoChange("actualizarMovimientos");

        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonsNavegacion();

        bar_botones.agregarComponente(new Etiqueta("CUENTA :"));
        bar_botones.agregarComponente(aut_cuentas);

        bar_botones.agregarSeparador();

        Fieldset fis_consulta = new Fieldset();
        Grid gri_fechas = new Grid();
        gri_fechas.setColumns(7);
        gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA DESDE :</strong>"));

        cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
        gri_fechas.getChildren().add(cal_fecha_inicio);
        gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA HASTA :</strong>"));

        cal_fecha_fin.setFechaActual();
        gri_fechas.getChildren().add(cal_fecha_fin);
        fis_consulta.getChildren().add(gri_fechas);

        Boton bot_consultar = new Boton();
        bot_consultar.setValue("Consultar");
        bot_consultar.setMetodo("actualizarMovimientos");
        bot_consultar.setIcon("ui-icon-search");

        gri_fechas.getChildren().add(bot_consultar);

        fis_consulta.getChildren().add(gri_fechas);

        Grid gri_saldos = new Grid();
        gri_saldos.setColumns(4);
        Etiqueta eti_sinicial = new Etiqueta("<strong>SALDO INICIAL :</strong>");
        eti_sinicial.setStyle("text-decoration: underline;");
        gri_saldos.getChildren().add(eti_sinicial);
        tex_saldo_inicial = new Texto();
        tex_saldo_inicial.setId("tex_saldo_inicial");
        tex_saldo_inicial.setDisabled(true);
        tex_saldo_inicial.setValue("0.00");
        tex_saldo_inicial.setSize(10);
        tex_saldo_inicial.setStyle("font-size: 13px;font-weight: bold;text-align: right;");
        gri_saldos.getChildren().add(tex_saldo_inicial);
        Etiqueta eti_sfinal = new Etiqueta("<strong>SALDO FINAL :</strong>");
        eti_sfinal.setStyle("text-decoration: underline;");
        gri_saldos.getChildren().add(eti_sfinal);
        tex_saldo_final = new Texto();
        tex_saldo_final.setId("tex_saldo_final");
        tex_saldo_final.setDisabled(true);
        tex_saldo_final.setStyle("font-size: 13px;font-weight: bold;text-align: right;");
        tex_saldo_final.setSize(10);
        tex_saldo_final.setValue("0.00");
        gri_saldos.getChildren().add(tex_saldo_final);
        gri_fechas.getChildren().add(new Espacio("50", "5"));

        gri_fechas.getChildren().add(gri_saldos);

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
        tab_movimientos.getColumna("SALDO").setEstilo("font-weight: bold;");
        tab_movimientos.getColumna("IDE_CNCCC").setFiltroContenido();
        tab_movimientos.getColumna("numero_teclb").setFiltroContenido();
        tab_movimientos.getColumna("beneficiari_teclb").setFiltroContenido();
        tab_movimientos.getColumna("nombre_tettb").setFiltroContenido();
        tab_movimientos.setColumnaSuma("INGRESOS,EGRESOS");
        tab_movimientos.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_movimientos);
        Grupo gru_grupo = new Grupo();
        gru_grupo.setStyle("overflow: auto;display: block;");
        gru_grupo.getChildren().add(fis_consulta);
        gru_grupo.getChildren().add(pat_panel);

        agregarComponente(gru_grupo);

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
            tex_saldo_inicial.setValue(utilitario.getFormatoNumero(dou_saldo_inicial));
            tex_saldo_final.setValue(utilitario.getFormatoNumero(dou_saldo_actual));
            utilitario.addUpdate("tex_saldo_inicial,tex_saldo_final");
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

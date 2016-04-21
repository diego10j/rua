/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_contabilidad;

import framework.aplicacion.Fila;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;
import servicios.contabilidad.ServicioContabilidadGeneral;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author diego.jacome
 */
public class pre_contabilidad extends Pantalla {

    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);

    private final MenuPanel mep_menu = new MenuPanel();
    private AutoCompletar aut_cuenta;

    private Tabla tab_consulta;

    //Consultas
    private Calendario cal_fecha_inicio;
    private Calendario cal_fecha_fin;
    private Radio rad_niveles;

    public pre_contabilidad() {
        bar_botones.limpiar();
        bar_botones.agregarComponente(new Etiqueta("CUENTA CONTABLE :"));

        mep_menu.setMenuPanel("OPCIONES CONTABLIDAD", "20%");
        mep_menu.agregarItem("Libro Mayor", "dibujarLibroMayor", "ui-icon-bookmark"); //1
        mep_menu.agregarItem("Libro Diario", "dibujarLibroDiario", "ui-icon-bookmark");//2
        mep_menu.agregarItem("Balance General", "dibujarBalanceGeneral", "ui-icon-bookmark");//3
        mep_menu.agregarItem("Estado de Resultados", "dibujarEstadoResultados", "ui-icon-bookmark");//4

        agregarComponente(mep_menu);

    }

    /**
     * Selecciona una cuenta en el autocompletar para el libro mayor
     *
     * @param evt
     */
    public void seleccionarCuenta(SelectEvent evt) {
        aut_cuenta.onSelect(evt);
        actualizarLibroMayor();
    }

    public void dibujarLibroMayor() {
        Grupo gru_grupo = new Grupo();

        Fieldset fis_consulta = new Fieldset();
        Grupo gp = new Grupo();
        gp.getChildren().add(new Etiqueta("<strong>CUENTA CONTABLE : </strong>"));

        aut_cuenta = new AutoCompletar();
        aut_cuenta.setId("aut_cuenta");
        aut_cuenta.setAutoCompletar(ser_contabilidad.getSqlCuentasHijas());
        aut_cuenta.setSize(75);
        aut_cuenta.setAutocompletarContenido(); // no startWith para la busqueda
        aut_cuenta.setMetodoChange("seleccionarCuenta");
        gp.getChildren().add(aut_cuenta);
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        gp.getChildren().add(bot_clean);

        fis_consulta.getChildren().add(gp);

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
        bot_consultar.setMetodo("actualizarLibroMayor");
        bot_consultar.setIcon("ui-icon-search");

        gri_fechas.getChildren().add(bot_consultar);

        Separator separar = new Separator();
        fis_consulta.getChildren().add(separar);

        gru_grupo.getChildren().add(fis_consulta);

        tab_consulta = new Tabla();
        tab_consulta.setNumeroTabla(-1);
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql(ser_contabilidad.getSqlMovimientosCuenta(aut_cuenta.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_consulta.setLectura(true);
        tab_consulta.getColumna("ide_cnccc").setNombreVisual("N. COMP.");
        tab_consulta.getColumna("ide_cnccc").setFiltro(true);
        tab_consulta.getColumna("fecha_trans_cnccc").setNombreVisual("FECHA");
        tab_consulta.getColumna("ide_cnlap").setVisible(false);
        tab_consulta.getColumna("debe").setLongitud(20);
        tab_consulta.getColumna("haber").setLongitud(20);
        tab_consulta.getColumna("saldo").setLongitud(25);
        tab_consulta.getColumna("debe").alinearDerecha();
        tab_consulta.getColumna("haber").alinearDerecha();
        tab_consulta.getColumna("saldo").alinearDerecha();
        tab_consulta.getColumna("debe").setSuma(false);
        tab_consulta.getColumna("haber").setSuma(false);
        tab_consulta.getColumna("saldo").setSuma(false);
        tab_consulta.getColumna("saldo").setEstilo("font-weight: bold;");
        tab_consulta.getColumna("valor_cndcc").setVisible(false);
        tab_consulta.setColumnaSuma("debe,haber,saldo");
        tab_consulta.setRows(20);
        tab_consulta.setOrdenar(false);
        tab_consulta.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_consulta);
        gru_grupo.getChildren().add(pat_panel);
        actualizarSaldosLibroMayor();
        mep_menu.dibujar(1, "LIBRO MAYOR", gru_grupo);
    }

    /**
     * Actualiza libro mayor segun las fechas selecionadas
     */
    public void actualizarLibroMayor() {
        if (isCuentaSeleccionada()) {
            tab_consulta.setSql(ser_contabilidad.getSqlMovimientosCuenta(aut_cuenta.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_consulta.ejecutarSql();
            actualizarSaldosLibroMayor();
        }
    }

    /**
     * Actualiza los solados libro mayor
     */
    private void actualizarSaldosLibroMayor() {
        double saldo_anterior = ser_contabilidad.getSaldoInicialCuenta(aut_cuenta.getValor(), cal_fecha_inicio.getFecha());
        double dou_saldo_inicial = saldo_anterior;
        double dou_saldo_actual = 0;
        double dou_debe = 0;
        double dou_haber = 0;
        String p_con_lugar_debe = utilitario.getVariable("p_con_lugar_debe");
        for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {
            if (tab_consulta.getValor(i, "ide_cnlap").equals(p_con_lugar_debe)) {
                tab_consulta.setValor(i, "debe", utilitario.getFormatoNumero(Math.abs(Double.parseDouble(tab_consulta.getValor(i, "valor_cndcc")))));
                dou_debe += Double.parseDouble(tab_consulta.getValor(i, "debe"));
            } else {
                tab_consulta.setValor(i, "haber", utilitario.getFormatoNumero(tab_consulta.getValor(i, "valor_cndcc")));
                dou_haber += Double.parseDouble(tab_consulta.getValor(i, "haber"));
            }
            dou_saldo_actual = saldo_anterior + Double.parseDouble(tab_consulta.getValor(i, "valor_cndcc"));
            tab_consulta.setValor(i, "saldo", utilitario.getFormatoNumero(dou_saldo_actual));
            saldo_anterior = dou_saldo_actual;
        }
        if (tab_consulta.isEmpty()) {
            dou_saldo_actual = dou_saldo_inicial;
            tab_consulta.setEmptyMessage("No existen Movimientos Contables en el rango de fechas seleccionado");
        }
        //INSERTA PRIMERA FILA SALDO INICIAL
        if (isCuentaSeleccionada()) {
            tab_consulta.setLectura(false);
            tab_consulta.insertar();
            tab_consulta.setValor("saldo", utilitario.getFormatoNumero(dou_saldo_inicial));
            tab_consulta.setValor("OBSERVACION", "SALDO INICIAL AL " + cal_fecha_inicio.getFecha());
            tab_consulta.setValor("fecha_trans_cnccc", cal_fecha_inicio.getFecha());
            tab_consulta.setLectura(true);
        }
        //ASIGNA SALDOS FINALES
        tab_consulta.getColumna("saldo").setTotal(dou_saldo_actual);
        tab_consulta.getColumna("debe").setTotal(dou_debe);
        tab_consulta.getColumna("haber").setTotal(dou_haber);
    }

    public void dibujarLibroDiario() {
        Grupo gru_grupo = new Grupo();

        Fieldset fis_consulta = new Fieldset();
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
        bot_consultar.setMetodo("actualizarLibroDiario");
        bot_consultar.setIcon("ui-icon-search");

        gri_fechas.getChildren().add(bot_consultar);

        Separator separar = new Separator();
        fis_consulta.getChildren().add(separar);
        gru_grupo.getChildren().add(fis_consulta);

        tab_consulta = new Tabla();
        tab_consulta.setNumeroTabla(-1);
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql(ser_contabilidad.getSqlLibroDiario(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_consulta.getColumna("ide_cndcc").setVisible(false);
        tab_consulta.setLectura(true);
        tab_consulta.getColumna("ide_cnccc").setNombreVisual("N. COMP.");
        tab_consulta.getColumna("ide_cnccc").setFiltro(true);
        tab_consulta.getColumna("fecha_trans_cnccc").setNombreVisual("FECHA");
        tab_consulta.getColumna("codig_recur_cndpc").setNombreVisual("CODIGO CUENTA CONTABLE");
        tab_consulta.getColumna("nombre_cndpc").setNombreVisual("CUENTA CONTABLE");
        tab_consulta.getColumna("observacion_cnccc").setNombreVisual("OBSERVACION");
        tab_consulta.getColumna("nombre_cntcm").setNombreVisual("TIPO DE COMPROBANTE");
        tab_consulta.getColumna("numero_cnccc").setFiltro(true);
        tab_consulta.getColumna("numero_cnccc").setNombreVisual("SECUENCIAL");
        tab_consulta.getColumna("numero_cnccc").setLongitud(25);
        tab_consulta.getColumna("debe").setLongitud(20);
        tab_consulta.getColumna("haber").setLongitud(20);
        tab_consulta.getColumna("debe").alinearDerecha();
        tab_consulta.getColumna("haber").alinearDerecha();
        tab_consulta.setColumnaSuma("debe,haber");
        tab_consulta.setRows(20);
        tab_consulta.setOrdenar(false);
        tab_consulta.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_consulta);
        gru_grupo.getChildren().add(pat_panel);
        mep_menu.dibujar(2, "LIBRO DIARIO", gru_grupo);
    }

    public void actualizarLibroDiario() {
        tab_consulta.setSql(ser_contabilidad.getSqlLibroDiario(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_consulta.ejecutarSql();
    }

    public void dibujarBalanceGeneral() {
        Grupo gru_grupo = new Grupo();

        Fieldset fis_consulta = new Fieldset();
        Grid gr_nivel = new Grid();
        gr_nivel.setColumns(2);
        gr_nivel.getChildren().add(new Etiqueta("<strong>NIVEL PLAN DE CUENTAS :</strong> "));
        rad_niveles = new Radio();
        rad_niveles.setRadio(utilitario.getConexion().consultar(ser_contabilidad.getSqlNivelPlandeCuentas()));
        gr_nivel.getChildren().add(rad_niveles);

        fis_consulta.getChildren().add(gr_nivel);

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
        bot_consultar.setMetodo("actualizarBalanceGeneral");
        bot_consultar.setIcon("ui-icon-search");

        gri_fechas.getChildren().add(bot_consultar);

        Separator separar = new Separator();
        fis_consulta.getChildren().add(separar);
        gru_grupo.getChildren().add(fis_consulta);

        tab_consulta = new Tabla();
        tab_consulta.setNumeroTabla(-1);
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql(ser_contabilidad.getSqlBalanceGeneral(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_consulta.setScrollable(true);
        tab_consulta.setScrollHeight(280);
        tab_consulta.setLectura(true);
        tab_consulta.setOrdenar(false);
        tab_consulta.getColumna("CODIG_RECUR_CNDPC").setNombreVisual("CODIGO CUENTA CONTABLE");
        tab_consulta.getColumna("nombre_cndpc").setNombreVisual("CUENTA CONTABLE");
        tab_consulta.getColumna("ide_cndpc").setVisible(false);
        tab_consulta.getColumna("ide_cnncu").setVisible(false);
        tab_consulta.getColumna("ide_cntcu").setVisible(false);
        tab_consulta.getColumna("con_ide_cndpc").setVisible(false);
        tab_consulta.getColumna("valor").setLongitud(30);
        tab_consulta.getColumna("valor").alinearDerecha();
        tab_consulta.getColumna("valor").setNombreVisual("SALDO");
        tab_consulta.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_consulta);
        gru_grupo.getChildren().add(pat_panel);
        calcularBalance();
        mep_menu.dibujar(3, "BALANCE GENERAL", gru_grupo);

    }

    public void actualizarBalanceGeneral() {
        tab_consulta.setSql(ser_contabilidad.getSqlBalanceGeneral(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_consulta.ejecutarSql();
        calcularBalance();
    }

    private void calcularBalance() {
        int nivel_tope = 0;
        if (rad_niveles != null) {
            try {
                nivel_tope = Integer.parseInt(String.valueOf(rad_niveles.getValue()));
            } catch (Exception e) {
            }
        }
        if (nivel_tope == 0) {
            tab_consulta.limpiar();
            return;
        }
        List lis_padres = new ArrayList();
        List lis_valor_padre = new ArrayList();
        double valor_acu = 0;
        int nivel = ser_contabilidad.getUltimoNivelCuentas();
        String padre;
        int band = 0;
        do {
            for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {
                if (tab_consulta.getValor(i, "ide_cnncu").equals(String.valueOf(nivel))) {
                    padre = tab_consulta.getValor(i, "con_ide_cndpc");
                    for (Object lis_padre : lis_padres) {
                        if (padre != null && !padre.isEmpty()) {
                            if (lis_padre.equals(padre)) {
                                band = 1;
                                break;
                            }
                        }
                    }
                    if (band == 0) {
                        if (padre != null && !padre.isEmpty()) {
                            lis_padres.add(padre);
                        }
                        for (int j = 0; j < tab_consulta.getTotalFilas(); j++) {
                            if (padre != null && !padre.isEmpty() && tab_consulta.getValor(j, "con_ide_cndpc") != null && !tab_consulta.getValor(j, "con_ide_cndpc").isEmpty()) {
                                if (padre.equals(tab_consulta.getValor(j, "con_ide_cndpc"))) {
                                    try {
                                        valor_acu = valor_acu + Double.parseDouble(tab_consulta.getValor(j, "valor"));
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        }
                        lis_valor_padre.add(valor_acu);
                        valor_acu = 0;
                    } else {
                        band = 0;
                    }
                }
            }
            for (int i = 0; i < lis_padres.size(); i++) {
                padre = lis_padres.get(i).toString();
                for (int j = 0; j < tab_consulta.getTotalFilas(); j++) {
                    if (tab_consulta.getValor(j, "ide_cndpc").equals(padre)) {
                        try {
                            tab_consulta.setValor(j, "valor", lis_valor_padre.get(i).toString());
                        } catch (Exception e) {
                        }
                    }
                }
            }
            nivel = nivel - 1;
        } while (nivel >= 2);

        //elimina cuentas con saldo 0 mayores que el nivel tope
        Iterator<Fila> it = tab_consulta.getFilas().iterator();
        int numColumnaNivel = tab_consulta.getNumeroColumna("ide_cnncu");
        while (it.hasNext()) {
            Fila filaActual = it.next(); // must be called before you can call i.remove()
            // Do something
            if (Integer.parseInt(String.valueOf(filaActual.getCampos()[numColumnaNivel])) > nivel_tope) {
                it.remove();
            }
        }
    }

    public void dibujarEstadoResultados() {
        Grupo gru_grupo = new Grupo();

        Fieldset fis_consulta = new Fieldset();
        //fis_consulta.setLegend("Detalle de la Consulta");

        Grid gr_nivel = new Grid();
        gr_nivel.setColumns(2);
        gr_nivel.getChildren().add(new Etiqueta("<strong>NIVEL PLAN DE CUENTAS :</strong> "));
        rad_niveles = new Radio();
        rad_niveles.setRadio(utilitario.getConexion().consultar(ser_contabilidad.getSqlNivelPlandeCuentas()));
        gr_nivel.getChildren().add(rad_niveles);

        fis_consulta.getChildren().add(gr_nivel);

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
        bot_consultar.setMetodo("actualizarEstadoResultados");
        bot_consultar.setIcon("ui-icon-search");

        gri_fechas.getChildren().add(bot_consultar);

        Separator separar = new Separator();
        fis_consulta.getChildren().add(separar);
        gru_grupo.getChildren().add(fis_consulta);

        tab_consulta = new Tabla();
        tab_consulta.setNumeroTabla(-1);
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql(ser_contabilidad.getSqlEstadoResultados(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_consulta.setScrollable(true);
        tab_consulta.setScrollHeight(280);
        tab_consulta.setLectura(true);
        tab_consulta.setOrdenar(false);
        tab_consulta.getColumna("CODIG_RECUR_CNDPC").setNombreVisual("CODIGO CUENTA CONTABLE");
        tab_consulta.getColumna("nombre_cndpc").setNombreVisual("CUENTA CONTABLE");
        tab_consulta.getColumna("ide_cndpc").setVisible(false);
        tab_consulta.getColumna("ide_cnncu").setVisible(false);
        tab_consulta.getColumna("ide_cntcu").setVisible(false);
        tab_consulta.getColumna("con_ide_cndpc").setVisible(false);
        tab_consulta.getColumna("valor").setLongitud(30);
        tab_consulta.getColumna("valor").alinearDerecha();
        tab_consulta.getColumna("valor").setNombreVisual("SALDO");
        tab_consulta.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_consulta);
        gru_grupo.getChildren().add(pat_panel);
        calcularBalance();
        mep_menu.dibujar(4, "ESTADO DE RESULTADOS", gru_grupo);

    }

    public void actualizarEstadoResultados() {
        tab_consulta.setSql(ser_contabilidad.getSqlEstadoResultados(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_consulta.ejecutarSql();
        calcularBalance();
    }

    /**
     * Validacion para que se seleccione un Proveedor del Autocompletar
     *
     * @return
     */
    private boolean isCuentaSeleccionada() {
        if (aut_cuenta.getValor() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar una Cuenta Contable", "Seleccione una cuenta contable de la lista del Autocompletar");
            return false;
        }
        return true;
    }

    /**
     * Limpia el autocompletar y la tabla de consulta
     */
    public void limpiar() {
        aut_cuenta.limpiar();
        tab_consulta.limpiar();
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

    public AutoCompletar getAut_cuenta() {
        return aut_cuenta;
    }

    public void setAut_cuenta(AutoCompletar aut_cuenta) {
        this.aut_cuenta = aut_cuenta;
    }

    public Tabla getTab_consulta() {
        return tab_consulta;
    }

    public void setTab_consulta(Tabla tab_consulta) {
        this.tab_consulta = tab_consulta;
    }
}

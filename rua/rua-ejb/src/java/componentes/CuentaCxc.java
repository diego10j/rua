/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import servicios.cuentas_x_cobrar.ServicioCliente;
import servicios.cuentas_x_cobrar.ServicioFacturaCxC;
import servicios.tesoreria.ServicioTesoreria;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
public class CuentaCxc extends Dialogo {

    private final Utilitario utilitario = new Utilitario();

    /////TESORERIA
    @EJB
    private final ServicioTesoreria ser_tesoreria = (ServicioTesoreria) utilitario.instanciarEJB(ServicioTesoreria.class);
    ////CLIENTES
    @EJB
    private final ServicioCliente ser_cliente = (ServicioCliente) utilitario.instanciarEJB(ServicioCliente.class);
    ////CXC
    @EJB
    private final ServicioFacturaCxC ser_factura = (ServicioFacturaCxC) utilitario.instanciarEJB(ServicioFacturaCxC.class);

    private AutoCompletar aut_cliente_cxc;
    private Calendario cal_fecha_pago_cxc;
    private AutoCompletar aut_cuenta_cxc;
    private Combo com_tip_tran_cxc;
    private Texto tex_num_cxc;
    private Texto ate_observacion_cxc;
    private Tabla tab_cuentas_x_cobrar;
    private Texto tex_diferencia_cxc;
    private Texto tex_valor_pagar_cxc;

    public CuentaCxc() {
        this.setWidth("85%");
        this.setHeight("90%");
        this.setTitle("CUENTAS POR COBRAR A CLIENTES");
        this.setResizable(false);
        this.setDynamic(false);

    }

    public Grid construir() {
        Grid contenido = new Grid();

        Grid gri1 = new Grid();
        gri1.setColumns(6);
        gri1.getChildren().add(new Etiqueta("<strong>CLIENTE :</strong>"));
        aut_cliente_cxc = new AutoCompletar();
        aut_cliente_cxc.setId("aut_cliente_cxc");
        aut_cliente_cxc.setRuta("pre_index.clase." + getId());
        aut_cliente_cxc.setMetodoChangeRuta("pre_index.clase." + getId() + ".cargarCuentasporCobrar");
        aut_cliente_cxc.setAutocompletarContenido();
        aut_cliente_cxc.setAutoCompletar(ser_cliente.getSqlComboClientes());

        gri1.getChildren().add(aut_cliente_cxc);
        gri1.getChildren().add(new Etiqueta("<strong>&nbsp;&nbsp;&nbsp;CUENTA DESTINO :</strong>"));
        aut_cuenta_cxc = new AutoCompletar();
        aut_cuenta_cxc.setId("aut_cuenta_cxc");
        aut_cuenta_cxc.setRuta("pre_index.clase." + getId());
        aut_cuenta_cxc.setMetodoChangeRuta("pre_index.clase." + getId() + ".cambioCuenta");
        aut_cuenta_cxc.setAutoCompletar(ser_tesoreria.getSqlComboCuentas());
        aut_cuenta_cxc.setDropdown(true);
        aut_cuenta_cxc.setAutocompletarContenido();
        gri1.getChildren().add(aut_cuenta_cxc);

        Grid gri2 = new Grid();
        gri2.setColumns(6);
        gri2.getChildren().add(new Etiqueta("<strong>TIPO DE TRANSACCIÓN :</strong>"));
        com_tip_tran_cxc = new Combo();
        com_tip_tran_cxc.setMetodoRuta("pre_index.clase." + getId() + ".cambioTipoTransBanco");
        com_tip_tran_cxc.setCombo(ser_tesoreria.getSqlTipoTransaccionPositivo());
        gri2.getChildren().add(com_tip_tran_cxc);
        gri2.getChildren().add(new Etiqueta("<strong>&nbsp;&nbsp;&nbsp;NUM. DOCUMENTO :</strong>"));
        tex_num_cxc = new Texto();
        tex_num_cxc.setId("tex_num_cxc");
        gri2.getChildren().add(tex_num_cxc);

        gri2.getChildren().add(new Etiqueta("<strong>&nbsp;&nbsp;&nbsp;FECHA :</strong>"));
        cal_fecha_pago_cxc = new Calendario();
        cal_fecha_pago_cxc.setFechaActual();
        gri2.getChildren().add(cal_fecha_pago_cxc);

        Grid gri3 = new Grid();
        gri3.setColumns(2);
        gri3.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN :</strong>"));
        ate_observacion_cxc = new Texto();
        ate_observacion_cxc.setStyle("width:" + (getAnchoPanel() - 140) + "px");
        gri3.getChildren().add(ate_observacion_cxc);

        contenido.getChildren().add(gri1);
        contenido.getChildren().add(gri2);
        contenido.getChildren().add(gri3);
        contenido.getChildren().add(new Separator());
        PanelGrid gri4 = new PanelGrid();
        gri4.setColumns(4);

        Etiqueta eti_valor_cobrar = new Etiqueta();
        Etiqueta eti_diferencia_cxc = new Etiqueta();
        eti_valor_cobrar.setValue("VALOR A COBRAR :");
        tex_diferencia_cxc = new Texto();
        tex_diferencia_cxc.setId("tex_diferencia_cxc");
        eti_diferencia_cxc.setStyle("font-size: 14px;font-weight: bold;padding-left:10px;");
        tex_diferencia_cxc.setStyle("font-size: 14px;font-weight: bold");
        tex_diferencia_cxc.setDisabled(true);
        tex_diferencia_cxc.setSoloNumeros();
        eti_diferencia_cxc.setValue("DIFERENCIA : ");
        tex_valor_pagar_cxc = new Texto();
        // tex_valor_pagar_cxc.setSoloNumeros();
        tex_valor_pagar_cxc.setId("tex_valor_pagar_cxc");
        tex_valor_pagar_cxc.setMetodoKeyPressRuta("pre_index.clase." + getId() + ".calcular_diferencia_cxc");
        tex_valor_pagar_cxc.setMetodoChangeRuta("pre_index.clase." + getId() + ".calcular_diferencia_cxc");
        eti_valor_cobrar.setStyle("font-size: 14px;font-weight: bold;");
        tex_valor_pagar_cxc.setStyle("font-size: 14px;font-weight: bold");
        gri4.getChildren().add(eti_valor_cobrar);
        gri4.getChildren().add(tex_valor_pagar_cxc);
        gri4.getChildren().add(eti_diferencia_cxc);
        gri4.getChildren().add(tex_diferencia_cxc);
        contenido.getChildren().add(gri4);
        contenido.getChildren().add(new Separator());

        tab_cuentas_x_cobrar = new Tabla();
        tab_cuentas_x_cobrar.setId("tab_cuentas_x_cobrar");
        tab_cuentas_x_cobrar.setRuta("pre_index.clase." + getId());
        tab_cuentas_x_cobrar.setSql(ser_cliente.getSqlCuentasPorCobrar(aut_cuenta_cxc.getValor()));
        tab_cuentas_x_cobrar.getColumna("saldo_x_pagar").setEstilo("font-size: 15px;font-weight: bold;");
        tab_cuentas_x_cobrar.getColumna("saldo_x_pagar").alinearDerecha();
        tab_cuentas_x_cobrar.getColumna("saldo_x_pagar").setLongitud(25);
        tab_cuentas_x_cobrar.getColumna("total_cccfa").setLongitud(25);
        tab_cuentas_x_cobrar.getColumna("total_cccfa").alinearDerecha();
        tab_cuentas_x_cobrar.getColumna("ide_cccfa").setVisible(false);
        tab_cuentas_x_cobrar.getColumna("secuencial_cccfa").setLongitud(25);

        tab_cuentas_x_cobrar.getColumna("fecha_emisi_cccfa").setNombreVisual("FECHA");
        tab_cuentas_x_cobrar.getColumna("secuencial_cccfa").setNombreVisual("NUM. FACTURA");
        tab_cuentas_x_cobrar.getColumna("total_cccfa").setNombreVisual("TOTAL");
        tab_cuentas_x_cobrar.getColumna("saldo_x_pagar").setNombreVisual("SALDO");
        tab_cuentas_x_cobrar.getColumna("observacion_cccfa").setNombreVisual("OBSERVACIÓN");

        tab_cuentas_x_cobrar.setScrollable(true);
        tab_cuentas_x_cobrar.setScrollHeight(getAltoPanel() - 275);
        tab_cuentas_x_cobrar.setCampoPrimaria("ide_ccctr");
        tab_cuentas_x_cobrar.setLectura(true);
        tab_cuentas_x_cobrar.setTipoSeleccion(true);
        tab_cuentas_x_cobrar.setCondicion("ide_ccctr=-1");
        tab_cuentas_x_cobrar.setColumnaSuma("saldo_x_pagar");

        tab_cuentas_x_cobrar.onSelectCheck("pre_index.clase." + getId() + ".seleccionaFacturaCxC");
        tab_cuentas_x_cobrar.onUnselectCheck("pre_index.clase." + getId() + ".deseleccionaFacturaCxC");
        tab_cuentas_x_cobrar.dibujar();

        contenido.getChildren().add(tab_cuentas_x_cobrar);

        contenido.setStyle("width:" + (getAnchoPanel() - 10) + "px; height:" + (getAltoPanel() - 5) + "px;overflow:auto;display:block;");
        this.getGri_cuerpo().getChildren().clear();
        this.setDialogo(contenido);
        this.getBot_aceptar().setMetodoRuta("pre_index.clase." + getId() + ".aceptar");
        return contenido;
    }

    /**
     * Carga las facturas por Cobrar cuando se selecciona un cliente del
     * autocompletar
     *
     * @param evt
     */
    public void cargarCuentasporCobrar(SelectEvent evt) {
        aut_cliente_cxc.onSelect(evt);
        tab_cuentas_x_cobrar.setSql(ser_cliente.getSqlCuentasPorCobrar(aut_cliente_cxc.getValor()));
        tab_cuentas_x_cobrar.ejecutarSql();
        tex_diferencia_cxc.setValue(utilitario.getFormatoNumero(0));
        tex_valor_pagar_cxc.setValue(utilitario.getFormatoNumero(0));
        if (tab_cuentas_x_cobrar.isEmpty()) {
            utilitario.agregarMensajeError("El cliente seleccionado no tiene cuentas por cobrar", "");
        }
    }

    public void deseleccionaFacturaCxC(UnselectEvent evt) {
        double total = 0;
        for (Fila actual : tab_cuentas_x_cobrar.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[5] + "") + total;
        }
        tex_valor_pagar_cxc.setValue(utilitario.getFormatoNumero(total));
        utilitario.addUpdate("tex_valor_pagar_cxc");
        calcular_diferencia_cxc();
    }

    public void seleccionaFacturaCxC(SelectEvent evt) {
        tab_cuentas_x_cobrar.seleccionarFila(evt);
        double total = 0;
        for (Fila actual : tab_cuentas_x_cobrar.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[5] + "") + total;
        }
        tex_valor_pagar_cxc.setValue(utilitario.getFormatoNumero(total));
        utilitario.addUpdate("tex_valor_pagar_cxc");
        calcular_diferencia_cxc();
    }

    public void calcular_diferencia_cxc() {
        double diferencia = 0;
        if (tex_valor_pagar_cxc.getValue() != null) {
            if (!tex_valor_pagar_cxc.getValue().toString().isEmpty()) {
                if (tab_cuentas_x_cobrar.getTotalFilas() > 0) {
                    try {
                        diferencia = tab_cuentas_x_cobrar.getSumaColumna("saldo_x_pagar") - Double.parseDouble(tex_valor_pagar_cxc.getValue().toString());
                    } catch (Exception e) {
                        tex_valor_pagar_cxc.setValue(utilitario.getFormatoNumero("0"));
                        utilitario.addUpdate("tex_valor_pagar_cxc");
                    }
                    tex_diferencia_cxc.setValue(utilitario.getFormatoNumero(diferencia));
                }
            }
        } else {
            if (tab_cuentas_x_cobrar.getTotalFilas() > 0) {
                diferencia = tab_cuentas_x_cobrar.getSumaColumna("saldo_x_pagar");
                tex_diferencia_cxc.setValue(utilitario.getFormatoNumero(diferencia));
            }
        }
        utilitario.addUpdate("tex_diferencia_cxc");
    }

    public void aceptar() {
        if (validar()) {
            cargarPagoCxC(Double.parseDouble(tex_valor_pagar_cxc.getValue().toString()));
        }
    }

    public void cambioCuenta(SelectEvent evt) {
        aut_cuenta_cxc.onSelect(evt);
        cambioTipoTransBanco();
    }

    public void cambioTipoTransBanco() {
//        CAMBIE
        if (com_tip_tran_cxc.getValue() != null) {
            if (aut_cuenta_cxc.getValor() != null) {
                tex_num_cxc.setValue(ser_tesoreria.getNumMaximoTipoTransaccion(aut_cuenta_cxc.getValor() + "", com_tip_tran_cxc.getValue() + ""));
            } else {
                aut_cuenta_cxc.limpiar();
                tex_num_cxc.limpiar();
            }
        }
        utilitario.addUpdate("tex_num_cxc,aut_cuenta_cxc");
    }

    private void cargarPagoCxC(double total_a_pagar) {

        List lis_fact_pagadas = new ArrayList();

        for (int i = 0; i < tab_cuentas_x_cobrar.getListaFilasSeleccionadas().size(); i++) {
            double monto_sobrante = 0;

            double valor_x_pagar = Double.parseDouble(utilitario.getFormatoNumero(tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[5]));
            if (valor_x_pagar > 0) {
                if (total_a_pagar >= valor_x_pagar) {
                    Object fila[] = {tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[1], tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getRowKey(), utilitario.getFormatoNumero(tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[5])};

                    lis_fact_pagadas.add(fila);

                    monto_sobrante = total_a_pagar - valor_x_pagar;

                    if (tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[1] != null) {
                        //ACTUALIZA LA FACTURA A PAGADA
                        utilitario.getConexion().agregarSqlPantalla(ser_factura.getSqlActualizaPagoFactura(String.valueOf(tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[1])));
                    }
                } else {
                    System.out.println(monto_sobrante + " ----- " + valor_x_pagar + "  VALOR A PAGAR  " + total_a_pagar);

                    monto_sobrante = total_a_pagar - valor_x_pagar;
                    if (monto_sobrante <= valor_x_pagar) {
                        System.out.println(monto_sobrante + "    *** BREAK  " + valor_x_pagar + "  VALOR A PAGAR  " + total_a_pagar);
                        Object fila[] = {tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[1], tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getRowKey(), utilitario.getFormatoNumero(total_a_pagar)};
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

        //ordenar lista de menor saldo a mayor saldo      
        for (Object lis_fact_pagada : lis_fact_pagadas) {
            Object[] obj_fila = (Object[]) lis_fact_pagada;
            System.out.println("ide_cccfa " + obj_fila[0] + " ide_ccctr " + obj_fila[1] + "*** valor " + obj_fila[2]);
            String ide_ccctr = String.valueOf(obj_fila[1]);
            //TRANSACCION EN TESORERIA y TRANSACCION CXC
            TablaGenerica tab_cabecera = utilitario.consultar(ser_factura.getSqlCabeceraFactura(String.valueOf(obj_fila[0])));
            String ide_teclb = ser_tesoreria.generarPagoFacturaCxC(tab_cabecera, aut_cuenta_cxc.getValor(), Double.parseDouble(String.valueOf(obj_fila[2])), String.valueOf(tex_num_cxc.getValue()), cal_fecha_pago_cxc.getFecha(), String.valueOf(com_tip_tran_cxc.getValue()));
            ser_factura.generarTransaccionPago(tab_cabecera, ide_ccctr, ide_teclb, Double.parseDouble(String.valueOf(obj_fila[2])), String.valueOf(ate_observacion_cxc.getValue()), String.valueOf(tex_num_cxc.getValue()));
        }
        
        utilitario.getConexion().setImprimirSqlConsola(true);
        utilitario.getConexion().guardarPantalla();

    }

    /**
     * Validaciones de la Transaccion CXC de Pago
     *
     * @return
     */
    private boolean validar() {

        if (com_tip_tran_cxc.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar el 'TIPO DE TRANSACCIÓN'", "");
            return false;
        }

        if (aut_cuenta_cxc.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar una 'CUENTA DESTINO' ", "");
            return false;
        }

        if (aut_cliente_cxc.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un 'CLIENTE' ", "");
            return false;
        }
        if (tab_cuentas_x_cobrar.isEmpty()) {
            utilitario.agregarMensajeInfo("El Cliente seleccionado no tiene Cuentas por Cobrar ", "");
            return false;
        }
        if (tab_cuentas_x_cobrar.getListaFilasSeleccionadas() == null || tab_cuentas_x_cobrar.getListaFilasSeleccionadas().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos una Factura", "");
            return false;
        }

        if (tex_valor_pagar_cxc.getValue() == null || tex_valor_pagar_cxc.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar el 'VALOR A PAGAR'", "");
            return false;
        } else {
            try {
                if (Double.parseDouble(tex_valor_pagar_cxc.getValue().toString()) <= 0) {
                    utilitario.agregarMensajeError("El 'VALOR A PAGAR' no es válido", "");
                    return false;
                }
            } catch (Exception e) {
                utilitario.agregarMensajeError("El 'VALOR A PAGAR' no es válido", "");
                return false;
            }
        }

        if (tex_diferencia_cxc.getValue() != null) {
            try {
                if (Double.parseDouble(tex_diferencia_cxc.getValue().toString()) < 0) {
                    utilitario.agregarMensajeError("El 'VALOR A PAGAR' es mayor al saldo total de la cuenta por cobrar", "");
                    return false;
                }
            } catch (Exception e) {
                utilitario.agregarMensajeError("El 'VALOR A PAGAR' no es válido", "");
                return false;
            }
        }

        //Valida que el monto a pagar ingresado sea superior al monto de las facturas seleccionadas
        if (tab_cuentas_x_cobrar.getListaFilasSeleccionadas().size() > 1) { //si se a seleccionado mas de una factura
            // double dou_suma_saldo_fact = 0;//SUMA EL SALDO DE LAS FACTURAS SELECCIONADAS
            double dou_saldo_menor = 0;  //ALMACENA EL VALOR DE MENOR PAGO
            for (int i = 0; i < tab_cuentas_x_cobrar.getListaFilasSeleccionadas().size(); i++) {
                double dou_saldo_actual = Double.parseDouble(String.valueOf(tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[5]));
                //      dou_suma_saldo_fact += dou_saldo_actual;
                if (i == 0) {
                    dou_saldo_menor = dou_saldo_actual;
                }

                if (dou_saldo_actual < dou_saldo_menor) {
                    dou_saldo_menor = dou_saldo_actual;
                }

            }
            if ((Double.parseDouble(tex_valor_pagar_cxc.getValue().toString())) < dou_saldo_menor) {
                utilitario.agregarMensajeError("El 'VALOR A PAGAR' es menor que el saldo de las Facturas Seleccionadas, el valor mínimo a pagar es: " + utilitario.getFormatoNumero(dou_saldo_menor), "");
                return false;
            }
        }

        return true;
    }

    @Override
    public void dibujar() {
        construir();
        super.dibujar();
    }

    public void limpiar() {
        aut_cliente_cxc.limpiar();
        aut_cuenta_cxc.limpiar();
        cal_fecha_pago_cxc.limpiar();
        com_tip_tran_cxc.setValue(null);
        tex_num_cxc.limpiar();
        ate_observacion_cxc.limpiar();
    }

    public AutoCompletar getAut_cliente_cxc() {
        return aut_cliente_cxc;
    }

    public void setAut_cliente_cxc(AutoCompletar aut_cliente_cxc) {
        this.aut_cliente_cxc = aut_cliente_cxc;
    }

    public AutoCompletar getAut_cuenta_cxc() {
        return aut_cuenta_cxc;
    }

    public void setAut_cuenta_cxc(AutoCompletar aut_cuenta_cxc) {
        this.aut_cuenta_cxc = aut_cuenta_cxc;
    }

    public Tabla getTab_cuentas_x_cobrar() {
        return tab_cuentas_x_cobrar;
    }

    public void setTab_cuentas_x_cobrar(Tabla tab_cuentas_x_cobrar) {
        this.tab_cuentas_x_cobrar = tab_cuentas_x_cobrar;
    }

}

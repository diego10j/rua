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
import servicios.cuentas_x_pagar.ServicioCuentasCxP;
import servicios.cuentas_x_pagar.ServicioProveedor;
import servicios.tesoreria.ServicioTesoreria;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
public class CuentaCxP extends Dialogo {

    private final Utilitario utilitario = new Utilitario();

    /////TESORERIA
    @EJB
    private final ServicioTesoreria ser_tesoreria1 = (ServicioTesoreria) utilitario.instanciarEJB(ServicioTesoreria.class);
    ////CLIENTES
    @EJB
    private final ServicioProveedor ser_proveedor = (ServicioProveedor) utilitario.instanciarEJB(ServicioProveedor.class);
    ////CXC
    @EJB
    private final ServicioCuentasCxP ser_cuentas_cxp = (ServicioCuentasCxP) utilitario.instanciarEJB(ServicioCuentasCxP.class);

    private AutoCompletar aut_cliente_cxc;
    private Calendario cal_fecha_pago_cxc;
    private AutoCompletar aut_cuenta_cxc;
    private Combo com_tip_tran_cxc;
    private Texto tex_num_cxc;
    private Texto ate_observacion_cxc;
    private Tabla tab_cuentas_x_cobrar;
    private Texto tex_diferencia_cxc;
    private Texto tex_valor_pagar_cxc;

    public CuentaCxP() {
        this.setWidth("85%");
        this.setHeight("90%");
        this.setTitle("CUENTAS POR PAGAR A PROVEDORES");
        this.setResizable(false);
        this.setDynamic(false);

    }

    public Grid construir() {
        Grid contenido = new Grid();

        Grid gri1 = new Grid();
        gri1.setColumns(6);
        gri1.getChildren().add(new Etiqueta("<strong>PROVEEDOR : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        aut_cliente_cxc = new AutoCompletar();
        aut_cliente_cxc.setId("aut_cliente_cxc");
        aut_cliente_cxc.setRuta("pre_index.clase." + getId());
        aut_cliente_cxc.setMetodoChangeRuta("pre_index.clase." + getId() + ".cargarCuentasporCobrar");
        aut_cliente_cxc.setAutocompletarContenido();
        aut_cliente_cxc.setAutoCompletar(ser_proveedor.getSqlComboProveedor());
        aut_cliente_cxc.setSize(70);
        gri1.getChildren().add(aut_cliente_cxc);
        gri1.getChildren().add(new Etiqueta("<strong>&nbsp;&nbsp;&nbsp;CUENTA ORIGEN : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        aut_cuenta_cxc = new AutoCompletar();
        aut_cuenta_cxc.setId("aut_cuenta_cxc");
        aut_cuenta_cxc.setRuta("pre_index.clase." + getId());
        aut_cuenta_cxc.setMetodoChangeRuta("pre_index.clase." + getId() + ".cambioCuenta");
        aut_cuenta_cxc.setAutoCompletar(ser_tesoreria1.getSqlComboCuentas());
        aut_cuenta_cxc.setDropdown(true);
        aut_cuenta_cxc.setAutocompletarContenido();
        gri1.getChildren().add(aut_cuenta_cxc);

        Grid gri2 = new Grid();
        gri2.setColumns(6);
        gri2.getChildren().add(new Etiqueta("<strong>TIPO DE TRANSACCIÓN : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        com_tip_tran_cxc = new Combo();
        com_tip_tran_cxc.setMetodoRuta("pre_index.clase." + getId() + ".cambioTipoTransBanco");
        com_tip_tran_cxc.setCombo(ser_tesoreria1.getSqlTipoTransaccionNegativo());
        gri2.getChildren().add(com_tip_tran_cxc);
        gri2.getChildren().add(new Etiqueta("<strong>&nbsp;&nbsp;&nbsp;NUM. DOCUMENTO : </strong>"));
        tex_num_cxc = new Texto();
        tex_num_cxc.setId("tex_num_cxc");
        gri2.getChildren().add(tex_num_cxc);

        gri2.getChildren().add(new Etiqueta("<strong>&nbsp;&nbsp;&nbsp;FECHA : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        cal_fecha_pago_cxc = new Calendario();
        cal_fecha_pago_cxc.setFechaActual();
        gri2.getChildren().add(cal_fecha_pago_cxc);

        Grid gri3 = new Grid();
        gri3.setColumns(2);
        gri3.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN : </strong><span style='color:red;font-weight: bold;'>*</span>"));
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
        eti_valor_cobrar.setValue("VALOR A PAGAR :");
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
        tab_cuentas_x_cobrar.setSql(ser_proveedor.getSqlCuentasPorPagar(aut_cuenta_cxc.getValor()));
        tab_cuentas_x_cobrar.getColumna("saldo_x_pagar").setEstilo("font-size: 15px;font-weight: bold;");
        tab_cuentas_x_cobrar.getColumna("saldo_x_pagar").alinearDerecha();
        tab_cuentas_x_cobrar.getColumna("saldo_x_pagar").setLongitud(25);
        tab_cuentas_x_cobrar.getColumna("total_cpcfa").setLongitud(25);
        tab_cuentas_x_cobrar.getColumna("total_cpcfa").alinearDerecha();
        tab_cuentas_x_cobrar.getColumna("ide_cpcfa").setVisible(false);
        tab_cuentas_x_cobrar.getColumna("numero_cpcfa").setLongitud(25);

        tab_cuentas_x_cobrar.getColumna("fecha_emisi_cpcfa").setNombreVisual("FECHA");
        tab_cuentas_x_cobrar.getColumna("numero_cpcfa").setNombreVisual("NUM. FACTURA");
        tab_cuentas_x_cobrar.getColumna("total_cpcfa").setNombreVisual("TOTAL");
        tab_cuentas_x_cobrar.getColumna("saldo_x_pagar").setNombreVisual("SALDO");
        tab_cuentas_x_cobrar.getColumna("observacion_cpcfa").setNombreVisual("OBSERVACIÓN");

        tab_cuentas_x_cobrar.setScrollable(true);
        tab_cuentas_x_cobrar.setScrollHeight(getAltoPanel() - 275);
        tab_cuentas_x_cobrar.setCampoPrimaria("ide_cpctr");
        tab_cuentas_x_cobrar.setLectura(true);
        tab_cuentas_x_cobrar.setTipoSeleccion(true);
        tab_cuentas_x_cobrar.setCondicion("ide_cpctr=-1");
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
        tab_cuentas_x_cobrar.setSql(ser_proveedor.getSqlCuentasPorPagar(aut_cliente_cxc.getValor()));
        tab_cuentas_x_cobrar.ejecutarSql();
        tex_diferencia_cxc.setValue(utilitario.getFormatoNumero(0));
        tex_valor_pagar_cxc.setValue(utilitario.getFormatoNumero(0));
        if (tab_cuentas_x_cobrar.isEmpty()) {
            utilitario.agregarMensajeError("El Proveedor seleccionado no tiene cuentas por pagar", "");
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
        if (validarDialogoCxP()) {
            cargarPagoCxP(Double.parseDouble(tex_valor_pagar_cxc.getValue().toString()));
            this.cerrar();
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
                tex_num_cxc.setValue(ser_tesoreria1.getNumMaximoTipoTransaccion(aut_cuenta_cxc.getValor() + "", com_tip_tran_cxc.getValue() + ""));
            } else {
                aut_cuenta_cxc.limpiar();
                tex_num_cxc.limpiar();
            }
        }
        utilitario.addUpdate("tex_num_cxc,aut_cuenta_cxc");
    }

    public void cargarPagoCxP(double total_a_pagar) {
        List lis_fact_pagadas = new ArrayList();
        String str_num_doc_factura_cxp = "";
        for (int i = 0; i < tab_cuentas_x_cobrar.getListaFilasSeleccionadas().size(); i++) {
            double monto_sobrante = 0;
            str_num_doc_factura_cxp = tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[3] + "";

            double valor_x_pagar = Double.parseDouble(tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[5] + "");
            if (valor_x_pagar > 0) {
                if (total_a_pagar >= valor_x_pagar) {
                    Object fila[] = {tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[1], tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getRowKey(), tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[5]};
                    lis_fact_pagadas.add(fila);
                    monto_sobrante = total_a_pagar - valor_x_pagar;
                    if (tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[1] != null) {
                        //ACTUALIZA LA FACTURA A PAGADA
                        utilitario.getConexion().agregarSqlPantalla(ser_cuentas_cxp.getSqlActualizaPagoDocumento(String.valueOf(tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[1])));
                    }
                } else {
                    monto_sobrante = total_a_pagar - valor_x_pagar;
                    if (monto_sobrante <= valor_x_pagar) {
                        System.out.println(monto_sobrante + "    *** BREAK  " + valor_x_pagar + "  VALOR A PAGAR  " + total_a_pagar);
                        Object fila[] = {tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[1], tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getRowKey(), utilitario.getFormatoNumero(total_a_pagar)};
                        lis_fact_pagadas.add(fila);
                        break;
                    }
                }
                total_a_pagar = monto_sobrante;
            } else {
                break;
            }
        }
        System.out.println("... " + utilitario.getConexion().getSqlPantalla().size());
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

        for (int i = 0; i < lis_fact_pagadas.size(); i++) {

            Object obj_fila[] = (Object[]) lis_fact_pagadas.get(i);
            System.out.println("ide_cpcfa " + obj_fila[0] + " ide_cpctr " + obj_fila[1] + " valor " + obj_fila[2]);

            String ide_ccctr = String.valueOf(obj_fila[1]);
            //TRANSACCION EN TESORERIA y TRANSACCION CXP
            TablaGenerica tab_cabecera = utilitario.consultar(ser_cuentas_cxp.getSqlCabeceraDocumento(String.valueOf(obj_fila[0])));

            String ide_teclb = ser_tesoreria1.generarPagoFacturaCxP(tab_cabecera, aut_cuenta_cxc.getValor(), Double.parseDouble(String.valueOf(obj_fila[2])), String.valueOf(tex_num_cxc.getValue()), cal_fecha_pago_cxc.getFecha(), String.valueOf(com_tip_tran_cxc.getValue()));
            ser_cuentas_cxp.generarTransaccionPago(tab_cabecera, ide_ccctr, ide_teclb, Double.parseDouble(String.valueOf(obj_fila[2])), String.valueOf(ate_observacion_cxc.getValue()), String.valueOf(tex_num_cxc.getValue()));
        }
        // utilitario.getConexion().setImprimirSqlConsola(true);
        utilitario.getConexion().guardarPantalla();
    }

    /**
     * Validaciones de la Transaccion CXC de Pago
     *
     * @return
     */
    public boolean validarDialogoCxP() {
        if (com_tip_tran_cxc.getValue() != null) {
            //realizo_pago_sin_bancos_cxp = false;
            if (aut_cuenta_cxc.getValor() == null) {
                utilitario.agregarMensajeError("Atencion ", "No ha seleccionado un Banco");
                return false;
            }
        }
//        else {
//            realizo_pago_sin_bancos_cxp = true;
//        }

        if (aut_cliente_cxc.getValue() == null) {
            utilitario.agregarMensajeError("Atencion ", "No ha seleccionado un Proveedor");
            return false;
        }

        if (tab_cuentas_x_cobrar.getTotalFilas() == 0) {
            utilitario.agregarMensajeError("Atencion ", "No tiene deuda con el Proveedor seleccionado");
            return false;
        }

        if (tab_cuentas_x_cobrar.getListaFilasSeleccionadas().isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede realizar el pago", "Debe seleccionar al menos una factura");
            return false;
        }

        if (tex_valor_pagar_cxc.getValue() == null || tex_valor_pagar_cxc.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Pago a Proveedores", "Atencion no ha ingresado el monto a cancelar");
            return false;
        }

        if (tex_diferencia_cxc.getValue() == null || tex_diferencia_cxc.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Pago a Proveedores", "Atencion no realizo ningun pago");
            return false;
        } else {
            if (Double.parseDouble(tex_diferencia_cxc.getValue() + "") < 0) {
                utilitario.agregarMensajeInfo("Pago a Proveedores", "Atencion  no puede pagar mas de lo que debe");
                return false;
            }
        }

        double total = 0;
        for (Fila actual : tab_cuentas_x_cobrar.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[5] + "") + total;
        }
        total = Double.parseDouble(utilitario.getFormatoNumero(total));
        double valor_a_pagar = Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(tex_valor_pagar_cxc.getValue() + "")));
        System.out.println("total " + total + " valor a pagar " + valor_a_pagar);
        if (total < valor_a_pagar) {
            utilitario.agregarMensajeInfo("No se puede cancelar", "El valor a cancelar es mayor que el valor de la deuda");
            return false;
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

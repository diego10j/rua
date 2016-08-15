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
    ////CXP
    @EJB
    private final ServicioCuentasCxP ser_cuentas_cxp = (ServicioCuentasCxP) utilitario.instanciarEJB(ServicioCuentasCxP.class);

    private AutoCompletar aut_provee_cxp;
    private Calendario cal_fecha_pago_cxp;
    private AutoCompletar aut_cuenta_cxp;
    private Combo com_tip_tran_cxp;
    private Texto tex_num_cxp;
    private Tabla tab_cuentas_x_pagar;
    private Texto tex_diferencia_cxp;
    private Texto tex_valor_pagar_cxp;

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
        aut_provee_cxp = new AutoCompletar();
        aut_provee_cxp.setId("aut_provee_cxp");
        aut_provee_cxp.setRuta("pre_index.clase." + getId());
        aut_provee_cxp.setMetodoChangeRuta("pre_index.clase." + getId() + ".cargarCuentasporPagar");
        aut_provee_cxp.setAutocompletarContenido();
        aut_provee_cxp.setAutoCompletar(ser_proveedor.getSqlComboProveedor());
        aut_provee_cxp.setSize(70);
        gri1.getChildren().add(aut_provee_cxp);
        gri1.getChildren().add(new Etiqueta("<strong>&nbsp;&nbsp;&nbsp;FECHA : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        cal_fecha_pago_cxp = new Calendario();
        cal_fecha_pago_cxp.setFechaActual();
        gri1.getChildren().add(cal_fecha_pago_cxp);
        Grid gri2 = new Grid();
        gri2.getChildren().add(new Etiqueta("<strong>DE LA CUENTA : </strong> <span style='color:red;font-weight: bold;'>*</span>"));
        aut_cuenta_cxp = new AutoCompletar();
        aut_cuenta_cxp.setId("aut_cuenta_cxp");
        aut_cuenta_cxp.setRuta("pre_index.clase." + getId());
        aut_cuenta_cxp.setMetodoChangeRuta("pre_index.clase." + getId() + ".cambioCuenta");
        aut_cuenta_cxp.setAutoCompletar(ser_tesoreria1.getSqlComboCuentas());
        aut_cuenta_cxp.setDropdown(true);
        aut_cuenta_cxp.setAutocompletarContenido();
        gri2.getChildren().add(aut_cuenta_cxp);
        gri2.setColumns(6);
        gri2.getChildren().add(new Etiqueta("<strong>TIPO DE TRANSACCIÓN : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        com_tip_tran_cxp = new Combo();
        com_tip_tran_cxp.setMetodoRuta("pre_index.clase." + getId() + ".cambioTipoTransBanco");
        com_tip_tran_cxp.setCombo(ser_tesoreria1.getSqlTipoTransaccionNegativo());
        gri2.getChildren().add(com_tip_tran_cxp);
        gri2.getChildren().add(new Etiqueta("<strong>&nbsp;&nbsp;&nbsp;NUM. DOCUMENTO : </strong>"));
        tex_num_cxp = new Texto();
        tex_num_cxp.setId("tex_num_cxp");
        gri2.getChildren().add(tex_num_cxp);
        contenido.getChildren().add(gri1);
        contenido.getChildren().add(gri2);
        contenido.getChildren().add(new Separator());
        PanelGrid gri4 = new PanelGrid();
        gri4.setColumns(4);
        Etiqueta eti_valor_cobrar = new Etiqueta();
        Etiqueta eti_diferencia_cxc = new Etiqueta();
        eti_valor_cobrar.setValue("VALOR A PAGAR :");
        tex_diferencia_cxp = new Texto();
        tex_diferencia_cxp.setId("tex_diferencia_cxp");
        eti_diferencia_cxc.setStyle("font-size: 14px;font-weight: bold;padding-left:10px;");
        tex_diferencia_cxp.setStyle("font-size: 14px;font-weight: bold");
        tex_diferencia_cxp.setDisabled(true);
        tex_diferencia_cxp.setSoloNumeros();
        eti_diferencia_cxc.setValue("DIFERENCIA : ");
        tex_valor_pagar_cxp = new Texto();
        // tex_valor_pagar_cxp.setSoloNumeros();
        tex_valor_pagar_cxp.setId("tex_valor_pagar_cxp");
        tex_valor_pagar_cxp.setMetodoKeyPressRuta("pre_index.clase." + getId() + ".calcularDiferenciaCxP");
        tex_valor_pagar_cxp.setMetodoChangeRuta("pre_index.clase." + getId() + ".calcularDiferenciaCxP");
        eti_valor_cobrar.setStyle("font-size: 14px;font-weight: bold;");
        tex_valor_pagar_cxp.setStyle("font-size: 14px;font-weight: bold");
        gri4.getChildren().add(eti_valor_cobrar);
        gri4.getChildren().add(tex_valor_pagar_cxp);
        gri4.getChildren().add(eti_diferencia_cxc);
        gri4.getChildren().add(tex_diferencia_cxp);
        contenido.getChildren().add(gri4);
        contenido.getChildren().add(new Separator());
        tab_cuentas_x_pagar = new Tabla();
        tab_cuentas_x_pagar.setId("tab_cuentas_x_pagar");
        tab_cuentas_x_pagar.setRuta("pre_index.clase." + getId());
        tab_cuentas_x_pagar.setSql(ser_proveedor.getSqlCuentasPorPagar(aut_cuenta_cxp.getValor()));
        tab_cuentas_x_pagar.getColumna("saldo_x_pagar").setEstilo("font-size: 15px;font-weight: bold;");
        tab_cuentas_x_pagar.getColumna("saldo_x_pagar").alinearDerecha();
        tab_cuentas_x_pagar.getColumna("saldo_x_pagar").setLongitud(25);
        tab_cuentas_x_pagar.getColumna("total_cpcfa").setLongitud(25);
        tab_cuentas_x_pagar.getColumna("total_cpcfa").alinearDerecha();
        tab_cuentas_x_pagar.getColumna("ide_cpcfa").setVisible(false);
        tab_cuentas_x_pagar.getColumna("numero_cpcfa").setLongitud(25);
        tab_cuentas_x_pagar.getColumna("fecha_emisi_cpcfa").setNombreVisual("FECHA");
        tab_cuentas_x_pagar.getColumna("numero_cpcfa").setNombreVisual("NUM. FACTURA");
        tab_cuentas_x_pagar.getColumna("total_cpcfa").setNombreVisual("TOTAL");
        tab_cuentas_x_pagar.getColumna("saldo_x_pagar").setNombreVisual("SALDO");
        tab_cuentas_x_pagar.getColumna("observacion_cpcfa").setNombreVisual("OBSERVACIÓN");
        tab_cuentas_x_pagar.setScrollable(true);
        tab_cuentas_x_pagar.setScrollHeight(getAltoPanel() - 225);
        tab_cuentas_x_pagar.setCampoPrimaria("ide_cpctr");
        tab_cuentas_x_pagar.setLectura(true);
        tab_cuentas_x_pagar.setTipoSeleccion(true);
        tab_cuentas_x_pagar.setCondicion("ide_cpctr=-1");
        tab_cuentas_x_pagar.setColumnaSuma("saldo_x_pagar");
        tab_cuentas_x_pagar.onSelectCheck("pre_index.clase." + getId() + ".seleccionaFacturaCxP");
        tab_cuentas_x_pagar.onUnselectCheck("pre_index.clase." + getId() + ".deseleccionaFacturaCxP");
        tab_cuentas_x_pagar.dibujar();
        contenido.getChildren().add(tab_cuentas_x_pagar);
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
    public void cargarCuentasporPagar(SelectEvent evt) {
        aut_provee_cxp.onSelect(evt);
        tab_cuentas_x_pagar.setSql(ser_proveedor.getSqlCuentasPorPagar(aut_provee_cxp.getValor()));
        tab_cuentas_x_pagar.ejecutarSql();
        tex_diferencia_cxp.setValue(utilitario.getFormatoNumero(0));
        tex_valor_pagar_cxp.setValue(utilitario.getFormatoNumero(0));
        if (tab_cuentas_x_pagar.isEmpty()) {
            utilitario.agregarMensajeError("El Proveedor seleccionado no tiene cuentas por pagar", "");
        }
    }

    public void deseleccionaFacturaCxP(UnselectEvent evt) {
        double total = 0;
        for (Fila actual : tab_cuentas_x_pagar.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[5] + "") + total;
        }
        tex_valor_pagar_cxp.setValue(utilitario.getFormatoNumero(total));
        utilitario.addUpdate("tex_valor_pagar_cxp");
        calcularDiferenciaCxP();
    }

    public void seleccionaFacturaCxP(SelectEvent evt) {
        tab_cuentas_x_pagar.seleccionarFila(evt);
        double total = 0;
        for (Fila actual : tab_cuentas_x_pagar.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[5] + "") + total;
        }
        tex_valor_pagar_cxp.setValue(utilitario.getFormatoNumero(total));
        utilitario.addUpdate("tex_valor_pagar_cxp");
        calcularDiferenciaCxP();
    }

    public void calcularDiferenciaCxP() {
        double diferencia = 0;
        if (tex_valor_pagar_cxp.getValue() != null) {
            if (!tex_valor_pagar_cxp.getValue().toString().isEmpty()) {
                if (tab_cuentas_x_pagar.getTotalFilas() > 0) {
                    try {
                        diferencia = tab_cuentas_x_pagar.getSumaColumna("saldo_x_pagar") - Double.parseDouble(tex_valor_pagar_cxp.getValue().toString());
                    } catch (Exception e) {
                        tex_valor_pagar_cxp.setValue(utilitario.getFormatoNumero("0"));
                        utilitario.addUpdate("tex_valor_pagar_cxp");
                    }
                    tex_diferencia_cxp.setValue(utilitario.getFormatoNumero(diferencia));
                }
            }
        } else {
            if (tab_cuentas_x_pagar.getTotalFilas() > 0) {
                diferencia = tab_cuentas_x_pagar.getSumaColumna("saldo_x_pagar");
                tex_diferencia_cxp.setValue(utilitario.getFormatoNumero(diferencia));
            }
        }
        utilitario.addUpdate("tex_diferencia_cxp");
    }

    public void aceptar() {
        if (validarDialogoCxP()) {
            cargarPagoCxP(Double.parseDouble(tex_valor_pagar_cxp.getValue().toString()));
            this.cerrar();
        }
    }

    public void cambioCuenta(SelectEvent evt) {
        aut_cuenta_cxp.onSelect(evt);
        cambioTipoTransBanco();
    }

    public void cambioTipoTransBanco() {
        if (com_tip_tran_cxp.getValue() != null) {
            if (aut_cuenta_cxp.getValor() != null) {
                tex_num_cxp.setValue(ser_tesoreria1.getNumMaximoTipoTransaccion(aut_cuenta_cxp.getValor() + "", com_tip_tran_cxp.getValue() + ""));
            } else {
                aut_cuenta_cxp.limpiar();
                tex_num_cxp.limpiar();
            }
        }
        utilitario.addUpdate("tex_num_cxp,aut_cuenta_cxp");
    }

    public void cargarPagoCxP(double total_a_pagar) {
        List lis_fact_pagadas = new ArrayList();
        for (int i = 0; i < tab_cuentas_x_pagar.getListaFilasSeleccionadas().size(); i++) {
            double monto_sobrante = 0;
            double valor_x_pagar = Double.parseDouble(tab_cuentas_x_pagar.getListaFilasSeleccionadas().get(i).getCampos()[5] + "");
            if (valor_x_pagar > 0) {
                if (total_a_pagar >= valor_x_pagar) {
                    Object fila[] = {tab_cuentas_x_pagar.getListaFilasSeleccionadas().get(i).getCampos()[1], tab_cuentas_x_pagar.getListaFilasSeleccionadas().get(i).getRowKey(), tab_cuentas_x_pagar.getListaFilasSeleccionadas().get(i).getCampos()[5]};
                    lis_fact_pagadas.add(fila);
                    monto_sobrante = total_a_pagar - valor_x_pagar;
                    if (tab_cuentas_x_pagar.getListaFilasSeleccionadas().get(i).getCampos()[1] != null) {
                        //ACTUALIZA LA FACTURA A PAGADA
                        utilitario.getConexion().agregarSqlPantalla(ser_cuentas_cxp.getSqlActualizaPagoDocumento(String.valueOf(tab_cuentas_x_pagar.getListaFilasSeleccionadas().get(i).getCampos()[1])));
                    }
                } else {
                    monto_sobrante = total_a_pagar - valor_x_pagar;
                    if (monto_sobrante <= valor_x_pagar) {
                        System.out.println(monto_sobrante + "    *** BREAK  " + valor_x_pagar + "  VALOR A PAGAR  " + total_a_pagar);
                        Object fila[] = {tab_cuentas_x_pagar.getListaFilasSeleccionadas().get(i).getCampos()[1], tab_cuentas_x_pagar.getListaFilasSeleccionadas().get(i).getRowKey(), utilitario.getFormatoNumero(total_a_pagar)};
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
        for (Object lis_fact_pagada : lis_fact_pagadas) {
            Object[] obj_fila = (Object[]) lis_fact_pagada;
            System.out.println("ide_cpcfa " + obj_fila[0] + " ide_cpctr " + obj_fila[1] + " valor " + obj_fila[2]);
            String ide_ccctr = String.valueOf(obj_fila[1]);
            //TRANSACCION EN TESORERIA y TRANSACCION CXP
            TablaGenerica tab_cabecera = utilitario.consultar(ser_cuentas_cxp.getSqlCabeceraDocumento(String.valueOf(obj_fila[0])));
            String str_observacion = "V/. PAGO " + ser_cuentas_cxp.getNombreTipoDocumento(tab_cabecera.getValor("ide_cntdo")) + " N." + tab_cabecera.getValor("numero_cpcfa");
            String ide_teclb = ser_tesoreria1.generarPagoFacturaCxP(tab_cabecera, aut_cuenta_cxp.getValor(), Double.parseDouble(String.valueOf(obj_fila[2])), String.valueOf(tex_num_cxp.getValue()), cal_fecha_pago_cxp.getFecha(), String.valueOf(com_tip_tran_cxp.getValue()), str_observacion);
            ser_cuentas_cxp.generarTransaccionPago(tab_cabecera, ide_ccctr, ide_teclb, Double.parseDouble(String.valueOf(obj_fila[2])), str_observacion, String.valueOf(tex_num_cxp.getValue()));
        }
        // utilitario.getConexion().setImprimirSqlConsola(true);
        utilitario.getConexion().guardarPantalla();
    }

    /**
     * Validaciones de la Transaccion CXP de Pago
     *
     * @return
     */
    public boolean validarDialogoCxP() {
        if (com_tip_tran_cxp.getValue() != null) {
            //realizo_pago_sin_bancos_cxp = false;
            if (aut_cuenta_cxp.getValor() == null) {
                utilitario.agregarMensajeInfo("Debe seleccionar una 'CUENTA' ", "");
                return false;
            }
        }

        if (aut_provee_cxp.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un 'PROVEEDOR' ", "");
            return false;
        }

        if (tab_cuentas_x_pagar.isEmpty()) {
            utilitario.agregarMensajeInfo("El Proveedor seleccionado no tiene Cuentas por Pagar ", "");
            return false;
        }

        if (tab_cuentas_x_pagar.getListaFilasSeleccionadas().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un Documento por Pagar", "");
            return false;
        }
        if (tex_valor_pagar_cxp.getValue() == null || tex_valor_pagar_cxp.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar el 'VALOR A PAGAR'", "");
            return false;
        } else {
            try {
                if (Double.parseDouble(tex_valor_pagar_cxp.getValue().toString()) <= 0) {
                    utilitario.agregarMensajeError("El 'VALOR A PAGAR' no es válido", "");
                    return false;
                }
            } catch (Exception e) {
                utilitario.agregarMensajeError("El 'VALOR A PAGAR' no es válido", "");
                return false;
            }
        }

        if (tex_diferencia_cxp.getValue() != null) {
            try {
                if (Double.parseDouble(tex_diferencia_cxp.getValue().toString()) < 0) {
                    utilitario.agregarMensajeError("El 'VALOR A PAGAR' es mayor al saldo total de la cuenta por cobrar", "");
                    return false;
                }
            } catch (Exception e) {
                utilitario.agregarMensajeError("El 'VALOR A PAGAR' no es válido", "");
                return false;
            }
        }

        double total = 0;
        for (Fila actual : tab_cuentas_x_pagar.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[5] + "") + total;
        }
        total = Double.parseDouble(utilitario.getFormatoNumero(total));
        double valor_a_pagar = Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(tex_valor_pagar_cxp.getValue() + "")));
        System.out.println("total " + total + " valor a pagar " + valor_a_pagar);
        if (total < valor_a_pagar) {
            utilitario.agregarMensajeError("El 'VALOR A PAGAR' es menor que el saldo de las Facturas Seleccionadas", "");
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
        aut_provee_cxp.limpiar();
        aut_cuenta_cxp.limpiar();
        cal_fecha_pago_cxp.limpiar();
        com_tip_tran_cxp.setValue(null);
        tex_num_cxp.limpiar();
    }

    public AutoCompletar getAut_provee_cxp() {
        return aut_provee_cxp;
    }

    public void setAut_provee_cxp(AutoCompletar aut_provee_cxp) {
        this.aut_provee_cxp = aut_provee_cxp;
    }

    public AutoCompletar getAut_cuenta_cxp() {
        return aut_cuenta_cxp;
    }

    public void setAut_cuenta_cxp(AutoCompletar aut_cuenta_cxp) {
        this.aut_cuenta_cxp = aut_cuenta_cxp;
    }

    public Tabla getTab_cuentas_x_pagar() {
        return tab_cuentas_x_pagar;
    }

    public void setTab_cuentas_x_pagar(Tabla tab_cuentas_x_pagar) {
        this.tab_cuentas_x_pagar = tab_cuentas_x_pagar;
    }
}

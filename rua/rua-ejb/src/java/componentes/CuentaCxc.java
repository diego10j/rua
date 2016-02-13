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
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import servicios.cuentas_x_cobrar.ServicioCliente;
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
    
    private AutoCompletar aut_cliente_cxc;
    private Calendario cal_fecha_pago_cxc;
    private AutoCompletar aut_cuenta_cxc = new AutoCompletar();
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
        aut_cliente_cxc.setAutoCompletar(ser_cliente.getSqlComboClientes());
        aut_cliente_cxc.setMetodoChangeRuta("pre_index.clase." + getId() + ".cargarCuentasporCobrar");
        gri1.getChildren().add(aut_cliente_cxc);
        gri1.getChildren().add(new Etiqueta("<strong>&nbsp;&nbsp;&nbsp;CUENTA DESTINO :</strong>"));
        aut_cuenta_cxc.setId("aut_cuenta_cxc");
        aut_cuenta_cxc.setRuta("pre_index.clase." + getId());
        aut_cuenta_cxc.setAutoCompletar(ser_tesoreria.getSqlComboCuentas());
        aut_cuenta_cxc.setDropdown(true);
        gri1.getChildren().add(aut_cuenta_cxc);
        
        Grid gri2 = new Grid();
        gri2.setColumns(6);
        gri2.getChildren().add(new Etiqueta("<strong>TIPO DE TRANSACCIÓN :</strong>"));
        com_tip_tran_cxc = new Combo();
        com_tip_tran_cxc.setMetodo("cambioTipoTransBancCxC");
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
        Grid gri4 = new Grid();
        gri4.setColumns(4);
        
        Etiqueta eti_valor_cobrar = new Etiqueta();
        Etiqueta eti_diferencia_cxc = new Etiqueta();
        eti_valor_cobrar.setValue("VALOR A COBRAR :");
        tex_diferencia_cxc = new Texto();
        tex_diferencia_cxc.setId("tex_diferencia_cxc");
        eti_diferencia_cxc.setStyle("font-size: 14px;font-weight: bold;text-decoration: underline;padding-left:10px;");
        tex_diferencia_cxc.setStyle("font-size: 14px;font-weight: bold");
        tex_diferencia_cxc.setDisabled(true);
        tex_diferencia_cxc.setSoloNumeros();
        eti_diferencia_cxc.setValue("DIFERENCIA : ");
        tex_valor_pagar_cxc = new Texto();
        // tex_valor_pagar_cxc.setSoloNumeros();
        tex_valor_pagar_cxc.setId("tex_valor_pagar_cxc");
        tex_valor_pagar_cxc.setMetodoKeyPressRuta("pre_index.clase." + getId() + ".calcular_diferencia_cxc");
        tex_valor_pagar_cxc.setMetodoChangeRuta("pre_index.clase." + getId() + ".calcular_diferencia_cxc");
        eti_valor_cobrar.setStyle("font-size: 14px;font-weight: bold;text-decoration: underline");
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
    
    private List lis_fact_pagadas = new ArrayList();
    
    private void cargarPagoCxC(double total_a_pagar) {
        lis_fact_pagadas.clear();
        for (int i = 0; i < tab_cuentas_x_cobrar.getListaFilasSeleccionadas().size(); i++) {
            double monto_sobrante = 0;
             
            double valor_x_pagar = Double.parseDouble(tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[5] + "");
            if (valor_x_pagar > 0) {
                if (total_a_pagar >= valor_x_pagar) {
                    Object fila[] = {tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[1], tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getRowKey(), tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[5]};
                    lis_fact_pagadas.add(fila);
                    monto_sobrante = total_a_pagar - valor_x_pagar;
                    long ide_cpctr = Long.parseLong(tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getRowKey());
                    TablaGenerica tab_tiene_factura = utilitario.consultar("select * from cxc_detall_transa where ide_ccctr=" + ide_cpctr + " and ide_empr=" + utilitario.getVariable("ide_empr") + " "
                            + "group by ide_cccfa,ide_ccdtr having ide_cccfa is not null");
                    tab_tiene_factura.imprimirSql();
                    if (tab_tiene_factura.getTotalFilas() > 0) {
                        if (tab_tiene_factura.getValor(0, "ide_cccfa") != null) {
                            utilitario.getConexion().agregarSqlPantalla("update cxc_cabece_factura set pagado_cccfa=true where ide_cccfa=" + tab_tiene_factura.getValor(0, "ide_cccfa") + " and ide_empr=" + utilitario.getVariable("ide_empr"));
                            System.out.println("update cxc_cabece_factura set pagado_cccfa=true where ide_cccfa=" + tab_tiene_factura.getValor(0, "ide_cccfa") + " and ide_empr=" + utilitario.getVariable("ide_empr"));
                        }
                    }
                } else {
                    monto_sobrante = total_a_pagar - valor_x_pagar;
                    Object fila[] = {tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[1], tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getRowKey(), (total_a_pagar)};
                    lis_fact_pagadas.add(fila);
                }
                total_a_pagar = monto_sobrante;
            } else {
                break;
            }
        }
        for (int i = 0; i < lis_fact_pagadas.size(); i++) {
            Object obj_fila[] = (Object[]) lis_fact_pagadas.get(i);
            System.out.println("ide_cpcfa " + obj_fila[0] + " ide_cpctr " + obj_fila[1] + " valor " + obj_fila[2]);
        }
        
    }

    /**
     * Validaciones de la Transaccion CXC de Pago
     *
     * @return
     */
    private boolean validar() {
        boolean realizo_pago_sin_bancos_cxc = false;
        if (com_tip_tran_cxc.getValue() != null) {
            realizo_pago_sin_bancos_cxc = false;
            if (com_tip_tran_cxc.getValue() == null) {
                utilitario.agregarMensajeInfo("Debe seleccionar el 'TIPO DE TRANSACCIÓN'", "");
                return false;
            }
            
            if (aut_cuenta_cxc.getValue() == null) {
                utilitario.agregarMensajeInfo("Debe seleccionar una 'CUENTA DESTINO' ", "");
                return false;
            }
        } else {
            realizo_pago_sin_bancos_cxc = true;
        }
        
        if (aut_cliente_cxc.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un 'CLIENTE' ", "");
            return false;
        }
        if (tab_cuentas_x_cobrar.isEmpty()) {
            utilitario.agregarMensajeInfo("El Cliente seleccionado no tiene Cuentas por Cobrar ", "");
            return false;
        }
        if (tab_cuentas_x_cobrar.getListaFilasSeleccionadas().isEmpty()) {
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

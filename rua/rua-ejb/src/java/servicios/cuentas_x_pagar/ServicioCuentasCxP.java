/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.cuentas_x_pagar;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import servicios.ServicioBase;
import servicios.contabilidad.ServicioContabilidadGeneral;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class ServicioCuentasCxP extends ServicioBase {

    @EJB
    private ServicioContabilidadGeneral ser_conta_general;

    @PostConstruct
    public void init() {
        //Recupera todos los parametros que se van a ocupar
        parametros = utilitario.getVariables(
                "p_cxp_tipo_trans_factura",
                "p_cxp_estado_factura_normal",
                "p_cxp_tipo_trans_retencion",
                "p_cxp_tipo_trans_anticipo");
    }

    /**
     * Registra la Factura en una transaccion cxp
     *
     * @param tab_cab_factura
     * @return ide_cpctr Cabecera de la Transaccion CxP
     */
    public String generarTransaccionCompra(Tabla tab_cab_factura) {
        String ide_cpctr = "-1";
        if (tab_cab_factura != null) {
            TablaGenerica tab_cab_tran_cxp = new TablaGenerica();
            tab_cab_tran_cxp.setTabla("cxp_cabece_transa", "ide_cpctr");
            tab_cab_tran_cxp.getColumna("ide_cpctr").setExterna(false);
            tab_cab_tran_cxp.setCondicion("ide_cpctr=-1");
            tab_cab_tran_cxp.ejecutarSql();
            TablaGenerica tab_det_tran_cxp = new TablaGenerica();
            tab_det_tran_cxp.setTabla("cxp_detall_transa", "ide_cpdtr");
            tab_det_tran_cxp.getColumna("ide_cpdtr").setExterna(false);
            tab_det_tran_cxp.setCondicion("ide_cpdtr=-1");
            tab_det_tran_cxp.ejecutarSql();
            tab_cab_tran_cxp.insertar();
            tab_cab_tran_cxp.setValor("ide_cpttr", parametros.get("p_cxp_tipo_trans_factura"));//Tipo transaccion Factura     
            tab_cab_tran_cxp.setValor("ide_geper", tab_cab_factura.getValor("ide_geper"));
            tab_cab_tran_cxp.setValor("fecha_trans_cpctr", tab_cab_factura.getValor("fecha_trans_cpcfa"));
            tab_cab_tran_cxp.setValor("ide_cpcfa", tab_cab_factura.getValor("ide_cpcfa"));
            if (tab_cab_factura.getValor("observacion_cpcfa") != null && !tab_cab_factura.getValor("observacion_cpcfa").isEmpty()) {
                tab_cab_tran_cxp.setValor("observacion_cpctr", tab_cab_factura.getValor("observacion_cpcfa"));
            }
            tab_cab_tran_cxp.guardar();
            tab_det_tran_cxp.insertar();
            tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
            tab_det_tran_cxp.setValor("ide_cpctr", tab_cab_tran_cxp.getValor("ide_cpctr"));
            tab_det_tran_cxp.setValor("ide_cpcfa", tab_cab_factura.getValor("ide_cpcfa"));
            if (tab_cab_factura.getValor("ide_cntdo").equals(utilitario.getVariable("p_con_tipo_documento_factura"))) {
                tab_det_tran_cxp.setValor("ide_cpttr", parametros.get("p_cxp_tipo_trans_factura"));//Tipo transaccion Factura     
            } else if (tab_cab_factura.getValor("ide_cntdo").equals(utilitario.getVariable("p_con_tipo_documento_liquidacion_compra"))) {
                tab_det_tran_cxp.setValor("ide_cpttr", parametros.get("p_cxp_tipo_trans_factura"));//!!!!!******CAMBIAR Tipo transaccion Liquidacion en compras  
            } else if (tab_cab_factura.getValor("ide_cntdo").equals(utilitario.getVariable("p_con_tipo_documento_nota_venta"))) {
                tab_det_tran_cxp.setValor("ide_cpttr", parametros.get("p_cxp_tipo_trans_factura"));//!!!!!******CAMBIAR Tipo transaccion nota de venta 
            } else {
                //por defecto
                tab_det_tran_cxp.setValor("ide_cpttr", parametros.get("p_cxp_tipo_trans_factura"));//Tipo transaccion Factura     
            }

            tab_det_tran_cxp.setValor("fecha_trans_cpdtr", tab_cab_factura.getValor("fecha_trans_cpcfa"));
            tab_det_tran_cxp.setValor("valor_cpdtr", utilitario.getFormatoNumero(tab_cab_factura.getValor("total_cpcfa")));
            tab_det_tran_cxp.setValor("observacion_cpdtr", tab_cab_factura.getValor("observacion_cpcfa"));
            tab_det_tran_cxp.setValor("numero_pago_cpdtr", "0");
            tab_det_tran_cxp.setValor("fecha_venci_cpdtr", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura.getValor("fecha_emisi_cpcfa")), ser_conta_general.getDiasFormaPago(tab_cab_factura.getValor("ide_cndfp")))));
            tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_cab_factura.getValor("numero_cpcfa"));
            tab_det_tran_cxp.setValor("ide_cnccc", tab_cab_factura.getValor("ide_cnccc"));
            tab_det_tran_cxp.setValor("valor_anticipo_cpdtr", "0");
            tab_det_tran_cxp.guardar();
            ide_cpctr = tab_cab_tran_cxp.getValor("ide_cpctr");
        }
        return ide_cpctr;
    }

    /**
     * Registra la Factura en una transaccion cxp
     *
     * @param tab_cab_factura
     * @return ide_cpctr Cabecera de la Transaccion CxP
     */
    public String generarModificaTransaccionCompra(Tabla tab_cab_factura) {
        String ide_cpctr = "-1";
        if (tab_cab_factura != null) {

            TablaGenerica tab_cab_tran_cxp = new TablaGenerica();
            tab_cab_tran_cxp.setTabla("cxp_cabece_transa", "ide_cpctr");
            tab_cab_tran_cxp.getColumna("ide_cpctr").setExterna(false);
            tab_cab_tran_cxp.setCondicion("ide_cpcfa=" + tab_cab_factura.getValor("ide_cpcfa"));
            tab_cab_tran_cxp.ejecutarSql();
            ide_cpctr = tab_cab_tran_cxp.getValor("ide_cpctr");
            if (ide_cpctr == null || ide_cpctr.isEmpty()) {
                ide_cpctr = "-1";
            }
            TablaGenerica tab_det_tran_cxp = new TablaGenerica();
            tab_det_tran_cxp.setTabla("cxp_detall_transa", "ide_cpdtr");
            tab_det_tran_cxp.getColumna("ide_cpdtr").setExterna(false);
            tab_det_tran_cxp.setCondicion("ide_cpctr=" + ide_cpctr + " and ide_cpttr=" + parametros.get("p_cxp_tipo_trans_factura"));
            tab_det_tran_cxp.ejecutarSql();
            if (tab_cab_tran_cxp.isEmpty()) {
                tab_cab_tran_cxp.insertar();
            } else {
                tab_cab_tran_cxp.modificar(tab_cab_tran_cxp.getFilaActual());
            }
            tab_cab_tran_cxp.insertar();
            tab_cab_tran_cxp.setValor("ide_cpttr", parametros.get("p_cxp_tipo_trans_factura"));//Tipo transaccion Factura     
            tab_cab_tran_cxp.setValor("ide_geper", tab_cab_factura.getValor("ide_geper"));
            tab_cab_tran_cxp.setValor("fecha_trans_cpctr", tab_cab_factura.getValor("fecha_trans_cpcfa"));
            tab_cab_tran_cxp.setValor("ide_cpcfa", tab_cab_factura.getValor("ide_cpcfa"));
            if (tab_cab_factura.getValor("observacion_cpcfa") != null && !tab_cab_factura.getValor("observacion_cpcfa").isEmpty()) {
                tab_cab_tran_cxp.setValor("observacion_cpctr", tab_cab_factura.getValor("observacion_cpcfa"));
            }
            tab_cab_tran_cxp.guardar();
            if (tab_det_tran_cxp.isEmpty()) {
                tab_det_tran_cxp.insertar();
            } else {
                tab_det_tran_cxp.modificar(tab_det_tran_cxp.getFilaActual());
            }

            tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
            tab_det_tran_cxp.setValor("ide_cpctr", tab_cab_tran_cxp.getValor("ide_cpctr"));
            tab_det_tran_cxp.setValor("ide_cpcfa", tab_cab_factura.getValor("ide_cpcfa"));
            if (tab_cab_factura.getValor("ide_cntdo").equals(utilitario.getVariable("p_con_tipo_documento_factura"))) {
                tab_det_tran_cxp.setValor("ide_cpttr", parametros.get("p_cxp_tipo_trans_factura"));//Tipo transaccion Factura     
            } else if (tab_cab_factura.getValor("ide_cntdo").equals(utilitario.getVariable("p_con_tipo_documento_liquidacion_compra"))) {
                tab_det_tran_cxp.setValor("ide_cpttr", parametros.get("p_cxp_tipo_trans_factura"));//!!!!!******CAMBIAR Tipo transaccion Liquidacion en compras  
            } else if (tab_cab_factura.getValor("ide_cntdo").equals(utilitario.getVariable("p_con_tipo_documento_nota_venta"))) {
                tab_det_tran_cxp.setValor("ide_cpttr", parametros.get("p_cxp_tipo_trans_factura"));//!!!!!******CAMBIAR Tipo transaccion nota de venta 
            } else {
                //por defecto
                tab_det_tran_cxp.setValor("ide_cpttr", parametros.get("p_cxp_tipo_trans_factura"));//Tipo transaccion Factura     
            }

            tab_det_tran_cxp.setValor("fecha_trans_cpdtr", tab_cab_factura.getValor("fecha_trans_cpcfa"));
            tab_det_tran_cxp.setValor("valor_cpdtr", utilitario.getFormatoNumero(tab_cab_factura.getValor("total_cpcfa")));
            tab_det_tran_cxp.setValor("observacion_cpdtr", tab_cab_factura.getValor("observacion_cpcfa"));
            tab_det_tran_cxp.setValor("numero_pago_cpdtr", "0");
            tab_det_tran_cxp.setValor("fecha_venci_cpdtr", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura.getValor("fecha_emisi_cpcfa")), ser_conta_general.getDiasFormaPago(tab_cab_factura.getValor("ide_cndfp")))));
            tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_cab_factura.getValor("numero_cpcfa"));
            tab_det_tran_cxp.setValor("ide_cnccc", tab_cab_factura.getValor("ide_cnccc"));
            tab_det_tran_cxp.setValor("valor_anticipo_cpdtr", "0");
            tab_det_tran_cxp.guardar();
            ide_cpctr = tab_cab_tran_cxp.getValor("ide_cpctr");
        }
        return ide_cpctr;
    }

    public String generarTransaccionCompraAnticipo(Tabla tab_cab_factura, String ide_cpctr) {

        if (tab_cab_factura != null) {

            TablaGenerica tab_det_tran_cxp = new TablaGenerica();
            tab_det_tran_cxp.setTabla("cxp_detall_transa", "ide_cpdtr");
            tab_det_tran_cxp.getColumna("ide_cpdtr").setExterna(false);
            tab_det_tran_cxp.setCondicion("ide_cpdtr=-1");
            tab_det_tran_cxp.ejecutarSql();

            tab_det_tran_cxp.insertar();
            tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
            tab_det_tran_cxp.setValor("ide_cpctr", ide_cpctr);
            tab_det_tran_cxp.setValor("ide_cpcfa", tab_cab_factura.getValor("ide_cpcfa"));
            if (tab_cab_factura.getValor("ide_cntdo").equals(utilitario.getVariable("p_con_tipo_documento_factura"))) {
                tab_det_tran_cxp.setValor("ide_cpttr", parametros.get("p_cxp_tipo_trans_factura"));//Tipo transaccion Factura     
            } else if (tab_cab_factura.getValor("ide_cntdo").equals(utilitario.getVariable("p_con_tipo_documento_liquidacion_compra"))) {
                tab_det_tran_cxp.setValor("ide_cpttr", parametros.get("p_cxp_tipo_trans_factura"));//!!!!!******CAMBIAR Tipo transaccion Liquidacion en compras  
            } else if (tab_cab_factura.getValor("ide_cntdo").equals(utilitario.getVariable("p_con_tipo_documento_nota_venta"))) {
                tab_det_tran_cxp.setValor("ide_cpttr", parametros.get("p_cxp_tipo_trans_factura"));//!!!!!******CAMBIAR Tipo transaccion nota de venta 
            } else {
                //por defecto
                tab_det_tran_cxp.setValor("ide_cpttr", parametros.get("p_cxp_tipo_trans_factura"));//Tipo transaccion Factura     
            }

            tab_det_tran_cxp.setValor("fecha_trans_cpdtr", tab_cab_factura.getValor("fecha_trans_cpcfa"));
            tab_det_tran_cxp.setValor("valor_cpdtr", utilitario.getFormatoNumero(tab_cab_factura.getValor("total_cpcfa")));
            tab_det_tran_cxp.setValor("observacion_cpdtr", tab_cab_factura.getValor("observacion_cpcfa"));
            tab_det_tran_cxp.setValor("numero_pago_cpdtr", "0");
            tab_det_tran_cxp.setValor("fecha_venci_cpdtr", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura.getValor("fecha_emisi_cpcfa")), ser_conta_general.getDiasFormaPago(tab_cab_factura.getValor("ide_cndfp")))));
            tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_cab_factura.getValor("numero_cpcfa"));
            tab_det_tran_cxp.setValor("ide_cnccc", tab_cab_factura.getValor("ide_cnccc"));
            tab_det_tran_cxp.setValor("valor_anticipo_cpdtr", "0");
            tab_det_tran_cxp.guardar();
        }
        return ide_cpctr;
    }

    public String generarTransaccionAnticipo(String ide_geper, TablaGenerica tab_libro_banco) {
        String ide_cpctr = "-1";
        if (tab_libro_banco != null) {
            TablaGenerica tab_cab_tran_cxp = new TablaGenerica();
            tab_cab_tran_cxp.setTabla("cxp_cabece_transa", "ide_cpctr");
            tab_cab_tran_cxp.getColumna("ide_cpctr").setExterna(false);
            tab_cab_tran_cxp.setCondicion("ide_cpctr=-1");
            tab_cab_tran_cxp.ejecutarSql();
            TablaGenerica tab_det_tran_cxp = new TablaGenerica();
            tab_det_tran_cxp.setTabla("cxp_detall_transa", "ide_cpdtr");
            tab_det_tran_cxp.getColumna("ide_cpdtr").setExterna(false);
            tab_det_tran_cxp.setCondicion("ide_cpdtr=-1");
            tab_det_tran_cxp.ejecutarSql();
            tab_cab_tran_cxp.insertar();
            tab_cab_tran_cxp.setValor("ide_cpttr", parametros.get("p_cxp_tipo_trans_anticipo"));//Tipo transaccion Anticipo       
            tab_cab_tran_cxp.setValor("ide_geper", ide_geper);
            tab_cab_tran_cxp.setValor("fecha_trans_cpctr", tab_libro_banco.getValor("fecha_trans_teclb"));
            tab_cab_tran_cxp.setValor("observacion_cpctr", tab_libro_banco.getValor("observacion_teclb"));
            tab_cab_tran_cxp.guardar();
            tab_det_tran_cxp.insertar();
            tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
            tab_det_tran_cxp.setValor("ide_cpctr", tab_cab_tran_cxp.getValor("ide_cpctr"));
            tab_det_tran_cxp.setValor("ide_cpttr", parametros.get("p_cxp_tipo_trans_anticipo"));//Tipo transaccion Anticipo     

            tab_det_tran_cxp.setValor("fecha_trans_cpdtr", tab_libro_banco.getValor("fecha_trans_teclb"));
            tab_det_tran_cxp.setValor("valor_cpdtr", utilitario.getFormatoNumero(tab_libro_banco.getValor("valor_teclb")));
            tab_det_tran_cxp.setValor("observacion_cpdtr", tab_libro_banco.getValor("observacion_teclb"));
            tab_det_tran_cxp.setValor("numero_pago_cpdtr", "0");
            tab_det_tran_cxp.setValor("fecha_venci_cpdtr", tab_libro_banco.getValor("fecha_trans_teclb"));
            tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_libro_banco.getValor("numero_teclb"));
            tab_det_tran_cxp.setValor("ide_cnccc", tab_libro_banco.getValor("ide_cnccc"));
            tab_det_tran_cxp.setValor("ide_teclb", tab_libro_banco.getValor("ide_teclb"));
            tab_det_tran_cxp.setValor("valor_anticipo_cpdtr", "0");
            tab_det_tran_cxp.guardar();
            ide_cpctr = tab_cab_tran_cxp.getValor("ide_cpctr");
        }
        return ide_cpctr;
    }

    /**
     * Registra la Factura en una transaccion cxp
     *
     * @param ide_cpcfa
     * @param valorRetencion
     * @return ide_cpctr Cabecera de la Transaccion CxP
     */
    public String generarTransaccionRetencion(String ide_cpcfa, double valorRetencion) {
        String ide_cpctr = "-1";
        TablaGenerica tab_cab_factura = utilitario.consultar("SELECT * FROM cxp_cabece_factur WHERE ide_cpcfa=" + ide_cpcfa);
        if (tab_cab_factura.isEmpty() == false) {
            TablaGenerica tab_cab_tran_cxp = new TablaGenerica();
            tab_cab_tran_cxp.setTabla("cxp_cabece_transa", "ide_cpctr");
            tab_cab_tran_cxp.setCondicion("ide_cpcfa=" + ide_cpcfa);
            tab_cab_tran_cxp.ejecutarSql();
            TablaGenerica tab_det_tran_cxp = new TablaGenerica();
            tab_det_tran_cxp.setTabla("cxp_detall_transa", "ide_cpdtr");
            tab_det_tran_cxp.getColumna("ide_cpdtr").setExterna(false);
            tab_det_tran_cxp.setCondicion("ide_cpctr=" + tab_cab_tran_cxp.getValor("ide_cpctr"));
            tab_det_tran_cxp.ejecutarSql();
            tab_det_tran_cxp.insertar();
            tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
            tab_det_tran_cxp.setValor("ide_cpctr", tab_cab_tran_cxp.getValor("ide_cpctr"));
            tab_det_tran_cxp.setValor("ide_cpcfa", ide_cpcfa);
            tab_det_tran_cxp.setValor("ide_cpttr", utilitario.getVariable("p_cxp_tipo_trans_retencion"));
            tab_det_tran_cxp.setValor("fecha_trans_cpdtr", tab_cab_factura.getValor("fecha_trans_cpcfa"));
            tab_det_tran_cxp.setValor("valor_cpdtr", utilitario.getFormatoNumero(valorRetencion));
            tab_det_tran_cxp.setValor("observacion_cpdtr", tab_cab_factura.getValor("observacion_cpcfa"));
            tab_det_tran_cxp.setValor("numero_pago_cpdtr", "0");
            tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_cab_factura.getValor("numero_cpcfa"));
            tab_det_tran_cxp.setValor("ide_cnccc", tab_cab_factura.getValor("ide_cnccc"));
            tab_det_tran_cxp.setValor("valor_anticipo_cpdtr", "0");
            tab_det_tran_cxp.guardar();
            ide_cpctr = tab_cab_tran_cxp.getValor("ide_cpctr");
        }
        return ide_cpctr;
    }

    /**
     * Registra la Factura en una transaccion cxp
     *
     * @param ide_cpcfa
     * @param valorRetencion
     * @return ide_cpctr Cabecera de la Transaccion CxP
     */
    public String generarModificarTransaccionRetencion(String ide_cpcfa, double valorRetencion) {
        String ide_cpctr = "-1";
        TablaGenerica tab_cab_factura = utilitario.consultar("SELECT * FROM cxp_cabece_factur WHERE ide_cpcfa=" + ide_cpcfa);
        if (tab_cab_factura.isEmpty() == false) {
            TablaGenerica tab_cab_tran_cxp = new TablaGenerica();
            tab_cab_tran_cxp.setTabla("cxp_cabece_transa", "ide_cpctr");
            tab_cab_tran_cxp.setCondicion("ide_cpcfa=" + ide_cpcfa);
            tab_cab_tran_cxp.ejecutarSql();
            TablaGenerica tab_det_tran_cxp = new TablaGenerica();
            tab_det_tran_cxp.setTabla("cxp_detall_transa", "ide_cpdtr");
            tab_det_tran_cxp.getColumna("ide_cpdtr").setExterna(false);
            tab_det_tran_cxp.setCondicion("ide_cpctr=" + tab_cab_tran_cxp.getValor("ide_cpctr") + " and ide_cpttr=" + parametros.get("p_cxp_tipo_trans_retencion"));
            tab_det_tran_cxp.ejecutarSql();
            if (tab_det_tran_cxp.isEmpty()) {
                tab_det_tran_cxp.insertar();
            } else {
                tab_det_tran_cxp.modificar(tab_det_tran_cxp.getFilaActual());
            }
            tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
            tab_det_tran_cxp.setValor("ide_cpctr", tab_cab_tran_cxp.getValor("ide_cpctr"));
            tab_det_tran_cxp.setValor("ide_cpcfa", ide_cpcfa);
            tab_det_tran_cxp.setValor("ide_cpttr", parametros.get("p_cxp_tipo_trans_retencion"));
            tab_det_tran_cxp.setValor("fecha_trans_cpdtr", tab_cab_factura.getValor("fecha_trans_cpcfa"));
            tab_det_tran_cxp.setValor("valor_cpdtr", utilitario.getFormatoNumero(valorRetencion));
            tab_det_tran_cxp.setValor("observacion_cpdtr", tab_cab_factura.getValor("observacion_cpcfa"));
            tab_det_tran_cxp.setValor("numero_pago_cpdtr", "0");
            tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_cab_factura.getValor("numero_cpcfa"));
            tab_det_tran_cxp.setValor("ide_cnccc", tab_cab_factura.getValor("ide_cnccc"));
            tab_det_tran_cxp.setValor("valor_anticipo_cpdtr", "0");
            tab_det_tran_cxp.guardar();
            ide_cpctr = tab_cab_tran_cxp.getValor("ide_cpctr");
        }
        return ide_cpctr;
    }

    public String generarTransaccionPago(TablaGenerica tab_cab_factura, String ide_cpctr, String ide_teclb, double valor, String observacion, String num_documento) {
        TablaGenerica tab_det_tran_cxp = new TablaGenerica();
        tab_det_tran_cxp.setTabla("cxp_detall_transa", "ide_cpdtr");
        tab_det_tran_cxp.getColumna("ide_cpdtr").setExterna(false);
        tab_det_tran_cxp.setCondicion("ide_cpdtr=-1");
        tab_det_tran_cxp.ejecutarSql();
        tab_det_tran_cxp.insertar();
        String str_p_cxp_tipo_trans_pago = utilitario.getVariable("p_cxp_tipo_trans_pago");
        tab_det_tran_cxp.setValor("ide_teclb", ide_teclb);
        tab_det_tran_cxp.setValor("ide_cpcfa", tab_cab_factura.getValor("ide_cpcfa"));
        tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("ide_usua"));
        tab_det_tran_cxp.setValor("ide_cpttr", str_p_cxp_tipo_trans_pago);
        tab_det_tran_cxp.setValor("ide_cpctr", ide_cpctr);
        tab_det_tran_cxp.setValor("fecha_trans_cpdtr", utilitario.getFechaActual());
        tab_det_tran_cxp.setValor("valor_cpdtr", utilitario.getFormatoNumero(valor));
        tab_det_tran_cxp.setValor("observacion_cpdtr", observacion);
        tab_det_tran_cxp.setValor("numero_pago_cpdtr", getNumeroPagoDocumento(ide_cpctr) + "");
        tab_det_tran_cxp.setValor("fecha_venci_cpdtr", utilitario.getFechaActual());
        if (num_documento == null || num_documento.isEmpty()) {
            num_documento = tab_cab_factura.getValor("numero_cpcfa");
        }
        tab_det_tran_cxp.setValor("docum_relac_cpdtr", num_documento);
        tab_det_tran_cxp.guardar();
        return tab_det_tran_cxp.getValor("ide_cpdtr");
    }

    public String getSqlTipoDocumentosCxP() {
        return "select ide_cntdo,nombre_cntdo from con_tipo_document where ide_cntdo in ("
                + utilitario.getVariable("p_con_tipo_documento_factura") + ","
                + utilitario.getVariable("p_con_tipo_documento_liquidacion_compra") + ","
                + utilitario.getVariable("p_con_tipo_documento_nota_venta") + ","
                + utilitario.getVariable("p_con_tipo_documento_reembolso") + ","
                + utilitario.getVariable("p_con_tipo_documento_nota_credito") + ")";
    }

    /**
     * Retorna los documentos cxp de una sucursal en un rango de fechas
     *
     * @param fechaInicio
     * @param fechaFin
     * @param tipoDocumento
     * @return
     */
    public String getSqlDocumentos(String fechaInicio, String fechaFin, String tipoDocumento) {
        String strCondicionTipoDoc = "";
        if (tipoDocumento != null && tipoDocumento.isEmpty() == false && tipoDocumento.equalsIgnoreCase("null") == false) {
            strCondicionTipoDoc = " and a.ide_cntdo=" + tipoDocumento + " ";
        }
        return "select a.ide_cpcfa,fecha_emisi_cpcfa,a.ide_cnccc,nombre_cntdo, numero_cpcfa, a.ide_cpefa,nombre_cpefa ,nom_geper,identificac_geper,base_grabada_cpcfa as ventas12,base_tarifa0_cpcfa+base_no_objeto_iva_cpcfa as ventas0,valor_iva_cpcfa,total_cpcfa, observacion_cpcfa, fecha_trans_cpcfa,numero_cncre,a.ide_cncre \n"
                + " from cxp_cabece_factur a \n"
                + " inner join gen_persona b on a.ide_geper=b.ide_geper \n"
                + " left join cxp_estado_factur c on a.ide_cpefa=c.ide_cpefa \n"
                + " inner join con_tipo_document e on a.ide_cntdo= e.ide_cntdo  \n"
                + " left join con_cabece_retenc f on a.ide_cncre= f.ide_cncre  \n"
                + " where fecha_emisi_cpcfa BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + " and a.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " and ide_rem_cpcfa is null \n"
                + strCondicionTipoDoc
                + " ORDER BY fecha_emisi_cpcfa desc,numero_cpcfa desc,ide_cpcfa desc";
    }

    /**
     * Retorna los documentos cxp que no tienen comprobante de contabilidad de
     * una sucursal en un rango de fechas
     *
     * @param fechaInicio
     * @param fechaFin
     * @param tipoDocumento
     * @return
     */
    public String getSqlDocumentosNoContabilidad(String fechaInicio, String fechaFin, String tipoDocumento) {
        String strCondicionTipoDoc = "";
        if (tipoDocumento != null && tipoDocumento.isEmpty() == false && tipoDocumento.equalsIgnoreCase("null") == false) {
            strCondicionTipoDoc = " and a.ide_cntdo=" + tipoDocumento + " ";
        }
        return "select a.ide_cpcfa,fecha_emisi_cpcfa,nombre_cntdo, numero_cpcfa, a.ide_cpefa,nombre_cpefa ,nom_geper,identificac_geper,base_grabada_cpcfa as ventas12,base_tarifa0_cpcfa+base_no_objeto_iva_cpcfa as ventas0,valor_iva_cpcfa,total_cpcfa, observacion_cpcfa, fecha_trans_cpcfa,numero_cncre \n"
                + " from cxp_cabece_factur a \n"
                + " inner join gen_persona b on a.ide_geper=b.ide_geper \n"
                + " left join cxp_estado_factur c on a.ide_cpefa=c.ide_cpefa \n"
                + " inner join con_tipo_document e on a.ide_cntdo= e.ide_cntdo  \n"
                + " left join con_cabece_retenc f on a.ide_cncre= f.ide_cncre  \n"
                + " where fecha_emisi_cpcfa BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + " and a.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + "\n"
                + " and a.ide_cnccc is null and ide_rem_cpcfa is null \n"
                + strCondicionTipoDoc
                + " ORDER BY fecha_emisi_cpcfa desc,numero_cpcfa desc,ide_cpcfa desc";
    }

    /**
     * Retorna los documentos cxp que no tienen retencion de una sucursal en un
     * rango de fechas
     *
     * @param fechaInicio
     * @param fechaFin
     * @param tipoDocumento
     * @return
     */
    public String getSqlDocumentosNoRetencion(String fechaInicio, String fechaFin, String tipoDocumento) {
        String strCondicionTipoDoc = "";
        if (tipoDocumento != null && tipoDocumento.isEmpty() == false && tipoDocumento.equalsIgnoreCase("null") == false) {
            strCondicionTipoDoc = " and a.ide_cntdo=" + tipoDocumento + " ";
        }
        return "select a.ide_cpcfa,fecha_emisi_cpcfa,a.ide_cnccc,nombre_cntdo, numero_cpcfa, a.ide_cpefa,nombre_cpefa ,nom_geper,identificac_geper,base_grabada_cpcfa as ventas12,base_tarifa0_cpcfa+base_no_objeto_iva_cpcfa as ventas0,valor_iva_cpcfa,total_cpcfa, observacion_cpcfa, fecha_trans_cpcfa \n"
                + " from cxp_cabece_factur a \n"
                + " inner join gen_persona b on a.ide_geper=b.ide_geper \n"
                + " left join cxp_estado_factur c on a.ide_cpefa=c.ide_cpefa \n"
                + " inner join con_tipo_document e on a.ide_cntdo= e.ide_cntdo  \n"
                + " where fecha_emisi_cpcfa BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + " and a.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " and ide_rem_cpcfa is null \n"
                + " and ide_cncre is null \n"
                + strCondicionTipoDoc
                + " ORDER BY fecha_emisi_cpcfa desc,numero_cpcfa desc,ide_cpcfa desc";
    }

    /**
     * Retorna los documentos Anulados cxp de una sucursal en un rango de fechas
     *
     * @param fechaInicio
     * @param fechaFin
     * @param tipoDocumento
     * @return
     */
    public String getSqlDocumentosAnulados(String fechaInicio, String fechaFin, String tipoDocumento) {
        String strCondicionTipoDoc = "";
        if (tipoDocumento != null && tipoDocumento.isEmpty() == false && tipoDocumento.equalsIgnoreCase("null") == false) {
            strCondicionTipoDoc = " and a.ide_cntdo=" + tipoDocumento + " ";
        }
        return "select a.ide_cpcfa,fecha_emisi_cpcfa,a.ide_cnccc,nombre_cntdo, numero_cpcfa, a.ide_cpefa,nombre_cpefa ,nom_geper,identificac_geper,base_grabada_cpcfa as ventas12,base_tarifa0_cpcfa+base_no_objeto_iva_cpcfa as ventas0,valor_iva_cpcfa,total_cpcfa, observacion_cpcfa, fecha_trans_cpcfa,numero_cncre \n"
                + " from cxp_cabece_factur a \n"
                + " inner join gen_persona b on a.ide_geper=b.ide_geper \n"
                + " left join cxp_estado_factur c on a.ide_cpefa=c.ide_cpefa \n"
                + " inner join con_tipo_document e on a.ide_cntdo= e.ide_cntdo  \n"
                + " left join con_cabece_retenc f on a.ide_cncre= f.ide_cncre  \n"
                + " where fecha_emisi_cpcfa BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + " and a.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + "\n"
                + " and a.ide_cpefa=" + utilitario.getVariable("p_cxp_estado_factura_anulada") + " and ide_rem_cpcfa is null \n"
                + strCondicionTipoDoc
                + " ORDER BY fecha_emisi_cpcfa desc,numero_cpcfa desc,ide_cpcfa desc";
    }

    /**
     * Retorna sentencia SQL para obtener los documentos pendientes de pago un
     * rango de fechas por pagar X SUCURSAL
     *
     * @param fechaInicio
     * @param fechaFin
     * @param tipoDocumento
     * @return
     */
    public String getSqlDocumentosPorPagar(String fechaInicio, String fechaFin, String tipoDocumento) {
        String strCondicionTipoDoc = "";
        if (tipoDocumento != null && tipoDocumento.isEmpty() == false && tipoDocumento.equalsIgnoreCase("null") == false) {
            strCondicionTipoDoc = " and cf.ide_cntdo=" + tipoDocumento + " ";
        }
        return "select dt.ide_cpctr,"
                + "dt.ide_cpcfa,"
                + "case when (cf.fecha_emisi_cpcfa) is null then ct.fecha_trans_cpctr else cf.fecha_emisi_cpcfa end as FECHA,"
                + "nom_geper,identificac_geper,nombre_cntdo,cf.numero_cpcfa,"
                + "cf.total_cpcfa,"
                + "sum (dt.valor_cpdtr*tt.signo_cpttr) as saldo_x_pagar,"
                + "case when (cf.observacion_cpcfa) is NULL then ct.observacion_cpctr else cf.observacion_cpcfa end as OBSERVACION "
                + "from cxp_detall_transa dt "
                + "left join cxp_cabece_transa ct on dt.ide_cpctr=ct.ide_cpctr "
                + "left join cxp_cabece_factur cf on cf.ide_cpcfa=ct.ide_cpcfa and cf.ide_cpefa=" + utilitario.getVariable("p_cxp_estado_factura_normal") + " "
                + "left join cxp_tipo_transacc tt on tt.ide_cpttr=dt.ide_cpttr "
                + "left join con_tipo_document co on cf.ide_cntdo= co.ide_cntdo "
                + "left join gen_persona b on cf.ide_geper=b.ide_geper "
                + "where cf.ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_rem_cpcfa is null "
                + "and cf.fecha_emisi_cpcfa BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + strCondicionTipoDoc
                + "GROUP BY dt.ide_cpcfa,dt.ide_cpctr,cf.numero_cpcfa,nombre_cntdo, "
                + "cf.observacion_cpcfa,ct.observacion_cpctr,cf.fecha_emisi_cpcfa,ct.fecha_trans_cpctr,cf.total_cpcfa,nom_geper,identificac_geper "
                + "HAVING sum (dt.valor_cpdtr*tt.signo_cpttr) > 0 "
                + "ORDER BY cf.fecha_emisi_cpcfa ASC ,ct.fecha_trans_cpctr ASC,dt.ide_cpctr ASC";
    }

    /**
     * Sql de total de compras por mes por sucursal
     *
     * @param anio
     * @return
     */
    public String getSqlTotalComprasMensuales(String anio) {

        return "select nombre_gemes,\n"
                + "(select count(ide_cpcfa) as num_documentos from cxp_cabece_factur a where EXTRACT(MONTH FROM fecha_emisi_cpcfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cpcfa) in(" + anio + ") and ide_cpefa=" + parametros.get("p_cxp_estado_factura_normal") + " and ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " and ide_rem_cpcfa is null),\n"
                + "(select sum(base_grabada_cpcfa) as compras12 from cxp_cabece_factur a where EXTRACT(MONTH FROM fecha_emisi_cpcfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cpcfa) in(" + anio + ")  and ide_cpefa=" + parametros.get("p_cxp_estado_factura_normal") + " and ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " and ide_rem_cpcfa is null),\n"
                + "(select sum(base_tarifa0_cpcfa+base_no_objeto_iva_cpcfa) as compras0 from cxp_cabece_factur a where EXTRACT(MONTH FROM fecha_emisi_cpcfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cpcfa) in(" + anio + ")  and ide_cpefa=" + parametros.get("p_cxp_estado_factura_normal") + " and ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " and ide_rem_cpcfa is null),\n"
                + "(select sum(valor_iva_cpcfa) as iva from cxp_cabece_factur a where EXTRACT(MONTH FROM fecha_emisi_cpcfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cpcfa) in(" + anio + ")   and ide_cpefa=" + parametros.get("p_cxp_estado_factura_normal") + " and ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " and ide_rem_cpcfa is null),\n"
                + "(select sum(total_cpcfa) as total from cxp_cabece_factur a where EXTRACT(MONTH FROM fecha_emisi_cpcfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cpcfa) in(" + anio + ")  and ide_cpefa=" + parametros.get("p_cxp_estado_factura_normal") + " and ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " and ide_rem_cpcfa is null) \n"
                + "from gen_mes \n"
                + "order by ide_gemes";
    }

    /**
     * Retorna el sql con los anios que exuste facturacion en la empresa
     *
     * @return
     */
    public String getSqlAniosFacturacion() {
        return "select distinct EXTRACT(YEAR FROM fecha_emisi_cpcfa)||'' as anio,EXTRACT(YEAR FROM fecha_emisi_cpcfa)||'' as nom_anio  "
                + "from cxp_cabece_factur where ide_empr=" + utilitario.getVariable("IDE_EMPR") + " order by 1 desc ";
    }

    /**
     * Retorna el Nombre de un tipo de documento
     *
     * @param ide_cntdo
     * @return
     */
    public String getNombreTipoDocumento(String ide_cntdo) {
        return utilitario.consultar("select * from con_tipo_document where ide_cntdo=" + ide_cntdo).getValor("nombre_cntdo");
    }

    /**
     * Retorna los pagos realizados a un Docuemnto
     *
     * @param ide_cpcfa
     * @return
     */
    public String getSqlPagosDocumento(String ide_cpcfa) {
        return "SELECT ide_cpdtr,fecha_trans_cpdtr,docum_relac_cpdtr,nombre_tettb ,valor_cpdtr, nombre_tecba ||' '||nombre_teban as DESTINO,observacion_cpdtr as OBSERVACION,c.ide_tecba\n"
                + "FROM cxp_detall_transa a \n"
                + "left JOIN  cxp_tipo_transacc b on a.ide_cpttr =b.ide_cpttr \n"
                + "left join  tes_cab_libr_banc c on a.ide_teclb=c.ide_teclb \n"
                + "left join tes_cuenta_banco  d on c.ide_tecba=d.ide_tecba \n"
                + "left join tes_banco e on d.ide_teban=e.ide_teban \n"
                + "left join tes_tip_tran_banc f on c.ide_tettb=f.ide_tettb \n"
                + "where numero_pago_cpdtr > 0 \n"
                + "and ide_cpcfa=" + ide_cpcfa + "\n"
                + "order by fecha_trans_cpdtr";
    }

    public String getSqlCabeceraDocumento(String ide_cpcfa) {
        return "SELECT * from cxp_cabece_factur where ide_cpcfa=" + ide_cpcfa;
    }

    public String getNumeroPagoDocumento(String ide_cpctr) {
        //RETORNA EL PAGO MAXIMO         
        List lis_sql = utilitario.getConexion().consultar("select max(numero_pago_cpdtr) from cxp_detall_transa where ide_cpctr=" + ide_cpctr);
        int num = 0;
        if (lis_sql.get(0) != null) {
            num = Integer.parseInt(lis_sql.get(0) + "") + 1;
        }
        return (num + 1) + "";
    }

    public String getSqlActualizaPagoDocumento(String ide_cpcfa) {
        return "update cxp_cabece_factur set pagado_cpcfa=true where ide_cpcfa=" + ide_cpcfa;
    }

    /**
     * Retorna Todas lOS SALDOS CXP de los proveedores
     *
     * @return
     */
    public String getSqlTransaccionesCxP() {
        return "SELECT d.ide_geper,identificac_geper AS IDENTIFICACION,nom_geper as PROVEEDOR,sum (valor_cpdtr*signo_cpttr) as SALDO\n"
                + "FROM cxp_detall_transa a \n"
                + "INNER JOIN cxp_tipo_transacc b on a.IDE_CPTTR =b.IDE_CPTTR \n"
                + "INNER JOIN cxp_cabece_transa d on a.ide_cpctr=d.ide_cpctr \n"
                + "INNER JOIN gen_persona e on d.ide_geper= e.ide_geper\n"
                + "WHERE d.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "GROUP BY d.ide_geper,identificac_geper,nom_geper\n"
                + "order by nom_geper";
    }

    public String getSqlAnticiposCliente(String ide_geper) {
        return "select b.ide_cpctr,nom_geper,valor_cpdtr,observacion_cpdtr from cxp_detall_transa  a\n"
                + "inner join cxp_cabece_transa b on a.ide_cpctr=b.ide_cpctr\n"
                + "inner join gen_persona c on b.ide_geper=c.ide_geper\n"
                + "where a.ide_cpttr=" + parametros.get("p_cxp_tipo_trans_anticipo") + "\n"
                + "and b.ide_geper=" + ide_geper + "\n"
                + "and b.ide_cpcfa is null " //sin factura --ahun no se utiliza
                + "and a.ide_sucu=" + utilitario.getVariable("IDE_SUCU");
    }

    public String getSqlDocumentosCliente(String ide_geper) {

        String fechaFin = utilitario.getFechaActual();
        String fechaInicio = utilitario.getFormatoFecha(utilitario.sumarDiasFecha(new Date(), -45));

        return "select a.ide_cpcfa,fecha_emisi_cpcfa,nombre_cntdo, numero_cpcfa,nom_geper,identificac_geper,base_grabada_cpcfa as ventas12,base_tarifa0_cpcfa+base_no_objeto_iva_cpcfa as ventas0,valor_iva_cpcfa,total_cpcfa, observacion_cpcfa, fecha_trans_cpcfa,numero_cncre,a.ide_cncre \n"
                + " from cxp_cabece_factur a \n"
                + " inner join gen_persona b on a.ide_geper=b.ide_geper \n"
                + " left join cxp_estado_factur c on a.ide_cpefa=c.ide_cpefa \n"
                + " inner join con_tipo_document e on a.ide_cntdo= e.ide_cntdo  \n"
                + " left join con_cabece_retenc f on a.ide_cncre= f.ide_cncre  \n"
                + " where fecha_emisi_cpcfa BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + " and a.ide_geper=" + ide_geper + " "
                + " and a.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " and ide_rem_cpcfa is null \n"
                + " ORDER BY fecha_emisi_cpcfa desc,numero_cpcfa desc,ide_cpcfa desc";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.cuentas_x_pagar;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
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
        parametros = utilitario.getVariables("p_cxp_tipo_trans_factura");

    }

    /**
     * Registra la Factura en una transaccion cxp
     *
     * @param tab_cab_factura
     * @return ide_ccctr Cabecera de la Transaccion CxP
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
            tab_det_tran_cxp.setValor("numero_pago_cpdtr", 0 + "");
            tab_det_tran_cxp.setValor("fecha_venci_cpdtr", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura.getValor("fecha_emisi_cpcfa")), ser_conta_general.getDiasFormaPago(tab_cab_factura.getValor("ide_cndfp")))));
            tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_cab_factura.getValor("numero_cpcfa"));
            tab_det_tran_cxp.setValor("ide_cnccc", tab_cab_factura.getValor("ide_cnccc"));
            tab_det_tran_cxp.setValor("valor_anticipo_cpdtr", "0");
            tab_det_tran_cxp.guardar();
            ide_cpctr = tab_cab_tran_cxp.getValor("ide_cpctr");
        }
        return ide_cpctr;
    }

    public String getSqlTipoDocumentosCxP() {
        return "select ide_cntdo,nombre_cntdo from con_tipo_document where ide_cntdo in ("
                + utilitario.getVariable("p_con_tipo_documento_factura") + ","
                + utilitario.getVariable("p_con_tipo_documento_liquidacion_compra") + ","
                + utilitario.getVariable("p_con_tipo_documento_nota_venta") + ")";
    }

    /**
     * Retorna los documentos cxp de una sucursal en un rango de fechas
     *
     * @param ide_ccdaf
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
        return "select a.ide_cpcfa,fecha_emisi_cpcfa,nombre_cntdo, numero_cpcfa, a.ide_cpefa,nombre_cpefa ,nom_geper,identificac_geper,base_grabada_cpcfa as ventas12,base_tarifa0_cpcfa+base_no_objeto_iva_cpcfa as ventas0,valor_iva_cpcfa,total_cpcfa, observacion_cpcfa, fecha_trans_cpcfa,ide_cnccc \n"
                + " from cxp_cabece_factur a \n"
                + " inner join gen_persona b on a.ide_geper=b.ide_geper \n"
                + " left join cxp_estado_factur c on a.ide_cpefa=c.ide_cpefa \n"
                + " inner join con_tipo_document e on a.ide_cntdo= e.ide_cntdo  \n"
                + " where fecha_emisi_cpcfa BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + " and a.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + "\n"
                + strCondicionTipoDoc
                + " ORDER BY fecha_emisi_cpcfa desc,numero_cpcfa desc,ide_cpcfa desc";
    }

}

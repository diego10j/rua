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
        parametros = utilitario.getVariables("IDE_SUCU", "IDE_EMPR", "IDE_USUA", "p_cxp_tipo_trans_factura");

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
            tab_det_tran_cxp.setValor("ide_usua", parametros.get("IDE_USUA"));
            tab_det_tran_cxp.setValor("ide_cpctr", tab_cab_tran_cxp.getValor("ide_cpctr"));
            tab_det_tran_cxp.setValor("ide_cpcfa", tab_cab_factura.getValor("ide_cpcfa"));
            tab_det_tran_cxp.setValor("ide_cpttr", parametros.get("p_cxp_tipo_trans_factura"));//Tipo transaccion Factura     
            tab_det_tran_cxp.setValor("fecha_trans_cpdtr", tab_cab_factura.getValor("fecha_trans_cpcfa"));
            tab_det_tran_cxp.setValor("valor_cpdtr", utilitario.getFormatoNumero(tab_cab_factura.getValor("total_cpcfa")));
            tab_det_tran_cxp.setValor("observacion_cpdtr", tab_cab_factura.getValor("observacion_cpcfa"));
            tab_det_tran_cxp.setValor("numero_pago_cpdtr", 0 + "");
            tab_det_tran_cxp.setValor("fecha_venci_cpdtr", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura.getValor("fecha_emisi_cpcfa")), ser_conta_general.getDiasFormaPago(tab_cab_factura.getValor("ide_cndfp")))));
            tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_cab_factura.getValor("numero_cpcfa"));
            tab_det_tran_cxp.setValor("ide_cnccc", tab_cab_factura.getValor("ide_cnccc"));
            tab_det_tran_cxp.guardar();
            ide_cpctr = tab_cab_tran_cxp.getValor("ide_cpctr");
        }
        return ide_cpctr;
    }

}

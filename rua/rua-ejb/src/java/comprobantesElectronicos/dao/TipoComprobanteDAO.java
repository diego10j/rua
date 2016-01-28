/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.entidades.Tipocomprobante;
import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@Stateless
public class TipoComprobanteDAO implements TipoComprobanteDAOLocal {

    @Override
    public Tipocomprobante getTipoComprobante(String alter_tribu_cntdo) {
        Tipocomprobante tipoComprobante = null;
        Utilitario utilitario = new Utilitario();
        TablaGenerica tab_consulta = utilitario.consultar("SELECT * from con_tipo_document where alter_tribu_cntdo='" + alter_tribu_cntdo + "'");
        if (tab_consulta.isEmpty() == false) {
            tipoComprobante = new Tipocomprobante(new Integer(tab_consulta.getValor("ide_cntdo")), tab_consulta.getValor("nombre_cntdo"), alter_tribu_cntdo);
        }
        return tipoComprobante;
    }

    @Override
    public Tipocomprobante getTipoFactura() {
        return getTipoComprobante("01");
    }

    @Override
    public Tipocomprobante getTipoNotadeCredito() {
        return getTipoComprobante("04");
    }

    @Override
    public Tipocomprobante getTipoNotadeDebito() {
        return getTipoComprobante("05");
    }

    @Override
    public Tipocomprobante getTipoGuiadeRemision() {
        return getTipoComprobante("06");
    }

    @Override
    public Tipocomprobante getTipoComprobantedeRetencion() {
        return getTipoComprobante("07");
    }

}

/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.entidades.Tipocomprobante;
import javax.ejb.Local;

/**
 *
 * @author dfjacome
 */
@Local
public interface TipoComprobanteDAOLocal {

    /**
     * Retorna Tipo Factura 1
     *
     * @return Tipocomprobante
     */
    public Tipocomprobante getTipoFactura();

    /**
     * Retorna Tipo Nota de Crédito 2
     *
     * @return Tipocomprobante
     */
    public Tipocomprobante getTipoNotadeCredito();

    /**
     * Retorna Tipo Nota de débito 3
     *
     * @return Tipocomprobante
     */
    public Tipocomprobante getTipoNotadeDebito();

    /**
     * Retorna Tipo Guía de remisión 4
     *
     * @return Tipocomprobante
     */
    public Tipocomprobante getTipoGuiadeRemision();

    /**
     * Retorna Tipo Comprobante de retención 5
     *
     * @return Tipocomprobante
     */
    public Tipocomprobante getTipoComprobantedeRetencion();

    /**
     * Retorna un tipo de comprobante
     *
     * @param valor
     * @return
     */
    public Tipocomprobante getTipoComprobante(String valor);
}

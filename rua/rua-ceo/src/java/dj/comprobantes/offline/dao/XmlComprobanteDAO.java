/*
 *********************************************************************
 Objetivo: Interface DAO con métodos para guardar y obtener el xml 
 del comprobante enviado al SRI
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.dao;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.dto.XmlComprobante;
import dj.comprobantes.offline.exception.GenericException;

/**
 *
 * @author diego.jacome
 */
public interface XmlComprobanteDAO {

    /**
     * Retorna el detalle del xml enviado al SRI
     *
     * @param comprobante
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public XmlComprobante getSriComprobanteActual(Comprobante comprobante) throws GenericException;

    /**
     * Guarda o modifica el xml enviado al SRI
     *
     * @param sriComprobante
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public void guardar(XmlComprobante sriComprobante) throws GenericException;

}

/*
 *********************************************************************
 Objetivo: Interface con métodos para generar XML de la Factura
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.exception.GenericException;

/**
 *
 * @author diego.jacome
 */
public interface FacturaService {

    /**
     * Genera el squema xml de una Factura Electronica Offline
     *
     * @param comprobante
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public String getXmlFactura(Comprobante comprobante) throws GenericException;
}

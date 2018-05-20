/*
 *********************************************************************
 Objetivo: Interface con métodos llamar web service de Recepción del SRI
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
public interface RecepcionService {

    /**
     * Firma Digitalmente el comprobante y lo envia al WS de Recepción offline
     * del SRI
     *
     * @param comprobateActual
     * @param xml
     * @throws GenericException
     */
    public void enviarRecepcionOfflineSRI(Comprobante comprobateActual, String xml) throws GenericException;
}

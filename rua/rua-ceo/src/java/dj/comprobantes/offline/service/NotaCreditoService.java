/*
 *********************************************************************
 Objetivo: Interface con métodos para generar XML de la Nota de Crédito
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
 * @author djacome
 */
public interface NotaCreditoService {

    /**
     * Genera el squema xml de una Nota de Crédito Electronica Offline
     *
     * @param comprobante
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public String getXmlNotaCredito(Comprobante comprobante) throws GenericException;
}

/*
 *********************************************************************
 Objetivo: Interface con métodos para crear archivos xml y pdf
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
public interface ArchivoService {

    /**
     * Retorna Archivo XML del Comprobante
     *
     * @param comprobante
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public byte[] getXml(Comprobante comprobante) throws GenericException;

    /**
     * Retorna Archivo PDF del Comprobante
     *
     * @param comprobante
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public byte[] getPdf(Comprobante comprobante) throws GenericException;
}

/*
 *********************************************************************
 Objetivo: Interface con métodos llamar web service de Autorización SRI
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.exception.GenericException;
import ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante;

/**
 *
 * @author diego.jacome
 */
public interface AutorizacionService {

    /**
     * Busca los comprobantes en estado RECIBIDO que pueden ser AUTORIZADOS,
     * envia al correo del cliente el archivo pdf y archivo xml
     *
     * @param comprobateActual
     * @throws GenericException
     */
    public void enviarRecibidosOfflineSRI(Comprobante comprobateActual) throws GenericException;

    /**
     * * Permite la invocacion del web service de autorizacion Offlien SRI
     *
     * @param claveDeAcceso que se enviara al WS del SRI
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public RespuestaComprobante enviarClaveDeAcceso(String claveDeAcceso) throws GenericException;

}

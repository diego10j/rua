/*
 *********************************************************************
 Objetivo: Interface con métodos para los comprobantes electrónicos
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
public interface ComprobanteService {

    /**
     * Consuta todos los comprobantes en estado PENDIENTE (CONTINGENCIA) ,los
     * firma electronicamente y los envia al Ws de Recepción Offline del SRI
     *
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public void enviarRecepcionSRI() throws GenericException;

    /**
     * Conslta todos los comprobantes en estado RECIBIDA, los envia al Ws de
     * Autorización Offline y envia el PDF y XML RIDE
     *
     * @throws GenericException
     */
    public void enviarAutorizacionSRI() throws GenericException;

    /**
     * Genera la clave de acceso a un comprobante
     *
     * @param comprobante
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public String getClaveAcceso(Comprobante comprobante) throws GenericException;

    /**
     * Actualiza el estado y mensajes que retorna el WS de Recepción
     *
     * @param xmlFirmado
     * @param comprobante
     * @param mensajeRespuesta
     * @throws GenericException
     */
    public void actualizarRecepcionComprobante(String xmlFirmado, Comprobante comprobante, String mensajeRespuesta) throws GenericException;

    /**
     * Actualiza estado y mensaje que retorna el WS de Autorización
     *
     * @param xmlFirmado
     * @param comprobante
     * @param mensajeRespuesta
     * @throws GenericException
     */
    public void actualizarAutorizacionComprobante(String xmlFirmado, Comprobante comprobante, String mensajeRespuesta) throws GenericException;

    /**
     * Retorna un comprobante por clave de acceso
     *
     * @param claveAcceso
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public Comprobante getComprobantePorClaveAcceso(String claveAcceso) throws GenericException;

    /**
     * Retorna un Comprobante por ide_srcom
     *
     * @param ide_srcom
     * @return
     * @throws GenericException
     */
    public Comprobante getComprobantePorId(Long ide_srcom) throws GenericException;

    /**
     * envia un comprobante al SRI
     *
     * @param claveAcceso
     * @throws GenericException
     */
    public void enviarComprobante(String claveAcceso) throws GenericException;

    /**
     * Retorna una guia de remision a partir de una factura
     *
     * @param comprobante
     * @return
     * @throws GenericException
     */
    public Comprobante getComprobanteGuia(Comprobante comprobante) throws GenericException;
}

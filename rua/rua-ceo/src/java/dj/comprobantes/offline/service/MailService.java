/*
 *********************************************************************
 Objetivo: Interface con métodos para enviar al correo del cliente el
 archivo pdf y xml del comprobante AUTORIZADO
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Comprobante;

/**
 *
 * @author diego.jacome
 */
public interface MailService {

    /**
     * Agrega un comprobante a una lista para ser enviados
     *
     * @param comprobanteMail
     * @return
     */
    public String agregarCorreo(Comprobante comprobanteMail);

    /**
     * Envia todos los comprobantes agregados a la lista
     *
     * @return
     */
    public String enviarTodos();

    /**
     * Envia un solo comprobante
     *
     * @param comprobante
     * @return
     */
    public String enviar(Comprobante comprobante);
}

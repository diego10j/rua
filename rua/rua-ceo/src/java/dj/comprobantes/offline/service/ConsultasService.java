/*
 *********************************************************************
 Objetivo: Interface con métodos para consultas utilizados en 
 web services
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 15-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.exception.GenericException;
import java.util.List;

/**
 *
 * @author djacome
 */
public interface ConsultasService {

    /**
     * Busca las facturas autorizadas de un cliente
     *
     * @param identificacion
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public List<Comprobante> getFacturasAutorizadas(String identificacion) throws GenericException;

    /**
     * Busca las notas de credito autorizadas de un cliente
     *
     * @param identificacion
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public List<Comprobante> getNotCreditoAutorizadas(String identificacion) throws GenericException;

    /**
     * Busca las notas de debito autorizadas de un cliente
     *
     * @param identificacion
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public List<Comprobante> getNotDebitoAutorizadas(String identificacion) throws GenericException;

    /**
     * Busca las guias de remision autorizadas de un cliente
     *
     * @param identificacion
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public List<Comprobante> getGuiasAutorizadas(String identificacion) throws GenericException;

    /**
     * Busca los comprobantes de retención autorizadas de un cliente
     *
     * @param identificacion
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public List<Comprobante> getRetencionesAutorizadas(String identificacion) throws GenericException;

}

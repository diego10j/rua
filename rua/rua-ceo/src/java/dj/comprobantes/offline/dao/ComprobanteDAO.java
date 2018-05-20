/*
 *********************************************************************
 Objetivo: Interface DAO con métodos para el Comprobante
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.dao;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import dj.comprobantes.offline.exception.GenericException;
import java.util.List;

/**
 *
 * @author diego.jacome
 */
public interface ComprobanteDAO {

    /**
     * Retorna los comprobantes que se encuentran en un determinado estado
     *
     * @param estado
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public List<Comprobante> getComprobantesPorEstado(EstadoComprobanteEnum estado) throws GenericException;

    /**
     * Retorna un comprobante por clave de acceso
     *
     * @param claveAcceso
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public Comprobante getComprobantePorClaveAcceso(String claveAcceso) throws GenericException;

    /**
     * Retorna los comprobantes AUTORIZADOS de un cliente
     *
     * @param identificacion
     * @param codigoDocumento
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public List<Comprobante> getComprobantesCliente(String identificacion, TipoComprobanteEnum codigoDocumento) throws GenericException;

    /**
     * Actualizar la información de un comprobante
     *
     * @param comprobante
     * @throws GenericException
     */
    public void actualizar(Comprobante comprobante) throws GenericException;

    /**
     * Retorna un comprobante por ide_srcom
     *
     * @param ide_srcom
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public Comprobante getComprobantePorId(Long ide_srcom) throws GenericException;

    /**
     * Retorna una Guia de Remision de una factura
     *
     * @param ide_srcom
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public Comprobante getComprobanteGuia(Long ide_srcom) throws GenericException;

    
    
    /**
     * Lista de comprobantes autorizados que no se encuentranb en la nube
     * @return
     * @throws GenericException 
     */
    public List<Comprobante> getComprobantesAutorizadosNoNube() throws GenericException;
}

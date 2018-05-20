/*
 *********************************************************************
 Objetivo: Interface RetencionService
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 06-Abril-2017             D. Jácome        RFC-201704-843
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.exception.GenericException;

/**
 *
 * @author djacome
 */
public interface RetencionService {

    /**
     * Genera el squema xml de un Comprobante de Retención Electronica Offline
     * @param comprobante
     * @return
     * @throws GenericException 
     */
    public String getXmlRetencion(Comprobante comprobante) throws GenericException;
        
    
    
}

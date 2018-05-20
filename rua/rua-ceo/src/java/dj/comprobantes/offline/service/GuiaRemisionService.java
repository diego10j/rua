/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.exception.GenericException;
import javax.ejb.Local;

/**
 *
 * @author djacome
 */
@Local
public interface GuiaRemisionService {

    /**
     * Genera el squema xml de una Guia de Remision Offline
     *
     * @param comprobante
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public String getXmlGuiaRemision(Comprobante comprobante) throws GenericException;
}

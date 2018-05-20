/*
 *********************************************************************
 Objetivo: Interface con métodos para obtener y actualizar datos del Emisor
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Emisor;
import dj.comprobantes.offline.exception.GenericException;

/**
 *
 * @author diego.jacome
 */
public interface EmisorService {

    /**
     * Retorna el emisor para Comprobantes Electroncios
     *
     * @param sucursal
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public Emisor getEmisor(String sucursal) throws GenericException;


}

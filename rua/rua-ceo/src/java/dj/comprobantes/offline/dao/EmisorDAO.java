/*
 *********************************************************************
 Objetivo: Interface DAO con métodos para el Emisor
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.dao;

import dj.comprobantes.offline.dto.Emisor;
import dj.comprobantes.offline.exception.GenericException;

/**
 *
 * @author diego.jacome
 */
public interface EmisorDAO {

    /**
     * Retorna el Emisor registrado para emitir comprobantes electrónicos
     *
     * @param sucursal
     * @return Emisor
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public Emisor getEmisor(String sucursal) throws GenericException;

}

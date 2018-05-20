/*
 *********************************************************************
 Objetivo: Interface DAO con métodos para obtener la Firma Electrónica
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.dao;

import dj.comprobantes.offline.dto.Firma;
import dj.comprobantes.offline.exception.GenericException;
import java.util.List;

/**
 *
 * @author diego.jacome
 */
public interface FirmaDAO {

    /**
     * Busca la firma digital que se encuentre disponible
     *
     * @param sucursal
     * @return Firma Disponible
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public Firma getFirmaDisponible(String sucursal) throws GenericException;

    /**
     * Busca todas las firmas digitales
     *
     * @return Lista de Firmas
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public List<Firma> getTodasFirmas() throws GenericException;
}

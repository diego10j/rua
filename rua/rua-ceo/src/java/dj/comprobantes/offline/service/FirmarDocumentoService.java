/*
 *********************************************************************
 Objetivo: Interface con métodos para firmar electronicamente 
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Firma;
import dj.comprobantes.offline.exception.GenericException;
import org.w3c.dom.Document;

/**
 *
 * @author diego.jacome
 */
public interface FirmarDocumentoService {

    /**
     * Firma Electronicamente a un documento xml
     *
     * @param document documneto XML
     * @param sucursal
     * @return Documento firmado
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public Document getDocumentoFirmado(Document document,String sucursal) throws GenericException;

    /**
     * Validamos el archivo de la firma digital y su clave de acceso, se valida
     * la fecha de expiración de la firma
     *
     * @param firma
     * @return retorna "" si todo valido correctamente, caso contrario retorna
     * el mensaje de error
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public boolean validaCertificado(Firma firma) throws GenericException;
}

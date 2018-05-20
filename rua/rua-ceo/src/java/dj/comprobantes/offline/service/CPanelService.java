/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.dto.DetalleComprobante;
import dj.comprobantes.offline.exception.GenericException;
import javax.ejb.Local;

/**
 *
 * @author djacome
 */
@Local
public interface CPanelService {

    /**
     * Guarda un comprbante en la nube CPanel, y su pdf 
     *
     * @param comprobante
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public boolean guardarComprobanteNube(Comprobante comprobante) throws GenericException;

    /**
     * Sube los comprobantes Autorizados que no se encuentran en la nube
     *
     * @throws GenericException
     */
    public void subirComprobantesPendientes() throws GenericException;

    /**
     * Reenvia un comprobante a un correo electronico
     *
     * @param correo
     * @param id
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public void reenviarComprobante(String correo, Long id) throws GenericException;

    /**
     * Guarda los detalles de un comprobante en la nube
     *
     * @param detalleComprobante
     * @throws GenericException
     */
    public void guardarDetalleComprobanteNube(DetalleComprobante detalleComprobante) throws GenericException;
}

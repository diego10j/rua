/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.entidades.Estadocomprobante;
import javax.ejb.Local;

/**
 *
 * @author dfjacome
 */
@Local
public interface EstadoComprobanteDAOLocal {

    /**
     * Busca una estado por el nombre
     *
     * @param nombreEstado
     * @return
     */
    public Estadocomprobante getEstadoporNombre(String nombreEstado);

    /**
     * Retorna estado Recibida 1
     *
     * @return Estadocomprobante
     */
    public Estadocomprobante getEstadoRecibida();

    /**
     * Retorna estado Devuelta 2
     *
     * @return Estadocomprobante
     */
    public Estadocomprobante getEstadoDevuelta();

    /**
     * Retorna estado Autorizado 3
     *
     * @return Estadocomprobante
     */
    public Estadocomprobante getEstadoAutorizado();

    /**
     * Retorna estado Rechazado 4
     *
     * @return Estadocomprobante
     */
    public Estadocomprobante getEstadoRechazado();

    /**
     * Retorna estado Contingencia 5
     *
     * @return Estadocomprobante
     */
    public Estadocomprobante getEstadoContingencia();

    /**
     * Retorna estado No Autorizado 6
     *
     * @return Estadocomprobante
     */
    public Estadocomprobante getEstadoNoAutorizado();

    /**
     * Retorna un tipo de documento
     *
     * @param valor
     * @return
     */
    public Estadocomprobante getEstadocomprobante(String valor);

}

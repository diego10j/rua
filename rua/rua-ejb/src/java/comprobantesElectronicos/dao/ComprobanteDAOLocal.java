/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.entidades.Clavecontingencia;
import comprobantesElectronicos.entidades.Comprobante;
import comprobantesElectronicos.entidades.Estadocomprobante;
import comprobantesElectronicos.entidades.Tipocomprobante;
import framework.componentes.Tabla;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author dfjacome
 */
@Local
public interface ComprobanteDAOLocal {

    /**
     * Busca un comprobante por numero de comprobante
     *
     * @param estab
     * @param ptoEmi
     * @param secuencial
     * @return
     */
    public Comprobante getComprobanteporNumero(String estab, String ptoEmi, String secuencial);

    /**
     * Actualiza la clave de acceso y el estado de un comprobante
     *
     * @param comprobante
     * @param claveAcceso
     * @param estadoComprobante
     */
    public void actualizarClaveAccesoyEstado(Comprobante comprobante, String claveAcceso, Estadocomprobante estadoComprobante);

    /**
     * Actualiza el estado de un comprobante
     *
     *
     * @param comprobante
     * @param estadoComprobante
     */
    public void actualizarEstado(Comprobante comprobante, Estadocomprobante estadoComprobante);

    /**
     * Actualiza la clave de acceso de un comprobante
     *
     * @param comprobante
     * @param claveAcceso
     */
    public void actualizarClaveAcceso(Comprobante comprobante, String claveAcceso);

    /**
     * Actualiza la clave de contingencia de un comprobante y asigna el tipo de
     * emision y la clave de acceso
     *
     * @param comprobante
     * @param claveContingencia
     */
    public void actualizarClaveContingencia(Comprobante comprobante, Clavecontingencia claveContingencia);

    /**
     * Busca un comprobante por codigo de comprobante
     *
     * @param Codigocomprobante
     * @return
     */
    public Comprobante getComprobanteporCodigocomprobante(String Codigocomprobante);

    /**
     * Busca un comprobante por clave de acceso
     *
     * @param claveAcceso
     * @return
     */
    public Comprobante getComprobanteporClaveAcceso(String claveAcceso);

    /**
     * Busca todos los comprobantes por estado
     *
     * @param estadoComprobante
     * @return
     */
    public List<Comprobante> getComprobantesEstado(Estadocomprobante estadoComprobante);

    /**
     * Busca todos los comprobantes de un cliente
     *
     * @param identificacion
     * @return
     */
    public List<Comprobante> getComprobantesAutorizadosCliente(String identificacion);

    /**
     * Busca todos los comprobantes de un cliente
     *
     * @param identificacion
     * @param secuencial
     * @return
     */
    public List<Comprobante> getComprobantesAutorizadosCliente(String identificacion, String secuencial);

    /**
     * Busca todos los comprobantes de un cliente
     *
     * @param identificacion
     * @param tipoComprobante
     * @return
     */
    public List<Comprobante> getComprobantesAutorizadosCliente(String identificacion, Tipocomprobante tipoComprobante);

    /**
     * Busca todos los comprobantes por tipo de comprobante
     *
     * @param tipoComprobante
     * @return
     */
    public List<Comprobante> getComprobantesTipo(Tipocomprobante tipoComprobante);

    /**
     * Busca todos los comprobantes por tipo de comprobante y estado de
     * comprobante
     *
     * @param tipoComprobante
     * @param estadoComprobante
     * @return
     */
    public List<Comprobante> getComprobantesTipoEstado(Tipocomprobante tipoComprobante, Estadocomprobante estadoComprobante);

    /**
     * Guarda un comprobante tipo Factura mediante un objeto Tabla
     *
     * @param tab_factura
     * @param tab_detalle
     * @return
     */
    public String guardarComprobanteFactura(Tabla tab_factura, Tabla tab_detalle);

}

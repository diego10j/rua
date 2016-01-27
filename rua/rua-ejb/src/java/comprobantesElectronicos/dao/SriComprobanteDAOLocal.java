/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.entidades.Comprobante;
import comprobantesElectronicos.entidades.Sricomprobante;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author dfjacome
 */
@Local
public interface SriComprobanteDAOLocal {

    /**
     * Retorna el último detalle del intento enviado al SRI
     *
     * @param comprobante
     * @return
     */
    public Sricomprobante getSriComprobanteActual(Comprobante comprobante);

    /**
     * Busca todos los intentos enviados al sri de un comprobante
     *
     * @param comprobante
     * @return
     */
    public List<Sricomprobante> getTodosSriComprobantes(Comprobante comprobante);

    public void crear(Sricomprobante sriComprobante);

    public void actualizar(Sricomprobante sriComprobante);
}

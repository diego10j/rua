/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.entidades.Firma;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author dfjacome
 */
@Local
public interface FirmaDAOLocal{

    /**
     * Busca la firma digital que se encuentre disponible
     *
     * @return Firma Disponible
     */
    public Firma getFirmaDisponible();

    /**
     * Busca todas las firmas digitales
     *
     * @return Lista de Firmas
     */
    public List<Firma> getTodasFirmas();
}

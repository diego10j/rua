/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.entidades.Clavecontingencia;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author dfjacome
 */
@Local
public interface ClaveContingenciaDAOLocal{

    /**
     * Busca todas las claves disponibles
     *
     * @return Lista de claves
     */
    public List<Clavecontingencia> getTodasClavesDisponibles();

    /**
     * Retorna una clave disponible
     *
     * @return Clavecontingencia
     */
    public Clavecontingencia getClaveDisponible();

    /**
     * Retorna el numero de claves disponibles
     *
     * @return numero entero
     */
    public int getNumeroClavesDisponibles();

    /**
     * Actualiza a no disponible una clave de contingencia que fue asignada
     * @param claveContingencia
     */
    public void actualizarClaveNoDisponible(Clavecontingencia claveContingencia);
}

/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.entidades.Tipodocumento;
import javax.ejb.Local;

/**
 *
 * @author dfjacome
 */
@Local
public interface TipoDocumentoDAOLocal {

    /**
     * Retorna el numero de tabla de cobis..catalogo
     *
     * @return
     */
    public String getTablaCatalogo();

    /**
     * Retorna un tipo de documento
     *
     * @param valor
     * @return
     */
    public Tipodocumento getTipoDocumento(String valor);
}

/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.entidades.Emisor;
import javax.ejb.Local;

/**
 *
 * @author dfjacome
 */
@Local
public interface EmisorDAOLocal {
    
    /**
     * Busca el Emisor registrado para emitir comprobantes electrónicos
     * @return Emisor
     */
    public Emisor getEmisor();   
    
}

/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.entidades.Clavecontingencia;
import framework.aplicacion.TablaGenerica;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@Stateless
public class ClaveContingenciaDAO implements ClaveContingenciaDAOLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public List<Clavecontingencia> getTodasClavesDisponibles() {
        Utilitario utilitario = new Utilitario();
        List<Clavecontingencia> lisClaveContingencia = new ArrayList();
        TablaGenerica tab_consulta = utilitario.consultar("select * from sri_claves_contingencia where cc_disponible=true");
        for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {

            Clavecontingencia clave = new Clavecontingencia();
            clave.setCodigoclave(new Long(tab_consulta.getValor(i, "ide_srclc")));
            clave.setClavecont(tab_consulta.getValor(i, "clavecont_srclc"));
            clave.setDisponiblecont(Boolean.valueOf(tab_consulta.getValor(i, "disponible_srclc")));
            lisClaveContingencia.add(clave);
        }
        return lisClaveContingencia;
    }

    @Override
    public void actualizarClaveNoDisponible(Clavecontingencia claveContingencia) {
        Utilitario utilitario = new Utilitario();
        utilitario.getConexion().ejecutarSql("UPDATE sri_claves_contingencia set disponible_srclc=false WHERE ide_srclc=" + claveContingencia.getCodigoclave());
    }

    @Override
    public Clavecontingencia getClaveDisponible() {
        List<Clavecontingencia> lisClaves = getTodasClavesDisponibles();
        if (lisClaves != null && !lisClaves.isEmpty()) {
            return lisClaves.get(0);
        }
        return null;
    }

    @Override
    public int getNumeroClavesDisponibles() {
        List<Clavecontingencia> lisClaves = getTodasClavesDisponibles();
        if (lisClaves != null && !lisClaves.isEmpty()) {
            return lisClaves.size();
        }
        return 0;
    }

}

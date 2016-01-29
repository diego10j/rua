/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.entidades.Estadocomprobante;
import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@Stateless
public class EstadoComprobanteDAO implements EstadoComprobanteDAOLocal {

    private final Utilitario utilitario = new Utilitario();

    @Override
    public Estadocomprobante getEstadocomprobante(String ide_sresc) {
        Estadocomprobante estadoComprobante = null;
        TablaGenerica tab_consulta = utilitario.consultar("select * from sri_estado_comprobante where ide_sresc=" + ide_sresc);
        if (tab_consulta.isEmpty() == false) {
            estadoComprobante = new Estadocomprobante(ide_sresc, tab_consulta.getValor("nombre_sresc"));
        }
        return estadoComprobante;
    }

    @Override
    public Estadocomprobante getEstadoporNombre(String nombre_sresc) {
        Estadocomprobante estadoComprobante = null;
        TablaGenerica tab_consulta = utilitario.consultar("select * from sri_estado_comprobante where nombre_sresc='" + nombre_sresc + "'");
        if (tab_consulta.isEmpty() == false) {
            estadoComprobante = new Estadocomprobante(tab_consulta.getValor("ide_sresc"), nombre_sresc);
        }
        return estadoComprobante;
    }

    @Override
    public Estadocomprobante getEstadoRecibida() {
        return getEstadocomprobante("1");
    }

    @Override
    public Estadocomprobante getEstadoDevuelta() {
        return getEstadocomprobante("2");
    }

    @Override
    public Estadocomprobante getEstadoAutorizado() {
        return getEstadocomprobante("3");
    }

    @Override
    public Estadocomprobante getEstadoRechazado() {
        return getEstadocomprobante("4");
    }

    @Override
    public Estadocomprobante getEstadoContingencia() {
        return getEstadocomprobante("5");
    }

    @Override
    public Estadocomprobante getEstadoNoAutorizado() {
        return getEstadocomprobante("6");
    }

}

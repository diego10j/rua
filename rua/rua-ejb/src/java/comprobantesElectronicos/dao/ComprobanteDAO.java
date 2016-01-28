/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.entidades.Clavecontingencia;
import comprobantesElectronicos.entidades.Comprobante;
import comprobantesElectronicos.entidades.Estadocomprobante;
import comprobantesElectronicos.entidades.Tipocomprobante;
import framework.aplicacion.TablaGenerica;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@Stateless
public class ComprobanteDAO implements ComprobanteDAOLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    private EstadoComprobanteDAOLocal estadoComprobanteDAO;

    private final Utilitario utilitario = new Utilitario();

    @Override
    public Comprobante getComprobanteporNumero(String estab, String ptoEmi, String secuencial) {
        TablaGenerica tab_consulta = utilitario.consultar("SELECT * FROM sri_comprobante  WHERE secuencial_srcom ='" + secuencial + "' and ptoemi_srcom='" + ptoEmi + "' and estab_srcom='" + estab + "'");
        Comprobante comprobante = new Comprobante(tab_consulta);
        return comprobante;
    }

    @Override
    public void actualizarClaveAccesoyEstado(Comprobante comprobante, String claveAcceso, Estadocomprobante estadoComprobante) {
        comprobante.setClaveacceso(claveAcceso);
        comprobante.setCodigoestado(estadoComprobante);
        actualizar(comprobante);
    }

    @Override
    public void actualizarEstado(Comprobante comprobante, Estadocomprobante estadoComprobante) {
        comprobante.setCodigoestado(estadoComprobante);
        actualizar(comprobante);
    }

    @Override
    public void actualizarClaveAcceso(Comprobante comprobante, String claveAcceso) {
        comprobante.setClaveacceso(claveAcceso);
        actualizar(comprobante);
    }

    @Override
    public void actualizarClaveContingencia(Comprobante comprobante, Clavecontingencia claveContingencia) {
        comprobante.setCodigoclave(claveContingencia);
        actualizar(comprobante);
    }

    @Override
    public Comprobante getComprobanteporCodigocomprobante(String Codigocomprobante) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Comprobante getComprobanteporClaveAcceso(String claveAcceso) {

        TablaGenerica tab_consulta = utilitario.consultar("SELECT * FROM sri_comprobante  WHERE claveacceso_srcom='" + claveAcceso + "'");
        Comprobante comprobante = new Comprobante(tab_consulta);
        return comprobante;

    }

    @Override
    public List<Comprobante> getComprobantesEstado(Estadocomprobante estadoComprobante) {
        List<Comprobante> lisComprobantesEstado = new ArrayList();
        TablaGenerica tab_consulta = utilitario.consultar("SELECT * FROM sri_comprobante  WHERE va_estado_comprobante='" + estadoComprobante.getCodigoestado() + "'");
        for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {
            lisComprobantesEstado.add(new Comprobante(tab_consulta, i));
        }
        return lisComprobantesEstado;
    }

    public void actualizar(Comprobante comprobante) {

        String va_estado_comprobante = comprobante.getCodigoestado() == null ? "Null" : "'" + comprobante.getCodigoestado().getCodigoestado() + "'";
        String va_clave_acceso = comprobante.getClaveacceso() == null ? "Null" : "'" + comprobante.getClaveacceso() + "'";
        String va_firma = comprobante.getCodigofirma() == null ? "Null" : "" + comprobante.getCodigofirma().getCodigofirma() + "";
        String va_clave_contingencia = comprobante.getCodigoclave() == null ? "Null" : "" + comprobante.getCodigoclave().getCodigoclave() + "";
        String va_autorizacion_sri = comprobante.getNumAutorizacion() == null ? "Null" : "'" + comprobante.getNumAutorizacion() + "'";
        String va_tipo_emision = comprobante.getTipoemision() == null ? "Null" : "" + comprobante.getTipoemision() + "";
        String va_fec_autoriza = comprobante.getFechaautoriza() == null ? "Null" : "'" + utilitario.getFormatoFecha(comprobante.getFechaautoriza()) + "'";

        String sql = "UPDATE sri_comprobante set"
                + " ide_sresc=" + va_estado_comprobante
                + " ,claveacceso_srcom=" + va_clave_acceso
                + " ,ide_srfid =" + va_firma
                + " ,ide_srclc=" + va_clave_contingencia
                + " ,autorizacion_srcom =" + va_autorizacion_sri
                + " ,tipoemision_srcom=" + va_tipo_emision
                + " ,autorizacion_srcom=" + va_fec_autoriza
                + " WHERE secuencial_srcom =" + comprobante.getCodigocomprobante();
        utilitario.getConexion().ejecutarSql(sql);

    }

    @Override
    public List<Comprobante> getComprobantesAutorizadosCliente(String ide_geper) {

        List<Comprobante> lisComprobantes = new ArrayList();

        TablaGenerica tab_consulta = utilitario.consultar("SELECT * FROM sri_comprobante  WHERE ide_geper=" + ide_geper + " and ide_sresc=" + estadoComprobanteDAO.getEstadoAutorizado().getCodigoestado() + " order by fechaemision_srcom desc");
        for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {
            lisComprobantes.add(new Comprobante(tab_consulta, i));
        }

        return lisComprobantes;
    }

    @Override
    public List<Comprobante> getComprobantesAutorizadosCliente(String ide_geper, Tipocomprobante tipoComprobante) {
        List<Comprobante> lisComprobantes = new ArrayList();
        TablaGenerica tab_consulta = utilitario.consultar("SELECT * FROM sri_comprobante  WHERE ide_geper=" + ide_geper + " and coddoc_srcom='" + tipoComprobante.getAlternotipcomp() + "' and ide_sresc=" + estadoComprobanteDAO.getEstadoAutorizado().getCodigoestado() + " order by fechaemision_srcom desc");
        for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {
            lisComprobantes.add(new Comprobante(tab_consulta, i));
        }
        return lisComprobantes;
    }

    @Override
    public List<Comprobante> getComprobantesAutorizadosCliente(String ide_geper, String secuencial) {

        List<Comprobante> lisComprobantes = new ArrayList();

        TablaGenerica tab_consulta = utilitario.consultar("SELECT * FROM sri_comprobante  WHERE ide_geper=" + ide_geper + " and secuencial_srcom='" + secuencial + "' and ide_sresc=" + estadoComprobanteDAO.getEstadoAutorizado().getCodigoestado() + " order by fechaemision_srcom desc");
        for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {
            lisComprobantes.add(new Comprobante(tab_consulta, i));
        }

        return lisComprobantes;
    }

    @Override
    public List<Comprobante> getComprobantesTipo(Tipocomprobante tipoComprobante) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Comprobante> getComprobantesTipoEstado(Tipocomprobante tipoComprobante, Estadocomprobante estadoComprobante) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

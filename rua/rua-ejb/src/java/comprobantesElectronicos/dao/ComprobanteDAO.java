/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.conexion.ConexionSybaseCentral;
import comprobantesElectronicos.ejb.ejbUtilitario;
import comprobantesElectronicos.entidades.Clavecontingencia;
import comprobantesElectronicos.entidades.Comprobante;
import comprobantesElectronicos.entidades.Estadocomprobante;
import comprobantesElectronicos.entidades.Tipocomprobante;
import framework.aplicacion.TablaGenerica;

import java.sql.ResultSet;
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
        ConexionSybaseCentral conn = new ConexionSybaseCentral();
        try {
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
            conn.ejecutar(sql);
        } catch (Exception e) {
        } finally {
            conn.desconectar();
        }
    }

    @Override
    public List<Comprobante> getComprobantesAutorizadosCliente(String identificacion) {
        ConexionSybaseCentral conn = new ConexionSybaseCentral();
        List<Comprobante> lisComprobantes = new ArrayList();
        try {
            ResultSet res = conn.consultar("SELECT * FROM cob_remesas..re_anexo_detalle  WHERE va_idcliente='" + identificacion + "' and va_estado_comprobante='" + estadoComprobanteDAO.getEstadoAutorizado().getCodigoestado() + "' order by va_fec_crea desc");
            while (res.next()) {
                lisComprobantes.add(new Comprobante(res));
            }
            res.close();
        } catch (Exception e) {
        } finally {
            conn.desconectar();
        }
        return lisComprobantes;
    }

    @Override
    public List<Comprobante> getComprobantesAutorizadosCliente(String identificacion, Tipocomprobante tipoComprobante) {
        ConexionSybaseCentral conn = new ConexionSybaseCentral();
        List<Comprobante> lisComprobantes = new ArrayList();
        try {
            ResultSet res = conn.consultar("SELECT * FROM cob_remesas..re_anexo_detalle  WHERE va_idcliente='" + identificacion + "' and va_tipoComprobante='" + tipoComprobante.getAlternotipcomp() + "' and va_estado_comprobante='" + estadoComprobanteDAO.getEstadoAutorizado().getCodigoestado() + "' order by va_fec_crea desc");
            while (res.next()) {
                lisComprobantes.add(new Comprobante(res));
            }
            res.close();
        } catch (Exception e) {
        } finally {
            conn.desconectar();
        }
        return lisComprobantes;
    }

    @Override
    public List<Comprobante> getComprobantesAutorizadosCliente(String identificacion, String secuencial) {
        ConexionSybaseCentral conn = new ConexionSybaseCentral();
        List<Comprobante> lisComprobantes = new ArrayList();
        try {
            ResultSet res = conn.consultar("SELECT * FROM cob_remesas..re_anexo_detalle  WHERE va_idcliente='" + identificacion + "' and va_nro_factura='" + secuencial + "' and va_estado_comprobante='" + estadoComprobanteDAO.getEstadoAutorizado().getCodigoestado() + "' order by va_fec_crea desc");
            while (res.next()) {
                lisComprobantes.add(new Comprobante(res));
            }
            res.close();
        } catch (Exception e) {
        } finally {
            conn.desconectar();
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

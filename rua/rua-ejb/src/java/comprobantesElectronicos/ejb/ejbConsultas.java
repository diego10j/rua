/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.ejb;

import comprobantesElectronicos.dao.ComprobanteDAOLocal;
import comprobantesElectronicos.dao.TipoComprobanteDAOLocal;
import comprobantesElectronicos.entidades.Comprobante;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author dfjacome
 */
@Stateless
@LocalBean
public class ejbConsultas {

    @EJB
    private ComprobanteDAOLocal comprobanteDAO;
    @EJB
    private TipoComprobanteDAOLocal tipoComprobanteDAO;
   
    /**
     * Busca las facturas autorizadas de un cliente
     *
     * @param identificacion
     * @return
     */
    public List<Comprobante> getFacturasAutorizadas(String identificacion) {
        return comprobanteDAO.getComprobantesAutorizadosCliente(identificacion, tipoComprobanteDAO.getTipoFactura());
    }

    /**
     * Busca las notas de credito autorizadas de un cliente
     *
     * @param identificacion
     * @return
     */
    public List<Comprobante> getNotCreditoAutorizadas(String identificacion) {
        return comprobanteDAO.getComprobantesAutorizadosCliente(identificacion, tipoComprobanteDAO.getTipoNotadeCredito());
    }

    /**
     * Busca las notas de debito autorizadas de un cliente
     *
     * @param identificacion
     * @return
     */
    public List<Comprobante> getNotDebitoAutorizadas(String identificacion) {
        return comprobanteDAO.getComprobantesAutorizadosCliente(identificacion, tipoComprobanteDAO.getTipoNotadeDebito());
    }

    /**
     * Busca las guias de remision autorizadas de un cliente
     *
     * @param identificacion
     * @return
     */
    public List<Comprobante> getGuiasAutorizadas(String identificacion) {
        return comprobanteDAO.getComprobantesAutorizadosCliente(identificacion, tipoComprobanteDAO.getTipoNotadeDebito());
    }

    /**
     * Busca los comprobantes de retención autorizados de un cliente
     *
     * @param identificacion
     * @return
     */
    public List<Comprobante> getRetencionesAutorizadas(String identificacion) {
        return comprobanteDAO.getComprobantesAutorizadosCliente(identificacion, tipoComprobanteDAO.getTipoComprobantedeRetencion());
    }

   
}

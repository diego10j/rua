/*
 * Autor: Diego Jácome   11-03-2015
 */
package paginas.comprobantesElectronicos.controladores;

import comprobantesElectronicos.dao.SriComprobanteDAOLocal;
import comprobantesElectronicos.ejb.ejbConsultas;
import comprobantesElectronicos.ejb.ejbReportes;
import comprobantesElectronicos.entidades.Comprobante;
import comprobantesElectronicos.entidades.Sricomprobante;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import persistencia.Conexion;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@ManagedBean
@ViewScoped
public class ConsultasBean implements Serializable {

    @EJB
    private ejbConsultas ejbConsultas;

    @EJB
    private ejbReportes ejbReportes;
    @EJB
    private SriComprobanteDAOLocal sriComprobanteDAO;

    private String identificacion;
    private Comprobante comprobanteActual;
    private List<Comprobante> lisFactura;
    private List<Comprobante> lisNotCredito;
    private List<Comprobante> lisNotDebito;
    private List<Comprobante> lisGuia;
    private List<Comprobante> lisRetencion;

    @PostConstruct
    public void limpiar() {
        identificacion = null;
        comprobanteActual = null;
        lisFactura = null;
        lisNotCredito = null;
        lisNotDebito = null;
        lisGuia = null;
        lisRetencion = null;
        Utilitario utilitario = new Utilitario();
        Conexion conexion = utilitario.getConexion();
        if (conexion == null) {
            conexion = new Conexion();
            String str_recursojdbc = utilitario.getPropiedad("recursojdbc");
            conexion.setUnidad_persistencia(str_recursojdbc);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("CONEXION", conexion);
        }
    }

    /**
     * Consulta los Comprobantes de acuerdo a la identificacion ingresada
     */
    public void consultar() {
        if (identificacion != null) {
            lisFactura = ejbConsultas.getFacturasAutorizadas(identificacion);
            lisNotCredito = ejbConsultas.getNotCreditoAutorizadas(identificacion);
            lisNotDebito = ejbConsultas.getNotDebitoAutorizadas(identificacion);
            lisGuia = ejbConsultas.getGuiasAutorizadas(identificacion);
            lisRetencion = ejbConsultas.getRetencionesAutorizadas(identificacion);
        }
    }

    public void generarFacturaPDF() {
        if (comprobanteActual != null) {
            ejbReportes.generarFacturaPDF(String.valueOf(comprobanteActual.getCodigocomprobante()));
        }
    }

    public void generarNotaCreditoPDF() {
        Sricomprobante sriComprobante = sriComprobanteDAO.getSriComprobanteActual(comprobanteActual);
        if (sriComprobante != null) {
            ejbReportes.generarNotaCreditoPDF(sriComprobante.getXmlcomprobante(), comprobanteActual);
        }
    }

    public void generarNotaDebitoPDF() {
        Sricomprobante sriComprobante = sriComprobanteDAO.getSriComprobanteActual(comprobanteActual);
        if (sriComprobante != null) {

        }
    }

    public void generarGuiaPDF() {
        Sricomprobante sriComprobante = sriComprobanteDAO.getSriComprobanteActual(comprobanteActual);
        if (sriComprobante != null) {

        }
    }

    public void generarRetencionPDF() {
        Sricomprobante sriComprobante = sriComprobanteDAO.getSriComprobanteActual(comprobanteActual);
        if (comprobanteActual != null) {
            ejbReportes.generarComprobanteRetencion(sriComprobante.getXmlcomprobante(), comprobanteActual);
        }
    }

    public void generarXML() {

        if (comprobanteActual != null) {
            ejbReportes.generarComprobanteXML(String.valueOf(comprobanteActual.getCodigocomprobante()));
        }
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public List<Comprobante> getLisFactura() {
        return lisFactura;
    }

    public void setLisFactura(List<Comprobante> lisFactura) {
        this.lisFactura = lisFactura;
    }

    public List<Comprobante> getLisNotCredito() {
        return lisNotCredito;
    }

    public void setLisNotCredito(List<Comprobante> lisNotCredito) {
        this.lisNotCredito = lisNotCredito;
    }

    public List<Comprobante> getLisNotDebito() {
        return lisNotDebito;
    }

    public void setLisNotDebito(List<Comprobante> lisNotDebito) {
        this.lisNotDebito = lisNotDebito;
    }

    public List<Comprobante> getLisGuia() {
        return lisGuia;
    }

    public void setLisGuia(List<Comprobante> lisGuia) {
        this.lisGuia = lisGuia;
    }

    public List<Comprobante> getLisRetencion() {
        return lisRetencion;
    }

    public void setLisRetencion(List<Comprobante> lisRetencion) {
        this.lisRetencion = lisRetencion;
    }

    public Comprobante getComprobanteActual() {
        return comprobanteActual;
    }

    public void setComprobanteActual(Comprobante comprobanteActual) {
        this.comprobanteActual = comprobanteActual;
    }

}

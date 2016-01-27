/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.entidades;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author dfjacome
 */
public class Sricomprobante implements Serializable {
    private Long codigocompsri;
    private Date fecha;   
    private String xmlcomprobante;   
    private String mensajerecepcion;
    private String mensajeautorizacion;   
    private Estadocomprobante codigoestado;   
    private Comprobante codigocomprobante;

    public Sricomprobante() {
    }

    public Sricomprobante(Long codigocompsri) {
        this.codigocompsri = codigocompsri;
    }

    public Sricomprobante(Long codigocompsri, Date fecha, String xmlcomprobante) {
        this.codigocompsri = codigocompsri;
        this.fecha = fecha;
        this.xmlcomprobante = xmlcomprobante;
    }

    public Long getCodigocompsri() {
        return codigocompsri;
    }

    public void setCodigocompsri(Long codigocompsri) {
        this.codigocompsri = codigocompsri;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getXmlcomprobante() {
        return xmlcomprobante;
    }

    public void setXmlcomprobante(String xmlcomprobante) {
        this.xmlcomprobante = xmlcomprobante;
    }

    public String getMensajerecepcion() {
        return mensajerecepcion;
    }

    public void setMensajerecepcion(String mensajerecepcion) {
        this.mensajerecepcion = mensajerecepcion;
    }

    public String getMensajeautorizacion() {
        return mensajeautorizacion;
    }

    public void setMensajeautorizacion(String mensajeautorizacion) {
        this.mensajeautorizacion = mensajeautorizacion;
    }

    public Estadocomprobante getCodigoestado() {
        return codigoestado;
    }

    public void setCodigoestado(Estadocomprobante codigoestado) {
        this.codigoestado = codigoestado;
    }

    public Comprobante getCodigocomprobante() {
        return codigocomprobante;
    }

    public void setCodigocomprobante(Comprobante codigocomprobante) {
        this.codigocomprobante = codigocomprobante;
    }
    
}

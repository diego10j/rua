/*
 *********************************************************************
 Objetivo: Clase Firma
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author diego.jacome
 */
public class XmlComprobante implements Serializable {

    private Long codigocompsri;
    private Date fecha;
    private String xmlcomprobante;
    private String mensajerecepcion;
    private String mensajeautorizacion;
    private Integer codigoestado;
    private Comprobante codigocomprobante;

    public XmlComprobante() {
    }

    public XmlComprobante(Long codigocompsri) {
        this.codigocompsri = codigocompsri;
    }

    public XmlComprobante(Long codigocompsri, Date fecha, String xmlcomprobante) {
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

    public Integer getCodigoestado() {
        return codigoestado;
    }

    public void setCodigoestado(Integer codigoestado) {
        this.codigoestado = codigoestado;
    }

    public Comprobante getCodigocomprobante() {
        return codigocomprobante;
    }

    public void setCodigocomprobante(Comprobante codigocomprobante) {
        this.codigocomprobante = codigocomprobante;
    }

}

/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.entidades;

import java.io.Serializable;

/**
 *
 * @author dfjacome
 */
public class Emisor implements Serializable {

    private Integer codigoemisor;
    private String ruc;
    private String razonsocial;
    private String nombrecomercial;
    private String dirmatriz;
    private String contribuyenteespecial;
    private String obligadocontabilidad;
    private String logoempresa;
    private Integer tiempomaxespera;
    private Integer ambiente;
    private String wsdlrecepcion;
    private String wsdlautirizacion;
    private String xmlversion;

    public Emisor() {
    }

    public Emisor(Integer codigoemisor) {
        this.codigoemisor = codigoemisor;
    }

    public Emisor(Integer codigoemisor, String ruc, String nombrecomercial, String dirmatriz) {
        this.codigoemisor = codigoemisor;
        this.ruc = ruc;
        this.nombrecomercial = nombrecomercial;
        this.dirmatriz = dirmatriz;
    }

    public Integer getCodigoemisor() {
        return codigoemisor;
    }

    public void setCodigoemisor(Integer codigoemisor) {
        this.codigoemisor = codigoemisor;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonsocial() {
        return razonsocial;
    }

    public void setRazonsocial(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public String getNombrecomercial() {
        return nombrecomercial;
    }

    public void setNombrecomercial(String nombrecomercial) {
        this.nombrecomercial = nombrecomercial;
    }

    public String getDirmatriz() {
        return dirmatriz;
    }

    public void setDirmatriz(String dirmatriz) {
        this.dirmatriz = dirmatriz;
    }

    public String getContribuyenteespecial() {
        return contribuyenteespecial;
    }

    public void setContribuyenteespecial(String contribuyenteespecial) {
        this.contribuyenteespecial = contribuyenteespecial;
    }

    public String getObligadocontabilidad() {
        return obligadocontabilidad;
    }

    public void setObligadocontabilidad(String obligadocontabilidad) {
        this.obligadocontabilidad = obligadocontabilidad;
    }

    public String getLogoempresa() {
        return logoempresa;
    }

    public void setLogoempresa(String logoempresa) {
        this.logoempresa = logoempresa;
    }

    public Integer getTiempomaxespera() {
        return tiempomaxespera;
    }

    public void setTiempomaxespera(Integer tiempomaxespera) {
        this.tiempomaxespera = tiempomaxespera;
    }

    public Integer getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(Integer ambiente) {
        this.ambiente = ambiente;
    }

    public String getWsdlrecepcion() {
        return wsdlrecepcion;
    }

    public void setWsdlrecepcion(String wsdlrecepcion) {
        this.wsdlrecepcion = wsdlrecepcion;
    }

    public String getWsdlautirizacion() {
        return wsdlautirizacion;
    }

    public void setWsdlautirizacion(String wsdlautirizacion) {
        this.wsdlautirizacion = wsdlautirizacion;
    }

    public String getXmlversion() {
        return xmlversion;
    }

    public void setXmlversion(String xmlversion) {
        this.xmlversion = xmlversion;
    }

}

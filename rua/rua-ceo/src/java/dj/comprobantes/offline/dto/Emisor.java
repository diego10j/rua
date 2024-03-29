/*
 *********************************************************************
 Objetivo: Clase Emisor
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.dto;

import java.io.Serializable;

/**
 *
 * @author diego.jacome
 */
public class Emisor implements Serializable {

    private Integer codigoemisor;
    private String ruc;
    private String razonsocial;
    private String nombrecomercial;
    private String dirmatriz;
    private String contribuyenteespecial;
    private String obligadocontabilidad;
    private Integer tiempomaxespera;
    private Integer ambiente;
    private String wsdlrecepcion;
    private String wsdlautirizacion;
    private String dirsucursal;
    private String xmlversion;
    private String telefonos;
    private boolean sinFinesLucro=false;
    private String correo;
    
    private int decimalesCantidad = 2;
    private int decimalesPrecioUnitario = 2;

    public Emisor() {
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

    public String getDirsucursal() {
        return dirsucursal;
    }

    public void setDirsucursal(String dirsucursal) {
        this.dirsucursal = dirsucursal;
    }

    public String getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }

    public boolean isSinFinesLucro() {
        return sinFinesLucro;
    }

    public void setSinFinesLucro(boolean sinFinesLucro) {
        this.sinFinesLucro = sinFinesLucro;
    }

    public int getDecimalesCantidad() {
        return decimalesCantidad;
    }

    public void setDecimalesCantidad(int decimalesCantidad) {
        this.decimalesCantidad = decimalesCantidad;
    }

    public int getDecimalesPrecioUnitario() {
        return decimalesPrecioUnitario;
    }

    public void setDecimalesPrecioUnitario(int decimalesPrecioUnitario) {
        this.decimalesPrecioUnitario = decimalesPrecioUnitario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
      
}

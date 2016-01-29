/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.entidades;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author dfjacome
 */
public class Firma implements Serializable {

    private Integer codigofirma;
    private String rutafirma;
    private String clavefirma;
    private Date fechaingreso;
    private Date fechacaducidad;
    private String nombrerepresentante;
    private String correorepresentante;
    private boolean disponiblefirma;
    private InputStream archivo;

    public Firma() {
    }

    public Firma(Integer codigofirma) {
        this.codigofirma = codigofirma;
    }

    public Firma(Integer codigofirma, String rutafirma, String clavefirma, Date fechaingreso, Date fechacaducidad, boolean disponiblefirma) {
        this.codigofirma = codigofirma;
        this.rutafirma = rutafirma;
        this.clavefirma = clavefirma;
        this.fechaingreso = fechaingreso;
        this.fechacaducidad = fechacaducidad;
        this.disponiblefirma = disponiblefirma;
    }

    public Integer getCodigofirma() {
        return codigofirma;
    }

    public void setCodigofirma(Integer codigofirma) {
        this.codigofirma = codigofirma;
    }

    public String getRutafirma() {
        return rutafirma;
    }

    public void setRutafirma(String rutafirma) {
        this.rutafirma = rutafirma;
    }

    public String getClavefirma() {
        return clavefirma;
    }

    public void setClavefirma(String clavefirma) {
        this.clavefirma = clavefirma;
    }

    public Date getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(Date fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    public Date getFechacaducidad() {
        return fechacaducidad;
    }

    public void setFechacaducidad(Date fechacaducidad) {
        this.fechacaducidad = fechacaducidad;
    }

    public String getNombrerepresentante() {
        return nombrerepresentante;
    }

    public void setNombrerepresentante(String nombrerepresentante) {
        this.nombrerepresentante = nombrerepresentante;
    }

    public String getCorreorepresentante() {
        return correorepresentante;
    }

    public void setCorreorepresentante(String correorepresentante) {
        this.correorepresentante = correorepresentante;
    }

    public boolean getDisponiblefirma() {
        return disponiblefirma;
    }

    public void setDisponiblefirma(boolean disponiblefirma) {
        this.disponiblefirma = disponiblefirma;
    }

    public InputStream getArchivo() {
        return archivo;
    }

    public void setArchivo(InputStream archivo) {
        this.archivo = archivo;
    }
    

    
}

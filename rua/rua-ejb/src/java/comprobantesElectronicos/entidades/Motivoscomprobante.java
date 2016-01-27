/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.entidades;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author dfjacome
 */
public class Motivoscomprobante implements Serializable {
    
    private Long codigomotivo;
    private String razon;
    private BigDecimal valormotivo;
    private Comprobante codigocomprobante;

    public Motivoscomprobante() {
    }

    public Motivoscomprobante(Long codigomotivo) {
        this.codigomotivo = codigomotivo;
    }

    public Motivoscomprobante(Long codigomotivo, String razon, BigDecimal valormotivo) {
        this.codigomotivo = codigomotivo;
        this.razon = razon;
        this.valormotivo = valormotivo;
    }

    public Long getCodigomotivo() {
        return codigomotivo;
    }

    public void setCodigomotivo(Long codigomotivo) {
        this.codigomotivo = codigomotivo;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public BigDecimal getValormotivo() {
        return valormotivo;
    }

    public void setValormotivo(BigDecimal valormotivo) {
        this.valormotivo = valormotivo;
    }

    public Comprobante getCodigocomprobante() {
        return codigocomprobante;
    }

    public void setCodigocomprobante(Comprobante codigocomprobante) {
        this.codigocomprobante = codigocomprobante;
    }  
}

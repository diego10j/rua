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
public class Detallecomprobante implements Serializable {
    
    private Long codigodetalle;   
    private String codigoprincipal;  
    private String codigoauxiliar;   
    private String descripciondet;   
    private BigDecimal cantidad;   
    private BigDecimal preciounitario;
    private BigDecimal descuento;
    private BigDecimal preciototalsinimpuesto;
    private Comprobante codigocomprobante;

    public Detallecomprobante() {
    }

    public Detallecomprobante(Long codigodetalle) {
        this.codigodetalle = codigodetalle;
    }

    public Detallecomprobante(Long codigodetalle, String descripciondet, BigDecimal cantidad, BigDecimal preciounitario, BigDecimal descuento, BigDecimal preciototalsinimpuesto) {
        this.codigodetalle = codigodetalle;
        this.descripciondet = descripciondet;
        this.cantidad = cantidad;
        this.preciounitario = preciounitario;
        this.descuento = descuento;
        this.preciototalsinimpuesto = preciototalsinimpuesto;
    }

    public Long getCodigodetalle() {
        return codigodetalle;
    }

    public void setCodigodetalle(Long codigodetalle) {
        this.codigodetalle = codigodetalle;
    }

    public String getCodigoprincipal() {
        return codigoprincipal;
    }

    public void setCodigoprincipal(String codigoprincipal) {
        this.codigoprincipal = codigoprincipal;
    }

    public String getCodigoauxiliar() {
        return codigoauxiliar;
    }

    public void setCodigoauxiliar(String codigoauxiliar) {
        this.codigoauxiliar = codigoauxiliar;
    }

    public String getDescripciondet() {
        return descripciondet;
    }

    public void setDescripciondet(String descripciondet) {
        this.descripciondet = descripciondet;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPreciounitario() {
        return preciounitario;
    }

    public void setPreciounitario(BigDecimal preciounitario) {
        this.preciounitario = preciounitario;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getPreciototalsinimpuesto() {
        return preciototalsinimpuesto;
    }

    public void setPreciototalsinimpuesto(BigDecimal preciototalsinimpuesto) {
        this.preciototalsinimpuesto = preciototalsinimpuesto;
    }

    public Comprobante getCodigocomprobante() {
        return codigocomprobante;
    }

    public void setCodigocomprobante(Comprobante codigocomprobante) {
        this.codigocomprobante = codigocomprobante;
    }
   
}

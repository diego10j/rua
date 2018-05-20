/*
 *********************************************************************
 Objetivo: Clase DetalleComprobante
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author diego.jacome
 */
public class DetalleComprobante implements Serializable {

    private String codigoprincipal;
    private String codigoauxiliar;
    private String descripciondet;
    private BigDecimal cantidad;
    private BigDecimal preciounitario;
    private BigDecimal descuento;
    private BigDecimal preciototalsinimpuesto;
    private BigDecimal porcentajeiva;
    private Comprobante comprobante;

    public DetalleComprobante(ResultSet resultado) {
        try {
            this.codigoprincipal = resultado.getString("codigo_inarti");
            this.codigoauxiliar = resultado.getString("ide_inarti");
            this.cantidad = resultado.getBigDecimal("cantidad_ccdfa");
            this.descripciondet = resultado.getString("nombre_inarti").trim();
            this.preciounitario = resultado.getBigDecimal("precio_ccdfa");
            this.descuento = new BigDecimal("0.00");
            this.preciototalsinimpuesto = resultado.getBigDecimal("total_ccdfa");
            if (resultado.getString("iva_inarti_ccdfa").equals("1")) {
                this.porcentajeiva = resultado.getBigDecimal("tarifa_iva_cccfa");
            } else {
                this.porcentajeiva = new BigDecimal("0.00");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public Comprobante getComprobante() {
        return comprobante;
    }

    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    public BigDecimal getPorcentajeiva() {
        return porcentajeiva;
    }

    public void setPorcentajeiva(BigDecimal porcentajeiva) {
        this.porcentajeiva = porcentajeiva;
    }

}

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

import dj.comprobantes.offline.enums.TipoImpuestoIvaEnum;
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
    private String codigoPorcentaje;

    public DetalleComprobante(ResultSet resultado) {
        try {
            this.codigoprincipal = resultado.getString("ide_inarti"); //codigo_inarti
            this.codigoauxiliar = resultado.getString("ide_inarti");
            this.cantidad = resultado.getBigDecimal("cantidad_ccdfa");
            this.descripciondet = resultado.getString("nombre_inarti");
            if (this.descripciondet != null) {
                this.descripciondet = this.descripciondet.trim();
            }
            this.preciounitario = resultado.getBigDecimal("precio_ccdfa");
            if (resultado.getBigDecimal("descuento_ccdfa") == null) {
                this.descuento = new BigDecimal("0.00");
            } else {
                this.descuento = resultado.getBigDecimal("descuento_ccdfa");
            }
            this.preciototalsinimpuesto = resultado.getBigDecimal("total_ccdfa");
            if (resultado.getString("iva_inarti_ccdfa").equals("1")) {
                this.porcentajeiva = resultado.getBigDecimal("tarifa_iva_cccfa");
            } else {
                this.porcentajeiva = new BigDecimal("0.00");
            }

            //CodigoPorcentaje
            switch (resultado.getString("iva_inarti_ccdfa")) {
                case "1":  //SI
                    this.codigoPorcentaje = TipoImpuestoIvaEnum.IVA_VENTA_15.getCodigo();
                    break;
                case "-1": //NO
                    this.codigoPorcentaje = TipoImpuestoIvaEnum.IVA_VENTA_0.getCodigo();
                    break;
                case "0":  //NO OBJETO
                    this.codigoPorcentaje = TipoImpuestoIvaEnum.IVA_NO_OBJETO.getCodigo();
                    break;
                default:
                    this.codigoPorcentaje = TipoImpuestoIvaEnum.IVA_VENTA_15.getCodigo();
                    break;
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

    public String getCodigoPorcentaje() {
        return codigoPorcentaje;
    }

    public void setCodigoPorcentaje(String codigoPorcentaje) {
        this.codigoPorcentaje = codigoPorcentaje;
    }
}

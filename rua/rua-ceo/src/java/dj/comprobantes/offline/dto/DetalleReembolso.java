/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dj.comprobantes.offline.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author djacome
 */
public class DetalleReembolso implements Serializable {

    private Comprobante comprobante;
    private String tipoIdentificacionProveedorReembolso;
    private String identificacionProveedorReembolso;
    private String codPaisPagoProveedorReembolso = "593";
    private String tipoProveedorReembolso;
    private String codDocReembolso;
    private String estabDocReembolso;
    private String ptoEmiDocReembolso;
    private String secuencialDocReembolso;
    private Date fechaEmisionDocReembolso;
    private String numeroautorizacionDocReemb;

    private BigDecimal subtotal0;
    private BigDecimal subtotal;
    private BigDecimal subtotalNoObjeto;
    private BigDecimal iva;
    private BigDecimal total;

    public DetalleReembolso(ResultSet resultado) {
        try {
            this.tipoIdentificacionProveedorReembolso = resultado.getString("tipo_ident_prov_reliq");
            this.identificacionProveedorReembolso = resultado.getString("ident_prov_reliq");
            this.codPaisPagoProveedorReembolso = resultado.getString("cod_pais_prov_reliq");
            this.tipoProveedorReembolso = resultado.getString("tipo_prov_reliq");
            this.codDocReembolso = resultado.getString("cod_doc_reliq");
            this.estabDocReembolso = resultado.getString("cod_estb_reliq");
            this.ptoEmiDocReembolso = resultado.getString("cod_ptemi_reliq");
            this.secuencialDocReembolso = resultado.getString("secuencial_reliq");
            this.fechaEmisionDocReembolso = resultado.getDate("fecha_emis_reliq");
            this.numeroautorizacionDocReemb = resultado.getString("autorizacion_reliq");

            if (resultado.getBigDecimal("subtotal0_reliq") == null) {
                this.subtotal0 = new BigDecimal("0");
            } else {
                this.subtotal0 = resultado.getBigDecimal("subtotal0_reliq");
            }

            if (resultado.getBigDecimal("subtotal_no_obje_reliq") == null) {
                this.subtotalNoObjeto = new BigDecimal("0");
            } else {
                this.subtotalNoObjeto = resultado.getBigDecimal("subtotal_no_obje_reliq");
            }

            if (resultado.getBigDecimal("base_grabada_reliq") == null) {
                // this.subtotal = this.totalsinimpuestos;
                this.subtotal = new BigDecimal("0");
            } else {
                this.subtotal = resultado.getBigDecimal("base_grabada_reliq");
            }
            if (resultado.getBigDecimal("iva_reliq") == null) {
                this.iva = new BigDecimal("0");
            } else {
                this.iva = resultado.getBigDecimal("iva_reliq");
            }
            if (resultado.getBigDecimal("total_reliq") == null) {
                this.total = new BigDecimal("0");
            } else {
                this.total = resultado.getBigDecimal("total_reliq");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Comprobante getComprobante() {
        return comprobante;
    }

    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    public String getTipoIdentificacionProveedorReembolso() {
        return tipoIdentificacionProveedorReembolso;
    }

    public void setTipoIdentificacionProveedorReembolso(String tipoIdentificacionProveedorReembolso) {
        this.tipoIdentificacionProveedorReembolso = tipoIdentificacionProveedorReembolso;
    }

    public String getIdentificacionProveedorReembolso() {
        return identificacionProveedorReembolso;
    }

    public void setIdentificacionProveedorReembolso(String identificacionProveedorReembolso) {
        this.identificacionProveedorReembolso = identificacionProveedorReembolso;
    }

    public String getCodPaisPagoProveedorReembolso() {
        return codPaisPagoProveedorReembolso;
    }

    public void setCodPaisPagoProveedorReembolso(String codPaisPagoProveedorReembolso) {
        this.codPaisPagoProveedorReembolso = codPaisPagoProveedorReembolso;
    }

    public String getTipoProveedorReembolso() {
        return tipoProveedorReembolso;
    }

    public void setTipoProveedorReembolso(String tipoProveedorReembolso) {
        this.tipoProveedorReembolso = tipoProveedorReembolso;
    }

    public String getCodDocReembolso() {
        return codDocReembolso;
    }

    public void setCodDocReembolso(String codDocReembolso) {
        this.codDocReembolso = codDocReembolso;
    }

    public String getEstabDocReembolso() {
        return estabDocReembolso;
    }

    public void setEstabDocReembolso(String estabDocReembolso) {
        this.estabDocReembolso = estabDocReembolso;
    }

    public String getPtoEmiDocReembolso() {
        return ptoEmiDocReembolso;
    }

    public void setPtoEmiDocReembolso(String ptoEmiDocReembolso) {
        this.ptoEmiDocReembolso = ptoEmiDocReembolso;
    }

    public String getSecuencialDocReembolso() {
        return secuencialDocReembolso;
    }

    public void setSecuencialDocReembolso(String secuencialDocReembolso) {
        this.secuencialDocReembolso = secuencialDocReembolso;
    }

    public Date getFechaEmisionDocReembolso() {
        return fechaEmisionDocReembolso;
    }

    public void setFechaEmisionDocReembolso(Date fechaEmisionDocReembolso) {
        this.fechaEmisionDocReembolso = fechaEmisionDocReembolso;
    }

    public String getNumeroautorizacionDocReemb() {
        return numeroautorizacionDocReemb;
    }

    public void setNumeroautorizacionDocReemb(String numeroautorizacionDocReemb) {
        this.numeroautorizacionDocReemb = numeroautorizacionDocReemb;
    }

    public BigDecimal getSubtotal0() {
        return subtotal0;
    }

    public void setSubtotal0(BigDecimal subtotal0) {
        this.subtotal0 = subtotal0;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getSubtotalNoObjeto() {
        return subtotalNoObjeto;
    }

    public void setSubtotalNoObjeto(BigDecimal subtotalNoObjeto) {
        this.subtotalNoObjeto = subtotalNoObjeto;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

}

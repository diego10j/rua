/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author User
 */
@Entity
@Table(name = "nrh_retencion_carga_familia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhRetencionCargaFamilia.findAll", query = "SELECT n FROM NrhRetencionCargaFamilia n"),
    @NamedQuery(name = "NrhRetencionCargaFamilia.findByIdeNrrcf", query = "SELECT n FROM NrhRetencionCargaFamilia n WHERE n.ideNrrcf = :ideNrrcf"),
    @NamedQuery(name = "NrhRetencionCargaFamilia.findByValorDescuentoNrrcf", query = "SELECT n FROM NrhRetencionCargaFamilia n WHERE n.valorDescuentoNrrcf = :valorDescuentoNrrcf"),
    @NamedQuery(name = "NrhRetencionCargaFamilia.findByFechaEdadNrrcf", query = "SELECT n FROM NrhRetencionCargaFamilia n WHERE n.fechaEdadNrrcf = :fechaEdadNrrcf"),
    @NamedQuery(name = "NrhRetencionCargaFamilia.findByActivoNrrcf", query = "SELECT n FROM NrhRetencionCargaFamilia n WHERE n.activoNrrcf = :activoNrrcf"),
    @NamedQuery(name = "NrhRetencionCargaFamilia.findByUsuarioIngre", query = "SELECT n FROM NrhRetencionCargaFamilia n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhRetencionCargaFamilia.findByFechaIngre", query = "SELECT n FROM NrhRetencionCargaFamilia n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhRetencionCargaFamilia.findByUsuarioActua", query = "SELECT n FROM NrhRetencionCargaFamilia n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhRetencionCargaFamilia.findByFechaActua", query = "SELECT n FROM NrhRetencionCargaFamilia n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhRetencionCargaFamilia.findByHoraIngre", query = "SELECT n FROM NrhRetencionCargaFamilia n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhRetencionCargaFamilia.findByHoraActua", query = "SELECT n FROM NrhRetencionCargaFamilia n WHERE n.horaActua = :horaActua")})
public class NrhRetencionCargaFamilia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrrcf")
    private Integer ideNrrcf;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_descuento_nrrcf")
    private BigDecimal valorDescuentoNrrcf;
    @Column(name = "fecha_edad_nrrcf")
    @Temporal(TemporalType.DATE)
    private Date fechaEdadNrrcf;
    @Column(name = "activo_nrrcf")
    private Boolean activoNrrcf;
    @Column(name = "usuario_ingre")
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "usuario_actua")
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @JoinColumn(name = "ide_nrrej", referencedColumnName = "ide_nrrej")
    @ManyToOne
    private NrhRetencionJudicial ideNrrej;
    @JoinColumn(name = "ide_gtcaf", referencedColumnName = "ide_gtcaf")
    @ManyToOne
    private GthCargasFamiliares ideGtcaf;

    public NrhRetencionCargaFamilia() {
    }

    public NrhRetencionCargaFamilia(Integer ideNrrcf) {
        this.ideNrrcf = ideNrrcf;
    }

    public Integer getIdeNrrcf() {
        return ideNrrcf;
    }

    public void setIdeNrrcf(Integer ideNrrcf) {
        this.ideNrrcf = ideNrrcf;
    }

    public BigDecimal getValorDescuentoNrrcf() {
        return valorDescuentoNrrcf;
    }

    public void setValorDescuentoNrrcf(BigDecimal valorDescuentoNrrcf) {
        this.valorDescuentoNrrcf = valorDescuentoNrrcf;
    }

    public Date getFechaEdadNrrcf() {
        return fechaEdadNrrcf;
    }

    public void setFechaEdadNrrcf(Date fechaEdadNrrcf) {
        this.fechaEdadNrrcf = fechaEdadNrrcf;
    }

    public Boolean getActivoNrrcf() {
        return activoNrrcf;
    }

    public void setActivoNrrcf(Boolean activoNrrcf) {
        this.activoNrrcf = activoNrrcf;
    }

    public String getUsuarioIngre() {
        return usuarioIngre;
    }

    public void setUsuarioIngre(String usuarioIngre) {
        this.usuarioIngre = usuarioIngre;
    }

    public Date getFechaIngre() {
        return fechaIngre;
    }

    public void setFechaIngre(Date fechaIngre) {
        this.fechaIngre = fechaIngre;
    }

    public String getUsuarioActua() {
        return usuarioActua;
    }

    public void setUsuarioActua(String usuarioActua) {
        this.usuarioActua = usuarioActua;
    }

    public Date getFechaActua() {
        return fechaActua;
    }

    public void setFechaActua(Date fechaActua) {
        this.fechaActua = fechaActua;
    }

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public NrhRetencionJudicial getIdeNrrej() {
        return ideNrrej;
    }

    public void setIdeNrrej(NrhRetencionJudicial ideNrrej) {
        this.ideNrrej = ideNrrej;
    }

    public GthCargasFamiliares getIdeGtcaf() {
        return ideGtcaf;
    }

    public void setIdeGtcaf(GthCargasFamiliares ideGtcaf) {
        this.ideGtcaf = ideGtcaf;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrrcf != null ? ideNrrcf.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhRetencionCargaFamilia)) {
            return false;
        }
        NrhRetencionCargaFamilia other = (NrhRetencionCargaFamilia) object;
        if ((this.ideNrrcf == null && other.ideNrrcf != null) || (this.ideNrrcf != null && !this.ideNrrcf.equals(other.ideNrrcf))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhRetencionCargaFamilia[ ideNrrcf=" + ideNrrcf + " ]";
    }
    
}

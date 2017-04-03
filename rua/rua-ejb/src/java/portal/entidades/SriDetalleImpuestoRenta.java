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
@Table(name = "sri_detalle_impuesto_renta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SriDetalleImpuestoRenta.findAll", query = "SELECT s FROM SriDetalleImpuestoRenta s"),
    @NamedQuery(name = "SriDetalleImpuestoRenta.findByIdeSrdir", query = "SELECT s FROM SriDetalleImpuestoRenta s WHERE s.ideSrdir = :ideSrdir"),
    @NamedQuery(name = "SriDetalleImpuestoRenta.findByFraccionBasicaSrdir", query = "SELECT s FROM SriDetalleImpuestoRenta s WHERE s.fraccionBasicaSrdir = :fraccionBasicaSrdir"),
    @NamedQuery(name = "SriDetalleImpuestoRenta.findByExcesoHastaSrdir", query = "SELECT s FROM SriDetalleImpuestoRenta s WHERE s.excesoHastaSrdir = :excesoHastaSrdir"),
    @NamedQuery(name = "SriDetalleImpuestoRenta.findByImpFraccionSrdir", query = "SELECT s FROM SriDetalleImpuestoRenta s WHERE s.impFraccionSrdir = :impFraccionSrdir"),
    @NamedQuery(name = "SriDetalleImpuestoRenta.findByImpFraccionExcedenteSrdir", query = "SELECT s FROM SriDetalleImpuestoRenta s WHERE s.impFraccionExcedenteSrdir = :impFraccionExcedenteSrdir"),
    @NamedQuery(name = "SriDetalleImpuestoRenta.findByUsuarioIngre", query = "SELECT s FROM SriDetalleImpuestoRenta s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SriDetalleImpuestoRenta.findByFechaIngre", query = "SELECT s FROM SriDetalleImpuestoRenta s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SriDetalleImpuestoRenta.findByUsuarioActua", query = "SELECT s FROM SriDetalleImpuestoRenta s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SriDetalleImpuestoRenta.findByFechaActua", query = "SELECT s FROM SriDetalleImpuestoRenta s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SriDetalleImpuestoRenta.findByHoraActua", query = "SELECT s FROM SriDetalleImpuestoRenta s WHERE s.horaActua = :horaActua"),
    @NamedQuery(name = "SriDetalleImpuestoRenta.findByHoraIngre", query = "SELECT s FROM SriDetalleImpuestoRenta s WHERE s.horaIngre = :horaIngre")})
public class SriDetalleImpuestoRenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_srdir")
    private Integer ideSrdir;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "fraccion_basica_srdir")
    private BigDecimal fraccionBasicaSrdir;
    @Column(name = "exceso_hasta_srdir")
    private BigDecimal excesoHastaSrdir;
    @Column(name = "imp_fraccion_srdir")
    private BigDecimal impFraccionSrdir;
    @Column(name = "imp_fraccion_excedente_srdir")
    private BigDecimal impFraccionExcedenteSrdir;
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
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @JoinColumn(name = "ide_srimr", referencedColumnName = "ide_srimr")
    @ManyToOne
    private SriImpuestoRenta ideSrimr;

    public SriDetalleImpuestoRenta() {
    }

    public SriDetalleImpuestoRenta(Integer ideSrdir) {
        this.ideSrdir = ideSrdir;
    }

    public Integer getIdeSrdir() {
        return ideSrdir;
    }

    public void setIdeSrdir(Integer ideSrdir) {
        this.ideSrdir = ideSrdir;
    }

    public BigDecimal getFraccionBasicaSrdir() {
        return fraccionBasicaSrdir;
    }

    public void setFraccionBasicaSrdir(BigDecimal fraccionBasicaSrdir) {
        this.fraccionBasicaSrdir = fraccionBasicaSrdir;
    }

    public BigDecimal getExcesoHastaSrdir() {
        return excesoHastaSrdir;
    }

    public void setExcesoHastaSrdir(BigDecimal excesoHastaSrdir) {
        this.excesoHastaSrdir = excesoHastaSrdir;
    }

    public BigDecimal getImpFraccionSrdir() {
        return impFraccionSrdir;
    }

    public void setImpFraccionSrdir(BigDecimal impFraccionSrdir) {
        this.impFraccionSrdir = impFraccionSrdir;
    }

    public BigDecimal getImpFraccionExcedenteSrdir() {
        return impFraccionExcedenteSrdir;
    }

    public void setImpFraccionExcedenteSrdir(BigDecimal impFraccionExcedenteSrdir) {
        this.impFraccionExcedenteSrdir = impFraccionExcedenteSrdir;
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

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public SriImpuestoRenta getIdeSrimr() {
        return ideSrimr;
    }

    public void setIdeSrimr(SriImpuestoRenta ideSrimr) {
        this.ideSrimr = ideSrimr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSrdir != null ? ideSrdir.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SriDetalleImpuestoRenta)) {
            return false;
        }
        SriDetalleImpuestoRenta other = (SriDetalleImpuestoRenta) object;
        if ((this.ideSrdir == null && other.ideSrdir != null) || (this.ideSrdir != null && !this.ideSrdir.equals(other.ideSrdir))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SriDetalleImpuestoRenta[ ideSrdir=" + ideSrdir + " ]";
    }
    
}

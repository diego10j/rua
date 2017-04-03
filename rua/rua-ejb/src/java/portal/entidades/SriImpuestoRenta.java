/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author User
 */
@Entity
@Table(name = "sri_impuesto_renta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SriImpuestoRenta.findAll", query = "SELECT s FROM SriImpuestoRenta s"),
    @NamedQuery(name = "SriImpuestoRenta.findByIdeSrimr", query = "SELECT s FROM SriImpuestoRenta s WHERE s.ideSrimr = :ideSrimr"),
    @NamedQuery(name = "SriImpuestoRenta.findByDetalleSrimr", query = "SELECT s FROM SriImpuestoRenta s WHERE s.detalleSrimr = :detalleSrimr"),
    @NamedQuery(name = "SriImpuestoRenta.findByFechaInicioSrimr", query = "SELECT s FROM SriImpuestoRenta s WHERE s.fechaInicioSrimr = :fechaInicioSrimr"),
    @NamedQuery(name = "SriImpuestoRenta.findByFechaFinSrimr", query = "SELECT s FROM SriImpuestoRenta s WHERE s.fechaFinSrimr = :fechaFinSrimr"),
    @NamedQuery(name = "SriImpuestoRenta.findByActivoSrimr", query = "SELECT s FROM SriImpuestoRenta s WHERE s.activoSrimr = :activoSrimr"),
    @NamedQuery(name = "SriImpuestoRenta.findByUsuarioIngre", query = "SELECT s FROM SriImpuestoRenta s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SriImpuestoRenta.findByFechaIngre", query = "SELECT s FROM SriImpuestoRenta s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SriImpuestoRenta.findByUsuarioActua", query = "SELECT s FROM SriImpuestoRenta s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SriImpuestoRenta.findByFechaActua", query = "SELECT s FROM SriImpuestoRenta s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SriImpuestoRenta.findByHoraIngre", query = "SELECT s FROM SriImpuestoRenta s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SriImpuestoRenta.findByHoraActua", query = "SELECT s FROM SriImpuestoRenta s WHERE s.horaActua = :horaActua")})
public class SriImpuestoRenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_srimr")
    private Integer ideSrimr;
    @Column(name = "detalle_srimr")
    private String detalleSrimr;
    @Column(name = "fecha_inicio_srimr")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioSrimr;
    @Column(name = "fecha_fin_srimr")
    @Temporal(TemporalType.DATE)
    private Date fechaFinSrimr;
    @Column(name = "activo_srimr")
    private Boolean activoSrimr;
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
    @OneToMany(mappedBy = "ideSrimr")
    private Collection<SriProyeccionIngres> sriProyeccionIngresCollection;
    @OneToMany(mappedBy = "ideSrimr")
    private Collection<SriDetalleImpuestoRenta> sriDetalleImpuestoRentaCollection;
    @OneToMany(mappedBy = "ideSrimr")
    private Collection<SriDeducibles> sriDeduciblesCollection;

    public SriImpuestoRenta() {
    }

    public SriImpuestoRenta(Integer ideSrimr) {
        this.ideSrimr = ideSrimr;
    }

    public Integer getIdeSrimr() {
        return ideSrimr;
    }

    public void setIdeSrimr(Integer ideSrimr) {
        this.ideSrimr = ideSrimr;
    }

    public String getDetalleSrimr() {
        return detalleSrimr;
    }

    public void setDetalleSrimr(String detalleSrimr) {
        this.detalleSrimr = detalleSrimr;
    }

    public Date getFechaInicioSrimr() {
        return fechaInicioSrimr;
    }

    public void setFechaInicioSrimr(Date fechaInicioSrimr) {
        this.fechaInicioSrimr = fechaInicioSrimr;
    }

    public Date getFechaFinSrimr() {
        return fechaFinSrimr;
    }

    public void setFechaFinSrimr(Date fechaFinSrimr) {
        this.fechaFinSrimr = fechaFinSrimr;
    }

    public Boolean getActivoSrimr() {
        return activoSrimr;
    }

    public void setActivoSrimr(Boolean activoSrimr) {
        this.activoSrimr = activoSrimr;
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

    @XmlTransient
    public Collection<SriProyeccionIngres> getSriProyeccionIngresCollection() {
        return sriProyeccionIngresCollection;
    }

    public void setSriProyeccionIngresCollection(Collection<SriProyeccionIngres> sriProyeccionIngresCollection) {
        this.sriProyeccionIngresCollection = sriProyeccionIngresCollection;
    }

    @XmlTransient
    public Collection<SriDetalleImpuestoRenta> getSriDetalleImpuestoRentaCollection() {
        return sriDetalleImpuestoRentaCollection;
    }

    public void setSriDetalleImpuestoRentaCollection(Collection<SriDetalleImpuestoRenta> sriDetalleImpuestoRentaCollection) {
        this.sriDetalleImpuestoRentaCollection = sriDetalleImpuestoRentaCollection;
    }

    @XmlTransient
    public Collection<SriDeducibles> getSriDeduciblesCollection() {
        return sriDeduciblesCollection;
    }

    public void setSriDeduciblesCollection(Collection<SriDeducibles> sriDeduciblesCollection) {
        this.sriDeduciblesCollection = sriDeduciblesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSrimr != null ? ideSrimr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SriImpuestoRenta)) {
            return false;
        }
        SriImpuestoRenta other = (SriImpuestoRenta) object;
        if ((this.ideSrimr == null && other.ideSrimr != null) || (this.ideSrimr != null && !this.ideSrimr.equals(other.ideSrimr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SriImpuestoRenta[ ideSrimr=" + ideSrimr + " ]";
    }
    
}

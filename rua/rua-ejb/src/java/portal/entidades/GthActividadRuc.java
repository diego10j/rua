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
@Table(name = "gth_actividad_ruc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthActividadRuc.findAll", query = "SELECT g FROM GthActividadRuc g"),
    @NamedQuery(name = "GthActividadRuc.findByIdeGtacr", query = "SELECT g FROM GthActividadRuc g WHERE g.ideGtacr = :ideGtacr"),
    @NamedQuery(name = "GthActividadRuc.findByDetalleGtacr", query = "SELECT g FROM GthActividadRuc g WHERE g.detalleGtacr = :detalleGtacr"),
    @NamedQuery(name = "GthActividadRuc.findByActivoGtacr", query = "SELECT g FROM GthActividadRuc g WHERE g.activoGtacr = :activoGtacr"),
    @NamedQuery(name = "GthActividadRuc.findByUsuarioIngre", query = "SELECT g FROM GthActividadRuc g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthActividadRuc.findByFechaIngre", query = "SELECT g FROM GthActividadRuc g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthActividadRuc.findByUsuarioActua", query = "SELECT g FROM GthActividadRuc g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthActividadRuc.findByFechaActua", query = "SELECT g FROM GthActividadRuc g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthActividadRuc.findByHoraIngre", query = "SELECT g FROM GthActividadRuc g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthActividadRuc.findByHoraActua", query = "SELECT g FROM GthActividadRuc g WHERE g.horaActua = :horaActua")})
public class GthActividadRuc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtacr")
    private Integer ideGtacr;
    @Column(name = "detalle_gtacr")
    private String detalleGtacr;
    @Column(name = "activo_gtacr")
    private Boolean activoGtacr;
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
    @OneToMany(mappedBy = "ideGtacr")
    private Collection<GthNegocioEmpleado> gthNegocioEmpleadoCollection;

    public GthActividadRuc() {
    }

    public GthActividadRuc(Integer ideGtacr) {
        this.ideGtacr = ideGtacr;
    }

    public Integer getIdeGtacr() {
        return ideGtacr;
    }

    public void setIdeGtacr(Integer ideGtacr) {
        this.ideGtacr = ideGtacr;
    }

    public String getDetalleGtacr() {
        return detalleGtacr;
    }

    public void setDetalleGtacr(String detalleGtacr) {
        this.detalleGtacr = detalleGtacr;
    }

    public Boolean getActivoGtacr() {
        return activoGtacr;
    }

    public void setActivoGtacr(Boolean activoGtacr) {
        this.activoGtacr = activoGtacr;
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
    public Collection<GthNegocioEmpleado> getGthNegocioEmpleadoCollection() {
        return gthNegocioEmpleadoCollection;
    }

    public void setGthNegocioEmpleadoCollection(Collection<GthNegocioEmpleado> gthNegocioEmpleadoCollection) {
        this.gthNegocioEmpleadoCollection = gthNegocioEmpleadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtacr != null ? ideGtacr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthActividadRuc)) {
            return false;
        }
        GthActividadRuc other = (GthActividadRuc) object;
        if ((this.ideGtacr == null && other.ideGtacr != null) || (this.ideGtacr != null && !this.ideGtacr.equals(other.ideGtacr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthActividadRuc[ ideGtacr=" + ideGtacr + " ]";
    }
    
}

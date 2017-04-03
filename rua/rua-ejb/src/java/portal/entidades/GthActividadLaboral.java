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
@Table(name = "gth_actividad_laboral")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthActividadLaboral.findAll", query = "SELECT g FROM GthActividadLaboral g"),
    @NamedQuery(name = "GthActividadLaboral.findByIdeGtacl", query = "SELECT g FROM GthActividadLaboral g WHERE g.ideGtacl = :ideGtacl"),
    @NamedQuery(name = "GthActividadLaboral.findByDetalleGtacl", query = "SELECT g FROM GthActividadLaboral g WHERE g.detalleGtacl = :detalleGtacl"),
    @NamedQuery(name = "GthActividadLaboral.findByActivoGtacl", query = "SELECT g FROM GthActividadLaboral g WHERE g.activoGtacl = :activoGtacl"),
    @NamedQuery(name = "GthActividadLaboral.findByUsuarioIngre", query = "SELECT g FROM GthActividadLaboral g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthActividadLaboral.findByFechaIngre", query = "SELECT g FROM GthActividadLaboral g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthActividadLaboral.findByUsuarioActua", query = "SELECT g FROM GthActividadLaboral g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthActividadLaboral.findByFechaActua", query = "SELECT g FROM GthActividadLaboral g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthActividadLaboral.findByHoraIngre", query = "SELECT g FROM GthActividadLaboral g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthActividadLaboral.findByHoraActua", query = "SELECT g FROM GthActividadLaboral g WHERE g.horaActua = :horaActua")})
public class GthActividadLaboral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtacl")
    private Integer ideGtacl;
    @Column(name = "detalle_gtacl")
    private String detalleGtacl;
    @Column(name = "activo_gtacl")
    private Boolean activoGtacl;
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
    @OneToMany(mappedBy = "ideGtacl")
    private Collection<GthFamiliar> gthFamiliarCollection;

    public GthActividadLaboral() {
    }

    public GthActividadLaboral(Integer ideGtacl) {
        this.ideGtacl = ideGtacl;
    }

    public Integer getIdeGtacl() {
        return ideGtacl;
    }

    public void setIdeGtacl(Integer ideGtacl) {
        this.ideGtacl = ideGtacl;
    }

    public String getDetalleGtacl() {
        return detalleGtacl;
    }

    public void setDetalleGtacl(String detalleGtacl) {
        this.detalleGtacl = detalleGtacl;
    }

    public Boolean getActivoGtacl() {
        return activoGtacl;
    }

    public void setActivoGtacl(Boolean activoGtacl) {
        this.activoGtacl = activoGtacl;
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
    public Collection<GthFamiliar> getGthFamiliarCollection() {
        return gthFamiliarCollection;
    }

    public void setGthFamiliarCollection(Collection<GthFamiliar> gthFamiliarCollection) {
        this.gthFamiliarCollection = gthFamiliarCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtacl != null ? ideGtacl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthActividadLaboral)) {
            return false;
        }
        GthActividadLaboral other = (GthActividadLaboral) object;
        if ((this.ideGtacl == null && other.ideGtacl != null) || (this.ideGtacl != null && !this.ideGtacl.equals(other.ideGtacl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthActividadLaboral[ ideGtacl=" + ideGtacl + " ]";
    }
    
}

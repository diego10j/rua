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
@Table(name = "gth_tipo_endeudamiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTipoEndeudamiento.findAll", query = "SELECT g FROM GthTipoEndeudamiento g"),
    @NamedQuery(name = "GthTipoEndeudamiento.findByIdeGtten", query = "SELECT g FROM GthTipoEndeudamiento g WHERE g.ideGtten = :ideGtten"),
    @NamedQuery(name = "GthTipoEndeudamiento.findByDetalleGtten", query = "SELECT g FROM GthTipoEndeudamiento g WHERE g.detalleGtten = :detalleGtten"),
    @NamedQuery(name = "GthTipoEndeudamiento.findByActivoGtten", query = "SELECT g FROM GthTipoEndeudamiento g WHERE g.activoGtten = :activoGtten"),
    @NamedQuery(name = "GthTipoEndeudamiento.findByUsuarioIngre", query = "SELECT g FROM GthTipoEndeudamiento g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoEndeudamiento.findByFechaIngre", query = "SELECT g FROM GthTipoEndeudamiento g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoEndeudamiento.findByUsuarioActua", query = "SELECT g FROM GthTipoEndeudamiento g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoEndeudamiento.findByFechaActua", query = "SELECT g FROM GthTipoEndeudamiento g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoEndeudamiento.findByHoraIngre", query = "SELECT g FROM GthTipoEndeudamiento g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoEndeudamiento.findByHoraActua", query = "SELECT g FROM GthTipoEndeudamiento g WHERE g.horaActua = :horaActua")})
public class GthTipoEndeudamiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtten")
    private Integer ideGtten;
    @Column(name = "detalle_gtten")
    private String detalleGtten;
    @Column(name = "activo_gtten")
    private Boolean activoGtten;
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
    @OneToMany(mappedBy = "ideGtten")
    private Collection<GthEndeudamientoEmpleado> gthEndeudamientoEmpleadoCollection;

    public GthTipoEndeudamiento() {
    }

    public GthTipoEndeudamiento(Integer ideGtten) {
        this.ideGtten = ideGtten;
    }

    public Integer getIdeGtten() {
        return ideGtten;
    }

    public void setIdeGtten(Integer ideGtten) {
        this.ideGtten = ideGtten;
    }

    public String getDetalleGtten() {
        return detalleGtten;
    }

    public void setDetalleGtten(String detalleGtten) {
        this.detalleGtten = detalleGtten;
    }

    public Boolean getActivoGtten() {
        return activoGtten;
    }

    public void setActivoGtten(Boolean activoGtten) {
        this.activoGtten = activoGtten;
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
    public Collection<GthEndeudamientoEmpleado> getGthEndeudamientoEmpleadoCollection() {
        return gthEndeudamientoEmpleadoCollection;
    }

    public void setGthEndeudamientoEmpleadoCollection(Collection<GthEndeudamientoEmpleado> gthEndeudamientoEmpleadoCollection) {
        this.gthEndeudamientoEmpleadoCollection = gthEndeudamientoEmpleadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtten != null ? ideGtten.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoEndeudamiento)) {
            return false;
        }
        GthTipoEndeudamiento other = (GthTipoEndeudamiento) object;
        if ((this.ideGtten == null && other.ideGtten != null) || (this.ideGtten != null && !this.ideGtten.equals(other.ideGtten))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoEndeudamiento[ ideGtten=" + ideGtten + " ]";
    }
    
}

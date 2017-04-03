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
@Table(name = "gth_grado_discapacidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthGradoDiscapacidad.findAll", query = "SELECT g FROM GthGradoDiscapacidad g"),
    @NamedQuery(name = "GthGradoDiscapacidad.findByIdeGtgrd", query = "SELECT g FROM GthGradoDiscapacidad g WHERE g.ideGtgrd = :ideGtgrd"),
    @NamedQuery(name = "GthGradoDiscapacidad.findByDetalleGtgrd", query = "SELECT g FROM GthGradoDiscapacidad g WHERE g.detalleGtgrd = :detalleGtgrd"),
    @NamedQuery(name = "GthGradoDiscapacidad.findByActivoGtgrd", query = "SELECT g FROM GthGradoDiscapacidad g WHERE g.activoGtgrd = :activoGtgrd"),
    @NamedQuery(name = "GthGradoDiscapacidad.findByUsuarioIngre", query = "SELECT g FROM GthGradoDiscapacidad g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthGradoDiscapacidad.findByFechaIngre", query = "SELECT g FROM GthGradoDiscapacidad g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthGradoDiscapacidad.findByUsuarioActua", query = "SELECT g FROM GthGradoDiscapacidad g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthGradoDiscapacidad.findByFechaActua", query = "SELECT g FROM GthGradoDiscapacidad g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthGradoDiscapacidad.findByHoraIngre", query = "SELECT g FROM GthGradoDiscapacidad g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthGradoDiscapacidad.findByHoraActua", query = "SELECT g FROM GthGradoDiscapacidad g WHERE g.horaActua = :horaActua")})
public class GthGradoDiscapacidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtgrd")
    private Integer ideGtgrd;
    @Column(name = "detalle_gtgrd")
    private String detalleGtgrd;
    @Column(name = "activo_gtgrd")
    private Boolean activoGtgrd;
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
    @OneToMany(mappedBy = "ideGtgrd")
    private Collection<GthDiscapacidadEmpleado> gthDiscapacidadEmpleadoCollection;

    public GthGradoDiscapacidad() {
    }

    public GthGradoDiscapacidad(Integer ideGtgrd) {
        this.ideGtgrd = ideGtgrd;
    }

    public Integer getIdeGtgrd() {
        return ideGtgrd;
    }

    public void setIdeGtgrd(Integer ideGtgrd) {
        this.ideGtgrd = ideGtgrd;
    }

    public String getDetalleGtgrd() {
        return detalleGtgrd;
    }

    public void setDetalleGtgrd(String detalleGtgrd) {
        this.detalleGtgrd = detalleGtgrd;
    }

    public Boolean getActivoGtgrd() {
        return activoGtgrd;
    }

    public void setActivoGtgrd(Boolean activoGtgrd) {
        this.activoGtgrd = activoGtgrd;
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
    public Collection<GthDiscapacidadEmpleado> getGthDiscapacidadEmpleadoCollection() {
        return gthDiscapacidadEmpleadoCollection;
    }

    public void setGthDiscapacidadEmpleadoCollection(Collection<GthDiscapacidadEmpleado> gthDiscapacidadEmpleadoCollection) {
        this.gthDiscapacidadEmpleadoCollection = gthDiscapacidadEmpleadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtgrd != null ? ideGtgrd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthGradoDiscapacidad)) {
            return false;
        }
        GthGradoDiscapacidad other = (GthGradoDiscapacidad) object;
        if ((this.ideGtgrd == null && other.ideGtgrd != null) || (this.ideGtgrd != null && !this.ideGtgrd.equals(other.ideGtgrd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthGradoDiscapacidad[ ideGtgrd=" + ideGtgrd + " ]";
    }
    
}

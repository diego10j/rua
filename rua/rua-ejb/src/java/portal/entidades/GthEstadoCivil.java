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
import javax.persistence.CascadeType;
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
@Table(name = "gth_estado_civil")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthEstadoCivil.findAll", query = "SELECT g FROM GthEstadoCivil g"),
    @NamedQuery(name = "GthEstadoCivil.findByIdeGtesc", query = "SELECT g FROM GthEstadoCivil g WHERE g.ideGtesc = :ideGtesc"),
    @NamedQuery(name = "GthEstadoCivil.findByDetalleGtesc", query = "SELECT g FROM GthEstadoCivil g WHERE g.detalleGtesc = :detalleGtesc"),
    @NamedQuery(name = "GthEstadoCivil.findByActivoGtesc", query = "SELECT g FROM GthEstadoCivil g WHERE g.activoGtesc = :activoGtesc"),
    @NamedQuery(name = "GthEstadoCivil.findByUsuarioIngre", query = "SELECT g FROM GthEstadoCivil g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthEstadoCivil.findByFechaIngre", query = "SELECT g FROM GthEstadoCivil g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthEstadoCivil.findByUsuarioActua", query = "SELECT g FROM GthEstadoCivil g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthEstadoCivil.findByFechaActua", query = "SELECT g FROM GthEstadoCivil g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthEstadoCivil.findByHoraIngre", query = "SELECT g FROM GthEstadoCivil g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthEstadoCivil.findByHoraActua", query = "SELECT g FROM GthEstadoCivil g WHERE g.horaActua = :horaActua")})
public class GthEstadoCivil implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtesc")
    private Integer ideGtesc;
    @Column(name = "detalle_gtesc")
    private String detalleGtesc;
    @Column(name = "activo_gtesc")
    private Boolean activoGtesc;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtesc")
    private Collection<GthEmpleado> gthEmpleadoCollection;

    public GthEstadoCivil() {
    }

    public GthEstadoCivil(Integer ideGtesc) {
        this.ideGtesc = ideGtesc;
    }

    public Integer getIdeGtesc() {
        return ideGtesc;
    }

    public void setIdeGtesc(Integer ideGtesc) {
        this.ideGtesc = ideGtesc;
    }

    public String getDetalleGtesc() {
        return detalleGtesc;
    }

    public void setDetalleGtesc(String detalleGtesc) {
        this.detalleGtesc = detalleGtesc;
    }

    public Boolean getActivoGtesc() {
        return activoGtesc;
    }

    public void setActivoGtesc(Boolean activoGtesc) {
        this.activoGtesc = activoGtesc;
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
    public Collection<GthEmpleado> getGthEmpleadoCollection() {
        return gthEmpleadoCollection;
    }

    public void setGthEmpleadoCollection(Collection<GthEmpleado> gthEmpleadoCollection) {
        this.gthEmpleadoCollection = gthEmpleadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtesc != null ? ideGtesc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthEstadoCivil)) {
            return false;
        }
        GthEstadoCivil other = (GthEstadoCivil) object;
        if ((this.ideGtesc == null && other.ideGtesc != null) || (this.ideGtesc != null && !this.ideGtesc.equals(other.ideGtesc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthEstadoCivil[ ideGtesc=" + ideGtesc + " ]";
    }
    
}

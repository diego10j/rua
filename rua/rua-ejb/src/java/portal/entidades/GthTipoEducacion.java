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
@Table(name = "gth_tipo_educacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTipoEducacion.findAll", query = "SELECT g FROM GthTipoEducacion g"),
    @NamedQuery(name = "GthTipoEducacion.findByIdeGtted", query = "SELECT g FROM GthTipoEducacion g WHERE g.ideGtted = :ideGtted"),
    @NamedQuery(name = "GthTipoEducacion.findByDetalleGtted", query = "SELECT g FROM GthTipoEducacion g WHERE g.detalleGtted = :detalleGtted"),
    @NamedQuery(name = "GthTipoEducacion.findByActivoGtted", query = "SELECT g FROM GthTipoEducacion g WHERE g.activoGtted = :activoGtted"),
    @NamedQuery(name = "GthTipoEducacion.findByUsuarioIngre", query = "SELECT g FROM GthTipoEducacion g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoEducacion.findByFechaIngre", query = "SELECT g FROM GthTipoEducacion g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoEducacion.findByUsuarioActua", query = "SELECT g FROM GthTipoEducacion g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoEducacion.findByFechaActua", query = "SELECT g FROM GthTipoEducacion g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoEducacion.findByHoraIngre", query = "SELECT g FROM GthTipoEducacion g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoEducacion.findByHoraActua", query = "SELECT g FROM GthTipoEducacion g WHERE g.horaActua = :horaActua")})
public class GthTipoEducacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtted")
    private Integer ideGtted;
    @Column(name = "detalle_gtted")
    private String detalleGtted;
    @Column(name = "activo_gtted")
    private Boolean activoGtted;
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
    @OneToMany(mappedBy = "ideGtted")
    private Collection<GthEducacionEmpleado> gthEducacionEmpleadoCollection;

    public GthTipoEducacion() {
    }

    public GthTipoEducacion(Integer ideGtted) {
        this.ideGtted = ideGtted;
    }

    public Integer getIdeGtted() {
        return ideGtted;
    }

    public void setIdeGtted(Integer ideGtted) {
        this.ideGtted = ideGtted;
    }

    public String getDetalleGtted() {
        return detalleGtted;
    }

    public void setDetalleGtted(String detalleGtted) {
        this.detalleGtted = detalleGtted;
    }

    public Boolean getActivoGtted() {
        return activoGtted;
    }

    public void setActivoGtted(Boolean activoGtted) {
        this.activoGtted = activoGtted;
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
    public Collection<GthEducacionEmpleado> getGthEducacionEmpleadoCollection() {
        return gthEducacionEmpleadoCollection;
    }

    public void setGthEducacionEmpleadoCollection(Collection<GthEducacionEmpleado> gthEducacionEmpleadoCollection) {
        this.gthEducacionEmpleadoCollection = gthEducacionEmpleadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtted != null ? ideGtted.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoEducacion)) {
            return false;
        }
        GthTipoEducacion other = (GthTipoEducacion) object;
        if ((this.ideGtted == null && other.ideGtted != null) || (this.ideGtted != null && !this.ideGtted.equals(other.ideGtted))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoEducacion[ ideGtted=" + ideGtted + " ]";
    }
    
}

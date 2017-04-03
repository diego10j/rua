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
@Table(name = "asi_estado_vacacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsiEstadoVacacion.findAll", query = "SELECT a FROM AsiEstadoVacacion a"),
    @NamedQuery(name = "AsiEstadoVacacion.findByIdeAsesv", query = "SELECT a FROM AsiEstadoVacacion a WHERE a.ideAsesv = :ideAsesv"),
    @NamedQuery(name = "AsiEstadoVacacion.findByDetalleAsesv", query = "SELECT a FROM AsiEstadoVacacion a WHERE a.detalleAsesv = :detalleAsesv"),
    @NamedQuery(name = "AsiEstadoVacacion.findByActivoAsesv", query = "SELECT a FROM AsiEstadoVacacion a WHERE a.activoAsesv = :activoAsesv"),
    @NamedQuery(name = "AsiEstadoVacacion.findByUsuarioIngre", query = "SELECT a FROM AsiEstadoVacacion a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiEstadoVacacion.findByFechaIngre", query = "SELECT a FROM AsiEstadoVacacion a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiEstadoVacacion.findByUsuarioActua", query = "SELECT a FROM AsiEstadoVacacion a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiEstadoVacacion.findByFechaActua", query = "SELECT a FROM AsiEstadoVacacion a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiEstadoVacacion.findByHoraActua", query = "SELECT a FROM AsiEstadoVacacion a WHERE a.horaActua = :horaActua"),
    @NamedQuery(name = "AsiEstadoVacacion.findByHoraIngre", query = "SELECT a FROM AsiEstadoVacacion a WHERE a.horaIngre = :horaIngre")})
public class AsiEstadoVacacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_asesv")
    private Integer ideAsesv;
    @Column(name = "detalle_asesv")
    private String detalleAsesv;
    @Column(name = "activo_asesv")
    private Boolean activoAsesv;
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
    @OneToMany(mappedBy = "ideAsesv")
    private Collection<AsiDetalleVacacion> asiDetalleVacacionCollection;

    public AsiEstadoVacacion() {
    }

    public AsiEstadoVacacion(Integer ideAsesv) {
        this.ideAsesv = ideAsesv;
    }

    public Integer getIdeAsesv() {
        return ideAsesv;
    }

    public void setIdeAsesv(Integer ideAsesv) {
        this.ideAsesv = ideAsesv;
    }

    public String getDetalleAsesv() {
        return detalleAsesv;
    }

    public void setDetalleAsesv(String detalleAsesv) {
        this.detalleAsesv = detalleAsesv;
    }

    public Boolean getActivoAsesv() {
        return activoAsesv;
    }

    public void setActivoAsesv(Boolean activoAsesv) {
        this.activoAsesv = activoAsesv;
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

    @XmlTransient
    public Collection<AsiDetalleVacacion> getAsiDetalleVacacionCollection() {
        return asiDetalleVacacionCollection;
    }

    public void setAsiDetalleVacacionCollection(Collection<AsiDetalleVacacion> asiDetalleVacacionCollection) {
        this.asiDetalleVacacionCollection = asiDetalleVacacionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAsesv != null ? ideAsesv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiEstadoVacacion)) {
            return false;
        }
        AsiEstadoVacacion other = (AsiEstadoVacacion) object;
        if ((this.ideAsesv == null && other.ideAsesv != null) || (this.ideAsesv != null && !this.ideAsesv.equals(other.ideAsesv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiEstadoVacacion[ ideAsesv=" + ideAsesv + " ]";
    }
    
}

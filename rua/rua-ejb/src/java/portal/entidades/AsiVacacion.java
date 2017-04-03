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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "asi_vacacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsiVacacion.findAll", query = "SELECT a FROM AsiVacacion a"),
    @NamedQuery(name = "AsiVacacion.findByIdeAsvac", query = "SELECT a FROM AsiVacacion a WHERE a.ideAsvac = :ideAsvac"),
    @NamedQuery(name = "AsiVacacion.findByFechaIngresoAsvac", query = "SELECT a FROM AsiVacacion a WHERE a.fechaIngresoAsvac = :fechaIngresoAsvac"),
    @NamedQuery(name = "AsiVacacion.findByFechaFiniquitoAsvac", query = "SELECT a FROM AsiVacacion a WHERE a.fechaFiniquitoAsvac = :fechaFiniquitoAsvac"),
    @NamedQuery(name = "AsiVacacion.findByObervacionAsvac", query = "SELECT a FROM AsiVacacion a WHERE a.obervacionAsvac = :obervacionAsvac"),
    @NamedQuery(name = "AsiVacacion.findByActivoAsvac", query = "SELECT a FROM AsiVacacion a WHERE a.activoAsvac = :activoAsvac"),
    @NamedQuery(name = "AsiVacacion.findByUsuarioIngre", query = "SELECT a FROM AsiVacacion a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiVacacion.findByFechaIngre", query = "SELECT a FROM AsiVacacion a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiVacacion.findByUsuarioActua", query = "SELECT a FROM AsiVacacion a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiVacacion.findByFechaActua", query = "SELECT a FROM AsiVacacion a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiVacacion.findByHoraIngre", query = "SELECT a FROM AsiVacacion a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AsiVacacion.findByHoraActua", query = "SELECT a FROM AsiVacacion a WHERE a.horaActua = :horaActua")})
public class AsiVacacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_asvac")
    private Integer ideAsvac;
    @Column(name = "fecha_ingreso_asvac")
    @Temporal(TemporalType.DATE)
    private Date fechaIngresoAsvac;
    @Column(name = "fecha_finiquito_asvac")
    @Temporal(TemporalType.DATE)
    private Date fechaFiniquitoAsvac;
    @Column(name = "obervacion_asvac")
    private String obervacionAsvac;
    @Column(name = "activo_asvac")
    private Boolean activoAsvac;
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
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @OneToMany(mappedBy = "ideAsvac")
    private Collection<AsiDetalleVacacion> asiDetalleVacacionCollection;

    public AsiVacacion() {
    }

    public AsiVacacion(Integer ideAsvac) {
        this.ideAsvac = ideAsvac;
    }

    public Integer getIdeAsvac() {
        return ideAsvac;
    }

    public void setIdeAsvac(Integer ideAsvac) {
        this.ideAsvac = ideAsvac;
    }

    public Date getFechaIngresoAsvac() {
        return fechaIngresoAsvac;
    }

    public void setFechaIngresoAsvac(Date fechaIngresoAsvac) {
        this.fechaIngresoAsvac = fechaIngresoAsvac;
    }

    public Date getFechaFiniquitoAsvac() {
        return fechaFiniquitoAsvac;
    }

    public void setFechaFiniquitoAsvac(Date fechaFiniquitoAsvac) {
        this.fechaFiniquitoAsvac = fechaFiniquitoAsvac;
    }

    public String getObervacionAsvac() {
        return obervacionAsvac;
    }

    public void setObervacionAsvac(String obervacionAsvac) {
        this.obervacionAsvac = obervacionAsvac;
    }

    public Boolean getActivoAsvac() {
        return activoAsvac;
    }

    public void setActivoAsvac(Boolean activoAsvac) {
        this.activoAsvac = activoAsvac;
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

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
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
        hash += (ideAsvac != null ? ideAsvac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiVacacion)) {
            return false;
        }
        AsiVacacion other = (AsiVacacion) object;
        if ((this.ideAsvac == null && other.ideAsvac != null) || (this.ideAsvac != null && !this.ideAsvac.equals(other.ideAsvac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiVacacion[ ideAsvac=" + ideAsvac + " ]";
    }
    
}

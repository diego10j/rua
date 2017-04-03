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
@Table(name = "gen_tipo_periodo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenTipoPeriodo.findAll", query = "SELECT g FROM GenTipoPeriodo g"),
    @NamedQuery(name = "GenTipoPeriodo.findByIdeGetpr", query = "SELECT g FROM GenTipoPeriodo g WHERE g.ideGetpr = :ideGetpr"),
    @NamedQuery(name = "GenTipoPeriodo.findByDetalleGetpr", query = "SELECT g FROM GenTipoPeriodo g WHERE g.detalleGetpr = :detalleGetpr"),
    @NamedQuery(name = "GenTipoPeriodo.findByActivoGetpr", query = "SELECT g FROM GenTipoPeriodo g WHERE g.activoGetpr = :activoGetpr"),
    @NamedQuery(name = "GenTipoPeriodo.findByUsuarioIngre", query = "SELECT g FROM GenTipoPeriodo g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenTipoPeriodo.findByFechaIngre", query = "SELECT g FROM GenTipoPeriodo g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenTipoPeriodo.findByUsuarioActua", query = "SELECT g FROM GenTipoPeriodo g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenTipoPeriodo.findByFechaActua", query = "SELECT g FROM GenTipoPeriodo g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenTipoPeriodo.findByHoraIngre", query = "SELECT g FROM GenTipoPeriodo g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenTipoPeriodo.findByHoraActua", query = "SELECT g FROM GenTipoPeriodo g WHERE g.horaActua = :horaActua")})
public class GenTipoPeriodo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_getpr")
    private Integer ideGetpr;
    @Column(name = "detalle_getpr")
    private String detalleGetpr;
    @Column(name = "activo_getpr")
    private Boolean activoGetpr;
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
    @OneToMany(mappedBy = "ideGetpr")
    private Collection<GthEndeudamientoEmpleado> gthEndeudamientoEmpleadoCollection;
    @OneToMany(mappedBy = "ideGetpr")
    private Collection<GthIdiomaEmpleado> gthIdiomaEmpleadoCollection;
    @OneToMany(mappedBy = "ideGetpr")
    private Collection<GthCapacitacionEmpleado> gthCapacitacionEmpleadoCollection;
    @OneToMany(mappedBy = "ideGetpr")
    private Collection<AsiMotivo> asiMotivoCollection;
    @OneToMany(mappedBy = "ideGetpr")
    private Collection<GthInversionEmpleado> gthInversionEmpleadoCollection;

    public GenTipoPeriodo() {
    }

    public GenTipoPeriodo(Integer ideGetpr) {
        this.ideGetpr = ideGetpr;
    }

    public Integer getIdeGetpr() {
        return ideGetpr;
    }

    public void setIdeGetpr(Integer ideGetpr) {
        this.ideGetpr = ideGetpr;
    }

    public String getDetalleGetpr() {
        return detalleGetpr;
    }

    public void setDetalleGetpr(String detalleGetpr) {
        this.detalleGetpr = detalleGetpr;
    }

    public Boolean getActivoGetpr() {
        return activoGetpr;
    }

    public void setActivoGetpr(Boolean activoGetpr) {
        this.activoGetpr = activoGetpr;
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

    @XmlTransient
    public Collection<GthIdiomaEmpleado> getGthIdiomaEmpleadoCollection() {
        return gthIdiomaEmpleadoCollection;
    }

    public void setGthIdiomaEmpleadoCollection(Collection<GthIdiomaEmpleado> gthIdiomaEmpleadoCollection) {
        this.gthIdiomaEmpleadoCollection = gthIdiomaEmpleadoCollection;
    }

    @XmlTransient
    public Collection<GthCapacitacionEmpleado> getGthCapacitacionEmpleadoCollection() {
        return gthCapacitacionEmpleadoCollection;
    }

    public void setGthCapacitacionEmpleadoCollection(Collection<GthCapacitacionEmpleado> gthCapacitacionEmpleadoCollection) {
        this.gthCapacitacionEmpleadoCollection = gthCapacitacionEmpleadoCollection;
    }

    @XmlTransient
    public Collection<AsiMotivo> getAsiMotivoCollection() {
        return asiMotivoCollection;
    }

    public void setAsiMotivoCollection(Collection<AsiMotivo> asiMotivoCollection) {
        this.asiMotivoCollection = asiMotivoCollection;
    }

    @XmlTransient
    public Collection<GthInversionEmpleado> getGthInversionEmpleadoCollection() {
        return gthInversionEmpleadoCollection;
    }

    public void setGthInversionEmpleadoCollection(Collection<GthInversionEmpleado> gthInversionEmpleadoCollection) {
        this.gthInversionEmpleadoCollection = gthInversionEmpleadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGetpr != null ? ideGetpr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenTipoPeriodo)) {
            return false;
        }
        GenTipoPeriodo other = (GenTipoPeriodo) object;
        if ((this.ideGetpr == null && other.ideGetpr != null) || (this.ideGetpr != null && !this.ideGetpr.equals(other.ideGetpr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenTipoPeriodo[ ideGetpr=" + ideGetpr + " ]";
    }
    
}

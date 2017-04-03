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
@Table(name = "gth_tipo_inversion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTipoInversion.findAll", query = "SELECT g FROM GthTipoInversion g"),
    @NamedQuery(name = "GthTipoInversion.findByIdeGttii", query = "SELECT g FROM GthTipoInversion g WHERE g.ideGttii = :ideGttii"),
    @NamedQuery(name = "GthTipoInversion.findByDetalleGttii", query = "SELECT g FROM GthTipoInversion g WHERE g.detalleGttii = :detalleGttii"),
    @NamedQuery(name = "GthTipoInversion.findByActivoGttii", query = "SELECT g FROM GthTipoInversion g WHERE g.activoGttii = :activoGttii"),
    @NamedQuery(name = "GthTipoInversion.findByUsuarioIngre", query = "SELECT g FROM GthTipoInversion g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoInversion.findByFechaIngre", query = "SELECT g FROM GthTipoInversion g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoInversion.findByUsuarioActua", query = "SELECT g FROM GthTipoInversion g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoInversion.findByFechaActua", query = "SELECT g FROM GthTipoInversion g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoInversion.findByHoraIngre", query = "SELECT g FROM GthTipoInversion g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoInversion.findByHoraActua", query = "SELECT g FROM GthTipoInversion g WHERE g.horaActua = :horaActua")})
public class GthTipoInversion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gttii")
    private Integer ideGttii;
    @Column(name = "detalle_gttii")
    private String detalleGttii;
    @Column(name = "activo_gttii")
    private Boolean activoGttii;
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
    @OneToMany(mappedBy = "ideGttii")
    private Collection<GthInversionEmpleado> gthInversionEmpleadoCollection;

    public GthTipoInversion() {
    }

    public GthTipoInversion(Integer ideGttii) {
        this.ideGttii = ideGttii;
    }

    public Integer getIdeGttii() {
        return ideGttii;
    }

    public void setIdeGttii(Integer ideGttii) {
        this.ideGttii = ideGttii;
    }

    public String getDetalleGttii() {
        return detalleGttii;
    }

    public void setDetalleGttii(String detalleGttii) {
        this.detalleGttii = detalleGttii;
    }

    public Boolean getActivoGttii() {
        return activoGttii;
    }

    public void setActivoGttii(Boolean activoGttii) {
        this.activoGttii = activoGttii;
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
    public Collection<GthInversionEmpleado> getGthInversionEmpleadoCollection() {
        return gthInversionEmpleadoCollection;
    }

    public void setGthInversionEmpleadoCollection(Collection<GthInversionEmpleado> gthInversionEmpleadoCollection) {
        this.gthInversionEmpleadoCollection = gthInversionEmpleadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttii != null ? ideGttii.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoInversion)) {
            return false;
        }
        GthTipoInversion other = (GthTipoInversion) object;
        if ((this.ideGttii == null && other.ideGttii != null) || (this.ideGttii != null && !this.ideGttii.equals(other.ideGttii))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoInversion[ ideGttii=" + ideGttii + " ]";
    }
    
}

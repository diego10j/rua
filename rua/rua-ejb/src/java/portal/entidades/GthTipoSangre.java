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
@Table(name = "gth_tipo_sangre")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTipoSangre.findAll", query = "SELECT g FROM GthTipoSangre g"),
    @NamedQuery(name = "GthTipoSangre.findByIdeGttis", query = "SELECT g FROM GthTipoSangre g WHERE g.ideGttis = :ideGttis"),
    @NamedQuery(name = "GthTipoSangre.findByDetalleGttis", query = "SELECT g FROM GthTipoSangre g WHERE g.detalleGttis = :detalleGttis"),
    @NamedQuery(name = "GthTipoSangre.findByActivoGttis", query = "SELECT g FROM GthTipoSangre g WHERE g.activoGttis = :activoGttis"),
    @NamedQuery(name = "GthTipoSangre.findByUsuarioIngre", query = "SELECT g FROM GthTipoSangre g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoSangre.findByFechaIngre", query = "SELECT g FROM GthTipoSangre g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoSangre.findByUsuarioActua", query = "SELECT g FROM GthTipoSangre g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoSangre.findByFechaActua", query = "SELECT g FROM GthTipoSangre g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoSangre.findByHoraIngre", query = "SELECT g FROM GthTipoSangre g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoSangre.findByHoraActua", query = "SELECT g FROM GthTipoSangre g WHERE g.horaActua = :horaActua")})
public class GthTipoSangre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gttis")
    private Integer ideGttis;
    @Column(name = "detalle_gttis")
    private String detalleGttis;
    @Column(name = "activo_gttis")
    private Boolean activoGttis;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttis")
    private Collection<GthEmpleado> gthEmpleadoCollection;

    public GthTipoSangre() {
    }

    public GthTipoSangre(Integer ideGttis) {
        this.ideGttis = ideGttis;
    }

    public Integer getIdeGttis() {
        return ideGttis;
    }

    public void setIdeGttis(Integer ideGttis) {
        this.ideGttis = ideGttis;
    }

    public String getDetalleGttis() {
        return detalleGttis;
    }

    public void setDetalleGttis(String detalleGttis) {
        this.detalleGttis = detalleGttis;
    }

    public Boolean getActivoGttis() {
        return activoGttis;
    }

    public void setActivoGttis(Boolean activoGttis) {
        this.activoGttis = activoGttis;
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
        hash += (ideGttis != null ? ideGttis.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoSangre)) {
            return false;
        }
        GthTipoSangre other = (GthTipoSangre) object;
        if ((this.ideGttis == null && other.ideGttis != null) || (this.ideGttis != null && !this.ideGttis.equals(other.ideGttis))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoSangre[ ideGttis=" + ideGttis + " ]";
    }
    
}

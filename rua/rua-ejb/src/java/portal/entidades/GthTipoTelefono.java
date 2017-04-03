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
@Table(name = "gth_tipo_telefono")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTipoTelefono.findAll", query = "SELECT g FROM GthTipoTelefono g"),
    @NamedQuery(name = "GthTipoTelefono.findByIdeGttit", query = "SELECT g FROM GthTipoTelefono g WHERE g.ideGttit = :ideGttit"),
    @NamedQuery(name = "GthTipoTelefono.findByDetalleGttit", query = "SELECT g FROM GthTipoTelefono g WHERE g.detalleGttit = :detalleGttit"),
    @NamedQuery(name = "GthTipoTelefono.findByActivoGttit", query = "SELECT g FROM GthTipoTelefono g WHERE g.activoGttit = :activoGttit"),
    @NamedQuery(name = "GthTipoTelefono.findByUsuarioIngre", query = "SELECT g FROM GthTipoTelefono g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoTelefono.findByFechaIngre", query = "SELECT g FROM GthTipoTelefono g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoTelefono.findByUsuarioActua", query = "SELECT g FROM GthTipoTelefono g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoTelefono.findByFechaActua", query = "SELECT g FROM GthTipoTelefono g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoTelefono.findByHoraIngre", query = "SELECT g FROM GthTipoTelefono g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoTelefono.findByHoraActua", query = "SELECT g FROM GthTipoTelefono g WHERE g.horaActua = :horaActua")})
public class GthTipoTelefono implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gttit")
    private Integer ideGttit;
    @Column(name = "detalle_gttit")
    private String detalleGttit;
    @Column(name = "activo_gttit")
    private Boolean activoGttit;
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
    @OneToMany(mappedBy = "ideGttit")
    private Collection<GthTelefono> gthTelefonoCollection;

    public GthTipoTelefono() {
    }

    public GthTipoTelefono(Integer ideGttit) {
        this.ideGttit = ideGttit;
    }

    public Integer getIdeGttit() {
        return ideGttit;
    }

    public void setIdeGttit(Integer ideGttit) {
        this.ideGttit = ideGttit;
    }

    public String getDetalleGttit() {
        return detalleGttit;
    }

    public void setDetalleGttit(String detalleGttit) {
        this.detalleGttit = detalleGttit;
    }

    public Boolean getActivoGttit() {
        return activoGttit;
    }

    public void setActivoGttit(Boolean activoGttit) {
        this.activoGttit = activoGttit;
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
    public Collection<GthTelefono> getGthTelefonoCollection() {
        return gthTelefonoCollection;
    }

    public void setGthTelefonoCollection(Collection<GthTelefono> gthTelefonoCollection) {
        this.gthTelefonoCollection = gthTelefonoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttit != null ? ideGttit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoTelefono)) {
            return false;
        }
        GthTipoTelefono other = (GthTipoTelefono) object;
        if ((this.ideGttit == null && other.ideGttit != null) || (this.ideGttit != null && !this.ideGttit.equals(other.ideGttit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoTelefono[ ideGttit=" + ideGttit + " ]";
    }
    
}

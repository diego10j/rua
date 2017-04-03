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
@Table(name = "gth_tipo_hobie")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTipoHobie.findAll", query = "SELECT g FROM GthTipoHobie g"),
    @NamedQuery(name = "GthTipoHobie.findByIdeGttih", query = "SELECT g FROM GthTipoHobie g WHERE g.ideGttih = :ideGttih"),
    @NamedQuery(name = "GthTipoHobie.findByDetalleGttih", query = "SELECT g FROM GthTipoHobie g WHERE g.detalleGttih = :detalleGttih"),
    @NamedQuery(name = "GthTipoHobie.findByActivoGttih", query = "SELECT g FROM GthTipoHobie g WHERE g.activoGttih = :activoGttih"),
    @NamedQuery(name = "GthTipoHobie.findByUsuarioIngre", query = "SELECT g FROM GthTipoHobie g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoHobie.findByFechaIngre", query = "SELECT g FROM GthTipoHobie g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoHobie.findByUsuarioActua", query = "SELECT g FROM GthTipoHobie g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoHobie.findByFechaActua", query = "SELECT g FROM GthTipoHobie g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoHobie.findByHoraIngre", query = "SELECT g FROM GthTipoHobie g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoHobie.findByHoraActua", query = "SELECT g FROM GthTipoHobie g WHERE g.horaActua = :horaActua")})
public class GthTipoHobie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gttih")
    private Integer ideGttih;
    @Column(name = "detalle_gttih")
    private String detalleGttih;
    @Column(name = "activo_gttih")
    private Boolean activoGttih;
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
    @OneToMany(mappedBy = "ideGttih")
    private Collection<GthHobie> gthHobieCollection;

    public GthTipoHobie() {
    }

    public GthTipoHobie(Integer ideGttih) {
        this.ideGttih = ideGttih;
    }

    public Integer getIdeGttih() {
        return ideGttih;
    }

    public void setIdeGttih(Integer ideGttih) {
        this.ideGttih = ideGttih;
    }

    public String getDetalleGttih() {
        return detalleGttih;
    }

    public void setDetalleGttih(String detalleGttih) {
        this.detalleGttih = detalleGttih;
    }

    public Boolean getActivoGttih() {
        return activoGttih;
    }

    public void setActivoGttih(Boolean activoGttih) {
        this.activoGttih = activoGttih;
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
    public Collection<GthHobie> getGthHobieCollection() {
        return gthHobieCollection;
    }

    public void setGthHobieCollection(Collection<GthHobie> gthHobieCollection) {
        this.gthHobieCollection = gthHobieCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttih != null ? ideGttih.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoHobie)) {
            return false;
        }
        GthTipoHobie other = (GthTipoHobie) object;
        if ((this.ideGttih == null && other.ideGttih != null) || (this.ideGttih != null && !this.ideGttih.equals(other.ideGttih))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoHobie[ ideGttih=" + ideGttih + " ]";
    }
    
}

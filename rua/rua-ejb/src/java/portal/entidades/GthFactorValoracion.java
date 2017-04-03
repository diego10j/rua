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
@Table(name = "gth_factor_valoracion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthFactorValoracion.findAll", query = "SELECT g FROM GthFactorValoracion g"),
    @NamedQuery(name = "GthFactorValoracion.findByIdeGtfav", query = "SELECT g FROM GthFactorValoracion g WHERE g.ideGtfav = :ideGtfav"),
    @NamedQuery(name = "GthFactorValoracion.findByDetalleGtfav", query = "SELECT g FROM GthFactorValoracion g WHERE g.detalleGtfav = :detalleGtfav"),
    @NamedQuery(name = "GthFactorValoracion.findByActivoGtfav", query = "SELECT g FROM GthFactorValoracion g WHERE g.activoGtfav = :activoGtfav"),
    @NamedQuery(name = "GthFactorValoracion.findByUsuarioIngre", query = "SELECT g FROM GthFactorValoracion g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthFactorValoracion.findByFechaIngre", query = "SELECT g FROM GthFactorValoracion g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthFactorValoracion.findByHoraIngre", query = "SELECT g FROM GthFactorValoracion g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthFactorValoracion.findByUsuarioActua", query = "SELECT g FROM GthFactorValoracion g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthFactorValoracion.findByFechaActua", query = "SELECT g FROM GthFactorValoracion g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthFactorValoracion.findByHoraActua", query = "SELECT g FROM GthFactorValoracion g WHERE g.horaActua = :horaActua")})
public class GthFactorValoracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtfav")
    private Long ideGtfav;
    @Column(name = "detalle_gtfav")
    private String detalleGtfav;
    @Column(name = "activo_gtfav")
    private Boolean activoGtfav;
    @Column(name = "usuario_ingre")
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "usuario_actua")
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "ideGtfav")
    private Collection<GthValoraGrupo> gthValoraGrupoCollection;
    @OneToMany(mappedBy = "ideGtfav")
    private Collection<GthGrupoValora> gthGrupoValoraCollection;

    public GthFactorValoracion() {
    }

    public GthFactorValoracion(Long ideGtfav) {
        this.ideGtfav = ideGtfav;
    }

    public Long getIdeGtfav() {
        return ideGtfav;
    }

    public void setIdeGtfav(Long ideGtfav) {
        this.ideGtfav = ideGtfav;
    }

    public String getDetalleGtfav() {
        return detalleGtfav;
    }

    public void setDetalleGtfav(String detalleGtfav) {
        this.detalleGtfav = detalleGtfav;
    }

    public Boolean getActivoGtfav() {
        return activoGtfav;
    }

    public void setActivoGtfav(Boolean activoGtfav) {
        this.activoGtfav = activoGtfav;
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

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
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

    @XmlTransient
    public Collection<GthValoraGrupo> getGthValoraGrupoCollection() {
        return gthValoraGrupoCollection;
    }

    public void setGthValoraGrupoCollection(Collection<GthValoraGrupo> gthValoraGrupoCollection) {
        this.gthValoraGrupoCollection = gthValoraGrupoCollection;
    }

    @XmlTransient
    public Collection<GthGrupoValora> getGthGrupoValoraCollection() {
        return gthGrupoValoraCollection;
    }

    public void setGthGrupoValoraCollection(Collection<GthGrupoValora> gthGrupoValoraCollection) {
        this.gthGrupoValoraCollection = gthGrupoValoraCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtfav != null ? ideGtfav.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthFactorValoracion)) {
            return false;
        }
        GthFactorValoracion other = (GthFactorValoracion) object;
        if ((this.ideGtfav == null && other.ideGtfav != null) || (this.ideGtfav != null && !this.ideGtfav.equals(other.ideGtfav))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthFactorValoracion[ ideGtfav=" + ideGtfav + " ]";
    }
    
}

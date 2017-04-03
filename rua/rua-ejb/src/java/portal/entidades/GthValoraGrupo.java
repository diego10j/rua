/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "gth_valora_grupo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthValoraGrupo.findAll", query = "SELECT g FROM GthValoraGrupo g"),
    @NamedQuery(name = "GthValoraGrupo.findByIdeGtvag", query = "SELECT g FROM GthValoraGrupo g WHERE g.ideGtvag = :ideGtvag"),
    @NamedQuery(name = "GthValoraGrupo.findByDetalleGtvag", query = "SELECT g FROM GthValoraGrupo g WHERE g.detalleGtvag = :detalleGtvag"),
    @NamedQuery(name = "GthValoraGrupo.findByPuntosGtvag", query = "SELECT g FROM GthValoraGrupo g WHERE g.puntosGtvag = :puntosGtvag"),
    @NamedQuery(name = "GthValoraGrupo.findByActivoGtvag", query = "SELECT g FROM GthValoraGrupo g WHERE g.activoGtvag = :activoGtvag"),
    @NamedQuery(name = "GthValoraGrupo.findByUsuarioIngre", query = "SELECT g FROM GthValoraGrupo g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthValoraGrupo.findByFechaIngre", query = "SELECT g FROM GthValoraGrupo g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthValoraGrupo.findByHoraIngre", query = "SELECT g FROM GthValoraGrupo g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthValoraGrupo.findByUsuarioActua", query = "SELECT g FROM GthValoraGrupo g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthValoraGrupo.findByFechaActua", query = "SELECT g FROM GthValoraGrupo g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthValoraGrupo.findByHoraActua", query = "SELECT g FROM GthValoraGrupo g WHERE g.horaActua = :horaActua")})
public class GthValoraGrupo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtvag")
    private Long ideGtvag;
    @Column(name = "detalle_gtvag")
    private String detalleGtvag;
    @Column(name = "puntos_gtvag")
    private BigInteger puntosGtvag;
    @Column(name = "activo_gtvag")
    private Boolean activoGtvag;
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
    @JoinColumn(name = "ide_gtfav", referencedColumnName = "ide_gtfav")
    @ManyToOne
    private GthFactorValoracion ideGtfav;
    @OneToMany(mappedBy = "ideGtvag")
    private Collection<GthDetalleValoracion> gthDetalleValoracionCollection;

    public GthValoraGrupo() {
    }

    public GthValoraGrupo(Long ideGtvag) {
        this.ideGtvag = ideGtvag;
    }

    public Long getIdeGtvag() {
        return ideGtvag;
    }

    public void setIdeGtvag(Long ideGtvag) {
        this.ideGtvag = ideGtvag;
    }

    public String getDetalleGtvag() {
        return detalleGtvag;
    }

    public void setDetalleGtvag(String detalleGtvag) {
        this.detalleGtvag = detalleGtvag;
    }

    public BigInteger getPuntosGtvag() {
        return puntosGtvag;
    }

    public void setPuntosGtvag(BigInteger puntosGtvag) {
        this.puntosGtvag = puntosGtvag;
    }

    public Boolean getActivoGtvag() {
        return activoGtvag;
    }

    public void setActivoGtvag(Boolean activoGtvag) {
        this.activoGtvag = activoGtvag;
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

    public GthFactorValoracion getIdeGtfav() {
        return ideGtfav;
    }

    public void setIdeGtfav(GthFactorValoracion ideGtfav) {
        this.ideGtfav = ideGtfav;
    }

    @XmlTransient
    public Collection<GthDetalleValoracion> getGthDetalleValoracionCollection() {
        return gthDetalleValoracionCollection;
    }

    public void setGthDetalleValoracionCollection(Collection<GthDetalleValoracion> gthDetalleValoracionCollection) {
        this.gthDetalleValoracionCollection = gthDetalleValoracionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtvag != null ? ideGtvag.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthValoraGrupo)) {
            return false;
        }
        GthValoraGrupo other = (GthValoraGrupo) object;
        if ((this.ideGtvag == null && other.ideGtvag != null) || (this.ideGtvag != null && !this.ideGtvag.equals(other.ideGtvag))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthValoraGrupo[ ideGtvag=" + ideGtvag + " ]";
    }
    
}

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
@Table(name = "gth_detalle_valoracion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthDetalleValoracion.findAll", query = "SELECT g FROM GthDetalleValoracion g"),
    @NamedQuery(name = "GthDetalleValoracion.findByIdeGtdev", query = "SELECT g FROM GthDetalleValoracion g WHERE g.ideGtdev = :ideGtdev"),
    @NamedQuery(name = "GthDetalleValoracion.findByPuntosGtdev", query = "SELECT g FROM GthDetalleValoracion g WHERE g.puntosGtdev = :puntosGtdev"),
    @NamedQuery(name = "GthDetalleValoracion.findByActivoGtdev", query = "SELECT g FROM GthDetalleValoracion g WHERE g.activoGtdev = :activoGtdev"),
    @NamedQuery(name = "GthDetalleValoracion.findByUsuarioIngre", query = "SELECT g FROM GthDetalleValoracion g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthDetalleValoracion.findByFechaIngre", query = "SELECT g FROM GthDetalleValoracion g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthDetalleValoracion.findByHoraIngre", query = "SELECT g FROM GthDetalleValoracion g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthDetalleValoracion.findByUsuarioActua", query = "SELECT g FROM GthDetalleValoracion g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthDetalleValoracion.findByFechaActua", query = "SELECT g FROM GthDetalleValoracion g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthDetalleValoracion.findByHoraActua", query = "SELECT g FROM GthDetalleValoracion g WHERE g.horaActua = :horaActua")})
public class GthDetalleValoracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtdev")
    private Long ideGtdev;
    @Column(name = "puntos_gtdev")
    private BigInteger puntosGtdev;
    @Column(name = "activo_gtdev")
    private Boolean activoGtdev;
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
    @OneToMany(mappedBy = "ideGtdev")
    private Collection<GthDescripcionValoracion> gthDescripcionValoracionCollection;
    @JoinColumn(name = "ide_gtvag", referencedColumnName = "ide_gtvag")
    @ManyToOne
    private GthValoraGrupo ideGtvag;
    @JoinColumn(name = "ide_gtfiv", referencedColumnName = "ide_gtfiv")
    @ManyToOne
    private GthFichaValoracion ideGtfiv;

    public GthDetalleValoracion() {
    }

    public GthDetalleValoracion(Long ideGtdev) {
        this.ideGtdev = ideGtdev;
    }

    public Long getIdeGtdev() {
        return ideGtdev;
    }

    public void setIdeGtdev(Long ideGtdev) {
        this.ideGtdev = ideGtdev;
    }

    public BigInteger getPuntosGtdev() {
        return puntosGtdev;
    }

    public void setPuntosGtdev(BigInteger puntosGtdev) {
        this.puntosGtdev = puntosGtdev;
    }

    public Boolean getActivoGtdev() {
        return activoGtdev;
    }

    public void setActivoGtdev(Boolean activoGtdev) {
        this.activoGtdev = activoGtdev;
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
    public Collection<GthDescripcionValoracion> getGthDescripcionValoracionCollection() {
        return gthDescripcionValoracionCollection;
    }

    public void setGthDescripcionValoracionCollection(Collection<GthDescripcionValoracion> gthDescripcionValoracionCollection) {
        this.gthDescripcionValoracionCollection = gthDescripcionValoracionCollection;
    }

    public GthValoraGrupo getIdeGtvag() {
        return ideGtvag;
    }

    public void setIdeGtvag(GthValoraGrupo ideGtvag) {
        this.ideGtvag = ideGtvag;
    }

    public GthFichaValoracion getIdeGtfiv() {
        return ideGtfiv;
    }

    public void setIdeGtfiv(GthFichaValoracion ideGtfiv) {
        this.ideGtfiv = ideGtfiv;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtdev != null ? ideGtdev.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthDetalleValoracion)) {
            return false;
        }
        GthDetalleValoracion other = (GthDetalleValoracion) object;
        if ((this.ideGtdev == null && other.ideGtdev != null) || (this.ideGtdev != null && !this.ideGtdev.equals(other.ideGtdev))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthDetalleValoracion[ ideGtdev=" + ideGtdev + " ]";
    }
    
}

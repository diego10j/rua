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
@Table(name = "gth_nivel_viatico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthNivelViatico.findAll", query = "SELECT g FROM GthNivelViatico g"),
    @NamedQuery(name = "GthNivelViatico.findByIdeGtniv", query = "SELECT g FROM GthNivelViatico g WHERE g.ideGtniv = :ideGtniv"),
    @NamedQuery(name = "GthNivelViatico.findByDetalleGtniv", query = "SELECT g FROM GthNivelViatico g WHERE g.detalleGtniv = :detalleGtniv"),
    @NamedQuery(name = "GthNivelViatico.findByObservacionGtniv", query = "SELECT g FROM GthNivelViatico g WHERE g.observacionGtniv = :observacionGtniv"),
    @NamedQuery(name = "GthNivelViatico.findByActivoGtniv", query = "SELECT g FROM GthNivelViatico g WHERE g.activoGtniv = :activoGtniv"),
    @NamedQuery(name = "GthNivelViatico.findByUsuarioIngre", query = "SELECT g FROM GthNivelViatico g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthNivelViatico.findByFechaIngre", query = "SELECT g FROM GthNivelViatico g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthNivelViatico.findByUsuarioActua", query = "SELECT g FROM GthNivelViatico g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthNivelViatico.findByFechaActua", query = "SELECT g FROM GthNivelViatico g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthNivelViatico.findByHoraIngre", query = "SELECT g FROM GthNivelViatico g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthNivelViatico.findByHoraActua", query = "SELECT g FROM GthNivelViatico g WHERE g.horaActua = :horaActua")})
public class GthNivelViatico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtniv")
    private Integer ideGtniv;
    @Column(name = "detalle_gtniv")
    private String detalleGtniv;
    @Column(name = "observacion_gtniv")
    private String observacionGtniv;
    @Column(name = "activo_gtniv")
    private Boolean activoGtniv;
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
    @OneToMany(mappedBy = "ideGtniv")
    private Collection<GenGrupoOcupacional> genGrupoOcupacionalCollection;
    @OneToMany(mappedBy = "ideGtniv")
    private Collection<GthNivelZonaViatico> gthNivelZonaViaticoCollection;

    public GthNivelViatico() {
    }

    public GthNivelViatico(Integer ideGtniv) {
        this.ideGtniv = ideGtniv;
    }

    public Integer getIdeGtniv() {
        return ideGtniv;
    }

    public void setIdeGtniv(Integer ideGtniv) {
        this.ideGtniv = ideGtniv;
    }

    public String getDetalleGtniv() {
        return detalleGtniv;
    }

    public void setDetalleGtniv(String detalleGtniv) {
        this.detalleGtniv = detalleGtniv;
    }

    public String getObservacionGtniv() {
        return observacionGtniv;
    }

    public void setObservacionGtniv(String observacionGtniv) {
        this.observacionGtniv = observacionGtniv;
    }

    public Boolean getActivoGtniv() {
        return activoGtniv;
    }

    public void setActivoGtniv(Boolean activoGtniv) {
        this.activoGtniv = activoGtniv;
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
    public Collection<GenGrupoOcupacional> getGenGrupoOcupacionalCollection() {
        return genGrupoOcupacionalCollection;
    }

    public void setGenGrupoOcupacionalCollection(Collection<GenGrupoOcupacional> genGrupoOcupacionalCollection) {
        this.genGrupoOcupacionalCollection = genGrupoOcupacionalCollection;
    }

    @XmlTransient
    public Collection<GthNivelZonaViatico> getGthNivelZonaViaticoCollection() {
        return gthNivelZonaViaticoCollection;
    }

    public void setGthNivelZonaViaticoCollection(Collection<GthNivelZonaViatico> gthNivelZonaViaticoCollection) {
        this.gthNivelZonaViaticoCollection = gthNivelZonaViaticoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtniv != null ? ideGtniv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthNivelViatico)) {
            return false;
        }
        GthNivelViatico other = (GthNivelViatico) object;
        if ((this.ideGtniv == null && other.ideGtniv != null) || (this.ideGtniv != null && !this.ideGtniv.equals(other.ideGtniv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthNivelViatico[ ideGtniv=" + ideGtniv + " ]";
    }
    
}

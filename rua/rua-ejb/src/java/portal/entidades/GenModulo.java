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
@Table(name = "gen_modulo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenModulo.findAll", query = "SELECT g FROM GenModulo g"),
    @NamedQuery(name = "GenModulo.findByIdeGemod", query = "SELECT g FROM GenModulo g WHERE g.ideGemod = :ideGemod"),
    @NamedQuery(name = "GenModulo.findByDetalleGemod", query = "SELECT g FROM GenModulo g WHERE g.detalleGemod = :detalleGemod"),
    @NamedQuery(name = "GenModulo.findByActivoGemod", query = "SELECT g FROM GenModulo g WHERE g.activoGemod = :activoGemod"),
    @NamedQuery(name = "GenModulo.findByUsuarioIngre", query = "SELECT g FROM GenModulo g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenModulo.findByFechaIngre", query = "SELECT g FROM GenModulo g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenModulo.findByHoraIngre", query = "SELECT g FROM GenModulo g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenModulo.findByUsuarioActua", query = "SELECT g FROM GenModulo g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenModulo.findByFechaActua", query = "SELECT g FROM GenModulo g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenModulo.findByHoraActua", query = "SELECT g FROM GenModulo g WHERE g.horaActua = :horaActua")})
public class GenModulo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gemod")
    private Long ideGemod;
    @Column(name = "detalle_gemod")
    private String detalleGemod;
    @Column(name = "activo_gemod")
    private Boolean activoGemod;
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
    @OneToMany(mappedBy = "ideGemod")
    private Collection<GenModuloAdjudicador> genModuloAdjudicadorCollection;
    @OneToMany(mappedBy = "ideGemod")
    private Collection<GenTipoPersonaModulo> genTipoPersonaModuloCollection;
    @OneToMany(mappedBy = "ideGemod")
    private Collection<GenModuloSecuencial> genModuloSecuencialCollection;
    @OneToMany(mappedBy = "ideGemod")
    private Collection<GenModuloEstado> genModuloEstadoCollection;
    @OneToMany(mappedBy = "genIdeGemod")
    private Collection<GenModulo> genModuloCollection;
    @JoinColumn(name = "gen_ide_gemod", referencedColumnName = "ide_gemod")
    @ManyToOne
    private GenModulo genIdeGemod;
    @OneToMany(mappedBy = "ideGemod")
    private Collection<GenModuloDocumento> genModuloDocumentoCollection;

    public GenModulo() {
    }

    public GenModulo(Long ideGemod) {
        this.ideGemod = ideGemod;
    }

    public Long getIdeGemod() {
        return ideGemod;
    }

    public void setIdeGemod(Long ideGemod) {
        this.ideGemod = ideGemod;
    }

    public String getDetalleGemod() {
        return detalleGemod;
    }

    public void setDetalleGemod(String detalleGemod) {
        this.detalleGemod = detalleGemod;
    }

    public Boolean getActivoGemod() {
        return activoGemod;
    }

    public void setActivoGemod(Boolean activoGemod) {
        this.activoGemod = activoGemod;
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
    public Collection<GenModuloAdjudicador> getGenModuloAdjudicadorCollection() {
        return genModuloAdjudicadorCollection;
    }

    public void setGenModuloAdjudicadorCollection(Collection<GenModuloAdjudicador> genModuloAdjudicadorCollection) {
        this.genModuloAdjudicadorCollection = genModuloAdjudicadorCollection;
    }

    @XmlTransient
    public Collection<GenTipoPersonaModulo> getGenTipoPersonaModuloCollection() {
        return genTipoPersonaModuloCollection;
    }

    public void setGenTipoPersonaModuloCollection(Collection<GenTipoPersonaModulo> genTipoPersonaModuloCollection) {
        this.genTipoPersonaModuloCollection = genTipoPersonaModuloCollection;
    }

    @XmlTransient
    public Collection<GenModuloSecuencial> getGenModuloSecuencialCollection() {
        return genModuloSecuencialCollection;
    }

    public void setGenModuloSecuencialCollection(Collection<GenModuloSecuencial> genModuloSecuencialCollection) {
        this.genModuloSecuencialCollection = genModuloSecuencialCollection;
    }

    @XmlTransient
    public Collection<GenModuloEstado> getGenModuloEstadoCollection() {
        return genModuloEstadoCollection;
    }

    public void setGenModuloEstadoCollection(Collection<GenModuloEstado> genModuloEstadoCollection) {
        this.genModuloEstadoCollection = genModuloEstadoCollection;
    }

    @XmlTransient
    public Collection<GenModulo> getGenModuloCollection() {
        return genModuloCollection;
    }

    public void setGenModuloCollection(Collection<GenModulo> genModuloCollection) {
        this.genModuloCollection = genModuloCollection;
    }

    public GenModulo getGenIdeGemod() {
        return genIdeGemod;
    }

    public void setGenIdeGemod(GenModulo genIdeGemod) {
        this.genIdeGemod = genIdeGemod;
    }

    @XmlTransient
    public Collection<GenModuloDocumento> getGenModuloDocumentoCollection() {
        return genModuloDocumentoCollection;
    }

    public void setGenModuloDocumentoCollection(Collection<GenModuloDocumento> genModuloDocumentoCollection) {
        this.genModuloDocumentoCollection = genModuloDocumentoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGemod != null ? ideGemod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenModulo)) {
            return false;
        }
        GenModulo other = (GenModulo) object;
        if ((this.ideGemod == null && other.ideGemod != null) || (this.ideGemod != null && !this.ideGemod.equals(other.ideGemod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenModulo[ ideGemod=" + ideGemod + " ]";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author User
 */
@Entity
@Table(name = "sis_perfil")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisPerfil.findAll", query = "SELECT s FROM SisPerfil s"),
    @NamedQuery(name = "SisPerfil.findByIdePerf", query = "SELECT s FROM SisPerfil s WHERE s.idePerf = :idePerf"),
    @NamedQuery(name = "SisPerfil.findByNomPerf", query = "SELECT s FROM SisPerfil s WHERE s.nomPerf = :nomPerf"),
    @NamedQuery(name = "SisPerfil.findByDescripcionPerf", query = "SELECT s FROM SisPerfil s WHERE s.descripcionPerf = :descripcionPerf"),
    @NamedQuery(name = "SisPerfil.findByActivoPerf", query = "SELECT s FROM SisPerfil s WHERE s.activoPerf = :activoPerf"),
    @NamedQuery(name = "SisPerfil.findByPermUtilPerf", query = "SELECT s FROM SisPerfil s WHERE s.permUtilPerf = :permUtilPerf")})
public class SisPerfil implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_perf")
    private Long idePerf;
    @Basic(optional = false)
    @Column(name = "nom_perf")
    private String nomPerf;
    @Column(name = "descripcion_perf")
    private String descripcionPerf;
    @Column(name = "activo_perf")
    private Boolean activoPerf;
    @Column(name = "perm_util_perf")
    private Boolean permUtilPerf;
    @OneToMany(mappedBy = "idePerf")
    private Collection<SisPerfilReporte> sisPerfilReporteCollection;
    @OneToMany(mappedBy = "idePerf")
    private Collection<SisPerfilObjeto> sisPerfilObjetoCollection;
    @OneToMany(mappedBy = "idePerf")
    private Collection<SisPerfilCampo> sisPerfilCampoCollection;
    @OneToMany(mappedBy = "idePerf")
    private Collection<SisUsuario> sisUsuarioCollection;
    @OneToMany(mappedBy = "idePerf")
    private Collection<SisPerfilOpcion> sisPerfilOpcionCollection;

    public SisPerfil() {
    }

    public SisPerfil(Long idePerf) {
        this.idePerf = idePerf;
    }

    public SisPerfil(Long idePerf, String nomPerf) {
        this.idePerf = idePerf;
        this.nomPerf = nomPerf;
    }

    public Long getIdePerf() {
        return idePerf;
    }

    public void setIdePerf(Long idePerf) {
        this.idePerf = idePerf;
    }

    public String getNomPerf() {
        return nomPerf;
    }

    public void setNomPerf(String nomPerf) {
        this.nomPerf = nomPerf;
    }

    public String getDescripcionPerf() {
        return descripcionPerf;
    }

    public void setDescripcionPerf(String descripcionPerf) {
        this.descripcionPerf = descripcionPerf;
    }

    public Boolean getActivoPerf() {
        return activoPerf;
    }

    public void setActivoPerf(Boolean activoPerf) {
        this.activoPerf = activoPerf;
    }

    public Boolean getPermUtilPerf() {
        return permUtilPerf;
    }

    public void setPermUtilPerf(Boolean permUtilPerf) {
        this.permUtilPerf = permUtilPerf;
    }

    @XmlTransient
    public Collection<SisPerfilReporte> getSisPerfilReporteCollection() {
        return sisPerfilReporteCollection;
    }

    public void setSisPerfilReporteCollection(Collection<SisPerfilReporte> sisPerfilReporteCollection) {
        this.sisPerfilReporteCollection = sisPerfilReporteCollection;
    }

    @XmlTransient
    public Collection<SisPerfilObjeto> getSisPerfilObjetoCollection() {
        return sisPerfilObjetoCollection;
    }

    public void setSisPerfilObjetoCollection(Collection<SisPerfilObjeto> sisPerfilObjetoCollection) {
        this.sisPerfilObjetoCollection = sisPerfilObjetoCollection;
    }

    @XmlTransient
    public Collection<SisPerfilCampo> getSisPerfilCampoCollection() {
        return sisPerfilCampoCollection;
    }

    public void setSisPerfilCampoCollection(Collection<SisPerfilCampo> sisPerfilCampoCollection) {
        this.sisPerfilCampoCollection = sisPerfilCampoCollection;
    }

    @XmlTransient
    public Collection<SisUsuario> getSisUsuarioCollection() {
        return sisUsuarioCollection;
    }

    public void setSisUsuarioCollection(Collection<SisUsuario> sisUsuarioCollection) {
        this.sisUsuarioCollection = sisUsuarioCollection;
    }

    @XmlTransient
    public Collection<SisPerfilOpcion> getSisPerfilOpcionCollection() {
        return sisPerfilOpcionCollection;
    }

    public void setSisPerfilOpcionCollection(Collection<SisPerfilOpcion> sisPerfilOpcionCollection) {
        this.sisPerfilOpcionCollection = sisPerfilOpcionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePerf != null ? idePerf.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisPerfil)) {
            return false;
        }
        SisPerfil other = (SisPerfil) object;
        if ((this.idePerf == null && other.idePerf != null) || (this.idePerf != null && !this.idePerf.equals(other.idePerf))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisPerfil[ idePerf=" + idePerf + " ]";
    }
    
}

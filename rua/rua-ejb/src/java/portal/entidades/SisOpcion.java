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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "sis_opcion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisOpcion.findAll", query = "SELECT s FROM SisOpcion s"),
    @NamedQuery(name = "SisOpcion.findByIdeOpci", query = "SELECT s FROM SisOpcion s WHERE s.ideOpci = :ideOpci"),
    @NamedQuery(name = "SisOpcion.findByNomOpci", query = "SELECT s FROM SisOpcion s WHERE s.nomOpci = :nomOpci"),
    @NamedQuery(name = "SisOpcion.findByTipoOpci", query = "SELECT s FROM SisOpcion s WHERE s.tipoOpci = :tipoOpci"),
    @NamedQuery(name = "SisOpcion.findByPaqueteOpci", query = "SELECT s FROM SisOpcion s WHERE s.paqueteOpci = :paqueteOpci"),
    @NamedQuery(name = "SisOpcion.findByAuditoriaOpci", query = "SELECT s FROM SisOpcion s WHERE s.auditoriaOpci = :auditoriaOpci"),
    @NamedQuery(name = "SisOpcion.findByManualOpci", query = "SELECT s FROM SisOpcion s WHERE s.manualOpci = :manualOpci")})
public class SisOpcion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_opci")
    private Long ideOpci;
    @Basic(optional = false)
    @Column(name = "nom_opci")
    private String nomOpci;
    @Column(name = "tipo_opci")
    private String tipoOpci;
    @Column(name = "paquete_opci")
    private String paqueteOpci;
    @Column(name = "auditoria_opci")
    private Boolean auditoriaOpci;
    @Column(name = "manual_opci")
    private String manualOpci;
    @OneToMany(mappedBy = "ideOpci")
    private Collection<SisReporte> sisReporteCollection;
    @OneToMany(mappedBy = "ideOpci")
    private Collection<SisObjetoOpcion> sisObjetoOpcionCollection;
    @OneToMany(mappedBy = "ideOpci")
    private Collection<SisPerfilOpcion> sisPerfilOpcionCollection;
    @OneToMany(mappedBy = "sisIdeOpci")
    private Collection<SisOpcion> sisOpcionCollection;
    @JoinColumn(name = "sis_ide_opci", referencedColumnName = "ide_opci")
    @ManyToOne
    private SisOpcion sisIdeOpci;
    @OneToMany(mappedBy = "ideOpci")
    private Collection<SisTabla> sisTablaCollection;

    public SisOpcion() {
    }

    public SisOpcion(Long ideOpci) {
        this.ideOpci = ideOpci;
    }

    public SisOpcion(Long ideOpci, String nomOpci) {
        this.ideOpci = ideOpci;
        this.nomOpci = nomOpci;
    }

    public Long getIdeOpci() {
        return ideOpci;
    }

    public void setIdeOpci(Long ideOpci) {
        this.ideOpci = ideOpci;
    }

    public String getNomOpci() {
        return nomOpci;
    }

    public void setNomOpci(String nomOpci) {
        this.nomOpci = nomOpci;
    }

    public String getTipoOpci() {
        return tipoOpci;
    }

    public void setTipoOpci(String tipoOpci) {
        this.tipoOpci = tipoOpci;
    }

    public String getPaqueteOpci() {
        return paqueteOpci;
    }

    public void setPaqueteOpci(String paqueteOpci) {
        this.paqueteOpci = paqueteOpci;
    }

    public Boolean getAuditoriaOpci() {
        return auditoriaOpci;
    }

    public void setAuditoriaOpci(Boolean auditoriaOpci) {
        this.auditoriaOpci = auditoriaOpci;
    }

    public String getManualOpci() {
        return manualOpci;
    }

    public void setManualOpci(String manualOpci) {
        this.manualOpci = manualOpci;
    }

    @XmlTransient
    public Collection<SisReporte> getSisReporteCollection() {
        return sisReporteCollection;
    }

    public void setSisReporteCollection(Collection<SisReporte> sisReporteCollection) {
        this.sisReporteCollection = sisReporteCollection;
    }

    @XmlTransient
    public Collection<SisObjetoOpcion> getSisObjetoOpcionCollection() {
        return sisObjetoOpcionCollection;
    }

    public void setSisObjetoOpcionCollection(Collection<SisObjetoOpcion> sisObjetoOpcionCollection) {
        this.sisObjetoOpcionCollection = sisObjetoOpcionCollection;
    }

    @XmlTransient
    public Collection<SisPerfilOpcion> getSisPerfilOpcionCollection() {
        return sisPerfilOpcionCollection;
    }

    public void setSisPerfilOpcionCollection(Collection<SisPerfilOpcion> sisPerfilOpcionCollection) {
        this.sisPerfilOpcionCollection = sisPerfilOpcionCollection;
    }

    @XmlTransient
    public Collection<SisOpcion> getSisOpcionCollection() {
        return sisOpcionCollection;
    }

    public void setSisOpcionCollection(Collection<SisOpcion> sisOpcionCollection) {
        this.sisOpcionCollection = sisOpcionCollection;
    }

    public SisOpcion getSisIdeOpci() {
        return sisIdeOpci;
    }

    public void setSisIdeOpci(SisOpcion sisIdeOpci) {
        this.sisIdeOpci = sisIdeOpci;
    }

    @XmlTransient
    public Collection<SisTabla> getSisTablaCollection() {
        return sisTablaCollection;
    }

    public void setSisTablaCollection(Collection<SisTabla> sisTablaCollection) {
        this.sisTablaCollection = sisTablaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideOpci != null ? ideOpci.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisOpcion)) {
            return false;
        }
        SisOpcion other = (SisOpcion) object;
        if ((this.ideOpci == null && other.ideOpci != null) || (this.ideOpci != null && !this.ideOpci.equals(other.ideOpci))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisOpcion[ ideOpci=" + ideOpci + " ]";
    }
    
}

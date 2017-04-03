/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author User
 */
@Entity
@Table(name = "sis_reglas_clave")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisReglasClave.findAll", query = "SELECT s FROM SisReglasClave s"),
    @NamedQuery(name = "SisReglasClave.findByIdeRecl", query = "SELECT s FROM SisReglasClave s WHERE s.ideRecl = :ideRecl"),
    @NamedQuery(name = "SisReglasClave.findByIdeEmpr", query = "SELECT s FROM SisReglasClave s WHERE s.ideEmpr = :ideEmpr"),
    @NamedQuery(name = "SisReglasClave.findByNomRecl", query = "SELECT s FROM SisReglasClave s WHERE s.nomRecl = :nomRecl"),
    @NamedQuery(name = "SisReglasClave.findByLongitudMinimaRecl", query = "SELECT s FROM SisReglasClave s WHERE s.longitudMinimaRecl = :longitudMinimaRecl"),
    @NamedQuery(name = "SisReglasClave.findByNumCaracEspeRecl", query = "SELECT s FROM SisReglasClave s WHERE s.numCaracEspeRecl = :numCaracEspeRecl"),
    @NamedQuery(name = "SisReglasClave.findByNumMayusRecl", query = "SELECT s FROM SisReglasClave s WHERE s.numMayusRecl = :numMayusRecl"),
    @NamedQuery(name = "SisReglasClave.findByNumMinuscRecl", query = "SELECT s FROM SisReglasClave s WHERE s.numMinuscRecl = :numMinuscRecl"),
    @NamedQuery(name = "SisReglasClave.findByNumNumerosRecl", query = "SELECT s FROM SisReglasClave s WHERE s.numNumerosRecl = :numNumerosRecl"),
    @NamedQuery(name = "SisReglasClave.findByIntentosRecl", query = "SELECT s FROM SisReglasClave s WHERE s.intentosRecl = :intentosRecl"),
    @NamedQuery(name = "SisReglasClave.findByLongitudLoginRecl", query = "SELECT s FROM SisReglasClave s WHERE s.longitudLoginRecl = :longitudLoginRecl"),
    @NamedQuery(name = "SisReglasClave.findByNumValidaAnteriorRecl", query = "SELECT s FROM SisReglasClave s WHERE s.numValidaAnteriorRecl = :numValidaAnteriorRecl")})
public class SisReglasClave implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_recl")
    private Integer ideRecl;
    @Column(name = "ide_empr")
    private Integer ideEmpr;
    @Column(name = "nom_recl")
    private String nomRecl;
    @Column(name = "longitud_minima_recl")
    private Integer longitudMinimaRecl;
    @Column(name = "num_carac_espe_recl")
    private Integer numCaracEspeRecl;
    @Column(name = "num_mayus_recl")
    private Integer numMayusRecl;
    @Column(name = "num_minusc_recl")
    private Integer numMinuscRecl;
    @Column(name = "num_numeros_recl")
    private Integer numNumerosRecl;
    @Column(name = "intentos_recl")
    private Integer intentosRecl;
    @Column(name = "longitud_login_recl")
    private Integer longitudLoginRecl;
    @Column(name = "num_valida_anterior_recl")
    private Short numValidaAnteriorRecl;

    public SisReglasClave() {
    }

    public SisReglasClave(Integer ideRecl) {
        this.ideRecl = ideRecl;
    }

    public Integer getIdeRecl() {
        return ideRecl;
    }

    public void setIdeRecl(Integer ideRecl) {
        this.ideRecl = ideRecl;
    }

    public Integer getIdeEmpr() {
        return ideEmpr;
    }

    public void setIdeEmpr(Integer ideEmpr) {
        this.ideEmpr = ideEmpr;
    }

    public String getNomRecl() {
        return nomRecl;
    }

    public void setNomRecl(String nomRecl) {
        this.nomRecl = nomRecl;
    }

    public Integer getLongitudMinimaRecl() {
        return longitudMinimaRecl;
    }

    public void setLongitudMinimaRecl(Integer longitudMinimaRecl) {
        this.longitudMinimaRecl = longitudMinimaRecl;
    }

    public Integer getNumCaracEspeRecl() {
        return numCaracEspeRecl;
    }

    public void setNumCaracEspeRecl(Integer numCaracEspeRecl) {
        this.numCaracEspeRecl = numCaracEspeRecl;
    }

    public Integer getNumMayusRecl() {
        return numMayusRecl;
    }

    public void setNumMayusRecl(Integer numMayusRecl) {
        this.numMayusRecl = numMayusRecl;
    }

    public Integer getNumMinuscRecl() {
        return numMinuscRecl;
    }

    public void setNumMinuscRecl(Integer numMinuscRecl) {
        this.numMinuscRecl = numMinuscRecl;
    }

    public Integer getNumNumerosRecl() {
        return numNumerosRecl;
    }

    public void setNumNumerosRecl(Integer numNumerosRecl) {
        this.numNumerosRecl = numNumerosRecl;
    }

    public Integer getIntentosRecl() {
        return intentosRecl;
    }

    public void setIntentosRecl(Integer intentosRecl) {
        this.intentosRecl = intentosRecl;
    }

    public Integer getLongitudLoginRecl() {
        return longitudLoginRecl;
    }

    public void setLongitudLoginRecl(Integer longitudLoginRecl) {
        this.longitudLoginRecl = longitudLoginRecl;
    }

    public Short getNumValidaAnteriorRecl() {
        return numValidaAnteriorRecl;
    }

    public void setNumValidaAnteriorRecl(Short numValidaAnteriorRecl) {
        this.numValidaAnteriorRecl = numValidaAnteriorRecl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideRecl != null ? ideRecl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisReglasClave)) {
            return false;
        }
        SisReglasClave other = (SisReglasClave) object;
        if ((this.ideRecl == null && other.ideRecl != null) || (this.ideRecl != null && !this.ideRecl.equals(other.ideRecl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisReglasClave[ ideRecl=" + ideRecl + " ]";
    }
    
}

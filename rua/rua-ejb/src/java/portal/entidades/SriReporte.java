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
@Table(name = "sri_reporte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SriReporte.findAll", query = "SELECT s FROM SriReporte s"),
    @NamedQuery(name = "SriReporte.findByIdeSrrep", query = "SELECT s FROM SriReporte s WHERE s.ideSrrep = :ideSrrep"),
    @NamedQuery(name = "SriReporte.findByCodigoSrrep", query = "SELECT s FROM SriReporte s WHERE s.codigoSrrep = :codigoSrrep"),
    @NamedQuery(name = "SriReporte.findByNombreSrrep", query = "SELECT s FROM SriReporte s WHERE s.nombreSrrep = :nombreSrrep"),
    @NamedQuery(name = "SriReporte.findByValorSrrep", query = "SELECT s FROM SriReporte s WHERE s.valorSrrep = :valorSrrep"),
    @NamedQuery(name = "SriReporte.findByNumeroSrrep", query = "SELECT s FROM SriReporte s WHERE s.numeroSrrep = :numeroSrrep")})
public class SriReporte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_srrep")
    private Integer ideSrrep;
    @Column(name = "codigo_srrep")
    private Integer codigoSrrep;
    @Column(name = "nombre_srrep")
    private String nombreSrrep;
    @Column(name = "valor_srrep")
    private String valorSrrep;
    @Column(name = "numero_srrep")
    private String numeroSrrep;

    public SriReporte() {
    }

    public SriReporte(Integer ideSrrep) {
        this.ideSrrep = ideSrrep;
    }

    public Integer getIdeSrrep() {
        return ideSrrep;
    }

    public void setIdeSrrep(Integer ideSrrep) {
        this.ideSrrep = ideSrrep;
    }

    public Integer getCodigoSrrep() {
        return codigoSrrep;
    }

    public void setCodigoSrrep(Integer codigoSrrep) {
        this.codigoSrrep = codigoSrrep;
    }

    public String getNombreSrrep() {
        return nombreSrrep;
    }

    public void setNombreSrrep(String nombreSrrep) {
        this.nombreSrrep = nombreSrrep;
    }

    public String getValorSrrep() {
        return valorSrrep;
    }

    public void setValorSrrep(String valorSrrep) {
        this.valorSrrep = valorSrrep;
    }

    public String getNumeroSrrep() {
        return numeroSrrep;
    }

    public void setNumeroSrrep(String numeroSrrep) {
        this.numeroSrrep = numeroSrrep;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSrrep != null ? ideSrrep.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SriReporte)) {
            return false;
        }
        SriReporte other = (SriReporte) object;
        if ((this.ideSrrep == null && other.ideSrrep != null) || (this.ideSrrep != null && !this.ideSrrep.equals(other.ideSrrep))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SriReporte[ ideSrrep=" + ideSrrep + " ]";
    }
    
}

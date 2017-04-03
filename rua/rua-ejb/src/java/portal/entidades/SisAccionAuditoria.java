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
@Table(name = "sis_accion_auditoria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisAccionAuditoria.findAll", query = "SELECT s FROM SisAccionAuditoria s"),
    @NamedQuery(name = "SisAccionAuditoria.findByIdeAcau", query = "SELECT s FROM SisAccionAuditoria s WHERE s.ideAcau = :ideAcau"),
    @NamedQuery(name = "SisAccionAuditoria.findByNomAcau", query = "SELECT s FROM SisAccionAuditoria s WHERE s.nomAcau = :nomAcau"),
    @NamedQuery(name = "SisAccionAuditoria.findByDescripcionAcau", query = "SELECT s FROM SisAccionAuditoria s WHERE s.descripcionAcau = :descripcionAcau")})
public class SisAccionAuditoria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_acau")
    private Integer ideAcau;
    @Basic(optional = false)
    @Column(name = "nom_acau")
    private String nomAcau;
    @Column(name = "descripcion_acau")
    private String descripcionAcau;

    public SisAccionAuditoria() {
    }

    public SisAccionAuditoria(Integer ideAcau) {
        this.ideAcau = ideAcau;
    }

    public SisAccionAuditoria(Integer ideAcau, String nomAcau) {
        this.ideAcau = ideAcau;
        this.nomAcau = nomAcau;
    }

    public Integer getIdeAcau() {
        return ideAcau;
    }

    public void setIdeAcau(Integer ideAcau) {
        this.ideAcau = ideAcau;
    }

    public String getNomAcau() {
        return nomAcau;
    }

    public void setNomAcau(String nomAcau) {
        this.nomAcau = nomAcau;
    }

    public String getDescripcionAcau() {
        return descripcionAcau;
    }

    public void setDescripcionAcau(String descripcionAcau) {
        this.descripcionAcau = descripcionAcau;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAcau != null ? ideAcau.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisAccionAuditoria)) {
            return false;
        }
        SisAccionAuditoria other = (SisAccionAuditoria) object;
        if ((this.ideAcau == null && other.ideAcau != null) || (this.ideAcau != null && !this.ideAcau.equals(other.ideAcau))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisAccionAuditoria[ ideAcau=" + ideAcau + " ]";
    }
    
}

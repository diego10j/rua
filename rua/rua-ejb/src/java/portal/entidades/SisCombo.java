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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author User
 */
@Entity
@Table(name = "sis_combo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisCombo.findAll", query = "SELECT s FROM SisCombo s"),
    @NamedQuery(name = "SisCombo.findByIdeComb", query = "SELECT s FROM SisCombo s WHERE s.ideComb = :ideComb"),
    @NamedQuery(name = "SisCombo.findByCampoComb", query = "SELECT s FROM SisCombo s WHERE s.campoComb = :campoComb"),
    @NamedQuery(name = "SisCombo.findByTablaComb", query = "SELECT s FROM SisCombo s WHERE s.tablaComb = :tablaComb"),
    @NamedQuery(name = "SisCombo.findByPrimariaComb", query = "SELECT s FROM SisCombo s WHERE s.primariaComb = :primariaComb"),
    @NamedQuery(name = "SisCombo.findByNombreComb", query = "SELECT s FROM SisCombo s WHERE s.nombreComb = :nombreComb"),
    @NamedQuery(name = "SisCombo.findByCondicionComb", query = "SELECT s FROM SisCombo s WHERE s.condicionComb = :condicionComb")})
public class SisCombo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_comb")
    private Long ideComb;
    @Basic(optional = false)
    @Column(name = "campo_comb")
    private String campoComb;
    @Column(name = "tabla_comb")
    private String tablaComb;
    @Column(name = "primaria_comb")
    private String primariaComb;
    @Column(name = "nombre_comb")
    private String nombreComb;
    @Column(name = "condicion_comb")
    private String condicionComb;
    @JoinColumn(name = "ide_tabl", referencedColumnName = "ide_tabl")
    @ManyToOne
    private SisTabla ideTabl;

    public SisCombo() {
    }

    public SisCombo(Long ideComb) {
        this.ideComb = ideComb;
    }

    public SisCombo(Long ideComb, String campoComb) {
        this.ideComb = ideComb;
        this.campoComb = campoComb;
    }

    public Long getIdeComb() {
        return ideComb;
    }

    public void setIdeComb(Long ideComb) {
        this.ideComb = ideComb;
    }

    public String getCampoComb() {
        return campoComb;
    }

    public void setCampoComb(String campoComb) {
        this.campoComb = campoComb;
    }

    public String getTablaComb() {
        return tablaComb;
    }

    public void setTablaComb(String tablaComb) {
        this.tablaComb = tablaComb;
    }

    public String getPrimariaComb() {
        return primariaComb;
    }

    public void setPrimariaComb(String primariaComb) {
        this.primariaComb = primariaComb;
    }

    public String getNombreComb() {
        return nombreComb;
    }

    public void setNombreComb(String nombreComb) {
        this.nombreComb = nombreComb;
    }

    public String getCondicionComb() {
        return condicionComb;
    }

    public void setCondicionComb(String condicionComb) {
        this.condicionComb = condicionComb;
    }

    public SisTabla getIdeTabl() {
        return ideTabl;
    }

    public void setIdeTabl(SisTabla ideTabl) {
        this.ideTabl = ideTabl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideComb != null ? ideComb.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisCombo)) {
            return false;
        }
        SisCombo other = (SisCombo) object;
        if ((this.ideComb == null && other.ideComb != null) || (this.ideComb != null && !this.ideComb.equals(other.ideComb))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisCombo[ ideComb=" + ideComb + " ]";
    }
    
}

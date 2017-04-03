/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author User
 */
@Embeddable
public class GenGrupoCargoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "ide_gegro")
    private int ideGegro;
    @Basic(optional = false)
    @Column(name = "ide_gecaf")
    private int ideGecaf;

    public GenGrupoCargoPK() {
    }

    public GenGrupoCargoPK(int ideGegro, int ideGecaf) {
        this.ideGegro = ideGegro;
        this.ideGecaf = ideGecaf;
    }

    public int getIdeGegro() {
        return ideGegro;
    }

    public void setIdeGegro(int ideGegro) {
        this.ideGegro = ideGegro;
    }

    public int getIdeGecaf() {
        return ideGecaf;
    }

    public void setIdeGecaf(int ideGecaf) {
        this.ideGecaf = ideGecaf;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideGegro;
        hash += (int) ideGecaf;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenGrupoCargoPK)) {
            return false;
        }
        GenGrupoCargoPK other = (GenGrupoCargoPK) object;
        if (this.ideGegro != other.ideGegro) {
            return false;
        }
        if (this.ideGecaf != other.ideGecaf) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenGrupoCargoPK[ ideGegro=" + ideGegro + ", ideGecaf=" + ideGecaf + " ]";
    }
    
}

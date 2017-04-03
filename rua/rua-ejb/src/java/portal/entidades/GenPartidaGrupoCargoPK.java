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
public class GenPartidaGrupoCargoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "ide_gepgc")
    private int ideGepgc;
    @Basic(optional = false)
    @Column(name = "ide_gegro")
    private int ideGegro;
    @Basic(optional = false)
    @Column(name = "ide_gecaf")
    private int ideGecaf;
    @Basic(optional = false)
    @Column(name = "ide_sucu")
    private int ideSucu;
    @Basic(optional = false)
    @Column(name = "ide_gedep")
    private int ideGedep;
    @Basic(optional = false)
    @Column(name = "ide_geare")
    private int ideGeare;

    public GenPartidaGrupoCargoPK() {
    }

    public GenPartidaGrupoCargoPK(int ideGepgc, int ideGegro, int ideGecaf, int ideSucu, int ideGedep, int ideGeare) {
        this.ideGepgc = ideGepgc;
        this.ideGegro = ideGegro;
        this.ideGecaf = ideGecaf;
        this.ideSucu = ideSucu;
        this.ideGedep = ideGedep;
        this.ideGeare = ideGeare;
    }

    public int getIdeGepgc() {
        return ideGepgc;
    }

    public void setIdeGepgc(int ideGepgc) {
        this.ideGepgc = ideGepgc;
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

    public int getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(int ideSucu) {
        this.ideSucu = ideSucu;
    }

    public int getIdeGedep() {
        return ideGedep;
    }

    public void setIdeGedep(int ideGedep) {
        this.ideGedep = ideGedep;
    }

    public int getIdeGeare() {
        return ideGeare;
    }

    public void setIdeGeare(int ideGeare) {
        this.ideGeare = ideGeare;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideGepgc;
        hash += (int) ideGegro;
        hash += (int) ideGecaf;
        hash += (int) ideSucu;
        hash += (int) ideGedep;
        hash += (int) ideGeare;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenPartidaGrupoCargoPK)) {
            return false;
        }
        GenPartidaGrupoCargoPK other = (GenPartidaGrupoCargoPK) object;
        if (this.ideGepgc != other.ideGepgc) {
            return false;
        }
        if (this.ideGegro != other.ideGegro) {
            return false;
        }
        if (this.ideGecaf != other.ideGecaf) {
            return false;
        }
        if (this.ideSucu != other.ideSucu) {
            return false;
        }
        if (this.ideGedep != other.ideGedep) {
            return false;
        }
        if (this.ideGeare != other.ideGeare) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenPartidaGrupoCargoPK[ ideGepgc=" + ideGepgc + ", ideGegro=" + ideGegro + ", ideGecaf=" + ideGecaf + ", ideSucu=" + ideSucu + ", ideGedep=" + ideGedep + ", ideGeare=" + ideGeare + " ]";
    }
    
}

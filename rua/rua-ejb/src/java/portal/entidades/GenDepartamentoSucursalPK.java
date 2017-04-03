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
public class GenDepartamentoSucursalPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "ide_sucu")
    private int ideSucu;
    @Basic(optional = false)
    @Column(name = "ide_gedep")
    private int ideGedep;
    @Basic(optional = false)
    @Column(name = "ide_geare")
    private int ideGeare;

    public GenDepartamentoSucursalPK() {
    }

    public GenDepartamentoSucursalPK(int ideSucu, int ideGedep, int ideGeare) {
        this.ideSucu = ideSucu;
        this.ideGedep = ideGedep;
        this.ideGeare = ideGeare;
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
        hash += (int) ideSucu;
        hash += (int) ideGedep;
        hash += (int) ideGeare;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenDepartamentoSucursalPK)) {
            return false;
        }
        GenDepartamentoSucursalPK other = (GenDepartamentoSucursalPK) object;
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
        return "portal.entidades.GenDepartamentoSucursalPK[ ideSucu=" + ideSucu + ", ideGedep=" + ideGedep + ", ideGeare=" + ideGeare + " ]";
    }
    
}

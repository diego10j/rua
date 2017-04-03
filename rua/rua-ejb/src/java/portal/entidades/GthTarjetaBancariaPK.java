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
public class GthTarjetaBancariaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "ide_gttab")
    private int ideGttab;
    @Basic(optional = false)
    @Column(name = "ide_gtttb")
    private int ideGtttb;

    public GthTarjetaBancariaPK() {
    }

    public GthTarjetaBancariaPK(int ideGttab, int ideGtttb) {
        this.ideGttab = ideGttab;
        this.ideGtttb = ideGtttb;
    }

    public int getIdeGttab() {
        return ideGttab;
    }

    public void setIdeGttab(int ideGttab) {
        this.ideGttab = ideGttab;
    }

    public int getIdeGtttb() {
        return ideGtttb;
    }

    public void setIdeGtttb(int ideGtttb) {
        this.ideGtttb = ideGtttb;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideGttab;
        hash += (int) ideGtttb;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTarjetaBancariaPK)) {
            return false;
        }
        GthTarjetaBancariaPK other = (GthTarjetaBancariaPK) object;
        if (this.ideGttab != other.ideGttab) {
            return false;
        }
        if (this.ideGtttb != other.ideGtttb) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTarjetaBancariaPK[ ideGttab=" + ideGttab + ", ideGtttb=" + ideGtttb + " ]";
    }
    
}

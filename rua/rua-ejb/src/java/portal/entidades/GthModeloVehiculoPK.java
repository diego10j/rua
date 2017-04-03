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
public class GthModeloVehiculoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "ide_gtmov")
    private int ideGtmov;
    @Basic(optional = false)
    @Column(name = "ide_gtmav")
    private int ideGtmav;

    public GthModeloVehiculoPK() {
    }

    public GthModeloVehiculoPK(int ideGtmov, int ideGtmav) {
        this.ideGtmov = ideGtmov;
        this.ideGtmav = ideGtmav;
    }

    public int getIdeGtmov() {
        return ideGtmov;
    }

    public void setIdeGtmov(int ideGtmov) {
        this.ideGtmov = ideGtmov;
    }

    public int getIdeGtmav() {
        return ideGtmav;
    }

    public void setIdeGtmav(int ideGtmav) {
        this.ideGtmav = ideGtmav;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideGtmov;
        hash += (int) ideGtmav;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthModeloVehiculoPK)) {
            return false;
        }
        GthModeloVehiculoPK other = (GthModeloVehiculoPK) object;
        if (this.ideGtmov != other.ideGtmov) {
            return false;
        }
        if (this.ideGtmav != other.ideGtmav) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthModeloVehiculoPK[ ideGtmov=" + ideGtmov + ", ideGtmav=" + ideGtmav + " ]";
    }
    
}

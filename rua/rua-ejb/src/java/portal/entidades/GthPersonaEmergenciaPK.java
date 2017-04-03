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
public class GthPersonaEmergenciaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "ide_gtpee")
    private int ideGtpee;
    @Basic(optional = false)
    @Column(name = "ide_gtemp")
    private int ideGtemp;

    public GthPersonaEmergenciaPK() {
    }

    public GthPersonaEmergenciaPK(int ideGtpee, int ideGtemp) {
        this.ideGtpee = ideGtpee;
        this.ideGtemp = ideGtemp;
    }

    public int getIdeGtpee() {
        return ideGtpee;
    }

    public void setIdeGtpee(int ideGtpee) {
        this.ideGtpee = ideGtpee;
    }

    public int getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(int ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideGtpee;
        hash += (int) ideGtemp;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthPersonaEmergenciaPK)) {
            return false;
        }
        GthPersonaEmergenciaPK other = (GthPersonaEmergenciaPK) object;
        if (this.ideGtpee != other.ideGtpee) {
            return false;
        }
        if (this.ideGtemp != other.ideGtemp) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthPersonaEmergenciaPK[ ideGtpee=" + ideGtpee + ", ideGtemp=" + ideGtemp + " ]";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author User
 */
@Entity
@Table(name = "nrh_cabecera_asiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhCabeceraAsiento.findAll", query = "SELECT n FROM NrhCabeceraAsiento n"),
    @NamedQuery(name = "NrhCabeceraAsiento.findByIdeNrcaa", query = "SELECT n FROM NrhCabeceraAsiento n WHERE n.ideNrcaa = :ideNrcaa"),
    @NamedQuery(name = "NrhCabeceraAsiento.findByFechaAsientoNrcaa", query = "SELECT n FROM NrhCabeceraAsiento n WHERE n.fechaAsientoNrcaa = :fechaAsientoNrcaa"),
    @NamedQuery(name = "NrhCabeceraAsiento.findByIdeAsientoRua", query = "SELECT n FROM NrhCabeceraAsiento n WHERE n.ideAsientoRua = :ideAsientoRua"),
    @NamedQuery(name = "NrhCabeceraAsiento.findByObservacionNrcaa", query = "SELECT n FROM NrhCabeceraAsiento n WHERE n.observacionNrcaa = :observacionNrcaa"),
    @NamedQuery(name = "NrhCabeceraAsiento.findByEstadoNrcaa", query = "SELECT n FROM NrhCabeceraAsiento n WHERE n.estadoNrcaa = :estadoNrcaa"),
    @NamedQuery(name = "NrhCabeceraAsiento.findByTraspasoNrcaa", query = "SELECT n FROM NrhCabeceraAsiento n WHERE n.traspasoNrcaa = :traspasoNrcaa")})
public class NrhCabeceraAsiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrcaa")
    private Long ideNrcaa;
    @Column(name = "fecha_asiento_nrcaa")
    @Temporal(TemporalType.DATE)
    private Date fechaAsientoNrcaa;
    @Column(name = "ide_asiento_rua")
    private BigInteger ideAsientoRua;
    @Column(name = "observacion_nrcaa")
    private String observacionNrcaa;
    @Column(name = "estado_nrcaa")
    private Boolean estadoNrcaa;
    @Column(name = "traspaso_nrcaa")
    private Boolean traspasoNrcaa;

    @JoinColumn(name = "ide_gepro", referencedColumnName = "ide_gepro")
    @ManyToOne
    private GenPeridoRol ideGepro;

    public NrhCabeceraAsiento() {
    }

    public NrhCabeceraAsiento(Long ideNrcaa) {
        this.ideNrcaa = ideNrcaa;
    }

    public Long getIdeNrcaa() {
        return ideNrcaa;
    }

    public void setIdeNrcaa(Long ideNrcaa) {
        this.ideNrcaa = ideNrcaa;
    }

    public Date getFechaAsientoNrcaa() {
        return fechaAsientoNrcaa;
    }

    public void setFechaAsientoNrcaa(Date fechaAsientoNrcaa) {
        this.fechaAsientoNrcaa = fechaAsientoNrcaa;
    }

    public BigInteger getIdeAsientoRua() {
        return ideAsientoRua;
    }

    public void setIdeAsientoRua(BigInteger ideAsientoRua) {
        this.ideAsientoRua = ideAsientoRua;
    }

    public String getObservacionNrcaa() {
        return observacionNrcaa;
    }

    public void setObservacionNrcaa(String observacionNrcaa) {
        this.observacionNrcaa = observacionNrcaa;
    }

    public Boolean getEstadoNrcaa() {
        return estadoNrcaa;
    }

    public void setEstadoNrcaa(Boolean estadoNrcaa) {
        this.estadoNrcaa = estadoNrcaa;
    }

    public Boolean getTraspasoNrcaa() {
        return traspasoNrcaa;
    }

    public void setTraspasoNrcaa(Boolean traspasoNrcaa) {
        this.traspasoNrcaa = traspasoNrcaa;
    }

 

    public GenPeridoRol getIdeGepro() {
        return ideGepro;
    }

    public void setIdeGepro(GenPeridoRol ideGepro) {
        this.ideGepro = ideGepro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrcaa != null ? ideNrcaa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhCabeceraAsiento)) {
            return false;
        }
        NrhCabeceraAsiento other = (NrhCabeceraAsiento) object;
        if ((this.ideNrcaa == null && other.ideNrcaa != null) || (this.ideNrcaa != null && !this.ideNrcaa.equals(other.ideNrcaa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhCabeceraAsiento[ ideNrcaa=" + ideNrcaa + " ]";
    }
    
}

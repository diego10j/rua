/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "nrh_estado_rol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhEstadoRol.findAll", query = "SELECT n FROM NrhEstadoRol n"),
    @NamedQuery(name = "NrhEstadoRol.findByIdeNresr", query = "SELECT n FROM NrhEstadoRol n WHERE n.ideNresr = :ideNresr"),
    @NamedQuery(name = "NrhEstadoRol.findByDetalleNresr", query = "SELECT n FROM NrhEstadoRol n WHERE n.detalleNresr = :detalleNresr"),
    @NamedQuery(name = "NrhEstadoRol.findByActivoNresr", query = "SELECT n FROM NrhEstadoRol n WHERE n.activoNresr = :activoNresr"),
    @NamedQuery(name = "NrhEstadoRol.findByUsuarioIngre", query = "SELECT n FROM NrhEstadoRol n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhEstadoRol.findByFechaIngre", query = "SELECT n FROM NrhEstadoRol n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhEstadoRol.findByUsuarioActua", query = "SELECT n FROM NrhEstadoRol n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhEstadoRol.findByFechaActua", query = "SELECT n FROM NrhEstadoRol n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhEstadoRol.findByHoraIngre", query = "SELECT n FROM NrhEstadoRol n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhEstadoRol.findByHoraActua", query = "SELECT n FROM NrhEstadoRol n WHERE n.horaActua = :horaActua")})
public class NrhEstadoRol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nresr")
    private Integer ideNresr;
    @Basic(optional = false)
    @Column(name = "detalle_nresr")
    private String detalleNresr;
    @Basic(optional = false)
    @Column(name = "activo_nresr")
    private boolean activoNresr;
    @Column(name = "usuario_ingre")
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "usuario_actua")
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "ideNresr")
    private Collection<NrhRol> nrhRolCollection;

    public NrhEstadoRol() {
    }

    public NrhEstadoRol(Integer ideNresr) {
        this.ideNresr = ideNresr;
    }

    public NrhEstadoRol(Integer ideNresr, String detalleNresr, boolean activoNresr) {
        this.ideNresr = ideNresr;
        this.detalleNresr = detalleNresr;
        this.activoNresr = activoNresr;
    }

    public Integer getIdeNresr() {
        return ideNresr;
    }

    public void setIdeNresr(Integer ideNresr) {
        this.ideNresr = ideNresr;
    }

    public String getDetalleNresr() {
        return detalleNresr;
    }

    public void setDetalleNresr(String detalleNresr) {
        this.detalleNresr = detalleNresr;
    }

    public boolean getActivoNresr() {
        return activoNresr;
    }

    public void setActivoNresr(boolean activoNresr) {
        this.activoNresr = activoNresr;
    }

    public String getUsuarioIngre() {
        return usuarioIngre;
    }

    public void setUsuarioIngre(String usuarioIngre) {
        this.usuarioIngre = usuarioIngre;
    }

    public Date getFechaIngre() {
        return fechaIngre;
    }

    public void setFechaIngre(Date fechaIngre) {
        this.fechaIngre = fechaIngre;
    }

    public String getUsuarioActua() {
        return usuarioActua;
    }

    public void setUsuarioActua(String usuarioActua) {
        this.usuarioActua = usuarioActua;
    }

    public Date getFechaActua() {
        return fechaActua;
    }

    public void setFechaActua(Date fechaActua) {
        this.fechaActua = fechaActua;
    }

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    @XmlTransient
    public Collection<NrhRol> getNrhRolCollection() {
        return nrhRolCollection;
    }

    public void setNrhRolCollection(Collection<NrhRol> nrhRolCollection) {
        this.nrhRolCollection = nrhRolCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNresr != null ? ideNresr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhEstadoRol)) {
            return false;
        }
        NrhEstadoRol other = (NrhEstadoRol) object;
        if ((this.ideNresr == null && other.ideNresr != null) || (this.ideNresr != null && !this.ideNresr.equals(other.ideNresr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhEstadoRol[ ideNresr=" + ideNresr + " ]";
    }
    
}

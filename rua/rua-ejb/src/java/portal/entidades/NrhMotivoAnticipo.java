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
import javax.persistence.CascadeType;
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
@Table(name = "nrh_motivo_anticipo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhMotivoAnticipo.findAll", query = "SELECT n FROM NrhMotivoAnticipo n"),
    @NamedQuery(name = "NrhMotivoAnticipo.findByIdeNrmoa", query = "SELECT n FROM NrhMotivoAnticipo n WHERE n.ideNrmoa = :ideNrmoa"),
    @NamedQuery(name = "NrhMotivoAnticipo.findByDetalleNrmoa", query = "SELECT n FROM NrhMotivoAnticipo n WHERE n.detalleNrmoa = :detalleNrmoa"),
    @NamedQuery(name = "NrhMotivoAnticipo.findByActivoNrmoa", query = "SELECT n FROM NrhMotivoAnticipo n WHERE n.activoNrmoa = :activoNrmoa"),
    @NamedQuery(name = "NrhMotivoAnticipo.findByUsuarioIngre", query = "SELECT n FROM NrhMotivoAnticipo n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhMotivoAnticipo.findByFechaIngre", query = "SELECT n FROM NrhMotivoAnticipo n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhMotivoAnticipo.findByUsuarioActua", query = "SELECT n FROM NrhMotivoAnticipo n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhMotivoAnticipo.findByFechaActua", query = "SELECT n FROM NrhMotivoAnticipo n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhMotivoAnticipo.findByHoraIngre", query = "SELECT n FROM NrhMotivoAnticipo n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhMotivoAnticipo.findByHoraActua", query = "SELECT n FROM NrhMotivoAnticipo n WHERE n.horaActua = :horaActua")})
public class NrhMotivoAnticipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrmoa")
    private Integer ideNrmoa;
    @Column(name = "detalle_nrmoa")
    private String detalleNrmoa;
    @Column(name = "activo_nrmoa")
    private Boolean activoNrmoa;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNrmoa")
    private Collection<NrhAnticipo> nrhAnticipoCollection;

    public NrhMotivoAnticipo() {
    }

    public NrhMotivoAnticipo(Integer ideNrmoa) {
        this.ideNrmoa = ideNrmoa;
    }

    public Integer getIdeNrmoa() {
        return ideNrmoa;
    }

    public void setIdeNrmoa(Integer ideNrmoa) {
        this.ideNrmoa = ideNrmoa;
    }

    public String getDetalleNrmoa() {
        return detalleNrmoa;
    }

    public void setDetalleNrmoa(String detalleNrmoa) {
        this.detalleNrmoa = detalleNrmoa;
    }

    public Boolean getActivoNrmoa() {
        return activoNrmoa;
    }

    public void setActivoNrmoa(Boolean activoNrmoa) {
        this.activoNrmoa = activoNrmoa;
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
    public Collection<NrhAnticipo> getNrhAnticipoCollection() {
        return nrhAnticipoCollection;
    }

    public void setNrhAnticipoCollection(Collection<NrhAnticipo> nrhAnticipoCollection) {
        this.nrhAnticipoCollection = nrhAnticipoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrmoa != null ? ideNrmoa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhMotivoAnticipo)) {
            return false;
        }
        NrhMotivoAnticipo other = (NrhMotivoAnticipo) object;
        if ((this.ideNrmoa == null && other.ideNrmoa != null) || (this.ideNrmoa != null && !this.ideNrmoa.equals(other.ideNrmoa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhMotivoAnticipo[ ideNrmoa=" + ideNrmoa + " ]";
    }
    
}

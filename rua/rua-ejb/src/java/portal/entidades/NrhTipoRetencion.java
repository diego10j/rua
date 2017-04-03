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
@Table(name = "nrh_tipo_retencion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhTipoRetencion.findAll", query = "SELECT n FROM NrhTipoRetencion n"),
    @NamedQuery(name = "NrhTipoRetencion.findByIdeNrtre", query = "SELECT n FROM NrhTipoRetencion n WHERE n.ideNrtre = :ideNrtre"),
    @NamedQuery(name = "NrhTipoRetencion.findByDetalleNrtre", query = "SELECT n FROM NrhTipoRetencion n WHERE n.detalleNrtre = :detalleNrtre"),
    @NamedQuery(name = "NrhTipoRetencion.findByActivoNrrte", query = "SELECT n FROM NrhTipoRetencion n WHERE n.activoNrrte = :activoNrrte"),
    @NamedQuery(name = "NrhTipoRetencion.findByUsuarioIngre", query = "SELECT n FROM NrhTipoRetencion n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhTipoRetencion.findByFechaIngre", query = "SELECT n FROM NrhTipoRetencion n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhTipoRetencion.findByUsuarioActua", query = "SELECT n FROM NrhTipoRetencion n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhTipoRetencion.findByFechaActua", query = "SELECT n FROM NrhTipoRetencion n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhTipoRetencion.findByHoraIngre", query = "SELECT n FROM NrhTipoRetencion n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhTipoRetencion.findByHoraActua", query = "SELECT n FROM NrhTipoRetencion n WHERE n.horaActua = :horaActua")})
public class NrhTipoRetencion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrtre")
    private Integer ideNrtre;
    @Column(name = "detalle_nrtre")
    private String detalleNrtre;
    @Column(name = "activo_nrrte")
    private Boolean activoNrrte;
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
    @OneToMany(mappedBy = "ideNrtre")
    private Collection<NrhRetencionJudicial> nrhRetencionJudicialCollection;

    public NrhTipoRetencion() {
    }

    public NrhTipoRetencion(Integer ideNrtre) {
        this.ideNrtre = ideNrtre;
    }

    public Integer getIdeNrtre() {
        return ideNrtre;
    }

    public void setIdeNrtre(Integer ideNrtre) {
        this.ideNrtre = ideNrtre;
    }

    public String getDetalleNrtre() {
        return detalleNrtre;
    }

    public void setDetalleNrtre(String detalleNrtre) {
        this.detalleNrtre = detalleNrtre;
    }

    public Boolean getActivoNrrte() {
        return activoNrrte;
    }

    public void setActivoNrrte(Boolean activoNrrte) {
        this.activoNrrte = activoNrrte;
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
    public Collection<NrhRetencionJudicial> getNrhRetencionJudicialCollection() {
        return nrhRetencionJudicialCollection;
    }

    public void setNrhRetencionJudicialCollection(Collection<NrhRetencionJudicial> nrhRetencionJudicialCollection) {
        this.nrhRetencionJudicialCollection = nrhRetencionJudicialCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrtre != null ? ideNrtre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhTipoRetencion)) {
            return false;
        }
        NrhTipoRetencion other = (NrhTipoRetencion) object;
        if ((this.ideNrtre == null && other.ideNrtre != null) || (this.ideNrtre != null && !this.ideNrtre.equals(other.ideNrtre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhTipoRetencion[ ideNrtre=" + ideNrtre + " ]";
    }
    
}

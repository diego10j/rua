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
@Table(name = "nrh_tipo_rubro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhTipoRubro.findAll", query = "SELECT n FROM NrhTipoRubro n"),
    @NamedQuery(name = "NrhTipoRubro.findByIdeNrtir", query = "SELECT n FROM NrhTipoRubro n WHERE n.ideNrtir = :ideNrtir"),
    @NamedQuery(name = "NrhTipoRubro.findByDetalleNrtir", query = "SELECT n FROM NrhTipoRubro n WHERE n.detalleNrtir = :detalleNrtir"),
    @NamedQuery(name = "NrhTipoRubro.findBySignoNrtir", query = "SELECT n FROM NrhTipoRubro n WHERE n.signoNrtir = :signoNrtir"),
    @NamedQuery(name = "NrhTipoRubro.findByActivoNrtir", query = "SELECT n FROM NrhTipoRubro n WHERE n.activoNrtir = :activoNrtir"),
    @NamedQuery(name = "NrhTipoRubro.findByUsuarioIngre", query = "SELECT n FROM NrhTipoRubro n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhTipoRubro.findByFechaIngre", query = "SELECT n FROM NrhTipoRubro n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhTipoRubro.findByUsuarioActua", query = "SELECT n FROM NrhTipoRubro n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhTipoRubro.findByFechaActua", query = "SELECT n FROM NrhTipoRubro n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhTipoRubro.findByHoraIngre", query = "SELECT n FROM NrhTipoRubro n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhTipoRubro.findByHoraActua", query = "SELECT n FROM NrhTipoRubro n WHERE n.horaActua = :horaActua")})
public class NrhTipoRubro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrtir")
    private Integer ideNrtir;
    @Column(name = "detalle_nrtir")
    private String detalleNrtir;
    @Column(name = "signo_nrtir")
    private Integer signoNrtir;
    @Column(name = "activo_nrtir")
    private Boolean activoNrtir;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNrtir")
    private Collection<NrhRubro> nrhRubroCollection;

    public NrhTipoRubro() {
    }

    public NrhTipoRubro(Integer ideNrtir) {
        this.ideNrtir = ideNrtir;
    }

    public Integer getIdeNrtir() {
        return ideNrtir;
    }

    public void setIdeNrtir(Integer ideNrtir) {
        this.ideNrtir = ideNrtir;
    }

    public String getDetalleNrtir() {
        return detalleNrtir;
    }

    public void setDetalleNrtir(String detalleNrtir) {
        this.detalleNrtir = detalleNrtir;
    }

    public Integer getSignoNrtir() {
        return signoNrtir;
    }

    public void setSignoNrtir(Integer signoNrtir) {
        this.signoNrtir = signoNrtir;
    }

    public Boolean getActivoNrtir() {
        return activoNrtir;
    }

    public void setActivoNrtir(Boolean activoNrtir) {
        this.activoNrtir = activoNrtir;
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
    public Collection<NrhRubro> getNrhRubroCollection() {
        return nrhRubroCollection;
    }

    public void setNrhRubroCollection(Collection<NrhRubro> nrhRubroCollection) {
        this.nrhRubroCollection = nrhRubroCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrtir != null ? ideNrtir.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhTipoRubro)) {
            return false;
        }
        NrhTipoRubro other = (NrhTipoRubro) object;
        if ((this.ideNrtir == null && other.ideNrtir != null) || (this.ideNrtir != null && !this.ideNrtir.equals(other.ideNrtir))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhTipoRubro[ ideNrtir=" + ideNrtir + " ]";
    }
    
}

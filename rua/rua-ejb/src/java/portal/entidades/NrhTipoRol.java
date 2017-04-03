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
@Table(name = "nrh_tipo_rol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhTipoRol.findAll", query = "SELECT n FROM NrhTipoRol n"),
    @NamedQuery(name = "NrhTipoRol.findByIdeNrtit", query = "SELECT n FROM NrhTipoRol n WHERE n.ideNrtit = :ideNrtit"),
    @NamedQuery(name = "NrhTipoRol.findByDetalleNrtit", query = "SELECT n FROM NrhTipoRol n WHERE n.detalleNrtit = :detalleNrtit"),
    @NamedQuery(name = "NrhTipoRol.findByNroDiasComercialNrtit", query = "SELECT n FROM NrhTipoRol n WHERE n.nroDiasComercialNrtit = :nroDiasComercialNrtit"),
    @NamedQuery(name = "NrhTipoRol.findByActivoNrtir", query = "SELECT n FROM NrhTipoRol n WHERE n.activoNrtir = :activoNrtir"),
    @NamedQuery(name = "NrhTipoRol.findByUsuarioIngre", query = "SELECT n FROM NrhTipoRol n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhTipoRol.findByFechaIngre", query = "SELECT n FROM NrhTipoRol n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhTipoRol.findByUsuarioActua", query = "SELECT n FROM NrhTipoRol n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhTipoRol.findByFechaActua", query = "SELECT n FROM NrhTipoRol n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhTipoRol.findByHoraIngre", query = "SELECT n FROM NrhTipoRol n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhTipoRol.findByHoraActua", query = "SELECT n FROM NrhTipoRol n WHERE n.horaActua = :horaActua")})
public class NrhTipoRol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrtit")
    private Integer ideNrtit;
    @Column(name = "detalle_nrtit")
    private String detalleNrtit;
    @Column(name = "nro_dias_comercial_nrtit")
    private Integer nroDiasComercialNrtit;
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
    @OneToMany(mappedBy = "ideNrtit")
    private Collection<GenPeridoRol> genPeridoRolCollection;
    @OneToMany(mappedBy = "ideNrtit")
    private Collection<NrhDetalleTipoNomina> nrhDetalleTipoNominaCollection;

    public NrhTipoRol() {
    }

    public NrhTipoRol(Integer ideNrtit) {
        this.ideNrtit = ideNrtit;
    }

    public Integer getIdeNrtit() {
        return ideNrtit;
    }

    public void setIdeNrtit(Integer ideNrtit) {
        this.ideNrtit = ideNrtit;
    }

    public String getDetalleNrtit() {
        return detalleNrtit;
    }

    public void setDetalleNrtit(String detalleNrtit) {
        this.detalleNrtit = detalleNrtit;
    }

    public Integer getNroDiasComercialNrtit() {
        return nroDiasComercialNrtit;
    }

    public void setNroDiasComercialNrtit(Integer nroDiasComercialNrtit) {
        this.nroDiasComercialNrtit = nroDiasComercialNrtit;
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
    public Collection<GenPeridoRol> getGenPeridoRolCollection() {
        return genPeridoRolCollection;
    }

    public void setGenPeridoRolCollection(Collection<GenPeridoRol> genPeridoRolCollection) {
        this.genPeridoRolCollection = genPeridoRolCollection;
    }

    @XmlTransient
    public Collection<NrhDetalleTipoNomina> getNrhDetalleTipoNominaCollection() {
        return nrhDetalleTipoNominaCollection;
    }

    public void setNrhDetalleTipoNominaCollection(Collection<NrhDetalleTipoNomina> nrhDetalleTipoNominaCollection) {
        this.nrhDetalleTipoNominaCollection = nrhDetalleTipoNominaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrtit != null ? ideNrtit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhTipoRol)) {
            return false;
        }
        NrhTipoRol other = (NrhTipoRol) object;
        if ((this.ideNrtit == null && other.ideNrtit != null) || (this.ideNrtit != null && !this.ideNrtit.equals(other.ideNrtit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhTipoRol[ ideNrtit=" + ideNrtit + " ]";
    }
    
}

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
@Table(name = "nrh_detalle_tipo_nomina")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhDetalleTipoNomina.findAll", query = "SELECT n FROM NrhDetalleTipoNomina n"),
    @NamedQuery(name = "NrhDetalleTipoNomina.findByIdeNrdtn", query = "SELECT n FROM NrhDetalleTipoNomina n WHERE n.ideNrdtn = :ideNrdtn"),
    @NamedQuery(name = "NrhDetalleTipoNomina.findByIdeSucu", query = "SELECT n FROM NrhDetalleTipoNomina n WHERE n.ideSucu = :ideSucu"),
    @NamedQuery(name = "NrhDetalleTipoNomina.findByActivoNrdtn", query = "SELECT n FROM NrhDetalleTipoNomina n WHERE n.activoNrdtn = :activoNrdtn"),
    @NamedQuery(name = "NrhDetalleTipoNomina.findByUsuarioIngre", query = "SELECT n FROM NrhDetalleTipoNomina n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhDetalleTipoNomina.findByFechaIngre", query = "SELECT n FROM NrhDetalleTipoNomina n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhDetalleTipoNomina.findByUsuarioActua", query = "SELECT n FROM NrhDetalleTipoNomina n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhDetalleTipoNomina.findByFechaActua", query = "SELECT n FROM NrhDetalleTipoNomina n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhDetalleTipoNomina.findByHoraIngre", query = "SELECT n FROM NrhDetalleTipoNomina n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhDetalleTipoNomina.findByHoraActua", query = "SELECT n FROM NrhDetalleTipoNomina n WHERE n.horaActua = :horaActua")})
public class NrhDetalleTipoNomina implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrdtn")
    private Integer ideNrdtn;
    @Column(name = "ide_sucu")
    private Integer ideSucu;
    @Column(name = "activo_nrdtn")
    private Boolean activoNrdtn;
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
    @JoinColumn(name = "ide_nrtit", referencedColumnName = "ide_nrtit")
    @ManyToOne
    private NrhTipoRol ideNrtit;
    @JoinColumn(name = "ide_nrtin", referencedColumnName = "ide_nrtin")
    @ManyToOne
    private NrhTipoNomina ideNrtin;
    @JoinColumn(name = "ide_gttem", referencedColumnName = "ide_gttem")
    @ManyToOne
    private GthTipoEmpleado ideGttem;
    @JoinColumn(name = "ide_gttco", referencedColumnName = "ide_gttco")
    @ManyToOne
    private GthTipoContrato ideGttco;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNrdtn")
    private Collection<NrhRol> nrhRolCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNrdtn")
    private Collection<NrhDetalleRubro> nrhDetalleRubroCollection;

    public NrhDetalleTipoNomina() {
    }

    public NrhDetalleTipoNomina(Integer ideNrdtn) {
        this.ideNrdtn = ideNrdtn;
    }

    public Integer getIdeNrdtn() {
        return ideNrdtn;
    }

    public void setIdeNrdtn(Integer ideNrdtn) {
        this.ideNrdtn = ideNrdtn;
    }

    public Integer getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(Integer ideSucu) {
        this.ideSucu = ideSucu;
    }

    public Boolean getActivoNrdtn() {
        return activoNrdtn;
    }

    public void setActivoNrdtn(Boolean activoNrdtn) {
        this.activoNrdtn = activoNrdtn;
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

    public NrhTipoRol getIdeNrtit() {
        return ideNrtit;
    }

    public void setIdeNrtit(NrhTipoRol ideNrtit) {
        this.ideNrtit = ideNrtit;
    }

    public NrhTipoNomina getIdeNrtin() {
        return ideNrtin;
    }

    public void setIdeNrtin(NrhTipoNomina ideNrtin) {
        this.ideNrtin = ideNrtin;
    }

    public GthTipoEmpleado getIdeGttem() {
        return ideGttem;
    }

    public void setIdeGttem(GthTipoEmpleado ideGttem) {
        this.ideGttem = ideGttem;
    }

    public GthTipoContrato getIdeGttco() {
        return ideGttco;
    }

    public void setIdeGttco(GthTipoContrato ideGttco) {
        this.ideGttco = ideGttco;
    }

    @XmlTransient
    public Collection<NrhRol> getNrhRolCollection() {
        return nrhRolCollection;
    }

    public void setNrhRolCollection(Collection<NrhRol> nrhRolCollection) {
        this.nrhRolCollection = nrhRolCollection;
    }

    @XmlTransient
    public Collection<NrhDetalleRubro> getNrhDetalleRubroCollection() {
        return nrhDetalleRubroCollection;
    }

    public void setNrhDetalleRubroCollection(Collection<NrhDetalleRubro> nrhDetalleRubroCollection) {
        this.nrhDetalleRubroCollection = nrhDetalleRubroCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrdtn != null ? ideNrdtn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhDetalleTipoNomina)) {
            return false;
        }
        NrhDetalleTipoNomina other = (NrhDetalleTipoNomina) object;
        if ((this.ideNrdtn == null && other.ideNrdtn != null) || (this.ideNrdtn != null && !this.ideNrdtn.equals(other.ideNrdtn))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhDetalleTipoNomina[ ideNrdtn=" + ideNrdtn + " ]";
    }
    
}

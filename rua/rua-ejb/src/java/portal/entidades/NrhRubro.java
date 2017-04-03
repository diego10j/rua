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
@Table(name = "nrh_rubro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhRubro.findAll", query = "SELECT n FROM NrhRubro n"),
    @NamedQuery(name = "NrhRubro.findByIdeNrrub", query = "SELECT n FROM NrhRubro n WHERE n.ideNrrub = :ideNrrub"),
    @NamedQuery(name = "NrhRubro.findByDetalleNrrub", query = "SELECT n FROM NrhRubro n WHERE n.detalleNrrub = :detalleNrrub"),
    @NamedQuery(name = "NrhRubro.findByActivoNrrub", query = "SELECT n FROM NrhRubro n WHERE n.activoNrrub = :activoNrrub"),
    @NamedQuery(name = "NrhRubro.findByUsuarioIngre", query = "SELECT n FROM NrhRubro n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhRubro.findByFechaIngre", query = "SELECT n FROM NrhRubro n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhRubro.findByUsuarioActua", query = "SELECT n FROM NrhRubro n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhRubro.findByFechaActua", query = "SELECT n FROM NrhRubro n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhRubro.findByHoraIngre", query = "SELECT n FROM NrhRubro n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhRubro.findByHoraActua", query = "SELECT n FROM NrhRubro n WHERE n.horaActua = :horaActua"),
    @NamedQuery(name = "NrhRubro.findByAnticipoNrrub", query = "SELECT n FROM NrhRubro n WHERE n.anticipoNrrub = :anticipoNrrub"),
    @NamedQuery(name = "NrhRubro.findByDecimoNrrub", query = "SELECT n FROM NrhRubro n WHERE n.decimoNrrub = :decimoNrrub")})
public class NrhRubro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrrub")
    private Integer ideNrrub;
    @Basic(optional = false)
    @Column(name = "detalle_nrrub")
    private String detalleNrrub;
    @Basic(optional = false)
    @Column(name = "activo_nrrub")
    private boolean activoNrrub;
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
    @Column(name = "anticipo_nrrub")
    private Boolean anticipoNrrub;
    @Column(name = "decimo_nrrub")
    private Boolean decimoNrrub;
    @OneToMany(mappedBy = "ideNrrub")
    private Collection<NrhRetencionRubroDescuento> nrhRetencionRubroDescuentoCollection;
    @OneToMany(mappedBy = "ideNrrub")
    private Collection<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaCollection;
    @JoinColumn(name = "ide_nrtir", referencedColumnName = "ide_nrtir")
    @ManyToOne(optional = false)
    private NrhTipoRubro ideNrtir;
    @JoinColumn(name = "ide_nrfoc", referencedColumnName = "ide_nrfoc")
    @ManyToOne(optional = false)
    private NrhFormaCalculo ideNrfoc;
    @OneToMany(mappedBy = "ideNrrub")
    private Collection<NrhRubroAsiento> nrhRubroAsientoCollection;
    @OneToMany(mappedBy = "ideNrrub")
    private Collection<NrhAmortizacion> nrhAmortizacionCollection;
    @OneToMany(mappedBy = "ideNrrub")
    private Collection<AsiMotivo> asiMotivoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNrrub")
    private Collection<NrhDetalleRubro> nrhDetalleRubroCollection;

    public NrhRubro() {
    }

    public NrhRubro(Integer ideNrrub) {
        this.ideNrrub = ideNrrub;
    }

    public NrhRubro(Integer ideNrrub, String detalleNrrub, boolean activoNrrub) {
        this.ideNrrub = ideNrrub;
        this.detalleNrrub = detalleNrrub;
        this.activoNrrub = activoNrrub;
    }

    public Integer getIdeNrrub() {
        return ideNrrub;
    }

    public void setIdeNrrub(Integer ideNrrub) {
        this.ideNrrub = ideNrrub;
    }

    public String getDetalleNrrub() {
        return detalleNrrub;
    }

    public void setDetalleNrrub(String detalleNrrub) {
        this.detalleNrrub = detalleNrrub;
    }

    public boolean getActivoNrrub() {
        return activoNrrub;
    }

    public void setActivoNrrub(boolean activoNrrub) {
        this.activoNrrub = activoNrrub;
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

    public Boolean getAnticipoNrrub() {
        return anticipoNrrub;
    }

    public void setAnticipoNrrub(Boolean anticipoNrrub) {
        this.anticipoNrrub = anticipoNrrub;
    }

    public Boolean getDecimoNrrub() {
        return decimoNrrub;
    }

    public void setDecimoNrrub(Boolean decimoNrrub) {
        this.decimoNrrub = decimoNrrub;
    }

    @XmlTransient
    public Collection<NrhRetencionRubroDescuento> getNrhRetencionRubroDescuentoCollection() {
        return nrhRetencionRubroDescuentoCollection;
    }

    public void setNrhRetencionRubroDescuentoCollection(Collection<NrhRetencionRubroDescuento> nrhRetencionRubroDescuentoCollection) {
        this.nrhRetencionRubroDescuentoCollection = nrhRetencionRubroDescuentoCollection;
    }

    @XmlTransient
    public Collection<NrhDetalleFacturaGuarderia> getNrhDetalleFacturaGuarderiaCollection() {
        return nrhDetalleFacturaGuarderiaCollection;
    }

    public void setNrhDetalleFacturaGuarderiaCollection(Collection<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaCollection) {
        this.nrhDetalleFacturaGuarderiaCollection = nrhDetalleFacturaGuarderiaCollection;
    }

    public NrhTipoRubro getIdeNrtir() {
        return ideNrtir;
    }

    public void setIdeNrtir(NrhTipoRubro ideNrtir) {
        this.ideNrtir = ideNrtir;
    }

    public NrhFormaCalculo getIdeNrfoc() {
        return ideNrfoc;
    }

    public void setIdeNrfoc(NrhFormaCalculo ideNrfoc) {
        this.ideNrfoc = ideNrfoc;
    }

    @XmlTransient
    public Collection<NrhRubroAsiento> getNrhRubroAsientoCollection() {
        return nrhRubroAsientoCollection;
    }

    public void setNrhRubroAsientoCollection(Collection<NrhRubroAsiento> nrhRubroAsientoCollection) {
        this.nrhRubroAsientoCollection = nrhRubroAsientoCollection;
    }

    @XmlTransient
    public Collection<NrhAmortizacion> getNrhAmortizacionCollection() {
        return nrhAmortizacionCollection;
    }

    public void setNrhAmortizacionCollection(Collection<NrhAmortizacion> nrhAmortizacionCollection) {
        this.nrhAmortizacionCollection = nrhAmortizacionCollection;
    }

    @XmlTransient
    public Collection<AsiMotivo> getAsiMotivoCollection() {
        return asiMotivoCollection;
    }

    public void setAsiMotivoCollection(Collection<AsiMotivo> asiMotivoCollection) {
        this.asiMotivoCollection = asiMotivoCollection;
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
        hash += (ideNrrub != null ? ideNrrub.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhRubro)) {
            return false;
        }
        NrhRubro other = (NrhRubro) object;
        if ((this.ideNrrub == null && other.ideNrrub != null) || (this.ideNrrub != null && !this.ideNrrub.equals(other.ideNrrub))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhRubro[ ideNrrub=" + ideNrrub + " ]";
    }
    
}

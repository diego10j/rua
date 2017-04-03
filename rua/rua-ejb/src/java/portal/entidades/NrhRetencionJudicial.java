/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "nrh_retencion_judicial")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhRetencionJudicial.findAll", query = "SELECT n FROM NrhRetencionJudicial n"),
    @NamedQuery(name = "NrhRetencionJudicial.findByIdeNrrej", query = "SELECT n FROM NrhRetencionJudicial n WHERE n.ideNrrej = :ideNrrej"),
    @NamedQuery(name = "NrhRetencionJudicial.findByNroOficioNrrej", query = "SELECT n FROM NrhRetencionJudicial n WHERE n.nroOficioNrrej = :nroOficioNrrej"),
    @NamedQuery(name = "NrhRetencionJudicial.findByFechaOficioNrrej", query = "SELECT n FROM NrhRetencionJudicial n WHERE n.fechaOficioNrrej = :fechaOficioNrrej"),
    @NamedQuery(name = "NrhRetencionJudicial.findByNroResolucionNrrej", query = "SELECT n FROM NrhRetencionJudicial n WHERE n.nroResolucionNrrej = :nroResolucionNrrej"),
    @NamedQuery(name = "NrhRetencionJudicial.findByValorDescuentoNrrej", query = "SELECT n FROM NrhRetencionJudicial n WHERE n.valorDescuentoNrrej = :valorDescuentoNrrej"),
    @NamedQuery(name = "NrhRetencionJudicial.findByFechaDescuentoNrrej", query = "SELECT n FROM NrhRetencionJudicial n WHERE n.fechaDescuentoNrrej = :fechaDescuentoNrrej"),
    @NamedQuery(name = "NrhRetencionJudicial.findByActivoNrrej", query = "SELECT n FROM NrhRetencionJudicial n WHERE n.activoNrrej = :activoNrrej"),
    @NamedQuery(name = "NrhRetencionJudicial.findByUsuarioIngre", query = "SELECT n FROM NrhRetencionJudicial n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhRetencionJudicial.findByFechaIngre", query = "SELECT n FROM NrhRetencionJudicial n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhRetencionJudicial.findByUsuarioActua", query = "SELECT n FROM NrhRetencionJudicial n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhRetencionJudicial.findByFechaActua", query = "SELECT n FROM NrhRetencionJudicial n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhRetencionJudicial.findByHoraIngre", query = "SELECT n FROM NrhRetencionJudicial n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhRetencionJudicial.findByHoraActua", query = "SELECT n FROM NrhRetencionJudicial n WHERE n.horaActua = :horaActua")})
public class NrhRetencionJudicial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrrej")
    private Integer ideNrrej;
    @Column(name = "nro_oficio_nrrej")
    private String nroOficioNrrej;
    @Column(name = "fecha_oficio_nrrej")
    @Temporal(TemporalType.DATE)
    private Date fechaOficioNrrej;
    @Column(name = "nro_resolucion_nrrej")
    private String nroResolucionNrrej;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_descuento_nrrej")
    private BigDecimal valorDescuentoNrrej;
    @Column(name = "fecha_descuento_nrrej")
    @Temporal(TemporalType.DATE)
    private Date fechaDescuentoNrrej;
    @Column(name = "activo_nrrej")
    private Boolean activoNrrej;
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
    @OneToMany(mappedBy = "ideNrrej")
    private Collection<NrhRetencionCargaFamilia> nrhRetencionCargaFamiliaCollection;
    @OneToMany(mappedBy = "ideNrrej")
    private Collection<NrhRetencionRubroDescuento> nrhRetencionRubroDescuentoCollection;
    @JoinColumn(name = "ide_nrtre", referencedColumnName = "ide_nrtre")
    @ManyToOne
    private NrhTipoRetencion ideNrtre;
    @JoinColumn(name = "ide_gttpr", referencedColumnName = "ide_gttpr")
    @ManyToOne
    private GthTipoParentescoRelacion ideGttpr;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "ide_geben", referencedColumnName = "ide_geben")
    @ManyToOne
    private GenBeneficiario ideGeben;

    public NrhRetencionJudicial() {
    }

    public NrhRetencionJudicial(Integer ideNrrej) {
        this.ideNrrej = ideNrrej;
    }

    public Integer getIdeNrrej() {
        return ideNrrej;
    }

    public void setIdeNrrej(Integer ideNrrej) {
        this.ideNrrej = ideNrrej;
    }

    public String getNroOficioNrrej() {
        return nroOficioNrrej;
    }

    public void setNroOficioNrrej(String nroOficioNrrej) {
        this.nroOficioNrrej = nroOficioNrrej;
    }

    public Date getFechaOficioNrrej() {
        return fechaOficioNrrej;
    }

    public void setFechaOficioNrrej(Date fechaOficioNrrej) {
        this.fechaOficioNrrej = fechaOficioNrrej;
    }

    public String getNroResolucionNrrej() {
        return nroResolucionNrrej;
    }

    public void setNroResolucionNrrej(String nroResolucionNrrej) {
        this.nroResolucionNrrej = nroResolucionNrrej;
    }

    public BigDecimal getValorDescuentoNrrej() {
        return valorDescuentoNrrej;
    }

    public void setValorDescuentoNrrej(BigDecimal valorDescuentoNrrej) {
        this.valorDescuentoNrrej = valorDescuentoNrrej;
    }

    public Date getFechaDescuentoNrrej() {
        return fechaDescuentoNrrej;
    }

    public void setFechaDescuentoNrrej(Date fechaDescuentoNrrej) {
        this.fechaDescuentoNrrej = fechaDescuentoNrrej;
    }

    public Boolean getActivoNrrej() {
        return activoNrrej;
    }

    public void setActivoNrrej(Boolean activoNrrej) {
        this.activoNrrej = activoNrrej;
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
    public Collection<NrhRetencionCargaFamilia> getNrhRetencionCargaFamiliaCollection() {
        return nrhRetencionCargaFamiliaCollection;
    }

    public void setNrhRetencionCargaFamiliaCollection(Collection<NrhRetencionCargaFamilia> nrhRetencionCargaFamiliaCollection) {
        this.nrhRetencionCargaFamiliaCollection = nrhRetencionCargaFamiliaCollection;
    }

    @XmlTransient
    public Collection<NrhRetencionRubroDescuento> getNrhRetencionRubroDescuentoCollection() {
        return nrhRetencionRubroDescuentoCollection;
    }

    public void setNrhRetencionRubroDescuentoCollection(Collection<NrhRetencionRubroDescuento> nrhRetencionRubroDescuentoCollection) {
        this.nrhRetencionRubroDescuentoCollection = nrhRetencionRubroDescuentoCollection;
    }

    public NrhTipoRetencion getIdeNrtre() {
        return ideNrtre;
    }

    public void setIdeNrtre(NrhTipoRetencion ideNrtre) {
        this.ideNrtre = ideNrtre;
    }

    public GthTipoParentescoRelacion getIdeGttpr() {
        return ideGttpr;
    }

    public void setIdeGttpr(GthTipoParentescoRelacion ideGttpr) {
        this.ideGttpr = ideGttpr;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenInstitucion getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(GenInstitucion ideGeins) {
        this.ideGeins = ideGeins;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public GenBeneficiario getIdeGeben() {
        return ideGeben;
    }

    public void setIdeGeben(GenBeneficiario ideGeben) {
        this.ideGeben = ideGeben;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrrej != null ? ideNrrej.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhRetencionJudicial)) {
            return false;
        }
        NrhRetencionJudicial other = (NrhRetencionJudicial) object;
        if ((this.ideNrrej == null && other.ideNrrej != null) || (this.ideNrrej != null && !this.ideNrrej.equals(other.ideNrrej))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhRetencionJudicial[ ideNrrej=" + ideNrrej + " ]";
    }
    
}

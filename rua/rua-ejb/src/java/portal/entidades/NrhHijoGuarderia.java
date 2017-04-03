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
@Table(name = "nrh_hijo_guarderia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhHijoGuarderia.findAll", query = "SELECT n FROM NrhHijoGuarderia n"),
    @NamedQuery(name = "NrhHijoGuarderia.findByIdeNrhig", query = "SELECT n FROM NrhHijoGuarderia n WHERE n.ideNrhig = :ideNrhig"),
    @NamedQuery(name = "NrhHijoGuarderia.findByFechaBeneficioNrhig", query = "SELECT n FROM NrhHijoGuarderia n WHERE n.fechaBeneficioNrhig = :fechaBeneficioNrhig"),
    @NamedQuery(name = "NrhHijoGuarderia.findByActivoNrhig", query = "SELECT n FROM NrhHijoGuarderia n WHERE n.activoNrhig = :activoNrhig"),
    @NamedQuery(name = "NrhHijoGuarderia.findByUsuarioIngre", query = "SELECT n FROM NrhHijoGuarderia n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhHijoGuarderia.findByFechaIngre", query = "SELECT n FROM NrhHijoGuarderia n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhHijoGuarderia.findByUsuarioActua", query = "SELECT n FROM NrhHijoGuarderia n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhHijoGuarderia.findByFechaActua", query = "SELECT n FROM NrhHijoGuarderia n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhHijoGuarderia.findByHoraIngre", query = "SELECT n FROM NrhHijoGuarderia n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhHijoGuarderia.findByHoraActua", query = "SELECT n FROM NrhHijoGuarderia n WHERE n.horaActua = :horaActua")})
public class NrhHijoGuarderia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrhig")
    private Integer ideNrhig;
    @Column(name = "fecha_beneficio_nrhig")
    @Temporal(TemporalType.DATE)
    private Date fechaBeneficioNrhig;
    @Column(name = "activo_nrhig")
    private Boolean activoNrhig;
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
    @JoinColumn(name = "ide_nrbee", referencedColumnName = "ide_nrbee")
    @ManyToOne
    private NrhBeneficioEmpleado ideNrbee;
    @JoinColumn(name = "ide_gtcaf", referencedColumnName = "ide_gtcaf")
    @ManyToOne
    private GthCargasFamiliares ideGtcaf;
    @OneToMany(mappedBy = "ideNrhig")
    private Collection<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaCollection;

    public NrhHijoGuarderia() {
    }

    public NrhHijoGuarderia(Integer ideNrhig) {
        this.ideNrhig = ideNrhig;
    }

    public Integer getIdeNrhig() {
        return ideNrhig;
    }

    public void setIdeNrhig(Integer ideNrhig) {
        this.ideNrhig = ideNrhig;
    }

    public Date getFechaBeneficioNrhig() {
        return fechaBeneficioNrhig;
    }

    public void setFechaBeneficioNrhig(Date fechaBeneficioNrhig) {
        this.fechaBeneficioNrhig = fechaBeneficioNrhig;
    }

    public Boolean getActivoNrhig() {
        return activoNrhig;
    }

    public void setActivoNrhig(Boolean activoNrhig) {
        this.activoNrhig = activoNrhig;
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

    public NrhBeneficioEmpleado getIdeNrbee() {
        return ideNrbee;
    }

    public void setIdeNrbee(NrhBeneficioEmpleado ideNrbee) {
        this.ideNrbee = ideNrbee;
    }

    public GthCargasFamiliares getIdeGtcaf() {
        return ideGtcaf;
    }

    public void setIdeGtcaf(GthCargasFamiliares ideGtcaf) {
        this.ideGtcaf = ideGtcaf;
    }

    @XmlTransient
    public Collection<NrhDetalleFacturaGuarderia> getNrhDetalleFacturaGuarderiaCollection() {
        return nrhDetalleFacturaGuarderiaCollection;
    }

    public void setNrhDetalleFacturaGuarderiaCollection(Collection<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaCollection) {
        this.nrhDetalleFacturaGuarderiaCollection = nrhDetalleFacturaGuarderiaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrhig != null ? ideNrhig.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhHijoGuarderia)) {
            return false;
        }
        NrhHijoGuarderia other = (NrhHijoGuarderia) object;
        if ((this.ideNrhig == null && other.ideNrhig != null) || (this.ideNrhig != null && !this.ideNrhig.equals(other.ideNrhig))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhHijoGuarderia[ ideNrhig=" + ideNrhig + " ]";
    }
    
}

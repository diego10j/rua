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
@Table(name = "nrh_amortizacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhAmortizacion.findAll", query = "SELECT n FROM NrhAmortizacion n"),
    @NamedQuery(name = "NrhAmortizacion.findByIdeNramo", query = "SELECT n FROM NrhAmortizacion n WHERE n.ideNramo = :ideNramo"),
    @NamedQuery(name = "NrhAmortizacion.findByFechaVencimientoNramo", query = "SELECT n FROM NrhAmortizacion n WHERE n.fechaVencimientoNramo = :fechaVencimientoNramo"),
    @NamedQuery(name = "NrhAmortizacion.findByCapitalNramo", query = "SELECT n FROM NrhAmortizacion n WHERE n.capitalNramo = :capitalNramo"),
    @NamedQuery(name = "NrhAmortizacion.findByInteresNramo", query = "SELECT n FROM NrhAmortizacion n WHERE n.interesNramo = :interesNramo"),
    @NamedQuery(name = "NrhAmortizacion.findByPrincipalNramo", query = "SELECT n FROM NrhAmortizacion n WHERE n.principalNramo = :principalNramo"),
    @NamedQuery(name = "NrhAmortizacion.findByCuotaNramo", query = "SELECT n FROM NrhAmortizacion n WHERE n.cuotaNramo = :cuotaNramo"),
    @NamedQuery(name = "NrhAmortizacion.findByFechaCanceladoNramo", query = "SELECT n FROM NrhAmortizacion n WHERE n.fechaCanceladoNramo = :fechaCanceladoNramo"),
    @NamedQuery(name = "NrhAmortizacion.findByNroCuotaNramo", query = "SELECT n FROM NrhAmortizacion n WHERE n.nroCuotaNramo = :nroCuotaNramo"),
    @NamedQuery(name = "NrhAmortizacion.findByIdeNrrol", query = "SELECT n FROM NrhAmortizacion n WHERE n.ideNrrol = :ideNrrol"),
    @NamedQuery(name = "NrhAmortizacion.findByActivoNramo", query = "SELECT n FROM NrhAmortizacion n WHERE n.activoNramo = :activoNramo"),
    @NamedQuery(name = "NrhAmortizacion.findByUsuarioIngre", query = "SELECT n FROM NrhAmortizacion n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhAmortizacion.findByFechaIngre", query = "SELECT n FROM NrhAmortizacion n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhAmortizacion.findByUsuarioActua", query = "SELECT n FROM NrhAmortizacion n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhAmortizacion.findByFechaActua", query = "SELECT n FROM NrhAmortizacion n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhAmortizacion.findByHoraIngre", query = "SELECT n FROM NrhAmortizacion n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhAmortizacion.findByHoraActua", query = "SELECT n FROM NrhAmortizacion n WHERE n.horaActua = :horaActua"),
    @NamedQuery(name = "NrhAmortizacion.findByPreCanceladoNramo", query = "SELECT n FROM NrhAmortizacion n WHERE n.preCanceladoNramo = :preCanceladoNramo")})
public class NrhAmortizacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nramo")
    private Integer ideNramo;
    @Basic(optional = false)
    @Column(name = "fecha_vencimiento_nramo")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimientoNramo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "capital_nramo")
    private BigDecimal capitalNramo;
    @Basic(optional = false)
    @Column(name = "interes_nramo")
    private BigDecimal interesNramo;
    @Column(name = "principal_nramo")
    private BigDecimal principalNramo;
    @Column(name = "cuota_nramo")
    private BigDecimal cuotaNramo;
    @Column(name = "fecha_cancelado_nramo")
    @Temporal(TemporalType.DATE)
    private Date fechaCanceladoNramo;
    @Basic(optional = false)
    @Column(name = "nro_cuota_nramo")
    private int nroCuotaNramo;
    @Column(name = "ide_nrrol")
    private Integer ideNrrol;
    @Basic(optional = false)
    @Column(name = "activo_nramo")
    private boolean activoNramo;
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
    @Column(name = "pre_cancelado_nramo")
    private Boolean preCanceladoNramo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNramo")
    private Collection<NrhPrecancelacion> nrhPrecancelacionCollection;
    @JoinColumn(name = "ide_nrrub", referencedColumnName = "ide_nrrub")
    @ManyToOne
    private NrhRubro ideNrrub;
    @JoinColumn(name = "ide_nrani", referencedColumnName = "ide_nrani")
    @ManyToOne(optional = false)
    private NrhAnticipoInteres ideNrani;

    public NrhAmortizacion() {
    }

    public NrhAmortizacion(Integer ideNramo) {
        this.ideNramo = ideNramo;
    }

    public NrhAmortizacion(Integer ideNramo, Date fechaVencimientoNramo, BigDecimal capitalNramo, BigDecimal interesNramo, int nroCuotaNramo, boolean activoNramo) {
        this.ideNramo = ideNramo;
        this.fechaVencimientoNramo = fechaVencimientoNramo;
        this.capitalNramo = capitalNramo;
        this.interesNramo = interesNramo;
        this.nroCuotaNramo = nroCuotaNramo;
        this.activoNramo = activoNramo;
    }

    public Integer getIdeNramo() {
        return ideNramo;
    }

    public void setIdeNramo(Integer ideNramo) {
        this.ideNramo = ideNramo;
    }

    public Date getFechaVencimientoNramo() {
        return fechaVencimientoNramo;
    }

    public void setFechaVencimientoNramo(Date fechaVencimientoNramo) {
        this.fechaVencimientoNramo = fechaVencimientoNramo;
    }

    public BigDecimal getCapitalNramo() {
        return capitalNramo;
    }

    public void setCapitalNramo(BigDecimal capitalNramo) {
        this.capitalNramo = capitalNramo;
    }

    public BigDecimal getInteresNramo() {
        return interesNramo;
    }

    public void setInteresNramo(BigDecimal interesNramo) {
        this.interesNramo = interesNramo;
    }

    public BigDecimal getPrincipalNramo() {
        return principalNramo;
    }

    public void setPrincipalNramo(BigDecimal principalNramo) {
        this.principalNramo = principalNramo;
    }

    public BigDecimal getCuotaNramo() {
        return cuotaNramo;
    }

    public void setCuotaNramo(BigDecimal cuotaNramo) {
        this.cuotaNramo = cuotaNramo;
    }

    public Date getFechaCanceladoNramo() {
        return fechaCanceladoNramo;
    }

    public void setFechaCanceladoNramo(Date fechaCanceladoNramo) {
        this.fechaCanceladoNramo = fechaCanceladoNramo;
    }

    public int getNroCuotaNramo() {
        return nroCuotaNramo;
    }

    public void setNroCuotaNramo(int nroCuotaNramo) {
        this.nroCuotaNramo = nroCuotaNramo;
    }

    public Integer getIdeNrrol() {
        return ideNrrol;
    }

    public void setIdeNrrol(Integer ideNrrol) {
        this.ideNrrol = ideNrrol;
    }

    public boolean getActivoNramo() {
        return activoNramo;
    }

    public void setActivoNramo(boolean activoNramo) {
        this.activoNramo = activoNramo;
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

    public Boolean getPreCanceladoNramo() {
        return preCanceladoNramo;
    }

    public void setPreCanceladoNramo(Boolean preCanceladoNramo) {
        this.preCanceladoNramo = preCanceladoNramo;
    }

    @XmlTransient
    public Collection<NrhPrecancelacion> getNrhPrecancelacionCollection() {
        return nrhPrecancelacionCollection;
    }

    public void setNrhPrecancelacionCollection(Collection<NrhPrecancelacion> nrhPrecancelacionCollection) {
        this.nrhPrecancelacionCollection = nrhPrecancelacionCollection;
    }

    public NrhRubro getIdeNrrub() {
        return ideNrrub;
    }

    public void setIdeNrrub(NrhRubro ideNrrub) {
        this.ideNrrub = ideNrrub;
    }

    public NrhAnticipoInteres getIdeNrani() {
        return ideNrani;
    }

    public void setIdeNrani(NrhAnticipoInteres ideNrani) {
        this.ideNrani = ideNrani;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNramo != null ? ideNramo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhAmortizacion)) {
            return false;
        }
        NrhAmortizacion other = (NrhAmortizacion) object;
        if ((this.ideNramo == null && other.ideNramo != null) || (this.ideNramo != null && !this.ideNramo.equals(other.ideNramo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhAmortizacion[ ideNramo=" + ideNramo + " ]";
    }
    
}

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
@Table(name = "nrh_anticipo_interes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhAnticipoInteres.findAll", query = "SELECT n FROM NrhAnticipoInteres n"),
    @NamedQuery(name = "NrhAnticipoInteres.findByIdeNrani", query = "SELECT n FROM NrhAnticipoInteres n WHERE n.ideNrani = :ideNrani"),
    @NamedQuery(name = "NrhAnticipoInteres.findByTasaInteresNrani", query = "SELECT n FROM NrhAnticipoInteres n WHERE n.tasaInteresNrani = :tasaInteresNrani"),
    @NamedQuery(name = "NrhAnticipoInteres.findByTasaEfectivaNrani", query = "SELECT n FROM NrhAnticipoInteres n WHERE n.tasaEfectivaNrani = :tasaEfectivaNrani"),
    @NamedQuery(name = "NrhAnticipoInteres.findByPlazoNrani", query = "SELECT n FROM NrhAnticipoInteres n WHERE n.plazoNrani = :plazoNrani"),
    @NamedQuery(name = "NrhAnticipoInteres.findByMesGraciaNrani", query = "SELECT n FROM NrhAnticipoInteres n WHERE n.mesGraciaNrani = :mesGraciaNrani"),
    @NamedQuery(name = "NrhAnticipoInteres.findByAmortizacionNrani", query = "SELECT n FROM NrhAnticipoInteres n WHERE n.amortizacionNrani = :amortizacionNrani"),
    @NamedQuery(name = "NrhAnticipoInteres.findByActivoNrani", query = "SELECT n FROM NrhAnticipoInteres n WHERE n.activoNrani = :activoNrani"),
    @NamedQuery(name = "NrhAnticipoInteres.findByUsuarioIngre", query = "SELECT n FROM NrhAnticipoInteres n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhAnticipoInteres.findByFechaIngre", query = "SELECT n FROM NrhAnticipoInteres n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhAnticipoInteres.findByUsuarioActua", query = "SELECT n FROM NrhAnticipoInteres n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhAnticipoInteres.findByFechaActua", query = "SELECT n FROM NrhAnticipoInteres n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhAnticipoInteres.findByHoraIngre", query = "SELECT n FROM NrhAnticipoInteres n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhAnticipoInteres.findByHoraActua", query = "SELECT n FROM NrhAnticipoInteres n WHERE n.horaActua = :horaActua")})
public class NrhAnticipoInteres implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrani")
    private Integer ideNrani;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "tasa_interes_nrani")
    private BigDecimal tasaInteresNrani;
    @Basic(optional = false)
    @Column(name = "tasa_efectiva_nrani")
    private BigDecimal tasaEfectivaNrani;
    @Basic(optional = false)
    @Column(name = "plazo_nrani")
    private int plazoNrani;
    @Basic(optional = false)
    @Column(name = "mes_gracia_nrani")
    private int mesGraciaNrani;
    @Basic(optional = false)
    @Column(name = "amortizacion_nrani")
    private int amortizacionNrani;
    @Basic(optional = false)
    @Column(name = "activo_nrani")
    private boolean activoNrani;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNrani")
    private Collection<NrhAmortizacion> nrhAmortizacionCollection;
    @JoinColumn(name = "ide_nrant", referencedColumnName = "ide_nrant")
    @ManyToOne(optional = false)
    private NrhAnticipo ideNrant;

    public NrhAnticipoInteres() {
    }

    public NrhAnticipoInteres(Integer ideNrani) {
        this.ideNrani = ideNrani;
    }

    public NrhAnticipoInteres(Integer ideNrani, BigDecimal tasaInteresNrani, BigDecimal tasaEfectivaNrani, int plazoNrani, int mesGraciaNrani, int amortizacionNrani, boolean activoNrani) {
        this.ideNrani = ideNrani;
        this.tasaInteresNrani = tasaInteresNrani;
        this.tasaEfectivaNrani = tasaEfectivaNrani;
        this.plazoNrani = plazoNrani;
        this.mesGraciaNrani = mesGraciaNrani;
        this.amortizacionNrani = amortizacionNrani;
        this.activoNrani = activoNrani;
    }

    public Integer getIdeNrani() {
        return ideNrani;
    }

    public void setIdeNrani(Integer ideNrani) {
        this.ideNrani = ideNrani;
    }

    public BigDecimal getTasaInteresNrani() {
        return tasaInteresNrani;
    }

    public void setTasaInteresNrani(BigDecimal tasaInteresNrani) {
        this.tasaInteresNrani = tasaInteresNrani;
    }

    public BigDecimal getTasaEfectivaNrani() {
        return tasaEfectivaNrani;
    }

    public void setTasaEfectivaNrani(BigDecimal tasaEfectivaNrani) {
        this.tasaEfectivaNrani = tasaEfectivaNrani;
    }

    public int getPlazoNrani() {
        return plazoNrani;
    }

    public void setPlazoNrani(int plazoNrani) {
        this.plazoNrani = plazoNrani;
    }

    public int getMesGraciaNrani() {
        return mesGraciaNrani;
    }

    public void setMesGraciaNrani(int mesGraciaNrani) {
        this.mesGraciaNrani = mesGraciaNrani;
    }

    public int getAmortizacionNrani() {
        return amortizacionNrani;
    }

    public void setAmortizacionNrani(int amortizacionNrani) {
        this.amortizacionNrani = amortizacionNrani;
    }

    public boolean getActivoNrani() {
        return activoNrani;
    }

    public void setActivoNrani(boolean activoNrani) {
        this.activoNrani = activoNrani;
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
    public Collection<NrhAmortizacion> getNrhAmortizacionCollection() {
        return nrhAmortizacionCollection;
    }

    public void setNrhAmortizacionCollection(Collection<NrhAmortizacion> nrhAmortizacionCollection) {
        this.nrhAmortizacionCollection = nrhAmortizacionCollection;
    }

    public NrhAnticipo getIdeNrant() {
        return ideNrant;
    }

    public void setIdeNrant(NrhAnticipo ideNrant) {
        this.ideNrant = ideNrant;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrani != null ? ideNrani.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhAnticipoInteres)) {
            return false;
        }
        NrhAnticipoInteres other = (NrhAnticipoInteres) object;
        if ((this.ideNrani == null && other.ideNrani != null) || (this.ideNrani != null && !this.ideNrani.equals(other.ideNrani))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhAnticipoInteres[ ideNrani=" + ideNrani + " ]";
    }
    
}

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
@Table(name = "gth_tipo_contrato")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTipoContrato.findAll", query = "SELECT g FROM GthTipoContrato g"),
    @NamedQuery(name = "GthTipoContrato.findByIdeGttco", query = "SELECT g FROM GthTipoContrato g WHERE g.ideGttco = :ideGttco"),
    @NamedQuery(name = "GthTipoContrato.findByIdeSbrel", query = "SELECT g FROM GthTipoContrato g WHERE g.ideSbrel = :ideSbrel"),
    @NamedQuery(name = "GthTipoContrato.findByDetalleGttco", query = "SELECT g FROM GthTipoContrato g WHERE g.detalleGttco = :detalleGttco"),
    @NamedQuery(name = "GthTipoContrato.findByDiaFincGttco", query = "SELECT g FROM GthTipoContrato g WHERE g.diaFincGttco = :diaFincGttco"),
    @NamedQuery(name = "GthTipoContrato.findByActivoGttco", query = "SELECT g FROM GthTipoContrato g WHERE g.activoGttco = :activoGttco"),
    @NamedQuery(name = "GthTipoContrato.findByUsuarioIngre", query = "SELECT g FROM GthTipoContrato g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoContrato.findByFechaIngre", query = "SELECT g FROM GthTipoContrato g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoContrato.findByUsuarioActua", query = "SELECT g FROM GthTipoContrato g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoContrato.findByFechaActua", query = "SELECT g FROM GthTipoContrato g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoContrato.findByHoraIngre", query = "SELECT g FROM GthTipoContrato g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoContrato.findByHoraActua", query = "SELECT g FROM GthTipoContrato g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GthTipoContrato.findByAnticipoGttco", query = "SELECT g FROM GthTipoContrato g WHERE g.anticipoGttco = :anticipoGttco"),
    @NamedQuery(name = "GthTipoContrato.findByGaranteGttco", query = "SELECT g FROM GthTipoContrato g WHERE g.garanteGttco = :garanteGttco")})
public class GthTipoContrato implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gttco")
    private Integer ideGttco;
    @Column(name = "ide_sbrel")
    private Integer ideSbrel;
    @Column(name = "detalle_gttco")
    private String detalleGttco;
    @Column(name = "dia_finc_gttco")
    private Integer diaFincGttco;
    @Column(name = "activo_gttco")
    private Boolean activoGttco;
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
    @Column(name = "anticipo_gttco")
    private Boolean anticipoGttco;
    @Column(name = "garante_gttco")
    private Boolean garanteGttco;
    @JoinColumn(name = "ide_gecae", referencedColumnName = "ide_gecae")
    @ManyToOne
    private GenCategoriaEstatus ideGecae;
    @OneToMany(mappedBy = "ideGttco")
    private Collection<NrhCondicionAnticipo> nrhCondicionAnticipoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttco")
    private Collection<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParCollection;
    @OneToMany(mappedBy = "ideGttco")
    private Collection<NrhDetalleTipoNomina> nrhDetalleTipoNominaCollection;

    public GthTipoContrato() {
    }

    public GthTipoContrato(Integer ideGttco) {
        this.ideGttco = ideGttco;
    }

    public Integer getIdeGttco() {
        return ideGttco;
    }

    public void setIdeGttco(Integer ideGttco) {
        this.ideGttco = ideGttco;
    }

    public Integer getIdeSbrel() {
        return ideSbrel;
    }

    public void setIdeSbrel(Integer ideSbrel) {
        this.ideSbrel = ideSbrel;
    }

    public String getDetalleGttco() {
        return detalleGttco;
    }

    public void setDetalleGttco(String detalleGttco) {
        this.detalleGttco = detalleGttco;
    }

    public Integer getDiaFincGttco() {
        return diaFincGttco;
    }

    public void setDiaFincGttco(Integer diaFincGttco) {
        this.diaFincGttco = diaFincGttco;
    }

    public Boolean getActivoGttco() {
        return activoGttco;
    }

    public void setActivoGttco(Boolean activoGttco) {
        this.activoGttco = activoGttco;
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

    public Boolean getAnticipoGttco() {
        return anticipoGttco;
    }

    public void setAnticipoGttco(Boolean anticipoGttco) {
        this.anticipoGttco = anticipoGttco;
    }

    public Boolean getGaranteGttco() {
        return garanteGttco;
    }

    public void setGaranteGttco(Boolean garanteGttco) {
        this.garanteGttco = garanteGttco;
    }

    public GenCategoriaEstatus getIdeGecae() {
        return ideGecae;
    }

    public void setIdeGecae(GenCategoriaEstatus ideGecae) {
        this.ideGecae = ideGecae;
    }

    @XmlTransient
    public Collection<NrhCondicionAnticipo> getNrhCondicionAnticipoCollection() {
        return nrhCondicionAnticipoCollection;
    }

    public void setNrhCondicionAnticipoCollection(Collection<NrhCondicionAnticipo> nrhCondicionAnticipoCollection) {
        this.nrhCondicionAnticipoCollection = nrhCondicionAnticipoCollection;
    }

    @XmlTransient
    public Collection<GenEmpleadosDepartamentoPar> getGenEmpleadosDepartamentoParCollection() {
        return genEmpleadosDepartamentoParCollection;
    }

    public void setGenEmpleadosDepartamentoParCollection(Collection<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParCollection) {
        this.genEmpleadosDepartamentoParCollection = genEmpleadosDepartamentoParCollection;
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
        hash += (ideGttco != null ? ideGttco.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoContrato)) {
            return false;
        }
        GthTipoContrato other = (GthTipoContrato) object;
        if ((this.ideGttco == null && other.ideGttco != null) || (this.ideGttco != null && !this.ideGttco.equals(other.ideGttco))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoContrato[ ideGttco=" + ideGttco + " ]";
    }
    
}

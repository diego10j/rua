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
@Table(name = "gen_institucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenInstitucion.findAll", query = "SELECT g FROM GenInstitucion g"),
    @NamedQuery(name = "GenInstitucion.findByIdeGeins", query = "SELECT g FROM GenInstitucion g WHERE g.ideGeins = :ideGeins"),
    @NamedQuery(name = "GenInstitucion.findByDetalleGeins", query = "SELECT g FROM GenInstitucion g WHERE g.detalleGeins = :detalleGeins"),
    @NamedQuery(name = "GenInstitucion.findByCodigoBancoGeins", query = "SELECT g FROM GenInstitucion g WHERE g.codigoBancoGeins = :codigoBancoGeins"),
    @NamedQuery(name = "GenInstitucion.findByActivoGeins", query = "SELECT g FROM GenInstitucion g WHERE g.activoGeins = :activoGeins"),
    @NamedQuery(name = "GenInstitucion.findByUsuarioIngre", query = "SELECT g FROM GenInstitucion g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenInstitucion.findByFechaIngre", query = "SELECT g FROM GenInstitucion g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenInstitucion.findByUsuarioActua", query = "SELECT g FROM GenInstitucion g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenInstitucion.findByFechaActua", query = "SELECT g FROM GenInstitucion g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenInstitucion.findByHoraIngre", query = "SELECT g FROM GenInstitucion g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenInstitucion.findByHoraActua", query = "SELECT g FROM GenInstitucion g WHERE g.horaActua = :horaActua")})
public class GenInstitucion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_geins")
    private Integer ideGeins;
    @Basic(optional = false)
    @Column(name = "detalle_geins")
    private String detalleGeins;
    @Column(name = "codigo_banco_geins")
    private String codigoBancoGeins;
    @Basic(optional = false)
    @Column(name = "activo_geins")
    private boolean activoGeins;
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
    @OneToMany(mappedBy = "ideGeins")
    private Collection<GenDetalleEmpleadoDepartame> genDetalleEmpleadoDepartameCollection;
    @OneToMany(mappedBy = "ideGeins")
    private Collection<GthEndeudamientoEmpleado> gthEndeudamientoEmpleadoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGeins")
    private Collection<GthSeguroVida> gthSeguroVidaCollection;
    @OneToMany(mappedBy = "ideGeins")
    private Collection<GthIdiomaEmpleado> gthIdiomaEmpleadoCollection;
    @JoinColumn(name = "ide_getii", referencedColumnName = "ide_getii")
    @ManyToOne(optional = false)
    private GenTipoInstitucion ideGetii;
    @OneToMany(mappedBy = "genIdeGeins")
    private Collection<GenInstitucion> genInstitucionCollection;
    @JoinColumn(name = "gen_ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion genIdeGeins;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGeins")
    private Collection<NrhPrecancelacion> nrhPrecancelacionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGeins")
    private Collection<GthCuentaBancariaEmpleado> gthCuentaBancariaEmpleadoCollection;
    @OneToMany(mappedBy = "ideGeins")
    private Collection<NrhBeneficioEmpleado> nrhBeneficioEmpleadoCollection;
    @OneToMany(mappedBy = "ideGeins")
    private Collection<NrhAnticipoAbono> nrhAnticipoAbonoCollection;
    @OneToMany(mappedBy = "ideGeins")
    private Collection<GthExperienciaDocenteEmplea> gthExperienciaDocenteEmpleaCollection;
    @OneToMany(mappedBy = "ideGeins")
    private Collection<GthCapacitacionEmpleado> gthCapacitacionEmpleadoCollection;
    @OneToMany(mappedBy = "ideGeins")
    private Collection<GthEducacionEmpleado> gthEducacionEmpleadoCollection;
    @OneToMany(mappedBy = "ideGeins")
    private Collection<GthExperienciaLaboralEmplea> gthExperienciaLaboralEmpleaCollection;
    @OneToMany(mappedBy = "ideGeins")
    private Collection<GthViaticos> gthViaticosCollection;
    @OneToMany(mappedBy = "ideGeins")
    private Collection<GthVehiculoEmpleado> gthVehiculoEmpleadoCollection;
    @OneToMany(mappedBy = "ideGeins")
    private Collection<GthInversionEmpleado> gthInversionEmpleadoCollection;
    @OneToMany(mappedBy = "ideGeins")
    private Collection<NrhRetencionJudicial> nrhRetencionJudicialCollection;

    public GenInstitucion() {
    }

    public GenInstitucion(Integer ideGeins) {
        this.ideGeins = ideGeins;
    }

    public GenInstitucion(Integer ideGeins, String detalleGeins, boolean activoGeins) {
        this.ideGeins = ideGeins;
        this.detalleGeins = detalleGeins;
        this.activoGeins = activoGeins;
    }

    public Integer getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(Integer ideGeins) {
        this.ideGeins = ideGeins;
    }

    public String getDetalleGeins() {
        return detalleGeins;
    }

    public void setDetalleGeins(String detalleGeins) {
        this.detalleGeins = detalleGeins;
    }

    public String getCodigoBancoGeins() {
        return codigoBancoGeins;
    }

    public void setCodigoBancoGeins(String codigoBancoGeins) {
        this.codigoBancoGeins = codigoBancoGeins;
    }

    public boolean getActivoGeins() {
        return activoGeins;
    }

    public void setActivoGeins(boolean activoGeins) {
        this.activoGeins = activoGeins;
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
    public Collection<GenDetalleEmpleadoDepartame> getGenDetalleEmpleadoDepartameCollection() {
        return genDetalleEmpleadoDepartameCollection;
    }

    public void setGenDetalleEmpleadoDepartameCollection(Collection<GenDetalleEmpleadoDepartame> genDetalleEmpleadoDepartameCollection) {
        this.genDetalleEmpleadoDepartameCollection = genDetalleEmpleadoDepartameCollection;
    }

    @XmlTransient
    public Collection<GthEndeudamientoEmpleado> getGthEndeudamientoEmpleadoCollection() {
        return gthEndeudamientoEmpleadoCollection;
    }

    public void setGthEndeudamientoEmpleadoCollection(Collection<GthEndeudamientoEmpleado> gthEndeudamientoEmpleadoCollection) {
        this.gthEndeudamientoEmpleadoCollection = gthEndeudamientoEmpleadoCollection;
    }

    @XmlTransient
    public Collection<GthSeguroVida> getGthSeguroVidaCollection() {
        return gthSeguroVidaCollection;
    }

    public void setGthSeguroVidaCollection(Collection<GthSeguroVida> gthSeguroVidaCollection) {
        this.gthSeguroVidaCollection = gthSeguroVidaCollection;
    }

    @XmlTransient
    public Collection<GthIdiomaEmpleado> getGthIdiomaEmpleadoCollection() {
        return gthIdiomaEmpleadoCollection;
    }

    public void setGthIdiomaEmpleadoCollection(Collection<GthIdiomaEmpleado> gthIdiomaEmpleadoCollection) {
        this.gthIdiomaEmpleadoCollection = gthIdiomaEmpleadoCollection;
    }

    public GenTipoInstitucion getIdeGetii() {
        return ideGetii;
    }

    public void setIdeGetii(GenTipoInstitucion ideGetii) {
        this.ideGetii = ideGetii;
    }

    @XmlTransient
    public Collection<GenInstitucion> getGenInstitucionCollection() {
        return genInstitucionCollection;
    }

    public void setGenInstitucionCollection(Collection<GenInstitucion> genInstitucionCollection) {
        this.genInstitucionCollection = genInstitucionCollection;
    }

    public GenInstitucion getGenIdeGeins() {
        return genIdeGeins;
    }

    public void setGenIdeGeins(GenInstitucion genIdeGeins) {
        this.genIdeGeins = genIdeGeins;
    }

    @XmlTransient
    public Collection<NrhPrecancelacion> getNrhPrecancelacionCollection() {
        return nrhPrecancelacionCollection;
    }

    public void setNrhPrecancelacionCollection(Collection<NrhPrecancelacion> nrhPrecancelacionCollection) {
        this.nrhPrecancelacionCollection = nrhPrecancelacionCollection;
    }

    @XmlTransient
    public Collection<GthCuentaBancariaEmpleado> getGthCuentaBancariaEmpleadoCollection() {
        return gthCuentaBancariaEmpleadoCollection;
    }

    public void setGthCuentaBancariaEmpleadoCollection(Collection<GthCuentaBancariaEmpleado> gthCuentaBancariaEmpleadoCollection) {
        this.gthCuentaBancariaEmpleadoCollection = gthCuentaBancariaEmpleadoCollection;
    }

    @XmlTransient
    public Collection<NrhBeneficioEmpleado> getNrhBeneficioEmpleadoCollection() {
        return nrhBeneficioEmpleadoCollection;
    }

    public void setNrhBeneficioEmpleadoCollection(Collection<NrhBeneficioEmpleado> nrhBeneficioEmpleadoCollection) {
        this.nrhBeneficioEmpleadoCollection = nrhBeneficioEmpleadoCollection;
    }

    @XmlTransient
    public Collection<NrhAnticipoAbono> getNrhAnticipoAbonoCollection() {
        return nrhAnticipoAbonoCollection;
    }

    public void setNrhAnticipoAbonoCollection(Collection<NrhAnticipoAbono> nrhAnticipoAbonoCollection) {
        this.nrhAnticipoAbonoCollection = nrhAnticipoAbonoCollection;
    }

    @XmlTransient
    public Collection<GthExperienciaDocenteEmplea> getGthExperienciaDocenteEmpleaCollection() {
        return gthExperienciaDocenteEmpleaCollection;
    }

    public void setGthExperienciaDocenteEmpleaCollection(Collection<GthExperienciaDocenteEmplea> gthExperienciaDocenteEmpleaCollection) {
        this.gthExperienciaDocenteEmpleaCollection = gthExperienciaDocenteEmpleaCollection;
    }

    @XmlTransient
    public Collection<GthCapacitacionEmpleado> getGthCapacitacionEmpleadoCollection() {
        return gthCapacitacionEmpleadoCollection;
    }

    public void setGthCapacitacionEmpleadoCollection(Collection<GthCapacitacionEmpleado> gthCapacitacionEmpleadoCollection) {
        this.gthCapacitacionEmpleadoCollection = gthCapacitacionEmpleadoCollection;
    }

    @XmlTransient
    public Collection<GthEducacionEmpleado> getGthEducacionEmpleadoCollection() {
        return gthEducacionEmpleadoCollection;
    }

    public void setGthEducacionEmpleadoCollection(Collection<GthEducacionEmpleado> gthEducacionEmpleadoCollection) {
        this.gthEducacionEmpleadoCollection = gthEducacionEmpleadoCollection;
    }

    @XmlTransient
    public Collection<GthExperienciaLaboralEmplea> getGthExperienciaLaboralEmpleaCollection() {
        return gthExperienciaLaboralEmpleaCollection;
    }

    public void setGthExperienciaLaboralEmpleaCollection(Collection<GthExperienciaLaboralEmplea> gthExperienciaLaboralEmpleaCollection) {
        this.gthExperienciaLaboralEmpleaCollection = gthExperienciaLaboralEmpleaCollection;
    }

    @XmlTransient
    public Collection<GthViaticos> getGthViaticosCollection() {
        return gthViaticosCollection;
    }

    public void setGthViaticosCollection(Collection<GthViaticos> gthViaticosCollection) {
        this.gthViaticosCollection = gthViaticosCollection;
    }

    @XmlTransient
    public Collection<GthVehiculoEmpleado> getGthVehiculoEmpleadoCollection() {
        return gthVehiculoEmpleadoCollection;
    }

    public void setGthVehiculoEmpleadoCollection(Collection<GthVehiculoEmpleado> gthVehiculoEmpleadoCollection) {
        this.gthVehiculoEmpleadoCollection = gthVehiculoEmpleadoCollection;
    }

    @XmlTransient
    public Collection<GthInversionEmpleado> getGthInversionEmpleadoCollection() {
        return gthInversionEmpleadoCollection;
    }

    public void setGthInversionEmpleadoCollection(Collection<GthInversionEmpleado> gthInversionEmpleadoCollection) {
        this.gthInversionEmpleadoCollection = gthInversionEmpleadoCollection;
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
        hash += (ideGeins != null ? ideGeins.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenInstitucion)) {
            return false;
        }
        GenInstitucion other = (GenInstitucion) object;
        if ((this.ideGeins == null && other.ideGeins != null) || (this.ideGeins != null && !this.ideGeins.equals(other.ideGeins))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenInstitucion[ ideGeins=" + ideGeins + " ]";
    }
    
}

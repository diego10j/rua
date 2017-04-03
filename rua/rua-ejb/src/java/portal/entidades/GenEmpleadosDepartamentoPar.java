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
import javax.persistence.JoinColumns;
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
@Table(name = "gen_empleados_departamento_par")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findAll", query = "SELECT g FROM GenEmpleadosDepartamentoPar g"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByIdeGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.ideGeedp = :ideGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByFechaGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.fechaGeedp = :fechaGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByFechaFinctrGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.fechaFinctrGeedp = :fechaFinctrGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByRmuGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.rmuGeedp = :rmuGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByAjusteSueldoGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.ajusteSueldoGeedp = :ajusteSueldoGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByFechaEncargoGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.fechaEncargoGeedp = :fechaEncargoGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByFechaAjusteGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.fechaAjusteGeedp = :fechaAjusteGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByFechaLiquidacionGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.fechaLiquidacionGeedp = :fechaLiquidacionGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByLiquidacionGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.liquidacionGeedp = :liquidacionGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByFechaEncargoFinGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.fechaEncargoFinGeedp = :fechaEncargoFinGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findBySueldoSubrogaGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.sueldoSubrogaGeedp = :sueldoSubrogaGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByEjecutoLiquidacionGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.ejecutoLiquidacionGeedp = :ejecutoLiquidacionGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByObservacionGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.observacionGeedp = :observacionGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByUsuarioIngre", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByFechaIngre", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByUsuarioActua", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByFechaActua", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByHoraIngre", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByHoraActua", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByActivoGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.activoGeedp = :activoGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByLineaSupervicionGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.lineaSupervicionGeedp = :lineaSupervicionGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByAcumulaFondosGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.acumulaFondosGeedp = :acumulaFondosGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByControlAsistenciaGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.controlAsistenciaGeedp = :controlAsistenciaGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByEncargadoSubrogadoGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.encargadoSubrogadoGeedp = :encargadoSubrogadoGeedp")})
public class GenEmpleadosDepartamentoPar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_geedp")
    private Integer ideGeedp;
    @Basic(optional = false)
    @Column(name = "fecha_geedp")
    @Temporal(TemporalType.DATE)
    private Date fechaGeedp;
    @Column(name = "fecha_finctr_geedp")
    @Temporal(TemporalType.DATE)
    private Date fechaFinctrGeedp;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "rmu_geedp")
    private BigDecimal rmuGeedp;
    @Column(name = "ajuste_sueldo_geedp")
    private BigDecimal ajusteSueldoGeedp;
    @Column(name = "fecha_encargo_geedp")
    @Temporal(TemporalType.DATE)
    private Date fechaEncargoGeedp;
    @Column(name = "fecha_ajuste_geedp")
    @Temporal(TemporalType.DATE)
    private Date fechaAjusteGeedp;
    @Column(name = "fecha_liquidacion_geedp")
    @Temporal(TemporalType.DATE)
    private Date fechaLiquidacionGeedp;
    @Column(name = "liquidacion_geedp")
    private Integer liquidacionGeedp;
    @Column(name = "fecha_encargo_fin_geedp")
    @Temporal(TemporalType.DATE)
    private Date fechaEncargoFinGeedp;
    @Column(name = "sueldo_subroga_geedp")
    private BigDecimal sueldoSubrogaGeedp;
    @Column(name = "ejecuto_liquidacion_geedp")
    private Integer ejecutoLiquidacionGeedp;
    @Column(name = "observacion_geedp")
    private String observacionGeedp;
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
    @Column(name = "activo_geedp")
    private Boolean activoGeedp;
    @Column(name = "linea_supervicion_geedp")
    private Boolean lineaSupervicionGeedp;
    @Column(name = "acumula_fondos_geedp")
    private Boolean acumulaFondosGeedp;
    @Column(name = "control_asistencia_geedp")
    private Boolean controlAsistenciaGeedp;
    @Column(name = "encargado_subrogado_geedp")
    private Boolean encargadoSubrogadoGeedp;
    @OneToMany(mappedBy = "genIdeGeedp")
    private Collection<AsiPermisosVacacionHext> asiPermisosVacacionHextCollection;
    @OneToMany(mappedBy = "genIdeGeedp3")
    private Collection<AsiPermisosVacacionHext> asiPermisosVacacionHextCollection1;
    @OneToMany(mappedBy = "genIdeGeedp2")
    private Collection<AsiPermisosVacacionHext> asiPermisosVacacionHextCollection2;
    @OneToMany(mappedBy = "ideGeedp")
    private Collection<AsiPermisosVacacionHext> asiPermisosVacacionHextCollection3;
    @OneToMany(mappedBy = "ideGeedp")
    private Collection<SriProyeccionIngres> sriProyeccionIngresCollection;

    @OneToMany(mappedBy = "ideGeedp")
    private Collection<AsiValidaNomina> asiValidaNominaCollection;
    @OneToMany(mappedBy = "ideGeedp")
    private Collection<GenModuloAdjudicador> genModuloAdjudicadorCollection;
    @OneToMany(mappedBy = "genIdeGeedp")
    private Collection<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaCollection;
    @OneToMany(mappedBy = "ideGeedp")
    private Collection<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaCollection1;
    @OneToMany(mappedBy = "genIdeGeedp2")
    private Collection<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaCollection2;
    @OneToMany(mappedBy = "genIdeGeedp2")
    private Collection<NrhAnticipo> nrhAnticipoCollection;
    @OneToMany(mappedBy = "genIdeGeedp3")
    private Collection<NrhAnticipo> nrhAnticipoCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGeedp")
    private Collection<NrhAnticipo> nrhAnticipoCollection2;
    @OneToMany(mappedBy = "genIdeGeedp")
    private Collection<NrhAnticipo> nrhAnticipoCollection3;

    @JoinColumn(name = "ide_gttsi", referencedColumnName = "ide_gttsi")
    @ManyToOne(optional = false)
    private GthTipoSindicato ideGttsi;
    @JoinColumn(name = "ide_gttem", referencedColumnName = "ide_gttem")
    @ManyToOne(optional = false)
    private GthTipoEmpleado ideGttem;
    @JoinColumn(name = "ide_gttco", referencedColumnName = "ide_gttco")
    @ManyToOne(optional = false)
    private GthTipoContrato ideGttco;
    @JoinColumn(name = "ide_gtgre", referencedColumnName = "ide_gtgre")
    @ManyToOne
    private GthGrupoEmpleado ideGtgre;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne(optional = false)
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_getiv", referencedColumnName = "ide_getiv")
    @ManyToOne
    private GenTipoVinculacion ideGetiv;
    @JoinColumns({
        @JoinColumn(name = "ide_gepgc", referencedColumnName = "ide_gepgc"),
        @JoinColumn(name = "ide_gegro", referencedColumnName = "ide_gegro"),
        @JoinColumn(name = "ide_gecaf", referencedColumnName = "ide_gecaf"),
        @JoinColumn(name = "ide_sucu", referencedColumnName = "ide_sucu"),
        @JoinColumn(name = "ide_gedep", referencedColumnName = "ide_gedep"),
        @JoinColumn(name = "ide_geare", referencedColumnName = "ide_geare")})
    @ManyToOne(optional = false)
    private GenPartidaGrupoCargo genPartidaGrupoCargo;
    @JoinColumns({
        @JoinColumn(name = "gen_ide_gegro", referencedColumnName = "ide_gegro"),
        @JoinColumn(name = "gen_ide_gecaf", referencedColumnName = "ide_gecaf")})
    @ManyToOne
    private GenGrupoCargo genGrupoCargo;
    @JoinColumn(name = "ide_geded", referencedColumnName = "ide_geded")
    @ManyToOne
    private GenDetalleEmpleadoDepartame ideGeded;
    @JoinColumn(name = "ide_gecae", referencedColumnName = "ide_gecae")
    @ManyToOne
    private GenCategoriaEstatus ideGecae;
    @OneToMany(mappedBy = "ideGeedp")
    private Collection<NrhGarante> nrhGaranteCollection;

    @OneToMany(mappedBy = "genIdeGeedp")
    private Collection<NrhBeneficioEmpleado> nrhBeneficioEmpleadoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGeedp")
    private Collection<NrhBeneficioEmpleado> nrhBeneficioEmpleadoCollection1;
    @OneToMany(mappedBy = "genIdeGeedp2")
    private Collection<NrhBeneficioEmpleado> nrhBeneficioEmpleadoCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGeedp")
    private Collection<NrhDetalleRol> nrhDetalleRolCollection;
    @OneToMany(mappedBy = "genIdeGeedp")
    private Collection<GthViaticos> gthViaticosCollection;
    @OneToMany(mappedBy = "ideGeedp")
    private Collection<GthViaticos> gthViaticosCollection1;
    @OneToMany(mappedBy = "genIdeGeedp3")
    private Collection<GthViaticos> gthViaticosCollection2;
    @OneToMany(mappedBy = "genIdeGeedp2")
    private Collection<GthViaticos> gthViaticosCollection3;

    @OneToMany(mappedBy = "ideGeedp")
    private Collection<NrhRetencionJudicial> nrhRetencionJudicialCollection;

    public GenEmpleadosDepartamentoPar() {
    }

    public GenEmpleadosDepartamentoPar(Integer ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public GenEmpleadosDepartamentoPar(Integer ideGeedp, Date fechaGeedp, BigDecimal rmuGeedp) {
        this.ideGeedp = ideGeedp;
        this.fechaGeedp = fechaGeedp;
        this.rmuGeedp = rmuGeedp;
    }

    public Integer getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(Integer ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public Date getFechaGeedp() {
        return fechaGeedp;
    }

    public void setFechaGeedp(Date fechaGeedp) {
        this.fechaGeedp = fechaGeedp;
    }

    public Date getFechaFinctrGeedp() {
        return fechaFinctrGeedp;
    }

    public void setFechaFinctrGeedp(Date fechaFinctrGeedp) {
        this.fechaFinctrGeedp = fechaFinctrGeedp;
    }

    public BigDecimal getRmuGeedp() {
        return rmuGeedp;
    }

    public void setRmuGeedp(BigDecimal rmuGeedp) {
        this.rmuGeedp = rmuGeedp;
    }

    public BigDecimal getAjusteSueldoGeedp() {
        return ajusteSueldoGeedp;
    }

    public void setAjusteSueldoGeedp(BigDecimal ajusteSueldoGeedp) {
        this.ajusteSueldoGeedp = ajusteSueldoGeedp;
    }

    public Date getFechaEncargoGeedp() {
        return fechaEncargoGeedp;
    }

    public void setFechaEncargoGeedp(Date fechaEncargoGeedp) {
        this.fechaEncargoGeedp = fechaEncargoGeedp;
    }

    public Date getFechaAjusteGeedp() {
        return fechaAjusteGeedp;
    }

    public void setFechaAjusteGeedp(Date fechaAjusteGeedp) {
        this.fechaAjusteGeedp = fechaAjusteGeedp;
    }

    public Date getFechaLiquidacionGeedp() {
        return fechaLiquidacionGeedp;
    }

    public void setFechaLiquidacionGeedp(Date fechaLiquidacionGeedp) {
        this.fechaLiquidacionGeedp = fechaLiquidacionGeedp;
    }

    public Integer getLiquidacionGeedp() {
        return liquidacionGeedp;
    }

    public void setLiquidacionGeedp(Integer liquidacionGeedp) {
        this.liquidacionGeedp = liquidacionGeedp;
    }

    public Date getFechaEncargoFinGeedp() {
        return fechaEncargoFinGeedp;
    }

    public void setFechaEncargoFinGeedp(Date fechaEncargoFinGeedp) {
        this.fechaEncargoFinGeedp = fechaEncargoFinGeedp;
    }

    public BigDecimal getSueldoSubrogaGeedp() {
        return sueldoSubrogaGeedp;
    }

    public void setSueldoSubrogaGeedp(BigDecimal sueldoSubrogaGeedp) {
        this.sueldoSubrogaGeedp = sueldoSubrogaGeedp;
    }

    public Integer getEjecutoLiquidacionGeedp() {
        return ejecutoLiquidacionGeedp;
    }

    public void setEjecutoLiquidacionGeedp(Integer ejecutoLiquidacionGeedp) {
        this.ejecutoLiquidacionGeedp = ejecutoLiquidacionGeedp;
    }

    public String getObservacionGeedp() {
        return observacionGeedp;
    }

    public void setObservacionGeedp(String observacionGeedp) {
        this.observacionGeedp = observacionGeedp;
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

    public Boolean getActivoGeedp() {
        return activoGeedp;
    }

    public void setActivoGeedp(Boolean activoGeedp) {
        this.activoGeedp = activoGeedp;
    }

    public Boolean getLineaSupervicionGeedp() {
        return lineaSupervicionGeedp;
    }

    public void setLineaSupervicionGeedp(Boolean lineaSupervicionGeedp) {
        this.lineaSupervicionGeedp = lineaSupervicionGeedp;
    }

    public Boolean getAcumulaFondosGeedp() {
        return acumulaFondosGeedp;
    }

    public void setAcumulaFondosGeedp(Boolean acumulaFondosGeedp) {
        this.acumulaFondosGeedp = acumulaFondosGeedp;
    }

    public Boolean getControlAsistenciaGeedp() {
        return controlAsistenciaGeedp;
    }

    public void setControlAsistenciaGeedp(Boolean controlAsistenciaGeedp) {
        this.controlAsistenciaGeedp = controlAsistenciaGeedp;
    }

    public Boolean getEncargadoSubrogadoGeedp() {
        return encargadoSubrogadoGeedp;
    }

    public void setEncargadoSubrogadoGeedp(Boolean encargadoSubrogadoGeedp) {
        this.encargadoSubrogadoGeedp = encargadoSubrogadoGeedp;
    }

    @XmlTransient
    public Collection<AsiPermisosVacacionHext> getAsiPermisosVacacionHextCollection() {
        return asiPermisosVacacionHextCollection;
    }

    public void setAsiPermisosVacacionHextCollection(Collection<AsiPermisosVacacionHext> asiPermisosVacacionHextCollection) {
        this.asiPermisosVacacionHextCollection = asiPermisosVacacionHextCollection;
    }

    @XmlTransient
    public Collection<AsiPermisosVacacionHext> getAsiPermisosVacacionHextCollection1() {
        return asiPermisosVacacionHextCollection1;
    }

    public void setAsiPermisosVacacionHextCollection1(Collection<AsiPermisosVacacionHext> asiPermisosVacacionHextCollection1) {
        this.asiPermisosVacacionHextCollection1 = asiPermisosVacacionHextCollection1;
    }

    @XmlTransient
    public Collection<AsiPermisosVacacionHext> getAsiPermisosVacacionHextCollection2() {
        return asiPermisosVacacionHextCollection2;
    }

    public void setAsiPermisosVacacionHextCollection2(Collection<AsiPermisosVacacionHext> asiPermisosVacacionHextCollection2) {
        this.asiPermisosVacacionHextCollection2 = asiPermisosVacacionHextCollection2;
    }

    @XmlTransient
    public Collection<AsiPermisosVacacionHext> getAsiPermisosVacacionHextCollection3() {
        return asiPermisosVacacionHextCollection3;
    }

    public void setAsiPermisosVacacionHextCollection3(Collection<AsiPermisosVacacionHext> asiPermisosVacacionHextCollection3) {
        this.asiPermisosVacacionHextCollection3 = asiPermisosVacacionHextCollection3;
    }

    @XmlTransient
    public Collection<SriProyeccionIngres> getSriProyeccionIngresCollection() {
        return sriProyeccionIngresCollection;
    }

    public void setSriProyeccionIngresCollection(Collection<SriProyeccionIngres> sriProyeccionIngresCollection) {
        this.sriProyeccionIngresCollection = sriProyeccionIngresCollection;
    }

     @XmlTransient
    public Collection<AsiValidaNomina> getAsiValidaNominaCollection() {
        return asiValidaNominaCollection;
    }

    public void setAsiValidaNominaCollection(Collection<AsiValidaNomina> asiValidaNominaCollection) {
        this.asiValidaNominaCollection = asiValidaNominaCollection;
    }

    @XmlTransient
    public Collection<GenModuloAdjudicador> getGenModuloAdjudicadorCollection() {
        return genModuloAdjudicadorCollection;
    }

    public void setGenModuloAdjudicadorCollection(Collection<GenModuloAdjudicador> genModuloAdjudicadorCollection) {
        this.genModuloAdjudicadorCollection = genModuloAdjudicadorCollection;
    }

    @XmlTransient
    public Collection<NrhDetalleFacturaGuarderia> getNrhDetalleFacturaGuarderiaCollection() {
        return nrhDetalleFacturaGuarderiaCollection;
    }

    public void setNrhDetalleFacturaGuarderiaCollection(Collection<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaCollection) {
        this.nrhDetalleFacturaGuarderiaCollection = nrhDetalleFacturaGuarderiaCollection;
    }

    @XmlTransient
    public Collection<NrhDetalleFacturaGuarderia> getNrhDetalleFacturaGuarderiaCollection1() {
        return nrhDetalleFacturaGuarderiaCollection1;
    }

    public void setNrhDetalleFacturaGuarderiaCollection1(Collection<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaCollection1) {
        this.nrhDetalleFacturaGuarderiaCollection1 = nrhDetalleFacturaGuarderiaCollection1;
    }

    @XmlTransient
    public Collection<NrhDetalleFacturaGuarderia> getNrhDetalleFacturaGuarderiaCollection2() {
        return nrhDetalleFacturaGuarderiaCollection2;
    }

    public void setNrhDetalleFacturaGuarderiaCollection2(Collection<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaCollection2) {
        this.nrhDetalleFacturaGuarderiaCollection2 = nrhDetalleFacturaGuarderiaCollection2;
    }

    @XmlTransient
    public Collection<NrhAnticipo> getNrhAnticipoCollection() {
        return nrhAnticipoCollection;
    }

    public void setNrhAnticipoCollection(Collection<NrhAnticipo> nrhAnticipoCollection) {
        this.nrhAnticipoCollection = nrhAnticipoCollection;
    }

    @XmlTransient
    public Collection<NrhAnticipo> getNrhAnticipoCollection1() {
        return nrhAnticipoCollection1;
    }

    public void setNrhAnticipoCollection1(Collection<NrhAnticipo> nrhAnticipoCollection1) {
        this.nrhAnticipoCollection1 = nrhAnticipoCollection1;
    }

    @XmlTransient
    public Collection<NrhAnticipo> getNrhAnticipoCollection2() {
        return nrhAnticipoCollection2;
    }

    public void setNrhAnticipoCollection2(Collection<NrhAnticipo> nrhAnticipoCollection2) {
        this.nrhAnticipoCollection2 = nrhAnticipoCollection2;
    }

    @XmlTransient
    public Collection<NrhAnticipo> getNrhAnticipoCollection3() {
        return nrhAnticipoCollection3;
    }

    public void setNrhAnticipoCollection3(Collection<NrhAnticipo> nrhAnticipoCollection3) {
        this.nrhAnticipoCollection3 = nrhAnticipoCollection3;
    }

    public GthTipoSindicato getIdeGttsi() {
        return ideGttsi;
    }

    public void setIdeGttsi(GthTipoSindicato ideGttsi) {
        this.ideGttsi = ideGttsi;
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

    public GthGrupoEmpleado getIdeGtgre() {
        return ideGtgre;
    }

    public void setIdeGtgre(GthGrupoEmpleado ideGtgre) {
        this.ideGtgre = ideGtgre;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenTipoVinculacion getIdeGetiv() {
        return ideGetiv;
    }

    public void setIdeGetiv(GenTipoVinculacion ideGetiv) {
        this.ideGetiv = ideGetiv;
    }

    public GenPartidaGrupoCargo getGenPartidaGrupoCargo() {
        return genPartidaGrupoCargo;
    }

    public void setGenPartidaGrupoCargo(GenPartidaGrupoCargo genPartidaGrupoCargo) {
        this.genPartidaGrupoCargo = genPartidaGrupoCargo;
    }

    public GenGrupoCargo getGenGrupoCargo() {
        return genGrupoCargo;
    }

    public void setGenGrupoCargo(GenGrupoCargo genGrupoCargo) {
        this.genGrupoCargo = genGrupoCargo;
    }

    public GenDetalleEmpleadoDepartame getIdeGeded() {
        return ideGeded;
    }

    public void setIdeGeded(GenDetalleEmpleadoDepartame ideGeded) {
        this.ideGeded = ideGeded;
    }

    public GenCategoriaEstatus getIdeGecae() {
        return ideGecae;
    }

    public void setIdeGecae(GenCategoriaEstatus ideGecae) {
        this.ideGecae = ideGecae;
    }

    @XmlTransient
    public Collection<NrhGarante> getNrhGaranteCollection() {
        return nrhGaranteCollection;
    }

    public void setNrhGaranteCollection(Collection<NrhGarante> nrhGaranteCollection) {
        this.nrhGaranteCollection = nrhGaranteCollection;
    }


    @XmlTransient
    public Collection<NrhBeneficioEmpleado> getNrhBeneficioEmpleadoCollection() {
        return nrhBeneficioEmpleadoCollection;
    }

    public void setNrhBeneficioEmpleadoCollection(Collection<NrhBeneficioEmpleado> nrhBeneficioEmpleadoCollection) {
        this.nrhBeneficioEmpleadoCollection = nrhBeneficioEmpleadoCollection;
    }

    @XmlTransient
    public Collection<NrhBeneficioEmpleado> getNrhBeneficioEmpleadoCollection1() {
        return nrhBeneficioEmpleadoCollection1;
    }

    public void setNrhBeneficioEmpleadoCollection1(Collection<NrhBeneficioEmpleado> nrhBeneficioEmpleadoCollection1) {
        this.nrhBeneficioEmpleadoCollection1 = nrhBeneficioEmpleadoCollection1;
    }

    @XmlTransient
    public Collection<NrhBeneficioEmpleado> getNrhBeneficioEmpleadoCollection2() {
        return nrhBeneficioEmpleadoCollection2;
    }

    public void setNrhBeneficioEmpleadoCollection2(Collection<NrhBeneficioEmpleado> nrhBeneficioEmpleadoCollection2) {
        this.nrhBeneficioEmpleadoCollection2 = nrhBeneficioEmpleadoCollection2;
    }

    @XmlTransient
    public Collection<NrhDetalleRol> getNrhDetalleRolCollection() {
        return nrhDetalleRolCollection;
    }

    public void setNrhDetalleRolCollection(Collection<NrhDetalleRol> nrhDetalleRolCollection) {
        this.nrhDetalleRolCollection = nrhDetalleRolCollection;
    }

    @XmlTransient
    public Collection<GthViaticos> getGthViaticosCollection() {
        return gthViaticosCollection;
    }

    public void setGthViaticosCollection(Collection<GthViaticos> gthViaticosCollection) {
        this.gthViaticosCollection = gthViaticosCollection;
    }

    @XmlTransient
    public Collection<GthViaticos> getGthViaticosCollection1() {
        return gthViaticosCollection1;
    }

    public void setGthViaticosCollection1(Collection<GthViaticos> gthViaticosCollection1) {
        this.gthViaticosCollection1 = gthViaticosCollection1;
    }

    @XmlTransient
    public Collection<GthViaticos> getGthViaticosCollection2() {
        return gthViaticosCollection2;
    }

    public void setGthViaticosCollection2(Collection<GthViaticos> gthViaticosCollection2) {
        this.gthViaticosCollection2 = gthViaticosCollection2;
    }

    @XmlTransient
    public Collection<GthViaticos> getGthViaticosCollection3() {
        return gthViaticosCollection3;
    }

    public void setGthViaticosCollection3(Collection<GthViaticos> gthViaticosCollection3) {
        this.gthViaticosCollection3 = gthViaticosCollection3;
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
        hash += (ideGeedp != null ? ideGeedp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenEmpleadosDepartamentoPar)) {
            return false;
        }
        GenEmpleadosDepartamentoPar other = (GenEmpleadosDepartamentoPar) object;
        if ((this.ideGeedp == null && other.ideGeedp != null) || (this.ideGeedp != null && !this.ideGeedp.equals(other.ideGeedp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenEmpleadosDepartamentoPar[ ideGeedp=" + ideGeedp + " ]";
    }
    
}

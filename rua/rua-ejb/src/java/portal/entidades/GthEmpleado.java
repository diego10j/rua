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
import javax.persistence.Lob;
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
@Table(name = "gth_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthEmpleado.findAll", query = "SELECT g FROM GthEmpleado g"),
    @NamedQuery(name = "GthEmpleado.findByIdeGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.ideGtemp = :ideGtemp"),
    @NamedQuery(name = "GthEmpleado.findByDocumentoIdentidadGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.documentoIdentidadGtemp = :documentoIdentidadGtemp"),
    @NamedQuery(name = "GthEmpleado.findByFechaIngresoPaisGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.fechaIngresoPaisGtemp = :fechaIngresoPaisGtemp"),
    @NamedQuery(name = "GthEmpleado.findByCarnetExtranjeriaGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.carnetExtranjeriaGtemp = :carnetExtranjeriaGtemp"),
    @NamedQuery(name = "GthEmpleado.findByPrimerNombreGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.primerNombreGtemp = :primerNombreGtemp"),
    @NamedQuery(name = "GthEmpleado.findBySegundoNombreGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.segundoNombreGtemp = :segundoNombreGtemp"),
    @NamedQuery(name = "GthEmpleado.findByApellidoPaternoGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.apellidoPaternoGtemp = :apellidoPaternoGtemp"),
    @NamedQuery(name = "GthEmpleado.findByApellidoMaternoGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.apellidoMaternoGtemp = :apellidoMaternoGtemp"),
    @NamedQuery(name = "GthEmpleado.findByFechaNacimientoGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.fechaNacimientoGtemp = :fechaNacimientoGtemp"),
    @NamedQuery(name = "GthEmpleado.findByCargoPublicoGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.cargoPublicoGtemp = :cargoPublicoGtemp"),
    @NamedQuery(name = "GthEmpleado.findByFechaIngresoGrupoGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.fechaIngresoGrupoGtemp = :fechaIngresoGrupoGtemp"),
    @NamedQuery(name = "GthEmpleado.findByFechaIngresoGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.fechaIngresoGtemp = :fechaIngresoGtemp"),
    @NamedQuery(name = "GthEmpleado.findByTarjetaMarcacionGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.tarjetaMarcacionGtemp = :tarjetaMarcacionGtemp"),
    @NamedQuery(name = "GthEmpleado.findByActivoGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.activoGtemp = :activoGtemp"),
    @NamedQuery(name = "GthEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthEmpleado.findByFechaIngre", query = "SELECT g FROM GthEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthEmpleado.findByUsuarioActua", query = "SELECT g FROM GthEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthEmpleado.findByFechaActua", query = "SELECT g FROM GthEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthEmpleado.findByHoraIngre", query = "SELECT g FROM GthEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthEmpleado.findByHoraActua", query = "SELECT g FROM GthEmpleado g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GthEmpleado.findBySeparacionBienesGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.separacionBienesGtemp = :separacionBienesGtemp"),
    @NamedQuery(name = "GthEmpleado.findByDiscapacitadoGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.discapacitadoGtemp = :discapacitadoGtemp"),
    @NamedQuery(name = "GthEmpleado.findByAcumulaDecimoGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.acumulaDecimoGtemp = :acumulaDecimoGtemp")})
public class GthEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtemp")
    private Integer ideGtemp;
    @Basic(optional = false)
    @Column(name = "documento_identidad_gtemp")
    private String documentoIdentidadGtemp;
    @Column(name = "fecha_ingreso_pais_gtemp")
    @Temporal(TemporalType.DATE)
    private Date fechaIngresoPaisGtemp;
    @Column(name = "carnet_extranjeria_gtemp")
    private String carnetExtranjeriaGtemp;
    @Basic(optional = false)
    @Column(name = "primer_nombre_gtemp")
    private String primerNombreGtemp;
    @Column(name = "segundo_nombre_gtemp")
    private String segundoNombreGtemp;
    @Basic(optional = false)
    @Column(name = "apellido_paterno_gtemp")
    private String apellidoPaternoGtemp;
    @Column(name = "apellido_materno_gtemp")
    private String apellidoMaternoGtemp;
    @Basic(optional = false)
    @Column(name = "fecha_nacimiento_gtemp")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimientoGtemp;
    @Column(name = "cargo_publico_gtemp")
    private Integer cargoPublicoGtemp;
    @Basic(optional = false)
    @Column(name = "fecha_ingreso_grupo_gtemp")
    @Temporal(TemporalType.DATE)
    private Date fechaIngresoGrupoGtemp;
    @Column(name = "fecha_ingreso_gtemp")
    @Temporal(TemporalType.DATE)
    private Date fechaIngresoGtemp;
    @Column(name = "tarjeta_marcacion_gtemp")
    private String tarjetaMarcacionGtemp;
    @Basic(optional = false)
    @Column(name = "activo_gtemp")
    private boolean activoGtemp;
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
    @Column(name = "separacion_bienes_gtemp")
    private Boolean separacionBienesGtemp;
    @Column(name = "discapacitado_gtemp")
    private Boolean discapacitadoGtemp;
    @Column(name = "acumula_decimo_gtemp")
    private Boolean acumulaDecimoGtemp;
    @Lob
    @Column(name = "path_foto_gtemp")
    private byte[] pathFotoGtemp;
    @Lob
    @Column(name = "path_firma_gtemp")
    private byte[] pathFirmaGtemp;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<AsiPermisosVacacionHext> asiPermisosVacacionHextCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<AsiNovedadDetalle> asiNovedadDetalleCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<SriProyeccionIngres> sriProyeccionIngresCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GenDetalleEmpleadoDepartame> genDetalleEmpleadoDepartameCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthDireccion> gthDireccionCollection;

    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthEndeudamientoEmpleado> gthEndeudamientoEmpleadoCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthSeguroVida> gthSeguroVidaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gthEmpleado")
    private Collection<GthRegistroMilitar> gthRegistroMilitarCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthTelefono> gthTelefonoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtemp")
    private Collection<NrhAnticipo> nrhAnticipoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtemp")
    private Collection<GthConyuge> gthConyugeCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthTerrenoEmpleado> gthTerrenoEmpleadoCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthIdiomaEmpleado> gthIdiomaEmpleadoCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthFichaValoracion> gthFichaValoracionCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthCuentaAnticipo> gthCuentaAnticipoCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthSituacionEconomicaEmplea> gthSituacionEconomicaEmpleaCollection;
    @JoinColumn(name = "ide_gttis", referencedColumnName = "ide_gttis")
    @ManyToOne(optional = false)
    private GthTipoSangre ideGttis;
    @JoinColumn(name = "ide_gttdi", referencedColumnName = "ide_gttdi")
    @ManyToOne(optional = false)
    private GthTipoDocumentoIdentidad ideGttdi;
    @JoinColumn(name = "ide_gtnac", referencedColumnName = "ide_gtnac")
    @ManyToOne(optional = false)
    private GthNacionalidad ideGtnac;
    @JoinColumn(name = "ide_gtgen", referencedColumnName = "ide_gtgen")
    @ManyToOne(optional = false)
    private GthGenero ideGtgen;
    @JoinColumn(name = "ide_gtesc", referencedColumnName = "ide_gtesc")
    @ManyToOne(optional = false)
    private GthEstadoCivil ideGtesc;
    @JoinColumn(name = "ide_gedip", referencedColumnName = "ide_gedip")
    @ManyToOne(optional = false)
    private GenDivisionPolitica ideGedip;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthTarjetaCreditoEmpleado> gthTarjetaCreditoEmpleadoCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<AsiVacacion> asiVacacionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtemp")
    private Collection<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthCasaEmpleado> gthCasaEmpleadoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtemp")
    private Collection<SriDeduciblesEmpleado> sriDeduciblesEmpleadoCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthCuentaBancariaEmpleado> gthCuentaBancariaEmpleadoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtemp")
    private Collection<NrhBeneficioEmpleado> nrhBeneficioEmpleadoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtemp")
    private Collection<GthDiscapacidadEmpleado> gthDiscapacidadEmpleadoCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthExperienciaDocenteEmplea> gthExperienciaDocenteEmpleaCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthCapacitacionEmpleado> gthCapacitacionEmpleadoCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthCorreo> gthCorreoCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthArchivoEmpleado> gthArchivoEmpleadoCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthAmigosEmpresaEmplea> gthAmigosEmpresaEmpleaCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthSituacionFinancieraEmple> gthSituacionFinancieraEmpleCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthEducacionEmpleado> gthEducacionEmpleadoCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthNegocioEmpleado> gthNegocioEmpleadoCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthExperienciaLaboralEmplea> gthExperienciaLaboralEmpleaCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthViaticos> gthViaticosCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthVehiculoEmpleado> gthVehiculoEmpleadoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gthEmpleado")
    private Collection<GthHobie> gthHobieCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthFamiliar> gthFamiliarCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtemp")
    private Collection<GthCargasFamiliares> gthCargasFamiliaresCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gthEmpleado")
    private Collection<GthPersonaEmergencia> gthPersonaEmergenciaCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthInversionEmpleado> gthInversionEmpleadoCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<GthDocumentacionEmpleado> gthDocumentacionEmpleadoCollection;
    @OneToMany(mappedBy = "ideGtemp")
    private Collection<NrhRetencionJudicial> nrhRetencionJudicialCollection;

    public GthEmpleado() {
    }

    public GthEmpleado(Integer ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GthEmpleado(Integer ideGtemp, String documentoIdentidadGtemp, String primerNombreGtemp, String apellidoPaternoGtemp, Date fechaNacimientoGtemp, Date fechaIngresoGrupoGtemp, boolean activoGtemp) {
        this.ideGtemp = ideGtemp;
        this.documentoIdentidadGtemp = documentoIdentidadGtemp;
        this.primerNombreGtemp = primerNombreGtemp;
        this.apellidoPaternoGtemp = apellidoPaternoGtemp;
        this.fechaNacimientoGtemp = fechaNacimientoGtemp;
        this.fechaIngresoGrupoGtemp = fechaIngresoGrupoGtemp;
        this.activoGtemp = activoGtemp;
    }

    public Integer getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(Integer ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public String getDocumentoIdentidadGtemp() {
        return documentoIdentidadGtemp;
    }

    public void setDocumentoIdentidadGtemp(String documentoIdentidadGtemp) {
        this.documentoIdentidadGtemp = documentoIdentidadGtemp;
    }

    public Date getFechaIngresoPaisGtemp() {
        return fechaIngresoPaisGtemp;
    }

    public void setFechaIngresoPaisGtemp(Date fechaIngresoPaisGtemp) {
        this.fechaIngresoPaisGtemp = fechaIngresoPaisGtemp;
    }

    public String getCarnetExtranjeriaGtemp() {
        return carnetExtranjeriaGtemp;
    }

    public void setCarnetExtranjeriaGtemp(String carnetExtranjeriaGtemp) {
        this.carnetExtranjeriaGtemp = carnetExtranjeriaGtemp;
    }

    public String getPrimerNombreGtemp() {
        return primerNombreGtemp;
    }

    public void setPrimerNombreGtemp(String primerNombreGtemp) {
        this.primerNombreGtemp = primerNombreGtemp;
    }

    public String getSegundoNombreGtemp() {
        return segundoNombreGtemp;
    }

    public void setSegundoNombreGtemp(String segundoNombreGtemp) {
        this.segundoNombreGtemp = segundoNombreGtemp;
    }

    public String getApellidoPaternoGtemp() {
        return apellidoPaternoGtemp;
    }

    public void setApellidoPaternoGtemp(String apellidoPaternoGtemp) {
        this.apellidoPaternoGtemp = apellidoPaternoGtemp;
    }

    public String getApellidoMaternoGtemp() {
        return apellidoMaternoGtemp;
    }

    public void setApellidoMaternoGtemp(String apellidoMaternoGtemp) {
        this.apellidoMaternoGtemp = apellidoMaternoGtemp;
    }

    public Date getFechaNacimientoGtemp() {
        return fechaNacimientoGtemp;
    }

    public void setFechaNacimientoGtemp(Date fechaNacimientoGtemp) {
        this.fechaNacimientoGtemp = fechaNacimientoGtemp;
    }

    public Integer getCargoPublicoGtemp() {
        return cargoPublicoGtemp;
    }

    public void setCargoPublicoGtemp(Integer cargoPublicoGtemp) {
        this.cargoPublicoGtemp = cargoPublicoGtemp;
    }

    public Date getFechaIngresoGrupoGtemp() {
        return fechaIngresoGrupoGtemp;
    }

    public void setFechaIngresoGrupoGtemp(Date fechaIngresoGrupoGtemp) {
        this.fechaIngresoGrupoGtemp = fechaIngresoGrupoGtemp;
    }

    public Date getFechaIngresoGtemp() {
        return fechaIngresoGtemp;
    }

    public void setFechaIngresoGtemp(Date fechaIngresoGtemp) {
        this.fechaIngresoGtemp = fechaIngresoGtemp;
    }

    public String getTarjetaMarcacionGtemp() {
        return tarjetaMarcacionGtemp;
    }

    public void setTarjetaMarcacionGtemp(String tarjetaMarcacionGtemp) {
        this.tarjetaMarcacionGtemp = tarjetaMarcacionGtemp;
    }

    public boolean getActivoGtemp() {
        return activoGtemp;
    }

    public void setActivoGtemp(boolean activoGtemp) {
        this.activoGtemp = activoGtemp;
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

    public Boolean getSeparacionBienesGtemp() {
        return separacionBienesGtemp;
    }

    public void setSeparacionBienesGtemp(Boolean separacionBienesGtemp) {
        this.separacionBienesGtemp = separacionBienesGtemp;
    }

    public Boolean getDiscapacitadoGtemp() {
        return discapacitadoGtemp;
    }

    public void setDiscapacitadoGtemp(Boolean discapacitadoGtemp) {
        this.discapacitadoGtemp = discapacitadoGtemp;
    }

    public Boolean getAcumulaDecimoGtemp() {
        return acumulaDecimoGtemp;
    }

    public void setAcumulaDecimoGtemp(Boolean acumulaDecimoGtemp) {
        this.acumulaDecimoGtemp = acumulaDecimoGtemp;
    }

    public byte[] getPathFotoGtemp() {
        return pathFotoGtemp;
    }

    public void setPathFotoGtemp(byte[] pathFotoGtemp) {
        this.pathFotoGtemp = pathFotoGtemp;
    }

    public byte[] getPathFirmaGtemp() {
        return pathFirmaGtemp;
    }

    public void setPathFirmaGtemp(byte[] pathFirmaGtemp) {
        this.pathFirmaGtemp = pathFirmaGtemp;
    }

    @XmlTransient
    public Collection<AsiPermisosVacacionHext> getAsiPermisosVacacionHextCollection() {
        return asiPermisosVacacionHextCollection;
    }

    public void setAsiPermisosVacacionHextCollection(Collection<AsiPermisosVacacionHext> asiPermisosVacacionHextCollection) {
        this.asiPermisosVacacionHextCollection = asiPermisosVacacionHextCollection;
    }

    @XmlTransient
    public Collection<AsiNovedadDetalle> getAsiNovedadDetalleCollection() {
        return asiNovedadDetalleCollection;
    }

    public void setAsiNovedadDetalleCollection(Collection<AsiNovedadDetalle> asiNovedadDetalleCollection) {
        this.asiNovedadDetalleCollection = asiNovedadDetalleCollection;
    }

    @XmlTransient
    public Collection<SriProyeccionIngres> getSriProyeccionIngresCollection() {
        return sriProyeccionIngresCollection;
    }

    public void setSriProyeccionIngresCollection(Collection<SriProyeccionIngres> sriProyeccionIngresCollection) {
        this.sriProyeccionIngresCollection = sriProyeccionIngresCollection;
    }

    @XmlTransient
    public Collection<GenDetalleEmpleadoDepartame> getGenDetalleEmpleadoDepartameCollection() {
        return genDetalleEmpleadoDepartameCollection;
    }

    public void setGenDetalleEmpleadoDepartameCollection(Collection<GenDetalleEmpleadoDepartame> genDetalleEmpleadoDepartameCollection) {
        this.genDetalleEmpleadoDepartameCollection = genDetalleEmpleadoDepartameCollection;
    }

    @XmlTransient
    public Collection<GthDireccion> getGthDireccionCollection() {
        return gthDireccionCollection;
    }

    public void setGthDireccionCollection(Collection<GthDireccion> gthDireccionCollection) {
        this.gthDireccionCollection = gthDireccionCollection;
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
    public Collection<GthRegistroMilitar> getGthRegistroMilitarCollection() {
        return gthRegistroMilitarCollection;
    }

    public void setGthRegistroMilitarCollection(Collection<GthRegistroMilitar> gthRegistroMilitarCollection) {
        this.gthRegistroMilitarCollection = gthRegistroMilitarCollection;
    }

    @XmlTransient
    public Collection<GthTelefono> getGthTelefonoCollection() {
        return gthTelefonoCollection;
    }

    public void setGthTelefonoCollection(Collection<GthTelefono> gthTelefonoCollection) {
        this.gthTelefonoCollection = gthTelefonoCollection;
    }

    @XmlTransient
    public Collection<NrhAnticipo> getNrhAnticipoCollection() {
        return nrhAnticipoCollection;
    }

    public void setNrhAnticipoCollection(Collection<NrhAnticipo> nrhAnticipoCollection) {
        this.nrhAnticipoCollection = nrhAnticipoCollection;
    }

    @XmlTransient
    public Collection<GthConyuge> getGthConyugeCollection() {
        return gthConyugeCollection;
    }

    public void setGthConyugeCollection(Collection<GthConyuge> gthConyugeCollection) {
        this.gthConyugeCollection = gthConyugeCollection;
    }

    @XmlTransient
    public Collection<GthTerrenoEmpleado> getGthTerrenoEmpleadoCollection() {
        return gthTerrenoEmpleadoCollection;
    }

    public void setGthTerrenoEmpleadoCollection(Collection<GthTerrenoEmpleado> gthTerrenoEmpleadoCollection) {
        this.gthTerrenoEmpleadoCollection = gthTerrenoEmpleadoCollection;
    }

    @XmlTransient
    public Collection<GthIdiomaEmpleado> getGthIdiomaEmpleadoCollection() {
        return gthIdiomaEmpleadoCollection;
    }

    public void setGthIdiomaEmpleadoCollection(Collection<GthIdiomaEmpleado> gthIdiomaEmpleadoCollection) {
        this.gthIdiomaEmpleadoCollection = gthIdiomaEmpleadoCollection;
    }

    @XmlTransient
    public Collection<GthFichaValoracion> getGthFichaValoracionCollection() {
        return gthFichaValoracionCollection;
    }

    public void setGthFichaValoracionCollection(Collection<GthFichaValoracion> gthFichaValoracionCollection) {
        this.gthFichaValoracionCollection = gthFichaValoracionCollection;
    }

    @XmlTransient
    public Collection<GthCuentaAnticipo> getGthCuentaAnticipoCollection() {
        return gthCuentaAnticipoCollection;
    }

    public void setGthCuentaAnticipoCollection(Collection<GthCuentaAnticipo> gthCuentaAnticipoCollection) {
        this.gthCuentaAnticipoCollection = gthCuentaAnticipoCollection;
    }

    @XmlTransient
    public Collection<GthSituacionEconomicaEmplea> getGthSituacionEconomicaEmpleaCollection() {
        return gthSituacionEconomicaEmpleaCollection;
    }

    public void setGthSituacionEconomicaEmpleaCollection(Collection<GthSituacionEconomicaEmplea> gthSituacionEconomicaEmpleaCollection) {
        this.gthSituacionEconomicaEmpleaCollection = gthSituacionEconomicaEmpleaCollection;
    }

    public GthTipoSangre getIdeGttis() {
        return ideGttis;
    }

    public void setIdeGttis(GthTipoSangre ideGttis) {
        this.ideGttis = ideGttis;
    }

    public GthTipoDocumentoIdentidad getIdeGttdi() {
        return ideGttdi;
    }

    public void setIdeGttdi(GthTipoDocumentoIdentidad ideGttdi) {
        this.ideGttdi = ideGttdi;
    }

    public GthNacionalidad getIdeGtnac() {
        return ideGtnac;
    }

    public void setIdeGtnac(GthNacionalidad ideGtnac) {
        this.ideGtnac = ideGtnac;
    }

    public GthGenero getIdeGtgen() {
        return ideGtgen;
    }

    public void setIdeGtgen(GthGenero ideGtgen) {
        this.ideGtgen = ideGtgen;
    }

    public GthEstadoCivil getIdeGtesc() {
        return ideGtesc;
    }

    public void setIdeGtesc(GthEstadoCivil ideGtesc) {
        this.ideGtesc = ideGtesc;
    }

    public GenDivisionPolitica getIdeGedip() {
        return ideGedip;
    }

    public void setIdeGedip(GenDivisionPolitica ideGedip) {
        this.ideGedip = ideGedip;
    }

    @XmlTransient
    public Collection<GthTarjetaCreditoEmpleado> getGthTarjetaCreditoEmpleadoCollection() {
        return gthTarjetaCreditoEmpleadoCollection;
    }

    public void setGthTarjetaCreditoEmpleadoCollection(Collection<GthTarjetaCreditoEmpleado> gthTarjetaCreditoEmpleadoCollection) {
        this.gthTarjetaCreditoEmpleadoCollection = gthTarjetaCreditoEmpleadoCollection;
    }

    @XmlTransient
    public Collection<AsiVacacion> getAsiVacacionCollection() {
        return asiVacacionCollection;
    }

    public void setAsiVacacionCollection(Collection<AsiVacacion> asiVacacionCollection) {
        this.asiVacacionCollection = asiVacacionCollection;
    }

    @XmlTransient
    public Collection<GenEmpleadosDepartamentoPar> getGenEmpleadosDepartamentoParCollection() {
        return genEmpleadosDepartamentoParCollection;
    }

    public void setGenEmpleadosDepartamentoParCollection(Collection<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParCollection) {
        this.genEmpleadosDepartamentoParCollection = genEmpleadosDepartamentoParCollection;
    }

    @XmlTransient
    public Collection<GthCasaEmpleado> getGthCasaEmpleadoCollection() {
        return gthCasaEmpleadoCollection;
    }

    public void setGthCasaEmpleadoCollection(Collection<GthCasaEmpleado> gthCasaEmpleadoCollection) {
        this.gthCasaEmpleadoCollection = gthCasaEmpleadoCollection;
    }

    @XmlTransient
    public Collection<SriDeduciblesEmpleado> getSriDeduciblesEmpleadoCollection() {
        return sriDeduciblesEmpleadoCollection;
    }

    public void setSriDeduciblesEmpleadoCollection(Collection<SriDeduciblesEmpleado> sriDeduciblesEmpleadoCollection) {
        this.sriDeduciblesEmpleadoCollection = sriDeduciblesEmpleadoCollection;
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
    public Collection<GthDiscapacidadEmpleado> getGthDiscapacidadEmpleadoCollection() {
        return gthDiscapacidadEmpleadoCollection;
    }

    public void setGthDiscapacidadEmpleadoCollection(Collection<GthDiscapacidadEmpleado> gthDiscapacidadEmpleadoCollection) {
        this.gthDiscapacidadEmpleadoCollection = gthDiscapacidadEmpleadoCollection;
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
    public Collection<GthCorreo> getGthCorreoCollection() {
        return gthCorreoCollection;
    }

    public void setGthCorreoCollection(Collection<GthCorreo> gthCorreoCollection) {
        this.gthCorreoCollection = gthCorreoCollection;
    }

    @XmlTransient
    public Collection<GthArchivoEmpleado> getGthArchivoEmpleadoCollection() {
        return gthArchivoEmpleadoCollection;
    }

    public void setGthArchivoEmpleadoCollection(Collection<GthArchivoEmpleado> gthArchivoEmpleadoCollection) {
        this.gthArchivoEmpleadoCollection = gthArchivoEmpleadoCollection;
    }

    @XmlTransient
    public Collection<GthAmigosEmpresaEmplea> getGthAmigosEmpresaEmpleaCollection() {
        return gthAmigosEmpresaEmpleaCollection;
    }

    public void setGthAmigosEmpresaEmpleaCollection(Collection<GthAmigosEmpresaEmplea> gthAmigosEmpresaEmpleaCollection) {
        this.gthAmigosEmpresaEmpleaCollection = gthAmigosEmpresaEmpleaCollection;
    }

    @XmlTransient
    public Collection<GthSituacionFinancieraEmple> getGthSituacionFinancieraEmpleCollection() {
        return gthSituacionFinancieraEmpleCollection;
    }

    public void setGthSituacionFinancieraEmpleCollection(Collection<GthSituacionFinancieraEmple> gthSituacionFinancieraEmpleCollection) {
        this.gthSituacionFinancieraEmpleCollection = gthSituacionFinancieraEmpleCollection;
    }

    @XmlTransient
    public Collection<GthEducacionEmpleado> getGthEducacionEmpleadoCollection() {
        return gthEducacionEmpleadoCollection;
    }

    public void setGthEducacionEmpleadoCollection(Collection<GthEducacionEmpleado> gthEducacionEmpleadoCollection) {
        this.gthEducacionEmpleadoCollection = gthEducacionEmpleadoCollection;
    }

    @XmlTransient
    public Collection<GthNegocioEmpleado> getGthNegocioEmpleadoCollection() {
        return gthNegocioEmpleadoCollection;
    }

    public void setGthNegocioEmpleadoCollection(Collection<GthNegocioEmpleado> gthNegocioEmpleadoCollection) {
        this.gthNegocioEmpleadoCollection = gthNegocioEmpleadoCollection;
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
    public Collection<GthHobie> getGthHobieCollection() {
        return gthHobieCollection;
    }

    public void setGthHobieCollection(Collection<GthHobie> gthHobieCollection) {
        this.gthHobieCollection = gthHobieCollection;
    }

    @XmlTransient
    public Collection<GthFamiliar> getGthFamiliarCollection() {
        return gthFamiliarCollection;
    }

    public void setGthFamiliarCollection(Collection<GthFamiliar> gthFamiliarCollection) {
        this.gthFamiliarCollection = gthFamiliarCollection;
    }

    @XmlTransient
    public Collection<GthCargasFamiliares> getGthCargasFamiliaresCollection() {
        return gthCargasFamiliaresCollection;
    }

    public void setGthCargasFamiliaresCollection(Collection<GthCargasFamiliares> gthCargasFamiliaresCollection) {
        this.gthCargasFamiliaresCollection = gthCargasFamiliaresCollection;
    }

    @XmlTransient
    public Collection<GthPersonaEmergencia> getGthPersonaEmergenciaCollection() {
        return gthPersonaEmergenciaCollection;
    }

    public void setGthPersonaEmergenciaCollection(Collection<GthPersonaEmergencia> gthPersonaEmergenciaCollection) {
        this.gthPersonaEmergenciaCollection = gthPersonaEmergenciaCollection;
    }

    @XmlTransient
    public Collection<GthInversionEmpleado> getGthInversionEmpleadoCollection() {
        return gthInversionEmpleadoCollection;
    }

    public void setGthInversionEmpleadoCollection(Collection<GthInversionEmpleado> gthInversionEmpleadoCollection) {
        this.gthInversionEmpleadoCollection = gthInversionEmpleadoCollection;
    }

    @XmlTransient
    public Collection<GthDocumentacionEmpleado> getGthDocumentacionEmpleadoCollection() {
        return gthDocumentacionEmpleadoCollection;
    }

    public void setGthDocumentacionEmpleadoCollection(Collection<GthDocumentacionEmpleado> gthDocumentacionEmpleadoCollection) {
        this.gthDocumentacionEmpleadoCollection = gthDocumentacionEmpleadoCollection;
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
        hash += (ideGtemp != null ? ideGtemp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthEmpleado)) {
            return false;
        }
        GthEmpleado other = (GthEmpleado) object;
        if ((this.ideGtemp == null && other.ideGtemp != null) || (this.ideGtemp != null && !this.ideGtemp.equals(other.ideGtemp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthEmpleado[ ideGtemp=" + ideGtemp + " ]";
    }
    
}

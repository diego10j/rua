/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author User
 */
@Entity
@Table(name = "nrh_garante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhGarante.findAll", query = "SELECT n FROM NrhGarante n"),
    @NamedQuery(name = "NrhGarante.findByIdeNrgar", query = "SELECT n FROM NrhGarante n WHERE n.ideNrgar = :ideNrgar"),
    @NamedQuery(name = "NrhGarante.findByApellidoPaternogNrgar", query = "SELECT n FROM NrhGarante n WHERE n.apellidoPaternogNrgar = :apellidoPaternogNrgar"),
    @NamedQuery(name = "NrhGarante.findByApellidoMaternogNrgar", query = "SELECT n FROM NrhGarante n WHERE n.apellidoMaternogNrgar = :apellidoMaternogNrgar"),
    @NamedQuery(name = "NrhGarante.findByPrimerNombregNrgar", query = "SELECT n FROM NrhGarante n WHERE n.primerNombregNrgar = :primerNombregNrgar"),
    @NamedQuery(name = "NrhGarante.findBySegundoNombregNrgar", query = "SELECT n FROM NrhGarante n WHERE n.segundoNombregNrgar = :segundoNombregNrgar"),
    @NamedQuery(name = "NrhGarante.findByDocumentoIdentidadgNrgar", query = "SELECT n FROM NrhGarante n WHERE n.documentoIdentidadgNrgar = :documentoIdentidadgNrgar"),
    @NamedQuery(name = "NrhGarante.findByLugarTrabajoNrgar", query = "SELECT n FROM NrhGarante n WHERE n.lugarTrabajoNrgar = :lugarTrabajoNrgar"),
    @NamedQuery(name = "NrhGarante.findByFechaIngresoNrgar", query = "SELECT n FROM NrhGarante n WHERE n.fechaIngresoNrgar = :fechaIngresoNrgar"),
    @NamedQuery(name = "NrhGarante.findByRmuNrgar", query = "SELECT n FROM NrhGarante n WHERE n.rmuNrgar = :rmuNrgar"),
    @NamedQuery(name = "NrhGarante.findByCargoNrgar", query = "SELECT n FROM NrhGarante n WHERE n.cargoNrgar = :cargoNrgar"),
    @NamedQuery(name = "NrhGarante.findByDepartamentoNrgar", query = "SELECT n FROM NrhGarante n WHERE n.departamentoNrgar = :departamentoNrgar"),
    @NamedQuery(name = "NrhGarante.findByApellidoPaternocNrgar", query = "SELECT n FROM NrhGarante n WHERE n.apellidoPaternocNrgar = :apellidoPaternocNrgar"),
    @NamedQuery(name = "NrhGarante.findByApellidoMaternocNrgar", query = "SELECT n FROM NrhGarante n WHERE n.apellidoMaternocNrgar = :apellidoMaternocNrgar"),
    @NamedQuery(name = "NrhGarante.findByPrimerNombrecNrgar", query = "SELECT n FROM NrhGarante n WHERE n.primerNombrecNrgar = :primerNombrecNrgar"),
    @NamedQuery(name = "NrhGarante.findBySegundoNombrecNrgar", query = "SELECT n FROM NrhGarante n WHERE n.segundoNombrecNrgar = :segundoNombrecNrgar"),
    @NamedQuery(name = "NrhGarante.findByDocumentoIdentidadcNrgar", query = "SELECT n FROM NrhGarante n WHERE n.documentoIdentidadcNrgar = :documentoIdentidadcNrgar"),
    @NamedQuery(name = "NrhGarante.findByMontoViviendaNrgar", query = "SELECT n FROM NrhGarante n WHERE n.montoViviendaNrgar = :montoViviendaNrgar"),
    @NamedQuery(name = "NrhGarante.findByMontoVehiculoNrgar", query = "SELECT n FROM NrhGarante n WHERE n.montoVehiculoNrgar = :montoVehiculoNrgar"),
    @NamedQuery(name = "NrhGarante.findByTotalPatrimonioNrgar", query = "SELECT n FROM NrhGarante n WHERE n.totalPatrimonioNrgar = :totalPatrimonioNrgar"),
    @NamedQuery(name = "NrhGarante.findByTelefonoNrgar", query = "SELECT n FROM NrhGarante n WHERE n.telefonoNrgar = :telefonoNrgar"),
    @NamedQuery(name = "NrhGarante.findByTelefonoConyugueNrgar", query = "SELECT n FROM NrhGarante n WHERE n.telefonoConyugueNrgar = :telefonoConyugueNrgar"),
    @NamedQuery(name = "NrhGarante.findByDireccionGaranteNrgar", query = "SELECT n FROM NrhGarante n WHERE n.direccionGaranteNrgar = :direccionGaranteNrgar"),
    @NamedQuery(name = "NrhGarante.findByActivoNrgar", query = "SELECT n FROM NrhGarante n WHERE n.activoNrgar = :activoNrgar"),
    @NamedQuery(name = "NrhGarante.findByUsuarioIngre", query = "SELECT n FROM NrhGarante n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhGarante.findByFechaIngre", query = "SELECT n FROM NrhGarante n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhGarante.findByUsuarioActua", query = "SELECT n FROM NrhGarante n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhGarante.findByFechaActua", query = "SELECT n FROM NrhGarante n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhGarante.findByHoraIngre", query = "SELECT n FROM NrhGarante n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhGarante.findByHoraActua", query = "SELECT n FROM NrhGarante n WHERE n.horaActua = :horaActua"),
    @NamedQuery(name = "NrhGarante.findByViviendaNrgar", query = "SELECT n FROM NrhGarante n WHERE n.viviendaNrgar = :viviendaNrgar"),
    @NamedQuery(name = "NrhGarante.findByVehiculoNrgar", query = "SELECT n FROM NrhGarante n WHERE n.vehiculoNrgar = :vehiculoNrgar")})
public class NrhGarante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrgar")
    private Integer ideNrgar;
    @Column(name = "apellido_paternog_nrgar")
    private String apellidoPaternogNrgar;
    @Column(name = "apellido_maternog_nrgar")
    private String apellidoMaternogNrgar;
    @Column(name = "primer_nombreg_nrgar")
    private String primerNombregNrgar;
    @Column(name = "segundo_nombreg_nrgar")
    private String segundoNombregNrgar;
    @Column(name = "documento_identidadg_nrgar")
    private String documentoIdentidadgNrgar;
    @Column(name = "lugar_trabajo_nrgar")
    private String lugarTrabajoNrgar;
    @Column(name = "fecha_ingreso_nrgar")
    @Temporal(TemporalType.DATE)
    private Date fechaIngresoNrgar;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "rmu_nrgar")
    private BigDecimal rmuNrgar;
    @Column(name = "cargo_nrgar")
    private String cargoNrgar;
    @Column(name = "departamento_nrgar")
    private String departamentoNrgar;
    @Column(name = "apellido_paternoc_nrgar")
    private String apellidoPaternocNrgar;
    @Column(name = "apellido_maternoc_nrgar")
    private String apellidoMaternocNrgar;
    @Column(name = "primer_nombrec_nrgar")
    private String primerNombrecNrgar;
    @Column(name = "segundo_nombrec_nrgar")
    private String segundoNombrecNrgar;
    @Column(name = "documento_identidadc_nrgar")
    private String documentoIdentidadcNrgar;
    @Column(name = "monto_vivienda_nrgar")
    private BigDecimal montoViviendaNrgar;
    @Column(name = "monto_vehiculo_nrgar")
    private BigDecimal montoVehiculoNrgar;
    @Column(name = "total_patrimonio_nrgar")
    private BigDecimal totalPatrimonioNrgar;
    @Column(name = "telefono_nrgar")
    private String telefonoNrgar;
    @Column(name = "telefono_conyugue_nrgar")
    private String telefonoConyugueNrgar;
    @Column(name = "direccion_garante_nrgar")
    private String direccionGaranteNrgar;
    @Column(name = "activo_nrgar")
    private Boolean activoNrgar;
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
    @Column(name = "vivienda_nrgar")
    private Boolean viviendaNrgar;
    @Column(name = "vehiculo_nrgar")
    private Boolean vehiculoNrgar;
    @JoinColumn(name = "ide_nrtig", referencedColumnName = "ide_nrtig")
    @ManyToOne
    private NrhTipoGarante ideNrtig;
    @JoinColumn(name = "ide_nrant", referencedColumnName = "ide_nrant")
    @ManyToOne
    private NrhAnticipo ideNrant;
    @JoinColumn(name = "ide_gttdi", referencedColumnName = "ide_gttdi")
    @ManyToOne
    private GthTipoDocumentoIdentidad ideGttdi;
    @JoinColumn(name = "gth_ide_gttdi", referencedColumnName = "ide_gttdi")
    @ManyToOne
    private GthTipoDocumentoIdentidad gthIdeGttdi;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;

    public NrhGarante() {
    }

    public NrhGarante(Integer ideNrgar) {
        this.ideNrgar = ideNrgar;
    }

    public Integer getIdeNrgar() {
        return ideNrgar;
    }

    public void setIdeNrgar(Integer ideNrgar) {
        this.ideNrgar = ideNrgar;
    }

    public String getApellidoPaternogNrgar() {
        return apellidoPaternogNrgar;
    }

    public void setApellidoPaternogNrgar(String apellidoPaternogNrgar) {
        this.apellidoPaternogNrgar = apellidoPaternogNrgar;
    }

    public String getApellidoMaternogNrgar() {
        return apellidoMaternogNrgar;
    }

    public void setApellidoMaternogNrgar(String apellidoMaternogNrgar) {
        this.apellidoMaternogNrgar = apellidoMaternogNrgar;
    }

    public String getPrimerNombregNrgar() {
        return primerNombregNrgar;
    }

    public void setPrimerNombregNrgar(String primerNombregNrgar) {
        this.primerNombregNrgar = primerNombregNrgar;
    }

    public String getSegundoNombregNrgar() {
        return segundoNombregNrgar;
    }

    public void setSegundoNombregNrgar(String segundoNombregNrgar) {
        this.segundoNombregNrgar = segundoNombregNrgar;
    }

    public String getDocumentoIdentidadgNrgar() {
        return documentoIdentidadgNrgar;
    }

    public void setDocumentoIdentidadgNrgar(String documentoIdentidadgNrgar) {
        this.documentoIdentidadgNrgar = documentoIdentidadgNrgar;
    }

    public String getLugarTrabajoNrgar() {
        return lugarTrabajoNrgar;
    }

    public void setLugarTrabajoNrgar(String lugarTrabajoNrgar) {
        this.lugarTrabajoNrgar = lugarTrabajoNrgar;
    }

    public Date getFechaIngresoNrgar() {
        return fechaIngresoNrgar;
    }

    public void setFechaIngresoNrgar(Date fechaIngresoNrgar) {
        this.fechaIngresoNrgar = fechaIngresoNrgar;
    }

    public BigDecimal getRmuNrgar() {
        return rmuNrgar;
    }

    public void setRmuNrgar(BigDecimal rmuNrgar) {
        this.rmuNrgar = rmuNrgar;
    }

    public String getCargoNrgar() {
        return cargoNrgar;
    }

    public void setCargoNrgar(String cargoNrgar) {
        this.cargoNrgar = cargoNrgar;
    }

    public String getDepartamentoNrgar() {
        return departamentoNrgar;
    }

    public void setDepartamentoNrgar(String departamentoNrgar) {
        this.departamentoNrgar = departamentoNrgar;
    }

    public String getApellidoPaternocNrgar() {
        return apellidoPaternocNrgar;
    }

    public void setApellidoPaternocNrgar(String apellidoPaternocNrgar) {
        this.apellidoPaternocNrgar = apellidoPaternocNrgar;
    }

    public String getApellidoMaternocNrgar() {
        return apellidoMaternocNrgar;
    }

    public void setApellidoMaternocNrgar(String apellidoMaternocNrgar) {
        this.apellidoMaternocNrgar = apellidoMaternocNrgar;
    }

    public String getPrimerNombrecNrgar() {
        return primerNombrecNrgar;
    }

    public void setPrimerNombrecNrgar(String primerNombrecNrgar) {
        this.primerNombrecNrgar = primerNombrecNrgar;
    }

    public String getSegundoNombrecNrgar() {
        return segundoNombrecNrgar;
    }

    public void setSegundoNombrecNrgar(String segundoNombrecNrgar) {
        this.segundoNombrecNrgar = segundoNombrecNrgar;
    }

    public String getDocumentoIdentidadcNrgar() {
        return documentoIdentidadcNrgar;
    }

    public void setDocumentoIdentidadcNrgar(String documentoIdentidadcNrgar) {
        this.documentoIdentidadcNrgar = documentoIdentidadcNrgar;
    }

    public BigDecimal getMontoViviendaNrgar() {
        return montoViviendaNrgar;
    }

    public void setMontoViviendaNrgar(BigDecimal montoViviendaNrgar) {
        this.montoViviendaNrgar = montoViviendaNrgar;
    }

    public BigDecimal getMontoVehiculoNrgar() {
        return montoVehiculoNrgar;
    }

    public void setMontoVehiculoNrgar(BigDecimal montoVehiculoNrgar) {
        this.montoVehiculoNrgar = montoVehiculoNrgar;
    }

    public BigDecimal getTotalPatrimonioNrgar() {
        return totalPatrimonioNrgar;
    }

    public void setTotalPatrimonioNrgar(BigDecimal totalPatrimonioNrgar) {
        this.totalPatrimonioNrgar = totalPatrimonioNrgar;
    }

    public String getTelefonoNrgar() {
        return telefonoNrgar;
    }

    public void setTelefonoNrgar(String telefonoNrgar) {
        this.telefonoNrgar = telefonoNrgar;
    }

    public String getTelefonoConyugueNrgar() {
        return telefonoConyugueNrgar;
    }

    public void setTelefonoConyugueNrgar(String telefonoConyugueNrgar) {
        this.telefonoConyugueNrgar = telefonoConyugueNrgar;
    }

    public String getDireccionGaranteNrgar() {
        return direccionGaranteNrgar;
    }

    public void setDireccionGaranteNrgar(String direccionGaranteNrgar) {
        this.direccionGaranteNrgar = direccionGaranteNrgar;
    }

    public Boolean getActivoNrgar() {
        return activoNrgar;
    }

    public void setActivoNrgar(Boolean activoNrgar) {
        this.activoNrgar = activoNrgar;
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

    public Boolean getViviendaNrgar() {
        return viviendaNrgar;
    }

    public void setViviendaNrgar(Boolean viviendaNrgar) {
        this.viviendaNrgar = viviendaNrgar;
    }

    public Boolean getVehiculoNrgar() {
        return vehiculoNrgar;
    }

    public void setVehiculoNrgar(Boolean vehiculoNrgar) {
        this.vehiculoNrgar = vehiculoNrgar;
    }

    public NrhTipoGarante getIdeNrtig() {
        return ideNrtig;
    }

    public void setIdeNrtig(NrhTipoGarante ideNrtig) {
        this.ideNrtig = ideNrtig;
    }

    public NrhAnticipo getIdeNrant() {
        return ideNrant;
    }

    public void setIdeNrant(NrhAnticipo ideNrant) {
        this.ideNrant = ideNrant;
    }

    public GthTipoDocumentoIdentidad getIdeGttdi() {
        return ideGttdi;
    }

    public void setIdeGttdi(GthTipoDocumentoIdentidad ideGttdi) {
        this.ideGttdi = ideGttdi;
    }

    public GthTipoDocumentoIdentidad getGthIdeGttdi() {
        return gthIdeGttdi;
    }

    public void setGthIdeGttdi(GthTipoDocumentoIdentidad gthIdeGttdi) {
        this.gthIdeGttdi = gthIdeGttdi;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrgar != null ? ideNrgar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhGarante)) {
            return false;
        }
        NrhGarante other = (NrhGarante) object;
        if ((this.ideNrgar == null && other.ideNrgar != null) || (this.ideNrgar != null && !this.ideNrgar.equals(other.ideNrgar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhGarante[ ideNrgar=" + ideNrgar + " ]";
    }
    
}

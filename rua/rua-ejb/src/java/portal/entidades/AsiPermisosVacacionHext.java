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
@Table(name = "asi_permisos_vacacion_hext")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsiPermisosVacacionHext.findAll", query = "SELECT a FROM AsiPermisosVacacionHext a"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByIdeAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.ideAspvh = :ideAspvh"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByIdeAsmot", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.ideAsmot = :ideAsmot"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByIdeSucu", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.ideSucu = :ideSucu"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByFechaSolicitudAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.fechaSolicitudAspvh = :fechaSolicitudAspvh"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByFechaDesdeAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.fechaDesdeAspvh = :fechaDesdeAspvh"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByFechaHastaAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.fechaHastaAspvh = :fechaHastaAspvh"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByDetalleAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.detalleAspvh = :detalleAspvh"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByNroDiasAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.nroDiasAspvh = :nroDiasAspvh"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByTipoAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.tipoAspvh = :tipoAspvh"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByNroDocumentoAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.nroDocumentoAspvh = :nroDocumentoAspvh"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByNroHorasAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.nroHorasAspvh = :nroHorasAspvh"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByRazonAnulaAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.razonAnulaAspvh = :razonAnulaAspvh"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByDocumentoAnulaAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.documentoAnulaAspvh = :documentoAnulaAspvh"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByFechaAnulaAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.fechaAnulaAspvh = :fechaAnulaAspvh"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByActivoAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.activoAspvh = :activoAspvh"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByUsuarioIngre", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByFechaIngre", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByUsuarioActua", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByFechaActua", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByHoraDesdeAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.horaDesdeAspvh = :horaDesdeAspvh"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByHoraHastaAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.horaHastaAspvh = :horaHastaAspvh"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByHoraIngre", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByHoraActua", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.horaActua = :horaActua"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByAprobadoAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.aprobadoAspvh = :aprobadoAspvh"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByRegistroNovedadAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.registroNovedadAspvh = :registroNovedadAspvh"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByAnuladoAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.anuladoAspvh = :anuladoAspvh"),
    @NamedQuery(name = "AsiPermisosVacacionHext.findByAprobadoTthhAspvh", query = "SELECT a FROM AsiPermisosVacacionHext a WHERE a.aprobadoTthhAspvh = :aprobadoTthhAspvh")})
public class AsiPermisosVacacionHext implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_aspvh")
    private Integer ideAspvh;
    @Column(name = "ide_asmot")
    private Integer ideAsmot;
    @Column(name = "ide_sucu")
    private Integer ideSucu;
    @Column(name = "fecha_solicitud_aspvh")
    @Temporal(TemporalType.DATE)
    private Date fechaSolicitudAspvh;
    @Column(name = "fecha_desde_aspvh")
    @Temporal(TemporalType.DATE)
    private Date fechaDesdeAspvh;
    @Column(name = "fecha_hasta_aspvh")
    @Temporal(TemporalType.DATE)
    private Date fechaHastaAspvh;
    @Column(name = "detalle_aspvh")
    private String detalleAspvh;
    @Column(name = "nro_dias_aspvh")
    private Integer nroDiasAspvh;
    @Column(name = "tipo_aspvh")
    private Integer tipoAspvh;
    @Column(name = "nro_documento_aspvh")
    private String nroDocumentoAspvh;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "nro_horas_aspvh")
    private BigDecimal nroHorasAspvh;
    @Column(name = "razon_anula_aspvh")
    private String razonAnulaAspvh;
    @Column(name = "documento_anula_aspvh")
    private String documentoAnulaAspvh;
    @Column(name = "fecha_anula_aspvh")
    @Temporal(TemporalType.DATE)
    private Date fechaAnulaAspvh;
    @Column(name = "activo_aspvh")
    private Boolean activoAspvh;
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
    @Column(name = "hora_desde_aspvh")
    @Temporal(TemporalType.TIME)
    private Date horaDesdeAspvh;
    @Column(name = "hora_hasta_aspvh")
    @Temporal(TemporalType.TIME)
    private Date horaHastaAspvh;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @Column(name = "aprobado_aspvh")
    private Boolean aprobadoAspvh;
    @Column(name = "registro_novedad_aspvh")
    private Boolean registroNovedadAspvh;
    @Column(name = "anulado_aspvh")
    private Boolean anuladoAspvh;
    @Column(name = "aprobado_tthh_aspvh")
    private Boolean aprobadoTthhAspvh;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumns({
        @JoinColumn(name = "ide_gemes", referencedColumnName = "ide_gemes"),
        @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")})
    @ManyToOne
    private GenPeriodo genPeriodo;
    @JoinColumn(name = "ide_geest", referencedColumnName = "ide_geest")
    @ManyToOne
    private GenEstados ideGeest;
    @JoinColumn(name = "gen_ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp;
    @JoinColumn(name = "gen_ide_geedp3", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp3;
    @JoinColumn(name = "gen_ide_geedp2", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp2;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @OneToMany(mappedBy = "ideAspvh")
    private Collection<AsiNovedadDetalle> asiNovedadDetalleCollection;
    @OneToMany(mappedBy = "ideAspvh")
    private Collection<AsiDetalleHorasExtras> asiDetalleHorasExtrasCollection;
    @OneToMany(mappedBy = "ideAspvh")
    private Collection<AsiDetalleVacacion> asiDetalleVacacionCollection;

    public AsiPermisosVacacionHext() {
    }

    public AsiPermisosVacacionHext(Integer ideAspvh) {
        this.ideAspvh = ideAspvh;
    }

    public Integer getIdeAspvh() {
        return ideAspvh;
    }

    public void setIdeAspvh(Integer ideAspvh) {
        this.ideAspvh = ideAspvh;
    }

    public Integer getIdeAsmot() {
        return ideAsmot;
    }

    public void setIdeAsmot(Integer ideAsmot) {
        this.ideAsmot = ideAsmot;
    }

    public Integer getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(Integer ideSucu) {
        this.ideSucu = ideSucu;
    }

    public Date getFechaSolicitudAspvh() {
        return fechaSolicitudAspvh;
    }

    public void setFechaSolicitudAspvh(Date fechaSolicitudAspvh) {
        this.fechaSolicitudAspvh = fechaSolicitudAspvh;
    }

    public Date getFechaDesdeAspvh() {
        return fechaDesdeAspvh;
    }

    public void setFechaDesdeAspvh(Date fechaDesdeAspvh) {
        this.fechaDesdeAspvh = fechaDesdeAspvh;
    }

    public Date getFechaHastaAspvh() {
        return fechaHastaAspvh;
    }

    public void setFechaHastaAspvh(Date fechaHastaAspvh) {
        this.fechaHastaAspvh = fechaHastaAspvh;
    }

    public String getDetalleAspvh() {
        return detalleAspvh;
    }

    public void setDetalleAspvh(String detalleAspvh) {
        this.detalleAspvh = detalleAspvh;
    }

    public Integer getNroDiasAspvh() {
        return nroDiasAspvh;
    }

    public void setNroDiasAspvh(Integer nroDiasAspvh) {
        this.nroDiasAspvh = nroDiasAspvh;
    }

    public Integer getTipoAspvh() {
        return tipoAspvh;
    }

    public void setTipoAspvh(Integer tipoAspvh) {
        this.tipoAspvh = tipoAspvh;
    }

    public String getNroDocumentoAspvh() {
        return nroDocumentoAspvh;
    }

    public void setNroDocumentoAspvh(String nroDocumentoAspvh) {
        this.nroDocumentoAspvh = nroDocumentoAspvh;
    }

    public BigDecimal getNroHorasAspvh() {
        return nroHorasAspvh;
    }

    public void setNroHorasAspvh(BigDecimal nroHorasAspvh) {
        this.nroHorasAspvh = nroHorasAspvh;
    }

    public String getRazonAnulaAspvh() {
        return razonAnulaAspvh;
    }

    public void setRazonAnulaAspvh(String razonAnulaAspvh) {
        this.razonAnulaAspvh = razonAnulaAspvh;
    }

    public String getDocumentoAnulaAspvh() {
        return documentoAnulaAspvh;
    }

    public void setDocumentoAnulaAspvh(String documentoAnulaAspvh) {
        this.documentoAnulaAspvh = documentoAnulaAspvh;
    }

    public Date getFechaAnulaAspvh() {
        return fechaAnulaAspvh;
    }

    public void setFechaAnulaAspvh(Date fechaAnulaAspvh) {
        this.fechaAnulaAspvh = fechaAnulaAspvh;
    }

    public Boolean getActivoAspvh() {
        return activoAspvh;
    }

    public void setActivoAspvh(Boolean activoAspvh) {
        this.activoAspvh = activoAspvh;
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

    public Date getHoraDesdeAspvh() {
        return horaDesdeAspvh;
    }

    public void setHoraDesdeAspvh(Date horaDesdeAspvh) {
        this.horaDesdeAspvh = horaDesdeAspvh;
    }

    public Date getHoraHastaAspvh() {
        return horaHastaAspvh;
    }

    public void setHoraHastaAspvh(Date horaHastaAspvh) {
        this.horaHastaAspvh = horaHastaAspvh;
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

    public Boolean getAprobadoAspvh() {
        return aprobadoAspvh;
    }

    public void setAprobadoAspvh(Boolean aprobadoAspvh) {
        this.aprobadoAspvh = aprobadoAspvh;
    }

    public Boolean getRegistroNovedadAspvh() {
        return registroNovedadAspvh;
    }

    public void setRegistroNovedadAspvh(Boolean registroNovedadAspvh) {
        this.registroNovedadAspvh = registroNovedadAspvh;
    }

    public Boolean getAnuladoAspvh() {
        return anuladoAspvh;
    }

    public void setAnuladoAspvh(Boolean anuladoAspvh) {
        this.anuladoAspvh = anuladoAspvh;
    }

    public Boolean getAprobadoTthhAspvh() {
        return aprobadoTthhAspvh;
    }

    public void setAprobadoTthhAspvh(Boolean aprobadoTthhAspvh) {
        this.aprobadoTthhAspvh = aprobadoTthhAspvh;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenPeriodo getGenPeriodo() {
        return genPeriodo;
    }

    public void setGenPeriodo(GenPeriodo genPeriodo) {
        this.genPeriodo = genPeriodo;
    }

    public GenEstados getIdeGeest() {
        return ideGeest;
    }

    public void setIdeGeest(GenEstados ideGeest) {
        this.ideGeest = ideGeest;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp() {
        return genIdeGeedp;
    }

    public void setGenIdeGeedp(GenEmpleadosDepartamentoPar genIdeGeedp) {
        this.genIdeGeedp = genIdeGeedp;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp3() {
        return genIdeGeedp3;
    }

    public void setGenIdeGeedp3(GenEmpleadosDepartamentoPar genIdeGeedp3) {
        this.genIdeGeedp3 = genIdeGeedp3;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp2() {
        return genIdeGeedp2;
    }

    public void setGenIdeGeedp2(GenEmpleadosDepartamentoPar genIdeGeedp2) {
        this.genIdeGeedp2 = genIdeGeedp2;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    @XmlTransient
    public Collection<AsiNovedadDetalle> getAsiNovedadDetalleCollection() {
        return asiNovedadDetalleCollection;
    }

    public void setAsiNovedadDetalleCollection(Collection<AsiNovedadDetalle> asiNovedadDetalleCollection) {
        this.asiNovedadDetalleCollection = asiNovedadDetalleCollection;
    }

    @XmlTransient
    public Collection<AsiDetalleHorasExtras> getAsiDetalleHorasExtrasCollection() {
        return asiDetalleHorasExtrasCollection;
    }

    public void setAsiDetalleHorasExtrasCollection(Collection<AsiDetalleHorasExtras> asiDetalleHorasExtrasCollection) {
        this.asiDetalleHorasExtrasCollection = asiDetalleHorasExtrasCollection;
    }

    @XmlTransient
    public Collection<AsiDetalleVacacion> getAsiDetalleVacacionCollection() {
        return asiDetalleVacacionCollection;
    }

    public void setAsiDetalleVacacionCollection(Collection<AsiDetalleVacacion> asiDetalleVacacionCollection) {
        this.asiDetalleVacacionCollection = asiDetalleVacacionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAspvh != null ? ideAspvh.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiPermisosVacacionHext)) {
            return false;
        }
        AsiPermisosVacacionHext other = (AsiPermisosVacacionHext) object;
        if ((this.ideAspvh == null && other.ideAspvh != null) || (this.ideAspvh != null && !this.ideAspvh.equals(other.ideAspvh))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiPermisosVacacionHext[ ideAspvh=" + ideAspvh + " ]";
    }
    
}

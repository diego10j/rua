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
@Table(name = "nrh_anticipo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhAnticipo.findAll", query = "SELECT n FROM NrhAnticipo n"),
    @NamedQuery(name = "NrhAnticipo.findByIdeNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.ideNrant = :ideNrant"),
    @NamedQuery(name = "NrhAnticipo.findByFechaSolicitudNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.fechaSolicitudNrant = :fechaSolicitudNrant"),
    @NamedQuery(name = "NrhAnticipo.findByFechaAprobacionNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.fechaAprobacionNrant = :fechaAprobacionNrant"),
    @NamedQuery(name = "NrhAnticipo.findByFechaVencimientoNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.fechaVencimientoNrant = :fechaVencimientoNrant"),
    @NamedQuery(name = "NrhAnticipo.findByNroAnticipoNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.nroAnticipoNrant = :nroAnticipoNrant"),
    @NamedQuery(name = "NrhAnticipo.findByMontoSolicitadoNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.montoSolicitadoNrant = :montoSolicitadoNrant"),
    @NamedQuery(name = "NrhAnticipo.findByMontoAprobadoNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.montoAprobadoNrant = :montoAprobadoNrant"),
    @NamedQuery(name = "NrhAnticipo.findByFechaRrhhNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.fechaRrhhNrant = :fechaRrhhNrant"),
    @NamedQuery(name = "NrhAnticipo.findByResolucionNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.resolucionNrant = :resolucionNrant"),
    @NamedQuery(name = "NrhAnticipo.findByFechaResolucionNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.fechaResolucionNrant = :fechaResolucionNrant"),
    @NamedQuery(name = "NrhAnticipo.findByObservacionNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.observacionNrant = :observacionNrant"),
    @NamedQuery(name = "NrhAnticipo.findByNroMemoNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.nroMemoNrant = :nroMemoNrant"),
    @NamedQuery(name = "NrhAnticipo.findByFechaAutorizaNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.fechaAutorizaNrant = :fechaAutorizaNrant"),
    @NamedQuery(name = "NrhAnticipo.findByRazonAutorizaNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.razonAutorizaNrant = :razonAutorizaNrant"),
    @NamedQuery(name = "NrhAnticipo.findByNroMesNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.nroMesNrant = :nroMesNrant"),
    @NamedQuery(name = "NrhAnticipo.findByArchivoMemoNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.archivoMemoNrant = :archivoMemoNrant"),
    @NamedQuery(name = "NrhAnticipo.findByActivoNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.activoNrant = :activoNrant"),
    @NamedQuery(name = "NrhAnticipo.findByUsuarioIngre", query = "SELECT n FROM NrhAnticipo n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhAnticipo.findByFechaIngre", query = "SELECT n FROM NrhAnticipo n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhAnticipo.findByUsuarioActua", query = "SELECT n FROM NrhAnticipo n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhAnticipo.findByFechaActua", query = "SELECT n FROM NrhAnticipo n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhAnticipo.findByHoraIngre", query = "SELECT n FROM NrhAnticipo n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhAnticipo.findByHoraActua", query = "SELECT n FROM NrhAnticipo n WHERE n.horaActua = :horaActua"),
    @NamedQuery(name = "NrhAnticipo.findByAnticipoNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.anticipoNrant = :anticipoNrant"),
    @NamedQuery(name = "NrhAnticipo.findByAnuladoNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.anuladoNrant = :anuladoNrant"),
    @NamedQuery(name = "NrhAnticipo.findByAbonoNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.abonoNrant = :abonoNrant"),
    @NamedQuery(name = "NrhAnticipo.findByReaprobadoNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.reaprobadoNrant = :reaprobadoNrant"),
    @NamedQuery(name = "NrhAnticipo.findByAprobadoNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.aprobadoNrant = :aprobadoNrant"),
    @NamedQuery(name = "NrhAnticipo.findByCalificadoNrant", query = "SELECT n FROM NrhAnticipo n WHERE n.calificadoNrant = :calificadoNrant")})
public class NrhAnticipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrant")
    private Integer ideNrant;
    @Column(name = "fecha_solicitud_nrant")
    @Temporal(TemporalType.DATE)
    private Date fechaSolicitudNrant;
    @Column(name = "fecha_aprobacion_nrant")
    @Temporal(TemporalType.DATE)
    private Date fechaAprobacionNrant;
    @Column(name = "fecha_vencimiento_nrant")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimientoNrant;
    @Column(name = "nro_anticipo_nrant")
    private Integer nroAnticipoNrant;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto_solicitado_nrant")
    private BigDecimal montoSolicitadoNrant;
    @Column(name = "monto_aprobado_nrant")
    private BigDecimal montoAprobadoNrant;
    @Column(name = "fecha_rrhh_nrant")
    @Temporal(TemporalType.DATE)
    private Date fechaRrhhNrant;
    @Column(name = "resolucion_nrant")
    private String resolucionNrant;
    @Column(name = "fecha_resolucion_nrant")
    @Temporal(TemporalType.DATE)
    private Date fechaResolucionNrant;
    @Column(name = "observacion_nrant")
    private String observacionNrant;
    @Column(name = "nro_memo_nrant")
    private String nroMemoNrant;
    @Column(name = "fecha_autoriza_nrant")
    @Temporal(TemporalType.DATE)
    private Date fechaAutorizaNrant;
    @Column(name = "razon_autoriza_nrant")
    private String razonAutorizaNrant;
    @Column(name = "nro_mes_nrant")
    private Integer nroMesNrant;
    @Column(name = "archivo_memo_nrant")
    private String archivoMemoNrant;
    @Column(name = "activo_nrant")
    private Boolean activoNrant;
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
    @Column(name = "anticipo_nrant")
    private Boolean anticipoNrant;
    @Column(name = "anulado_nrant")
    private Boolean anuladoNrant;
    @Column(name = "abono_nrant")
    private Boolean abonoNrant;
    @Column(name = "reaprobado_nrant")
    private Boolean reaprobadoNrant;
    @Column(name = "aprobado_nrant")
    private Boolean aprobadoNrant;
    @Column(name = "calificado_nrant")
    private Boolean calificadoNrant;
    @JoinColumn(name = "ide_nrmoa", referencedColumnName = "ide_nrmoa")
    @ManyToOne(optional = false)
    private NrhMotivoAnticipo ideNrmoa;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne(optional = false)
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "gen_ide_geedp2", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp2;
    @JoinColumn(name = "gen_ide_geedp3", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp3;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne(optional = false)
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "gen_ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNrant")
    private Collection<NrhCapacidadPago> nrhCapacidadPagoCollection;
    @OneToMany(mappedBy = "ideNrant")
    private Collection<NrhGarante> nrhGaranteCollection;
    @OneToMany(mappedBy = "ideNrant")
    private Collection<NrhAnticipoAbono> nrhAnticipoAbonoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNrant")
    private Collection<NrhAnticipoInteres> nrhAnticipoInteresCollection;

    public NrhAnticipo() {
    }

    public NrhAnticipo(Integer ideNrant) {
        this.ideNrant = ideNrant;
    }

    public Integer getIdeNrant() {
        return ideNrant;
    }

    public void setIdeNrant(Integer ideNrant) {
        this.ideNrant = ideNrant;
    }

    public Date getFechaSolicitudNrant() {
        return fechaSolicitudNrant;
    }

    public void setFechaSolicitudNrant(Date fechaSolicitudNrant) {
        this.fechaSolicitudNrant = fechaSolicitudNrant;
    }

    public Date getFechaAprobacionNrant() {
        return fechaAprobacionNrant;
    }

    public void setFechaAprobacionNrant(Date fechaAprobacionNrant) {
        this.fechaAprobacionNrant = fechaAprobacionNrant;
    }

    public Date getFechaVencimientoNrant() {
        return fechaVencimientoNrant;
    }

    public void setFechaVencimientoNrant(Date fechaVencimientoNrant) {
        this.fechaVencimientoNrant = fechaVencimientoNrant;
    }

    public Integer getNroAnticipoNrant() {
        return nroAnticipoNrant;
    }

    public void setNroAnticipoNrant(Integer nroAnticipoNrant) {
        this.nroAnticipoNrant = nroAnticipoNrant;
    }

    public BigDecimal getMontoSolicitadoNrant() {
        return montoSolicitadoNrant;
    }

    public void setMontoSolicitadoNrant(BigDecimal montoSolicitadoNrant) {
        this.montoSolicitadoNrant = montoSolicitadoNrant;
    }

    public BigDecimal getMontoAprobadoNrant() {
        return montoAprobadoNrant;
    }

    public void setMontoAprobadoNrant(BigDecimal montoAprobadoNrant) {
        this.montoAprobadoNrant = montoAprobadoNrant;
    }

    public Date getFechaRrhhNrant() {
        return fechaRrhhNrant;
    }

    public void setFechaRrhhNrant(Date fechaRrhhNrant) {
        this.fechaRrhhNrant = fechaRrhhNrant;
    }

    public String getResolucionNrant() {
        return resolucionNrant;
    }

    public void setResolucionNrant(String resolucionNrant) {
        this.resolucionNrant = resolucionNrant;
    }

    public Date getFechaResolucionNrant() {
        return fechaResolucionNrant;
    }

    public void setFechaResolucionNrant(Date fechaResolucionNrant) {
        this.fechaResolucionNrant = fechaResolucionNrant;
    }

    public String getObservacionNrant() {
        return observacionNrant;
    }

    public void setObservacionNrant(String observacionNrant) {
        this.observacionNrant = observacionNrant;
    }

    public String getNroMemoNrant() {
        return nroMemoNrant;
    }

    public void setNroMemoNrant(String nroMemoNrant) {
        this.nroMemoNrant = nroMemoNrant;
    }

    public Date getFechaAutorizaNrant() {
        return fechaAutorizaNrant;
    }

    public void setFechaAutorizaNrant(Date fechaAutorizaNrant) {
        this.fechaAutorizaNrant = fechaAutorizaNrant;
    }

    public String getRazonAutorizaNrant() {
        return razonAutorizaNrant;
    }

    public void setRazonAutorizaNrant(String razonAutorizaNrant) {
        this.razonAutorizaNrant = razonAutorizaNrant;
    }

    public Integer getNroMesNrant() {
        return nroMesNrant;
    }

    public void setNroMesNrant(Integer nroMesNrant) {
        this.nroMesNrant = nroMesNrant;
    }

    public String getArchivoMemoNrant() {
        return archivoMemoNrant;
    }

    public void setArchivoMemoNrant(String archivoMemoNrant) {
        this.archivoMemoNrant = archivoMemoNrant;
    }

    public Boolean getActivoNrant() {
        return activoNrant;
    }

    public void setActivoNrant(Boolean activoNrant) {
        this.activoNrant = activoNrant;
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

    public Boolean getAnticipoNrant() {
        return anticipoNrant;
    }

    public void setAnticipoNrant(Boolean anticipoNrant) {
        this.anticipoNrant = anticipoNrant;
    }

    public Boolean getAnuladoNrant() {
        return anuladoNrant;
    }

    public void setAnuladoNrant(Boolean anuladoNrant) {
        this.anuladoNrant = anuladoNrant;
    }

    public Boolean getAbonoNrant() {
        return abonoNrant;
    }

    public void setAbonoNrant(Boolean abonoNrant) {
        this.abonoNrant = abonoNrant;
    }

    public Boolean getReaprobadoNrant() {
        return reaprobadoNrant;
    }

    public void setReaprobadoNrant(Boolean reaprobadoNrant) {
        this.reaprobadoNrant = reaprobadoNrant;
    }

    public Boolean getAprobadoNrant() {
        return aprobadoNrant;
    }

    public void setAprobadoNrant(Boolean aprobadoNrant) {
        this.aprobadoNrant = aprobadoNrant;
    }

    public Boolean getCalificadoNrant() {
        return calificadoNrant;
    }

    public void setCalificadoNrant(Boolean calificadoNrant) {
        this.calificadoNrant = calificadoNrant;
    }

    public NrhMotivoAnticipo getIdeNrmoa() {
        return ideNrmoa;
    }

    public void setIdeNrmoa(NrhMotivoAnticipo ideNrmoa) {
        this.ideNrmoa = ideNrmoa;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp2() {
        return genIdeGeedp2;
    }

    public void setGenIdeGeedp2(GenEmpleadosDepartamentoPar genIdeGeedp2) {
        this.genIdeGeedp2 = genIdeGeedp2;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp3() {
        return genIdeGeedp3;
    }

    public void setGenIdeGeedp3(GenEmpleadosDepartamentoPar genIdeGeedp3) {
        this.genIdeGeedp3 = genIdeGeedp3;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp() {
        return genIdeGeedp;
    }

    public void setGenIdeGeedp(GenEmpleadosDepartamentoPar genIdeGeedp) {
        this.genIdeGeedp = genIdeGeedp;
    }

    @XmlTransient
    public Collection<NrhCapacidadPago> getNrhCapacidadPagoCollection() {
        return nrhCapacidadPagoCollection;
    }

    public void setNrhCapacidadPagoCollection(Collection<NrhCapacidadPago> nrhCapacidadPagoCollection) {
        this.nrhCapacidadPagoCollection = nrhCapacidadPagoCollection;
    }

    @XmlTransient
    public Collection<NrhGarante> getNrhGaranteCollection() {
        return nrhGaranteCollection;
    }

    public void setNrhGaranteCollection(Collection<NrhGarante> nrhGaranteCollection) {
        this.nrhGaranteCollection = nrhGaranteCollection;
    }

    @XmlTransient
    public Collection<NrhAnticipoAbono> getNrhAnticipoAbonoCollection() {
        return nrhAnticipoAbonoCollection;
    }

    public void setNrhAnticipoAbonoCollection(Collection<NrhAnticipoAbono> nrhAnticipoAbonoCollection) {
        this.nrhAnticipoAbonoCollection = nrhAnticipoAbonoCollection;
    }

    @XmlTransient
    public Collection<NrhAnticipoInteres> getNrhAnticipoInteresCollection() {
        return nrhAnticipoInteresCollection;
    }

    public void setNrhAnticipoInteresCollection(Collection<NrhAnticipoInteres> nrhAnticipoInteresCollection) {
        this.nrhAnticipoInteresCollection = nrhAnticipoInteresCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrant != null ? ideNrant.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhAnticipo)) {
            return false;
        }
        NrhAnticipo other = (NrhAnticipo) object;
        if ((this.ideNrant == null && other.ideNrant != null) || (this.ideNrant != null && !this.ideNrant.equals(other.ideNrant))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhAnticipo[ ideNrant=" + ideNrant + " ]";
    }
    
}

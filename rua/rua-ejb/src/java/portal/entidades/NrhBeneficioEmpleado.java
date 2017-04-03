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
@Table(name = "nrh_beneficio_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhBeneficioEmpleado.findAll", query = "SELECT n FROM NrhBeneficioEmpleado n"),
    @NamedQuery(name = "NrhBeneficioEmpleado.findByIdeNrbee", query = "SELECT n FROM NrhBeneficioEmpleado n WHERE n.ideNrbee = :ideNrbee"),
    @NamedQuery(name = "NrhBeneficioEmpleado.findByNroDocumentoNrbee", query = "SELECT n FROM NrhBeneficioEmpleado n WHERE n.nroDocumentoNrbee = :nroDocumentoNrbee"),
    @NamedQuery(name = "NrhBeneficioEmpleado.findByFechaSolicitudNrbee", query = "SELECT n FROM NrhBeneficioEmpleado n WHERE n.fechaSolicitudNrbee = :fechaSolicitudNrbee"),
    @NamedQuery(name = "NrhBeneficioEmpleado.findByFechaAutorizaNrbee", query = "SELECT n FROM NrhBeneficioEmpleado n WHERE n.fechaAutorizaNrbee = :fechaAutorizaNrbee"),
    @NamedQuery(name = "NrhBeneficioEmpleado.findByDetalleNrbee", query = "SELECT n FROM NrhBeneficioEmpleado n WHERE n.detalleNrbee = :detalleNrbee"),
    @NamedQuery(name = "NrhBeneficioEmpleado.findByGuarderiaNrbee", query = "SELECT n FROM NrhBeneficioEmpleado n WHERE n.guarderiaNrbee = :guarderiaNrbee"),
    @NamedQuery(name = "NrhBeneficioEmpleado.findByActivoNrbee", query = "SELECT n FROM NrhBeneficioEmpleado n WHERE n.activoNrbee = :activoNrbee"),
    @NamedQuery(name = "NrhBeneficioEmpleado.findByUsuarioIngre", query = "SELECT n FROM NrhBeneficioEmpleado n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhBeneficioEmpleado.findByFechaIngre", query = "SELECT n FROM NrhBeneficioEmpleado n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhBeneficioEmpleado.findByUsuarioActua", query = "SELECT n FROM NrhBeneficioEmpleado n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhBeneficioEmpleado.findByFechaActua", query = "SELECT n FROM NrhBeneficioEmpleado n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhBeneficioEmpleado.findByHoraIngre", query = "SELECT n FROM NrhBeneficioEmpleado n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhBeneficioEmpleado.findByHoraActua", query = "SELECT n FROM NrhBeneficioEmpleado n WHERE n.horaActua = :horaActua")})
public class NrhBeneficioEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrbee")
    private Integer ideNrbee;
    @Column(name = "nro_documento_nrbee")
    private String nroDocumentoNrbee;
    @Column(name = "fecha_solicitud_nrbee")
    @Temporal(TemporalType.DATE)
    private Date fechaSolicitudNrbee;
    @Column(name = "fecha_autoriza_nrbee")
    @Temporal(TemporalType.DATE)
    private Date fechaAutorizaNrbee;
    @Basic(optional = false)
    @Column(name = "detalle_nrbee")
    private String detalleNrbee;
    @Basic(optional = false)
    @Column(name = "guarderia_nrbee")
    private int guarderiaNrbee;
    @Basic(optional = false)
    @Column(name = "activo_nrbee")
    private boolean activoNrbee;
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
    @OneToMany(mappedBy = "ideNrbee")
    private Collection<NrhHijoGuarderia> nrhHijoGuarderiaCollection;
    @OneToMany(mappedBy = "ideNrbee")
    private Collection<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaCollection;
    @JoinColumn(name = "ide_nrrab", referencedColumnName = "ide_nrrab")
    @ManyToOne(optional = false)
    private NrhRazonBeneficio ideNrrab;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne(optional = false)
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;
    @JoinColumn(name = "gen_ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne(optional = false)
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "gen_ide_geedp2", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp2;

    public NrhBeneficioEmpleado() {
    }

    public NrhBeneficioEmpleado(Integer ideNrbee) {
        this.ideNrbee = ideNrbee;
    }

    public NrhBeneficioEmpleado(Integer ideNrbee, String detalleNrbee, int guarderiaNrbee, boolean activoNrbee) {
        this.ideNrbee = ideNrbee;
        this.detalleNrbee = detalleNrbee;
        this.guarderiaNrbee = guarderiaNrbee;
        this.activoNrbee = activoNrbee;
    }

    public Integer getIdeNrbee() {
        return ideNrbee;
    }

    public void setIdeNrbee(Integer ideNrbee) {
        this.ideNrbee = ideNrbee;
    }

    public String getNroDocumentoNrbee() {
        return nroDocumentoNrbee;
    }

    public void setNroDocumentoNrbee(String nroDocumentoNrbee) {
        this.nroDocumentoNrbee = nroDocumentoNrbee;
    }

    public Date getFechaSolicitudNrbee() {
        return fechaSolicitudNrbee;
    }

    public void setFechaSolicitudNrbee(Date fechaSolicitudNrbee) {
        this.fechaSolicitudNrbee = fechaSolicitudNrbee;
    }

    public Date getFechaAutorizaNrbee() {
        return fechaAutorizaNrbee;
    }

    public void setFechaAutorizaNrbee(Date fechaAutorizaNrbee) {
        this.fechaAutorizaNrbee = fechaAutorizaNrbee;
    }

    public String getDetalleNrbee() {
        return detalleNrbee;
    }

    public void setDetalleNrbee(String detalleNrbee) {
        this.detalleNrbee = detalleNrbee;
    }

    public int getGuarderiaNrbee() {
        return guarderiaNrbee;
    }

    public void setGuarderiaNrbee(int guarderiaNrbee) {
        this.guarderiaNrbee = guarderiaNrbee;
    }

    public boolean getActivoNrbee() {
        return activoNrbee;
    }

    public void setActivoNrbee(boolean activoNrbee) {
        this.activoNrbee = activoNrbee;
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
    public Collection<NrhHijoGuarderia> getNrhHijoGuarderiaCollection() {
        return nrhHijoGuarderiaCollection;
    }

    public void setNrhHijoGuarderiaCollection(Collection<NrhHijoGuarderia> nrhHijoGuarderiaCollection) {
        this.nrhHijoGuarderiaCollection = nrhHijoGuarderiaCollection;
    }

    @XmlTransient
    public Collection<NrhDetalleFacturaGuarderia> getNrhDetalleFacturaGuarderiaCollection() {
        return nrhDetalleFacturaGuarderiaCollection;
    }

    public void setNrhDetalleFacturaGuarderiaCollection(Collection<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaCollection) {
        this.nrhDetalleFacturaGuarderiaCollection = nrhDetalleFacturaGuarderiaCollection;
    }

    public NrhRazonBeneficio getIdeNrrab() {
        return ideNrrab;
    }

    public void setIdeNrrab(NrhRazonBeneficio ideNrrab) {
        this.ideNrrab = ideNrrab;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenInstitucion getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(GenInstitucion ideGeins) {
        this.ideGeins = ideGeins;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp() {
        return genIdeGeedp;
    }

    public void setGenIdeGeedp(GenEmpleadosDepartamentoPar genIdeGeedp) {
        this.genIdeGeedp = genIdeGeedp;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp2() {
        return genIdeGeedp2;
    }

    public void setGenIdeGeedp2(GenEmpleadosDepartamentoPar genIdeGeedp2) {
        this.genIdeGeedp2 = genIdeGeedp2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrbee != null ? ideNrbee.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhBeneficioEmpleado)) {
            return false;
        }
        NrhBeneficioEmpleado other = (NrhBeneficioEmpleado) object;
        if ((this.ideNrbee == null && other.ideNrbee != null) || (this.ideNrbee != null && !this.ideNrbee.equals(other.ideNrbee))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhBeneficioEmpleado[ ideNrbee=" + ideNrbee + " ]";
    }
    
}

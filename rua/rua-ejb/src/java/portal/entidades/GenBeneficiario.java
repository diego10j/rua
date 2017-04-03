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
@Table(name = "gen_beneficiario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenBeneficiario.findAll", query = "SELECT g FROM GenBeneficiario g"),
    @NamedQuery(name = "GenBeneficiario.findByIdeGeben", query = "SELECT g FROM GenBeneficiario g WHERE g.ideGeben = :ideGeben"),
    @NamedQuery(name = "GenBeneficiario.findByTitularGeben", query = "SELECT g FROM GenBeneficiario g WHERE g.titularGeben = :titularGeben"),
    @NamedQuery(name = "GenBeneficiario.findByFechaIngresoGeben", query = "SELECT g FROM GenBeneficiario g WHERE g.fechaIngresoGeben = :fechaIngresoGeben"),
    @NamedQuery(name = "GenBeneficiario.findByRepresentanteLegalGeben", query = "SELECT g FROM GenBeneficiario g WHERE g.representanteLegalGeben = :representanteLegalGeben"),
    @NamedQuery(name = "GenBeneficiario.findByDireccionRepresentanteGeben", query = "SELECT g FROM GenBeneficiario g WHERE g.direccionRepresentanteGeben = :direccionRepresentanteGeben"),
    @NamedQuery(name = "GenBeneficiario.findByTelefonoRepresentanteGeben", query = "SELECT g FROM GenBeneficiario g WHERE g.telefonoRepresentanteGeben = :telefonoRepresentanteGeben"),
    @NamedQuery(name = "GenBeneficiario.findByDocumentoRepresentanteGeben", query = "SELECT g FROM GenBeneficiario g WHERE g.documentoRepresentanteGeben = :documentoRepresentanteGeben"),
    @NamedQuery(name = "GenBeneficiario.findByDocumentoTitularGeben", query = "SELECT g FROM GenBeneficiario g WHERE g.documentoTitularGeben = :documentoTitularGeben"),
    @NamedQuery(name = "GenBeneficiario.findByActivoGeben", query = "SELECT g FROM GenBeneficiario g WHERE g.activoGeben = :activoGeben"),
    @NamedQuery(name = "GenBeneficiario.findByUsuarioIngre", query = "SELECT g FROM GenBeneficiario g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenBeneficiario.findByFechaIngre", query = "SELECT g FROM GenBeneficiario g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenBeneficiario.findByUsuarioActua", query = "SELECT g FROM GenBeneficiario g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenBeneficiario.findByFechaActua", query = "SELECT g FROM GenBeneficiario g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenBeneficiario.findByHoraIngre", query = "SELECT g FROM GenBeneficiario g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenBeneficiario.findByHoraActua", query = "SELECT g FROM GenBeneficiario g WHERE g.horaActua = :horaActua")})
public class GenBeneficiario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_geben")
    private Integer ideGeben;
    @Basic(optional = false)
    @Column(name = "titular_geben")
    private String titularGeben;
    @Column(name = "fecha_ingreso_geben")
    @Temporal(TemporalType.DATE)
    private Date fechaIngresoGeben;
    @Column(name = "representante_legal_geben")
    private String representanteLegalGeben;
    @Column(name = "direccion_representante_geben")
    private String direccionRepresentanteGeben;
    @Column(name = "telefono_representante_geben")
    private String telefonoRepresentanteGeben;
    @Column(name = "documento_representante_geben")
    private String documentoRepresentanteGeben;
    @Basic(optional = false)
    @Column(name = "documento_titular_geben")
    private String documentoTitularGeben;
    @Column(name = "activo_geben")
    private Boolean activoGeben;
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
    @OneToMany(mappedBy = "ideGeben")
    private Collection<GthDireccion> gthDireccionCollection;
    @OneToMany(mappedBy = "ideGeben")
    private Collection<GthTelefono> gthTelefonoCollection;
    @OneToMany(mappedBy = "ideGeben")
    private Collection<NrhRubroAsiento> nrhRubroAsientoCollection;
    @OneToMany(mappedBy = "ideGeben")
    private Collection<GthCuentaBancariaEmpleado> gthCuentaBancariaEmpleadoCollection;
    @OneToMany(mappedBy = "ideGeben")
    private Collection<GthCorreo> gthCorreoCollection;
    @JoinColumn(name = "ide_gttdi", referencedColumnName = "ide_gttdi")
    @ManyToOne(optional = false)
    private GthTipoDocumentoIdentidad ideGttdi;
    @JoinColumn(name = "gth_ide_gttdi", referencedColumnName = "ide_gttdi")
    @ManyToOne
    private GthTipoDocumentoIdentidad gthIdeGttdi;
    @JoinColumn(name = "ide_gttae", referencedColumnName = "ide_gttae")
    @ManyToOne(optional = false)
    private GthTipoActividadEconomica ideGttae;
    @JoinColumn(name = "ide_getic", referencedColumnName = "ide_getic")
    @ManyToOne(optional = false)
    private GenTipoContribuyente ideGetic;
    @OneToMany(mappedBy = "ideGeben")
    private Collection<NrhRetencionJudicial> nrhRetencionJudicialCollection;

    public GenBeneficiario() {
    }

    public GenBeneficiario(Integer ideGeben) {
        this.ideGeben = ideGeben;
    }

    public GenBeneficiario(Integer ideGeben, String titularGeben, String documentoTitularGeben) {
        this.ideGeben = ideGeben;
        this.titularGeben = titularGeben;
        this.documentoTitularGeben = documentoTitularGeben;
    }

    public Integer getIdeGeben() {
        return ideGeben;
    }

    public void setIdeGeben(Integer ideGeben) {
        this.ideGeben = ideGeben;
    }

    public String getTitularGeben() {
        return titularGeben;
    }

    public void setTitularGeben(String titularGeben) {
        this.titularGeben = titularGeben;
    }

    public Date getFechaIngresoGeben() {
        return fechaIngresoGeben;
    }

    public void setFechaIngresoGeben(Date fechaIngresoGeben) {
        this.fechaIngresoGeben = fechaIngresoGeben;
    }

    public String getRepresentanteLegalGeben() {
        return representanteLegalGeben;
    }

    public void setRepresentanteLegalGeben(String representanteLegalGeben) {
        this.representanteLegalGeben = representanteLegalGeben;
    }

    public String getDireccionRepresentanteGeben() {
        return direccionRepresentanteGeben;
    }

    public void setDireccionRepresentanteGeben(String direccionRepresentanteGeben) {
        this.direccionRepresentanteGeben = direccionRepresentanteGeben;
    }

    public String getTelefonoRepresentanteGeben() {
        return telefonoRepresentanteGeben;
    }

    public void setTelefonoRepresentanteGeben(String telefonoRepresentanteGeben) {
        this.telefonoRepresentanteGeben = telefonoRepresentanteGeben;
    }

    public String getDocumentoRepresentanteGeben() {
        return documentoRepresentanteGeben;
    }

    public void setDocumentoRepresentanteGeben(String documentoRepresentanteGeben) {
        this.documentoRepresentanteGeben = documentoRepresentanteGeben;
    }

    public String getDocumentoTitularGeben() {
        return documentoTitularGeben;
    }

    public void setDocumentoTitularGeben(String documentoTitularGeben) {
        this.documentoTitularGeben = documentoTitularGeben;
    }

    public Boolean getActivoGeben() {
        return activoGeben;
    }

    public void setActivoGeben(Boolean activoGeben) {
        this.activoGeben = activoGeben;
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
    public Collection<GthDireccion> getGthDireccionCollection() {
        return gthDireccionCollection;
    }

    public void setGthDireccionCollection(Collection<GthDireccion> gthDireccionCollection) {
        this.gthDireccionCollection = gthDireccionCollection;
    }

    @XmlTransient
    public Collection<GthTelefono> getGthTelefonoCollection() {
        return gthTelefonoCollection;
    }

    public void setGthTelefonoCollection(Collection<GthTelefono> gthTelefonoCollection) {
        this.gthTelefonoCollection = gthTelefonoCollection;
    }

    @XmlTransient
    public Collection<NrhRubroAsiento> getNrhRubroAsientoCollection() {
        return nrhRubroAsientoCollection;
    }

    public void setNrhRubroAsientoCollection(Collection<NrhRubroAsiento> nrhRubroAsientoCollection) {
        this.nrhRubroAsientoCollection = nrhRubroAsientoCollection;
    }

    @XmlTransient
    public Collection<GthCuentaBancariaEmpleado> getGthCuentaBancariaEmpleadoCollection() {
        return gthCuentaBancariaEmpleadoCollection;
    }

    public void setGthCuentaBancariaEmpleadoCollection(Collection<GthCuentaBancariaEmpleado> gthCuentaBancariaEmpleadoCollection) {
        this.gthCuentaBancariaEmpleadoCollection = gthCuentaBancariaEmpleadoCollection;
    }

    @XmlTransient
    public Collection<GthCorreo> getGthCorreoCollection() {
        return gthCorreoCollection;
    }

    public void setGthCorreoCollection(Collection<GthCorreo> gthCorreoCollection) {
        this.gthCorreoCollection = gthCorreoCollection;
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

    public GthTipoActividadEconomica getIdeGttae() {
        return ideGttae;
    }

    public void setIdeGttae(GthTipoActividadEconomica ideGttae) {
        this.ideGttae = ideGttae;
    }

    public GenTipoContribuyente getIdeGetic() {
        return ideGetic;
    }

    public void setIdeGetic(GenTipoContribuyente ideGetic) {
        this.ideGetic = ideGetic;
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
        hash += (ideGeben != null ? ideGeben.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenBeneficiario)) {
            return false;
        }
        GenBeneficiario other = (GenBeneficiario) object;
        if ((this.ideGeben == null && other.ideGeben != null) || (this.ideGeben != null && !this.ideGeben.equals(other.ideGeben))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenBeneficiario[ ideGeben=" + ideGeben + " ]";
    }
    
}

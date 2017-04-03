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
@Table(name = "gth_participa_negocio_emplea")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthParticipaNegocioEmplea.findAll", query = "SELECT g FROM GthParticipaNegocioEmplea g"),
    @NamedQuery(name = "GthParticipaNegocioEmplea.findByIdeGtpne", query = "SELECT g FROM GthParticipaNegocioEmplea g WHERE g.ideGtpne = :ideGtpne"),
    @NamedQuery(name = "GthParticipaNegocioEmplea.findByPrimerNombreGtpne", query = "SELECT g FROM GthParticipaNegocioEmplea g WHERE g.primerNombreGtpne = :primerNombreGtpne"),
    @NamedQuery(name = "GthParticipaNegocioEmplea.findBySegundoNombreGtpne", query = "SELECT g FROM GthParticipaNegocioEmplea g WHERE g.segundoNombreGtpne = :segundoNombreGtpne"),
    @NamedQuery(name = "GthParticipaNegocioEmplea.findByApellidoPaternoGtpne", query = "SELECT g FROM GthParticipaNegocioEmplea g WHERE g.apellidoPaternoGtpne = :apellidoPaternoGtpne"),
    @NamedQuery(name = "GthParticipaNegocioEmplea.findByApellidoMaternoGtpne", query = "SELECT g FROM GthParticipaNegocioEmplea g WHERE g.apellidoMaternoGtpne = :apellidoMaternoGtpne"),
    @NamedQuery(name = "GthParticipaNegocioEmplea.findByDocumentoIdentidadGtpne", query = "SELECT g FROM GthParticipaNegocioEmplea g WHERE g.documentoIdentidadGtpne = :documentoIdentidadGtpne"),
    @NamedQuery(name = "GthParticipaNegocioEmplea.findByIngerenciaAdministraGtpne", query = "SELECT g FROM GthParticipaNegocioEmplea g WHERE g.ingerenciaAdministraGtpne = :ingerenciaAdministraGtpne"),
    @NamedQuery(name = "GthParticipaNegocioEmplea.findByPorcentajeParticipaGtpne", query = "SELECT g FROM GthParticipaNegocioEmplea g WHERE g.porcentajeParticipaGtpne = :porcentajeParticipaGtpne"),
    @NamedQuery(name = "GthParticipaNegocioEmplea.findByActivoGtpne", query = "SELECT g FROM GthParticipaNegocioEmplea g WHERE g.activoGtpne = :activoGtpne"),
    @NamedQuery(name = "GthParticipaNegocioEmplea.findByUsuarioIngre", query = "SELECT g FROM GthParticipaNegocioEmplea g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthParticipaNegocioEmplea.findByFechaIngre", query = "SELECT g FROM GthParticipaNegocioEmplea g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthParticipaNegocioEmplea.findByUsuarioActua", query = "SELECT g FROM GthParticipaNegocioEmplea g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthParticipaNegocioEmplea.findByFechaActua", query = "SELECT g FROM GthParticipaNegocioEmplea g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthParticipaNegocioEmplea.findByHoraIngre", query = "SELECT g FROM GthParticipaNegocioEmplea g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthParticipaNegocioEmplea.findByHoraActua", query = "SELECT g FROM GthParticipaNegocioEmplea g WHERE g.horaActua = :horaActua")})
public class GthParticipaNegocioEmplea implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtpne")
    private Integer ideGtpne;
    @Column(name = "primer_nombre_gtpne")
    private String primerNombreGtpne;
    @Column(name = "segundo_nombre_gtpne")
    private String segundoNombreGtpne;
    @Column(name = "apellido_paterno_gtpne")
    private String apellidoPaternoGtpne;
    @Column(name = "apellido_materno_gtpne")
    private String apellidoMaternoGtpne;
    @Column(name = "documento_identidad_gtpne")
    private String documentoIdentidadGtpne;
    @Column(name = "ingerencia_administra_gtpne")
    private Integer ingerenciaAdministraGtpne;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcentaje_participa_gtpne")
    private BigDecimal porcentajeParticipaGtpne;
    @Column(name = "activo_gtpne")
    private Boolean activoGtpne;
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
    @JoinColumn(name = "ide_gttdi", referencedColumnName = "ide_gttdi")
    @ManyToOne
    private GthTipoDocumentoIdentidad ideGttdi;
    @JoinColumn(name = "ide_gtnee", referencedColumnName = "ide_gtnee")
    @ManyToOne
    private GthNegocioEmpleado ideGtnee;
    @JoinColumn(name = "ide_gtnac", referencedColumnName = "ide_gtnac")
    @ManyToOne
    private GthNacionalidad ideGtnac;

    public GthParticipaNegocioEmplea() {
    }

    public GthParticipaNegocioEmplea(Integer ideGtpne) {
        this.ideGtpne = ideGtpne;
    }

    public Integer getIdeGtpne() {
        return ideGtpne;
    }

    public void setIdeGtpne(Integer ideGtpne) {
        this.ideGtpne = ideGtpne;
    }

    public String getPrimerNombreGtpne() {
        return primerNombreGtpne;
    }

    public void setPrimerNombreGtpne(String primerNombreGtpne) {
        this.primerNombreGtpne = primerNombreGtpne;
    }

    public String getSegundoNombreGtpne() {
        return segundoNombreGtpne;
    }

    public void setSegundoNombreGtpne(String segundoNombreGtpne) {
        this.segundoNombreGtpne = segundoNombreGtpne;
    }

    public String getApellidoPaternoGtpne() {
        return apellidoPaternoGtpne;
    }

    public void setApellidoPaternoGtpne(String apellidoPaternoGtpne) {
        this.apellidoPaternoGtpne = apellidoPaternoGtpne;
    }

    public String getApellidoMaternoGtpne() {
        return apellidoMaternoGtpne;
    }

    public void setApellidoMaternoGtpne(String apellidoMaternoGtpne) {
        this.apellidoMaternoGtpne = apellidoMaternoGtpne;
    }

    public String getDocumentoIdentidadGtpne() {
        return documentoIdentidadGtpne;
    }

    public void setDocumentoIdentidadGtpne(String documentoIdentidadGtpne) {
        this.documentoIdentidadGtpne = documentoIdentidadGtpne;
    }

    public Integer getIngerenciaAdministraGtpne() {
        return ingerenciaAdministraGtpne;
    }

    public void setIngerenciaAdministraGtpne(Integer ingerenciaAdministraGtpne) {
        this.ingerenciaAdministraGtpne = ingerenciaAdministraGtpne;
    }

    public BigDecimal getPorcentajeParticipaGtpne() {
        return porcentajeParticipaGtpne;
    }

    public void setPorcentajeParticipaGtpne(BigDecimal porcentajeParticipaGtpne) {
        this.porcentajeParticipaGtpne = porcentajeParticipaGtpne;
    }

    public Boolean getActivoGtpne() {
        return activoGtpne;
    }

    public void setActivoGtpne(Boolean activoGtpne) {
        this.activoGtpne = activoGtpne;
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

    public GthTipoDocumentoIdentidad getIdeGttdi() {
        return ideGttdi;
    }

    public void setIdeGttdi(GthTipoDocumentoIdentidad ideGttdi) {
        this.ideGttdi = ideGttdi;
    }

    public GthNegocioEmpleado getIdeGtnee() {
        return ideGtnee;
    }

    public void setIdeGtnee(GthNegocioEmpleado ideGtnee) {
        this.ideGtnee = ideGtnee;
    }

    public GthNacionalidad getIdeGtnac() {
        return ideGtnac;
    }

    public void setIdeGtnac(GthNacionalidad ideGtnac) {
        this.ideGtnac = ideGtnac;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtpne != null ? ideGtpne.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthParticipaNegocioEmplea)) {
            return false;
        }
        GthParticipaNegocioEmplea other = (GthParticipaNegocioEmplea) object;
        if ((this.ideGtpne == null && other.ideGtpne != null) || (this.ideGtpne != null && !this.ideGtpne.equals(other.ideGtpne))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthParticipaNegocioEmplea[ ideGtpne=" + ideGtpne + " ]";
    }
    
}

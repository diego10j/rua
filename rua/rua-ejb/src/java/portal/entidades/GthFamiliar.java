/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
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
@Table(name = "gth_familiar")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthFamiliar.findAll", query = "SELECT g FROM GthFamiliar g"),
    @NamedQuery(name = "GthFamiliar.findByIdeGtfam", query = "SELECT g FROM GthFamiliar g WHERE g.ideGtfam = :ideGtfam"),
    @NamedQuery(name = "GthFamiliar.findByPrimerNombreGtfam", query = "SELECT g FROM GthFamiliar g WHERE g.primerNombreGtfam = :primerNombreGtfam"),
    @NamedQuery(name = "GthFamiliar.findBySegundoNombreGtfam", query = "SELECT g FROM GthFamiliar g WHERE g.segundoNombreGtfam = :segundoNombreGtfam"),
    @NamedQuery(name = "GthFamiliar.findByApellidoPaternoGtfam", query = "SELECT g FROM GthFamiliar g WHERE g.apellidoPaternoGtfam = :apellidoPaternoGtfam"),
    @NamedQuery(name = "GthFamiliar.findByApellidoMaternoGtfam", query = "SELECT g FROM GthFamiliar g WHERE g.apellidoMaternoGtfam = :apellidoMaternoGtfam"),
    @NamedQuery(name = "GthFamiliar.findByFechaNacimientoGtfam", query = "SELECT g FROM GthFamiliar g WHERE g.fechaNacimientoGtfam = :fechaNacimientoGtfam"),
    @NamedQuery(name = "GthFamiliar.findByDocumentoIdentidadGtfam", query = "SELECT g FROM GthFamiliar g WHERE g.documentoIdentidadGtfam = :documentoIdentidadGtfam"),
    @NamedQuery(name = "GthFamiliar.findByActivoGtfam", query = "SELECT g FROM GthFamiliar g WHERE g.activoGtfam = :activoGtfam"),
    @NamedQuery(name = "GthFamiliar.findByUsuarioIngre", query = "SELECT g FROM GthFamiliar g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthFamiliar.findByFechaIngre", query = "SELECT g FROM GthFamiliar g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthFamiliar.findByUsuarioActua", query = "SELECT g FROM GthFamiliar g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthFamiliar.findByFechaActua", query = "SELECT g FROM GthFamiliar g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthFamiliar.findByHoraIngre", query = "SELECT g FROM GthFamiliar g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthFamiliar.findByHoraActua", query = "SELECT g FROM GthFamiliar g WHERE g.horaActua = :horaActua")})
public class GthFamiliar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtfam")
    private Integer ideGtfam;
    @Column(name = "primer_nombre_gtfam")
    private String primerNombreGtfam;
    @Column(name = "segundo_nombre_gtfam")
    private String segundoNombreGtfam;
    @Column(name = "apellido_paterno_gtfam")
    private String apellidoPaternoGtfam;
    @Column(name = "apellido_materno_gtfam")
    private String apellidoMaternoGtfam;
    @Column(name = "fecha_nacimiento_gtfam")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimientoGtfam;
    @Column(name = "documento_identidad_gtfam")
    private String documentoIdentidadGtfam;
    @Column(name = "activo_gtfam")
    private Boolean activoGtfam;
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
    @JoinColumn(name = "ide_gttpr", referencedColumnName = "ide_gttpr")
    @ManyToOne
    private GthTipoParentescoRelacion ideGttpr;
    @JoinColumn(name = "ide_gttdi", referencedColumnName = "ide_gttdi")
    @ManyToOne
    private GthTipoDocumentoIdentidad ideGttdi;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_gtacl", referencedColumnName = "ide_gtacl")
    @ManyToOne
    private GthActividadLaboral ideGtacl;

    public GthFamiliar() {
    }

    public GthFamiliar(Integer ideGtfam) {
        this.ideGtfam = ideGtfam;
    }

    public Integer getIdeGtfam() {
        return ideGtfam;
    }

    public void setIdeGtfam(Integer ideGtfam) {
        this.ideGtfam = ideGtfam;
    }

    public String getPrimerNombreGtfam() {
        return primerNombreGtfam;
    }

    public void setPrimerNombreGtfam(String primerNombreGtfam) {
        this.primerNombreGtfam = primerNombreGtfam;
    }

    public String getSegundoNombreGtfam() {
        return segundoNombreGtfam;
    }

    public void setSegundoNombreGtfam(String segundoNombreGtfam) {
        this.segundoNombreGtfam = segundoNombreGtfam;
    }

    public String getApellidoPaternoGtfam() {
        return apellidoPaternoGtfam;
    }

    public void setApellidoPaternoGtfam(String apellidoPaternoGtfam) {
        this.apellidoPaternoGtfam = apellidoPaternoGtfam;
    }

    public String getApellidoMaternoGtfam() {
        return apellidoMaternoGtfam;
    }

    public void setApellidoMaternoGtfam(String apellidoMaternoGtfam) {
        this.apellidoMaternoGtfam = apellidoMaternoGtfam;
    }

    public Date getFechaNacimientoGtfam() {
        return fechaNacimientoGtfam;
    }

    public void setFechaNacimientoGtfam(Date fechaNacimientoGtfam) {
        this.fechaNacimientoGtfam = fechaNacimientoGtfam;
    }

    public String getDocumentoIdentidadGtfam() {
        return documentoIdentidadGtfam;
    }

    public void setDocumentoIdentidadGtfam(String documentoIdentidadGtfam) {
        this.documentoIdentidadGtfam = documentoIdentidadGtfam;
    }

    public Boolean getActivoGtfam() {
        return activoGtfam;
    }

    public void setActivoGtfam(Boolean activoGtfam) {
        this.activoGtfam = activoGtfam;
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

    public GthTipoParentescoRelacion getIdeGttpr() {
        return ideGttpr;
    }

    public void setIdeGttpr(GthTipoParentescoRelacion ideGttpr) {
        this.ideGttpr = ideGttpr;
    }

    public GthTipoDocumentoIdentidad getIdeGttdi() {
        return ideGttdi;
    }

    public void setIdeGttdi(GthTipoDocumentoIdentidad ideGttdi) {
        this.ideGttdi = ideGttdi;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GthActividadLaboral getIdeGtacl() {
        return ideGtacl;
    }

    public void setIdeGtacl(GthActividadLaboral ideGtacl) {
        this.ideGtacl = ideGtacl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtfam != null ? ideGtfam.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthFamiliar)) {
            return false;
        }
        GthFamiliar other = (GthFamiliar) object;
        if ((this.ideGtfam == null && other.ideGtfam != null) || (this.ideGtfam != null && !this.ideGtfam.equals(other.ideGtfam))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthFamiliar[ ideGtfam=" + ideGtfam + " ]";
    }
    
}

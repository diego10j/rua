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
@Table(name = "gth_documentacion_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthDocumentacionEmpleado.findAll", query = "SELECT g FROM GthDocumentacionEmpleado g"),
    @NamedQuery(name = "GthDocumentacionEmpleado.findByIdeGtdce", query = "SELECT g FROM GthDocumentacionEmpleado g WHERE g.ideGtdce = :ideGtdce"),
    @NamedQuery(name = "GthDocumentacionEmpleado.findByNroIessGtdce", query = "SELECT g FROM GthDocumentacionEmpleado g WHERE g.nroIessGtdce = :nroIessGtdce"),
    @NamedQuery(name = "GthDocumentacionEmpleado.findByFechaAfiliacionGtdce", query = "SELECT g FROM GthDocumentacionEmpleado g WHERE g.fechaAfiliacionGtdce = :fechaAfiliacionGtdce"),
    @NamedQuery(name = "GthDocumentacionEmpleado.findByFechaCaducidadGtdce", query = "SELECT g FROM GthDocumentacionEmpleado g WHERE g.fechaCaducidadGtdce = :fechaCaducidadGtdce"),
    @NamedQuery(name = "GthDocumentacionEmpleado.findByNroCasillaGtdce", query = "SELECT g FROM GthDocumentacionEmpleado g WHERE g.nroCasillaGtdce = :nroCasillaGtdce"),
    @NamedQuery(name = "GthDocumentacionEmpleado.findByPasaporteGtdce", query = "SELECT g FROM GthDocumentacionEmpleado g WHERE g.pasaporteGtdce = :pasaporteGtdce"),
    @NamedQuery(name = "GthDocumentacionEmpleado.findByNroCuentaGtdce", query = "SELECT g FROM GthDocumentacionEmpleado g WHERE g.nroCuentaGtdce = :nroCuentaGtdce"),
    @NamedQuery(name = "GthDocumentacionEmpleado.findByActivoGtdce", query = "SELECT g FROM GthDocumentacionEmpleado g WHERE g.activoGtdce = :activoGtdce"),
    @NamedQuery(name = "GthDocumentacionEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthDocumentacionEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthDocumentacionEmpleado.findByFechaIngre", query = "SELECT g FROM GthDocumentacionEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthDocumentacionEmpleado.findByUsuarioActua", query = "SELECT g FROM GthDocumentacionEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthDocumentacionEmpleado.findByFechaActua", query = "SELECT g FROM GthDocumentacionEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthDocumentacionEmpleado.findByHoraIngre", query = "SELECT g FROM GthDocumentacionEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthDocumentacionEmpleado.findByHoraActua", query = "SELECT g FROM GthDocumentacionEmpleado g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GthDocumentacionEmpleado.findByFechaEntregaDeclaracionGtdce", query = "SELECT g FROM GthDocumentacionEmpleado g WHERE g.fechaEntregaDeclaracionGtdce = :fechaEntregaDeclaracionGtdce"),
    @NamedQuery(name = "GthDocumentacionEmpleado.findByFechaProximaDeclaracionGtdce", query = "SELECT g FROM GthDocumentacionEmpleado g WHERE g.fechaProximaDeclaracionGtdce = :fechaProximaDeclaracionGtdce")})
public class GthDocumentacionEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtdce")
    private Integer ideGtdce;
    @Column(name = "nro_iess_gtdce")
    private String nroIessGtdce;
    @Column(name = "fecha_afiliacion_gtdce")
    @Temporal(TemporalType.DATE)
    private Date fechaAfiliacionGtdce;
    @Column(name = "fecha_caducidad_gtdce")
    @Temporal(TemporalType.DATE)
    private Date fechaCaducidadGtdce;
    @Column(name = "nro_casilla_gtdce")
    private String nroCasillaGtdce;
    @Column(name = "pasaporte_gtdce")
    private String pasaporteGtdce;
    @Column(name = "nro_cuenta_gtdce")
    private String nroCuentaGtdce;
    @Column(name = "activo_gtdce")
    private Boolean activoGtdce;
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
    @Column(name = "fecha_entrega_declaracion_gtdce")
    @Temporal(TemporalType.DATE)
    private Date fechaEntregaDeclaracionGtdce;
    @Column(name = "fecha_proxima_declaracion_gtdce")
    @Temporal(TemporalType.DATE)
    private Date fechaProximaDeclaracionGtdce;
    @JoinColumn(name = "ide_gttlc", referencedColumnName = "ide_gttlc")
    @ManyToOne
    private GthTipoLicenciaConducir ideGttlc;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_gecae", referencedColumnName = "ide_gecae")
    @ManyToOne
    private GenCategoriaEstatus ideGecae;

    public GthDocumentacionEmpleado() {
    }

    public GthDocumentacionEmpleado(Integer ideGtdce) {
        this.ideGtdce = ideGtdce;
    }

    public Integer getIdeGtdce() {
        return ideGtdce;
    }

    public void setIdeGtdce(Integer ideGtdce) {
        this.ideGtdce = ideGtdce;
    }

    public String getNroIessGtdce() {
        return nroIessGtdce;
    }

    public void setNroIessGtdce(String nroIessGtdce) {
        this.nroIessGtdce = nroIessGtdce;
    }

    public Date getFechaAfiliacionGtdce() {
        return fechaAfiliacionGtdce;
    }

    public void setFechaAfiliacionGtdce(Date fechaAfiliacionGtdce) {
        this.fechaAfiliacionGtdce = fechaAfiliacionGtdce;
    }

    public Date getFechaCaducidadGtdce() {
        return fechaCaducidadGtdce;
    }

    public void setFechaCaducidadGtdce(Date fechaCaducidadGtdce) {
        this.fechaCaducidadGtdce = fechaCaducidadGtdce;
    }

    public String getNroCasillaGtdce() {
        return nroCasillaGtdce;
    }

    public void setNroCasillaGtdce(String nroCasillaGtdce) {
        this.nroCasillaGtdce = nroCasillaGtdce;
    }

    public String getPasaporteGtdce() {
        return pasaporteGtdce;
    }

    public void setPasaporteGtdce(String pasaporteGtdce) {
        this.pasaporteGtdce = pasaporteGtdce;
    }

    public String getNroCuentaGtdce() {
        return nroCuentaGtdce;
    }

    public void setNroCuentaGtdce(String nroCuentaGtdce) {
        this.nroCuentaGtdce = nroCuentaGtdce;
    }

    public Boolean getActivoGtdce() {
        return activoGtdce;
    }

    public void setActivoGtdce(Boolean activoGtdce) {
        this.activoGtdce = activoGtdce;
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

    public Date getFechaEntregaDeclaracionGtdce() {
        return fechaEntregaDeclaracionGtdce;
    }

    public void setFechaEntregaDeclaracionGtdce(Date fechaEntregaDeclaracionGtdce) {
        this.fechaEntregaDeclaracionGtdce = fechaEntregaDeclaracionGtdce;
    }

    public Date getFechaProximaDeclaracionGtdce() {
        return fechaProximaDeclaracionGtdce;
    }

    public void setFechaProximaDeclaracionGtdce(Date fechaProximaDeclaracionGtdce) {
        this.fechaProximaDeclaracionGtdce = fechaProximaDeclaracionGtdce;
    }

    public GthTipoLicenciaConducir getIdeGttlc() {
        return ideGttlc;
    }

    public void setIdeGttlc(GthTipoLicenciaConducir ideGttlc) {
        this.ideGttlc = ideGttlc;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenCategoriaEstatus getIdeGecae() {
        return ideGecae;
    }

    public void setIdeGecae(GenCategoriaEstatus ideGecae) {
        this.ideGecae = ideGecae;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtdce != null ? ideGtdce.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthDocumentacionEmpleado)) {
            return false;
        }
        GthDocumentacionEmpleado other = (GthDocumentacionEmpleado) object;
        if ((this.ideGtdce == null && other.ideGtdce != null) || (this.ideGtdce != null && !this.ideGtdce.equals(other.ideGtdce))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthDocumentacionEmpleado[ ideGtdce=" + ideGtdce + " ]";
    }
    
}

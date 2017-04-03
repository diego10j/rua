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
@Table(name = "gth_conyuge")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthConyuge.findAll", query = "SELECT g FROM GthConyuge g"),
    @NamedQuery(name = "GthConyuge.findByIdeGtcon", query = "SELECT g FROM GthConyuge g WHERE g.ideGtcon = :ideGtcon"),
    @NamedQuery(name = "GthConyuge.findByDocumentoIdentidadGtcon", query = "SELECT g FROM GthConyuge g WHERE g.documentoIdentidadGtcon = :documentoIdentidadGtcon"),
    @NamedQuery(name = "GthConyuge.findByObservacionGtcon", query = "SELECT g FROM GthConyuge g WHERE g.observacionGtcon = :observacionGtcon"),
    @NamedQuery(name = "GthConyuge.findByLugarTrabajoGtcon", query = "SELECT g FROM GthConyuge g WHERE g.lugarTrabajoGtcon = :lugarTrabajoGtcon"),
    @NamedQuery(name = "GthConyuge.findByActivoGtcon", query = "SELECT g FROM GthConyuge g WHERE g.activoGtcon = :activoGtcon"),
    @NamedQuery(name = "GthConyuge.findByPrimerNombreGtcon", query = "SELECT g FROM GthConyuge g WHERE g.primerNombreGtcon = :primerNombreGtcon"),
    @NamedQuery(name = "GthConyuge.findBySegundoNombreGtcon", query = "SELECT g FROM GthConyuge g WHERE g.segundoNombreGtcon = :segundoNombreGtcon"),
    @NamedQuery(name = "GthConyuge.findByApellidoPaternoGtcon", query = "SELECT g FROM GthConyuge g WHERE g.apellidoPaternoGtcon = :apellidoPaternoGtcon"),
    @NamedQuery(name = "GthConyuge.findByApellidoMaternoGtcon", query = "SELECT g FROM GthConyuge g WHERE g.apellidoMaternoGtcon = :apellidoMaternoGtcon"),
    @NamedQuery(name = "GthConyuge.findByUsuarioIngre", query = "SELECT g FROM GthConyuge g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthConyuge.findByFechaIngre", query = "SELECT g FROM GthConyuge g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthConyuge.findByUsuarioActua", query = "SELECT g FROM GthConyuge g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthConyuge.findByFechaActua", query = "SELECT g FROM GthConyuge g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthConyuge.findByHoraIngre", query = "SELECT g FROM GthConyuge g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthConyuge.findByHoraActua", query = "SELECT g FROM GthConyuge g WHERE g.horaActua = :horaActua")})
public class GthConyuge implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtcon")
    private Integer ideGtcon;
    @Column(name = "documento_identidad_gtcon")
    private String documentoIdentidadGtcon;
    @Column(name = "observacion_gtcon")
    private String observacionGtcon;
    @Column(name = "lugar_trabajo_gtcon")
    private String lugarTrabajoGtcon;
    @Column(name = "activo_gtcon")
    private Boolean activoGtcon;
    @Column(name = "primer_nombre_gtcon")
    private String primerNombreGtcon;
    @Column(name = "segundo_nombre_gtcon")
    private String segundoNombreGtcon;
    @Column(name = "apellido_paterno_gtcon")
    private String apellidoPaternoGtcon;
    @Column(name = "apellido_materno_gtcon")
    private String apellidoMaternoGtcon;
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
    @OneToMany(mappedBy = "ideGtcon")
    private Collection<GthTelefono> gthTelefonoCollection;
    @JoinColumn(name = "ide_gttdi", referencedColumnName = "ide_gttdi")
    @ManyToOne
    private GthTipoDocumentoIdentidad ideGttdi;
    @JoinColumn(name = "ide_gtnac", referencedColumnName = "ide_gtnac")
    @ManyToOne
    private GthNacionalidad ideGtnac;
    @JoinColumn(name = "ide_gtgen", referencedColumnName = "ide_gtgen")
    @ManyToOne
    private GthGenero ideGtgen;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne(optional = false)
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_gtcar", referencedColumnName = "ide_gtcar")
    @ManyToOne
    private GthCargo ideGtcar;
    @OneToMany(mappedBy = "ideGtcon")
    private Collection<GthUnionLibre> gthUnionLibreCollection;

    public GthConyuge() {
    }

    public GthConyuge(Integer ideGtcon) {
        this.ideGtcon = ideGtcon;
    }

    public Integer getIdeGtcon() {
        return ideGtcon;
    }

    public void setIdeGtcon(Integer ideGtcon) {
        this.ideGtcon = ideGtcon;
    }

    public String getDocumentoIdentidadGtcon() {
        return documentoIdentidadGtcon;
    }

    public void setDocumentoIdentidadGtcon(String documentoIdentidadGtcon) {
        this.documentoIdentidadGtcon = documentoIdentidadGtcon;
    }

    public String getObservacionGtcon() {
        return observacionGtcon;
    }

    public void setObservacionGtcon(String observacionGtcon) {
        this.observacionGtcon = observacionGtcon;
    }

    public String getLugarTrabajoGtcon() {
        return lugarTrabajoGtcon;
    }

    public void setLugarTrabajoGtcon(String lugarTrabajoGtcon) {
        this.lugarTrabajoGtcon = lugarTrabajoGtcon;
    }

    public Boolean getActivoGtcon() {
        return activoGtcon;
    }

    public void setActivoGtcon(Boolean activoGtcon) {
        this.activoGtcon = activoGtcon;
    }

    public String getPrimerNombreGtcon() {
        return primerNombreGtcon;
    }

    public void setPrimerNombreGtcon(String primerNombreGtcon) {
        this.primerNombreGtcon = primerNombreGtcon;
    }

    public String getSegundoNombreGtcon() {
        return segundoNombreGtcon;
    }

    public void setSegundoNombreGtcon(String segundoNombreGtcon) {
        this.segundoNombreGtcon = segundoNombreGtcon;
    }

    public String getApellidoPaternoGtcon() {
        return apellidoPaternoGtcon;
    }

    public void setApellidoPaternoGtcon(String apellidoPaternoGtcon) {
        this.apellidoPaternoGtcon = apellidoPaternoGtcon;
    }

    public String getApellidoMaternoGtcon() {
        return apellidoMaternoGtcon;
    }

    public void setApellidoMaternoGtcon(String apellidoMaternoGtcon) {
        this.apellidoMaternoGtcon = apellidoMaternoGtcon;
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
    public Collection<GthTelefono> getGthTelefonoCollection() {
        return gthTelefonoCollection;
    }

    public void setGthTelefonoCollection(Collection<GthTelefono> gthTelefonoCollection) {
        this.gthTelefonoCollection = gthTelefonoCollection;
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

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GthCargo getIdeGtcar() {
        return ideGtcar;
    }

    public void setIdeGtcar(GthCargo ideGtcar) {
        this.ideGtcar = ideGtcar;
    }

    @XmlTransient
    public Collection<GthUnionLibre> getGthUnionLibreCollection() {
        return gthUnionLibreCollection;
    }

    public void setGthUnionLibreCollection(Collection<GthUnionLibre> gthUnionLibreCollection) {
        this.gthUnionLibreCollection = gthUnionLibreCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtcon != null ? ideGtcon.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthConyuge)) {
            return false;
        }
        GthConyuge other = (GthConyuge) object;
        if ((this.ideGtcon == null && other.ideGtcon != null) || (this.ideGtcon != null && !this.ideGtcon.equals(other.ideGtcon))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthConyuge[ ideGtcon=" + ideGtcon + " ]";
    }
    
}

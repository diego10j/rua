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
@Table(name = "gth_cargas_familiares")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthCargasFamiliares.findAll", query = "SELECT g FROM GthCargasFamiliares g"),
    @NamedQuery(name = "GthCargasFamiliares.findByIdeGtcaf", query = "SELECT g FROM GthCargasFamiliares g WHERE g.ideGtcaf = :ideGtcaf"),
    @NamedQuery(name = "GthCargasFamiliares.findByPrimerNombreGtcaf", query = "SELECT g FROM GthCargasFamiliares g WHERE g.primerNombreGtcaf = :primerNombreGtcaf"),
    @NamedQuery(name = "GthCargasFamiliares.findBySegundoNombreGtcaf", query = "SELECT g FROM GthCargasFamiliares g WHERE g.segundoNombreGtcaf = :segundoNombreGtcaf"),
    @NamedQuery(name = "GthCargasFamiliares.findByApellidoPaternoGtcaf", query = "SELECT g FROM GthCargasFamiliares g WHERE g.apellidoPaternoGtcaf = :apellidoPaternoGtcaf"),
    @NamedQuery(name = "GthCargasFamiliares.findByApellidoMaternoGtcaf", query = "SELECT g FROM GthCargasFamiliares g WHERE g.apellidoMaternoGtcaf = :apellidoMaternoGtcaf"),
    @NamedQuery(name = "GthCargasFamiliares.findByFechaNacimientoGtcaf", query = "SELECT g FROM GthCargasFamiliares g WHERE g.fechaNacimientoGtcaf = :fechaNacimientoGtcaf"),
    @NamedQuery(name = "GthCargasFamiliares.findByDocumentoIdentidadGtcaf", query = "SELECT g FROM GthCargasFamiliares g WHERE g.documentoIdentidadGtcaf = :documentoIdentidadGtcaf"),
    @NamedQuery(name = "GthCargasFamiliares.findByActivoGtcaf", query = "SELECT g FROM GthCargasFamiliares g WHERE g.activoGtcaf = :activoGtcaf"),
    @NamedQuery(name = "GthCargasFamiliares.findByUsuarioIngre", query = "SELECT g FROM GthCargasFamiliares g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthCargasFamiliares.findByFechaIngre", query = "SELECT g FROM GthCargasFamiliares g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthCargasFamiliares.findByUsuarioActua", query = "SELECT g FROM GthCargasFamiliares g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthCargasFamiliares.findByFechaActua", query = "SELECT g FROM GthCargasFamiliares g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthCargasFamiliares.findByHoraIngre", query = "SELECT g FROM GthCargasFamiliares g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthCargasFamiliares.findByHoraActua", query = "SELECT g FROM GthCargasFamiliares g WHERE g.horaActua = :horaActua")})
public class GthCargasFamiliares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtcaf")
    private Integer ideGtcaf;
    @Basic(optional = false)
    @Column(name = "primer_nombre_gtcaf")
    private String primerNombreGtcaf;
    @Column(name = "segundo_nombre_gtcaf")
    private String segundoNombreGtcaf;
    @Basic(optional = false)
    @Column(name = "apellido_paterno_gtcaf")
    private String apellidoPaternoGtcaf;
    @Column(name = "apellido_materno_gtcaf")
    private String apellidoMaternoGtcaf;
    @Basic(optional = false)
    @Column(name = "fecha_nacimiento_gtcaf")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimientoGtcaf;
    @Column(name = "documento_identidad_gtcaf")
    private String documentoIdentidadGtcaf;
    @Basic(optional = false)
    @Column(name = "activo_gtcaf")
    private boolean activoGtcaf;
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
    @OneToMany(mappedBy = "ideGtcaf")
    private Collection<NrhHijoGuarderia> nrhHijoGuarderiaCollection;
    @OneToMany(mappedBy = "ideGtcaf")
    private Collection<NrhRetencionCargaFamilia> nrhRetencionCargaFamiliaCollection;
    @JoinColumn(name = "ide_gttpr", referencedColumnName = "ide_gttpr")
    @ManyToOne(optional = false)
    private GthTipoParentescoRelacion ideGttpr;
    @JoinColumn(name = "ide_gttdi", referencedColumnName = "ide_gttdi")
    @ManyToOne(optional = false)
    private GthTipoDocumentoIdentidad ideGttdi;
    @JoinColumn(name = "ide_gtgen", referencedColumnName = "ide_gtgen")
    @ManyToOne(optional = false)
    private GthGenero ideGtgen;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne(optional = false)
    private GthEmpleado ideGtemp;

    public GthCargasFamiliares() {
    }

    public GthCargasFamiliares(Integer ideGtcaf) {
        this.ideGtcaf = ideGtcaf;
    }

    public GthCargasFamiliares(Integer ideGtcaf, String primerNombreGtcaf, String apellidoPaternoGtcaf, Date fechaNacimientoGtcaf, boolean activoGtcaf) {
        this.ideGtcaf = ideGtcaf;
        this.primerNombreGtcaf = primerNombreGtcaf;
        this.apellidoPaternoGtcaf = apellidoPaternoGtcaf;
        this.fechaNacimientoGtcaf = fechaNacimientoGtcaf;
        this.activoGtcaf = activoGtcaf;
    }

    public Integer getIdeGtcaf() {
        return ideGtcaf;
    }

    public void setIdeGtcaf(Integer ideGtcaf) {
        this.ideGtcaf = ideGtcaf;
    }

    public String getPrimerNombreGtcaf() {
        return primerNombreGtcaf;
    }

    public void setPrimerNombreGtcaf(String primerNombreGtcaf) {
        this.primerNombreGtcaf = primerNombreGtcaf;
    }

    public String getSegundoNombreGtcaf() {
        return segundoNombreGtcaf;
    }

    public void setSegundoNombreGtcaf(String segundoNombreGtcaf) {
        this.segundoNombreGtcaf = segundoNombreGtcaf;
    }

    public String getApellidoPaternoGtcaf() {
        return apellidoPaternoGtcaf;
    }

    public void setApellidoPaternoGtcaf(String apellidoPaternoGtcaf) {
        this.apellidoPaternoGtcaf = apellidoPaternoGtcaf;
    }

    public String getApellidoMaternoGtcaf() {
        return apellidoMaternoGtcaf;
    }

    public void setApellidoMaternoGtcaf(String apellidoMaternoGtcaf) {
        this.apellidoMaternoGtcaf = apellidoMaternoGtcaf;
    }

    public Date getFechaNacimientoGtcaf() {
        return fechaNacimientoGtcaf;
    }

    public void setFechaNacimientoGtcaf(Date fechaNacimientoGtcaf) {
        this.fechaNacimientoGtcaf = fechaNacimientoGtcaf;
    }

    public String getDocumentoIdentidadGtcaf() {
        return documentoIdentidadGtcaf;
    }

    public void setDocumentoIdentidadGtcaf(String documentoIdentidadGtcaf) {
        this.documentoIdentidadGtcaf = documentoIdentidadGtcaf;
    }

    public boolean getActivoGtcaf() {
        return activoGtcaf;
    }

    public void setActivoGtcaf(boolean activoGtcaf) {
        this.activoGtcaf = activoGtcaf;
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
    public Collection<NrhRetencionCargaFamilia> getNrhRetencionCargaFamiliaCollection() {
        return nrhRetencionCargaFamiliaCollection;
    }

    public void setNrhRetencionCargaFamiliaCollection(Collection<NrhRetencionCargaFamilia> nrhRetencionCargaFamiliaCollection) {
        this.nrhRetencionCargaFamiliaCollection = nrhRetencionCargaFamiliaCollection;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtcaf != null ? ideGtcaf.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthCargasFamiliares)) {
            return false;
        }
        GthCargasFamiliares other = (GthCargasFamiliares) object;
        if ((this.ideGtcaf == null && other.ideGtcaf != null) || (this.ideGtcaf != null && !this.ideGtcaf.equals(other.ideGtcaf))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthCargasFamiliares[ ideGtcaf=" + ideGtcaf + " ]";
    }
    
}

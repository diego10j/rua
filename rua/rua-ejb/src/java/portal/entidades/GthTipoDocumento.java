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
@Table(name = "gth_tipo_documento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTipoDocumento.findAll", query = "SELECT g FROM GthTipoDocumento g"),
    @NamedQuery(name = "GthTipoDocumento.findByIdeGttdc", query = "SELECT g FROM GthTipoDocumento g WHERE g.ideGttdc = :ideGttdc"),
    @NamedQuery(name = "GthTipoDocumento.findByDetalleGttdc", query = "SELECT g FROM GthTipoDocumento g WHERE g.detalleGttdc = :detalleGttdc"),
    @NamedQuery(name = "GthTipoDocumento.findByUsuarioIngre", query = "SELECT g FROM GthTipoDocumento g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoDocumento.findByFechaIngre", query = "SELECT g FROM GthTipoDocumento g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoDocumento.findByUsuarioActua", query = "SELECT g FROM GthTipoDocumento g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoDocumento.findByFechaActua", query = "SELECT g FROM GthTipoDocumento g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoDocumento.findByHoraIngre", query = "SELECT g FROM GthTipoDocumento g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoDocumento.findByHoraActua", query = "SELECT g FROM GthTipoDocumento g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GthTipoDocumento.findByGuarderiaGttdc", query = "SELECT g FROM GthTipoDocumento g WHERE g.guarderiaGttdc = :guarderiaGttdc"),
    @NamedQuery(name = "GthTipoDocumento.findByActivoGttdc", query = "SELECT g FROM GthTipoDocumento g WHERE g.activoGttdc = :activoGttdc")})
public class GthTipoDocumento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gttdc")
    private Integer ideGttdc;
    @Column(name = "detalle_gttdc")
    private String detalleGttdc;
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
    @Column(name = "guarderia_gttdc")
    private Boolean guarderiaGttdc;
    @Column(name = "activo_gttdc")
    private Boolean activoGttdc;
    @OneToMany(mappedBy = "ideGttdc")
    private Collection<GenModuloDocumento> genModuloDocumentoCollection;

    public GthTipoDocumento() {
    }

    public GthTipoDocumento(Integer ideGttdc) {
        this.ideGttdc = ideGttdc;
    }

    public Integer getIdeGttdc() {
        return ideGttdc;
    }

    public void setIdeGttdc(Integer ideGttdc) {
        this.ideGttdc = ideGttdc;
    }

    public String getDetalleGttdc() {
        return detalleGttdc;
    }

    public void setDetalleGttdc(String detalleGttdc) {
        this.detalleGttdc = detalleGttdc;
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

    public Boolean getGuarderiaGttdc() {
        return guarderiaGttdc;
    }

    public void setGuarderiaGttdc(Boolean guarderiaGttdc) {
        this.guarderiaGttdc = guarderiaGttdc;
    }

    public Boolean getActivoGttdc() {
        return activoGttdc;
    }

    public void setActivoGttdc(Boolean activoGttdc) {
        this.activoGttdc = activoGttdc;
    }

    @XmlTransient
    public Collection<GenModuloDocumento> getGenModuloDocumentoCollection() {
        return genModuloDocumentoCollection;
    }

    public void setGenModuloDocumentoCollection(Collection<GenModuloDocumento> genModuloDocumentoCollection) {
        this.genModuloDocumentoCollection = genModuloDocumentoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttdc != null ? ideGttdc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoDocumento)) {
            return false;
        }
        GthTipoDocumento other = (GthTipoDocumento) object;
        if ((this.ideGttdc == null && other.ideGttdc != null) || (this.ideGttdc != null && !this.ideGttdc.equals(other.ideGttdc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoDocumento[ ideGttdc=" + ideGttdc + " ]";
    }
    
}

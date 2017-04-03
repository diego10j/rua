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
@Table(name = "nrh_tipo_nomina")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhTipoNomina.findAll", query = "SELECT n FROM NrhTipoNomina n"),
    @NamedQuery(name = "NrhTipoNomina.findByIdeNrtin", query = "SELECT n FROM NrhTipoNomina n WHERE n.ideNrtin = :ideNrtin"),
    @NamedQuery(name = "NrhTipoNomina.findByDetalleNrtin", query = "SELECT n FROM NrhTipoNomina n WHERE n.detalleNrtin = :detalleNrtin"),
    @NamedQuery(name = "NrhTipoNomina.findByActivoNrtin", query = "SELECT n FROM NrhTipoNomina n WHERE n.activoNrtin = :activoNrtin"),
    @NamedQuery(name = "NrhTipoNomina.findByUsuarioIngre", query = "SELECT n FROM NrhTipoNomina n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhTipoNomina.findByFechaIngre", query = "SELECT n FROM NrhTipoNomina n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhTipoNomina.findByUsuarioActua", query = "SELECT n FROM NrhTipoNomina n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhTipoNomina.findByFechaActua", query = "SELECT n FROM NrhTipoNomina n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhTipoNomina.findByHoraIngre", query = "SELECT n FROM NrhTipoNomina n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhTipoNomina.findByHoraActua", query = "SELECT n FROM NrhTipoNomina n WHERE n.horaActua = :horaActua")})
public class NrhTipoNomina implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrtin")
    private Integer ideNrtin;
    @Column(name = "detalle_nrtin")
    private String detalleNrtin;
    @Column(name = "activo_nrtin")
    private Boolean activoNrtin;
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
    @OneToMany(mappedBy = "ideNrtin")
    private Collection<NrhDetalleTipoNomina> nrhDetalleTipoNominaCollection;
    @OneToMany(mappedBy = "ideNrtin")
    private Collection<GenTipoAsiento> genTipoAsientoCollection;

    public NrhTipoNomina() {
    }

    public NrhTipoNomina(Integer ideNrtin) {
        this.ideNrtin = ideNrtin;
    }

    public Integer getIdeNrtin() {
        return ideNrtin;
    }

    public void setIdeNrtin(Integer ideNrtin) {
        this.ideNrtin = ideNrtin;
    }

    public String getDetalleNrtin() {
        return detalleNrtin;
    }

    public void setDetalleNrtin(String detalleNrtin) {
        this.detalleNrtin = detalleNrtin;
    }

    public Boolean getActivoNrtin() {
        return activoNrtin;
    }

    public void setActivoNrtin(Boolean activoNrtin) {
        this.activoNrtin = activoNrtin;
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
    public Collection<NrhDetalleTipoNomina> getNrhDetalleTipoNominaCollection() {
        return nrhDetalleTipoNominaCollection;
    }

    public void setNrhDetalleTipoNominaCollection(Collection<NrhDetalleTipoNomina> nrhDetalleTipoNominaCollection) {
        this.nrhDetalleTipoNominaCollection = nrhDetalleTipoNominaCollection;
    }

    @XmlTransient
    public Collection<GenTipoAsiento> getGenTipoAsientoCollection() {
        return genTipoAsientoCollection;
    }

    public void setGenTipoAsientoCollection(Collection<GenTipoAsiento> genTipoAsientoCollection) {
        this.genTipoAsientoCollection = genTipoAsientoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrtin != null ? ideNrtin.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhTipoNomina)) {
            return false;
        }
        NrhTipoNomina other = (NrhTipoNomina) object;
        if ((this.ideNrtin == null && other.ideNrtin != null) || (this.ideNrtin != null && !this.ideNrtin.equals(other.ideNrtin))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhTipoNomina[ ideNrtin=" + ideNrtin + " ]";
    }
    
}

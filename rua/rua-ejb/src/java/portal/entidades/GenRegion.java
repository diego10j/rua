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
@Table(name = "gen_region")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenRegion.findAll", query = "SELECT g FROM GenRegion g"),
    @NamedQuery(name = "GenRegion.findByIdeGereg", query = "SELECT g FROM GenRegion g WHERE g.ideGereg = :ideGereg"),
    @NamedQuery(name = "GenRegion.findByDetalleGereg", query = "SELECT g FROM GenRegion g WHERE g.detalleGereg = :detalleGereg"),
    @NamedQuery(name = "GenRegion.findByActivoGereg", query = "SELECT g FROM GenRegion g WHERE g.activoGereg = :activoGereg"),
    @NamedQuery(name = "GenRegion.findByUsuarioIngre", query = "SELECT g FROM GenRegion g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenRegion.findByFechaIngre", query = "SELECT g FROM GenRegion g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenRegion.findByUsuarioActua", query = "SELECT g FROM GenRegion g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenRegion.findByFechaActua", query = "SELECT g FROM GenRegion g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenRegion.findByHoraIngre", query = "SELECT g FROM GenRegion g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenRegion.findByHoraActua", query = "SELECT g FROM GenRegion g WHERE g.horaActua = :horaActua")})
public class GenRegion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gereg")
    private Integer ideGereg;
    @Column(name = "detalle_gereg")
    private String detalleGereg;
    @Column(name = "activo_gereg")
    private Boolean activoGereg;
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
    @OneToMany(mappedBy = "ideGereg")
    private Collection<GenDivisionPolitica> genDivisionPoliticaCollection;
    @OneToMany(mappedBy = "ideGereg")
    private Collection<NrhDetalleRubro> nrhDetalleRubroCollection;

    public GenRegion() {
    }

    public GenRegion(Integer ideGereg) {
        this.ideGereg = ideGereg;
    }

    public Integer getIdeGereg() {
        return ideGereg;
    }

    public void setIdeGereg(Integer ideGereg) {
        this.ideGereg = ideGereg;
    }

    public String getDetalleGereg() {
        return detalleGereg;
    }

    public void setDetalleGereg(String detalleGereg) {
        this.detalleGereg = detalleGereg;
    }

    public Boolean getActivoGereg() {
        return activoGereg;
    }

    public void setActivoGereg(Boolean activoGereg) {
        this.activoGereg = activoGereg;
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
    public Collection<GenDivisionPolitica> getGenDivisionPoliticaCollection() {
        return genDivisionPoliticaCollection;
    }

    public void setGenDivisionPoliticaCollection(Collection<GenDivisionPolitica> genDivisionPoliticaCollection) {
        this.genDivisionPoliticaCollection = genDivisionPoliticaCollection;
    }

    @XmlTransient
    public Collection<NrhDetalleRubro> getNrhDetalleRubroCollection() {
        return nrhDetalleRubroCollection;
    }

    public void setNrhDetalleRubroCollection(Collection<NrhDetalleRubro> nrhDetalleRubroCollection) {
        this.nrhDetalleRubroCollection = nrhDetalleRubroCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGereg != null ? ideGereg.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenRegion)) {
            return false;
        }
        GenRegion other = (GenRegion) object;
        if ((this.ideGereg == null && other.ideGereg != null) || (this.ideGereg != null && !this.ideGereg.equals(other.ideGereg))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenRegion[ ideGereg=" + ideGereg + " ]";
    }
    
}

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
@Table(name = "gen_area_cargo_jerarquia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenAreaCargoJerarquia.findAll", query = "SELECT g FROM GenAreaCargoJerarquia g"),
    @NamedQuery(name = "GenAreaCargoJerarquia.findByIdeGeacj", query = "SELECT g FROM GenAreaCargoJerarquia g WHERE g.ideGeacj = :ideGeacj"),
    @NamedQuery(name = "GenAreaCargoJerarquia.findByActivoGeacj", query = "SELECT g FROM GenAreaCargoJerarquia g WHERE g.activoGeacj = :activoGeacj"),
    @NamedQuery(name = "GenAreaCargoJerarquia.findByUsuarioIngre", query = "SELECT g FROM GenAreaCargoJerarquia g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenAreaCargoJerarquia.findByFechaIngre", query = "SELECT g FROM GenAreaCargoJerarquia g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenAreaCargoJerarquia.findByHoraIngre", query = "SELECT g FROM GenAreaCargoJerarquia g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenAreaCargoJerarquia.findByUsuarioActua", query = "SELECT g FROM GenAreaCargoJerarquia g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenAreaCargoJerarquia.findByFechaActua", query = "SELECT g FROM GenAreaCargoJerarquia g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenAreaCargoJerarquia.findByHoraActua", query = "SELECT g FROM GenAreaCargoJerarquia g WHERE g.horaActua = :horaActua")})
public class GenAreaCargoJerarquia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_geacj")
    private Long ideGeacj;
    @Column(name = "activo_geacj")
    private Boolean activoGeacj;
    @Column(name = "usuario_ingre")
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "usuario_actua")
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @JoinColumn(name = "ide_genij", referencedColumnName = "ide_genij")
    @ManyToOne
    private GenNivelJerarquico ideGenij;
    @JoinColumn(name = "ide_gecaf", referencedColumnName = "ide_gecaf")
    @ManyToOne
    private GenCargoFuncional ideGecaf;
    @OneToMany(mappedBy = "genIdeGeacj")
    private Collection<GenAreaCargoJerarquia> genAreaCargoJerarquiaCollection;
    @JoinColumn(name = "gen_ide_geacj", referencedColumnName = "ide_geacj")
    @ManyToOne
    private GenAreaCargoJerarquia genIdeGeacj;
    @JoinColumn(name = "ide_geare", referencedColumnName = "ide_geare")
    @ManyToOne
    private GenArea ideGeare;

    public GenAreaCargoJerarquia() {
    }

    public GenAreaCargoJerarquia(Long ideGeacj) {
        this.ideGeacj = ideGeacj;
    }

    public Long getIdeGeacj() {
        return ideGeacj;
    }

    public void setIdeGeacj(Long ideGeacj) {
        this.ideGeacj = ideGeacj;
    }

    public Boolean getActivoGeacj() {
        return activoGeacj;
    }

    public void setActivoGeacj(Boolean activoGeacj) {
        this.activoGeacj = activoGeacj;
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

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
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

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public GenNivelJerarquico getIdeGenij() {
        return ideGenij;
    }

    public void setIdeGenij(GenNivelJerarquico ideGenij) {
        this.ideGenij = ideGenij;
    }

    public GenCargoFuncional getIdeGecaf() {
        return ideGecaf;
    }

    public void setIdeGecaf(GenCargoFuncional ideGecaf) {
        this.ideGecaf = ideGecaf;
    }

    @XmlTransient
    public Collection<GenAreaCargoJerarquia> getGenAreaCargoJerarquiaCollection() {
        return genAreaCargoJerarquiaCollection;
    }

    public void setGenAreaCargoJerarquiaCollection(Collection<GenAreaCargoJerarquia> genAreaCargoJerarquiaCollection) {
        this.genAreaCargoJerarquiaCollection = genAreaCargoJerarquiaCollection;
    }

    public GenAreaCargoJerarquia getGenIdeGeacj() {
        return genIdeGeacj;
    }

    public void setGenIdeGeacj(GenAreaCargoJerarquia genIdeGeacj) {
        this.genIdeGeacj = genIdeGeacj;
    }

    public GenArea getIdeGeare() {
        return ideGeare;
    }

    public void setIdeGeare(GenArea ideGeare) {
        this.ideGeare = ideGeare;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGeacj != null ? ideGeacj.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenAreaCargoJerarquia)) {
            return false;
        }
        GenAreaCargoJerarquia other = (GenAreaCargoJerarquia) object;
        if ((this.ideGeacj == null && other.ideGeacj != null) || (this.ideGeacj != null && !this.ideGeacj.equals(other.ideGeacj))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenAreaCargoJerarquia[ ideGeacj=" + ideGeacj + " ]";
    }
    
}

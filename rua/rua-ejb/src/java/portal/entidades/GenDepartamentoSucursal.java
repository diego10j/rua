/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "gen_departamento_sucursal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenDepartamentoSucursal.findAll", query = "SELECT g FROM GenDepartamentoSucursal g"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByIdeSucu", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.genDepartamentoSucursalPK.ideSucu = :ideSucu"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByIdeGedep", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.genDepartamentoSucursalPK.ideGedep = :ideGedep"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByIdeGeare", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.genDepartamentoSucursalPK.ideGeare = :ideGeare"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByActivoGedes", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.activoGedes = :activoGedes"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByUsuarioIngre", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByFechaIngre", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByUsuarioActua", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByFechaActua", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByHoraIngre", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByHoraActua", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.horaActua = :horaActua")})
public class GenDepartamentoSucursal implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GenDepartamentoSucursalPK genDepartamentoSucursalPK;
    @Column(name = "activo_gedes")
    private Boolean activoGedes;
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
    @JoinColumn(name = "ide_sucu", referencedColumnName = "ide_sucu", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private SisSucursal sisSucursal;
    @JoinColumn(name = "ide_gedep", referencedColumnName = "ide_gedep", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private GenDepartamento genDepartamento;
    @JoinColumn(name = "ide_geare", referencedColumnName = "ide_geare", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private GenArea genArea;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genDepartamentoSucursal")
    private Collection<GenPartidaGrupoCargo> genPartidaGrupoCargoCollection;

    public GenDepartamentoSucursal() {
    }

    public GenDepartamentoSucursal(GenDepartamentoSucursalPK genDepartamentoSucursalPK) {
        this.genDepartamentoSucursalPK = genDepartamentoSucursalPK;
    }

    public GenDepartamentoSucursal(int ideSucu, int ideGedep, int ideGeare) {
        this.genDepartamentoSucursalPK = new GenDepartamentoSucursalPK(ideSucu, ideGedep, ideGeare);
    }

    public GenDepartamentoSucursalPK getGenDepartamentoSucursalPK() {
        return genDepartamentoSucursalPK;
    }

    public void setGenDepartamentoSucursalPK(GenDepartamentoSucursalPK genDepartamentoSucursalPK) {
        this.genDepartamentoSucursalPK = genDepartamentoSucursalPK;
    }

    public Boolean getActivoGedes() {
        return activoGedes;
    }

    public void setActivoGedes(Boolean activoGedes) {
        this.activoGedes = activoGedes;
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

    public SisSucursal getSisSucursal() {
        return sisSucursal;
    }

    public void setSisSucursal(SisSucursal sisSucursal) {
        this.sisSucursal = sisSucursal;
    }

    public GenDepartamento getGenDepartamento() {
        return genDepartamento;
    }

    public void setGenDepartamento(GenDepartamento genDepartamento) {
        this.genDepartamento = genDepartamento;
    }

    public GenArea getGenArea() {
        return genArea;
    }

    public void setGenArea(GenArea genArea) {
        this.genArea = genArea;
    }

    @XmlTransient
    public Collection<GenPartidaGrupoCargo> getGenPartidaGrupoCargoCollection() {
        return genPartidaGrupoCargoCollection;
    }

    public void setGenPartidaGrupoCargoCollection(Collection<GenPartidaGrupoCargo> genPartidaGrupoCargoCollection) {
        this.genPartidaGrupoCargoCollection = genPartidaGrupoCargoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (genDepartamentoSucursalPK != null ? genDepartamentoSucursalPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenDepartamentoSucursal)) {
            return false;
        }
        GenDepartamentoSucursal other = (GenDepartamentoSucursal) object;
        if ((this.genDepartamentoSucursalPK == null && other.genDepartamentoSucursalPK != null) || (this.genDepartamentoSucursalPK != null && !this.genDepartamentoSucursalPK.equals(other.genDepartamentoSucursalPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenDepartamentoSucursal[ genDepartamentoSucursalPK=" + genDepartamentoSucursalPK + " ]";
    }
    
}

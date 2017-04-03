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
import javax.persistence.CascadeType;
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
@Table(name = "gen_departamento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenDepartamento.findAll", query = "SELECT g FROM GenDepartamento g"),
    @NamedQuery(name = "GenDepartamento.findByIdeGedep", query = "SELECT g FROM GenDepartamento g WHERE g.ideGedep = :ideGedep"),
    @NamedQuery(name = "GenDepartamento.findByIdeGeare", query = "SELECT g FROM GenDepartamento g WHERE g.ideGeare = :ideGeare"),
    @NamedQuery(name = "GenDepartamento.findByDetalleGedep", query = "SELECT g FROM GenDepartamento g WHERE g.detalleGedep = :detalleGedep"),
    @NamedQuery(name = "GenDepartamento.findByTipoGedep", query = "SELECT g FROM GenDepartamento g WHERE g.tipoGedep = :tipoGedep"),
    @NamedQuery(name = "GenDepartamento.findByNivelGedep", query = "SELECT g FROM GenDepartamento g WHERE g.nivelGedep = :nivelGedep"),
    @NamedQuery(name = "GenDepartamento.findByNivelOrganicoGedep", query = "SELECT g FROM GenDepartamento g WHERE g.nivelOrganicoGedep = :nivelOrganicoGedep"),
    @NamedQuery(name = "GenDepartamento.findByPosicionHijosGedep", query = "SELECT g FROM GenDepartamento g WHERE g.posicionHijosGedep = :posicionHijosGedep"),
    @NamedQuery(name = "GenDepartamento.findByOrdenGedep", query = "SELECT g FROM GenDepartamento g WHERE g.ordenGedep = :ordenGedep"),
    @NamedQuery(name = "GenDepartamento.findByOrdenImprimeGedep", query = "SELECT g FROM GenDepartamento g WHERE g.ordenImprimeGedep = :ordenImprimeGedep"),
    @NamedQuery(name = "GenDepartamento.findByActivoGedep", query = "SELECT g FROM GenDepartamento g WHERE g.activoGedep = :activoGedep"),
    @NamedQuery(name = "GenDepartamento.findByUsuarioIngre", query = "SELECT g FROM GenDepartamento g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenDepartamento.findByFechaIngre", query = "SELECT g FROM GenDepartamento g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenDepartamento.findByUsuarioActua", query = "SELECT g FROM GenDepartamento g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenDepartamento.findByFechaActua", query = "SELECT g FROM GenDepartamento g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenDepartamento.findByHoraIngre", query = "SELECT g FROM GenDepartamento g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenDepartamento.findByHoraActua", query = "SELECT g FROM GenDepartamento g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GenDepartamento.findByAbreviaturaGedep", query = "SELECT g FROM GenDepartamento g WHERE g.abreviaturaGedep = :abreviaturaGedep")})
public class GenDepartamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gedep")
    private Integer ideGedep;
    @Column(name = "ide_geare")
    private Integer ideGeare;
    @Column(name = "detalle_gedep")
    private String detalleGedep;
    @Column(name = "tipo_gedep")
    private String tipoGedep;
    @Column(name = "nivel_gedep")
    private Integer nivelGedep;
    @Column(name = "nivel_organico_gedep")
    private Integer nivelOrganicoGedep;
    @Column(name = "posicion_hijos_gedep")
    private String posicionHijosGedep;
    @Column(name = "orden_gedep")
    private Integer ordenGedep;
    @Column(name = "orden_imprime_gedep")
    private Integer ordenImprimeGedep;
    @Column(name = "activo_gedep")
    private Boolean activoGedep;
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
    @Column(name = "abreviatura_gedep")
    private String abreviaturaGedep;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genDepartamento")
    private Collection<GenDepartamentoSucursal> genDepartamentoSucursalCollection;
    @OneToMany(mappedBy = "genIdeGedep")
    private Collection<GenDepartamento> genDepartamentoCollection;
    @JoinColumn(name = "gen_ide_gedep", referencedColumnName = "ide_gedep")
    @ManyToOne
    private GenDepartamento genIdeGedep;

    public GenDepartamento() {
    }

    public GenDepartamento(Integer ideGedep) {
        this.ideGedep = ideGedep;
    }

    public Integer getIdeGedep() {
        return ideGedep;
    }

    public void setIdeGedep(Integer ideGedep) {
        this.ideGedep = ideGedep;
    }

    public Integer getIdeGeare() {
        return ideGeare;
    }

    public void setIdeGeare(Integer ideGeare) {
        this.ideGeare = ideGeare;
    }

    public String getDetalleGedep() {
        return detalleGedep;
    }

    public void setDetalleGedep(String detalleGedep) {
        this.detalleGedep = detalleGedep;
    }

    public String getTipoGedep() {
        return tipoGedep;
    }

    public void setTipoGedep(String tipoGedep) {
        this.tipoGedep = tipoGedep;
    }

    public Integer getNivelGedep() {
        return nivelGedep;
    }

    public void setNivelGedep(Integer nivelGedep) {
        this.nivelGedep = nivelGedep;
    }

    public Integer getNivelOrganicoGedep() {
        return nivelOrganicoGedep;
    }

    public void setNivelOrganicoGedep(Integer nivelOrganicoGedep) {
        this.nivelOrganicoGedep = nivelOrganicoGedep;
    }

    public String getPosicionHijosGedep() {
        return posicionHijosGedep;
    }

    public void setPosicionHijosGedep(String posicionHijosGedep) {
        this.posicionHijosGedep = posicionHijosGedep;
    }

    public Integer getOrdenGedep() {
        return ordenGedep;
    }

    public void setOrdenGedep(Integer ordenGedep) {
        this.ordenGedep = ordenGedep;
    }

    public Integer getOrdenImprimeGedep() {
        return ordenImprimeGedep;
    }

    public void setOrdenImprimeGedep(Integer ordenImprimeGedep) {
        this.ordenImprimeGedep = ordenImprimeGedep;
    }

    public Boolean getActivoGedep() {
        return activoGedep;
    }

    public void setActivoGedep(Boolean activoGedep) {
        this.activoGedep = activoGedep;
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

    public String getAbreviaturaGedep() {
        return abreviaturaGedep;
    }

    public void setAbreviaturaGedep(String abreviaturaGedep) {
        this.abreviaturaGedep = abreviaturaGedep;
    }

    @XmlTransient
    public Collection<GenDepartamentoSucursal> getGenDepartamentoSucursalCollection() {
        return genDepartamentoSucursalCollection;
    }

    public void setGenDepartamentoSucursalCollection(Collection<GenDepartamentoSucursal> genDepartamentoSucursalCollection) {
        this.genDepartamentoSucursalCollection = genDepartamentoSucursalCollection;
    }

    @XmlTransient
    public Collection<GenDepartamento> getGenDepartamentoCollection() {
        return genDepartamentoCollection;
    }

    public void setGenDepartamentoCollection(Collection<GenDepartamento> genDepartamentoCollection) {
        this.genDepartamentoCollection = genDepartamentoCollection;
    }

    public GenDepartamento getGenIdeGedep() {
        return genIdeGedep;
    }

    public void setGenIdeGedep(GenDepartamento genIdeGedep) {
        this.genIdeGedep = genIdeGedep;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGedep != null ? ideGedep.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenDepartamento)) {
            return false;
        }
        GenDepartamento other = (GenDepartamento) object;
        if ((this.ideGedep == null && other.ideGedep != null) || (this.ideGedep != null && !this.ideGedep.equals(other.ideGedep))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenDepartamento[ ideGedep=" + ideGedep + " ]";
    }
    
}

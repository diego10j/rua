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
@Table(name = "gth_tipo_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTipoEmpleado.findAll", query = "SELECT g FROM GthTipoEmpleado g"),
    @NamedQuery(name = "GthTipoEmpleado.findByIdeGttem", query = "SELECT g FROM GthTipoEmpleado g WHERE g.ideGttem = :ideGttem"),
    @NamedQuery(name = "GthTipoEmpleado.findByDetalleGttem", query = "SELECT g FROM GthTipoEmpleado g WHERE g.detalleGttem = :detalleGttem"),
    @NamedQuery(name = "GthTipoEmpleado.findByProcesoCoreGttem", query = "SELECT g FROM GthTipoEmpleado g WHERE g.procesoCoreGttem = :procesoCoreGttem"),
    @NamedQuery(name = "GthTipoEmpleado.findByActivoGttem", query = "SELECT g FROM GthTipoEmpleado g WHERE g.activoGttem = :activoGttem"),
    @NamedQuery(name = "GthTipoEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthTipoEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoEmpleado.findByFechaIngre", query = "SELECT g FROM GthTipoEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoEmpleado.findByUsuarioActua", query = "SELECT g FROM GthTipoEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoEmpleado.findByFechaActua", query = "SELECT g FROM GthTipoEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoEmpleado.findByHoraIngre", query = "SELECT g FROM GthTipoEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoEmpleado.findByHoraActua", query = "SELECT g FROM GthTipoEmpleado g WHERE g.horaActua = :horaActua")})
public class GthTipoEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gttem")
    private Integer ideGttem;
    @Column(name = "detalle_gttem")
    private String detalleGttem;
    @Column(name = "proceso_core_gttem")
    private String procesoCoreGttem;
    @Column(name = "activo_gttem")
    private Boolean activoGttem;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttem")
    private Collection<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParCollection;
    @OneToMany(mappedBy = "ideGttem")
    private Collection<NrhDetalleTipoNomina> nrhDetalleTipoNominaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttem")
    private Collection<GenPartidaGrupoCargo> genPartidaGrupoCargoCollection;

    public GthTipoEmpleado() {
    }

    public GthTipoEmpleado(Integer ideGttem) {
        this.ideGttem = ideGttem;
    }

    public Integer getIdeGttem() {
        return ideGttem;
    }

    public void setIdeGttem(Integer ideGttem) {
        this.ideGttem = ideGttem;
    }

    public String getDetalleGttem() {
        return detalleGttem;
    }

    public void setDetalleGttem(String detalleGttem) {
        this.detalleGttem = detalleGttem;
    }

    public String getProcesoCoreGttem() {
        return procesoCoreGttem;
    }

    public void setProcesoCoreGttem(String procesoCoreGttem) {
        this.procesoCoreGttem = procesoCoreGttem;
    }

    public Boolean getActivoGttem() {
        return activoGttem;
    }

    public void setActivoGttem(Boolean activoGttem) {
        this.activoGttem = activoGttem;
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
    public Collection<GenEmpleadosDepartamentoPar> getGenEmpleadosDepartamentoParCollection() {
        return genEmpleadosDepartamentoParCollection;
    }

    public void setGenEmpleadosDepartamentoParCollection(Collection<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParCollection) {
        this.genEmpleadosDepartamentoParCollection = genEmpleadosDepartamentoParCollection;
    }

    @XmlTransient
    public Collection<NrhDetalleTipoNomina> getNrhDetalleTipoNominaCollection() {
        return nrhDetalleTipoNominaCollection;
    }

    public void setNrhDetalleTipoNominaCollection(Collection<NrhDetalleTipoNomina> nrhDetalleTipoNominaCollection) {
        this.nrhDetalleTipoNominaCollection = nrhDetalleTipoNominaCollection;
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
        hash += (ideGttem != null ? ideGttem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoEmpleado)) {
            return false;
        }
        GthTipoEmpleado other = (GthTipoEmpleado) object;
        if ((this.ideGttem == null && other.ideGttem != null) || (this.ideGttem != null && !this.ideGttem.equals(other.ideGttem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoEmpleado[ ideGttem=" + ideGttem + " ]";
    }
    
}

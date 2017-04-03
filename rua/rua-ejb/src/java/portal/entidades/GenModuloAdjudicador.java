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
@Table(name = "gen_modulo_adjudicador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenModuloAdjudicador.findAll", query = "SELECT g FROM GenModuloAdjudicador g"),
    @NamedQuery(name = "GenModuloAdjudicador.findByIdeGemoa", query = "SELECT g FROM GenModuloAdjudicador g WHERE g.ideGemoa = :ideGemoa"),
    @NamedQuery(name = "GenModuloAdjudicador.findByActivoGemoa", query = "SELECT g FROM GenModuloAdjudicador g WHERE g.activoGemoa = :activoGemoa"),
    @NamedQuery(name = "GenModuloAdjudicador.findByUsuarioIngre", query = "SELECT g FROM GenModuloAdjudicador g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenModuloAdjudicador.findByFechaIngre", query = "SELECT g FROM GenModuloAdjudicador g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenModuloAdjudicador.findByHoraIngre", query = "SELECT g FROM GenModuloAdjudicador g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenModuloAdjudicador.findByUsuarioActua", query = "SELECT g FROM GenModuloAdjudicador g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenModuloAdjudicador.findByFechaActua", query = "SELECT g FROM GenModuloAdjudicador g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenModuloAdjudicador.findByHoraActua", query = "SELECT g FROM GenModuloAdjudicador g WHERE g.horaActua = :horaActua")})
public class GenModuloAdjudicador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gemoa")
    private Long ideGemoa;
    @Column(name = "activo_gemoa")
    private Boolean activoGemoa;
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
    @JoinColumn(name = "ide_gemod", referencedColumnName = "ide_gemod")
    @ManyToOne
    private GenModulo ideGemod;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;

    public GenModuloAdjudicador() {
    }

    public GenModuloAdjudicador(Long ideGemoa) {
        this.ideGemoa = ideGemoa;
    }

    public Long getIdeGemoa() {
        return ideGemoa;
    }

    public void setIdeGemoa(Long ideGemoa) {
        this.ideGemoa = ideGemoa;
    }

    public Boolean getActivoGemoa() {
        return activoGemoa;
    }

    public void setActivoGemoa(Boolean activoGemoa) {
        this.activoGemoa = activoGemoa;
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

    public GenModulo getIdeGemod() {
        return ideGemod;
    }

    public void setIdeGemod(GenModulo ideGemod) {
        this.ideGemod = ideGemod;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGemoa != null ? ideGemoa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenModuloAdjudicador)) {
            return false;
        }
        GenModuloAdjudicador other = (GenModuloAdjudicador) object;
        if ((this.ideGemoa == null && other.ideGemoa != null) || (this.ideGemoa != null && !this.ideGemoa.equals(other.ideGemoa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenModuloAdjudicador[ ideGemoa=" + ideGemoa + " ]";
    }
    
}

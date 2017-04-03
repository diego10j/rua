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
@Table(name = "gen_modulo_documento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenModuloDocumento.findAll", query = "SELECT g FROM GenModuloDocumento g"),
    @NamedQuery(name = "GenModuloDocumento.findByIdeGemdo", query = "SELECT g FROM GenModuloDocumento g WHERE g.ideGemdo = :ideGemdo"),
    @NamedQuery(name = "GenModuloDocumento.findByActivoGemdo", query = "SELECT g FROM GenModuloDocumento g WHERE g.activoGemdo = :activoGemdo"),
    @NamedQuery(name = "GenModuloDocumento.findByUsuarioIngre", query = "SELECT g FROM GenModuloDocumento g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenModuloDocumento.findByFechaIngre", query = "SELECT g FROM GenModuloDocumento g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenModuloDocumento.findByHoraIngre", query = "SELECT g FROM GenModuloDocumento g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenModuloDocumento.findByUsuarioActua", query = "SELECT g FROM GenModuloDocumento g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenModuloDocumento.findByFechaActua", query = "SELECT g FROM GenModuloDocumento g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenModuloDocumento.findByHoraActua", query = "SELECT g FROM GenModuloDocumento g WHERE g.horaActua = :horaActua")})
public class GenModuloDocumento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gemdo")
    private Long ideGemdo;
    @Column(name = "activo_gemdo")
    private Boolean activoGemdo;
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

    @JoinColumn(name = "ide_gttdc", referencedColumnName = "ide_gttdc")
    @ManyToOne
    private GthTipoDocumento ideGttdc;
    @JoinColumn(name = "ide_gemod", referencedColumnName = "ide_gemod")
    @ManyToOne
    private GenModulo ideGemod;

    public GenModuloDocumento() {
    }

    public GenModuloDocumento(Long ideGemdo) {
        this.ideGemdo = ideGemdo;
    }

    public Long getIdeGemdo() {
        return ideGemdo;
    }

    public void setIdeGemdo(Long ideGemdo) {
        this.ideGemdo = ideGemdo;
    }

    public Boolean getActivoGemdo() {
        return activoGemdo;
    }

    public void setActivoGemdo(Boolean activoGemdo) {
        this.activoGemdo = activoGemdo;
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



    public GthTipoDocumento getIdeGttdc() {
        return ideGttdc;
    }

    public void setIdeGttdc(GthTipoDocumento ideGttdc) {
        this.ideGttdc = ideGttdc;
    }

    public GenModulo getIdeGemod() {
        return ideGemod;
    }

    public void setIdeGemod(GenModulo ideGemod) {
        this.ideGemod = ideGemod;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGemdo != null ? ideGemdo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenModuloDocumento)) {
            return false;
        }
        GenModuloDocumento other = (GenModuloDocumento) object;
        if ((this.ideGemdo == null && other.ideGemdo != null) || (this.ideGemdo != null && !this.ideGemdo.equals(other.ideGemdo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenModuloDocumento[ ideGemdo=" + ideGemdo + " ]";
    }
    
}

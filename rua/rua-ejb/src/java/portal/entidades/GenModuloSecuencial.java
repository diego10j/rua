/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "gen_modulo_secuencial")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenModuloSecuencial.findAll", query = "SELECT g FROM GenModuloSecuencial g"),
    @NamedQuery(name = "GenModuloSecuencial.findByIdeGemos", query = "SELECT g FROM GenModuloSecuencial g WHERE g.ideGemos = :ideGemos"),
    @NamedQuery(name = "GenModuloSecuencial.findByNumeroSecuencialGemos", query = "SELECT g FROM GenModuloSecuencial g WHERE g.numeroSecuencialGemos = :numeroSecuencialGemos"),
    @NamedQuery(name = "GenModuloSecuencial.findByActivoGemos", query = "SELECT g FROM GenModuloSecuencial g WHERE g.activoGemos = :activoGemos"),
    @NamedQuery(name = "GenModuloSecuencial.findByUsuarioIngre", query = "SELECT g FROM GenModuloSecuencial g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenModuloSecuencial.findByFechaIngre", query = "SELECT g FROM GenModuloSecuencial g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenModuloSecuencial.findByHoraIngre", query = "SELECT g FROM GenModuloSecuencial g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenModuloSecuencial.findByUsuarioActua", query = "SELECT g FROM GenModuloSecuencial g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenModuloSecuencial.findByFechaActua", query = "SELECT g FROM GenModuloSecuencial g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenModuloSecuencial.findByHoraActua", query = "SELECT g FROM GenModuloSecuencial g WHERE g.horaActua = :horaActua")})
public class GenModuloSecuencial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gemos")
    private Long ideGemos;
    @Column(name = "numero_secuencial_gemos")
    private BigInteger numeroSecuencialGemos;
    @Column(name = "activo_gemos")
    private Boolean activoGemos;
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

    public GenModuloSecuencial() {
    }

    public GenModuloSecuencial(Long ideGemos) {
        this.ideGemos = ideGemos;
    }

    public Long getIdeGemos() {
        return ideGemos;
    }

    public void setIdeGemos(Long ideGemos) {
        this.ideGemos = ideGemos;
    }

    public BigInteger getNumeroSecuencialGemos() {
        return numeroSecuencialGemos;
    }

    public void setNumeroSecuencialGemos(BigInteger numeroSecuencialGemos) {
        this.numeroSecuencialGemos = numeroSecuencialGemos;
    }

    public Boolean getActivoGemos() {
        return activoGemos;
    }

    public void setActivoGemos(Boolean activoGemos) {
        this.activoGemos = activoGemos;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGemos != null ? ideGemos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenModuloSecuencial)) {
            return false;
        }
        GenModuloSecuencial other = (GenModuloSecuencial) object;
        if ((this.ideGemos == null && other.ideGemos != null) || (this.ideGemos != null && !this.ideGemos.equals(other.ideGemos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenModuloSecuencial[ ideGemos=" + ideGemos + " ]";
    }
    
}

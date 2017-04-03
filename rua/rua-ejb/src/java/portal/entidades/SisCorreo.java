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
@Table(name = "sis_correo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisCorreo.findAll", query = "SELECT s FROM SisCorreo s"),
    @NamedQuery(name = "SisCorreo.findByIdeCorr", query = "SELECT s FROM SisCorreo s WHERE s.ideCorr = :ideCorr"),
    @NamedQuery(name = "SisCorreo.findBySmtpCorr", query = "SELECT s FROM SisCorreo s WHERE s.smtpCorr = :smtpCorr"),
    @NamedQuery(name = "SisCorreo.findByPuertoCorr", query = "SELECT s FROM SisCorreo s WHERE s.puertoCorr = :puertoCorr"),
    @NamedQuery(name = "SisCorreo.findByUsuarioCorr", query = "SELECT s FROM SisCorreo s WHERE s.usuarioCorr = :usuarioCorr"),
    @NamedQuery(name = "SisCorreo.findByCorreoCorr", query = "SELECT s FROM SisCorreo s WHERE s.correoCorr = :correoCorr"),
    @NamedQuery(name = "SisCorreo.findByClaveCorr", query = "SELECT s FROM SisCorreo s WHERE s.claveCorr = :claveCorr"),
    @NamedQuery(name = "SisCorreo.findByUsuarioIngre", query = "SELECT s FROM SisCorreo s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisCorreo.findByFechaIngre", query = "SELECT s FROM SisCorreo s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisCorreo.findByUsuarioActua", query = "SELECT s FROM SisCorreo s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisCorreo.findByFechaActua", query = "SELECT s FROM SisCorreo s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisCorreo.findByHoraIngre", query = "SELECT s FROM SisCorreo s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisCorreo.findByHoraActua", query = "SELECT s FROM SisCorreo s WHERE s.horaActua = :horaActua")})
public class SisCorreo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_corr")
    private Integer ideCorr;
    @Column(name = "smtp_corr")
    private String smtpCorr;
    @Column(name = "puerto_corr")
    private String puertoCorr;
    @Column(name = "usuario_corr")
    private String usuarioCorr;
    @Column(name = "correo_corr")
    private String correoCorr;
    @Column(name = "clave_corr")
    private String claveCorr;
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

    public SisCorreo() {
    }

    public SisCorreo(Integer ideCorr) {
        this.ideCorr = ideCorr;
    }

    public Integer getIdeCorr() {
        return ideCorr;
    }

    public void setIdeCorr(Integer ideCorr) {
        this.ideCorr = ideCorr;
    }

    public String getSmtpCorr() {
        return smtpCorr;
    }

    public void setSmtpCorr(String smtpCorr) {
        this.smtpCorr = smtpCorr;
    }

    public String getPuertoCorr() {
        return puertoCorr;
    }

    public void setPuertoCorr(String puertoCorr) {
        this.puertoCorr = puertoCorr;
    }

    public String getUsuarioCorr() {
        return usuarioCorr;
    }

    public void setUsuarioCorr(String usuarioCorr) {
        this.usuarioCorr = usuarioCorr;
    }

    public String getCorreoCorr() {
        return correoCorr;
    }

    public void setCorreoCorr(String correoCorr) {
        this.correoCorr = correoCorr;
    }

    public String getClaveCorr() {
        return claveCorr;
    }

    public void setClaveCorr(String claveCorr) {
        this.claveCorr = claveCorr;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCorr != null ? ideCorr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisCorreo)) {
            return false;
        }
        SisCorreo other = (SisCorreo) object;
        if ((this.ideCorr == null && other.ideCorr != null) || (this.ideCorr != null && !this.ideCorr.equals(other.ideCorr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisCorreo[ ideCorr=" + ideCorr + " ]";
    }
    
}

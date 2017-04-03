/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "gth_hobie")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthHobie.findAll", query = "SELECT g FROM GthHobie g"),
    @NamedQuery(name = "GthHobie.findByIdeGthob", query = "SELECT g FROM GthHobie g WHERE g.gthHobiePK.ideGthob = :ideGthob"),
    @NamedQuery(name = "GthHobie.findByIdeGtemp", query = "SELECT g FROM GthHobie g WHERE g.gthHobiePK.ideGtemp = :ideGtemp"),
    @NamedQuery(name = "GthHobie.findByActivoGthob", query = "SELECT g FROM GthHobie g WHERE g.activoGthob = :activoGthob"),
    @NamedQuery(name = "GthHobie.findByUsuarioIngre", query = "SELECT g FROM GthHobie g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthHobie.findByFechaIngre", query = "SELECT g FROM GthHobie g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthHobie.findByUsuarioActua", query = "SELECT g FROM GthHobie g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthHobie.findByFechaActua", query = "SELECT g FROM GthHobie g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthHobie.findByHoraIngre", query = "SELECT g FROM GthHobie g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthHobie.findByHoraActua", query = "SELECT g FROM GthHobie g WHERE g.horaActua = :horaActua")})
public class GthHobie implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GthHobiePK gthHobiePK;
    @Column(name = "activo_gthob")
    private Boolean activoGthob;
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
    @JoinColumn(name = "ide_gttih", referencedColumnName = "ide_gttih")
    @ManyToOne
    private GthTipoHobie ideGttih;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private GthEmpleado gthEmpleado;

    public GthHobie() {
    }

    public GthHobie(GthHobiePK gthHobiePK) {
        this.gthHobiePK = gthHobiePK;
    }

    public GthHobie(int ideGthob, int ideGtemp) {
        this.gthHobiePK = new GthHobiePK(ideGthob, ideGtemp);
    }

    public GthHobiePK getGthHobiePK() {
        return gthHobiePK;
    }

    public void setGthHobiePK(GthHobiePK gthHobiePK) {
        this.gthHobiePK = gthHobiePK;
    }

    public Boolean getActivoGthob() {
        return activoGthob;
    }

    public void setActivoGthob(Boolean activoGthob) {
        this.activoGthob = activoGthob;
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

    public GthTipoHobie getIdeGttih() {
        return ideGttih;
    }

    public void setIdeGttih(GthTipoHobie ideGttih) {
        this.ideGttih = ideGttih;
    }

    public GthEmpleado getGthEmpleado() {
        return gthEmpleado;
    }

    public void setGthEmpleado(GthEmpleado gthEmpleado) {
        this.gthEmpleado = gthEmpleado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gthHobiePK != null ? gthHobiePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthHobie)) {
            return false;
        }
        GthHobie other = (GthHobie) object;
        if ((this.gthHobiePK == null && other.gthHobiePK != null) || (this.gthHobiePK != null && !this.gthHobiePK.equals(other.gthHobiePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthHobie[ gthHobiePK=" + gthHobiePK + " ]";
    }
    
}

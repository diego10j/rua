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
@Table(name = "gen_lugar_aplica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenLugarAplica.findAll", query = "SELECT g FROM GenLugarAplica g"),
    @NamedQuery(name = "GenLugarAplica.findByIdeGelua", query = "SELECT g FROM GenLugarAplica g WHERE g.ideGelua = :ideGelua"),
    @NamedQuery(name = "GenLugarAplica.findByDetalleGelua", query = "SELECT g FROM GenLugarAplica g WHERE g.detalleGelua = :detalleGelua"),
    @NamedQuery(name = "GenLugarAplica.findByActivoGelua", query = "SELECT g FROM GenLugarAplica g WHERE g.activoGelua = :activoGelua"),
    @NamedQuery(name = "GenLugarAplica.findByUsuarioIngre", query = "SELECT g FROM GenLugarAplica g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenLugarAplica.findByFechaIngre", query = "SELECT g FROM GenLugarAplica g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenLugarAplica.findByUsuarioActua", query = "SELECT g FROM GenLugarAplica g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenLugarAplica.findByFechaActua", query = "SELECT g FROM GenLugarAplica g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenLugarAplica.findByHoraIngre", query = "SELECT g FROM GenLugarAplica g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenLugarAplica.findByHoraActua", query = "SELECT g FROM GenLugarAplica g WHERE g.horaActua = :horaActua")})
public class GenLugarAplica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gelua")
    private Integer ideGelua;
    @Basic(optional = false)
    @Column(name = "detalle_gelua")
    private String detalleGelua;
    @Basic(optional = false)
    @Column(name = "activo_gelua")
    private boolean activoGelua;
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

    public GenLugarAplica() {
    }

    public GenLugarAplica(Integer ideGelua) {
        this.ideGelua = ideGelua;
    }

    public GenLugarAplica(Integer ideGelua, String detalleGelua, boolean activoGelua) {
        this.ideGelua = ideGelua;
        this.detalleGelua = detalleGelua;
        this.activoGelua = activoGelua;
    }

    public Integer getIdeGelua() {
        return ideGelua;
    }

    public void setIdeGelua(Integer ideGelua) {
        this.ideGelua = ideGelua;
    }

    public String getDetalleGelua() {
        return detalleGelua;
    }

    public void setDetalleGelua(String detalleGelua) {
        this.detalleGelua = detalleGelua;
    }

    public boolean getActivoGelua() {
        return activoGelua;
    }

    public void setActivoGelua(boolean activoGelua) {
        this.activoGelua = activoGelua;
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
        hash += (ideGelua != null ? ideGelua.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenLugarAplica)) {
            return false;
        }
        GenLugarAplica other = (GenLugarAplica) object;
        if ((this.ideGelua == null && other.ideGelua != null) || (this.ideGelua != null && !this.ideGelua.equals(other.ideGelua))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenLugarAplica[ ideGelua=" + ideGelua + " ]";
    }
    
}

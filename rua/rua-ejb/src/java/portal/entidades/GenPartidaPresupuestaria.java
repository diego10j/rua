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
@Table(name = "gen_partida_presupuestaria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenPartidaPresupuestaria.findAll", query = "SELECT g FROM GenPartidaPresupuestaria g"),
    @NamedQuery(name = "GenPartidaPresupuestaria.findByIdeGepap", query = "SELECT g FROM GenPartidaPresupuestaria g WHERE g.ideGepap = :ideGepap"),
    @NamedQuery(name = "GenPartidaPresupuestaria.findByDetalleGepap", query = "SELECT g FROM GenPartidaPresupuestaria g WHERE g.detalleGepap = :detalleGepap"),
    @NamedQuery(name = "GenPartidaPresupuestaria.findByCodigoPartidaGepap", query = "SELECT g FROM GenPartidaPresupuestaria g WHERE g.codigoPartidaGepap = :codigoPartidaGepap"),
    @NamedQuery(name = "GenPartidaPresupuestaria.findByActivoGepap", query = "SELECT g FROM GenPartidaPresupuestaria g WHERE g.activoGepap = :activoGepap"),
    @NamedQuery(name = "GenPartidaPresupuestaria.findByUsuarioIngre", query = "SELECT g FROM GenPartidaPresupuestaria g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenPartidaPresupuestaria.findByFechaIngre", query = "SELECT g FROM GenPartidaPresupuestaria g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenPartidaPresupuestaria.findByUsuarioActua", query = "SELECT g FROM GenPartidaPresupuestaria g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenPartidaPresupuestaria.findByFechaActua", query = "SELECT g FROM GenPartidaPresupuestaria g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenPartidaPresupuestaria.findByHoraIngre", query = "SELECT g FROM GenPartidaPresupuestaria g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenPartidaPresupuestaria.findByHoraActua", query = "SELECT g FROM GenPartidaPresupuestaria g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GenPartidaPresupuestaria.findByIdePrcla", query = "SELECT g FROM GenPartidaPresupuestaria g WHERE g.idePrcla = :idePrcla")})
public class GenPartidaPresupuestaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gepap")
    private Integer ideGepap;
    @Basic(optional = false)
    @Column(name = "detalle_gepap")
    private String detalleGepap;
    @Basic(optional = false)
    @Column(name = "codigo_partida_gepap")
    private String codigoPartidaGepap;
    @Basic(optional = false)
    @Column(name = "activo_gepap")
    private boolean activoGepap;
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
    @Column(name = "ide_prcla")
    private Integer idePrcla;


    public GenPartidaPresupuestaria() {
    }

    public GenPartidaPresupuestaria(Integer ideGepap) {
        this.ideGepap = ideGepap;
    }

    public GenPartidaPresupuestaria(Integer ideGepap, String detalleGepap, String codigoPartidaGepap, boolean activoGepap) {
        this.ideGepap = ideGepap;
        this.detalleGepap = detalleGepap;
        this.codigoPartidaGepap = codigoPartidaGepap;
        this.activoGepap = activoGepap;
    }

    public Integer getIdeGepap() {
        return ideGepap;
    }

    public void setIdeGepap(Integer ideGepap) {
        this.ideGepap = ideGepap;
    }

    public String getDetalleGepap() {
        return detalleGepap;
    }

    public void setDetalleGepap(String detalleGepap) {
        this.detalleGepap = detalleGepap;
    }

    public String getCodigoPartidaGepap() {
        return codigoPartidaGepap;
    }

    public void setCodigoPartidaGepap(String codigoPartidaGepap) {
        this.codigoPartidaGepap = codigoPartidaGepap;
    }

    public boolean getActivoGepap() {
        return activoGepap;
    }

    public void setActivoGepap(boolean activoGepap) {
        this.activoGepap = activoGepap;
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

    public Integer getIdePrcla() {
        return idePrcla;
    }

    public void setIdePrcla(Integer idePrcla) {
        this.idePrcla = idePrcla;
    }

 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGepap != null ? ideGepap.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenPartidaPresupuestaria)) {
            return false;
        }
        GenPartidaPresupuestaria other = (GenPartidaPresupuestaria) object;
        if ((this.ideGepap == null && other.ideGepap != null) || (this.ideGepap != null && !this.ideGepap.equals(other.ideGepap))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenPartidaPresupuestaria[ ideGepap=" + ideGepap + " ]";
    }
    
}

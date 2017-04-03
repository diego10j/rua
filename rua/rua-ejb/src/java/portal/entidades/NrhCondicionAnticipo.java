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
@Table(name = "nrh_condicion_anticipo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhCondicionAnticipo.findAll", query = "SELECT n FROM NrhCondicionAnticipo n"),
    @NamedQuery(name = "NrhCondicionAnticipo.findByIdeNrcoa", query = "SELECT n FROM NrhCondicionAnticipo n WHERE n.ideNrcoa = :ideNrcoa"),
    @NamedQuery(name = "NrhCondicionAnticipo.findByNroRemuneracionesNrcoa", query = "SELECT n FROM NrhCondicionAnticipo n WHERE n.nroRemuneracionesNrcoa = :nroRemuneracionesNrcoa"),
    @NamedQuery(name = "NrhCondicionAnticipo.findByPlazoMaximoPagoNrcoa", query = "SELECT n FROM NrhCondicionAnticipo n WHERE n.plazoMaximoPagoNrcoa = :plazoMaximoPagoNrcoa"),
    @NamedQuery(name = "NrhCondicionAnticipo.findByMinimoTrabajoNrcoa", query = "SELECT n FROM NrhCondicionAnticipo n WHERE n.minimoTrabajoNrcoa = :minimoTrabajoNrcoa"),
    @NamedQuery(name = "NrhCondicionAnticipo.findByActivoNrcoa", query = "SELECT n FROM NrhCondicionAnticipo n WHERE n.activoNrcoa = :activoNrcoa"),
    @NamedQuery(name = "NrhCondicionAnticipo.findByUsuarioIngre", query = "SELECT n FROM NrhCondicionAnticipo n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhCondicionAnticipo.findByFechaIngre", query = "SELECT n FROM NrhCondicionAnticipo n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhCondicionAnticipo.findByUsuarioActua", query = "SELECT n FROM NrhCondicionAnticipo n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhCondicionAnticipo.findByFechaActua", query = "SELECT n FROM NrhCondicionAnticipo n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhCondicionAnticipo.findByHoraIngre", query = "SELECT n FROM NrhCondicionAnticipo n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhCondicionAnticipo.findByHoraActua", query = "SELECT n FROM NrhCondicionAnticipo n WHERE n.horaActua = :horaActua")})
public class NrhCondicionAnticipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrcoa")
    private Integer ideNrcoa;
    @Column(name = "nro_remuneraciones_nrcoa")
    private Integer nroRemuneracionesNrcoa;
    @Column(name = "plazo_maximo_pago_nrcoa")
    private Integer plazoMaximoPagoNrcoa;
    @Column(name = "minimo_trabajo_nrcoa")
    private Integer minimoTrabajoNrcoa;
    @Column(name = "activo_nrcoa")
    private Boolean activoNrcoa;
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
    @JoinColumn(name = "ide_gttco", referencedColumnName = "ide_gttco")
    @ManyToOne
    private GthTipoContrato ideGttco;

    public NrhCondicionAnticipo() {
    }

    public NrhCondicionAnticipo(Integer ideNrcoa) {
        this.ideNrcoa = ideNrcoa;
    }

    public Integer getIdeNrcoa() {
        return ideNrcoa;
    }

    public void setIdeNrcoa(Integer ideNrcoa) {
        this.ideNrcoa = ideNrcoa;
    }

    public Integer getNroRemuneracionesNrcoa() {
        return nroRemuneracionesNrcoa;
    }

    public void setNroRemuneracionesNrcoa(Integer nroRemuneracionesNrcoa) {
        this.nroRemuneracionesNrcoa = nroRemuneracionesNrcoa;
    }

    public Integer getPlazoMaximoPagoNrcoa() {
        return plazoMaximoPagoNrcoa;
    }

    public void setPlazoMaximoPagoNrcoa(Integer plazoMaximoPagoNrcoa) {
        this.plazoMaximoPagoNrcoa = plazoMaximoPagoNrcoa;
    }

    public Integer getMinimoTrabajoNrcoa() {
        return minimoTrabajoNrcoa;
    }

    public void setMinimoTrabajoNrcoa(Integer minimoTrabajoNrcoa) {
        this.minimoTrabajoNrcoa = minimoTrabajoNrcoa;
    }

    public Boolean getActivoNrcoa() {
        return activoNrcoa;
    }

    public void setActivoNrcoa(Boolean activoNrcoa) {
        this.activoNrcoa = activoNrcoa;
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

    public GthTipoContrato getIdeGttco() {
        return ideGttco;
    }

    public void setIdeGttco(GthTipoContrato ideGttco) {
        this.ideGttco = ideGttco;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrcoa != null ? ideNrcoa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhCondicionAnticipo)) {
            return false;
        }
        NrhCondicionAnticipo other = (NrhCondicionAnticipo) object;
        if ((this.ideNrcoa == null && other.ideNrcoa != null) || (this.ideNrcoa != null && !this.ideNrcoa.equals(other.ideNrcoa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhCondicionAnticipo[ ideNrcoa=" + ideNrcoa + " ]";
    }
    
}

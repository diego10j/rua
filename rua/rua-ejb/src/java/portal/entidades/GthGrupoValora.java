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
@Table(name = "gth_grupo_valora")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthGrupoValora.findAll", query = "SELECT g FROM GthGrupoValora g"),
    @NamedQuery(name = "GthGrupoValora.findByIdeGtgrv", query = "SELECT g FROM GthGrupoValora g WHERE g.ideGtgrv = :ideGtgrv"),
    @NamedQuery(name = "GthGrupoValora.findByPuntosGtgrv", query = "SELECT g FROM GthGrupoValora g WHERE g.puntosGtgrv = :puntosGtgrv"),
    @NamedQuery(name = "GthGrupoValora.findByActivoGtgrv", query = "SELECT g FROM GthGrupoValora g WHERE g.activoGtgrv = :activoGtgrv"),
    @NamedQuery(name = "GthGrupoValora.findByUsuarioIngre", query = "SELECT g FROM GthGrupoValora g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthGrupoValora.findByFechaIngre", query = "SELECT g FROM GthGrupoValora g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthGrupoValora.findByHoraIngre", query = "SELECT g FROM GthGrupoValora g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthGrupoValora.findByUsuarioActua", query = "SELECT g FROM GthGrupoValora g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthGrupoValora.findByFechaActua", query = "SELECT g FROM GthGrupoValora g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthGrupoValora.findByHoraActua", query = "SELECT g FROM GthGrupoValora g WHERE g.horaActua = :horaActua")})
public class GthGrupoValora implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtgrv")
    private Long ideGtgrv;
    @Column(name = "puntos_gtgrv")
    private BigInteger puntosGtgrv;
    @Column(name = "activo_gtgrv")
    private Boolean activoGtgrv;
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
    @JoinColumn(name = "ide_gtfav", referencedColumnName = "ide_gtfav")
    @ManyToOne
    private GthFactorValoracion ideGtfav;
    @JoinColumn(name = "ide_gegro", referencedColumnName = "ide_gegro")
    @ManyToOne
    private GenGrupoOcupacional ideGegro;

    public GthGrupoValora() {
    }

    public GthGrupoValora(Long ideGtgrv) {
        this.ideGtgrv = ideGtgrv;
    }

    public Long getIdeGtgrv() {
        return ideGtgrv;
    }

    public void setIdeGtgrv(Long ideGtgrv) {
        this.ideGtgrv = ideGtgrv;
    }

    public BigInteger getPuntosGtgrv() {
        return puntosGtgrv;
    }

    public void setPuntosGtgrv(BigInteger puntosGtgrv) {
        this.puntosGtgrv = puntosGtgrv;
    }

    public Boolean getActivoGtgrv() {
        return activoGtgrv;
    }

    public void setActivoGtgrv(Boolean activoGtgrv) {
        this.activoGtgrv = activoGtgrv;
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

    public GthFactorValoracion getIdeGtfav() {
        return ideGtfav;
    }

    public void setIdeGtfav(GthFactorValoracion ideGtfav) {
        this.ideGtfav = ideGtfav;
    }

    public GenGrupoOcupacional getIdeGegro() {
        return ideGegro;
    }

    public void setIdeGegro(GenGrupoOcupacional ideGegro) {
        this.ideGegro = ideGegro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtgrv != null ? ideGtgrv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthGrupoValora)) {
            return false;
        }
        GthGrupoValora other = (GthGrupoValora) object;
        if ((this.ideGtgrv == null && other.ideGtgrv != null) || (this.ideGtgrv != null && !this.ideGtgrv.equals(other.ideGtgrv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthGrupoValora[ ideGtgrv=" + ideGtgrv + " ]";
    }
    
}

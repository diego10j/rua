/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "sri_detalle_proyecccion_ingres")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SriDetalleProyecccionIngres.findAll", query = "SELECT s FROM SriDetalleProyecccionIngres s"),
    @NamedQuery(name = "SriDetalleProyecccionIngres.findByIdeSrdpi", query = "SELECT s FROM SriDetalleProyecccionIngres s WHERE s.ideSrdpi = :ideSrdpi"),
    @NamedQuery(name = "SriDetalleProyecccionIngres.findByIdeGemes", query = "SELECT s FROM SriDetalleProyecccionIngres s WHERE s.ideGemes = :ideGemes"),
    @NamedQuery(name = "SriDetalleProyecccionIngres.findByValorSrdpi", query = "SELECT s FROM SriDetalleProyecccionIngres s WHERE s.valorSrdpi = :valorSrdpi"),
    @NamedQuery(name = "SriDetalleProyecccionIngres.findByUsuarioIngre", query = "SELECT s FROM SriDetalleProyecccionIngres s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SriDetalleProyecccionIngres.findByFechaIngre", query = "SELECT s FROM SriDetalleProyecccionIngres s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SriDetalleProyecccionIngres.findByUsuarioActua", query = "SELECT s FROM SriDetalleProyecccionIngres s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SriDetalleProyecccionIngres.findByFechaActua", query = "SELECT s FROM SriDetalleProyecccionIngres s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SriDetalleProyecccionIngres.findByHoraIngre", query = "SELECT s FROM SriDetalleProyecccionIngres s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SriDetalleProyecccionIngres.findByHoraActua", query = "SELECT s FROM SriDetalleProyecccionIngres s WHERE s.horaActua = :horaActua")})
public class SriDetalleProyecccionIngres implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_srdpi")
    private Integer ideSrdpi;
    @Column(name = "ide_gemes")
    private Integer ideGemes;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_srdpi")
    private BigDecimal valorSrdpi;
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
    @JoinColumn(name = "ide_srpri", referencedColumnName = "ide_srpri")
    @ManyToOne
    private SriProyeccionIngres ideSrpri;

    public SriDetalleProyecccionIngres() {
    }

    public SriDetalleProyecccionIngres(Integer ideSrdpi) {
        this.ideSrdpi = ideSrdpi;
    }

    public Integer getIdeSrdpi() {
        return ideSrdpi;
    }

    public void setIdeSrdpi(Integer ideSrdpi) {
        this.ideSrdpi = ideSrdpi;
    }

    public Integer getIdeGemes() {
        return ideGemes;
    }

    public void setIdeGemes(Integer ideGemes) {
        this.ideGemes = ideGemes;
    }

    public BigDecimal getValorSrdpi() {
        return valorSrdpi;
    }

    public void setValorSrdpi(BigDecimal valorSrdpi) {
        this.valorSrdpi = valorSrdpi;
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

    public SriProyeccionIngres getIdeSrpri() {
        return ideSrpri;
    }

    public void setIdeSrpri(SriProyeccionIngres ideSrpri) {
        this.ideSrpri = ideSrpri;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSrdpi != null ? ideSrdpi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SriDetalleProyecccionIngres)) {
            return false;
        }
        SriDetalleProyecccionIngres other = (SriDetalleProyecccionIngres) object;
        if ((this.ideSrdpi == null && other.ideSrdpi != null) || (this.ideSrdpi != null && !this.ideSrdpi.equals(other.ideSrdpi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SriDetalleProyecccionIngres[ ideSrdpi=" + ideSrdpi + " ]";
    }
    
}

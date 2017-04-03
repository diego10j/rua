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
@Table(name = "nrh_detalle_rol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhDetalleRol.findAll", query = "SELECT n FROM NrhDetalleRol n"),
    @NamedQuery(name = "NrhDetalleRol.findByIdeNrdro", query = "SELECT n FROM NrhDetalleRol n WHERE n.ideNrdro = :ideNrdro"),
    @NamedQuery(name = "NrhDetalleRol.findByValorNrdro", query = "SELECT n FROM NrhDetalleRol n WHERE n.valorNrdro = :valorNrdro"),
    @NamedQuery(name = "NrhDetalleRol.findByOrdenCalculoNrdro", query = "SELECT n FROM NrhDetalleRol n WHERE n.ordenCalculoNrdro = :ordenCalculoNrdro"),
    @NamedQuery(name = "NrhDetalleRol.findByUsuarioIngre", query = "SELECT n FROM NrhDetalleRol n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhDetalleRol.findByFechaIngre", query = "SELECT n FROM NrhDetalleRol n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhDetalleRol.findByUsuarioActua", query = "SELECT n FROM NrhDetalleRol n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhDetalleRol.findByFechaActua", query = "SELECT n FROM NrhDetalleRol n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhDetalleRol.findByHoraIngre", query = "SELECT n FROM NrhDetalleRol n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhDetalleRol.findByHoraActua", query = "SELECT n FROM NrhDetalleRol n WHERE n.horaActua = :horaActua"),
    @NamedQuery(name = "NrhDetalleRol.findByIdeNrrolDecimos", query = "SELECT n FROM NrhDetalleRol n WHERE n.ideNrrolDecimos = :ideNrrolDecimos")})
public class NrhDetalleRol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrdro")
    private Integer ideNrdro;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "valor_nrdro")
    private BigDecimal valorNrdro;
    @Column(name = "orden_calculo_nrdro")
    private Integer ordenCalculoNrdro;
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
    @Column(name = "ide_nrrol_decimos")
    private Integer ideNrrolDecimos;
    @JoinColumn(name = "ide_nrrol", referencedColumnName = "ide_nrrol")
    @ManyToOne(optional = false)
    private NrhRol ideNrrol;
    @JoinColumn(name = "ide_nrder", referencedColumnName = "ide_nrder")
    @ManyToOne(optional = false)
    private NrhDetalleRubro ideNrder;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne(optional = false)
    private GenEmpleadosDepartamentoPar ideGeedp;

    public NrhDetalleRol() {
    }

    public NrhDetalleRol(Integer ideNrdro) {
        this.ideNrdro = ideNrdro;
    }

    public NrhDetalleRol(Integer ideNrdro, BigDecimal valorNrdro) {
        this.ideNrdro = ideNrdro;
        this.valorNrdro = valorNrdro;
    }

    public Integer getIdeNrdro() {
        return ideNrdro;
    }

    public void setIdeNrdro(Integer ideNrdro) {
        this.ideNrdro = ideNrdro;
    }

    public BigDecimal getValorNrdro() {
        return valorNrdro;
    }

    public void setValorNrdro(BigDecimal valorNrdro) {
        this.valorNrdro = valorNrdro;
    }

    public Integer getOrdenCalculoNrdro() {
        return ordenCalculoNrdro;
    }

    public void setOrdenCalculoNrdro(Integer ordenCalculoNrdro) {
        this.ordenCalculoNrdro = ordenCalculoNrdro;
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

    public Integer getIdeNrrolDecimos() {
        return ideNrrolDecimos;
    }

    public void setIdeNrrolDecimos(Integer ideNrrolDecimos) {
        this.ideNrrolDecimos = ideNrrolDecimos;
    }

    public NrhRol getIdeNrrol() {
        return ideNrrol;
    }

    public void setIdeNrrol(NrhRol ideNrrol) {
        this.ideNrrol = ideNrrol;
    }

    public NrhDetalleRubro getIdeNrder() {
        return ideNrder;
    }

    public void setIdeNrder(NrhDetalleRubro ideNrder) {
        this.ideNrder = ideNrder;
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
        hash += (ideNrdro != null ? ideNrdro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhDetalleRol)) {
            return false;
        }
        NrhDetalleRol other = (NrhDetalleRol) object;
        if ((this.ideNrdro == null && other.ideNrdro != null) || (this.ideNrdro != null && !this.ideNrdro.equals(other.ideNrdro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhDetalleRol[ ideNrdro=" + ideNrdro + " ]";
    }
    
}

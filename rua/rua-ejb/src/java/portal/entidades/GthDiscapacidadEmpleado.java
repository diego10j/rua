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
@Table(name = "gth_discapacidad_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthDiscapacidadEmpleado.findAll", query = "SELECT g FROM GthDiscapacidadEmpleado g"),
    @NamedQuery(name = "GthDiscapacidadEmpleado.findByIdeGtdie", query = "SELECT g FROM GthDiscapacidadEmpleado g WHERE g.ideGtdie = :ideGtdie"),
    @NamedQuery(name = "GthDiscapacidadEmpleado.findByFechaEmisionGtdie", query = "SELECT g FROM GthDiscapacidadEmpleado g WHERE g.fechaEmisionGtdie = :fechaEmisionGtdie"),
    @NamedQuery(name = "GthDiscapacidadEmpleado.findByNroCarnetGtdie", query = "SELECT g FROM GthDiscapacidadEmpleado g WHERE g.nroCarnetGtdie = :nroCarnetGtdie"),
    @NamedQuery(name = "GthDiscapacidadEmpleado.findByPorcentajeGtdie", query = "SELECT g FROM GthDiscapacidadEmpleado g WHERE g.porcentajeGtdie = :porcentajeGtdie"),
    @NamedQuery(name = "GthDiscapacidadEmpleado.findByActivoGtdie", query = "SELECT g FROM GthDiscapacidadEmpleado g WHERE g.activoGtdie = :activoGtdie"),
    @NamedQuery(name = "GthDiscapacidadEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthDiscapacidadEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthDiscapacidadEmpleado.findByFechaIngre", query = "SELECT g FROM GthDiscapacidadEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthDiscapacidadEmpleado.findByUsuarioActua", query = "SELECT g FROM GthDiscapacidadEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthDiscapacidadEmpleado.findByFechaActua", query = "SELECT g FROM GthDiscapacidadEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthDiscapacidadEmpleado.findByHoraIngre", query = "SELECT g FROM GthDiscapacidadEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthDiscapacidadEmpleado.findByHoraActua", query = "SELECT g FROM GthDiscapacidadEmpleado g WHERE g.horaActua = :horaActua")})
public class GthDiscapacidadEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtdie")
    private Integer ideGtdie;
    @Column(name = "fecha_emision_gtdie")
    @Temporal(TemporalType.DATE)
    private Date fechaEmisionGtdie;
    @Basic(optional = false)
    @Column(name = "nro_carnet_gtdie")
    private String nroCarnetGtdie;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "porcentaje_gtdie")
    private BigDecimal porcentajeGtdie;
    @Basic(optional = false)
    @Column(name = "activo_gtdie")
    private boolean activoGtdie;
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
    @JoinColumn(name = "ide_gttds", referencedColumnName = "ide_gttds")
    @ManyToOne(optional = false)
    private GthTipoDiscapacidad ideGttds;
    @JoinColumn(name = "ide_gtgrd", referencedColumnName = "ide_gtgrd")
    @ManyToOne
    private GthGradoDiscapacidad ideGtgrd;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne(optional = false)
    private GthEmpleado ideGtemp;

    public GthDiscapacidadEmpleado() {
    }

    public GthDiscapacidadEmpleado(Integer ideGtdie) {
        this.ideGtdie = ideGtdie;
    }

    public GthDiscapacidadEmpleado(Integer ideGtdie, String nroCarnetGtdie, BigDecimal porcentajeGtdie, boolean activoGtdie) {
        this.ideGtdie = ideGtdie;
        this.nroCarnetGtdie = nroCarnetGtdie;
        this.porcentajeGtdie = porcentajeGtdie;
        this.activoGtdie = activoGtdie;
    }

    public Integer getIdeGtdie() {
        return ideGtdie;
    }

    public void setIdeGtdie(Integer ideGtdie) {
        this.ideGtdie = ideGtdie;
    }

    public Date getFechaEmisionGtdie() {
        return fechaEmisionGtdie;
    }

    public void setFechaEmisionGtdie(Date fechaEmisionGtdie) {
        this.fechaEmisionGtdie = fechaEmisionGtdie;
    }

    public String getNroCarnetGtdie() {
        return nroCarnetGtdie;
    }

    public void setNroCarnetGtdie(String nroCarnetGtdie) {
        this.nroCarnetGtdie = nroCarnetGtdie;
    }

    public BigDecimal getPorcentajeGtdie() {
        return porcentajeGtdie;
    }

    public void setPorcentajeGtdie(BigDecimal porcentajeGtdie) {
        this.porcentajeGtdie = porcentajeGtdie;
    }

    public boolean getActivoGtdie() {
        return activoGtdie;
    }

    public void setActivoGtdie(boolean activoGtdie) {
        this.activoGtdie = activoGtdie;
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

    public GthTipoDiscapacidad getIdeGttds() {
        return ideGttds;
    }

    public void setIdeGttds(GthTipoDiscapacidad ideGttds) {
        this.ideGttds = ideGttds;
    }

    public GthGradoDiscapacidad getIdeGtgrd() {
        return ideGtgrd;
    }

    public void setIdeGtgrd(GthGradoDiscapacidad ideGtgrd) {
        this.ideGtgrd = ideGtgrd;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtdie != null ? ideGtdie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthDiscapacidadEmpleado)) {
            return false;
        }
        GthDiscapacidadEmpleado other = (GthDiscapacidadEmpleado) object;
        if ((this.ideGtdie == null && other.ideGtdie != null) || (this.ideGtdie != null && !this.ideGtdie.equals(other.ideGtdie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthDiscapacidadEmpleado[ ideGtdie=" + ideGtdie + " ]";
    }
    
}

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
@Table(name = "gth_archivo_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthArchivoEmpleado.findAll", query = "SELECT g FROM GthArchivoEmpleado g"),
    @NamedQuery(name = "GthArchivoEmpleado.findByIdeGtaem", query = "SELECT g FROM GthArchivoEmpleado g WHERE g.ideGtaem = :ideGtaem"),
    @NamedQuery(name = "GthArchivoEmpleado.findByIdeGttdc", query = "SELECT g FROM GthArchivoEmpleado g WHERE g.ideGttdc = :ideGttdc"),
    @NamedQuery(name = "GthArchivoEmpleado.findByIdeNrbee", query = "SELECT g FROM GthArchivoEmpleado g WHERE g.ideNrbee = :ideNrbee"),
    @NamedQuery(name = "GthArchivoEmpleado.findByIdeAspvh", query = "SELECT g FROM GthArchivoEmpleado g WHERE g.ideAspvh = :ideAspvh"),
    @NamedQuery(name = "GthArchivoEmpleado.findByDetalleGtaem", query = "SELECT g FROM GthArchivoEmpleado g WHERE g.detalleGtaem = :detalleGtaem"),
    @NamedQuery(name = "GthArchivoEmpleado.findByFechaArchivo", query = "SELECT g FROM GthArchivoEmpleado g WHERE g.fechaArchivo = :fechaArchivo"),
    @NamedQuery(name = "GthArchivoEmpleado.findByActivoGtaem", query = "SELECT g FROM GthArchivoEmpleado g WHERE g.activoGtaem = :activoGtaem"),
    @NamedQuery(name = "GthArchivoEmpleado.findByPathGtaem", query = "SELECT g FROM GthArchivoEmpleado g WHERE g.pathGtaem = :pathGtaem"),
    @NamedQuery(name = "GthArchivoEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthArchivoEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthArchivoEmpleado.findByFechaIngre", query = "SELECT g FROM GthArchivoEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthArchivoEmpleado.findByUsuarioActua", query = "SELECT g FROM GthArchivoEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthArchivoEmpleado.findByFechaActua", query = "SELECT g FROM GthArchivoEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthArchivoEmpleado.findByHoraIngre", query = "SELECT g FROM GthArchivoEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthArchivoEmpleado.findByHoraActua", query = "SELECT g FROM GthArchivoEmpleado g WHERE g.horaActua = :horaActua")})
public class GthArchivoEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtaem")
    private Integer ideGtaem;
    @Column(name = "ide_gttdc")
    private Integer ideGttdc;
    @Column(name = "ide_nrbee")
    private Integer ideNrbee;
    @Column(name = "ide_aspvh")
    private Integer ideAspvh;
    @Column(name = "detalle_gtaem")
    private String detalleGtaem;
    @Column(name = "fecha_archivo")
    @Temporal(TemporalType.DATE)
    private Date fechaArchivo;
    @Column(name = "activo_gtaem")
    private Boolean activoGtaem;
    @Column(name = "path_gtaem")
    private String pathGtaem;
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
    @JoinColumn(name = "ide_gttar", referencedColumnName = "ide_gttar")
    @ManyToOne
    private GthTipoArchivo ideGttar;
    @JoinColumn(name = "ide_gtnee", referencedColumnName = "ide_gtnee")
    @ManyToOne
    private GthNegocioEmpleado ideGtnee;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;

    public GthArchivoEmpleado() {
    }

    public GthArchivoEmpleado(Integer ideGtaem) {
        this.ideGtaem = ideGtaem;
    }

    public Integer getIdeGtaem() {
        return ideGtaem;
    }

    public void setIdeGtaem(Integer ideGtaem) {
        this.ideGtaem = ideGtaem;
    }

    public Integer getIdeGttdc() {
        return ideGttdc;
    }

    public void setIdeGttdc(Integer ideGttdc) {
        this.ideGttdc = ideGttdc;
    }

    public Integer getIdeNrbee() {
        return ideNrbee;
    }

    public void setIdeNrbee(Integer ideNrbee) {
        this.ideNrbee = ideNrbee;
    }

    public Integer getIdeAspvh() {
        return ideAspvh;
    }

    public void setIdeAspvh(Integer ideAspvh) {
        this.ideAspvh = ideAspvh;
    }

    public String getDetalleGtaem() {
        return detalleGtaem;
    }

    public void setDetalleGtaem(String detalleGtaem) {
        this.detalleGtaem = detalleGtaem;
    }

    public Date getFechaArchivo() {
        return fechaArchivo;
    }

    public void setFechaArchivo(Date fechaArchivo) {
        this.fechaArchivo = fechaArchivo;
    }

    public Boolean getActivoGtaem() {
        return activoGtaem;
    }

    public void setActivoGtaem(Boolean activoGtaem) {
        this.activoGtaem = activoGtaem;
    }

    public String getPathGtaem() {
        return pathGtaem;
    }

    public void setPathGtaem(String pathGtaem) {
        this.pathGtaem = pathGtaem;
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

    public GthTipoArchivo getIdeGttar() {
        return ideGttar;
    }

    public void setIdeGttar(GthTipoArchivo ideGttar) {
        this.ideGttar = ideGttar;
    }

    public GthNegocioEmpleado getIdeGtnee() {
        return ideGtnee;
    }

    public void setIdeGtnee(GthNegocioEmpleado ideGtnee) {
        this.ideGtnee = ideGtnee;
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
        hash += (ideGtaem != null ? ideGtaem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthArchivoEmpleado)) {
            return false;
        }
        GthArchivoEmpleado other = (GthArchivoEmpleado) object;
        if ((this.ideGtaem == null && other.ideGtaem != null) || (this.ideGtaem != null && !this.ideGtaem.equals(other.ideGtaem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthArchivoEmpleado[ ideGtaem=" + ideGtaem + " ]";
    }
    
}

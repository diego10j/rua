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
@Table(name = "gth_amigos_empresa_emplea")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthAmigosEmpresaEmplea.findAll", query = "SELECT g FROM GthAmigosEmpresaEmplea g"),
    @NamedQuery(name = "GthAmigosEmpresaEmplea.findByIdeGtaee", query = "SELECT g FROM GthAmigosEmpresaEmplea g WHERE g.ideGtaee = :ideGtaee"),
    @NamedQuery(name = "GthAmigosEmpresaEmplea.findByIdeGecaf", query = "SELECT g FROM GthAmigosEmpresaEmplea g WHERE g.ideGecaf = :ideGecaf"),
    @NamedQuery(name = "GthAmigosEmpresaEmplea.findByIdeGeare", query = "SELECT g FROM GthAmigosEmpresaEmplea g WHERE g.ideGeare = :ideGeare"),
    @NamedQuery(name = "GthAmigosEmpresaEmplea.findByPrimerNombreGtaee", query = "SELECT g FROM GthAmigosEmpresaEmplea g WHERE g.primerNombreGtaee = :primerNombreGtaee"),
    @NamedQuery(name = "GthAmigosEmpresaEmplea.findBySegundoNombreGtaee", query = "SELECT g FROM GthAmigosEmpresaEmplea g WHERE g.segundoNombreGtaee = :segundoNombreGtaee"),
    @NamedQuery(name = "GthAmigosEmpresaEmplea.findByApellidoPaternoGtaee", query = "SELECT g FROM GthAmigosEmpresaEmplea g WHERE g.apellidoPaternoGtaee = :apellidoPaternoGtaee"),
    @NamedQuery(name = "GthAmigosEmpresaEmplea.findByApellidoMaternoGtaee", query = "SELECT g FROM GthAmigosEmpresaEmplea g WHERE g.apellidoMaternoGtaee = :apellidoMaternoGtaee"),
    @NamedQuery(name = "GthAmigosEmpresaEmplea.findByActivoGtaee", query = "SELECT g FROM GthAmigosEmpresaEmplea g WHERE g.activoGtaee = :activoGtaee"),
    @NamedQuery(name = "GthAmigosEmpresaEmplea.findByUsuarioIngre", query = "SELECT g FROM GthAmigosEmpresaEmplea g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthAmigosEmpresaEmplea.findByFechaIngre", query = "SELECT g FROM GthAmigosEmpresaEmplea g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthAmigosEmpresaEmplea.findByUsuarioActua", query = "SELECT g FROM GthAmigosEmpresaEmplea g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthAmigosEmpresaEmplea.findByFechaActua", query = "SELECT g FROM GthAmigosEmpresaEmplea g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthAmigosEmpresaEmplea.findByHoraIngre", query = "SELECT g FROM GthAmigosEmpresaEmplea g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthAmigosEmpresaEmplea.findByHoraActua", query = "SELECT g FROM GthAmigosEmpresaEmplea g WHERE g.horaActua = :horaActua")})
public class GthAmigosEmpresaEmplea implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtaee")
    private Integer ideGtaee;
    @Column(name = "ide_gecaf")
    private Integer ideGecaf;
    @Column(name = "ide_geare")
    private Integer ideGeare;
    @Column(name = "primer_nombre_gtaee")
    private String primerNombreGtaee;
    @Column(name = "segundo_nombre_gtaee")
    private String segundoNombreGtaee;
    @Column(name = "apellido_paterno_gtaee")
    private String apellidoPaternoGtaee;
    @Column(name = "apellido_materno_gtaee")
    private String apellidoMaternoGtaee;
    @Column(name = "activo_gtaee")
    private Boolean activoGtaee;
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
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;

    public GthAmigosEmpresaEmplea() {
    }

    public GthAmigosEmpresaEmplea(Integer ideGtaee) {
        this.ideGtaee = ideGtaee;
    }

    public Integer getIdeGtaee() {
        return ideGtaee;
    }

    public void setIdeGtaee(Integer ideGtaee) {
        this.ideGtaee = ideGtaee;
    }

    public Integer getIdeGecaf() {
        return ideGecaf;
    }

    public void setIdeGecaf(Integer ideGecaf) {
        this.ideGecaf = ideGecaf;
    }

    public Integer getIdeGeare() {
        return ideGeare;
    }

    public void setIdeGeare(Integer ideGeare) {
        this.ideGeare = ideGeare;
    }

    public String getPrimerNombreGtaee() {
        return primerNombreGtaee;
    }

    public void setPrimerNombreGtaee(String primerNombreGtaee) {
        this.primerNombreGtaee = primerNombreGtaee;
    }

    public String getSegundoNombreGtaee() {
        return segundoNombreGtaee;
    }

    public void setSegundoNombreGtaee(String segundoNombreGtaee) {
        this.segundoNombreGtaee = segundoNombreGtaee;
    }

    public String getApellidoPaternoGtaee() {
        return apellidoPaternoGtaee;
    }

    public void setApellidoPaternoGtaee(String apellidoPaternoGtaee) {
        this.apellidoPaternoGtaee = apellidoPaternoGtaee;
    }

    public String getApellidoMaternoGtaee() {
        return apellidoMaternoGtaee;
    }

    public void setApellidoMaternoGtaee(String apellidoMaternoGtaee) {
        this.apellidoMaternoGtaee = apellidoMaternoGtaee;
    }

    public Boolean getActivoGtaee() {
        return activoGtaee;
    }

    public void setActivoGtaee(Boolean activoGtaee) {
        this.activoGtaee = activoGtaee;
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

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtaee != null ? ideGtaee.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthAmigosEmpresaEmplea)) {
            return false;
        }
        GthAmigosEmpresaEmplea other = (GthAmigosEmpresaEmplea) object;
        if ((this.ideGtaee == null && other.ideGtaee != null) || (this.ideGtaee != null && !this.ideGtaee.equals(other.ideGtaee))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthAmigosEmpresaEmplea[ ideGtaee=" + ideGtaee + " ]";
    }
    
}

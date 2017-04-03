/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author User
 */
@Entity
@Table(name = "sis_usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisUsuario.findAll", query = "SELECT s FROM SisUsuario s"),
    @NamedQuery(name = "SisUsuario.findByIdeUsua", query = "SELECT s FROM SisUsuario s WHERE s.ideUsua = :ideUsua"),
    @NamedQuery(name = "SisUsuario.findByNomUsua", query = "SELECT s FROM SisUsuario s WHERE s.nomUsua = :nomUsua"),
    @NamedQuery(name = "SisUsuario.findByNickUsua", query = "SELECT s FROM SisUsuario s WHERE s.nickUsua = :nickUsua"),
    @NamedQuery(name = "SisUsuario.findByMailUsua", query = "SELECT s FROM SisUsuario s WHERE s.mailUsua = :mailUsua"),
    @NamedQuery(name = "SisUsuario.findByFechaRegUsua", query = "SELECT s FROM SisUsuario s WHERE s.fechaRegUsua = :fechaRegUsua"),
    @NamedQuery(name = "SisUsuario.findByActivoUsua", query = "SELECT s FROM SisUsuario s WHERE s.activoUsua = :activoUsua"),
    @NamedQuery(name = "SisUsuario.findByTemaUsua", query = "SELECT s FROM SisUsuario s WHERE s.temaUsua = :temaUsua"),
    @NamedQuery(name = "SisUsuario.findByBloqueadoUsua", query = "SELECT s FROM SisUsuario s WHERE s.bloqueadoUsua = :bloqueadoUsua"),
    @NamedQuery(name = "SisUsuario.findByFechaCaducUsua", query = "SELECT s FROM SisUsuario s WHERE s.fechaCaducUsua = :fechaCaducUsua"),
    @NamedQuery(name = "SisUsuario.findByCambiaClaveUsua", query = "SELECT s FROM SisUsuario s WHERE s.cambiaClaveUsua = :cambiaClaveUsua"),
    @NamedQuery(name = "SisUsuario.findByIdeGtemp", query = "SELECT s FROM SisUsuario s WHERE s.ideGtemp = :ideGtemp")})
public class SisUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_usua")
    private Long ideUsua;
    @Basic(optional = false)
    @Column(name = "nom_usua")
    private String nomUsua;
    @Basic(optional = false)
    @Column(name = "nick_usua")
    private String nickUsua;
    @Column(name = "mail_usua")
    private String mailUsua;
    @Column(name = "fecha_reg_usua")
    @Temporal(TemporalType.DATE)
    private Date fechaRegUsua;
    @Column(name = "activo_usua")
    private Boolean activoUsua;
    @Column(name = "tema_usua")
    private String temaUsua;
    @Column(name = "bloqueado_usua")
    private Boolean bloqueadoUsua;
    @Column(name = "fecha_caduc_usua")
    @Temporal(TemporalType.DATE)
    private Date fechaCaducUsua;
    @Column(name = "cambia_clave_usua")
    private Boolean cambiaClaveUsua;
    @Column(name = "ide_gtemp")
    private Integer ideGtemp;
 
    @JoinColumn(name = "ide_perf", referencedColumnName = "ide_perf")
    @ManyToOne
    private SisPerfil idePerf;
    @OneToMany(mappedBy = "ideUsua")
    private Collection<SisBloqueo> sisBloqueoCollection;


    public SisUsuario() {
    }

    public SisUsuario(Long ideUsua) {
        this.ideUsua = ideUsua;
    }

    public SisUsuario(Long ideUsua, String nomUsua, String nickUsua) {
        this.ideUsua = ideUsua;
        this.nomUsua = nomUsua;
        this.nickUsua = nickUsua;
    }

    public Long getIdeUsua() {
        return ideUsua;
    }

    public void setIdeUsua(Long ideUsua) {
        this.ideUsua = ideUsua;
    }

    public String getNomUsua() {
        return nomUsua;
    }

    public void setNomUsua(String nomUsua) {
        this.nomUsua = nomUsua;
    }

    public String getNickUsua() {
        return nickUsua;
    }

    public void setNickUsua(String nickUsua) {
        this.nickUsua = nickUsua;
    }

    public String getMailUsua() {
        return mailUsua;
    }

    public void setMailUsua(String mailUsua) {
        this.mailUsua = mailUsua;
    }

    public Date getFechaRegUsua() {
        return fechaRegUsua;
    }

    public void setFechaRegUsua(Date fechaRegUsua) {
        this.fechaRegUsua = fechaRegUsua;
    }

    public Boolean getActivoUsua() {
        return activoUsua;
    }

    public void setActivoUsua(Boolean activoUsua) {
        this.activoUsua = activoUsua;
    }

    public String getTemaUsua() {
        return temaUsua;
    }

    public void setTemaUsua(String temaUsua) {
        this.temaUsua = temaUsua;
    }

    public Boolean getBloqueadoUsua() {
        return bloqueadoUsua;
    }

    public void setBloqueadoUsua(Boolean bloqueadoUsua) {
        this.bloqueadoUsua = bloqueadoUsua;
    }

    public Date getFechaCaducUsua() {
        return fechaCaducUsua;
    }

    public void setFechaCaducUsua(Date fechaCaducUsua) {
        this.fechaCaducUsua = fechaCaducUsua;
    }

    public Boolean getCambiaClaveUsua() {
        return cambiaClaveUsua;
    }

    public void setCambiaClaveUsua(Boolean cambiaClaveUsua) {
        this.cambiaClaveUsua = cambiaClaveUsua;
    }

    public Integer getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(Integer ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

 

 
    public SisPerfil getIdePerf() {
        return idePerf;
    }

    public void setIdePerf(SisPerfil idePerf) {
        this.idePerf = idePerf;
    }


    public void setSisBloqueoCollection(Collection<SisBloqueo> sisBloqueoCollection) {
        this.sisBloqueoCollection = sisBloqueoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideUsua != null ? ideUsua.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisUsuario)) {
            return false;
        }
        SisUsuario other = (SisUsuario) object;
        if ((this.ideUsua == null && other.ideUsua != null) || (this.ideUsua != null && !this.ideUsua.equals(other.ideUsua))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisUsuario[ ideUsua=" + ideUsua + " ]";
    }
    
}

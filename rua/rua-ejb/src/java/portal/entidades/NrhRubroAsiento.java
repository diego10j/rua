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
@Table(name = "nrh_rubro_asiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhRubroAsiento.findAll", query = "SELECT n FROM NrhRubroAsiento n"),
    @NamedQuery(name = "NrhRubroAsiento.findByIdeNrrua", query = "SELECT n FROM NrhRubroAsiento n WHERE n.ideNrrua = :ideNrrua"),
    @NamedQuery(name = "NrhRubroAsiento.findByIdeGelua", query = "SELECT n FROM NrhRubroAsiento n WHERE n.ideGelua = :ideGelua"),
    @NamedQuery(name = "NrhRubroAsiento.findByIdeGeare", query = "SELECT n FROM NrhRubroAsiento n WHERE n.ideGeare = :ideGeare"),
    @NamedQuery(name = "NrhRubroAsiento.findByActivoNrrua", query = "SELECT n FROM NrhRubroAsiento n WHERE n.activoNrrua = :activoNrrua"),
    @NamedQuery(name = "NrhRubroAsiento.findByUsuarioIngre", query = "SELECT n FROM NrhRubroAsiento n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhRubroAsiento.findByFechaIngre", query = "SELECT n FROM NrhRubroAsiento n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhRubroAsiento.findByUsuarioActua", query = "SELECT n FROM NrhRubroAsiento n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhRubroAsiento.findByFechaActua", query = "SELECT n FROM NrhRubroAsiento n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhRubroAsiento.findByHoraIngre", query = "SELECT n FROM NrhRubroAsiento n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhRubroAsiento.findByHoraActua", query = "SELECT n FROM NrhRubroAsiento n WHERE n.horaActua = :horaActua"),
    @NamedQuery(name = "NrhRubroAsiento.findByTodosNrrua", query = "SELECT n FROM NrhRubroAsiento n WHERE n.todosNrrua = :todosNrrua"),
    @NamedQuery(name = "NrhRubroAsiento.findByIdeCocac", query = "SELECT n FROM NrhRubroAsiento n WHERE n.ideCocac = :ideCocac"),
    @NamedQuery(name = "NrhRubroAsiento.findByIdeCndpc", query = "SELECT n FROM NrhRubroAsiento n WHERE n.ideCndpc = :ideCndpc")})
public class NrhRubroAsiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrrua")
    private Integer ideNrrua;
    @Column(name = "ide_gelua")
    private Integer ideGelua;
    @Column(name = "ide_geare")
    private Integer ideGeare;
    @Basic(optional = false)
    @Column(name = "activo_nrrua")
    private boolean activoNrrua;
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
    @Column(name = "todos_nrrua")
    private Boolean todosNrrua;
    @Column(name = "ide_cocac")
    private Integer ideCocac;
    @Column(name = "ide_cndpc")
    private Integer ideCndpc;
    @JoinColumn(name = "ide_nrrub", referencedColumnName = "ide_nrrub")
    @ManyToOne
    private NrhRubro ideNrrub;
    @JoinColumn(name = "ide_getia", referencedColumnName = "ide_getia")
    @ManyToOne
    private GenTipoAsiento ideGetia;
    @JoinColumn(name = "ide_gecuc", referencedColumnName = "ide_gecuc")
    @ManyToOne
    private GenCuentaContable ideGecuc;
    @JoinColumn(name = "ide_geben", referencedColumnName = "ide_geben")
    @ManyToOne
    private GenBeneficiario ideGeben;

    public NrhRubroAsiento() {
    }

    public NrhRubroAsiento(Integer ideNrrua) {
        this.ideNrrua = ideNrrua;
    }

    public NrhRubroAsiento(Integer ideNrrua, boolean activoNrrua) {
        this.ideNrrua = ideNrrua;
        this.activoNrrua = activoNrrua;
    }

    public Integer getIdeNrrua() {
        return ideNrrua;
    }

    public void setIdeNrrua(Integer ideNrrua) {
        this.ideNrrua = ideNrrua;
    }

    public Integer getIdeGelua() {
        return ideGelua;
    }

    public void setIdeGelua(Integer ideGelua) {
        this.ideGelua = ideGelua;
    }

    public Integer getIdeGeare() {
        return ideGeare;
    }

    public void setIdeGeare(Integer ideGeare) {
        this.ideGeare = ideGeare;
    }

    public boolean getActivoNrrua() {
        return activoNrrua;
    }

    public void setActivoNrrua(boolean activoNrrua) {
        this.activoNrrua = activoNrrua;
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

    public Boolean getTodosNrrua() {
        return todosNrrua;
    }

    public void setTodosNrrua(Boolean todosNrrua) {
        this.todosNrrua = todosNrrua;
    }

    public Integer getIdeCocac() {
        return ideCocac;
    }

    public void setIdeCocac(Integer ideCocac) {
        this.ideCocac = ideCocac;
    }

    public Integer getIdeCndpc() {
        return ideCndpc;
    }

    public void setIdeCndpc(Integer ideCndpc) {
        this.ideCndpc = ideCndpc;
    }

    public NrhRubro getIdeNrrub() {
        return ideNrrub;
    }

    public void setIdeNrrub(NrhRubro ideNrrub) {
        this.ideNrrub = ideNrrub;
    }

    public GenTipoAsiento getIdeGetia() {
        return ideGetia;
    }

    public void setIdeGetia(GenTipoAsiento ideGetia) {
        this.ideGetia = ideGetia;
    }

    public GenCuentaContable getIdeGecuc() {
        return ideGecuc;
    }

    public void setIdeGecuc(GenCuentaContable ideGecuc) {
        this.ideGecuc = ideGecuc;
    }

    public GenBeneficiario getIdeGeben() {
        return ideGeben;
    }

    public void setIdeGeben(GenBeneficiario ideGeben) {
        this.ideGeben = ideGeben;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrrua != null ? ideNrrua.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhRubroAsiento)) {
            return false;
        }
        NrhRubroAsiento other = (NrhRubroAsiento) object;
        if ((this.ideNrrua == null && other.ideNrrua != null) || (this.ideNrrua != null && !this.ideNrrua.equals(other.ideNrrua))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhRubroAsiento[ ideNrrua=" + ideNrrua + " ]";
    }
    
}

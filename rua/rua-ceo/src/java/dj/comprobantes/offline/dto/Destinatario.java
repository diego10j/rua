/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dj.comprobantes.offline.dto;

import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author djacome
 */
public class Destinatario {

    private String identificacionDestinatario;
    private String razonSocialDestinatario;
    private String dirDestinatario;
    private String motivoTraslado;
    private String docAduaneroUnico = "000";
    private String codEstabDestino;
    private String ruta = "RUTA";
    private String codDocSustento;
    private String numDocSustento;
    private String numAutDocSustento;
    private Date fechaEmisionDocSustento;
    private String telefono;
    private String correo;

    public Destinatario(ResultSet resultado) {

        try {
            this.identificacionDestinatario = resultado.getString("identificac_geper").trim();
            this.razonSocialDestinatario = resultado.getString("nom_geper").trim();
            this.dirDestinatario = resultado.getString("direccion_geper").trim();
            this.motivoTraslado = resultado.getString("nombre_cctgi");
            //this.docAduaneroUnico = resultado.getString("");
            this.codEstabDestino = resultado.getString("estab_srcom");
            this.codDocSustento = resultado.getString("coddoc_srcom");
            this.numDocSustento = resultado.getString("estab_srcom") + "-" + resultado.getString("ptoemi_srcom") + "-" + resultado.getString("secuencial_srcom");
            this.numAutDocSustento = resultado.getString("claveacceso_srcom");
            this.fechaEmisionDocSustento = resultado.getDate("fechaemision_srcom");
            this.telefono = resultado.getString("telefono_geper");
            this.correo = resultado.getString("correo_geper");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getIdentificacionDestinatario() {
        return identificacionDestinatario;
    }

    public void setIdentificacionDestinatario(String identificacionDestinatario) {
        this.identificacionDestinatario = identificacionDestinatario;
    }

    public String getRazonSocialDestinatario() {
        return razonSocialDestinatario;
    }

    public void setRazonSocialDestinatario(String razonSocialDestinatario) {
        this.razonSocialDestinatario = razonSocialDestinatario;
    }

    public String getDirDestinatario() {
        return dirDestinatario;
    }

    public void setDirDestinatario(String dirDestinatario) {
        this.dirDestinatario = dirDestinatario;
    }

    public String getMotivoTraslado() {
        return motivoTraslado;
    }

    public void setMotivoTraslado(String motivoTraslado) {
        this.motivoTraslado = motivoTraslado;
    }

    public String getDocAduaneroUnico() {
        return docAduaneroUnico;
    }

    public void setDocAduaneroUnico(String docAduaneroUnico) {
        this.docAduaneroUnico = docAduaneroUnico;
    }

    public String getCodEstabDestino() {
        return codEstabDestino;
    }

    public void setCodEstabDestino(String codEstabDestino) {
        this.codEstabDestino = codEstabDestino;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getCodDocSustento() {
        return codDocSustento;
    }

    public void setCodDocSustento(String codDocSustento) {
        this.codDocSustento = codDocSustento;
    }

    public String getNumDocSustento() {
        return numDocSustento;
    }

    public void setNumDocSustento(String numDocSustento) {
        this.numDocSustento = numDocSustento;
    }

    public String getNumAutDocSustento() {
        return numAutDocSustento;
    }

    public void setNumAutDocSustento(String numAutDocSustento) {
        this.numAutDocSustento = numAutDocSustento;
    }

    public Date getFechaEmisionDocSustento() {
        return fechaEmisionDocSustento;
    }

    public void setFechaEmisionDocSustento(Date fechaEmisionDocSustento) {
        this.fechaEmisionDocSustento = fechaEmisionDocSustento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    
}

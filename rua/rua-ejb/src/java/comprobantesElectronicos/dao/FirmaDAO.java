/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.entidades.Firma;
import framework.aplicacion.TablaGenerica;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@Stateless
public class FirmaDAO implements FirmaDAOLocal {

    private final Utilitario utilitario = new Utilitario();

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Firma getFirmaDisponible() {

        Firma firma = null;

        TablaGenerica tab_consulta = utilitario.consultar("select * from sri_firma_digital where disponible_srfid =true");
        if (tab_consulta.isEmpty() == false) {
            firma = new Firma();
            firma.setCodigofirma(new Integer(tab_consulta.getValor("ide_srfid")));
            firma.setRutafirma(tab_consulta.getValor("ruta_srfid"));
            firma.setClavefirma(tab_consulta.getValor("password_srfid"));
            firma.setFechaingreso(utilitario.getFecha(tab_consulta.getValor("fecha_ingreso_srfid")));
            firma.setFechacaducidad(utilitario.getFecha(tab_consulta.getValor("fecha_caduca_srfid")));
            firma.setNombrerepresentante(tab_consulta.getValor("nombre_representante_srfid"));
            firma.setCorreorepresentante(tab_consulta.getValor("correo_representante_srfid"));
            firma.setDisponiblefirma(true);
            if (tab_consulta.getValor("archivo_srfid") != null) {
                firma.setArchivo(new ByteArrayInputStream((byte[]) tab_consulta.getValorObjeto("archivo_srfid")));
            }
        }

        return firma;
    }

    @Override
    public List<Firma> getTodasFirmas() {

        List<Firma> lisFirmas = new ArrayList();
        try {
            TablaGenerica tab_consulta = utilitario.consultar("select * from sri_firma_digital");
            for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {
                Firma firma = new Firma();
                firma.setCodigofirma(new Integer(tab_consulta.getValor(i, "ide_srfid")));
                firma.setRutafirma(tab_consulta.getValor(i, "ruta_srfid"));
                firma.setClavefirma(tab_consulta.getValor(i, "password_srfid"));
                firma.setFechaingreso(utilitario.getFecha(tab_consulta.getValor(i, "fecha_ingreso_srfid")));
                firma.setFechacaducidad(utilitario.getFecha(tab_consulta.getValor(i, "fecha_caduca_srfid")));
                firma.setNombrerepresentante(tab_consulta.getValor(i, "nombre_representante_srfid"));
                firma.setCorreorepresentante(tab_consulta.getValor(i, "correo_representante_srfid"));
                firma.setDisponiblefirma(Boolean.valueOf(tab_consulta.getValor(i, "disponible_srfid")));
                lisFirmas.add(firma);
            }

        } catch (Exception e) {
        }
        return lisFirmas;
    }

}

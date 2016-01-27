/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.conexion.ConexionSybaseCentral;
import comprobantesElectronicos.entidades.Tipocomprobante;
import java.sql.ResultSet;
import javax.ejb.Stateless;

/**
 *
 * @author dfjacome
 */
@Stateless
public class TipoComprobanteDAO implements TipoComprobanteDAOLocal {

    public String tablaCatalogo;

    @Override
    public String getTablaCatalogo() {
        //Si es null busca el numero de la tabla
        if (tablaCatalogo == null) {
            ConexionSybaseCentral conn = new ConexionSybaseCentral();
            try {
                ResultSet res = conn.consultar("select codigo from cobis..cl_tabla where tabla='tipo_comprobante_fe'");
                if (res.next()) {
                    tablaCatalogo = res.getString("codigo");
                }
                res.close();
            } catch (Exception e) {
                conn.desconectar();
            }
        }
        return tablaCatalogo;
    }

    @Override
    public Tipocomprobante getTipoComprobante(String valor) {
        Tipocomprobante tipoComprobante = null;
        ConexionSybaseCentral conn = new ConexionSybaseCentral();
        try {
            ResultSet res = conn.consultar("select * from cobis..cl_catalogo where tabla=" + getTablaCatalogo() + " and codigo='" + valor + "'");
            if (res.next()) {
                tipoComprobante = new Tipocomprobante(res.getString("valor"), res.getString("codigo"));
            }
            res.close();

        } catch (Exception e) {
        } finally {
            conn.desconectar();
        }
        return tipoComprobante;
    }

    @Override
    public Tipocomprobante getTipoFactura() {
        return getTipoComprobante("01");
    }

    @Override
    public Tipocomprobante getTipoNotadeCredito() {
        return getTipoComprobante("04");
    }

    @Override
    public Tipocomprobante getTipoNotadeDebito() {
        return getTipoComprobante("05");
    }

    @Override
    public Tipocomprobante getTipoGuiadeRemision() {
        return getTipoComprobante("06");
    }

    @Override
    public Tipocomprobante getTipoComprobantedeRetencion() {
        return getTipoComprobante("07");
    }

}

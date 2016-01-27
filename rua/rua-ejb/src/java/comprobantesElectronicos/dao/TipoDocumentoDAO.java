/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.conexion.ConexionSybaseCentral;
import comprobantesElectronicos.entidades.Tipodocumento;
import java.sql.ResultSet;
import javax.ejb.Stateless;

/**
 *
 * @author dfjacome
 */
@Stateless
public class TipoDocumentoDAO implements TipoDocumentoDAOLocal {

    public String tablaCatalogo;

    @Override
    public Tipodocumento getTipoDocumento(String valor) {
        ConexionSybaseCentral conn = new ConexionSybaseCentral();
        Tipodocumento tipoComprobante = null;
        try {
            ResultSet res = conn.consultar("select * from cobis..cl_catalogo where tabla=" + getTablaCatalogo() + " and codigo='" + valor + "'");
            if (res.next()) {
                tipoComprobante = new Tipodocumento(res.getString("valor"), res.getString("codigo"));
            }
            res.close();
        } catch (Exception e) {
        } finally {
            conn.desconectar();
        }
        return tipoComprobante;
    }

    @Override
    public String getTablaCatalogo() {
        //Si es null busca el numero de la tabla
        if (tablaCatalogo == null) {
            ConexionSybaseCentral conn = new ConexionSybaseCentral();
            try {
                ResultSet res = conn.consultar("select codigo from cobis..cl_tabla where tabla='tipo_cliente_fe'");
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
}

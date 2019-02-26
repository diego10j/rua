/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pensiones;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import servicios.cuentas_x_cobrar.ServicioCuentasCxC;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author djacome
 */
public class pre_genera_cxc extends Pantalla {

    private Calendario cal_ini = new Calendario();
    private Calendario cal_fin = new Calendario();

    @EJB
    private final ServicioCuentasCxC ser_factura = (ServicioCuentasCxC) utilitario.instanciarEJB(ServicioCuentasCxC.class);

    public pre_genera_cxc() {
        Grid grid = new Grid();
        grid.setColumns(2);
        grid.getChildren().add(new Etiqueta("FECHA INICIO :"));
        grid.getChildren().add(cal_ini);
        grid.getChildren().add(new Etiqueta("FECHA FIN :"));
        grid.getChildren().add(cal_fin);

        Boton bot = new Boton();
        bot.setValue("Aceptar");
        bot.setMetodo("generar");
        grid.setFooter(bot);

        agregarComponente(grid);
    }

    public void generar() {
        TablaGenerica tab = utilitario.consultar("SELECT ide_cccfa,fecha_emisi_cccfa from cxc_cabece_factura  where fecha_emisi_cccfa BETWEEN  '" + cal_ini.getFecha() + "' and '" + cal_fin.getFecha() + "' " + " "
                + " AND a.ide_ccefa =" + utilitario.getVariable("p_cxc_estado_factura_normal"));
        Tabla tabla = new Tabla();
        tabla.setTabla("cxc_cabece_factura", "ide_cccfa", 9990);
        tabla.setCondicion("ide_cccfa = -1");
        tabla.ejecutarSql();
        if (tab.isEmpty() == false) {
            for (int i = 0; i < tab.getTotalFilas(); i++) {
                tabla.setCondicion("ide_cccfa = " + tab.getValor(i, "ide_cccfa"));
                tabla.ejecutarSql();
                ser_factura.generarModificarTransaccionFactura(tabla);
            }
        }
    }

    @Override
    public void insertar() {

    }

    @Override
    public void guardar() {

    }

    @Override
    public void eliminar() {
    }

}

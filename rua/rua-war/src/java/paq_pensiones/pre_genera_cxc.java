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
import framework.componentes.Texto;
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
    private Texto txt_titulo = new Texto();

    @EJB
    private final ServicioCuentasCxC ser_factura = (ServicioCuentasCxC) utilitario.instanciarEJB(ServicioCuentasCxC.class);

    public pre_genera_cxc() {
        Grid grid = new Grid();
        grid.setColumns(2);
        grid.getChildren().add(new Etiqueta("FECHA INICIO :"));
        grid.getChildren().add(cal_ini);
        grid.getChildren().add(new Etiqueta("FECHA FIN :"));
        grid.getChildren().add(cal_fin);
        txt_titulo.setId("txt_titulo");
grid.getChildren().add(txt_titulo);        

        Boton bot = new Boton();
        bot.setValue("AceptarVArios");
        bot.setMetodo("generar");
        grid.setFooter(bot);

        
                Boton bot2 = new Boton();
        bot2.setValue("Aceptar por Fcatura");
        bot2.setMetodo("generarTitulo");
        grid.setFooter(bot2);
        
        
        agregarComponente(grid);
    }
    public void generarTitulo(){
    TablaGenerica tab = utilitario.consultar("SELECT ide_cccfa,fecha_emisi_cccfa from cxc_cabece_factura  where ide_cccfa in("+txt_titulo.getValue()+") "
         
        + " AND ide_ccefa =" + utilitario.getVariable("p_cxc_estado_factura_normal"));
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
            guardarPantalla();
        }
}
    public void generar() {
        //TablaGenerica tab = utilitario.consultar("SELECT ide_cccfa,fecha_emisi_cccfa from cxc_cabece_factura  where fecha_emisi_cccfa BETWEEN  '" + cal_ini.getFecha() + "' and '" + cal_fin.getFecha() + "' " + " "
        TablaGenerica tab = utilitario.consultar("SELECT ide_cccfa,fecha_emisi_cccfa from cxc_cabece_factura  where ide_cccfa in ( select " +
"ide_cccfa\n" +
"from (\n" +
"select ide_titulo_recval,a.ide_gemes,nombre_gemes ,gen_ide_geper,ide_concepto_recon\n" +
"from rec_valores  a,gen_mes b\n" +
"where a.ide_gemes= b.ide_gemes\n" +
"and a.ide_recest= 2\n" +
"and a.ide_gemes in (\n" +
"7,8,9,10,11,12\n" +
")\n" +
") a\n" +
"left join (\n" +
"select a.ide_cccfa,ide_geper,replace(observacion_ccdfa,'PENSION ','') as mes,observacion_ccdfa\n" +
"from cxc_deta_factura a,cxc_cabece_factura b\n" +
"where a.ide_cccfa  = b.ide_cccfa\n" +
") b on a.nombre_gemes =b.mes\n" +
"and gen_ide_geper= ide_geper\n" +
"where not ide_cccfa is null\n" +
"order by ide_geper  ) "
  
        + " AND ide_ccefa =" + utilitario.getVariable("p_cxc_estado_factura_normal"));
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
            guardarPantalla();
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

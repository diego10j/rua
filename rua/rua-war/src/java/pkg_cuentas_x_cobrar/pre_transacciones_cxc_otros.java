/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_cuentas_x_cobrar;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_transacciones_cxc_otros extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();

    private AutoCompletar aut_filtro_persona = new AutoCompletar();

    public pre_transacciones_cxc_otros() {

        aut_filtro_persona.setId("aut_filtro_persona");
        aut_filtro_persona.setAutoCompletar("select ide_geper,identificac_geper,nom_geper from gen_persona where nivel_geper='HIJO' "
                + "AND es_cliente_geper IS TRUE AND ide_empr=" + utilitario.getVariable("ide_empr"));
        aut_filtro_persona.setMetodoChange("filtrar_proveedor");
        aut_filtro_persona.setAutocompletarContenido();
        aut_filtro_persona.setGlobal(true);
        aut_filtro_persona.setSize(75);
        bar_botones.agregarComponente(new Etiqueta("Cliente: "));
        bar_botones.agregarComponente(aut_filtro_persona);

        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarComponente(bot_limpiar);

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("cxc_cabece_transa", "ide_ccctr", 1);
        tab_tabla1.getColumna("ide_ccttr").setCombo("cxc_tipo_transacc", "ide_ccttr", "nombre_ccttr", "");
        tab_tabla1.getColumna("fecha_trans_ccctr").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("ide_geper").setVisible(true);
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.setCondicionSucursal(true);
        //tab_tabla1.setRecuperarLectura(true);
        tab_tabla1.setRows(10);
        tab_tabla1.setCondicion("ide_geper=-1");
        tab_tabla1.setCampoOrden("fecha_trans_ccctr desc");
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("cxc_detall_transa", "ide_ccdtr", 2);
        tab_tabla2.getColumna("ide_cccfa").setVisible(false);
        tab_tabla2.getColumna("ide_ccttr").setCombo("cxc_tipo_transacc", "ide_ccttr", "nombre_ccttr", "");
        tab_tabla2.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "");
        tab_tabla2.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_tabla2.getColumna("fecha_trans_ccdtr").setValorDefecto(utilitario.getFechaActual());
        tab_tabla2.getColumna("ide_usua").setVisible(false);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, pat_panel2, "50%", "H");

        agregarComponente(div_division);

        //      llenar();
    }

    public void limpiar() {
        tab_tabla1.setCondicion("ide_geper=-1");
        tab_tabla1.ejecutarSql();
        tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
        aut_filtro_persona.setValue(null);
        utilitario.addUpdate("aut_filtro_persona");
    }

    public void filtrar_proveedor(SelectEvent evt) {
        System.out.println("si entra el metodo");
        aut_filtro_persona.onSelect(evt);
        if (aut_filtro_persona.getValue() != null) {
            System.out.println("si entra el metodo " + aut_filtro_persona.getValor());
            tab_tabla1.setCondicion("ide_geper=" + aut_filtro_persona.getValor());
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());

        } else {
            utilitario.agregarMensajeInfo("No existe el Proveedor", "Ingrese otro proveedor");
        }

    }

    public void llenar() {
        TablaGenerica tab_valor = utilitario.consultar("select sum(dcc.valor_cndcc*sc.signo_cnscu) as valor,dpc.ide_cndpc,dpc.nombre_cndpc "
                + "from  con_cab_comp_cont ccc "
                + "inner join  con_det_comp_cont dcc on ccc.ide_cnccc=dcc.ide_cnccc "
                + "inner join con_det_plan_cuen dpc on  dpc.ide_cndpc = dcc.ide_cndpc "
                + "inner join con_tipo_cuenta tc on dpc.ide_cntcu=tc.ide_cntcu "
                + "inner  join con_signo_cuenta sc on tc.ide_cntcu=sc.ide_cntcu and dcc.ide_cnlap=sc.ide_cnlap "
                + "WHERE (ccc.fecha_trans_cnccc BETWEEN '2011-01-01' and '2012-12-31') "
                + "and ccc.ide_cneco in (0,2,3) and ccc.ide_sucu=0 and dpc.ide_cntcu in (0,3,4) and "
                + "dcc.ide_cndpc in (SELECT ide_cndpc from con_det_plan_cuen where con_ide_cndpc in "
                + "(select ide_cndpc from con_det_plan_cuen where con_ide_cndpc=136)) "
                + "and dpc.ide_cndpc>201 "
                + "and dpc.ide_cndpc!= 143 "
                + "GROUP BY dpc.ide_cndpc,codig_recur_cndpc, nombre_cndpc,con_ide_cndpc "
                + "HAVING (sum(dcc.valor_cndcc*sc.signo_cnscu) <>0) ORDER BY codig_recur_cndpc");
        int j = 0;
        for (int i = 0; i < tab_tabla1.getTotalFilas(); i++) {
            System.out.println("ide cabecera " + tab_tabla1.getValor(i, "ide_ccctr"));
            if (Double.parseDouble(tab_tabla1.getValor(i, "ide_ccctr")) > 17) {
                tab_tabla2.insertar();
                System.out.println("valor " + tab_valor.getValor(j, "valor"));
                tab_tabla2.setValor("ide_ccctr", tab_tabla1.getValor(i, "ide_ccctr"));
                tab_tabla2.setValor("valor_ccdtr", tab_valor.getValor(j, "valor"));
                j = j + 1;
            }

        }
        tab_tabla2.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void insertar() {
        if (aut_filtro_persona.getValor() != null) {
            if (tab_tabla1.isFocus()) {
                tab_tabla1.insertar();
                tab_tabla1.setValor("ide_geper", aut_filtro_persona.getValor());
            }
            if (tab_tabla2.isFocus()) {
                tab_tabla2.insertar();
                tab_tabla2.setValor("ide_cccfa", tab_tabla1.getValor("ide_cccfa"));
            }
        } else {
            utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Proveedor en el autocompletar");
        }
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        tab_tabla2.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

    public AutoCompletar getAut_filtro_persona() {
        return aut_filtro_persona;
    }

    public void setAut_filtro_persona(AutoCompletar aut_filtro_persona) {
        this.aut_filtro_persona = aut_filtro_persona;
    }

}

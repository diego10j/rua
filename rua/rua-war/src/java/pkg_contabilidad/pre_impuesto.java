package pkg_contabilidad;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_impuesto extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Division div_division = new Division();

    public pre_impuesto() {

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("con_impuesto", "ide_cnimp", 1);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);

        agregarComponente(div_division);
//        subirplan();
        //ponerhijos();
    }

    public String getnivelpadre(int nivel) {
        int padre_nivel_2 = 0;
        int padre_nivel_3 =832;
        int padre_nivel_4 = 833;
        int padre_nivel_5 = 834;
        if (nivel == 2) {
            return padre_nivel_2 + "";
        } else if (nivel == 3) {
            return padre_nivel_3 + "";
        } else if (nivel == 4) {
            return padre_nivel_4 + "";
        } else if (nivel == 5) {
            return padre_nivel_5 + "";
        } else {
            return null;
        }

    }

    public void ponerhijos() {
        TablaGenerica tab_cuentas = utilitario.consultar("select * from con_det_plan_cuen where ide_cntcu=1 "
                + "order by codig_recur_cndpc");
        String ide_padre_anterior = null;
        for (int i = 4; i < tab_cuentas.getTotalFilas(); i++) {

            int ide_nivel_anterior = Integer.parseInt(tab_cuentas.getValor(i - 1, "ide_cnncu"));
            int ide_nivel_actual = Integer.parseInt(tab_cuentas.getValor(i, "ide_cnncu"));
            String ide_cndpc_anterior = tab_cuentas.getValor(i - 1, "ide_cndpc");
            if (Integer.parseInt(tab_cuentas.getValor(i, "ide_cnncu")) > ide_nivel_anterior) {
                utilitario.getConexion().agregarSqlPantalla("update con_det_plan_cuen set con_ide_cndpc=" + ide_cndpc_anterior + " where ide_cndpc=" + tab_cuentas.getValor(i, "ide_cndpc"));
                ide_padre_anterior = ide_cndpc_anterior;
            } else {
                if (Integer.parseInt(tab_cuentas.getValor(i, "ide_cnncu")) < ide_nivel_anterior) {
                    utilitario.getConexion().agregarSqlPantalla("update con_det_plan_cuen set con_ide_cndpc=" + getnivelpadre(ide_nivel_actual) + " where ide_cndpc=" + tab_cuentas.getValor(i, "ide_cndpc"));
                    ide_padre_anterior = getnivelpadre(ide_nivel_actual);
                } else {
                    if (Integer.parseInt(tab_cuentas.getValor(i, "ide_cnncu")) == ide_nivel_anterior) {
                        utilitario.getConexion().agregarSqlPantalla("update con_det_plan_cuen set con_ide_cndpc=" + ide_padre_anterior + " where ide_cndpc=" + tab_cuentas.getValor(i, "ide_cndpc"));
                    }
                }
            }
        }
        utilitario.getConexion().guardarPantalla();

    }

    public void subirplan() {
        TablaGenerica tab_plan_cuentas_a_subir = utilitario.consultar("select *from plan_de_cuentas");
        Tabla tab_det_plan_cuenta = new Tabla();
        tab_det_plan_cuenta.setTabla("con_det_plan_cuen", "ide_cndpc", -1);
        tab_det_plan_cuenta.setCondicion("ide_cndpc=-1");
        tab_det_plan_cuenta.ejecutarSql();
        for (int i = 0; i < tab_plan_cuentas_a_subir.getTotalFilas(); i++) {
            tab_det_plan_cuenta.insertar();
            tab_det_plan_cuenta.setValor("ide_empr", utilitario.getVariable("ide_empr"));
            tab_det_plan_cuenta.setValor("ide_sucu", utilitario.getVariable("ide_empr"));
            //tipo cuenta
            if (tab_plan_cuentas_a_subir.getValor(i, "codigo").startsWith("1")) {
                tab_det_plan_cuenta.setValor("ide_cntcu", "0");
            }
            if (tab_plan_cuentas_a_subir.getValor(i, "codigo").startsWith("2")) {
                tab_det_plan_cuenta.setValor("ide_cntcu", "4");
            }
            if (tab_plan_cuentas_a_subir.getValor(i, "codigo").startsWith("3")) {
                tab_det_plan_cuenta.setValor("ide_cntcu", "5");
            }
            if (tab_plan_cuentas_a_subir.getValor(i, "codigo").startsWith("4")) {
                tab_det_plan_cuenta.setValor("ide_cntcu", "3");
            }
            if (tab_plan_cuentas_a_subir.getValor(i, "codigo").startsWith("5")) {
                tab_det_plan_cuenta.setValor("ide_cntcu", "2");
            }
            if (tab_plan_cuentas_a_subir.getValor(i, "codigo").startsWith("6")) {
                tab_det_plan_cuenta.setValor("ide_cntcu", "1");
            }
            if (tab_plan_cuentas_a_subir.getValor(i, "codigo").startsWith("9")) {
                tab_det_plan_cuenta.setValor("ide_cntcu", "6");
            }
            tab_det_plan_cuenta.setValor("ide_cncpc", "0");
            tab_det_plan_cuenta.setValor("ide_cnncu", tab_plan_cuentas_a_subir.getValor(i, "tipo"));
            tab_det_plan_cuenta.setValor("codig_recur_cndpc", tab_plan_cuentas_a_subir.getValor(i, "codigo"));
            if (tab_plan_cuentas_a_subir.getValor(i, "nombre") != null && !tab_plan_cuentas_a_subir.getValor(i, "nombre").isEmpty()) {
                tab_det_plan_cuenta.setValor("nombre_cndpc", tab_plan_cuentas_a_subir.getValor(i, "nombre"));
            } else {
                tab_det_plan_cuenta.setValor("nombre_cndpc", "sin nombre");

            }
            if (Integer.parseInt(tab_plan_cuentas_a_subir.getValor(i, "tipo")) < 6) {
                tab_det_plan_cuenta.setValor("nivel_cndpc", "PADRE");
            } else {
                tab_det_plan_cuenta.setValor("nivel_cndpc", "HIJO");
            }
        }
        tab_det_plan_cuenta.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
}

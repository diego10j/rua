/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_activos;

/**
 *
 * @author ANDRES
 */

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import servicios.activos.ServicioActivosFijos;
import sistema.aplicacion.Pantalla;

public class pre_movimiento_activo extends Pantalla{
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
 @EJB
    private final ServicioActivosFijos ser_activos = (ServicioActivosFijos) utilitario.instanciarEJB(ServicioActivosFijos.class);    
    public pre_movimiento_activo (){
        tab_tabla1.setId("tab_tabla1");   //identificador
        tab_tabla1.setSql("select a.ide_acact,fecha_acafi,valor_compra_acafi,valor_depreciado_acafi,secuencial_acact as codigo_acact,fecha_asigna_acact,codigo_gecas,nombre_gecas,\n" +
"codigo_geobr,nombre_geobr, b.codigo_acuba,b.nombre_acuba,g.nom_geper, b.nombre_acuba as dep_activo,\n" +
"serie_acafi,codigo_barras_acafi,cantidad_acafi,nombre_inarti,\n" +
"observacion_acafi,nombre_aceaf,cod_anterior_acafi,observacion_acact,k.nom_geper as nuevo_custodio,\n" +
"(case when extract(month from fecha_asigna_acact) =1 then 'Enero'\n" +
"when extract(month from fecha_asigna_acact) =2 then 'Febrero'\n" +
"when extract(month from fecha_asigna_acact) =3 then 'Marzo'\n" +
"when extract(month from fecha_asigna_acact) =4 then 'Abril'\n" +
"when extract(month from fecha_asigna_acact) =5 then 'Mayo'\n" +
"when extract(month from fecha_asigna_acact) =6 then 'Junio'\n" +
"when extract(month from fecha_asigna_acact) =7 then 'Julio'\n" +
"when extract(month from fecha_asigna_acact) =8 then 'Agosto'\n" +
"when extract(month from fecha_asigna_acact) =9 then 'Septiembre'\n" +
"when extract(month from fecha_asigna_acact) =10 then 'Octubre'\n" +
"when extract(month from fecha_asigna_acact) =11 then 'Noviembre'\n" +
"when extract(month from fecha_asigna_acact) =12 then 'Diciembre' end) as mes,\n" +
"cast((extract(day from fecha_asigna_acact)) as integer) as dia,\n" +
"cast((extract (year from fecha_asigna_acact)) as integer) as anio\n" +
",g.denominacion_geper as denom_custodio,\n" +
"k.denominacion_geper as denom_nuevo_custodio,(case when bloqueado_acact=false then 'NO APROBADO' when anulado_acact=true then 'ANULADO' else '' end) as estado\n" +
"from act_acta_constata a\n" +
"left join act_movimiento j on a.ide_acact = j.ide_acact\n" +
"left join act_activo_fijo d  on d.ide_acafi=j.ide_acafi\n" +
"left join act_ubicacion_activo b on d.ide_acuba=b.ide_acuba\n" +
"left join gen_casa e on a.ide_gecas=e.ide_gecas\n" +
"left join gen_OBRA f on a.ide_geobr=f.ide_geobr\n" +
"left join gen_persona g on a.ide_geper=g.ide_geper\n" +
"left join inv_articulo h  on d.ide_inarti=h.ide_inarti\n" +
"left join act_estado_activo_fijo i on j.ide_aceaf=i.ide_aceaf\n" +
"left join gen_persona k on a.gen_ide_geper= k.ide_geper\n" +
"left join act_ubicacion_activo l on d.act_ide_acuba = l.ide_acuba\n" +
"--where a.ide_acact= $P{ide_acact}\n" +
"order by nombre_inarti,a.ide_acact");
        tab_tabla1.getColumna("codigo_barras_acafi").setFiltro(true);
        
        tab_tabla1.dibujar();
        PanelTabla pat_tabla1 = new PanelTabla();
        pat_tabla1.setId("pat_tabla1");
        pat_tabla1.setPanelTabla(tab_tabla1);
        
       
       
        
        Division div_tabla1 = new Division();
        div_tabla1.setId("div_tabla1");
        div_tabla1.dividir1(pat_tabla1);
        agregarComponente(div_tabla1);
        
    }
    @Override
    public void insertar() {
        if (tab_tabla1.isFocus()){
        tab_tabla1.insertar();
        }
        else if (tab_tabla2.isFocus()){
        tab_tabla2.insertar();
        }
       
    }

    @Override
    public void guardar() {
        if (tab_tabla1.guardar()){
        if (tab_tabla2.guardar()){
            guardarPantalla();
        }
        }
        
        
    }

    @Override
    public void eliminar() {
       if (tab_tabla1.isFocus()){
        tab_tabla1.eliminar();
        }
       if (tab_tabla2.isFocus()){
        tab_tabla2.eliminar();
        }
    
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
    
}

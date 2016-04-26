package paq_bodega.ejb;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import framework.aplicacion.TablaGenerica;

import paq_contabilidad.ejb.ServicioContabilidad;
import sistema.aplicacion.Utilitario;

/**
 * Session Bean implementation class ServicioBodega
 */

@Stateless
@LocalBean

public class ServicioBodega {

	/**
	 * Default constructor. 
	 */
	private Utilitario utilitario=new Utilitario();
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public TablaGenerica getTablaInventario (String codigo){

		TablaGenerica tab_inventario = utilitario.consultar("select ide_bomat, codigo_bomat,detalle_bomat " +
				" from bodt_material " +
				" WHERE ide_bomat in ("+codigo+") " +
				" ORDER BY detalle_bomat");
		return tab_inventario;

	}
	public String getMaterialBodega (String ide_bomat){

		String tab_inventario ="select ide_bomat, codigo_bomat,detalle_bomat " +
				" from bodt_material " +
				" WHERE ide_bomat in ("+ide_bomat+") " +
				" ORDER BY detalle_bomat";
		//System.out.println("llega como parametro "+ide);
		return tab_inventario;

	}
	/**
	 * Este servicio retorna los materiales existentes en el c�logo de materiales 
	 * @param activo
	 * @return
	 */
	public String getInventario (String grupo,String activo,String ide_botip){

		String tab_inventario="select ide_bomat, codigo_bomat,detalle_bomat,detalle_bogrm " +
				"from bodt_material a,bodt_grupo_material b" +
				" WHERE activo_bomat in ("+activo+") AND a.ide_bogrm = b.ide_bogrm ";
		if(grupo.equals("0")){
		       tab_inventario +=" AND a.ide_bogrm = b.ide_bogrm AND a.ide_botip in("+ide_botip+") "	;
		}
		
		        tab_inventario +="ORDER BY detalle_bogrm,detalle_bomat";
		       // System.out.println("material servicio "+tab_inventario);
			return tab_inventario;


	}
	/**
	 * Este servicio retorna los datos del inventario por material y Año 
	 * @param material = Recibe el ide del material a consultar
	 * @param anio = Recibe Año del inventario del material a conultar.
	 * @return String 
	 */
	public String getDatosInventario (String material,String anio){

		String tab_inventario="select ide_boinv,ide_geani,ingreso_material_boinv,egreso_material_boinv,existencia_inicial_boinv,costo_anterior_boinv,costo_actual_boinv,"
+" fecha_ingr_articulo_boinv,costo_inicial_boinv,ide_bomat,(case when existencia_inicial_boinv is null then 0 else existencia_inicial_boinv end) +  (case when ingreso_material_boinv is null then 0 else  ingreso_material_boinv end) - (case when egreso_material_boinv is null then 0 else egreso_material_boinv end) as existencia_actual from bodt_inventario where ide_geani ="+anio+" and ide_bomat ="+material;
	    return tab_inventario;

	}
	/**
	 * Este servicio retorna los datos del inventario por  Año 
	 * @param anio = Recibe Año del inventario del material a conultar.
	 * @return String 
	 */
	public String getDatosInventarioAnio (String anio){

		String tab_inventario="select ide_boinv,ide_geani,ingreso_material_boinv,egreso_material_boinv,existencia_inicial_boinv,costo_anterior_boinv,costo_actual_boinv,"
+" fecha_ingr_articulo_boinv,costo_inicial_boinv,a.ide_bomat,(case when existencia_inicial_boinv is null then 0 else existencia_inicial_boinv end) +  (case when ingreso_material_boinv is null then 0 else  ingreso_material_boinv end) - (case when egreso_material_boinv is null then 0 else egreso_material_boinv end) as existencia_actual,codigo_bomat,detalle_bomat from bodt_inventario a, bodt_material b where a.ide_bomat = b.ide_bomat and ide_geani ="+anio;
	    return tab_inventario;

	}
	/**
	 * Este servicio retorna los datos del inventario por clave principal de la tabla bodt_inventario 
	 * @param ide_boinv = Recibe el ide del material a consultar
	 * @return String 
	 */
	public String getDatosInventarioPrincipal (String ide_boinv){

		String tab_inventario="select ide_boinv,ide_geani,ingreso_material_boinv,egreso_material_boinv,existencia_inicial_boinv,costo_anterior_boinv,costo_actual_boinv,"
+" fecha_ingr_articulo_boinv,costo_inicial_boinv,ide_bomat,(case when existencia_inicial_boinv is null then 0 else existencia_inicial_boinv end) +  (case when ingreso_material_boinv is null then 0 else  ingreso_material_boinv end) - (case when egreso_material_boinv is null then 0 else egreso_material_boinv end) as existencia_actual from bodt_inventario where ide_boinv in ("+ide_boinv+"); ";
	    return tab_inventario;

	}
	public TablaGenerica getTablaProveedor (String ide_tepro){
		TablaGenerica tab_proveedor=utilitario.consultar("select ide_tepro,nombre_tepro,ruc_tepro " +
				" from tes_proveedor where ide_tepro in ("+ide_tepro+")" +
				" order by nombre_tepro");
		return tab_proveedor;
	}
	
	public String getProveedor (String activo){
		String tab_proveedor="select ide_tepro,nombre_tepro,ruc_tepro " +
				" from tes_proveedor where activo_tepro in ("+activo+")" +
				" order by nombre_tepro";
		return tab_proveedor;
		
	}
	/**
	 * Metodo que permite obtener el inventario con su descripcion de material  
	 * @return String SQL, codigo del inventario, nombre del material, codigo del material.
	 */	
	public String getInventarioMaterial (){
		String tab_proveedor="select ide_boinv,codigo_bomat,detalle_bomat from bodt_inventario a, bodt_material b"
+" where a.ide_bomat = b.ide_bomat order by codigo_bomat";
		return tab_proveedor;
		
	}

	/**
	 * Metodo que permite crear matriz para generaciuon de Kardex de Bodega 
	 * @return Boolean, true si se creo la matriz correctamente, false si no se creo
	 */	
	public Boolean matrizKardexInventarios(String codigo_materiales){
		System.out.println("kardex "+codigo_materiales);
	    Boolean resultado =false;
		double val_ingreso=0;
		double val_egreso=0;
		TablaGenerica inventario=utilitario.consultar(getDatosInventarioPrincipal(codigo_materiales));
		String sql_insert="";
		utilitario.getConexion().ejecutarSql("delete from rep_bodt_kardex; ");
		for(int i=0;i<inventario.getTotalFilas();i++){
			TablaGenerica total_ingresos=utilitario.consultar(numeroMatrizKardex("bodt_bodega", inventario.getValor(i,"ide_boinv")));
			TablaGenerica total_egresos=utilitario.consultar(numeroMatrizKardex("bodt_egreso", inventario.getValor(i,"ide_boinv")));
			val_ingreso= Double.parseDouble(total_ingresos.getValor("contador"));
			val_egreso= Double.parseDouble(total_egresos.getValor("contador"));
			
			if(val_ingreso<val_egreso){
				TablaGenerica inventario_egreso = utilitario.consultar(datosMatrizKardex("bodt_egreso", inventario.getValor(i,"ide_boinv")));
			    TablaGenerica inventario_ingreso= utilitario.consultar(datosMatrizKardex("bodt_bodega", inventario.getValor(i,"ide_boinv")));
				for(int j=0;j<inventario_egreso.getTotalFilas();j++){
			    	sql_insert ="insert into rep_bodt_kardex (codigo_rebok,ide_boinv,codigo_ingreso_rebok,codigo_egreso_rebok) values ("+j+","+inventario.getValor(i,"ide_boinv")+",null,"+inventario_egreso.getValor(j,"ide_boegr")+");";
			    	utilitario.getConexion().ejecutarSql(sql_insert);
	       
				}
				for(int k=0;k<inventario_ingreso.getTotalFilas();k++){
			    	String sql_update="update rep_bodt_kardex set codigo_ingreso_rebok="+inventario_ingreso.getValor(k,"ide_bobod")+" where ide_boinv="+inventario.getValor(i,"ide_boinv")+" and codigo_rebok="+k;
			    	utilitario.getConexion().ejecutarSql(sql_update);

				}
			}
			else{
				TablaGenerica inventario_ingreso = utilitario.consultar(datosMatrizKardex("bodt_bodega", inventario.getValor(i,"ide_boinv")));
				TablaGenerica inventario_egreso = utilitario.consultar(datosMatrizKardex("bodt_egreso", inventario.getValor(i,"ide_boinv")));

				for(int j=0;j<inventario_ingreso.getTotalFilas();j++){
			    	sql_insert ="insert into rep_bodt_kardex (codigo_rebok,ide_boinv,codigo_ingreso_rebok,codigo_egreso_rebok) values ("+j+","+inventario.getValor(i,"ide_boinv")+","+inventario_ingreso.getValor(j,"ide_bobod")+",null);";
			    	utilitario.getConexion().ejecutarSql(sql_insert);

				}
				for(int k=0;k<inventario_egreso.getTotalFilas();k++){
			    	String sql_update="update rep_bodt_kardex set codigo_egreso_rebok="+inventario_ingreso.getValor(k,"ide_boegr")+" where ide_boinv="+inventario.getValor(i,"ide_boinv")+" and codigo_rebok="+k;
			    	utilitario.getConexion().ejecutarSql(sql_update);
	       
				}
				
			}

		}
		return resultado;
	}
	public String datosMatrizKardex(String tabla, String codigo){
		String sql="Select * from "+tabla+" where ide_boinv="+codigo;
		System.out.println("DATOS MATRIZ "+sql);
		return sql;
	}
	public String numeroMatrizKardex(String tabla,String codigo){
		String sql ="select a.ide_boinv,(case when contador is null then 0 else contador end) as contador"
+" from bodt_inventario a"
+" left join ("
+" select count(ide_boinv) as contador,ide_boinv"
+" from "+tabla+" where not ide_boinv is null"
+" group by ide_boinv"
+" ) b on a.ide_boinv = b.ide_boinv"
+" where a.ide_boinv="+codigo;
		System.out.println("nunero MATRIZ "+sql);

		return sql;
	}
public double getResultadoStock(String ide_bomat,String ide_geani){
	double  stock;
	TablaGenerica sql=utilitario.consultar("select 1 as codigo, inicial-egreso as stock from (select ingreso_material_boinv,egreso_material_boinv," +
			" (CASE WHEN ingreso_material_boinv is null THEN 0 ELSE ingreso_material_boinv end) as inicial," +
			" (CASE WHEN egreso_material_boinv is null THEN 0 ELSE egreso_material_boinv end)as egreso" +
			" from bodt_inventario" +
			" where ide_bomat in ("+ide_bomat+") and ide_geani in ("+ide_geani+")) a");
	
	stock=Double.parseDouble(sql.getValor("stock").toString());
	return stock;
   
}
/**
 * Metodo que permite registrar de forma automatica la cantidad de material ingresados al stock de inventarios  
 * @param material recibe el codigo del material de bodega
 * @param anio recibe el anio fiscal en el que esea ingresra al inventario el material. 
 * @param cantidad_ingreso recibe  la cantidad de material  a ser ingresado a los inventarios. 
 * @param valor_total recibe el valor total del material  a ser ingresado a los inventarios. 
 * @return boolean True = si se registro con exito en el inventario False= si existio algun error al actualizar inventarios.
 */
public boolean registraInventarioIngresos(String material,String anio,String cantidad_ingreso,String valor_total){
	boolean confirma=false;
	double stock=0;
	TablaGenerica datos_inventario=utilitario.consultar(getDatosInventario(material, anio));
	double costo_actual=Double.parseDouble(datos_inventario.getValor("costo_actual_boinv"));
    stock=getResultadoStock(material, anio);
	// Permite conocer el valor actual de mi stock
    double valor_stock_inventario =costo_actual*stock;
    //Cantidad actual ingresada a inventarios desde el formulario
    double cantidad_actual=Double.parseDouble(cantidad_ingreso);
    //Valor total actual ingresado para inventarios desde el formulario
    double valor_actual=Double.parseDouble(valor_total);
    // Permite suma stock existen mas el numero actual de ingreso de materiales;
    double total_nueva_existencia=stock+cantidad_actual;
    // Permite Sumar Valores para depues determinar mi costo vigente por producto
    double valor_nueva_existencia=valor_stock_inventario+valor_actual;
    // Determino el valor indivual nuevo del producto
    double valor_nuevo_individual=valor_nueva_existencia/total_nueva_existencia;

    String sql_actualiza_inventarios="update bodt_inventario set ingreso_material_boinv=ingreso_material_boinv+"+cantidad_actual
    		+",costo_anterior_boinv=costo_actual_boinv, costo_actual_boinv="+valor_nuevo_individual+" where ide_bomat="+material+" and ide_geani="+anio+";";

    String resultado_upadte="";
    resultado_upadte=utilitario.getConexion().ejecutarSql(sql_actualiza_inventarios);
    if (resultado_upadte.isEmpty()) {
    	confirma=true;
     }
	return confirma;
}
/**
 * Metodo que permite registrar de forma automatica la cantidad de material egresado  
 * @param material recibe el codigo del material de bodega
 * @param cantidad_egreso recibe  la cantidad de material  a ser egresado a los inventarios. 
 * @return boolean True = si se registro con exito en el inventario False= si existio algun error al actualizar inventarios.
 */
public boolean registraInventarioEgresos(String material,String cantidad_egreso){
	boolean confirma=false;	
	//Sql actualiza los egresos en inventarios
    String sql_actualiza_inventarios="update bodt_inventario set egreso_material_boinv = egreso_material_boinv + "+cantidad_egreso+" where ide_boinv ="+material+";";

    String resultado_upadte="";
    resultado_upadte=utilitario.getConexion().ejecutarSql(sql_actualiza_inventarios);
    if (resultado_upadte.isEmpty()) {
    	confirma=true;
     }
	return confirma;
}
public String getSolicitud(String activo){
	String tab_solicitud="select ide_adsoc,detalle_adsoc,nro_solicitud_adsoc,valor_adsoc,nombre_tepro,ruc_tepro " +
			" from adq_solicitud_compra a , tes_proveedor b" +
			" where a.ide_tepro=b.ide_tepro and activo_adsoc in("+activo+") order by nro_solicitud_adsoc";
	return tab_solicitud;
}
public String getSolicitudFactura(String activo){
	String tab_solicitud="select c.ide_adfac,(case when num_factura_adfac is null then 'S/D' else num_factura_adfac end) as num_factura_adfac,fecha_factura_adfac,nro_solicitud_adsoc,valor_adsoc,ruc_tepro,nombre_tepro,detalle_adsoc" 
+" from adq_solicitud_compra a , tes_proveedor b, adq_factura c"
+" where a.ide_tepro=b.ide_tepro and a.ide_adsoc = c.ide_adsoc and activo_adfac in("+activo+") order by nro_solicitud_adsoc";
	return tab_solicitud;
}
public String getEgresoBodegaActivos(String ide_geani){
	String tab_solicitud="select ide_bocoe,numero_egreso_bocoe,fecha_egreso_bocoe,uso_bocoe"
+" from bodt_concepto_egreso where activo_bocoe = true and ide_bocoe  in ("
+" select ide_bocoe from bodt_egreso where ide_bobod in ("
+" select ide_bobod from bodt_bodega where tipo_ingreso_bobod=2 and ide_geani = "+ide_geani
+" ) group by ide_bocoe ) order by numero_egreso_bocoe desc";
	return tab_solicitud;
}
public String getMaterialesPorEgreso(String ide_bocoe,String estado){
	String tab_solicitud="select c.ide_boegr,ide_bocoe,documento_egreso_boegr,codigo_bomat,detalle_bomat,fecha_egreso_boegr,"
+" cantidad_egreso_boegr,fecha_compra_bobod,num_factura_bobod,descripcion_bobod,marca_bobod,"
+" modelo_bobod,serie_bobod,color_bobod, b.ide_tepro,nombre_tepro"
+" from bodt_material a,bodt_bodega b,bodt_egreso c,tes_proveedor d"
+" where a.ide_bomat = b.ide_bomat"
+" and b.ide_bobod = c.ide_bobod"
+" and b.ide_tepro = d.ide_tepro"
+" and ide_bocoe in ( "+ide_bocoe+" ) "
+" and activo_boegr in ("+estado+" )" 
+" order by detalle_bomat";
	return tab_solicitud;
}

public String getMaterialesEgresoCodigo(String ide_boegr,String estado){
	String tab_solicitud="select c.ide_boegr,ide_bocoe,documento_egreso_boegr,codigo_bomat,detalle_bomat,fecha_egreso_boegr,"
+" cantidad_egreso_boegr,fecha_compra_bobod,num_factura_bobod,descripcion_bobod,marca_bobod,"
+" modelo_bobod,serie_bobod,color_bobod, b.ide_tepro,nombre_tepro,valor_unitario_bobod,valor_total_bobod"
+" from bodt_material a,bodt_bodega b,bodt_egreso c,tes_proveedor d"
+" where a.ide_bomat = b.ide_bomat"
+" and b.ide_bobod = c.ide_bobod"
+" and b.ide_tepro = d.ide_tepro"
+" and c.ide_boegr in ( "+ide_boegr+" ) "
+" order by detalle_bomat";
	return tab_solicitud;
}
public String getEstadoFactura(String ide_solicitud_compra,String estado){
	String tab_solicitud="select ide_adfac,ide_adsoc,activo_adfac from adq_factura where ide_adsoc in ("+ide_solicitud_compra+") and activo_adfac in ("+estado+")";
	return tab_solicitud;
}
public TablaGenerica getTablaGenericaSolicitudCompra(String ide_addef){
	TablaGenerica tab_solicitud_comp=utilitario.consultar("select b.ide_addef,b.ide_adfac,a.ide_tepro, a.ide_adsoc,detalle_adsoc,num_factura_adfac,valor_adsoc,nro_solicitud_adsoc,valor_total_addef,valor_unitario_addef,cantidad_addef," +
				" codigo_bomat,detalle_bomat,d.ide_bomat,fecha_factura_adfac,marca_addef,serie_addef,color_addef,modelo_addef from adq_solicitud_compra a,adq_detalle_factura b,adq_factura c , bodt_material d" +
				" where a.ide_adsoc=c.ide_adsoc and b.ide_adfac=c.ide_adfac  and d.ide_bomat=b.ide_bomat and  b.ide_addef in ("+ide_addef+") order by codigo_bomat");
	return tab_solicitud_comp;
}
public TablaGenerica getTablaGenericaValidaInventario(String ide_addef,String anio){
	TablaGenerica tab_solicitud_comp=utilitario.consultar("select ide_addef,a.ide_bomat from adq_detalle_factura a"
+" left join bodt_inventario b on  a.ide_bomat= b.ide_bomat and b.ide_geani ="+anio
+" where b.ide_bomat is null and ide_addef in ("+ide_addef+")");
	return tab_solicitud_comp;
}
public TablaGenerica getTablaGenericaMaterial(String ide_bomat){
	TablaGenerica tab_solicitud_comp=utilitario.consultar("select ide_bomat,detalle_bomat,codigo_bomat from bodt_material where ide_bomat in ("+ide_bomat+")");
	return tab_solicitud_comp;
}
public String getEgresoSolicitud(){
	String tab_solicitud="select a.ide_adsoc,num_factura_bobod as numero_factura,numero_ingreso_bobod as ingreso_bodega,num_doc_bobod as documento_ingreso_bodega,detalle_adsoc as detalle_compra,"
+" nro_solicitud_adsoc as numero_solicitud_compra,valor_adsoc as valor_compra,nombre_tepro as proveedor,ruc_tepro as ruc_proveedor" 
+" from adq_solicitud_compra a , tes_proveedor b,("
+" select ide_adsoc,num_factura_bobod,num_doc_bobod,numero_ingreso_bobod from bodt_bodega where activo_bobod =true  group by ide_adsoc,num_doc_bobod,num_factura_bobod,numero_ingreso_bobod"
+" ) c where a.ide_tepro=b.ide_tepro and a.ide_adsoc = c.ide_adsoc order by num_doc_bobod";
	return tab_solicitud;
}
public TablaGenerica getEgresoSolicitudBodega(String compra){
	TablaGenerica tab_solicitud=utilitario.consultar("select ide_bobod,ide_bomat,cantidad_ingreso_bobod,ide_adsoc from bodt_bodega where ide_adsoc ="+compra);
	return tab_solicitud;
}
public String getMaterialBodegaCompras(){
	String tab_solicitud="select ide_bobod,detalle_bomat, codigo_bomat from bodt_bodega a,bodt_material b where a.ide_bomat = b.ide_bomat and not ide_adsoc is null";
	return tab_solicitud;
}
public String getCodigoMaterial(String cod_material){
	String tab_solicitud="";
	return tab_solicitud;
}
}

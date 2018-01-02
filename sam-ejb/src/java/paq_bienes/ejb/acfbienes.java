/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bienes.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author l-suntaxi
 */
@Stateless
public class acfbienes {

    private Utilitario utilitario = new Utilitario();
    private Conexion conSqlcy;

    public String listaMax(String tipo) {
        conSqlcy();
        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSqlcy();
        tabFuncionario.setConexion(conSqlcy);
//        tabFuncionario.setSql("select 0 as id, (case when max(numero) is null then 1 else max(numero)+1 end) as maximo from acf");
        tabFuncionario.setSql("select id, (case when max(numero) is null then 1 else max(numero)+1 end) as maximo from numericos where (case when anio = year(GETDATE()) then 1 when anio = (year(GETDATE())-1) then 1 else 0 end)=1 and estado=1 and tipo ='" + tipo + "' group by id");
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("maximo");
        return ValorMax;
    }

    public void setUpdateFallecido(Integer codigo, String tipo) {
        String auSql = "update numericos\n"
                + "set numero=" + codigo + "\n"
                + "where id =(select id from numericos where (case when anio = year(GETDATE()) then 1 when anio = (year(GETDATE())-1) then 1 else 0 end)=1 and estado=1 and tipo ='" + tipo + "')";
        conSqlcy();
        conSqlcy.ejecutarSql(auSql);
        desConSqlcy();
    }

    public void setUpdateActa(Integer codigo) {
        String auSql = "update ACF_INMUEBLES\n"
                + "set acta_baja = 0 \n"
                + "where id_inmueble = " + codigo;
        conSqlcy();
        conSqlcy.ejecutarSql(auSql);
        desConSqlcy();
    }

    public TablaGenerica getInforCuenta() {
        conSqlcy();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSqlcy);
        tabPersona.setSql("select  distinct gru_id, gru_nombre  from activo INNER join GRUPO G on activo.GRU_ID1= G.GRU_ID order by gru_nombre");
        desConSqlcy();
        return tabPersona;
    }

    public TablaGenerica getInfoTotales(Integer numero) {
        conSqlcy();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSqlcy);
        tabPersona.setSql("SELECT c.CUSTODIO,SUM(c.VALOR) AS TOTAL,SUM(c.CANTIDAD) AS ITEM ,year(cast(c.fecha_acta as date))  as anio,c.numero,c.fecha_acta\n"
                + "FROM ACF c,\n"
                + "(select * from ACF where ide_acta = " + numero + ") as a\n"
                + "WHERE c.numero = a.numero and c.fecha_acta  = a.fecha_acta and c.CUSTODIO = a.CUSTODIO\n"
                + "GROUP BY c.CUSTODIO,c.fecha_acta,c.numero");
        tabPersona.ejecutarSql();
        desConSqlcy();
        return tabPersona;
    }

    public TablaGenerica getActivoTotales(Integer numero) {
        conSqlcy();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSqlcy);
        tabPersona.setSql("select count(*) as item, sum(act_valorcompra) as total from ACTIVO where CUS_ID1 = " + numero);
        tabPersona.ejecutarSql();
        desConSqlcy();
        return tabPersona;
    }

    public String guardActa(String ACT_ID, String fecha, String responsable, String numero, String codigo) {
        conSqlcy();
        String mensaje = "Registro Guardado";
        conSqlcy.ejecutarSql("insert into acf (IDE_ACTA,ACF.barras,ACF.codigo,ACF.dependencia,ACF.direccion,ACF.oficina,ACF.custodio,\n"
                + "ACF.cuenta,ACF.observaciones,ACF.nombre,ACF.descripcion,ACF.estado,ACF.marca,ACF.modelo,ACF.serie,ACF.fecha,ACF.valor,ACF.util,ACF.residual,ACF.responsable,ACF.fecha_acta,ACF.numero,ACF.cantidad)\n"
                + "\n"
                + "SELECT 	( SELECT max (IDE_ACTA)+1 from acf)  ,\n"
                + "	[ACT_CODBARRAS] as CODBARRAS,ACT_CODIGO1 as codigoanterior,\n"
                + "	UO1.UOR_NOMBRE as dependencia,\n"
                + "	UO2.UOR_NOMBRE as direccion,\n"
                + "	UO3.UOR_NOMBRE as odicina,\n"
                + "	C.CUS_APELLIDOS+' '+ISNULL(C.CUS_NOMBRES,'') as custodio,\n"
                + "	G.GRU_NOMBRE as cuenta,\n"
                + "  ACT_OBSERVACIONES as observaciones,\n"
                + "	GG.GRU_NOMBRE as nombre,\n"
                + "  GGG.GRU_NOMBRE as descripcion,\n"
                + "	E.EST_NOMBRE as estado,\n"
                + "	MAR_NOMBRE as MARCA,\n"
                + "	MOD_NOMBRE as MODELO,\n"
                + "	ACT_SERIE1 as serie,ACT_FECHACOMPRA as fechacompra,ACT_VALORCOMPRA as valorcompra,ACT_VIDAUTIL as vidautil,ACT_VALORRESIDUALNIIF as valorresidual,\n"
                + "'" + responsable + "','" + fecha + "'," + numero + "\n"
                + ",(CASE WHEN can.car_valor IS NULL THEN 1 ELSE cast(can.car_valor as integer) END)  AS cantidad \n"
                + "	FROM ACTIVO\n"
                + "	INNER join ESTADO E\n"
                + "	on activo.EST_ID1= E.EST_ID\n"
                + "	INNER join ESTADO EE\n"
                + "	on activo.EST_ID2= EE.EST_ID\n"
                + "	INNER join ESTADO EEE\n"
                + "	on activo.EST_ID3= EEE.EST_ID\n"
                + "	INNER join GRUPO G\n"
                + "	on activo.GRU_ID1= G.GRU_ID\n"
                + "	INNER join GRUPO GG\n"
                + "	on activo.GRU_ID2= GG.GRU_ID\n"
                + "	INNER join GRUPO GGG\n"
                + "	on activo.GRU_ID3= GGG.GRU_ID\n"
                + "	INNER join UGEOGRAFICA UG1\n"
                + "	on activo.UGE_ID1= UG1.UGE_ID\n"
                + "	INNER join UGEOGRAFICA UG2\n"
                + "	on activo.UGE_ID2= UG2.UGE_ID\n"
                + "	INNER join UGEOGRAFICA UG3\n"
                + "	on activo.UGE_ID3= UG3.UGE_ID\n"
                + "	INNER join UGEOGRAFICA UG4\n"
                + "	on activo.UGE_ID4= UG4.UGE_ID\n"
                + "	INNER join UORGANICA UO1\n"
                + "	on activo.UOR_ID1= UO1.UOR_ID\n"
                + "	INNER join UORGANICA UO2\n"
                + "	on activo.UOR_ID2= UO2.UOR_ID\n"
                + "	INNER join UORGANICA UO3\n"
                + "	on activo.UOR_ID3= UO3.UOR_ID\n"
                + "	INNER join CUSTODIO C\n"
                + "	on activo.CUS_ID1= C.CUS_ID	\n"
                + "	INNER join MARCA\n"
                + "	on activo.MAR_ID= MARCA.MAR_ID\n"
                + "	INNER join MODELO\n"
                + "	on activo.MOD_ID= MODELO.MOD_ID\n"
                + "	left join COLOR\n"
                + "	on activo.COL_ID= COLOR.COL_ID\n"
                + "	INNER join EMPRESA\n"
                + "	on activo.EMP_ID= EMPRESA.EMP_ID\n"
                + "	left join ESTRUCOMP ES1\n"
                + "	on activo.ECO_ID1= ES1.ECO_ID\n"
                + "	left join ESTRUCOMP ES2\n"
                + "	on activo.ECO_ID2= ES2.ECO_ID\n"
                + "	left join PROVEEDOR PRO on activo.PRO_ID= PRO.PRO_ID  "
                + "left join (select act_id,cfa_nombre,convert(DECIMAL(17,2),car_valor) as car_valor from CARACTERISTICA a, cfamilia b where b.cfa_id=a.cfa_id and cfa_nombre='Cantidad'\n"
                + ") can \n"
                + "on can.act_id=ACTIVO.ACT_ID "
                + "where ACTIVO.ACT_ID='" + ACT_ID + "'");

        desConSqlcy();
        return mensaje;
    }

    public TablaGenerica buscaDatosAvaluos(String clave) {
        conSqlcy();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSqlcy);
        tabPersona.setSql("select * from vw_ActivosInmuebles\n"
                + "inner join  vw_CatastroUrbanoRural  on MAE_CLVACT = clave\n"
                + "where MAE_CLVACT = '"+clave+"'");
        tabPersona.ejecutarSql();
        desConSqlcy();
        return tabPersona;
    }

    public TablaGenerica buscaFirmaAvaluos(String descripcion) {
        conSqlcy();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSqlcy);
        tabPersona.setSql("SELECT * FROM SIGAG.dbo.SIS_AUXREPOR "
                + "where SIGAG.dbo.SIS_AUXREPOR.REPT_NOMBRE like '"+descripcion+"'");
        tabPersona.ejecutarSql();
        tabPersona.imprimirSql();
        desConSqlcy();
        return tabPersona;
    }
    
    private void conSqlcy() {
        if (conSqlcy == null) {
            conSqlcy = new Conexion();
            conSqlcy.setUnidad_persistencia(utilitario.getPropiedad("recursojdbcayman"));
        }
    }

    private void desConSqlcy() {
        if (conSqlcy != null) {
            conSqlcy.desconectar(true);
            conSqlcy = null;
        }
    }
}

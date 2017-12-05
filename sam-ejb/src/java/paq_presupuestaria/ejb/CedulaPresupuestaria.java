/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
@Stateless
public class CedulaPresupuestaria {

    private Conexion conPostgres, conOracle, conSQL;
    private Utilitario utilitario = new Utilitario();

    public CedulaPresupuestaria() {
    }

    public void setDatosParametrosGastos(Integer codigo, String cuenta, String tipo, String num_proyecto, String auxiliar, String nom_proyecto, String direccion, String programa) {
        String strSqlr = "insert into clas_orien_gasto_proyecto (id_codigo,cuenta,tipo,num_proyecto,auxiliar,nom_proyecto,direccion,programa)\n"
                + "values(" + codigo + "," + cuenta + ",'" + tipo + "','" + num_proyecto + "','" + auxiliar + "','" + nom_proyecto + "','" + direccion + "'," + programa + ")";
        conSql();
        conSQL.ejecutarSql(strSqlr);
        desSql();
    }

    public void setDatosParametroGasto(Integer codigo, String tipo, String partida, String clasificador, String funcion, String orientacion, String programa) {
        String strSqlr = "insert into presu_clasificador (id_codigo,tipo_presupuesto,partida,clasificador_presu,funcion,orientacion,programa)\n"
                + "values (" + codigo + ",'" + tipo + "','" + partida + "','" + clasificador + "'," + funcion + "," + orientacion + ",'" + programa + "')";
        conSql();
        conSQL.ejecutarSql(strSqlr);
        desSql();
    }

    public void setDatosCedulaGastos(Integer id_codigo, String cuenta, String tipo, String num_proyecto, String auxiliar, String programa, Double inicial, Double reforma,
            Double codificado, Double compromiso, Double devengado, Double ejecutado) {
        String strSqlr = "insert into  clas_orien_proyecto (id_codigo,cuenta,tipo,num_proyecto,auxiliar,programa,inicial,reforma,\n"
                + " codificado,compromiso,devengado,ejecutado)\n"
                + "values (" + id_codigo + "," + cuenta + ",'" + tipo + "','" + num_proyecto + "','" + auxiliar + "'," + programa + "," + inicial + "," + reforma + ",\n"
                + "" + codificado + "," + compromiso + "," + devengado + "," + ejecutado + ")";
        conSql();
        conSQL.ejecutarSql(strSqlr);
        desSql();
    }

    public void setClasificadoGastos(Integer codigo, String clasificador, String funcion) {
        // Forma el sql para actualizacion
        String strSqlr = "update presu_clasificador\n"
                + "set orientacion =" + clasificador + ",\n"
                + "funcion = " + funcion + "\n"
                + "where id_codigo=" + codigo;
        conSql();
        conSQL.ejecutarSql(strSqlr);
        desSql();
    }

    public void setGastoClasificacion() {
        String strSqlr = "delete from clas_orien_proyecto";
        conSql();
        conSQL.ejecutarSql(strSqlr);
        desSql();
    }

    public TablaGenerica getGastosProyectoClasificador() {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select ROWNUM as numero, CUENDT,TAAD02,AUAD02,AUAD01,inicial,dep,POSTM,proyecto   \n"
                + " from (  \n"
                + " select CUENDT,TAAD02,AUAD01,inicial,dep\n"
                + ",(case when TAAD02 = 'CC' then null else AUAD02 end ) as AUAD02 \n"
                + ",(case when TAAD02 = 'CC' then AUAD02 else POSTM end ) as POSTM \n"
                + " ,(select DISTINCT NOMBMA from USFIMRU.TIGSA_GLm05 where CAUXMA = AUAD02 and POSTMA=POSTM ) as proyecto  \n"
                + " from (  \n"
                + " select * from (  \n"
                + " select CUENDT,TAAD02,AUAD02,AUAD01, sum(MONTDT) as inicial   \n"
                + " from (SELECT CUENDT,TAAD02, AUAD02,AUAD01,MONTDT  \n"
                + " FROM USFIMRU.TIGSA_GLB01   \n"
                + " where STATDT='E'   \n"
                + " AND CCIADT='CM'   \n"
                + " AND SAPRDT>=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "14\n"
                + " AND SAPRDT<=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "15\n"
                + " and MONTDT>0  \n"
                + " and AUAD02  is not null)  \n"
                + " group by CUENDT,TAAD02,AUAD02,AUAD01)  \n"
                + " left join   \n"
                + " (SELECT CAUXMA,POSTMA as POSTM,( select NOMBZ1 from USFIMRU.TIGSA_GLM11 where CLASZ1 = '001' and ZONAZ1 = SUBSTR(ZONAMA, 0, 2)) as dep FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')  \n"
                + " on AUAD02 = CAUXMA))");
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getFuncionGasto() {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSQL);
        tabFuncionario.setSql("select DISTINCT tipo_presupuesto,gr,gr1,sub,item,cuenta,funcion,orientacion,programa\n"
                + "from (select 'G' as tipo_presupuesto,substring(cuenta,1,6) as cuenta,substring(cuenta,1,1) as gr,substring(cuenta,2,1) as gr1,substring(cuenta,3,2) as sub,substring(cuenta,5,2) as item , orientacion,funcion,programa\n"
                + "from(select cuenta,orientacion,tipo,funcion,programa\n"
                + "from clas_orien_gasto_proyecto\n"
                + "where orientacion is null) as a) as b");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public String listaMax() {
        conSql();

        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSQL);
        tabFuncionario.setSql("select 0 as id ,max(id_codigo) AS maximo from presu_clasificador");
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("maximo") + 1;
        desSql();
        return ValorMax;
    }

    public TablaGenerica getVerificaParametro1(String cuenta, String tipo, String proyecto, String auxi, String programa) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSQL);
        tabFuncionario.setSql("select * from clas_orien_gasto_proyecto\n"
                + "where cuenta =" + cuenta + "  and tipo ='" + tipo + "' and num_proyecto ='" + proyecto + "' and auxiliar ='" + auxi + "' and programa='" + programa + "'");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public TablaGenerica getVerificaParametro(String tipo, String cuenta, String programa) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSQL);
        tabFuncionario.setSql("select * from presu_clasificador\n"
                + "where tipo_presupuesto ='" + tipo + "' and clasificador_presu='" + cuenta + "' and  programa='" + programa + "'");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public TablaGenerica getVerificaParame(String tipo, String cuenta, String proyecto, String auxi, String programa) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSQL);
        tabFuncionario.setSql("select * from presu_clasificador\n"
                + "where tipo_presupuesto ='" + tipo + "' and clasificador_presu='" + cuenta + "' and funcion='" + proyecto + "' and orientacion='" + auxi + "' and  programa='" + programa + "'");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public TablaGenerica getCedulaPresupuesto(String mes, Integer meses,String anio) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select ROWNUM as numero,CUENDT,TAAD02  \n"
                + ",(case when TAAD02='CC' then null else AUAD02 end) as AUAD02  \n"
                + ",AUAD01,dep  \n"
                + ",(case when TAAD02='CC' then AUAD02 else POSTM end) as POSTM  \n"
                + ",(case when inicial is null then 0 else inicial end)as inicial,reforma,codificado,compromiso,devengado,ejecutado \n"
                + "from(select DISTINCT CUENDT,TAAD02,AUAD02,AUAD01, inicial,dep,POSTM,proyecto    \n"
                + ",(case when reforma is null then 0 else reforma end)as reforma    \n"
                + ",(case when codificado is null then 0 else codificado end) as codificado    \n"
                + ",(case when compromiso is null then 0 else compromiso end) as compromiso    \n"
                + ",(case when devengado is null then 0 else devengado end)as devengado    \n"
                + ",(case when ejecutado is null then 0 else ejecutado end) as ejecutado \n"
                + "from (select CUENDT,TAAD02,AUAD01,AUAD02,sum(codificado) as codificado,POSTM,Dep,proyecto  \n"
                + "from (select CUENDT,TAAD02,AUAD01,AUAD02,sum(codificado) as codificado,POSTM,Dep,proyecto   \n"
                + "from (select CUENDT,TAAD02,AUAD01,AUAD02,codificado ,POSTM,Dep \n"
                + ",(select DISTINCT NOMBMA from USFIMRU.TIGSA_GLm05 where CAUXMA = AUAD02 and POSTMA=POSTM ) as proyecto   \n"
                + "from (select CUENDT,TAAD02,AUAD01,AUAD02, sum(MONTDT) as codificado ,FDOCDT \n"
                + "from (SELECT CUENDT,TAAD02,AUAD01,(case when TAAD02 = 'CC' then null else AUAD02 end ) as AUAD02,MONTDT,FDOCDT \n"
                + "FROM USFIMRU.TIGSA_GLB01     \n"
                + "where STATDT='E'     \n"
                + "AND CCIADT='CM'     \n"
                + "AND SAPRDT>=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "14 \n"
                + "AND SAPRDT<=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "15 \n"
                + "and AUAD02  is not null)    \n"
                + "where TAAD02 = 'PY'    \n"
                + "group by CUENDT,TAAD02,AUAD01,AUAD02,FDOCDT) \n"
                + "left join  \n"
                + "(SELECT CAUXMA,POSTMA as POSTM,( select NOMBZ1 from USFIMRU.TIGSA_GLM11 where CLASZ1 = '001' and ZONAZ1 = SUBSTR(ZONAMA, 0, 2)) as dep FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')    \n"
                + "on AUAD02 = CAUXMA \n"
                + "where substr(FDOCDT,1,5) <= " + meses + ") \n"
                + "group by CUENDT,TAAD02,AUAD01,AUAD02,codificado ,POSTM,Dep,proyecto)\n"
                + "group by CUENDT,TAAD02,AUAD01,AUAD02,POSTM,Dep,proyecto)\n"
                + "left join \n"
                + "(select CUENDT as cuentai,TAAD02 as auxi,AUAD02 as auxi1,AUAD01 as auxi2,inicial,POSTM as auxi3   \n"
                + "from (select * from (select CUENDT,TAAD02,AUAD02,AUAD01, sum(MONTDT) as inicial    \n"
                + "from (SELECT CUENDT,TAAD02,(case when TAAD02 = 'CC' then null else AUAD02 end ) as AUAD02,AUAD01,MONTDT   \n"
                + "FROM USFIMRU.TIGSA_GLB01    \n"
                + "where STATDT='E'    \n"
                + "AND CCIADT='CM'    \n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "14     \n"
                + "and AUAD02  is not null)   \n"
                + "group by CUENDT,TAAD02,AUAD02,AUAD01)   \n"
                + "left join    \n"
                + "(SELECT CAUXMA,POSTMA as POSTM FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')   \n"
                + "on AUAD02 = CAUXMA)   \n"
                + "where TAAD02 = 'PY')\n"
                + "on CUENDT = cuentai and POSTM = auxi3 and TAAD02 = auxi and AUAD02 = auxi1 and AUAD01 = auxi2\n"
                + "left join \n"
                + "(select * from (select CUENDT as cuentar,TAAD02 as auxr,AUAD01 as auxr1,AUAD02 as auxr2, sum(MONTDT) as reforma    \n"
                + "from (SELECT CUENDT,TAAD02,AUAD01,(case when TAAD02 = 'CC' then null else AUAD02 end ) as AUAD02,MONTDT   \n"
                + "FROM USFIMRU.TIGSA_GLB01    \n"
                + "where STATDT='E'    \n"
                + "AND CCIADT='CM'    \n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "15 \n"
                + "and substr(FDOCDT,1,5) <= " + meses + "\n"
                + "and AUAD02  is not null)   \n"
                + "where TAAD02 = 'PY'   \n"
                + "group by CUENDT,TAAD02,AUAD01,AUAD02))\n"
                + "on CUENDT= cuentar and TAAD02= auxr and AUAD01 = auxr1 and AUAD02= auxr2 \n"
                + "left join    \n"
                + "(select CUENDT as cuentac,TAAD02 as auxc,AUAD01 as auxc1,AUAD02 as auxc2, sum(MONTDT) as compromiso    \n"
                + "from (SELECT CUENDT,TAAD02,AUAD01,(case when TAAD02 = 'CC' then null else AUAD02 end ) as AUAD02,MONTDT   \n"
                + "FROM USFIMRU.TIGSA_GLB01    \n"
                + "where STATDT='E'   \n"
                + "AND CCIADT='CM'   \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01   \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='1'   \n"
                + "AND TMOVDT='D')   \n"
                + "where TAAD02 = 'PY'   \n"
                + "group by CUENDT,TAAD02,AUAD01,AUAD02)   \n"
                + "on CUENDT= cuentac and TAAD02= auxc and AUAD01 = auxc1 and AUAD02= auxc2\n"
                + "left join    \n"
                + "  (select CUENDTD,devengado,AUAD02 as auxe2 , \n"
                + " (case when (max(ROUND(ejecutado,2))over (PARTITION BY substr(CUENDTD,1,2) ORDER BY substr(CUENDTD,1,2))) = ejecutado \n"
                + " then (totalee+(prorateo-totale)) else ejecutado end) as ejecutado \n"
                + " ,TAAD02 as auxe,AUAD01 as auxe1 \n"
                + " from( \n"
                + " select CUENDTD,devengado,AUAD02,ROUND(ejecutado,2) as ejecutado,prorateo  \n"
                + " ,(sum(ROUND(ejecutado,2))over (PARTITION BY substr(CUENDTD,1,2) ORDER BY substr(CUENDTD,1,2))) as totale \n"
                + " ,(max(ROUND(ejecutado,2))over (PARTITION BY substr(CUENDTD,1,2) ORDER BY substr(CUENDTD,1,2))) as totalee \n"
                + " ,TAAD02,AUAD01 \n"
                + " from ( \n"
                + " select CUENDT as CUENDTD,devengado,AUAD02,((prorateo/total)*devengado) as ejecutado,prorateo,TAAD02,AUAD01 \n"
                + " from ( \n"
                + " select  CUENDT,devengado,AUAD02 ,substr(CUENDT,1,2) as pro \n"
                + " ,(sum(devengado)over (PARTITION BY substr(CUENDT,1,2) ORDER BY substr(CUENDT,1,2))) as total \n"
                + " ,TAAD02,AUAD01 from ( \n"
                + " select CUENDT,sum(devengado) as devengado,AUAD02 ,TAAD02,AUAD01 from ( \n"
                + " SELECT CUENDT , AUAD02 , SUM(MONTDT) as devengado,TAAD02,AUAD01 \n"
                + " FROM USFIMRU.TIGSA_GLB01 \n"
                + " where STATDT='E' \n"
                + " AND CCIADT='CM' \n"
                + " AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01 \n"
                + " AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "" + mes + "\n"
                + " AND CAUXDT='2' \n"
                + " AND TMOVDT='D' \n"
                + " GROUP BY CUENDT,AUAD02,TAAD02,AUAD01) \n"
                + " GROUP BY CUENDT,AUAD02,TAAD02,AUAD01) \n"
                + " where TAAD02 = 'PY') \n"
                + " left join \n"
                + " (select cuenta,prorateo,(case when substr(cuenta,4,2) = 98 then '97' else substr(cuenta,4,2) end) as cu from ( \n"
                + " SELECT substr(CUENDT,1,5) as cuenta, SUM(MONTDT) as prorateo \n"
                + " FROM USFIMRU.TIGSA_GLB01 \n"
                + " where STATDT='E' \n"
                + " and CCIADT = 'MR' \n"
                + " AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01 \n"
                + " AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "" + mes + "\n"
                + " AND TMOVDT='D' \n"
                + " and CUENDT like '213%' \n"
                + " group by substr(CUENDT,1,5))) \n"
                + " on pro= cu) \n"
                + " order by devengado) )   \n"
                + " on CUENDT= CUENDTD and TAAD02= auxe and AUAD01 = auxe1 and AUAD02= auxe2\n"
                + "UNION\n"
                + "select DISTINCT CUENDT,TAAD02,AUAD02,null as AUAD01,inicial,null as dep,null as POSTM, null as proyecto   \n"
                + ",(case when reforma is null then 0 else reforma end)as reforma   \n"
                + ",(case when codificado is null then 0 else codificado end) as codificado   \n"
                + ",(case when compromiso is null then 0 else compromiso end) as compromiso   \n"
                + ",(case when devengado is null then 0 else devengado end)as devengado   \n"
                + ",(case when ejecutado is null then 0 else ejecutado end) as ejecutado\n"
                + "from (select CUENDT,TAAD02, sum(inicial) as codificado,AUAD02 \n"
                + "from (select CUENDT,TAAD02, MONTDT as inicial,AUAD02,FDOCDT \n"
                + "from (SELECT CUENDT,TAAD02,MONTDT,AUAD02 ,FDOCDT \n"
                + "FROM USFIMRU.TIGSA_GLB01    \n"
                + "where STATDT='E'    \n"
                + "AND CCIADT='CM'    \n"
                + "AND SAPRDT>=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "14\n"
                + "AND SAPRDT<=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "15  \n"
                + "and AUAD02  is not null) \n"
                + "where TAAD02 = 'CC' and substr(FDOCDT,1,5) <= " + meses + ")\n"
                + "group by CUENDT,TAAD02,AUAD02)\n"
                + "left join \n"
                + "(select * from (   \n"
                + "select CUENDT as cuentai,TAAD02 as auxi, sum(MONTDT) as inicial,AUAD02 as auxi1  \n"
                + "from (SELECT CUENDT,TAAD02,MONTDT,AUAD02  \n"
                + "FROM USFIMRU.TIGSA_GLB01    \n"
                + "where STATDT='E'    \n"
                + "AND CCIADT='CM'    \n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "14  \n"
                + "and AUAD02  is not null)   \n"
                + "group by CUENDT,TAAD02,AUAD02)   \n"
                + "where auxi = 'CC')\n"
                + "on CUENDT= cuentai and TAAD02= auxi  and AUAD02 = auxi1\n"
                + "left join \n"
                + "(select * from (   \n"
                + "select CUENDT as cuentar,TAAD02 as auxr, sum(MONTDT) as reforma,AUAD02  as auxr1  \n"
                + "from (SELECT CUENDT,TAAD02,MONTDT,AUAD02   \n"
                + "FROM USFIMRU.TIGSA_GLB01    \n"
                + "where STATDT='E'    \n"
                + "AND CCIADT='CM'    \n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "15   \n"
                + "and substr(FDOCDT,1,5) <= " + meses + "  \n"
                + "and AUAD02  is not null)   \n"
                + "where TAAD02 = 'CC'   \n"
                + "group by CUENDT,TAAD02,AUAD02))\n"
                + "on CUENDT= cuentar and TAAD02= auxr  and AUAD02 = auxr1\n"
                + "left join (select CUENDT as cuentac,TAAD02 as auxc,sum(MONTDT) as compromiso,AUAD02 as auxc1  \n"
                + "from (SELECT CUENDT,TAAD02,MONTDT,AUAD02  \n"
                + "FROM USFIMRU.TIGSA_GLB01    \n"
                + "where STATDT='E'   \n"
                + "AND CCIADT='CM'   \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01   \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "" + mes + " \n"
                + "AND CAUXDT='1'   \n"
                + "AND TMOVDT='D')   \n"
                + "where TAAD02 = 'CC'   \n"
                + "group by CUENDT,TAAD02,AUAD02)   \n"
                + "on CUENDT= cuentac and TAAD02= auxc  and AUAD02 = auxc1\n"
                + "left join (select CUENDTD,devengado,POSTMAD, \n"
                + "(case when (max(ROUND(ejecutado,2))over (PARTITION BY substr(CUENDTD,1,2) ORDER BY substr(CUENDTD,1,2))) = ejecutado \n"
                + "then (totalee+(prorateo-totale)) else ejecutado end) as ejecutado \n"
                + ",TAAD02 as auxd \n"
                + "from( \n"
                + "select CUENDTD,devengado,POSTMAD,ROUND(ejecutado,2) as ejecutado,prorateo  \n"
                + ",(sum(ROUND(ejecutado,2))over (PARTITION BY substr(CUENDTD,1,2) ORDER BY substr(CUENDTD,1,2))) as totale \n"
                + ",(max(ROUND(ejecutado,2))over (PARTITION BY substr(CUENDTD,1,2) ORDER BY substr(CUENDTD,1,2))) as totalee \n"
                + ",TAAD02 \n"
                + "from ( \n"
                + "select CUENDT as CUENDTD,devengado,POSTMA as POSTMAD,((prorateo/total)*devengado) as ejecutado,prorateo,TAAD02 \n"
                + "from ( \n"
                + "select  CUENDT,devengado,POSTMA,substr(CUENDT,1,2) as pro \n"
                + ",(sum(devengado)over (PARTITION BY substr(CUENDT,1,2) ORDER BY substr(CUENDT,1,2))) as total \n"
                + ",TAAD02 from ( \n"
                + "select CUENDT,sum(devengado) as devengado,POSTMA,TAAD02 from ( \n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as devengado,TAAD02 \n"
                + "FROM USFIMRU.TIGSA_GLB01 \n"
                + "where STATDT='E' \n"
                + "AND CCIADT='CM' \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01 \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='2' \n"
                + "AND TMOVDT='D' \n"
                + "GROUP BY CUENDT, AUAD02,TAAD02) \n"
                + "left join \n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC' \n"
                + "UNION \n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY') \n"
                + "on AUAD02 = CAUXMA \n"
                + "GROUP BY CUENDT,POSTMA,TAAD02) \n"
                + "where TAAD02 = 'CC') \n"
                + "left join \n"
                + "(select cuenta,prorateo,(case when substr(cuenta,4,2) = 98 then '97' else substr(cuenta,4,2) end) as cu from ( \n"
                + "SELECT substr(CUENDT,1,5) as cuenta, SUM(MONTDT) as prorateo \n"
                + "FROM USFIMRU.TIGSA_GLB01 \n"
                + "where STATDT='E' \n"
                + "and CCIADT = 'MR' \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01 \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "" + mes + "\n"
                + "AND TMOVDT='D' \n"
                + "and CUENDT like '213%' \n"
                + "group by substr(CUENDT,1,5))) \n"
                + "on pro= cu) \n"
                + "order by devengado))   \n"
                + "on CUENDT= CUENDTD and TAAD02= auxd and AUAD02 = POSTMAD)");
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getIngresos(String mes, Integer meses,String anio) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select DISTINCT CUENMC,SUBSTR(CUENMC, 1, 2) as gr,SUBSTR(CUENMC, 3, 2)as sub,SUBSTR(CUENMC, 5, 2)as item,NOLAAD  \n"
                + ",to_char((case when inicial is null then 0 else inicial end), 'fm999999999.90')as inicial  \n"
                + ",reforma  \n"
                + ",to_char(devengado, 'fm999999999.90') as devengado  \n"
                + ",to_char(ejecutado, 'fm999999999.90') as ejecutado  \n"
                + ",to_char((case when (codificado) is null then 0 else (codificado)end), 'fm999999999.90') as codificado  \n"
                + ",to_char((case when (codificado - devengado) is null then 0 else(codificado - devengado)end), 'fm999999999.90') as saldo  \n"
                + ", 'I' as tipo \n"
                + "from (select DISTINCT CUENMC,CEDTMC,nivemc,  \n"
                + "(case when nivemc = 1 then inicial1  \n"
                + "when nivemc = 2 then inicial2  \n"
                + "when nivemc = 3 then inicial4  \n"
                + "when nivemc = 4 then inicial6  \n"
                + "when nivemc = 5 then inicial8  \n"
                + "when nivemc = 6 then inicial end)  \n"
                + "as codificado,NOLAAD  \n"
                + "from(  \n"
                + "select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8,inicial6,inicial4,inicial2  \n"
                + ",sum(inicial) over (PARTITION BY substr(CUENMC,1,1) ORDER BY substr(CUENMC,1,1)) as inicial1  \n"
                + "from (  \n"
                + "select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8,inicial6,inicial4  \n"
                + ",sum(inicial) over (PARTITION BY substr(CUENMC,1,2) ORDER BY substr(CUENMC,1,2)) as inicial2  \n"
                + "from (  \n"
                + "select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8,inicial6  \n"
                + ",sum(inicial) over (PARTITION BY substr(CUENMC,1,4) ORDER BY substr(CUENMC,1,4)) as inicial4  \n"
                + "from (  \n"
                + "select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8  \n"
                + ",sum(inicial) over (PARTITION BY substr(CUENMC,1,6) ORDER BY substr(CUENMC,1,6)) as inicial6  \n"
                + "from (  \n"
                + "select CUENMC,CEDTMC,nivemc,NOLAAD,inicial  \n"
                + ",sum(inicial) over (PARTITION BY substr(CUENMC,1,8) ORDER BY substr(CUENMC,1,8)) as inicial8  \n"
                + "from (select CUENMC,CEDTMC,nivemc,NOLAAD  \n"
                + "from USFIMRU.TIGSA_GLM03  \n"
                + "where TIPLMC= 'P' and substr(CEDTMC,1,1) in (1,2,3))  \n"
                + "left join  \n"
                + "(SELECT CUENDT,SUM(inicial) as inicial \n"
                + "from (select * \n"
                + "from (SELECT CUENDT,SUM(MONTDT)*-1 as inicial ,FDOCDT \n"
                + "FROM USFIMRU.TIGSA_GLB01  \n"
                + "where STATDT='E'  \n"
                + "AND CCIADT='CM'  \n"
                + "AND SAPRDT>=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "14\n"
                + "AND SAPRDT<=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "15\n"
                + "GROUP BY CUENDT,FDOCDT )\n"
                + "where substr(FDOCDT,1,5) <=" + meses + ")\n"
                + "GROUP BY CUENDT\n"
                + "order by CUENDT)  \n"
                + "on CUENMC = CUENDT))))))\n"
                + "left join \n"
                + "(select DISTINCT CUENMC as CUENMCi,CEDTMC as CEDTMCi,nivemc as nivemci,  \n"
                + "(case when nivemc = 1 then inicial1  \n"
                + "when nivemc = 2 then inicial2  \n"
                + "when nivemc = 3 then inicial4  \n"
                + "when nivemc = 4 then inicial6  \n"
                + "when nivemc = 5 then inicial8  \n"
                + "when nivemc = 6 then inicial end)  \n"
                + "as inicial \n"
                + "from(  \n"
                + "select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8,inicial6,inicial4,inicial2  \n"
                + ",sum(inicial) over (PARTITION BY substr(CUENMC,1,1) ORDER BY substr(CUENMC,1,1)) as inicial1  \n"
                + "from (  \n"
                + "select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8,inicial6,inicial4  \n"
                + ",sum(inicial) over (PARTITION BY substr(CUENMC,1,2) ORDER BY substr(CUENMC,1,2)) as inicial2  \n"
                + "from (  \n"
                + "select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8,inicial6  \n"
                + ",sum(inicial) over (PARTITION BY substr(CUENMC,1,4) ORDER BY substr(CUENMC,1,4)) as inicial4  \n"
                + "from (  \n"
                + "select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8  \n"
                + ",sum(inicial) over (PARTITION BY substr(CUENMC,1,6) ORDER BY substr(CUENMC,1,6)) as inicial6  \n"
                + "from (  \n"
                + "select CUENMC,CEDTMC,nivemc,NOLAAD,inicial  \n"
                + ",sum(inicial) over (PARTITION BY substr(CUENMC,1,8) ORDER BY substr(CUENMC,1,8)) as inicial8  \n"
                + "from (select CUENMC,CEDTMC,nivemc,NOLAAD  \n"
                + "from USFIMRU.TIGSA_GLM03  \n"
                + "where TIPLMC= 'P' and substr(CEDTMC,1,1) in (1,2,3))  \n"
                + "left join  \n"
                + "(SELECT CUENDT,SUM(MONTDT)*-1 as inicial  \n"
                + "FROM USFIMRU.TIGSA_GLB01  \n"
                + "where STATDT='E'  \n"
                + "AND CCIADT='CM'  \n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "14  \n"
                + "GROUP BY CUENDT  \n"
                + "order by CUENDT)  \n"
                + "on CUENMC = CUENDT))))))\n"
                + "on CUENMC = CUENMCi and nivemc=nivemci\n"
                + "left join  \n"
                + "(select CUENMC as CUENMCr, (case when reforma is null then 0 else reforma end)as reforma ,nivemc as nivemcr  \n"
                + "from(  \n"
                + "select DISTINCT CUENMC as ,nivemc,  \n"
                + "(case when nivemc = 1 then inicial1  \n"
                + "when nivemc = 2 then inicial2  \n"
                + "when nivemc = 3 then inicial4  \n"
                + "when nivemc = 4 then inicial6  \n"
                + "when nivemc = 5 then inicial8  \n"
                + "when nivemc = 6 then inicial end)  \n"
                + "as reforma  \n"
                + "from(select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8,inicial6,inicial4,inicial2  \n"
                + ",sum(inicial) over (PARTITION BY substr(CUENMC,1,1) ORDER BY substr(CUENMC,1,1)) as inicial1  \n"
                + "from (  \n"
                + "select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8,inicial6,inicial4  \n"
                + ",sum(inicial) over (PARTITION BY substr(CUENMC,1,2) ORDER BY substr(CUENMC,1,2)) as inicial2  \n"
                + "from (  \n"
                + "select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8,inicial6  \n"
                + ",sum(inicial) over (PARTITION BY substr(CUENMC,1,4) ORDER BY substr(CUENMC,1,4)) as inicial4  \n"
                + "from (  \n"
                + "select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8  \n"
                + ",sum(inicial) over (PARTITION BY substr(CUENMC,1,6) ORDER BY substr(CUENMC,1,6)) as inicial6  \n"
                + "from (  \n"
                + "select CUENMC,CEDTMC,nivemc,NOLAAD,inicial  \n"
                + ",sum(inicial) over (PARTITION BY substr(CUENMC,1,8) ORDER BY substr(CUENMC,1,8)) as inicial8  \n"
                + "from (select CUENMC,CEDTMC,nivemc,NOLAAD  \n"
                + "from USFIMRU.TIGSA_GLM03  \n"
                + "where TIPLMC= 'P' and substr(CEDTMC,1,1) in (1,2,3))  \n"
                + "left join  \n"
                + "(SELECT CUENDT,SUM(MONTDT)*-1 as inicial  \n"
                + "FROM USFIMRU.TIGSA_GLB01  \n"
                + "where STATDT='E'  \n"
                + "AND CCIADT='CM'  \n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "15  \n"
                + "and substr(FDOCDT,1,5) <= " + meses + " \n"
                + "GROUP BY CUENDT  \n"
                + "order by CUENDT)  \n"
                + "on CUENMC = CUENDT)))))))  \n"
                + "on CUENMC = CUENMCr and nivemc=nivemcr\n"
                + "left join  \n"
                + "(select CUENMC as CUENMCd, (case when devengado is null then 0 else devengado end)as devengado ,nivemc as nivemcd  \n"
                + "from(select CUENMC,CEDTMC,nivemc,  \n"
                + "(case when nivemc = 1 then inicial1  \n"
                + "when nivemc = 2 then inicial2  \n"
                + "when nivemc = 3 then inicial4  \n"
                + "when nivemc = 4 then inicial6  \n"
                + "when nivemc = 5 then inicial8  \n"
                + "when nivemc = 6 then devengado end)  \n"
                + "as devengado  \n"
                + "from (  \n"
                + "select CUENMC,CEDTMC,NIVEMC,devengado,inicial8,inicial6,inicial4,inicial2  \n"
                + ",sum(devengado) over (PARTITION BY substr(CUENMC,1,1) ORDER BY substr(CUENMC,1,1)) as inicial1  \n"
                + "from (select CUENMC,CEDTMC,NIVEMC,devengado,inicial8,inicial6,inicial4  \n"
                + ",sum(devengado) over (PARTITION BY substr(CUENMC,1,2) ORDER BY substr(CUENMC,1,2)) as inicial2  \n"
                + "from(select CUENMC,CEDTMC,NIVEMC,devengado,inicial8,inicial6  \n"
                + ",sum(devengado) over (PARTITION BY substr(CUENMC,1,4) ORDER BY substr(CUENMC,1,4)) as inicial4  \n"
                + "from (select CUENMC,CEDTMC,NIVEMC,devengado,inicial8  \n"
                + ",sum(devengado) over (PARTITION BY substr(CUENMC,1,6) ORDER BY substr(CUENMC,1,6)) as inicial6  \n"
                + "from (select CUENMC,CEDTMC,NIVEMC,devengado  \n"
                + ",sum(devengado) over (PARTITION BY substr(CUENMC,1,8) ORDER BY substr(CUENMC,1,8)) as inicial8  \n"
                + "from (select CUENMC,CEDTMC,NIVEMC  \n"
                + "from USFIMRU.TIGSA_GLM03  \n"
                + "where TIPLMC= 'P' and substr(CEDTMC,1,1) in (1,2,3))  \n"
                + "left join  \n"
                + "(select * from (  \n"
                + "select F01CTD,devengado from(  \n"
                + "SELECT CUENDT,sum(MONTDT)*-1 as devengado,CAUXDT  \n"
                + "FROM USFIMRU.TIGSA_GLB01  \n"
                + "WHERE STATDT='E'  \n"
                + "AND CCIADT='MR'  \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01  \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "" + mes + "\n"
                + "AND CUENDT IN (SELECT F01CTO FROM USFIMRU.ECEF01 WHERE F01A1D='2')  \n"
                + "AND CAUXDT IN (SELECT F01A1O FROM USFIMRU.ECEF01 WHERE F01A1D='2')  \n"
                + "group by CUENDT,CAUXDT  \n"
                + "union  \n"
                + "SELECT CUENDT,SUM(MONTDT)*-1 as devengado,AUAD01  \n"
                + "FROM USFIMRU.TIGSA_GLB01  \n"
                + "WHERE STATDT='E'  \n"
                + "AND CCIADT='MR'  \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01  \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "" + mes + "\n"
                + "AND CUENDT IN (SELECT F01CTO FROM USFIMRU.ECEF01 WHERE F01A1D='2')  \n"
                + "AND AUAD01 in (SELECT F01A2O FROM USFIMRU.ECEF01 WHERE F01A1D='2')  \n"
                + "AND TMOVDT='H'  \n"
                + "GROUP BY CUENDT,AUAD01)  \n"
                + "left join  \n"
                + "(SELECT F01CTO, (case when F01A1O is null then F01A2O else F01A1O end) as aux,F01CTD FROM USFIMRU.ECEF01 WHERE F01A1D='2')  \n"
                + "on F01CTO=CUENDT and aux =CAUXDT  \n"
                + "where F01CTD <> '3801010000')  \n"
                + "union  \n"
                + "select * from (  \n"
                + "select DISTINCT F01CTD,devengado from(  \n"
                + "select CUENDT,devengado from(  \n"
                + "SELECT CUENDT,sum(MONTDT)*-1 as devengado,CAUXDT  \n"
                + "FROM USFIMRU.TIGSA_GLB01  \n"
                + "WHERE STATDT='E'  \n"
                + "AND CCIADT='MR'  \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01  \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "" + mes + "\n"
                + "AND CUENDT IN (SELECT F01CTO FROM USFIMRU.ECEF01 WHERE F01A1D ='2')  \n"
                + "AND TMOVDT='H'  \n"
                + "group by CUENDT,CAUXDT  \n"
                + "order by CAUXDT)  \n"
                + "left join  \n"
                + "(SELECT F01CTO, (case when F01A1O is null then F01A2O else F01A1O end) as aux,F01CTD FROM USFIMRU.ECEF01 WHERE F01A1D='2')  \n"
                + "on F01CTO=CUENDT and aux =CAUXDT )  \n"
                + "inner join  \n"
                + "(SELECT F01CTO,F01CTD FROM USFIMRU.ECEF01 WHERE F01A1D ='2' and F01COO = 'H')  \n"
                + "on CUENDT = F01CTO\n"
                + "where F01CTD <> '3602010000'))  \n"
                + "on CUENMC= F01CTD)))))))  \n"
                + "on CUENMC = CUENMCd and nivemc=nivemcd  \n"
                + "left join  \n"
                + "(select CUENMC as CUENMCe, (case when ejecutado is null then 0 else ejecutado end)as ejecutado ,nivemc as nivemce  \n"
                + "from(select CUENMC,CEDTMC,nivemc,  \n"
                + "(case when nivemc = 1 then inicial1  \n"
                + "when nivemc = 2 then inicial2  \n"
                + "when nivemc = 3 then inicial4  \n"
                + "when nivemc = 4 then inicial6  \n"
                + "when nivemc = 5 then inicial8  \n"
                + "when nivemc = 6 then ejecutado end)  \n"
                + "as ejecutado  \n"
                + "from (  \n"
                + "select CUENMC,CEDTMC,NIVEMC,ejecutado,inicial8,inicial6,inicial4,inicial2  \n"
                + ",sum(ejecutado) over (PARTITION BY substr(CUENMC,1,1) ORDER BY substr(CUENMC,1,1)) as inicial1  \n"
                + "from (select CUENMC,CEDTMC,NIVEMC,ejecutado,inicial8,inicial6,inicial4  \n"
                + ",sum(ejecutado) over (PARTITION BY substr(CUENMC,1,2) ORDER BY substr(CUENMC,1,2)) as inicial2  \n"
                + "from(select CUENMC,CEDTMC,NIVEMC,ejecutado,inicial8,inicial6  \n"
                + ",sum(ejecutado) over (PARTITION BY substr(CUENMC,1,4) ORDER BY substr(CUENMC,1,4)) as inicial4  \n"
                + "from (select CUENMC,CEDTMC,NIVEMC,ejecutado,inicial8  \n"
                + ",sum(ejecutado) over (PARTITION BY substr(CUENMC,1,6) ORDER BY substr(CUENMC,1,6)) as inicial6  \n"
                + "from (select CUENMC,CEDTMC,NIVEMC,ejecutado  \n"
                + ",sum(ejecutado) over (PARTITION BY substr(CUENMC,1,8) ORDER BY substr(CUENMC,1,8)) as inicial8  \n"
                + "from (select CUENMC,CEDTMC,NIVEMC  \n"
                + "from USFIMRU.TIGSA_GLM03  \n"
                + "where TIPLMC= 'P' and substr(CEDTMC,1,1) in (1,2,3))  \n"
                + "left join  \n"
                + "(select F01CTD,sum(MONTDT)*-1 as ejecutado from( \n"
                + "SELECT CUENDT,MONTDT,CAUXDT \n"
                + "FROM USFIMRU.TIGSA_GLB01 \n"
                + "WHERE STATDT='E' \n"
                + "AND CCIADT='MR' \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01 \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "" + mes + "\n"
                + "AND CUENDT IN (SELECT F01CTO FROM USFIMRU.ECEF01 WHERE F01A1D='3') \n"
                + "AND CAUXDT in (SELECT F01A1O FROM USFIMRU.ECEF01 WHERE F01A1D='3') \n"
                + "AND TMOVDT='H') \n"
                + "left join \n"
                + "(SELECT F01CTO,F01A1O,F01CTD FROM USFIMRU.ECEF01 WHERE F01A1D='3') \n"
                + "on F01CTO=CUENDT and F01A1O =CAUXDT \n"
                + "group by F01CTD \n"
                + "having F01CTD <> '3801010000' and F01CTD <> '1801030000' \n"
                + "union \n"
                + "select F01CTD,sum(devengado) as ejecutado from ( \n"
                + "select DISTINCT F01CTD,devengado from( \n"
                + "select CUENDT,devengado from( \n"
                + "SELECT CUENDT,sum(MONTDT)*-1 as devengado,CAUXDT \n"
                + "FROM USFIMRU.TIGSA_GLB01 \n"
                + "WHERE STATDT='E' \n"
                + "AND CCIADT='MR' \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01 \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "" + mes + "\n"
                + "AND CUENDT IN (SELECT F01CTO FROM USFIMRU.ECEF01 WHERE F01A1D ='3') \n"
                + "AND TMOVDT='H' \n"
                + "group by CUENDT,CAUXDT \n"
                + "order by CAUXDT) \n"
                + "left join \n"
                + "(SELECT F01CTO, (case when F01A1O is null then F01A2O else F01A1O end) as aux,F01CTD FROM USFIMRU.ECEF01  WHERE F01A1D='3') \n"
                + "on F01CTO=CUENDT and aux =CAUXDT) \n"
                + "inner join \n"
                + "(SELECT F01CTO,F01CTD FROM USFIMRU.ECEF01 WHERE F01A1D ='3' and F01COO = 'H') \n"
                + "on CUENDT = F01CTO) \n"
                + "where F01CTD = '3801010000' \n"
                + "group by F01CTD \n"
                + "union \n"
                + "select F01CTD,sum(MONTDT)*-1 as ejecutado from ( \n"
                + "SELECT CUENDT,MONTDT,AUAD01 \n"
                + "FROM USFIMRU.TIGSA_GLB01 \n"
                + "WHERE STATDT='E' \n"
                + "AND CCIADT='MR' \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01 \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "" + mes + "\n"
                + "AND CUENDT IN (SELECT F01CTO FROM USFIMRU.ECEF01 WHERE F01A1D='2') \n"
                + "AND AUAD01 IN (SELECT F01A2O FROM USFIMRU.ECEF01 WHERE F01A1D='2') \n"
                + "AND TMOVDT = 'H') \n"
                + "left join \n"
                + "(SELECT F01CTO,F01A2O,F01CTD FROM USFIMRU.ECEF01 WHERE F01A1D='2') \n"
                + "on F01CTO=CUENDT and F01A2O =AUAD01 \n"
                + "group by F01CTD)  \n"
                + "on CUENMC= F01CTD)))))))  \n"
                + "on CUENMC = CUENMCe and nivemc=nivemce\n"
                + "where nivemc between 4 and 4  \n"
                + "order by CUENMC");
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }
//

    public TablaGenerica getGastoClasificacion() {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSQL);
        tabFuncionario.setSql("select 1 as ani, 'G' as tipo, cuenta,substring(cuenta,1,2) as gr,substring(cuenta,3,2) as sub,substring(cuenta,5,2) as item ,  \n"
                + " funcion,orientacion,inicial,reforma,codificado,compromiso,devengado,ejecutado  \n"
                + " ,(codificado-devengado) as saldo  \n"
                + " ,(codificado- compromiso) as saldoCo  \n"
                + " ,(compromiso - devengado) as saldoDe  \n"
                + " from (select substring(cuenta,1,6) as cuenta,  \n"
                + "funcion,  \n"
                + "orientacion,  \n"
                + "sum(inicial) as inicial,  \n"
                + "sum(reforma) as reforma,  \n"
                + "sum(codificado) as codificado,  \n"
                + "sum(compromiso) as compromiso,  \n"
                + "sum(devengado) as devengado,  \n"
                + "sum(ejecutado) as ejecutado  \n"
                + "from (select cuenta,funcion,orientacion,\n"
                + "sum(inicial) as inicial,\n"
                + "sum(reforma) as reforma,\n"
                + "sum(codificado) as codificado,  \n"
                + "sum(compromiso) as compromiso,  \n"
                + "sum(devengado) as devengado,  \n"
                + "sum(ejecutado) as ejecutado\n"
                + "from (\n"
                + "SELECT p.cuenta,g.funcion,g.orientacion,p.tipo,p.num_proyecto,  \n"
                + "p.auxiliar,p.programa,p.inicial,p.reforma,p.codificado,  \n"
                + "p.compromiso,  \n"
                + "p.devengado,  \n"
                + "p.ejecutado  \n"
                + "FROM dbo.clas_orien_proyecto p  \n"
                + "left join dbo.clas_orien_gasto_proyecto g on p.cuenta = g.cuenta  \n"
                + "and p.tipo = g.tipo  \n"
                + "and p.programa = g.programa \n"
                + "where  p.tipo = 'CC' \n"
                + "union  \n"
                + "SELECT p.cuenta,g.funcion,g.orientacion,p.tipo,p.num_proyecto,  \n"
                + "p.auxiliar,p.programa,p.inicial,p.reforma,p.codificado,  \n"
                + "p.compromiso,  \n"
                + "p.devengado,  \n"
                + "p.ejecutado  \n"
                + "FROM dbo.clas_orien_proyecto p  \n"
                + "left join dbo.clas_orien_gasto_proyecto g on p.cuenta = g.cuenta  \n"
                + "and p.tipo = g.tipo  \n"
                + "and p.programa = g.programa \n"
                + "and p.num_proyecto = g.num_proyecto  \n"
                + "and p.auxiliar = g.auxiliar  \n"
                + "where  p.tipo = 'PY') as a\n"
                + "group by cuenta,funcion,orientacion) as a  \n"
                + "group by substring(cuenta,1,6),  \n"
                + "funcion,  \n"
                + "orientacion)as a");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    //sentencia de conexion a base de datos
    private void conSql() {
        if (conSQL == null) {
            conSQL = new Conexion();
            conSQL.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        }
    }

    private void desSql() {
        if (conSQL != null) {
            conSQL.desconectar(true);
            conSQL = null;
        }
    }

    private void conPostgresql() {
        if (conPostgres == null) {
            conPostgres = new Conexion();
            conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        }
    }

    private void desPostgresql() {
        if (conPostgres != null) {
            conPostgres.desconectar(true);
            conPostgres = null;
        }
    }

    private void conOraclesql() {
        if (conOracle == null) {
            conOracle = new Conexion();
            conOracle.setUnidad_persistencia(utilitario.getPropiedad("oraclejdbc"));
        }
    }

    private void desOraclesql() {
        if (conOracle != null) {
            conOracle.desconectar(true);
            conOracle = null;
        }
    }
}

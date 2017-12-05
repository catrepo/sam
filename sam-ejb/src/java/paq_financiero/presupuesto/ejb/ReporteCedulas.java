/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.presupuesto.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
@Stateless
public class ReporteCedulas {

    private Conexion conOracle, conSQL;
    private Utilitario utilitario = new Utilitario();

    public ReporteCedulas() {
    }

    public TablaGenerica getDatosFirmas(String cadena) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSQL);
        tabFuncionario.setSql("SELECT REPT_NOMBRE,REPT_RESPON,REPT_TIPO,REPT_FECHA\n"
                + "FROM SIS_AUXREPOR\n"
                + "WHERE REPT_NOMBRE like '" + cadena + "'");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public void setGastoClasificacion() {
        String strSqlr = "delete from clas_orien_proyecto";
        conSql();
        conSQL.ejecutarSql(strSqlr);
        desSql();
    }

    public TablaGenerica getDatosIngreso(String cuenta) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select CUENMC,CEDTMC,NOLAAD\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'R' and substr(CEDTMC,1,1) in (1,2,3) and NIVEMC=6\n"
                + "and CUENMC = '" + cuenta + "'");
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getDatosPrograma(Integer cuenta) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select * from (\n"
                + "SELECT CAUXMA,NOMBMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'and ZONAMA like '_22%'\n"
                + "union\n"
                + "select ZONAZ1,NOMBZ1 from USFIMRU.TIGSA_GLM11 where CLASZ1 = '002')\n"
                + "where CAUXMA ='" + cuenta + "'");
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getDatosPartida(String cuenta, Integer inicial, Integer reforma, String programa) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select *  \n"
                + "from (select DISTINCT substr(CUENMC,1,10) as CUENMC,CEDTMC,NOLAAD   \n"
                + "from USFIMRU.TIGSA_GLM03   \n"
                + "where TIPLMC= 'R' and NIVEMC between 1 and 6 and substr(CEDTMC,1,1) in (5,6,7,8,9)   \n"
                + "and LENGTH( substr(CUENMC,1,10))=10) \n"
                + "left join \n"
                + "(SELECT CUENDT,AUAD02   \n"
                + "FROM USFIMRU.TIGSA_GLB01  \n"
                + "where STATDT='E'  \n"
                + "AND CCIADT <> 'CM' and CCIADT <> 'MR' \n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + reforma + "\n"
                + "and AUAD02 is not null) \n"
                + "on CUENMC = CUENDT \n"
                + "where AUAD02 =" + programa + " and cuenmc = '" + cuenta + "'");
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public String maxRegistro() {
        conSql();
        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSQL);
        tabFuncionario.setSql("select 0 as id ,(case when max(id_codigo) is null then 0 else max(id_codigo) end) AS maximo from clas_orien_gasto_proyecto");
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("maximo") + 1;
        desSql();
        return ValorMax;
    }

    public TablaGenerica getVerificaParametro(String cuenta, String tipo, String proyecto, String auxi, String programa) {
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

    public void setDatosParametrosGastos(Integer codigo, String cuenta, String tipo, String num_proyecto, String auxiliar, String nom_proyecto, String direccion, String programa, Integer anio, String orientacion) {
        String strSqlr = "insert into clas_orien_gasto_proyecto (id_codigo,cuenta,tipo,num_proyecto,auxiliar,nom_proyecto,direccion,programa,anio,orientacion)\n"
                + "values(" + codigo + "," + cuenta + ",'" + tipo + "','" + num_proyecto + "','" + auxiliar + "','" + nom_proyecto + "','" + direccion + "'," + programa + "," + anio + ",'" + orientacion + "')";
        conSql();
        conSQL.ejecutarSql(strSqlr);
        desSql();
    }

    public TablaGenerica getDatosCedulasClasificador(Integer inicial, Integer reforma, Integer fin) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select ROWNUM as numero,CUENDT,(case when TAAD01 is null then 'CC' else TAAD01 end)TAAD01,substr(AUAD01,3,6)AUAD01,CCIADT,codificado,dep ,AUAD02 \n"
                + " ,(select DISTINCT NOMBMA from USFIMRU.TIGSA_GLm05 where CAUXMA = AUAD01 and POSTMA=AUAD02)as proyecto \n"
                + ",orientacion\n"
                + " from (select CUENDT,codificado,AUAD02,AUAD01,CCIADT,TAAD01,orientacion\n"
                + "from(\n"
                + "select CUENDT,codificado,AUAD02,AUAD01,CCIADT,TAAD01\n"
                + ",(SELECT DISTINCT DESCAD FROM USFIMRU.TIGSA_GLB01 WHERE TIPLDT='R' AND SAPRDT=11614 AND CUENDT=a.CUENDT AND AUAD02=a.AUAD02 and AUAD01 = a.AUAD01) as orientacion\n"
                + "from(select CUENDT,sum(MONTDT) as codificado,AUAD02,AUAD01,CCIADT,TAAD01 \n"
                + "from USFIMRU.TIGSA_GLB01 \n"
                + "where STATDT='E' \n"
                + "AND CCIADT <> 'CM' and CCIADT <> 'MR' \n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + reforma + "\n"
                + "and AUAD02 is not null \n"
                + "and substr(FDOCDT,1,5) <= " + fin + "\n"
                + "and TAAD01 = 'PY'\n"
                + "group by CUENDT,AUAD02,AUAD01,CCIADT,TAAD01) a\n"
                + "union\n"
                + "select CUENDT,codificado,AUAD02,AUAD01,CCIADT,TAAD01\n"
                + ",(SELECT DISTINCT DESCAD FROM USFIMRU.TIGSA_GLB01 WHERE TIPLDT='R' AND SAPRDT=11614 AND CUENDT=a.CUENDT AND AUAD02=a.AUAD02)  as orientacion\n"
                + "from(select CUENDT,sum(MONTDT) as codificado,AUAD02,AUAD01,CCIADT,TAAD01 \n"
                + "from USFIMRU.TIGSA_GLB01 \n"
                + "where STATDT='E' \n"
                + "AND CCIADT <> 'CM' and CCIADT <> 'MR' \n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + reforma + "\n"
                + "and AUAD02 is not null \n"
                + "and substr(FDOCDT,1,5) <= " + fin + "\n"
                + "and TAAD01 is null\n"
                + "group by CUENDT,AUAD02,AUAD01,CCIADT,TAAD01) a)) \n"
                + " left join \n"
                + " (SELECT CAUXMA,postma \n"
                + " ,(select NOMBZ1 from USFIMRU.TIGSA_GLM11 where CLASZ1 = '001' and ZONAZ1 = SUBSTR(ZONAMA, 0, 2)) as dep  \n"
                + " FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')  \n"
                + " on AUAD01 = CAUXMA and  AUAD02=postma");
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getCedulaGastos(String mes, Integer meses, String anio) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select ROWNUM as numero,CUENDT,TAAD01,substr(AUAD01, 3, 5)AUAD01,dep,CCIADT,AUAD02,inicial,reforma,codificado,compromiso,devengado,ejecutado \n"
                + " from(select CUENDT,TAAD01,AUAD01,CCIADT,codificado,dep ,AUAD02,proyecto \n"
                + ",(case when inicial is null then 0 else inicial end)inicial,(case when reforma is null then 0 else reforma end)reforma \n"
                + ",(case when compromiso is null then 0 else compromiso end)compromiso \n"
                + ",(case when devengado is null then 0 else devengado end)devengado\n"
                + ",(case when ejecutado is null then 0 else ejecutado end)ejecutado \n"
                + "from (select CUENDT,TAAD01,AUAD01,CCIADT,codificado,dep ,AUAD02\n"
                + ",(select DISTINCT NOMBMA from USFIMRU.TIGSA_GLm05 where CAUXMA = AUAD01 and POSTMA=AUAD02)as proyecto  \n"
                + "from (select CUENDT,(case when sum(MONTDT) is null then 0 else sum(MONTDT) end ) as codificado,AUAD02,AUAD01,CCIADT,TAAD01\n"
                + "from USFIMRU.TIGSA_GLB01  \n"
                + "where STATDT='E'  \n"
                + "AND CCIADT <> 'CM' and CCIADT <> 'MR'  \n"
                + "AND SAPRDT>=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "14  \n"
                + "AND SAPRDT<=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "15 \n"
                + "and TAAD01 ='PY' \n"
                + "AND TMOVDT='D'\n"
                + "and AUAD02 is not null  \n"
                + "and substr(FDOCDT,1,5) <= " + meses + "\n"
                + "group by CUENDT,AUAD02,AUAD01,CCIADT,TAAD01) \n"
                + "left join  \n"
                + "(SELECT CAUXMA,postma  \n"
                + ",(select NOMBZ1 from USFIMRU.TIGSA_GLM11 where CLASZ1 = '001' and ZONAZ1 = SUBSTR(ZONAMA, 0, 2)) as dep   \n"
                + "FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY') \n"
                + "on AUAD01 = CAUXMA and  AUAD02=postma)\n"
                + "left join \n"
                + "(select CUENDT as CUENDTI,(case when MONTDT is null then 0 else MONTDT end )  as inicial,AUAD02 as auxi,AUAD01 as auxi1,CCIADT as auxi2,TAAD01 as auxi4,TICODT as auxi3\n"
                + "from USFIMRU.TIGSA_GLB01 \n"
                + "where STATDT='E' \n"
                + "AND CCIADT <> 'CM' and CCIADT <> 'MR' \n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "14 \n"
                + "and TAAD01 ='PY' \n"
                + "AND TMOVDT='D'\n"
                + "and AUAD02 is not null)\n"
                + "on CUENDT = CUENDTI and AUAD01 = auxi1 and CCIADT=auxi2 and AUAD02=auxi\n"
                + "left join \n"
                + "(select CUENDT as CUENDTR,(case when MONTDT is null then 0 else MONTDT end ) as reforma,AUAD02 as auxr,AUAD01 as auxr1,CCIADT as auxr2 ,TICODT as auxr3\n"
                + "from USFIMRU.TIGSA_GLB01 \n"
                + "where STATDT='E' \n"
                + "AND CCIADT <> 'CM' and CCIADT <> 'MR' \n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "15 \n"
                + "and TAAD01 ='PY' \n"
                + "AND TMOVDT='D'\n"
                + "and AUAD02 is not null \n"
                + "and substr(FDOCDT,1,5) <= " + meses + ")\n"
                + "on CUENDT = CUENDTR and AUAD01 = auxr1 and CCIADT=auxr2 and AUAD02=auxr\n"
                + "left join \n"
                + "(select CUENDT as CUENDTC,(case when sum(MONTDT) is null then 0 else sum(MONTDT) end ) as compromiso\n"
                + ",AUAD02 as auxc,AUAD01 as auxc1,CCIADT as auxc2\n"
                + "from USFIMRU.TIGSA_GLB01 \n"
                + "where STATDT='E' \n"
                + "AND CCIADT <> 'CM' and CCIADT <> 'MR' \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01 \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + mes + "\n"
                + "AND CAUXDT='1' \n"
                + "AND TMOVDT='D' \n"
                + "AND TAAD01 ='PY' \n"
                + "group by CUENDT,AUAD02,AUAD01,CCIADT)\n"
                + "on CUENDT = CUENDTC and AUAD01 = auxc1 and CCIADT=auxc2 and AUAD02=auxc\n"
                + " left join  \n"
                + " (select CUENDT as CUENDTD,devengado,AUAD02 as auxd, \n"
                + " (case when (max(ROUND(ejecutado,2))over (PARTITION BY substr(CUENDT,1,2) ORDER BY substr(CUENDT,1,2))) = ejecutado \n"
                + " then (totalee+(prorateo-totale)) else ejecutado end) as ejecutado \n"
                + " ,AUAD01 as auxd1,CCIADT as auxd2 \n"
                + " from ( \n"
                + " select CUENDT,devengado,AUAD02,ROUND(ejecutado,2) as ejecutado,prorateo,AUAD01,CCIADT \n"
                + " ,(sum(ROUND(ejecutado,2))over (PARTITION BY substr(CUENDT,1,2) ORDER BY substr(CUENDT,1,2))) as totale \n"
                + " ,(max(ROUND(ejecutado,2))over (PARTITION BY substr(CUENDT,1,2) ORDER BY substr(CUENDT,1,2))) as totalee \n"
                + " from (select CUENDT,devengado,AUAD02,((case when prorateo = 0 then 0 else (prorateo/total)end) *devengado) as ejecutado,prorateo,AUAD01,CCIADT \n"
                + " from (select CUENDT,devengado,AUAD02,substr(CUENDT,1,2) as pro \n"
                + " ,(sum(devengado)over (PARTITION BY substr(CUENDT,1,2) ORDER BY substr(CUENDT,1,2))) as total \n"
                + " ,AUAD01,CCIADT \n"
                + " from(select CUENDT,(case when sum(MONTDT) is null then 0 else sum(MONTDT) end ) as devengado,AUAD02,AUAD01,CCIADT \n"
                + " from USFIMRU.TIGSA_GLB01 \n"
                + " where STATDT='E' \n"
                + " AND CCIADT <> 'CM' and CCIADT <> 'MR' \n"
                + " AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01  \n"
                + " AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + mes + " \n"
                + " AND CAUXDT='2' \n"
                + " AND TMOVDT='D' \n"
                + " and TAAD01 ='PY' \n"
                + " group by CUENDT,AUAD02,AUAD01,CCIADT)) \n"
                + " left join \n"
                + " (select cuenta,prorateo,(case when substr(cuenta,4,2) = 98 then '97' else substr(cuenta,4,2) end) as cu \n"
                + " from(select substr(CUENDT,1,5) as cuenta,(case when sum(MONTDT) is null then 0 else  sum(MONTDT) end ) as prorateo \n"
                + " from USFIMRU.TIGSA_GLB01 \n"
                + " where STATDT='E' \n"
                + " AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01 \n"
                + " AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + mes + " \n"
                + " AND TMOVDT='D' \n"
                + " AND CCIADT = 'MR' \n"
                + " and CUENDT like '213%' \n"
                + " group by substr(CUENDT,1,5))) \n"
                + " on pro= cu))) \n"
                + " on CUENDT = CUENDTD and AUAD02 = auxd and AUAD01 = auxd1 and CCIADT = auxd2 \n"
                + "union \n"
                + "select CUENDT,(case when TAAD01 is null then 'CC' else TAAD01 end)TAAD01,null as AUAD01,null as CCIADT,codificado,null as dep\n"
                + ",AUAD02,null as proyecto \n"
                + " ,(case when inicial is null then 0 else inicial end)inicial \n"
                + " ,(case when reforma is null then 0 else reforma end)reforma \n"
                + " ,(case when compromiso is null then 0 else compromiso end)compromiso \n"
                + " ,(case when devengado is null then 0 else devengado end)devengado,(case when ejecutado is null then 0 else ejecutado end)ejecutado \n"
                + " from(select CUENDT,(case when sum(MONTDT) is null then 0 else sum(MONTDT) end ) as codificado,AUAD02,TAAD01 \n"
                + " from USFIMRU.TIGSA_GLB01  \n"
                + " where STATDT='E'  \n"
                + " AND CCIADT <> 'CM' and CCIADT <> 'MR'  \n"
                + " AND SAPRDT>=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "14  \n"
                + " AND SAPRDT<=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "15 \n"
                + " and TAAD01 is null \n"
                + " and AUAD02 is not null  \n"
                + " and substr(FDOCDT,1,5) <= " + meses + " \n"
                + " group by CUENDT,AUAD02,TAAD01) \n"
                + " left join \n"
                + " (select CUENDT as CUENDTI,(case when sum(MONTDT) is null then 0 else sum(MONTDT) end ) as inicial,AUAD02 as auxi \n"
                + " from USFIMRU.TIGSA_GLB01 \n"
                + " where STATDT='E' \n"
                + " AND CCIADT <> 'CM' and CCIADT <> 'MR' \n"
                + " AND SAPRDT=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "14 \n"
                + " and TAAD01 is null \n"
                + " and AUAD02 is not null \n"
                + " group by CUENDT,AUAD02) \n"
                + " on CUENDT = CUENDTI and AUAD02 = auxi \n"
                + " left join \n"
                + " (select CUENDT as CUENDTR,(case when sum(MONTDT) is null then 0 else sum(MONTDT) end ) as reforma,AUAD02 as auxr \n"
                + " from USFIMRU.TIGSA_GLB01 \n"
                + " where STATDT='E' \n"
                + " AND CCIADT <> 'CM' and CCIADT <> 'MR' \n"
                + " AND SAPRDT=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "15 \n"
                + "  and TAAD01 is null \n"
                + " and AUAD02 is not null \n"
                + " and substr(FDOCDT,1,5) <= " + meses + " \n"
                + " group by CUENDT,AUAD02) \n"
                + " on CUENDT = CUENDTR and AUAD02 = auxr \n"
                + " left join  \n"
                + " (select CUENDT as CUENDTC,(case when sum(MONTDT) is null then 0 else sum(MONTDT) end ) as compromiso,AUAD02 as auxc \n"
                + " from USFIMRU.TIGSA_GLB01 \n"
                + " where STATDT='E' \n"
                + " AND CCIADT <> 'CM' and CCIADT <> 'MR' \n"
                + " AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01 \n"
                + " AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + mes + " \n"
                + " AND CAUXDT='1' \n"
                + " AND TMOVDT='D' \n"
                + " and TAAD01 is null \n"
                + " group by CUENDT,AUAD02) \n"
                + " on CUENDT = CUENDTC and AUAD02 = auxc \n"
                + " left join \n"
                + " (select CUENDT as CUENDTD,devengado,AUAD02 as auxd, \n"
                + " (case when (max(ROUND(ejecutado,2))over (PARTITION BY substr(CUENDT,1,2) ORDER BY substr(CUENDT,1,2))) = ejecutado \n"
                + " then (totalee+(prorateo-totale)) else ejecutado end) as ejecutado \n"
                + " from ( \n"
                + " select CUENDT,devengado,AUAD02,ROUND(ejecutado,2) as ejecutado,prorateo \n"
                + " ,(sum(ROUND(ejecutado,2))over (PARTITION BY substr(CUENDT,1,2) ORDER BY substr(CUENDT,1,2))) as totale \n"
                + " ,(max(ROUND(ejecutado,2))over (PARTITION BY substr(CUENDT,1,2) ORDER BY substr(CUENDT,1,2))) as totalee \n"
                + " from (select CUENDT,devengado,AUAD02,((case when prorateo = 0 then 0 else (prorateo/total)end) *devengado) as ejecutado,prorateo \n"
                + " from (select CUENDT,devengado,AUAD02,substr(CUENDT,1,2) as pro \n"
                + " ,(sum(devengado)over (PARTITION BY substr(CUENDT,1,2) ORDER BY substr(CUENDT,1,2))) as total \n"
                + " from(select CUENDT,(case when sum(MONTDT) is null then 0 else  sum(MONTDT) end )  as devengado,AUAD02 \n"
                + " from USFIMRU.TIGSA_GLB01 \n"
                + " where STATDT='E' \n"
                + " AND CCIADT <> 'CM' and CCIADT <> 'MR' \n"
                + " AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01 \n"
                + " AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + mes + " \n"
                + " AND CAUXDT='2' \n"
                + " AND TMOVDT='D' \n"
                + " and TAAD01 is null \n"
                + " group by CUENDT,AUAD02)) \n"
                + " left join \n"
                + " (select cuenta,prorateo,(case when substr(cuenta,4,2) = 98 then '97' else substr(cuenta,4,2) end) as cu \n"
                + " from(select substr(CUENDT,1,5) as cuenta,(case when sum(MONTDT) is null then 0 else  sum(MONTDT) end ) as prorateo \n"
                + " from USFIMRU.TIGSA_GLB01 \n"
                + " where STATDT='E' \n"
                + " AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01 \n"
                + " AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + mes + "\n"
                + " AND TMOVDT='D' \n"
                + " AND CCIADT = 'MR' \n"
                + " and CUENDT like '213%' \n"
                + " group by substr(CUENDT,1,5))) \n"
                + " on pro= cu))) \n"
                + " on CUENDT = CUENDTD and AUAD02 = auxd)");
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
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

    public TablaGenerica getCedulaIngresos(String mes, Integer meses, String anio) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select CUENMC,SUBSTR(CUENMC, 1, 2) as gr,SUBSTR(CUENMC, 3, 2)as sub,SUBSTR(CUENMC, 5, 2)as item   \n"
                + ",to_char((case when inicial is null then 0 else inicial end), 'fm999999999.90')as inicial     \n"
                + ",to_char((case when reforma is null then 0 else reforma end), 'fm999999999.90')as reforma        \n"
                + ",to_char(devengado, 'fm999999999.90') as devengado     \n"
                + ",to_char(ejecutado, 'fm999999999.90') as ejecutado     \n"
                + ",to_char((case when (codificado) is null then 0 else (codificado)end), 'fm999999999.90') as codificado     \n"
                + ",to_char((case when (codificado - devengado) is null then 0 else(codificado - devengado)end), 'fm999999999.90') as saldo     \n"
                + ", 'I' as tipo  \n"
                + "from (\n"
                + "select substr(CUENMCC,1,6)CUENMC,sum((case when inicial is null then 0 else inicial end))inicial,sum((case when reforma is null then 0 else reforma end))reforma,\n"
                + "sum((case when codificado is null then 0 else codificado end))codificado,\n"
                + "sum((case when devengado is null then 0 else devengado end))devengado,\n"
                + "sum((case when ejecutado is null then 0 else ejecutado end))ejecutado\n"
                + "from(select CUENMC as CUENMCC ,nivemc,codificado\n"
                + "from (select CUENMC,nivemc\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'R' and substr(CEDTMC,1,1) in (1,2,3) and NIVEMC = 6)\n"
                + "left join\n"
                + "(SELECT CUENDT,SUM(MONTDT)*-1 as codificado\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT<>'MR'\n"
                + "AND SAPRDT>=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "14\n"
                + "AND SAPRDT<=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "15\n"
                + "and substr(FDOCDT,1,5) <= " + meses + "\n"
                + "GROUP BY CUENDT\n"
                + "order by CUENDT)\n"
                + "on CUENMC = CUENDT)\n"
                + "left join\n"
                + "(select CUENMC as CUENMCI,nivemc,inicial\n"
                + "from (select CUENMC,nivemc\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'R' and substr(CEDTMC,1,1) in (1,2,3) and NIVEMC = 6)\n"
                + "left join\n"
                + "(SELECT CUENDT,SUM(MONTDT)*-1 as inicial\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT<>'MR'\n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "14\n"
                + "GROUP BY CUENDT\n"
                + "HAVING SUM(MONTDT)<=0\n"
                + "order by CUENDT)\n"
                + "on CUENMC = CUENDT)\n"
                + "on CUENMCC = CUENMCI\n"
                + "left join\n"
                + "(select CUENMC as CUENMCR ,nivemc,reforma\n"
                + "from (select CUENMC,nivemc\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'R' and substr(CEDTMC,1,1) in (1,2,3) and NIVEMC = 6)\n"
                + "left join\n"
                + "(SELECT CUENDT,SUM(MONTDT)*-1 as reforma\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT<>'MR'\n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "15\n"
                + "and substr(FDOCDT,1,5) <= " + meses + "\n"
                + "GROUP BY CUENDT\n"
                + "order by CUENDT)\n"
                + "on CUENMC = CUENDT)\n"
                + "on CUENMCC = CUENMCR\n"
                + "left join\n"
                + "(select CUENMC as CUENMCD,NIVEMC,devengado\n"
                + "from (select CUENMC,NIVEMC\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'R' and substr(CEDTMC,1,1) in (1,2,3) and NIVEMC = 6)\n"
                + "left join\n"
                + "(select F01CTD,devengado\n"
                + "from(select CUENDT,inicial\n"
                + "from (select CUENMC,nivemc\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'R' and substr(CEDTMC,1,1) in (1,2,3) and NIVEMC = 6)\n"
                + "left join\n"
                + "(SELECT CUENDT,SUM(MONTDT)*-1 as inicial\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT<>'MR'\n"
                + "AND SAPRDT>=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "14\n"
                + "AND SAPRDT<=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "15\n"
                + "and substr(FDOCDT,1,5) <= " + meses + "\n"
                + "GROUP BY CUENDT\n"
                + "order by CUENDT)\n"
                + "on CUENMC = CUENDT)\n"
                + "inner join\n"
                + "(select F01CTD,devengado\n"
                + "from (SELECT  DISTINCT a.F01CTD,sum(MONTDT*-1) as devengado\n"
                + "FROM USFIMRU.TIGSA_GLB01,\n"
                + "(SELECT  F01CIO\n"
                + ",(case when (case when F01A1O is null then F01A2O else F01A1O end) is null then '0'\n"
                + " else  (case when F01A1O is null then F01A2O else F01A1O end) end) as F01A1O\n"
                + ",F01CTD,F01CTO\n"
                + " FROM USFIMRU.ECEF01\n"
                + "WHERE F01A1D='2') a\n"
                + "WHERE STATDT='E'\n"
                + "AND CCIADT=a.F01CIO\n"
                + "AND CUENDT = a.F01CTO\n"
                + "AND (case when CAUXDT is null then '0' else CAUXDT end)  =a.F01A1O\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + mes + "\n"
                + "AND TMOVDT='H'\n"
                + "group by F01CTD)\n"
                + "where F01CTD <> '3801010000'\n"
                + "union\n"
                + "SELECT  DISTINCT a.F01CTD,sum(MONTDT*-1) as devengado\n"
                + "FROM USFIMRU.TIGSA_GLB01,\n"
                + "(SELECT  F01CIO\n"
                + ",(case when (case when F01A1O is null then F01A2O else F01A1O end) is null then '0'\n"
                + " else  (case when F01A1O is null then F01A2O else F01A1O end) end) as F01A1O\n"
                + ",F01CTD,F01CTO\n"
                + " FROM USFIMRU.ECEF01\n"
                + "WHERE F01A1D='2') a\n"
                + "WHERE STATDT='E'\n"
                + "AND CCIADT=a.F01CIO\n"
                + "AND CUENDT = a.F01CTO\n"
                + "AND (case when AUAD01 is null then '0' else AUAD01 end)  = a.F01A1O\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + mes + "\n"
                + "AND TMOVDT='H'\n"
                + "group by F01CTD\n"
                + "union\n"
                + "SELECT  DISTINCT a.F01CTD,sum(MONTDT*-1) as devengado\n"
                + "FROM USFIMRU.TIGSA_GLB01,\n"
                + "(SELECT  DISTINCT F01CIO\n"
                + ",F01CTD,F01CTO\n"
                + " FROM USFIMRU.ECEF01\n"
                + "WHERE F01A1D='2' and F01COO = 'H') a\n"
                + "WHERE STATDT='E'\n"
                + "AND CCIADT=a.F01CIO\n"
                + "AND CUENDT = a.F01CTO\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + mes + "\n"
                + "AND TMOVDT='H'\n"
                + "group by F01CTD)\n"
                + "on CUENDT = F01CTD)\n"
                + "on CUENMC= F01CTD)\n"
                + "on CUENMCC = CUENMCD\n"
                + "left join\n"
                + "(\n"
                + "select CUENMC as CUENMCE,NIVEMC,ejecutado\n"
                + "from (select CUENMC,NIVEMC\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'R' and substr(CEDTMC,1,1) in (1,2,3) and NIVEMC = 6)\n"
                + "left join\n"
                + "(select F01CTD,ejecutado\n"
                + "from(select CUENDT,inicial\n"
                + "from (select CUENMC,nivemc\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'R' and substr(CEDTMC,1,1) in (1,2,3) and NIVEMC = 6)\n"
                + "left join\n"
                + "(SELECT CUENDT,SUM(MONTDT)*-1 as inicial\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT<>'MR'\n"
                + "AND SAPRDT>=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "14\n"
                + "AND SAPRDT<=1" + (Integer.parseInt(String.valueOf(anio).substring(2, 4)) - 1) + "15\n"
                + "and substr(FDOCDT,1,5) <= " + meses + "\n"
                + "GROUP BY CUENDT\n"
                + "order by CUENDT)\n"
                + "on CUENMC = CUENDT)\n"
                + "inner join\n"
                + "(select F01CTD,devengado as ejecutado\n"
                + "from (SELECT  DISTINCT a.F01CTD,sum(MONTDT*-1) as devengado\n"
                + "FROM USFIMRU.TIGSA_GLB01,\n"
                + "(SELECT  F01CIO\n"
                + ",(case when (case when F01A1O is null then F01A2O else F01A1O end) is null then '0'\n"
                + " else  (case when F01A1O is null then F01A2O else F01A1O end) end) as F01A1O\n"
                + ",F01CTD,F01CTO\n"
                + " FROM USFIMRU.ECEF01\n"
                + "WHERE F01A1D='3') a\n"
                + "WHERE STATDT='E'\n"
                + "AND CCIADT=a.F01CIO\n"
                + "AND CUENDT = a.F01CTO\n"
                + "AND (case when CAUXDT is null then '0' else CAUXDT end)  =a.F01A1O\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + mes + "\n"
                + "AND TMOVDT='H'\n"
                + "group by F01CTD)\n"
                + "where F01CTD <> '3801010000'\n"
                + "union\n"
                + "select F01CTD,devengado\n"
                + "from (SELECT  DISTINCT a.F01CTD,sum(MONTDT*-1) as devengado\n"
                + "FROM USFIMRU.TIGSA_GLB01,\n"
                + "(SELECT  DISTINCT F01CIO\n"
                + ",F01CTD,F01CTO\n"
                + " FROM USFIMRU.ECEF01\n"
                + "WHERE F01A1D='3' and F01COO = 'H') a\n"
                + "WHERE STATDT='E'\n"
                + "AND CCIADT=a.F01CIO\n"
                + "AND CUENDT = a.F01CTO\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(anio).substring(2, 4)) + mes + "\n"
                + "AND TMOVDT='H'\n"
                + "group by F01CTD)\n"
                + "where F01CTD = '3801010000' or F01CTD = '3602010000')\n"
                + "on CUENDT = F01CTD)\n"
                + "on CUENMC= F01CTD\n"
                + "order by  CUENMC)\n"
                + "on CUENMCC = CUENMCE\n"
                + "group by  substr(CUENMCC,1,6))"
                + "where (case when codificado = 0 then (case when reforma = 0 then (case when devengado = 0 then (case when ejecutado = 0 then 1 else 0 end) else 0 end ) else 0 end) else 0 end)!=1\n"
                + "order by CUENMC");
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public void setClasificadorGastos(Integer anio) {
        // Forma el sql para actualizacion
        String strSqlr = "update clas_orien_gasto_proyecto \n"
                + "set clas_orien_gasto_proyecto.funcion = h.funcion\n"
                + "from (select DISTINCT cuenta,tipo,auxiliar,programa,funcion,orientacion\n"
                + "from  clas_orien_gasto_proyecto \n"
                + "where anio=(" + anio + "-1)) as h\n"
                + "where clas_orien_gasto_proyecto.cuenta = h.cuenta and clas_orien_gasto_proyecto.tipo = h.tipo \n"
                + "and clas_orien_gasto_proyecto.auxiliar =h.auxiliar and clas_orien_gasto_proyecto.programa = h.programa\n"
                + "and clas_orien_gasto_proyecto.anio = " + anio;
        conSql();
        conSQL.ejecutarSql(strSqlr);
        desSql();
    }

    public TablaGenerica getGastoClasificacion(Integer anio) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSQL);
        tabFuncionario.setSql("EXEC SIGAG.dbo.PAT_GASTO_ARCHIVO_PLANO "
                + "@anio =" + anio + "");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public TablaGenerica getGastoClasificacion1(Integer anio) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSQL);
        tabFuncionario.setSql("SELECT ani,tipo,cuenta,gr,sub,item,funcion,orientacion,inicial,reforma,codificado,compromiso,devengado,ejecutado,saldo,saldoCo,saldoDe\n"
                + "from cedulasGastos_VT\n"
                + "where anio=" + anio);
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public TablaGenerica getDatosProyecto(Integer proyecto, Integer ani) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select AUAD01,AUAD01 as num_proyec,AUAD02,NDOCDC ,proyecto,CUENDT\n"
                + "from (select CUENDT,AUAD01,AUAD02,proyecto\n"
                + " ,LISTAGG(NDOCDC, ',') WITHIN GROUP (ORDER BY AUAD02) AS NDOCDC \n"
                + "from (select CUENDT,AUAD01,AUAD02,NDOCDC,proyecto\n"
                + "from (select CUENDT,AUAD01,AUAD02\n"
                + ",(select DISTINCT NOMBMA from USFIMRU.TIGSA_GLm05 where CAUXMA = AUAD01 and POSTMA=AUAD02 ) as proyecto\n"
                + "from USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT <> 'CM' and CCIADT <> 'MR'\n"
                + "AND SAPRDT>=1" + ani + "14\n"
                + "AND SAPRDT<=1" + ani + "15\n"
                + "AND TAAD01 = 'PY'\n"
                + "and AUAD02 is not null\n"
                + "and substr(FDOCDT,1,5) <= 1" + (ani + 1) + "12)\n"
                + "inner join \n"
                + "(select NDOCDC,CUENDC,MONTDC,AUA2DC from USFIMRU.PRCO01) \n"
                + "on CUENDT = CUENDC and AUAD02 = AUA2DC \n"
                + "order by auad01)\n"
                + "group by CUENDT,AUAD01,AUAD02,proyecto)\n"
                + "where AUAD01 ='" + proyecto + "'");
        tabFuncionario.ejecutarSql();
        desOraclesql();
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

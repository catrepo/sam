package paq_cementerio.ejb;

import framework.aplicacion.TablaGenerica;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
@Stateless
public class cementerio {

    private Conexion conSql;
    private Conexion conSqler;
    private Conexion conSqlapli;
    private Utilitario utilitario = new Utilitario();

    public TablaGenerica getMovimiento(Integer mov) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT e.IDE_CATASTRO as cod_catastro,IDE_DET_MOVIMIENTO as COD_RUTA,\n"
                + "NOMBRES_APELLIDOS_CMREP,\n"
                + "DOCUMENTO_IDENTIDAD_CMREP,\n"
                + "DIRECCION_CMREP,\n"
                + "TELEFONOS_CMREP,\n"
                + "CELULAR_CMREP,\n"
                + "EMAIL_CMREP,\n"
                + "CEDULA_FALLECIDO,\n"
                + "NOMBRES,\n"
                + "FECHA_NACIMIENTO,\n"
                + "FECHA_DEFUNCION,\n"
                + "F.DETALLE_LUGAR,VALOR,PERIODO,\n"
                + "SECTOR,\n"
                + "NUMERO,\n"
                + "E.MODULO,DETALLE_CMACC, 'DIRECCION REPRESENTANTE:'+ (CASE WHEN DIRECCION_CMREP IS NULL THEN '' ELSE DIRECCION_CMREP END) AS  domicilio,\n"
                //                + "'PAGO POR ARRENDAMIENTO DE UN '+DETALLE_LUGAR+' BLOQUE/SERIE:'+SERIE_BLOQUE+'   NIVEL/FILA:'+NUMERO_FILA+'   NUMERO:'+convert(varchar,NUMERO)+'  PARA LOS RESTOS DEL SR(A):'+NOMBRES+' POR:'+convert(varchar,PERIODO)+'AÑOS' AS ANTECEDENTES,DETALLE_LUGAR+' BLOQUE/SERIE:'+SERIE_BLOQUE+'  NIVEL/FILA:'+NUMERO_FILA+'   NUMERO:'+convert(varchar,NUMERO) AS GIRO_PRODUCTO,'140200' AS CODIGO_DIRECCION,'"+utilitario.getVariable("aniotitulo")+"' AS PERIODO_TITULO,'"+utilitario.getVariable("aniotitulo")+"' AS PERIODO_LIQUIDACION,'CMTR' AS COD_RUBRO\n"
                + "('PAGO POR '+(case when IDE_TIPO_MOVIMIENTO=1 then  'ARRENDAMIENTO' when IDE_TIPO_MOVIMIENTO=2 then 'RENOVACION'  "
                + "when IDE_TIPO_MOVIMIENTO=3 then 'EXHUMACION' else '' END)+ ' DE UN '  +(case when F.DETALLE_LUGAR='SITIO' THEN F.DETALLE_LUGAR+' EN EL CEMENTERIO MUNICIPAL '+' SECTOR:'+SECTOR+' MODULO:'+convert(varchar,E.MODULO)+' NUMERO:'+NUMERO when F.DETALLE_LUGAR='NICHO' THEN F.DETALLE_LUGAR+' EN EL CEMENTERIO MUNICIPAL '+' SECTOR:'+SECTOR+'   MODULO:'+convert(varchar,E.MODULO)+'  NUMERO:'+NUMERO when F.DETALLE_LUGAR='MAUSOLEO' THEN  'NUMERO:'+convert(varchar,E.NUMERO) else  '' END) +'. PARA LOS RESTOS DEL SR(A):'+NOMBRES+' POR '+convert(varchar,PERIODO_ARRENDAMIENTO)+' AÑOS. DESDE EL ' +convert(varchar, FECHA_DESDE)+' HASTA EL '+convert(varchar, FECHA_HASTA) )AS ANTECEDENTES,(case when F.DETALLE_LUGAR='SITIO' THEN F.DETALLE_LUGAR+' EN EL CEMENTERIO MUNICIPAL SECTOR:'+SECTOR+'  MODULO:'+convert(varchar,E.MODULO)+'  NUMERO:'+NUMERO when F.DETALLE_LUGAR='NICHO' THEN F.DETALLE_LUGAR+' EN EL CEMENTERIO MUNICIPAL SECTOR:'+SECTOR+'   MODULO:'+NUMERO+'  NUMERO:'+convert(varchar,E.MODULO) when F.DETALLE_LUGAR='MAUSOLEO' THEN  'EN EL CEMENTERIO MUNICIPAL NUMERO:'+convert(varchar,E.MODULO) else  '' END) AS GIRO_PRODUCTO,'190600' AS CODIGO_DIRECCION,'2016' AS PERIODO_TITULO,'2016' AS PERIODO_LIQUIDACION,'CMTR' AS COD_RUBRO \n"
                + "FROM\n"
                + "CMT_DETALLE_MOVIMIENTO A\n"
                + "INNER JOIN dbo.CMT_REPRESENTANTE B ON A.IDE_CMREP=B.IDE_CMREP\n"
                + "INNER JOIN dbo.CMT_FALLECIDO C ON C.IDE_FALLECIDO=A.IDE_FALLECIDO\n"
                + "INNER JOIN dbo.CMT_CATASTRO E ON E.IDE_CATASTRO =a.IDE_CATASTRO\n"
                + "INNER JOIN dbo.CMT_LUGAR F ON F.IDE_LUGAR = E.IDE_LUGAR\n"
                + "INNER JOIN CMT_ACCION G ON IDE_TIPO_MOVIMIENTO=G.IDE_CMACC"
                + "  where IDE_DET_MOVIMIENTO =" + mov);
//        tabPersona.imprimirSql();
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica getIdeDetalleMovimiento(String tabla) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT\n"
                + "IDE_BLOQ,\n"
                + "IDE_USUA,\n"
                + "TABLA_BLOQ,\n"
                + "MAXIMO_BLOQ,\n"
                + "USUARIO_BLOQ\n"
                + "from SIS_BLOQUEO\n"
                + "where TABLA_BLOQ = '" + tabla + "'");
//        tabPersona.imprimirSql();
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica getUsuarioInfor(String login) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT\n"
                + "USU_CEDULA,\n"
                + "USU_NOMBRE,\n"
                + "USU_LOGIN,\n"
                + "dep_nombre,\n"
                + "DEP_ABREVIATURA,\n"
                + "DEP_CODIGO\n"
                + "FROM\n"
                + "dbo.SEG_USUARIOS_VTA\n"
                + "where USU_LOGIN = '" + login + "'");
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica getInforCatastro(Integer lugar, String sector, String modulo, Integer ini, Integer fin) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT 0 as id,(CASE WHEN COUNT(IDE_LUGAR) is null then 0 else COUNT(IDE_LUGAR) end) as conteo\n"
                + "FROM dbo.CMT_CATASTRO\n"
                + "where IDE_LUGAR = " + lugar + " and SECTOR = " + sector + " and MODULO = '" + modulo + "' and NUMERO BETWEEN " + ini + " AND " + fin + "\n"
                + "and DISPONIBLE_OCUPADO = 'OCUPADO'");
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica getDatosAdicionales(Integer mov, String nombre, String fecha1, String fecha2, Integer anio, Integer catastro) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT\n"
                + "('PAGO POR '+\n"
                + "(case when " + mov + "=1 then  'ARRENDAMIENTO' when " + mov + "=2 then 'RENOVACION'  when " + mov + "=3 then 'EXHUMACION' else '' END)\n"
                + "+ ' DE UN '  +\n"
                + "(case when F.DETALLE_LUGAR='SITIO' THEN F.DETALLE_LUGAR+' SECTOR:'+SECTOR+' MODULO:'+convert(varchar,E.MODULO)+' NUMERO:'+NUMERO when F.DETALLE_LUGAR='NICHO' THEN F.DETALLE_LUGAR+' SECTOR:'+SECTOR+'   MODULO:'+convert(varchar,E.MODULO)+'  NUMERO:'+NUMERO when F.DETALLE_LUGAR='MAUSOLEO' THEN  'NUMERO:'+convert(varchar,E.NUMERO) else  '' END) \n"
                + "+'. PARA LOS RESTOS DEL SR(A): " + nombre + " POR '+convert(varchar," + anio + ")+' AÑOS. DESDE EL ' +convert(varchar, '" + fecha1 + "')+' HASTA EL '+convert(varchar, '" + fecha2 + "') ) AS ANTECEDENTES,\n"
                + "(case when F.DETALLE_LUGAR='SITIO' THEN F.DETALLE_LUGAR+' SECTOR:'+SECTOR+'  MODULO:'+convert(varchar,E.MODULO)+'  NUMERO:'+NUMERO \n"
                + "when F.DETALLE_LUGAR='NICHO' THEN F.DETALLE_LUGAR+' SECTOR:'+SECTOR+'   MODULO:'+convert(varchar,E.MODULO)+'  NUMERO:'+NUMERO \n"
                + "when F.DETALLE_LUGAR='MAUSOLEO' THEN  'NUMERO:'+convert(varchar,E.MODULO) else  '' END) AS GIRO_PRODUCTO\n"
                + "FROM dbo.CMT_CATASTRO AS E\n"
                + "INNER JOIN dbo.CMT_LUGAR AS F ON E.IDE_LUGAR = F.IDE_LUGAR "
                + "where F.IDE_LUGAR= (select IDE_LUGAR from CMT_CATASTRO  where IDE_CATASTRO = " + catastro + ") and E.IDE_CATASTRO =" + catastro);
        tabPersona.ejecutarSql();
//        tabPersona.imprimirSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica getLiquidacion(String mov) {
        conSqler();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSqler);
        tabPersona.setSql("select CODIGO_LIQUIDACION,PERIODO_LIQUIDACION,NUM_LIQUIDACION,COD_RUTA,CEDULA_RUC,\n"
                + "NOMBRE_RAZONSOCIAL,DOMICILIO,ANTECEDENTES,OBSERVACIONES,FECHA_LIQUIDACION,LOGIN_LIQUIDADOR,VALORTOTAL_LIQUIDACION,\n"
                + " EMITIDO,DIRECCION_REPRESENTANTE,REPRESENTANTE_LOCALCOMERCIAL,ACTIVIDAD_LOCALCOMERCIAL,\n"
                + "GIRO_PRODUCTO,COD_RUBRO,COD_CATASTRO,FECHA_DESDE,fecha_hasta,numDocumento,EMITIDO,ESTADO_LIQUIDACION, FECHA_DESDE,	FECHA_HASTA\n"
                + " from LIQUIDACIONES where ESTADO_LIQUIDACION = 'ANULADO' and "
                + "  num_liquidacion ='" + mov + "'");
//        tabPersona.imprimirSql();
        tabPersona.ejecutarSql();
        desConSqler();
        return tabPersona;
    }

    public TablaGenerica getLiquidacion1(String mov) {
        conSqler();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSqler);
        tabPersona.setSql("select * from ER.dbo.TITULO_CREDITO \n"
                + "where num_liquidacion ='" + mov + "' and PAGADO = 'NO' and estado_titulo NOT in ('anulado','baja')");
//        tabPersona.imprimirSql();
        tabPersona.ejecutarSql();
        desConSqler();
        return tabPersona;
    }

    public TablaGenerica getDatosFallecidos(String ide) {
        conSqlapli();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSqlapli);
        tabPersona.setSql("select a.id_fallecido,cedula_fallecido,nombre_fallecido,tipo_pago,fecha_defuncion,lugar_actual,cedula_representante,representante_actual,\n"
                + "	 fecha_ingreso,num_titulo,valor_pago,anio_inicio,anio_fin,anio_arrendamiento,id_control from FALLECIDOS a, CONTROL_ARRENDAMIENTO b\n"
                + "	where a.id_fallecido=b.id_fallecido\n"
                + "	AND  "
                + "  a.id_fallecido ='" + ide + "' ORDER BY a.id_fallecido ");
//        tabPersona.imprimirSql();
        tabPersona.ejecutarSql();
        desconSqlapli();
        return tabPersona;
    }

    public TablaGenerica getMaxdetalleli(String periodo, String titulo) {
        conSqler();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSqler);
        tabPersona.setSql("select 1 as codigo, (CASE WHEN MAX(IDE_DET_LIQ) IS NULL THEN 1 ELSE MAX(IDE_DET_LIQ)+1 END) as maximo from DETALLE_LIQUIDACION where periodo_liquidacion ='" + periodo + "' and periodo_titulo ='" + titulo + "'");
//        tabPersona.imprimirSql();
        tabPersona.ejecutarSql();
        desConSqler();
        return tabPersona;
    }

    public TablaGenerica getVerificaCuentas(String periodo) {
        conSqler();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSqler);
        tabPersona.setSql("select * from ER.dbo.cuenta where codigofiscal_cuenta = '" + periodo + "'");
        tabPersona.ejecutarSql();
        desConSqler();
        return tabPersona;
    }

    public TablaGenerica getMaxFallecidosIde() {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select 0 as id, (CASE WHEN max(ide_fallecido) IS NULL THEN 0 ELSE max(ide_fallecido) END) as maximo from cmt_fallecido ");
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public String getDetalleMaxLiqui(String periodo, String titulo) {
        conSqler();
        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSqler();
        tabFuncionario.setConexion(conSqler);
        tabFuncionario.setSql("select 1 as codigo, (CASE WHEN MAX(IDE_DET_LIQ) IS NULL THEN 1 ELSE MAX(IDE_DET_LIQ)+1 END) as maximo from DETALLE_LIQUIDACION where periodo_liquidacion ='" + periodo + "' and periodo_titulo ='" + titulo + "'");
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("maximo");
        desConSqler();
        return ValorMax;
    }

    public void setGuardaDetalle(String codigoliqui, String perliquidacion, String idedetalleliqui, String pertitulo, String codifiscal, String descuenta, String valorcuenta, String contado) {
        String strSqlr = "insert into DETALLE_LIQUIDACION (CODIGO_LIQUIDACION,PERIODO_LIQUIDACION,IDE_DET_LIQ,ID_DETALLELIQUIDACION,PERIODO_TITULO,CODIGOFISCAL_CUENTA,DSC_CUENTA,VALOR_CUENTA,CONTADO) values (\n"
                + codigoliqui + ",'" + perliquidacion + "'," + idedetalleliqui + ",1,'" + pertitulo + "','" + codifiscal + "','" + descuenta + "'," + valorcuenta + ",1)";
        conSqler();
        conSqler.ejecutarSql(strSqlr);
        desConSqler();
    }

    public void setGuardaDetalle1(String codigoliqui, String perliquidacion, String idedetalleliqui, String pertitulo, String codifiscal, String descuenta, String valorcuenta, String contado, String catastro, String ruta) {
        String strSqlr = "insert into DETALLE_LIQUIDACION (CODIGO_LIQUIDACION,PERIODO_LIQUIDACION,IDE_DET_LIQ,ID_DETALLELIQUIDACION,PERIODO_TITULO,CODIGOFISCAL_CUENTA,DSC_CUENTA,VALOR_CUENTA,CONTADO,PERIODO_EMISION_AAPP,PERIODO_COBRO_AAPP) values (\n"
                + codigoliqui + ",'" + perliquidacion + "'," + idedetalleliqui + ",1,'" + pertitulo + "','" + codifiscal + "','" + descuenta + "'," + valorcuenta + ",1,'" + catastro + "','" + ruta + "')";
        conSqler();
        conSqler.ejecutarSql(strSqlr);
        desConSqler();
    }

    public String guardaCheck(int codigo, String mov, String doc) {
        conSql();
        String mensaje = "Regsitro Guardado";
        String guardar = "INSERT INTO CMT_DETALLE_CHECK(ide_det_check,ide_det_movimiento,descripcion) VALUES('" + codigo + "','','" + doc + "')";
        System.out.println("envio a guardar " + guardar);
        conSql.ejecutarSql(guardar);
        desConSql();
        return mensaje;
    }

    public TablaGenerica getDatoCatastro(String detalle, String bloque, String fila, String numero) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select A.IDE_LUGAR,ide_catastro,DETALLE_LUGAR,SECTOR,NUMERO,MODULO,disponible_ocupado  "
                + "from CMT_CATASTRO a INNER JOIN CMT_LUGAR b on a.IDE_LUGAR=b.IDE_LUGAR "
                + "where A.IDE_LUGAR='" + detalle + "' and SECTOR='" + bloque + "' and NUMERO='" + fila + "' and MODULO='" + numero + "' ");
        tabFuncionario.ejecutarSql();
//        tabFuncionario.imprimirSql();
        desConSql();
        return tabFuncionario;
    }

    public TablaGenerica periodoCatastro(String catastro) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select detalle_lugar,sector,MODULO,numero ,periodo,valor,b.ide_lugar  \n"
                + "from CMT_CATASTRO a, CMT_LUGAR b where a.ide_lugar=b.ide_lugar and ide_catastro\n"
                + "=" + catastro);
        tabPersona.ejecutarSql();
//        tabPersona.imprimirSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica getListaFallecidos(Integer catastro) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select c.IDE_CATASTRO,f.NOMBRES,\n"
                + "(case when f.catastro_anterior is null then '()' else '(Catastro Anterior: '+f.catastro_anterior+')' end) as catastro_anterior \n"
                + "FROM dbo.CMT_FALLECIDO AS f\n"
                + "INNER JOIN dbo.CMT_CATASTRO AS c ON f.IDE_CATASTRO = c.IDE_CATASTRO\n"
                + "WHERE c.IDE_CATASTRO =" + catastro + " and isnull(tipo_pago,0)!=4");
        tabPersona.ejecutarSql();
//        tabPersona.imprimirSql();
        desConSql();
        return tabPersona;
    }

    public void set_updateValor(String codigo, String vale, String fecha, String num) {
        String auSql = "update CMT_DETALLE_MOVIMIENTO set NUM_LIQUIDACION='" + num + "', FECHA_INGRESA=CAST('" + codigo + "' AS datetime), \n"
                + "FECHA_DESDE=CAST('" + codigo + "' AS datetime), \n"
                + "FECHA_HASTA=cast('" + fecha + "' AS datetime) \n"
                + "where ide_det_movimiento =" + vale;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void set_updateHabilita(int codigo) {
        String auSql = "update cmt_catastro set catastro_habilita=1 where ide_catastro= " + codigo;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void setUpdateLiberaCatastro(int codigo, String login) {
        String auSql = "update CMT_CATASTRO\n"
                + "set DISPONIBLE_OCUPADO = 'DISPONIBLE',\n"
                + "ver = null,\n"
                + "TOTAL_INGRESA = (select count(*) from CMT_FALLECIDO where ide_catastro = " + codigo + "),\n"
                + "USUARIO_ACTUA='" + login + "',\n"
                + "FECHA_ACTUA= GETDATE(),\n"
                + "HORA_ACTUA= CONVERT (time, GETDATE())\n"
                + "where ide_catastro =" + codigo;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void setUpdatePuestoHabilita(int codigo, String sector, String modulo, String numero, String login) {
        String auSql = "update CMT_CATASTRO\n"
                + "set TOTAL_INGRESA = (select count(*) from CMT_FALLECIDO where ide_lugar= " + codigo + " and sector=" + sector + " and modulo='" + modulo + "' and numero =" + numero + "),\n"
                + "USUARIO_ACTUA='" + login + "',\n"
                + "DISPONIBLE_OCUPADO='OCUPADO',\n"
                + "ver=1,\n"
                + "FECHA_ACTUA= GETDATE(),\n"
                + "HORA_ACTUA= CONVERT (time, GETDATE())\n"
                + "where ide_lugar= " + codigo + " and sector=" + sector + " and modulo='" + modulo + "' and numero = " + numero;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void set_updateDesHabilita(int codigo) {
        String auSql = "update cmt_catastro set catastro_habilita=0  where ide_catastro= " + codigo;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void setUpdateDesHabilita(String codigo, String sector, String modulo, String numero) {
        String auSql = "update cmt_catastro set catastro_habilita=0  where ide_catastro= (SELECT IDE_CATASTRO\n"
                + "from CMT_CATASTRO\n"
                + "where IDE_LUGAR =(select IDE_LUGAR from CMT_LUGAR  where DETALLE_LUGAR = '" + codigo + "') and SECTOR ='" + sector + "' and MODULO ='" + modulo + "' and NUMERO = '" + numero + "')";
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void setUpdateDetalleMov(Integer codigo, Integer ide) {
        String auSql = "update CMT_REPRESENTANTE set orden_representante=(select max(orden_representante)+1 from CMT_REPRESENTANTE where IDE_FALLECIDO = " + ide + ") \n"
                + "where  ide_cmrep=" + codigo;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void set_updateDetalleMovCheck(String codigo) {
        String auSql = "update CMT_DETALLE_CHECK set ide_det_movimiento='" + codigo + "' \n"
                + "where  ide_det_movimiento=0";
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void set_updateNumLiq(String id, String num, String codigo, String perl, String perti) {
        String auSql = "update CMT_DETALLE_MOVIMIENTO set CODIGO_LIQUIDACION=" + codigo + ", NUM_LIQUIDACION=" + num + ",PERIODO_LIQUIDACION=" + perl + ",PERIODO_TITULO=" + perti + "  where ide_det_movimiento =" + id;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void setRepresentante(Integer codigo, String nom, String cedula, String dir, String fono, String cel, String mail, String login) {
        String auSql = "update CMT_REPRESENTANTE\n"
                + "set NOMBRES_APELLIDOS_CMREP ='" + nom + "',\n"
                + "DOCUMENTO_IDENTIDAD_CMREP='" + cedula + "',\n"
                + "DIRECCION_CMREP='" + dir + "',\n"
                + "TELEFONOS_CMREP='" + fono + "',\n"
                + "CELULAR_CMREP='" + cel + "',\n"
                + "EMAIL_CMREP='" + mail + "',\n"
                + "USUARIO_ACTUA='" + login + "',\n"
                + "FECHA_ACTUA = GETDATE(),\n"
                + "HORA_ACTUA = CONVERT (time, SYSDATETIME())\n"
                + "where IDE_CMREP = " + codigo;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void set_updateAntecedentes(String ante, String plazo, String liq) {
        String auSql = "update liquidaciones set antecedentes='" + ante + "'+'  DESDE EL   '+'" + plazo + "' \n"
                + "where NUM_LIQUIDACION ='" + liq + "'";
        conSqler();
        conSqler.ejecutarSql(auSql);
        conSqler.isImprimirSqlConsola();
        desConSqler();
    }

    public void setUpdateRuta(String ruta, String periodo, String cedula, String liq) {
        String auSql = "update liquidaciones set cod_ruta='" + ruta + "' \n"
                + "where NUM_LIQUIDACION ='" + liq + "' and PERIODO_LIQUIDACION ='" + periodo + "' and CEDULA_RUC ='" + cedula + "'";
        conSqler();
        conSqler.ejecutarSql(auSql);
        desConSqler();
    }

    public void set_updateAntecedentes1(String liq) {
        String auSql = "update liquidaciones set COD_RUBRO='CMTR'    \n"
                + "where NUM_LIQUIDACION ='" + liq + "'";
        conSqler();
        conSqler.ejecutarSql(auSql);
        conSqler.isImprimirSqlConsola();
        desConSqler();
    }

    public void set_updateDisponible(String vale) {
        String auSql = "update CMT_CATASTRO set DISPONIBLE_OCUPADO='OCUPADO' \n"
                + "where IDE_CATASTRO =" + vale;
        conSql();
        conSql.ejecutarSql(auSql);
        conSql.isImprimirSqlConsola();
        desConSql();
    }

    public void set_updateAnulaLiquicion(String vale, String obser, String login) {
        System.out.println("ENTRA A set_updateAnulaLiquicion");
        conSqler();
        String auSql = "update liquidaciones set estado_liquidacion='ANULADO'"
                + ",OBSERVACIONES='" + obser + "' \n"
                + ",liq_usurev = '" + login + "' \n"
                + "where NUM_LIQUIDACION ='" + vale + "'";
        conSqler.isImprimirSqlConsola();
        conSqler.ejecutarSql(auSql);
        desConSqler();
    }

    public void setUpdateAnulaDetalle(String vale, String login, String obser) {
        System.out.println("ENTRA A set_updateAnulaLiquicion");
        conSql();
        String auSql = "update CMT_DETALLE_MOVIMIENTO  "
                + "set ESTADO_PAGO='AN'"
                + ",DOCUMENTOS='" + obser + "' \n"
                + ",USUARIO_ACTUA = '" + login + "'\n"
                + ",FECHA_ACTUA = getdate() \n"
                + "where NUM_LIQUIDACION ='" + vale + "'";
        conSql.isImprimirSqlConsola();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void set_updateCatastroMausoleo(String vale, String ide_catastro) {
        System.out.println("ENTRA A set_updateCatastroMausoleo");
        conSql();
        String auSql = "update cmt_catastro set MAUSOLEO='" + vale + "' \n"
                + "where ide_catastro ='" + ide_catastro + "'";
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void set_updateTotalOsario(String vale, String total) {
        String auSql = "update CMT_CATASTRO set TOTAL_INGRESA=" + total + " \n"
                + "where IDE_CATASTRO =" + vale;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void set_updateTotalLugar(String id, String total) {
        String auSql = "update CMT_CATASTRO set TOTAL_INGRESA=(select (case when count(*) is null then 0 else count(*)end) as conteo \n"
                + "from CMT_FALLECIDO \n"
                + "where IDE_CATASTRO = " + id + " and ISNULL(tipo_pago, 0)!=4) \n"
                + "where IDE_CATASTRO =" + id;
        conSql();
        conSql.ejecutarSql(auSql);
        conSql.isImprimirSqlConsola();
        desConSql();
    }

    public void set_updateDisponibleExhumado(String vale) {
        String auSql = "update CMT_CATASTRO set DISPONIBLE_OCUPADO='DISPONIBLE' \n"
                + "where IDE_CATASTRO =" + vale;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void set_updateEstadoRep(String vale, String estado) {
        String auSql = "update CMT_REPRESENTANTE set estado='" + estado + "' \n"
                + "where IDE_CMREP =" + vale;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public String maxNumF() {
        conSql();
        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select 0 as id ,(case when max(NUMERO_FORMULARIO) is null then 1 else max(NUMERO_FORMULARIO)+1 end) AS maximo\n"
                + "from CMT_DETALLE_MOVIMIENTO");
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("maximo");
        desConSql();
        return ValorMax;
    }

    public String maxCheckDetalle() {
        conSql();
        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select 0 as id ,(case when max(ide_det_check) is null then 1 else max(ide_det_check)+1 end) AS maximo\n"
                + "from cmt_detalle_check");
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("maximo");
        desConSql();
        return ValorMax;
    }

    public String maxDetalleMovimiento() {
        conSql();
        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select 0 as id ,(case when max(IDE_DET_MOVIMIENTO) is null then 1 else max(IDE_DET_MOVIMIENTO)+1 end) AS maximo\n"
                + "from CMT_DETALLE_MOVIMIENTO");
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("maximo");
        desConSql();
        return ValorMax;
    }

    public String maxTotalOsario(String codigo) {
        conSql();
        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT 0 AS ID,SUM(TOTAL)AS maximo FROM (SELECT 0 AS ID, (CASE WHEN MAX(TOTAL_INGRESA) IS NULL THEN 0 ELSE MAX(TOTAL_INGRESA) END) +1 AS TOTAL FROM CMT_CATASTRO WHERE IDE_CATASTRO='" + codigo + "'\n"
                + "UNION SELECT 0,0 )A");
        tabFuncionario.ejecutarSql();
//        tabFuncionario.imprimirSql();
        ValorMax = tabFuncionario.getValor("maximo");
        desConSql();
        return ValorMax;
    }

    public String restaTotalOsario(String codigo) {
        conSql();
        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select 0 as id, (case when count(*) is null then 0 else count(*) end ) as sitios\n"
                + "from CMT_FALLECIDO \n"
                + "where IDE_CATASTRO = " + codigo + " and tipo_pago != (select IDE_TIPO_PAGO from CMT_TIPO_PAGO where DESCRIPCION = 'EXHUMACION')");
        tabFuncionario.ejecutarSql();
//        tabFuncionario.imprimirSql();
        ValorMax = tabFuncionario.getValor("sitios");
        desConSql();
        return ValorMax;
    }

    public String maxTotalLugar(String codigo) {
        conSql();
        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select 0 as id, (case when count(*) is null then 0 else count(*) end ) as sitios\n"
                + "from CMT_FALLECIDO \n"
                + "where IDE_CATASTRO = " + codigo + " and tipo_pago != (select IDE_TIPO_PAGO from CMT_TIPO_PAGO where DESCRIPCION = 'EXHUMACION')");
        tabFuncionario.ejecutarSql();
//        tabFuncionario.imprimirSql();
        ValorMax = tabFuncionario.getValor("sitios");
        desConSql();
        return ValorMax;
    }

    public void set_updateMigraDatos(String vale, String aprueba) {
        String auSql = "update CMT_MIGRACION_DATOS set aprueba=" + aprueba + " \n"
                + "where  aprueba is null and cod_tabla =" + vale;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public String numLiq(String codigo) {
        conSqler();
        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSqler();
        tabFuncionario.setConexion(conSqler);
        tabFuncionario.setSql("SELECT 0 as id, NUM_LIQUIDACION FROM LIQUIDACIONES where cod_ruta='" + codigo + "'");
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("NUM_LIQUIDACION");
        desConSqler();
        return ValorMax;
    }

    public String codLiq(String codigo) {
        conSqler();
        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSqler();
        tabFuncionario.setConexion(conSqler);
        tabFuncionario.setSql("SELECT 0 as id, CODIGO_LIQUIDACION FROM LIQUIDACIONES where cod_ruta='" + codigo + "'");
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("CODIGO_LIQUIDACION");
        desConSqler();
        return ValorMax;
    }

    public String antLiq(String codigo) {
        conSqler();
        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSqler();
        tabFuncionario.setConexion(conSqler);
        tabFuncionario.setSql("SELECT 0 as id, antecedentes FROM LIQUIDACIONES where cod_ruta='" + codigo + "'");
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("antecedentes");
        desConSqler();
        return ValorMax;
    }

    public String tipoMovimiento(String codigo) {
        conSql();
        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select 0 AS ID,DETALLE_CMACC as maximo \n"
                + "from  cmt_accion b where  IDE_CMACC=" + codigo);
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("maximo");
        desConSql();
        return ValorMax;
    }

    public String cedulaFallecido(String codigo) {
        conSql();
        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select sum(id),sum(CEDULA_FALLECIDO) as CEDULA_FALLECIDO from (\n"
                + "SELECT 0 AS ID,  CEDULA_FALLECIDO   FROM CMT_FALLECIDO WHERE CEDULA_FALLECIDO='" + codigo + "'\n"
                + "union select 0,0  )a");
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("CEDULA_FALLECIDO");
        desConSql();
        return ValorMax;
    }

    public TablaGenerica getCedulaFallecido(String codigo) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT *  FROM CMT_FALLECIDO WHERE CEDULA_FALLECIDO='" + codigo + "' and tipo_pago = 4");
//        tabPersona.imprimirSql();
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica getRepresentanteCedulaFallecido(String codigo) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select TOP 1 * from CMT_REPRESENTANTE\n"
                + "WHERE IDE_FALLECIDO = (select TOP 1 IDE_FALLECIDO from cmt_FALLECIDO where CEDULA_FALLECIDO ='"+codigo+"' AND tipo_pago = 4 order by fecha_ingre desc)\n"
                + "order by fecha_ingre desc");
//        tabPersona.imprimirSql();
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public String certificadoDefuncion(String codigo) {
        conSqler();
        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSqler();
        tabFuncionario.setConexion(conSqler);
        tabFuncionario.setSql("SELECT CERTIFICADO_DEFUN FROM CMT_FALLECIDO WHERE CERTIFICADO_DEFUN='" + codigo + "'");
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("CERTIFICADO_DEFUN");
        desConSqler();
        return ValorMax;
    }

    public TablaGenerica consultaCatastro(Integer ide) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select a.IDE_CATASTRO,DETALLE_LUGAR,SECTOR,NUMERO,MODULO,max(case when fecha_hasta is null then DATEADD(month, 1, GETDATE()) else fecha_hasta end)as fecha_hasta"
                + ",3 as tipo,UBICACION,a.DISPONIBLE_OCUPADO,b.periodo,a.catastro_habilita,(select max(FECHA_DEFUNCION) from CMT_FALLECIDO where IDE_CATASTRO = a.IDE_CATASTRO) as fecha_fallecido\n"
                + "from CMT_CATASTRO a left join cmt_lugar b on a.ide_lugar=b.ide_lugar\n"
                + "left join (select fecha_hasta, ide_catastro \n"
                + "from CMT_DETALLE_MOVIMIENTO \n"
                + "where IDE_DET_MOVIMIENTO = (select max(IDE_DET_MOVIMIENTO)as IDE_DET_MOVIMIENTO\n"
                + "from CMT_DETALLE_MOVIMIENTO  \n"
                + "where IDE_CATASTRO =" + ide + " ))c  on  c.IDE_CATASTRO=a.IDE_CATASTRO  \n"
                + "where  a.IDE_CATASTRO =" + ide + " group by a.IDE_CATASTRO,DETALLE_LUGAR,SECTOR,NUMERO,MODULO,UBICACION,a.DISPONIBLE_OCUPADO,b.periodo,a.catastro_habilita ORDER BY DETALLE_LUGAR,SECTOR,NUMERO,MODULO");
//        tabPersona.imprimirSql();
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica consulDetalle(String codigos) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select DETALLE_LUGAR,sector ,modulo,numero, FECHA_HASTA,fallecidos\n"
                + "from (SELECT DISTINCT IDE_CATASTRO,DETALLE_LUGAR,IDE_LUGAR,sector ,modulo,numero, FECHA_HASTA\n"
                + ",(select STUFF( \n"
                + "(SELECT CAST(',' AS varchar(MAX)) + nombres \n"
                + "FROM CMT_CATASTRO_ARRIENDO_FALLECIDO_VTA \n"
                + "where CMT_CATASTRO_ARRIENDO_FALLECIDO_VTA.IDE_CATASTRO=c.IDE_CATASTRO\n"
                + "ORDER BY nombres\n"
                + "FOR XML PATH('')), 1, 1, '')) as fallecidos  \n"
                + "FROM CMT_CATASTRO_ARRIENDO_FALLECIDO_VTA c) as  a\n"
                + "where IDE_CATASTRO in (" + codigos + ")");
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica consultaCatastrOsario(Integer mov) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select  a.IDE_CATASTRO,DETALLE_LUGAR,SECTOR,NUMERO,MODULO\n"
                + " from CMT_CATASTRO a inner join cmt_lugar b on a.ide_lugar=b.ide_lugar\n"
                + " where DETALLE_LUGAR='OSARIO'  "
                + " AND a.IDE_CATASTRO =" + mov);
//        tabPersona.imprimirSql();
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica consultaAccion(Integer tipo) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT IDE_CMACC,DETALLE_CMACC,parametro,parametro1,parametro2,id_tipo,id_parametro\n"
                + "from CMT_ACCION\n"
                + "where ide_cmacc =" + tipo);
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica consultaCatastroNichoComun(Integer mov) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select  a.IDE_CATASTRO,DETALLE_LUGAR,SECTOR,NUMERO,MODULO\n"
                + " from CMT_CATASTRO a inner join cmt_lugar b on a.ide_lugar=b.ide_lugar\n"
                + " where DETALLE_LUGAR='NICHO COMUN'  "
                + " AND a.IDE_CATASTRO =" + mov);
//        tabPersona.imprimirSql();
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica consultaCatastroAnterior(Integer mov) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select  a.IDE_CATASTRO,DETALLE_LUGAR,SECTOR,NUMERO,MODULO\n"
                + " from CMT_CATASTRO a inner join cmt_lugar b on a.ide_lugar=b.ide_lugar\n"
                + " where (DETALLE_LUGAR='SITIO' OR DETALLE_LUGAR='NICHO') "
                + " AND a.IDE_CATASTRO =" + mov);
//        tabPersona.imprimirSql();
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica consultaFallecido(Integer mov) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select * from CMT_FALLECIDO_RENOVACION where IDE_FALLECIDO=" + mov);
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica consultaFallecido1(Integer mov) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select fecha_desde,fecha_hasta,FECHA_DEFUNCION from CMT_FALLECIDO_RENOVACION where IDE_CATASTRO=" + mov);
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica consultaExhumacion(Integer mov) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT\n"
                + "IDE_CMACC,\n"
                + "DETALLE_CMACC,\n"
                + "parametro,\n"
                + "parametro1,\n"
                + "parametro2,\n"
                + "id_tipo,\n"
                + "id_parametro\n"
                + "FROM CMT_ACCION\n"
                + "where  IDE_CMACC =" + mov);
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica consultaFallecidOsario(Integer mov) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql(" SELECT top 1 B.IDE_FALLECIDO,DETALLE_LUGAR+'-'+SECTOR+'-'+convert(varchar,NUMERO)+'-'+convert(varchar,MODULO) AS CATASTRO,IDE_TIPO_MOVIMIENTO ,FECHA_INGRESA, FECHA_DESDE,FECHA_HASTA,NUM_LIQUIDACION, IDE_CMGEN,CEDULA_FALLECIDO,NOMBRES ,FECHA_NACIMIENTO, FECHA_DEFUNCION\n"
                + ",a.IDE_CATASTRO,SECTOR ,NUMERO ,MODULO ,DETALLE_LUGAR , FECHA_DOCUMENTO_CMARE, \n"
                + "a.IDE_CMREP,IDE_CMTID,NOMBRES_APELLIDOS_CMREP, DOCUMENTO_IDENTIDAD_CMREP,DIRECCION_CMREP,TELEFONOS_CMREP,\n"
                + "CELULAR_CMREP, EMAIL_CMREP,ESTADO,CERTIFICADO_DEFUN,PERIODO_ARRENDAMIENTO,CODIGO_LIQUIDACION,PERIODO_LIQUIDACION,PERIODO_TITULO,VALOR_LIQUIDACION\n"
                + "FROM CMT_DETALLE_MOVIMIENTO A, CMT_FALLECIDO B , CMT_REPRESENTANTE C,CMT_CATASTRO d,  CMT_LUGAR E \n"
                + "WHERE E.IDE_LUGAR=D.IDE_LUGAR AND A.IDE_FALLECIDO=B.IDE_FALLECIDO AND a.IDE_CMREP=C.IDE_CMREP and a.ide_catastro=d.ide_catastro \n"
                + " and DETALLE_LUGAR='OSARIO'  AND B.IDE_FALLECIDO=" + mov + " order by IDE_DET_MOVIMIENTO desc");
//        tabPersona.imprimirSql();
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public String consultaCatastroDuplicado(Integer numero, String serie, String fila, String lugar, String PISO) {
        conSql();
        String ValorMax;
        TablaGenerica tab_consulta = new TablaGenerica();
        conSql();
        tab_consulta.setConexion(conSql);
        tab_consulta.setSql("SELECT 0 AS ID, SUM(IDE_CATASTRO) AS IDE_CATASTRO FROM (select  0 as id,ide_catastro \n"
                + "from CMT_LUGAR a, CMT_CATASTRO b \n"
                + "where a. IDE_LUGAR=b.IDE_LUGAR and DETALLE_LUGAR='" + lugar + "' \n"
                + "and SECTOR='" + serie + "' and MODULO='" + fila + "' and NUMERO=" + numero + " and UBICACION='" + PISO + "' UNION select  DISTINCT 0 as id,'0' as ide_catastro from CMT_LUGAR )A ");
        tab_consulta.ejecutarSql();
//        tab_consulta.imprimirSql();
        ValorMax = tab_consulta.getValor("IDE_CATASTRO");
        return ValorMax;
    }

    public String consultaCatastroDuplicadoNicho(Integer numero, String serie, String fila, String lugar) {
        conSql();
        String ValorMax;
        TablaGenerica tab_consulta = new TablaGenerica();
        conSql();
        tab_consulta.setConexion(conSql);
        tab_consulta.setSql("SELECT 0 AS ID, SUM(IDE_CATASTRO) AS IDE_CATASTRO FROM (select  0 as id,ide_catastro \n"
                + "from CMT_LUGAR a, CMT_CATASTRO b \n"
                + "where a. IDE_LUGAR=b.IDE_LUGAR and DETALLE_LUGAR='" + lugar + "' \n"
                + "and SECTOR='" + serie + "' and MODULO='" + fila + "' and NUMERO=" + numero + "  UNION select  DISTINCT 0 as id,'0' as ide_catastro from CMT_LUGAR )A ");
        tab_consulta.ejecutarSql();
//        tab_consulta.imprimirSql();
        ValorMax = tab_consulta.getValor("IDE_CATASTRO");
        return ValorMax;
    }

    public void ingresaMovimiento(String catastro, String representant, String det_movimiento, String fallecido) {
        String parametro = "UPDATE CMT_DETALLE_MOVIMIENTO SET IDE_CATASTRO=" + catastro + " ,IDE_CMREP =" + representant + " ,IDE_FALLECIDO=" + fallecido + " WHERE ide_det_movimiento=" + det_movimiento;
        conSql();
        conSql.ejecutarSql(parametro);
        desConSql();
    }

    public void setUpdateMovimiento(String representant, String det_movimiento) {
        String parametro = "UPDATE CMT_DETALLE_MOVIMIENTO SET IDE_CMREP =" + representant + " WHERE ide_det_movimiento=" + det_movimiento;
        conSql();
        conSql.ejecutarSql(parametro);
        desConSql();
    }

    public String consultaRepAc(Integer numero) {
        conSql();
        String ValorMax;
        TablaGenerica tab_consulta = new TablaGenerica();
        conSql();
        tab_consulta.setConexion(conSql);
        tab_consulta.setSql("select 0 AS ID, estado from  CMT_FALLECIDO a,CMT_REPRESENTANTE b, CMT_DETALLE_MOVIMIENTO c\n"
                + " where a.IDE_FALLECIDO=b.IDE_FALLECIDO and c.IDE_FALLECIDO=a.IDE_FALLECIDO and IDE_DET_MOVIMIENTO=" + numero);
        tab_consulta.ejecutarSql();
//        tab_consulta.imprimirSql();
        ValorMax = tab_consulta.getValor("estado");
        return ValorMax;
    }

    public int getRegFallecido(Integer numero, String cedula) {
        conSql();
        int ValorMax;
        TablaGenerica tab_consulta = new TablaGenerica();
        conSql();
        tab_consulta.setConexion(conSql);
        tab_consulta.setSql("select 0 AS ID, (case when max(IDE_RENTAS_FALLECIDO) is null then 0 when max(IDE_RENTAS_FALLECIDO)is not null then max(IDE_RENTAS_FALLECIDO) end) as valor from  CMT_FALLECIDO a\n"
                + " where CEDULA_FALLECIDO = '" + cedula + "' and IDE_RENTAS_FALLECIDO=" + numero);
        tab_consulta.ejecutarSql();
//        tab_consulta.imprimirSql();
        ValorMax = Integer.parseInt(tab_consulta.getValor("valor"));
        return ValorMax;
    }

    public String getRegRepresentante(Integer codigo) {
        conSql();
        String ValorMax;
        TablaGenerica tab_consulta = new TablaGenerica();
        conSql();
        tab_consulta.setConexion(conSql);
        tab_consulta.setSql("SELECT 0 as id,(case when max(orden_representante) is null then 0 else max(orden_representante) end) as valor\n"
                + "FROM dbo.CMT_REPRESENTANTE where IDE_FALLECIDO = " + codigo);
        tab_consulta.ejecutarSql();
        ValorMax = tab_consulta.getValor("valor");
        return ValorMax;
    }

    public String consultaCatastroDuplicadoMausoleo(Integer numero, String lugar) {
        conSql();
        String ValorMax;
        TablaGenerica tab_consulta = new TablaGenerica();
        conSql();
        tab_consulta.setConexion(conSql);
        tab_consulta.setSql("SELECT 0 AS ID, SUM(IDE_CATASTRO) AS IDE_CATASTRO FROM (select  0 as id,ide_catastro \n"
                + "from CMT_LUGAR a, CMT_CATASTRO b \n"
                + "where a. IDE_LUGAR=b.IDE_LUGAR and DETALLE_LUGAR='" + lugar + "' \n"
                + " and MODULO=" + numero + " UNION select  DISTINCT 0 as id,'0' as ide_catastro from CMT_LUGAR )A ");
        tab_consulta.ejecutarSql();
//        tab_consulta.imprimirSql();
        ValorMax = tab_consulta.getValor("IDE_CATASTRO");
        return ValorMax;
    }

    public String consultaCatastroDuplicadoMausoleoCompuesto(Integer ide_catastro, Integer IDE_MAU_COMP) {
        conSql();
        String ValorMax;
        TablaGenerica tab_consulta = new TablaGenerica();
        conSql();
        tab_consulta.setConexion(conSql);
        tab_consulta.setSql("SELECT 0 AS ID, SUM(IDE_MAU_UBICA) AS IDE_CATASTRO FROM (\n"
                + "select 0 as id,IDE_MAU_UBICA from CMT_MAUSOLEO_COMPUESTO a, CMT_MAU_UBICA b\n"
                + "WHERE a.IDE_MAU_COMP=b.IDE_MAU_COMP and ide_catastro=" + ide_catastro + " and a.IDE_MAU_COMP=" + IDE_MAU_COMP + "\n"
                + "UNION select  DISTINCT 0 as id,'0' as IDE_MAU_UBICA )A ");
        tab_consulta.ejecutarSql();
//        tab_consulta.imprimirSql();
        ValorMax = tab_consulta.getValor("IDE_CATASTRO");
        return ValorMax;
    }

    public TablaGenerica getLugarCatastro(int lugar) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        conSql();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select ide_lugar,valor,codigofiscal_cuenta,dsc_cuenta,periodo "
                + "from cmt_lugar "
                + "where ide_lugar in (select ide_lugar from CMT_CATASTRO where ide_catastro=" + lugar + ")");
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica getLugaresCatastro(int lugar, String sector, String modulo, String numero) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        conSql();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select CMT_LUGAR.ide_lugar,valor,codigofiscal_cuenta,dsc_cuenta,periodo\n"
                + "from CMT_LUGAR\n"
                + "inner join CMT_CATASTRO on CMT_LUGAR.IDE_LUGAR = CMT_CATASTRO.IDE_LUGAR\n"
                + "where CMT_LUGAR.ide_lugar = " + lugar + " and sector = " + sector + " and modulo= '" + modulo + "' and numero ='" + numero + "'");
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica getValorCatastro(String lugar) {
        conSqler();
        TablaGenerica tabPersona = new TablaGenerica();
        conSqler();
        tabPersona.setConexion(conSqler);
        tabPersona.setSql("SELECT CODIGO ,TIPO,COD_DIRECCION,DECRIPCION ,VALOR ,FECHA_I ,FECHA_F ,ESTADO\n"
                + "  FROM PARAMETROS_VAL\n"
                + "  WHERE cod_direccion ='190600'\n"
                + "AND DECRIPCION like '%" + lugar + "%' ");
        tabPersona.ejecutarSql();
//        tabPersona.imprimirSql();
        desConSqler();
        return tabPersona;
    }

    public TablaGenerica getCodLiquidacion() {
        conSqler();
        TablaGenerica tabPersona = new TablaGenerica();
        conSqler();
        tabPersona.setConexion(conSqler);
        tabPersona.setSql("select cast(contador as varchar) as id,periodo  from numericos \n"
                + "where cod_numerico ='TC_GLOBAL' and periodo= (select valor_parametro from parametros where cod_parametro = 'PERIODO_LIQUIDACION' and estado_parametro = 'ACTIVO')");
        tabPersona.ejecutarSql();
        desConSqler();
        return tabPersona;
    }

    public TablaGenerica getNumLiquidacion(String direccion) {
        conSqler();
        TablaGenerica tabPersona = new TablaGenerica();
        conSqler();
        tabPersona.setConexion(conSqler);
        tabPersona.setSql("select cast (contador as varchar) as num,periodo,(select valor_parametro from parametros where cod_parametro = 'PERIODO_LIQUIDACION' and estado_parametro = 'ACTIVO') as parametro from numericos\n"
                + "where cod_numerico = '" + direccion + "' and periodo = (select valor_parametro from parametros where cod_parametro = 'PERIODO_LIQUIDACION' and estado_parametro = 'ACTIVO')");
        tabPersona.ejecutarSql();
        desConSqler();
        return tabPersona;
    }

    public TablaGenerica getLiquidacionTitulo(String liquidacion) {
        conSqler();
        TablaGenerica tabPersona = new TablaGenerica();
        conSqler();
        tabPersona.setConexion(conSqler);
        tabPersona.setSql("select * \n"
                + "from TITULO_CREDITO \n"
                + "where NUM_LIQUIDACION = '" + liquidacion + "' and \n"
                + "pagado = 'SI'");
        tabPersona.ejecutarSql();
        desConSqler();
        return tabPersona;
    }

    public TablaGenerica getValorLiquidacion(String liquidacion) {
        conSqler();
        TablaGenerica tabPersona = new TablaGenerica();
        conSqler();
        tabPersona.setConexion(conSqler);
        tabPersona.setSql("select * \n"
                + "from LIQUIDACIONES \n"
                + "where NUM_LIQUIDACION = '" + liquidacion + "'");
        tabPersona.ejecutarSql();
        desConSqler();
        return tabPersona;
    }

    public void setRegistroLiquidacion(String codDireccion, String perTitulo, String iniciales, String clvCatastral, String cedRuc, String nomRazonsocial,
            String cedRuc2, String nomRazonsocial2, String domicilio, String barrio, String antecedentes, String observaciones, String trmRegycont,
            String logLiquidador, Double valortotal_liquidacion, String codRubro, String dirRepresentante, String giroProducto, String actEconomica,
            String representante, String codCatastro, String codRuta, String fechaIni, String fechaFin, String tipo, String numDocumento, String codLiquidacion, String numLiquidacion) {
        String au_sql = "EXEC ER.dbo.PA_LIQUIDACIONES_INSERT_DATOS_CEM"
                + "     @codigo_direccion ='" + codDireccion + "' ,\n"
                + "	@periodo_titulo = " + perTitulo + ",\n"
                + "	@iniciales = " + iniciales + ",\n"
                + "	@clave_catastral = " + clvCatastral + ",\n"
                + "	@cedula_ruc = " + cedRuc + ",\n"
                + "	@nombre_razonsocial = " + nomRazonsocial + ",\n"
                + "	@cedula_ruc2 = " + cedRuc2 + ",\n"
                + "	@nombre_razonsocial2 = " + nomRazonsocial2 + ",\n"
                + "	@domicilio = " + domicilio + ",\n"
                + "	@barrio = " + barrio + ",\n"
                + "	@antecedentes = " + antecedentes + ",\n"
                + "	@observaciones = " + observaciones + ",\n"
                + "	@tramite_regycont = " + trmRegycont + ",\n"
                + "	@login_liquidador = " + logLiquidador + ",\n"
                + "	@valortotal_liquidacion = " + valortotal_liquidacion + ",\n"
                + "	@cod_rubro = " + codRubro + ",\n"
                + "	@direccion_representante = " + dirRepresentante + ",\n"
                + "	@giro_producto = " + giroProducto + ",\n"
                + "	@actividad_economica  = " + actEconomica + ",\n"
                + "	@representante  = " + representante + ",\n"
                + "	@cod_catastro  = " + codCatastro + ",\n"
                + "	@cod_ruta  = " + codRuta + ",\n"
                + "	@fecha_ini  = " + fechaIni + ",\n"
                + "	@fecha_fin  = " + fechaFin + ",\n"
                + "	@tipo  = " + tipo + ",\n"
                + "	@numDocumento  = " + numDocumento + ",\n"
                + "	@cod_liquidacion  = " + codLiquidacion + ",\n"
                + "	@num_liquidacion  = " + numLiquidacion + ",\n"
                + "     @archivo  = null,\n"
                + "	@dirArch  = null,\n"
                + "	@nomArc  = null,\n"
                + "     @chasis = null,\n"
                + "	@placas = null";
        conSqler();
        conSqler.ejecutarSql(au_sql);
        desConSqler();
    }

    public void setUpdateCodLiquidacion(String periodo) {
        String auSql = "update numericos\n"
                + "set contador = contador+1\n"
                + "where cod_numerico = 'TC_GLOBAL' and periodo = " + periodo + "";
        conSqler();
        conSqler.ejecutarSql(auSql);
        desConSqler();
    }

    public void setUpdateNumLiquidacion(String direccion, String periodo) {
        String auSql = "update numericos\n"
                + "set contador = contador+1\n"
                + "where cod_numerico ='" + direccion + "' and periodo= " + periodo + "";
        conSqler();
        conSqler.ejecutarSql(auSql);
        desConSqler();
    }

    public TablaGenerica getRegistroLiquidacion(String codDireccion, String perTitulo, String iniciales, String clvCatastral, String cedula_ruc, String nombRazonsocial,
            String cedula_ruc2, String nombRazonsocial2, String domicilio, String barrio, String antecedentes, String observaciones, String trmRegycont,
            String logLiquidador, Double valTotal_liquidacion, String codRubro, String dirRepresentante, String giroProducto, String actidEconomica,
            String representante, String codCatastro, String codRuta, String fechaInicio, String fechaFin, String tipo, String numDocumento) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        conSql();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("EXEC sp_liquidacion_cementerio "
                + "@codigo_direccion =" + codDireccion + ",\n"
                + "@periodo_titulo =" + perTitulo + ",\n"
                + "@iniciales =" + iniciales + ",\n"
                + "@clave_catastral =" + clvCatastral + ",\n"
                + "@cedula_ruc =" + cedula_ruc + ",\n"
                + "@nombre_razonsocial =" + nombRazonsocial + ",\n"
                + "@cedula_ruc2 =" + cedula_ruc2 + ",\n"
                + "@nombre_razonsocial2 =" + nombRazonsocial2 + ",\n"
                + "@domicilio =" + domicilio + ",\n"
                + "@barrio =" + barrio + ",\n"
                + "@antecedentes=" + antecedentes + ",\n"
                + "@observaciones =" + observaciones + ",\n"
                + "@tramite_regycont=" + trmRegycont + ",\n"
                + "@login_liquidador =" + logLiquidador + ",\n"
                + "@valortotal_liquidacion =" + valTotal_liquidacion + ",\n"
                + "@cod_rubro =" + codRubro + ",\n"
                + "@direccion_representante =" + dirRepresentante + ",\n"
                + "@giro_producto =" + giroProducto + ",\n"
                + "@actividad_economica =" + actidEconomica + ",\n"
                + "@representante =" + representante + ",\n"
                + "@cod_catastro =" + codCatastro + ",\n"
                + "@cod_ruta =" + codRuta + ",\n"
                + "@fecha_ini =" + fechaInicio + ",\n"
                + "@fecha_fin =" + fechaFin + ",\n"
                + "@tipo =" + tipo + ", \n"
                + "@numDocumento=" + numDocumento + ",\n"
                + "@archivo=null,\n"
                + "@dirArch=null,\n"
                + "@nomArc=null");
        tabPersona.ejecutarSql();
//        tabPersona.imprimirSql();
        desConSql();
        return tabPersona;
    }

    public void getDatosFallecido(Integer idFallecido, String cedFallecido, String nombres, String fecha, String login, String lugar, String ide, String numero, String modulo, String sector, Integer ban, Integer id, String ant_catastro) {
        String mensaje = "EXEC dbo.CMT_DATOS_FALLECIDOS_INSERT "
                + "@ide_fallecido ='" + idFallecido + "',"
                + "@cedula_fallecido ='" + cedFallecido + "',"
                + "@nombres ='" + nombres + "',"
                + "@fecha_defuncion = '" + fecha + "',"
                + "@login_usuario = '" + login + "',"
                + "@lugar_actual = '" + lugar + "',"
                + "@nuevo_lugar = '" + ide + "',"
                + "@nueva_fila = '" + numero + "',"
                + "@nuevo_bloque = '" + sector + "',"
                + "@nuevo_numero = '" + modulo + "',"
                + "@ban = " + ban + ","
                + "@id_fallecido = '" + id + "',"
                + "@ant_catastro = '" + ant_catastro + "'";
        conSql();
        conSql.ejecutarSql(mensaje);
        desConSql();
    }

    public String getCuentaDisponible(String lugar) {
        conSql();
        String ValorMax;
        TablaGenerica tab_consulta = new TablaGenerica();
        conSql();
        tab_consulta.setConexion(conSql);
        tab_consulta.setSql("select sum(a.DIS) AS DIS , a.DISPONIBLE_OCUPADO \n"
                + "from (SELECT COUNT(DISPONIBLE_OCUPADO) AS DIS, DISPONIBLE_OCUPADO \n"
                + "from CMT_CATASTRO a, cmt_lugar b\n"
                + "where a.ide_lugar=b.ide_lugar \n"
                + "and a.ide_lugar =" + lugar + " \n"
                + "AND DISPONIBLE_OCUPADO='DISPONIBLE'\n"
                + "GROUP BY DISPONIBLE_OCUPADO\n"
                + "union \n"
                + "select 0,'DISPONIBLE') as a\n"
                + "group by DISPONIBLE_OCUPADO ");
        tab_consulta.ejecutarSql();
//        tab_consulta.imprimirSql();
        ValorMax = tab_consulta.getValor("DIS");
        return ValorMax;
    }

    public String getCuentaOcupado(String lugar) {
        conSql();
        String ValorMax;
        TablaGenerica tab_consulta = new TablaGenerica();
        conSql();
        tab_consulta.setConexion(conSql);
        tab_consulta.setSql("select sum(a.DIS) AS OCUPADO , a.DISPONIBLE_OCUPADO \n"
                + "from (SELECT COUNT(DISPONIBLE_OCUPADO) AS DIS, DISPONIBLE_OCUPADO \n"
                + "from CMT_CATASTRO a, cmt_lugar b\n"
                + "where a.ide_lugar=b.ide_lugar \n"
                + "and a.ide_lugar =" + lugar + "\n"
                + "AND DISPONIBLE_OCUPADO='OCUPADO'\n"
                + "GROUP BY DISPONIBLE_OCUPADO\n"
                + "union \n"
                + "select 0,'OCUPADO') as a\n"
                + "group by DISPONIBLE_OCUPADO    ");
        tab_consulta.ejecutarSql();
//        tab_consulta.imprimirSql();
        ValorMax = tab_consulta.getValor("OCUPADO");
        return ValorMax;
    }

    public TablaGenerica getValorCatastro1(String lugar) {
        conSqler();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSqler);
        tabPersona.setSql(" select CODIGOFISCAL_CUENTA, DSC_CUENTA, COD_RUBRO from cuenta where DSC_CUENTA LIKE '%" + lugar + "%' AND ESTADO_CUENTA='ACTIVO'  ");
        tabPersona.ejecutarSql();
        desConSqler();
        return tabPersona;
    }

    public TablaGenerica getStockCementerio(String lugar) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select dis,DISPONIBLE_OCUPADO\n"
                + "from(SELECT COUNT(DISPONIBLE_OCUPADO)AS DIS,DISPONIBLE_OCUPADO\n"
                + "from CMT_CATASTRO a, cmt_lugar b\n"
                + "where a.ide_lugar=b.ide_lugar  AND DISPONIBLE_OCUPADO in ('OCUPADO','DISPONIBLE')\n"
                + "and a.ide_lugar=" + lugar + " \n"
                + "GROUP BY DISPONIBLE_OCUPADO) as f");
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica getRubroCodigo(Integer ide) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT IDE_CATASTRO,DETALLE_LUGAR,SECTOR,NUMERO,MODULO,cod_rubro\n"
                + "FROM CMT_CATASTRO A INNER JOIN CMT_LUGAR B ON A.IDE_LUGAR = B.IDE_LUGAR\n"
                + "WHERE IDE_CATASTRO=" + ide);
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica getFormulario(Integer codigo) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT b.ide_catastro,b.fecha_ingre,NUM_LIQUIDACION,FECHA_DESDE,FECHA_HASTA,DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP\n"
                + ",DIRECCION_CMREP,TELEFONOS_CMREP,CELULAR_CMREP,EMAIL_CMREP,CEDULA_FALLECIDO,NOMBRES,FECHA_NACIMIENTO,FECHA_DEFUNCION\n"
                + "FROM dbo.CMT_CATASTRO a\n"
                + "INNER JOIN dbo.CMT_FALLECIDO d ON d.IDE_CATASTRO = a.IDE_CATASTRO\n"
                + "INNER JOIN dbo.CMT_DETALLE_MOVIMIENTO b ON b.IDE_FALLECIDO = d.IDE_FALLECIDO \n"
                + "INNER JOIN dbo.CMT_REPRESENTANTE c ON d.IDE_FALLECIDO = c.IDE_FALLECIDO\n"
                + "WHERE a.IDE_CATASTRO = " + codigo + "\n"
                + "and b.IDE_DET_MOVIMIENTO in (select max(IDE_DET_MOVIMIENTO) from CMT_DETALLE_MOVIMIENTO group by ide_fallecido)   ");
        tabPersona.ejecutarSql();
//        tabPersona.imprimirSql();
        desConSql();
        return tabPersona;
    }

    public void deleteMov(String ide_mov) {
        String au_sql = "delete from CMT_REPRESENTANTE  where ide_cmrep=(select ide_cmrep from CMT_DETALLE_MOVIMIENTO where IDE_DET_MOVIMIENTO=" + ide_mov + ")\n"
                + "delete from CMT_FALLECIDO where ide_fallecido=(select ide_fallecido from CMT_DETALLE_MOVIMIENTO where IDE_DET_MOVIMIENTO=" + ide_mov + ")\n"
                + "delete from cmt_detalle_check where IDE_DET_MOVIMIENTO=" + ide_mov + ";delete from cmt_detalle_movimiento where IDE_DET_MOVIMIENTO =" + ide_mov + "";
        conSql();
        conSql.ejecutarSql(au_sql);
        desConSql();
    }

    public TablaGenerica insertCatastroNuevo(Integer mov) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select * from CMT_MIGRACION_DATOS where cod_tabla =" + mov);
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica setCatastroID(Integer lugar, String sector, String modulo, Integer num) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select IDE_CATASTRO, IDE_LUGAR,\n"
                + "(case when catastro_anterior is null then (select top 1 catastro_anterior from CMT_FALLECIDO where IDE_CATASTRO=CMT_CATASTRO.IDE_CATASTRO and catastro_anterior is not null) else catastro_anterior end) as catastro_anterior \n"
                + "from CMT_CATASTRO where IDE_LUGAR=" + lugar + " and SECTOR =" + sector + " "
                + "and MODULO='" + modulo + "' and NUMERO=" + num);
        tabPersona.ejecutarSql();
//        tabPersona.imprimirSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica getCatasFallecido(String lugar, String sector, String modulo, Integer numero) {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT IDE_CATASTRO,IDE_LUGAR,SECTOR,MODULO,NUMERO\n"
                + "FROM dbo.CMT_CATASTRO\n"
                + "where ide_lugar = (SELECT IDE_LUGAR FROM dbo.CMT_LUGAR where DETALLE_LUGAR = '" + lugar + "') and sector = '" + sector + "' "
                + "and modulo = '" + modulo + "' and numero =" + numero);
//        tabPersona.imprimirSql();
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public TablaGenerica getEstadoFallecido(String ide) {
        conSqlapli();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSqlapli);
        tabPersona.setSql("select * from fallecidos where id_fallecido= " + ide);
//        tabPersona.imprimirSql();
        tabPersona.ejecutarSql();
        desconSqlapli();
        return tabPersona;
    }

    public TablaGenerica getSisBloqueo() {
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT\n"
                + "TABLA_BLOQ,\n"
                + "MAXIMO_BLOQ\n"
                + "FROM SIS_BLOQUEO\n"
                + "where TABLA_BLOQ in ('CMT_CATASTRO','CMT_FALLECIDO','CMT_REPRESENTANTE','CMT_DETALLE_MOVIMIENTO')");
        tabPersona.ejecutarSql();
        desConSql();
        return tabPersona;
    }

    public void set_updateFallecidoRentas(String ide, String lugar, String bloque, String fila, String numero, String piso) {
        String auSql = "update fallecidos set nuevo_lugar='" + lugar + "',nuevo_bloque='" + bloque + "',nuevo_fila='" + fila + "',nuevo_numero='" + numero + "',   nuevo_piso='" + piso + "'  ,bloqueo='SI'  where id_fallecido= " + ide;
        conSqlapli();
        conSqlapli.ejecutarSql(auSql);
        desconSqlapli();
    }

    public void set_updateBloqueExcel(int ide) {
        String auSql = "update CMT_MIGRACION_DATOS set bloqueo='SI'  where cod_tabla= " + ide;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void set_updateBloque(String ide, Integer bloq) {
        String auSql = "update SIS_BLOQUEO\n"
                + "set MAXIMO_BLOQ = " + bloq + "\n"
                + "where TABLA_BLOQ = " + ide + "";
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void setUpdateCatastro(Integer codigo, String catastro) {
        String auSql = "update CMT_CATASTRO\n"
                + "set ver=1,\n"
                + "DISPONIBLE_OCUPADO = 'OCUPADO',\n"
                + "catastro_anterior ='" + catastro + "',\n"
                + "TOTAL_INGRESA = ((select (case when max(TOTAL_INGRESA) is null then 0 else max(TOTAL_INGRESA) end) from CMT_CATASTRO where IDE_CATASTRO =" + codigo + ")+1)\n"
                + "where IDE_CATASTRO =" + codigo;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void setUpdateRepres(Integer codigo) {
        String auSql = "update CMT_REPRESENTANTE\n"
                + "set ESTADO='BAJA'\n"
                + "where IDE_FALLECIDO =" + codigo;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void setUpdateFallecido(Integer codigo) {
        String auSql = "update CMT_FALLECIDO\n"
                + "set TIPO_PAGO=(select IDE_TIPO_PAGO from CMT_TIPO_PAGO where DESCRIPCION ='EXHUMACION')\n"
                + "where IDE_FALLECIDO =" + codigo;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void setUpdateCambioFallecido(String usuario, Integer codigo, Integer lugar, String sector, String modulo, String numero, Integer catastro) {
        String auSql = "update CMT_FALLECIDO\n"
                + "set USUARIO_ACTUA='" + usuario + "',\n"
                + "FECHA_ACTUA=GETDATE(),\n"
                + "HORA_ACTUA=CONVERT (time, GETDATE()),\n"
                + "IDE_CATASTRO=(select IDE_CATASTRO from CMT_CATASTRO where IDE_lugar =" + lugar + " and sector='" + sector + "' and modulo='" + modulo + "' and numero='" + numero + "'),\n"
                + "catastro_anterior=(select  CMT_LUGAR.detalle_lugar + ' ' +(case when CMT_LUGAR.detalle_lugar = 'NICHO' then 'SERIE'\n"
                + "when CMT_LUGAR.detalle_lugar = 'NICHO' then 'BLOQUE' ELSE CMT_LUGAR.detalle_lugar END) + ' :' +\n"
                + "sector + ' ' +\n"
                + "(case when CMT_LUGAR.detalle_lugar = 'NICHO' then 'NIVEL'\n"
                + "when CMT_LUGAR.detalle_lugar = 'NICHO' then 'FILA' ELSE CMT_LUGAR.detalle_lugar END) + ' :' +\n"
                + "modulo + ' NUM:' + NUMERO\n"
                + "from CMT_CATASTRO \n"
                + "inner join CMT_LUGAR on CMT_CATASTRO.IDE_LUGAR = CMT_LUGAR.IDE_LUGAR\n"
                + "where IDE_CATASTRO =" + catastro + " )\n"
                + "where IDE_FALLECIDO = " + codigo;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void setDeleteCatastro(Integer lugar, String sector, String modulo, Integer numero) {
        String auSql = "delete FROM dbo.CMT_CATASTRO\n"
                + "where IDE_LUGAR = " + lugar + " and SECTOR = " + sector + " and MODULO = '" + modulo + "' and NUMERO= " + numero;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void CMT_DATOS_REPRESENTANTE_INSERT(String repre, String ide, String login_usuario, String cedula, String nombre, String dir, String tel, String estado) {
        String mensaje = "EXEC dbo.CMT_DATOS_REPRESENTANTE_INSERT "
                + "@id_representante ='" + repre + "',"
                + "@ide_fallecido ='" + ide + "',"
                + "@login_usuario ='" + login_usuario + "',"
                + "@cedula_represent = '" + cedula + "',"
                + "@nombres_represent = '" + nombre + "',"
                + "@direccion_represent = '" + dir + "',"
                + "@telefono_represent = '" + tel + "',"
                + "@estado_represent = '" + estado + "'";
        conSql();
        conSql.ejecutarSql(mensaje);
        desConSql();
    }

    public void CMT_MOVIMIENTOS_FALLECIDOS_INSERT(String id_control, String ide, String login_usuario, String tipo_pago, String lugar,
            String cedula, String nombre, String fecha, String lug, String sector, String modulo, String numero) {
        String mensaje = "EXEC dbo.CMT_MOVIMIENTOS_FALLECIDOS_INSERT "
                + "@id_CONTROL ='" + id_control + "',"
                + "@ide_fallecido ='" + ide + "',"
                + "@login_usuario ='" + login_usuario + "',"
                + "@tipo_pago ='" + tipo_pago + "',"
                + "@lugar_actual ='" + lugar + "',"
                + "@ced_fallecido = '" + cedula + "',"
                + "@nom_fallecido = '" + nombre + "',"
                + "@fec_fallecido = '" + fecha + "',"
                + "@det_lugar = '" + lug + "',"
                + "@det_sector = '" + sector + "',"
                + "@det_modulo = '" + modulo + "',"
                + "@det_numero = " + numero + "";
        conSql();
        conSql.ejecutarSql(mensaje);
    }

    public void CMT_FALLECIDOS_INSERT_FALTANTE(String cedula, String nombre, String fdefuncion, String fnacimiento, String login, Integer lugar, String catastro) {
        String mensaje = "EXEC dbo.CMT_FALLECIDOS_INSERT_FALTANTE "
                + "@cedula_fallecido =" + cedula + ","
                + "@nombres ='" + nombre + "',"
                + "@fecha_defuncion =" + fdefuncion + ","
                + "@fecha_nacimiento =" + fnacimiento + ","
                + "@login_usuario ='" + login + "',"
                + "@id_catastro = " + lugar + ","
                + "@ant_catastro = '" + catastro + "'";
        conSql();
        conSql.ejecutarSql(mensaje);
    }

    public void setInsertCatastro_SP(String lugar, String bloque, String fila, String numero, String piso, String login, String actual) {
        String auSql = "EXEC [dbo].[cmt_LugarlArrendamiento]"
                + "@nuevo_lugar='" + lugar + "' \n"
                + ",@nuevo_bloque='" + bloque + "'\n"
                + ",@nuevo_fila='" + fila + "'\n"
                + ",@nuevo_numero='" + numero + "'\n"
                + ",@nuevo_piso='" + piso + "' \n"
                + ",@login_usuario  ='" + login + "'"
                + ",@catastro_anterior='" + actual + "'";
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void setInserCatastro_SP(String sector, String modulo, String numero, String ubicacion, Integer lugar, String mausoleo, String login) {
        String auSql = "EXEC [dbo].[CMT_DATOS_CATASTRO_INSERT]"
                + "@sector =" + sector + "\n"
                + ",@modulo =" + modulo.toUpperCase() + "\n"
                + ",@numero =" + numero + "\n"
                + ",@ubicacion =" + ubicacion + " \n"
                + ",@id_lugar =" + lugar + "\n"
                + ",@mausoleo ='" + mausoleo.toUpperCase() + "'"
                + ",@usuario =" + login + "";
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void setInserDetalle_SP(String sector, String modulo, String numero, Integer lugar, String login, Integer id) {
        String auSql = "EXEC [dbo].[CMT_MOVIMIENTOS_CAMBIO_FALLECIDOS]"
                + "@USUARIO_ACTUA ='" + login + "'\n"
                + ",@IDE_lugar =" + lugar + "\n"
                + ",@sector =" + sector + "\n"
                + ",@modulo =" + modulo.toUpperCase() + " \n"
                + ",@numero =" + numero + ""
                + ",@IDE_MOVIMIENTO =" + id + "";
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public void setQuitarFallecido_SP(Integer lugar, String sector, String modulo, String numero, String login) {
        String auSql = "EXEC [dbo].[CMT_QUITAR_FALLECIDOS_LIBERAR]"
                + "@lugar =" + lugar + "\n"
                + ",@sector ='" + sector + "'\n"
                + ",@modulo ='" + modulo.toUpperCase() + "' \n"
                + ",@numero ='" + numero + "' \n"
                + ",@login_usuario ='" + login + "'";
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    public TablaGenerica getRecuperaCedulaTipoFallecido(String cedula) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT IDE_TIPO_FALLECIDO,DESCRIPCION,CEDULA\n"
                + "FROM CMT_TIPO_FALLECIDO\n"
                + "where IDE_TIPO_FALLECIDO = '" + cedula + "'");
        tabFuncionario.ejecutarSql();
        desConSql();
        return tabFuncionario;
    }

    public TablaGenerica getTipoPago(Integer tipo) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select IDE_TIPO_PAGO,DESCRIPCION from CMT_TIPO_PAGO where IDE_TIPO_PAGO =" + tipo);
        tabFuncionario.ejecutarSql();
        desConSql();
        return tabFuncionario;
    }

    public int getVerificar_sitios(Integer lugar, String sector, String modulo, String inicio, String fin) {
        conSql();
        int ValorMax;
        TablaGenerica tab_consulta = new TablaGenerica();
        conSql();
        tab_consulta.setConexion(conSql);
        tab_consulta.setSql("select 0 as id,count(*) as valor\n"
                + "from(SELECT count(*) as conteo\n"
                + "FROM CMT_CATASTRO\n"
                + "where IDE_LUGAR = " + lugar + " and SECTOR ='" + sector + "' and MODULO ='" + modulo + "' and  NUMERO between " + inicio + " and " + fin + "\n"
                + "group by IDE_LUGAR,SECTOR,MODULO,NUMERO,UBICACION) as a");
        tab_consulta.ejecutarSql();
//        tab_consulta.imprimirSql();
        ValorMax = Integer.parseInt(tab_consulta.getValor("valor"));
        return ValorMax;
    }

    public TablaGenerica getRecuperaLugar(Integer lugar) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT IDE_LUGAR,DETALLE_LUGAR,ISNULL(VALOR, 0) as VALOR,ISNULL(PERIODO,0) as PERIODO,codigofiscal_cuenta FROM CMT_LUGAR where IDE_LUGAR = " + lugar);
        tabFuncionario.ejecutarSql();
        desConSql();
        return tabFuncionario;
    }

    public TablaGenerica getConteoRegistro(Integer catastro) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select 0 as id,count(*) as conteo\n"
                + "from (select cedula_fallecido,ide_catastro from CMT_FALLECIDO where tipo_pago in (select ide_tipo_pago from cmt_tipo_pago where descripcion!='EXHUMACION')\n"
                + "or tipo_pago is null) as a\n"
                + "where a.ide_catastro = " + catastro);
        tabFuncionario.ejecutarSql();
        desConSql();
        return tabFuncionario;
    }

    public TablaGenerica getLugarAnio(String sitio) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select * from CMT_LUGAR where DETALLE_LUGAR = '" + sitio + "'");
        tabFuncionario.ejecutarSql();
        desConSql();
        return tabFuncionario;
    }

    public TablaGenerica getVerDispSitio(String sitio, String sector, String modulo, String numero) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT c.IDE_CATASTRO,count(*) as conteo,c.DISPONIBLE_OCUPADO\n"
                + "FROM dbo.CMT_LUGAR l\n"
                + "INNER JOIN dbo.CMT_CATASTRO c ON c.IDE_LUGAR = l.IDE_LUGAR\n"
                + "where l.DETALLE_LUGAR = '" + sitio + "' AND C.SECTOR='" + sector + "' AND C.MODULO='" + modulo + "' and c.NUMERO='" + numero + "'\n"
                + "and c.DISPONIBLE_OCUPADO in('OCUPADO','DISPONIBLE')\n"
                + "group by c.IDE_CATASTRO,c.DISPONIBLE_OCUPADO");
        tabFuncionario.ejecutarSql();
        desConSql();
        return tabFuncionario;
    }

    public TablaGenerica getConceptoLiquidacion(String lugares, String anio) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select 0 as id, 'PAGO POR RENOVACION DE UN ' +(select DISTINCT DETALLE_LUGAR from CMT_CATASTRO_ARRIENDO_FALLECIDO_VTA \n"
                + "where IDE_CATASTRO in (" + lugares + "))+'(S) EN EL CEMENTERIO MUNICIPAL'+\n"
                + "STUFF(( select CAST('- ' AS varchar(MAX)) + descripcion  \n"
                + "from(select descripcion \n"
                + "from (select DISTINCT 'SECTOR : '+sector+' '+\n"
                + "(select STUFF(( \n"
                + "select CAST(';MODULO : ' AS varchar(MAX)) + a.modulo+' NÚMERO : '+a.numero+' PARA LOS RESTOS DEL SR(A)(S): '+fallecido\n"
                + "from (select DISTINCT modulo,numero,sector,\n"
                + "(select STUFF( \n"
                + "(SELECT CAST(',' AS varchar(MAX)) + nombres \n"
                + "FROM CMT_CATASTRO_ARRIENDO_FALLECIDO_VTA d\n"
                + "where d.IDE_CATASTRO=a.IDE_CATASTRO and d.sector = a.sector and d.modulo=a.modulo\n"
                + "ORDER BY nombres\n"
                + "FOR XML PATH('')), 1, 1, '')) as fallecido\n"
                + "from CMT_CATASTRO a \n"
                + "where a.IDE_CATASTRO in (" + lugares + ") and a.sector = c.sector) a \n"
                + "order by modulo \n"
                + "FOR XML PATH('')), 1, 1, '')) as descripcion\n"
                + "from CMT_CATASTRO_ARRIENDO_FALLECIDO_VTA c\n"
                + "where IDE_CATASTRO in (" + lugares + ")) as a) as b\n"
                + "order by descripcion\n"
                + "FOR XML PATH('')), 1, 1, '')+' POR " + anio + " AÑOS ' as concepto");
        tabFuncionario.ejecutarSql();
//        tabFuncionario.imprimirSql();
        desConSql();
        return tabFuncionario;
    }

    public TablaGenerica getConceptoLiquidacion1(String lugares, String anio) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select 0 as id, 'PAGO POR RENOVACION DE UN ' +DETALLE_LUGAR +'(S) EN EL CEMENTERIO MUNICIPAL SECTOR(S): '+sector +' MODULO(S): '+\n"
                + "modulo+' NUMERO(S): '+numero +' PARA LOS RESTOS DEL SR(A)(S): '+\n"
                + "fallecidos+',POR " + anio + " AÑOS.' as descripcion\n"
                + "from (\n"
                + "select DISTINCT CMT_LUGAR.DETALLE_LUGAR,\n"
                + "(select STUFF((\n"
                + "select CAST(',' AS varchar(MAX)) + a.sector\n"
                + "from (select DISTINCT sector \n"
                + "from CMT_CATASTRO \n"
                + "where IDE_CATASTRO in (" + lugares + ")) a\n"
                + "order by sector\n"
                + "FOR XML PATH('')), 1, 1, '')) as sector,\n"
                + "(select STUFF((\n"
                + "select CAST(',' AS varchar(MAX)) + a.modulo\n"
                + "from (select DISTINCT modulo \n"
                + "from CMT_CATASTRO \n"
                + "where IDE_CATASTRO in (" + lugares + ")) a\n"
                + "order by modulo\n"
                + "FOR XML PATH('')), 1, 1, '')) as modulo,\n"
                + "(select STUFF((\n"
                + "select CAST(',' AS varchar(MAX)) + a.numero\n"
                + "from (select DISTINCT numero \n"
                + "from CMT_CATASTRO \n"
                + "where IDE_CATASTRO in (" + lugares + ")) a\n"
                + "order by numero\n"
                + "FOR XML PATH('')), 1, 1, '')) as numero,\n"
                + "(select STUFF(\n"
                + "(SELECT CAST(',' AS varchar(MAX)) + nombres\n"
                + "FROM SIGAG.dbo.CMT_FALLECIDO\n"
                + "where IDE_CATASTRO in (" + lugares + ")\n"
                + "ORDER BY nombres\n"
                + "FOR XML PATH('')\n"
                + "), 1, 1, '')) as fallecidos\n"
                + "from CMT_CATASTRO \n"
                + "inner join CMT_LUGAR on CMT_CATASTRO.ide_lugar = CMT_LUGAR.ide_lugar\n"
                + "where IDE_CATASTRO in (" + lugares + ")) as a");
        tabFuncionario.ejecutarSql();
//        tabFuncionario.imprimirSql();
        desConSql();
        return tabFuncionario;
    }

    public void executeSprocInParams(Integer id, String busqueda, Integer opcion) {
        conSql();
        try {
            PreparedStatement pstmt = conSql.getConnection().prepareStatement("{call dbo.CMT_DATOS_EXHUMACION(?,?,?)}");
            pstmt.setInt(1, id);
            pstmt.setString(2, busqueda);
            pstmt.setInt(3, opcion);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("EMPLOYEE:");
                System.out.println(rs.getString("CEDULA_FALLECIDO") + ", " + rs.getString("NOMBRES"));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desConSql();
        }
    }

    private void conSql() {
        if (conSql == null) {
            conSql = new Conexion();
            conSql.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        }
    }

    private void desConSql() {
        if (conSql != null) {
            conSql.desconectar(true);
            conSql = null;
        }
    }

    private void conSqler() {
        if (conSqler == null) {
            conSqler = new Conexion();
            conSqler.setUnidad_persistencia(utilitario.getPropiedad("recursojdbcer"));
        }
    }

    private void desConSqler() {
        if (conSqler != null) {
            conSqler.desconectar(true);
            conSqler = null;
        }
    }

    private void conSqlapli() {
        if (conSqlapli == null) {
            conSqlapli = new Conexion();
            conSqlapli.setUnidad_persistencia(utilitario.getPropiedad("recursojdbcapli"));
        }
    }

    private void desconSqlapli() {
        if (conSqlapli != null) {
            conSqlapli.desconectar(true);
            conSqlapli = null;
        }
    }
}

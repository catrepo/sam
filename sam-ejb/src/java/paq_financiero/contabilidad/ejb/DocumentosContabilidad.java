/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.contabilidad.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
@Stateless
public class DocumentosContabilidad {

    private Conexion conSql,//Conexion a la base de sigag
            conOracle;//Cnexion a la base de postgres 2014
    private Utilitario utilitario = new Utilitario();

    public TablaGenerica getOrdenPago(String fecha) {
        conPostgresql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select * from tes_orden_pago where tes_fecha_ingreso = '" + fecha + "' and tes_estado = 'Pendiente'");
        tabPersona.ejecutarSql();
        desPostgresql();
        return tabPersona;
    }

    public TablaGenerica getDocumentos(String fecha, String numero) {
        conPostgresql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("select * from tes_documentos where doc_numero ='" + numero + "' and doc_fecha='" + fecha + "'");
        tabPersona.ejecutarSql();
        desPostgresql();
        return tabPersona;
    }

    public TablaGenerica getDocumentoValidar(Integer numero) {
        conPostgresql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT doc_numero,doc_responsable,doc_concepto,doc_valor,doc_revisiondev,doc_revisioncon \n"
                + "FROM tes_documentos where id_documento =" + numero);
        tabPersona.ejecutarSql();
        desPostgresql();
        return tabPersona;
    }

    public TablaGenerica getDocumentoAfectacion(String numero) {
        conPostgresql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT\n"
                + "fecha,\n"
                + "ide_comprobante,\n"
                + "(case when (SELECT count(*) as valor FROM  pre_mensual where reg_cont=cast(tes_comprobante_pago.orden_pago as integer))>0 then 'Financiero' else 'Tesoreria' end) AS descripcion,\n"
                + "comprobante\n"
                + "FROM tes_comprobante_pago\n"
                + "where orden_pago = '" + numero + "'");
        tabPersona.ejecutarSql();
        desPostgresql();
        return tabPersona;
    }

    public void setOrdenPago(String numero) {
        String auSql = "EXEC SIGAG.dbo.TES_INSERT_DATOS_ORDEN "
                + "@codigo ='" + numero + "',\n"
                + "@tabla ='TES_DOCUMENTOS'";
        conPostgresql();
        conSql.ejecutarSql(auSql);
        desPostgresql();
    }
    
    public void setReingreso(String numero,String login) {
        String auSql = "EXEC SIGAG.dbo.TES_INSERT_REINGRESO_ORDEN "
                + "@codigo ='" + numero + "',\n"
                + "@login ='" + login + "',\n"
                + "@tabla ='TES_DOCUMENTOS'";
        conPostgresql();
        conSql.ejecutarSql(auSql);
        desPostgresql();
    }
    
    public void setTramitesIngreso(String codigo,String fecha,String login,int tipo,String doc_numero,String responsable,String concepto,String Observacion,int estado,int proceso,int id,Double valor) {
        String auSql = "EXEC SIGAG.dbo.TES_INSERT_ACCIONES_TRAMITES "
                + "@codigo =" + codigo + ",\n"
                + "@fecha_rev =" + fecha + ",\n"
                + "@login ='" + login + "',\n"
                + "@tipo =" + tipo + ",\n"
                + "@doc_numero ='" + doc_numero + "',\n"
                + "@responsable ='" + responsable + "',\n"
                + "@concepto ='" + concepto + "',\n"
                + "@observacion ='" + Observacion + "',\n"
                + "@estado =" + estado + ",\n"
                + "@valor =" + valor + ",\n"
                + "@proceso =" + valor + ",\n"
                + "@id =" + id + ",\n"
                + "@tabla ='TES_DOCUMENTOS'";
        conPostgresql();
        conSql.ejecutarSql(auSql);
        desPostgresql();
    }

    public void setOrdenPago1(String numero) {
        String parametro = "insert into tes_documentos (id_tipo,doc_fecha,doc_numero,doc_responsable,doc_valor,doc_concepto)\n"
                + "select 1,tes_fecha_ingreso,tes_numero_orden, tes_proveedor ,tes_valor,tes_concepto from tes_orden_pago where tes_numero_orden = '" + numero + "'";
        conPostgresql();
        conSql.ejecutarSql(parametro);
        desPostgresql();
    }

    public void setReingreso(Integer numero) {
        String parametro = "insert into tes_documentos (id_tipo,doc_fecha,doc_numero,doc_responsable,doc_valor,doc_concepto)\n"
                + "select 1,tes_fecha_ingreso,tes_numero_orden,tes_proveedor,tes_valor,tes_concepto from tes_orden_pago where tes_numero_orden = (SELECT doc_numero FROM tes_documentos where id_documento=" + numero + ")";
        conPostgresql();
        conSql.ejecutarSql(parametro);
        desPostgresql();
    }

    public void setOtroDocumento(Integer tipo, String fecha, String numero, String respon, Double valor, String concepto, String revision, String fechar, String login) {
        String parametro = "insert into tes_documentos (id_tipo,doc_fecha,doc_numero,doc_responsable,doc_valor,doc_concepto,doc_revision,doc_fecharev,doc_loginrev)\n"
                + "values (" + tipo + ",'" + fecha + "','" + numero + "','" + respon + "'," + valor + ",'" + concepto + "','" + revision + "','" + fechar + "','" + login + "')";
        conPostgresql();
        conSql.ejecutarSql(parametro);
        desPostgresql();
    }

        public void setOrdenDocumentos(Integer tipo, String fecha, String numero, String respon, Double valor, String concepto, String revision, String fechar, String login) {
        String auSql = "EXEC SIGAG.dbo.TES_INSERT_DATOS_ORDEN_DOC "
                + "@tipo =" + tipo + ",\n"
                + "@fecha ='" + fecha + "',\n"
                + "@numero ='" + numero + "',\n"
                + "@respon ='" + respon + "',\n"
                + "@valor =" + valor + ",\n"
                + "@concepto ='" + concepto + "',\n"
                + "@revision =" + revision + ",\n"
                + "@fechar ='" + fechar + "',\n"
                + "@login ='" + login + "',\n"
                + "@tabla ='TES_DOCUMENTOS'";
        conPostgresql();
        conSql.ejecutarSql(auSql);
        desPostgresql();
    }
    
    public void setActulizaDocumento(Integer codigo, String descripcion, String descripcion1, String valor, String valor1, String login, String valor2) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_documentos\n"
                + "set " + descripcion + " = " + valor + ","
                + "" + descripcion1 + "=" + valor1 + ",\n"
                + "" + login + "=" + valor2 + "\n"
                + "where id_documento = " + codigo;
        conPostgresql();
        conSql.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void setActulizaDocumentos(Integer codigo, String descripcion, String descripcion1, String valor, String valor2) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_documentos\n"
                + "set " + descripcion + " = " + valor + ","
                + "" + descripcion1 + "=" + valor2 + "\n"
                + "where id_documento = " + codigo;
        conPostgresql();
        conSql.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void setActulizaDocum(Integer codigo, String descripcion, String descripcion1, String descripcion2, String login, Integer valor, String valor1, String valor2, String valor3) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_documentos\n"
                + "set " + descripcion + " = " + valor + ","
                + "" + descripcion1 + "=" + valor1 + ",\n"
                + "" + descripcion2 + "=" + valor2 + ",\n"
                + "" + login + "=" + valor3 + "\n"
                + "where id_documento = " + codigo;
        conPostgresql();
        conSql.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void setActulizaDocum1(Integer codigo, String descripcion, String descripcion1, String descripcion2, String login, String valor, String valor1, String valor2, String valor3) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_documentos\n"
                + "set " + descripcion + " = " + valor + ","
                + "" + descripcion1 + "=" + valor1 + ",\n"
                + "" + descripcion2 + "=" + valor2 + ",\n"
                + "" + login + "=" + valor3 + "\n"
                + "where id_documento = " + codigo;
        conPostgresql();
        conSql.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void setActulizaAdministrador(Integer codigo, String logina, String loginu, String revi, String devo, String observa) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_documentos\n"
                + "set doc_asignacion=1,\n"
                + "doc_loginasi='" + logina + "',\n"
                + "doc_usuasignacion='" + loginu + "',\n"
                + "doc_revisioncon=" + revi + ",\n"
                + "doc_revisiondev=" + devo + ",\n"
                + "doc_observacion='" + observa + "'\n"
                + "where id_documento= " + codigo;
        conPostgresql();
        conSql.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void setDeleteDocumento(Integer anti) {
        String auSql = "delete from tes_documentos where id_documento =" + anti;
        conPostgresql();
        conSql.ejecutarSql(auSql);
        desPostgresql();
    }

    public String maximOrden() {
        conPostgresql();

        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select 0 as id ,(case when max(tes_numero_orden) is null then '0' when max(tes_numero_orden)is not null then max(tes_numero_orden) end) AS maximo\n"
                + "from tes_orden_pago where tes_estado_doc = '1'");
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("maximo");
        desPostgresql();
        return ValorMax;
    }

    public void actOrden(String tipo, Integer numero, String usu, String comentario) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_orden_pago set tes_estado='Anulada'\n"
                + ",tes_nota='ANULADA'\n"
                + ",tes_login_anu ='" + usu + "'\n"
                + ",tes_comentario_anula ='" + comentario + "'\n"
                + ",tes_fecha_anu ='" + utilitario.getFechaActual() + "'\n"
                + "where tes_ide_orden_pago =" + numero + " and tes_numero_orden ='" + tipo + "'";
        conPostgresql();
        conSql.ejecutarSql(strSqlr);
        desPostgresql();
    }

    /*
    conexion a oracle
     */
    public TablaGenerica getBuscar_Datos_Proveedores(String cedRuc) {
        conOraclesql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conOracle);
        tabPersona.setSql("SELECT DISTINCT RUC_CI,NOMBRE,NNITMA,CODTRA \n"
                + "from USFIMRU.VI_LISTA_PROVEEDORES \n"
                + "left join USRRHH.nodattra on USRRHH.nodattra.cedciu = USFIMRU.VI_LISTA_PROVEEDORES.RUC_CI\n"
                + "where ruc_ci = '" + cedRuc + "'");
        tabPersona.ejecutarSql();
        desOraclesql();
        return tabPersona;
    }

        public TablaGenerica getCatalogoDato(String datos, String tabla, String condicion) {
        conPostgresql();
        TablaGenerica tab_funcionario = new TablaGenerica();
        conPostgresql();
        tab_funcionario.setConexion(conSql);
        tab_funcionario.setSql("select " + datos + " from " + tabla + " where " + condicion + "");
        tab_funcionario.ejecutarSql();
        desPostgresql();
        return tab_funcionario;
    }
        
        public TablaGenerica getNumeroCampos(String nombre) {
        conPostgresql();
        TablaGenerica tabTabla = new TablaGenerica();
        conPostgresql();
        tabTabla.setConexion(conSql);
        tabTabla.setSql("SELECT table_name,count(*) As NumeroCampos\n"
                + "FROM information_schema.columns \n"
                + "WHERE table_name = '" + nombre + "'\n"
                + "GROUP BY table_name");
        tabTabla.ejecutarSql();
        desPostgresql();
        return tabTabla;
    }
    
        public TablaGenerica getEstrucTabla(String nombre, Integer posicion) {
        conPostgresql();
        TablaGenerica tabTabla = new TablaGenerica();
        conPostgresql();
        tabTabla.setConexion(conSql);
        tabTabla.setSql("SELECT ordinal_position,column_name, data_type\n"
                + "FROM information_schema.columns \n"
                + "WHERE table_name = '" + nombre + "' and ordinal_position = " + posicion);
        tabTabla.ejecutarSql();
        desPostgresql();
        return tabTabla;
    }

    public void setActuaRegis(Integer codigo, String desc, String dato, String valor, String cadena) {
        String au_sql = "update " + desc + " set\n"
                + "" + dato + " ='" + valor + "'\n" +//mve_horometro
                "where " + cadena + "=" + codigo;
        conPostgresql();
        conSql.ejecutarSql(au_sql);
        desPostgresql();
    }
        
    /*
     * sentencia de conexion a base de datos
     */
    private void conPostgresql() {
        if (conSql == null) {
            conSql = new Conexion();
            conSql.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        }
    }

    private void desPostgresql() {
        if (conSql != null) {
            conSql.desconectar(true);
            conSql = null;
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

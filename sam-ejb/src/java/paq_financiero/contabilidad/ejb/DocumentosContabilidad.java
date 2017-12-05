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
            conPostgres;//Cnexion a la base de postgres 2014
    private Utilitario utilitario = new Utilitario();

    public TablaGenerica getOrdenPago(String fecha) {
        conPostgresql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conPostgres);
        tabPersona.setSql("select * from tes_orden_pago where tes_fecha_ingreso = '" + fecha + "' and tes_estado = 'Pendiente'");
        tabPersona.ejecutarSql();
        desPostgresql();
        return tabPersona;
    }

    public TablaGenerica getDocumentos(String fecha, String numero) {
        conPostgresql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conPostgres);
        tabPersona.setSql("select * from tes_documentos where doc_numero ='" + numero + "' and doc_fecha='" + fecha + "'");
        tabPersona.ejecutarSql();
        desPostgresql();
        return tabPersona;
    }

    public TablaGenerica getDocumentoValidar(Integer numero) {
        conPostgresql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conPostgres);
        tabPersona.setSql("SELECT doc_numero,doc_responsabe,doc_concepto,doc_valor,doc_revisiondev,doc_revisioncon \n"
                + "FROM tes_documentos where id_documento =" + numero);
        tabPersona.ejecutarSql();
        desPostgresql();
        return tabPersona;
    }

    public TablaGenerica getDocumentoAfectacion(String numero) {
        conPostgresql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conPostgres);
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
        String parametro = "insert into tes_documentos (id_tipo,doc_fecha,doc_numero,doc_responsabe,doc_valor,doc_concepto)\n"
                + "select 1,tes_fecha_ingreso,tes_numero_orden,(case when tes_empleado is null then tes_proveedor else tes_empleado end),tes_valor,tes_concepto from tes_orden_pago where tes_numero_orden = '" + numero + "'";
        conPostgresql();
        conPostgres.ejecutarSql(parametro);
        desPostgresql();
    }

    public void setReingreso(Integer numero) {
        String parametro = "insert into tes_documentos (id_tipo,doc_fecha,doc_numero,doc_responsabe,doc_valor,doc_concepto)\n"
                + "select 1,tes_fecha_ingreso,tes_numero_orden,(case when tes_empleado is null then tes_proveedor else tes_empleado end),tes_valor,tes_concepto  from tes_orden_pago where tes_numero_orden = (SELECT doc_numero FROM tes_documentos where id_documento=" + numero + ")";
        conPostgresql();
        conPostgres.ejecutarSql(parametro);
        desPostgresql();
    }

    public void setOtroDocumento(Integer tipo, String fecha, String numero, String respon, Double valor, String concepto, String revision, String fechar, String login) {
        String parametro = "insert into tes_documentos (id_tipo,doc_fecha,doc_numero,doc_responsabe,doc_valor,doc_concepto,doc_revision,doc_fecharev,doc_loginrev)\n"
                + "values (" + tipo + ",'" + fecha + "','" + numero + "','" + respon + "'," + valor + ",'" + concepto + "','" + revision + "','" + fechar + "','" + login + "')";
        conPostgresql();
        conPostgres.ejecutarSql(parametro);
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
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void setActulizaDocumentos(Integer codigo, String descripcion, String descripcion1, String valor, String valor2) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_documentos\n"
                + "set " + descripcion + " = " + valor + ","
                + "" + descripcion1 + "=" + valor2 + "\n"
                + "where id_documento = " + codigo;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
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
        conPostgres.ejecutarSql(strSqlr);
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
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void setActulizaAdministrador(Integer codigo, String logina, String loginu, String revi, String devo, String observa) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_documentos\n"
                + "set doc_asignacion='t',\n"
                + "doc_loginasi='" + logina + "',\n"
                + "doc_usuasignacion='" + loginu + "',\n"
                + "doc_revisioncon=" + revi + ",\n"
                + "doc_revisiondev=" + devo + ",\n"
                + "doc_observacion='" + observa + "'\n"
                + "where id_documento= " + codigo;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void setDeleteDocumento(Integer anti) {
        String auSql = "delete from tes_documentos where id_documento =" + anti;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }

    /*
     * sentencia de conexion a base de datos
     */
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
}

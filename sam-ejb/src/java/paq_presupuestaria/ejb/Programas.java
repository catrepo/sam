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
 * @author p-sistemas
 */
@Stateless
public class Programas {

    private Conexion conPostgres, conOracle, conSQL;
    private Utilitario utilitario = new Utilitario();

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    /*
     * GENERAL
     */
    //borrar tabla conc_cedula_presupuestaria_fechas
    public Programas() {
    }

    public void actualizacionPrograma() {
        String strSqlr = "insert into conc_cedula_presupuestaria_fechas (ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,nivel,ide_funcion,des_funcion,cod_funcion)\n"
                + "select ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,nivel,ide_funcion,des_funcion,cod_funcion\n"
                + "from conc_clasificador,pre_funcion_programa\n"
                + "order by ide_funcion,pre_codigo";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    /*
     * INGRESOS CONSOLIDADOS
     */
    public void eiminarIngreso() {
        String strSqlr = "delete from conc_cedula_presupuestaria_fechas";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void setEiminarMovimiento(Integer ide) {
        String strSqlr = "delete from cont_detalle_movimiento where ide_detalle_mov = " + ide;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void insertaIngresos(Integer ano, Integer tipo, String fecha) {
        // Forma el sql para el ingreso
        String strSqlr = "insert into conc_cedula_presupuestaria_fechas (ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,ano_curso,nivel,fechaced)"
                + "select ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo," + ano + ",nivel,'" + fecha + "' from conc_clasificador"
                + " where tipo =" + tipo + " order by pre_codigo ";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();

    }

    public void setCuentaContable(Integer cuenta, Integer movimiento, Double debe, Double haber, Double devengado, String descripcion, String tipo, String deposito) {
        // Forma el sql para el ingreso
        String strSqlr = "insert into cont_detalle_movimiento (ide_cuenta,ide_movimiento,mov_debe,mov_haber,mov_devengado,mov_descripcion,ide_tipo_movimiento,doc_deposito)\n"
                + "values (" + cuenta + "," + movimiento + "," + debe + "," + haber + "," + devengado + ",'" + descripcion + "','" + tipo + "','" + deposito + "')";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();

    }

    public void setCuentaConta(Integer cuenta, Integer movimiento, Double debe, Double haber, Double devengado) {
        // Forma el sql para el ingreso
        String strSqlr = "insert into cont_detalle_movimiento (ide_cuenta,ide_movimiento,mov_debe,mov_haber,mov_devengado,mov_descripcion,ide_tipo_movimiento,doc_deposito)\n"
                + "values(" + cuenta + "," + movimiento + "," + debe + "," + haber + "," + devengado + ",'S/D','F','0')";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();

    }

    public void actualizarIngresos(Integer combo) {
        // Forma el sql para actualizacion
        String strSqlr = "update conc_cedula_presupuestaria_fechas \n"
                + "set reforma1= 0, devengado1=0, pagado1=0, cobrado1=0, compromiso1=0, cobradoc1=0,val_inicial=0 where tipo=" + combo + "";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarComprobante(Integer combo) {
        // Forma el sql para actualizacion
        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado \n"
                + "set numero_cuenta=link.numero_cuenta,  \n"
                + "tipo_cuenta=link.cod_cuenta,  \n"
                + "codigo_banco=link.codigo_banco,\n"
                + "ban_nombre=link.ban_nombre\n"
                + "from ((select ide_detalle_listado,ide_listado, \n"
                + "item, \n"
                + "comprobante, \n"
                + "cedula_pass_beneficiario, \n"
                + "nombre_beneficiario,numero_cuenta, \n"
                + "cod_cuenta, valor,\n"
                + "ban_nombre,codigo_banco from(\n"
                + "(SELECT ide_detalle_listado, \n"
                + "ide_listado, \n"
                + "item, \n"
                + "comprobante, \n"
                + "cedula_pass_beneficiario, \n"
                + "nombre_beneficiario,valor\n"
                + "FROM \n"
                + "tes_detalle_comprobante_pago_listado \n"
                + "where ide_listado = " + combo + ") as aa\n"
                + "inner join\n"
                + "(SELECT  \n"
                + "e.cedula_pass, \n"
                + "e.numero_cuenta, \n"
                + "e.cod_cuenta, \n"
                + "b.codigo_banco,\n"
                + "b.ban_nombre\n"
                + "FROM \n"
                + "srh_empleado e , \n"
                + "ocebanco b  \n"
                + "where e.cod_banco = b.ban_codigo) as bb\n"
                + "on aa.cedula_pass_beneficiario = bb.cedula_pass))\n"
                + "union\n"
                + "(select ide_detalle_listado,ide_listado, \n"
                + "item, \n"
                + "comprobante, \n"
                + "cedula_pass_beneficiario, \n"
                + "nombre_beneficiario, \n"
                + "numero_cuenta, \n"
                + "cod_cuenta, valor,ban_nombre ,\n"
                + "codigo_banco from(\n"
                + "(SELECT ide_detalle_listado, \n"
                + "ide_listado, \n"
                + "item, \n"
                + "comprobante, \n"
                + "cedula_pass_beneficiario, \n"
                + "nombre_beneficiario,valor\n"
                + "FROM \n"
                + "tes_detalle_comprobante_pago_listado \n"
                + "where ide_listado = " + combo + ") as aa\n"
                + "inner join\n"
                + "(SELECT \n"
                + "p.ide_proveedor, \n"
                + "p.ruc, \n"
                + "p.titular, \n"
                + "p.ban_codigo, \n"
                + "p.numero_cuenta,\n"
                + "(case when p.tipo_cuenta = 'C' then 1  when p.tipo_cuenta = 'A' then 2 end) \n"
                + "as cod_cuenta, \n"
                + "o.codigo_banco, \n"
                + "o.ban_nombre \n"
                + "FROM \n"
                + "tes_proveedores p , \n"
                + "ocebanco o \n"
                + "where p.ban_codigo = o.ban_codigo) as cc\n"
                + "on aa.cedula_pass_beneficiario= cc.ruc) \n"
                + "where cedula_pass_beneficiario!='0')) as link\n"
                + "where tes_detalle_comprobante_pago_listado.ide_detalle_listado =link.ide_detalle_listado  and \n"
                + "tes_detalle_comprobante_pago_listado.ide_listado =link.ide_listado and tes_detalle_comprobante_pago_listado.item =link.item and \n"
                + "tes_detalle_comprobante_pago_listado.cedula_pass_beneficiario=link.cedula_pass_beneficiario and \n"
                + "tes_detalle_comprobante_pago_listado.comprobante =link.comprobante and tes_detalle_comprobante_pago_listado.ide_estado_listado = (SELECT ide_estado_listado FROM tes_estado_listado WHERE estado like 'ENVIADO')";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarDatosIngresos(String ini, String fin, Integer anio, Integer lice) {
        // Forma1 el sql para actualizacion
        String strSqlr = "update conc_cedula_presupuestaria_fechas set \n"
                + "reforma1 = reforma \n"
                + "from ( select ide_clasificador, ((case when sum(debito) is null then 0 else sum(debito) end) -(case when sum(credito) is null then 0 else sum(credito) end) ) as reforma\n"
                + "from pre_anual a,( select sum (val_reforma_d) as debito,sum(val_reforma_h) as credito,ide_pre_anual  from pre_reforma_mes where fecha_reforma between '" + ini + "'"
                + " and '" + fin + "' group by ide_pre_anual) b where a.ide_pre_anual=b.ide_pre_anual and ano=" + anio + " and not ide_clasificador is null group by ide_clasificador ) \n"
                + " a where a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador; update conc_cedula_presupuestaria_fechas set val_inicial = inicial from \n"
                + " ( select sum (val_presupuestado_i) as inicial, ide_clasificador from pre_anual where  ano=" + anio + " and not ide_clasificador is null group by ide_clasificador ) a \n"
                + " where a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador; update conc_cedula_presupuestaria_fechas  \n"
                + " set cobradoc1= comprometido, devengado1=devengado from ( select b.ide_clasificador,ano,tipo, \n"
                + " sum( (case when cobradoc is null then 0 else cobradoc end ) )as comprometido, \n"
                + " sum((case when devengado is null then 0 else devengado end) ) as devengado from pre_anual a,\n"
                + "  conc_clasificador b,pre_mensual c where a.ide_clasificador=b.ide_clasificador and a.ide_pre_anual = c.ide_pre_anual and ano=" + anio + ""
                + "  and fecha_ejecucion between '" + ini + "' and '" + fin + "' and tipo= " + lice + " group by b.ide_clasificador,ano,tipo order by tipo ) a \n"
                + "  where a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarDatos2() {
        String strSqlr = "update conc_cedula_presupuestaria_fechas set reforma1 = reforma \n"
                + "from ( select sum(reforma1) as reforma,con_ide_clasificador \n"
                + "from conc_cedula_presupuestaria_fechas group by con_ide_clasificador ) a where a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarDatosg3() {
        String strSqlr = "update conc_cedula_presupuestaria_fechas set val_inicial = reforma from ( select sum(val_inicial) as reforma,con_ide_clasificador \n"
                + "from conc_cedula_presupuestaria_fechas group by con_ide_clasificador ) a where a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";

        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarDatosg4(Integer ani, Integer tip) {
        String strSqlr = "update conc_cedula_presupuestaria_fechas  set cobradoc1= conbrado, devengado1=devengado \n"
                + "from ( select sum((case when cobradoc1 is null then 0 else cobradoc1 end)) as conbrado, \n"
                + "sum((case when devengado1 is null then 0 else devengado1 end)) as devengado, con_ide_clasificador \n"
                + "from conc_cedula_presupuestaria_fechas  where ano_curso=" + ani + " and tipo= " + tip + " group by  con_ide_clasificador ) a \n"
                + "where a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }
    /*
     * GASTOS PROGRAMAS
     */

    public void insertarGastos(Integer an, String fec, Integer ti) {
        // Forma el sql para insertar
        String strSqlr = "insert into conc_cedula_presupuestaria_fechas (ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo,ano_curso,nivel,\n"
                + "ide_funcion,des_funcion,cod_funcion,fechaced)\n"
                + "select ide_clasificador,pre_codigo,con_ide_clasificador,pre_descripcion,tipo," + an + ",nivel,ide_funcion,des_funcion,cod_funcion,'" + fec + "'\n"
                + "from conc_clasificador,pre_funcion_programa\n"
                + "where tipo = " + ti + ""
                + "order by ide_funcion,pre_codigo";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void insertRegistro(Integer lista, Integer item, Integer detalle, String comprobar) {
        // Forma el sql para insertar
        String strSqlr = "insert into tes_registro_pagos_listado( ide_listado,item,comprobante,cedula_pass_beneficiario,\n"
                + "nombre_beneficiario,valor,numero_cuenta,ban_nombre,tipo_cuenta,comentario,codigo_banco,\n"
                + "num_documento,ide_estado_listado,ide_detalle_listado,fecha_accion,num_transferencia)\n"
                + "SELECT ide_listado ,item ,comprobante ,cedula_pass_beneficiario ,nombre_beneficiario ,\n"
                + "valor ,numero_cuenta ,ban_nombre ,tipo_cuenta ,comentario ,codigo_banco ,num_documento ,\n"
                + "ide_estado_listado,ide_detalle_listado,'" + utilitario.getFechaActual() + "',num_transferencia\n"
                + "FROM tes_detalle_comprobante_pago_listado\n"
                + "where ide_listado=" + lista + " and item =" + item + " and comprobante like '" + comprobar + "' and ide_detalle_listado =" + detalle;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarGastos(Integer cen) {
        // Forma el sql para actualizar
        String strSqlr = "update conc_cedula_presupuestaria_fechas \n"
                + "set reforma1= 0, devengado1=0, pagado1=0, cobrado1=0, compromiso1=0, cobradoc1=0,val_inicial=0 where tipo= " + cen;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarGastos1(String in, String fi, Integer an, Integer lic) {
        // Forma el sql para actualizar 1
        String strSqlr = "update conc_cedula_presupuestaria_fechas\n"
                + "set reforma1 = reforma\n"
                + "from (select a.ide_clasificador,sum(reforma) as reforma,ide_funcion\n"
                + "from prec_programas a, (select ide_programa,\n"
                + "((case when sum(debito) is null then 0 else sum(debito) end) -(case when sum(credito) is null then 0 else sum(credito) end) ) as reforma\n"
                + "from pre_anual a,(select sum (val_reforma_d) as debito,sum(val_reforma_h) as credito,ide_pre_anual \n"
                + "from pre_reforma_mes where fecha_reforma between '" + in + "' and '" + fi + "'\n"
                + "group by ide_pre_anual) b\n"
                + "where a.ide_pre_anual=b.ide_pre_anual\n"
                + "and ano=" + an + " and not ide_programa is null\n"
                + "group by ide_programa ) b\n"
                + "where a.ide_programa = b.ide_programa\n"
                + "group by a.ide_clasificador,ide_funcion) a\n"
                + "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo=" + lic + " and  a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarGastos2(Integer ani, Integer licn) {
        // Forma el sql para actualizar 2
        String str_sql4 = "update conc_cedula_presupuestaria_fechas\n"
                + "set val_inicial = inicial\n"
                + "from (select ide_clasificador,sum(inicial) as inicial,ide_funcion\n"
                + "from prec_programas a,(select sum (val_presupuestado_i) as inicial, ide_programa\n"
                + "from pre_anual\n"
                + "where  ano=" + ani + " and not  ide_programa is null\n"
                + "group by ide_programa) b\n"
                + "where a.ide_programa=b.ide_programa\n"
                + "group by ide_clasificador,ide_funcion) a\n"
                + "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo=" + licn + " and  a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        conPostgresql();
        conPostgres.ejecutarSql(str_sql4);
        desPostgresql();
    }

    public void actualizarTablaGastos3(Integer anio, String ini, String fin, Integer licen) {
        // Forma el sql para actualizar 3
        String str_sql5 = "update conc_cedula_presupuestaria_fechas \n"
                + "set compromiso1= comprometido,\n"
                + "devengado1=devengado,\n"
                + "pagado1=pagado\n"
                + "from (select ide_clasificador,sum(comprometido) as comprometido,sum(devengado) as devengado,sum(pagado) as pagado,ide_funcion\n"
                + "from prec_programas a, (select b.ide_programa,ano,\n"
                + "sum( (case when comprometido is null then 0 else comprometido end ) )as comprometido,\n"
                + "sum((case when devengado is null then 0 else devengado end) ) as devengado,\n"
                + "sum((case when pagado is null then 0 else pagado end) ) as pagado\n"
                + "from pre_anual a, prec_programas b,pre_mensual c\n"
                + "where a.ide_programa=b.ide_programa\n"
                + "and a.ide_pre_anual = c.ide_pre_anual and ano=" + anio + "\n"
                + "and fecha_ejecucion between '" + ini + "' and '" + fin + "'\n"
                + "group by b.ide_programa,ano) b\n"
                + "where a.ide_programa=b.ide_programa\n"
                + "group by ide_clasificador,ide_funcion) a\n"
                + "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion \n"
                + "and conc_cedula_presupuestaria_fechas.tipo=" + licen + " and a.ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        conPostgresql();
        conPostgres.ejecutarSql(str_sql5);
        desPostgresql();
    }

    public void actualizarTablaGastos4(Integer licn) {
        // Forma el sql para actualizar 4
        String str_sql6 = "update conc_cedula_presupuestaria_fechas\n"
                + "set reforma1 = reforma\n"
                + "from (select sum(reforma1) as reforma,con_ide_clasificador,ide_funcion,tipo\n"
                + "from conc_cedula_presupuestaria_fechas\n"
                + "group by con_ide_clasificador,ide_funcion,tipo having tipo=" + licn + ") a\n"
                + "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo=a.tipo \n"
                + "and  a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        conPostgresql();
        conPostgres.ejecutarSql(str_sql6);
        desPostgresql();
    }

    public void actualizarTablaGastos5(Integer lice, Integer ano) {
        // Forma el sql para actualizar 5
        String str_sql7 = "update conc_cedula_presupuestaria_fechas\n"
                + "set val_inicial = reforma\n"
                + "from (select sum(val_inicial) as reforma,con_ide_clasificador,ide_funcion,tipo\n"
                + "from conc_cedula_presupuestaria_fechas\n"
                + "group by con_ide_clasificador,ide_funcion,tipo having tipo=" + lice + ") a\n"
                + "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo=a.tipo \n"
                + "and a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador;\n"
                + "update conc_cedula_presupuestaria_fechas \n"
                + "set compromiso1= comprometido,\n"
                + "devengado1=devengado,\n"
                + "pagado1=pagado\n"
                + "from (select sum((case when compromiso1 is null then 0 else compromiso1 end)) as comprometido,\n"
                + "sum((case when devengado1 is null then 0 else devengado1 end)) as devengado,\n"
                + "sum((case when pagado1 is null then 0 else pagado1 end)) as pagado,\n"
                + "con_ide_clasificador,ide_funcion,tipo\n"
                + "from conc_cedula_presupuestaria_fechas \n"
                + "where ano_curso= " + ano + " and tipo= " + lice + "\n"
                + "group by  con_ide_clasificador,ide_funcion,tipo ) a\n"
                + "where a.ide_funcion = conc_cedula_presupuestaria_fechas.ide_funcion and conc_cedula_presupuestaria_fechas.tipo=a.tipo \n"
                + "and a.con_ide_clasificador = conc_cedula_presupuestaria_fechas.ide_clasificador";
        conPostgresql();
        conPostgres.ejecutarSql(str_sql7);
        desPostgresql();
    }

    public void actuProveedor(Integer provee, String titular, Integer banco, String cuenta, String tipcuen, String activida, String fono, Integer tiprov, String fono1, String ruc, String cuban, String usu) {
        String proveedor = "UPDATE tes_proveedores set titular = '" + titular + "' ,ban_codigo =" + banco + ",numero_cuenta ='" + cuenta + "',\n"
                + "tipo_cuenta = '" + tipcuen + "',actividad ='" + activida + "',telefono1 ='" + fono + "',ide_tipo_proveedor =" + tiprov + " ,\n"
                + "telefono2 ='" + fono1 + "' ,ruc ='" + ruc + "',codigo_banco ='" + cuban + "',\n"
                + "usuario_actua ='" + usu + "',ip_actua = '" + utilitario.getIp() + "',fecha_actua ='" + utilitario.getFechaActual() + "' WHERE ide_proveedor =" + provee;
        conPostgresql();
        conPostgres.ejecutarSql(proveedor);
        desPostgresql();
    }

    public void actuEmpleado(Integer banco, String numero, String usu, Integer ci, Integer tipo) {
        String empleado = "Update srh_empleado set cod_banco=" + banco + ",numero_cuenta='" + numero + "',ip_responsable ='" + utilitario.getIp() + "',"
                + "nom_responsable='" + usu + "',cod_cuenta=" + tipo + ",fecha_responsable='" + utilitario.getFechaActual() + "'WHERE cod_empleado = " + ci;
        conPostgresql();
        conPostgres.ejecutarSql(empleado);
        desPostgresql();
    }

    public void actuListado(String ci, String respon, String usu, Integer ide) {
        String strSqlr = "UPDATE  tes_comprobante_pago_listado \n"
                + "set fecha_pagado = '" + utilitario.getFechaActual() + "',ci_paga='" + ci + "' ,responsable_paga='" + respon + "',"
                + "usuario_actua_paga='" + usu + "',ip_actua_paga='" + utilitario.getIp() + "'\n"
                + "WHERE ide_listado =" + ide;

        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actuaComprobante(String cuenta, String nombre, String tipo, String usu, String comprobante, Integer lista, Integer detalle) {

        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado\n"
                + "set usuario_ingre_envia='" + usu + "',\n"
                + "ip_ingre_envia='" + utilitario.getIp() + "',\n"
                + "ide_estado_listado=(SELECT ide_estado_listado FROM tes_estado_listado WHERE estado like 'PAGADO')\n"
                + "WHERE comprobante like '" + comprobante + "' and ide_listado =" + lista + " and ide_detalle_listado =" + detalle;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void devolverComprobante(String usua, String comprobante, Integer lista, Integer detalle, Integer item) {

        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado \n"
                + "set usuario_actua_devolucion ='" + usua + "',\n"
                + "ip_actua_devolucion='" + utilitario.getIp() + "',\n"
                + "ide_estado_listado=(SELECT ide_estado_listado FROM tes_estado_listado WHERE estado like 'DEVUELTO') \n"
                + "WHERE comprobante like '" + comprobante + "' and ide_listado = " + lista + " and ide_detalle_listado =" + detalle + " and item=" + item;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void devolverLista(String cedula, Integer lista, Integer item) {

        String strSqlr = "UPDATE tes_comprobante_pago_listado \n"
                + "set devolucion = (SELECT ide_estado_listado FROM tes_estado_listado WHERE estado like 'DEVUELTO')\n"
                + "WHERE ci_envia like '" + cedula + "' and ide_listado = " + lista + "and item=" + item;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void ActuaListaComp(String comprobante) {

        String strSqlr = "UPDATE tes_comprobante_pago\n"
                + "set estado_comprobante = (SELECT ide_estado_listado FROM tes_estado_listado WHERE estado LIKE 'PENDIENTE')\n"
                + "where comprobante = '" + comprobante + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void regreComprobante(String cuenta, String usu, String comprobante, Integer lista, Integer detalle) {

        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado\n"
                + "set numero_cuenta='" + cuenta + "',\n"
                + "usuario_ingre_envia='" + usu + "',\n"
                + "ip_ingre_envia='" + utilitario.getIp() + "',\n"
                + "ide_estado_listado=(SELECT ide_estado_listado FROM tes_estado_listado WHERE estado like 'ENVIADO')\n"
                + "WHERE comprobante like '" + comprobante + "' and ide_listado =" + lista + " and ide_detalle_listado =" + detalle;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void rechazoComprobante(String cuenta, String comprobante, Integer lista, String comentario) {

        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado \n"
                + "set ide_estado_listado=(SELECT ide_estado_listado FROM tes_estado_listado WHERE estado like 'DEVUELTO'),comentario = '" + comentario + "'\n"
                + "WHERE comprobante like'" + comprobante + "'  and ide_listado =" + lista + " and num_documento like'" + cuenta + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void regresoRechazo(String cuenta, String comprobante, Integer lista) {

        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado \n"
                + "set ide_estado_listado=(SELECT ide_estado_listado FROM tes_estado_listado WHERE estado like 'PAGADO'),comentario = null\n"
                + "WHERE comprobante like'" + comprobante + "'  and ide_listado =" + lista + " and num_documento like'" + cuenta + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void numTransferencia(String cuenta, String comprobante, Integer lista, String trans) {

        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado \n"
                + "set num_transferencia='" + trans + "'\n"
                + "WHERE comprobante like'" + comprobante + "' and ide_listado =" + lista + " and num_documento like'" + cuenta + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void estadoLisCom(String cuenta) {

        String strSqlr = "UPDATE tes_comprobante_pago\n"
                + "SET estado_comprobante = (SELECT ide_estado_listado FROM tes_estado_listado where estado ='PAGADO')\n"
                + "WHERE comprobante = '" + cuenta + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void numTransComprobante(String numero, String fecha, String comprobante, Integer lista, Integer detalle) {

        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado\n"
                + "set num_documento='" + numero + "',\n"
                + "fecha_transferencia='" + fecha + "'\n"
                + "WHERE comprobante like '" + comprobante + "' and ide_listado =" + lista + " and ide_detalle_listado =" + detalle;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarComprobante(Integer codigo, String tipo, Integer detalle, Integer listado, String usu, String registro, Integer estado) {

        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado \n"
                + "set ban_codigo=" + codigo + ",\n"
                + "tipo_cuenta='" + tipo + "',usuario_actua_pagado='" + usu + "',ide_estado_detalle_listado =" + estado + " ,ban_nombre = (SELECT o.ban_nombre FROM ocebanco o WHERE o.ban_codigo =" + codigo + "),num_transferencia = '" + registro + "',ip_actua_pagado='" + utilitario.getIp() + "'\n"
                + "WHERE ide_detalle_listado=" + detalle + " and ide_listado =" + listado;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actuLis(String ci, String respon, Integer ide) {
        String strSqlr = "UPDATE tes_comprobante_pago_listado\n"
                + "set fecha_devolucion='" + utilitario.getFechaActual() + "',ci_devolucion='" + ci + "',usuario_actua_devolucion='" + respon + "',"
                + "ip_actua_devolucion ='" + utilitario.getIp() + "'\n"
                + "where ide_listado = " + ide;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actuLisDevol(Integer ide) {
        String strSqlr = "UPDATE tes_comprobante_pago_listado\n"
                + "set devolucion='1' where ide_listado = " + ide;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actuLisDevolver(Integer ide, Integer item) {
        String strSqlr = "UPDATE tes_comprobante_pago_listado\n"
                + "set estado=(SELECT ide_estado_listado FROM tes_estado_listado WHERE estado like 'CERRADO') where item=" + item + " and ide_listado = " + ide;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actuCuentasBanco(String numero, String nombre, String tipo, String codigo, Integer ide, Integer lis, String compro, String cedula) {
        String strSqlr = "update tes_detalle_comprobante_pago_listado\n"
                + "set numero_cuenta ='" + numero + "',\n"
                + "ban_nombre ='" + nombre + "',\n"
                + "tipo_cuenta ='" + tipo + "',\n"
                + "codigo_banco ='" + codigo + "'\n"
                + "WHERE\n"
                + "ide_detalle_listado =" + ide + " and\n"
                + "ide_listado =" + lis + " and\n"
                + "comprobante like '" + compro + "' and\n"
                + "cedula_pass_beneficiario like '" + cedula + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actuComponente(String cuenta, Integer codigo, String tipo, Integer detalle, Integer listado, String usu, String registro, Integer estado) {
        String strSqlr = "UPDATE tes_detalle_comprobante_pago_listado\n"
                + "set numero_cuenta ='" + cuenta + "',ban_codigo=" + codigo + ",ban_nombre=(SELECT ban_nombre FROM ocebanco WHERE ban_codigo =" + codigo + "),"
                + "tipo_cuenta='" + tipo + "',usuario_actua_devolucion='" + usu + "',num_transferencia='" + registro + "',ip_actua_devolucion='" + utilitario.getIp() + "',ide_estado_detalle_listado=" + estado + "\n"
                + "where ide_detalle_listado = " + detalle + " and ide_listado = " + listado;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actualizarDetalleC(String tipo, String numero, String banco, Integer detalle, Integer lista, Integer item, String comprobante, String cedula) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_detalle_comprobante_pago_listado\n"
                + "set tipo_cuenta ='" + tipo + "',\n"
                + "numero_cuenta='" + numero + "',\n"
                + "codigo_banco='" + banco + "'\n"
                + "where \n"
                + "ide_detalle_listado=" + detalle + " and\n"
                + "ide_listado=" + lista + " and\n"
                + "item=" + item + " and\n"
                + "comprobante= '" + comprobante + "'and\n"
                + "cedula_pass_beneficiario='" + cedula + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
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
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actCertificado(String tipo, Integer numero, String usu, String comentario) {
        // Forma el sql para actualizacion
        String strSqlr = "update cert_fondos "
                + "set estado='Anulado' , login_anulacion ='" + usu + "',observacion='" + comentario + "',fecha_anulacion='" + utilitario.getFechaActual() + "' "
                + "where id_codigo=" + numero + " and numero_certificado='" + tipo + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void setOrdenPago(String tipo, Integer numero, String etiqueta, String campo) {
        String str_sql = "update tes_orden_pago\n"
                + "set " + etiqueta + "=" + campo + " \n"
                + "where tes_ide_orden_pago = " + numero + " and tes_numero_orden = '" + tipo + "'";
        conPostgresql();
        conPostgres.ejecutarSql(str_sql);
        desPostgresql();
    }

    public void setOrdenPagos(String tipo, Integer numero, String etiqueta, Double campo) {
        String str_sql = "update tes_orden_pago\n"
                + "set " + etiqueta + "=" + campo + " \n"
                + "where tes_ide_orden_pago = " + numero + " and tes_numero_orden = '" + tipo + "'";
        conPostgresql();
        conPostgres.ejecutarSql(str_sql);
        desPostgresql();
    }

    public void actOrdenTotalPro(String tipo, Integer numero, String asunto, Integer idp, String proveedor, Double valor, String letras, String concepto, String acuerdo, String nota, String comprobante, String fecha, String estado, String usu, String fechaen, String tipoe) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_orden_pago set\n"
                + "tes_asunto ='" + asunto + "',\n"
                + "tes_id_proveedor=" + idp + ",\n"
                + "tes_proveedor='" + proveedor + "',\n"
                + "tes_valor=" + valor + ",\n"
                + "tes_valor_letras='" + letras + "',\n"
                + "tes_concepto='" + concepto + "',\n"
                + "tes_acuerdo='" + acuerdo + "',\n"
                + "tes_nota='" + nota + "',\n"
                + "tes_comprobante_egreso='" + comprobante + "',\n"
                + "tes_fecha_comprobante='" + fecha + "',\n"
                + "tes_estado='" + estado + "',\n"
                + "tes_login_envio='" + usu + "',\n"
                + "tipo_solicitantep='" + tipoe + "',\n"
                + "tes_fecha_envio='" + fechaen + "'\n"
                + //                "tes_fecha_actu='"+utilitario.getFechaActual()+"'\n" +
                "where  tes_ide_orden_pago = " + numero + " and tes_numero_orden = '" + tipo + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actOrdenTotalPro1(String tipo, Integer numero, String asunto, Integer idp, String proveedor, Double valor, String letras, String concepto, String acuerdo, String nota, String estado, String usu) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_orden_pago set\n"
                + "tes_asunto ='" + asunto + "',\n"
                + "tes_id_proveedor=" + idp + ",\n"
                + "tes_proveedor='" + proveedor + "',\n"
                + "tes_valor=" + valor + ",\n"
                + "tes_valor_letras='" + letras + "',\n"
                + "tes_concepto='" + concepto + "',\n"
                + "tes_acuerdo='" + acuerdo + "',\n"
                + "tes_nota='" + nota + "',\n"
                + "tes_estado='" + estado + "',\n"
                + "tes_login_mod='" + usu + "',\n"
                + "tes_fecha_mod='" + utilitario.getFechaActual() + "'\n"
                + "where  tes_ide_orden_pago = " + numero + " and tes_numero_orden = '" + tipo + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actOrdenTotalEmp(String tipo, Integer numero, String asunto, Integer ide, String empleado, Double valor, String letras, String concepto, String acuerdo, String nota, String comprobante, String fecha, String estado, String usu, String fechaen, String tipoa) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_orden_pago set\n"
                + "tes_asunto ='" + asunto + "',\n"
                + "tes_cod_empleado=" + ide + ",\n"
                + "tes_empleado='" + empleado + "',\n"
                + "tes_valor=" + valor + ",\n"
                + "tes_valor_letras='" + letras + "',\n"
                + "tes_concepto='" + concepto + "',\n"
                + "tes_acuerdo='" + acuerdo + "',\n"
                + "tes_nota='" + nota + "',\n"
                + "tes_comprobante_egreso='" + comprobante + "',\n"
                + "tes_fecha_comprobante='" + fecha + "',\n"
                + "tes_estado='" + estado + "',\n"
                + "tes_login_envio='" + usu + "',\n"
                + "tipo_solicitantep='" + tipoa + "',\n"
                + "tes_fecha_envio='" + fechaen + "'\n"
                + //                "tes_fecha_actu='"+utilitario.getFechaActual()+"'\n" +
                "where  tes_ide_orden_pago = " + numero + " and tes_numero_orden = '" + tipo + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void actOrdenTotalEmp1(String tipo, Integer numero, String asunto, Integer ide, String empleado, Double valor, String letras, String concepto, String acuerdo, String nota, String estado, String usu) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_orden_pago set\n"
                + "tes_asunto ='" + asunto + "',\n"
                + "tes_cod_empleado=" + ide + ",\n"
                + "tes_empleado='" + empleado + "',\n"
                + "tes_valor=" + valor + ",\n"
                + "tes_valor_letras='" + letras + "',\n"
                + "tes_concepto='" + concepto + "',\n"
                + "tes_acuerdo='" + acuerdo + "',\n"
                + "tes_nota='" + nota + "',\n"
                + "tes_estado='" + estado + "',\n"
                + "tes_login_mod='" + usu + "',\n"
                + "tes_fecha_mod='" + utilitario.getFechaActual() + "'\n"
                + "where  tes_ide_orden_pago = " + numero + " and tes_numero_orden = '" + tipo + "'";
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void setActuDetallePag(Integer codigo, String cuenta, String banco, String nombre, Integer detalle, String comprobante, Integer item, String cuentan) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_detalle_comprobante_pago_listado\n"
                + "set ban_codigo=" + codigo + ",\n"
                + "tipo_cuenta ='" + cuenta + "',\n"
                + "codigo_banco='" + banco + "',\n"
                + "numero_cuenta='" + cuentan + "',\n"
                + "ban_nombre='" + nombre + "'\n"
                + "where ide_listado=" + detalle + " and comprobante ='" + comprobante + "'  and item=" + item;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void setActulizaOrdenPago(Integer codigo, String descripcion, String descripcion1, String valor, String valor1, String login, String valor2) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_orden_pago\n"
                + "set " + descripcion + " = " + valor + ","
                + "" + descripcion1 + "=" + valor1 + ","
                + "" + login + "=" + valor2 + "\n"
                + "where tes_ide_orden_pago = " + codigo;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public void setActuOrdenPago(Integer codigo, String descripcion, String descripcion1, String valor, String valor1) {
        // Forma el sql para actualizacion
        String strSqlr = "update tes_orden_pago\n"
                + "set " + descripcion + " = " + valor + ","
                + "" + descripcion1 + "=" + valor1 + "\n"
                + "where tes_ide_orden_pago = " + codigo;
        conPostgresql();
        conPostgres.ejecutarSql(strSqlr);
        desPostgresql();
    }

    public TablaGenerica periodo(Integer periodo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_tipo_cuenta,tipo_cuenta,cuenta FROM ocebanco_tipo_cuenta WHERE ide_tipo_cuenta =" + periodo);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getEmpleado(Integer periodo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select cod_empleado,nombres,cedula_pass from srh_empleado where cod_empleado=" + periodo);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getProveedor(Integer periodo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_proveedor,titular FROM tes_proveedores where ide_proveedor=" + periodo + " order by titular");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica banco(Integer banco) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ban_codigo,ban_nombre,num_banco FROM ocebanco WHERE ban_codigo =" + banco);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getTranferencia(Integer iden) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_detalle_listado,ide_listado,num_documento FROM tes_detalle_comprobante_pago_listado where ide_detalle_listado =" + iden);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica item(Integer banco) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT \n"
                + " ide_listado, \n"
                + " item, \n"
                + " fecha_listado, \n"
                + " ci_envia, \n"
                + " ci_paga, \n"
                + " responsable_paga, \n"
                + " estado \n"
                + " FROM \n"
                + " tes_comprobante_pago_listado \n"
                + " where ci_paga is null and responsable_paga is null and ide_listado =" + banco);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;

    }

    public TablaGenerica empleado() {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT cod_empleado,cedula_pass,nombres,cod_empleado,estado\n"
                + "FROM srh_empleado WHERE cod_cargo = 101 and estado = 1");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getCuentas(Integer codigo, Integer anio) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_conc,conc_descripcion,substr(conc_cuentas,0,10) as cuenta,substr(conc_cuentas,11,20)as cuenta1,conc_columnas  FROM conc_distributivo where ide_conc = " + codigo + " and ano = " + anio);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getMovimiento(String cuenta, String cuenta1, Integer movimiento, Integer anio, Integer periodo, Integer distributivo, String columna) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select\n"
                + "(case when (select ide_cuenta from conc_catalogo_cuentas where cedula = e.cedula_pass and cue_codigo like '" + cuenta + "%' order by ide_cuenta desc limit 1) is not null \n"
                + "then (select ide_cuenta from conc_catalogo_cuentas where cedula = e.cedula_pass and cue_codigo like '" + cuenta + "%' order by ide_cuenta desc limit 1)\n"
                + "when (select ide_cuenta from conc_catalogo_cuentas where cedula = e.cedula_pass and cue_codigo like '" + cuenta + "%' order by ide_cuenta desc limit 1) is null\n"
                + "then (select ide_cuenta from conc_catalogo_cuentas where cedula = e.cedula_pass and cue_codigo like '" + cuenta1 + "%' order by ide_cuenta desc limit 1) end) as cuenta,   \n"
                + "" + movimiento + " as movimiento,  \n"
                + "0 as debe,  \n"
                + "r.valor,  \n"
                + "0 as devengado,  \n"
                + "'S/D' as descripcion,  \n"
                + "'F' as tipo_movimiento,  \n"
                + "'S/D' as doc_deposito  \n"
                + "from srh_roles r  \n"
                + "inner join srh_empleado e on r.ide_empleado=e.cod_empleado  \n"
                + "where r.ano=" + anio + " and r.ide_periodo=" + periodo + " and  \n"
                + "r.id_distributivo_roles=" + distributivo + " and r.ide_columnas in (" + columna + ") and \n"
                + "r.valor >0\n"
                + "order by e.nombres");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getMovimientos(String cuenta, Integer movimiento, Integer anio, Integer periodo, Integer distributivo, Integer columna) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select( \n"
                + "select ide_cuenta \n"
                + "from conc_catalogo_cuentas \n"
                + "where cedula = e.cedula_pass and (cue_codigo like'112.01.01%' or cue_codigo like'112.01.02%')\n"
                + "order by ide_cuenta desc limit 1) as cuenta,  \n"
                + "" + movimiento + " as movimiento, \n"
                + "0 as debe, \n"
                + "r.valor, \n"
                + "0 as devengado, \n"
                + "'S/D' as descripcion, \n"
                + "'F' as tipo_movimiento, \n"
                + "'S/D' as doc_deposito \n"
                + "from srh_roles r \n"
                + "inner join srh_empleado e on r.ide_empleado=e.cod_empleado \n"
                + "where r.ano=" + anio + " and r.ide_periodo=" + periodo + " and \n"
                + "r.id_distributivo_roles=" + distributivo + " and r.ide_columnas=" + columna + " and\n"
                + "r.valor >0"
                + "order by e.nombres");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getAsientosCuentas(String cuenta, String cuenta1, Integer movimiento, Integer anio, Integer periodo, Integer distributivo, String columna) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select( \n"
                + "select ide_cuenta \n"
                + "from conc_catalogo_cuentas \n"
                + "where cedula = e.cedula_pass and (cue_codigo like'" + cuenta + "%' or cue_codigo like'" + cuenta1 + "%')\n"
                + "order by ide_cuenta desc limit 1) as cuenta,  \n"
                + "" + movimiento + " as movimiento, \n"
                + "0 as debe, \n"
                + "r.valor, \n"
                + "0 as devengado, \n"
                + "'S/D' as descripcion, \n"
                + "'F' as tipo_movimiento, \n"
                + "'S/D' as doc_deposito \n"
                + "from srh_roles r \n"
                + "inner join srh_empleado e on r.ide_empleado=e.cod_empleado \n"
                + "where r.ano=" + anio + " and r.ide_periodo=" + periodo + " and \n"
                + "r.id_distributivo_roles=" + distributivo + " and r.ide_columnas in(select ide_col from srh_columnas where codigo_col ='" + columna + "' and distributivo = " + distributivo + ")and\n"
                + "r.valor >0"
                + "order by e.nombres");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getDetalleMovimientos(Integer movimiento, Integer cuenta) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_detalle_mov,\n"
                + "ide_clasificador,\n"
                + "ide_cuenta,\n"
                + "ide_movimiento,\n"
                + "mov_debe,\n"
                + "mov_haber,\n"
                + "mov_devengado,\n"
                + "mov_usuario,\n"
                + "mov_descripcion\n"
                + "from cont_detalle_movimiento\n"
                + "where ide_cuenta = " + cuenta + " and ide_movimiento =" + movimiento);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getReporte(Integer movimiento, Integer cuenta) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT descripcion,anio,\n"
                + "(case when 1=" + cuenta + " then 'Empleado' when 2=" + cuenta + " then 'Trabajador' end) as distributivo,\n"
                + "(select ide_col from srh_columnas where codigo_col=conc_cuentas_asientos.abreviatura and distributivo = " + cuenta + ") as columna,\n"
                + "trim(trailing ';' from substring(cuentas from 1 for 10)) as cuenta,\n"
                + "ltrim(substring(cuentas from 11 for 11),';' ) as cuenta1\n"
                + "FROM conc_cuentas_asientos\n"
                + "where id_codigo=" + movimiento);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica Pagos_lista(Integer item, Integer lista) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "ide_detalle_listado,\n"
                + "ide_listado,\n"
                + "comprobante,\n"
                + "cedula_pass_beneficiario,\n"
                + "nombre_beneficiario,\n"
                + "valor,\n"
                + "numero_cuenta,\n"
                + "codigo_banco,\n"
                + "ban_nombre,\n"
                + "tipo_cuenta,\n"
                + "null as proceso\n"
                + "FROM\n"
                + "tes_detalle_comprobante_pago_listado AS d\n"
                + "where ide_estado_listado = (SELECT ide_estado_listado FROM tes_estado_listado where estado like 'ENVIADO') and item =" + item + " and ide_listado =" + lista);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica empleado1(String cedula) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "e.cod_empleado,\n"
                + "e.cedula_pass,\n"
                + "e.nombres,\n"
                + "e.numero_cuenta,\n"
                + "e.cod_cuenta,\n"
                + "b.codigo_banco\n"
                + "FROM\n"
                + "srh_empleado e ,\n"
                + "ocebanco b \n"
                + "where e.cod_banco = b.ban_codigo and e.cedula_pass like '" + cedula + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica empleadoCod(String codigo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT cod_empleado,cedula_pass,nombres,fecha_ingreso,fecha_nombramiento,relacion_laboral,id_distributivo\n"
                + "FROM srh_empleado where estado = 1 and cod_empleado = '" + codigo + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;

    }

    public TablaGenerica getorden_valida(String codigo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT tes_ide_orden_pago,tes_numero_orden,tes_estado,tes_id_proveedor,tes_proveedor,"
                + "tes_cod_empleado,tes_empleado,tes_fecha_envio,tes_fecha_comprobante,tes_comprobante_egreso\n"
                + "FROM tes_orden_pago where tes_numero_orden ='" + codigo + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica proveedor(String ruc) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "p.ide_proveedor,\n"
                + "p.ruc,\n"
                + "p.titular,\n"
                + "p.ban_codigo,\n"
                + "p.numero_cuenta,\n"
                + "p.tipo_cuenta,\n"
                + "p.ide_tipo_proveedor,\n"
                + "p.codigo_banco,\n"
                + "o.ban_nombre\n"
                + "FROM\n"
                + "tes_proveedores p ,\n"
                + "ocebanco o\n"
                + "where p.ban_codigo = o.ban_codigo and ruc like '" + ruc + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica proveedor1(Integer ruc) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "p.ide_proveedor,\n"
                + "p.ruc,\n"
                + "p.titular,\n"
                + "p.ban_codigo,\n"
                + "p.numero_cuenta,\n"
                + "p.tipo_cuenta,\n"
                + "p.ide_tipo_proveedor,\n"
                + "p.codigo_banco,\n"
                + "o.ban_nombre\n"
                + "FROM\n"
                + "tes_proveedores p ,\n"
                + "ocebanco o\n"
                + "where p.ban_codigo = o.ban_codigo and p.ide_proveedor =" + ruc);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica busEstado(String estado) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_detalle_listado,ide_listado,item,ide_estado_listado\n"
                + "FROM tes_detalle_comprobante_pago_listado\n"
                + "where comprobante like '" + estado + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;

    }

    public TablaGenerica Beneficiarios(Integer item) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "ide_detalle_listado,\n"
                + "ide_listado,\n"
                + "item,\n"
                + "comprobante,\n"
                + "cedula_pass_beneficiario,\n"
                + "nombre_beneficiario,\n"
                + "valor,\n"
                + "numero_cuenta,\n"
                + "tipo_cuenta,\n"
                + "codigo_banco\n"
                + "FROM\n"
                + "tes_detalle_comprobante_pago_listado\n"
                + "where ide_listado =" + item);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;

    }

    public TablaGenerica getDatos(String codigo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select  \n"
                + "(case when a.identificacion is null then b.identificacion when a.identificacion is not null then a.identificacion end) as identificacion, \n"
                + "(case when a.nombres is null then b.nombres when a.nombres is not null then a.nombres end) as nombres,\n"
                + "(case when a.ban_codigo is null then b.ban_codigo when a.ban_codigo is not null then a.ban_codigo end) as ban_codigo, \n"
                + "b.codigo_banco,\n"
                + "(case when a.numero_cuenta is null then b.numero_cuenta when a.numero_cuenta is not null then a.numero_cuenta end) as numero_cuenta, \n"
                + "(case when a.cod_cuenta is null then b.cod_cuenta when a.cod_cuenta is not null then a.cod_cuenta end) as cod_cuenta \n"
                + "from  \n"
                + "(SELECT \n"
                + "cast(cedula_pass as varchar) as identificacion, \n"
                + "nombres, \n"
                + "cast(cod_banco as varchar)as ban_codigo, \n"
                + "cast(numero_cuenta as varchar) as numero_cuenta, \n"
                + "cast(cod_cuenta as varchar) as cod_cuenta\n"
                + "from srh_empleado \n"
                + "where cedula_pass = '" + codigo + "' and \n"
                + "estado = 1)as a \n"
                + "full outer join \n"
                + "(SELECT \n"
                + "cast(p.ruc as varchar) AS identificacion, \n"
                + "p.titular AS nombres,\n"
                + "cast(p.ban_codigo as varchar) AS ban_codigo,\n"
                + "p.codigo_banco, \n"
                + "cast(p.numero_cuenta as varchar) AS numero_cuenta, \n"
                + "(case when p.tipo_cuenta = 'C' then '1'  when p.tipo_cuenta = 'A' then '2' end) AS cod_cuenta \n"
                + "FROM tes_proveedores AS p \n"
                + "where p.ruc = '" + codigo + "'\n"
                + ") as b  \n"
                + "on a.identificacion = b.identificacion");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;

    }

    public TablaGenerica getBanco(String codigo, Integer banco) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ban_codigo,ban_nombre,codigo_banco\n"
                + "from ocebanco\n"
                + "where ban_codigo = " + banco + " or codigo_banco = '" + codigo + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getAsientoContable(Integer cuenta, Integer movimiento) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select\n"
                + "ide_detalle_mov,\n"
                + "ide_cuenta,\n"
                + "ide_movimiento,\n"
                + "mov_debe,\n"
                + "mov_haber,\n"
                + "mov_devengado,\n"
                + "mov_descripcion,\n"
                + "ide_tipo_movimiento,\n"
                + "doc_deposito\n"
                + "from cont_detalle_movimiento\n"
                + "where ide_cuenta =" + cuenta + " and ide_movimiento =" + movimiento);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getAsientoBeneficiarios(String nombre, Integer distributivo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT id_retencion,\n"
                + "beneficiario,\n"
                + "cedula_beneficiario,\n"
                + "mensual\n"
                + "FROM conc_lista_retenciones\n"
                + "where estado = 'A' and cedula_beneficiario= '" + nombre + "' and (select id_distributivo from srh_empleado where cod_empleado = conc_lista_retenciones.cod_empleado) = " + distributivo);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getAsientos(Integer movimiento) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT id_codigo,descripcion,cuentas,substring(cuentas from 1 for 9) as cuenta, substring(cuentas from 11 for 21) as cuenta1,abreviatura FROM conc_cuentas_asientos where id_codigo =" + movimiento);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getCuentaCont(String cuenta, String cuenta1) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "ide_cuenta,\n"
                + "cue_codigo,\n"
                + "cue_descripcion,\n"
                + "cedula,\n"
                + "nivel,\n"
                + "conciliacion_c,\n"
                + "tipo_c,\n"
                + "tesoreria\n"
                + "FROM conc_catalogo_cuentas\n"
                + "where cue_codigo like '" + cuenta + "%' or cue_codigo like'" + cuenta1 + "%'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getCuentaRent(Integer distributivo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select * from(select ide_cuenta,cue_codigo from conc_catalogo_cuentas) as a\n"
                + "inner join\n"
                + "(\n"
                + "SELECT \n"
                + "c.cue_codigo as cuenta, \n"
                + "c.cue_descripcion as descripcion, \n"
                + "l.mensual as valor \n"
                + "FROM conc_lista_retenciones l \n"
                + "inner join conc_catalogo_cuentas c on c.cedula= l.cedula_beneficiario \n"
                + "inner join srh_empleado e on e.cod_empleado= l.cod_empleado \n"
                + "where e.id_distributivo=" + distributivo + " \n"
                + "union all \n"
                + "select \n"
                + "(case when substring(pre_codigo from 1 for 2) = '51' then trim(trailing ';' from substring(cuentas from 1 for 10)) \n"
                + "when substring(pre_codigo from 1 for 2) = '71' then ltrim(substring(cuentas from 11 for 11),';' ) end) as cuenta , \n"
                + "descripcion, \n"
                + "sum as valor \n"
                + "from(SELECT descripcion,cuentas FROM conc_cuentas_asientos where estado ='A') as a \n"
                + "inner join ( \n"
                + "select descripcion_col as tipo,ide_columnas,pre_codigo,sum(valor) \n"
                + "from srh_roles  r \n"
                + "inner join prec_programas p on r.ide_programa=p.ide_programa \n"
                + "inner join  srh_empleado e on e.cod_empleado=r.ide_empleado \n"
                + "inner join  conc_clasificador c on p.ide_clasificador=c.ide_clasificador \n"
                + "inner join  srh_columnas h on r.ide_columnas=h.ide_col \n"
                + "where ano=" + utilitario.getAnio(utilitario.getFechaActual()) + " \n"
                + "and  id_distributivo_roles=" + distributivo + " \n"
                + "and ide_periodo=" + utilitario.getMes(utilitario.getFechaActual()) + "\n"
                + "and ide_columnas in(4,3,9,10,13,84,56,91,50,5,80) \n"
                + "and valor>0 \n"
                + "group by c.pre_codigo,ide_columnas,descripcion_col \n"
                + "order by descripcion_col) as b \n"
                + "on a.descripcion = b.tipo \n"
                + "order by descripcion,cuenta) as b\n"
                + "on a.cue_codigo= b.cuenta");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getCuentaRt(Integer cuenta, Integer asiento) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select * from (select ide_cuenta,cue_codigo from conc_catalogo_cuentas) as u\n"
                + "inner join (\n"
                + "select  \n"
                + " valor, \n"
                + " (case when trim(trailing ';' from substring(cuentas from 1 for 10)) like '%51%' and numero = '51' then trim(trailing ';' from substring(cuentas from 1 for 10)) \n"
                + " else ltrim(substring(cuentas from 11 for 11),';' ) end) as cuenta \n"
                + " from ( \n"
                + " SELECT cuentas,abreviatura, \n"
                + " (select ide_col from srh_columnas where codigo_col=conc_cuentas_asientos.abreviatura and distributivo=" + cuenta + ") as columna \n"
                + " FROM conc_cuentas_asientos \n"
                + " where id_codigo_asiento=" + asiento + ") as l \n"
                + " inner join  \n"
                + " (select sum(valor) as valor,ide_col,(case when substring(partida_pres, 4, 2) = '51' or substring(partida_pres, 4, 2)='71' then substring(partida_pres, 4, 2)  \n"
                + " when substring(partida_pres, 7, 2)!='51' or  substring(partida_pres, 7, 2)!='71' then substring(partida_pres, 7, 2)end) as numero \n"
                + " from( \n"
                + " select partida_pres,valor,b.ide_col \n"
                + " from srh_roles a, srh_columnas b, srh_empleado c \n"
                + " where a.ide_columnas = b.ide_col \n"
                + " and a.ide_empleado = c.cod_empleado \n"
                + " and a.ide_periodo = " + (utilitario.getMes(utilitario.getFechaActual()) - 1) + " \n"
                + " and ano= " + utilitario.getAnio(utilitario.getFechaActual()) + " \n"
                + " and valor > 0 \n"
                + " and not (valor*calcula) = 0 \n"
                + " and id_distributivo =" + cuenta + "\n"
                + " order by partida_pres \n"
                + " ) as a \n"
                + " group by (case when substring(partida_pres, 4, 2) = '51' or substring(partida_pres, 4, 2)='71' then substring(partida_pres, 4, 2)  \n"
                + " when substring(partida_pres, 7, 2)!='51' or  substring(partida_pres, 7, 2)!='71' then substring(partida_pres, 7, 2)end),ide_col) as k \n"
                + " on l.columna = k.ide_col) as p\n"
                + "on u.cue_codigo = p.cuenta");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getCertificado(Integer movimiento) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT nombres,cod_cargo,cod_tipo,cod_empleado,cod_direccion from srh_empleado where cod_empleado = " + movimiento);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getIdCuentas(String movimiento) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT ide_cuenta, con_ide_cuenta,cue_codigo,cue_descripcion FROM conc_catalogo_cuentas where cue_codigo = '" + movimiento + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getCodCuentas(String cuenta) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT id_codigo,descripcion FROM conc_cuentas_asientos where abreviatura='" + cuenta + "'");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getVerificarCertf(Integer cuenta) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("SELECT\n"
                + "id_codigo,\n"
                + "fecha_certificado,\n"
                + "numero_certificado,\n"
                + "memorando,\n"
                + "fecha_memorando,\n"
                + "descripcion_proyecto,\n"
                + "estado\n"
                + "FROM cert_fondos\n"
                + "where id_codigo =" + cuenta);
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public TablaGenerica getIESSCuenta(Integer distributivo) {
        conPostgresql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select a.partida,cast((a.pres_iess+a.aport_iess+a.ext_iess+b.total_aportes) as numeric (10,2)) as valor_total from (\n"
                + "select \n"
                + "(case when substring(a.partida_pres, 4, 2) = '51' or substring(a.partida_pres, 4, 2)='71' then substring(a.partida_pres, 4, 2) \n"
                + "when substring(a.partida_pres, 7, 2)!='51' or  substring(a.partida_pres, 7, 2)!='71' then substring(a.partida_pres, 7, 2)end) as partida,\n"
                + "sum(b.prestamo_iess) as pres_iess,sum(c.aporte_iess)as aport_iess,(case when sum(d.exte_iess) is null then 0\n"
                + "when sum(d.exte_iess) is not null then sum(d.exte_iess) end ) as ext_iess from \n"
                + "(SELECT e.cod_empleado,e.partida_pres\n"
                + "FROM srh_roles AS r\n"
                + "INNER JOIN prec_programas AS p ON r.ide_programa = p.ide_programa\n"
                + "INNER JOIN srh_empleado AS e ON e.cod_empleado = r.ide_empleado\n"
                + "WHERE r.ano = " + utilitario.getAnio(utilitario.getFechaActual()) + " \n"
                + "AND r.id_distributivo_roles =" + distributivo + " \n"
                + "AND r.ide_periodo =" + utilitario.getMes(utilitario.getFechaActual()) + "\n"
                + "AND r.ide_columnas IN (14,40)\n"
                + ") as a\n"
                + "left join \n"
                + "(SELECT e.cod_empleado,r.valor AS prestamo_iess\n"
                + "FROM srh_roles AS r ,srh_empleado AS e\n"
                + "WHERE e.cod_empleado = r.ide_empleado AND\n"
                + "r.ano =" + utilitario.getAnio(utilitario.getFechaActual()) + " AND\n"
                + "r.id_distributivo_roles =" + distributivo + " AND\n"
                + "r.ide_periodo =" + utilitario.getMes(utilitario.getFechaActual()) + " AND\n"
                + "r.ide_columnas IN (21, 59) AND\n"
                + "r.valor > 0) as b\n"
                + "on a.cod_empleado = b.cod_empleado\n"
                + "left join \n"
                + "(SELECT e.cod_empleado,r.valor AS aporte_iess\n"
                + "FROM srh_roles AS r ,srh_empleado AS e\n"
                + "WHERE e.cod_empleado = r.ide_empleado AND\n"
                + "r.ano =" + utilitario.getAnio(utilitario.getFechaActual()) + " AND\n"
                + "r.id_distributivo_roles =" + distributivo + " AND\n"
                + "r.ide_periodo =" + utilitario.getMes(utilitario.getFechaActual()) + " AND\n"
                + "r.ide_columnas IN (33, 71) AND\n"
                + "r.valor > 0) as c\n"
                + "on a.cod_empleado = c.cod_empleado\n"
                + "left join \n"
                + "(SELECT e.cod_empleado,r.valor AS exte_iess\n"
                + "FROM srh_roles AS r ,srh_empleado AS e\n"
                + "WHERE e.cod_empleado = r.ide_empleado AND\n"
                + "r.ano =" + utilitario.getAnio(utilitario.getFechaActual()) + " AND\n"
                + "r.id_distributivo_roles =" + distributivo + " AND\n"
                + "r.ide_periodo =" + utilitario.getMes(utilitario.getFechaActual()) + " AND\n"
                + "r.ide_columnas IN (111, 112) AND\n"
                + "r.valor > 0) as d\n"
                + "on a.cod_empleado = d.cod_empleado\n"
                + "group by (case when substring(a.partida_pres, 4, 2) = '51' or substring(a.partida_pres, 4, 2)='71' then substring(a.partida_pres, 4, 2) \n"
                + "when substring(a.partida_pres, 7, 2)!='51' or  substring(a.partida_pres, 7, 2)!='71' then substring(a.partida_pres, 7, 2)end)) as a\n"
                + "inner join \n"
                + "(select (case when substring(partida_pres, 4, 2) = '51' or substring(partida_pres, 4, 2)='71' then substring(partida_pres, 4, 2) \n"
                + "when substring(partida_pres, 7, 2)!='51' or  substring(partida_pres, 7, 2)!='71' then substring(partida_pres, 7, 2)end) as partida\n"
                + ",sum(total_aportes) as total_aportes from (\n"
                + "select partida_pres,\n"
                + "(case when id_distributivo= 1 then ((sum(valor) * 11.15)/100) \n"
                + "when id_distributivo = 2 then ((sum(valor) * 11.15)/100) end) +\n"
                + "(case when id_distributivo= 1 then  ((sum(valor) * 0.5)/100)\n"
                + "when id_distributivo= 2 then ((sum(valor) * 0.5)/100) end) +\n"
                + "(case when id_distributivo =1 then 0\n"
                + "when id_distributivo =2 then ((sum(valor) * 0.5)/100) end) as total_aportes\n"
                + "from (\n"
                + "select partida_pres,a.ide_programa,valor,id_distributivo\n"
                + "from srh_roles a, srh_columnas b, srh_empleado c\n"
                + "where a.ide_columnas = b.ide_col\n"
                + "and a.ide_empleado = c.cod_empleado\n"
                + "and a.ide_periodo =" + utilitario.getMes(utilitario.getFechaActual()) + "\n"
                + "and ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "\n"
                + "and valor > 0\n"
                + "and not (valor*calcula) = 0\n"
                + "and  ide_columnas in (25,70,18,92,93,75,76,19)\n"
                + "and id_distributivo =" + distributivo + ") a, \n"
                + "(select numero,cod_programa,a.ide_programa from (\n"
                + "select count(distinct ide_empleado) as numero,ide_programa\n"
                + "from srh_roles where ano=" + utilitario.getAnio(utilitario.getFechaActual()) + "  \n"
                + "and ide_periodo=" + utilitario.getMes(utilitario.getFechaActual()) + " \n"
                + "and id_distributivo_roles=" + distributivo + "\n"
                + "group by ide_programa\n"
                + ") a,prec_programas b\n"
                + "where a.ide_programa=b.ide_programa\n"
                + ") b\n"
                + "group by partida_pres,a.ide_programa,b.ide_programa,id_distributivo\n"
                + "having a.ide_programa = b.ide_programa\n"
                + ") a\n"
                + "group by (case when substring(partida_pres, 4, 2) = '51' or substring(partida_pres, 4, 2)='71' then substring(partida_pres, 4, 2) \n"
                + "when substring(partida_pres, 7, 2)!='51' or  substring(partida_pres, 7, 2)!='71' then substring(partida_pres, 7, 2)end))as b\n"
                + "on a.partida = b.partida");
        tabFuncionario.ejecutarSql();
        desPostgresql();
        return tabFuncionario;
    }

    public String listaMax() {
        conPostgresql();

        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select 0 as id ,\n"
                + "(case when max(num_documento) is null then 'LIST-2014-00000' when max(num_documento)is not null then max(num_documento) end) AS maximo\n"
                + "from tes_detalle_comprobante_pago_listado");
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("maximo");
        desPostgresql();
        return ValorMax;
    }

    public String maximOrden() {
        conPostgresql();

        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select 0 as id ,(case when max(tes_numero_orden) is null then '0' when max(tes_numero_orden)is not null then max(tes_numero_orden) end) AS maximo\n"
                + "from tes_orden_pago where tes_estado_doc = '1'");
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("maximo");
        desPostgresql();
        return ValorMax;
    }

    public String maxCertificados(Integer anio) {
        conPostgresql();

        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select 0 as id ,(case when max(numero_certificado) is null then '0' when max(numero_certificado)is not null then max(numero_certificado) end) AS maximo\n"
                + "from cert_fondos where anio=" + anio);
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("maximo");
        desPostgresql();
        return ValorMax;
    }

    public TablaGenerica getDatosPrograma(Integer cuenta) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select ZONAZ1,NOMBZ1 from USFIMRU.TIGSA_GLM11 where CLASZ1 = '002' and ZONAZ1 =" + cuenta);
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getDatosPrograma1(Integer cuenta) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select * from (\n"
                + "SELECT CAUXMA,NOMBMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'and ZONAMA like '_22%'\n"
                + "union\n"
                + "select ZONAZ1,NOMBZ1 from USFIMRU.TIGSA_GLM11 where CLASZ1 = '002')\n"
                + "where CAUXMA =" + cuenta);
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getDatosDesglose(Integer cuenta) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("SELECT CAUXMA,NOMBMA  FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'and ZONAMA like '_22%' AND CAUXMA =" + cuenta);
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getDatosIngreso(String cuenta) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select CUENMC,CEDTMC,NOLAAD\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'P' and substr(CEDTMC,1,1) in (1,2,3) and NIVEMC=6\n"
                + "and CUENMC = '" + cuenta + "'");
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getDatosPartida(String cuenta, Integer inicial, Integer reforma, String programa) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select CUENMC,CUENMC as partida,CEDTMC,NOLAAD, POSTMA from(  \n"
                + "select DISTINCT substr(CUENMC,1,10) as CUENMC,CEDTMC,NOLAAD  \n"
                + "from USFIMRU.TIGSA_GLM03  \n"
                + "where TIPLMC= 'P' and NIVEMC between 1 and 6 and substr(CEDTMC,1,1) in (5,6,7,8,9)  \n"
                + "and LENGTH(substr(CUENMC,1,10))=10)  \n"
                + "inner join  \n"
                + "(select DISTINCT CUENDT as CUENDTi1,POSTMA from ( \n"
                + "SELECT CUENDT , AUAD02  \n"
                + "FROM USFIMRU.TIGSA_GLB01 \n"
                + "where STATDT='E' \n"
                + "AND CCIADT='CM' \n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + reforma + "\n"
                + "and AUAD02 is not null) \n"
                + "left join \n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC' \n"
                + "UNION \n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY') \n"
                + "on AUAD02 = CAUXMA)  \n"
                + "on CUENMC = CUENDTi1  \n"
                + "where postma = " + programa + " and cuenmc = '" + cuenta + "'\n"
                + "order by CUENMC");
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getDatosPartidas(String cuenta, Integer inicial, Integer reforma, String programa) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select CUENMC,CUENMC as partida,CEDTMC,NOLAAD from( \n"
                + "select DISTINCT substr(CUENMC,1,10) as CUENMC,CEDTMC,NOLAAD \n"
                + "from USFIMRU.TIGSA_GLM03 \n"
                + "where TIPLMC= 'P' and NIVEMC between 1 and 6 and substr(CEDTMC,1,1) in (5,6,7,8,9) \n"
                + "and LENGTH( substr(CUENMC,1,10))=10) \n"
                + "inner join \n"
                + "(select CUENDTi1 from ( \n"
                + "SELECT substr(CUENDT,1,10) as CUENDTi1 FROM USFIMRU.TIGSA_GLB01 \n"
                + "where STATDT='E' \n"
                + "AND CCIADT='CM' \n"
                + "AND TMOVDT='D' \n"
                + "AND SAPRDT>=" + inicial + " \n"
                + "AND SAPRDT<=" + reforma + " \n"
                + "AND TAAD02 BETWEEN 'CC' and 'PY' \n"
                + "AND CLDODT BETWEEN 'AI' and 'CO' \n"
                + "AND AUAD02 in (SELECT CAUXMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'and ZONAMA like '_22%'\n"
                + "UNION\n"
                + "SELECT CAUXMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY'and ZONAMA like '__22%'\n"
                + "UNION\n"
                + "SELECT CAUXMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY'and ZONAMA like '___22%')\n"
                + "and AUAD02 = " + programa + " \n"
                + "and CUENDT=" + cuenta + "\n"
                + "GROUP BY CUENDT ) \n"
                + "group by CUENDTi1) \n"
                + "on CUENMC = CUENDTi1 \n"
                + "order by CUENMC");
        tabFuncionario.ejecutarSql();
        tabFuncionario.imprimirSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getDatosProyecto(Integer proyecto,Integer ani) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select * from (select ROWNUM as numero,CUENDT,AUAD02 as proyect, NDOCDC as tramit,nombre from ( \n"
                + "select DISTINCT CUENDT,AUAD02,(select DISTINCT NOMBMA from USFIMRU.TIGSA_GLm05 where CAUXMA = AUAD02 and POSTMA=POSTM ) as nombre \n"
                + ",LISTAGG(NDOCDC, ',') WITHIN GROUP (ORDER BY AUAD02) AS NDOCDC  \n"
                + "from( \n"
                + "SELECT DISTINCT CUENDT, MONTDT as codificado,AUAD02 \n"
                + "FROM USFIMRU.TIGSA_GLB01 \n"
                + "where STATDT='E' \n"
                + "AND CCIADT='CM' \n"
                + "AND TMOVDT='D' \n"
                + "AND SAPRDT>=1" + ani + "14 \n"
                + "AND SAPRDT<=1" + ani + "15 \n"
                + "AND TAAD02 = 'PY' \n"
                + "AND AUAD02 in (SELECT CAUXMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')) \n"
                + "inner join \n"
                + "(select NDOCDC,CUENDC,MONTDC,AUA2DC from USFIMRU.PRCO01) \n"
                + "on CUENDT = CUENDC and AUAD02 = AUA2DC \n"
                + "left join \n"
                + "(SELECT CAUXMA,POSTMA as POSTM FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY') \n"
                + "on AUAD02 = CAUXMA \n"
                + "group by CUENDT,AUAD02,POSTM))\n"
                + "where numero =" + proyecto);
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getGatos(String mes) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select CUENMC,CEDTMC,NOLAAD,codigo,SUBSTR(CUENMC, 1, 2) as gr,SUBSTR(CUENMC, 3, 2)as sub,SUBSTR(CUENMC, 5, 2)as item,inicial, reforma,codificado,compromiso,devengado,ejecutado \n"
                + ",to_char((inicial - devengado), 'fm999999999.90') as saldo \n"
                + ",to_char((codificado- compromiso), 'fm999999999.90') as saldoCo \n"
                + ",to_char((compromiso - devengado), 'fm999999999.90') as saldoDE \n"
                + ",'G' as tipo \n"
                + "from(select CUENMC,CEDTMC,NOLAAD,codigo\n"
                + ",to_char(sum(inicial), 'fm999999999.90') as inicial\n"
                + ",to_char(sum(reforma), 'fm999999999.90') as reforma\n"
                + ",to_char(sum(codificado), 'fm999999999.90') as codificado\n"
                + ",to_char(sum(compromiso), 'fm999999999.90') as compromiso\n"
                + ",to_char(sum(devengado), 'fm999999999.90') as devengado\n"
                + ",to_char(sum(ejecutado), 'fm999999999.90') as ejecutado\n"
                + "from (select DISTINCT CUENMC,CEDTMC,NOLAAD,nivemc,POSTMA,inicial\n"
                + ",(case when reforma is null then 0 else reforma end)as reforma\n"
                + ",(case when compromiso is null then 0 else compromiso end)as compromiso\n"
                + ",(case when devengado is null then 0 else devengado end)as devengado\n"
                + ",(case when ejecutado  is null then 0 else ejecutado  end)as ejecutado\n"
                + ",((case when reforma is null then 0 else reforma end )+(case when inicial is null then 0 else inicial end ))as codificado\n"
                + ",(case when POSTMA ='40' then POSTMA else '0' end) as codigo\n"
                + "from (select CUENMC,CEDTMC,NOLAAD,nivemc,POSTMA,inicial \n"
                + "from (select CUENMC,CEDTMC,NOLAAD,nivemc\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'P' and substr(CEDTMC,1,1) in (5,6,7,8,9))\n"
                + "left join\n"
                + "(select CUENDT,sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "14\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY CUENDT,POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 8),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 \n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "14\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 8),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 6),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 \n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "14\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 6),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 4),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 \n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "14\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 4),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 2),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 \n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "14\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 2),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 1),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 \n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "14\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 1),POSTMA)\n"
                + "on CUENMC = CUENDT\n"
                + "order by CUENMC,POSTMA)\n"
                + "left join\n"
                + "(select CEDTMC as cuentar,inicial as reforma,POSTMA as partidar,  nivemc as nivemcr\n"
                + "from (select CUENMC,CEDTMC,NOLAAD,nivemc\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'P' and substr(CEDTMC,1,1) in (5,6,7,8,9))\n"
                + "left join\n"
                + "(select CUENDT,sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "15\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY CUENDT,POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 8),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "15\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 8),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 6),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "15\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 6),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 4),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "15\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 4),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 2),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "15\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 2),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 1),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "15\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 1),POSTMA)\n"
                + "on CUENMC = CUENDT\n"
                + "order by CUENMC,POSTMA)\n"
                + "on CEDTMC=cuentar and POSTMA=partidar and nivemc = nivemcr\n"
                + "left join\n"
                + "(select CEDTMC as cuentac,inicial as compromiso,POSTMA as partidac ,nivemc as nivemcc\n"
                + "from (select CUENMC,CEDTMC,NOLAAD,nivemc\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'P' and substr(CEDTMC,1,1) in (5,6,7,8,9))\n"
                + "left join\n"
                + "(select CUENDT,sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='1'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY CUENDT,POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 8),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='1'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + ")\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 8),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 6),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='1'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + ")\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 6),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 4),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='1'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + ")\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 4),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 2),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='1'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + ")\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 2),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 1),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='1'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 1),POSTMA)\n"
                + "on CUENMC = CUENDT\n"
                + "order by CUENMC,POSTMA)\n"
                + "on CEDTMC=cuentac and POSTMA=partidac and nivemc = nivemcc\n"
                + "left join\n"
                + "(select CEDTMC as cuentad,inicial as devengado,POSTMA as partidad,nivemc as nivemcd\n"
                + "from (select CUENMC,CEDTMC,NOLAAD,nivemc\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'P' and substr(CEDTMC,1,1) in (5,6,7,8,9))\n"
                + "left join\n"
                + "(select CUENDT,sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='2'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY CUENDT,POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 8),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='2'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 8),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 6),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='2'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 6),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 4),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='2'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 4),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 2),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='2'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 2),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 1),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='2'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 1),POSTMA)\n"
                + "on CUENMC = CUENDT\n"
                + "order by CUENMC,POSTMA)\n"
                + "on CEDTMC=cuentad and POSTMA=partidad and nivemc = nivemcd\n"
                + "left join\n"
                + "(select CEDTMC as cuentae,inicial as ejecutado,POSTMA as partidae,nivemc as nivemce\n"
                + "from (select CUENMC,CEDTMC,NOLAAD,nivemc\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'P' and substr(CEDTMC,1,1) in (5,6,7,8,9))\n"
                + "left join\n"
                + "(select CUENDT,sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='3'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY CUENDT,POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 8),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='3'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 8),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 6),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='3'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 6),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 4),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='3'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 4),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 2),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='3'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 2),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 1),sum(inicial) as inicial,POSTMA\n"
                + "from (SELECT CUENDT,SUM(MONTDT) as inicial,AUAD02 FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01\n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + "\n"
                + "AND CAUXDT='3'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT,AUAD02\n"
                + "HAVING SUM(MONTDT)>0)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 1),POSTMA)\n"
                + "on CUENMC = CUENDT\n"
                + "order by CUENMC,POSTMA)\n"
                + "on CEDTMC=cuentae and POSTMA=partidae and nivemc = nivemce\n"
                + "where POSTMA is not null and nivemc  between 4 and 4)\n"
                + "group by CUENMC,CEDTMC,NOLAAD,codigo)\n"
                + "order by CEDTMC");
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getIngresos(String mes) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select DISTINCT CUENMC,SUBSTR(CUENMC, 1, 2) as gr,SUBSTR(CUENMC, 3, 2)as sub,SUBSTR(CUENMC, 5, 2)as item,NOLAAD\n"
                + " ,to_char((case when inicial is null then 0 else inicial end), 'fm999999999.90')as inicial\n"
                + " ,reforma\n"
                + " ,to_char(devengado, 'fm999999999.90') as devengado\n"
                + " ,to_char(ejecutado, 'fm999999999.90') as ejecutado \n"
                + " ,to_char((case when (inicial + reforma) is null then 0 else (inicial + reforma)end), 'fm999999999.90') as codificado \n"
                + " ,to_char((case when (inicial - devengado) is null then 0 else(inicial - devengado)end), 'fm999999999.90') as saldo \n"
                + " , 'I' as tipo \n"
                + " from(select DISTINCT CUENMC,CEDTMC,nivemc, \n"
                + " (case when nivemc = 1 then inicial1 \n"
                + " when nivemc = 2 then inicial2 \n"
                + " when nivemc = 3 then inicial4 \n"
                + " when nivemc = 4 then inicial6 \n"
                + " when nivemc = 5 then inicial8 \n"
                + " when nivemc = 6 then inicial end) \n"
                + " as inicial,NOLAAD \n"
                + " from( \n"
                + " select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8,inicial6,inicial4,inicial2 \n"
                + " ,sum(inicial) over (PARTITION BY substr(CUENMC,1,1) ORDER BY substr(CUENMC,1,1)) as inicial1 \n"
                + " from ( \n"
                + " select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8,inicial6,inicial4 \n"
                + " ,sum(inicial) over (PARTITION BY substr(CUENMC,1,2) ORDER BY substr(CUENMC,1,2)) as inicial2 \n"
                + " from ( \n"
                + " select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8,inicial6 \n"
                + " ,sum(inicial) over (PARTITION BY substr(CUENMC,1,4) ORDER BY substr(CUENMC,1,4)) as inicial4 \n"
                + " from ( \n"
                + " select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8 \n"
                + " ,sum(inicial) over (PARTITION BY substr(CUENMC,1,6) ORDER BY substr(CUENMC,1,6)) as inicial6 \n"
                + " from ( \n"
                + " select CUENMC,CEDTMC,nivemc,NOLAAD,inicial \n"
                + " ,sum(inicial) over (PARTITION BY substr(CUENMC,1,8) ORDER BY substr(CUENMC,1,8)) as inicial8 \n"
                + " from (select CUENMC,CEDTMC,nivemc,NOLAAD \n"
                + " from USFIMRU.TIGSA_GLM03 \n"
                + " where TIPLMC= 'P' and substr(CEDTMC,1,1) in (1,2,3)) \n"
                + " left join \n"
                + " (SELECT CUENDT,SUM(MONTDT)*-1 as inicial \n"
                + " FROM USFIMRU.TIGSA_GLB01 \n"
                + " where STATDT='E' \n"
                + " AND CCIADT='CM' \n"
                + " AND SAPRDT=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "14 \n"
                + " GROUP BY CUENDT \n"
                + " HAVING SUM(MONTDT)<0 \n"
                + " order by CUENDT) \n"
                + " on CUENMC = CUENDT)))))) \n"
                + " left join \n"
                + " (select CUENMC as CUENMCr, (case when reforma is null then 0 else reforma end)as reforma ,nivemc as nivemcr \n"
                + " from( \n"
                + " select DISTINCT CUENMC as ,nivemc, \n"
                + " (case when nivemc = 1 then inicial1 \n"
                + " when nivemc = 2 then inicial2 \n"
                + " when nivemc = 3 then inicial4 \n"
                + " when nivemc = 4 then inicial6 \n"
                + " when nivemc = 5 then inicial8 \n"
                + " when nivemc = 6 then inicial end) \n"
                + " as reforma \n"
                + " from( \n"
                + " select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8,inicial6,inicial4,inicial2 \n"
                + " ,sum(inicial) over (PARTITION BY substr(CUENMC,1,1) ORDER BY substr(CUENMC,1,1)) as inicial1 \n"
                + " from ( \n"
                + " select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8,inicial6,inicial4 \n"
                + " ,sum(inicial) over (PARTITION BY substr(CUENMC,1,2) ORDER BY substr(CUENMC,1,2)) as inicial2 \n"
                + " from ( \n"
                + " select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8,inicial6 \n"
                + " ,sum(inicial) over (PARTITION BY substr(CUENMC,1,4) ORDER BY substr(CUENMC,1,4)) as inicial4 \n"
                + " from ( \n"
                + " select CUENMC,CEDTMC,nivemc,NOLAAD,inicial,inicial8 \n"
                + " ,sum(inicial) over (PARTITION BY substr(CUENMC,1,6) ORDER BY substr(CUENMC,1,6)) as inicial6 \n"
                + " from ( \n"
                + " select CUENMC,CEDTMC,nivemc,NOLAAD,inicial \n"
                + " ,sum(inicial) over (PARTITION BY substr(CUENMC,1,8) ORDER BY substr(CUENMC,1,8)) as inicial8 \n"
                + " from (select CUENMC,CEDTMC,nivemc,NOLAAD \n"
                + " from USFIMRU.TIGSA_GLM03 \n"
                + " where TIPLMC= 'P' and substr(CEDTMC,1,1) in (1,2,3)) \n"
                + " left join \n"
                + " (SELECT CUENDT,SUM(MONTDT)*-1 as inicial \n"
                + " FROM USFIMRU.TIGSA_GLB01 \n"
                + " where STATDT='E' \n"
                + " AND CCIADT='CM' \n"
                + " AND SAPRDT=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "15 \n"
                + " GROUP BY CUENDT \n"
                + " HAVING SUM(MONTDT)<0 \n"
                + " order by CUENDT) \n"
                + " on CUENMC = CUENDT))))))) \n"
                + " on CUENMC = CUENMCr and nivemc=nivemcr \n"
                + " left join \n"
                + " (select CUENMC as CUENMCd, (case when devengado is null then 0 else devengado end)as devengado ,nivemc as nivemcd \n"
                + " from(select CUENMC,CEDTMC,nivemc, \n"
                + " (case when nivemc = 1 then inicial1 \n"
                + " when nivemc = 2 then inicial2 \n"
                + " when nivemc = 3 then inicial4 \n"
                + " when nivemc = 4 then inicial6 \n"
                + " when nivemc = 5 then inicial8 \n"
                + " when nivemc = 6 then devengado end) \n"
                + " as devengado \n"
                + " from ( \n"
                + " select CUENMC,CEDTMC,NIVEMC,devengado,inicial8,inicial6,inicial4,inicial2 \n"
                + " ,sum(devengado) over (PARTITION BY substr(CUENMC,1,1) ORDER BY substr(CUENMC,1,1)) as inicial1 \n"
                + " from (select CUENMC,CEDTMC,NIVEMC,devengado,inicial8,inicial6,inicial4 \n"
                + " ,sum(devengado) over (PARTITION BY substr(CUENMC,1,2) ORDER BY substr(CUENMC,1,2)) as inicial2 \n"
                + " from(select CUENMC,CEDTMC,NIVEMC,devengado,inicial8,inicial6 \n"
                + " ,sum(devengado) over (PARTITION BY substr(CUENMC,1,4) ORDER BY substr(CUENMC,1,4)) as inicial4 \n"
                + " from (select CUENMC,CEDTMC,NIVEMC,devengado,inicial8 \n"
                + " ,sum(devengado) over (PARTITION BY substr(CUENMC,1,6) ORDER BY substr(CUENMC,1,6)) as inicial6 \n"
                + " from (select CUENMC,CEDTMC,NIVEMC,devengado \n"
                + " ,sum(devengado) over (PARTITION BY substr(CUENMC,1,8) ORDER BY substr(CUENMC,1,8)) as inicial8 \n"
                + " from (select CUENMC,CEDTMC,NIVEMC \n"
                + " from USFIMRU.TIGSA_GLM03 \n"
                + " where TIPLMC= 'P' and substr(CEDTMC,1,1) in (1,2,3)) \n"
                + " left join \n"
                + " (select * from (\n"
                + "select F01CTD,devengado from( \n"
                + "SELECT CUENDT,sum(MONTDT)*-1 as devengado,CAUXDT \n"
                + "FROM USFIMRU.TIGSA_GLB01 \n"
                + "WHERE STATDT='E' \n"
                + "AND CCIADT='MR' \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01 \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + " \n"
                + "AND CUENDT IN (SELECT F01CTO FROM USFIMRU.ECEF01 WHERE F01A1D='2')\n"
                + "AND CAUXDT IN (SELECT F01A1O FROM USFIMRU.ECEF01 WHERE F01A1D='2') \n"
                + "group by CUENDT,CAUXDT \n"
                + "union \n"
                + "SELECT CUENDT,SUM(MONTDT)*-1 as devengado,AUAD01 \n"
                + "FROM USFIMRU.TIGSA_GLB01 \n"
                + "WHERE STATDT='E' \n"
                + "AND CCIADT='MR' \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01 \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + " \n"
                + "AND CUENDT IN (SELECT F01CTO FROM USFIMRU.ECEF01 WHERE F01A1D='2') \n"
                + "AND AUAD01 in (SELECT F01A2O FROM USFIMRU.ECEF01 WHERE F01A1D='2') \n"
                + "AND TMOVDT='H' \n"
                + "GROUP BY CUENDT,AUAD01) \n"
                + "left join \n"
                + "(SELECT F01CTO, (case when F01A1O is null then F01A2O else F01A1O end) as aux,F01CTD FROM USFIMRU.ECEF01  WHERE F01A1D='2') \n"
                + "on F01CTO=CUENDT and aux =CAUXDT \n"
                + "where F01CTD <> '3801010000')\n"
                + "union \n"
                + "select * from (\n"
                + "select DISTINCT F01CTD,devengado from(\n"
                + "select CUENDT,devengado from(\n"
                + "SELECT CUENDT,sum(MONTDT)*-1 as devengado,CAUXDT\n"
                + "FROM USFIMRU.TIGSA_GLB01 \n"
                + "WHERE STATDT='E' \n"
                + "AND CCIADT='MR' \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01 \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + " \n"
                + "AND CUENDT IN (SELECT F01CTO FROM USFIMRU.ECEF01 WHERE F01A1D ='2')\n"
                + "AND TMOVDT='H' \n"
                + "group by CUENDT,CAUXDT\n"
                + "order by CAUXDT)\n"
                + "left join \n"
                + "(SELECT F01CTO, (case when F01A1O is null then F01A2O else F01A1O end) as aux,F01CTD FROM USFIMRU.ECEF01  WHERE F01A1D='2') \n"
                + "on F01CTO=CUENDT and aux =CAUXDT \n"
                + ")\n"
                + "inner join \n"
                + "(SELECT F01CTO,F01CTD FROM USFIMRU.ECEF01 WHERE F01A1D ='2' and F01COO = 'H')\n"
                + "on CUENDT = F01CTO)) \n"
                + " on CUENMC= F01CTD))))))) \n"
                + " on CUENMC = CUENMCd and nivemc=nivemcd \n"
                + " left join \n"
                + " (select CUENMC as CUENMCe, (case when ejecutado is null then 0 else ejecutado end)as ejecutado ,nivemc as nivemce \n"
                + " from(select CUENMC,CEDTMC,nivemc, \n"
                + " (case when nivemc = 1 then inicial1 \n"
                + " when nivemc = 2 then inicial2 \n"
                + " when nivemc = 3 then inicial4 \n"
                + " when nivemc = 4 then inicial6 \n"
                + " when nivemc = 5 then inicial8 \n"
                + " when nivemc = 6 then ejecutado end) \n"
                + " as ejecutado \n"
                + " from ( \n"
                + " select CUENMC,CEDTMC,NIVEMC,ejecutado,inicial8,inicial6,inicial4,inicial2 \n"
                + " ,sum(ejecutado) over (PARTITION BY substr(CUENMC,1,1) ORDER BY substr(CUENMC,1,1)) as inicial1 \n"
                + " from (select CUENMC,CEDTMC,NIVEMC,ejecutado,inicial8,inicial6,inicial4 \n"
                + " ,sum(ejecutado) over (PARTITION BY substr(CUENMC,1,2) ORDER BY substr(CUENMC,1,2)) as inicial2 \n"
                + " from(select CUENMC,CEDTMC,NIVEMC,ejecutado,inicial8,inicial6 \n"
                + " ,sum(ejecutado) over (PARTITION BY substr(CUENMC,1,4) ORDER BY substr(CUENMC,1,4)) as inicial4 \n"
                + " from (select CUENMC,CEDTMC,NIVEMC,ejecutado,inicial8 \n"
                + " ,sum(ejecutado) over (PARTITION BY substr(CUENMC,1,6) ORDER BY substr(CUENMC,1,6)) as inicial6 \n"
                + " from (select CUENMC,CEDTMC,NIVEMC,ejecutado \n"
                + " ,sum(ejecutado) over (PARTITION BY substr(CUENMC,1,8) ORDER BY substr(CUENMC,1,8)) as inicial8 \n"
                + " from (select CUENMC,CEDTMC,NIVEMC \n"
                + " from USFIMRU.TIGSA_GLM03 \n"
                + " where TIPLMC= 'P' and substr(CEDTMC,1,1) in (1,2,3)) \n"
                + " left join \n"
                + " (select F01CTD, sum(inicial) as ejecutado from ( \n"
                + " SELECT CUENDT,SUM(MONTDT)*-1 as inicial,AUAD01 as CAUXDT FROM USFIMRU.TIGSA_GLB01 \n"
                + " WHERE STATDT='E' \n"
                + " AND CCIADT='MR' \n"
                + " AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01 \n"
                + " AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + " \n"
                + " AND CUENDT IN (SELECT F01CTO FROM USFIMRU.ECEF01 WHERE F01A1D='2') \n"
                + " AND TMOVDT='H' \n"
                + " AND AUAD01  in (SELECT F01A2O FROM USFIMRU.ECEF01 WHERE F01A1D='2') \n"
                + " GROUP BY CUENDT,AUAD01 \n"
                + " union \n"
                + " SELECT CUENDT,SUM(MONTDT)*-1 as inicial,CAUXDT FROM USFIMRU.TIGSA_GLB01 \n"
                + " WHERE STATDT='E' \n"
                + " AND CCIADT='MR' \n"
                + " AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01 \n"
                + " AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + " \n"
                + " AND CUENDT IN (SELECT F01CTO FROM USFIMRU.ECEF01 WHERE F01A1D='3') \n"
                + " AND TMOVDT='H' \n"
                + " AND CAUXDT  in (SELECT F01A1O FROM USFIMRU.ECEF01 WHERE F01A1D='3') \n"
                + " GROUP BY CUENDT,CAUXDT) \n"
                + " left join \n"
                + " (SELECT F01CTO,F01A1O,F01CTD FROM USFIMRU.ECEF01 WHERE F01A1D='3' \n"
                + " UNION \n"
                + " SELECT F01CTO,F01A2O,F01CTD FROM USFIMRU.ECEF01 WHERE F01A1D='2') \n"
                + " on F01CTO=CUENDT and F01A1O =CAUXDT \n"
                + " group by F01CTD) \n"
                + " on CUENMC= F01CTD))))))) \n"
                + " on CUENMC = CUENMCe and nivemc=nivemce \n"
                + " where nivemc between 4 and 4 \n"
                + " order by CUENMC");
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getGastosProyecto(String mes) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select ROWNUM as numero,CUENDT,TAAD02\n"
                + ",(case when TAAD02='CC' then null else AUAD02 end) as AUAD02\n"
                + ",AUAD01,dep\n"
                + ",(case when TAAD02='CC' then AUAD02 else POSTM end) as POSTM\n"
                + ",proyecto,inicial,reforma,codificado,compromiso,devengado,ejecutado   \n"
                + " from (select DISTINCT CUENDT,TAAD02,AUAD02,AUAD01,inicial,dep,POSTM,proyecto  \n"
                + " ,(case when reforma is null then 0 else reforma end)as reforma  \n"
                + " ,(inicial + (case when reforma is null then 0 else reforma end)) as codificado  \n"
                + " ,(case when compromiso is null then 0 else compromiso end) as compromiso  \n"
                + " ,(case when devengado is null then 0 else devengado end)as devengado  \n"
                + " ,(case when ejecutado is null then 0 else ejecutado end) as ejecutado  \n"
                + "from (select CUENDT,TAAD02,AUAD02,AUAD01,inicial,dep,POSTM \n"
                + ",(select DISTINCT NOMBMA from USFIMRU.TIGSA_GLm05 where CAUXMA = AUAD02 and POSTMA=POSTM ) as proyecto \n"
                + "from (select * from (select CUENDT,TAAD02,AUAD02,AUAD01, sum(MONTDT) as inicial  \n"
                + "from (SELECT CUENDT,TAAD02,(case when TAAD02 = 'CC' then null else AUAD02 end ) as AUAD02,AUAD01,MONTDT \n"
                + "FROM USFIMRU.TIGSA_GLB01  \n"
                + "where STATDT='E'  \n"
                + "AND CCIADT='CM'  \n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "14  \n"
                + "and MONTDT>0 \n"
                + "and AUAD02  is not null) \n"
                + "group by CUENDT,TAAD02,AUAD02,AUAD01) \n"
                + "left join  \n"
                + "(SELECT CAUXMA,POSTMA as POSTM,( select NOMBZ1 from USFIMRU.TIGSA_GLM11 where CLASZ1 = '001' and ZONAZ1 = SUBSTR(ZONAMA, 0, 2)) as dep FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY') \n"
                + "on AUAD02 = CAUXMA) \n"
                + "where TAAD02 = 'PY') \n"
                + "left join  \n"
                + "(select * from (select CUENDT as cuentar,TAAD02 as auxr,AUAD01 as auxr1,AUAD02 as auxr2, sum(MONTDT) as reforma  \n"
                + "from (SELECT CUENDT,TAAD02,AUAD01,(case when TAAD02 = 'CC' then null else AUAD02 end ) as AUAD02,MONTDT \n"
                + "FROM USFIMRU.TIGSA_GLB01  \n"
                + "where STATDT='E'  \n"
                + "AND CCIADT='CM'  \n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "15 \n"
                + "and MONTDT>0 \n"
                + "and AUAD02  is not null) \n"
                + "where TAAD02 = 'PY' \n"
                + "group by CUENDT,TAAD02,AUAD01,AUAD02)) \n"
                + "on CUENDT= cuentar and TAAD02= auxr and AUAD01 = auxr1 and AUAD02= auxr2 \n"
                + "left join  \n"
                + "(select CUENDT as cuentac,TAAD02 as auxc,AUAD01 as auxc1,AUAD02 as auxc2, sum(MONTDT) as compromiso  \n"
                + "from (SELECT CUENDT,TAAD02,AUAD01,(case when TAAD02 = 'CC' then null else AUAD02 end ) as AUAD02,MONTDT \n"
                + "FROM USFIMRU.TIGSA_GLB01  \n"
                + "where STATDT='E' \n"
                + "AND CCIADT='CM' \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01 \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + " \n"
                + "AND CAUXDT='1' \n"
                + "AND TMOVDT='D') \n"
                + "where TAAD02 = 'PY' \n"
                + "group by CUENDT,TAAD02,AUAD01,AUAD02) \n"
                + "on CUENDT= cuentac and TAAD02= auxc and AUAD01 = auxc1 and AUAD02= auxc2 \n"
                + "left join  \n"
                + "(select CUENDT as cuentad,TAAD02 as auxd,AUAD01 as auxd1,AUAD02 as auxd2, sum(MONTDT) as devengado  \n"
                + "from (SELECT CUENDT,TAAD02,AUAD01,(case when TAAD02 = 'CC' then null else AUAD02 end ) as AUAD02,MONTDT \n"
                + "FROM USFIMRU.TIGSA_GLB01  \n"
                + "where STATDT='E' \n"
                + "AND CCIADT='CM' \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01 \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + " \n"
                + "AND CAUXDT='2' \n"
                + "AND TMOVDT='D' \n"
                + ") \n"
                + "where TAAD02 = 'PY' \n"
                + "group by CUENDT,TAAD02,AUAD01,AUAD02) \n"
                + "on CUENDT= cuentad and TAAD02= auxd and AUAD01 = auxd1 and AUAD02= auxd2 \n"
                + "left join (select CUENDT as cuentae,TAAD02 as auxe,AUAD01 as auxe1,AUAD02 as auxe2, sum(MONTDT) as ejecutado  \n"
                + "from (SELECT CUENDT,TAAD02,AUAD01,(case when TAAD02 = 'CC' then null else AUAD02 end ) as AUAD02,MONTDT \n"
                + "FROM USFIMRU.TIGSA_GLB01  \n"
                + "where STATDT='E' \n"
                + "AND CCIADT='CM' \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01 \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + " \n"
                + "AND CAUXDT='3' \n"
                + "AND TMOVDT='D' \n"
                + ") \n"
                + "where TAAD02 = 'PY' \n"
                + "group by CUENDT,TAAD02,AUAD01,AUAD02) \n"
                + "on CUENDT= cuentad and TAAD02= auxe and AUAD01 = auxe1 and AUAD02= auxe2 \n"
                + "union  \n"
                + "select DISTINCT CUENDT,TAAD02,AUAD02,null as AUAD01,inicial,null as dep,null as POSTM, null as proyecto \n"
                + ",(case when reforma is null then 0 else reforma end)as reforma \n"
                + ",(inicial + (case when reforma is null then 0 else reforma end)) as codificado \n"
                + ",(case when compromiso is null then 0 else compromiso end) as compromiso \n"
                + ",(case when devengado is null then 0 else devengado end)as devengado \n"
                + ",(case when ejecutado is null then 0 else ejecutado end) as ejecutado  \n"
                + "from (select * from ( \n"
                + "select CUENDT,TAAD02, sum(MONTDT) as inicial,AUAD02\n"
                + "from (SELECT CUENDT,TAAD02,MONTDT,AUAD02\n"
                + "FROM USFIMRU.TIGSA_GLB01  \n"
                + "where STATDT='E'  \n"
                + "AND CCIADT='CM'  \n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "14  \n"
                + "and MONTDT>0 \n"
                + "and AUAD02  is not null) \n"
                + "group by CUENDT,TAAD02,AUAD02) \n"
                + "where TAAD02 = 'CC') \n"
                + "left join (select * from ( \n"
                + "select CUENDT as cuentar,TAAD02 as auxr, sum(MONTDT) as reforma,AUAD02  as auxr1\n"
                + "from (SELECT CUENDT,TAAD02,MONTDT,AUAD02 \n"
                + "FROM USFIMRU.TIGSA_GLB01  \n"
                + "where STATDT='E'  \n"
                + "AND CCIADT='CM'  \n"
                + "AND SAPRDT=1" + (Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) - 1) + "15 \n"
                + "and MONTDT>0 \n"
                + "and AUAD02  is not null) \n"
                + "where TAAD02 = 'CC' \n"
                + "group by CUENDT,TAAD02,AUAD02)) \n"
                + "on CUENDT= cuentar and TAAD02= auxr  and AUAD02 = auxr1\n"
                + "left join (select CUENDT as cuentac,TAAD02 as auxc,sum(MONTDT) as compromiso,AUAD02 as auxc1\n"
                + "from (SELECT CUENDT,TAAD02,MONTDT,AUAD02\n"
                + "FROM USFIMRU.TIGSA_GLB01  \n"
                + "where STATDT='E' \n"
                + "AND CCIADT='CM' \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01 \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + " \n"
                + "AND CAUXDT='1' \n"
                + "AND TMOVDT='D' \n"
                + ") \n"
                + "where TAAD02 = 'CC' \n"
                + "group by CUENDT,TAAD02,AUAD02) \n"
                + "on CUENDT= cuentac and TAAD02= auxc  and AUAD02 = auxc1\n"
                + "left join (select CUENDT as cuentad,TAAD02 as auxd ,sum(MONTDT) as devengado, AUAD02 as auxd1\n"
                + "from (SELECT CUENDT,TAAD02,MONTDT ,AUAD02\n"
                + "FROM USFIMRU.TIGSA_GLB01  \n"
                + "where STATDT='E' \n"
                + "AND CCIADT='CM' \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01 \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + " \n"
                + "AND CAUXDT='2' \n"
                + "AND TMOVDT='D' ) \n"
                + "where TAAD02 = 'CC' \n"
                + "group by CUENDT,TAAD02,AUAD02) \n"
                + "on CUENDT= cuentad and TAAD02= auxd and AUAD02 = auxd1\n"
                + "left join (select CUENDT as cuentae,TAAD02 as auxe,sum(MONTDT) as ejecutado,AUAD02 as auxe1\n"
                + "from (SELECT CUENDT,TAAD02,MONTDT,AUAD02\n"
                + "FROM USFIMRU.TIGSA_GLB01  \n"
                + "where STATDT='E' \n"
                + "AND CCIADT='CM' \n"
                + "AND SAPRDT>=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "01 \n"
                + "AND SAPRDT<=1" + Integer.parseInt(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4)) + "" + mes + " \n"
                + "AND CAUXDT='3' \n"
                + "AND TMOVDT='D' ) \n"
                + "where TAAD02 = 'CC' \n"
                + "group by CUENDT,TAAD02,AUAD02) \n"
                + "on CUENDT= cuentae and TAAD02= auxe and AUAD02 = auxe1)");
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
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

    public TablaGenerica getCedulasPresupuesto(Integer inicial, Integer cuenta) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select CUENDT , SUM(inicial) as inicial, POSTMA from (\n"
                + "select CUENDT , POSTMA , SUM(inicial) as inicial  from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as inicial\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=" + inicial + "\n"
                + "GROUP BY CUENDT, AUAD02\n"
                + "having AUAD02 is not null)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY CUENDT,POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 8) as CUENDT , POSTMA , SUM(inicial) as inicial  from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as inicial\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=" + inicial + "\n"
                + "GROUP BY CUENDT, AUAD02\n"
                + "having AUAD02 is not null)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 8),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 6) as CUENDT , POSTMA , SUM(inicial) as inicial  from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as inicial\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=" + inicial + "\n"
                + "GROUP BY CUENDT, AUAD02\n"
                + "having AUAD02 is not null)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 6),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 4) as CUENDT , POSTMA , SUM(inicial) as inicial  from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as inicial\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=" + inicial + "\n"
                + "GROUP BY CUENDT, AUAD02\n"
                + "having AUAD02 is not null)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 4),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 2) as CUENDT , POSTMA , SUM(inicial) as inicial  from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as inicial\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=" + inicial + "\n"
                + "GROUP BY CUENDT, AUAD02\n"
                + "having AUAD02 is not null)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 2),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 1) as CUENDT , POSTMA , SUM(inicial) as inicial  from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as inicial\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=" + inicial + "\n"
                + "GROUP BY CUENDT, AUAD02\n"
                + "having AUAD02 is not null)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 1),POSTMA)\n"
                + "GROUP BY CUENDT,POSTMA\n"
                + "having POSTMA =" + cuenta);
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getPresupuestoReforma(Integer reforma, Integer cuenta) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select CUENDT as CUENDTR, SUM(reforma) as reforma, POSTMA as POSTMAR \n"
                + "from (\n"
                + "select CUENDT , POSTMA , SUM(reforma) as reforma  from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as reforma\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=" + reforma + "\n"
                + "GROUP BY CUENDT, AUAD02\n"
                + "having AUAD02 is not null)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY CUENDT,POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 8) as CUENDT , POSTMA , SUM(reforma) as reforma  from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as reforma\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=" + reforma + "\n"
                + "GROUP BY CUENDT, AUAD02\n"
                + "having AUAD02 is not null)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 8),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 6) as CUENDT , POSTMA , SUM(reforma) as reforma  from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as reforma\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=" + reforma + "\n"
                + "GROUP BY CUENDT, AUAD02\n"
                + "having AUAD02 is not null)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 6),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 4) as CUENDT , POSTMA , SUM(reforma) as reforma  from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as reforma\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=" + reforma + "\n"
                + "GROUP BY CUENDT, AUAD02\n"
                + "having AUAD02 is not null)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 4),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 2) as CUENDT , POSTMA , SUM(reforma) as reforma  from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as reforma\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=" + reforma + "\n"
                + "GROUP BY CUENDT, AUAD02\n"
                + "having AUAD02 is not null)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 2),POSTMA\n"
                + "union\n"
                + "select SUBSTR(CUENDT, 1, 1) as CUENDT , POSTMA , SUM(reforma) as reforma  from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as reforma\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT=" + reforma + "\n"
                + "GROUP BY CUENDT, AUAD02\n"
                + "having AUAD02 is not null)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 1),POSTMA)\n"
                + "GROUP BY CUENDT,POSTMA\n"
                + "having POSTMA =" + cuenta);
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getPresupuestoCompromiso(Integer inicial, Integer fin, Integer cuenta) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select CUENDT as CUENDTc,sum(compromiso) as compromiso,POSTMA as POSTMAc from(\n"
                + "select CUENDT,sum(compromiso) as compromiso,POSTMA from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as compromiso\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + fin + "\n"
                + "AND CAUXDT='1'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT, AUAD02)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY CUENDT,POSTMA\n"
                + "UNION\n"
                + "select SUBSTR(CUENDT, 1, 8) as CUENDT,sum(compromiso) as compromiso,POSTMA from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as compromiso\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + fin + "\n"
                + "AND CAUXDT='1'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT, AUAD02)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 8),POSTMA\n"
                + "UNION\n"
                + "select SUBSTR(CUENDT, 1, 6) as CUENDT,sum(compromiso) as compromiso,POSTMA from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as compromiso\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + fin + "\n"
                + "AND CAUXDT='1'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT, AUAD02)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 6),POSTMA\n"
                + "UNION\n"
                + "select SUBSTR(CUENDT, 1, 4) as CUENDT,sum(compromiso) as compromiso,POSTMA from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as compromiso\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + fin + "\n"
                + "AND CAUXDT='1'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT, AUAD02)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 4),POSTMA\n"
                + "UNION\n"
                + "select SUBSTR(CUENDT, 1, 2) as CUENDT,sum(compromiso) as compromiso,POSTMA from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as compromiso\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + fin + "\n"
                + "AND CAUXDT='1'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT, AUAD02)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 2),POSTMA\n"
                + "UNION\n"
                + "select SUBSTR(CUENDT, 1, 1) as CUENDT,sum(compromiso) as compromiso,POSTMA from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as compromiso\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + fin + "\n"
                + "AND CAUXDT='1'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT, AUAD02)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY SUBSTR(CUENDT, 1, 1),POSTMA)\n"
                + "GROUP BY CUENDT,POSTMA\n"
                + "having POSTMA =" + cuenta);
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getPresupuestoDevengado(Integer inicial, Integer fin, Integer cuenta) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select * from (\n"
                + "select CUENDT as CUENDTD,devengado,POSTMA as POSTMAD,ROUND(((devengado/total)*prorateo),2) as ejecutado\n"
                + "from (\n"
                + "select  CUENDT,devengado,POSTMA,substr(CUENDT,1,2) as pro\n"
                + ",(sum(devengado)over (PARTITION BY substr(CUENDT,1,2) ORDER BY substr(CUENDT,1,2))) as total from (\n"
                + "select CUENDT,sum(devengado) as devengado,POSTMA from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as devengado\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + fin + "\n"
                + "AND CAUXDT='2'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT, AUAD02)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY CUENDT,POSTMA))\n"
                + "left join\n"
                + "(select cuenta,prorateo,(case when substr(cuenta,4,2) = 98 then '97' else substr(cuenta,4,2) end) as cu from (\n"
                + "SELECT substr(CUENDT,1,5) as cuenta, SUM(MONTDT) as prorateo\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "and CCIADT = 'MR'\n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + fin + "\n"
                + "AND TMOVDT='D'\n"
                + "and CUENDT like '213%'\n"
                + "group by substr(CUENDT,1,5)))\n"
                + "on pro= cu\n"
                + "UNION\n"
                + "select SUBSTR(CUENDT, 1, 8) as CUENDT, sum(devengado) as devengado, POSTMA, sum(ejecutado) as ejecutado\n"
                + "from(\n"
                + "select CUENDT,devengado,POSTMA,ROUND(((devengado/total)*prorateo),2) as ejecutado\n"
                + "from (\n"
                + "select  CUENDT,devengado,POSTMA,substr(CUENDT,1,2) as pro\n"
                + ",(sum(devengado)over (PARTITION BY substr(CUENDT,1,2) ORDER BY substr(CUENDT,1,2))) as total from (\n"
                + "select CUENDT,sum(devengado) as devengado,POSTMA from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as devengado\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + fin + "\n"
                + "AND CAUXDT='2'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT, AUAD02)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY CUENDT,POSTMA))\n"
                + "left join\n"
                + "(select cuenta,prorateo,(case when substr(cuenta,4,2) = 98 then '97' else substr(cuenta,4,2) end) as cu from (\n"
                + "SELECT substr(CUENDT,1,5) as cuenta, SUM(MONTDT) as prorateo\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "and CCIADT = 'MR'\n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + fin + "\n"
                + "AND TMOVDT='D'\n"
                + "and CUENDT like '213%'\n"
                + "group by substr(CUENDT,1,5)))\n"
                + "on pro= cu)\n"
                + "group by SUBSTR(CUENDT, 1, 8), POSTMA\n"
                + "UNION\n"
                + "select SUBSTR(CUENDT, 1, 6) as CUENDT, sum(devengado) as devengado, POSTMA, sum(ejecutado) as ejecutado\n"
                + "from(\n"
                + "select CUENDT,devengado,POSTMA,ROUND(((devengado/total)*prorateo),2) as ejecutado\n"
                + "from (\n"
                + "select  CUENDT,devengado,POSTMA,substr(CUENDT,1,2) as pro\n"
                + ",(sum(devengado)over (PARTITION BY substr(CUENDT,1,2) ORDER BY substr(CUENDT,1,2))) as total from (\n"
                + "select CUENDT,sum(devengado) as devengado,POSTMA from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as devengado\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + fin + "\n"
                + "AND CAUXDT='2'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT, AUAD02)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY CUENDT,POSTMA))\n"
                + "left join\n"
                + "(select cuenta,prorateo,(case when substr(cuenta,4,2) = 98 then '97' else substr(cuenta,4,2) end) as cu from (\n"
                + "SELECT substr(CUENDT,1,5) as cuenta, SUM(MONTDT) as prorateo\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "and CCIADT = 'MR'\n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + fin + "\n"
                + "AND TMOVDT='D'\n"
                + "and CUENDT like '213%'\n"
                + "group by substr(CUENDT,1,5)))\n"
                + "on pro= cu)\n"
                + "group by SUBSTR(CUENDT, 1, 6), POSTMA\n"
                + "UNION\n"
                + "select SUBSTR(CUENDT, 1, 4) as CUENDT, sum(devengado) as devengado, POSTMA, sum(ejecutado) as ejecutado\n"
                + "from(\n"
                + "select CUENDT,devengado,POSTMA,ROUND(((devengado/total)*prorateo),2) as ejecutado\n"
                + "from (\n"
                + "select  CUENDT,devengado,POSTMA,substr(CUENDT,1,2) as pro\n"
                + ",(sum(devengado)over (PARTITION BY substr(CUENDT,1,2) ORDER BY substr(CUENDT,1,2))) as total from (\n"
                + "select CUENDT,sum(devengado) as devengado,POSTMA from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as devengado\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + fin + "\n"
                + "AND CAUXDT='2'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT, AUAD02)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY CUENDT,POSTMA))\n"
                + "left join\n"
                + "(select cuenta,prorateo,(case when substr(cuenta,4,2) = 98 then '97' else substr(cuenta,4,2) end) as cu from (\n"
                + "SELECT substr(CUENDT,1,5) as cuenta, SUM(MONTDT) as prorateo\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "and CCIADT = 'MR'\n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + fin + "\n"
                + "AND TMOVDT='D'\n"
                + "and CUENDT like '213%'\n"
                + "group by substr(CUENDT,1,5)))\n"
                + "on pro= cu)\n"
                + "group by SUBSTR(CUENDT, 1, 4), POSTMA\n"
                + "UNION\n"
                + "select SUBSTR(CUENDT, 1, 2) as CUENDT, sum(devengado) as devengado, POSTMA, sum(ejecutado) as ejecutado\n"
                + "from(\n"
                + "select CUENDT,devengado,POSTMA,ROUND(((devengado/total)*prorateo),2) as ejecutado\n"
                + "from (\n"
                + "select  CUENDT,devengado,POSTMA,substr(CUENDT,1,2) as pro\n"
                + ",(sum(devengado)over (PARTITION BY substr(CUENDT,1,2) ORDER BY substr(CUENDT,1,2))) as total from (\n"
                + "select CUENDT,sum(devengado) as devengado,POSTMA from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as devengado\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + fin + "\n"
                + "AND CAUXDT='2'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT, AUAD02)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY CUENDT,POSTMA))\n"
                + "left join\n"
                + "(select cuenta,prorateo,(case when substr(cuenta,4,2) = 98 then '97' else substr(cuenta,4,2) end) as cu from (\n"
                + "SELECT substr(CUENDT,1,5) as cuenta, SUM(MONTDT) as prorateo\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "and CCIADT = 'MR'\n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + fin + "\n"
                + "AND TMOVDT='D'\n"
                + "and CUENDT like '213%'\n"
                + "group by substr(CUENDT,1,5)))\n"
                + "on pro= cu)\n"
                + "group by SUBSTR(CUENDT, 1, 2), POSTMA\n"
                + "UNION\n"
                + "select SUBSTR(CUENDT, 1, 1) as CUENDT, sum(devengado) as devengado, POSTMA, sum(ejecutado) as ejecutado\n"
                + "from(\n"
                + "select CUENDT,devengado,POSTMA,ROUND(((devengado/total)*prorateo),2) as ejecutado\n"
                + "from (\n"
                + "select  CUENDT,devengado,POSTMA,substr(CUENDT,1,2) as pro\n"
                + ",(sum(devengado)over (PARTITION BY substr(CUENDT,1,2) ORDER BY substr(CUENDT,1,2))) as total from (\n"
                + "select CUENDT,sum(devengado) as devengado,POSTMA from (\n"
                + "SELECT CUENDT , AUAD02 , SUM(MONTDT) as devengado\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "AND CCIADT='CM'\n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + fin + "\n"
                + "AND CAUXDT='2'\n"
                + "AND TMOVDT='D'\n"
                + "GROUP BY CUENDT, AUAD02)\n"
                + "left join\n"
                + "(SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'CC'\n"
                + "UNION\n"
                + "SELECT CAUXMA,POSTMA FROM USFIMRU.TIGSA_GLM05 where TAUXMA = 'PY')\n"
                + "on AUAD02 = CAUXMA\n"
                + "GROUP BY CUENDT,POSTMA))\n"
                + "left join\n"
                + "(select cuenta,prorateo,(case when substr(cuenta,4,2) = 98 then '97' else substr(cuenta,4,2) end) as cu from (\n"
                + "SELECT substr(CUENDT,1,5) as cuenta, SUM(MONTDT) as prorateo\n"
                + "FROM USFIMRU.TIGSA_GLB01\n"
                + "where STATDT='E'\n"
                + "and CCIADT = 'MR'\n"
                + "AND SAPRDT>=" + inicial + "\n"
                + "AND SAPRDT<=" + fin + "\n"
                + "AND TMOVDT='D'\n"
                + "and CUENDT like '213%'\n"
                + "group by substr(CUENDT,1,5)))\n"
                + "on pro= cu)\n"
                + "group by SUBSTR(CUENDT, 1, 1), POSTMA)\n"
                + "where POSTMAD =" + cuenta);
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getPresupuestoConcepto() {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select CUENMC\n"
                + ",NOLAAD,nivemc\n"
                + "from USFIMRU.TIGSA_GLM03\n"
                + "where TIPLMC= 'P' and substr(CEDTMC,1,1) in (5,6,7,8,9)\n"
                + "order by CEDTMC,nivemc");
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public void setPresupuestoValores(Integer codigo, String clasificador, Double reforma, String cadena) {
        // Forma el sql para actualizacion
        String strSqlr = "update clas_cedulas_presupuestario\n"
                + "set " + cadena + " =" + reforma + "\n"
                + "where cuenmc= '" + clasificador + "' and postma =" + codigo;
        conSql();
        conSQL.ejecutarSql(strSqlr);
        desSql();
    }

    public void setPresupuestoConcepto(String clasificador, String concepto, Integer nivel) {
        // Forma el sql para actualizacion
        String strSqlr = "update clas_cedulas_presupuestario\n"
                + "set nolaad ='" + concepto + "',\n"
                + "nivemc = " + nivel + "\n"
                + "where cuenmc= '" + clasificador + "'";
        conSql();
        conSQL.ejecutarSql(strSqlr);
        desSql();
    }

    public void setPresupuestoValor(Integer codigo, String clasificador, Double devengado, Double ejecutado) {
        // Forma el sql para actualizacion
        String strSqlr = "update clas_cedulas_presupuestario\n"
                + "set devengado =" + devengado + ",\n"
                + "ejecutado = " + ejecutado + "\n"
                + "where cuenmc= '" + clasificador + "' and postma =" + codigo;
        conSql();
        conSQL.ejecutarSql(strSqlr);
        desSql();
    }

    public void setGastoVaciar() {
        String strSqlr = "delete from clas_cedulas_presupuestario";
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

    public void setDatosParametrosGastos(Integer codigo, String cuenta, String tipo, String num_proyecto, String auxiliar, String nom_proyecto, String direccion, String programa) {
        String strSqlr = "insert into clas_orien_gasto_proyecto (id_codigo,cuenta,tipo,num_proyecto,auxiliar,nom_proyecto,direccion,programa)\n"
                + "values(" + codigo + "," + cuenta + ",'" + tipo + "','" + num_proyecto + "','" + auxiliar + "','" + nom_proyecto + "','" + direccion + "'," + programa + ")";
        conSql();
        conSQL.ejecutarSql(strSqlr);
        desSql();
    }

    public void setCedulaPresupuesto(Integer codigo, Integer cuenmc, String partida, String NOLAAD, Double inicial, Double reforma, Double codificado, Double compromiso, Double devengado, Double ejecutado, Double saldo, Integer nivemc, Integer POSTMA) {
        String strSqlr = "insert into clas_cedulas_presupuestario (id_codigo,cuenmc,partida,NOLAAD,inicial,reforma,codificado,compromiso,devengado,ejecutado,saldo,nivemc,POSTMA)\n"
                + "values (" + codigo + "," + cuenmc + ",'" + partida + "','" + NOLAAD + "'," + inicial + "," + reforma + "," + codificado + "," + compromiso + "," + devengado + "," + ejecutado + "," + saldo + "," + nivemc + "," + POSTMA + ")";
        conSql();
        conSQL.ejecutarSql(strSqlr);
        desSql();
    }

    public void setCedulaInicial(Integer codigo, String cuenmc, Double inicial, Integer POSTMA) {
        String strSqlr = "insert into clas_cedulas_presupuestario (id_codigo,cuenmc,inicial,POSTMA) \n"
                + "VALUES (" + codigo + "," + cuenmc + "," + inicial + "," + POSTMA + ")";
        conSql();
        conSQL.ejecutarSql(strSqlr);
        desSql();
    }

    public TablaGenerica getGastoClasificacion() {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSQL);
        tabFuncionario.setSql("select 1 as ani, 'G' as tipo, cuenta,substring(cuenta,1,2) as gr,substring(cuenta,3,2) as sub,substring(cuenta,5,2) as item ,  \n"
                + " funcion,orientacion,inicial,reforma,codificado,compromiso,devengado,ejecutado  \n"
                + " ,(inicial-devengado) as saldo  \n"
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


    public TablaGenerica getVerificacion(String cuenta, String codigo) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSQL);
        tabFuncionario.setSql("SELECT tipo_presupuesto,partida,clasificador_presu,funcion,orientacion,programa\n"
                + "FROM presu_clasificador\n"
                + "where clasificador_presu =" + cuenta + " and programa = " + codigo);
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public String listaMax1() {
        conSql();

        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSQL);
        tabFuncionario.setSql("select 0 as id ,max(id_codigo) AS maximo from clas_orien_gasto_proyecto");
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

    public TablaGenerica getVerificaClasificador(String cuenta, String orientacion, String tipo, String proyecto, String programa, String funcion) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSQL);
        tabFuncionario.setSql("SELECT\n"
                + "id_codigo,\n"
                + "cuenta,\n"
                + "orientacion\n"
                + "FROM clas_orien_gasto_proyecto\n"
                + "where cuenta= " + cuenta + " and orientacion='" + orientacion + "' and tipo='" + tipo + "' and \n"
                + "num_proyecto=" + proyecto + " and programa=" + programa + " and funcion='" + funcion + "'");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public void setClasificadoGastos(Integer codigo, String clasificador, String funcion) {
        // Forma el sql para actualizacion
        String strSqlr = "update clas_orien_gasto_proyecto\n"
                + "set orientacion =" + clasificador + ",\n"
                + "funcion = " + funcion + "\n"
                + "where id_codigo=" + codigo;
        conSql();
        conSQL.ejecutarSql(strSqlr);
        desSql();
    }

    public void setClasificadorGastos(Integer codigo, String clasificador, String funcion) {
        // Forma el sql para actualizacion
        String strSqlr = "update clas_orien_gasto_proyecto\n"
                + "set orientacion ='" + clasificador + "',\n"
                + "funcion = '" + funcion + "'\n"
                + "where id_codigo=" + codigo;
        conSql();
        conSQL.ejecutarSql(strSqlr);
        desSql();
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

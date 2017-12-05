/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_remuneraciones.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
@Stateless
public class BeanRemuneracion {

    private Utilitario utilitario = new Utilitario();
    private Conexion conSql,//Conexion a la base de sigag
            conNomina;//Conexion a la base de manauto

    public void llenarSolicitud(Integer solicitud, Double valor, Integer cuota, Integer periodo, Integer anio) {
        String auSql = "insert into nom_detalle (id_solicitud,valor,numcuota,ide_periodo,anio) \n"
                + "values (" + solicitud + "," + valor + "," + cuota + "," + periodo + "," + anio + ")";
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void llenarLiquidacion(Integer solicitud, Integer codigo, String fecha, Double valor, String obs, String id, String registro) {
        String auSql = "insert into nom_abono (id_solicitud,cod_abono,fecha_abono,valor,obs_abono,ide_responsable,fecha_registro) \n"
                + "values (" + solicitud + "," + codigo + ",'" + fecha + "'," + valor + ",'" + obs + "','" + id + "','" + registro + "')";
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void setTempRol(String codigo, String cedula, String nombre, Double valor) {
        String auSql = "insert into nom_temp_rol (codtra,cedciu,nomtra,valor)\n"
                + "values('" + codigo + "','" + cedula + "','" + nombre + "'," + valor + ")";
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public TablaGenerica getVerificaSolicitud(String cedula) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT s.id_tipo,s.ced_empleado,s.estado_solicitud,a.id_tipo\n"
                + "FROM nom_solicitud s\n"
                + "INNER JOIN nom_anticipo a ON a.id_solicitud = s.id_solicitud\n"
                + "where s.ced_empleado= '" + cedula + "' \n"
                + "and (s.estado_solicitud = (3) or s.estado_solicitud = (4) or s.estado_solicitud = (7))");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public TablaGenerica getFechaPreCancelacion(String cedula) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT  top 1 s.fecha_ant,s.id_tipo,s.ced_empleado,s.estado_solicitud,d.num_pago,d.fecha_pago \n"
                + "FROM nom_solicitud s \n"
                + "INNER JOIN nom_anticipo a ON a.id_solicitud = s.id_solicitud \n"
                + "left join nom_docu_pago d on d.id_solicitud = s.id_solicitud \n"
                + "where s.ced_empleado= '" + cedula + "'\n"
                + "and s.estado_solicitud = (6)\n"
                + "order by s.fecha_ant desc");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public TablaGenerica getVerificaSolicitudAn(String cedula) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT top 1 s.id_tipo,s.ced_empleado,s.estado_solicitud,a.id_tipo,a.anio_fin,a.mes_fin,s.anio\n"
                + "FROM nom_solicitud s \n"
                + "INNER JOIN nom_anticipo a ON a.id_solicitud = s.id_solicitud \n"
                + "where s.ced_empleado= '" + cedula + "'  and estado_solicitud <> (select id_tipo from nom_tipo where desc_tipo ='Anulado') \n"
                + "order by anio desc");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public TablaGenerica getAutorizaAnticipo(String cedula, Integer anio) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT top 1 id_autoriza, ide_empleado,ced_empleado, pasar_rmu,nom_empleado,autoriza_anticipo,\n"
                + "pasar_rmu,fecha_recomendada,acepta_fecha,pasar_garante,ide_autoriza,ip_autoriza,\n"
                + "pasar_cuota,year(fecha_autoriza) as anio, MONTH(fecha_autoriza) as mes,day(fecha_autoriza) as dia,pasar_fiscal  \n"
                + "from nom_autorizacion\n"
                + "where ced_empleado = '" + cedula + "' and anio_autoriza =" + anio + " and estado is null\n"
                + "order by fecha_autoriza desc");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public TablaGenerica getSolicitud(Integer codigo) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT \n"
                + "s.id_solicitud, \n"
                + "a.fecha, \n"
                + "s.ced_empleado, \n"
                + "s.nom_empleado, \n"
                + "a.valor, \n"
                + "a.cuotas, \n"
                + "a.valor_saldo, \n"
                + "(select saldo_cuota from nom_detalle where id_solicitud = s.id_solicitud and saldo_cuota is not null) as saldo_cuota,\n"
                + "(select id_detalle from nom_detalle where id_solicitud = s.id_solicitud and saldo_cuota is not null) as id_detalle,\n"
                + "(select desc_tipo from nom_tipo where id_tipo = a.id_tipo) as tipo \n"
                + "FROM dbo.nom_solicitud s \n"
                + "INNER JOIN dbo.nom_anticipo a ON a.id_solicitud = s.id_solicitud \n"
                + "where a.estado_anticipo in (select id_tipo from nom_tipo where desc_tipo in('Aprobado','Cobrando')) \n"
                + "and s.id_solicitud =" + codigo);
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public TablaGenerica getVerificaCondicion(String parametro, String disributivo, String tipo) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT id_parametro,ide_parametro,descripcion,id_tipo,parametro,parametro1,parametro2 "
                + "FROM nom_parametros where ide_parametro ='" + parametro + "'and parametro=" + tipo + " and id_tipo like '%" + disributivo + "%'");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public TablaGenerica getVerificaCondicion1(String parametro, String tipo, String puede) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT id_parametro,ide_parametro,descripcion,id_tipo,parametro,parametro1,parametro2\n"
                + "FROM nom_parametros\n"
                + "where ide_parametro = '" + parametro + "' and id_tipo like '%" + tipo + "%' and parametro2 like '%" + puede + "%'");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public TablaGenerica getActivaCondicion(String parametro, String tipo) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT id_parametro,ide_parametro,descripcion,id_tipo,parametro,parametro1,parametro2\n"
                + "FROM nom_parametros\n"
                + "where ide_parametro ='" + parametro + "' and id_tipo like'%" + tipo + "%'");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public TablaGenerica getBuscarTipo(Integer tipo) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT id_tipo,desc_tipo,obs_tipo,descripcion\n"
                + "FROM nom_tipo where id_tipo = " + tipo);
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public TablaGenerica getVerficaGarante(String cedula) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT g.ced_garante,\n"
                + "g.ide_garante\n"
                + "FROM nom_solicitud AS s\n"
                + "INNER JOIN nom_garante g ON g.id_solicitud= s.id_solicitud\n"
                + "WHERE \n"
                + "(s.estado_solicitud = (3) OR\n"
                + "s.estado_solicitud = (4) OR\n"
                + "s.estado_solicitud = (7)) AND\n"
                + "g.ced_garante = '" + cedula + "'");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public TablaGenerica getTipoAnticipo(Integer tipo, String parametro, String busca) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT t.id_tipo,\n"
                + "t.desc_tipo,\n"
                + "p.ide_parametro,\n"
                + "t.descripcion,\n"
                + "p.cuotas,\n"
                + "p.descripcion as permitido,\n"
                + "p.parametro\n"
                + "FROM nom_tipo t\n"
                + "inner join nom_parametros p on t.id_tipo = p.id_tipo\n"
                + "where t.id_tipo = " + tipo + " and " + busca + " like '%" + parametro + "%'");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public String listaMax() {
        conSql();
        String ValorMax;
        TablaGenerica tab_consulta = new TablaGenerica();
        conSql();
        tab_consulta.setConexion(conSql);
        tab_consulta.setSql("select 0 as id, \n"
                + "(case when max(lista_aprobacion) is null then 'LIST-2016-00000' when max(lista_aprobacion)is not null then max(lista_aprobacion) end) AS maximo\n"
                + "from nom_solicitud");
        tab_consulta.ejecutarSql();
        ValorMax = tab_consulta.getValor("maximo");
        return ValorMax;
    }

    public String Parametrolist() {
        conSql();
        String ValorMax;
        TablaGenerica tab_consulta = new TablaGenerica();
        conSql();
        tab_consulta.setConexion(conSql);
        tab_consulta.setSql("select ide_parametro,parametro from nom_parametros where ide_parametro ='VCR'");
        tab_consulta.ejecutarSql();
        ValorMax = tab_consulta.getValor("parametro");
        return ValorMax;
    }

    public TablaGenerica getNumListado(String busca, Integer cod) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select * from (\n"
                + "select ROW_NUMBER() OVER(ORDER BY fecha_aprobacion ) AS Row, fecha_aprobacion, lista_aprobacion,ide_aprobacion,ide_responsable from(SELECT DISTINCT fecha_aprobacion, lista_aprobacion,ide_aprobacion,ide_responsable FROM nom_solicitud  where fecha_aprobacion = '" + busca + "') as a) as b\n"
                + "where Row =" + cod + "\n"
                + "order by lista_aprobacion");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public TablaGenerica getDatosDirector() {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT DESCRIPCION,RESPONSABLE\n"
                + "FROM RESUG_AREA_RESPON\n"
                + "where IDE_CODIGO = (select ID_CODIGO from RESUG_AREA_RESPON where DESCRIPCION = 'TALENTO HUMANO') and ROL= 'D' and ESTADO = 'A'");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
    }

    public TablaGenerica getUsuario(String iden) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT IDE_USUA,NOM_USUA FROM SIS_USUARIO where NICK_USUA like '" + iden + "'");
        tabFuncionario.ejecutarSql();
        conSql.desconectar(true);
        conSql = null;
        return tabFuncionario;
    }

    public TablaGenerica getMesAnterior(String cedula, Integer anio, String mes) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT\n"
                + "codtra,\n"
                + "ingresos,\n"
                + "descuentos,\n"
                + "liquido,\n"
                + "mes,\n"
                + "anio\n"
                + "FROM\n"
                + "dbo.nom_auxiliar\n"
                + "where codtra = " + cedula + " and mes = '" + mes + "' and anio = " + anio + "");
        tabFuncionario.ejecutarSql();
        conSql.desconectar(true);
        conSql = null;
        return tabFuncionario;
    }

    public TablaGenerica getIdSolicitud(Integer codigo) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT id_anticipo,id_solicitud,id_tipo,fecha FROM nom_anticipo\n"
                + "where id_anticipo =" + codigo);
        tabFuncionario.ejecutarSql();
        conSql.desconectar(true);
        conSql = null;
        return tabFuncionario;
    }

    public TablaGenerica getIdDetalle(Integer codigo) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT\n"
                + "id_detalle,\n"
                + "id_tipo,\n"
                + "id_solicitud,\n"
                + "valor,\n"
                + "numcuota,\n"
                + "ide_periodo,\n"
                + "anio\n"
                + "from nom_detalle\n"
                + "where id_solicitud = " + codigo + "");
        tabFuncionario.ejecutarSql();
        conSql.desconectar(true);
        conSql = null;
        return tabFuncionario;
    }

    public TablaGenerica getSolicitudID(Integer codigo) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT\n"
                + "dbo.nom_detalle.id_detalle,\n"
                + "dbo.nom_detalle.id_tipo,\n"
                + "dbo.nom_detalle.id_solicitud,\n"
                + "dbo.nom_detalle.valor,\n"
                + "dbo.nom_detalle.numcuota,\n"
                + "dbo.nom_detalle.ide_periodo,\n"
                + "dbo.nom_detalle.anio,\n"
                + "dbo.nom_detalle.fecha_cobro,\n"
                + "dbo.nom_detalle.valor_rol,\n"
                + "dbo.nom_detalle.saldo_cuota,\n"
                + "dbo.nom_detalle.fecha_registro,\n"
                + "dbo.nom_solicitud.ced_empleado,\n"
                + "dbo.nom_solicitud.nom_empleado,\n"
                + "dbo.nom_solicitud.rmu_empleado\n"
                + "FROM dbo.nom_solicitud \n"
                + "inner join dbo.nom_detalle on dbo.nom_solicitud.id_solicitud = dbo.nom_detalle.id_solicitud\n"
                + "where dbo.nom_detalle.id_detalle =" + codigo + "");
        tabFuncionario.ejecutarSql();
        conSql.desconectar(true);
        conSql = null;
        return tabFuncionario;
    }

    public TablaGenerica getValorAnticipo(String codigo) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT\n"
                + "id_anticipo,\n"
                + "id_solicitud,\n"
                + "id_tipo,\n"
                + "fecha,\n"
                + "valor,\n"
                + "valor_saldo\n"
                + "FROM\n"
                + "dbo.nom_anticipo\n"
                + "where valor_saldo=0 and id_solicitud in (SELECT id_solicitud\n"
                + "FROM nom_solicitud\n"
                + "where  estado_solicitud in (SELECT id_tipo FROM dbo.nom_tipo where desc_tipo = 'Cobrando') and ced_empleado='" + codigo + "')");
        tabFuncionario.ejecutarSql();
        conSql.desconectar(true);
        conSql = null;
        return tabFuncionario;
    }

    public TablaGenerica getAnticipoAprobado(Integer codigo) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT\n"
                + "id_anticipo,\n"
                + "id_solicitud,\n"
                + "id_tipo,\n"
                + "fecha,\n"
                + "valor,\n"
                + "valor_saldo\n"
                + "FROM\n"
                + "dbo.nom_anticipo\n"
                + "where id_solicitud in (SELECT id_solicitud\n"
                + "FROM nom_solicitud\n"
                + "where  estado_solicitud in (SELECT id_tipo FROM dbo.nom_tipo where desc_tipo in('Aprobado','Cobrando')) and ced_empleado='" + codigo + "')");
        tabFuncionario.ejecutarSql();
        conSql.desconectar(true);
        conSql = null;
        return tabFuncionario;
    }

    public TablaGenerica getValorCalculado(Integer codigo, Integer cuota) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select sum(valor) as valor,id_solicitud from (\n"
                + "select top " + cuota + " id_detalle, valor,id_solicitud from nom_detalle \n"
                + "where id_solicitud = " + codigo + " and id_tipo is null order by id_detalle desc) as a\n"
                + "group by id_solicitud");
        tabFuncionario.ejecutarSql();
        conSql.desconectar(true);
        conSql = null;
        return tabFuncionario;
    }

    public String getListaCodigo(Integer id, String cadena) {
        conSql();
        String ValorMax;
        TablaGenerica tab_consulta = new TablaGenerica();
        conSql();
        tab_consulta.setConexion(conSql);
        tab_consulta.setSql("select 0 as id, \n"
                + "(case when max(num_pago) is null then '" + cadena + "-0000' when max(num_pago)is not null then max(num_pago) end) AS maximo\n"
                + "from nom_docu_pago where cod_pago = " + id);
        tab_consulta.ejecutarSql();
//        tab_consulta.imprimirSql();
        ValorMax = tab_consulta.getValor("maximo");
        return ValorMax;
    }

    public TablaGenerica getVerificaPago(Integer codigo) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT\n"
                + "d.id_pago,\n"
                + "d.fecha_documento,\n"
                + "d.num_pago,\n"
                + "d.id_solicitud,\n"
                + "n.ced_empleado,\n"
                + "n.nom_empleado,\n"
                + "d.valor_pagar,\n"
                + "(SELECT desc_tipo FROM nom_tipo where id_tipo =d.cod_pago) as opcion,\n"
                + "(SELECT desc_tipo FROM nom_tipo where id_tipo =d.tipo_pago) as tipo,\n"
                + "d.sub_valor\n"
                + "FROM nom_docu_pago d\n"
                + "inner join nom_solicitud n on d.id_solicitud = n.id_solicitud\n"
                + "where d.id_pago =" + codigo);
        tabFuncionario.ejecutarSql();
        conSql.desconectar(true);
        conSql = null;
        return tabFuncionario;
    }

    public TablaGenerica getVerificaSaldo(Integer codigo, Double valor) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select id_anticipo,id_solicitud,valor,(case when (valor_saldo-" + valor + ") = 0 then 0 else (valor_saldo-" + valor + ")end) as estado \n"
                + "from nom_anticipo where id_solicitud =" + codigo);
        tabFuncionario.ejecutarSql();
        conSql.desconectar(true);
        conSql = null;
        return tabFuncionario;
    }

    public TablaGenerica getVerificaFecha() {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select * from (\n"
                + "select top 1 0 as id,fecha_ant from nom_solicitud  \n"
                + "where estado_solicitud = (select id_tipo from nom_tipo where desc_tipo = 'Aprobado') order by fecha_ant desc) a");
        tabFuncionario.ejecutarSql();
        conSql.desconectar(true);
        conSql = null;
        return tabFuncionario;
    }

    public void llenarListado(Integer solic, String cedula, String lista, String usu) {
        String auSql = "UPDATE nom_solicitud\n"
                + "set lista_aprobacion ='" + lista + "',\n"
                + "ide_aprobacion ='" + usu + "',\n"
                + "fecha_aprobacion ='" + utilitario.getFechaActual() + "'\n"
                + "where id_solicitud = " + solic + " and ced_empleado = '" + cedula + "'";
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void actuaDevolucion(Integer solic, Integer tipo) {
        String auSql = "UPDATE nom_anticipo set estado_anticipo = " + tipo + "\n"
                + "where id_solicitud =" + solic;
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void actuaAutori(Integer solic) {
        String auSql = "update nom_autorizacion\n"
                + "set estado ='1'\n"
                + "where id_autoriza =" + solic;
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void setEncriptar(String cedula, String clave) {
        String auSql = "UPDATE hoja1 set clave = '" + clave + "'\n"
                + "where cedula = '" + cedula + "'";
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void actuaSoliDevolucion(Integer solic) {
        String auSql = "UPDATE nom_solicitud set ide_aprobacion = null,\n"
                + "lista_aprobacion = null,fecha_aprobacion = null,estado_solicitud=null\n"
                + "where id_solicitud =" + solic;
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void negarSolicitud(Integer anti, String estado) {
        String auSql = "update nom_solicitud\n"
                + "set estado_solicitud = (SELECT id_tipo FROM nom_tipo WHERE desc_tipo like " + estado + ")\n"
                + "FROM nom_solicitud\n"
                + "where id_solicitud = " + anti;
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void actuaSolicitud(Integer anti, String cuota, Integer aprob, String usu) {
        String auSql = "update nom_solicitud\n"
                + "set estado_solicitud =" + aprob + ",\n"
                + "ide_aprobacion ='" + usu + "',\n"
                + "fecha_aprobacion ='" + utilitario.getFechaActual() + "'\n"
                + "WHERE id_solicitud=" + anti + " and ced_empleado = '" + cuota + "'";
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void setAnulaSolicitud(Integer anti, String comentario, String usu, String estado) {
        String auSql = "update nom_solicitud\n"
                + "set estado_solicitud = (SELECT id_tipo FROM nom_tipo WHERE desc_tipo like '" + estado + "'),\n"
                + "obs_comentario ='" + comentario + "',\n"
                + "obs_login ='" + usu + "',\n"
                + "obs_fecha ='" + utilitario.getFechaActual() + "'\n"
                + "WHERE id_solicitud=" + anti;
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void actualizSolicitud(Integer anti, String estado) {
        String auSql = "update nom_anticipo\n"
                + "set estado_anticipo =(SELECT id_tipo FROM nom_tipo WHERE desc_tipo like '" + estado + "')\n"
                + "where id_solicitud = " + anti;
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void setActuaAnticipo(Integer anti, String estado, Double valor) {
        String auSql = "update nom_anticipo\n"
                + "set estado_anticipo =(SELECT id_tipo FROM nom_tipo WHERE desc_tipo like " + estado + "),"
                + " valor_pagado = (valor_pagado+" + valor + "),"
                + " valor_saldo = (valor-(valor_pagado+" + valor + "))\n"
                + "where id_solicitud = " + anti;
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void actualizDetalle(Integer anti, String estado, String fecha) {
        String auSql = "update nom_detalle\n"
                + "set id_tipo = (SELECT id_tipo FROM nom_tipo WHERE desc_tipo like " + estado + "),\n"
                + "fecha_cobro = '" + fecha + "' \n"
                + "where id_detalle =" + anti;
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void actualizDetalle1(Integer anti, String estado, String fecha) {
        String auSql = "update nom_detalle\n"
                + "set id_tipo = (SELECT id_tipo FROM nom_tipo WHERE desc_tipo like " + estado + "),\n"
                + "fecha_registro = '" + fecha + "' \n"
                + "where id_detalle =" + anti;
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void descontarDetalle(String id, Integer mes, Integer anio, String fecha, Double valor) {
        String auSql = "update nom_detalle\n"
                + "set id_tipo = (SELECT id_tipo FROM dbo.nom_tipo where desc_tipo = 'Cancelado'),\n"
                + "fecha_cobro = '" + fecha + "'\n"
                + "where id_detalle = (select id_detalle from nom_detalle \n"
                + "where id_solicitud in (SELECT id_solicitud\n"
                + "FROM nom_solicitud\n"
                + "where  estado_solicitud in (SELECT id_tipo FROM dbo.nom_tipo where desc_tipo = 'Aprobado' or desc_tipo ='Cobrando') and ced_empleado='" + id + "')\n"
                + "and id_tipo is null and anio=" + anio + " and ide_periodo = " + mes + ") and valor = " + valor + "";
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void descargoCuotas(String cedula, Integer anio, Integer periodo, String fecha, Double valor) {
        String auSql = "EXEC SIGAG.dbo.PAT_CUOTA_ROL_ANTICIPO_DESCARGO "
                + "@cedula ='" + cedula + "',\n"
                + "@anio =" + anio + ",\n"
                + "@periodo =" + periodo + ",\n"
                + "@fecha ='" + fecha + "',\n"
                + "@valor=" + valor + "";
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void registroAbono(int codigo) {
        String auSql = "EXEC SIGAG.dbo.PAT_REGISTRO_ABONO_CUOTA "
                + "@ID =" + codigo + "";
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void regCuotaAbono(int id, String pago, int cod_pago, double valor, String obs, String login, int detalle) {
        String auSql = "EXEC SIGAG.dbo.NOM_CUOTAS_ANTICIPO "
                + "@id_solicitud =" + id + ","
                + "@num_pago ='" + pago + "',"
                + "@cod_pago =" + cod_pago + ","
                + "@sub_valor =" + valor + ","
                + "@obs_pago ='" + obs + "',"
                + "@login_registro ='" + login + "',"
                + "@id_detalle =" + detalle + "";
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void descontarAnticipo(String id) {
        String auSql = "update nom_anticipo \n"
                + "set nom_anticipo.valor_pagado =a.saldo, \n"
                + "nom_anticipo.cuota_pagada=a.cuota, \n"
                + "nom_anticipo.valor_saldo = (nom_anticipo.valor-a.saldo) \n"
                + "from (select id_solicitud, sum(saldo) as saldo,sum(cuota) as cuota\n"
                + "from (select id_solicitud \n"
                + ",(case when id_tipo = 6 then (case when sum(valor) is null then 0 else sum(valor) end) \n"
                + "when id_tipo = 9 then ((case when sum(valor) is null then 0 else sum(valor) end)- (case when sum(saldo_cuota) is null then 0 else sum(saldo_cuota) end))end )\n"
                + "as saldo\n"
                + ",count(valor)as cuota \n"
                + "from nom_detalle  \n"
                + "where id_solicitud in (SELECT id_solicitud \n"
                + "FROM nom_solicitud \n"
                + "where  estado_solicitud in (SELECT id_tipo FROM dbo.nom_tipo where desc_tipo in('Cobrando','Aprobado')) and ced_empleado='"+id+"') \n"
                + "and id_tipo in (SELECT id_tipo FROM dbo.nom_tipo where desc_tipo in('Cancelado','Abono')) \n"
                + "group by id_solicitud,id_tipo) as a\n"
                + "group by  id_solicitud,cuota) as a \n"
                + "where nom_anticipo.id_solicitud =a.id_solicitud";
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void estadoAnticipo(Integer id, String estado) {
        String auSql = "update nom_anticipo\n"
                + "set estado_anticipo = (SELECT id_tipo FROM dbo.nom_tipo where desc_tipo = '" + estado + "')\n"
                + "where id_anticipo = " + id;
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void estadoSolicitud(Integer id, String estado) {
        String auSql = "update nom_solicitud\n"
                + "set estado_solicitud = (SELECT id_tipo FROM dbo.nom_tipo where desc_tipo = '" + estado + "')\n"
                + "where id_solicitud = " + id;
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void setRegPagos(Integer id, String date, Double valor, String numero, String texto, String archivo, String login, String fecha) {
        String auSql = "update nom_docu_pago\n"
                + "set fecha_pago ='" + date + "',\n"
                + "valor=" + valor + ",\n"
                + "num_documento='" + numero + "',\n"
                + "obs_pago='" + texto + "',\n"
                + "adjunto_abono='" + archivo + "',\n"
                + "login_registro='" + login + "',\n"
                + "fecha_registro='" + fecha + "'\n"
                + "where id_pago =" + id;
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void setActualizaDetalle(Integer id, String estado) {
        String auSql = "update nom_detalle\n"
                + "set id_tipo = (SELECT id_tipo FROM nom_tipo WHERE desc_tipo like '" + estado + "'),\n"
                + "fecha_cobro ='" + utilitario.getFechaActual() + "' \n"
                + "where id_detalle in (select id_detalle from nom_detalle where id_solicitud = " + id + ")";
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void setActualizaActividad(String cedula, String tipo) {
        String auSql = "update dbo.nom_actividades\n"
                + "set tipo ='" + tipo + "'\n"
                + "where cedula = '" + cedula + "'";
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void deleteDevolver(Integer anti) {
        String auSql = "delete from nom_detalle where id_solicitud =" + anti;
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void deleteTempRol() {
        String auSql = "delete from nom_temp_rol";
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public void deleteActividades() {
        String auSql = "delete from nom_actividades";
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    public TablaGenerica getActualServidores(String cedula, String mes, String anio, String id) {
        conNomina();
        TablaGenerica tabDatos = new TablaGenerica();
        conNomina();
        tabDatos.setConexion(conNomina);
        tabDatos.setSql("select * from (\n"
                + "select codtra,nomtra,cedciu,egreso,ingreso,codcia, 1 as id,\n"
                + "(ingreso-egreso) as liquido\n"
                + "from (\n"
                + "select codtra,nomtra,cedciu,codcia\n"
                + "from nodattra)\n"
                + "inner join\n"
                + "(SELECT codtra as codd,\n"
                + "sum(valtrs) as egreso\n"
                + "FROM NOTRANOM\n"
                + "where TIPTRS = 'D' and CODPER = '" + mes + "' and to_char(fecini,'YYYY') = " + anio + "\n"
                + "group by codtra)\n"
                + "on codtra = codd\n"
                + "inner join\n"
                + "(SELECT codtra as codi,\n"
                + "sum(valtrs) as ingreso\n"
                + "FROM NOTRANOM\n"
                + "where TIPTRS = 'I' and CODPER = '" + mes + "' and to_char(fecini,'YYYY') = " + anio + "\n"
                + "group by codtra)\n"
                + "on codtra = codi\n"
                + "where cedciu = '" + cedula + "')\n"
                + "where id=" + id);
        tabDatos.ejecutarSql();
        desNomina();
        return tabDatos;
    }

    public TablaGenerica getAnteriorServidores(String cedula, String mes, String anio, String id) {
        conNomina();
        TablaGenerica tabDatos = new TablaGenerica();
        conNomina();
        tabDatos.setConexion(conNomina);
        tabDatos.setSql("select * from (\n"
                + "select codtra,nomtra,cedciu,egreso,ingreso,codcia, 0 as id, \n"
                + "(ingreso-egreso) as liquido \n"
                + "from (\n"
                + "select DISTINCT codtra,nomtra,cedciu,codcia\n"
                + "from NODTTRPR\n"
                + "where cedciu = '" + cedula + "' and codcon <> '01'\n"
                + "union \n"
                + "select codtra,nomtra,cedciu,codcia\n"
                + "from nodattra\n"
                + "where cedciu = '" + cedula + "')\n"
                + " inner join \n"
                + " (SELECT codtra as codd, \n"
                + " sum(valtrs) as egreso \n"
                + " FROM NOHISNOM \n"
                + " where TIPTRS = 'D' and CODPER = '" + mes + "' and to_char(fecini,'YYYY') = " + anio + "\n"
                + " group by codtra) \n"
                + " on codtra = codd \n"
                + " inner join \n"
                + " (SELECT codtra as codi, \n"
                + " sum(valtrs) as ingreso \n"
                + " FROM NOHISNOM \n"
                + " where TIPTRS = 'I' and CODPER = '" + mes + "' and to_char(fecini,'YYYY') = " + anio + "\n"
                + " group by codtra) \n"
                + " on codtra = codi )\n"
                + " where id=" + id);
        tabDatos.ejecutarSql();
        desNomina();
        return tabDatos;
    }

    public TablaGenerica getVerificaDatos(String cedula) {
        conNomina();
        TablaGenerica tabDatos = new TablaGenerica();
        conNomina();
        tabDatos.setConexion(conNomina);
        tabDatos.setSql("select codtra,nomtra,cedciu,fecnac,fecing,fecsal,suebas,tipctt,fectct,\n"
                + "(select noldat from nodatdat where tipdat='TC' AND CODDAT=TIPCTT) AS RELACION_LABORAL,\n"
                + "tipafi,\n"
                + "(select noldat from nodatdat where tipdat='TP' AND CODDAT=TIPAFI) AS CONTRATO,\n"
                + "tiptra,codcgo,\n"
                + "(SELECT NOLCGO FROM NODATCGO WHERE NODATCGO.CODCGO=NODATTRA.CODCGO AND NODATCGO.CODCIA=NODATTRA.CODCIA) AS CARGO,codare,\n"
                + "(SELECT NOLARE FROM NODATARE WHERE NODATARE.CODCIA=NODATTRA.CODCIA AND NODATARE.CODARE=NODATTRA.CODARE) AS AREA,CODPRS,coddep,codofc,codreg,codcon\n"
                + "from nodattra WHERE cedciu = '" + cedula + "' order by fecing desc");
        tabDatos.ejecutarSql();
        desNomina();
        return tabDatos;
    }

    public TablaGenerica getDatosServidores(String cedula) {
        conNomina();
        TablaGenerica tabDatos = new TablaGenerica();
        conNomina();
        tabDatos.setConexion(conNomina);
        tabDatos.setSql("select CODTRA,CEDCIU,nomtra,FECING,SUEBAS,tipctt,tipafi,tiptra,fectct,codcgo,codare,coddep \n"
                + "from nodattra  \n"
                + "where nodattra.CEDCIU = '" + cedula + "'");
        tabDatos.ejecutarSql();
        desNomina();
        return tabDatos;
    }

    public TablaGenerica getMesActual(String cedula, Integer anio, String mes) {
        conNomina();
        TablaGenerica tabDatos = new TablaGenerica();
        conNomina();
        tabDatos.setConexion(conNomina);
        tabDatos.setSql("select codtra , ingresos,descuentos ,(ingresos-descuentos) as liquido from (   \n"
                + "SELECT codtra,sum(valtrs) as ingresos    \n"
                + "FROM NOTRANOM n where n.TIPTRS = 'I' and CODPER = '" + mes + "' and to_char(fecini,'YYYY') = " + anio + "  \n"
                + "group by n.codtra)\n"
                + "left join    \n"
                + "(SELECT codtra as cod,sum (valtrs) as descuentos    \n"
                + "FROM NOTRANOM where TIPTRS = 'D' and CODPER = '" + mes + "' and to_char(fecini,'YYYY') = " + anio + "  \n"
                + "group by codtra)\n"
                + "on codtra = cod\n"
                + "where (ingresos-descuentos) >0 and codtra = '" + cedula + "'");
        tabDatos.ejecutarSql();
        desNomina();
        return tabDatos;
    }

    public TablaGenerica getMesAnterior1(String cedula, Integer anio, String mes) {
        conNomina();
        TablaGenerica tabDatos = new TablaGenerica();
        conNomina();
        tabDatos.setConexion(conNomina);
        tabDatos.setSql("select codtra , ingresos,descuentos ,(ingresos-descuentos) as liquido from (   \n"
                + "SELECT codtra,sum(valtrs) as ingresos    \n"
                + "FROM NOHISNOM n where n.TIPTRS = 'I' and CODPER = '" + mes + "' and to_char(fecini,'YYYY') = " + anio + "  \n"
                + "group by n.codtra)\n"
                + "left join    \n"
                + "(SELECT codtra as cod,sum (valtrs) as descuentos    \n"
                + "FROM NOHISNOM where TIPTRS = 'D' and CODPER = '" + mes + "' and to_char(fecini,'YYYY') = " + anio + "  \n"
                + "group by codtra)\n"
                + "on codtra = cod\n"
                + "where (ingresos-descuentos) >0 and codtra = '" + cedula + "'");
        tabDatos.ejecutarSql();
        desNomina();
        return tabDatos;
    }

    public TablaGenerica getDatosGarante(String cedula, String garante) {
        conNomina();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conNomina();
        tabFuncionario.setConexion(conNomina);
        tabFuncionario.setSql("SELECT DISTINCT n.CODTRA,n.NOMTRA,n.CEDCIU,n.TIPTRA,d.CODDAT,n.CODCGO,n.SUEBAS,n.tipctt, (SELECT NOLCGO FROM NODATCGO WHERE NODATCGO.CODCGO=n.CODCGO AND NODATCGO.CODCIA=n.CODCIA) AS CARGO \n"
                + "FROM USRRHH.NODATTRA n\n"
                + "inner join USRRHH.NODATDAT d on n.TIPAFI = d.CODDAT\n"
                + "WHERE n.CEDCIU = '" + cedula + "' and d.tipdat='TP' and d.CODDAT IN(" + garante + ")");
        tabFuncionario.ejecutarSql();
        desNomina();
        return tabFuncionario;
    }

    public TablaGenerica getInfoGarante(String garante) {
        conNomina();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conNomina();
        tabFuncionario.setConexion(conNomina);
        tabFuncionario.setSql("SELECT n.CODTRA,n.NOMTRA\n"
                + "FROM USRRHH.NODATTRA n\n"
                + "inner join USRRHH.NODATDAT d on n.TIPAFI = d.CODDAT\n"
                + "where d.tipdat='TP' and d.CODDAT IN(" + garante + ")");
        tabFuncionario.ejecutarSql();
        desNomina();
        return tabFuncionario;
    }

    public TablaGenerica getInfoGarante1() {
        conNomina();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conNomina();
        tabFuncionario.setConexion(conNomina);
        tabFuncionario.setSql("SELECT n.CODTRA,n.NOMTRA\n"
                + "FROM USRRHH.NODATTRA n\n"
                + "inner join USRRHH.NODATDAT d on n.TIPAFI = d.CODDAT\n"
                + "where d.tipdat='TP'");
        tabFuncionario.ejecutarSql();
        desNomina();
        return tabFuncionario;
    }

    public TablaGenerica getAnticipoAct(String mes, Integer anio) {
        conNomina();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conNomina();
        tabFuncionario.setConexion(conNomina);
        tabFuncionario.setSql("SELECT DISTINCT NOTRANOM.codtra as cod, NOTRANOM.valtrs ,NODTTRPR.CEDCIU\n"
                + "FROM NOTRANOM\n"
                + "inner join NODTTRPR on  NOTRANOM.CODTRA = NODTTRPR.codtra\n"
                + "where NOTRANOM.TIPTRS = 'D' and NOTRANOM.CODPER = '" + mes + "' and to_char(NOTRANOM.fecini,'YYYY') =  " + anio + "\n"
                + "and NOTRANOM.CODTRS = 'ANT'");
        tabFuncionario.ejecutarSql();
        desNomina();
        return tabFuncionario;
    }

    public TablaGenerica getAnticipoAnt(String mes, Integer anio) {
        conNomina();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conNomina();
        tabFuncionario.setConexion(conNomina);
        tabFuncionario.setSql("SELECT DISTINCT NOHISNOM.codtra as cod, NOHISNOM.valtrs ,NODTTRPR.CEDCIU\n"
                + "FROM NOHISNOM \n"
                + "inner join NODTTRPR on  NOHISNOM.CODTRA = NODTTRPR.codtra\n"
                + "where NOHISNOM.TIPTRS = 'D' and NOHISNOM.CODPER = '" + mes + "' and to_char(NOHISNOM.fecini,'YYYY') =  " + anio + "\n"
                + "and NOHISNOM.CODTRS = 'ANT'");
        tabFuncionario.ejecutarSql();
        desNomina();
        return tabFuncionario;
    }

    public TablaGenerica getAnticipoRol(String mes, Integer anio, String tipo) {
        conNomina();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conNomina();
        tabFuncionario.setConexion(conNomina);
        tabFuncionario.setSql("select codtra,cedciu,nomtra,tipctt,descuentos\n"
                + "from (select codtra,cedciu,nomtra,tipctt\n"
                + "from nodattra)\n"
                + "left join\n"
                + "(SELECT codtra as cod, valtrs as descuentos\n"
                + "FROM NOHISNOM where TIPTRS = 'D' and CODPER = '" + mes + "' and to_char(fecini,'YYYY') = " + anio + "\n"
                + "and CODTRS = 'ANT')\n"
                + "on codtra =  cod\n"
                + "where descuentos is not null and tipctt = '" + tipo + "'\n"
                + "order by nomtra");
        tabFuncionario.ejecutarSql();
        desNomina();
        return tabFuncionario;
    }

    /**
     * Creates a new instance of BeanRemuneracion
     */
    private void conSql() {
        if (conSql == null) {
            conSql = new Conexion();
            conSql.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        }
    }

    private void desSql() {
        if (conSql != null) {
            conSql.desconectar(true);
            conSql = null;
        }
    }

    private void conNomina() {
        if (conNomina == null) {
            conNomina = new Conexion();
            conNomina.setUnidad_persistencia(utilitario.getPropiedad("oraclejdb"));
        }
    }

    private void desNomina() {
        if (conNomina != null) {
            conNomina.desconectar(true);
            conNomina = null;
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_utilitario.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
@Stateless
public class BeanConstatacion {
    /*
     * Variables para conexion de base de datos
     */

    private Conexion conSql,//Conexion a la base de sql
            conPostgres//Conexion a la base de postgres
            ;
    private Utilitario utilitario = new Utilitario();

    public TablaGenerica Ingreso() {
        conPostgresql();
        TablaGenerica tabCatastro = new TablaGenerica();
        conPostgresql();
        tabCatastro.setConexion(conPostgres);
        tabCatastro.setSql("SELECT clave,codigo FROM cust_bien_inmuebles");
        tabCatastro.ejecutarSql();
        desPostgresql();
        return tabCatastro;
    }

    public TablaGenerica CatastroAvaluos() {
        conSql();
        TablaGenerica tabCatastro = new TablaGenerica();
        conSql();
        tabCatastro.setConexion(conSql);
        tabCatastro.setSql("select clave,clave as catastro,barrio from prediosv where propietario like 'municipio%' or clave='100100146000' or clave='080206010000' or clave='080102801000' order by clave");
        tabCatastro.ejecutarSql();
        desSql();
        return tabCatastro;
    }

    public TablaGenerica CatastroBienes() {
        conPostgresql();
        TablaGenerica tabCatastro = new TablaGenerica();
        conPostgresql();
        tabCatastro.setConexion(conPostgres);
        tabCatastro.setSql("SELECT\n"
                + "clavecatastral as clave,\n"
                + "clavecatastral as catastro,\n"
                + "des_activo,\n"
                + "ide_activo,\n"
                + "id_ubica\n"
                + "FROM afi_activos\n"
                + "where  nombre like 'TERRENO%' AND clavecatastral is not null and clavecatastral<>'0200213000'\n"
                + "order by clavecatastral");
        tabCatastro.ejecutarSql();
        desPostgresql();
        return tabCatastro;
    }

    public TablaGenerica getBienes(String id) {
        conPostgresql();
        TablaGenerica tabCatastro = new TablaGenerica();
        conPostgresql();
        tabCatastro.setConexion(conPostgres);
        tabCatastro.setSql("SELECT a.clavecatastral,a.codigo,(a.val_compra+a.val_reavaluo) as val_compra,\n"
                + "(SELECT cue_codigo FROM conc_catalogo_cuentas where ide_cuenta = a.ide_cuenta) AS cuenta,\n"
                + "(SELECT cue_descripcion FROM conc_catalogo_cuentas where ide_cuenta = a.ide_cuenta) AS descripcion,\n"
                + "(case when a.propiedad = 'M' then 'Municipio' \n"
                + "when a.propiedad = 'C' then 'Comodato' \n"
                + "when a.propiedad = 'P' then 'Particular' end) AS propiedad,\n"
                + "(SELECT o.des_oficina FROM afi_ubica f \n"
                + "INNER JOIN afi_direccion d ON f.id_direccion = d.id_direccion \n"
                + "INNER JOIN afi_dependencia e ON f.id_dependencia = e.id_dependencia \n"
                + "INNER JOIN afi_oficina o ON f.id_oficina = o.id_oficina \n"
                + "where f.id_ubica =a.id_ubica ) AS ubicacion\n"
                + "FROM afi_activos AS a where a.clavecatastral = '" + id + "' or a.des_activo like '%" + id + "%'");
        tabCatastro.ejecutarSql();
        desPostgresql();
        return tabCatastro;
    }

    public TablaGenerica getActas(Integer id) {
        conPostgresql();
        TablaGenerica tabCatastro = new TablaGenerica();
        conPostgresql();
        tabCatastro.setConexion(conPostgres);
        tabCatastro.setSql("SELECT c.id_inmueble,c.numero_acta,c.responsable_bienes,c.responsable_administrativo,c.clave,a.codigo,a.val_compra,\n"
                + "(SELECT cue_codigo FROM conc_catalogo_cuentas where ide_cuenta = a.ide_cuenta) as cuenta,\n"
                + "(SELECT cue_descripcion FROM conc_catalogo_cuentas where ide_cuenta = a.ide_cuenta) as descripcion,\n"
                + "(case when a.propiedad = 'M' then 'Municipio'\n"
                + "when a.propiedad = 'C' then 'Comodato'\n"
                + "when a.propiedad = 'P' then 'Particular' end) as propiedad,\n"
                + "(SELECT o.des_oficina FROM afi_ubica f\n"
                + "left JOIN afi_direccion d ON f.id_direccion = d.id_direccion\n"
                + "left JOIN afi_dependencia e ON f.id_dependencia = e.id_dependencia\n"
                + "left JOIN afi_oficina o ON f.id_oficina = o.id_oficina\n"
                + "where f.id_ubica =a.id_ubica ) as ubicacion\n"
                + "FROM cust_bien_inmuebles c\n"
                + "left join afi_activos a on c.clave=a.clavecatastral where c.id_inmueble =" + id);
        tabCatastro.ejecutarSql();
        desPostgresql();
        return tabCatastro;
    }

    public TablaGenerica getActaInmuebles(String acta, String clave) {
        conPostgresql();
        TablaGenerica tabCatastro = new TablaGenerica();
        conPostgresql();
        tabCatastro.setConexion(conPostgres);
        tabCatastro.setSql("SELECT id_inmueble,clave,fecha,numero_acta FROM cust_bien_inmuebles where numero_acta = '" + acta + "' and clave ='" + clave + "'");
        tabCatastro.ejecutarSql();
        desPostgresql();
        return tabCatastro;
    }

    public String getBienes() {
        conPostgresql();
        String nombre;
        TablaGenerica tab_consulta = new TablaGenerica();
        conPostgresql();
        tab_consulta.setConexion(conPostgres);
        tab_consulta.setSql("select 0 as id, e.cod_empleado FROM srh_empleado e inner join srh_cargos c on e.cod_cargo = c.cod_cargo where e.cedula_pass='1706539051'");
        tab_consulta.ejecutarSql();
        nombre = tab_consulta.getValor("cod_empleado");
        return nombre;
    }

    public String getAdministrativo() {
        conPostgresql();
        String nombre;
        TablaGenerica tab_consulta = new TablaGenerica();
        conPostgresql();
        tab_consulta.setConexion(conPostgres);
        tab_consulta.setSql("select 0 as id, e.cod_empleado FROM srh_empleado e inner join srh_cargos c on e.cod_cargo = c.cod_cargo where e.cod_cargo = 577");
        tab_consulta.ejecutarSql();
        nombre = tab_consulta.getValor("cod_empleado");
        return nombre;
    }

    public TablaGenerica getCatastro(String clave) {
        conSql();
        TablaGenerica tabCatastro = new TablaGenerica();
        conSql();
        tabCatastro.setConexion(conSql);
        tabCatastro.setSql("SELECT clave,cedula,propietario,direccion,barrio,calles,canton,construccion,valor,parroquia,predio,registro,metros\n"
                + "from prediosv\n"
                + "where clave = '" + clave + "'");
        tabCatastro.ejecutarSql();
        desSql();
        return tabCatastro;
    }

    public TablaGenerica getActa(String acta, Integer codigo) {
        conPostgresql();
        TablaGenerica tabCatastro = new TablaGenerica();
        conPostgresql();
        tabCatastro.setConexion(conPostgres);
        tabCatastro.setSql("SELECT DISTINCT\n"
                + "fec_entrega,\n"
                + "num_acta,\n"
                + "ide_custodio,\n"
                + "delegado_direccion\n"
                + "from cust_custodio\n"
                + "where num_acta='" + acta + "' and ide_custodio =" + codigo);
        tabCatastro.ejecutarSql();
        desPostgresql();
        return tabCatastro;
    }

    public void setActa(String clave, String fecha, String acta, String login, String bienes, String administrativo) {
        String parametro = "insert into cust_bien_inmuebles(clave,fecha,numero_acta,ip_registro,login_registro,responsable_bienes,responsable_administrativo)\n"
                + "values ('" + clave + "','" + fecha + "','" + acta + "','" + utilitario.getIp() + "','" + login + "','" + bienes + "','" + administrativo + "')";
        conPostgresql();
        conPostgres.ejecutarSql(parametro);
        desPostgresql();
    }

    public String maxComprobantes(Integer anio) {
        conPostgresql();
        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conPostgresql();
        tabFuncionario.setConexion(conPostgres);
        tabFuncionario.setSql("select 0 as id ,(case when max(numero_acta) is null then '0' when max(numero_acta)is not null then max(numero_acta) end) AS maximo\n"
                + "from cust_bien_inmuebles where anio="+anio);
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("maximo");
        desPostgresql();
        return ValorMax;
    }

    /*
     * Funcion para conectar y deconectar, a la base
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

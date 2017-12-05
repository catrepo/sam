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
public class BeanEncuestaSatisfaccion {

    private Conexion conSql,//Conexion a la base de sql
            conPostgres//Conexion a la base de postgres
            ;
    private Utilitario utilitario = new Utilitario();

    public TablaGenerica busCredenciales(String cedula,String cadena) {
        conSql();
        TablaGenerica tabCatastro = new TablaGenerica();
        conSql();
        tabCatastro.setConexion(conSql);
        tabCatastro.setSql("SELECT * FROM catastrov where "+cadena+" ='"+cedula+"'");
        tabCatastro.ejecutarSql();
        desSql();
        return tabCatastro;
    }

    public TablaGenerica getDirecciones() {
        conPostgresql();
        TablaGenerica tabCatastro = new TablaGenerica();
        conPostgresql();
        tabCatastro.setConexion(conPostgres);
        tabCatastro.setSql("SELECT cod_direccion,nombre_dir FROM srh_direccion where estado_dir ='ACTIVA' order by nombre_dir");
        tabCatastro.ejecutarSql();
        desPostgresql();
        return tabCatastro;
    }

    public TablaGenerica getUsuarios() {
        conPostgresql();
        TablaGenerica tabCatastro = new TablaGenerica();
        conPostgresql();
        tabCatastro.setConexion(conPostgres);
        tabCatastro.setSql("SELECT cod_empleado,nombres FROM srh_empleado where estado = 1 order by nombres");
        tabCatastro.ejecutarSql();
        desPostgresql();
        return tabCatastro;
    }

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

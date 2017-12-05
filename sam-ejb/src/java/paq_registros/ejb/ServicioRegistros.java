/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_registros.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;
import persistencia.Conexion;

/**
 *
 * @author Diego
 */
@Stateless
public class ServicioRegistros {

    private Utilitario utilitario = new Utilitario();
    private Conexion conSql,//Conexion a la base de sigag
            con_manauto,//Conexion a la base de manauto
            con_postgres,//Cnexion a la base de postgres 2014
            con_ciudadania; //Conexion a la base de ciudadania

    public TablaGenerica getPersona(String cedula) {
        //Busca a una persona en la tabla maestra por número de cédula
        con_ciudadanos();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_ciudadania);
        tab_persona.setSql("SELECT * FROM MAESTRO WHERE cedula='" + cedula.substring(0, cedula.length() - 1) + "' and digito_verificador='" + cedula.substring(cedula.length() - 1) + "'");
        tab_persona.ejecutarSql();
        con_ciudadania.desconectar(true);
        con_ciudadania = null;
        return tab_persona;
    }

    public TablaGenerica getPersonaPasaporte(String pasaporte) {
        //Busca a una persona en la tabla maestra por número de pasaporte
        con_ciudadanos();
        TablaGenerica tab_persona = new TablaGenerica();
        tab_persona.setConexion(con_ciudadania);
        tab_persona.setSql("SELECT * FROM MAESTRO_PASAPORTE WHERE CODIGO='" + pasaporte + "'");
        tab_persona.ejecutarSql();
        con_ciudadania.desconectar(true);
        con_ciudadania = null;
        return tab_persona;
    }

    public TablaGenerica getEmpresa(String ruc) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        con_ciudadanos();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(con_ciudadania);
        tabPersona.setSql("SELECT * FROM MAESTRO_RUC WHERE RUC='" + ruc + "'");
        tabPersona.ejecutarSql();
        con_ciudadania.desconectar(true);
        con_ciudadania = null;
        return tabPersona;
    }

    public TablaGenerica getCiudadano(String cedula) {
        //Busca a una empresa en la tabla maestra_ruc por ruc
        conSql();
        TablaGenerica tabPersona = new TablaGenerica();
        tabPersona.setConexion(conSql);
        tabPersona.setSql("SELECT top 1 cedula,propietario,parroquia,barrio,predio,calles\n"
                + "FROM prediosv\n"
                + "where cedula = '" + cedula + "'");
        tabPersona.ejecutarSql();
        desSql();
        return tabPersona;
    }

    private void con_ciudadanos() {
        if (con_ciudadania == null) {
            con_ciudadania = new Conexion();
            con_ciudadania.setUnidad_persistencia(utilitario.getPropiedad("ciudadania"));
        }
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
}

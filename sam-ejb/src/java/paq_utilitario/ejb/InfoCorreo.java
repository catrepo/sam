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
 * @author p-chumana
 */
@Stateless
public class InfoCorreo {

    private Utilitario utilitario = new Utilitario();
    private Conexion conSql;//Conexion a la base de manauto

    public TablaGenerica getVerificaSolicitud(String tipo) {
        conSql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("SELECT CORREO_CORR,PASS_CORR,SMTP_HOST,SMTP_STARTTLS,SMTP_PORT,SMTP_AUTH,NOTA_CORR,APLIC_CORR\n"
                + "FROM SIS_CORREO\n"
                + "where APLIC_CORR ='" + tipo + "'");
        tabFuncionario.ejecutarSql();
        desSql();
        return tabFuncionario;
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

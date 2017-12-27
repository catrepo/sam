/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_utilitario.ejb;

import framework.aplicacion.TablaGenerica;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.xml.ws.WebServiceRef;
import sistema.aplicacion.Utilitario;
import paq_webservice.ClassCiudadania;
import paq_webservice.ConsultaCiudadano;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
@Stateless
public class ClaseGenerica {

    
    
    @WebServiceRef(wsdlLocation = "META-INF/wsdl/186.46.84.36/ConsultaCiudadania/ciudadania.asmx.wsdl")//37
    private ConsultaCiudadano service;
    /*
     * Objeto para conexion de base de datos
     */
    private Conexion conSql,//Conexion a la base de sql
            conPostgres//Conexion a la base de postgres
            ;
    private List lisCatastro = new ArrayList();
    private String secMes = new String();
    /*
     * Objeto para conexion
     */
    private Utilitario utilitario = new Utilitario();

    public List buscaCiudadano(String cedula) {
        lisCatastro.clear();
        String usu = "reclamos";
        String pass = "buzon2016$";
        try {
            service.getConsultaCiudadanoSoap12().busquedaPorCedula(cedula, usu, pass);
            Object[] obj = new Object[5];
            obj[0] = service.getConsultaCiudadanoSoap12().busquedaPorCedula(cedula, usu, pass).getCedula();
            obj[1] = service.getConsultaCiudadanoSoap12().busquedaPorCedula(cedula, usu, pass).getNombre();
            obj[2] = service.getConsultaCiudadanoSoap12().busquedaPorCedula(cedula, usu, pass).getDireccion();
            obj[3] = service.getConsultaCiudadanoSoap12().busquedaPorCedula(cedula, usu, pass).getTelefono();
            obj[4] = service.getConsultaCiudadanoSoap12().busquedaPorCedula(cedula, usu, pass).getSexo();
            lisCatastro.add(obj);
        } catch (Exception e) {
        }
        return lisCatastro;
    }

    /*
     * función para sacar todos los campos de una tabla
     */
    public TablaGenerica getNumeroCampos(String nombre) {
        conPostgresql();
        TablaGenerica tabTabla = new TablaGenerica();
        conPostgresql();
        tabTabla.setConexion(conPostgres);
        tabTabla.setSql("SELECT table_name,count(*) As NumeroCampos\n"
                + "FROM information_schema.columns \n"
                + "WHERE table_name = '" + nombre + "'\n"
                + "GROUP BY table_name");
        tabTabla.ejecutarSql();
        desPostgresql();
        return tabTabla;
    }

    public TablaGenerica getCatalogoTabla(String datos, String tabla, String condicion) {
        conPostgresql();
        TablaGenerica tabTabla = new TablaGenerica();
        conPostgresql();
        tabTabla.setConexion(conPostgres);
        tabTabla.setSql("select " + datos + " from " + tabla + " where " + condicion + "");
        tabTabla.ejecutarSql();
        desPostgresql();
        return tabTabla;
    }

    public TablaGenerica getEstrucTabla(String nombre, Integer posicion) {
        conPostgresql();
        TablaGenerica tabTabla = new TablaGenerica();
        conPostgresql();
        tabTabla.setConexion(conPostgres);
        tabTabla.setSql("SELECT ordinal_position,column_name, data_type\n"
                + "FROM information_schema.columns \n"
                + "WHERE table_name = '" + nombre + "' and ordinal_position = " + posicion);
        tabTabla.ejecutarSql();
        desPostgresql();
        return tabTabla;
    }

    public void setActuaRegistro(Integer codigo, String desc, String dato, String valor, String cadena) {
        String auSql = "update " + desc + " set\n"
                + "" + dato + " ='" + valor + "'\n" +//mve_horometro
                "where " + cadena + "=" + codigo;
        conPostgresql();
        conPostgres.ejecutarSql(auSql);
        desPostgresql();
    }
    /*
     * Funcion para conectar y deconectar, a la base
     */

    /*
     * Permite identificar el mes abreviado
     */
    public String meses(Integer numero) {
        switch (numero) {
            case 12:
                secMes = "12";
                break;
            case 11:
                secMes = "11";
                break;
            case 10:
                secMes = "10";
                break;
            case 9:
                secMes = "09";
                break;
            case 8:
                secMes = "08";
                break;
            case 7:
                secMes = "07";
                break;
            case 6:
                secMes = "06";
                break;
            case 5:
                secMes = "05";
                break;
            case 4:
                secMes = "04";
                break;
            case 3:
                secMes = "03";
                break;
            case 2:
                secMes = "02";
                break;
            case 1:
                secMes = "01";
                break;
        }
        return secMes;
    }

    public String verificaNull(String myString) {
        if (myString == null) {
            secMes = "0";
        } else {
            secMes = myString;
        }
        return secMes;
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

    private ClassCiudadania busquedaPorCedula(java.lang.String cedula, java.lang.String usuario, java.lang.String password) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        paq_webservice.ConsultaCiudadanoSoap port = service.getConsultaCiudadanoSoap();
        return port.busquedaPorCedula(cedula, usuario, password);
    }
}

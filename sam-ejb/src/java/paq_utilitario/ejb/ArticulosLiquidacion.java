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
public class ArticulosLiquidacion {

    private Conexion conSql, conER,
            conPostgres;//Conexion a la base de sigag
    private Utilitario utilitario = new Utilitario();

    public TablaGenerica getArticulos() {
        conPostgresql();
        TablaGenerica tabArticulos = new TablaGenerica();
        conPostgresql();
        tabArticulos.setConexion(conPostgres);
        tabArticulos.setSql("select a.ide_bodt_articulo,c.des_material from bodt_articulos a\n"
                + "inner join bodc_material c on a.ide_mat_bodega = c.ide_mat_bodega\n"
                + "where c.des_material like 'MEDIDOR DE AGUA DE%' or c.des_material like'LLAVE DE COMPUERTA%'\n"
                + "and a.ide_bodt_articulo <>5427 or a.ide_bodt_articulo =9780");
        tabArticulos.ejecutarSql();
        desPostgresql();
        return tabArticulos;
    }

    public TablaGenerica getInfoTasa(Integer codigo) {
        conEr();
        TablaGenerica tabCatastro = new TablaGenerica();
        conEr();
        tabCatastro.setConexion(conER);
        tabCatastro.setSql("SELECT * FROM er..TASA where codigo=" + codigo);
        tabCatastro.ejecutarSql();
        desEr();
        return tabCatastro;
    }

    public TablaGenerica ListaArticulo(Integer codigo) {
        conEr();
        TablaGenerica tabCatastro = new TablaGenerica();
        conEr();
        tabCatastro.setConexion(conER);
        tabCatastro.setSql("SELECT * FROM er..TASA where id_tasa=" + codigo);
        tabCatastro.ejecutarSql();
        desEr();
        return tabCatastro;
    }

    public TablaGenerica ConteoMedidor() {
        conSql();
        TablaGenerica tabCatastro = new TablaGenerica();
        conSql();
        tabCatastro.setConexion(conSql);
        tabCatastro.setSql("SELECT count(TIPO_ARTICULO) as conteo,TIPO_ARTICULO\n"
                + "FROM AAT_MOVIENTOS\n"
                + "where AAT_MOVIENTOS.ESTADO = 'D'\n"
                + "GROUP BY TIPO_ARTICULO\n"
                + "HAVING count(TIPO_ARTICULO)>0");
        tabCatastro.ejecutarSql();
        desSql();
        return tabCatastro;
    }

    public TablaGenerica AsignacionMedidor(Integer codigo) {
        conSql();
        TablaGenerica tabCatastro = new TablaGenerica();
        conSql();
        tabCatastro.setConexion(conSql);
        tabCatastro.setSql("SELECT top 1 ID_MOVIMIENTO,TIPO_ARTICULO,SERIE FROM AAT_MOVIENTOS where AAT_MOVIENTOS.ESTADO = 'D' and tipo_articulo=" + codigo);
        tabCatastro.ejecutarSql();
        desEr();
        return tabCatastro;
    }

    public TablaGenerica ConteoLlave(Integer codigo) {
        conSql();
        TablaGenerica tabCatastro = new TablaGenerica();
        conSql();
        tabCatastro.setConexion(conSql);
        tabCatastro.setSql("SELECT top 1 ID_MOVIMIENTO,TIPO_ARTICULO,stock FROM AAT_MOVIENTOS where AAT_MOVIENTOS.ESTADO = 'D' and STOCK>0 and tipo_articulo=" + codigo);
        tabCatastro.ejecutarSql();
        desEr();
        return tabCatastro;
    }

    public TablaGenerica ConteoLla(Integer codigo) {
        conSql();
        TablaGenerica tabCatastro = new TablaGenerica();
        conSql();
        tabCatastro.setConexion(conSql);
        tabCatastro.setSql("SELECT top 1 ID_MOVIMIENTO,TIPO_ARTICULO,stock FROM AAT_MOVIENTOS where AAT_MOVIENTOS.ESTADO = 'D' and STOCK=>1 and tipo_articulo=" + codigo);
        tabCatastro.ejecutarSql();
        desEr();
        return tabCatastro;
    }

    public String maxComprobantes(String tipo) {
        conSql();
        String ValorMax;
        TablaGenerica tabFuncionario = new TablaGenerica();
        conSql();
        tabFuncionario.setConexion(conSql);
        tabFuncionario.setSql("select 0 as id ,(case when max(numero) is null then '0' when max(numero)is not null then max(numero) end) AS maximo\n"
                + "from aat_cabecera_movimiento where tipo_movimiento = '" + tipo + "'");
        tabFuncionario.ejecutarSql();
        ValorMax = tabFuncionario.getValor("maximo");
        desSql();
        return ValorMax;
    }

    public void setActuaRegis(Integer codigo, String desc) {
        String auSql = "update AAT_MOVIENTOS\n"
                + "set " + desc + "\n"
                + "where id_movimiento =" + codigo;
        conSql();
        conSql.ejecutarSql(auSql);
        desSql();
    }

    private void conEr() {
        if (conER == null) {
            conER = new Conexion();
            conER.setUnidad_persistencia(utilitario.getPropiedad("recursojdbc"));
        }
    }

    private void desEr() {
        if (conER != null) {
            conER.desconectar(true);
            conER = null;
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

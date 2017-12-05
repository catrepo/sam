/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
public class ListaRetencionJudicial extends Pantalla {

    private Conexion conPostgres = new Conexion();
    private Tabla tabTabla = new Tabla();
    private Tabla tabConsulta = new Tabla();

    public ListaRetencionJudicial() {
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        tabTabla.setId("tabTabla");
        tabTabla.setConexion(conPostgres);
        tabTabla.setTabla("conc_lista_retenciones", "id_retencion", 1);
        tabTabla.getColumna("cod_empleado").setFiltro(true);
        tabTabla.getColumna("beneficiario").setFiltro(true);
        tabTabla.getColumna("cod_empleado").setCombo("select cod_empleado,nombres from srh_empleado where estado = 1");
        tabTabla.getColumna("cod_empleado").setAutoCompletar();
        tabTabla.getColumna("fecha_ingreso").setValorDefecto(utilitario.getFechaActual());
        tabTabla.getColumna("login").setValorDefecto(utilitario.getVariable("NICK"));
        tabTabla.getColumna("ip").setValorDefecto(utilitario.getIp());
        tabTabla.setHeader("Listado Retenciones Judiciales");
        List lista = new ArrayList();
        Object fila1[] = {
            "A", "A"
        };
        Object fila2[] = {
            "D", "D"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        tabTabla.getColumna("estado").setRadio(lista, null);
        tabTabla.getColumna("estado").setMetodoChange("estado");
        tabTabla.setRows(10);
        tabTabla.dibujar();
        tabTabla.getColumna("fecha_ingreso").setVisible(false);
        tabTabla.getColumna("login").setVisible(false);
        tabTabla.getColumna("ip").setVisible(false);
        tabTabla.getColumna("fecha_salida").setVisible(false);
        tabTabla.getColumna("id_retencion").setVisible(false);
        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabTabla);

        Division div = new Division();
        div.setId("div");
        div.dividir1(pnt);
        agregarComponente(div);
    }

    public void estado() {
        if (tabTabla.getValor("estado").equals("D")) {
            tabTabla.getColumna("fecha_salida").setValorDefecto(utilitario.getFechaActual());
            utilitario.addUpdate("tabTabla");
        }
    }

    @Override
    public void insertar() {
        tabTabla.insertar();
    }

    @Override
    public void guardar() {
        tabTabla.guardar();
        conPostgres.guardarPantalla();
    }

    @Override
    public void eliminar() {
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_evaluacion;

import framework.componentes.AutoCompletar;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-sistemas
 */
public class AutorizacionEncuesta extends Pantalla {

    private Tabla tabTabla = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Panel panOpcion = new Panel();
    private AutoCompletar autBusca = new AutoCompletar();

    public AutorizacionEncuesta() {
        tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT ID_AUTORIZA,FECHAT,EXTENCION FROM ESF_AUTORIZA_ENCUESTA");
        autBusca.setSize(80);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("AUTORIZACIÓN DE REGISTRO DE ENCUESTA DE SATISFACCIÓN DE SERVICIOS");
        agregarComponente(panOpcion);

        dibujarPantalla();

    }

    public void dibujarPantalla() {
        limpiarPanel();
        tabTabla.setId("tabTabla");
        tabTabla.setTabla("esf_autoriza_encuesta", "id_autoriza", 0);
        if (autBusca.getValue() == null) {
            tabTabla.setCondicion("id_autoriza=-1");
        } else {
            tabTabla.setCondicion("id_autoriza=" + autBusca.getValor());
        }
        tabTabla.getColumna("fechat").setValorDefecto(utilitario.getFechaActual());
        tabTabla.getColumna("login").setValorDefecto(utilitario.getVariable("NICK"));
        tabTabla.getColumna("fecha_autorizacion").setValorDefecto(utilitario.getFechaHoraActual());
        tabTabla.getColumna("login").setVisible(false);
        tabTabla.getColumna("fecha_autorizacion").setVisible(false);
        tabTabla.setTipoFormulario(true);
        tabTabla.getGrid().setColumns(2);
        tabTabla.dibujar();
        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabTabla);
        Grupo gru = new Grupo();
        gru.getChildren().add(pnt);
        panOpcion.getChildren().add(gru);
    }

    private void limpiarPanel() {
        //borra el contenido de la división central central
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tabTabla.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }
}

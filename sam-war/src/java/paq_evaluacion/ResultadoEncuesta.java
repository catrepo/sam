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
public class ResultadoEncuesta extends Pantalla {

    private Tabla tabTabla = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Panel panOpcion = new Panel();
    private AutoCompletar autBusca = new AutoCompletar();

    public ResultadoEncuesta() {

        tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT ID_ENCUESTA,FECHAE FROM ESF_ENCUESTA");
        autBusca.setSize(80);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("RESULTADOS DE ENCUESTAS DE SATISFACCIÓN REALIZADAS");
        agregarComponente(panOpcion);

        dibujarPantalla();
    }

    public void dibujarPantalla() {
        tabTabla.setId("tabTabla");
        tabTabla.setTabla("esf_encuesta", "id_encuesta", 1);
        if (autBusca.getValue() == null) {
            tabTabla.setCondicion("id_encuesta=-1");
        } else {
            tabTabla.setCondicion("id_encuesta=" + autBusca.getValor());
        }
        tabTabla.getColumna("excelente").setMetodoChange("calculo");
        tabTabla.getColumna("muy_bueno").setMetodoChange("calculo");
        tabTabla.getColumna("bueno").setMetodoChange("calculo");
        tabTabla.getColumna("regular").setMetodoChange("calculo");
        tabTabla.getColumna("mala").setMetodoChange("calculo");
        tabTabla.getColumna("si").setMetodoChange("atencion");
        tabTabla.getColumna("no").setMetodoChange("atencion");
        tabTabla.getColumna("fechae").setValorDefecto(utilitario.getFechaActual());
        tabTabla.getColumna("login").setValorDefecto(utilitario.getVariable("NICK"));
        tabTabla.getColumna("fecha_ingreso").setValorDefecto(utilitario.getFechaHoraActual());
        tabTabla.getColumna("login").setVisible(false);
        tabTabla.getColumna("fecha_ingreso").setVisible(false);
        tabTabla.getColumna("porcentajeb").setLectura(true);
        tabTabla.getColumna("porcentajem").setLectura(true);
        tabTabla.getColumna("porcentajesi").setLectura(true);
        tabTabla.getColumna("porcentajeno").setLectura(true);
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

    public void calculo() {
        int e = 0, mb = 0, b = 0, r = 0, m = 0, total = 0;
        double prb = 0.0, prm = 0.0;
        if (tabTabla.getValor("excelente") != null && !tabTabla.getValor("excelente").toString().isEmpty()) {
            e = Integer.parseInt(tabTabla.getValor("excelente"));
        } else {
            e = 0;
        }
        if (tabTabla.getValor("muy_bueno") != null && !tabTabla.getValor("muy_bueno").toString().isEmpty()) {
            mb = Integer.parseInt(tabTabla.getValor("muy_bueno"));
        } else {
            mb = 0;
        }
        if (tabTabla.getValor("bueno") != null && !tabTabla.getValor("bueno").toString().isEmpty()) {
            b = Integer.parseInt(tabTabla.getValor("bueno"));
        } else {
            b = 0;
        }
        if (tabTabla.getValor("regular") != null && !tabTabla.getValor("regular").toString().isEmpty()) {
            r = Integer.parseInt(tabTabla.getValor("regular"));
        } else {
            r = 0;
        }
        if (tabTabla.getValor("mala") != null && !tabTabla.getValor("mala").toString().isEmpty()) {
            m = Integer.parseInt(tabTabla.getValor("mala"));
        } else {
            m = 0;
        }
        total = e + mb + b + r + 0 + m;
        if (tabTabla.getValor("numero_encuestados") != null) {
            if (total >= Integer.parseInt(tabTabla.getValor("numero_encuestados"))) {
                prb = ((e + mb + b) * 100) / Integer.parseInt(tabTabla.getValor("numero_encuestados"));
                prm = ((r + m) * 100) / Integer.parseInt(tabTabla.getValor("numero_encuestados"));
                tabTabla.setValor("porcentajeb", String.valueOf(prb));
                tabTabla.setValor("porcentajem", String.valueOf(prm));
                utilitario.addUpdate("tabTabla");
            } else {
                utilitario.agregarMensaje("Resultados, Mayor Que Número de Encuestados", null);
            }
        } else {
            utilitario.agregarMensaje("Ingrese Número de Encuestados", null);
        }

    }

    public void atencion() {
        int s = 0, n = 0, b = 0, r = 0, m = 0, total = 0;
        double prb = 0.0, prm = 0.0;
        if (tabTabla.getValor("si") != null && !tabTabla.getValor("si").toString().isEmpty()) {
            s = Integer.parseInt(tabTabla.getValor("si"));
        } else {
            s = 0;
        }
        if (tabTabla.getValor("no") != null && !tabTabla.getValor("no").toString().isEmpty()) {
            n = Integer.parseInt(tabTabla.getValor("no"));
        } else {
            n = 0;
        }
        total = s + n;
        if (tabTabla.getValor("numero_encuestados") != null) {
            if (total >= Integer.parseInt(tabTabla.getValor("numero_encuestados"))) {
                prb = ((s) * 100) / Integer.parseInt(tabTabla.getValor("numero_encuestados"));
                prm = ((n) * 100) / Integer.parseInt(tabTabla.getValor("numero_encuestados"));
                tabTabla.setValor("porcentajesi", String.valueOf(prb));
                tabTabla.setValor("porcentajeno", String.valueOf(prm));
                utilitario.addUpdate("tabTabla");
            } else {
                utilitario.agregarMensaje("Resultados, Mayor Que Número de Encuestados", null);
            }
        } else {
            utilitario.agregarMensaje("Ingrese Número de Encuestados", null);
        }
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
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

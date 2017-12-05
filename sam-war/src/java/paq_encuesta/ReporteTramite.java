/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_encuesta;

import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.List;
import org.primefaces.component.chart.pie.PieChart;
import org.primefaces.model.chart.PieChartModel;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class ReporteTramite extends Pantalla {

    private Tabla setDirecciones = new Tabla();
    private Panel panDirecciones = new Panel();
    private Panel panDatos = new Panel();
    /*
     * Grafico Estadistico
     */
    private PieChartModel modeloPastel = new PieChartModel();
    private PieChart graficoPastel = new PieChart();

    public ReporteTramite() {

        panDirecciones.setId("panDirecciones");
        panDirecciones.setTransient(true);
        panDirecciones.setHeader("DIRECCIONES");

        panDatos.setId("panDatos");
        panDatos.setTransient(true);

        Division div = new Division();
        div.setId("div");
        div.dividir2(panDirecciones, panDatos, "30%", "V");
        agregarComponente(div);
        dibujaPantalla();
        dibujarGrafico();
    }

    public void dibujaPantalla() {
        Boton botMensaje = new Boton();
        botMensaje.setValue("Mostra");
        botMensaje.setExcluirLectura(true);
        botMensaje.setIcon("ui-icon-search");
        botMensaje.setMetodo("mostrarDatos");

        setDirecciones.setId("setDirecciones");
        setDirecciones.setSql("SELECT\n"
                + "ID_CODIGO,\n"
                + "DESCRIPCION\n"
                + "FROM RESUG_AREA_RESPON\n"
                + "where IDE_CODIGO is null and ID_CODIGO <>0\n"
                + "order by ID_CODIGO");
        setDirecciones.getColumna("DESCRIPCION").setFiltro(true);
        setDirecciones.setLectura(true);
        setDirecciones.dibujar();
        PanelTabla pnd = new PanelTabla();
        pnd.getChildren().add(botMensaje);
        pnd.setPanelTabla(setDirecciones);
        panDirecciones.getChildren().add(pnd);
    }

    public void dibujarGrafico() {
        graficoPastel.setId("graficoPastel");
        graficoPastel.setTitle("DATOS DE PROCESO");
        graficoPastel.setLegendPosition("w");
        graficoPastel.setShowDataLabels(true);
        graficoPastel.setDiameter(245);
        graficoPastel.setSliceMargin(10);
        graficoPastel.setValue(modeloPastel);
        List lis = utilitario.getConexion().consultar("select count(*) as ingreso,a.zona from \n"
                + "(SELECT CLAVE, ZONA FROM zona_riesgos) as a\n"
                + "inner join \n"
                + "(SELECT cedula_propietario FROM encuesta_volcan) as b\n"
                + "on a.clave = b.cedula_propietario\n"
                + "group by a.zona");
        if (!lis.isEmpty()) {
            for (int i = 0; i < lis.size(); i++) {
                Object[] fila = (Object[]) lis.get(i);
                modeloPastel.set("Zona " + fila[1], Float.parseFloat(fila[0] + ""));
            }
        } else {
            utilitario.agregarNotificacionInfo("Reporte", "Error No Existe Información Ingresada");
        }
        Grupo gru = new Grupo();
        gru.getChildren().add(graficoPastel);
        panDatos.getChildren().add(gru);
    }

    @Override
    public void insertar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Tabla getSetDirecciones() {
        return setDirecciones;
    }

    public void setSetDirecciones(Tabla setDirecciones) {
        this.setDirecciones = setDirecciones;
    }
}

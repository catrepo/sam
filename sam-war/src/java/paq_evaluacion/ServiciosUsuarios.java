/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_evaluacion;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Division;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import sistema.aplicacion.Pantalla;
import paq_utilitario.ejb.BeanEncuestaSatisfaccion;

/**
 *
 * @author p-sistemas
 */
public class ServiciosUsuarios extends Pantalla {

    private Tabla tabTabla = new Tabla();
    private Tabla tabDetalle = new Tabla();
    private Tabla tabServicio = new Tabla();
    private List listDireccion = new ArrayList();
    private List listUsuario = new ArrayList();
    private Panel panOpcion = new Panel();
    private AutoCompletar autBusca = new AutoCompletar();
    @EJB
    private BeanEncuestaSatisfaccion encuesta = (BeanEncuestaSatisfaccion) utilitario.instanciarEJB(BeanEncuestaSatisfaccion.class);

    public ServiciosUsuarios() {

        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT ID_ASIGNACION,OBSERVACION FROM ESF_USUARIO_SERVICIO");
        autBusca.setSize(80);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("ASIGNACIÓN DE SERVICIOS A DIRECCIONES");
        agregarComponente(panOpcion);
        dibujarPantalla();
    }

    public void dibujarPantalla() {
        limpiarPanel();
        TablaGenerica tabDato = encuesta.getDirecciones();
        for (int i = 0; i < tabDato.getTotalFilas(); i++) {
            Object[] obj = new Object[2];
            obj[0] = tabDato.getValor(i, "cod_direccion");
            obj[1] = tabDato.getValor(i, "nombre_dir");
            listDireccion.add(obj);
        }

        TablaGenerica tabDatos = encuesta.getUsuarios();
        for (int i = 0; i < tabDatos.getTotalFilas(); i++) {
            Object[] obj = new Object[2];
            obj[0] = tabDatos.getValor(i, "cod_empleado");
            obj[1] = tabDatos.getValor(i, "nombres");
            listUsuario.add(obj);
        }

        tabTabla.setId("tabTabla");
        tabTabla.setTabla("esf_usuario_servicio", "id_asignacion", 1);
        if (autBusca.getValue() == null) {
            tabTabla.setCondicion("id_asignacion=-1");
        } else {
            tabTabla.setCondicion("id_asignacion=" + autBusca.getValor());
        }
        tabTabla.getColumna("id_usuario").setCombo(listUsuario);
        tabTabla.getColumna("id_direccion").setCombo(listDireccion);
        tabTabla.getColumna("id_direccion").setMetodoChange("actualizaLista");
        tabTabla.setTipoFormulario(true);
        tabTabla.agregarRelacion(tabDetalle);
        tabTabla.getGrid().setColumns(2);
        tabTabla.dibujar();
        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabTabla);

        tabServicio.setId("tabServicio");
        tabServicio.setTabla("esf_servicios", "id_servicio", 2);
        tabServicio.getColumna("codigo").setVisible(false);
        tabServicio.getColumna("id_servicio").setVisible(false);
        tabServicio.getColumna("id_direccion").setVisible(false);
        tabServicio.getColumna("seleccion").setCheck();
        tabServicio.getColumna("nombre").setLectura(true);
        tabServicio.setRows(10);
        tabServicio.dibujar();
        PanelTabla pns = new PanelTabla();
        pns.setPanelTabla(tabServicio);

        tabDetalle.setId("tabDetalle");
        tabDetalle.setTabla("esf_detalle_servicios", "id_detalle", 3);
        tabDetalle.getColumna("id_servicio").setCombo("SELECT ID_SERVICIO,NOMBRE FROM ESF_SERVICIOS order by NOMBRE");
        tabDetalle.getColumna("id_detalle").setVisible(false);
        tabDetalle.setRows(10);
        tabDetalle.dibujar();
        PanelTabla pnd = new PanelTabla();
        pnd.setPanelTabla(tabDetalle);

        Division div = new Division();
//        div.dividir2(pnt, pns, "30%", "h");
        div.dividir3(pnt, pns, pnd, "35%", "40%", "h");
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        panOpcion.getChildren().add(gru);
    }

    private void limpiarPanel() {
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }

    public void actualizaLista() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabServicio.setCondicion(getFiltrosAcceso());
            tabServicio.ejecutarSql();
            utilitario.addUpdate("tabServicio");
        }
    }

    private String getFiltrosAcceso() {
        String str_filtros = "";
        if (tabTabla.getValor("id_direccion") != null) {

            str_filtros = "id_direccion = "
                    + String.valueOf(tabTabla.getValor("id_direccion"));

        } else {
            utilitario.agregarMensajeInfo("Filtros no válidos",
                    "Debe seleccionar valor");
        }
        return str_filtros;
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tabTabla.guardar()) {
            if (tabDetalle.guardar()) {
                guardarPantalla();
            }
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

    public Tabla getTabDetalle() {
        return tabDetalle;
    }

    public void setTabDetalle(Tabla tabDetalle) {
        this.tabDetalle = tabDetalle;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }

    public Tabla getTabServicio() {
        return tabServicio;
    }

    public void setTabServicio(Tabla tabServicio) {
        this.tabServicio = tabServicio;
    }
}

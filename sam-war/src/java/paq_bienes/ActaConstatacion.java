/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bienes;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import sistema.aplicacion.Pantalla;
import paq_utilitario.ejb.BeanConstatacion;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
public class ActaConstatacion extends Pantalla {

    private Calendario fecha = new Calendario();
    private Texto numActa = new Texto();
    private Combo custodio = new Combo();
    private Combo delegado = new Combo();
    private Conexion conPostgres = new Conexion();
    private Tabla setTabla = new Tabla();
    private Panel panOpcion = new Panel();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    @EJB
    private BeanConstatacion lista = (BeanConstatacion) utilitario.instanciarEJB(BeanConstatacion.class);

    public ActaConstatacion() {
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonsNavegacion();

        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        panOpcion.setId("panOpcion");
        panOpcion.setHeader("ACTA DE CONSTATACIÓN FISICA");
        panOpcion.setStyle("width: 800px;");
        agregarComponente(panOpcion);
        dibujarPantalla();

//        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conPostgres);
        agregarComponente(sef_formato);
    }

    public void dibujarPantalla() {

        Grid griComponentes = new Grid();
        griComponentes.setColumns(4);
        griComponentes.getChildren().add(new Etiqueta("Fecha : "));
        griComponentes.getChildren().add(fecha);
        griComponentes.getChildren().add(new Etiqueta("Numero Acta : "));
        griComponentes.getChildren().add(numActa);
        griComponentes.getChildren().add(new Etiqueta("Custodio : "));
        custodio.setId("custodio");
        custodio.setConexion(conPostgres);
        custodio.setCombo("select ide_custodio,des_custodio from afi_custodio  where tipo = 'IN' order by des_custodio");
        griComponentes.getChildren().add(custodio);
        griComponentes.getChildren().add(new Etiqueta("Delegado : "));
        delegado.setId("delegado");
        delegado.setConexion(conPostgres);
        delegado.setCombo("select cod_empleado,nombres from srh_empleado where estado = 1 order by nombres ");
        griComponentes.getChildren().add(delegado);

        Grid griBoton = new Grid();
        griBoton.setColumns(3);

        Boton botSearch = new Boton();
        botSearch.setId("botSearch");
        botSearch.setValue("Buscar");
        botSearch.setIcon("ui-icon-search");
        botSearch.setMetodo("busqueda");

        Boton botPrint = new Boton();
        botPrint.setId("botPrint");
        botPrint.setValue("Imprimir");
        botPrint.setIcon("ui-icon-print");
        botPrint.setMetodo("abrirListaReportes");
        griBoton.getChildren().add(botSearch);
        griBoton.getChildren().add(botPrint);
        griComponentes.setFooter(griBoton);

        panOpcion.getChildren().add(griComponentes);
    }

    public void busqueda() {
        TablaGenerica tabTabla = lista.getActa(numActa.getValue() + "", Integer.parseInt(custodio.getValue() + ""));
        if (!tabTabla.isEmpty()) {
            fecha.setValue(tabTabla.getValor("fec_entrega"));
            delegado.setValue(tabTabla.getValor("delegado_direccion"));
            utilitario.addUpdate("fecha");
            utilitario.addUpdate("delegado");
        } else {
            utilitario.agregarMensaje("No encuentra acta", null);
        }
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "Acta Constatacion":
                mostrarReporte();
                break;
        }
    }

    public void mostrarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "Acta Constatacion":
                p_parametros.put("codigo", Integer.parseInt(custodio.getValue() + ""));
                p_parametros.put("acta", numActa.getValue());
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
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

    public Tabla getSetTabla() {
        return setTabla;
    }

    public void setSetTabla(Tabla setTabla) {
        this.setTabla = setTabla;
    }

    public Combo getCustodio() {
        return custodio;
    }

    public void setCustodio(Combo custodio) {
        this.custodio = custodio;
    }

    public Combo getDelegado() {
        return delegado;
    }

    public void setDelegado(Combo delegado) {
        this.delegado = delegado;
    }

    public Calendario getFecha() {
        return fecha;
    }

    public void setFecha(Calendario fecha) {
        this.fecha = fecha;
    }

    public Texto getNumActa() {
        return numActa;
    }

    public void setNumActa(Texto numActa) {
        this.numActa = numActa;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }

    public Map getP_parametros() {
        return p_parametros;
    }

    public void setP_parametros(Map p_parametros) {
        this.p_parametros = p_parametros;
    }
}

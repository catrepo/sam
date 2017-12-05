/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class pre_reportes_cementerio extends Pantalla {

    //declaracion de tablas
    private Tabla tabConsulta = new Tabla();
    //Creacion Calendarios
    private Calendario fecha1 = new Calendario();
    private Combo cmbTipo = new Combo();
    private Panel panOpcion = new Panel();
    //REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();

    public pre_reportes_cementerio() {
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonsNavegacion();


        /*
         * Captura de usuario que se logea
         */
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        /*
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);

        Imagen encabeza = new Imagen();
        encabeza.setStyle("font-size:19px;color:black;text-align:center;");
        encabeza.setValue("imagenes/logo.png");

        Panel tabp2 = new Panel();
        tabp2.setStyle("font-size:19px;color:black;text-align:center;");
        tabp2.setHeader("CEMENTERIO MUNICIPAL - REPORTES");
        
        Grid griCuerpo = new Grid();
        griCuerpo.setColumns(2);

        cmbTipo.setId("cmbTipo");
        cmbTipo.setCombo("select ide_lugar,detalle_lugar from CMT_LUGAR");
        
        griCuerpo.getChildren().add(new Etiqueta("LUGAR: "));
        griCuerpo.getChildren().add(cmbTipo);
        griCuerpo.getChildren().add(new Etiqueta("FECHA: (dd/mm/yyyy)"));
        fecha1.setFechaActual();
        fecha1.setTipoBoton(true);
        griCuerpo.getChildren().add(fecha1);
        panOpcion.getChildren().add(griCuerpo);
        Boton botBuscar = new Boton();
        botBuscar.setValue("Imprimir");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-document-b");
        botBuscar.setMetodo("abrirListaReportes");
        bar_botones.agregarBoton(botBuscar);

        tabp2.getChildren().add(encabeza);
        tabp2.getChildren().add(panOpcion);
        tabp2.getChildren().add(botBuscar);

        agregarComponente(tabp2);


    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "PERIODO ARRIENDO FALLECIDO":
                dibujarReporte();
                break;
            case "PERIODO ARRIENDO CATASTRO":
                dibujarReporte();
                break;
        }
    }

    public void dibujarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "PERIODO ARRIENDO FALLECIDO":
                p_parametros.put("TIPO_LUGAR", cmbTipo.getValue()+"");
                p_parametros.put("FECHA_CONSULTA ", fecha1.getFecha().toString());
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "PERIODO ARRIENDO CATASTRO":
                TablaGenerica tadDato = utilitario.consultar("select ide_lugar,detalle_lugar from CMT_LUGAR where ide_lugar ="+ cmbTipo.getValue());
                p_parametros.put("LUGAR", tadDato.getValor("detalle_lugar"));
                p_parametros.put("TIPO_LUGAR", cmbTipo.getValue()+"");
                p_parametros.put("FECHA_CONSULTA ", fecha1.getFecha().toString());
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
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

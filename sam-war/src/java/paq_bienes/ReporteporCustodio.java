/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author l-suntaxi
 */
package paq_bienes;

import framework.componentes.Boton;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_beans.Archivo;
import paq_bienes.ejb.acfbienes;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

public class ReporteporCustodio extends Pantalla {

    //Variable conexion
    private Conexion CAYMAN = new Conexion();
    //declaracion de tablas
    private Tabla tabConsulta = new Tabla();
    private SeleccionTabla setCuentas = new SeleccionTabla();
    private SeleccionTabla setCustodios = new SeleccionTabla();
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    //PARA ASIGNACION DE MES
    String selec_mes = new String();
    Archivo file = new Archivo();
    @EJB
    private acfbienes admin = (acfbienes) utilitario.instanciarEJB(acfbienes.class);

    public ReporteporCustodio() {

        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonEliminar();
        /*
         * Cadena de conexión base de datos
         */
        CAYMAN.setUnidad_persistencia(utilitario.getPropiedad("recursojdbcayman"));
        CAYMAN.NOMBRE_MARCA_BASE = "sqlserver";

        /*
         * Captura de usuario que se logea
         */
        tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        /*
         * opción para impresión
         */
        /*
         * CONFIGURACIÓN DE OBJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(CAYMAN);
        agregarComponente(sef_formato);

        Panel tabp2 = new Panel();
        tabp2.setStyle("font-size:19px;color:black;text-align:center;");
        tabp2.setHeader("REPORTES BIENES Y SEGUROS SISTEMA CAYMAN");

        Grid griCuerpo = new Grid();
        griCuerpo.setStyle("text-align:center;position:absolute;left:500px;");
        griCuerpo.setColumns(1);
        
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo.png");

        Boton botPrint = new Boton();
        botPrint.setId("botPrint");
        botPrint.setValue("IMPRIMIR");
        botPrint.setIcon("ui-icon-print");
        botPrint.setMetodo("abrirListaReportes");
        griCuerpo.getChildren().add(quinde);
        griCuerpo.getChildren().add(botPrint);
        tabp2.getChildren().add(griCuerpo);
        agregarComponente(tabp2);

        setCuentas.setId("setCuentas");
        setCuentas.getTab_seleccion().setConexion(CAYMAN);
        setCuentas.setSeleccionTabla("select  distinct gru_id, gru_nombre  from activo INNER join GRUPO G on activo.GRU_ID1= G.GRU_ID order by gru_nombre", "gru_id");
        setCuentas.getTab_seleccion().getColumna("gru_nombre").setFiltro(true);
        setCuentas.setRadio();
        setCuentas.getBot_aceptar().setMetodo("dibujarReporte");
        setCuentas.setHeader("SELECCIONAR PROGRAMA");
        agregarComponente(setCuentas);

        setCustodios.setId("setCustodios");
        setCustodios.getTab_seleccion().setConexion(CAYMAN);
        setCustodios.setSeleccionTabla("select DISTINCT custodio as nombre,custodio\n"
                + "from(select CUS_APELLIDOS+' '+ISNULL(CUS_NOMBRES,'') as custodio\n"
                + "from CUSTODIO ) as a", "nombre");
        setCustodios.getTab_seleccion().getColumna("custodio").setLongitud(70);
        setCustodios.getTab_seleccion().getColumna("custodio").setFiltro(true);
        setCustodios.setRadio();
        setCustodios.getBot_aceptar().setMetodo("dibujarReporte");
        setCustodios.setHeader("SELECCIONAR PROGRAMA");
        agregarComponente(setCustodios);

    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "ACTIVOS POR CUENTAS":
                setCuentas.dibujar();
                break;
            case "ACTIVOS POR CUSTODIO":
                setCustodios.dibujar();
                break;
                case "ACTIVOS FIJOS GENERAL":
                dibujarReporte();
                break;
        }
    }

    public void dibujarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "ACTIVOS POR CUENTAS":
                p_parametros.put("cuenta", Integer.parseInt(setCuentas.getValorSeleccionado() + ""));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;

            case "ACTIVOS POR CUSTODIO":
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                p_parametros.put("custodio", setCustodios.getValorSeleccionado() + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
                case "ACTIVOS FIJOS GENERAL":
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

    public Conexion getCAYMAN() {
        return CAYMAN;
    }

    public void setCAYMAN(Conexion CAYMAN) {
        this.CAYMAN = CAYMAN;
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

    public SeleccionTabla getSetCuentas() {
        return setCuentas;
    }

    public void setSetCuentas(SeleccionTabla setCuentas) {
        this.setCuentas = setCuentas;
    }

    public SeleccionTabla getSetCustodios() {
        return setCustodios;
    }

    public void setSetCustodios(SeleccionTabla setCustodios) {
        this.setCustodios = setCustodios;
    }
}

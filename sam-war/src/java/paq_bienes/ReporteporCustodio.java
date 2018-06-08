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
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private Tabla setGrupos = new Tabla();
    private SeleccionTabla setActivo = new SeleccionTabla();
    private SeleccionTabla setCuentas = new SeleccionTabla();
    private SeleccionTabla setCustodios = new SeleccionTabla();
    private SeleccionTabla setOpciones = new SeleccionTabla();
    private Combo cmbOpcion = new Combo();
    private Texto texBusqueda = new Texto();
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();

    private Etiqueta etiTodos = new Etiqueta("TODOS LOS REGISTROS:");
    private Check chkTodos = new Check();
    private Dialogo diaGrupos = new Dialogo();
    private Grid gridG = new Grid();
    private Grid gridg = new Grid();

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
//        setCuentas.getBot_aceptar().setMetodo("dibujarReporte");
        setCuentas.getBot_aceptar().setMetodo("buscarOpciones");
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

        List list = new ArrayList();
        Object fila1[] = {
            "0", "Cod. Barras"
        };
        Object fila2[] = {
            "1", "Custodio"
        };
        Object fila3[] = {
            "2", "Cod. Antiguo"
        };
        list.add(fila1);;
        list.add(fila2);;
        list.add(fila3);;

        cmbOpcion.setCombo(list);
        cmbOpcion.eliminarVacio();

        Grid griPri = new Grid();
        griPri.setColumns(3);

        Grid gri_busca1 = new Grid();
        gri_busca1.setColumns(5);
        gri_busca1.getChildren().add(new Etiqueta("Buscar por :"));
        gri_busca1.getChildren().add(cmbOpcion);
        griPri.getChildren().add(gri_busca1);

        gri_busca1.getChildren().add(new Etiqueta("Ingrese: "));
        texBusqueda.setSize(50);
        gri_busca1.getChildren().add(texBusqueda);

        griPri.getChildren().add(gri_busca1);
        bar_botones.agregarComponente(griPri);

        Boton bot_buscar1 = new Boton();
        bot_buscar1.setValue("Buscar");
        bot_buscar1.setIcon("ui-icon-search");
        bot_buscar1.setMetodo("busquedaInfo");
        bar_botones.agregarBoton(bot_buscar1);
        gri_busca1.getChildren().add(bot_buscar1);

        setActivo.setId("setActivo");
        setActivo.getTab_seleccion().setConexion(CAYMAN);
        setActivo.setSeleccionTabla("select CODIGOBARRAS,CODIGOBARRAS as codigo, des_activo,nombre_t,nombre from vw_ActivosFijos where CODIGOBARRAS='" + texBusqueda.getValue() + "'", "CODIGOBARRAS");
        setActivo.getTab_seleccion().getColumna("codigo").setLongitud(30);
        setActivo.getTab_seleccion().getColumna("des_activo").setLongitud(35);
        setActivo.getTab_seleccion().getColumna("nombre_t").setLongitud(35);
        setActivo.getTab_seleccion().getColumna("nombre_t").setFiltro(true);
        setActivo.getTab_seleccion().setRows(10);
        setActivo.setRadio();
        setActivo.getGri_cuerpo().setHeader(gri_busca1);//consultaFallecido
        setActivo.getBot_aceptar().setMetodo("dibujarReporte");
        setActivo.setHeader("SELECCIONAR ARTICULO");
        agregarComponente(setActivo);

        Grid griGrupo = new Grid();
        griGrupo.setColumns(2);
        griGrupo.getChildren().add(etiTodos);
        griGrupo.getChildren().add(chkTodos);
        diaGrupos.setId("diaGrupos");
        diaGrupos.setTitle("GRUPOS"); //titulo
        diaGrupos.setWidth("45%"); //siempre en porcentajes  ancho
        diaGrupos.setHeight("50%");//siempre porcentaje   alto
        diaGrupos.setResizable(false); //para que no se pueda cambiar el tamaño
        diaGrupos.getGri_cuerpo().setHeader(griGrupo);
        diaGrupos.getBot_aceptar().setMetodo("dibujarReporte");
        gridg.setColumns(4);
        agregarComponente(diaGrupos);
    }

    public void busquedaInfo() {
        if (cmbOpcion.getValue().equals("0")) {
            buscarSolicitud();
        } else if (cmbOpcion.getValue().equals("1")) {
            buscarSolicitud1();
        } else if (cmbOpcion.getValue().equals("2")) {
            buscarSolicitud2();
        } else {
            utilitario.agregarMensaje("Parametros de busqueda no seleccionados", null);
        }
    }

    public void buscarSolicitud() {
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setActivo.getTab_seleccion().setSql("select CODIGOBARRAS,CODIGOBARRAS as codigo, des_activo,nombre_t,nombre from vw_ActivosFijos where CODIGOBARRAS LIKE '%" + texBusqueda.getValue() + "'");
            setActivo.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud1() {
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setActivo.getTab_seleccion().setSql("select CODIGOBARRAS, CODIGOBARRAS as codigo,des_activo,nombre_t,nombre from vw_ActivosFijos where responsable LIKE '%" + texBusqueda.getValue() + "%'");
            setActivo.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud2() {
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setActivo.getTab_seleccion().setSql("select CODIGOBARRAS, CODIGOBARRAS as codigo,des_activo,nombre_t,nombre from vw_ActivosFijos where codigo LIKE '%" + texBusqueda.getValue() + "%'");
            setActivo.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarOpciones() {
        diaGrupos.Limpiar();
        Grid griBuscap = new Grid();
        setGrupos.setId("setGrupos");
        setGrupos.setConexion(CAYMAN);
        setGrupos.setSql("select  distinct g1.gru_id, g1.gru_nombre  \n"
                + "from activo \n"
                + "INNER join GRUPO G on activo.GRU_ID1= G.GRU_ID \n"
                + "inner join GRUPO G1 on G.GRU_ID= G1.GRU_PADRE and activo.GRU_ID2= G1.GRU_ID \n"
                + "where G.GRU_ID  = " + Integer.parseInt(setCuentas.getValorSeleccionado() + "") + "\n"
                + "order by gru_nombre");
        setGrupos.getColumna("gru_nombre").setFiltro(true);
        setGrupos.setTipoSeleccion(false);
        setGrupos.setRows(10);
        setGrupos.dibujar();
        griBuscap.getChildren().add(setGrupos);
        gridg.getChildren().add(griBuscap);
        diaGrupos.setDialogo(gridg);
        diaGrupos.dibujar();

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
            case "ACTIVOS INDIVIDUAL":
                setActivo.dibujar();
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
                if(chkTodos.getValue().equals(true)){
                     p_parametros.put("tipo", 1);
                }else{
                     p_parametros.put("tipo", 0);
                }
                p_parametros.put("grupo", Integer.parseInt(setGrupos.getValorSeleccionado() + ""));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                System.err.println("->> " + p_parametros);
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "ACTIVOS INDIVIDUAL":
                p_parametros.put("cuenta", setActivo.getValorSeleccionado() + "");
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                System.err.println("->" + p_parametros);
                System.err.println("->" + rep_reporte.getPath());
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
            case "CONSOLIDADO ACTIVOS":
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

    public SeleccionTabla getSetActivo() {
        return setActivo;
    }

    public void setSetActivo(SeleccionTabla setActivo) {
        this.setActivo = setActivo;
    }

    public SeleccionTabla getSetOpciones() {
        return setOpciones;
    }

    public void setSetOpciones(SeleccionTabla setOpciones) {
        this.setOpciones = setOpciones;
    }

    public Tabla getSetGrupos() {
        return setGrupos;
    }

    public void setSetGrupos(Tabla setGrupos) {
        this.setGrupos = setGrupos;
    }

}

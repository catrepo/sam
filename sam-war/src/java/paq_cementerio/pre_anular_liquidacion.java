package paq_cementerio;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_cementerio.ejb.cementerio;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author l-suntaxi
 */
public class pre_anular_liquidacion extends Pantalla {

    private Conexion conSqler = new Conexion();
    private Tabla tab_tabla = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Panel panOpcion = new Panel();
    private SeleccionTabla setSolicitud = new SeleccionTabla();
    private Texto texBusqueda = new Texto();
    private Combo cmbOpcion = new Combo();
    private Etiqueta etiAdvertencia = new Etiqueta("<BR><CENTER>¿Seguro de realizar la Anulación?</CENTER></BR>");
    private Dialogo diaDialogo = new Dialogo();
    private Grid grid = new Grid();
    private Grid gridD = new Grid();
    private SeleccionCalendario sec_rango = new SeleccionCalendario();
    private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
    private AutoCompletar aut_busca = new AutoCompletar();

    /*
     * Variable para imprimir reportes
     */
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    @EJB
    private cementerio cementerioM = (cementerio) utilitario.instanciarEJB(cementerio.class);

    public pre_anular_liquidacion() {
        //         Imagen de encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logoactual.png");
        agregarComponente(quinde);

        conSqler.setUnidad_persistencia(utilitario.getPropiedad("recursojdbcer"));
        conSqler.NOMBRE_MARCA_BASE = "sqlserver";

        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonsNavegacion();

        Boton bot_busca1 = new Boton();
        bot_busca1.setValue("Busq. Liquidación");
        bot_busca1.setExcluirLectura(true);
        bot_busca1.setIcon("ui-icon-search");
        bot_busca1.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca1);

        Boton bot_activar = new Boton();
        bot_activar.setValue("Anular Liquidacion");
        bot_activar.setExcluirLectura(true);
        bot_activar.setIcon("ui-icon-cancel");
        bot_activar.setMetodo("conAnulacion");
        bar_botones.agregarBoton(bot_activar);

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        sec_rango.setId("sec_rango");
        sec_rango.getBot_aceptar().setMetodo("aceptarReporte");
        sec_rango.setFechaActual();
        agregarComponente(sec_rango);
        agregarComponente(sef_reporte);

        List list = new ArrayList();
        Object fila1[] = {
            "0", "Nombres Fallecido"
        };
        Object fila2[] = {
            "1", "Cédula Fallecido"
        };
        Object fila3[] = {
            "2", "Nombres Representante"
        };
        Object fila4[] = {
            "3", "Cedula Representante"
        };
        Object fila5[] = {
            "4", "Numero Liquidacion"
        };
        list.add(fila1);;
        list.add(fila2);;
        list.add(fila3);;
        list.add(fila4);;
        list.add(fila5);;
        Grid griPri = new Grid();
        griPri.setColumns(3);

        cmbOpcion.setCombo(list);
        cmbOpcion.eliminarVacio();

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

        aut_busca.setId("aut_busca");
        aut_busca.setConexion(conSqler);
        aut_busca.setAutoCompletar("SELECT CODIGO_LIQUIDACION,NUM_LIQUIDACION,NOMBRE_RAZONSOCIAL,VALORTOTAL_LIQUIDACION,FECHA_VENCIMIENTO\n"
                + "FROM LIQUIDACIONES where  COD_RUBRO='CMTR' and EMITIDO = 'NO' ORDER BY CODIGO_LIQUIDACION");
//        aut_busca.setMetodoChange("buscarPersona");
        aut_busca.setSize(70);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("<CENTER>ANULAR LIQUIDACIONES<CENTER>");
        agregarComponente(panOpcion);
        dibujarPantalla();

        Boton bot_buscar1 = new Boton();
        bot_buscar1.setValue("Buscar");
        bot_buscar1.setIcon("ui-icon-search");
        bot_buscar1.setMetodo("busquedaInfo");
        bar_botones.agregarBoton(bot_buscar1);
        gri_busca1.getChildren().add(bot_buscar1);

        setSolicitud.setId("setSolicitud");
        setSolicitud.getTab_seleccion().setConexion(conSqler);
        setSolicitud.setSeleccionTabla("SELECT codigo_liquidacion, num_liquidacion,REPRESENTANTE_LOCALCOMERCIAL,ACTIVIDAD_LOCALCOMERCIAL,NOMBRE_RAZONSOCIAL,CEDULA_RUC,ANTECEDENTES,VALORTOTAL_LIQUIDACION\n"
                + "FROM LIQUIDACIONES  where COD_RUBRO='CMTR' AND EMITIDO = 'NO' ORDER BY CODIGO_LIQUIDACION", "num_liquidacion");
        setSolicitud.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setSolicitud.getTab_seleccion().getColumna("codigo_liquidacion").setLongitud(20);
        setSolicitud.getTab_seleccion().getColumna("NOMBRE_RAZONSOCIAL").setLongitud(50);
        setSolicitud.getTab_seleccion().getColumna("CEDULA_RUC").setLongitud(15);
        setSolicitud.getTab_seleccion().getColumna("REPRESENTANTE_LOCALCOMERCIAL").setLongitud(50);
        setSolicitud.getTab_seleccion().getColumna("ACTIVIDAD_LOCALCOMERCIAL").setLongitud(15);
        setSolicitud.getTab_seleccion().getColumna("ANTECEDENTES").setLongitud(60);
        setSolicitud.getTab_seleccion().getColumna("VALORTOTAL_LIQUIDACION").setLongitud(15);
        setSolicitud.getTab_seleccion().getColumna("codigo_liquidacion").setNombreVisual("CODIGO");   
        setSolicitud.getTab_seleccion().getColumna("NOMBRE_RAZONSOCIAL").setNombreVisual("REPRESENTANTE");
        setSolicitud.getTab_seleccion().getColumna("CEDULA_RUC").setNombreVisual("C.I. REPRESENTANTE");
        setSolicitud.getTab_seleccion().getColumna("REPRESENTANTE_LOCALCOMERCIAL").setNombreVisual("FALLECIDO");
        setSolicitud.getTab_seleccion().getColumna("ACTIVIDAD_LOCALCOMERCIAL").setNombreVisual("C.I. FALLECIDO");
        setSolicitud.getTab_seleccion().getColumna("ANTECEDENTES").setNombreVisual("ANTECEDENTES");
        setSolicitud.getTab_seleccion().getColumna("VALORTOTAL_LIQUIDACION").setNombreVisual("VALOR");
        setSolicitud.getTab_seleccion().setRows(10);
        setSolicitud.setRadio();
        setSolicitud.getGri_cuerpo().setHeader(gri_busca1);//consultaFallecido
        setSolicitud.getBot_aceptar().setMetodo("cemeneterioMovimiento");
        setSolicitud.setWidth("80%");
        setSolicitud.setHeader("BUSQUEDA DE LIQUIDACIÓN");
        agregarComponente(setSolicitud);

        /*
         * Configuración y llamado de objeto reporte
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conSqler);
        agregarComponente(sef_formato);

        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("Advertencia"); //titulo
        diaDialogo.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("20%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getBot_aceptar().setMetodo("anulaLiquidacion");
        grid.setColumns(2);
        agregarComponente(diaDialogo);
    }

    public void dibujarPantalla() {
        limpiarPanel();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(conSqler);
        tab_tabla.setTabla("LIQUIDACIONES", "codigo_liquidacion", 1);
        if (aut_busca.getValue() == null) {
            tab_tabla.setCondicion("codigo_liquidacion=-1");
        } else {
            tab_tabla.setCondicion("codigo_liquidacion=" + aut_busca.getValor());
        }
        tab_tabla.getColumna("NUM_LIQUIDACION").setNombreVisual("LIQUIDACION");
        tab_tabla.getColumna("ANTECEDENTES").setNombreVisual("ANTECEDENTES");
        tab_tabla.getColumna("OBSERVACIONES").setNombreVisual("OBSERVACIONES");
        tab_tabla.getColumna("FECHA_LIQUIDACION").setNombreVisual("FECHA LIQUIDACION");
        tab_tabla.getColumna("LOGIN_LIQUIDADOR").setNombreVisual("LOGIN_LIQUIDADOR");
        tab_tabla.getColumna("EMITIDO").setNombreVisual("EMITIDO");
        tab_tabla.getColumna("ESTADO_LIQUIDACION").setNombreVisual("ESTADO_LIQUIDACION");
        tab_tabla.getColumna("VALORTOTAL_LIQUIDACION").setNombreVisual("VALORTOTAL_LIQUIDACION");
        
        tab_tabla.getColumna("CEDULA_RUC").setNombreVisual("C.I. REPRESENTANTE");
        tab_tabla.getColumna("NOMBRE_RAZONSOCIAL").setNombreVisual("REPRESENTANTE");
        tab_tabla.getColumna("REPRESENTANTE_LOCALCOMERCIAL").setNombreVisual("FALLECIDO");
        tab_tabla.getColumna("ACTIVIDAD_LOCALCOMERCIAL").setNombreVisual("C.I. FALLECIDO");
        
        tab_tabla.getColumna("FECHA_HASTA ").setNombreVisual("HASTA");
        tab_tabla.getColumna("FECHA_DESDE").setNombreVisual("DESDE");
        tab_tabla.getColumna("NUMDOCUMENTO ").setNombreVisual("PERIODO ARRENDAMIENTO");
        tab_tabla.getColumna("PERIODO_LIQUIDACION ").setVisible(false);
        tab_tabla.getColumna("CODIGO_LIQUIDACION ").setLectura(true);
        tab_tabla.getColumna("NOMBRE_RAZONSOCIAL").setLectura(true);
        tab_tabla.getColumna("NUM_LIQUIDACION").setLectura(true);
        tab_tabla.getColumna("CEDULA_RUC").setLectura(true);
        tab_tabla.getColumna("ANTECEDENTES").setLectura(true);
        tab_tabla.getColumna("FECHA_LIQUIDACION").setLectura(true);
        tab_tabla.getColumna("VALORTOTAL_LIQUIDACION").setLectura(true);
        tab_tabla.getColumna("DIRECCION_REPRESENTANTE").setLectura(true);
        tab_tabla.getColumna("REPRESENTANTE_LOCALCOMERCIAL").setLectura(true);
        tab_tabla.getColumna("ACTIVIDAD_LOCALCOMERCIAL").setLectura(true);
        tab_tabla.getColumna("COD_CATASTRO").setLectura(true);
        tab_tabla.getColumna("DOMICILIO").setLectura(true);
        tab_tabla.getColumna("LOGIN_LIQUIDADOR").setLectura(true);
        tab_tabla.getColumna("fecha_desde").setLectura(true);
        tab_tabla.getColumna("fecha_hasta").setLectura(true);
        tab_tabla.getColumna("nombreDoc").setLectura(true);
        tab_tabla.getColumna("DIRECCION_REPRESENTANTE").setVisible(false);
        tab_tabla.getColumna("COD_RUTA").setVisible(false);
        tab_tabla.getColumna("COPIA_CIORUC").setVisible(false);
        tab_tabla.getColumna("GIRO_PRODUCTO").setVisible(false);
        tab_tabla.getColumna("DOMICILIO").setVisible(false);
        tab_tabla.getColumna("PLAZO").setVisible(false);
        tab_tabla.getColumna("BARRIO").setVisible(false);
        tab_tabla.getColumna("TRAMITE_REGYCONT").setVisible(false);
//        tab_tabla.getColumna("LOGIN_LIQUIDADOR").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tab_tabla.getColumna("RESP_LIQUIDACION").setVisible(false);
        tab_tabla.getColumna("VALORTOTAL_CONVENIO").setVisible(false);
        tab_tabla.getColumna("FECHA_EMISION").setVisible(false);
        tab_tabla.getColumna("EST_PROPIEDAD").setVisible(false);
        tab_tabla.getColumna("EST_VIA").setVisible(false);
        tab_tabla.getColumna("TELEFONO").setVisible(false);
        tab_tabla.getColumna("VAL_AREMT2").setVisible(false);
        tab_tabla.getColumna("VAL_AREACO").setVisible(false);
        tab_tabla.getColumna("CARTA_IMPREDIAL").setVisible(false);
        tab_tabla.getColumna("AUTORIZ_ROTURASFALTO").setVisible(false);
        tab_tabla.getColumna("NUMERO_TITULO").setVisible(false);
        tab_tabla.getColumna("FECHA_ESTADO_LIQUIDACION").setVisible(false);
        tab_tabla.getColumna("NUMERO_COMPROMISO").setVisible(false);
        tab_tabla.getColumna("NUMERO_SOLICITUD").setVisible(false);
        tab_tabla.getColumna("FECHA_SOLICITUD").setVisible(false);
        tab_tabla.getColumna("NOMBRE_VENDEDOR").setVisible(false);
        tab_tabla.getColumna("CEDULA_VENDEDOR").setVisible(false);
        tab_tabla.getColumna("MES_LIQUIDACION").setVisible(false);
        tab_tabla.getColumna("val_avterr").setVisible(false);
        tab_tabla.getColumna("val_avcons").setVisible(false);
        tab_tabla.getColumna("val_otrmej").setVisible(false);
        tab_tabla.getColumna("val_real").setVisible(false);
        tab_tabla.getColumna("val_exonera").setVisible(false);
        tab_tabla.getColumna("liq_fecrev").setVisible(false);
        tab_tabla.getColumna("liq_usurev").setVisible(false);
        tab_tabla.getColumna("liq_mejper").setVisible(false);
        tab_tabla.getColumna("liq_cem").setVisible(false);
        tab_tabla.getColumna("liq_alicuo").setVisible(false);
        tab_tabla.getColumna("liq_tipo").setVisible(false);
        tab_tabla.getColumna("liq_dya").setVisible(false);
        tab_tabla.getColumna("liq_canton").setVisible(false);
        tab_tabla.getColumna("base_imponible").setVisible(false);
        tab_tabla.getColumna("VALORTOTAL_CONTADO").setVisible(false);
        tab_tabla.getColumna("CODIGO_DIRECCION").setVisible(false);
        tab_tabla.getColumna("PERIODO_TITULO").setVisible(false);
        tab_tabla.getColumna("tipo").setVisible(false);
        tab_tabla.getColumna("clave_catastral").setVisible(false);
        tab_tabla.getColumna("nombreDoc").setVisible(false);
        tab_tabla.getColumna("login_update").setVisible(false);
        tab_tabla.getColumna("fecha_update").setVisible(false);
        tab_tabla.getColumna("motivo").setVisible(false);
        tab_tabla.getColumna("FECHA_VENCIMIENTO").setVisible(false);
        tab_tabla.getColumna("COD_RUBRO ").setVisible(false);
        tab_tabla.getColumna("LIQ_OBSINDREV").setVisible(false);
        tab_tabla.getColumna("LIQ_OBSREV").setVisible(false);
        tab_tabla.getColumna("LIQ_MAQREV").setVisible(false);
        tab_tabla.getColumna("COD_CATASTRO").setVisible(false);
        tab_tabla.getColumna("EMITIDO").setVisible(false);
        tab_tabla.getColumna("PLACAS").setVisible(false);
        tab_tabla.getColumna("CHASIS").setVisible(false);
        tab_tabla.getColumna("codigo_liquidacion").setVisible(false);
        //tab_tabla.setCondicion(" NUM_LIQUIDACION in (select top  1 NUM_LIQUIDACION from LIQUIDACIONES  where COD_RUBRO='CMTR' order by FECHA_LIQUIDACION desc)");
//        tab_tabla.setCampoOrden("fechA_LIQUIDACION desc");
        tab_tabla.getColumna("numDocumento").setLectura(true);
        tab_tabla.getColumna("emitido").setLectura(true);

        tab_tabla.getGrid().setColumns(4);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        Grupo gru = new Grupo();
        gru.getChildren().add(div_division);
        panOpcion.getChildren().add(gru);
    }

//    public void buscarPersona(SelectEvent evt) {
//        aut_busca.onSelect(evt);
//        System.out.println("aut_busca.getValor()" + aut_busca.getValor());
//        if (aut_busca.getValor() != null) {
//            System.out.println("aut_busc" + aut_busca.getValor());
//            tab_tabla.setFilaActual(aut_busca.getValor());
//            utilitario.addUpdate("tab_tabla");
//
//        }
//    }
    private void limpiarPanel() {
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }

    public void abrirCuadro() {
        setSolicitud.dibujar();
    }

    public void cemeneterioMovimiento() {
        if (!getFiltrosAcceso().isEmpty()) {
            tab_tabla.setCondicion(getFiltrosAcceso());
            tab_tabla.ejecutarSql();
            utilitario.addUpdate("tab_tabla");
            setSolicitud.cerrar();
        }
    }

    private String getFiltrosAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (setSolicitud.getValorSeleccionado() != null) {
            str_filtros = "num_liquidacion = '" + setSolicitud.getValorSeleccionado() + "'";
        } else {
            utilitario.agregarMensajeInfo("Filtros no válidos", "Debe seleccionar registro");
        }
        return str_filtros;
    }

    public void busquedaInfo() {
        if (cmbOpcion.getValue().equals("0")) {
            buscarSolicitud();
        } else if (cmbOpcion.getValue().equals("1")) {
            buscarSolicitud1();
        } else if (cmbOpcion.getValue().equals("2")) {
            buscarSolicitud2();
        } else if (cmbOpcion.getValue().equals("3")) {
            buscarSolicitud3();
        } else if (cmbOpcion.getValue().equals("4")) {
            buscarSolicitud4();
        } else {
            utilitario.agregarMensaje("Parametros de busqueda no seleccionados", null);
        }
    }

    public void buscarSolicitud() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("SELECT codigo_liquidacion, num_liquidacion,REPRESENTANTE_LOCALCOMERCIAL,ACTIVIDAD_LOCALCOMERCIAL,NOMBRE_RAZONSOCIAL ,CEDULA_RUC AS CEDULA\n"
                    + ",ANTECEDENTES,VALORTOTAL_LIQUIDACION,FECHA_VENCIMIENTO,EMITIDO\n"
                    + " FROM LIQUIDACIONES where COD_RUBRO='CMTR' AND EMITIDO = 'NO' AND REPRESENTANTE_LOCALCOMERCIAL  LIKE '%" + texBusqueda.getValue() + "%' order by fecha_liquidacion desc ");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud1() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("SELECT codigo_liquidacion, num_liquidacion,REPRESENTANTE_LOCALCOMERCIAL,ACTIVIDAD_LOCALCOMERCIAL,NOMBRE_RAZONSOCIAL AS REPRESENTANTE ,CEDULA_RUC AS CEDULA,\n"
                    + "ANTECEDENTES,VALORTOTAL_LIQUIDACION,FECHA_VENCIMIENTO,EMITIDO\n"
                    + " FROM LIQUIDACIONES where COD_RUBRO='CMTR' AND EMITIDO = 'NO' AND ACTIVIDAD_LOCALCOMERCIAL LIKE '%" + texBusqueda.getValue() + "%' order by fecha_liquidacion desc");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud2() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("SELECT codigo_liquidacion, num_liquidacion,REPRESENTANTE_LOCALCOMERCIAL,ACTIVIDAD_LOCALCOMERCIAL,NOMBRE_RAZONSOCIAL AS REPRESENTANTE ,CEDULA_RUC AS CEDULA,\n"
                    + "ANTECEDENTES,VALORTOTAL_LIQUIDACION,FECHA_VENCIMIENTO,EMITIDO\n"
                    + " FROM LIQUIDACIONES where COD_RUBRO='CMTR' AND EMITIDO = 'NO' AND NOMBRE_RAZONSOCIAL LIKE '%" + texBusqueda.getValue() + "%' order by fecha_liquidacion desc");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud3() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("SELECT codigo_liquidacion, num_liquidacion,REPRESENTANTE_LOCALCOMERCIAL,ACTIVIDAD_LOCALCOMERCIAL,NOMBRE_RAZONSOCIAL AS REPRESENTANTE ,CEDULA_RUC AS CEDULA,\n"
                    + "ANTECEDENTES,VALORTOTAL_LIQUIDACION,FECHA_VENCIMIENTO,EMITIDO\n"
                    + " FROM LIQUIDACIONES where COD_RUBRO='CMTR' AND EMITIDO = 'NO' AND CEDULA_RUC LIKE '%" + texBusqueda.getValue() + "%' order by fecha_liquidacion desc");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud4() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setSolicitud.getTab_seleccion().setSql("SELECT codigo_liquidacion, num_liquidacion,REPRESENTANTE_LOCALCOMERCIAL,ACTIVIDAD_LOCALCOMERCIAL,NOMBRE_RAZONSOCIAL AS REPRESENTANTE ,CEDULA_RUC AS CEDULA,\n"
                    + "ANTECEDENTES,VALORTOTAL_LIQUIDACION,FECHA_VENCIMIENTO,EMITIDO\n"
                    + " FROM LIQUIDACIONES where COD_RUBRO='CMTR' AND EMITIDO = 'NO' and  NUM_LIQUIDACION LIKE '%" + texBusqueda.getValue() + "%' order by fecha_liquidacion desc");
            setSolicitud.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    @Override
    public void insertar() {
    }

    public void abrirBusqueda() {
        setSolicitud.dibujar();
        texBusqueda.limpiar();
        setSolicitud.getTab_seleccion().limpiar();
    }

    public void conAnulacion() {
        
        if (tab_tabla.getValor("ESTADO_LIQUIDACION").equals("EMITIDO") || tab_tabla.getValor("ESTADO_LIQUIDACION").equals("ANULADO")) {
            utilitario.agregarMensaje("Liquidacion no puede ser anulada", "");
        } else {
            diaDialogo.Limpiar();
            diaDialogo.setDialogo(grid);
            grid.getChildren().add(etiAdvertencia);
            diaDialogo.setDialogo(gridD);
            diaDialogo.dibujar();

        }
    }

    public void anulaLiquidacion() {
        cementerioM.set_updateAnulaLiquicion(tab_tabla.getValor("NUM_LIQUIDACION"), tab_tabla.getValor("OBSERVACIONES"), tabConsulta.getValor("NICK_USUA"));
        cementerioM.setUpdateAnulaDetalle(tab_tabla.getValor("NUM_LIQUIDACION"), tabConsulta.getValor("NICK_USUA"), tab_tabla.getValor("OBSERVACIONES"));
        utilitario.agregarMensaje("Liquidacion fue anulada", "");
        diaDialogo.cerrar();
        utilitario.addUpdate("tab_tabla");
    }

    @Override
    public void guardar() {
//        guardarPantalla();
    }

    @Override
    public void inicio() {
        super.inicio(); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public void fin() {
        super.fin(); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public void siguiente() {
        super.siguiente(); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public void atras() {
        super.atras(); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    @Override
    public void aceptarReporte() {
        System.out.println("sali akkk");
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "MOVIMIENTO FALLECIDO":
                aceptoOrden();
                break;
        }
    }

    public void aceptoOrden() {
        System.out.println("voy por  akkk" + tab_tabla.getValor("NUM_LIQUIDACION"));
        switch (rep_reporte.getNombre()) {
            case "MOVIMIENTO FALLECIDO":
//              p_parametros.put("nom_resp", tab_tabla2.getValor("NICK_USUA") + "");
                p_parametros.put("NUM_LIQUIDACION", tab_tabla.getValor("NUM_LIQUIDACION") + "");
                System.out.println(p_parametros);
                System.out.println(rep_reporte.getPath());
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Conexion getConSqler() {
        return conSqler;
    }

    public void setConSqler(Conexion conSqler) {
        this.conSqler = conSqler;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
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

    public SeleccionTabla getSetSolicitud() {
        return setSolicitud;
    }

    public void setSetSolicitud(SeleccionTabla setSolicitud) {
        this.setSolicitud = setSolicitud;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.BuscarTabla;
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
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_cementerio.ejb.cementerio;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author l-suntaxi
 */
public class pre_catastro_ubicacion extends Pantalla {
    
    private Tabla tab_categoria = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Tabla tab_tipo = new Tabla();
    private Tabla setFallecido = new Tabla();
    private Tabla setInformacion = new Tabla();
    private SeleccionTabla setRegistros = new SeleccionTabla();
    private SeleccionTabla setUbicacion = new SeleccionTabla();
    private AutoCompletar aut_busca = new AutoCompletar();
    private Texto txtDisponible = new Texto();
    private Texto txtOcupado = new Texto();
    private Texto txtTotal = new Texto();
    private Panel panOpcion = new Panel();
    private Dialogo diaRegistro = new Dialogo();
    private Dialogo diaFallecidos = new Dialogo();
    private Dialogo diaInformacion = new Dialogo();
    private Dialogo diaLugares = new Dialogo();
    private Grid gridre = new Grid();
    private Grid gridfa = new Grid();
    private Grid gridif = new Grid();
    private Grid gridl = new Grid();
    private Grid gridRe = new Grid();
    private Grid gridFa = new Grid();
    private Grid gridIf = new Grid();
    private Grid gridL = new Grid();
    private Combo cmbTipo = new Combo();
    private Combo cmbLugar = new Combo();
    private Combo cmbEstado = new Combo();
    private Combo cmbLugares = new Combo();
    
    private Etiqueta etiLugares = new Etiqueta("TIPO LUGAR : ");
    
    @EJB
    private cementerio cementerioM = (cementerio) utilitario.instanciarEJB(cementerio.class);

    //Declaración para reportes
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();

    public pre_catastro_ubicacion() {
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logoactual.png");
        agregarComponente(quinde);
        bar_botones.quitarBotonsNavegacion();
        
        //Para capturar el usuario que se encuntra utilizando la opción
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();
        
        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT IDE_LUGAR,DETALLE_LUGAR FROM CMT_LUGAR");
        aut_busca.setMetodoChange("buscarPersona");
        aut_busca.setSize(40);
//        bar_botones.agregarComponente(new Etiqueta("Buscar Lugar:"));
//        bar_botones.agregarComponente(aut_busca);

        cmbLugar.setId("cmbLugar");
        cmbLugar.setCombo("SELECT IDE_LUGAR,DETALLE_LUGAR FROM CMT_LUGAR");
        cmbLugar.setMetodo("buscarPersona");
        cmbLugar.eliminarVacio();
        
        cmbLugares.setId("cmbLugares");
        cmbLugares.setCombo("select * from (\n"
                + "select IDE_LUGAR,DETALLE_LUGAR from CMT_LUGAR\n"
                + "union\n"
                + "select 0, 'TODOS')s");
        cmbLugares.eliminarVacio();

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setStyle("text-align:center;");
        panOpcion.setHeader("CATASTRO CEMENTERIO");
        agregarComponente(panOpcion);
        
        cmbTipo.setId("cmbTipo");
        cmbTipo.setCombo("select distinct (case when disponible_ocupado='DISPONIBLE' THEN 'DISPONIBLE' ELSE 'OCUPADO' END) AS ID, disponible_ocupado from cmt_catastro");

        //Ingreso y busqueda de solicitudes 
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("mostrarDatos");
        bar_botones.agregarBoton(bot_buscar);
        
        gri_busca.getChildren().add(cmbTipo);
        gri_busca.getChildren().add(bot_buscar);
        
        setRegistros.setId("setRegistros");
        setRegistros.setSeleccionTabla("select top 10 IDE_CATASTRO,DETALLE_LUGAR,SECTOR,NUMERO,MODULO from CMT_CATASTRO a, cmt_lugar b\n"
                + "where a.ide_lugar=b.ide_lugar AND DISPONIBLE_OCUPADO='DISPONIBLE' ORDER BY SECTOR,NUMERO,MODULO", "IDE_CATASTRO");
        setRegistros.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setRegistros.getTab_seleccion().getColumna("SECTOR").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("NUMERO").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("MODULO").setFiltro(true);
        setRegistros.getBot_aceptar().setDisabled(true);
        setRegistros.getTab_seleccion().setRows(20);
        setRegistros.setWidth("45%");
        setRegistros.setRadio();
        setRegistros.setResizable(false);
        setRegistros.getGri_cuerpo().setHeader(gri_busca);
//        setRegistros.getBot_aceptar().setMetodo("consultaCatastro");
        setRegistros.setHeader("BUSCAR CATASTRO CEMENTERIO");
        agregarComponente(setRegistros);
        
        diaRegistro.setId("diaRegistro");
        diaRegistro.setTitle("Consulta de Datos"); //titulo
        diaRegistro.setWidth("40%"); //siempre en porcentajes  ancho
//        diaRegistro.setHeight("50%");//siempre porcentaje   alto
        diaRegistro.setResizable(false); //para que no se pueda cambiar el tamaño
        diaRegistro.getBot_aceptar().setDisabled(true);
        diaRegistro.getBot_cancelar().setMetodo("regresaForma");
        gridre.setColumns(4);
        agregarComponente(diaRegistro);
        dibujarPantalla();
        
        diaFallecidos.setId("diaFallecidos");
        diaFallecidos.setTitle("DATOS DE OCUPACIÓN DE SITIO"); //titulo
//        diaFallecidos.setWidth("70%"); //siempre en porcentajes  ancho
        diaFallecidos.setResizable(false); //para que no se pueda cambiar el tamaño
        diaFallecidos.getBot_aceptar().setMetodo("verFallecido");
        diaFallecidos.getBot_cancelar().setMetodo("regresaForma");
        gridre.setColumns(4);
        agregarComponente(diaFallecidos);
        
        diaInformacion.setId("diaInformacion");
        diaInformacion.setTitle("INFORMACIÓN DE FALLECIDO"); //titulo
//        diaFallecidos.setWidth("70%"); //siempre en porcentajes  ancho
        diaInformacion.setHeight("60%");//siempre porcentaje   alto
//        diaFallecidos.setResizable(false); //para que no se pueda cambiar el tamaño
        diaInformacion.getBot_aceptar().setDisabled(true);
        diaInformacion.getBot_cancelar().setMetodo("returnForma");
        gridif.setColumns(4);
        agregarComponente(diaInformacion);
        
        diaLugares.setId("diaLugares");
        diaLugares.setTitle("Catastro CEMENTERIO");
        diaLugares.setWidth("30%");
        diaLugares.setHeight("25%");
        diaLugares.getBot_aceptar().setMetodo("abrirReporte");
        gridl.setColumns(4);
        agregarComponente(diaLugares);
        
        setUbicacion.setId("setUbicacion");
        setUbicacion.setSeleccionTabla("SELECT IDE_CATASTRO, SECTOR, MODULO, NUMERO, DISPONIBLE_OCUPADO, UBICACION, MAUSOLEO,catastro_anterior\n"
                + "FROM CMT_CATASTRO\n"
                + "where DISPONIBLE_OCUPADO is not null and IDE_CATASTRO = -1", "IDE_CATASTRO");
        setUbicacion.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setUbicacion.getTab_seleccion().getColumna("SECTOR").setFiltro(true);
        setUbicacion.getTab_seleccion().getColumna("NUMERO").setFiltro(true);
        setUbicacion.getTab_seleccion().getColumna("MODULO").setFiltro(true);
        setUbicacion.getTab_seleccion().getColumna("MAUSOLEO").setLongitud(50);
        setUbicacion.getTab_seleccion().getColumna("catastro_anterior").setLongitud(50);
        setUbicacion.getTab_seleccion().setRows(15);
        setUbicacion.setWidth("60%");
        setUbicacion.setRadio();
        setUbicacion.setResizable(false);
        setUbicacion.getBot_aceptar().setMetodo("muestraRegistro");
        setUbicacion.setHeader("BUSCAR CATASTRO CEMENTERIO");
        agregarComponente(setUbicacion);

        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
    }
    
    public void cargarCatastro() {
        setUbicacion.getTab_seleccion().setSql("SELECT IDE_CATASTRO, SECTOR, MODULO, NUMERO, DISPONIBLE_OCUPADO, UBICACION, MAUSOLEO,catastro_anterior\n"
                + "FROM CMT_CATASTRO\n"
                + "where DISPONIBLE_OCUPADO is not null and IDE_LUGAR=" + Integer.parseInt(tab_categoria.getValor("IDE_LUGAR")));
        setUbicacion.getTab_seleccion().ejecutarSql();
        setUbicacion.dibujar();
    }
    
    public void mostrarDatos() {
        if (cmbTipo.getValue() != null && cmbTipo.getValue().toString().isEmpty() == false) {
            setRegistros.getTab_seleccion().setSql(" select IDE_CATASTRO,DETALLE_LUGAR,SECTOR,NUMERO,MODULO from CMT_CATASTRO a, cmt_lugar b\n"
                    + "where a.ide_lugar=b.ide_lugar AND DETALLE_LUGAR= '" + tab_categoria.getValor("DETALLE_LUGAR") + "'  AND DISPONIBLE_OCUPADO='" + cmbTipo.getValue() + "'");
            setRegistros.getTab_seleccion().ejecutarSql();
            setRegistros.getTab_seleccion().imprimirSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }
    
    public void dibujarPantalla() {
        limpiarPanel();
        tab_categoria.setId("tab_categoria");
        tab_categoria.setTabla("CMT_LUGAR", "IDE_LUGAR", 1);
        tab_categoria.getColumna("IDE_LUGAR").setNombreVisual("Codigo");
        tab_categoria.getColumna("detalle_lugar").setMetodoChange("buscaValor");
        tab_categoria.getColumna("VALOR").setFormatoNumero(2);
        
        tab_categoria.getGrid().setColumns(2);
        tab_categoria.setLectura(true);
        tab_categoria.setTipoFormulario(true);
        tab_categoria.agregarRelacion(tab_tipo);
        tab_categoria.dibujar();
        PanelTabla tabp = new PanelTabla();
        tabp.setMensajeWarn("LUGAR DEL CEMENTERIO");
        
        txtDisponible.setId("txtDisponible");
        txtDisponible.setSize(5);
//        txtDebe.setValue(tab_categoria.getValor("IDE_LUGAR"));
        txtDisponible.setValue(cementerioM.getCuentaDisponible(tab_categoria.getValor("IDE_LUGAR")));
        txtOcupado.setId("txtOcupado");
        txtOcupado.setSize(5);
        txtOcupado.setValue(cementerioM.getCuentaOcupado(tab_categoria.getValor("IDE_LUGAR")));
//
        txtTotal.setId("txtTotal");
        txtTotal.setSize(5);
        txtTotal.setValue(Integer.parseInt(cementerioM.getCuentaOcupado(tab_categoria.getValor("IDE_LUGAR"))) + Integer.parseInt(cementerioM.getCuentaDisponible(tab_categoria.getValor("IDE_LUGAR"))));
        
        Grid gri_busca = new Grid();
        gri_busca.setColumns(10);
        
        gri_busca.getChildren().add(new Etiqueta("<B>Disponibles: </B>"));
        gri_busca.getChildren().add(txtDisponible);
        gri_busca.getChildren().add(new Etiqueta("<B>Ocupados: </B>"));
        gri_busca.getChildren().add(txtOcupado);
        gri_busca.getChildren().add(new Etiqueta("<B>Total: </B>"));
        gri_busca.getChildren().add(txtTotal);
        
        Grid griBusca = new Grid();
        griBusca.setColumns(2);
        griBusca.getChildren().add(new Etiqueta("<B>Buscar : </B>"));
        griBusca.getChildren().add(cmbLugar);
        tabp.getChildren().add(griBusca);
        tabp.setPanelTabla(tab_categoria);
        tabp.getChildren().add(gri_busca);
        
        tab_tipo.setId("tab_tipo");
        tab_tipo.setTabla("CMT_CATASTRO", "IDE_CATASTRO", 2);
        tab_tipo.getColumna("DISPONIBLE_OCUPADO").setValorDefecto("DISPONIBLE");
        tab_tipo.getColumna("DISPONIBLE_OCUPADO").setLectura(true);
        tab_tipo.getColumna("IDE_CATASTRO").setOnClick("abrirFallecido");
        tab_tipo.getColumna("ver").setCheck();
        tab_tipo.getColumna("ver").setMetodoChange("mostrarRegistro");
        
        tab_tipo.getColumna("SECTOR").setMetodoChange("validaBloque");
//        tab_tipo.getColumna("NUMERO_FILA").setMetodoChange("validaBloquel");
        tab_tipo.getColumna("DISPONIBLE_OCUPADO").setFiltro(true);
        tab_tipo.getColumna("SECTOR").setFiltro(true);
        tab_tipo.getColumna("NUMERO").setFiltro(true);
        tab_tipo.getColumna("SECTOR").setLongitud(5);
        tab_tipo.getColumna("NUMERO").setLongitud(5);
        tab_tipo.getColumna("DISPONIBLE_OCUPADO").setLongitud(25);
        tab_tipo.getColumna("TOTAL_INGRESA").setLongitud(10);
        tab_tipo.setCampoOrden("SECTOR desc");
        tab_tipo.getColumna("TOTAL_INGRESA").setValorDefecto("0");
        tab_tipo.getColumna("TOTAL_INGRESA").setVisible(false);
        tab_tipo.getColumna("habilita").setVisible(false);
        tab_tipo.getColumna("pagado").setVisible(false);
        tab_tipo.getColumna("fec_desde").setVisible(false);
        tab_tipo.getColumna("fec_hasta").setVisible(false);
        tab_tipo.getColumna("MAUSOLEO").setLectura(true);
        tab_tipo.getColumna("DISPONIBLE_OCUPADO").setNombreVisual("ESTADO");
        tab_tipo.getGrid().setColumns(2);
        tab_tipo.setTipoFormulario(true);
        tab_tipo.dibujar();
        
        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("cargarCatastro");
        
        PanelTabla tabp1 = new PanelTabla();
        tabp1.setMensajeWarn("UBICACIÓN");
        tabp1.getChildren().add(bot_buscar);
        tabp1.setPanelTabla(tab_tipo);
        
        Division div = new Division();
        div.setId("divTablas");
        div.dividir2(tabp, tabp1, "45%", "H");
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        panOpcion.getChildren().add(gru);
    }
    
    public void mostrarRegistro() {
        if (tab_tipo.getValor("ver") != null && tab_tipo.getValor("DISPONIBLE_OCUPADO").equals("OCUPADO") && tab_tipo.getValor("ver").toString().isEmpty() == false) {
            String telf = "", celu = "", mail = "", direcc = "";
            TablaGenerica tabDato = cementerioM.getFormulario(Integer.parseInt(tab_tipo.getValor("IDE_CATASTRO")));
            if (!tabDato.isEmpty()) {
                
                diaFallecidos.Limpiar();
                diaFallecidos.setDialogo(gridfa);
                gridFa.getChildren().add(setFallecido);
                setFallecido.setId("setFallecido");
                setFallecido.setSql("SELECT DISTINCT IDE_FALLECIDO,CEDULA_FALLECIDO as CEDULA,NOMBRES,FECHA_INGRE as Fec_Ingreso,FECHA_DEFUNCION,DETALLE_CMACC"
                        + ",(select descripcion from CMT_TIPO_PAGO where ide_tipo_pago = TIPO_PAGO) as TIPO\n"
                        + "from CMT_FALLECIDO_RENOVACION\n"
                        + "where IDE_CATASTRO = " + Integer.parseInt(tab_tipo.getValor("IDE_CATASTRO")) + "\n"
                        + "order by FECHA_DEFUNCION");
                setFallecido.getColumna("FECHA_DEFUNCION").setLongitud(30);
                setFallecido.getColumna("NOMBRES").setLongitud(70);
                setFallecido.getColumna("DETALLE_CMACC").setLongitud(30);
                setFallecido.getColumna("IDE_FALLECIDO").setVisible(false);
                setFallecido.setRows(10);
                setFallecido.setLectura(true);
                diaFallecidos.setDialogo(gridFa);
                setFallecido.dibujar();
                diaFallecidos.dibujar();
            } else {
                utilitario.agregarMensajeInfo("No se encuentra registro", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Lugar se encuentra disponible", "No se puede visualizar datos");
        }
    }
    
    public void verFallecido() {
        diaInformacion.Limpiar();
        diaInformacion.setDialogo(gridif);
        gridIf.getChildren().add(setInformacion);
        setInformacion.setId("setInformacion");
        setInformacion.setSql("SELECT CEDULA_FALLECIDO,NOMBRES,FECHA_INGRE,FECHA_DEFUNCION,FECHA_DESDE,FECHA_HASTA,liquidacion,CERTIFICADO_DEFUN as CERTF_DEFUNCIÓN,\n"
                + "DOCUMENTO_IDENTIDAD_CMREP as CEDULA_REPRESENTANTE,representante AS NOM_REPRESENTANTE,TELEFONOS_CMREP AS TELF_REPRESENTANTE,CELULAR_CMREP AS CEL_REPRESENTANTE\n"
                + "from CMT_FALLECIDO_RENOVACION\n"
                + "where IDE_FALLECIDO = '" + setFallecido.getValorSeleccionado() + "'");
        setInformacion.setTipoFormulario(true);
        setInformacion.setLectura(true);
        diaInformacion.setDialogo(gridIf);
        setInformacion.dibujar();
        diaInformacion.dibujar();
    }
    
    public void regresaForma() {
        diaFallecidos.cerrar();
        tab_tipo.setValor("ver", null);
        utilitario.addUpdate("tab_tipo");
    }
    
    public void returnForma() {
        diaInformacion.cerrar();
        mostrarRegistro();
    }
    
    private void limpiarPanel() {
        //borra el contenido de la división central central
        panOpcion.getChildren().clear();
    }
    
    public void limpia() {
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }
    
    @Override
    public void insertar() {

//        txtDisponible.setValue("0");
//        txtOcupado.setValue("0");
//        txtTotal.setValue("0");
//        utilitario.addUpdate("txtDisponible");
//        utilitario.addUpdate("txtOcupado");
        tab_tipo.insertar();
    }
    
    @Override
    public void actualizar() {
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
        aut_busca.actualizar();
        aut_busca.setSize(70);
        utilitario.addUpdate("aut_busca");
    }
    
    public void buscarPersona() {
//        aut_busca.onSelect(evt);
        System.out.println("aut_busca.getValor() " + cmbLugar.getValue());
        if (cmbLugar.getValue() != null) {
            System.out.println("aut_busc" + cmbLugar.getValue());
            tab_categoria.setFilaActual(cmbLugar.getValue() + "");
            utilitario.addUpdate("tab_categoria");
            stock();
        }
    }
    
    public void stock() {
        Integer disponible = 0, ocupado = 0;
        TablaGenerica tabDatos = cementerioM.getStockCementerio(cmbLugar.getValue() + "");
        if (!tabDatos.isEmpty()) {
            for (int i = 0; i < tabDatos.getTotalFilas(); i++) {
                if (tabDatos.getValor(i, "DISPONIBLE_OCUPADO").equals("DISPONIBLE")) {
                    disponible = Integer.parseInt(tabDatos.getValor(i, "dis"));
                    txtDisponible.setValue(disponible);
                }
                if (tabDatos.getValor(i, "DISPONIBLE_OCUPADO").equals("OCUPADO")) {
                    ocupado = Integer.parseInt(tabDatos.getValor(i, "dis"));
                    txtOcupado.setValue(ocupado);
                }
            }
        } else {
            disponible = 0;
            ocupado = 0;
            txtDisponible.setValue(disponible);
            txtOcupado.setValue(ocupado);
        }
        txtTotal.setValue(disponible + ocupado);
        utilitario.addUpdate("txtDisponible,txtOcupado,txtTotal");
    }
    
    public void validaBloquel() {
        if (tab_categoria.getValor("DETALLE_LUGAR ").equals("MAUSOLEO")) {
            utilitario.agregarMensajeError("Mausoleo", "No tiene NUMERO");
            tab_tipo.setValor("NUMERO", null);
            utilitario.addUpdate("tab_tipo");
        }//actualiza solo componentes
    }
    
    public void validaBloque() {
        if (tab_categoria.getValor("DETALLE_LUGAR ").equals("MAUSOLEO")) {
            tab_tipo.setValor("SECTOR", null);
            utilitario.addUpdate("tab_tipo");
            utilitario.agregarMensajeError("Mausoleo", "No tiene serie");
        }//actualiza solo componentes
        utilitario.agregarMensajeError("Mausoleo", "No tiene serie SEHUNNFF");
    }
    
    public void buscaValor() {
        System.out.println("tab_categoria.getValor(\"DETALLE_LUGAR\")ZZZZZZZZZZZZZZZZZZZZ" + tab_categoria.getValor("DETALLE_LUGAR"));
        TablaGenerica tabDato = cementerioM.getValorCatastro(tab_categoria.getValor("DETALLE_LUGAR"));
        TablaGenerica tabDato1 = cementerioM.getValorCatastro1(tab_categoria.getValor("DETALLE_LUGAR"));
        System.out.println("+tabDatozzzzzzzzzzzz" + tabDato);
        if (!tabDato.isEmpty()) {
            // Cargo la información de la base de datos maestra   
            tab_categoria.setValor("VALOR", tabDato.getValor("VALOR"));
            tab_categoria.setValor("CODIGOFISCAL_CUENTA", tabDato1.getValor("CODIGOFISCAL_CUENTA"));
            tab_categoria.setValor("DSC_CUENTA", tabDato1.getValor("DSC_CUENTA"));
            utilitario.addUpdate("tab_categoria");
        } else {
            utilitario.agregarMensajeInfo("No existe datos con esas descripciones", "");
        }
    }
    
    public void muestraRegistro() {
        if (!getFiltrosAcceso().isEmpty()) {
            tab_tipo.setCondicion(getFiltrosAcceso());
            tab_tipo.ejecutarSql();
            utilitario.addUpdate("tab_tipo");
        }
        setUbicacion.cerrar();
    }
    
    private String getFiltrosAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (setUbicacion.getValorSeleccionado() != null) {
            
            str_filtros = "IDE_LUGAR = " + Integer.parseInt(tab_categoria.getValor("IDE_LUGAR")) + "and  IDE_CATASTRO = " + Integer.parseInt(setUbicacion.getValorSeleccionado());
            
        } else {
            utilitario.agregarMensajeInfo("Filtros no válidos",
                    "Debe seleccionar valor");
        }
        return str_filtros;
    }
    
    @Override
    public void guardar() {
//        if (tab_categoria.isFocus()) {
//            System.out.println("entre a guardar aqui 1");
//            tab_categoria.guardar();
//            guardarPantalla();
//        } else 
//            if (tab_tipo.isFocus()) {
//            System.out.println("entre a guardar aqui 2");
        if (tab_categoria.getValor("DETALLE_LUGAR").equals("SITIO")) {
            String tabDato = cementerioM.consultaCatastroDuplicado(Integer.parseInt(tab_tipo.getValor("MODULO")), tab_tipo.getValor("SECTOR"), tab_tipo.getValor("NUMERO"), tab_categoria.getValor("DETALLE_LUGAR"), tab_tipo.getValor("UBICACION"));
            System.out.println("tabDato<<" + tabDato);
            if (tabDato.equals("0")) {
                tab_tipo.guardar();
                guardarPantalla();
            } else {
                utilitario.agregarMensajeError("Registro No Puede Ser Guardado", "Ya existe datos registrados con esas descripciones");
            }
        } else {
            String tabDato = cementerioM.consultaCatastroDuplicadoNicho(Integer.parseInt(tab_tipo.getValor("MODULO")), tab_tipo.getValor("SECTOR"), tab_tipo.getValor("NUMERO"), tab_categoria.getValor("DETALLE_LUGAR"));
            System.out.println("tabDato<<" + tabDato);
            if (tabDato.equals("0")) {
                tab_tipo.guardar();
                guardarPantalla();
            } else {
                utilitario.agregarMensajeError("Registro No Puede Ser Guardado", "Ya existe datos registrados con esas descripciones");
            }
        }
//        }
//        if (tab_tipo.isFocus()) {
//            String tabDato = cementerioM.consultaCatastroDuplicado(Integer.parseInt(tab_tipo.getValor("NUMERO")), tab_tipo.getValor("SECTOR"), tab_tipo.getValor("NUMERO_FILA"), tab_categoria.getValor("DETALLE_LUGAR"));
//            String tabDato1 = cementerioM.consultaCatastroDuplicadoMausoleo(Integer.parseInt(tab_tipo.getValor("NUMERO")), tab_categoria.getValor("DETALLE_LUGAR"));
//            System.out.println("tab_categoria.getValor(\"IDE_LUGAR\")" + tab_categoria.getValor("IDE_LUGAR"));
//            System.out.println("tabDato<<" + tabDato);
//            System.out.println("tabDato1<<" +tabDato1);
//
//            if (!tab_categoria.getValor("DETALLE_LUGAR").equals("MAUSOLEO") && tabDato.equals("0")) {
//                tab_categoria.guardar();
//                tab_tipo.guardar();
//                guardarPantalla();
//            } else if (tabDato1.equals("0") && tab_categoria.getValor("DETALLE_LUGAR").equals("MAUSOLEO")) {
//                tab_categoria.guardar();
//                tab_tipo.guardar();
//                guardarPantalla();
//            } else {
//                utilitario.agregarMensajeError("Registro No Puede Ser Guardado", "Ya existe datos registrados con esas descripciones");
//            }
//        }
    }
    
    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }
    
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }
    
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
                case "REPORTE CATASTRO CEMENTERIO":
                diaLugares.setDialogo(gridl);
                Grid griBusc = new Grid();
                griBusc.setColumns(2);
                griBusc.getChildren().add(etiLugares);
                griBusc.getChildren().add(cmbLugares);
                gridL.getChildren().add(griBusc);
                diaLugares.setDialogo(gridL);
                diaLugares.dibujar();
                break;
        }
    }

    public void abrirReporte() {
        switch (rep_reporte.getNombre()) {
            case "REPORTE CATASTRO CEMENTERIO":
                sef_formato.setConexion(getConexion());
                p_parametros.put("tipo", Integer.parseInt(cmbLugares.getValue()+""));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }
    public Tabla getTab_categoria() {
        return tab_categoria;
    }
    
    public void setTab_categoria(Tabla tab_categoria) {
        this.tab_categoria = tab_categoria;
    }
    
    public Tabla getTab_tipo() {
        return tab_tipo;
    }
    
    public void setTab_tipo(Tabla tab_tipo) {
        this.tab_tipo = tab_tipo;
    }
    
    public AutoCompletar getAut_busca() {
        return aut_busca;
    }
    
    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }
    
    public Dialogo getDiaRegistro() {
        return diaRegistro;
    }
    
    public void setDiaRegistro(Dialogo diaRegistro) {
        this.diaRegistro = diaRegistro;
    }
    
    public SeleccionTabla getSetRegistros() {
        return setRegistros;
    }
    
    public void setSetRegistros(SeleccionTabla setRegistros) {
        this.setRegistros = setRegistros;
    }
    
    public SeleccionTabla getSetUbicacion() {
        return setUbicacion;
    }
    
    public void setSetUbicacion(SeleccionTabla setUbicacion) {
        this.setUbicacion = setUbicacion;
    }
    
    public Tabla getSetFallecido() {
        return setFallecido;
    }
    
    public void setSetFallecido(Tabla setFallecido) {
        this.setFallecido = setFallecido;
    }
    
    public Tabla getSetInformacion() {
        return setInformacion;
    }
    
    public void setSetInformacion(Tabla setInformacion) {
        this.setInformacion = setInformacion;
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

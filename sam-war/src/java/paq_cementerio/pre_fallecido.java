/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import paq_cementerio.ejb.cementerio;
import sistema.aplicacion.Pantalla;
import paq_webservice.ClassCiudadania;

/**
 *
 * @author l-suntaxi
 */
public class pre_fallecido extends Pantalla {

    private Tabla tabCatastro = new Tabla();
    private Tabla tabFallecido = new Tabla();
    private Tabla setDetalle = new Tabla();
    private Combo cmbOpcion = new Combo();
    private Texto texBusqueda = new Texto();
    private SeleccionTabla setFallecido = new SeleccionTabla();
    private SeleccionTabla setDetall = new SeleccionTabla();
    private Panel panOpcion = new Panel();
    private Dialogo diaFallecidos = new Dialogo();
    private Grid gridre = new Grid();
    private Grid gridfa = new Grid();
    private Grid gridFa = new Grid();
    @EJB
    private cementerio admin = (cementerio) utilitario.instanciarEJB(cementerio.class);

    public pre_fallecido() {

        Boton botBuscar = new Boton();
        botBuscar.setValue("Busqueda de Fallecidos");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("buscaFallecido");
        bar_botones.agregarBoton(botBuscar);

        Grid griPri = new Grid();
        griPri.setColumns(3);

        List list = new ArrayList();
        Object fila1[] = {
            "0", "Cédula Fallecido"
        };
        Object fila2[] = {
            "1", "Nombres Fallecido"
        };
        list.add(fila2);;
        list.add(fila1);;
        cmbOpcion.setCombo(list);
        cmbOpcion.eliminarVacio();

        Grid gribusca = new Grid();
        gribusca.setColumns(5);
        gribusca.getChildren().add(new Etiqueta("Buscar por :"));
        gribusca.getChildren().add(cmbOpcion);
        griPri.getChildren().add(gribusca);
        gribusca.getChildren().add(new Etiqueta("Ingrese: "));
        texBusqueda.setSize(50);
        gribusca.getChildren().add(texBusqueda);

        Boton botBuscar1 = new Boton();
        botBuscar1.setValue("Buscar");
        botBuscar1.setIcon("ui-icon-search");
        botBuscar1.setMetodo("busquedaInfo");
        gribusca.getChildren().add(botBuscar1);
        griPri.getChildren().add(gribusca);

        setFallecido.setId("setFallecido");
        setFallecido.setSeleccionTabla("SELECT IDE_CATASTRO,CATASTRO,cedula,nombres,FECHA_DEFUNCION FROM FALLECIDO_CATASTRO_VTA where ide_catastro=-1 order by nombres", "ide_catastro");
        setFallecido.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setFallecido.getTab_seleccion().getColumna("nombres").setFiltro(true);
        setFallecido.getTab_seleccion().getColumna("CATASTRO").setLongitud(30);
        setFallecido.getTab_seleccion().getColumna("cedula").setLongitud(50);
        setFallecido.getTab_seleccion().getColumna("FECHA_DEFUNCION").setLongitud(50);
        setFallecido.getTab_seleccion().getColumna("nombres").setLongitud(70);
        setFallecido.getTab_seleccion().setRows(7);
        setFallecido.setRadio();
        setFallecido.getGri_cuerpo().setHeader(griPri);
        setFallecido.getBot_aceptar().setMetodo("buscarInformacion");
        setFallecido.setWidth("80%");
        setFallecido.setHeader("INGRESE DESCRIPCIÓN");
        agregarComponente(setFallecido);

        
        setDetall.setId("setDetall");
        setDetall.setSeleccionTabla("SELECT dbo.CMT_FALLECIDO.CEDULA_FALLECIDO,\n"
                    + "dbo.CMT_FALLECIDO.NOMBRES,\n"
                    + "dbo.CMT_DETALLE_MOVIMIENTO.FECHA_INGRE,\n"
                    + "dbo.CMT_DETALLE_MOVIMIENTO.FECHA_DESDE,\n"
                    + "dbo.CMT_DETALLE_MOVIMIENTO.FECHA_HASTA,\n"
                    + "dbo.CMT_DETALLE_MOVIMIENTO.CODIGO_LIQUIDACION,\n"
                    + "dbo.CMT_DETALLE_MOVIMIENTO.NUM_LIQUIDACION,\n"
                    + "dbo.CMT_DETALLE_MOVIMIENTO.VALOR_LIQUIDACION,\n"
//                    + "dbo.CMT_DETALLE_MOVIMIENTO.PERIODO_ARRENDAMIENTO,\n"
                    + "dbo.CMT_DETALLE_MOVIMIENTO.OBSERVACION,\n"
                    + "dbo.CMT_DETALLE_MOVIMIENTO.antecedentes,\n"
                    + "dbo.CMT_DETALLE_MOVIMIENTO.TIPO_PAGO,\n"
                    + "dbo.CMT_TIPO_PAGO.DESCRIPCION\n"
                    + "from CMT_DETALLE_MOVIMIENTO \n"
                    + "left join CMT_TIPO_PAGO on CMT_DETALLE_MOVIMIENTO.IDE_DET_MOVIMIENTO = CMT_TIPO_PAGO.ide_tipo_pago\n"
                    + "left join CMT_FALLECIDO on CMT_DETALLE_MOVIMIENTO.IDE_FALLECIDO=CMT_FALLECIDO.IDE_FALLECIDO\n"
                    + "where CMT_DETALLE_MOVIMIENTO.IDE_FALLECIDO=-1", "CEDULA_FALLECIDO");
        setDetall.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setDetall.getTab_seleccion().setRows(10);
        setDetall.setWidth("80%");
        setDetall.setHeight("50%");
        setDetall.setHeader("INGRESE DESCRIPCIÓN");
        agregarComponente(setDetall);
        
        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("<center>INFORMACION DE FALLECIDO - MOVIMIENTOS</center>");
        agregarComponente(panOpcion);

        diaFallecidos.setId("diaFallecidos");
        diaFallecidos.setTitle("Detalle de Movimientos"); //titulo
        diaFallecidos.setWidth("90%"); //siempre en porcentajes  ancho
        diaFallecidos.setHeight("50%"); //siempre en porcentajes  ancho
        diaFallecidos.setResizable(false); //para que no se pueda cambiar el tamaño
        gridre.setColumns(4);
        agregarComponente(diaFallecidos);

        dibujarPantalla();
    }

    public void dibujarPantalla() {
        tabCatastro.setId("tabCatastro");
        tabCatastro.setTabla("cmt_catastro", "ide_catastro", 1);
        tabCatastro.getColumna("ide_lugar").setCombo("SELECT IDE_LUGAR,DETALLE_LUGAR from CMT_LUGAR");
        tabCatastro.getColumna("ide_catastro").setVisible(false);
        tabCatastro.getColumna("disponible_ocupado").setVisible(false);
        tabCatastro.getColumna("total_ingresa").setVisible(false);
        tabCatastro.getColumna("habilita").setVisible(false);
        tabCatastro.getColumna("ubicacion").setVisible(false);
        tabCatastro.getColumna("pagado").setVisible(false);
        tabCatastro.getColumna("fec_desde").setVisible(false);
        tabCatastro.getColumna("fec_hasta").setVisible(false);
        tabCatastro.getColumna("ver").setVisible(false);
        tabCatastro.setCondicion("ide_catastro=-1");
        tabCatastro.agregarRelacion(tabFallecido);
        tabCatastro.getGrid().setColumns(4);
        tabCatastro.setTipoFormulario(true);
        tabCatastro.dibujar();
        PanelTabla pntCatastro = new PanelTabla();
        pntCatastro.setMensajeWarn("<CENTER><B>UBICACIÓN DEL FALLECIDO<CENTER><B>");
        pntCatastro.setPanelTabla(tabCatastro);

        tabFallecido.setId("tabFallecido");
        tabFallecido.setTabla("cmt_fallecido", "ide_fallecido", 2);
        tabFallecido.getColumna("IDE_CMGEN").setCombo("select IDE_CMGEN,DETALLE_CMGEN from CMT_GENERO");
        tabFallecido.getColumna("CEDULA_FALLECIDO").setMetodoChange("buscaPersona");
        tabFallecido.getColumna("FECHA_DOCUMENTO_CMARE").setVisible(false);
        tabFallecido.getColumna("CEDULA_REPRESENTANTE").setVisible(false);
        tabFallecido.getColumna("REPRESENTANTE_ACTUAL").setVisible(false);
        tabFallecido.getColumna("TIPO_PAGO").setVisible(false);
        tabFallecido.getColumna("TIPO_FALLECIDO").setVisible(false);
        tabFallecido.getGrid().setColumns(2);
        tabFallecido.setTipoFormulario(true);
        tabFallecido.dibujar();
        PanelTabla pntFallecido = new PanelTabla();
        Boton botBuscar1 = new Boton();
        botBuscar1.setValue("ver Detalle");
        botBuscar1.setExcluirLectura(true);
        botBuscar1.setIcon("ui-icon-search");
        botBuscar1.setMetodo("dibujaDetalle");
        pntFallecido.setMensajeWarn("<CENTER><B>INFORMACIÓN DE FALLECIDOS<CENTER><B>");
        pntFallecido.getChildren().add(botBuscar1);
        pntFallecido.setPanelTabla(tabFallecido);

        Division div = new Division();
        div.setId("div");
        div.dividir2(pntCatastro, pntFallecido, "30%", "h");

        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        panOpcion.getChildren().add(gru);
    }

    public void busquedaInfo() {
        if (cmbOpcion.getValue().equals("0")) {
            buscaCedula();
        } else if (cmbOpcion.getValue().equals("1")) {
            buscaNombre();
        } else {
            utilitario.agregarMensaje("Parametros de busqueda no seleccionados", null);
        }
    }

    public void buscaNombre() {
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setFallecido.getTab_seleccion().setSql("SELECT IDE_CATASTRO,CATASTRO,cedula,nombres,FECHA_DEFUNCION FROM FALLECIDO_CATASTRO_VTA "
                    + "where nombres like '%" + texBusqueda.getValue().toString() + "%' order by nombres");
            setFallecido.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscaCedula() {
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setFallecido.getTab_seleccion().setSql("SELECT IDE_CATASTRO,CATASTRO,cedula,nombres,FECHA_DEFUNCION FROM FALLECIDO_CATASTRO_VTA "
                    + "where cedula like '%" + texBusqueda.getValue().toString() + "%' order by nombres");
            setFallecido.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscaFallecido() {
        setFallecido.dibujar();
        setFallecido.getTab_seleccion().limpiar();
    }

    public void buscarInformacion() {
        buscarCatastro();
        buscarFallecido();
//        buscarDetalle();
        setFallecido.cerrar();
    }

    public void buscarCatastro() {
        if (!getFiltroCatastro().isEmpty()) {
            tabCatastro.setCondicion(getFiltroCatastro());
            tabCatastro.ejecutarSql();
            utilitario.addUpdate("tabCatastro");
        }
    }

    private String getFiltroCatastro() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        System.err.println(" " + setFallecido.getValorSeleccionado());
        if (setFallecido.getValorSeleccionado() != null) {
            str_filtros = "ide_catastro = " + Integer.parseInt(setFallecido.getValorSeleccionado());
        } else {
            utilitario.agregarMensajeInfo("Filtros no válidos", "Debe seleccionar valor");
        }
        return str_filtros;
    }

    public void dibujaDetalle(){
    setDetall.dibujar();
    setDetall.getTab_seleccion().limpiar();
    buscaDetalle();
    }
    
    public void buscaDetalle() {
        if (tabCatastro.getValor("ide_catastro") != null && tabFallecido.getValor("ide_fallecido") != null) {
            System.err.println("cat "+tabCatastro.getValor("ide_catastro"));
            System.err.println("fal "+tabFallecido.getValor("ide_fallecido"));            
            setDetall.getTab_seleccion().setSql("SELECT dbo.CMT_FALLECIDO.CEDULA_FALLECIDO,\n"
                    + "dbo.CMT_FALLECIDO.NOMBRES,\n"
                    + "dbo.CMT_DETALLE_MOVIMIENTO.FECHA_INGRE,\n"
                    + "dbo.CMT_DETALLE_MOVIMIENTO.FECHA_DESDE,\n"
                    + "dbo.CMT_DETALLE_MOVIMIENTO.FECHA_HASTA,\n"
                    + "dbo.CMT_DETALLE_MOVIMIENTO.CODIGO_LIQUIDACION,\n"
                    + "dbo.CMT_DETALLE_MOVIMIENTO.NUM_LIQUIDACION,\n"
                    + "dbo.CMT_DETALLE_MOVIMIENTO.VALOR_LIQUIDACION,\n"
//                    + "dbo.CMT_DETALLE_MOVIMIENTO.PERIODO_ARRENDAMIENTO,\n"
                    + "dbo.CMT_DETALLE_MOVIMIENTO.OBSERVACION,\n"
                    + "dbo.CMT_DETALLE_MOVIMIENTO.antecedentes,\n"
                    + "dbo.CMT_DETALLE_MOVIMIENTO.TIPO_PAGO,\n"
                    + "dbo.CMT_TIPO_PAGO.DESCRIPCION\n"
                    + "from CMT_DETALLE_MOVIMIENTO \n"
                    + "left join CMT_TIPO_PAGO on CMT_DETALLE_MOVIMIENTO.IDE_DET_MOVIMIENTO = CMT_TIPO_PAGO.ide_tipo_pago\n"
                    + "left join CMT_FALLECIDO on CMT_DETALLE_MOVIMIENTO.IDE_FALLECIDO=CMT_FALLECIDO.IDE_FALLECIDO\n"
                    + "where CMT_DETALLE_MOVIMIENTO.IDE_FALLECIDO="+tabFallecido.getValor("ide_fallecido")+" "
                    + "and CMT_DETALLE_MOVIMIENTO.IDE_CATASTRO="+tabCatastro.getValor("ide_catastro")+"order by fecha_desde desc");
                        setDetall.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Datos no se encuentran", null);
        }
    }

    public void buscarFallecido() {
        if (!getFiltroFallecido().isEmpty()) {
            tabFallecido.setCondicion(getFiltroFallecido());
            tabFallecido.ejecutarSql();
            utilitario.addUpdate("tabFallecido");
        }
    }

    private String getFiltroFallecido() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
//        System.err.println(" " + setFallecido.getValorSeleccionado());
        if (setFallecido.getValorSeleccionado() != null) {
            str_filtros = "ide_catastro = " + Integer.parseInt(setFallecido.getValorSeleccionado());
        } else {
            utilitario.agregarMensajeInfo("Filtros no válidos", "Debe seleccionar valor");
        }
        return str_filtros;
    }

    public void buscaPersona() {
        String cedulaF, usu, pass, cedula, date_fallecido, str_fechad2, str_fecha2;
        Date str_fechad1, str_fecha1;
        cedulaF = admin.cedulaFallecido(tabFallecido.getValor("CEDULA_FALLECIDO"));
        if (!cedulaF.equals(tabFallecido.getValor("CEDULA_FALLECIDO"))) {
            if (utilitario.validarCedula(tabFallecido.getValor("CEDULA_FALLECIDO"))) {
                usu = "cementerio";
                pass = "cmtr2016$";
                cedula = tabFallecido.getValor("CEDULA_FALLECIDO");
                paq_webservice.ConsultaCiudadano service = new paq_webservice.ConsultaCiudadano();
                ClassCiudadania persona = service.getConsultaCiudadanoSoap().busquedaPorCedula(cedula, usu, pass);

                int sexo;
                if (persona.getSexo().equals("FEMENINO")) {
                    sexo = 2;
                } else {
                    sexo = 1;
                }

                str_fecha1 = utilitario.DeStringADateformato3(persona.getFechaNacimiento());
                System.out.println("str_fecha1<<<<<" + str_fecha1);
                str_fecha2 = utilitario.DeDateAString(str_fecha1);
                System.out.println("str_fecha2<<<<<" + str_fecha2);

                tabFallecido.setValor("NOMBRES", persona.getNombre());
                tabFallecido.setValor("IDE_CMGEN", String.valueOf(sexo));
                tabFallecido.setValor("FECHA_NACIMIENTO", str_fecha2);
                date_fallecido = persona.getFechaFallecido();
                if (date_fallecido != null && date_fallecido.isEmpty() == false) {
                    str_fechad1 = utilitario.DeStringADateformato3(date_fallecido);
                    str_fechad2 = utilitario.DeDateAString(str_fechad1);

                } else {
                    String fecha_fallecido = busquedaCedulaActual(cedula, usu, pass).getFechaFallecido();
                    if (fecha_fallecido != null && fecha_fallecido.isEmpty() == false) {
                        str_fechad1 = utilitario.DeStringADateformato3(fecha_fallecido);
                        str_fechad2 = utilitario.DeDateAString(str_fechad1);

                    } else {
                        str_fechad1 = null;
                        str_fechad2 = null;
                        tabFallecido.setValor("NOMBRES", null);
                        tabFallecido.setValor("IDE_CMGEN", null);
                        tabFallecido.setValor("FECHA_NACIMIENTO", null);
                        utilitario.agregarMensajeError(null, "Fecha de Defunciòn no registrada");
                    }
                }

                tabFallecido.setValor("FECHA_DEFUNCION", str_fechad2);
                utilitario.addUpdate("tabFallecido");

            } else {
                utilitario.agregarMensaje("Número de Cédula Incorrecto", null);
            }
        } else {
            utilitario.agregarMensajeInfo("Fallecido", "ya se encuentra registrado");
        }
    }

    @Override
    public void insertar() {
        TablaGenerica tabDatos = admin.consultaCatastro(Integer.parseInt(tabCatastro.getValor("ide_catastro")));
        if (!tabDatos.isEmpty()) {
            if (tabDatos.getValor("catastro_habilita") != "0") {
                tabFallecido.insertar();
            } else {
                utilitario.agregarMensajeInfo("Habilitar catastro para poder ingresar nuevo fallecido", null);
            }
        } else {
            utilitario.agregarMensajeInfo("Datos no encontrado", null);
        }
    }

    @Override
    public void guardar() {
        if (tabFallecido.guardar()) {
            guardarPantalla();
        }
        actualizaIngreso();
    }

    public void actualizaIngreso() {
        String total_ingresa = admin.maxTotalLugar(tabCatastro.getValor("IDE_CATASTRO"));
        System.out.println("total_ingresa" + total_ingresa);
        admin.set_updateTotalLugar(tabCatastro.getValor("IDE_CATASTRO"), total_ingresa);
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public SeleccionTabla getSetFallecido() {
        return setFallecido;
    }

    public void setSetFallecido(SeleccionTabla setFallecido) {
        this.setFallecido = setFallecido;
    }

    public Tabla getTabCatastro() {
        return tabCatastro;
    }

    public void setTabCatastro(Tabla tabCatastro) {
        this.tabCatastro = tabCatastro;
    }

    public Tabla getTabFallecido() {
        return tabFallecido;
    }

    public void setTabFallecido(Tabla tabFallecido) {
        this.tabFallecido = tabFallecido;
    }

    public Tabla getSetDetalle() {
        return setDetalle;
    }

    public void setSetDetalle(Tabla setDetalle) {
        this.setDetalle = setDetalle;
    }

    public SeleccionTabla getSetDetall() {
        return setDetall;
    }

    public void setSetDetall(SeleccionTabla setDetall) {
        this.setDetall = setDetall;
    }

    private static ClassCiudadania busquedaCedulaActual(java.lang.String cedula, java.lang.String usuario, java.lang.String password) {
        paq_webservice.ConsultaCiudadano service = new paq_webservice.ConsultaCiudadano();
        paq_webservice.ConsultaCiudadanoSoap port = service.getConsultaCiudadanoSoap();
        return port.busquedaCedulaActual(cedula, usuario, password);
    }
}

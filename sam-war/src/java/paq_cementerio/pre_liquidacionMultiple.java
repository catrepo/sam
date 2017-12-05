/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
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

/**
 *
 * @author p-chumana
 */
public class pre_liquidacionMultiple extends Pantalla {

    private Tabla tabConsulta = new Tabla();
    private Tabla tabCatastro = new Tabla();
    private Tabla tabRepresentante = new Tabla();
    private Tabla tabFallecido = new Tabla();
    private Tabla tabDetalle = new Tabla();
    private Combo cmbOpcion = new Combo();
    private SeleccionTabla setFallecido = new SeleccionTabla();
    private Texto texBusqueda = new Texto();
    private AutoCompletar autBusca = new AutoCompletar();
    private Panel panOpcion = new Panel();
    private Texto txtNumLiquidacion = new Texto();
    private AreaTexto txtAntecedentes = new AreaTexto();
    private AreaTexto txtObservacion = new AreaTexto();
    private Texto txtValor = new Texto();
    private Texto txtArrendamiento = new Texto();
    private Texto txtCodigoNumero = new Texto();
    private Calendario fechaActual = new Calendario();
    private Combo cmbTipo = new Combo();
    private Etiqueta LblNumLiquidacion = new Etiqueta("<br>Nº Liquidación : </br>");
    private Etiqueta LblAntecedentes = new Etiqueta("<br>Nº Antecedentes : </br>");
    private Etiqueta LblObservacion = new Etiqueta("<br>Nº Observación : </br>");
    private Etiqueta LblValor = new Etiqueta("<br>Nº Valor : </br>");
    private Etiqueta LblArrendamiento = new Etiqueta("<br>Nº Periodo Arrendamiento : </br>");
    private Etiqueta LblFechaActual = new Etiqueta("<br>Nº Fecha Actual : </br>");
    private Etiqueta LblTipo = new Etiqueta("<br>Nº Tipo Movimiento : </br>");
    private Etiqueta LblCodigo = new Etiqueta("<br>Nº Codigo Liquidación : </br>");
    String giro_producto, dep_codigo, dep_abreviatura, maximo_bloq, id, periodo, num, parametro, cod, str_fecha1, str_fecha2, compuestoPlazo, perLiq;
    @EJB
    private cementerio admin = (cementerio) utilitario.instanciarEJB(cementerio.class);

    public pre_liquidacionMultiple() {
        Boton botBusca = new Boton();
        botBusca.setValue("BUSCAR");
        botBusca.setExcluirLectura(true);
        botBusca.setIcon("ui-icon-search");
        botBusca.setMetodo("buscaRegistro");
        bar_botones.agregarBoton(botBusca);
        bar_botones.agregarSeparador();

        Boton botSeleccion = new Boton();
        botSeleccion.setValue("Check");
        botSeleccion.setIcon("ui-icon-check");
        botSeleccion.setMetodo("checkAll");
        bar_botones.agregarBoton(botSeleccion);

        Boton botQuitar = new Boton();
        botQuitar.setValue("Un-Check");
        botQuitar.setIcon("ui-icon-cancel");
        botQuitar.setMetodo("unCheckAll");
        bar_botones.agregarBoton(botQuitar);
        bar_botones.agregarSeparador();

        Boton botProcesar = new Boton();
        botProcesar.setValue("Procesar");
        botProcesar.setIcon("ui-icon-gear");
        botProcesar.setMetodo("crearDetalle");
        bar_botones.agregarBoton(botProcesar);

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        cmbTipo.setId("cmbTipo");
        cmbTipo.setCombo("SELECT IDE_TIPO_PAGO, DESCRIPCION FROM dbo.CMT_TIPO_PAGO where IDE_TIPO_PAGO in (3)");

        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT IDE_FALLECIDO,FECHA_INGRE,liquidacion,CEDULA_FALLECIDO,NOMBRES,FECHA_DEFUNCION,CATASTRO,FECHA_DESDE,FECHA_HASTA\n"
                + "FROM CMT_FALLECIDO_RENOVACION");
        autBusca.setMetodoChange("buscarPersona");
        autBusca.setSize(70);

        Grid griBuscar = new Grid();
        griBuscar.setColumns(1);

        List list = new ArrayList();
        Object fila1[] = {
            "0", "Nombres Representante"
        };
        Object fila2[] = {
            "1", "Cédula Representante"
        };
        list.add(fila1);;
        list.add(fila2);;
        cmbOpcion.setCombo(list);
        cmbOpcion.eliminarVacio();

        Boton botRegistro = new Boton();
        botRegistro.setValue("Buscar");
        botRegistro.setIcon("ui-icon-search");
        botRegistro.setMetodo("busquedaInfo");

        Grid griMast = new Grid();
        griMast.setColumns(2);

        Grid griPri = new Grid();
        griPri.setColumns(3);

        griMast.getChildren().add(new Etiqueta("Buscar por :"));
        griMast.getChildren().add(cmbOpcion);
        texBusqueda.setSize(50);
        griPri.getChildren().add(new Etiqueta("Buscar Representante :"));
        griPri.getChildren().add(texBusqueda);
        griPri.getChildren().add(botRegistro);
        griBuscar.getChildren().add(griMast);
        griBuscar.getChildren().add(griPri);

        setFallecido.setId("setFallecido");
        setFallecido.setSeleccionTabla("select DISTINCT DOCUMENTO_IDENTIDAD_CMREP,DOCUMENTO_IDENTIDAD_CMREP as Cédula,NOMBRES_APELLIDOS_CMREP \n"
                + "from CMT_REPRESENTANTE\n"
                + "where DOCUMENTO_IDENTIDAD_CMREP <>'0000000000   ' and  DOCUMENTO_IDENTIDAD_CMREP <>'01000        '\n"
                + "and  DOCUMENTO_IDENTIDAD_CMREP <>'00000000000' and  DOCUMENTO_IDENTIDAD_CMREP <>'010          '\n"
                + "and ESTADO='ACTIVO' and DOCUMENTO_IDENTIDAD_CMREP ='-1'", "DOCUMENTO_IDENTIDAD_CMREP");
        setFallecido.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setFallecido.getTab_seleccion().getColumna("NOMBRES_APELLIDOS_CMREP").setFiltro(true);
        setFallecido.getTab_seleccion().setRows(10);
        setFallecido.setRadio();
        setFallecido.getGri_cuerpo().setHeader(griBuscar);//consultaFallecido
        setFallecido.getBot_aceptar().setMetodo("actualizaLista");
        setFallecido.setWidth("50%");
        setFallecido.setHeader("BUSCAR POR PARAMETROS");
        agregarComponente(setFallecido);

        fechaActual.setId("fechaActual");
        fechaActual.setFechaActual();
        fechaActual.setTipoBoton(true);

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        agregarComponente(panOpcion);

        dibujarPantalla();
    }

    public void dibujarPantalla() {

        tabCatastro.setId("tabCatastro");
        tabCatastro.setTabla("CMT_CATASTRO", "IDE_CATASTRO", 1);
        tabCatastro.getColumna("ide_lugar").setCombo("SELECT IDE_LUGAR,DETALLE_LUGAR FROM CMT_LUGAR");
        tabCatastro.getColumna("habilita").setCheck();
        tabCatastro.getColumna("ide_lugar").setLongitud(30);
        tabCatastro.getColumna("habilita").setMetodoChange("armaComentario");
        tabCatastro.setCondicion("IDE_CATASTRO=-1");
        tabCatastro.agregarRelacion(tabFallecido);
        tabCatastro.setRows(2);
        tabCatastro.dibujar();
        PanelTabla pntCatastro = new PanelTabla();
        pntCatastro.setMensajeWarn("DATOS DE CATASTRO");
        pntCatastro.setPanelTabla(tabCatastro);

        tabRepresentante.setId("tabRepresentante");
        tabRepresentante.setTabla("CMT_REPRESENTANTE", "IDE_CMREP", 2);
        tabRepresentante.setCondicion("IDE_CMREP=-1");
        tabRepresentante.setTipoFormulario(true);
        tabRepresentante.getGrid().setColumns(4);
        tabRepresentante.dibujar();
        PanelTabla pntRepresentante = new PanelTabla();
        pntRepresentante.setMensajeWarn("DATOS DE REPRESENTANTE");
        pntRepresentante.setPanelTabla(tabRepresentante);

        tabFallecido.setId("tabFallecido");
        tabFallecido.setTabla("CMT_FALLECIDO", "IDE_FALLECIDO", 3);
        tabFallecido.getColumna("IDE_CMGEN").setCombo("select IDE_CMGEN,DETALLE_CMGEN from CMT_GENERO");
        tabFallecido.getColumna("IDE_CATASTRO").setCombo("SELECT IDE_CATASTRO,DETALLE_LUGAR+'-'+SECTOR+'-'+convert(varchar,MODULO)+'-'+convert(varchar,NUMERO) FROM CMT_CATASTRO A INNER JOIN CMT_LUGAR B ON A.IDE_LUGAR = B.IDE_LUGAR ");
        tabFallecido.setRows(4);
        tabFallecido.dibujar();
        PanelTabla pntFallecido = new PanelTabla();
        pntFallecido.setMensajeWarn("DATOS DE FALLECIDOS");
        pntFallecido.setPanelTabla(tabFallecido);

        Grid griLiquidacion = new Grid();
        griLiquidacion.setColumns(6);
        griLiquidacion.getChildren().add(LblFechaActual);
        fechaActual.setId("fechaActual");
        griLiquidacion.getChildren().add(fechaActual);
        griLiquidacion.getChildren().add(LblTipo);
        griLiquidacion.getChildren().add(cmbTipo);
//        cmbTipo.setMetodo("armaComentario");
        griLiquidacion.getChildren().add(LblNumLiquidacion);
        txtNumLiquidacion.setId("txtNumLiquidacion");
        griLiquidacion.getChildren().add(txtNumLiquidacion);
        griLiquidacion.getChildren().add(LblArrendamiento);
        txtArrendamiento.setId("txtArrendamiento");
        griLiquidacion.getChildren().add(txtArrendamiento);
        griLiquidacion.getChildren().add(LblValor);
        txtValor.setId("txtValor");
        griLiquidacion.getChildren().add(txtValor);
        griLiquidacion.getChildren().add(LblCodigo);
        griLiquidacion.getChildren().add(txtCodigoNumero);
        griLiquidacion.getChildren().add(LblAntecedentes);
        txtAntecedentes.setId("txtAntecedentes");
        txtAntecedentes.setCols(50);
        griLiquidacion.getChildren().add(txtAntecedentes);
        griLiquidacion.getChildren().add(LblObservacion);
        txtObservacion.setId("txtObservacion");
        txtObservacion.setCols(50);
        griLiquidacion.getChildren().add(txtObservacion);
        PanelTabla pntLiquidacion = new PanelTabla();
        pntLiquidacion.setMensajeWarn("DETALLE DE LIQUIDACIÓN");
        pntLiquidacion.getChildren().add(griLiquidacion);

        tabDetalle.setId("tabDetalle");
        tabDetalle.setTabla("CMT_DETALLE_MOVIMIENTO", "IDE_DET_MOVIMIENTO", 4);
        tabDetalle.getColumna("ide_tipo_movimiento").setCombo("SELECT IDE_TIPO_PAGO, DESCRIPCION FROM dbo.CMT_TIPO_PAGO where IDE_TIPO_PAGO in (3)");
        tabDetalle.getColumna("usuario_ingre").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tabDetalle.getColumna("fecha_ingre").setValorDefecto(utilitario.getFechaActual());
        tabDetalle.getColumna("fecha_ingresa").setValorDefecto(utilitario.getFechaActual());
        tabDetalle.setCondicion("IDE_DET_MOVIMIENTO=-1");
        tabDetalle.dibujar();
        PanelTabla pntDetalle = new PanelTabla();
        pntDetalle.setMensajeWarn("DETALLE MOVIMIENTOS PARA LIQUIDACIÓN");
        pntDetalle.setPanelTabla(tabDetalle);

        Division div = new Division();
        div.setId("div");
        div.dividir2(pntCatastro, pntRepresentante, "50%", "V");

        Division data = new Division();
        data.setId("data");
//        data.dividir2(pntFallecido, pntLiquidacion, "45%", "V");
        data.dividir1(pntLiquidacion);

        Division pnt = new Division();
        pnt.setId("pnt");
        pnt.dividir3(div, data, pntDetalle, "32%", "39%", "H");

        Grupo gru = new Grupo();
        gru.getChildren().add(pnt);
        panOpcion.getChildren().add(gru);
    }

    public void buscaRegistro() {
        setFallecido.dibujar();
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

    public void busquedaInfo() {
        if (cmbOpcion.getValue().equals("0")) {
            buscarSolicitud();
        } else if (cmbOpcion.getValue().equals("1")) {
            buscarSolicitud1();
        } else {
            utilitario.agregarMensaje("Parametros de busqueda no seleccionados", null);
        }
    }

    public void buscarSolicitud() {
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setFallecido.getTab_seleccion().setSql("select DISTINCT DOCUMENTO_IDENTIDAD_CMREP,DOCUMENTO_IDENTIDAD_CMREP as Cédula,NOMBRES_APELLIDOS_CMREP \n"
                    + "from (select DISTINCT DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP \n"
                    + "from CMT_REPRESENTANTE\n"
                    + "where DOCUMENTO_IDENTIDAD_CMREP <>'0000000000   ' and  DOCUMENTO_IDENTIDAD_CMREP <>'01000        '\n"
                    + "and  DOCUMENTO_IDENTIDAD_CMREP <>'00000000000' and  DOCUMENTO_IDENTIDAD_CMREP <>'010          '\n"
                    + "and ESTADO='ACTIVO') as a\n"
                    + "where NOMBRES_APELLIDOS_CMREP like '%" + texBusqueda.getValue() + "%' order by NOMBRES_APELLIDOS_CMREP");
            setFallecido.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarSolicitud1() {
        System.out.println("SALI X AK");
        if (texBusqueda.getValue() != null && texBusqueda.getValue().toString().isEmpty() == false) {
            setFallecido.getTab_seleccion().setSql("select DISTINCT DOCUMENTO_IDENTIDAD_CMREP,DOCUMENTO_IDENTIDAD_CMREP as Cédula,NOMBRES_APELLIDOS_CMREP \n"
                    + "from (select DISTINCT DOCUMENTO_IDENTIDAD_CMREP,NOMBRES_APELLIDOS_CMREP \n"
                    + "from CMT_REPRESENTANTE\n"
                    + "where DOCUMENTO_IDENTIDAD_CMREP <>'0000000000   ' and  DOCUMENTO_IDENTIDAD_CMREP <>'01000        '\n"
                    + "and  DOCUMENTO_IDENTIDAD_CMREP <>'00000000000' and  DOCUMENTO_IDENTIDAD_CMREP <>'010          '\n"
                    + "and ESTADO='ACTIVO') as a\n"
                    + "where DOCUMENTO_IDENTIDAD_CMREP =  '" + texBusqueda.getValue() + "' order by NOMBRES_APELLIDOS_CMREP");
            setFallecido.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void actualizaLista() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabCatastro.setCondicion(getFiltrosAcceso());
            tabCatastro.ejecutarSql();
            cmbTipo.setValue("3");
            utilitario.addUpdate("tabCatastro,cmbTipo");
            representanteLista();
            setFallecido.cerrar();
        }
    }

    private String getFiltrosAcceso() {
        String str_filtros = "";
        if (setFallecido.getValorSeleccionado() != null) {
            str_filtros = "IDE_CATASTRO IN (select IDE_CATASTRO from CMT_FALLECIDO where IDE_FALLECIDO in(select DISTINCT IDE_FALLECIDO \n"
                    + "from CMT_REPRESENTANTE\n"
                    + "where DOCUMENTO_IDENTIDAD_CMREP = '" + setFallecido.getValorSeleccionado() + "'))";
        }
        return str_filtros;
    }

    public void representanteLista() {
        if (!getFiltrosRepresentante().isEmpty()) {
            tabRepresentante.setCondicion(getFiltrosRepresentante());
            tabRepresentante.ejecutarSql();
            utilitario.addUpdate("tabRepresentante");
        }
    }

    private String getFiltrosRepresentante() {
        String str_filtros = "";
        if (setFallecido.getValorSeleccionado() != null) {
            str_filtros = "IDE_CMREP = (SELECT top 1 IDE_CMREP from CMT_REPRESENTANTE where DOCUMENTO_IDENTIDAD_CMREP ='" + setFallecido.getValorSeleccionado() + "' ORDER BY IDE_CMREP)";
        }
        return str_filtros;
    }

    public void checkAll() {
        for (int i = 0; i < tabCatastro.getTotalFilas(); i++) {
            tabCatastro.setValor(i, "habilita", "true");
        }
        armaComentario();
        utilitario.addUpdate("tabCatastro");
    }

    public void unCheckAll() {
        for (int i = 0; i < tabCatastro.getTotalFilas(); i++) {
            tabCatastro.setValor(i, "habilita", "false");
        }
        utilitario.addUpdate("tabCatastro");
        limpiarDatos();
        armaComentario();
        limpiaPatalla();
    }

    public void limpiarDatos() {
        txtArrendamiento.setValue("");
        txtObservacion.setValue("");
        txtValor.setValue("");
        txtAntecedentes.setValue("");
        txtNumLiquidacion.setValue("");
        txtCodigoNumero.setValue("");
//        cmbTipo.setValue(null);
        utilitario.addUpdate("txtArrendamiento,txtObservacion,txtValor,txtAntecedentes,txtNumLiquidacion,cmbTipo");
    }

    //permite limpiar el formulario sin guardar o mantener algun dato
    public void limpiaPatalla() {
        for (int i = 0; i < tabCatastro.getTotalFilas(); i++) {
//            if (tabDetalle.getValor(i, "IDE_DET_MOVIMIENTO") == null) {
            tabDetalle.eliminar();
//            }
        }
    }

    public void armaComentario() {
        String rows = "";
        Integer numero = 0;
        for (int i = 0; i < tabCatastro.getTotalFilas(); i++) {
            if (tabCatastro.getValor(i, "habilita").equals("true")) {
                rows += tabCatastro.getValor(i, "IDE_CATASTRO") + ",";
                numero = i + 1;
            }
        }
        if (numero > 0) {
            System.err.println("" + rows);
            String cadenaNueva = rows.substring(0, rows.length() - 1);
            System.err.println("" + cadenaNueva + ", numero " + numero);
            crearConcepto(cadenaNueva, numero);
        } else {
            limpiarDatos();
        }
    }

    public void crearConcepto(String cadena, Integer numero) {
        double cantidad, valor, total;
        TablaGenerica tabInfo = admin.periodoCatastro(tabCatastro.getValor("IDE_CATASTRO"));
        if (!tabInfo.isEmpty()) {
            TablaGenerica tabDato = admin.getConceptoLiquidacion(cadena, tabInfo.getValor("PERIODO"));
            if (!tabDato.isEmpty()) {
                txtArrendamiento.setValue(tabInfo.getValor("PERIODO"));
                txtAntecedentes.setValue(tabDato.getValor("concepto"));
                cantidad = Integer.parseInt(tabInfo.getValor("PERIODO")) * numero;
                valor = Double.valueOf(tabInfo.getValor("valor"));
                total = cantidad * valor;
                txtValor.setValue(total);
                utilitario.addUpdate("txtArrendamiento,txtValor,txtAntecedentes");
            } else {
                utilitario.agregarMensaje("Antecedentes no se pueden formar", null);
            }
        } else {
            utilitario.agregarMensajeInfo("Error al buscar datos", "");
        }
    }

    public void crearDetalle() {
        for (int i = 0; i < tabCatastro.getTotalFilas(); i++) {
            if (tabCatastro.getValor(i, "habilita").equals("true")) {
                TablaGenerica tabDato = utilitario.consultar("select max(fecha_hasta) as fecha,IDE_CATASTRO from CMT_DETALLE_MOVIMIENTO where IDE_CATASTRO = " + tabCatastro.getValor(i, "ide_catastro") + " group by IDE_CATASTRO");
                TablaGenerica tabFalle = utilitario.consultar("select top 1 ide_fallecido, fecha_defuncion from CMT_FALLECIDO where IDE_CATASTRO = " + tabCatastro.getValor(i, "ide_catastro") + " order by fecha_defuncion desc");
                TablaGenerica tabInfo = admin.periodoCatastro(tabCatastro.getValor(i, "IDE_CATASTRO"));
                if (!tabInfo.isEmpty()) {
                    TablaGenerica tabTipo = utilitario.consultar("SELECT IDE_TIPO_PAGO, DESCRIPCION FROM dbo.CMT_TIPO_PAGO where IDE_TIPO_PAGO =" + cmbTipo.getValue());
                    tabDetalle.insertar();
                    tabDetalle.setValor("ide_catastro", tabCatastro.getValor(i, "ide_catastro"));
                    tabDetalle.setValor("fecha_ingre", utilitario.getFechaActual());
                    tabDetalle.setValor("usuario_ingre", utilitario.getVariable("NICK"));
                    tabDetalle.setValor("num_liquidacion", txtNumLiquidacion.getValue() + "");
                    tabDetalle.setValor("periodo_arrendamiento", tabInfo.getValor("PERIODO") + "");
                    tabDetalle.setValor("ide_fallecido", tabFalle.getValor("ide_fallecido") + "");
                    tabDetalle.setValor("IDE_TIPO_MOVIMIENTO", cmbTipo.getValue() + "");
                    tabDetalle.setValor("valor_liquidacion", Double.valueOf(tabInfo.getValor("PERIODO")) * Double.valueOf(tabInfo.getValor("valor")) + "");
                    tabDetalle.setValor("representante", tabRepresentante.getValor("NOMBRES_APELLIDOS_CMREP") + "");
                    tabDetalle.setValor("tipo_pago", tabTipo.getValor("DESCRIPCION") + "");
                    tabDetalle.setValor("FECHA_DESDE", tabDato.getValor("fecha") + "");

                    utilitario.addUpdate("tabDetalle");
                    llenaFechas(Integer.parseInt(tabCatastro.getValor(i, "ide_catastro")));
                } else {
                    utilitario.agregarMensajeInfo("Error al buscar datos", "");
                }

            }
        }
    }

    public void llenaFechas(int i) {
        String fecha = utilitario.nuevaFecha(tabDetalle.getValor("FECHA_DESDE"));
        System.err.println("" + fecha);
        int periodo1 = Integer.parseInt(tabDetalle.getValor("PERIODO_ARRENDAMIENTO"));
        TablaGenerica tabFecha = utilitario.consultar("select 0 as id, DATEADD(YEAR, " + periodo1 + ", '" + fecha + "') as fecha");
        System.err.println("NF " + tabFecha.getValor("fecha"));
        tabDetalle.setValor("FECHA_HASTA", tabFecha.getValor("fecha"));
        utilitario.addUpdateTabla(tabDetalle, "FECHA_HASTA", "");
        System.out.println("FECHA_HASTA  " + tabFecha.getValor("fecha"));
        utilitario.addUpdate("tabDetalle");
        creaConcepto();
    }

    public void creaConcepto() {
        String rows = "";
        String Texto = "";

        TablaGenerica tab_lugar = admin.getListaFallecidos(Integer.parseInt(tabCatastro.getValor("IDE_CATASTRO")));
        if (!tab_lugar.isEmpty()) {
            for (int i = 0; i < tab_lugar.getTotalFilas(); i++) {
                rows += (tab_lugar.getValor(i, "NOMBRES ") + ",");
            }
        }

        TablaGenerica tadDato = utilitario.consultar("select (select DETALLE_LUGAR from CMT_LUGAR where IDE_LUGAR = CMT_CATASTRO.IDE_LUGAR) as IDE_LUGAR ,SECTOR,modulo,NUMERO\n"
                + "from CMT_CATASTRO \n"
                + "where IDE_CATASTRO= " + tabCatastro.getValor("IDE_CATASTRO"));

        TablaGenerica tabInfo = admin.getTipoPago(Integer.parseInt(cmbTipo.getValue() + ""));

        Texto = "PAGO POR " + tabInfo.getValor("descripcion") + " DE UN " + tadDato.getValor("IDE_LUGAR") + " EN EL CEMENTERIO MUNICIPAL SECTOR: " + tadDato.getValor("SECTOR") + " MODULO: " + tadDato.getValor("modulo") + "\n"
                + "NUMERO: " + tadDato.getValor("NUMERO") + "(" + tabCatastro.getValor("catastro_anterior") + ")" + ". PARA LOS RESTOS DEL SR(A)(S): " + rows + " POR " + tabDetalle.getValor("PERIODO_ARRENDAMIENTO") + " AÑOS.\n"
                + "DESDE EL " + tabDetalle.getValor("FECHA_DESDE") + " HASTA EL " + tabDetalle.getValor("FECHA_HASTA") + "";
// 
        tabDetalle.setValor("antecedentes", Texto);
        System.err.println("" + Texto);
        utilitario.addUpdate("tabDetalle");
    }

    @Override
    public void insertar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardar() {
        if (tabDetalle.getTotalFilas() > 0) {
            if (tabDetalle.guardar()) {
                guardarPantalla();
                insertaLiquidacion();
                ejecutaSP();
                ejecutarProceso();
                actualizaDetalle();
            }
        } else {
            utilitario.agregarMensajeInfo("Para guardar la información debe procesar primero", null);
        }
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void insertaLiquidacion() {
        giro_producto = "";
        dep_codigo = "";
        dep_abreviatura = "";
        maximo_bloq = "";
        id = "";
        periodo = "";
        cod = "";
        str_fecha1 = "";
        str_fecha2 = "";
        compuestoPlazo = "";
        perLiq = "";
        TablaGenerica tabUsuInfo = admin.getUsuarioInfor(tabConsulta.getValor("NICK_USUA"));
        if (!tabUsuInfo.isEmpty()) {
            dep_codigo = tabUsuInfo.getValor("dep_codigo");
            dep_abreviatura = tabUsuInfo.getValor("dep_abreviatura");
            TablaGenerica tabCodigo = admin.getCodLiquidacion();
            if (!tabCodigo.isEmpty()) {
                id = tabCodigo.getValor("id");
                periodo = tabCodigo.getValor("periodo");
                TablaGenerica tabLiquidacion = admin.getNumLiquidacion(dep_codigo);
                if (!tabLiquidacion.isEmpty()) {
                    num = tabLiquidacion.getValor("num");
                    parametro = tabLiquidacion.getValor("parametro");
                    perLiq = tabLiquidacion.getValor("periodo");
                    TablaGenerica tabRubro = admin.getRubroCodigo(Integer.parseInt(tabCatastro.getValor("IDE_CATASTRO")));
                    if (!tabRubro.isEmpty()) {
                        cod = tabRubro.getValor("cod_rubro");
                        txtNumLiquidacion.setValue(num + "-" + dep_abreviatura + "-" + parametro);
                        txtCodigoNumero.setValue(id);
                        utilitario.addUpdate("txtNumLiquidacion,txtCodigoNumero");
                        giro_producto = txtAntecedentes.getValue() + "";

                        System.err.println(giro_producto + "" + dep_codigo + "" + dep_abreviatura + id + "" + periodo + "" + num + "" + parametro + "" + cod + "" + str_fecha1 + "" + str_fecha2 + "" + compuestoPlazo + "" + perLiq);
                    } else {
                        utilitario.agregarMensajeError("Codigo rubro falta", null);
                    }
                } else {
                    utilitario.agregarMensajeError("Codigo de liquidacion falta", null);
                }
            } else {
                utilitario.agregarMensajeError("Num de Liquidacion falta", null);
            }
        } else {
            utilitario.agregarMensajeError("Codigo y abreviatura de dependencia faltante", null);
        }
    }

    public void ejecutaSP() {
        admin.setRegistroLiquidacion(dep_codigo, "'" + utilitario.getAnio(utilitario.getFechaActual()) + "'", "'" + dep_abreviatura + "'", null, "'" + tabRepresentante.getValor("DOCUMENTO_IDENTIDAD_CMREP") + "'", "'" + tabRepresentante.getValor("NOMBRES_APELLIDOS_CMREP") + "'", null, null, "'" + tabRepresentante.getValor("DIRECCION_CMREP") + "'", null, "'" + txtAntecedentes.getValue() + "" + "'", null, null, "'" + tabConsulta.getValor("NICK_USUA") + "'", Double.parseDouble(txtValor.getValue() + ""), "'" + cod + "'", null, "'" + giro_producto + "'", null, null, null, null, null, null, null, "'" + txtArrendamiento.getValue() + "'", "'" + txtCodigoNumero.getValue() + "'", "'" + txtNumLiquidacion.getValue() + "'");
        System.out.println("actualizando numericos");
        admin.setUpdateNumLiquidacion(dep_codigo, perLiq);
        admin.setUpdateCodLiquidacion(periodo);
    }

    public void ejecutarProceso() {
        System.err.println(txtCodigoNumero.getValue() + " ->>codigo liquidacion");
        System.err.println(txtNumLiquidacion.getValue() + " ->>numero liquidacion");

        for (int i = 0; i < tabDetalle.getTotalFilas(); i++) {
            TablaGenerica tabLugares = utilitario.consultar("select sector+'-'+modulo+'-'+numero as lugar,IDE_CATASTRO from CMT_CATASTRO where IDE_CATASTRO=" + tabDetalle.getValor(i, "IDE_CATASTRO"));
            TablaGenerica tab_lugar = admin.getLugarCatastro(Integer.parseInt(tabDetalle.getValor(i, "IDE_CATASTRO")));
            if (!tab_lugar.isEmpty()) {
                String maximo = admin.getDetalleMaxLiqui(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())), String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));
                admin.setGuardaDetalle1(txtCodigoNumero.getValue().toString(), String.valueOf(utilitario.getAnio(utilitario.getFechaActual())), maximo, String.valueOf(utilitario.getAnio(utilitario.getFechaActual())), tab_lugar.getValor("codigofiscal_cuenta"), tab_lugar.getValor("dsc_cuenta") + "(" + tabLugares.getValor("lugar") + ")", tabDetalle.getValor("VALOR_LIQUIDACION").toString() + "", "1", tabDetalle.getValor(i, "IDE_CATASTRO"), tabDetalle.getValor(i, "IDE_DET_MOVIMIENTO") + "");
            } else {
                utilitario.agregarMensajeError("Faltan datos de catastro o lugar ", tabDetalle.getValor(i, "IDE_CATASTRO"));
            }
            admin.set_updateDisponible(tabDetalle.getValor(i, "IDE_CATASTRO"));
            String total_ingresa = admin.maxTotalLugar(tabDetalle.getValor(i, "IDE_CATASTRO"));
            System.out.println("total_ingresa" + total_ingresa);
            admin.set_updateTotalLugar(tabDetalle.getValor(i, "IDE_CATASTRO"), total_ingresa);
            System.out.println("salir de ER");
        }
    }

    public void actualizaDetalle() {
        for (int i = 0; i < tabDetalle.getTotalFilas(); i++) {
            admin.set_updateNumLiq(tabDetalle.getValor(i, "IDE_DET_MOVIMIENTO"), "'" + (num + "-" + dep_abreviatura + "-" + parametro) + "'", "'" + id + "'", null, null);
        }
    }

    public Tabla getTabCatastro() {
        return tabCatastro;
    }

    public void setTabCatastro(Tabla tabCatastro) {
        this.tabCatastro = tabCatastro;
    }

    public Tabla getTabRepresentante() {
        return tabRepresentante;
    }

    public void setTabRepresentante(Tabla tabRepresentante) {
        this.tabRepresentante = tabRepresentante;
    }

    public SeleccionTabla getSetFallecido() {
        return setFallecido;
    }

    public void setSetFallecido(SeleccionTabla setFallecido) {
        this.setFallecido = setFallecido;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }

    public Tabla getTabFallecido() {
        return tabFallecido;
    }

    public void setTabFallecido(Tabla tabFallecido) {
        this.tabFallecido = tabFallecido;
    }

    public Tabla getTabDetalle() {
        return tabDetalle;
    }

    public void setTabDetalle(Tabla tabDetalle) {
        this.tabDetalle = tabDetalle;
    }
}

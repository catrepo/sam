/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_remuneraciones;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import paq_remuneraciones.ejb.BeanRemuneracion;
import sistema.aplicacion.Pantalla;
import paq_utilitario.ejb.ClaseGenerica;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
public class ReporteAnticipos extends Pantalla {

    private Conexion Nomina = new Conexion();
    private Tabla tabTabla1 = new Tabla();
    private Tabla tabTabla2 = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Tabla setDatos = new Tabla();
    private Combo cmbAnio = new Combo();
    private Combo cmbAnio1 = new Combo();
    private Combo cmbPeriodo = new Combo();
    private Combo cmbDistributivo = new Combo();
    private Combo cmbTipo = new Combo();
    private Calendario fechaInicio = new Calendario();
    private Calendario fechaFin = new Calendario();
    private Etiqueta txeAnio = new Etiqueta("AÑO :");
    private Etiqueta txeAnio1 = new Etiqueta("AÑO :");
    private Etiqueta txeTipo = new Etiqueta("TIPO :");
    private Etiqueta txePeriodo = new Etiqueta(" PERIODO : ");
    private Etiqueta txeDistributivo = new Etiqueta("TIPO : ");
    private Etiqueta txeIni = new Etiqueta("FECHA INICIO :");
    private Etiqueta txeFin = new Etiqueta("FECHA FIN :");
    //Declaración para reportes
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
       
    private Dialogo diaDialogor = new Dialogo();
    private Dialogo diaDialogo = new Dialogo();
    private Dialogo diaDialogot = new Dialogo();
    private Dialogo diaDiaAnticipo = new Dialogo();
    private Dialogo diaDiaCedula = new Dialogo();
    private Grid gridD = new Grid();
    private Grid gridR = new Grid();
    private Grid gridT = new Grid();
    private Grid gridA = new Grid();
    private Grid gridC = new Grid();
    private Grid grid = new Grid();
    private Grid grir = new Grid();
    private Grid grit = new Grid();
    private Grid gria = new Grid();
    private Grid gric = new Grid();
    @EJB
    private BeanRemuneracion adminRemuneracion = (BeanRemuneracion) utilitario.instanciarEJB(BeanRemuneracion.class);
    private ClaseGenerica generico = (ClaseGenerica) utilitario.instanciarEJB(ClaseGenerica.class);

    public ReporteAnticipos() {
               
        Nomina.setUnidad_persistencia(utilitario.getPropiedad("oraclejdb"));
        Nomina.NOMBRE_MARCA_BASE = "oracle";
        
        
        Boton bot_busca = new Boton();
        bot_busca.setValue("BUSCAR");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("actualizaLista");
        bar_botones.agregarBoton(bot_busca);

        Boton botGarente = new Boton();
        botGarente.setValue("GARANTES");
        botGarente.setExcluirLectura(true);
        botGarente.setIcon("ui-icon-search");
        botGarente.setMetodo("garanteLista");
        bar_botones.agregarBoton(botGarente);

        //Para capturar el usuario que se encuntra utilizando la opción
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        tabTabla1.setId("tabTabla1");
        tabTabla1.setSql("SELECT DISTINCT ced_empleado,nom_empleado\n"
                + "FROM nom_solicitud\n"
                + "order by nom_empleado");
        tabTabla1.getColumna("ced_empleado").setFiltro(true);
        tabTabla1.getColumna("nom_empleado").setFiltro(true);
        tabTabla1.setRows(36);
        tabTabla1.setLectura(true);
        tabTabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tabTabla1);

        tabTabla2.setId("tabTabla2");
        tabTabla2.setTabla("nom_anticipo", "id_anticipo", 1);
        tabTabla2.getColumna("estado_anticipo").setCombo("SELECT id_tipo,desc_tipo FROM dbo.nom_tipo");
        tabTabla2.getColumna("estado_anticipo").setAutoCompletar();
        tabTabla2.getColumna("valor_saldo").setVisible(false);
        tabTabla2.getColumna("id_anticipo").setVisible(false);
        tabTabla2.getColumna("porcentaje").setVisible(false);
        tabTabla2.getColumna("id_tipo").setVisible(false);
        tabTabla2.setLectura(true);
        tabTabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tabTabla2);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, pat_panel2, "28%", "V");
        agregarComponente(div_division);

        cmbAnio.setId("cmbAnio");
        cmbAnio.setCombo("SELECT ano_curso,ano_curso as anio FROM conc_ano order by ano_curso desc");
        
        cmbAnio1.setId("cmbAnio1");
        cmbAnio1.setCombo("SELECT ano_curso,ano_curso as anio FROM conc_ano order by ano_curso desc");

        cmbPeriodo.setId("cmbPeriodo");
        cmbPeriodo.setCombo("SELECT periodo,periodo as mes FROM dbo.conc_periodo");

        List listas = new ArrayList();
        Object filas1[] = {
            "NL", "EMPLEADO"
        };

        Object filas2[] = {
            "CT", "TRABAJADOR"
        };
        listas.add(filas1);;
        listas.add(filas2);;
        cmbDistributivo.setId("cmbDistributivo");
        cmbDistributivo.setCombo(listas);

        diaDialogor.setId("diaDialogor");
        diaDialogor.setWidth("20%"); //siempre en porcentajes  ancho
        diaDialogor.setHeight("25%");//siempre porcentaje   alto
        diaDialogor.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogor.getBot_aceptar().setMetodo("aceptoAnticipo");
        gridR.setColumns(4);
        agregarComponente(diaDialogor);

        /*
         * muestra un listado de garantes
         */
        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("GARANTES"); //titulo
        diaDialogo.setWidth("75%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("70%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        gridD.setColumns(4);
        agregarComponente(diaDialogo);

        cmbTipo.setId("cmbTipo");
        cmbTipo.setCombo("SELECT id_tipo,desc_tipo FROM dbo.nom_tipo WHERE obs_tipo = 'EST'order by desc_tipo");
        
        diaDialogot.setId("diaDialogot");
        diaDialogot.setTitle("TIPO DE ANTICIPO"); //titulo
        diaDialogot.setWidth("25%"); //siempre en porcentajes  ancho
        diaDialogot.setHeight("20%");//siempre porcentaje   alto
        diaDialogot.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogot.getBot_aceptar().setMetodo("aceptoAnticipo");
        gridT.setColumns(4);
        agregarComponente(diaDialogot);
        
        diaDiaAnticipo.setId("diaDiaAnticipo");
        diaDiaAnticipo.setTitle("AÑO DE ANTICIPO"); //titulo
        diaDiaAnticipo.setWidth("25%"); //siempre en porcentajes  ancho
        diaDiaAnticipo.setHeight("20%");//siempre porcentaje   alto
        diaDiaAnticipo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDiaAnticipo.getBot_aceptar().setMetodo("aceptoAnticipo");
        gridA.setColumns(4);
        agregarComponente(diaDiaAnticipo);
        
        diaDiaCedula.setId("diaDiaCedula");
        diaDiaCedula.setWidth("25%"); //siempre en porcentajes  ancho
        diaDiaCedula.setHeight("20%");//siempre porcentaje   alto
        diaDiaCedula.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDiaCedula.getBot_aceptar().setMetodo("aceptoAnticipo");
        gridC.setColumns(4);
        agregarComponente(diaDiaCedula);
        
        filtraLista();

        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);
    }

    public void garanteLista() {


        diaDialogo.setDialogo(grid);
        setDatos.setId("setDatos");
        setDatos.setSql("select * from(select codigo,nombre,cedula,suelddo, distributivo,tipo,direccion\n"
                + "from listado_garantes) a\n"
                + "left join (select ide_garante,ced_garante \n"
                + "from nom_solicitud\n"
                + "inner join nom_garante on nom_solicitud.id_solicitud = nom_garante.id_solicitud\n"
                + "where estado_solicitud in(3,4,7) or estado_solicitud is null) b\n"
                + "on b.ced_garante =a.cedula\n"
                + "where ced_garante is null\n"
                + "order by distributivo,nombre");
        setDatos.getColumna("nombre").setFiltro(true);
        setDatos.getColumna("cedula").setFiltro(true);
        setDatos.getColumna("distributivo").setFiltro(true);
        setDatos.getColumna("codigo").setLongitud(5);
        setDatos.getColumna("cedula").setLongitud(15);
        setDatos.getColumna("distributivo").setLongitud(5);
        setDatos.getColumna("tipo").setLongitud(5);
        setDatos.getColumna("ide_garante").setVisible(false);
        setDatos.getColumna("ced_garante").setVisible(false);
        setDatos.getColumna("suelddo").setVisible(false);
        setDatos.setRows(20);
        setDatos.setLectura(true);
        gridD.getChildren().add(setDatos);
        diaDialogo.setDialogo(gridD);
        setDatos.dibujar();
        diaDialogo.dibujar();
    }

    public void filtraLista() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabTabla2.setCondicion(getFiltrosLista());
            tabTabla2.ejecutarSql();
            utilitario.addUpdate("tabTabla2");
        }
    }

    private String getFiltrosLista() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        str_filtros = "id_anticipo = -1";
        return str_filtros;
    }

    public void actualizaLista() {
        if (!getFiltrosAcceso().isEmpty()) {
            tabTabla2.setCondicion(getFiltrosAcceso());
            tabTabla2.ejecutarSql();
            utilitario.addUpdate("tabTabla2");
        }
    }

    private String getFiltrosAcceso() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (tabTabla1.getValorSeleccionado() != null) {

            str_filtros = "id_anticipo in (SELECT dbo.nom_anticipo.id_anticipo\n"
                    + "FROM dbo.nom_solicitud\n"
                    + "INNER JOIN dbo.nom_anticipo ON dbo.nom_anticipo.id_solicitud = dbo.nom_solicitud.id_solicitud\n"
                    + "where dbo.nom_solicitud.ced_empleado = '" + tabTabla1.getValorSeleccionado() + "') ";

        } else {
            utilitario.agregarMensajeInfo("Filtros no válidos",
                    "Debe seleccionar valor");
        }
        return str_filtros;
    }

    public void listaRol() {
        adminRemuneracion.deleteTempRol();
        TablaGenerica tabValidar = adminRemuneracion.getAnticipoRol(generico.meses(Integer.parseInt(cmbPeriodo.getValue() + "")), Integer.parseInt(cmbAnio.getValue() + ""), cmbDistributivo.getValue() + "");
        if (!tabValidar.isEmpty()) {
            for (int i = 0; i < tabValidar.getTotalFilas(); i++) {
                adminRemuneracion.setTempRol(tabValidar.getValor(i, "codtra"), tabValidar.getValor(i, "cedciu"), tabValidar.getValor(i, "nomtra"), Double.valueOf(tabValidar.getValor(i, "descuentos")));
            }
        }
    }

    public void generaCedulas(String fecha0, String fecha1){
        adminRemuneracion.getGeneraListaCedula(fecha0, fecha1);
    }
    
    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "CONFIDENCIAL ANTICIPO":
                aceptoAnticipo();
                break;
            case "DESCUENTO ANTICIPO POR MES":
                diaDialogor.setDialogo(grir);
                Grid griBusca = new Grid();
                griBusca.setColumns(2);
                griBusca.getChildren().add(txeAnio);
                griBusca.getChildren().add(cmbAnio);
                griBusca.getChildren().add(txePeriodo);
                griBusca.getChildren().add(cmbPeriodo);
                griBusca.getChildren().add(txeDistributivo);
                griBusca.getChildren().add(cmbDistributivo);
                gridR.getChildren().add(griBusca);
                diaDialogor.setDialogo(gridR);
                diaDialogor.dibujar();
                break;
            case "TOTAL ANTICIPOS GENERAL":
                aceptoAnticipo();
                break;
            case "REPORTE GENERAL DE ANTIIPOS":
                aceptoAnticipo();
                break;
            case "REPORTE GENERAL DE ANTICIPOS":
                diaDiaAnticipo.setDialogo(gria);
                Grid griBus = new Grid();
                griBus.setColumns(2);
                griBus.getChildren().add(txeAnio1);
                griBus.getChildren().add(cmbAnio1);
                gridA.getChildren().add(griBus);
                diaDiaAnticipo.setDialogo(gridA);
                diaDiaAnticipo.dibujar();
                break;
                case "REPORTE ANTICIPOS GENERAL ESTADO":
                diaDialogot.setDialogo(grit);
                Grid griBusc = new Grid();
                griBusc.setColumns(2);
                griBusc.getChildren().add(txeTipo);
                griBusc.getChildren().add(cmbTipo);
                gridT.getChildren().add(griBusc);
                diaDialogot.setDialogo(gridT);
                diaDialogot.dibujar();
                break;
                case "REPORTE CEDULAS":
                diaDiaCedula.setDialogo(gric);
                Grid griBu = new Grid();
                griBu.setColumns(2);
                griBu.getChildren().add(txeIni);
                griBu.getChildren().add(fechaInicio);
                griBu.getChildren().add(txeFin);
                griBu.getChildren().add(fechaFin);
                gridC.getChildren().add(griBu);
                diaDiaCedula.setDialogo(gridC);
                diaDiaCedula.dibujar();
                break;
        }
    }

    public void aceptoAnticipo() {
        switch (rep_reporte.getNombre()) {
            case "CONFIDENCIAL ANTICIPO":
                sef_formato.setConexion(getConexion());
                TablaGenerica tabDato = adminRemuneracion.getIdSolicitud(Integer.parseInt(tabTabla2.getValorSeleccionado()));
                if (!tabDato.isEmpty()) {
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    p_parametros.put("codigo", Integer.parseInt(tabDato.getValor("id_solicitud") + ""));
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                } else {
                    utilitario.agregarMensaje("No puede Mostrar Reporte", "Datos no encontrados");
                }
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "DESCUENTO ANTICIPO POR MES":
                sef_formato.setConexion(getConexion());
                listaRol();
                p_parametros.put("anio", cmbAnio.getValue() + "");
                p_parametros.put("periodo", cmbPeriodo.getValue() + "");
                p_parametros.put("tipo", cmbDistributivo.getValue() + "");
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                if (cmbDistributivo.getValue().equals("NL")) {
                    p_parametros.put("distributivo", "Empleado");
                } else {
                    p_parametros.put("distributivo", "Trabajador");
                }
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "TOTAL ANTICIPOS GENERAL":
                sef_formato.setConexion(getConexion());
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "REPORTE GENERAL DE ANTICIPOS":
                sef_formato.setConexion(getConexion());
                p_parametros.put("anio", cmbAnio1.getValue() + "");
                p_parametros.put("anioAnt", String.valueOf(Integer.parseInt(cmbAnio1.getValue().toString())-1) +"");
                p_parametros.put("fecha", utilitario.getFechaActual() + "");
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
            case "REPORTE ANTICIPOS GENERAL ESTADO":
                sef_formato.setConexion(getConexion());
                p_parametros.put("estado", Integer.parseInt(cmbTipo.getValue()+""));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
                case "REPORTE CEDULAS":
                    sef_formato.setConexion(Nomina);
                    //genera lista de cedulas en SQL
//                    generaCedulas(fechaInicio.getFecha(), fechaInicio.getFecha());
                String mes12,
                 mes13;
                if (String.valueOf((utilitario.getMes(fechaInicio.getFecha()))).length() > 1) {
                    mes12 = String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                } else {
                    mes12 = "0" + String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
                }
                if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
                    mes13 = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                } else {
                    mes13 = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
                }
                p_parametros.put("inicial", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "14"));
                p_parametros.put("reforma", Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "15"));
                p_parametros.put("fechai", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaInicio.getFecha()))).substring(2, 4) + "" + mes12));
                p_parametros.put("fechaf", Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes13));
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

    public Tabla getTabTabla1() {
        return tabTabla1;
    }

    public void setTabTabla1(Tabla tabTabla1) {
        this.tabTabla1 = tabTabla1;
    }

    public Tabla getTabTabla2() {
        return tabTabla2;
    }

    public void setTabTabla2(Tabla tabTabla2) {
        this.tabTabla2 = tabTabla2;
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

    public Tabla getSetDatos() {
        return setDatos;
    }

    public void setSetDatos(Tabla setDatos) {
        this.setDatos = setDatos;
    }

    public void setP_parametros(Map p_parametros) {
        this.p_parametros = p_parametros;
    }

    public Conexion getNomina() {
        return Nomina;
    }

    public void setNomina(Conexion Nomina) {
        this.Nomina = Nomina;
    }
    
}

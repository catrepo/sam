
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;


import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_encuesta.ejb.BeanFormularios;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class ReporteLiquidacion extends Pantalla {

    private Texto txtCedula = new Texto();
    private Combo cmbOpcion = new Combo();
    private Calendario fecha1 = new Calendario();
    private Calendario fecha2 = new Calendario();
    private Tabla tabDenuncia = new Tabla();
    private Tabla tabMovimiento = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Panel panOpcion = new Panel();
    private AutoCompletar autBusca = new AutoCompletar();


    public ReporteLiquidacion() {
        //         Imagen de encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logoactual.png");
        agregarComponente(quinde);

        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonEliminar();

        tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();


        List list = new ArrayList();
        Object fila1[] = {
            "0", "Cédula Fallecido"
        };
        Object fila2[] = {
            "1", "Apellido Fallecido"
        };
        Object fila3[] = {
            "2", "Rango Fecha Defuncion"
        };
        Object fila4[] = {
            "3", "Catastro"
        };
        list.add(fila1);;
        list.add(fila2);;
        list.add(fila3);;
        list.add(fila4);;
        cmbOpcion.setCombo(list);

        Grid griPri = new Grid();
        griPri.setColumns(3);

        Grid griPan = new Grid();
        griPan.setColumns(2);
        griPan.getChildren().add(new Etiqueta("Buscar por :"));
        griPan.getChildren().add(cmbOpcion);
        griPri.getChildren().add(griPan);

        Grid griSear = new Grid();
        griSear.setColumns(3);
        griSear.getChildren().add(new Etiqueta("Ingrese: "));
        txtCedula.setSize(50);
        griSear.getChildren().add(txtCedula);

        Grid griDate = new Grid();
        griDate.setColumns(4);
        griDate.getChildren().add(new Etiqueta("Fecha Inicial: "));
        fecha1.setSize(11);
        griDate.getChildren().add(fecha1);
        griDate.getChildren().add(new Etiqueta("Fecha Final: "));
        fecha1.setSize(11);
        griDate.getChildren().add(fecha2);
        griDate.getChildren().add(fecha2);
        griSear.getChildren().add(griDate);
        griPri.getChildren().add(griSear);

        Grid griBot = new Grid();
        griBot.setColumns(2);
        Boton botBuscar = new Boton();
        botBuscar.setValue("Buscar ");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("busquedaInfo");
        griBot.getChildren().add(botBuscar);



        Boton botVer = new Boton();
        botVer.setValue("Ver");
        botVer.setExcluirLectura(true);
        botVer.setIcon("ui-icon-open");
        botVer.setMetodo("crearArchivo");
        griBot.getChildren().add(botVer);


        griPri.getChildren().add(griBot);
        bar_botones.agregarComponente(griPri);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("REPORTE GENERAL DE FALLECIDOS");
        agregarComponente(panOpcion);


        dibujarPantalla();

    }

    public void dibujarPantalla() {
        limpiarPanel();
        tabDenuncia.setId("tabDenuncia");
        tabDenuncia.setTabla("CMT_FALLECIDO", "IDE_FALLECIDO", 1);
        if (autBusca.getValue() == null) {
            tabDenuncia.setCondicion("IDE_FALLECIDO=-1");
        } else {
            tabDenuncia.setCondicion("IDE_FALLECIDO in (" + autBusca.getValor() + ")");
        }

        tabDenuncia.getColumna("IDE_CMGEN").setCombo("select IDE_CMGEN,DETALLE_CMGEN from CMT_GENERO");
        tabDenuncia.getColumna("IDE_CATASTRO").setCombo("SELECT IDE_CATASTRO,DETALLE_LUGAR+'-'+SECTOR+'-'+convert(varchar,NUMERO)+'-'+convert(varchar,MODULO) FROM CMT_CATASTRO A INNER JOIN CMT_LUGAR B ON A.IDE_LUGAR = B.IDE_LUGAR ");
        tabDenuncia.getColumna("IDE_CMGEN").setFiltro(true);
        tabDenuncia.getColumna("CEDULA_FALLECIDO").setFiltro(true);
        tabDenuncia.getColumna("NOMBRES").setFiltro(true);
        tabDenuncia.getColumna("FECHA_DEFUNCION").setFiltro(true);
        tabDenuncia.getColumna("IDE_CATASTRO").setFiltro(true);
        tabDenuncia.getColumna("IDE_FALLECIDO").setNombreVisual("CODIGO");
        tabDenuncia.getColumna("IDE_CMGEN").setNombreVisual("GENERO");
        tabDenuncia.getColumna("IDE_CMGEN").setLongitud(30);
        tabDenuncia.getColumna("CEDULA_FALLECIDO").setLongitud(30);
        tabDenuncia.getColumna("NOMBRES").setNombreVisual("APELLIDOS Y NOMBRES");
        tabDenuncia.getColumna("NOMBRES").setLongitud(200);
        tabDenuncia.getColumna("IDE_CATASTRO").setNombreVisual("CATASTRO");
        tabDenuncia.getColumna("IDE_CATASTRO").setLongitud(50);
        tabDenuncia.getColumna("FECHA_NACIMIENTO").setLongitud(30);
        tabDenuncia.getColumna("FECHA_DEFUNCION").setLongitud(30);
        tabDenuncia.getColumna("CERTIFICADO_DEFUN").setNombreVisual("CERT. DEFUNCION");
        tabDenuncia.getColumna("CERTIFICADO_DEFUN").setLongitud(30);
        tabDenuncia.getColumna("FECHA_DOCUMENTO_CMARE").setNombreVisual("FECHA INGRESO");
        tabDenuncia.getColumna("FECHA_DOCUMENTO_CMARE").setLongitud(30);
        tabDenuncia.getColumna("cedula_representante").setVisible(false);
        tabDenuncia.getColumna("representante_actual").setVisible(false);
        tabDenuncia.getColumna("FECHA_NACIMIENTO").setVisible(false);
        tabDenuncia.getColumna("TIPO_FALLECIDO").setVisible(false);
        tabDenuncia.getColumna("FECHA_INGRE").setVisible(false);
        tabDenuncia.getColumna("tipo_pago").setVisible(false);
        tabDenuncia.setCampoPrimaria("IDE_FALLECIDO");
//        tabDenuncia.setTipoFormulario(true);
//        tabDenuncia.getGrid().setColumns(2);
        tabDenuncia.setRows(10);
        tabDenuncia.agregarRelacion(tabMovimiento);
        tabDenuncia.setLectura(true);
        tabDenuncia.dibujar();
        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabDenuncia);

        tabMovimiento.setId("tabMovimiento");
        tabMovimiento.setTabla("CMT_DETALLE_MOVIMIENTO", "IDE_DET_MOVIMIENTO", 2);
        tabMovimiento.getColumna("IDE_CMREP").setCombo("select IDE_CMREP,NOMBRES_APELLIDOS_CMREP from CMT_representante");
        tabMovimiento.getColumna("IDE_TIPO_MOVIMIENTO").setCombo("SELECT IDE_CMACC,DETALLE_CMACC from cmt_accion");
//        tabMovimiento.getColumna("IDE_CATASTRO").setCombo("SELECT IDE_CATASTRO,DETALLE_LUGAR+'-'+SERIE_BLOQUE+'-'+convert(varchar,NUMERO_FILA)+'-'+convert(varchar,NUMERO) FROM CMT_CATASTRO A INNER JOIN CMT_LUGAR B ON A.IDE_LUGAR = B.IDE_LUGAR ");
        tabMovimiento.getColumna("IDE_DET_MOVIMIENTO").setNombreVisual("CODIGO");
        tabMovimiento.getColumna("IDE_CMREP").setNombreVisual("REPRESENTANTE");
        tabMovimiento.getColumna("IDE_CMREP").setLongitud(150);
        tabMovimiento.getColumna("IDE_TIPO_MOVIMIENTO").setLongitud(30);
        tabMovimiento.getColumna("IDE_TIPO_MOVIMIENTO").setNombreVisual("MOVIMIENTO");
        tabMovimiento.getColumna("IDE_TIPO_MOVIMIENTO").setLongitud(30);
//        tabMovimiento.getColumna("IDE_FALLECIDO").setVisible(false);
        tabMovimiento.getColumna("IDE_CATASTRO").setNombreVisual("CATASTRO");
        tabMovimiento.getColumna("IDE_CATASTRO").setLongitud(25);
        tabMovimiento.getColumna("FECHA_INGRESA").setVisible(false);
        tabMovimiento.getColumna("FECHA_DESDE").setLongitud(30);
        tabMovimiento.getColumna("FECHA_HASTA").setLongitud(30);
        tabMovimiento.getColumna("PERIODO_ARRENDAMIENTO").setLongitud(15);
        tabMovimiento.getColumna("PERIODO_ARRENDAMIENTO").setNombreVisual("PERIODO");
        tabMovimiento.getColumna("NUM_LIQUIDACION").setNombreVisual("LIQUIDACION");
        tabMovimiento.getColumna("NUM_LIQUIDACION").setLongitud(30);
        tabMovimiento.getColumna("VALOR_LIQUIDACION").setNombreVisual("VALOR");
        tabMovimiento.getColumna("VALOR_LIQUIDACION").setLongitud(10);
        tabMovimiento.getColumna("DOCUMENTOS").setVisible(false);
        tabMovimiento.getColumna("CODIGO_LIQUIDACION").setVisible(false);
        tabMovimiento.getColumna("PERIODO_LIQUIDACION").setVisible(false);
        tabMovimiento.getColumna("PERIODO_TITULO").setVisible(false);
        tabMovimiento.getColumna("IDE_CATASTRO2").setVisible(false);
        tabMovimiento.getColumna("VER").setVisible(false);
        tabMovimiento.getColumna("IDE_CMREP").setVisible(false);
        tabMovimiento.getColumna("IDE_TIPO_MOVIMIENTO").setVisible(false);
        tabMovimiento.getColumna("IDE_CATASTRO").setVisible(false);
        tabMovimiento.getColumna("tipo_renovacion").setVisible(false);
        tabMovimiento.getColumna("VALOR_LIQUIDACION_calculado").setVisible(false);
        tabMovimiento.getColumna("ESTADO_PAGO").setVisible(false);
        tabMovimiento.setRows(10);
        tabMovimiento.setLectura(true);
        tabMovimiento.dibujar();
        PanelTabla pnr = new PanelTabla();
        pnr.setPanelTabla(tabMovimiento);
        Division div = new Division();
        div.setId("div");
        div.dividir2(pnt, pnr, "30%", "v");

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
   

    public void busquedaInfo() {
        if (cmbOpcion.getValue().equals("0")) {
            buscaCedula();
        } else if (cmbOpcion.getValue().equals("1")) {
            buscaApellidoFallecido();
        } else if (cmbOpcion.getValue().equals("2")) {
            buscaFecha();
        } else if (cmbOpcion.getValue().equals("3")) {
            buscaCatastro();
        } else {
            utilitario.agregarMensaje("Parametros de busqueda no seleccionados", null);
        }
    }


    /*
     * Opciones de busqueda
     */
    public void buscaCedula() {
        if (!getBuscaCedula().isEmpty()) {
            tabDenuncia.setCondicion(getBuscaCedula());
            tabDenuncia.ejecutarSql();
            utilitario.addUpdate("tabDenuncia");
        }
    }

    private String getBuscaCedula() {
        String str_filtros = "";
        str_filtros = "CEDULA_FALLECIDO = '" + txtCedula.getValue() + "'";
        return str_filtros;
    }

    public void buscaApellidoFallecido() {
        if (!getBuscaApellidoFallecido().isEmpty()) {
            tabDenuncia.setCondicion(getBuscaApellidoFallecido());
            tabDenuncia.ejecutarSql();
            utilitario.addUpdate("tabDenuncia");
        }
    }

    private String getBuscaApellidoFallecido() {
        String str_filtros = "";
        System.out.println("NOMBRES");

        str_filtros = "NOMBRES like '" + txtCedula.getValue() + "%'";
        return str_filtros;
    }

    public void buscaCatastro() {
        if (!getBuscaCatastro().isEmpty()) {
            tabDenuncia.setCondicion(getBuscaCatastro());
            tabDenuncia.ejecutarSql();
            utilitario.addUpdate("tabDenuncia");
        }
    }

    private String getBuscaCatastro() {
        String str_filtros = "";
        str_filtros = "IDE_CATASTRO =(SELECT IDE_CATASTRO\n"
                + "FROM CMT_CATASTRO A INNER JOIN CMT_LUGAR B ON A.IDE_LUGAR = B.IDE_LUGAR \n"
                + "where DETALLE_LUGAR+'-'+SECTOR+'-'+convert(varchar,NUMERO)+'-'+convert(varchar,MODULO)='" + txtCedula.getValue() + "')";
        return str_filtros;
    }
//    public void buscaDireccion() {
//        if (!getBuscaDireccion().isEmpty()) {
//            tabDenuncia.setCondicion(getBuscaDireccion());
//            tabDenuncia.ejecutarSql();
//            utilitario.addUpdate("tabDenuncia");
//        }
//    }

//    private String getBuscaDireccion() {
//        String str_filtros = "";
//        str_filtros = "where estado = " + Integer.parseInt(cmbEstado.getValue() + "") + " "
//                + "and id_formulario in (SELECT ID_FORMULARIO FROM RESUG_ACTIVIDADES WHERE UNI_RESPONSABLE = " + Integer.parseInt(cmbDireccion.getValue() + "") + ") ";
//        return str_filtros;
//    }
    public void buscaFecha() {
        System.out.println("sigo ak");

        if (!getBuscaFecha().isEmpty()) {
            tabDenuncia.setCondicion(getBuscaFecha());
            tabDenuncia.ejecutarSql();
            utilitario.addUpdate("tabDenuncia");
        }
    }

    private String getBuscaFecha() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        str_filtros = "FECHA_DEFUNCION between '" + fecha1.getFecha() + "' and '" + fecha2.getFecha() + "'";
        return str_filtros;
    }

    public void buscaGeneral() {
        if (!getBuscaGeneral().isEmpty()) {
            tabDenuncia.setCondicion(getBuscaGeneral());
            tabDenuncia.ejecutarSql();
            utilitario.addUpdate("tabDenuncia");
        }
    }

    private String getBuscaGeneral() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "",
                anio = String.valueOf(utilitario.getAnio(utilitario.getFechaActual())),
                mes = String.valueOf(utilitario.getMes(utilitario.getFechaActual()) - 1),
                dia = "01";

//        str_filtros = "estado <> (SELECT ID_ESTADO FROM RESUG_ESTADO where estado = 'Negado') and estado <> (SELECT ID_ESTADO FROM RESUG_ESTADO where estado = 'Concluido') and fecha1 between '" + dia + "/" + mes + "/" + anio + "' and '" + utilitario.getFechaActual() + "'";
        return str_filtros;
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

    public Tabla getTabDenuncia() {
        return tabDenuncia;
    }

    public void setTabDenuncia(Tabla tabDenuncia) {
        this.tabDenuncia = tabDenuncia;
    }

    public Tabla getTabMovimiento() {
        return tabMovimiento;
    }

    public void setTabMovimiento(Tabla tabMovimiento) {
        this.tabMovimiento = tabMovimiento;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }
}

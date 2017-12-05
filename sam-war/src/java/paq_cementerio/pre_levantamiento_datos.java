/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_cementerio.ejb.cementerio;

/**
 *
 * @author Diego
 */
public class pre_levantamiento_datos extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Combo cmbOpcion = new Combo();
    private Combo cmbBloque = new Combo();
    private Calendario fecha1 = new Calendario();
    private Calendario fecha2 = new Calendario();
    private Tabla tabConsulta = new Tabla();
    private Panel panOpcion = new Panel();
    private AutoCompletar autBusca = new AutoCompletar();
    @EJB
    private cementerio cementerioM = (cementerio) utilitario.instanciarEJB(cementerio.class);

    public pre_levantamiento_datos() {        //         Imagen de encabezado
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logoactual.png");
        agregarComponente(quinde);

        bar_botones.quitarBotonsNavegacion();
//        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonEliminar();

        tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        cmbOpcion.setId("cmbOpcion");
        cmbOpcion.setCombo("SELECT distinct IDE_LUGAR  as id,DETALLE_LUGAR  FROM CMT_LUGAR order by DETALLE_LUGAR ");


        Grid griSear = new Grid();
        griSear.setColumns(8);
        griSear.getChildren().add(new Etiqueta("Lugar: "));
        griSear.getChildren().add(cmbOpcion);

        cmbBloque.setId("cmbBloque");
        cmbBloque.setCombo("SELECT distinct sector,sector FROM CMT_catastro");

        Grid griEsta = new Grid();
        griEsta.setColumns(2);
        griEsta.getChildren().add(new Etiqueta("Bloque: "));
        griEsta.getChildren().add(cmbBloque);


//        Grid griDate = new Grid();
//        griDate.setColumns(4);
//        griDate.getChildren().add(new Etiqueta("Fecha Inicial: "));
//        fecha1.setSize(11);
//        griDate.getChildren().add(fecha1);
//        griDate.getChildren().add(new Etiqueta("Fecha Final: "));
//        fecha1.setSize(11);
//        griDate.getChildren().add(fecha2);
//        griDate.getChildren().add(fecha2);

        griSear.getChildren().add(griEsta);
//        griSear.getChildren().add(griDate);


        Grid griBot = new Grid();
        griBot.setColumns(2);
        Boton botBuscar = new Boton();
        botBuscar.setValue("Buscar ");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("busquedaInfo");
        griBot.getChildren().add(botBuscar);

        griSear.getChildren().add(griBot);
        bar_botones.agregarComponente(griSear);

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("REPORTE GENERAL DE FALLECIDOS");
        agregarComponente(panOpcion);



        dibujarPantalla();

    }

    public void dibujarPantalla() {
        limpiarPanel();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("MIGRACIONFALLECIDOS", "IDE", 1);
        if (autBusca.getValue() == null) {
            tab_tabla.setCondicion("IDE=-1");
        } else {
            tab_tabla.setCondicion("IDE in (" + autBusca.getValor() + ")");
        }

//        tab_tabla.getColumna("ren_nombres").setFiltro(true);
//        tab_tabla.getColumna("ren_cedula").setFiltro(true);
//        tab_tabla.getColumna("ren_FILA").setFiltro(true);
//        tab_tabla.getColumna("ren_ide_fallecido").setNombreVisual("COD FALLECIDO");
//        tab_tabla.getColumna("ren_cedula").setNombreVisual("CEDULA");
//        tab_tabla.getColumna("ren_fecha_defuncion").setNombreVisual("FECHA DEFUNCION");
//        tab_tabla.getColumna("ren_nombres").setNombreVisual("APELLIDOS Y NOMBRES");
//        tab_tabla.getColumna("ren_lugar").setNombreVisual("LUGAR");
//        tab_tabla.getColumna("ren_bloque").setNombreVisual("BLOQUE");
//        tab_tabla.getColumna("ren_FILA").setNombreVisual("FILA");
//        tab_tabla.getColumna("ren_NUMERO").setNombreVisual("NUMERO");
        tab_tabla.getColumna("serie").setLongitud(15);
        tab_tabla.getColumna("nivel").setLongitud(15);
        tab_tabla.getColumna("MODULO").setLongitud(15);
        tab_tabla.getColumna("UBICACION").setLongitud(15);
        tab_tabla.getColumna("NOMBREFALLECIDO").setLongitud(50);
//        tab_tabla.getColumna("observaciones").setLongitud(50);
//        tab_tabla.getColumna("ren_cedula").setLectura(true);
//        tab_tabla.getColumna("ren_nombres").setLectura(true);
//        tab_tabla.getColumna("ren_lugar").setLectura(true);
//        tab_tabla.getColumna("ren_bloque").setLectura(true);
//        tab_tabla.getColumna("ren_FILA").setLectura(true);
//        tab_tabla.getColumna("ren_NUMERO").setLectura(true);
//        tab_tabla.getColumna("ren_ide_fallecido").setLectura(true);
//        tab_tabla.getColumna("ren_fecha_defuncion").setLectura(true);
//        tab_tabla.getColumna("ex_cedula").setNombreVisual("EXCEL CEDULA");
//        tab_tabla.getColumna("ex_fecha_defuncion").setNombreVisual("EXCEL FECHA DEFUNCION");
//        tab_tabla.getColumna("ex_nombres").setNombreVisual("EXCEL APELLIDOS Y NOMBRES");
//        tab_tabla.getColumna("ex_lugar").setNombreVisual("EXCEL LUGAR");
//        tab_tabla.getColumna("ex_bloque").setNombreVisual("EXCEL BLOQUE");
//        tab_tabla.getColumna("ex_FILA").setNombreVisual("EXCEL FILA");
//        tab_tabla.getColumna("ex_NUMERO").setNombreVisual("EXCEL NUMERO");
//        tab_tabla.getColumna("ex_cedula").setLongitud(20);
//        tab_tabla.getColumna("ex_nombres").setLongitud(50);
//        tab_tabla.getColumna("ex_lugar").setLongitud(15);
//        tab_tabla.getColumna("ex_bloque").setLongitud(10);
//        tab_tabla.getColumna("ex_FILA").setLongitud(10);
//        tab_tabla.getColumna("ex_NUMERO").setLongitud(10);
//        tab_tabla.getColumna("ex_cedula").setLectura(true);
//        tab_tabla.getColumna("ex_nombres").setLectura(true);
//        tab_tabla.getColumna("ex_lugar").setLectura(true);
//        tab_tabla.getColumna("ex_bloque").setLectura(true);
//        tab_tabla.getColumna("ex_FILA").setLectura(true);
        tab_tabla.getColumna("SERIEANTIGUA").setVisible(false);
        tab_tabla.getColumna("LUGAR").setVisible(false);
        tab_tabla.getColumna("sector").setVisible(false);
        tab_tabla.getColumna("bloque").setVisible(false);
        tab_tabla.getColumna("nicho").setVisible(false);

        List lista = new ArrayList();
        Object fila1[] = {
            "1", "SI"
        };
        Object fila2[] = {
            "2", "NO"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        tab_tabla.getColumna("aprueba").setRadio(lista, "1");
        tab_tabla.setRows(10);
//        tab_tabla.setLectura(true);
        tab_tabla.dibujar();
        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tab_tabla);




        Division div = new Division();
        div.setId("div");
        div.dividir2(pnt, null, "80%", "h");

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
        System.out.println("cmbOpcion.getValue().equals(\"0\")<<<<<<<<<<<<<<<<<<<<" + cmbOpcion.getValue());
        System.out.println("cmbBloque.getValue()<<<<<<<<<<<<<<<<<<<<" + cmbBloque.getValue());
        if (!cmbOpcion.getValue().equals("0")) {
            buscaCatastro();
        } else if (!fecha1.getFecha().isEmpty()) {
            buscaFecha();
        } else {
            utilitario.agregarMensaje("Parametros de busqueda no seleccionados", null);
        }
    }

    public void buscaCatastro() {
        if (!getBuscaCatastro().isEmpty()) {
            tab_tabla.setCondicion(getBuscaCatastro());
            tab_tabla.ejecutarSql();
            utilitario.addUpdate("tab_tabla");
        }
    }

    public void buscaFecha() {
        System.out.println("sigo ak");

        if (!getBuscaFecha().isEmpty()) {
            tab_tabla.setCondicion(getBuscaFecha());
            tab_tabla.ejecutarSql();
            utilitario.addUpdate("tab_tabla");
        }
    }

    private String getBuscaCatastro() {
        String str_filtros = "";
        str_filtros = "ex_lugar =(SELECT DISTINCT DETALLE_LUGAR\n"
                + "FROM CMT_CATASTRO A INNER JOIN CMT_LUGAR B ON A.IDE_LUGAR = B.IDE_LUGAR \n"
                + "where A.IDE_LUGAR='" + cmbOpcion.getValue() + "')  AND EX_BLOQUE=" + cmbBloque.getValue() + "";
        return str_filtros;
    }

    private String getBuscaFecha() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        str_filtros = "FECHA_DEFUNCION between '" + fecha1.getFecha() + "' and '" + fecha2.getFecha() + "'";
        return str_filtros;
    }

    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        if (tab_tabla.guardar()) {
            for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {

                cementerioM.set_updateMigraDatos(tab_tabla.getValor("aprueba"), tab_tabla.getValor(i, "cod_tabla"));
                guardarPantalla();
                utilitario.addUpdate("tab_tabla");
            }
        } else {
            utilitario.agregarMensajeInfo("Registro no  actualizado", "");

        }
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }
}

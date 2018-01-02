package paq_bienes;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_bienes.ejb.acfbienes;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author l-suntaxi
 */
public class ActaContatacionFisicaMuebles extends Pantalla {

    private Conexion CAYMAN = new Conexion();
    private Combo cmbTipo = new Combo();
    private Tabla tabDenuncia = new Tabla();
    private Tabla tabMovimiento = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Texto txtItems = new Texto();
    private Texto txtValor = new Texto();
    private Dialogo diaDialogo = new Dialogo();
    private Grid grid = new Grid();
    private Grid gridD = new Grid();
    private AutoCompletar aut_busca = new AutoCompletar();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    @EJB
    private acfbienes activos = (acfbienes) utilitario.instanciarEJB(acfbienes.class);

    public ActaContatacionFisicaMuebles() {
        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonEliminar();

        CAYMAN.setUnidad_persistencia(utilitario.getPropiedad("recursojdbcayman"));
        CAYMAN.NOMBRE_MARCA_BASE = "sqlserver";

        aut_busca.setId("aut_busca");
        aut_busca.setConexion(CAYMAN);

        tabConsulta.setId("tab_consulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        cmbTipo.setConexion(CAYMAN);
        cmbTipo.setId("cmbOpcion");
        cmbTipo.setCombo("SELECT CUS_ID,CUS_APELLIDOS+' '+ISNULL(CUS_NOMBRES,'') as custodio from custodio order by CUS_APELLIDOS+' '+ISNULL(CUS_NOMBRES,'')   ");


        bar_botones.agregarComponente(new Etiqueta("Custodio :"));
        bar_botones.agregarComponente(cmbTipo);

        Boton botBuscar = new Boton();
        botBuscar.setValue("Buscar ");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("busquedaInfo");
        bar_botones.agregarBoton(botBuscar);


        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("Seleccione Acta"); //titulo
        diaDialogo.setWidth("30%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("20%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getBot_aceptar().setMetodo("mostrarReporte");
        grid.setColumns(4);
        agregarComponente(diaDialogo);

        tabDenuncia.setId("tabDenuncia");
        tabDenuncia.setConexion(CAYMAN);
        tabDenuncia.setTabla("ACF", "IDE_ACTA", 1);
        if (aut_busca.getValue() == null) {
            tabDenuncia.setCondicion("IDE_ACTA=-1");
        } else {
            tabDenuncia.setCondicion("IDE_ACTA in (" + aut_busca.getValor() + ")");
        }
        tabDenuncia.getColumna("numero").setNombreVisual("NUMERO");
        tabDenuncia.getColumna("fecha_acta").setNombreVisual("FECHA");
        tabDenuncia.getColumna("responsable").setNombreVisual("DELEGADO DIRECCION");
        tabDenuncia.getColumna("IDE_ACTA").setNombreVisual("CODIGO");
        tabDenuncia.getColumna("responsable").setCombo("SELECT CUS_ID,CUS_APELLIDOS+' '+ISNULL(CUS_NOMBRES,'') as custodio from custodio order by CUS_APELLIDOS+' '+ISNULL(CUS_NOMBRES,'')   ");

//        tipoactivo barras  codigo  dependencia direccion oficina custodio cuenta  observaciones  nombre descripcion
// estado
//marca modelo serie fecha  valor  util residual
        tabDenuncia.getColumna("tipoactivo").setVisible(false);
        tabDenuncia.getColumna("barras").setVisible(false);
        tabDenuncia.getColumna("codigo").setVisible(false);
        tabDenuncia.getColumna("dependencia").setVisible(false);
        tabDenuncia.getColumna("direccion").setVisible(false);
        tabDenuncia.getColumna("oficina").setVisible(false);
        tabDenuncia.getColumna("custodio").setVisible(false);
        tabDenuncia.getColumna("cuenta").setVisible(false);
        tabDenuncia.getColumna("observaciones").setVisible(false);
        tabDenuncia.getColumna("nombre").setVisible(false);
        tabDenuncia.getColumna("descripcion").setVisible(false);
        tabDenuncia.getColumna("estado").setVisible(false);
        tabDenuncia.getColumna("marca").setVisible(false);
        tabDenuncia.getColumna("modelo").setVisible(false);
        tabDenuncia.getColumna("serie").setVisible(false);
        tabDenuncia.getColumna("fecha").setVisible(false);
        tabDenuncia.getColumna("valor").setVisible(false);
        tabDenuncia.getColumna("util").setVisible(false);
        tabDenuncia.getColumna("residual").setVisible(false);
        tabDenuncia.setCondicion("ide_acta= (select max(ide_acta) from acf)");
//      tabDenuncia.setGenerarPrimaria(false);

//tabDenuncia.agregarRelacion(tabMovimiento);
        tabDenuncia.getGrid().setColumns(4);
        tabDenuncia.setTipoFormulario(true);
        tabDenuncia.dibujar();
        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabDenuncia);

        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        txtItems.setId("txtItems");
        txtItems.setSize(5);
        gri_busca.getChildren().add(new Etiqueta("<B>Total Items: </B>"));
        gri_busca.getChildren().add(txtItems);
        txtValor.setId("txtValor");
        txtValor.setSize(10);
        gri_busca.getChildren().add(new Etiqueta("<B>Total Valor: </B>"));
        gri_busca.getChildren().add(txtValor);
        
        Division dic = new Division();
        dic.setId("dic");
        dic.dividir2(pnt, gri_busca, "70%", "v");
        
        tabMovimiento.setId("tabMovimiento");
        tabMovimiento.setConexion(CAYMAN);
        tabMovimiento.setTabla("activo", "ACT_ID", 2);
        tabMovimiento.getColumna("EMP_ID").setVisible(false);
        tabMovimiento.getColumna("ACT_FECHACREACION").setVisible(false);
        tabMovimiento.getColumna("ACT_FECHAINIDEPRE").setVisible(false);
        tabMovimiento.getColumna("ACT_DEPRECIABLESRI").setVisible(false);
        tabMovimiento.getColumna("ACT_DEPRECIADONIIF").setVisible(false);
        tabMovimiento.getColumna("ACT_DEPRECIABLE").setVisible(false);
        tabMovimiento.getColumna("ACT_DEPRECIADOSRI").setVisible(false);
        tabMovimiento.getColumna("ACT_FECHAINIOPER").setVisible(false);
        tabMovimiento.getColumna("ACT_FECHAINIDEPRENIIF").setVisible(false);
        tabMovimiento.getColumna("ACT_FECHABAJA").setVisible(false);
        tabMovimiento.getColumna("USERNAME").setVisible(false);
        tabMovimiento.getColumna("ACT_CODBARRASPADRE").setVisible(false);

        tabMovimiento.getColumna("ACT_FOTO1").setVisible(false);
        tabMovimiento.getColumna("ACT_FOTO2").setVisible(false);
        tabMovimiento.getColumna("ACT_DOC1").setVisible(false);
        tabMovimiento.getColumna("ACT_DOC2").setVisible(false);
        tabMovimiento.getColumna("GRU_ID1").setVisible(false);
        tabMovimiento.getColumna("GRU_ID2").setVisible(false);
        tabMovimiento.getColumna("GRU_ID3").setVisible(false);
        tabMovimiento.getColumna("UGE_ID1").setVisible(false);
        tabMovimiento.getColumna("UGE_ID2").setVisible(false);
        tabMovimiento.getColumna("UGE_ID3").setVisible(false);


        tabMovimiento.getColumna("UGE_ID4").setVisible(false);
        tabMovimiento.getColumna("UOR_ID1").setVisible(false);
        tabMovimiento.getColumna("UOR_ID2").setVisible(false);
        tabMovimiento.getColumna("UOR_ID3").setVisible(false);
        tabMovimiento.getColumna("CUS_ID1").setVisible(false);
        tabMovimiento.getColumna("CUS_ID2").setVisible(false);
        tabMovimiento.getColumna("EST_ID1").setVisible(false);
        tabMovimiento.getColumna("EST_ID2").setVisible(false);
        tabMovimiento.getColumna("EST_ID3").setVisible(false);
        tabMovimiento.getColumna("MAR_ID").setVisible(false);

        tabMovimiento.getColumna("MOD_ID").setVisible(false);
        tabMovimiento.getColumna("COL_ID").setVisible(false);
        tabMovimiento.getColumna("ECO_ID1").setVisible(false);
        tabMovimiento.getColumna("ECO_ID2").setVisible(false);
        tabMovimiento.getColumna("PRO_ID").setVisible(false);
        tabMovimiento.getColumna("ACT_TIPOING").setVisible(false);
        tabMovimiento.getColumna("ACT_VIDAUTILNIIF").setVisible(false);
        tabMovimiento.getColumna("ACT_VALORRESIDUALNIIF").setVisible(false);
        tabMovimiento.getColumna("ACT_GARANTIA").setVisible(false);


        tabMovimiento.getColumna("ACT_FECHAGARANTIAVENCE").setVisible(false);
        tabMovimiento.getColumna("ACT_TRANSFEROK").setVisible(false);
        tabMovimiento.getColumna("ACT_PPC").setVisible(false);
        tabMovimiento.getColumna("ACT_H1").setVisible(false);
        tabMovimiento.getColumna("ACT_H2").setVisible(false);
        tabMovimiento.getColumna("ACT_PE").setVisible(false);
        tabMovimiento.getColumna("ACT_OBSERVACIONES2").setVisible(false);
        tabMovimiento.getColumna("ACT_TIPOBAJA").setVisible(false);
        tabMovimiento.getColumna("ACT_OBSBAJA").setVisible(false);

        tabMovimiento.getColumna("ACT_TIPO").setLongitud(25);
        tabMovimiento.getColumna("ACT_CODBARRAS").setLongitud(35);
        tabMovimiento.getColumna("ACT_FECHACOMPRA").setLongitud(35);
        tabMovimiento.getColumna("ACT_SERIE1").setLongitud(25);


        tabMovimiento.getColumna("ACT_CODIGO1").setNombreVisual("CODIGO");
        tabMovimiento.getColumna("ACT_NUMFACT").setNombreVisual("FACTURA");
        tabMovimiento.getColumna("ACT_VALORCOMPRA").setNombreVisual("VALOR");
        tabMovimiento.getColumna("ACT_VIDAUTIL").setNombreVisual("VIDA UTIL");
        tabMovimiento.getColumna("ACT_ANIO").setNombreVisual("AÑO");

        tabMovimiento.getColumna("ACT_TIPO").setNombreVisual("TIPO");
        tabMovimiento.getColumna("ACT_CODBARRAS").setNombreVisual("COD BARRAS");
        tabMovimiento.getColumna("ACT_FECHACOMPRA").setNombreVisual("FECHA COMPRA");
        tabMovimiento.getColumna("ACT_SERIE1").setNombreVisual("SERIE");
        tabMovimiento.setCampoOrden("ACT_CODIGO1 DESC");
        tabMovimiento.setCondicion("ACT_ID=-1");
//        tabMovimiento.setGenerarPrimaria(false);
        tabMovimiento.setRows(30);
        tabMovimiento.setLectura(true);
        tabMovimiento.dibujar();

        PanelTabla pnr = new PanelTabla();
        pnr.setPanelTabla(tabMovimiento);

        Division div = new Division();
        div.setId("div");
        div.dividir2(dic, pnr, "20%", "h");
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(CAYMAN);
        agregarComponente(sef_formato);

        agregarComponente(div);
    }

    public void buscarPersona(SelectEvent evt) {
        aut_busca.onSelect(evt);
        System.out.println("aut_busca.getValor()" + aut_busca.getValor());

        if (aut_busca.getValor() != null) {
            System.out.println("aut_busc" + aut_busca.getValor());

            tabDenuncia.setFilaActual(aut_busca.getValor());
            utilitario.addUpdate("tabDenuncia");
        }
    }
    /*
     * Metodo para ingresar un comentario de cierre
     */

    public void busquedaInfo() {
        if (cmbTipo.getValue() != null && cmbTipo.getValue().toString().isEmpty() == false) {
            buscaCedula();
        } else {
            utilitario.agregarMensaje("Parametros de busqueda no seleccionados", null);
        }
    }

    public void buscaCedula() {
        limpieza();
        if (!getBuscaCedula().isEmpty()) {
            tabMovimiento.setCondicion(getBuscaCedula());
            tabMovimiento.ejecutarSql();
            utilitario.addUpdate("tabMovimiento");
            datosTotal();
        }
    }

    private String getBuscaCedula() {
        if (Integer.parseInt(activos.listaMax("ACF")) > 0) {
            tabDenuncia.insertar();
            tabDenuncia.setValor("numero", activos.listaMax("ACF"));
                    System.err.println("cmbTipo.getValue()<<<<<<<<<<" + cmbTipo.getValue());
            String str_filtros = "";
            str_filtros = "CUS_ID1 = '" + cmbTipo.getValue() + "'";
            return str_filtros;
        } else {
            utilitario.agregarMensaje("No se encuentra número para acta", "");
            return null;
        }
    }

    public void limpieza() {
        txtItems.setValue("");
        txtValor.setValue("");
        utilitario.addUpdate("txtItems,txtValor");
    }
    
    public void datosTotal(){
        TablaGenerica tabDatos = activos.getActivoTotales(Integer.parseInt(cmbTipo.getValue() + ""));
        if (!tabDatos.isEmpty()) {
            txtItems.setValue(tabDatos.getValor("item"));
            txtValor.setValue(tabDatos.getValor("total"));
            utilitario.addUpdate("txtItems,txtValor");
        } else {
            utilitario.agregarMensaje("Datos ocurrio un error", null);
        }
    }
    
    public void buscaActas() {
        diaDialogo.Limpiar();
        diaDialogo.setDialogo(grid);
        diaDialogo.setDialogo(gridD);
        diaDialogo.dibujar();
    }

    @Override
    public void abrirListaReportes() {
        System.out.println("entro akkk");
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        System.out.println("sali akkk");
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "REPORTE ACTA":
                aceptoOrden();
                break;
            case "REPORTE TOTAL DETALLE":
                aceptoOrden();
                break;
        }
    }

    public void aceptoOrden() {
        System.out.println("voy por  akkk" + tabDenuncia.getValor("NUMero"));
        switch (rep_reporte.getNombre()) {
            case "REPORTE ACTA":
                p_parametros.put("num_acta", tabDenuncia.getValor("numero") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;

            case "REPORTE TOTAL DETALLE":
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    @Override
    public void insertar() {
        tabDenuncia.insertar();
    }

    public void estadoComp() {
        for (int i = 0; i < tabMovimiento.getTotalFilas(); i++) {
            System.out.println("tabMovimiento.getValor(i, \"ACT_ID\")<<<<<<<<<<<<<<<<<<<<<<<<<" + tabMovimiento.getValor(i, "ACT_ID"));
            System.out.println(",tabDenuncia.getValor(\"fecha_acta\"<<<<<<<<<<<<<<<<<<<<<<<<<" + tabDenuncia.getValor("fecha_acta"));
            System.out.println("tabDenuncia.getValor(\"responsable\")<<<<<<<<<<<<<<<<<<<<<" + tabDenuncia.getValor("responsable"));
            System.out.println("tabDenuncia.getValor(\"numero\")<<<<<<<<<<<<<<<<<<<<<" + tabDenuncia.getValor("numero"));
            System.out.println("tabDenuncia.getValor(\"codigo\")<<<<<<<<<<<<<<<<<<" + tabDenuncia.getValor("codigo"));

            activos.guardActa(tabMovimiento.getValor(i, "ACT_ID"), tabDenuncia.getValor("fecha_acta"), tabDenuncia.getValor("responsable"), tabDenuncia.getValor("numero"), tabDenuncia.getValor("codigo"));
        }
        activos.setUpdateFallecido(Integer.parseInt(tabDenuncia.getValor("numero")), "ACF");
        utilitario.agregarMensaje("Registro ingresado correctamente", "");
//        tabDenuncia.setValor("ide_acta", activos.listaMaxActa());
        utilitario.addUpdate("tabDenuncia");
    }

    @Override
    public void guardar() {
        estadoComp();
//tabDenuncia.guardar();

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

    public Conexion getCAYMAN() {
        return CAYMAN;
    }

    public void setCAYMAN(Conexion CAYMAN) {
        this.CAYMAN = CAYMAN;
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
}

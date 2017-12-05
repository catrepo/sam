/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

/**
 *
 * @author l-suntaxi
 */
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import sistema.aplicacion.Pantalla;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_cementerio.ejb.cementerio;

/**
 *
 * @author Diego
 */
public class pre_movimiento extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private AutoCompletar aut_busca = new AutoCompletar();
    /*
     * Variable para imprimir reportes
     */
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    private Dialogo dia_fallecido = new Dialogo();
    private Tabla tab_fallecido = new Tabla();
    @EJB
    private cementerio cementerioM = (cementerio) utilitario.instanciarEJB(cementerio.class);

    public pre_movimiento() {
        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("select ide_lugar,detalle_lugar from CMT_LUGAR order by DETALLE_LUGAR");
        aut_busca.setMetodoChange("buscarMovimiento");
        aut_busca.setSize(80);
        bar_botones.agregarComponente(new Etiqueta("Buscar:"));
        bar_botones.agregarComponente(aut_busca);

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("CMT_CATASTRO", "IDE_CATASTRO", 1);
        tab_tabla1.setHeader("MOVIMIENTO CEMENTERIO");//DETALLE_LUGAR,SECTOR,NUMERO,NUMERO_FILA 
        tab_tabla1.getColumna("IDE_LUGAR").setCombo("SELECT IDE_LUGAR,DETALLE_LUGAR FROM CMT_LUGAR   ORDER BY DETALLE_LUGAR");
        tab_tabla1.setGenerarPrimaria(false);
        tab_tabla1.getColumna("MODULO").setMetodoChange("datosCatastro");
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.dibujar();
        PanelTabla tabp = new PanelTabla();
        tabp.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("CMT_DETALLE_MOVIMIENTO", "IDE_DET_MOVIMIENTO", 2);
        tab_tabla2.setHeader("DETALLE MOVIMIENTO");
        tab_tabla2.getColumna("IDE_TIPO_MOVIMIENTO").setCombo("SELECT IDE_CMACC,DETALLE_CMACC FROM CMT_ACCION");

        tab_tabla2.getColumna("IDE_CMREP").setCombo("SELECT IDE_CMREP,NOMBRES_APELLIDOS_CMREP FROM CMT_REPRESENTANTE order by NOMBRES_APELLIDOS_CMREP");
        tab_tabla2.getColumna("IDE_FALLECIDO").setCombo("SELECT IDE_FALLECIDO,NOMBRES FROM CMT_FALLECIDO");
        tab_tabla2.getGrid().setColumns(4);
        tab_tabla2.setTipoFormulario(true);
        tab_tabla2.dibujar();
        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_tabla2);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(tabp, tabp1, "30%", "H");
        agregarComponente(div_division);

        /*
         * Configuración y llamado de objeto reporte
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);

        // crear dialogo fallecido
        dia_fallecido.setId("dia_fallecido");
        dia_fallecido.setTitle("CREAR DATOS FALLECIDO");
        dia_fallecido.setWidth("45%");
        dia_fallecido.setHeight("45%");

        tab_fallecido.setId("tab_fallecido");
        tab_fallecido.setTabla("CMT_FALLECIDO", "IDE_FALLECIDO", 3);
        tab_fallecido.setHeader("DATOS DEL FALLECIDO");
        tab_fallecido.getColumna("IDE_CMGEN").setCombo("select IDE_CMGEN,DETALLE_CMGEN from CMT_GENERO");

        tab_fallecido.setMostrarNumeroRegistros(false);
        tab_fallecido.setTipoFormulario(true);
        tab_fallecido.setCondicion("IDE_FALLECIDO=-1");
        tab_fallecido.dibujar();

        dia_fallecido.getBot_aceptar().setMetodo("aceptarFallecido");
        dia_fallecido.setDialogo(tab_fallecido);
        agregarComponente(dia_fallecido);

        Boton bot_crear_fallecido = new Boton();
        bot_crear_fallecido.setValue("CREAR DATOS FALLECIDO");
        bot_crear_fallecido.setMetodo("abrirFallecido");
        bar_botones.agregarBoton(bot_crear_fallecido);
    }

    public void abrirFallecido() {
        dia_fallecido.dibujar();
        tab_fallecido.insertar();
    }

    public void aceptarFallecido() {
        if (tab_fallecido.guardar()) {
            if (guardarPantalla().isEmpty()) {
                dia_fallecido.cerrar();
                tab_tabla2.actualizarCombos();
                tab_tabla2.insertar();
                tab_tabla2.setValor("ide_fallecido", tab_fallecido.getValor("ide_fallecido"));
                tab_tabla2.setValor("NUMERO_FORMULARIO", cementerioM.maxNumF());

            }
        }
    }

    public void datosCatastro() {
        TablaGenerica tab_dato = cementerioM.getDatoCatastro(tab_tabla1.getValor("IDE_LUGAR"), tab_tabla1.getValor("SECTOR"), tab_tabla1.getValor("NUMERO"), tab_tabla1.getValor("MODULO"));
        if (!tab_dato.isEmpty()) {
            tab_tabla1.setValor("ide_catastro", tab_dato.getValor("ide_catastro"));
            utilitario.addUpdate("tab_tabla1");
        } else {
            utilitario.agregarMensaje("Catastro No existe", "Revice datos");
        }
    }

    public void buscarMovimiento(SelectEvent evt) {
        aut_busca.onSelect(evt);
        if (aut_busca.getValor() != null) {
            tab_tabla1.setCondicion("ide_lugar=" + aut_busca.getValor().toString());
            tab_tabla1.ejecutarSql();
        }
    }

    public void numero() {
        String numero = cementerioM.maxNumF();
        Integer cantidad = 0;
        cantidad = Integer.parseInt(numero) + 1;
        utilitario.addUpdate("tab_tabla2");
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
            case "MOVIMIENTO FALLECIDO":
                aceptoOrden();
                break;
        }
    }

    public void aceptoOrden() {
        System.out.println("voy por  akkk" + tab_tabla2.getValor("IDE_DET_MOVIMIENTO"));
        switch (rep_reporte.getNombre()) {
            case "MOVIMIENTO FALLECIDO":
//              p_parametros.put("nom_resp", tab_tabla2.getValor("NICK_USUA") + "");
                p_parametros.put("ide_det_mov", Integer.parseInt(tab_tabla2.getValor("IDE_DET_MOVIMIENTO") + ""));
                System.out.println(p_parametros);
                System.out.println(rep_reporte.getPath());
                rep_reporte.cerrar();

                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }
//  public void fecha() {
//        TablaGenerica tab_dato = cementerioM.fechaFin(tab_tabla2.getValor("FECHA_LIQUIDACION"),Integer.parseInt( tab_tabla.getValor("PLAZO")));
//        if (!tab_dato.isEmpty()) {
//            System.out.println("<<<<<<<<<<"+tab_dato.getValor("FECHA_VENCIMIENTO"));
//            tab_tabla2.setValor("FECHA_EMISION", tab_dato.getValor("FECHA_VENCIMIENTO"));
//            utilitario.addUpdate("tab_tabla");
//        } else {
//            utilitario.agregarMensaje("No existe", "Revice datos");
//        }
//    }

    @Override
    public void insertar() {
        tab_tabla2.insertar();
        tab_tabla2.setValor("NUMERO_FORMULARIO", cementerioM.maxNumF());
    }

    @Override
    public void guardar() {
        if (tab_tabla2.guardar()) {
            System.out.println("IDE_TIPO_MOVIMIENTO<<<<<<<<" + tab_tabla2.getValor("IDE_TIPO_MOVIMIENTO"));
            System.out.println("cementerioM<<<<<<<<" + cementerioM.tipoMovimiento(tab_tabla2.getValor("IDE_TIPO_MOVIMIENTO")));
            if (cementerioM.tipoMovimiento(tab_tabla2.getValor("IDE_TIPO_MOVIMIENTO")).equals("Exhumacion")) {
                cementerioM.set_updateDisponibleExhumado(tab_tabla1.getValor("IDE_CATASTRO"));
            } else {
                cementerioM.set_updateDisponible(tab_tabla1.getValor("IDE_CATASTRO"));
            }
        }
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
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

    public Dialogo getDia_fallecido() {
        return dia_fallecido;
    }

    public void setDia_fallecido(Dialogo dia_fallecido) {
        this.dia_fallecido = dia_fallecido;
    }

    public Tabla getTab_fallecido() {
        return tab_fallecido;
    }

    public void setTab_fallecido(Tabla tab_fallecido) {
        this.tab_fallecido = tab_fallecido;
    }
}
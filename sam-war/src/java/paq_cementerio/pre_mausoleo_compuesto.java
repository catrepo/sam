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
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import sistema.aplicacion.Pantalla;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_cementerio.ejb.cementerio;

public class pre_mausoleo_compuesto extends Pantalla {

    private Tabla tab_categoria = new Tabla();
    private Tabla tab_tipo = new Tabla();
    private AutoCompletar aut_busca = new AutoCompletar();
    private SeleccionTabla setRegistros = new SeleccionTabla();
    private Combo cmbTipo = new Combo();
    @EJB
    private cementerio cementerioM = (cementerio) utilitario.instanciarEJB(cementerio.class);

    public pre_mausoleo_compuesto() {

        cmbTipo.setId("cmbTipo");
        cmbTipo.setCombo("SELECT distinct IDE_LUGAR  as id,DETALLE_LUGAR  FROM CMT_LUGAR WHERE DETALLE_LUGAR='SITIO' OR DETALLE_LUGAR='NICHO'");

        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Disponibles");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("buscaRegistro");
        bar_botones.agregarBoton(bot_busca);
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
        setRegistros.setSeleccionTabla("select top 10 IDE_CATASTRO,DETALLE_LUGAR,SECTOR,NUMERO  from CMT_CATASTRO a, cmt_lugar b\n"
                + "where a.ide_lugar=b.ide_lugar  AND DETALLE_LUGAR='NICHO' OR DETALLE_LUGAR='SITIO' ORDER BY DETALLE_LUGAR", "IDE_CATASTRO");
        setRegistros.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        setRegistros.getTab_seleccion().getColumna("DETALLE_LUGAR").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("SECTOR").setFiltro(true);
        setRegistros.getTab_seleccion().getColumna("NUMERO").setFiltro(true);
//        setRegistros.getTab_seleccion().getColumna("MODULO").setFiltro(true);
        setRegistros.getTab_seleccion().setRows(12);
        setRegistros.setWidth("60%");
        setRegistros.setRadio();
        setRegistros.getGri_cuerpo().setHeader(gri_busca);
        setRegistros.getBot_aceptar().setMetodo("consultaCatastro");
        setRegistros.setHeader("BUSCAR CATASTRO CEMENTERIO");
        agregarComponente(setRegistros);


        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT IDE_MAU_COMP,DESCRIPCION_MAU from  CMT_MAUSOLEO_COMPUESTO");
        aut_busca.setMetodoChange("buscarMausoleoCompuesto");
        aut_busca.setSize(80);
        bar_botones.agregarComponente(new Etiqueta("Buscar:"));
        bar_botones.agregarComponente(aut_busca);

        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);

        tab_categoria.setId("tab_categoria");
        tab_categoria.setTabla("CMT_MAUSOLEO_COMPUESTO", "IDE_MAU_COMP", 1);
        tab_categoria.setHeader("MAUSOLEO COMPUESTO");
        tab_categoria.setTipoFormulario(true);
        tab_categoria.agregarRelacion(tab_tipo);
        tab_categoria.dibujar();
        PanelTabla tabp = new PanelTabla();
        tabp.setPanelTabla(tab_categoria);

        tab_tipo.setId("tab_tipo");
        tab_tipo.setTabla("CMT_MAU_UBICA", "IDE_MAU_UBICA", 2);
        tab_tipo.setHeader("DETALLE MOUSOLEO COMPUESTO");
        tab_tipo.getColumna("IDE_CATASTRO").setCombo("SELECT IDE_CATASTRO,DETALLE_LUGAR FROM CMT_CATASTRO A INNER JOIN CMT_LUGAR B ON A.IDE_LUGAR=B.IDE_LUGAR  where DETALLE_LUGAR='SITIO' ORDER BY DETALLE_LUGAR+'-'+convert(varchar,NUMERO)");
        tab_tipo.getColumna("IDE_CATASTRO").setVisible(false);
        tab_tipo.getColumna("detalle_lugar").setLectura(true);
        tab_tipo.getColumna("bloque").setLectura(true);
        tab_tipo.getColumna("NIVEL").setLectura(true);
//        tab_tipo.getColumna("MODULO").setLectura(true);
//        tab_tipo.getColumna("UBICACION").setLectura(true);
        tab_tipo.dibujar();
        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_tipo);

        Division div = new Division();
        div.dividir2(tabp, tabp1, "40%", "h");
        agregarComponente(div);
    }

    public void buscaRegistro() {
        setRegistros.dibujar();
    }

    public void actualizaCatastroaMausoleo() {
        System.out.println("tab_categoria.getValor(\"DESCRIPCION_MAU\")<<<<<<<<" + tab_categoria.getValor("DESCRIPCION_MAU"));
        TablaGenerica tabDato = cementerioM.consultaCatastro(Integer.parseInt(setRegistros.getValorSeleccionado()));
        System.out.println("IDE_CATASTRO<<<<<<<<" + tabDato.getValor("IDE_CATASTRO"));

        cementerioM.set_updateCatastroMausoleo(tab_categoria.getValor("DESCRIPCION_MAU"), tabDato.getValor("IDE_CATASTRO"));
        utilitario.agregarMensaje("Nombre de Mausoleo Registrado en Catastro", "");
        utilitario.addUpdate("tab_tipo");
    }

    public void consultaCatastro() {
        String tabdato1 = cementerioM.consultaCatastroDuplicadoMausoleoCompuesto(Integer.parseInt(setRegistros.getValorSeleccionado()), Integer.parseInt(tab_categoria.getValor("IDE_MAU_COMP")));
        if (tabdato1.equals("0")) {
            tab_tipo.insertar();
            TablaGenerica tabDato = cementerioM.consultaCatastro(Integer.parseInt(setRegistros.getValorSeleccionado()));
            if (!tabDato.isEmpty()) {
                tab_tipo.setValor("IDE_CATASTRO ", tabDato.getValor("IDE_CATASTRO"));
                tab_tipo.setValor("DETALLE_LUGAR ", tabDato.getValor("DETALLE_LUGAR"));
                tab_tipo.setValor("BLOQUE", tabDato.getValor("SECTOR"));
                tab_tipo.setValor("NIVEL", tabDato.getValor("NUMERO"));
//                tab_tipo.setValor("MODULO", tabDato.getValor("MODULO"));
//                tab_tipo.setValor("UBICACION", tabDato.getValor("UBICACION"));
                setRegistros.cerrar();
                actualizaCatastroaMausoleo();
                utilitario.addUpdate("tab_tipo");
            } else {
                utilitario.agregarMensajeError("Datos", "No Se Encuentra Registrado");
            }
            guardar();
        } else {
            utilitario.agregarMensajeError("Registro No Puede Ser Guardado", "Ya existe datos registrados con esas descripciones");
        }
    }

    public void mostrarDatos() {
        if (cmbTipo.getValue() != null && cmbTipo.getValue().toString().isEmpty() == false) {
            setRegistros.getTab_seleccion().setSql(" select TOP 10 IDE_CATASTRO,DETALLE_LUGAR,SECTOR,NUMERO from CMT_CATASTRO a, cmt_lugar b\n"
                    + "where a.ide_lugar=b.ide_lugar AND DISPONIBLE_OCUPADO='DISPONIBLE' and  a.IDE_lugar ='" + cmbTipo.getValue() + "'");
            setRegistros.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar un valor en el texto", "");
        }
    }

    public void buscarMausoleoCompuesto(SelectEvent evt) {
        aut_busca.onSelect(evt);
        if (aut_busca.getValor() != null) {
            tab_categoria.setFilaActual(aut_busca.getValor());
            utilitario.addUpdate("tab_categoria");
        }
    }

    public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {

        tab_categoria.guardar();
        tab_tipo.guardar();
        guardarPantalla();

    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
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

    public SeleccionTabla getSetRegistros() {
        return setRegistros;
    }

    public void setSetRegistros(SeleccionTabla setRegistros) {
        this.setRegistros = setRegistros;
    }
}

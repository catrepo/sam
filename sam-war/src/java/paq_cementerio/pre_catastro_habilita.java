/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cementerio;

import framework.aplicacion.TablaGenerica;
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
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.Date;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_cementerio.ejb.cementerio;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author l-suntaxi
 */
public class pre_catastro_habilita extends Pantalla {

    private Tabla tab_categoria = new Tabla();
    private Tabla tab_tipo = new Tabla();
    private SeleccionTabla setRegistros = new SeleccionTabla();
    private AutoCompletar aut_busca = new AutoCompletar();
    private Texto txtDebe = new Texto();
    private Texto txtHaber = new Texto();
    private Panel panOpcion = new Panel();
    private Dialogo diaRegistro = new Dialogo();
    private Grid gridre = new Grid();
    private Grid gridRe = new Grid();
    private Texto txtId = new Texto();
    private Texto txtFecha = new Texto();
    private Texto txtCedula = new Texto();
    private Texto txtNombre = new Texto();
    private Texto txtCedulaR = new Texto();
    private Texto txtNombreR = new Texto();
    private Texto txtDireccion = new Texto();
    private Texto txtTelefono = new Texto();
    private Texto txtCelular = new Texto();
    private Texto txtMail = new Texto();
    private Texto txtNumL = new Texto();
    private Etiqueta etiNumL = new Etiqueta("NUMERO LIQUIDACION : ");
    private Etiqueta etiMail = new Etiqueta("E-MAIL : ");
    private Etiqueta etiCedula = new Etiqueta("CÉDULA FALLECIDO : ");
    private Etiqueta etiNombre = new Etiqueta("NOMBRE FALLECIDO: ");
    private Etiqueta etiFecha = new Etiqueta("FECHA DEFUNCION : ");
    private Etiqueta etiCedulaR = new Etiqueta("CEDULA REPRESENTANTE:");
    private Etiqueta etiNombreR = new Etiqueta("NOMBRE REPRESENTANTE:");
    private Etiqueta etiDireccion = new Etiqueta("DIRECCION REPRESENTANTE:");
    private Etiqueta etiTelefono = new Etiqueta("TELÉFONO : ");
    private Etiqueta etiCelular = new Etiqueta("CELULAR : ");
    private Combo cmbTipo = new Combo();
    @EJB
    private cementerio cementerioM = (cementerio) utilitario.instanciarEJB(cementerio.class);

    public pre_catastro_habilita() {
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logoactual.png");
        agregarComponente(quinde);
        bar_botones.quitarBotonsNavegacion();

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("");
        agregarComponente(panOpcion);


        aut_busca.setId("aut_busca");
        aut_busca.setAutoCompletar("SELECT IDE_LUGAR,DETALLE_LUGAR FROM CMT_LUGAR");
        aut_busca.setMetodoChange("buscarPersona");
        aut_busca.setSize(70);
        bar_botones.agregarComponente(new Etiqueta("Buscador:"));
        bar_botones.agregarComponente(aut_busca);


//        Boton bot_busca = new Boton();
//        bot_busca.setValue("Busqueda Disponibles");
//        bot_busca.setExcluirLectura(true);
//        bot_busca.setIcon("ui-icon-search");
//        bot_busca.setMetodo("buscaRegistro");
//        bar_botones.agregarBoton(bot_busca);

        cmbTipo.setId("cmbTipo");
        cmbTipo.setCombo("select distinct (case when disponible_ocupado='DISPONIBLE' THEN 'DISPONIBLE' ELSE 'OCUPADO' END) AS ID, disponible_ocupado from cmt_catastro");

        //Ingreso y busqueda de solicitudes 
        Grid gri_busca = new Grid();
        gri_busca.setColumns(2);
        Boton bot_save = new Boton();
        bot_save.setValue("Habilitar");
        bot_save.setExcluirLectura(true);
        bot_save.setIcon("ui-icon-disk");
        bot_save.setMetodo("mostrarRegistro");
        bar_botones.agregarBoton(bot_save);

        Boton bot_sa = new Boton();
        bot_sa.setValue("Desabilitar");
        bot_sa.setExcluirLectura(true);
        bot_sa.setIcon("ui-icon-disk");
        bot_sa.setMetodo("mostrarRegistro1");
        bar_botones.agregarBoton(bot_sa);

        Boton bot_buscar = new Boton();
        bot_buscar.setValue("Buscar");
        bot_buscar.setIcon("ui-icon-search");
        bot_buscar.setMetodo("mostrarDatos");
        bar_botones.agregarBoton(bot_buscar);

        gri_busca.getChildren().add(cmbTipo);
        gri_busca.getChildren().add(bot_buscar);

        setRegistros.setId("setRegistros");
        setRegistros.setSeleccionTabla("select top 10 IDE_CATASTRO,DETALLE_LUGAR,SECTOR,NUMERO,MODULO from CMT_CATASTRO a, cmt_lugar b\n"
                + "where a.ide_lugar=b.ide_lugar AND DISPONIBLE_OCUPADO='DISPONIBLE' ORDER BY DETALLE_LUGAR", "IDE_CATASTRO");
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
        diaRegistro.setWidth("50%"); //siempre en porcentajes  ancho
        diaRegistro.setHeight("50%");//siempre porcentaje   alto
        diaRegistro.setResizable(false); //para que no se pueda cambiar el tamaño
        diaRegistro.getBot_aceptar().setDisabled(true);
        diaRegistro.getBot_cancelar().setMetodo("regresaForma");
        gridre.setColumns(4);
        agregarComponente(diaRegistro);
        dibujarPantalla();

    }

//    public void buscaRegistro() {
//        setRegistros.dibujar();
//    }
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
//           if (aut_busca.getValue() == null) {
//            tab_categoria.setCondicion("IDE_LUGAR=-1");
//        } else {
//            tab_categoria.setCondicion("IDE_LUGAR=" + aut_busca.getValor());
//        }
        tab_categoria.getColumna("detalle_lugar").setMetodoChange("buscaValor");
        tab_categoria.getColumna("VALOR").setFormatoNumero(2);

        tab_categoria.getGrid().setColumns(4);
        tab_categoria.setTipoFormulario(true);
        tab_categoria.agregarRelacion(tab_tipo);
        tab_categoria.dibujar();
        PanelTabla tabp = new PanelTabla();
        tabp.setPanelTabla(tab_categoria);

        tab_tipo.setId("tab_tipo");
        tab_tipo.setTabla("CMT_CATASTRO", "IDE_CATASTRO", 2);
        tab_tipo.setHeader("UBICACION");
        tab_tipo.getColumna("DISPONIBLE_OCUPADO").setValorDefecto("DISPONIBLE");
        tab_tipo.getColumna("DISPONIBLE_OCUPADO").setLectura(true);
        tab_tipo.getColumna("IDE_CATASTRO").setOnClick("abrirFallecido");
        tab_tipo.getColumna("ver").setCheck();
//        tab_tipo.getColumna("ver").setMetodoChange("mostrarRegistro");
        tab_tipo.getColumna("ver").setVisible(false);
        tab_tipo.getColumna("habilita").setVisible(false);
        tab_tipo.getColumna("catastro_habilita").setVisible(false);
        tab_tipo.getColumna("SECTOR").setMetodoChange("validaBloque");
        tab_tipo.getColumna("NUMERO").setMetodoChange("validaBloquel");
        tab_tipo.getColumna("DISPONIBLE_OCUPADO").setFiltro(true);
        tab_tipo.getColumna("SECTOR").setFiltro(true);
        tab_tipo.getColumna("NUMERO").setFiltro(true);
        tab_tipo.getColumna("MODULO").setFiltro(true);
        tab_tipo.getColumna("SECTOR").setLongitud(25);
        tab_tipo.getColumna("NUMERO").setLongitud(25);
        tab_tipo.getColumna("DISPONIBLE_OCUPADO").setLongitud(25);
        tab_tipo.getColumna("TOTAL_INGRESA").setLongitud(30);
        tab_tipo.setCampoOrden("SECTOR desc");
        tab_tipo.getColumna("TOTAL_INGRESA").setValorDefecto("0");
        tab_tipo.dibujar();

        PanelTabla tabp1 = new PanelTabla();
        tabp1.setPanelTabla(tab_tipo);


        txtDebe.setId("txtDebe");
        txtDebe.setSize(8);
//        txtDebe.setValue(tab_categoria.getValor("IDE_LUGAR"));
        txtDebe.setValue(cementerioM.getCuentaDisponible(tab_categoria.getValor("IDE_LUGAR")));
        txtHaber.setId("txtHaber");
        txtHaber.setSize(8);
        txtHaber.setValue(cementerioM.getCuentaOcupado(tab_categoria.getValor("IDE_LUGAR")));
        Grid gri_busca = new Grid();
        gri_busca.setColumns(8);

        gri_busca.getChildren().add(new Etiqueta("Disponibles: "));
        gri_busca.getChildren().add(txtDebe);
        gri_busca.getChildren().add(new Etiqueta("Ocupados: "));
        gri_busca.getChildren().add(txtHaber);

        Division div = new Division();
        div.setId("divTablas");
        div.dividir3(tabp, gri_busca, tabp1, "30%", "61%", "H");
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        panOpcion.getChildren().add(gru);
    }

    public void mostrarRegistro() {

        int ban = 0;
        TablaGenerica tabDato = cementerioM.consultaCatastro(Integer.parseInt(tab_tipo.getValor("IDE_CATASTRO")));
        if (!tabDato.isEmpty()) {
            String fechActual = utilitario.getFechaActual();
            Date fecha = utilitario.DeStringADateformato2(fechActual);

            String fechRenvFallecido = tabDato.getValor("fecha_hasta");
            String fechFallecido = tabDato.getValor("fecha_fallecido");
            Date fecha1 = utilitario.DeStringADateformato2(fechRenvFallecido);
            int dias = utilitario.getDiferenciasDeFechas(fecha1, fecha);
            int anio = utilitario.getEdad(fechFallecido);


            if (dias <= 0) {
                if (anio > Integer.parseInt(tabDato.getValor("periodo"))) {
                    ban = 1;
                } else {
                    ban = 2;
                }
            } else {
                if (tabDato.getValor("catastro_habilita") == null) {
                    if (anio > Integer.parseInt(tabDato.getValor("periodo"))) {
                        ban = 1;
                    } else {
                        ban = 2;
                    }
                } else {
                    if (tabDato.getValor("catastro_habilita").equals("1")) {
                        if (anio > Integer.parseInt(tabDato.getValor("periodo"))) {
                            ban = 1;
                        } else {
                            ban = 2;
                        }
                    } else {
                        ban = 2;
                    }
                }
            }
            if (ban == 1) {
                System.out.println("tab_tipo.getValo     " + tab_tipo.getValor("IDE_CATASTRO"));
                cementerioM.set_updateHabilita(Integer.parseInt(tab_tipo.getValor("IDE_CATASTRO")));
                utilitario.addUpdateTabla(tab_tipo, "catastro_habilita", "");
                utilitario.agregarMensajeInfo("Catastro Habilitado", "");
            } else {
                utilitario.agregarMensajeError("Periodo de Exhumacion no corresponde",null);
            }
        }
    }

    public void mostrarRegistro1() {
        System.out.println("tab_tipo.getValo     " + tab_tipo.getValor("IDE_CATASTRO"));
        cementerioM.set_updateDesHabilita(Integer.parseInt(tab_tipo.getValor("IDE_CATASTRO")));
        utilitario.addUpdateTabla(tab_tipo, "catastro_habilita", "");

        utilitario.agregarMensajeInfo("Catastro Desabilitado", "");
    }

    public void mostrarRegistro2() {
        if (tab_tipo.getValor("ver") != null && tab_tipo.getValor("DISPONIBLE_OCUPADO").equals("OCUPADO") && tab_tipo.getValor("ver").toString().isEmpty() == false) {

            System.out.println("tab_tipo.getValor(\"IDE_CATASTRO\")<<<<<<<<<entro ak      " + tab_tipo.getValor("IDE_CATASTRO"));
            TablaGenerica tabDato = cementerioM.getFormulario(Integer.parseInt(tab_tipo.getValor("IDE_CATASTRO")));
            if (!tabDato.isEmpty()) {
                diaRegistro.Limpiar();
                diaRegistro.setDialogo(gridre);
                Grid griMostrar = new Grid();
                griMostrar.getChildren().clear();
                griMostrar.setColumns(2);
                txtNumL.setSize(10);
                griMostrar.getChildren().add(etiNumL);
                griMostrar.getChildren().add(txtNumL);
                txtFecha.setSize(10);
                griMostrar.getChildren().add(etiFecha);
                griMostrar.getChildren().add(txtFecha);
                txtCedula.setSize(10);
                griMostrar.getChildren().add(etiCedula);
                griMostrar.getChildren().add(txtCedula);
                txtNombre.setSize(38);
                griMostrar.getChildren().add(etiNombre);
                griMostrar.getChildren().add(txtNombre);
                txtCedulaR.setSize(10);
                griMostrar.getChildren().add(etiCedulaR);
                griMostrar.getChildren().add(txtCedulaR);
                txtNombreR.setSize(60);
                griMostrar.getChildren().add(etiNombreR);
                griMostrar.getChildren().add(txtNombreR);
                txtDireccion.setSize(60);
                griMostrar.getChildren().add(etiDireccion);
                griMostrar.getChildren().add(txtDireccion);
                txtTelefono.setSize(10);
                griMostrar.getChildren().add(etiTelefono);
                griMostrar.getChildren().add(txtTelefono);
                txtCelular.setSize(10);
                griMostrar.getChildren().add(etiCelular);
                griMostrar.getChildren().add(txtCelular);
                txtMail.setSize(30);
                griMostrar.getChildren().add(etiMail);
                griMostrar.getChildren().add(txtMail);
                txtNumL.setSize(30);
                griMostrar.getChildren().add(etiNumL);
                griMostrar.getChildren().add(txtNumL);

                gridre.getChildren().add(griMostrar);

                Grid griBotones = new Grid();
                griBotones.getChildren().clear();
                griBotones.setColumns(2);

                gridRe.getChildren().add(griBotones);
                diaRegistro.setDialogo(gridRe);
                diaRegistro.dibujar();
                txtId.setValue(tabDato.getValor("ide_catastro") + "");
                txtNumL.setValue(tabDato.getValor("NUM_LIQUIDACION") + "");

                txtCedula.setValue(tabDato.getValor("CEDULA_FALLECIDO") + "");
                txtNombre.setValue(tabDato.getValor("NOMBRES") + "");
                txtFecha.setValue(tabDato.getValor("FECHA_DEFUNCION") + "");
                txtCedulaR.setValue(tabDato.getValor("DOCUMENTO_IDENTIDAD_CMREP") + "");
                txtNombreR.setValue(tabDato.getValor("NOMBRES_APELLIDOS_CMREP") + "");
                txtDireccion.setValue(tabDato.getValor("DIRECCION_CMREP") + "");
                txtTelefono.setValue(tabDato.getValor("TELEFONOS_CMREP") + "");
                txtCelular.setValue(tabDato.getValor("CELULAR_CMREP") + "");
                txtMail.setValue(tabDato.getValor("EMAIL_CMREP") + "");

                utilitario.addUpdate("txtNumL");
                utilitario.addUpdate("txtCedula");
                utilitario.addUpdate("txtNombre");
                utilitario.addUpdate("txtFecha");

                utilitario.addUpdate("txtCedulaR");
                utilitario.addUpdate("txtNombreR");
                utilitario.addUpdate("txtDireccion");
                utilitario.addUpdate("txtTelefono");
                utilitario.addUpdate("txtCelular");
                utilitario.addUpdate("txtMail");

            } else {
                utilitario.agregarMensajeInfo("No se encuentra registro", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Lugar se encuentra disponible", "No se puede visualizar datos");
        }
    }

    public void regresaForma() {
        diaRegistro.cerrar();
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

        txtDebe.setValue("0");
        txtHaber.setValue("0");
        utilitario.addUpdate("txtDebe");
        utilitario.addUpdate("txtHaber");
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void actualizar() {
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
        aut_busca.actualizar();
        aut_busca.setSize(70);
        utilitario.addUpdate("aut_busca");
    }

    public void buscarPersona(SelectEvent evt) {
        aut_busca.onSelect(evt);
        System.out.println("aut_busca.getValor()" + aut_busca.getValor());

        if (aut_busca.getValor() != null) {
            System.out.println("aut_busc" + aut_busca.getValor());
            tab_categoria.setFilaActual(aut_busca.getValor());
//             String disponible=cementerioM.getCuentaDisponible(aut_busca.getLabel());
            String disponible = cementerioM.getCuentaDisponible(aut_busca.getValor());
            txtDebe.setValue(disponible);
            String ocupado = cementerioM.getCuentaOcupado(aut_busca.getValor());
            txtHaber.setValue(ocupado);
            utilitario.addUpdate("tab_categoria,txtDebe,txtHaber");
        }
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

    @Override
    public void guardar() {

        if (tab_categoria.isFocus()) {
            System.out.println("entre a guardar");
            tab_categoria.guardar();
            guardarPantalla();
        }
//        if (tab_tipo.isFocus()) {
//            System.out.println("entre a guardar");
//            tab_tipo.guardar();
//            guardarPantalla();
//        }
        if (tab_tipo.isFocus()) {
            String tabDato = cementerioM.consultaCatastroDuplicado(Integer.parseInt(tab_tipo.getValor("MODULO")), tab_tipo.getValor("SECTOR"), tab_tipo.getValor("NUMERO"), tab_categoria.getValor("DETALLE_LUGAR"), tab_categoria.getValor("UBICACION"));
            String tabDato1 = cementerioM.consultaCatastroDuplicadoMausoleo(Integer.parseInt(tab_tipo.getValor("MODULO")), tab_categoria.getValor("DETALLE_LUGAR"));
            System.out.println("tab_categoria.getValor(\"IDE_LUGAR\")" + tab_categoria.getValor("IDE_LUGAR"));
            if (!tab_categoria.getValor("DETALLE_LUGAR").equals("MAUSOLEO") && tabDato.equals("0")) {
                tab_categoria.guardar();
                tab_tipo.guardar();
                guardarPantalla();
            } else if (tabDato1.equals("0") && tab_categoria.getValor("DETALLE_LUGAR").equals("MAUSOLEO")) {
                tab_categoria.guardar();
                tab_tipo.guardar();
                guardarPantalla();
            } else {
                utilitario.agregarMensajeError("Registro No Puede Ser Guardado", "Ya existe datos registrados con esas descripciones");
            }
        }
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
}

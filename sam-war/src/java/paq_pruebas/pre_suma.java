/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;

import framework.componentes.Boton;
import framework.componentes.Grid;
import framework.componentes.Texto;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_suma extends Pantalla {

    private Texto tex_numero1 = new Texto();
    private Texto tex_numero2 = new Texto();
    private Texto tex_resultado = new Texto();

    public pre_suma() {
        tex_numero1.setSoloNumeros(); //Hace que se ingresen cantidades numericas
        tex_numero2.setSoloNumeros(); //Hace que se ingresen cantidades numericas        
        tex_resultado.setDisabled(true); //Desactiva el cuadro de texto

        Boton bot_suma = new Boton();
        bot_suma.setValue("SUMAR");
        bot_suma.setMetodo("sumarNumeros");

        Grid grid = new Grid();
        grid.setColumns(4);
        grid.getChildren().add(tex_numero1);
        grid.getChildren().add(tex_numero2);

        tex_resultado.setId("tex_resultado");

        grid.getChildren().add(tex_resultado);
        grid.getChildren().add(bot_suma);

        agregarComponente(grid);


        ///MENSAJES

        Boton bot1 = new Boton();
        bot1.setValue("MENSAJE ");
        bot1.setMetodo("agregarMensaje");
        bar_botones.agregarBoton(bot1);


        Boton bot2 = new Boton();
        bot2.setValue("MENSAJE INFO");
        bot2.setMetodo("agregarMensajeInf");
        bar_botones.agregarBoton(bot2);

        Boton bot3 = new Boton();
        bot3.setValue("MENSAJE ERROR");
        bot3.setMetodo("agregarMensajeError");
        bar_botones.agregarBoton(bot3);


        //NOTIFICACIONES

        Boton bot4 = new Boton();
        bot4.setValue("MOTIFICACIÓN");
        bot4.setMetodo("agregarNotificacion");
        bar_botones.agregarBoton(bot4);


    }

    public void agregarNotificacion() {
        utilitario.agregarNotificacionInfo("TITULO DE LA NOTIFICACIÓN", "Detalle");
    }

    public void agregarMensaje() {
        utilitario.agregarMensaje("TITULO", "Mensaje Normal");
    }

    public void agregarMensajeInf() {
        utilitario.agregarMensajeInfo("TITULO", "Mensaje Info");
    }

    public void agregarMensajeError() {
        utilitario.agregarMensajeError("TITULO", "Mensaje Error");
    }

    public void sumarNumeros() {
        //Sumar el texto 1 y texto 2
        double dou_num1;
        double dou_num2;
        double dou_resultado = 0;

        try {
            dou_num1 = Double.parseDouble(tex_numero1.getValue() + "");
            dou_num2 = Double.parseDouble(tex_numero2.getValue() + "");
            dou_resultado = dou_num1 + dou_num2;
        } catch (Exception e) {
        }

        tex_resultado.setValue(dou_resultado + "");
        utilitario.addUpdate("tex_resultado"); //Actualiza la pantalla

    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }
}

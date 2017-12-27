/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_adquisicion.ejb;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
/**
/**
 *
 * @author Andres
 */
@Stateless
public class ServiciosAdquisiones {
           /**
     * Retorna los tipos de aprobaciones que existen
     *
     * @return sql de aprobado
     */
    public String getAprobado() {
        String sql="";
        sql="SELECT IDE_ADAPRO, DETALLE_ADAPRO FROM ADQ_APROBADO order by DETALLE_ADAPRO";
        return sql;
    }
    public String getTipoArea() {
        String sql="";
        sql="select IDE_ADTIAR, DETALLE_ADTIAR from ADQ_TIPO_AREA";
        return sql;
    }
    public String getGrupoMaterial() {
        String sql="";
        sql="SELECT IDE_ADGRMA, CODIGO_ADGRMA, DETALLE_ADGRMA FROM ADQ_GRUPO_MATERIAL order by DETALLE_ADGRMA";
        return sql;
    }
    public String getPartidaPresupuestaria() {
        String sql="";
        sql="SELECT IDE_ADPAPR, CODIGO_ADPAPR, DETALLE_ADPAPR FROM ADQ_PARTIDA_PRESUPUESTARIA order by CODIGO_ADPAPR";
        return sql;
    }
    public String getAreaPartida() {
        String sql="";
        sql="select ide_adarpa,codigo_adarpa,detalle_adpapr from ADQ_AREA_PARTIDA a,ADQ_PARTIDA_PRESUPUESTARIA b where a.IDE_ADPAPR = b.IDE_ADPAPR order by codigo_adpapr";
        return sql;
    }
    public String getPartidaMaterial() {
        String sql="";
        sql="select ide_adpama,CODIGO_ADPAMA,DETALLE_ADMATE\n" +
            "from ADQ_PARTIDA_MATERIAL a, ADQ_MATERIAL b\n" +
            "where a.IDE_ADMATE=b.IDE_ADMATE\n" +
            "order by CODIGO_ADPAMA";
        return sql;
    }
     public String getMaterial() {
        String sql="";
        sql="select IDE_ADMATE,CODIGO_ADMATE,DETALLE_ADMATE\n" +
            "from ADQ_MATERIAL " +
            "order by CODIGO_ADMATE";
        return sql;
    }
    public String getAreaAdministrativa() {
        String sql="";
        sql="SELECT IDE_ADARAD, CODIGO_ADARAD, DETALLE_ADARAD FROM ADQ_AREA_ADMINISTRATIVA";
        return sql;
    }
    public String getEmpleado() {
        String sql="";
        sql="SELECT IDE_ADEMPLE, CEDULA_ADEMPLE, NOMBRES_ADEMPLE FROM ADQ_EMPLEADO ORDER BY NOMBRES_ADEMPLE";
        return sql;
    }
    public String getEmpleadoDepartamento(String tipo,String empleado, String estado, String departamento) {
        String sql="select ide_ademde,CEDULA_ADEMPLE,NOMBRES_ADEMPLE from ADQ_EMPLEADO_DEPARTAMENTO a,ADQ_EMPLEADO b where a.IDE_ADEMPLE = b.IDE_ADEMPLE ";
        if(tipo.equals("1")){
            sql +=" and ide_ademde in ("+empleado+")";
        }
        if(tipo.equals("2")){
            sql +=" and ACTIVO_ADEMDE in ("+estado+") and IDE_ADEMDE ="+departamento;
        }
        sql+=" order by NOMBRES_ADEMPLE";
        return sql;
    }
    public String getEmpleadoAprueba(String tipo,String empleado, String estado, String departamento) {
        String sql="SELECT IDE_ADEMAP,CEDULA_ADEMPLE,NOMBRES_ADEMPLE FROM ADQ_EMPLEADO_APRUEBA a, ADQ_EMPLEADO b WHERE a.IDE_ADEMPLE = b.IDE_ADEMPLE ";
        if(tipo.equals("1")){
            sql +=" and IDE_ADEMAP in ("+empleado+")";
        }
        if(tipo.equals("2")){
            sql +=" and ACTIVO_ADEMAP in ("+estado+") and IDE_ADEMAP ="+departamento;
        }
        sql+=" order by NOMBRES_ADEMPLE";
        return sql;
    }
    
    public String getCargo() {
        String sql="";
        sql="SELECT IDE_ADCARG, DETALLE_ADCARG FROM ADQ_CARGO";
        return sql;
    }
    public String getTipoAprobador() {
        String sql="";
        sql="SELECT IDE_ADTIAP, DETALLE_ADTIAP FROM ADQ_TIPO_APROBADOR";
        return sql;
    }
    public String getTipoDenominacion() {
        String sql="";
        sql="select IDE_ADTIDE, DETALLE_ADTIDE from ADQ_TIPO_DENOMINACION";
        return sql;
    }
    public String getUsuario(String activo) {
        String sql="";
        sql="SELECT IDE_USUA, NICK_USUA, NOM_USUA FROM SIS_USUARIO WHERE ACTIVO_USUA in ("+activo+")";
        return sql;
    }
    public String getUsuarioSistema(String ide_usua,String activo,String tipo_aprobador) {
        String sql="";
        sql="select ide_ademde,NOMBRES_ADEMPLE,CEDULA_ADEMPLE from ADQ_EMPLEADO_DEPARTAMENTO a, ADQ_EMPLEADO b where a.IDE_ADEMPLE=b.IDE_ADEMPLE and ACTIVO_ADEMDE = "+activo+" and IDE_ADTIAP = "+tipo_aprobador+" and IDE_USUA ="+ide_usua;
        return sql;
    }   
    public String getUsuarioSistemaAprobador(String ide_usua,String activo,String tipo_aprobador) {
        String sql="";
        sql="select ide_ademap,NOMBRES_ADEMPLE,CEDULA_ADEMPLE from ADQ_EMPLEADO_APRUEBA a, ADQ_EMPLEADO b where a.IDE_ADEMPLE=b.IDE_ADEMPLE and ACTIVO_ADEMAP = "+activo+"  and IDE_USUA ="+ide_usua;
        return sql;
    }      
}

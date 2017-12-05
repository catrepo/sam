
package paq_webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BusquedaPorRucResult" type="{http://ruminahui.gob.ec/}ClassCiudadania" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "busquedaPorRucResult"
})
@XmlRootElement(name = "BusquedaPorRucResponse")
public class BusquedaPorRucResponse {

    @XmlElement(name = "BusquedaPorRucResult")
    protected ClassCiudadania busquedaPorRucResult;

    /**
     * Obtiene el valor de la propiedad busquedaPorRucResult.
     * 
     * @return
     *     possible object is
     *     {@link ClassCiudadania }
     *     
     */
    public ClassCiudadania getBusquedaPorRucResult() {
        return busquedaPorRucResult;
    }

    /**
     * Define el valor de la propiedad busquedaPorRucResult.
     * 
     * @param value
     *     allowed object is
     *     {@link ClassCiudadania }
     *     
     */
    public void setBusquedaPorRucResult(ClassCiudadania value) {
        this.busquedaPorRucResult = value;
    }

}

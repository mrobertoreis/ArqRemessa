//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.0 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.08.03 at 12:27:56 AM GMT-03:00 
//

package br.com.nordestefomento.jrimum.texgit.language;

import javax.xml.bind.annotation.XmlEnum;

/**
 * 
 * 
 * <p>
 * Basicamente s�o defini��es de dire��es de escrita e leitura.
 * </p>
 * 
 * 
 * 
 * <p>
 * Java class for enumSide.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name=&quot;enumSide&quot;&gt;
 *   &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *     &lt;enumeration value=&quot;LEFT&quot;/&gt;
 *     &lt;enumeration value=&quot;RIGHT&quot;/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlEnum
public enum EnumSide {

	LEFT, RIGHT;

	public String value() {
		return name();
	}

	public static EnumSide fromValue(String v) {
		return valueOf(v);
	}

}

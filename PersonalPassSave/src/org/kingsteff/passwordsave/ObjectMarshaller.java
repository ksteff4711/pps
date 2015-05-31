package org.kingsteff.passwordsave;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class ObjectMarshaller {

	
	
	private XStream xstream = new XStream(new StaxDriver());
	
	

	public String toXmlWithXStream(Object incoming) {
		return xstream.toXML(incoming);
		
		
	}

	public Object fromXmlWithXStream(String xml) {
		
		return xstream.fromXML(xml);
	}

	public Object fromXml(String incoming) {
		
		return xstream.fromXML(incoming);
	}

}

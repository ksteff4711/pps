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

	private String toXml(Object input) {
		try {
			final JAXBContext context = JAXBContext.newInstance(input
					.getClass());
			final Marshaller marshaller = context.createMarshaller();
			String resultString = new String();

			// Marshal the javaObject and write the XML to the stringWriter

			StringWriter st = new StringWriter();
			marshaller.marshal(input, st);
			resultString = st.toString();
			return resultString;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public String toXmlWithXStream(Object incoming) {
		XStream xstream = new XStream(new StaxDriver());
		String xml = xstream.toXML(incoming);
		return xml;
	}

	public Object fromXmlWithXStream(String xml) {
		XStream xstream = new XStream(new StaxDriver());
		return xstream.fromXML(xml);
	}

	private Object fromXml(String xml, Class objectClass) {
		try {
			final JAXBContext context = JAXBContext.newInstance(objectClass);
			final Unmarshaller unmarshaller = context.createUnmarshaller();

			// Unmarshal the XML in the stringWriter back into an object
			final Object javaObject2 = (Object) unmarshaller
					.unmarshal(new StringReader(xml));

			// Print out the contents of the JavaObject we just unmarshalled
			// from
			// the XML
			return javaObject2;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}

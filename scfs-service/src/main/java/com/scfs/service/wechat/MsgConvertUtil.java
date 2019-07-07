package com.scfs.service.wechat;

import com.scfs.domain.wechat.domain.TextMessage;
import org.apache.commons.io.IOUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by Administrator on 2016/12/7.
 */
public class MsgConvertUtil {

	public static String toXml(TextMessage textMessage) throws Exception {
		if (textMessage == null)
			return "";

		JAXBContext context = JAXBContext.newInstance(TextMessage.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.setProperty(Marshaller.JAXB_FRAGMENT, true);

		StringWriter sw = new StringWriter();
		m.marshal(textMessage, sw);
		return sw.toString();
	}

	public static TextMessage toTextMessage(String xml) throws Exception {
		JAXBContext jaxbContext = JAXBContext.newInstance(TextMessage.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		StringReader reader = new StringReader(xml);
		TextMessage textMessage = (TextMessage) jaxbUnmarshaller.unmarshal(reader);
		IOUtils.closeQuietly(reader);
		return textMessage;
	}
}

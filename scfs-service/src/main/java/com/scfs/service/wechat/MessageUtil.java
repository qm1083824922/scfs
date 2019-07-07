package com.scfs.service.wechat;

import com.scfs.domain.wechat.domain.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/5.
 */
public class MessageUtil {
	/**
	 * 返回消息类型：文本
	 */
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 返回消息类型：音乐
	 */
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

	/**
	 * 返回消息类型：图文
	 */
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";

	/**
	 * 请求消息类型：文本
	 */
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 请求消息类型：图片
	 */
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * 请求消息类型：链接
	 */
	public static final String REQ_MESSAGE_TYPE_LINK = "link";

	/**
	 * 请求消息类型：地理位置
	 */
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

	/**
	 * 请求消息类型：音频
	 */
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

	/**
	 * 请求消息类型：推送
	 */
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	/**
	 * 事件类型：subscribe(订阅)
	 */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/**
	 * 事件类型：unsubscribe(取消订阅)
	 */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * 事件类型：CLICK(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_CLICK = "CLICK";

	/**
	 * @Description: 解析微信发来的请求（XML）
	 * @param @param
	 *            request
	 * @param @return
	 * @param @throws
	 *            Exception
	 * @author dapengniao
	 * @date 2016 年 3 月 7 日 上午 10:04:02
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
		// 将解析结果存储在 HashMap 中
		Map<String, String> map = new HashMap<String, String>();
		// 从 request 中取得输入流
		InputStream inputStream = request.getInputStream();
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到 xml 根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());

		// 释放资源
		inputStream.close();
		return map;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(String str) throws Exception {
		// 将解析结果存储在 HashMap 中
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		StringReader sr = new StringReader(str);
		Document document = reader.read(sr);
		Element root = document.getRootElement();
		List<Element> elementList = root.elements();
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}
		return map;
	}

	// public static void main(String[] args) {
	// try {
	// TextMessage text = new TextMessage();
	// text.setContent("你好、、<<<、、");
	// text.setFromUserName("aaaaa>a");
	// text.setToUserName("bbbb&b");
	// text.setMsgType("text");
	// File file = new File("D:\\file.xml");
	// JAXBContext jaxbContext = JAXBContext.newInstance(TextMessage.class);
	// Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	//
	// // output pretty printed
	// jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	// jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
	//
	// jaxbMarshaller.marshal(text, file);
	// jaxbMarshaller.marshal(text, System.out);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

	/**
	 * @Description: 文本消息对象转换成 xml
	 * @param @param
	 *            textMessage
	 * @param @return
	 */
	public static String textMessageToXml(String content, Map<String, String> map) {
		TextMessage textMessage = new TextMessage(map);
		textMessage.setContent(content);
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	/**
	 * @Description: 图文消息对象转换成 xml
	 * @param @param
	 *            newsMessage
	 * @param @return
	 */
	public static String newsMessageToXml(NewsMessage newsMessage) {
		xstream.alias("xml", newsMessage.getClass());
		// xstream.alias("item", new Article().getClass());
		return xstream.toXML(newsMessage);
	}

	/**
	 * @Description: 图片消息对象转换成 xml
	 * @param @param
	 *            imageMessage
	 * @param @return
	 */
	public static String imageMessageToXml(ImageMessage imageMessage) {
		xstream.alias("xml", imageMessage.getClass());
		return xstream.toXML(imageMessage);
	}

	/**
	 * 对象到 xml 的处理
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有 xml 节点的转换都增加 CDATA 标记
				boolean cdata = true;

				@SuppressWarnings("rawtypes")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
}

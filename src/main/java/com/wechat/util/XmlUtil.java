package com.wechat.util;

import java.util.Iterator;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.util.Assert;

/**
 * xml解析工具类
 */
public class XmlUtil {
	@SuppressWarnings("unchecked")
	public static JSONObject convertStringXmlToJson(String xml) {
		Assert.notNull(xml);
		Document doc = null;
		// 读取并解析XML文档
		// SAXReader就是一个管道，用一个流的方式，把xml文件读出来
		// SAXReader reader = new SAXReader(); //User.hbm.xml表示你要解析的xml文档
		// Document document = reader.read(new File("User.hbm.xml"));
		try {
			doc = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			e.printStackTrace();
		} 
		//将字符串转为XML
		Element rootElement = doc.getRootElement();//获取根节点
		JSONObject json = new JSONObject();
		for (Iterator<Element> it = rootElement.elementIterator(); it.hasNext();) {
			Element element = (Element) it.next();
			String name = element.getName();
			String text = element.getText();
			json.put(name, text);
		}
		return json;
		
	}
}

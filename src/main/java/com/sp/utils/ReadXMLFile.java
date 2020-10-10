package com.sp.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadXMLFile {

	public static List<HashMap<String, String>> parse(File file) {
		List<HashMap<String, String>> ret = new ArrayList<HashMap<String, String>>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			doc.getDocumentElement().normalize();
			//
			// System.out.println("Root element :" +
			// doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("batch");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				// System.out.println("\nCurrent Element :" +
				// nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					HashMap<String, String> hm = new HashMap<String, String>();
					hm.put("type", eElement.getAttribute("type"));
					try {
						hm.put("path", eElement.getElementsByTagName("path").item(0).getTextContent());
						hm.put("argument", eElement.getElementsByTagName("argument").item(0).getTextContent());
						hm.put("description", eElement.getElementsByTagName("desciption").item(0).getTextContent());
					} catch (Exception e) {
					}
					ret.add(hm);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

//	public static void main(String argv[]) {
//		parse(ConstantValue.CONFIG_BATCH_FILE);
//	}

}
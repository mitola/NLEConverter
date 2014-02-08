/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bttstudio.nleconverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Mitola
 */
public class Parser {

    private DocumentBuilderFactory builderFactory;
    private DocumentBuilder inputBuilder;
    private DocumentBuilder outputBuilder;
    private int max_x = 0;
    private int max_y = 0;

    //takes An input file and parses it to default AndEngine format
    public boolean parse(File input, File output) {
        try {
            builderFactory = DocumentBuilderFactory.newInstance();
            inputBuilder = builderFactory.newDocumentBuilder();
            outputBuilder = builderFactory.newDocumentBuilder();
            Document inputDocument = inputBuilder.parse(new FileInputStream(input));
            Document outputDocument = outputBuilder.newDocument();
            Element root = outputDocument.createElement("level");
            NodeList list = inputDocument.getElementsByTagName("Object");
            for (int i = 0; i < list.getLength(); i++) {
                Node n = list.item(i);

                NodeList children = n.getChildNodes();
                Element out = outputDocument.createElement("entity");
                for (int j = 0; j < children.getLength(); j++) {
                    Node n1 = children.item(j);
                    if (n1.getNodeName().equalsIgnoreCase("X")) {
                        out.setAttribute("x", n1.getTextContent());
                        try {
                            int x = Integer.valueOf(n1.getTextContent());
                            if (x > max_x) {
                                max_x = x;
                            }
                        } catch (NumberFormatException e) {
                        }
                    }
                    if (n1.getNodeName().equalsIgnoreCase("Y")) {
                        out.setAttribute("y", n1.getTextContent());
                        try {
                            int y = Integer.valueOf(n1.getTextContent());
                            if (y > max_y) {
                                max_y = y;
                            }
                        } catch (NumberFormatException e) {
                        }
                    }
                    if (n1.getNodeName().equalsIgnoreCase("TextureName")) {
                        out.setAttribute("type", n1.getTextContent());
                    }
                    if (n1.getNodeName().equalsIgnoreCase("ScaleX")) {
                        out.setAttribute("scaleX", n1.getTextContent());
                    }
                    if (n1.getNodeName().equalsIgnoreCase("ScaleY")) {
                        out.setAttribute("scaleY", n1.getTextContent());
                    }
                    if (n1.getNodeName().equalsIgnoreCase("Rotation")) {
                        out.setAttribute("rotation", n1.getTextContent());
                    }
                }
                root.appendChild(out);
            }
            root.setAttribute("width", (max_x + 256) + "");
            root.setAttribute("height", (max_y + 256) + "");
            outputDocument.appendChild(root);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(outputDocument);
            StreamResult result = new StreamResult(output);

            transformer.transform(source, result);
            return true;
        } catch (ParserConfigurationException | SAXException | IOException | DOMException | TransformerFactoryConfigurationError | TransformerException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }
}

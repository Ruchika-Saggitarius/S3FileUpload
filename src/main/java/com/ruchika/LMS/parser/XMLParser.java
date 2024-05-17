package com.ruchika.LMS.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

public class XMLParser {
    public static void main(String[] args) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse("src/main/resources/1.xml"); // path to the XML file
            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "/GoodreadsResponse/book"; // XPath expression
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element bookElement = (Element) node;

                    // get the values of 
                    String id = getElementValue(bookElement, "id");
                    String title = getElementValue(bookElement, "title");
                    String isbn = getElementValue(bookElement, "isbn");
                    String description = getElementValue(bookElement, "description");
                    String image_url = getElementValue(bookElement, "image_url");
                    int publication_year = Integer.parseInt(getElementValue(bookElement, "publication_year"));
                    String publisher = getElementValue(bookElement, "publisher");
                    String language_code = getElementValue(bookElement, "language_code");
                    String average_rating = getElementValue(bookElement, "average_rating");


                    NodeList authorNodeList = bookElement.getElementsByTagName("author");
                    String author = "";
                    if (authorNodeList.getLength() > 0) {
                        Element authorElement = (Element) authorNodeList.item(0);
                        author = getElementValue(authorElement, "name");
                    }


                    System.out.println("Book Id: " + id);
                    System.out.println("Title: " + title);
                    System.out.println("Author: " + author);
                    System.out.println("ISBN: " + isbn);
                    System.out.println("Description: " + description);
                    System.out.println("Image URL: " + image_url);
                    System.out.println("Publication Year: " + publication_year);
                    System.out.println("Publisher: " + publisher);
                    System.out.println("Language Code: " + language_code);
                    System.out.println("Average Rating: " + average_rating);
                    System.out.println();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getElementValue(Element parentElement, String tagName) {
        NodeList nodeList = parentElement.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node != null) {
                return node.getTextContent().trim();
            }
        }
        return "";
    }
}

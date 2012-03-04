package com.g4share.jSynch.config;

import com.g4share.jSynch.share.Constants;
import com.google.inject.Inject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;

/**
 * User: gm
 * Date: 3/3/12
 */
public class XmlFileReader implements XmlReader{
    private ConfigStore store;

    @Override
    @Inject
    public void setStore(ConfigStore configStore){
        this.store = configStore;
    }

    @Override
    public ConfigStore getStore() {
        return store;
    }

    /**
     * reads information stored in specified file
     * @param configFileName file name
     */
    @Override
    public Constants.Codes read(String configFileName){
        //open file
        File confFile = new File(configFileName);
        if(!confFile.exists()){
            store.ErrorOccurred("config file \"" + configFileName + "\" does not exists.");
            return Constants.Codes.FATAL_ERROR_CODE;
        }

        //create sax parser
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser;
        try {
            saxParser = factory.newSAXParser();
        } catch (ParserConfigurationException | SAXException ex) {
            store.ErrorOccurred("config cannot be opened.");
            return Constants.Codes.FATAL_ERROR_CODE;
        }


        //sax handler definition
        DefaultHandler handler = new DefaultHandler() {
            //store current xml node
            private EnumSet<XmlNode> currentPath = EnumSet.of(XmlNode.none);

            @Override
            public void startElement(String uri, String localName, String qName,
                                     Attributes attributes) throws SAXException {

                //parse qname as XmlNode
                XmlNode node = XmlNode.fromString(qName);
                if (node == XmlNode.none) return;

                //if node is defined
                if (node.isPathKey(currentPath)){
                    //if the node is the last in its defined path
                    HashMap<String, String> keyValues = new HashMap<>();

                    for(int i = 0; i < attributes.getLength(); i++){
                        //store each attribude
                        keyValues.put(attributes.getQName(i), attributes.getValue(i));
                    }

                    //add node to store
                    store.AddNode(node, keyValues);
                    return;
                }

                //change current xml node
                currentPath.add(node);
            }

            @Override
            public void endElement(String uri, String localName,
                                   String qName) throws SAXException {
                XmlNode node = XmlNode.fromString(qName);
                if (node == XmlNode.none) return;

                ///chane current xml node back
                currentPath.remove(node);
            }

            @Override
            public void characters(char ch[], int start, int length) throws SAXException { }

        };
        try {
            saxParser.parse(configFileName, handler);
        } catch (SAXException | IOException ex) {
            store.ErrorOccurred("error read config: possible wrong format.");
            return Constants.Codes.FATAL_ERROR_CODE;
        }

        return Constants.Codes.SUCCESS_CODE;
    }
}

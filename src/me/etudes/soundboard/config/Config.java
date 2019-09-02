package me.etudes.soundboard.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer.Info;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

public class Config {

    private static String path = System.getProperty("user.home") + "\\AppData\\Roaming\\SoundBoard\\config.xml";
    private static Config config = new Config();

    private Config() {
        if(!new File(path).exists()) {
            init();
        }
    }

    private void init() {
        String dir = System.getProperty("user.home") + "\\AppData\\Roaming\\SoundBoard";
        File dirFolder = new File(dir);
        if(!dirFolder.exists()) {
            dirFolder.mkdir();
        }
        File file = new File(path);
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("config/config.xml");
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static Config getConfig() {
        return config;
    }

    public Info getSelectedDevice() {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
            Element root = document.getDocumentElement();
            Element device = (Element) root.getElementsByTagName("device").item(0);
            String result = device.getElementsByTagName("name").item(0).getTextContent();
            return getDevice(result);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setDevice(Info info) {
        String name = info.getName();
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
            Element root = document.getDocumentElement();
            Element device = (Element) root.getElementsByTagName("device").item(0);
            Element element = (Element) device.getElementsByTagName("name").item(0);
            element.setTextContent(name);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(path));
            transformer.transform(source, result);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String getSoundName(int index) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
            Element root = document.getDocumentElement();
            Element sound = (Element) root.getElementsByTagName("sound").item(index);
            String result = sound.getElementsByTagName("name").item(0).getTextContent();
            if(result.equals("")) return null;
            else return result;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSoundPath(int index) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
            Element root = document.getDocumentElement();
            Element sound = (Element) root.getElementsByTagName("sound").item(index);
            String result = sound.getElementsByTagName("path").item(0).getTextContent();
            if(result.equals("")) return null;
            else return result;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSoundName(int index, String name) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
            Element root = document.getDocumentElement();
            Element sound = (Element) root.getElementsByTagName("sound").item(index);
            Element nameElement = (Element) sound.getElementsByTagName("name").item(0);
            nameElement.setTextContent(name);

            saveConfig(document);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setSoundPath(int index, String path) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(Config.path));
            Element root = document.getDocumentElement();
            Element sound = (Element) root.getElementsByTagName("sound").item(index);
            Element pathElement = (Element) sound.getElementsByTagName("path").item(0);
            pathElement.setTextContent(path);

            saveConfig(document);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDefaultChecked() {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
            Element root = document.getDocumentElement();
            Element defaultElement = (Element) root.getElementsByTagName("default-check").item(0);
            String value = defaultElement.getElementsByTagName("value").item(0).getTextContent();
            return value.equals("true");
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setDefaultChecked(boolean flag) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
            Element root = document.getDocumentElement();
            Element defaultElement = (Element) root.getElementsByTagName("default-check").item(0);
            Element value = (Element) defaultElement.getElementsByTagName("value").item(0);
            value.setTextContent(flag ? "true" : "false");
            saveConfig(document);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void saveConfig(Document document) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(path));
            transformer.transform(source, result);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private Info getDevice(String name) {
        Info[] devices = AudioSystem.getMixerInfo();
        return Arrays.stream(devices).filter(device -> device.getName().equals(name)).findFirst().orElse(null);
    }
}

package CommandLineInterface.Parsers;

import CommandLineInterface.Parsers.FileParser;
import PersonalCalendar.CalendarEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public abstract class XMLParser implements FileParser {

    //Members~~~~~~~~~~~~~~~~~~~~~~~~~~
    private File file;

    private HashSet<CalendarEvent> fileContent=null;

    //Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~
    protected abstract HashSet<CalendarEvent> customReadMethod(Document document) throws Exception;

    protected abstract void customWriteMethod(Document doc, HashSet<CalendarEvent> content);

    protected abstract void setFileDefaultOptions(File file) throws IOException;

    public boolean createFileIfNotExist(String path){
        file=new File(path);

        try {
            if(!file.createNewFile())
                return false;

            setFileDefaultOptions(file);

            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public boolean deleteFile(String path){
        File file=new File(path);
        return file.delete();
    }

    public HashSet<CalendarEvent> readFile(String path) throws Exception{
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder= factory.newDocumentBuilder();

            Document document=builder.parse(path);

            return customReadMethod(document);

        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("File is unreachable.");
        }
    }

    public boolean writeFile(HashSet<CalendarEvent> content){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.newDocument();

            Element rootElement = document.createElement("calendar");
            document.appendChild(rootElement);

            customWriteMethod(document,content);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
            return true;
        } catch (ParserConfigurationException | TransformerException e) {
            return false;
        }
    }

    //Getters/Setters~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void setFile(File file) {
        this.file = file;
    }

    public File getFile(){
        return file;
    }

    public HashSet<CalendarEvent> getFileContent() {
        return fileContent;
    }

    public void setFileContent(HashSet<CalendarEvent> fileContent) {
        this.fileContent = fileContent;
    }
}

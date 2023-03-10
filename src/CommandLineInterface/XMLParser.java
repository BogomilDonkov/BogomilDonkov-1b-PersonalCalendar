package CommandLineInterface;

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

public abstract class XMLParser {

    //Members~~~~~~~~~~~~~~~~~~~~~~~~~~
    private File file;

    //Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~
    protected abstract ArrayList<Object> customReadMethod(Document document) throws Exception;

    protected abstract void customWriteMethod(Document doc, ArrayList<Object> content);

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

    public ArrayList<Object> readFile(String path) throws Exception{
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder= factory.newDocumentBuilder();

            Document document=builder.parse(path);

            return customReadMethod(document);

        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("File is unreachable.");
        }
    }

    public boolean writeFile(ArrayList<Object> content){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.newDocument();

            Element rootElement = document.createElement("calendar");
            document.appendChild(rootElement);

            customWriteMethod(document,content);

            // write the content into xml file
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
}

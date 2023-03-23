package PersonalCalendar;

import CommandLineInterface.Parsers.XMLParser;
import PersonalCalendar.Exceptions.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import static PersonalCalendar.CalendarEvent.*;


public class CalendarXMLParser extends XMLParser {
    //Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    protected void customWriteMethod(Document document,HashSet<CalendarEvent> content){
        for (CalendarEvent event : content) {
            Element element=document.createElement("event");


            String name = event.getName();
            String date = DATE_FORMAT.format(event.getDate());
            String startTime = TIME_FORMAT.format(event.getStartTime());
            String endTime = TIME_FORMAT.format(event.getEndTime());
            //for(int i=4;i<)
            String note = event.getNote();

            element.setAttribute("name", name);
            element.setAttribute("date", date);
            element.setAttribute("startTime", startTime);
            element.setAttribute("endTime", endTime);
            element.setAttribute("note", note);

            document.getFirstChild().appendChild(element);
        }
    }

    @Override
    protected void setFileDefaultOptions(File file) throws IOException {
        FileWriter writer=new FileWriter(file);
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><calendar/>");
        writer.close();
    }

    @Override
    protected HashSet<CalendarEvent> customReadMethod(Document document) throws CalendarDateException, CalendarTimeException, InvalidTimeIntervalException {

        HashSet<CalendarEvent> calendarEvents= new HashSet<>();
        document.getDocumentElement().normalize();

        NodeList eventList= document.getElementsByTagName("event");

        for(int i=0;i<eventList.getLength();i++){
            Node event = eventList.item(i);
            if(event.getNodeType()==Node.ELEMENT_NODE){
                Element eventElement=(Element)event;

                String name= eventElement.getAttribute("name");
                String date= eventElement.getAttribute("date");
                String startTime= eventElement.getAttribute("startTime");
                String endTime= eventElement.getAttribute("endTime");
                String note=eventElement.getAttribute("note");

                CalendarEvent content= new CalendarEvent(name,date,startTime,endTime,note);

                calendarEvents.add(content);
            }
        }
        return calendarEvents;
    }


}

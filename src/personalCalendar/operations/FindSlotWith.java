package personalCalendar.operations;

import cli.interfaces.FileParser;
import cli.interfaces.Operation;
import personalCalendar.models.CalendarEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;

import static personalCalendar.models.CalendarEvent.*;

public class FindSlotWith implements Operation {
    //region Members
    private final FileParser fileParser;
    private final ArrayList<String> instructions;
    //endregion

    //region Constructors
    public FindSlotWith(FileParser fileParser, ArrayList<String> instructions) {
        this.fileParser = fileParser;
        this.instructions = instructions;
    }
    //endregion

    //region Methods
    @Override
    public boolean execute() {
        try {
            HashSet<CalendarEvent> externalFileCalendarEvents = new HashSet<>(fileParser.readFile(instructions.get(2)));

            long hours = Integer.parseInt(instructions.get(1));

            LocalDate date= LocalDate.parse(instructions.get(0), DATE_FORMATTER);
            LocalTime startTime = LocalTime.parse("08:00");
            LocalTime endTime = LocalTime.parse("17:00");

            //Проверка дали датата съществува в двата календара
            if (!checkInBothCalendarsIfDateExists(date,fileParser.getFileContent(),externalFileCalendarEvents)){
                System.out.println("Не съществува такова събитие в двата календара");
                return false;
            }

            //Запазваме събитията от двата календара на едно място
            HashSet<CalendarEvent> combinedEventsByDate=new HashSet<>();

            //Намираме и запазваме всички събития от календара, зареден в програмата, с подадената дата от потребителя в колекция
            combinedEventsByDate.addAll(fileParser.getFileContent().stream().filter(item -> item.getDate().equals(date)).toList());

            //Намираме и запазваме всички събития от календара, подаден от потребителя, с подадената дата от потребителя в колекция
            combinedEventsByDate.addAll(externalFileCalendarEvents.stream().filter(item -> item.getDate().equals(date)).toList());


            //Събираме всички диапазони на събития в една времева линия и сравняваме разликите между диапазоните на събитията
            double leftGap=0;
            double rightGap=0;

            for(CalendarEvent event:combinedEventsByDate){

                if(startTime.until(event.getStartTime(), ChronoUnit.HOURS)<0)
                    startTime=event.getEndTime();
                else
                    leftGap = startTime.until(event.getStartTime(), ChronoUnit.HOURS);

                if(event.getEndTime().until(endTime, ChronoUnit.HOURS)<0)
                    endTime=event.getStartTime();
                else
                    rightGap = event.getEndTime().until(endTime, ChronoUnit.HOURS);

                if((leftGap<0||rightGap<0))
                    continue;

                if(leftGap<hours&&rightGap<hours)
                    continue;


                if(leftGap<hours)
                    startTime=event.getEndTime();


                if(rightGap<hours)
                    endTime=event.getStartTime();
            }

            //Извеждаме подходящо съобщение
            if(leftGap>=hours||rightGap>=hours)
            {
                System.out.println("Има свободно място "+leftGap + " " + rightGap);
                return true;
            }
            else
                System.out.println("Няма свободно място в двата календара"+leftGap + " " + rightGap);
            return false;


        } catch (Exception e) {
            String fileDirectory = instructions.get(2);
            System.out.println("File not found " + fileDirectory);
            return false;
        }
    }
    //endregion

    //region InternalMethods
    private boolean checkInBothCalendarsIfDateExists(LocalDate date,HashSet<CalendarEvent> firstCalendar,HashSet<CalendarEvent> secondCalendar){
        boolean doesFirstCalendarEventExist = false;
        boolean doesSecondCalendarEventExist = false;

        //Проверяваме дали съществува събитие с датата която сме подали в календара зареден в програмата
        for (CalendarEvent event : firstCalendar) {
            if (event.getDate().equals(date)) {
                if (!event.isHoliday()) {
                    doesFirstCalendarEventExist = true;
                    break;
                }
            }
        }

        //Проверяваме дали съществува събитие с датата която сме подали в календара подаден от потребителя
        for (CalendarEvent event : secondCalendar) {
            if (event.getDate().equals(date)) {
                if (!event.isHoliday()) {
                    doesSecondCalendarEventExist = true;
                    break;
                }
            }
        }

        return (doesFirstCalendarEventExist && doesSecondCalendarEventExist);
    }
    //endregion

}


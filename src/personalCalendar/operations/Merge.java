package personalCalendar.operations;

import cli.interfaces.FileParser;
import cli.interfaces.Operation;
import personalCalendar.models.CalendarEvent;

import java.util.*;

public class Merge implements Operation {

    //region Members
    private final FileParser fileParser;
    private final ArrayList<String> instructions;
    //endregion

    //region Constructors
    public Merge(FileParser fileParser, ArrayList<String> instructions) {
        this.fileParser = fileParser;
        this.instructions = instructions;
    }
    //endregion

    //region InternalMethods

    //endregion

    //region Methods
    @Override
    public boolean execute() {
        String fileDirectory = instructions.get(0);

        HashSet<CalendarEvent> calendarEvents;

        try {
            //Записваме в променлива събитията от подадения календар
            calendarEvents=new HashSet<>(fileParser.readFile(fileDirectory));
        } catch (Exception e) {
            System.out.println("File not found " + fileDirectory);
            return false;
        }
            //Ключовете са събитията на текущия календар, които се сблъскват със събитията от подадения календар
            Map<CalendarEvent,CalendarEvent> collisionMap=new HashMap<>();

            //Проверяваме кои при кои събития от двата календара има колизия
            for(CalendarEvent event:fileParser.getFileContent())
                for(CalendarEvent event1:calendarEvents)
                    if(!event1.checkCompatibility(event))
                        collisionMap.put(event,event1);

            //Проверяваме дали е настъпила колизия между събития
            if(!collisionMap.isEmpty())
            {
                System.out.println("There is collision between events: ");
                //Извеждаме между кои събития има колизия
                for(Map.Entry<CalendarEvent,CalendarEvent> entry : collisionMap.entrySet()){
                    System.out.println(entry.getKey() + " collide with: " + entry.getValue());
                }

                //Правим запитване до потребителя дали иска да редактира датата и часа на несъвместимите събития
                System.out.println("""
                        
                        If you want to proceed, you need to edit your events.
                        Do you want to proceed ?  Y/N\s""");
                System.out.println(">");

                //Прочитаме отговора на потребителя в променлива и извършваме обработка
                String option = new Scanner(System.in).nextLine();
                while(true) {
                    if (option.equals("N") || option.equals("n"))
                    {
                        System.out.println("Both calendars: "+ fileParser.getFile().getName() + " and " + fileDirectory+ " were not merged!");
                        return false;
                    }

                    if (option.equals("Y") || option.equals("y"))
                    {
                        //Минаваме през всяко едно несъвместимо събитие и приемаме новите стойности въведени от потребителя.
                        //Новите стойности директно се предават в Book класа, тъй като той държи нужната логика за запис в колекция от календарни събития
                        for(CalendarEvent event:collisionMap.values()){
                            System.out.println("Event to change: ");
                            System.out.println(event);
                            System.out.println("New values: <date> <startTime> <enbTime> "+event.getName()+ " "+ event.getNote());

                            Book rebookEvent=null;

                            do {
                                System.out.println(">");

                                String newInput = new Scanner(System.in).nextLine();

                                if (newInput.equals(""))
                                    continue;

                                ArrayList<String> newInstructions = new ArrayList<>(List.of(newInput.split("\\s+")));

                                if (newInstructions.isEmpty())
                                    continue;

                                if (newInstructions.size()<3) {
                                    System.out.println("expects <date> <startTime> <endTime>");
                                    continue;
                                }

                                newInstructions.add(event.getName());
                                newInstructions.add(event.getNote());

                                rebookEvent = new Book(fileParser,newInstructions);
                            }while(!Objects.requireNonNull(rebookEvent).execute());
                        }
                    }
                }
            }
            fileParser.getFileContent().addAll(calendarEvents);
            System.out.println("Both calendars: "+ fileParser.getFile().getName() + " and " + fileDirectory+ " are successfully merged!");
            return true;
    }
    //endregion
}

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class CreateCal {
    public static void main(String[] args) throws Exception {

        Semester s2022_1 = new Semester("26/09/2022", "02/12/2022");
        Semester s2022_2 = new Semester("09/01/2023", "17/03/2023");
        Semester s2022_3 = new Semester("17/04/2023", "23/06/2023");

        Semester s2023_1 = new Semester("18/09/2023", "02/02/2024", "18/12/2023", "05/01/2024", 6);
        Semester s2023_2 = new Semester("05/02/2024", "07/06/2024", "25/03/2024", "05/04/2024");

        Semester s2024_1 = new Semester("16/09/2024", "31/01/2025", "16/12/2024", "03/01/2025", 6);
        Semester s2024_2 = new Semester("03/02/2025", "06/06/2025", "07/04/2025", "18/04/2025");

        //System.out.println(s2022_1);
        System.out.print(Semester.iCalHeader());

        System.out.print(s2022_1);
        System.out.print(s2022_2);
        System.out.print(s2022_3);

        System.out.print(s2023_1);
        System.out.print(s2023_2);

        System.out.print(s2024_1);
        System.out.print(s2024_2);

        System.out.print(Semester.iCalFooter());
    }
}

class Semester {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat icf = new SimpleDateFormat("yyyyMMdd");  // format for iCal

    Date start_date;
    Date end_date;

    Date vacation_start;
    Date vacation_end;

    int week_num_start = 0; // This is 1 for terms, 0 for semesters
    int cons_week = -1; // which week is consolidation week
    int teaching_weeks = 11; // how many teaching weeks are there? (usually 11)

    public Semester(String s, String e) throws Exception {
        this(s, e, "01/01/2000", "01/01/2000", -1);
        week_num_start = 1; // assume if only start and end we're talking Terms...
    }

    public Semester(String s, String e, String v_s, String v_e) throws Exception {
        this(s, e, v_s, v_e, -1);
    }

    public Semester(String s, String e, String v_s, String v_e, int cons_week) throws Exception {
        start_date = sdf.parse(s);
        end_date = sdf.parse(e);
        vacation_start = sdf.parse(v_s);
        vacation_end = sdf.parse(v_e);
        this.cons_week = cons_week;
    }

    public String toString() {
        Calendar c = Calendar.getInstance();
        c.setTime(start_date);
        String output = "";

        int week_cnt = week_num_start;
        int counter = week_num_start;

        while (c.getTime().compareTo(end_date) < 0) {
            if ((c.getTime().compareTo(vacation_start) >= 0) && (c.getTime().compareTo(vacation_end) <= 0)) {
                //output += icf.format(c.getTime()) + " - Vacation Week\n";
            } else {
                if (counter == cons_week) {
                    output += toiCalEvent(c.getTime(), "Consolidation Week");
                    //output += icf.format(c.getTime()) + " - Consolidation Week\n";
                } else if (week_cnt > teaching_weeks) {
                    output += toiCalEvent(c.getTime(), "Assessment Week");
                    //output += icf.format(c.getTime()) + " - Assessment Week\n";
                } else {
                    output += toiCalEvent(c.getTime(), "Teaching Week " + week_cnt);
                    //output += icf.format(c.getTime()) + " - Week " + week_cnt + "\n";
                    week_cnt++;
                }
                counter++;
            }
            c.add(Calendar.DAY_OF_MONTH, 7);
        }

        return output;
    }

    public static String toiCalEvent(Date d, String title) {
        String ret = "BEGIN:VEVENT\n"
                   + "DTSTAMP;VALUE=DATE:19760401\n"
                   + "DTSTART;VALUE=DATE:" + icf.format(d) + "\n"
                   + "CLASS:PUBLIC\n"
                   + "SUMMARY;LANGUAGE=en:" + title +"\n"
                   + "TRANSP:TRANSPARENT\n"
                   + "END:VEVENT\n";
        return ret;
    }

    public static String iCalHeader() {
        return "BEGIN:VCALENDAR\n" +
               "VERSION:2.0\n" +
               "PRODID:icalendar-ruby\n" +
               "CALSCALE:GREGORIAN\n" +
               "X-WR-CALNAME:University of York - Week Numbers\n" +
               "X-APPLE-LANGUAGE:en\n" +
               "X-APPLE-REGION:GB\n";
    }

    public static String iCalFooter() {
        return "END:VCALENDAR\n";
    }
}

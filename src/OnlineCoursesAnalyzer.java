import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 *
 * This is just a demo for you, please run it on JDK17 (some statements may be not allowed in lower version).
 * This is just a demo, and you can extend and implement functions
 * based on this demo, or implement it in a different way.
 */
public class OnlineCoursesAnalyzer {

    List<Course> courses = new ArrayList<>();

    public OnlineCoursesAnalyzer(String datasetPath) {
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(datasetPath, StandardCharsets.UTF_8));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] info = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1);
                Course course = new Course(info[0], info[1], new Date(info[2]), info[3], info[4], info[5],
                        Integer.parseInt(info[6]), Integer.parseInt(info[7]), Integer.parseInt(info[8]),
                        Integer.parseInt(info[9]), Integer.parseInt(info[10]), Double.parseDouble(info[11]),
                        Double.parseDouble(info[12]), Double.parseDouble(info[13]), Double.parseDouble(info[14]),
                        Double.parseDouble(info[15]), Double.parseDouble(info[16]), Double.parseDouble(info[17]),
                        Double.parseDouble(info[18]), Double.parseDouble(info[19]), Double.parseDouble(info[20]),
                        Double.parseDouble(info[21]), Double.parseDouble(info[22]));
                courses.add(course);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //1
    public Map<String, Integer> getPtcpCountByInst() {
        Map<String , Integer> map = new TreeMap<>();
        for (int i = 0; i < courses.size(); i++) {
            map.put(courses.get(i).institution,0);
        }
        for (int i = 0; i < courses.size(); i++) {
            int num = courses.get(i).participants;
            String inst = courses.get(i).institution;
            int count = map.get(inst);
            count = count + num;
            map.put(inst,count);
        }
        return map;
    }

    //2
    public Map<String, Integer> getPtcpCountByInstAndSubject() {
        Map<String , Integer> map = new HashMap<>();
        for (int i = 0; i < courses.size(); i++) {
            Course temp = courses.get(i);
            String key = temp.institution+"-"+temp.subject;
            map.put(key,0);
        }
        for (int i = 0; i < courses.size(); i++) {
            Course temp = courses.get(i);
            int num = temp.participants;
            String inst = temp.institution+"-"+temp.subject;
            int count = map.get(inst);
            count = count + num;
            map.put(inst,count);
        }
        List<Map.Entry<String, Integer>> sortList = new ArrayList<>(map.entrySet());
        Collections.sort(sortList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                int diff = o2.getValue() - o1.getValue();
                if (diff==0){
                    return o1.getKey().compareTo(o2.getKey());
                }
                else return diff;
            }
        });
        Map<String ,Integer> sortedmap = new LinkedHashMap<>();
        for (Map.Entry<String,Integer> entry:sortList
             ) {
            sortedmap.put(entry.getKey(),entry.getValue());
        }
        return sortedmap;
    }

    //3
    public Map<String, List<List<String>>> getCourseListOfInstructor() {
        Map<String,List<List<String>>> map = new HashMap<>();
        Set<String> instructorsSet = new HashSet<>();
        for (int i = 0; i < courses.size(); i++) {
            Course temp = courses.get(i);
            String[] str = temp.instructors.split(",");
            for (int j = 0; j < str.length; j++) {
                str[j] = str[j].trim();
                instructorsSet.add(str[j]);
            }
        }
        for (String temp : instructorsSet) {
            List<String> a = new ArrayList<>();
            List<String> c = new ArrayList<>();
            List<List<String>> b = new ArrayList<>(2);
            b.add(a);
            b.add(c);
            map.put(temp,b);
        }
        for (Course temp : courses) {
            String[] str = temp.instructors.split(",");
            if (str.length==1) {
                str[0]=str[0].trim();
                map.get(str[0]).get(0).add(temp.title);
            }
            else {
                for (int i = 0; i < str.length; i++) {
                    str[i] = str[i].trim();
                    map.get(str[i]).get(1).add(temp.title);
                }
            }
        }
        for (String temp : instructorsSet) {
            Collections.sort(map.get(temp).get(0));
            Collections.sort(map.get(temp).get(1));
        }
        return map;
    }

    //4
    public List<String> getCourses(int topK, String by) {
        return null;
    }

    //5
    public List<String> searchCourses(String courseSubject, double percentAudited, double totalCourseHours) {
        return null;
    }

    //6
    public List<String> recommendCourses(int age, int gender, int isBachelorOrHigher) {
        return null;
    }

}

class Course {
    String institution;//0
    String number;//1
    Date launchDate;//2
    String title;//3
    String instructors;
    String subject;
    int year;
    int honorCode;
    int participants;
    int audited;
    int certified;
    double percentAudited;
    double percentCertified;
    double percentCertified50;
    double percentVideo;
    double percentForum;
    double gradeHigherZero;
    double totalHours;
    double medianHoursCertification;
    double medianAge;
    double percentMale;
    double percentFemale;
    double percentDegree;

    public Course(String institution, String number, Date launchDate,
                  String title, String instructors, String subject,
                  int year, int honorCode, int participants,
                  int audited, int certified, double percentAudited,
                  double percentCertified, double percentCertified50,
                  double percentVideo, double percentForum, double gradeHigherZero,
                  double totalHours, double medianHoursCertification,
                  double medianAge, double percentMale, double percentFemale,
                  double percentDegree) {
        this.institution = institution;
        this.number = number;
        this.launchDate = launchDate;
        if (title.startsWith("\"")) title = title.substring(1);
        if (title.endsWith("\"")) title = title.substring(0, title.length() - 1);
        this.title = title;
        if (instructors.startsWith("\"")) instructors = instructors.substring(1);
        if (instructors.endsWith("\"")) instructors = instructors.substring(0, instructors.length() - 1);
        this.instructors = instructors;
        if (subject.startsWith("\"")) subject = subject.substring(1);
        if (subject.endsWith("\"")) subject = subject.substring(0, subject.length() - 1);
        this.subject = subject;
        this.year = year;
        this.honorCode = honorCode;
        this.participants = participants;
        this.audited = audited;
        this.certified = certified;
        this.percentAudited = percentAudited;
        this.percentCertified = percentCertified;
        this.percentCertified50 = percentCertified50;
        this.percentVideo = percentVideo;
        this.percentForum = percentForum;
        this.gradeHigherZero = gradeHigherZero;
        this.totalHours = totalHours;
        this.medianHoursCertification = medianHoursCertification;
        this.medianAge = medianAge;
        this.percentMale = percentMale;
        this.percentFemale = percentFemale;
        this.percentDegree = percentDegree;
    }
}
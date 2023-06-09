import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
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
    Map<String, Integer> map = new TreeMap<>();
    for (int i = 0; i < courses.size(); i++) {
      map.put(courses.get(i).institution, 0);
    }
    for (int i = 0; i < courses.size(); i++) {
      int num = courses.get(i).participants;
      String inst = courses.get(i).institution;
      int count = map.get(inst);
      count = count + num;
      map.put(inst, count);
    }
    return map;
  }

  //2
  public Map<String, Integer> getPtcpCountByInstAndSubject() {
    Map<String, Integer> map = new HashMap<>();
    for (int i = 0; i < courses.size(); i++) {
      Course temp = courses.get(i);
      String key = temp.institution + "-" + temp.subject;
      map.put(key, 0);
    }
    for (int i = 0; i < courses.size(); i++) {
      Course temp = courses.get(i);
      int num = temp.participants;
      String inst = temp.institution + "-" + temp.subject;
      int count = map.get(inst);
      count = count + num;
      map.put(inst, count);
    }
    List<Map.Entry<String, Integer>> sortList = new ArrayList<>(map.entrySet());
    Collections.sort(sortList, new Comparator<Map.Entry<String, Integer>>() {
      @Override
      public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
        int diff = o2.getValue() - o1.getValue();
        if (diff == 0) {
          return o1.getKey().compareTo(o2.getKey());
        } else return diff;
      }
    });
    Map<String, Integer> sortedmap = new LinkedHashMap<>();
    for (Map.Entry<String, Integer> entry : sortList
    ) {
      sortedmap.put(entry.getKey(), entry.getValue());
    }
    return sortedmap;
  }

  //3
  public Map<String, List<List<String>>> getCourseListOfInstructor() {
    Map<String, List<List<String>>> map = new HashMap<>();
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
      map.put(temp, b);
    }
    for (Course temp : courses) {
      String[] str = temp.instructors.split(",");
      if (str.length == 1) {
        str[0] = str[0].trim();
        if (!map.get(str[0]).get(0).contains(temp.title))
          map.get(str[0]).get(0).add(temp.title);
      } else {
        for (int i = 0; i < str.length; i++) {
          str[i] = str[i].trim();
          if (!map.get(str[i]).get(1).contains(temp.title))
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
    List<String> list = new ArrayList<>();
    if (Objects.equals(by, "hours")) {
      Set<String> title = new HashSet<>();
      Map<String, Double> map = new HashMap<>();
      for (Course temp : courses) {
        title.add(temp.title);
      }
      for (String temp : title) {
        map.put(temp, 0.0);
      }
      for (Course temp : courses) {
        if (temp.totalHours > map.get(temp.title))
          map.put(temp.title, temp.totalHours);
      }
      list.addAll(title);
      Collections.sort(list, new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
          if (map.get(o1) < map.get(o2)) return 1;
          if (map.get(o1) > map.get(o2)) return -1;
          return o1.compareTo(o2);
        }
      });
      List<String> list1 = new ArrayList<>();
      for (int i = 0; i < topK; i++) {
        list1.add(list.get(i));
      }
      return list1;
    } else {
      Set<String> title = new HashSet<>();
      Map<String, Integer> map = new HashMap<>();
      for (Course temp : courses) {
        title.add(temp.title);
      }
      for (String temp : title) {
        map.put(temp, 0);
      }
      for (Course temp : courses) {
        if (temp.participants > map.get(temp.title))
          map.put(temp.title, temp.participants);
      }
      list.addAll(title);
      Collections.sort(list, new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
          if (map.get(o1) < map.get(o2)) return 1;
          if (map.get(o1) > map.get(o2)) return -1;
          return o1.compareTo(o2);
        }
      });
      List<String> list1 = new ArrayList<>();
      for (int i = 0; i < topK; i++) {
        list1.add(list.get(i));
      }
      return list1;
    }
  }

  //5
  public List<String> searchCourses(String courseSubject, double percentAudited, double totalCourseHours) {
    courseSubject = courseSubject.toUpperCase(Locale.ROOT);
    for (Course temp : courses) {
      temp.subject = temp.subject.toUpperCase(Locale.ROOT);
    }
    Set<String> a = new HashSet<>();
    for (Course temp : courses) {
      if (temp.subject.contains(courseSubject) && temp.percentAudited >= percentAudited && temp.totalHours <= totalCourseHours) {
        a.add(temp.title);
      }
    }
    List<String> list = new ArrayList<>(a);
    Collections.sort(list);
    return list;
  }

  //6
  public List<String> recommendCourses(int age, int gender, int isBachelorOrHigher) {
    Set<String> number = new HashSet<>();
    for (Course temp : courses) {
      number.add(temp.number);
    }
    Map<String, Integer> time = new HashMap<>();
    Map<String, Double> midage = new HashMap<>();
    Map<String, Double> midgender = new HashMap<>();
    Map<String, Double> midisBachelorOrHigher = new HashMap<>();
    for (String temp : number) {
      time.put(temp, 0);
      midage.put(temp, 0.0);
      midgender.put(temp, 0.0);
      midisBachelorOrHigher.put(temp, 0.0);
    }
    for (Course temp : courses) {
      int d = time.get(temp.number);
      d++;
      time.put(temp.number, d);
    }
    for (Course temp : courses) {
      double a = midage.get(temp.number);
      a += temp.medianAge;
      midage.put(temp.number, a);
      double b = midgender.get(temp.number);
      b += temp.percentMale;
      midgender.put(temp.number, b);
      double c = midisBachelorOrHigher.get(temp.number);
      c += temp.percentDegree;
      midisBachelorOrHigher.put(temp.number, c);
    }
    for (String temp : number) {
      double n = midage.get(temp) / time.get(temp);
      midage.put(temp, n);
      double m = midgender.get(temp) / time.get(temp);
      midgender.put(temp, m);
      double k = midisBachelorOrHigher.get(temp) / time.get(temp);
      midisBachelorOrHigher.put(temp, k);
    }
    Map<String, Double> result = new HashMap<>();
    for (String temp : number) {
      double val = Math.pow(age - midage.get(temp), 2) + Math.pow(gender * 100 - midgender.get(temp), 2) + Math.pow(isBachelorOrHigher * 100 - midisBachelorOrHigher.get(temp), 2);
      result.put(temp, val);
    }
    Map<String, List<Course>> numtotitle = new HashMap<>();
    for (String temp : number) {
      List<Course> a = new ArrayList<>();
      numtotitle.put(temp, a);
    }
    for (Course temp : courses) {
      numtotitle.get(temp.number).add(temp);
    }
    for (String temp : number) {
      numtotitle.get(temp).sort(new Comparator<Course>() {
        @Override
        public int compare(Course o1, Course o2) {
          return o2.launchDate.compareTo(o1.launchDate);
        }
      });
    }
    Map<String, String> numtitle = new HashMap<>();
    for (String temp : number) {
      numtitle.put(temp, numtotitle.get(temp).get(0).title);
    }
    List<String> num = new ArrayList<>(number);
    num.sort((o1, o2) -> {
      if (result.get(o1) > result.get(o2)) return 1;
      if (result.get(o1) < result.get(o2)) return -1;
      return numtitle.get(o1).compareTo(numtitle.get(o2));
    });
    Set<String> result2 = new HashSet<>();
    List<String> result1 = new ArrayList<>();
    for (String s : num) {
      if (result2.add(numtitle.get(s)))
        result1.add(numtitle.get(s));
    }

    return result1.subList(0, 10);
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
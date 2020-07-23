package org.mycompany.devlab;


import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

//import static org.mycompany.devlab.UploadServlet.UPLOAD_DIR;


public class Document {
    //настройки
    public static final String PATH_TO_PROPERTIES ="/resources/config.properties";


    //атрибуты документа
    private Integer id;          //уникальный ID
    private String name;        //название
    private String docType;     //тип (СОП, РИ, КП, Алгоритм)
    private Date date;          //дата утверждения документа
    private String number;      //номер документа (СОП.КДЛ-001-2020) -> номер 001 автоинкремент если загружается ворд файл!!
    private String zones;      //Область применения документа (otdels = poliklinika;stacionar;apteka;KDL и т.д)
    private String razrab;    //автор документа
    Properties properties = new Properties();

    //консруктор
    public Document(String name, String docType, /*Date date, */String number,  String zones, String razrab) {
        this.name = name;
        this.docType = docType;
        //this.date = date;
        this.number = number;
        this.zones = zones;
        this.razrab = razrab;
    }

    public Document() {
    }

    //удаление документа (строки из файла)
    // TODO: 19.07.2020 добавить сдвиг номеров при удалении документа. Если утвержденный - не удалять!
    public List<String[]> delete(Integer id, List<String[]> allRows){
        List<String[]> rows = new ArrayList<String[]>();
        Integer count = 0;
        for (String[] r:allRows
             ) {
            if(!(id==count)){
                rows.add(r);
            } else {
                //удаление самого файла
                File delFile = new File(r[r.length-2]+File.separator+r[r.length-1]);
                boolean deleted = delFile.delete();
            }
            count++;
        }

        return rows;
    }

    //сохранние документа (for Upload servlet)
    public String storeDoc(
        String put, //путь программы
        String fileName // название файла
    ) {
        String fileExtension = getFileExtension(fileName); //получаем расширение файла (без точки)

        try (FileInputStream fileInputStream = new FileInputStream(put+PATH_TO_PROPERTIES)) {

            properties.load(fileInputStream);
            String mainFileName = properties.getProperty("main_file"); //получаем название основного файла CSV со всеми записями о документах
            String dir = properties.getProperty("dir"); //получаем директорию для складывания файлов Doc и Docx

            Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
            calendar.setTime(new Date());
            int year = calendar.get(Calendar.YEAR); //текущий год
            int counter; //счетчик номера документа

           DateFormat formatter = new SimpleDateFormat("YYYY.MM.dd__HH-mm-ss");

           String dateTimeAdded = formatter.format(calendar.getTime()); //время добавления документа
           //String newFileName = message+fileName.substring(fileName.length()-4, fileName.length());


            //проверка наличия файла CSV
            File file = new File(/*put+File.separator+*/ mainFileName);
            //Иcтoчниk: https://j4web.ru/java-io/proverka-sushhestvovaniya-fajla-v-java.html
            if (file.exists()) {
                //Build reader instance
                CSVReader reader = new CSVReader(new FileReader(mainFileName), ';', '"', '/',0);
                //Read all rows at once
                List<String[]> allRows = reader.readAll();
                List<String[]> oneTypeRows = new ArrayList<>();
                for (String[] st:allRows) {
                    if (st[3].equals(getDocType())){
                        oneTypeRows.add(st); //получаем список только из документов переданного типа
                    }
                }
                if (oneTypeRows==null|oneTypeRows.size()==0){
                    counter = 1;
                }
                else counter=oneTypeRows.size()+1;

                //приводим номер с вида 1 к виду 001
                number = makeNumber(counter);

                String newFileName = getDocType()+"."+getZones()+"-"+number+"-"+String.valueOf(year)+"."+fileExtension;
                //Переименовываем файл
                File srcFile = new File(dir+fileName);
                File destFile = new File(dir+newFileName);
                boolean renamed = srcFile.renameTo(destFile);

                CSVWriter writer = new CSVWriter(new FileWriter(mainFileName, true), ';'); //создаем с дозаписью
                //пример разбиения строки на массив по разделителю ","
                //String [] record = "4,Mark,Anderson,USA,50".split(",");

                //дата пустая, т.к. просто загрузка оригинала документа - дата вводится при загрузке pdf
                String [] record = new String[]{number, "В разработке", name, docType, zones, razrab, dir, newFileName, dateTimeAdded};

                //пример записи в csv
                writer.writeNext(record);
                writer.close();
            } else {
                //System.out.println("Фaйл нe cyщecтвyeт.");
                CSVWriter writer = new CSVWriter(new FileWriter(mainFileName, true), ';'); //создаем с дозаписью
                //пример разбиения строки на массив по разделителю ","
                //String [] record = "4,Mark,Anderson,USA,50".split(",");

                //приводим номер с вида 1 к виду 001
                setNumber(makeNumber(1)); //самый первый документ вообще

                String newFileName = getDocType()+"."+getZones()+"-"+getNumber()+"-"+String.valueOf(year);
                //Переименовываем файл
                File srcFile = new File(dir+fileName);
                File destFile = new File(dir+newFileName);
                boolean renamed = srcFile.renameTo(destFile);

                //дата пустая, т.к. просто загрузка оригинала документа - дата вводится при загрузке pdf
                String [] record = new String[]{number, "В разработке", name, docType, zones, razrab, dir, newFileName, dateTimeAdded};

                //пример записи в csv
                writer.writeNext(record);
                writer.close();
            }


        }
        catch (IOException e){
            System.out.println(PATH_TO_PROPERTIES + " не найден(");
            e.printStackTrace();
        }
    return getNumber();
    } //void storeDocument


    //поиск документа ( FindServlet )
    public List<String[]> find(
        String[] whatToFind,
        List<String[]>allRows)
    {
        //мапа для добавленияя найденных строк после перебора по каждому поиску (для случая если в поиске задано более 1 параметра)
        Map resMap = new HashMap<String[], Integer>();

        //перебираем полученный массив строк для поиска, если не равно ""
        int count = 0; //счетчик количества нахождений параметра
        int paramsCount = 0; //счетчик переданных параметорв на поиск
        // Инициализирую счетчик
        Integer item;
        for (int i = 0; i<whatToFind.length; i++)
        {   // i - позиция для поиска в массивах строк листа
            if (!(whatToFind[i].equals("")))
            {
                paramsCount++; //есть параметр для поиска
                for (String[] st: allRows)
                {
                    if (st[i].contains(whatToFind[i]))
                    {
                        count++; //нашлось что-то
                        resMap.put(allRows.iterator(), count); //добавляем текущую строку (ключ) и счетчик


/*                        item = resMap.get(allRows.iterator());
                        if (item == null) hm.put(wrd, 1); // если нет в списке то добавить со значением 1
                        else hm.put(wrd, item + 1); // если есть такая фамилия(Key), то +1*/
                    }
                }

            }
        }
        return null;
    }


    //редактирование документа (for "edit" in ListServlet)
    public List<String[]> editDocument(
            List<String[]> allRows, // все строки
            String put, //путь программы
            String fileName, // название файла
            boolean isNeedReplace, //маркер необходимости удаления файла
            Integer id // номер строки для изменения
    ) {

        List<String[]> rows = new ArrayList<String[]>(); //заготовка для перегона строк
        String [] record = null;                         //заготовка для искомой строки

        //если файл не приложен -> просто заменяем строку в CSV
        if (fileName.equals("") & !isNeedReplace){

            Integer count = 0;
            for (String[] r:allRows
                    ) {
                if(!(id==count)){
                    rows.add(r);
                } else {  //замена строки без удаления файла

                    //получаем название файла документа - последняя ячейка
                    String fn = r[r.length-1];

                    //дату в строку короткую - dd:MM:YYYY с тем же именем файла - fn
 //                   record = new String[]{number, convert_date(), name, docType, zones, opisanie, put+File.separator+UPLOAD_DIR, fn};

                    rows.add(record);
                }
                count++;
            }
        }

        //если файл приложен -> заменяем строку в CSV и сам файл в папке uploads
        if (!(fileName.equals("")) & isNeedReplace){
            try (FileInputStream fileInputStream = new FileInputStream(put+PATH_TO_PROPERTIES)){
 //               ;
 //               properties.load(fileInputStream);
                String mainFileName = properties.getProperty("main_file"); //получаем название основного файла

                Calendar calendar = Calendar.getInstance();

                DateFormat formatter = new SimpleDateFormat("YYYY.MM.dd__HH-mm-ss");

                String message = formatter.format(calendar.getTime());
                String newFileName = message+fileName.substring(fileName.length()-4, fileName.length());
                //время создания документа + расширение файла

                //Переименовываем файл
 //               File srcFile = new File(put+File.separator+UPLOAD_DIR+File.separator+fileName);
//                File destFile = new File(put+File.separator+UPLOAD_DIR+File.separator+newFileName);
//                boolean renamed = srcFile.renameTo(destFile);


                //////////////////////////////////////////
                // !!!! здесь надо писать в ту же строку, как в предыдущем if

                Integer count = 0;
                for (String[] r:allRows
                        ) {
                    if(!(id==count)){
                        rows.add(r);
                    } else {  //замена строки c удалением файла

                        //дату в строку короткую - dd:MM:YYYY с тем же именем файла - fn
  //                      record = new String[]{number, convert_date(), name, docType, zones, opisanie, put+File.separator+UPLOAD_DIR, newFileName};

                        rows.add(record);
                    }
                    count++;
                }
                fileInputStream.close();

            }
            catch (IOException e){
                System.out.println(PATH_TO_PROPERTIES + " не найден(");
                e.printStackTrace();
            }

        }

        return rows;

    } // editDocument


    private String convert_date() {
        //Fri May 15 00:00:00 GMT+05:00 2020
        //DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        //Wed Feb 12 00:00:00 GMT+05:00 2020
        //11Ma020

       // Calendar calendar = Calendar.getInstance();

        Locale rLocale = new Locale("ru");
        SimpleDateFormat sFormatter = new SimpleDateFormat("dd MMM yyyy", rLocale);

        return sFormatter.format(date);
    }

    private String makeNumber(int counter){
        String d = String.valueOf(counter);
        if (d.length()==3) return d;
        if (d.length()==2) return "0"+d;
        if (d.length()==1) return "00"+d;
        return "";
    }

    private String getFileExtension(String fileName){
//        String result = "___";
//        String[] tmp = fileName.split(".");
//        int razmer = tmp.length;
//        if (!(razmer==0)) {
//            result = tmp[razmer];
//        }
//        return result;

        // если в имени файла есть точка и она не является первым символом в названии файла
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            // то вырезаем все знаки после последней точки в названии файла, то есть ХХХХХ.txt -> txt
            return fileName.substring(fileName.lastIndexOf(".")+1);
            // в противном случае возвращаем заглушку, то есть расширение не найдено
        else return "___";
    }

    //Геттеры
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDocType() {
        return docType;
    }

    public Date getDate() {
        return date;
    }

    public String getNumber() {
        return number;
    }

    public String getZones() {
        return zones;
    }

    public String getRazrab() {
        return razrab;
    }

    //Сеттеры
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setZones(String zones) {
        this.zones = zones;
    }

    public void setRazrab(String razrab) {
        this.razrab = razrab;
    }
}

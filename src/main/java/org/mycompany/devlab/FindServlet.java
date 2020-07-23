package org.mycompany.devlab;

import au.com.bytecode.opencsv.CSVReader;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mycompany.devlab.Document.PATH_TO_PROPERTIES;


@MultipartConfig //запрос может содержать несколько параметров
public class FindServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public static final String UPLOAD_DIR = "uploads";
    public int otdelsSize;

    public FindServlet() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,IOException {
        Properties prop = new Properties();
        ServletContext servletContext = request.getServletContext();

        // Местоположение веб приложения на жестком диске (hard disk).
        String realPath = servletContext.getRealPath("");

        //Загружаем properties
        FileInputStream fis = new FileInputStream(realPath+PATH_TO_PROPERTIES);
        prop.load(fis);

        String otdels = prop.getProperty("otdels"); //получаем отделы для выборки
        request.setAttribute("otdelsFromProps", otdels);

        String types = prop.getProperty("types"); //получаем типы документов для выборки
        request.setAttribute("typesFromProps", types);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("find.jsp");
        requestDispatcher.forward(request, response);

    }

    //@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding ("UTF-8");

        //параметры запроса
        String number = request.getParameter("number");
        String doc_name = request.getParameter("name");
        String dataFrom = request.getParameter("data1");
        String dataTo = request.getParameter("data2");
        String tip = request.getParameter("tip");
        String opisanie = request.getParameter("opisanie");

        ServletContext servletContext = request.getServletContext();

        // Местоположение веб приложения на жестком диске (hard disk).
        String realPath = servletContext.getRealPath("");

        //Загружаем properties
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(realPath+PATH_TO_PROPERTIES);
        properties.load(fis);

        String otdels = properties.getProperty("otdels"); //получаем отделы для выборки
        request.setAttribute("otdelsFromProps", otdels);

        String types = properties.getProperty("types"); //получаем типы документов для выборки
        request.setAttribute("typesFromProps", types);

        String otd = ""; //готовим строку для вывода только выбранных в форме отделов
        //записываем в глобальную переменную размер массива отделов (для использования в extractOtdels())
        String[] tempo = otdels.split(";");
        otdelsSize = tempo.length;
        for (int i = 0; i<otdelsSize; i++){
            if (  !(request.getParameter("ch"+i)==null)  ){
                otd=otd+tempo[i]+", ";
            }
        }
        //убираем запятую в конце
        String otdelsOK = "";
        if (!(otd.length()==0))  {
            otdelsOK=otd.substring(0, otd.length()-2);
        }

        //проверка пустого запроса
        if (
            number.equals("") &
            doc_name.equals("") &
            dataFrom.equals("") &
            dataTo.equals("") &
            tip.equals("") &
            otdelsOK.equals("") &
            opisanie.equals("")
                )
        {
            //выводим сообщение о пустом запросе и просим ввести заново
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("empty.jsp");
            requestDispatcher.forward(request, response);
        }
        else //запрос не пустой
        {
            //Даты заданы корректные? (первая меньше второй или они равны) или же обе пустые
            if ((isDatesCorrect(dataFrom, dataTo))|(dataFrom.equals("") & dataTo.equals("")))
            {
                String mainFileName = properties.getProperty("main_file"); //получаем название основного файла

                //Build reader instance
                CSVReader reader = new CSVReader(new FileReader(mainFileName), ';', '"', '/',0);
                //Read all rows at once
                List<String[]> allRows = reader.readAll();

                String[] toFind = {
                        number,
                        "",//data, //по дате будет последний фильтр
                        doc_name,
                        tip,
                        otdelsOK,
                        opisanie};

                //поиск
                List<String[]> findedRows = find(toFind, allRows); //поиск без учета вхождения дат, если даже они заданы
                //окончательная фильрация по датам, если задано
                if ((!dataFrom.equals("") & !dataTo.equals("")))
                {
                    findedRows = findByDate(findedRows, dataFrom, dataTo);
                }

                //для просмотра файла в браузере
                request.setAttribute("uploadDir", UPLOAD_DIR);
                request.setAttribute("findedRows", findedRows);
                request.setAttribute("toFind", toFind);
                request.setAttribute("dataFrom", dataFrom);
                request.setAttribute("dataTo", dataTo);

                RequestDispatcher requestDispatcher = request.getRequestDispatcher("finded.jsp");
                requestDispatcher.forward(request, response);
            }
            else
            {
                request.setAttribute("dataFrom", dataFrom);
                request.setAttribute("dataTo", dataTo);
                //выводим сообщение о не корректных датах и просим ввести их заново
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("wrongDates.jsp");
                requestDispatcher.forward(request, response);
            }

        }
    }

    /**
     * Search string from List that contains params to find
     */
    private List<String[]> find(String[] whatToFind, List<String[]> l){
        //мапа для добавленияя найденных строк после перебора по каждому поиску (для случая если в поиске задано более 1 параметра)
        Map resMap = new HashMap<String[], Integer>();

        //перебираем полученный массив строк для поиска, если не равно ""
        int paramsCount = 0; //счетчик переданных параметорв на поиск

        for (int i = 0; i<whatToFind.length; i++)
        {   // i - позиция для поиска в массивах строк листа
            if (!(whatToFind[i].equals("")))
            {
                paramsCount++; //есть параметр для поиска
                for (String[] st: l)
                {
                    if (st[i].toLowerCase().contains(whatToFind[i].toLowerCase()))
                    {
                        //нашлось что-то
                        if ((resMap.get(st)==null))
                        {
                            resMap.put(st, 1); //добавляем текущую строку (ключ) и счетчик (первое нахождение)
                        } else {
                            int v = (Integer)resMap.get(st); //получаем значение счетчика
                            v++;                             // и увеличиваем
                            resMap.put(st, v);               // перезаписываем счетчик
                        }
                    }
                }
            }
        }
        final int counter = paramsCount;
        //формируем список по принципу: у какого ключа (String[]) значение value равно количеству переданных параметров?
        //если "да", то добавлем на вывод.
        List<String[]> result = new ArrayList<>(); //для вывода результата
        resMap.forEach((key, value) -> {

            if ((Integer)value==counter){
                result.add((String[])key);
            }

        });
        
        return result;
    }

    private boolean isDatesCorrect(String dataFrom, String dataTo){
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        try {
            Date dateFrom = dateFormat.parse(dataFrom);
            Date dateTo = dateFormat.parse(dataTo);
            return (dateFrom.before(dateTo))|(dateFrom.equals(dateTo));
            //первая дата меньше второй или они равны
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<String[]> findByDate(List<String[]> findedRows, String dataFrom, String dataTo){
        //готовим строку с датой из строки в локаль даты формы
        //т.е. переводим из формата 01 Jun 2020 в 2020-06-01
        String DateFromString = "";
        Locale rLocale = new Locale("ru"); //русская локаль
        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", rLocale);
        SimpleDateFormat newFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        //перебираем все строки и проверяем дату на вхождение в диапазон
        for (int i=0; i<findedRows.size(); i++){
            String[] temp = findedRows.get(i);

            //преобразуем дату
            try {
                Date date = formatter.parse(temp[1]);
                DateFromString = newFormatter.format(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if ((isDatesCorrect(dataFrom, DateFromString))&(isDatesCorrect(DateFromString, dataTo))){
                //если дата в диапазоне - ничего не делаем
            }
            else {
                findedRows.remove(i);
            }
        }
        return findedRows;
    }

}
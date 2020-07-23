package org.mycompany.devlab;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mycompany.devlab.Document.PATH_TO_PROPERTIES;
//import static org.mycompany.devlab.UploadServlet.UPLOAD_DIR;


@MultipartConfig
public class ListServlet extends HttpServlet {
    public int otdelsSize;

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        Properties prop = new Properties();
        ServletContext servletContext = httpServletRequest.getServletContext();

        // Местоположение веб приложения на жестком диске (hard disk).
        String realPath = servletContext.getRealPath("");

        //Загружаем properties
        try (FileInputStream fis = new FileInputStream(realPath+PATH_TO_PROPERTIES)){
            prop.load(fis);
        }
        catch (FileNotFoundException e) {
            String errorMessage = "Отсутствует файл настроек, запуск невозможен :(";
            httpServletRequest.setAttribute("errorMessage", errorMessage);
            e.printStackTrace();
            String serviceInformation = e.getLocalizedMessage();
            httpServletRequest.setAttribute("serviceInformation", serviceInformation);

            RequestDispatcher requestDispatcher = httpServletRequest.getRequestDispatcher("error.jsp");
            requestDispatcher.forward(httpServletRequest, httpServletResponse);
        }


        String mainFileName = prop.getProperty("main_file"); //получаем название основного файла
        String otdels = prop.getProperty("otdels"); //получаем отделы для выборки
        String types = prop.getProperty("types"); //получаем типы документов для выборки
        String dir = prop.getProperty("dir"); //получаем типы документов для выборки

        // Раскладываем адрес на составляющие
        String[] list = httpServletRequest.getRequestURI().split("/");
        //забираем команду
        String action = list[list.length-1];


        List<String[]> allRows = new ArrayList<>();

        //Build reader instance
        try (CSVReader reader = new CSVReader(new FileReader(mainFileName), ';', '"', '/',0)){
            //Read all rows at once
            allRows = reader.readAll();
            //выбираем необходимый JSP в зависимости что нажато
            switch (action == null ? "info" : action) {
                case "edit":
                    //выбираем номер строки для действия
                    Integer id = Integer.valueOf(httpServletRequest.getParameter("id"));
                    //Получаем строку для редактирования в форме
                    String[] editString = allRows.get(id);

                    //готовим строку с датой для подстановки в форму
                    //т.е. переводим из формата 01 Jun 2020 в 2020-06-01
                    String formDate = "";
                    Locale rLocale = new Locale("ru"); //русская локаль
                    //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", rLocale);
                    SimpleDateFormat newFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                    try {
                        Date date = formatter.parse(editString[1]);
                        formDate = newFormatter.format(date);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //для просмотра файла в браузере
                    httpServletRequest.setAttribute("uploadDir", dir); //папка с файлами документов
                    httpServletRequest.setAttribute("typesFromProps", types); // типы документов из пропертиз
                    httpServletRequest.setAttribute("otdelsFromProps", otdels); // отделы из пропертиз

                    //для вывода на экран документа для редактирования
                    httpServletRequest.setAttribute("number", editString[0]); //номер документа
                    httpServletRequest.setAttribute("data", formDate); //дата
                    httpServletRequest.setAttribute("name", editString[2]); //название
                    httpServletRequest.setAttribute("type", editString[3]); //        тип
                    httpServletRequest.setAttribute("zones", editString[4]); //        зона действия
                    httpServletRequest.setAttribute("opis", editString[5]); //        описание
                    httpServletRequest.setAttribute("fn", editString[7]); //        имя файла
                    httpServletRequest.setAttribute("id", id); //        id
                    RequestDispatcher requestDispatcher = httpServletRequest.getRequestDispatcher("edit.jsp");
                    requestDispatcher.forward(httpServletRequest, httpServletResponse);
                    break;

                case "delete":
                    //выбираем номер строки для действия
                    Integer idd = Integer.valueOf(httpServletRequest.getParameter("id"));
                    //Получаем строку для удаления
                    String[] delString = allRows.get(idd);

                    //для просмотра файла в браузере
                    httpServletRequest.setAttribute("uploadDir", dir);

                    //выводим на экран документ для получения подтверждения
                    httpServletRequest.setAttribute("number", delString[0]); //номер документа
                    httpServletRequest.setAttribute("date", delString[1]); //дата
                    httpServletRequest.setAttribute("name", delString[2]); //название
                    httpServletRequest.setAttribute("type", delString[3]); //        тип
                    httpServletRequest.setAttribute("id", idd); //        id
                    RequestDispatcher requestDispatcher2 = httpServletRequest.getRequestDispatcher("remove.jsp");
                    requestDispatcher2.forward(httpServletRequest, httpServletResponse);
                    break;
                case "info":
                default:
                    //для просмотра файла в браузере
                    httpServletRequest.setAttribute("uploadDir", dir);
                    //File papka = new File("../"+"docbase");
                    //httpServletRequest.setAttribute("papka", papka);
                    httpServletRequest.setAttribute("otdelsFromProps", otdels); // отделы из пропертиз
                    //передаем строки на вывод посредством list.jsp
                    httpServletRequest.setAttribute("allRows", allRows);
                    requestDispatcher = httpServletRequest.getRequestDispatcher("list.jsp");
                    requestDispatcher.forward(httpServletRequest, httpServletResponse);
                    break;
            }
        }
        catch (FileNotFoundException e) {
            String errorMessage = "База данных недоступна.<br>" +
                    "<a href=/upload>Добавьте</a> новый документ, чтобы создать новую.";
            httpServletRequest.setAttribute("errorMessage", errorMessage);
            e.printStackTrace();
            String serviceInformation = e.getLocalizedMessage();
            httpServletRequest.setAttribute("serviceInformation", serviceInformation);

            RequestDispatcher requestDispatcher = httpServletRequest.getRequestDispatcher("error.jsp");
            requestDispatcher.forward(httpServletRequest, httpServletResponse);
        }




    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Properties prop = new Properties();
        Document document = new Document();
        ServletContext servletContext = request.getServletContext();

        // Местоположение веб приложения на жестком диске (hard disk).
        String realPath = servletContext.getRealPath("");

        //Загружаем properties
        FileInputStream fis = new FileInputStream(realPath+PATH_TO_PROPERTIES);
        prop.load(fis);
        String mainFileName = prop.getProperty("main_file"); //получаем название основного файла
        String dir = prop.getProperty("dir"); //получаем типы документов для выборки

        //Build reader instance
        CSVReader reader = new CSVReader(new FileReader(mainFileName), ';', '"', '/',0);
        //Read all rows at once
        List<String[]> allRows = reader.readAll();

        reader.close();
        fis.close();

        // Раскладываем адрес на составляющие
        String[] list = request.getRequestURI().split("/");
        //забираем команду
        String action = list[list.length-1];


        //выбираем необходимый JSP в зависимости что нажато
        switch (action == null ? "info" : action) {
            case "edit":
                try{
                    request.setCharacterEncoding ("UTF-8");
                    //для просмотра файла в браузере
                    request.setAttribute("uploadDir", dir);
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("edited.jsp");
                    //параметры запроса
                    String doc_name = request.getParameter("name");
                    String razrab = request.getParameter("razrab");
                    String data = request.getParameter("data");
                    String number = request.getParameter("number");
                    String tip = request.getParameter("tip");
                    String otdels = prop.getProperty("otdels"); //получаем отделы для выборки
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
                    String otdelsOK = "Не задано";
                    if (!(otd.length()==0))  {
                        otdelsOK=otd.substring(0, otd.length()-2);
                    }

                    //номер строки из запроса
                    Integer id = Integer.valueOf(request.getParameter("iid").trim());

                    //оригинальная строка до изменения
                    String[] origRow = allRows.get(id);

                    //задаем формат даты
                    DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);

                    //Создаем заготовку документа
                    try {
                       document.setName(doc_name);
                       document.setDocType(tip);
                       document.setDate(dateFormat.parse(data));
                       document.setNumber(number);
                       document.setZones(otdelsOK);
                       document.setRazrab(razrab);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    // gets absolute path of the web application
                    //String applicationPath = request.getServletContext().getRealPath("");
                    // constructs path of the directory to save uploaded file
                    //String uploadFilePath = realPath + File.separator + UPLOAD_DIR;

                    //получаем все части из запроса и перебираем
                    // write all files in upload folder
                    for (Part part : request.getParts()) {

                        Long size = part.getSize();
                        String contentType = part.getContentType();


                        //маркер необходимости замены файла документа
                        boolean isNeedReplace;

                        if (!(contentType==null)){

                            //если файл не приложен -> просто заменяем строку в CSV
                            if ((contentType.equals("application/octet-stream")) & size==0){
                                //замена файла документа не требуется
                                isNeedReplace = false;

                                //обработка документа : запись в csv файл (в ту же строку) без замены самого файла
                                List<String[]> editedRows = document.editDocument(allRows, realPath, "", isNeedReplace, id);
                                // TODO: 24.06.2020
                                //запись измененных строк в базу
                                CSVWriter writer = new CSVWriter(new FileWriter(mainFileName), ';'); //создаем с дозаписью
                                writer.writeAll(editedRows);
                                writer.close();

                                //достаем измененну строку
                                String[] editedRow = editedRows.get(id);

                                //и размер файла
                                Long fileSize = getDocumentSize(editedRow, false);

                                //передаем исходную и измененную строку на вывод посредством edited.jsp
                                request.setAttribute("editedRow", editedRow);
                                request.setAttribute("origRow", origRow);
                                request.setAttribute("size", fileSize);


                            } //if нет файла

                            //если файл приложен -> заменяем строку в CSV и сам файл в папке uploads
                            if ((contentType.equals("application/pdf")) & size>0){
                                //необходима замена файла документа
                                isNeedReplace = true;
                                String fileName = extractFileName(part); //получаем имя файла
                                part.write(dir + File.separator + fileName ); //пишем приложенный файл в папку

                                //обработка документа : запись в csv файл (в ту же строку) с заменой самого файла
                                List<String[]> editedRows = document.editDocument(allRows, realPath, fileName, isNeedReplace, id);

                                //запись измененных строк в базу
                                CSVWriter writer = new CSVWriter(new FileWriter(mainFileName), ';'); //создаем с дозаписью
                                writer.writeAll(editedRows);
                                writer.close();

                                //достаем измененну строку
                                String[] editedRow = editedRows.get(id);

                                //и размер файла
                                Long fileSize = getDocumentSize(allRows.get(id), true);
                                //Long c = Long.valueOf()
                                //передаем исходную и измененную строку на вывод посредством edited.jsp
                                request.setAttribute("editedRow", editedRow);
                                request.setAttribute("origRow", origRow);
                                request.setAttribute("size", fileSize);
                                request.setAttribute("newSize", size);

                            } //if есть файл

                        }

                    } //for Part part
                    requestDispatcher.forward(request, response);
                } finally {
                        //writer.close();
                        // br.close();
                }
                break;
            case "delete":
                //параметры запроса
                Integer id = Integer.valueOf(request.getParameter("iid").trim());

                //точно удаляем
                List<String[]> delRows = document.delete(id, allRows); //удаляем строку из List и сам файл

                //записываем строки в базу
                CSVWriter writer = new CSVWriter(new FileWriter(mainFileName), ';'); //создаем с дозаписью
                writer.writeAll(delRows);
                writer.close();

                //для просмотра файла в браузере
                request.setAttribute("uploadDir", dir);

                //передаем строки на вывод посредством list.jsp
                request.setAttribute("allRows", delRows);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("list.jsp");
                requestDispatcher.forward(request, response);
                break;
        }
    }

    /**
     * Extracts file name from HTTP header content-disposition
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }

    private Long getDocumentSize(String[] editedRow, boolean deleteFile){

        File f = new File(editedRow[6]+File.separator+editedRow[7]);
        Long res = f.length();

        if (deleteFile) { //удаляем старый файл
            f.delete();
        }

        return res;
    }

}

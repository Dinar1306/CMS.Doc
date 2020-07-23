package org.mycompany.devlab;


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


@MultipartConfig
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static final String UPLOAD_DIR = "uploads";
    //public int otdelsSize;

    public UploadServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        Properties prop = new Properties();
        ServletContext servletContext = request.getServletContext();

        // Местоположение веб приложения на жестком диске (hard disk).
        String realPath = servletContext.getRealPath("");
        try {
          //Загружаем properties
          try (FileInputStream fis = new FileInputStream(realPath+PATH_TO_PROPERTIES)) {
             prop.load(fis); // что-то делаем со stream
             String otdels = prop.getProperty("otdels"); //получаем отделы для выборки
             request.setAttribute("otdelsFromProps", otdels);

             String types = prop.getProperty("types"); //получаем типы документов для выборки
             request.setAttribute("typesFromProps", types);

             RequestDispatcher requestDispatcher = request.getRequestDispatcher("upload.jsp");
             requestDispatcher.forward(request, response);
          }
          catch (FileNotFoundException e){
            String errorMessage = "Отсутствует файл настроек, запуск невозможен :(";
            request.setAttribute("errorMessage", errorMessage);
            e.printStackTrace();
            String serviceInformation = e.getLocalizedMessage();
            request.setAttribute("serviceInformation", serviceInformation);

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("error.jsp");
            requestDispatcher.forward(request, response);
          }

        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    //@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html; charset=UTF-8");

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try {
            request.setCharacterEncoding ("UTF-8");
        } catch (UnsupportedEncodingException e) {
            String errorMessage = "Ошибка запроса - не поддерживаемая кодировка :(";
            request.setAttribute("errorMessage", errorMessage);
            e.printStackTrace();
            String serviceInformation = e.getLocalizedMessage();
            request.setAttribute("serviceInformation", serviceInformation);

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("error.jsp");
            requestDispatcher.forward(request, response);
        }

        //параметры запроса
        String doc_name = request.getParameter("name");
        String razrab = request.getParameter("razrab");
        //String data = request.getParameter("data");
        String number = ""; //будет составляться в Document.java
        String tip = request.getParameter("tip");


        ServletContext servletContext = request.getServletContext();

        // Местоположение веб приложения на жестком диске (hard disk).
        String realPath = servletContext.getRealPath("");

        //Загружаем properties
        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream(realPath+PATH_TO_PROPERTIES)) {
            properties.load(fis); // что-то делаем со stream
        }
        catch (FileNotFoundException e) {
            String errorMessage = "Отсутствует файл настроек, запуск невозможен :(";
            request.setAttribute("errorMessage", errorMessage);
            e.printStackTrace();
            String serviceInformation = e.getLocalizedMessage();
            request.setAttribute("serviceInformation", serviceInformation);

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("error.jsp");
            requestDispatcher.forward(request, response);
        }

        String otdels = properties.getProperty("otdels"); //получаем отделы (область действия)
        //готовим строку для вывода только выбранного в форме отдела
        String otd = "";
        String[] tempo = otdels.split(";");
        int otdelsSize = tempo.length;
        for (int i = 0; i<otdelsSize; i++){
            String[] ss = tempo[i].split("-");
            if   (request.getParameter("otdel").equals(ss[1]))  { //если равно полученному из формы
                    otd=otd+ss[0]; // берем начало (т.е. сокращ. вариант)
            }
        }

        String types = properties.getProperty("types"); //получаем виды документов
        //готовим строку для вывода только выбранного в форме вида документа
        String vid = "";
        String[] vidy = types.split(";");
        int typesSize = vidy.length;
        for (int i = 0; i<typesSize; i++){
            String[] ss1 = vidy[i].split("-");
            if   (request.getParameter("tip").equals(ss1[1]))  { //если равно полученному из формы
                vid=vid+ss1[0];  // берем начало (т.е. сокращ. вариант)
            }
        }

        //создадим документ по параметрам
        //DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        //Document document = null;
//        try {
            Document document = new Document(doc_name, vid, /*dateFormat.parse(data),*/ number, otd, razrab);
//        } catch (ParseException e) {
//            String errorMessage = "Ошибка преобразования даты документа :(";
//            request.setAttribute("errorMessage", errorMessage);
//            e.printStackTrace();
//            String serviceInformation = e.getLocalizedMessage();
//            request.setAttribute("serviceInformation", serviceInformation);
//
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher("error.jsp");
//            requestDispatcher.forward(request, response);
//        }


        // обработка загруженного файла
        // gets absolute path of the web application
        String applicationPath = request.getServletContext().getRealPath("");
        // constructs path of the directory to save uploaded file
        //String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
        String uploadFilePath = properties.getProperty("dir"); //получаем отдел (область действия);
        // creates upload folder if it does not exists
        File uploadFolder = new File(uploadFilePath);
        if (!uploadFolder.exists()) {  //если папки не существует, то создаем
            uploadFolder.mkdirs();
        }
        //int count = 0;
        try {

        //получаем part файла из запроса
        // write all files in upload folder
        for (Part part : request.getParts()) {

            if (part != null && part.getSize() > 0) {

                String contentType = part.getContentType();
                Long size = part.getSize();

                if (!(contentType==null)) {
                    String fileName = extractFileName(part);
                    part.write(uploadFilePath + fileName);
                    //запись в csv файл
                    number = document.storeDoc(realPath, fileName); //получаем запись вида СОП.КДЛ-003-2020

                    request.setAttribute("doc_name", doc_name);

                    request.setAttribute("number", number);

                  //  request.setAttribute("data", data);

                    request.setAttribute("tip", tip);

                    request.setAttribute("otd", otd);
                    request.setAttribute("razrab", razrab);

                    request.setAttribute("size", size);

                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("uploaded.jsp");
                    requestDispatcher.forward(request, response);
                }


            }

        } //for Part part



        }
        catch (IOException e){

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


}
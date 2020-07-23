<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles/w3.css">
    <link rel="stylesheet" href="styles/list.css">

</head>
    <body class="w3-light-grey">
      <div class="w3-container w3-blue-grey w3-opacity w3-right-align">
                <h1>СМК.Док 1.0</h1>
      </div>

    <%@ page import="java.util.List" %>
    <%@ page import="java.io.*" %>


      <div class="w3-container w3-center">
                  <div class="w3-bar w3-padding-large w3-padding-16">
                      <button class="w3-btn w3-hover-light-blue w3-round-large" onclick="location.href='list'">Список</button>
                      <button class="w3-btn w3-hover-light-blue w3-round-large" onclick="location.href='find'">Найти</button>
                      <button class="w3-btn w3-hover-light-blue w3-round-large" onclick="location.href='upload'">Добавить</button>
                      <button class="w3-btn w3-hover-light-blue w3-round-large" onclick="location.href='/help.html'">Помощь</button><br>
                  </div>
              </div>
        <b>Всего документов:
        <%
        List<String[]> allRows = (List<String[]>) request.getAttribute("allRows");
        out.println(allRows.size());

        //out.println("<br>");
        //File papka = (File) request.getAttribute("papka");
        //out.println("<a href=\"file://D:/docbase/001-2020.docx\" >просм.</a>");

        //получаем отделы из файла properties
                            String otdels = (String) request.getAttribute("otdelsFromProps");
                            String[] temp2 = otdels.split(";");
        %>
        </b>
        <br>
        <%  //выводим csv файл

                    String st = (String)request.getAttribute("uploadDir");
                    if (allRows != null && !allRows.isEmpty()) {
                                            out.println("<div class=\"divTable\" width=\"90%\" >");
                                            out.println("<div class=\"divTableBody\">");
                                            Integer j = 0; // счетчик строк
                                            //заголовок таблицы
                                            out.println("<div class=\"divTableRow\">");
                                                out.println("<div class=\"divTableHead\"><b>№ п/п</b></div>");
                                                out.println("<div class=\"divTableHead\"><b>Номер</b></div>");
                                                out.println("<div class=\"divTableHead\"><b>Утвержден</b></div>");
                                                out.println("<div class=\"divTableHead\"><b>Название</b></div>");
                                                out.println("<div class=\"divTableHead\"><b>Вид</b></div>");
                                                out.println("<div class=\"divTableHead\"><b>Область применения</b></div>");
                                                out.println("<div class=\"divTableHead\"><b>Разработал(а)</b></div>");
                                                out.println("<div class=\"divTableHead\"><b>Просмотр</b></div>");
                                                out.println("<div class=\"divTableHead\"><b>Редактирование</b></div>");
                                                out.println("<div class=\"divTableHead\"><b>Удаление</b></div>");
                                            out.println("</div>");
                                            for (String[] s : allRows) {
                                                int length = s.length;
                                                out.println("<div class=\"divTableRow\">");
                                                //выводим ячейку с порядковым номером строки таблицы
                                                out.print("<div class=\"divTableCell\">"+(++j)+"</div>");
                                                for(int i=0; i<length-3; i++){
                                                    //выводим остальные ячейки (до кнопок действий)
                                                    //сначала составляем номеро
                                                    if(i==0){
                                                            out.print("<div class=\"divTableCell\">"+s[7].substring(0, s[7].lastIndexOf("."))+"</div>");
                                                    }
                                                    else if(i==4){
                                                         String otdel = "";
                                                         for (String s0 : temp2) {
                                                            String[] ss = s0.split("-");
                                                            if (s[i].equals(ss[0])){
                                                                otdel = ss[1];
                                                            }
                                                         }
                                                         out.print("<div class=\"divTableCell\">"+otdel+"</div>");
                                                    }
                                                    else    out.print("<div class=\"divTableCell\">"+s[i]+"</div>");
                                                }
                                                //uploadDir + название файла
                                                String str = st+s[length-2];
                                                //out.println("<div class=\"divTableCell\">"+str+"</div>");
                                                out.println("<div class=\"divTableCell w3-center\"><a class=\"w3-button w3-ripple w3-teal\" href=\""+str+"\" >просм.</a></div>");
                                                out.println("<div class=\"divTableCell w3-center\"><a class=\"w3-button w3-ripple w3-teal\" href=\"edit?id="+(j-1)+"\" >редак.</a></div>");
                                                out.println("<div class=\"divTableCell w3-center\"><a class=\"w3-button w3-ripple w3-teal\"  href=\"delete?id="+(j-1)+"\" >удал.</a></div>");
                                                out.println("</div>");
                                                //j++;
                                            }

                                            out.println("</div>");
                                            out.println("</div>");

                                        } else out.println("<div class=\"w3-panel w3-red w3-display-container w3-card-4 w3-round\">\n"
                    +
                                                "   <span onclick=\"this.parentElement.style.display='none'\"\n" +
                                                "   class=\"w3-button w3-margin-right w3-display-right w3-round-large w3-hover-red w3-border w3-border-red w3-hover-border-grey\">×</span>\n" +
                                                "   <h5>There are no documents yet!</h5>\n" +
                                                "</div>");
                %>



        <br>
        <br>
        <br>
        <a href="mailto:tmcufa@mail.ru">Сообщить о проблеме</a>
    </body>
</html>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles/w3.css">
    <link rel="stylesheet" href="styles/list.css">

</head>
    <body class="w3-light-grey">
      <div class="w3-container w3-blue-grey w3-opacity w3-right-align" id="fullscreen">
                      <h1>СМК.Док 1.0</h1>
      </div>

    <%@ page import="java.util.List" %>


      <div class="w3-container w3-center">
                  <div class="w3-bar w3-padding-large w3-padding-16">
                      <button class="w3-btn w3-hover-light-blue w3-round-large" onclick="location.href='list'">Список</button>
                      <button class="w3-btn w3-hover-light-blue w3-round-large" onclick="location.href='find'">Найти</button>
                      <button class="w3-btn w3-hover-light-blue w3-round-large" onclick="location.href='upload'">Добавить</button>
                      <button class="w3-btn w3-hover-light-blue w3-round-large" onclick="location.href='/help.html'">Помощь</button><br>
                  </div>
              </div>
        <b>Найдено документов:
        <%
        List<String[]> findedRows = (List<String[]>) request.getAttribute("findedRows");
        String dataFrom = (String) request.getAttribute("dataFrom");
        String dataTo = (String) request.getAttribute("dataTo");
        out.println(findedRows.size()+"   |   ");
        %>
        Параметры поиска ==></b>
        <%
        String[] toFind = (String[]) request.getAttribute("toFind");
        if (!(toFind[0].equals(""))) out.print("Номер: " + toFind[0] + "; ");
        if (!(dataFrom.equals(""))) out.print("Дата: c " + dataFrom + " ");
        if (!(dataTo.equals(""))) out.print("по " + dataTo + "; ");
        if (!(toFind[2].equals(""))) out.print("Название: " + toFind[2] + "; ");
        if (!(toFind[3].equals(""))) out.print("Тип: " + toFind[3] + "; ");
        if (!(toFind[4].equals(""))) out.print("Зона действия: " + toFind[4] + "; ");
        if (!(toFind[5].equals(""))) out.print("Описание: " + toFind[5] + "; ");
        %>
        <br>
        <%  //выводим csv файл

                    String st = (String)request.getAttribute("uploadDir");
                    if (findedRows != null && !findedRows.isEmpty()) {
                                            out.println("<div class=\"divTable\" width=\"90%\" >");
                                            out.println("<div class=\"divTableBody\">");
                                            Integer j = 0; // счетчик строк
                                            //заголовок таблицы
                                            out.println("<div class=\"divTableRow\">");
                                                out.println("<div class=\"divTableHead\"><b>Номер</b></div>");
                                                out.println("<div class=\"divTableHead\"><b>Дата</b></div>");
                                                out.println("<div class=\"divTableHead\"><b>Название</b></div>");
                                                out.println("<div class=\"divTableHead\"><b>Тип</b></div>");
                                                out.println("<div class=\"divTableHead\"><b>Зона действия</b></div>");
                                                out.println("<div class=\"divTableHead\"><b>Описание</b></div>");
                                                out.println("<div class=\"divTableHead\"><b>Просмотр</b></div>");
                                                out.println("<div class=\"divTableHead\"><b>Редактирование</b></div>");
                                                out.println("<div class=\"divTableHead\"><b>Удаление</b></div>");
                                            out.println("</div>");
                                            for (String[] s : findedRows) {
                                                int length = s.length;
                                                out.println("<div class=\"divTableRow\">");
                                                for(int i=0; i<length-2; i++){
                                                    //

                                                    //выводим ячейку
                                                    out.println("<div class=\"divTableCell\">"+s[i]+"</div>");
                                                }
                                                //uploadDir + название файла
                                                String str = st+"\\"+s[length-1];
                                                //out.println("<div class=\"divTableCell\">"+str+"</div>");
                                                out.println("<div class=\"divTableCell w3-center\"><a class=\"w3-button w3-ripple w3-teal\" href=\""+str+"\" >просм.</a></div>");
                                                out.println("<div class=\"divTableCell w3-center\"><a class=\"w3-button w3-ripple w3-teal\" href=\"edit?id="+j+"\" >редак.</a></div>");
                                                out.println("<div class=\"divTableCell w3-center\"><a class=\"w3-button w3-ripple w3-teal\"  href=\"delete?id="+j+"\" >удал.</a></div>");
                                                out.println("</div>");
                                                j++;
                                            }

                                            out.println("</div>");
                                            out.println("</div>");

                                        } else out.println("<div class=\"w3-panel w3-red w3-display-container w3-card-4 w3-round\">\n"
                    +
                                                "   <span onclick=\"this.parentElement.style.display='none'\"\n" +
                                                "   class=\"w3-button w3-margin-right w3-display-right w3-round-large w3-hover-red w3-border w3-border-red w3-hover-border-grey\">×</span>\n" +
                                                "   <h5>Документы не найдены :(</h5>\n" +
                                                "</div>");
                %>



        <br>
        <br>
        <br>
        <a href="mailto:tmcufa@mail.ru">Сообщить о проблеме</a>
    </body>
</html>

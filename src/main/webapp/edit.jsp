<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import ="java.util.*" %>
<%@ page import ="java.io.*" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles/w3.css">
    <title>Edit document here</title>
</head>
<body class="w3-light-grey">
      <div class="w3-container w3-blue-grey w3-opacity w3-right-align" id="fullscreen">
                      <h1>СМК.Док 1.0</h1>
      </div>
      <div class="w3-container w3-center">
                        <div class="w3-bar w3-padding-large w3-padding-24">
                            <button class="w3-btn w3-hover-light-blue w3-round-large" onclick="location.href='list'">Список</button>
                            <button class="w3-btn w3-hover-light-blue w3-round-large" onclick="location.href='find'">Найти</button>
                            <button class="w3-btn w3-hover-light-blue w3-round-large" onclick="location.href='upload'">Добавить</button>
                            <button class="w3-btn w3-hover-light-blue w3-round-large" onclick="location.href='/help.html'">Помощь</button><br>
                        </div>
      </div>
<div class="w3-content w3-center">

<!--получаем данные документа-->
            <% String imya = (String) request.getAttribute("name");
               String n = (String) request.getAttribute("number");
               String t = (String) request.getAttribute("type");
               String d = (String) request.getAttribute("data");
               Integer id = (Integer) request.getAttribute("id");
               String opis = (String) request.getAttribute("opis");
               String uploadDir = (String) request.getAttribute("uploadDir");
               String fn = (String) request.getAttribute("fn");
               //Long r = (Long) request.getAttribute("size");
               %>

<span class="w3-left">Измените данные в полях формы и прикрепите (при необходимости) файл в формате PDF:</span>
</div><br>
<div class="w3-content w3-center">
<form action="edit" enctype="multipart/form-data" method="POST">
<!--enctype="application / x-www-form-urlencoded"  multipart/form-data  -->
    <fieldset>
        <legend>Редактирование документа</legend>
        <p><label class="w3-left">Название </label><input class="w3-input" name="name" type="text" id="name" value="<% out.print(imya); %>" required></p>
        <p><label class="w3-left">Дата&nbsp;</label><input class="w3-input w3-quarter" name="data" type="date" id="data" value="<% out.print(d); %>" required></p>
        <p><label class="w3-left">&nbsp;&nbsp;Номер&nbsp;</label><input class="w3-input w3-quarter" name="number" type="text" id="number" value="<% out.print(n); %>" required></p>
        <p><label class="w3-left">&nbsp;&nbsp;Тип&nbsp;</label>

        <select class="w3-input w3-quarter" name="tip" id="tip" required>
        <%  //получаем типы документов из файла properties
            String types = (String) request.getAttribute("typesFromProps");
            //получаем тип документа для редактирования
            String type = (String) request.getAttribute("type");
            String[] temp = types.split(";");

            //если тип совпадает, значит пункт выбран по умолчанию
            if (temp != null && temp.length!=0){
                for (String s : temp){
                    if (s.equals(type)) {
                         out.println("<option selected>" + s + "</option>");
                    }
                    else {
                         out.println("<option >" + s + "</option>");
                    }

                }
            }
        %>
        </select>
        </p>
        <br><br>
        <p><label>Действует для: </label>
        <%          //получаем отделы из файла properties
                    String otdels = (String) request.getAttribute("otdelsFromProps");
                    String[] temp2 = otdels.split(";");

                    //получаем зоны действия документа для редактирования
                    String zones = (String) request.getAttribute("zones");
                    String[] docZones = zones.split(", ");

                    //если есть в множестве, значит чекбокс с галкой
                    Set<String> targetSet = new HashSet<String>();
                    Collections.addAll(targetSet, docZones);

                    if (temp2 != null && temp2.length!=0){
                        String galka = "";
                        for (int i=0; i<temp2.length; i++){
                                 if (targetSet.contains(temp2[i])) {
                                    galka = " checked";
                                 }
                                 out.print("<input type=\"checkbox\" name=\"ch");
                                 out.print(i);
                                 out.print("\"");
                                 out.print(galka);
                                 out.print("/>");

                                 //out.println("\"+" "+galka+/>" +"&nbsp;"+ temp2[i]);
                                 out.println("&nbsp;" + temp2[i]);
                                 galka = "";
                        }
                    }
         %>
        </p>
        <p><label class="w3-left">Описание </label><input class="w3-input" name="opisanie" id="opisanie" value="<% out.print(opis); %>"></p>

        <p>Скан документа: <button type = "submit" formaction = "<% out.print(uploadDir);
                                                                    out.print(File.separator);
                                                                    out.print(fn); %>">Открыть</button>
           &nbsp;&nbsp;//&nbsp;&nbsp;Заменить файл документа? <input name="file" type="file" id="file" accept=".pdf" ></p>

        <p><input name="iid" type="hidden" value="<% out.print(id); %>"></p>

        <!--<input type="submit" value="ДААААА">-->

    </fieldset>

    <p><button type="submit" >Изменить</button></p>
</form>

</div>
</div>
</div>
                <br>
                <br>
                <br>
                <a href="mailto:tmcufa@mail.ru">Сообщить о проблеме</a>
</body>
</html>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles/w3.css">
    <title>Wrong dates!</title>
</head>
<body class="w3-light-grey">
      <div class="w3-container w3-blue-grey w3-opacity w3-right-align">
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
            <%
               String dataFrom = (String) request.getAttribute("dataFrom");
               String dataTo = (String) request.getAttribute("dataTo");
            %>

<span class="w3-left">Задайте параметры поиска в полях формы:</span>
</div><br>
<div class="w3-content w3-center">
<form action="find" enctype="multipart/form-data" method="POST">
<!--enctype="application / x-www-form-urlencoded"  multipart/form-data  -->
    <fieldset>
        <legend>Данные документа</legend>
        <p><label class="w3-left">Название </label><input class="w3-input" name="name" type="text" id="name" ></p>
        <p><label class="w3-left">Дата:&nbsp;c&nbsp;&nbsp;</label><input class="w3-input w3-quarter" name="data1" type="date" id="data1" value="<% out.print(dataFrom); %>" ></p>

        <p><label class="w3-left">&nbsp;&nbsp;Номер&nbsp;</label><input class="w3-input w3-quarter" name="number" type="text" id="number" ></p>
        <p><label class="w3-left">&nbsp;&nbsp;Тип&nbsp;</label>

        <select class="w3-input w3-quarter" name="tip" id="tip" >
        <%  //выводим типы документов из файла properties
            String types = (String) request.getAttribute("typesFromProps");
            String[] temp = types.split(";");

            if (temp != null && temp.length!=0){
                out.println("<option ></option>");
                for (String s : temp){
                    out.println("<option >" + s + "</option>");
                }
            }
        %>
        </select>
        </p>
        <br><br>
        <p><label class="w3-left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;по&nbsp;</label><input class="w3-input w3-quarter" name="data2" type="date" id="data2" value="<% out.print(dataTo); %>" ></p>

        <p><label>Действует для: </label>
        <%  //выводим отделы из файла properties
                    String otdels = (String) request.getAttribute("otdelsFromProps");
                    String[] temp2 = otdels.split(";");

                    if (temp2 != null && temp2.length!=0){
                        for (int i=0; i<temp2.length; i++){
                                 out.print("<input type=\"checkbox\" name=\"ch");
                                 out.print(i);
                                 out.println("\"/>" + "&nbsp;" + temp2[i]);
                        }
                    }
         %>
        </p><br>
        <p><label class="w3-left">Описание </label><input class="w3-input" name="opisanie" id="opisanie"></p>





    </fieldset>
    <!--<p><input type="submit" value="Найти"></p>-->
    <p><button type="submit" >Найти</button></p>
    <p>Не корректный диапазон дат! Пожалуйста исправьте.</p>
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
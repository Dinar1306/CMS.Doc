<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" %>
<html lang="en">
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="styles/w3.css">
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
            <h3>
               Документ успешно добавлен!
            </h3>
            <!--выводим документ-->
            <% String s = (String) request.getAttribute("doc_name");
               String n = (String) request.getAttribute("number");
               String d = (String) request.getAttribute("data");
               String t = (String) request.getAttribute("tip");
               String o = (String) request.getAttribute("otd");
               String opis = (String) request.getAttribute("razrab");
               Long r = (Long) request.getAttribute("size");
               %>
        <center>
        <table border="0" class="w3-center">
                <tr><td align="right"><b>Название:</b></td><td align="left"><%out.println(s); %></td></tr>
                <tr><td align="right"><b>Тип:</b></td><td align="left"><%out.println(t); %></td></tr>
                <tr><td align="right"><b>Номер:</b></td><td align="left"><%out.println(n); %></td></tr>
                <tr><td align="right"><b>Дата:</b></td><td align="left"><%out.println(d); %></td></tr>
                <tr><td align="right"><b>Действует:</b></td><td align="left"><%out.println(o); %></td></tr>
                <tr><td align="right"><b>Описание:</b></td><td align="left"><%out.println(opis); %></td></tr>
                <tr><td align="right"><b>Размер:</b></td><td align="left"><%out.println(r); %> байт</td></tr>
        </table>
        <br>


        </center>




      </div>
                      <br>
                      <br>
                      <br>
                      <a href="mailto:tmcufa@mail.ru">Сообщить о проблеме</a>

    </body>
</html>

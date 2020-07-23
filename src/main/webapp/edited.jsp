<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" %>
<html lang="en">
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="styles/w3.css">
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
            <h3>
               Документ успешно изменён!
            </h3>
            <!--выводим документ-->
            <% String[] orig = (String[]) request.getAttribute("origRow");
               String[] edit = (String[]) request.getAttribute("editedRow");

               // тут надо и старый и новый размер
               Long s = (Long) request.getAttribute("size");
               Long s2 = (Long) request.getAttribute("newSize");
                //размер нового или старого файла
               Long c = s;
               if (!(s2==null)) { //есть новый файл
                    c = s2;
               }

               %>
        <center>
        <table border="0" class="w3-center">
                <tr><td align="right"><b>Текущее</b><td align="center">&nbsp;</td><td align="left"><b>&nbsp;&nbsp;Новое</b></td></tr>
                <tr><td align="right"><%out.println(orig[2]); %></td><td align="center"><b>Название</b></td><td align="left"><%out.println(edit[2]); %></td></tr>
                <tr><td align="right"><%out.println(orig[3]); %></td><td align="center"><b>Тип</b></td><td align="left"><%out.println(edit[3]); %></td></tr>
                <tr><td align="right"><%out.println(orig[0]); %></td><td align="center"><b>Номер</b></td><td align="left"><%out.println(edit[0]); %></td></tr>
                <tr><td align="right"><%out.println(orig[1]); %></td><td align="center"><b>Дата</b></td><td align="left"><%out.println(edit[1]); %></td></tr>
                <tr><td align="right"><%out.println(orig[4]); %></td><td align="center"><b>Действует</b></td><td align="left"><%out.println(edit[4]); %></td></tr>
                <tr><td align="right"><%out.println(orig[5]); %></td><td align="center"><b>Описание</b></td><td align="left"><%out.println(edit[5]); %></td></tr>
                <tr><td align="right"><%out.println(s); %>  байт</td><td align="center"><b>Размер</b></td><td align="left"><%out.println(c); %> байт</td></tr>
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

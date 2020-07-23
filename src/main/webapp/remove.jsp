<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" %>
<html lang="en">
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="styles/w3.css">
<title>Remove document here</title>
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
               Точно удалить документ?
            </h3>
            <!--получаем данные документа-->
            <% String s = (String) request.getAttribute("name");
               String n = (String) request.getAttribute("number");
               String t = (String) request.getAttribute("type");
               String d = (String) request.getAttribute("date");
               Integer id = (Integer) request.getAttribute("id");
               %>
        <center>
        <table border="0" class="w3-center">
                <tr><td align="right"><b>Название:</b></td><td align="left"><%out.println(s); %></td></tr>
                <tr><td align="right"><b>Тип:</b></td><td align="left"><%out.println(t); %></td></tr>
                <tr><td align="right"><b>Номер:</b></td><td align="left"><%out.println(n); %></td></tr>
                <tr><td align="right"><b>Дата:</b></td><td align="left"><%out.println(d); %></td></tr>
        </table>
        <br>

        <table border="0" width="100px" class="w3-center">
                <tr>
                      <td><form action = "delete" method="POST" class="w3-button w3-ripple w3-teal">
                                      <input type="submit" value="ДА">
                                      <input name="iid" type="hidden" value="<% out.print(id); %>">
                           </form>
                      </td>
                      <td><form action="list"  method="GET" class="w3-button w3-ripple w3-teal">
                                      <input type="submit" value="НЕТ">
                          </form>
                      </td>
                </tr>

        </table>
        </center>




      </div>



        <br>
                <br>
                <br>
                <a href="mailto:tmcufa@mail.ru">Сообщить о проблеме</a>
    </body>
</html>

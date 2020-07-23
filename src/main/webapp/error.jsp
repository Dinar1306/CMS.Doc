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
             <%
                    String errorMessage = (String) request.getAttribute("errorMessage");
                    out.println(errorMessage);
             %>
        </h3>

        <br><br><br><br>

        <p align="left">
            <%
                String serviceInformation = (String) request.getAttribute("serviceInformation");
                out.println("Сервисная информация:");
                out.println(serviceInformation);
            %>
        </p>

      </div>
                      <br>
                      <br>
                      <br>
                      <br>
                      <br>
                      <br>
                      <a href="mailto:tmcufa@mail.ru">Сообщить о проблеме</a>
    </body>
</html>

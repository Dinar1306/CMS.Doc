<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles/w3.css">
    <title>Upload document here</title>
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

<span class="w3-left">Заполните поля формы и прикрепите файл в формате DOC или DOCX:</span>
</div><br>
<div class="w3-content w3-center">
<form action="upload" enctype="multipart/form-data" method="POST">
<!--enctype="application / x-www-form-urlencoded"  multipart/form-data  -->
    <fieldset>
        <legend>Добавление документа</legend>
        <p><label class="w3-left">Название </label><input class="w3-input" name="name" type="text" id="name" required></p>
        <!--<p><label class="w3-left">Дата&nbsp;</label><input class="w3-input w3-quarter" name="data" type="date" id="data" required></p>-->
        <!--<p><label class="w3-left">&nbsp;&nbsp;Номер&nbsp;</label><input class="w3-input w3-quarter" name="number" type="text" id="number" required></p>-->

        <label class="w3-left">&nbsp;&nbsp;Вид&nbsp;</label>

        <select class="w3-input w3-quarter" name="tip" id="tip" required>
        <%  //выводим типы документов из файла properties
            String types = (String) request.getAttribute("typesFromProps");
            String[] temp = types.split(";");

            if (temp != null && temp.length!=0){
                for (String s : temp){
                        String[] ss = s.split("-");
                        out.println("<option>" + ss[1] + "</option>");
                }

            }
        %>
        </select>

        <p><label class="w3-left">&nbsp;&nbsp;Применение&nbsp;</label>
        <select class="w3-input w3-quarter" name="otdel" id="otdel" required>
        <%  //выводим отделы из файла properties
                    String otdels = (String) request.getAttribute("otdelsFromProps");
                    String[] temp2 = otdels.split(";");

                    if (temp2 != null && temp2.length!=0){
                        for (String s : temp2){
                                String[] ss = s.split("-");
                                out.println("<option>" + ss[1] + "</option>");
                        }
                    }
         %>
         </select>
        </p>
        <p><label class="w3-left">&nbsp;&nbsp;Разработал(а)&nbsp;</label><input class="w3-input w3-quarter" name="razrab" type="text" id="razrab"></p>
        <br><br><br>
        <p><input name="file" type="file" id="file" accept=".doc,.docx" required></p>



    </fieldset>
    <!--<p><input type="submit" value="Отправить"></p>-->
    <p><button type="submit" >Отправить</button></p>
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
<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.1/css/all.css"
          integrity="sha384-gfdkjb5BdAXd+lj+gudLWI+BXq4IuLW5IT+brZEZsLFm++aCMlF1V92rMkPaX4PP" crossorigin="anonymous">
    <title>Приложение TODO</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
</head>

<body onload="return doTask('00');">
    <div class="container pt-3">
    <div class="row">
        <ul class="nav">
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp"> <c:out value="${user.name}"/> | Выйти</a>
            </li>
        </ul>
    </div>
    <div class="row">
        <div class="card" style="width: 100%">
          

            <div class="card-body">
                <div class="container-fluid">
    <h2 align="center"><span><span style="color: green;">TO</span><span style="color: red;">DO</span> список</h2>
</div>
<div class="container">
    <form action="<%=request.getContextPath()%>/task.do" method="post">
        <label for="description">Новая задача:</label><br>
        <textarea title="Введите описание задачи." id="description" name="description" rows="4" cols="50"></textarea>
        <br><br>
        <button type="submit" class="btn btn-default" onclick="return validate();">Добавить</button>
    </form><br>
    <h2>Задачи:</h2><br>
    <table class="table" id="table">
        <thead>
        <input type="checkbox" id="checkbox2" onclick="return checkCheckbox();"> показать все</input>
        <tr>
            <th><i class="fa fa-check" style="color:green"> - выполнено</i></th>
            <th>Дата начала</th>
            <th>Описание</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${tasks}" var="task">
            <tr>
                <td>
                    <input id="checkbox1" value=<c:out value="${task.id}"/> type="checkbox" onclick="return doTask(value);">
                        <c:out value="${task.id}"/></input>
                </td>
                <td>
                    <c:out value="${task.created}"/>
                </td>
                <td>
                    <c:out value="${task.description}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <br>
</div> 
            </div>
        </div>
    </div>
</div>
    
  
    <%
        if (request.getSession().getAttribute("user") == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
    %>
 
    
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
<script src="https://code.jquery.com/jquery-3.4.1.min.js" ></script>

<script>
    
    function validate() {
        if ($('#description').val() == "") {
            alert($('#description').attr('title'));
            return false;
        }
        addTask();
    }

    function checkCheckbox() {
        console.log("start checkCheckbox");
        const checkbox = document.querySelector('#checkbox2');
        if ( checkbox.checked ) {
            console.log("checkbox2 - checked");
            showAll();
        } else {
            console.log("checkbox2 - not checked");
            doTask("00");
        }
    }

    function addTask() {
        let query = JSON.stringify($('#description').val());
        $.ajax({
            type: "POST",
            url: 'http://localhost:8080/todo/task.do',
            data: query,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(data) {
                console.log("success");

                console.log(data);
            },
            error: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    function doTask(id) {
        console.log("start doTask");
        let options = {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            weekday: 'long',
            timezone: 'UTC',
            hour: 'numeric',
            minute: 'numeric'
        };
        $.ajax({
            type: "POST",
            url: 'http://localhost:8080/todo/do.do',
            data: JSON.stringify(id),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(data) {
                console.log(data);
                let table;
                for(let i=0; i<data.length; i++) {
                    table += '<tr><td>' + '<input onclick="return doTask(value);" type="checkbox" value=' + data[i].id + '/> ' + '</td><td>'
                        + new Date(data[i].created).toLocaleString("ru", options) + '</td><td>' + data[i].description + '</td></tr>';
                }
                $('#table > tbody').empty().append(table);
            },
            error: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    function showAll() {
  
        console.log("start showAll");
        let options = {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            weekday: 'long',
            timezone: 'UTC',
            hour: 'numeric',
            minute: 'numeric'
        };
        $.ajax({
            type: "POST",
            url: 'http://localhost:8080/todo/show.do',
            data: JSON.stringify("request"),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(data) {
                console.log(data);
                let table;
                for(let i=0; i<data.length; i++) {
                    let checkbox = '<input onclick="return doTask(value);" type="checkbox" value=' + data[i].id + '/> ';
                    let color = ' style=\"background-color:#98FB98;\"';
                    if(data[i].done) {color = ' style=\"background-color:#FA8072;\"'; checkbox = 'выполнено';}
                    table += '<tr><td>' + checkbox + '</td><td'+color+'>'
                        + new Date(data[i].created).toLocaleString("ru", options) + '</td><td'+color+'>' + data[i].description + '</td></tr>';
                }
                $('#table > tbody').empty().append(table);
            },
            error: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

</script>


</body>
</html>

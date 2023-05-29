<!DOCTYPE html>
<html lang="en" xmlns:th="http://thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Main</title>
</head>
<style>
    .main{
        border:2px solid gray;
        padding: 10px;
    }
    button{
        margin: 5px;
    }
</style>
<body>
<h4>Library management</h4>
<div class="main">
    <div>
        <a href="/people"><button>List of readers</button></a>
        <a href="/books"><button>List of books</button></a>
    </div>
    <div>
        <a href="/people/0"><button>Check library storage</button></a>
    </div>
    <div>
        <a href="/books/control"><button>Manage books</button></a>
    </div>
</div>

</body>
</html>

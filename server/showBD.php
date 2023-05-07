<?php // сохранить utf-8 !
// -------------------------------------------------------------------------- логины пароли
$mysql_host = "maxmaee4.beget.tech"; // sql сервер
$mysql_user = "maxmaee4_diploma"; // пользователь
$mysql_password = "Tarakan96"; // пароль
$mysql_database = "maxmaee4_diploma"; // имя базы данных 
// -------------------------------------------------------------------------- если база недоступна
if (!mysqli_connect($mysql_host, $mysql_user, $mysql_password)){
	
echo "<h2>База недоступна!</h2>";
exit;
}else{
// -------------------------------------------------------------------------- если база доступна
echo "<h2>База доступна!</h2>";

$mysql_connection= mysqli_connect($mysql_host, $mysql_user, $mysql_password);
mysqli_select_db($mysql_connection,$mysql_database);
mysqli_set_charset($mysql_connection,'utf8');
// -------------------------------------------------------------------------- выведем JSON
$q=mysqli_query($mysql_connection,"SELECT * FROM users");
echo "<h3>Json ответ:</h3>";
// Выводим json
while($e=mysqli_fetch_assoc($q))
        $output[]=$e;
print(json_encode($output,JSON_UNESCAPED_UNICODE));

}
mysqli_close($mysql_connection);
// -------------------------------------------------------------------------- разорвем соединение с БД
?>



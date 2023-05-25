<?php
error_reporting(E_ALL);
ini_set('display_startup_errors', 1);
ini_set('display_errors', '1');
// необходимые HTTP-заголовки
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers:*");
//функция для перехвата запроса 
// получаем соединение с базой данных
include_once "../config/database.php";

// создание объекта Юзера
include_once "../objects/friends.php";


$database = new Database();
$db = $database->getConnection();
$friend = new Friend($db);

// получаем отправленные данные
$data = json_decode(file_get_contents("php://input"));
$friend->friend1 = $data->friend1;
$friend->friend2 = $data->friend2;


if (
    !empty($friend->friend1) &&
    !empty($friend->friend2) &&
    $friend->isFriends()){
    if($friend->create()){
       // Устанавливаем код ответа
    http_response_code(200);
 
    // Покажем сообщение о том, что пользователь был создан
    echo json_encode(array("message" => "Дружба была создана"), JSON_UNESCAPED_UNICODE);
}  
    }
 
// Сообщение, если не удаётся создать пользователя
else {
    
 
    // Устанавливаем код ответа
    http_response_code(400);
 
    // Покажем сообщение о том, что создать пользователя не удалось
    echo json_encode(array("message" => "Невозможно создать дружбу" ),JSON_UNESCAPED_UNICODE);
    
}
?>
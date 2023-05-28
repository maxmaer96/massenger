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

// создание объекта сообщение
include_once "../objects/messages.php";


$database = new Database();
$db = $database->getConnection();
$message = new Message($db);

// получаем отправленные данные
$data = json_decode(file_get_contents("php://input"));
$message->text = $data->text;
$message->to_who = $data->to_who;
$message->from_who = $data->from_who;
if (
    !empty($message->text) &&
    !empty($message->to_who) &&
    !empty($message->from_who)&&
    $message->create()){
       // Устанавливаем код ответа
    http_response_code(200);
 
    // Покажем сообщение о том, что пользователь был создан
    echo json_encode(array("message" => "Сообщение было отправлено"), JSON_UNESCAPED_UNICODE);
}  
 
// Сообщение, если не удаётся создать пользователя
else {
    
 
    // Устанавливаем код ответа
    http_response_code(400);
 
    // Покажем сообщение о том, что создать пользователя не удалось
    echo json_encode(array("message" => "Сообщение не было отправлено" ),JSON_UNESCAPED_UNICODE);
    
}

?>
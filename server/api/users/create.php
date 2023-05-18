<?php

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
include_once "../objects/users.php";


$database = new Database();
$db = $database->getConnection();
$user = new User($db);

// получаем отправленные данные
$data = json_decode(file_get_contents("php://input"));
$user->username = $data->username;
$user->age = $data->age;
$user->email = $data->email;
$user->password = $data->password;

if (
    !empty($user->username) &&
    !empty($user->email) &&
    $user->$email_exists == false &&
    !empty($user->password) &&
    !empty($user->age) &&
    $user->create()
) {
    // Устанавливаем код ответа
    http_response_code(200);
 
    // Покажем сообщение о том, что пользователь был создан
    echo json_encode(array("message" => "Пользователь был создан"), JSON_UNESCAPED_UNICODE);
}
 
// Сообщение, если не удаётся создать пользователя
else {
    
 
    // Устанавливаем код ответа
    http_response_code(400);
 
    // Покажем сообщение о том, что создать пользователя не удалось
    echo json_encode(array("message" => "Невозможно создать пользователя" ),JSON_UNESCAPED_UNICODE);
    
}
?>
<?php
error_reporting(E_ALL);
ini_set('display_startup_errors', 1);
ini_set('display_errors', '1');
// Заголовки
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
 // Файлы необходимые для соединения с БД
include_once "../config/database.php";
include_once "../objects/users.php";
// Получаем соединение с БД
$database = new Database();
$db = $database->getConnection();
// Создание объекта "User"
$user = new User($db);

// Получаем данные
$data = json_decode(file_get_contents("php://input"));
if(($data->username!=null)&&($data->about!=null)){
    $user->about=$data->about;
    $user->username=$data->username;
    
   if($user->updateAbout()){
        // Код ответа
            http_response_code(200);

            // Показать сообщение об ошибке
            echo json_encode(array("message" => "данные обновлены успешно"));
   }else{
       // Код ответа
            http_response_code(401);

            // Показать сообщение об ошибке
            echo json_encode(array("message" => "Невозможно обновить пользователя.Неверные данные"));
   }
   
}else
{
    
    // Код ответа
    http_response_code(401);

    // Сообщить пользователю что доступ запрещен
    echo json_encode(array("message" => "Данные пропали по пути сюда" ));
}
?>
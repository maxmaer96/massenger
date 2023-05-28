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
include_once "../objects/messages.php";


$database = new Database();
$db = $database->getConnection();
$message = new Message($db);
// получаем отправленные данные
$data = json_decode(file_get_contents("php://input"));
$user1 = $data->user1;
$user2 = $data->user2;
if(!empty($user1)&&
(!empty($user2)))
{
    
  $messagelist=$message->getMessageList($user1,$user2) ;
   // устанавливаем код ответа - 200 OK
    http_response_code(200);

    // выводим данные о товаре в формате JSON
    echo json_encode($messagelist, JSON_UNESCAPED_UNICODE);
}

else{
    // установим код ответа - 404 Не найдено
    http_response_code(404);

    // сообщаем пользователю, что товары не найдены
    echo json_encode(array("message" => "Сообщений нет милорд"), JSON_UNESCAPED_UNICODE);  
}
?>

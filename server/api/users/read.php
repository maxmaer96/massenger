<?php

// необходимые HTTP-заголовки
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");


// подключение базы данных и файл, содержащий объекты
include_once "../config/database.php";
include_once "../objects/users.php";

// получаем соединение с базой данных
$database = new Database();
$db = $database->getConnection();

// инициализируем объект
$user = new User($db);
 
// запрашиваем товары
$stmt = $user->read();
$num = $stmt->rowCount();

// проверка, найдено ли больше 0 записей
if ($num > 0) {
    // массив товаров
    $user_arr = array();
    $user_arr["records"] = array();

    // получаем содержимое нашей таблицы
    // fetch() быстрее, чем fetchAll()
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        // извлекаем строку
        extract($row);
        $user_item = array(
            "username" => $username,
            "email" => $email,
            "password" => $password,
            "age" => $age,
            "about" => $about
        );
        array_push($user_arr["records"], $user_item);
    }

    // устанавливаем код ответа - 200 OK
    http_response_code(200);

    // выводим данные о товаре в формате JSON
    echo json_encode($user_arr, JSON_UNESCAPED_UNICODE);
}

else {
    // установим код ответа - 404 Не найдено
    http_response_code(404);

    // сообщаем пользователю, что товары не найдены
    echo json_encode(array("message" => "юзеры не найдены"), JSON_UNESCAPED_UNICODE);
}
?>
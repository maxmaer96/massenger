<?php

class User
{
    // подключение к базе данных и таблице "users"
    private $conn;
    private $table_name = "users";

    // свойства объекта
    public $username;
    public $email;
    public $password;
    public $age;
    public $photo;
    public $about;
    

    // конструктор для соединения с базой данных
    public function __construct($db)
    {
        $this->conn = $db;
    }

// метод для получения товаров
function read()
{
    // выбираем все записи
    $query = "SELECT * FROM " . $this->table_name ;

    // подготовка запроса
    $stmt = $this->conn->prepare($query);

    // выполняем запрос
    $stmt->execute();
    return $stmt;
} 

// метод для создания Юзеров
function create()
{
    // запрос для вставки (создания) записей
    $query = "INSERT INTO
            " . $this->table_name . "
        SET
            username= :username, email= :email, password= :password, age= :age,photo= 'пусто' ,about= 'пусто' ";

    // подготовка запроса
    $stmt = $this->conn->prepare($query);

    // очистка
    $this->username = htmlspecialchars(strip_tags($this->username));
    $this->email = htmlspecialchars(strip_tags($this->email));
    $this->password = htmlspecialchars(strip_tags($this->password));
    $this->age = htmlspecialchars(strip_tags($this->age));
   

    // привязка значений
    $stmt->bindParam(":username", $this->username,PDO::PARAM_STR);
    $stmt->bindParam(":email", $this->email,PDO::PARAM_STR);
    $stmt->bindParam(":age", $this->age,PDO::PARAM_STR);
    
    // Для защиты пароля
        // Хешируем пароль перед сохранением в базу данных
        $password_hash = password_hash($this->password, PASSWORD_DEFAULT);
        $stmt->bindParam(":password", $password_hash,PDO::PARAM_STR);


    // выполняем запрос
    if ($stmt->execute()) {
        return true;
    }
    return false;
}   

  // Проверка, существует ли электронная почта в нашей базе данных
function emailExists() {
 
    // Запрос, чтобы проверить, существует ли электронная почта
    $query = "SELECT username, age, photo, about, password
            FROM " . $this->table_name . "
            WHERE email = ?
            LIMIT 0,1";
 
    // Подготовка запроса
    $stmt = $this->conn->prepare($query);
 
    // Инъекция
    $this->email=htmlspecialchars(strip_tags($this->email));
 
    // Привязываем значение e-mail
    $stmt->bindParam(1, $this->email);
 
    // Выполняем запрос
    $stmt->execute();
 
    // Получаем количество строк
    $num = $stmt->rowCount();
 
    // Если электронная почта существует,
    // Присвоим значения свойствам объекта для легкого доступа и использования для php сессий
    if ($num > 0) {
 
        // Получаем значения
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
 
        // Присвоим значения свойствам объекта
        $this->username = $row["username"];
        $this->age = $row["age"];
        $this->photo = $row["photo"];
        $this->about = $row["about"];
        $this->password = $row["password"];
 
        // Вернём "true", потому что в базе данных существует электронная почта
        return true;
    }
 
    // Вернём "false", если адрес электронной почты не существует в базе данных
    return false;
}
 
// Обновить запись пользователя
public function updateAbout() {
 
 
    $query = "UPDATE " . $this->table_name . "
            SET about = :about
            WHERE username = :username";
 
    // Подготовка запроса
    $stmt = $this->conn->prepare($query);
 
    // Инъекция (очистка)
    $this->username=htmlspecialchars(strip_tags($this->username));
    $this->about=htmlspecialchars(strip_tags($this->about));
 
    // Привязываем значения с HTML формы
    $stmt->bindParam(":about", $this->about);
    $stmt->bindParam(":username", $this->username);
 
    // Если выполнение успешно, то информация о пользователе будет сохранена в базе данных
    if($stmt->execute()) {
        return true;
    }
 
    return false;
}
public function updatePhoto() {
 
 
    $query = "UPDATE " . $this->table_name . "
            SET photo = :photo
            WHERE username = :username";
 
    // Подготовка запроса
    $stmt = $this->conn->prepare($query);
 
    // Инъекция (очистка)
    $this->username=htmlspecialchars(strip_tags($this->username));
    $this->photo=htmlspecialchars(strip_tags($this->photo));
 
    // Привязываем значения с HTML формы
    $stmt->bindParam(":photo", $this->photo);
    $stmt->bindParam(":username", $this->username);
 
    // Если выполнение успешно, то информация о пользователе будет сохранена в базе данных
    if($stmt->execute()) {
        return true;
    }
 
    return false;
}

}

?>
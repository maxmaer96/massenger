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
    $query = "SELECT * FROM" . $this->table_name ;

    // подготовка запроса
    $stmt = $this->conn->prepare($query);

    // выполняем запрос
    $stmt->execute();
    return $stmt;
} 

// метод для создания товаров
function create()
{
    // запрос для вставки (создания) записей
    $query = "INSERT INTO
            " . $this->table_name . "
        SET
            username=:username, email=:email, password=:password, age=:age";

    // подготовка запроса
    $stmt = $this->conn->prepare($query);

    // очистка
    $this->name = htmlspecialchars(strip_tags($this->username));
    $this->price = htmlspecialchars(strip_tags($this->email));
    $this->description = htmlspecialchars(strip_tags($this->password));
    $this->category_id = htmlspecialchars(strip_tags($this->age));
   

    // привязка значений
    $stmt->bindParam(":username", $this->username);
    $stmt->bindParam(":email", $this->email);
    $stmt->bindParam(":password", $this->password);
    $stmt->bindParam(":age", $this->age);
    

    // выполняем запрос
    if ($stmt->execute()) {
        return true;
    }
    return false;
}   
}

?>
<?php

class Database
{
    // укажите свои учетные данные базы данных
    private $host = "maxmaee4.beget.tech";
    private $db_name = "maxmaee4_diploma";
    private $username = "maxmaee4_diploma";
    private $password = "Tarakan96";
    public $conn;

    // получаем соединение с БД
    public function getConnection()
    {
        $this->conn = null;

        try {
            $this->conn = new PDO("mysql:host=" . $this->host . ";dbname=" . $this->db_name, $this->username, $this->password);
            $this->conn->exec("set names utf8");
        } catch (PDOException $exception) {
            echo "Ошибка подключения: " . $exception->getMessage();
        }

        return $this->conn;
    }
}


?>
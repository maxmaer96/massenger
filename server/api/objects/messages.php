<?php
class Message
{
    // подключение к базе данных и таблице "users"
    private $conn;
    private $table_name = "message";

    // свойства объекта
    public $_id;
    public $from_who;
    public $to_who;
    public $when_sent;
    public $text;
    
    

    // конструктор для соединения с базой данных
    public function __construct($db)
    {
        $this->conn = $db;
    }
    // метод для создания связи дружбы
function create()
{
    // запрос для вставки (создания) записей
     $query = "INSERT INTO
            " . $this->table_name . "
        SET
            _id= NULL, from_who= :from_who, to_who= :to_who, when_sent= now(), text= :text ";


    // подготовка запроса
    $stmt = $this->conn->prepare($query);

    // очистка
    
    $this->from_who=htmlspecialchars(strip_tags($this->from_who));
    $this->to_who=htmlspecialchars(strip_tags($this->to_who));
    $this->text=htmlspecialchars(strip_tags($this->text));

    // привязка значений
     $stmt->bindParam(":from_who", $this->from_who);
     $stmt->bindParam(":to_who", $this->to_who);
     $stmt->bindParam(":text", $this->text);
    

    // выполняем запрос
    if ($stmt->execute()) {
        return true;
    }
    return false;
}  

function getMessageList($user1,$user2){
      $query = "SELECT * FROM ". $this->table_name ."
      WHERE to_who=:user1 and from_who=:user2 
      or to_who=:user2 and from_who=:user1";
     
    // Подготовка запроса
    $stmt = $this->conn->prepare($query);
 
    // Инъекция
    $user1=htmlspecialchars(strip_tags($user1));
    $user2=htmlspecialchars(strip_tags($user2));
 
    // Привязываем значение 
    $stmt->bindParam(":user1", $user1);
     $stmt->bindParam(":user2", $user2);
    // Выполняем запрос
      $stmt->execute();
    //массив для списка друзей
    $messageList=array("count"=>$stmt->rowCount());
    
    //переменная счетчик
      $i=1;
      
         while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        
             $messageList["message".$i]=$row["text"];
             $messageList["message".$i . " from_who"]=$row["from_who"];
             $messageList["message".$i . " to_who"]=$row["to_who"];
             $messageList["message".$i . " when_sent"]=$row["when_sent"];
             $i+=1;
        }
      
      return $messageList;  
    }
}

?>
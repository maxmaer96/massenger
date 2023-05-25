<?php

class Friend
{
    // подключение к базе данных и таблице "users"
    private $conn;
    private $table_name = "friend";

    // свойства объекта
    public $id;
    public $friend1;
    public $friend2;
    

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
            _id= NULL, friend1= :friend1, friend2= :friend2 ";

    // подготовка запроса
    $stmt = $this->conn->prepare($query);

    // очистка
    
    $this->friend1=htmlspecialchars(strip_tags($this->friend1));
    $this->friend2=htmlspecialchars(strip_tags($this->friend2));
   

    // привязка значений
     $stmt->bindParam(":friend1", $this->friend1);
    $stmt->bindParam(":friend2", $this->friend2);
    
    

    // выполняем запрос
    if ($stmt->execute()) {
        return true;
    }
    return false;
}  
function isFriends(){
    //немного проверок на дурака
    if($this->friend1==$this->friend2){
    return false;
}
// запрос для вывода записей
    $query = "SELECT * 
    FROM " . $this->table_name . "
    WHERE friend1=:friend1 and friend2=:friend2 
    or friend2=:friend1 and friend1=:friend2 
    LIMIT 0,1";

    // подготовка запроса
    $stmt = $this->conn->prepare($query);
     // Инъекция
    $this->friend1=htmlspecialchars(strip_tags($this->friend1));
    $this->friend2=htmlspecialchars(strip_tags($this->friend2));
    // Привязываем значение e-mail
    $stmt->bindParam(":friend1", $this->friend1);
    $stmt->bindParam(":friend2", $this->friend2);
    
    // Выполняем запрос
    $stmt->execute();
 
    // Получаем количество строк
    $num = $stmt->rowCount();
 
    // Если эти двое уже дружать, то возвращаем false
    
    if ($num > 0) {
        return false;
}

//если нет то возвращаем true
return true;
}
function getFriendList(){
      $query = "SELECT friend1,friend2 FROM " . $this->table_name . " WHERE friend1=:friend1 OR friend2=:friend1 ";
     
    // Подготовка запроса
    $stmt = $this->conn->prepare($query);
 
    // Инъекция
    $this->friend1=htmlspecialchars(strip_tags($this->friend1));
 
    // Привязываем значение e-mail
    $stmt->bindParam(":friend1", $this->friend1);
    // Выполняем запрос
      $stmt->execute();
    //массив для списка друзей
    $friendArr=array();
    //переменная счетчик
      $i=1;
      
         while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            
        if($this->friend1==$row["friend2"]){
       
      $friendArr["user".$i]=$row["friend1"];
        }else{
        
             $friendArr["user".$i]=$row["friend2"];
        
        }
      $i+=1;
    }
    
    
    return $friendArr;
}
function getFriendInfo($friendArr){
    //в резултат сразу внесу количество столбцов(друзей), что бы проще было извлечь данные впоследствии
    
    $resultInfo=array("count"=>count($friendArr));
    // проходимся по каждому юзеру в списке и забираем их данные
    for($i=1;$i<=count($friendArr);$i++){
     $query = "SELECT username,photo,email FROM users where username=:user ";
     
    $stmt = $this->conn->prepare($query);
     $stmt->bindParam(":user", $friendArr["user".$i]);
     $stmt->execute();
     $row = $stmt->fetch(PDO::FETCH_ASSOC);
     
     $resultInfo["user".$i]=$row["username"];
     $resultInfo["user".$i." photo"]=$row["photo"];
     $resultInfo["user".$i." email"]=$row["email"];
    }
    return $resultInfo;
    
}
}
    
?>

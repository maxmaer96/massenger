<?php
// Показ сообщений об ошибках
error_reporting(E_ALL);
 
// Установим часовой пояс по умолчанию
date_default_timezone_set("Europe/Moscow");
 
// Переменные, используемые для JWT
$key = "apchi";
$iss = "mememe";
$aud = "maxwell";
$iat = 1684174502;
$nbf = 1684174502;
?>
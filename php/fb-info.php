<?php

session_start();

require 'vendor/autoload.php';

use Facebook\FacebookSession;
use Facebook\FacebookRequest;
use Facebook\GraphUser;
use Facebook\FacebookRequestException;


FacebookSession::setDefaultApplication('278376725619348', 'c708ce358828a1319e349c3a4fe68250');

//echo $_GET["fb-session"];

$session = new FacebookSession($_GET["fb-session"]);

if($session) {

  try {

    $user_profile = (new FacebookRequest(
      $session, 'GET', '/me'
    ))->execute()->getGraphObject(GraphUser::className());

    //form a clojure map
    echo '{:fb-id ' . '"' . $user_profile->getId() . '"' .
        ' :fb-link ' . '"' . $user_profile->getLink() . '"' .
        ' :name ' . '"' . $user_profile->getName() . '"' .
        ' :fname ' . '"' . $user_profile->getFirstName() . '"' .
        ' :lname ' . '"' . $user_profile->getLastName() . '"' .
        '}';

  } catch(FacebookRequestException $e) {

    echo '{:e-code ' . '"' . $e->getCode() . '"' .
        ' :e-msg ' . '"' . $e->getMessage() . '"' .
        '}';

  }   
}

?>

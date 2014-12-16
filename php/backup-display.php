<?php

session_start();

//require_once( 'Facebook/.php' );
require_once( 'Facebook/HttpClients/FacebookHttpable.php' );
require_once( 'Facebook/HttpClients/FacebookStreamHttpClient.php' );
require_once( 'Facebook/FacebookSDKException.php' );
require_once( 'Facebook/FacebookSession.php' );
require_once( 'Facebook/FacebookRequest.php' );
require_once( 'Facebook/GraphObject.php' );
require_once( 'Facebook/GraphUser.php' );
require_once( 'Facebook/FacebookRequestException.php' );
require_once( 'Facebook/Entities/AccessToken.php' );




//use Facebook\;
use Facebook\HttpClients\FacebookHttpable;
use Facebook\HttpClients\FacebookStreamHttpClien;
use Facebook\FacebookSDKException;
use Facebook\GraphObject;
use Facebook\FacebookSession;
use Facebook\FacebookRequest;
use Facebook\GraphUser;
use Facebook\FacebookRequestException;
use Facebook\Entities\AccessToken;



FacebookSession::setDefaultApplication('278376725619348', 'c708ce358828a1319e349c3a4fe68250');

echo " session ok ";

// If you already have a valid access token:
$session = new FacebookSession('CAAD9LqHYIpQBAAoFJUWirj7cHhK1f35kIyLcG1Mp3ODltYyh12H0V0BN4LqjSDxLVnljnSwqs1Qe29JdH4XWIPO9Fpq5XYGPLbI8vHZApJIl4T8Gvtcaylu2JmLZB1PMa0DNiBtkep90EWWBdB2uAnb5ZC7iSR2ZCvmVMXEmu2YMJIubtaUZCifProH0ncRjCH1EVUZB8nR2yWXjZBuxwkVJuzlUc3B0ncZD');

if($session) {

  try {

    $user_profile = (new FacebookRequest(
      $session, 'GET', '/me'
    ))->execute()->getGraphObject(GraphUser::className());

    echo "Name: " . $user_profile->getName();

  } catch(FacebookRequestException $e) {

    echo "Exception occured, code: " . $e->getCode();
    echo " with message: " . $e->getMessage();

  }   
}

?>

<!DOCTYPE html>
<html>
<head>
<title>Facebook Login JavaScript Example</title>
<meta charset="UTF-8">
</head>
<body>
</body>
</html>
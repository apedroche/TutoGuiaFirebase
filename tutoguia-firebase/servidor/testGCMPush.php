<?php
    
    class GCMPush {
        private static $API_ACCESS_KEY = 'AIzaSyC6MvT3rWRwAoGDzWnWBPY3wzpjJ6XOGig';
        private static $GCM_URL = 'https://gcm-http.googleapis.com/gcm/send';
        
        public static function sendGcmPush($gcmid, $json_msg) {
            $headers = array(
                             'Authorization: key=' . self::$API_ACCESS_KEY,
                             'Content-Type: application/json'
                             );
            $fields = array(
                            'to' => $gcmid,
                            'data'	=> $json_msg
                            );
            $ch = curl_init();
            curl_setopt( $ch, CURLOPT_URL, self::$GCM_URL);
            curl_setopt( $ch, CURLOPT_POST, true );
            curl_setopt( $ch, CURLOPT_HTTPHEADER, $headers );
            curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
            curl_setopt( $ch, CURLOPT_SSL_VERIFYPEER, false );
            curl_setopt( $ch, CURLOPT_POSTFIELDS, json_encode($fields));
            $result = curl_exec($ch);
            curl_close($ch);
            return $result;
        }
        
        public static function sendGcmPushTest($gcmid, $json_msg) {
            $headers = array(
                             'Authorization: key=' . self::$API_ACCESS_KEY,
                             'Content-Type: application/json'
                             );
            return $headers;
        }
        
        public static function sendGenericMessage($gcmid, $title, $message){
            $msg = array( "title"=>$title, "message"=>$message );
            return self::sendGcmPush($gcmid, $msg);
        }
    }

    $token = $_GET['token'];
    $title = $_GET['title'];
    $message = $_GET['message'];
    echo GCMPush::sendGenericMessage($token, $title, $message);

?>
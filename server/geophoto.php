<?php
   // enable error reporting
   ini_set('display_errors',1);
   ini_set('display_startup_errors',1);
   error_reporting(-1);
   if(isset($_POST["data"])){
     // time
     date_default_timezone_set("Europe/Rome");
     $time = date("Ymd-Gis");
     // files
     $xmlfn = $time.".xml";
     $imagefn = $time.".jpg";
     $xml = simplexml_load_string($_POST["data"]);
     // save the image
     $image = fopen($imagefn, "w");
     fwrite($image, base64_decode($xml->image));
     fclose($image);
     // in the xml substitute the image with its filename
     $xml->image = $imagefn;
     // save the xml
     $xml->asXML($xmlfn);
   } else {
   ?>
<!DOCTYPE html>
<html>
  <head>
    <title>GeoPhoto</title>
    <meta charset="UTF-8">
    <style type="text/css">
      body {
        max-width: 650px;
        margin: auto auto 10px;
      }
      h1 {
        color: #6060B0;
        text-align: center;
      }
     .date {
        font-weight: bold;
        color: #6060FF;
      }
      li {
        margin-top: 10px;
        background: #EEEEFF;
      }
      form {
        text-align: center;
      }
    </style>
  </head>
  <body>
    <h1>GeoPhoto</h1>
    <ul>
      <?php
         foreach(scandir(".") as $filename){
           if(pathinfo($filename, PATHINFO_EXTENSION) == "xml"){
             $xml = simplexml_load_file($filename);
             echo '<li>';
             echo '<div class="date">'.$xml->date."</div>\n";
             echo '<div class="image"><a href="'.urlencode($xml->image)
                  .'">image</a></div>';
             echo '<div class="location"> Lat. '.$xml->location->latitude
                 .', Long. '.$xml->location->longitude."</div>\n";
             echo "</li>\n";
           }
         }
         ?>
    </ul>
    <form action="geophoto.php" method="post">
      <input type="text" name="data"/>
      <input type="submit"/>
    </form>
  </body>
</html>
<?php } ?>
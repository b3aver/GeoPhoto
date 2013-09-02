<?php
    // enable error reporting
    ini_set('display_errors',1);
    ini_set('display_startup_errors',1);
    error_reporting(-1);
    if(count($_POST) != 0){
        date_default_timezone_set("Europe/Rome");
        // Simplest PHP ACRA backend
        // from: https://gist.github.com/KevinGaudin/5560305
        // Outputs all POST parameters to a text file. The file name is the date_time of the report reception
        $fileName = date('Y-m-d_H-i-s').'.txt';
        $file = fopen($fileName,'w') or die('Could not create report file: ' . $fileName);
        foreach($_POST as $key => $value) {
            $reportLine = $key." = ".$value."\n";
            fwrite($file, $reportLine) or die ('Could not write to report file ' . $reportLine);
        }
        fclose($file);
        // End Simplest PHP ACRA backend
    }
?>
<!DOCTYPE html>
<html>
  <head>
    <title>GeoPhoto Crash Report</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="style.css">
  </head>
  <body>
  <h1>GeoPhoto Crash Report</h1>
    <ul>
      <?php
         foreach(scandir(".") as $filename){
           if(pathinfo($filename, PATHINFO_EXTENSION) == "txt"){
             echo '<li>';
             echo '<a href="'.$filename.'">'.$filename.'</a>';
             echo "</li>\n";
           }
         }
         ?>
    </ul>
    <form action="report.php" method="post">
      <input type="text" name="data"/>
      <input type="submit"/>
    </form>
  </body>
</html>

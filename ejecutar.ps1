# Script para ejecutar la aplicación con encoding UTF-8
$OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::InputEncoding = [System.Text.Encoding]::UTF8
chcp 65001 > $null

./mvnw.cmd exec:java "-Dexec.mainClass=ar.edu.uner.tpi.main.Main"

# Macro Fit

Bachelorarbeit - Spring Boot REST-Anwendung


### Voraussetzungen
- Docker installiert ([Anleitung: Installation von Docker](https://www.docker.com/))
- Java installiert: Version 20 oder höher ([Offizielle Seite von Oracle](https://www.oracle.com/java/technologies/downloads/))
  - Mit dem Befehl `java --version` kann die installierte Java-Version auf dem System überprüft werden.

## Datenbank

### Datenbank starten

- Terminal öffnen und in das Verzeichnis `/docker` wechseln
- `docker-compose up --build -d` ausführen

Dieser Befehl erstellt und startet die Docker-Container im Hintergrund. Die Datenbank wird dabei ausgeführt und ist über die konfigurierte Adresse erreichbar.

### PgAdmin Oberfläche aufrufen
- Die PostgreSQL Datenbank ist über [pgAdmin](http://localhost:8081/) verwaltbar.
- Benutzer: admin@pgadmin.com
- Password: admin
- Verbindung herstellen - _Rechtsklick auf 'Servers' im Object Explorer > Register > Server_
  - Name: Recipe Database
  - Hostname: postgres
  - Port: 5432
  - Benutzer/Password: postgres/postgres
- [Dokumentation](https://www.pgadmin.org/docs/) 

## Konfiguration
Die Konfiguration der Spring Boot-Anwendung erfolgt über die Datei [application.properties](src/main/resources/application.properties). 
Hier werden Einstellungen und Konfigurationen für bspw. Datenbankverbindungsinformationen, Logging und andere anwendungsspezifische Parameter vorgenommen.

## Applikation Starten
```shell
mvn spring-boot:run-
```
Nach Start ist die Applikation unter dem von Spring Boot festgelegten Port zu erreichen (http://localhost:8080/)

## Tests

### Alle Tests

```shell
    mvn clean verify
```
### Coverage
Hierfür wird Jacoco verwendet. Dieser generiert die Code-Coverage-Berichte automatisch nach der Ausführung der Tests im Verzeichnis [target/site/jacoco](target/site/jacoco).

## Support
Falls der Start der Anwendung nicht gelingen sollte, können Sie mich über folgende E-Mails erreichen:
- sfi.dogan@gmail.com
- s0574414@htw-berlin.de

@echo off
:: Guardamos la ruta raíz donde está este archivo .bat
set BASE_DIR=%~dp0

echo Iniciando Servidor de Descubrimiento Eureka (Puerto 8761)...
cd /d "%BASE_DIR%eureka"    
start cmd /k "mvnw spring-boot:run"

echo Esperando 12 segundos a que Eureka se estabilice...
timeout /t 12 /nobreak > nul

echo Iniciando API Gateway...
cd /d "%BASE_DIR%gateway"
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio Agendamiento...
cd /d "%BASE_DIR%ms_agendamiento"
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio Pacientes...
cd /d "%BASE_DIR%ms_pacientes"
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio Profesionales...
cd /d "%BASE_DIR%ms_profesionales"
start cmd /k "mvnw spring-boot:run"
echo Ecosistema lanzado. Dashboard disponible en http://localhost:8761
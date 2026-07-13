

Proyecto PlenamenteMS

## Dominio del Proyecto

Este proyecto consiste en una plataforma de gestión médica y agendamiento clínico para la administración de pacientes y profesionales de la salud mental. El sistema permite gestionar perfiles médicos, historiales de pacientes y coordinar citas de manera automatizada a través de un ecosistema desacoplado.

---

## Integrantes

ms_agendamiento : Steven Peralta
ms_pacientes: Pedro Lizana
ms_profesionales: Nataly Arriaza 

---

## Arquitectura y Microservicios Implementados
El sistema está construido bajo una arquitectura de microservicios utilizando **Spring Cloud** y **Docker**. A continuación se detallan los componentes:

| Microservicio | Tecnología / BD | Descripción |
| :--- | :--- | :--- |
| **`eureka-server`** | Spring Cloud Eureka | Servidor de descubrimiento para el registro y localización de los servicios. |
| **`api-gateway`** | Spring Cloud Gateway | Puerta de enlace única y enrutador principal de las peticiones externas. |
| **`mariadb-db`** | MariaDB | Base de datos relacional compartida (esquemas independientes). |
| **`ms-pacientes`** | Spring Boot / Java | Gestión de datos personales, historiales médicos y fichas de pacientes. |
| **`ms-profesion`** | Spring Boot / Java | Administración de perfiles profesionales, especialidades y horarios del personal médico. |
| **`ms-agendamiento`**| Spring Boot / Java | Motor central para la reserva, cancelación y bloques horarios de citas médicas. |

---

## Rutas Principales del Gateway
Todas las peticiones del exterior se deben centralizar a través del **API Gateway** en el puerto `8090`. Las rutas base configuradas son:

* **Pacientes:** `GET / POST / PUT / DELETE` ➡️ `http://localhost:8090/api/v1/pacientes`
* **Profesionales:** `GET / POST / PUT / DELETE` ➡️ `http://localhost:8090/api/v1/profesionales`
* **Agendamientos:** `GET / POST / PUT / DELETE` ➡️ `http://localhost:8090/api/v1/agendamientos`

---

## Documentación de la API (Swagger)
Puedes interactuar con los endpoints y revisar los contratos de la API de manera local ingresando a los siguientes enlaces con el proyecto encendido:

* **Gateway / Agregador Swagger:** [http://localhost:8090/swagger-ui.html](http://localhost:8090/swagger-ui.html) *(o la ruta que use tu gateway)*
* **Eureka Dashboard:** [http://localhost:8761](http://localhost:8761)

---

## Instrucciones de Ejecución

### Ejecución Local (con Docker)
Para levantar todo el ecosistema en tu computadora local de manera automática, asegúrate de tener **Docker Desktop** abierto y ejecuta los siguientes comandos en la raíz del proyecto:

1. **Construir las imágenes desde cero (limpiando caché):**

    docker-compose build --no-cache //para que se cree la imagen de docker

    docker-compose up //para que se levante el docker   

    docker-compose down //para detener y eliminar la imagen de docker
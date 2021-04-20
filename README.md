# Bootcamp Mercado Libre - Desafío Spring

* **Autor:** Sofía Pigino
* **Fecha de Entrega:** 20/04/2021

## Contenido

### Hoteles
A continuación algunos ejemplos para ejecutar las consignas:
* Listado de todos los hoteles disponibles
    * `GET` http://localhost:8080/api/v1/hotels
* Listado de todos los hoteles disponibles en un determinado rango de fechas y según el destino seleccionado
    * `GET` http://localhost:8080/api/v1/hotels?dateFrom=18/04/2021&amp;dateTo=20/04/2021&amp;destination=Cartagena
* Realizar una reserva de un hotel, indicando cantidad de personas, fecha de entrada, fecha de salida y tipo de habitación. Obtener como respuesta el monto total de la reserva realizada
    * `POST` http://localhost:8080/api/v1/booking
    * JSON para el body request  
    ```
      {
        "username" : "seba_gonzalez@unmail.com.ar",
        "booking" : {
            "dateFrom" : "10/02/2021",
            "dateTo" : "12/02/2021",
            "destination" : "Puerto Iguazú",
            "hotelCode" : "CH-0003",
            "peopleAmount" : 2,
            "roomType" : "doble",
            "people" : [
                {
                    "dni" : "12345678",
                    "name" : "Pepito",
                    "lastname" : "Gomez",
                    "birthDate" : "10/11/1982",
                    "mail" : "pepitogomez@gmail.com"
                },
                 {
                    "dni" : "13345678",
                    "name" : "Fulanito",
                    "lastname" : "Gomez",
                    "birthDate" : "10/11/1983",
                    "mail" : "fulanitogomez@gmail.com"
                }
            ],
            "paymentMethod" : {
                "type" : "CREDIT",
                "number" : "1234-1234-1234-1234",
                "dues" : 6
            }
        }
    }
    ```
### Vuelos
A continuación algunos ejemplos para ejecutar las consignas:
* Listado de todos los vuelos disponibles
  * `GET` http://localhost:8080/api2/v1/flights
  
* Listado de todos los vuelos disponibles en un determinado rango de fechas y según el destino y el origen seleccionados
  * `GET` <http://localhost:8080/api2/v1/flights?origin=Buenos Aires&destination=Puerto Iguazú&dateFrom=10/02/2021&dateTo=15/02/2021>
  
* Realizar una reserva de un vuelo, indicando cantidad de personas, origen, destino, fecha de ida y fecha de vuelta. Obtener como respuesta el monto total de la reserva realizada
  * `POST` http://localhost:8080/api2/v1/flight-reservation
  * JSON para el body request
    ```
      {
        "username": "arjonamiguel@gmail.com",
        "flightReservation": {
            "dateFrom": "10/02/2021",
            "dateTo": "15/02/2021",
            "origin": "Buenos Aires",
            "destination": "Puerto Iguazú",
            "flightNumber": "BAPI-1235",
            "seats": 2,
            "seatType": "ECONOMY",
            "people": [
                {
                    "dni": "12345678",
                    "name": "Pepito",
                    "lastname": "Gomez",
                    "birthDate": "10/11/1982",
                    "mail": "pepitogomez@gmail.com"
                },
                {
                    "dni": "13345678",
                    "name": "Fulanito",
                    "lastname": "Gomez",
                    "birthDate": "10/11/1983",
                    "mail": "fulanitogomez@gmail.com"
                }
            ],
            "paymentMethod": {
                "type": "credit",
                "number": "1234-1234-1234-1234",
                "dues": 6
            }
        }
      }
    ```
 # Descripción de la API

Este proyecto consiste en una API que maneja tres documentos principales: **Usuario**, **Talleres** y **Reservas**. A continuación, se detallan cada uno de los documentos y sus respectivos campos.

## Documentos

### 1. Usuario
Este documento representa a los usuarios registrados en el sistema.


**Campos:**
- `id` (String): Identificador único del usuario.
- `username` (String): Nombre de usuario.
- `email` (String): Correo electrónico del usuario.
- `password` (String): Contraseña encriptada del usuario.
- `telefono` (String): Número de teléfono del usuario.
- `rol` (String): USER o ADMIN (USER por defecto).


### 3. Talleres
Este documento representa los talleres asignados a los usuarios.

**Campos:**
- `id` (ObjectId): Identificador único del taller.
- `titulo` (String): Título de la tarea.
- `descripcion` (String): Descripción detallada del taller.
- `plazas` (Int): Plazas limitadas del taller.
- `fecha` (Date): Fecha en la que se creó el taller.
- `estado` (String): Estado del taller (`activo`, `cancelado`).
- `reservas` (List<Reserva>): Lista que contiene las reservas del taller.

---

## ENDPOINTS

### 1. **Usuario**:
- `POST /usuarios/register`: Crear un nuevo usuario (Registro).
- `POST /usuarios/login`: Autenticación de usuario (Login).
- `GET /usuarios/{username}`: Obtener un usuario específico por su username.
- `PUT /usuarios/updatePassword`: Actualizar la contraseña del usuario autenticado.
- `PUT /usuarios/activarBono/{username}`: Activar el bono del usuario **(Solo Admin o el propio usuario)**.
- `DELETE /usuarios/delete/{username}/{password}`: Eliminar un usuario proporcionando username y contraseña **(Solo Admin o el propio usuario)**.
- `GET /usuarios/getAll`: Obtener una lista de todos los usuarios **(Solo Admin)**.


### 2. **Talleres**:
- `GET /talleres/{id}`: Obtener un taller específico por su ID.
- `GET /talleres/getAll`: Obtener una lista de todos los talleres.
- `POST /talleres/register`: Registrar un nuevo taller.
- `PUT /talleres/update/{id}`: Actualizar un taller existente por su ID.
- `DELETE /talleres/delete/{id}`: Eliminar un taller por su ID **(Requiere autenticación)**.


### 3. **Reservas**:
- `GET /reservas/{username}`: Obtener todas las reservas de un usuario específico **(Requiere autenticación)**.
- `GET /reservas/getAll`: Obtener todas las reservas del sistema **(Requiere autenticación)**.
- `GET /reservas/first/{username}`: Obtener la primera reserva de un usuario **(Requiere autenticación)**.
- `POST /reservas/register`: Registrar una nueva reserva **(Requiere autenticación)**.
- `DELETE /reservas/delete/{id}/taller/{tallerID}`: Eliminar una reserva específica por ID y ID del taller **(Requiere autenticación)**.
- `DELETE /reservas/delete/{tallerID}`: Eliminar una reserva por ID del taller **(Requiere autenticación)**.
- `DELETE /reservas/deleteAll/{username}`: Eliminar todas las reservas de un usuario **(Requiere autenticación)**.


---

  ## Lógica de negocio

### 1. **Roles de usuario**
- El rol **USER** solo podrá ver, registrar o eliminar las reservas propias.
- El rol **ADMIN** tendrá acceso completo para registrar, eliminar y actualizar cualquier usuario, talleres y reservas así como verlos todos.

### 2. **Autenticación**
- Los usuarios deben autenticarse para obtener un token JWT. Este token debe incluir los detalles del rol (User o Admin) y se debe verificar en cada solicitud para garantizar que el usuario tenga acceso al recurso solicitado.

### 3. **Permisos de usuario**
- **Usuario con rol USER** solo puede gestionar tareas y direcciones propias.
- **Usuario con rol Admin** puede gestionar tareas, direcciones y usuarios de cualquier usuario.

### 4. **Flujo de talleres**
- Los talleres pueden pasar de un estado `disponible` a `completo`, según se cubran las plazas. ADMIN es el único que puede crear, modificar o eliminar talleres

### 5. **Flujo de reservas**
- Las reservas pueden ser observadas, registradas y eliminadas por el propio usuario y solo las propias. Mientras que ADMIN tiene acceso a todas las reservas.

### 6. **Protección de datos**
- Un usuario solo puede actualizar o eliminar su propia cuenta, tareas y direcciones. Las solicitudes a recursos de otros usuarios serán bloqueadas si no es un ADMIN.

---

## Excepciones y Códigos de estado

1. **404 Not Found**:
   - Cuando un recurso no se encuentra (usuario, dirección, tarea).

2. **400 Bad Request**:
   - Cuando los datos proporcionados no son válidos o están incompletos (por ejemplo, falta un campo obligatorio).

3. **401 Unauthorized**:
   - Cuando el usuario no está autenticado o el token de acceso es inválido.

4. **403 Forbidden**:
   - Cuando un usuario intenta acceder a recursos que no le pertenecen sin permisos (por ejemplo, un usuario intentando ver o eliminar tareas de otro usuario).

5. **500 Internal Server Error**:
   - Para cualquier error no controlado en el servidor (por ejemplo, problemas en la base de datos).

---

## Restricciones de seguridad

1. **Autenticación y autorización**:
   - Uso de JWT (JSON Web Token) para la autenticación. Solo los usuarios autenticados pueden acceder y modificar sus propios recursos. Los Admins tienen permisos para acceder y modificar los recursos de cualquier usuario.
   
2. **Cifrado de contraseñas**:
   - Las contraseñas se *hashea* antes de almacenarse en la base de datos.

3. **Validación de entradas**:
   - Se valida que los datos entrantes sean correctos.
  
---

## Pruebas

Se han realizado varias pruebas en la carpeta *test/kotlin/com.es.API_REST_SEGURA*.
Para ello se ha tenido que importar nuevas dependencias para poder hacer los test automáticos, creando mockk para cada *service* y para *AuthenticationManager*. Sin embargo, usamos *SecurityConfig* real para simular la seguridad de la API con los roles.






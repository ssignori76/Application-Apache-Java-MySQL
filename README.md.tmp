# Runbook Operativo: Applicazione Web Containerizzata

Questo repository contiene un'applicazione web a 3 tier composta da un frontend headless servito da Apache, un backend REST in Java (Spring Boot) e un database MySQL, il tutto orchestrato tramite Docker Compose.

---

## 1. Quickstart e Lifecycle Container

Per gestire l'intero stack applicativo posizionarsi nella root del progetto ed eseguire i seguenti comandi:

*   **Avvio stack (foreground):**
    ```bash
    docker compose up --build
    ```
*   **Avvio stack (detached):**
    ```bash
    docker compose up -d --build
    ```
*   **Stop stack:**
    ```bash
    docker compose down
    ```
*   **Stop + reset dati (rimozione volumi DB):**
    ```bash
    docker compose down -v
    ```
*   **Stato dei servizi:**
    ```bash
    docker compose ps
    ```
*   **Rebuild di un singolo servizio (es. backend):**
    ```bash
    docker compose build backend
    ```
*   **Restart singolo servizio (es. frontend):**
    ```bash
    docker compose restart frontend
    ```
*   **Accesso shell (debug) (es. nel db):**
    ```bash
    docker compose exec db sh
    ```
    *(Nota: per il db o altri servizi basati su Linux)*

---

## 2. Log: consultazione, struttura e verbosità

### 2.1 Consultazione log via Docker Compose

*   **Tutti i servizi (ultime 200 righe e follow):**
    ```bash
    docker compose logs -f --tail=200
    ```
*   **Singolo servizio (es. backend):**
    ```bash
    docker compose logs -f --tail=200 backend
    ```

### 2.2 Logging backend Java

Il backend emette log nativamente su `stdout/stderr`. È configurato tramite il file `backend/src/main/resources/application.properties`.

*   **File di configurazione:** `application.properties`
*   **Variabili d'ambiente supportate per la modifica dinamica:**
    *   `LOG_LEVEL_ROOT`: controlla la verbosità generale di Spring (default `INFO`)
    *   `LOG_LEVEL_APP`: controlla la verbosità del package applicativo `com.example.backend` (default `DEBUG`)
*   **Struttura (Formato testuale):** Il formato è customizzabile tramite la variabile `LOG_PATTERN_CONSOLE`. Il formato di default utilizzato è: `logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n`
*   **Procedura per applicare le modifiche:**
    Modificare il file `docker-compose.yml` nella sezione `environment` del servizio `backend` per cambiare le variabili (es. settando `LOG_LEVEL_APP=TRACE`), dopodiché applicare le modifiche ricreando il container:
    ```bash
    docker compose up -d --build backend
    ```

### 2.3 Logging Apache (Frontend)

*   **File e direttive:** Apache è configurato tramite il `httpd.conf` base dell'immagine `httpd:2.4-alpine`. Le direttive base `ErrorLog /proc/self/fd/2` e `CustomLog /proc/self/fd/1 common` mappano rispettivamente gli errori su `stderr` e gli accessi su `stdout`.
*   **Come cambiare verbosità:** Tramite la direttiva `LogLevel warn` all'interno del file `/usr/local/apache2/conf/httpd.conf`.
*   **Come cambiare struttura:** Modificando le direttive `LogFormat` e `CustomLog` sempre in `/usr/local/apache2/conf/httpd.conf`.
*   **Consultazione:** Essendo emessi su stdout/stderr, i log sono consultabili normalmente via:
    ```bash
    docker compose logs -f frontend
    ```

---

## 3. DB: accesso, schema, query operative, data forcing

### 3.1 Accesso a MySQL via compose

Per collegarsi interattivamente al DB (inserire la password `rootpassword` oppure `app_password` a seconda dell'utente scelto):
```bash
docker compose exec db mysql -uapp_user -papp_password app_db
```

### 3.2 Inventario schema

*   **Elenco tabelle:** `SHOW TABLES;`
*   **Tabelle realmente create:**
    1.  `users`: Contiene le credenziali e le anagrafiche. (PK: `id`).
    2.  `services`: Contiene i servizi acquistabili. (PK: `id`).
    3.  `orders`: Traccia gli ordini effettuati. (PK: `id`, FK: `user_id` -> `users.id`, FK: `service_id` -> `services.id`).

### 3.3 Query operative specifiche (Eseguibili da console MySQL)

*   **Lista utenti con ruolo:**
    ```sql
    SELECT id, username, first_name, last_name, role FROM users;
    ```
*   **Lookup per username:**
    ```sql
    SELECT * FROM users WHERE username = 'admin';
    ```
*   **Catalogo servizi:**
    ```sql
    SELECT id, name, description, price FROM services;
    ```
*   **Storico ordini per customer (es. user_id = 2):**
    ```sql
    SELECT o.id, o.status, s.name, o.created_at 
    FROM orders o JOIN services s ON o.service_id = s.id 
    WHERE o.user_id = 2 ORDER BY o.created_at DESC;
    ```
*   **Dettaglio ordine con join su servizio:**
    ```sql
    SELECT o.id, u.username, s.name, s.price, o.status, o.note 
    FROM orders o 
    JOIN users u ON o.user_id = u.id 
    JOIN services s ON o.service_id = s.id 
    WHERE o.id = 1;
    ```
*   **Lista ordini admin con filtri (stato SUBMITTED):**
    ```sql
    SELECT * FROM orders WHERE status = 'SUBMITTED' ORDER BY created_at DESC;
    ```

### 3.4 Data forcing

*   **Inserimento servizi (Base/Medio/Avanzato):**
    ```sql
    INSERT INTO services (name, description, price) VALUES 
    ('Extra', 'Servizio aggiuntivo extra', 49.99);
    ```
*   **Inserimento admin e customer (Hash Bcrypt per `P@ssw0rd!`):**
    ```sql
    INSERT INTO users (username, password, first_name, last_name, role) VALUES 
    ('nuovocustomer', '$2b$12$nfZIjfgGKuzM8LI66yeoiOgg9CkabDNrFLFM7I1yqJHck9E6QNlHO', 'Gino', 'Bianchi', 'CUSTOMER');
    ```
*   **Inserimento ordine di test:**
    ```sql
    INSERT INTO orders (user_id, service_id, status, note) VALUES 
    (3, 1, 'SUBMITTED', 'Ordine forzato da SQL');
    ```
*   **Aggiornamento stato ordine:**
    ```sql
    UPDATE orders SET status = 'COMPLETED' WHERE id = 1;
    ```
*   **Cancellazione controllata e rigenerazione seed:**
    ```sql
    DELETE FROM orders;
    DELETE FROM services;
    DELETE FROM users;
    -- Riavviare il container db forzando il volume reset eliminerà i dati 
    -- e lascerà ricaricare init.sql in automatico: docker compose down -v && docker compose up -d
    ```

---

## 4. API: Test manuale completo via `curl`

Definire le variabili nel proprio terminale prima di procedere:
```bash
export API_BASE=http://localhost:80/api/v1
export FE_BASE=http://localhost:80
```
*Nota: il frontend su porta 80 agisce da proxy `/api/` verso il backend sulla porta 8080. Entrambe le chiamate passano da `localhost:80`.*

### Frontend e Servizi Pubblici
*   **Homepage:**
    ```bash
    curl -i "$FE_BASE/"
    ```
    *Status Atteso: 200 OK (Ritorna HTML)*
*   **Lista servizi:**
    ```bash
    curl -i "$API_BASE/services"
    ```
    *Status Atteso: 200 OK. Body: `[{"id":1,"name":"Base",...}]`*

### Auth Customer e Ordini
*   **Login Customer:**
    ```bash
    curl -i -c customer.cookies -H "Content-Type: application/json" -d '{"username":"customer","password":"P@ssw0rd!"}' "$API_BASE/auth/login"
    ```
    *Status Atteso: 200 OK. Ritorna il JSON dell'utente.*
*   **Profilo:**
    ```bash
    curl -i -b customer.cookies "$API_BASE/me"
    ```
    *Status Atteso: 200 OK.*
*   **Storico ordini:**
    ```bash
    curl -i -b customer.cookies "$API_BASE/orders"
    ```
    *Status Atteso: 200 OK. Array JSON degli ordini.*
*   **Dettaglio ordine:**
    ```bash
    curl -i -b customer.cookies "$API_BASE/orders/1"
    ```
    *Status Atteso: 200 OK.*
*   **Creazione ordine:**
    ```bash
    curl -i -b customer.cookies -H "Content-Type: application/json" -d '{"serviceId":1,"note":"Test ordine via curl"}' "$API_BASE/orders"
    ```
    *Status Atteso: 201 Created.*
*   **Logout:**
    ```bash
    curl -i -b customer.cookies -X POST "$API_BASE/auth/logout"
    ```
    *Status Atteso: 200 OK.*

### Auth Admin e Gestione
*   **Login Admin:**
    ```bash
    curl -i -c admin.cookies -H "Content-Type: application/json" -d '{"username":"admin","password":"P@ssw0rd!"}' "$API_BASE/auth/login"
    ```
    *Status Atteso: 200 OK.*
*   **Lista ordini:**
    ```bash
    curl -i -b admin.cookies "$API_BASE/admin/orders"
    ```
    *Status Atteso: 200 OK.*
*   **Lista ordini filtrata:**
    ```bash
    curl -i -b admin.cookies "$API_BASE/admin/orders?status=SUBMITTED"
    ```
*   **Creazione utente (CRUD):**
    ```bash
    curl -i -b admin.cookies -H "Content-Type: application/json" -d '{"firstName":"Mario","lastName":"Rossi","username":"mrossi","password":"P@ssw0rd!","role":"CUSTOMER"}' "$API_BASE/admin/users"
    ```
    *Status Atteso: 201 Created.*
*   **Lettura utenti (CRUD):**
    ```bash
    curl -i -b admin.cookies "$API_BASE/admin/users"
    ```
*   **Update utente (CRUD):**
    ```bash
    curl -i -b admin.cookies -X PUT -H "Content-Type: application/json" -d '{"firstName":"Gigi","lastName":"Rossi","role":"CUSTOMER"}' "$API_BASE/admin/users/3"
    ```
*   **Delete utente (CRUD):**
    ```bash
    curl -i -b admin.cookies -X DELETE "$API_BASE/admin/users/3"
    ```
    *Status Atteso: 204 No Content.*

### Test Negativi (Sicurezza)
*   **Admin senza auth:**
    ```bash
    curl -i "$API_BASE/admin/orders"
    ```
    *Status Atteso: 401 Unauthorized.*
*   **Admin con cookie customer:**
    ```bash
    curl -i -b customer.cookies "$API_BASE/admin/orders"
    ```
    *Status Atteso: 403 Forbidden.*

---

## 5. Testing Automatizzato

Il repository contiene test automatici scritti in Java JUnit 5 + Mockito + Spring Boot Test, eseguiti di default durante la fase di build dell'immagine Docker (`mvn clean package`).

*   **Backend Unit Test:** `OrderServiceTest` (verifica la logica di submit ordine usando un db mockato).
*   **Backend Integration Test:** `UserRepositoryTest` (verifica la corretta associazione ORM su un database in-memory H2).
*   **Backend Authorization Test:** `SecurityAuthorizationTest` (MockMvc verifica che un utente `CUSTOMER` riceva 403 se tenta l'accesso ad `/api/v1/admin/orders` e un `ADMIN` riceva 200).

Per eseguire i test isolatamente senza buildare i container, dalla cartella backend:
```bash
cd backend && mvn test
```
---

## 6. Note Tecniche e Fix Recenti

### 6.1 Apache ServerName e ProxyPass
- È stata aggiunta la direttiva `ServerName FE-test-JS` per eliminare il warning di Apache all'avvio.
- È stato corretto il mapping del proxy: `ProxyPass /api/v1/` punta ora correttamente a `http://backend:8080/api/v1/`. Precedentemente, un mapping generico su `/api/` causava errori 403 Forbidden durante le chiamate POST (come il login) effettuate dal browser.


### 6.2 Configurazione Test Backend
Per consentire la corretta esecuzione dei test automatizzati durante la build Docker (`mvn clean package`), è stato aggiunto il file `backend/src/test/resources/application.properties`. Questo file configura un database H2 in-memory con `ddl-auto=create-drop`, evitando che i test tentino di connettersi a MySQL (non ancora disponibile durante la fase di build dell'immagine).


---


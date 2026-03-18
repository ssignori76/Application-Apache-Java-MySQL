## Specifica per agente AI: applicazione web containerizzata + runbook operativo completo in `Readme.MD`

### Ruolo

*   Agisci come **AI Software Engineer** incaricato di creare **da zero** un’applicazione web a 3 tier: **FE Apache (static headless JS)**, **MW Java (REST API)**, **DB MySQL**, distribuibile con **Docker Compose**.
*   Il risultato deve essere **eseguibile**, **testato**, **documentato**, con build **interamente via Dockerfile** (nessuna compilazione manuale sul host).

***

## Deliverable obbligatori

*   Repository con:
    *   `frontend/` (asset statici: HTML/CSS/JS serviti da Apache)
    *   `backend/` (Java REST API)
    *   `db/` (schema init + seed)
    *   `docker-compose.yml`
    *   `Readme.MD` (manuale operativo completo + checklist + test + curl + query DB)
*   Avvio locale con:
    *   `docker compose up --build`
*   Persistenza dati MySQL su volume.
*   Inizializzazione DB automatica (schema + seed) al primo avvio.

***

## Architettura e vincoli

*   FE:
    *   Apache HTTP Server serve **solo contenuti statici**.
    *   Frontend “headless”: JS nel browser chiama il backend via HTTP.
*   Backend:
    *   Java (framework a scelta; suggerito Spring Boot).
    *   API REST versionate (es. `/api/v1/...`).
    *   Autenticazione + autorizzazione con ruoli `CUSTOMER` e `ADMIN`.
    *   Password **hashate** (vietato plaintext).
    *   Log applicativi obbligatori su `stdout/stderr`.
*   DB:
    *   MySQL con schema relazionale minimo per utenti/clienti/ordini/servizi.
*   Docker:
    *   Ogni tier in container separato con proprio `Dockerfile`.
    *   Build FE e BE via Dockerfile (multi-stage consentito).
*   Compose:
    *   Rete dedicata.
    *   Healthcheck su DB e BE (obbligatorio).
    *   Il BE deve attendere DB ready (startup ordering + retry).

***

## Funzionalità applicative

*   Homepage pubblica.
*   Login.
*   Area privata cliente:
    *   Visualizzazione riferimenti cliente.
    *   Storico ordini sottomessi (lista + dettaglio).
*   Pagina servizi acquistabili:
    *   3 servizi predefiniti: **Base**, **Medio**, **Avanzato** (prezzo + descrizione).
    *   Sottomissione ordine (servizio + note).
*   Area amministrativa su URL dedicata:
    *   Verifica ordini su DB (lista + filtri minimi).
    *   Gestione utenti (CRUD): `nome`, `cognome`, `username`, `password`, `ruolo`.
*   Stati ordine minimi:
    *   `SUBMITTED`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`.

***

## `Readme.MD` (obbligatorio): Manuale di gestione operativa (Runbook)

### 1) Quickstart e lifecycle container

*   Avvio stack (foreground):
    *   `docker compose up --build`
*   Avvio stack (detached):
    *   `docker compose up -d --build`
*   Stop stack:
    *   `docker compose down`
*   Stop + reset dati (rimozione volumi):
    *   `docker compose down -v`
*   Stato servizi:
    *   `docker compose ps`
*   Rebuild singolo servizio:
    *   `docker compose build <service>`
*   Restart singolo servizio:
    *   `docker compose restart <service>`
*   Accesso shell (debug):
    *   `docker compose exec <service> sh`

### 2) Log: consultazione, struttura e verbosità

#### 2.1 Consultazione log via Docker Compose

*   Tutti i servizi:
    *   `docker compose logs -f --tail=200`
*   Singolo servizio:
    *   `docker compose logs -f --tail=200 <service>`

#### 2.2 Logging backend Java (obbligatorio)

*   Il backend deve produrre log su `stdout/stderr` e deve supportare:
    *   Modifica verbosità via variabili d’ambiente (nomi esatti documentati).
    *   Modifica struttura log:
        *   Formato testuale (default) con: timestamp, level, logger, messaggio.
        *   Formato strutturato (JSON) se implementato, attivabile via configurazione documentata.
*   `Readme.MD` deve includere:
    *   File di configurazione logging realmente usato (nome e path).
    *   Variabili d’ambiente supportate (es. root level e package/app level).
    *   Procedura applicazione modifica:
        *   `docker compose up -d --build <backend_service>` oppure `docker compose restart <backend_service>` (specificare quale è necessario in base all’implementazione).

#### 2.3 Logging Apache (obbligatorio)

*   `Readme.MD` deve includere:
    *   File e direttive effettive per `ErrorLog`, `CustomLog`.
    *   Come cambiare verbosità con `LogLevel`.
    *   Come cambiare struttura con `LogFormat`.
    *   Dove vengono emessi i log (stdout/stderr vs file) e come leggerli con `docker compose logs`.

***

## DB: accesso, schema, query operative, data forcing (obbligatorio in `Readme.MD`)

### 1) Accesso a MySQL via compose

*   Connessione interattiva:
    *   `docker compose exec <db_service> mysql -u<user> -p<password> <database>`

### 2) Inventario schema

*   Elenco tabelle:
    *   `SHOW TABLES;`
*   Per ogni tabella:
    *   `DESCRIBE <table>;`
*   `Readme.MD` deve riportare:
    *   Elenco tabelle realmente create.
    *   Scopo di ogni tabella in una riga.
    *   Colonne chiave (PK/FK) e relazioni principali.

### 3) Query operative specifiche (SQL completi e coerenti con lo schema reale)

*   Utenti:
    *   lista utenti con ruolo
    *   lookup per username
*   Catalogo servizi:
    *   lista servizi (Base/Medio/Avanzato)
*   Ordini:
    *   storico ordini per customer
    *   dettaglio ordine con join su servizio/i
    *   lista ordini admin con filtri (stato, range date)
*   Tutte le query devono essere riportate in forma completa e immediatamente eseguibile.

### 4) Data forcing (SQL completi e coerenti con lo schema reale)

*   Inserimento servizi Base/Medio/Avanzato.
*   Inserimento customer e admin.
*   Inserimento ordini di test per un customer.
*   Aggiornamento stato ordine.
*   Cancellazione controllata dataset di test e rigenerazione seed.
*   Le operazioni che coinvolgono password devono rispettare il meccanismo reale adottato (hash o endpoint dedicato); nel runbook devono essere presenti procedure eseguibili senza ambiguità.

***

## API: test manuale completo via `curl` (obbligatorio in `Readme.MD`)

### 1) Convenzioni runbook

*   Definire variabili ambiente (valori coerenti con `docker-compose.yml`):
    *   `API_BASE=http://localhost:<BE_PORT>/api/v1`
    *   `FE_BASE=http://localhost:<FE_PORT>`
*   Definire modalità di autenticazione implementata e i comandi `curl` coerenti:
    *   Se sessione cookie: usare `-c` (cookie jar) e `-b` (cookie jar).
    *   Se bearer token: estrarre token dalla risposta login e riusarlo in `Authorization: Bearer ...`.
*   Usare cookie jar separati:
    *   `customer.cookies`
    *   `admin.cookies`

### 2) `curl` obbligatori per tutte le funzionalità (comandi puntuali + expected status)

*   FE:
    *   Homepage:
        *   `curl -i "$FE_BASE/"`
*   Servizi:
    *   Lista servizi:
        *   `curl -i "$API_BASE/services"`
*   Auth customer:
    *   Login:
        *   `curl -i -c customer.cookies -H "Content-Type: application/json" -d '{"username":"<customer_user>","password":"<customer_pass>"}' "$API_BASE/auth/login"`
    *   Profilo:
        *   `curl -i -b customer.cookies "$API_BASE/me"`
    *   Storico ordini:
        *   `curl -i -b customer.cookies "$API_BASE/orders"`
    *   Dettaglio ordine:
        *   `curl -i -b customer.cookies "$API_BASE/orders/<order_id>"`
    *   Creazione ordine:
        *   `curl -i -b customer.cookies -H "Content-Type: application/json" -d '{"serviceId":"<service_id>","note":"Test ordine via curl"}' "$API_BASE/orders"`
    *   Logout:
        *   `curl -i -b customer.cookies "$API_BASE/auth/logout"`
*   Auth admin:
    *   Login:
        *   `curl -i -c admin.cookies -H "Content-Type: application/json" -d '{"username":"<admin_user>","password":"<admin_pass>"}' "$API_BASE/auth/login"`
*   Admin ordini:
    *   Lista ordini:
        *   `curl -i -b admin.cookies "$API_BASE/admin/orders"`
    *   Lista ordini filtrata (se implementata):
        *   `curl -i -b admin.cookies "$API_BASE/admin/orders?status=SUBMITTED"`
*   Admin utenti (CRUD):
    *   Create user:
        *   `curl -i -b admin.cookies -H "Content-Type: application/json" -d '{"firstName":"Mario","lastName":"Rossi","username":"mrossi","password":"P@ssw0rd!","role":"CUSTOMER"}' "$API_BASE/admin/users"`
    *   List users:
        *   `curl -i -b admin.cookies "$API_BASE/admin/users"`
    *   Update user:
        *   `curl -i -b admin.cookies -X PUT -H "Content-Type: application/json" -d '{"firstName":"Mario","lastName":"Rossi","role":"CUSTOMER"}' "$API_BASE/admin/users/<user_id>"`
    *   Delete user:
        *   `curl -i -b admin.cookies -X DELETE "$API_BASE/admin/users/<user_id>"`
*   Negative tests (sicurezza):
    *   Admin senza auth:
        *   `curl -i "$API_BASE/admin/orders"`
    *   Admin con cookie customer:
        *   `curl -i -b customer.cookies "$API_BASE/admin/orders"`

Per ogni comando `curl` elencato in `Readme.MD` devono essere presenti:

*   HTTP status atteso.
*   Esempio minimo di response body (coerente con implementazione reale).

***

## Testing automatizzato (obbligatorio)

*   Backend:
    *   Unit test su service layer.
    *   Integration test su repository/DB.
    *   Test autorizzazione: accesso admin negato senza ruolo.
*   End-to-end:
    *   Smoke test customer: homepage → login → servizi → submit ordine → storico.
    *   Smoke test admin: login → lista ordini → CRUD utenti.
*   Operativi:
    *   Verifica stack healthy.
    *   Verifica log visibili e variabili di verbosità funzionanti.
    *   Verifica accesso DB e query runbook eseguibili.
    *   Verifica data forcing riflesso in API/UI.



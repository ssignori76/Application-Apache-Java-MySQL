# Application-Apache-Java-MySQL (Monorepo)

Questa applicazione è composta da un **Backend Java (Spring Boot)**, un **Frontend Apache** e un database **MySQL**.
Il progetto è strutturato per supportare diverse modalità di deployment utilizzando i branch di Git.

## 📌 Navigazione Repository (Branching Strategy)

Il progetto è organizzato in diversi branch per supportare differenti modalità di deployment:

*   **`main`**: Contiene la descrizione generale del progetto, l'architettura logica e la documentazione di alto livello.
*   **`docker`**: Configurazione ottimizzata per **Docker Compose** (utilizzo locale). Spostati in questo branch per lo sviluppo rapido.
*   **`k8s`**: Manifest e configurazioni specifiche per **Kubernetes** (testato su Minikube con ConfigMaps e Namespace dedicato).

---

## 🏗 Architettura

```
┌──────────────┐      ┌──────────────┐      ┌──────────────┐
│   Frontend   │      │   Backend    │      │   Database   │
│  Apache 2.4  │─────▶│ Spring Boot  │─────▶│  MySQL 8.0   │
│  (port 80)   │      │  (port 8080) │      │  (port 3306) │
└──────────────┘      └──────────────┘      └──────────────┘
```

- **Frontend**: Apache HTTP Server con reverse proxy verso il backend (`/api/v1/` → `backend:8080`)
- **Backend**: Spring Boot 3.3.4, Java 17, REST API con autenticazione basata su sessione
- **Database**: MySQL 8.0 con schema inizializzato automaticamente

---

## 🚀 Quickstart Rapido

### Docker Compose (Branch `docker`)
Per un avvio rapido in locale:
```bash
git checkout docker
cp .env.example .env   # Personalizza le credenziali se necessario
docker compose up -d --build
```
L'applicazione sarà disponibile su `http://localhost:80`.

### Kubernetes (Branch `k8s`)
Per il deployment su cluster:
```bash
git checkout k8s
# Segui le istruzioni nel README del branch k8s
```

---

## 🔑 Credenziali di Default

| Utente     | Username   | Password    | Ruolo    |
|------------|------------|-------------|----------|
| Admin      | `admin`    | `P@ssw0rd!` | ADMIN    |
| Cliente    | `customer` | `P@ssw0rd!` | CUSTOMER |

> ⚠️ **Importante**: Cambiare le credenziali di default prima di qualsiasi deployment in produzione.

---

## 📡 API Endpoints

### Autenticazione
| Metodo | Endpoint              | Descrizione           | Accesso    |
|--------|-----------------------|-----------------------|------------|
| POST   | `/api/v1/auth/login`  | Login utente          | Pubblico   |
| POST   | `/api/v1/auth/logout` | Logout utente         | Autenticato|
| GET    | `/api/v1/me`          | Profilo utente corrente| Autenticato|

### Servizi
| Metodo | Endpoint           | Descrizione            | Accesso  |
|--------|--------------------|------------------------|----------|
| GET    | `/api/v1/services` | Lista servizi disponibili | Pubblico |

### Ordini (Customer)
| Metodo | Endpoint              | Descrizione          | Accesso           |
|--------|-----------------------|----------------------|-------------------|
| POST   | `/api/v1/orders`      | Crea nuovo ordine    | CUSTOMER / ADMIN  |
| GET    | `/api/v1/orders`      | Lista ordini propri  | CUSTOMER / ADMIN  |
| GET    | `/api/v1/orders/{id}` | Dettaglio ordine     | CUSTOMER / ADMIN  |

### Amministrazione
| Metodo | Endpoint                    | Descrizione            | Accesso |
|--------|-----------------------------|------------------------|---------|
| GET    | `/api/v1/admin/orders`      | Tutti gli ordini       | ADMIN   |
| GET    | `/api/v1/admin/users`       | Lista utenti           | ADMIN   |
| POST   | `/api/v1/admin/users`       | Crea utente            | ADMIN   |
| PUT    | `/api/v1/admin/users/{id}`  | Aggiorna utente        | ADMIN   |
| DELETE | `/api/v1/admin/users/{id}`  | Elimina utente         | ADMIN   |

---

## 🛠 Note Tecniche Recenti (v0.0.2)

Le ultime evoluzioni del progetto includono:
*   **Fix CORS**: Risolto l'errore 403 Forbidden configurando un `CorsConfigurationSource` esplicito nel backend per supportare chiamate da diversi Origin.
*   **Kubernetes Ready**: Esternalizzazione delle configurazioni Apache in ConfigMaps e isolamento nel namespace `apache-java-mysql`.
*   **Build Isolation**: Utilizzo di database H2 in-memory per l'esecuzione dei test durante la build dell'immagine Docker.
*   **Input Validation**: Validazione delle richieste API con Bean Validation (`@NotBlank`, `@NotNull`, `@Size`).
*   **Error Handling**: Gestione strutturata degli errori con risposte HTTP appropriate (400, 403, 404, 500).
*   **Security Hardening**: Protezione XSS nel frontend, password non esposta nelle risposte API.

---

## 📦 Immagini Docker Hub
Le immagini dell'applicazione sono disponibili pubblicamente sul profilo Docker Hub di `lusig76`:
- **Backend**: `lusig76/java-backend:latest`
- **Frontend**: `lusig76/apache-frontend:latest`

## 📋 Requisiti
- Git
- Docker & Docker Compose (per il branch `docker`)
- Cluster Kubernetes (Minikube, Kind, GKE, ecc.) & kubectl (per il branch `k8s`)

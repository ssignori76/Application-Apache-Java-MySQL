# Specifica per Agente AI: Applicazione Web 3-Tier (v0.0.2)

Questo documento è il punto di ingresso per ogni agente AI che lavora sul progetto. Deve essere letto interamente prima di ogni modifica.

## 1. Organizzazione del Repository (Git)

Il repository è strutturato in branch specializzati. Non mescolare le configurazioni tra i branch:

*   **`main`**: Contiene la descrizione generale del progetto, l'architettura logica e la documentazione di alto livello. È il branch di riferimento per la visione d'insieme.
*   **`docker`**: Specializzato per l'esecuzione tramite **Docker Compose**. Contiene i file `docker-compose.yml` e le configurazioni ottimizzate per lo sviluppo locale.
*   **`k8s`**: Specializzato per **Kubernetes (Minikube)**. Contiene i manifest in `k8s/` e utilizza le **ConfigMaps** per esternalizzare la configurazione di Apache e il seed del database.

---

## 2. Architettura Tecnica e Vincoli

*   **Frontend (FE)**: Apache HTTP Server (2.4-alpine). Serve asset statici (HTML/CSS/JS). Agisce da **Reverse Proxy** per le chiamate API verso `/api/v1/`.
*   **Backend (BE)**: Java 17 con Spring Boot 3.3.4. API REST versionate. Sicurezza gestita con Spring Security (sessioni basate su JSESSIONID).
*   **Database (DB)**: MySQL 8.0. Inizializzazione automatica via `init.sql`.
*   **Build**: Interamente containerizzata via Dockerfile (multi-stage). Il backend deve essere compilabile in isolamento (test con DB H2).

---

## 3. Protocollo di Test e Validazione (Mandatorio)

Ogni modifica deve essere validata seguendo questo protocollo:

### 3.1 Test Automatizzati (Backend)
*   **Unit Test**: Logica dei servizi (`OrderServiceTest`).
*   **Integration Test**: Persistenza su DB H2 in-memory (`UserRepositoryTest`).
*   **Security Test**: Verifica autorizzazioni e ruoli (`SecurityAuthorizationTest`).
    *   *Comando*: `cd backend && mvn test`

### 3.2 Test di Connettività e CORS
*   **Verifica CORS**: Assicurarsi che le richieste POST (login) non falliscano con errore 403 dal browser.
*   **Simulazione Browser**: `curl -v -X POST http://localhost/api/v1/auth/login -H "Origin: http://<USER_IP>"` (Deve restituire 200 OK e gli header CORS).

### 3.3 Test Operativi (Docker)
*   Verifica dello stack: `docker compose ps` (Tutti i servizi devono essere `healthy`).
*   Verifica log: `docker compose logs -f backend`.

### 3.4 Test Operativi (Kubernetes)
*   Verifica ConfigMaps: `kubectl get configmap frontend-config` (Deve contenere `httpd.conf`).
*   Verifica Accesso: `curl -i http://$(minikube ip):30080/api/v1/services`.
*   Hot Update: Modifica ConfigMap -> `kubectl apply` -> `kubectl rollout restart`.

---

## 4. Workflow di Sviluppo per Agenti AI

1.  **Ricerca**: Identificare il branch corretto su cui lavorare in base al target (Docker vs K8s).
2.  **Validazione**: Prima di modificare, riprodurre eventuali bug tramite script `curl` o test JUnit.
3.  **Implementazione**:
    *   Mantenere la compatibilità tra i branch.
    *   Aggiornare il `Readme.MD` specifico del branch se cambiano le modalità operative.
4.  **Versionamento**:
    *   Innalzare la versione nel `pom.xml` per cambiamenti significativi.
    *   Utilizzare i tag Git (es. `v0.0.x`) per i rilasci stabili.
    *   Aggiornare le immagini su Docker Hub (`lusig76/`) se necessario.

---

## 5. Documentazione Obbligatoria (Readme.MD)

Ogni branch deve avere un `Readme.MD` che dettagli:
*   Comandi di avvio e stop.
*   Procedure di consultazione log.
*   Query SQL operative per il data forcing.
*   Esempi completi di chiamate `curl` con status HTTP attesi.

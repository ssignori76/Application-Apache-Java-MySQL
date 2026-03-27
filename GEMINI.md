# Status Sessione e Handover per Agente AI

Questo file riassume lo stato finale del progetto dopo la sessione del 27/03/2026.

## 1. Stato del Progetto (v0.0.2)
- **Branch Strategy**:
    - `main`: Documentazione generale e navigazione.
    - `docker`: Sviluppo locale con Docker Compose (Fix CORS e H2 test inclusi).
    - `k8s`: Deployment Kubernetes con **ConfigMaps** per Apache e DB.
- **Immagini Docker (lusig76)**:
    - `lusig76/java-backend:latest`
    - `lusig76/apache-frontend:latest`
    - Entrambe le immagini sono state testate e funzionano su Minikube.

## 2. Fix Critici Effettuati
- **CORS 403**: Risolto in `SecurityConfig.java` tramite `CorsConfigurationSource`. Le chiamate POST dal browser ora funzionano.
- **Build Docker**: I test Maven ora usano H2 (`src/test/resources/application.properties`), evitando errori di connessione al DB durante la build.
- **Dockerfile**: Il backend Dockerfile ora usa un wildcard per il JAR (`backend-*.jar`), supportando i cambiamenti di versione nel `pom.xml`.

## 3. Test Eseguiti e Superati
- [x] **Unit & Integration Test**: `mvn test` nel backend (JUnit 5 + H2).
- [x] **CORS Check**: `curl` con header `Origin` simulato (ritorna 200 OK).
- [x] **Docker Compose**: Stack healthy e funzionante su porta 80.
- [x] **Kubernetes**: Deploy completo su Minikube con ConfigMap mounting.

## 4. Credenziali Predefinite
- **Admin**: `admin` / `P@ssw0rd!`
- **Customer**: `customer` / `P@ssw0rd!`

## 5. Istruzioni per il prossimo Agente
1. Leggere sempre `ai-agent-instruction.md` prima di iniziare.
2. Sincronizzare i cambiamenti di logica del backend tra i branch `docker` e `k8s`.
3. Verificare i test su Kubernetes usando l'IP di Minikube e la porta `30080`.

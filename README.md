# Application-Apache-Java-MySQL (Monorepo)

Questa applicazione è composta da un **Backend Java (Spring Boot)**, un **Frontend Apache** e un database **MySQL**.
Il progetto è strutturato per supportare diverse modalità di deployment utilizzando i branch di Git.

## 📌 Navigazione Repository (Branching Strategy)

Il progetto è organizzato in diversi branch per supportare differenti modalità di deployment:

*   **`main`**: Contiene la descrizione generale del progetto, l'architettura logica e la documentazione di alto livello.
*   **`docker`**: Configurazione ottimizzata per **Docker Compose** (utilizzo locale). Spostati in questo branch per lo sviluppo rapido.
*   **`k8s`**: Manifest e configurazioni specifiche per **Kubernetes** (testato su Minikube con ConfigMaps e Namespace dedicato).

---

## 🚀 Quickstart Rapido

### Docker Compose (Branch `docker`)
Per un avvio rapido in locale:
```bash
git checkout docker
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

## 🛠 Note Tecniche Recenti (v0.0.2)

Le ultime evoluzioni del progetto includono:
*   **Fix CORS**: Risolto l'errore 403 Forbidden configurando un `CorsConfigurationSource` esplicito nel backend per supportare chiamate da diversi Origin.
*   **Kubernetes Ready**: Esternalizzazione delle configurazioni Apache in ConfigMaps e isolamento nel namespace `apache-java-mysql`.
*   **Build Isolation**: Utilizzo di database H2 in-memory per l'esecuzione dei test durante la build dell'immagine Docker.

---

## 📦 Immagini Docker Hub
Le immagini dell'applicazione sono disponibili pubblicamente sul profilo Docker Hub di `lusig76`:
- **Backend**: `lusig76/java-backend:latest`
- **Frontend**: `lusig76/apache-frontend:latest`

## 📋 Requisiti
- Git
- Docker & Docker Compose (per il branch `docker`)
- Cluster Kubernetes (Minikube, Kind, GKE, ecc.) & kubectl (per il branch `k8s`)

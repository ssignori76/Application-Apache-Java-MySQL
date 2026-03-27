# Runbook Operativo: Applicazione Web Containerizzata

Questo repository contiene un'applicazione web a 3 tier composta da un frontend headless servito da Apache, un backend REST in Java (Spring Boot) e un database MySQL.

## 📌 Navigazione Repository

Il progetto è organizzato in diversi branch per supportare differenti modalità di deployment:

*   **`main`**: Informazioni generali, architettura e documentazione principale.
*   **`docker`**: Configurazione ottimizzata per **Docker Compose** (utilizzo locale).
*   **`k8s`**: Manifest e configurazioni specifiche per **Kubernetes** (testato su Minikube con ConfigMaps).

---

## 🚀 Quickstart (Docker Compose)

Per un avvio rapido in locale tramite Docker Compose, assicurati di essere sul branch `docker`:

```bash
git checkout docker
docker compose up -d --build
```

L'applicazione sarà disponibile su `http://localhost:80`.

---

## 🛠 Note Tecniche Recenti (v0.0.2)

Le ultime evoluzioni del progetto includono:
*   **Fix CORS**: Risolto l'errore 403 Forbidden configurando un `CorsConfigurationSource` esplicito nel backend per supportare chiamate da diversi Origin.
*   **Kubernetes Ready**: Esternalizzazione delle configurazioni Apache in ConfigMaps (disponibile nel branch `k8s`).
*   **Build Isolation**: Utilizzo di database H2 in-memory per l'esecuzione dei test durante la build dell'immagine Docker.

---

## 1. Lifecycle Container (Dettagli)
...

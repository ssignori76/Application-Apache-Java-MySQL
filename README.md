# Application-Apache-Java-MySQL (Monorepo)

Questa applicazione è composta da un **Backend Java (Spring Boot)**, un **Frontend Apache** e un database **MySQL**.
Il progetto è strutturato per supportare diverse modalità di deployment utilizzando i branch di Git.

## Struttura del Progetto (Branching Strategy)
- **`main`**: Contiene il codice sorgente dell'applicazione (Backend e Frontend).
- **`docker`**: Contiene la configurazione per **Docker Compose**.
- **`k8s`**: Contiene i manifest YAML per il deployment su **Kubernetes**.

---

## 🐋 Docker (Branch: `docker`)
Per vedere la versione Docker Compose, sposta la tua sessione sul branch `docker`:
```bash
git checkout docker
```
In questo branch troverai il file `docker-compose.yml` e uno script per il push delle immagini su Docker Hub.

---

## ☸️ Kubernetes (Branch: `k8s`)
Per vedere la configurazione Kubernetes, sposta la tua sessione sul branch `k8s`:
```bash
git checkout k8s
```
In questo branch troverai i manifest YAML per Deployments, Services, ConfigMaps e Secrets.

---

## Immagini Docker Hub
Le immagini dell'applicazione sono disponibili pubblicamente sul profilo Docker Hub di `lusig76`:
- **Backend**: `lusig76/java-backend:latest`
- **Frontend**: `lusig76/apache-frontend:latest`

## Requisiti
- Git
- Docker & Docker Compose (per il branch `docker`)
- Cluster Kubernetes (Minikube, Kind, GKE, ecc.) & kubectl (per il branch `k8s`)

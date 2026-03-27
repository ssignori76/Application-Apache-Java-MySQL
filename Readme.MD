# Runbook Operativo: Applicazione Web su Kubernetes (Minikube)

Questo branch è **specializzato per l'utilizzo con Kubernetes**. L'intera applicazione è isolata nel namespace `apache-java-mysql`.

---

## 1. Distribuzione su Minikube

Per distribuire l'intero stack applicativo:

1.  **Avvio Minikube:**
    ```bash
    minikube start --driver=docker
    ```

2.  **Build delle immagini (Minikube Docker Daemon):**
    ```bash
    eval $(minikube docker-env)
    docker build -t app-backend:latest ./backend
    docker build -t app-frontend:latest ./frontend
    ```

3.  **Applicazione dei manifest (Namespace incluso):**
    ```bash
    kubectl apply -f k8s/
    ```

4.  **Verifica dello stato dei Pod:**
    ```bash
    kubectl get pods -n apache-java-mysql -w
    ```

---

## 2. Accesso all'Applicazione

L'applicazione è esposta tramite un servizio di tipo `NodePort` sulla porta **30080**.

1.  **Recupero IP di Minikube:**
    ```bash
    export MINIKUBE_IP=$(minikube ip)
    ```

2.  **Accesso Web:**
    Apri il browser su `http://$MINIKUBE_IP:30080`.

3.  **Variabili per Test API (curl):**
    ```bash
    export API_BASE=http://$MINIKUBE_IP:30080/api/v1
    export FE_BASE=http://$MINIKUBE_IP:30080
    ```

---

## 3. Gestione ConfigMaps (Apache & DB)

Tutti i file di configurazione sono gestiti tramite ConfigMaps nel namespace `apache-java-mysql`.

### 3.1 Verifica e Ispezione
*   **Elenco ConfigMap:**
    ```bash
    kubectl get configmap -n apache-java-mysql
    ```
*   **Contenuto configurazione Apache:**
    ```bash
    kubectl describe configmap frontend-config -n apache-java-mysql
    ```

### 3.2 Aggiornamento della configurazione (Hot Update)
Se è necessario modificare la configurazione di Apache:

1.  Modificare il file `k8s/frontend-configmap.yaml`.
2.  Applicare la modifica:
    ```bash
    kubectl apply -f k8s/frontend-configmap.yaml
    ```
3.  Effettuare il restart dei pod:
    ```bash
    kubectl rollout restart deployment frontend -n apache-java-mysql
    ```
4.  Verificare l'aggiornamento:
    ```bash
    kubectl exec -n apache-java-mysql -it deployment/frontend -- cat /usr/local/apache2/conf/httpd.conf
    ```

---

## 4. Log e Debug su Kubernetes

*   **Log del Frontend (Apache):**
    ```bash
    kubectl logs -n apache-java-mysql -l app=frontend -f
    ```
*   **Log del Backend (Spring Boot):**
    ```bash
    kubectl logs -n apache-java-mysql -l app=backend -f
    ```
*   **Accesso interattivo al database:**
    ```bash
    kubectl exec -n apache-java-mysql -it deployment/db -- mysql -uapp_user -papp_password app_db
    ```

---

## 5. Note Tecniche

### 5.1 Namespace dedicato
Tutti gli oggetti Kubernetes (Deployment, Service, ConfigMap, PVC) sono creati nel namespace `apache-java-mysql`. Questo garantisce isolamento e ordine all'interno del cluster.

### 5.2 Dockerfile Fix
Il `backend/Dockerfile` è stato aggiornato (v0.0.2) per utilizzare un wildcard nel nome del file JAR (`backend-*.jar`).

### 5.3 CORS e Sicurezza
La configurazione di Spring Security include un `CorsConfigurationSource` esplicito per risolvere gli errori **403 Forbidden**.

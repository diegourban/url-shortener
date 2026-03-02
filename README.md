# url-shortener

A URL shortening service that converts long URLs into short, encoded codes. Built with Java 25, Spring Boot 4, PostgreSQL, and deployed on Kubernetes. 
Observability is provided by OpenTelemetry, with Prometheus for metrics, Tempo for traces, and Loki for logs, all visualized through Grafana.

> **Note:** This setup is currently focused on the **dev** environment, running on Minikube.

## Monitoring Stack Setup

### Prerequisites

Add Helm repositories:

```bash
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add grafana https://grafana.github.io/helm-charts
helm repo update
```

### Install Prometheus (metrics)

```bash
helm install prometheus prometheus-community/kube-prometheus-stack -n monitoring --create-namespace
```

### Install Tempo (tracing backend)

```bash
helm install tempo grafana/tempo -n monitoring
```

### Install Loki (log aggregation)

```bash
helm install loki grafana/loki-stack -n monitoring
```


## Accessing Grafana

Port-forward Grafana to localhost:

```bash
export POD_NAME=$(kubectl --namespace monitoring get pod -l "app.kubernetes.io/name=grafana,app.kubernetes.io/instance=prometheus" -oname)
kubectl --namespace monitoring port-forward $POD_NAME 3000
```

Then open http://localhost:3000 and log in with:

- **User:** admin
- **Password:** retrieve with:
  ```bash
  kubectl --namespace monitoring get secrets prometheus-grafana -o jsonpath="{.data.admin-password}" | base64 -d ; echo
  ```

### Grafana Data Sources

| Data Source | URL |
|-------------|-----|
| Prometheus  | `http://prometheus-kube-prometheus-prometheus.monitoring:9090` |
| Tempo       | `http://tempo.monitoring:4317` |
| Loki        | `http://loki.monitoring:3100` |

## Deploy Application

```bash
kubectl apply -f k8s/dev/
```

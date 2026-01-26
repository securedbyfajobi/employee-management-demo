{{/*
Expand the name of the chart.
*/}}
{{- define "employee-app.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
*/}}
{{- define "employee-app.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "employee-app.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "employee-app.labels" -}}
helm.sh/chart: {{ include "employee-app.chart" . }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}

{{/*
Selector labels for frontend
*/}}
{{- define "employee-app.frontend.selectorLabels" -}}
app: frontend
app.kubernetes.io/name: frontend
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Selector labels for backend
*/}}
{{- define "employee-app.backend.selectorLabels" -}}
app: backend
app.kubernetes.io/name: backend
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Selector labels for reports
*/}}
{{- define "employee-app.reports.selectorLabels" -}}
app: reports
app.kubernetes.io/name: reports
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Selector labels for mysql
*/}}
{{- define "employee-app.mysql.selectorLabels" -}}
app: mysql
app.kubernetes.io/name: mysql
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the image name
*/}}
{{- define "employee-app.image" -}}
{{- $registry := .global.imageRegistry -}}
{{- $repository := .image.repository -}}
{{- $tag := .image.tag | default "latest" -}}
{{- if $registry }}
{{- printf "%s/%s:%s" $registry $repository $tag }}
{{- else }}
{{- printf "%s:%s" $repository $tag }}
{{- end }}
{{- end }}

{{/*
Pod security context
*/}}
{{- define "employee-app.podSecurityContext" -}}
runAsNonRoot: {{ .runAsNonRoot }}
runAsUser: {{ .runAsUser }}
runAsGroup: {{ .runAsGroup }}
fsGroup: {{ .fsGroup }}
seccompProfile:
  type: RuntimeDefault
{{- end }}

{{/*
Container security context
*/}}
{{- define "employee-app.containerSecurityContext" -}}
allowPrivilegeEscalation: false
privileged: false
readOnlyRootFilesystem: {{ .readOnlyRootFilesystem | default false }}
capabilities:
  drop:
    - ALL
{{- end }}

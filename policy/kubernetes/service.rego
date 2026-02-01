package kubernetes.service

import rego.v1

# Services must not use NodePort in production namespaces
deny contains msg if {
	input.kind == "Service"
	input.spec.type == "NodePort"
	msg := sprintf("Service '%s' must not use NodePort — use ClusterIP or LoadBalancer", [input.metadata.name])
}

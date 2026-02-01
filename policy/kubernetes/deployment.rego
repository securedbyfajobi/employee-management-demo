package kubernetes.deployment

import rego.v1

# Deployments must not run containers as root
deny contains msg if {
	input.kind == "Deployment"
	some container in input.spec.template.spec.containers
	not container.securityContext.runAsNonRoot
	msg := sprintf("Container '%s' in Deployment '%s' must set securityContext.runAsNonRoot: true", [container.name, input.metadata.name])
}

# Containers must drop ALL capabilities
deny contains msg if {
	input.kind == "Deployment"
	some container in input.spec.template.spec.containers
	not container.securityContext.capabilities.drop
	msg := sprintf("Container '%s' in Deployment '%s' must drop ALL capabilities", [container.name, input.metadata.name])
}

# Containers must have resource requests
deny contains msg if {
	input.kind == "Deployment"
	some container in input.spec.template.spec.containers
	not container.resources.requests
	msg := sprintf("Container '%s' in Deployment '%s' must set resource requests", [container.name, input.metadata.name])
}

# Containers must have resource limits
deny contains msg if {
	input.kind == "Deployment"
	some container in input.spec.template.spec.containers
	not container.resources.limits
	msg := sprintf("Container '%s' in Deployment '%s' must set resource limits", [container.name, input.metadata.name])
}

# Container images must not use 'latest' tag
deny contains msg if {
	input.kind == "Deployment"
	some container in input.spec.template.spec.containers
	endswith(container.image, ":latest")
	msg := sprintf("Container '%s' in Deployment '%s' must not use the 'latest' image tag", [container.name, input.metadata.name])
}

# Container images must have an explicit tag
deny contains msg if {
	input.kind == "Deployment"
	some container in input.spec.template.spec.containers
	not contains(container.image, ":")
	msg := sprintf("Container '%s' in Deployment '%s' must use an explicit image tag", [container.name, input.metadata.name])
}

# Containers must use a read-only root filesystem
deny contains msg if {
	input.kind == "Deployment"
	some container in input.spec.template.spec.containers
	not container.securityContext.readOnlyRootFilesystem
	msg := sprintf("Container '%s' in Deployment '%s' must set securityContext.readOnlyRootFilesystem: true", [container.name, input.metadata.name])
}

# Containers must have readiness probes
warn contains msg if {
	input.kind == "Deployment"
	some container in input.spec.template.spec.containers
	not container.readinessProbe
	msg := sprintf("Container '%s' in Deployment '%s' should have a readinessProbe", [container.name, input.metadata.name])
}

# Containers must have liveness probes
warn contains msg if {
	input.kind == "Deployment"
	some container in input.spec.template.spec.containers
	not container.livenessProbe
	msg := sprintf("Container '%s' in Deployment '%s' should have a livenessProbe", [container.name, input.metadata.name])
}

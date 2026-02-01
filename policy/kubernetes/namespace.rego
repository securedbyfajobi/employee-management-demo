package kubernetes.namespace

import rego.v1

# Namespaces must have Pod Security Standards labels
deny contains msg if {
	input.kind == "Namespace"
	not input.metadata.labels["pod-security.kubernetes.io/enforce"]
	msg := sprintf("Namespace '%s' must have pod-security.kubernetes.io/enforce label", [input.metadata.name])
}

# Namespaces must have an owner label
deny contains msg if {
	input.kind == "Namespace"
	not input.metadata.labels.owner
	msg := sprintf("Namespace '%s' must have an 'owner' label", [input.metadata.name])
}

# Owner label must be a valid team name (lowercase alphanumeric with hyphens, 3-63 chars)
deny contains msg if {
	input.kind == "Namespace"
	owner := input.metadata.labels.owner
	not regex.match(`^[a-z][a-z0-9-]{2,62}$`, owner)
	msg := sprintf("Namespace '%s' owner label '%s' must be lowercase alphanumeric with hyphens (3-63 chars, e.g. 'platform-team')", [input.metadata.name, owner])
}

# Namespaces must have Prometheus scrape annotation
deny contains msg if {
	input.kind == "Namespace"
	not input.metadata.annotations["prometheus.io/scrape"]
	msg := sprintf("Namespace '%s' must have prometheus.io/scrape annotation", [input.metadata.name])
}

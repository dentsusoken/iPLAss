---
applyTo: "**"
excludeAgent: "coding-agent"
---

# Code Review Guidelines

Prefixes: [must] mandatory fix / [imo] suggestion / [ask] clarification / [nits] minor / [fyi] informational
Mapping: language guideline rules phrased as "use X" → [must]. Rules phrased as "recommend" or "consider" → [imo].

## Design

- Names MUST convey intent — if no concise name fits, split the unit
- Keep visibility to the minimum required
- Exceptions MUST be logged or rethrown with context
- Log messages MUST include concrete context (IDs, counts, state)
- Shared mutable state MUST be synchronized or use immutable/thread-local alternatives
- Validate and sanitize all input at system boundaries (user input, external APIs, file uploads)
- Store secrets (passwords, API keys, tokens) in external configuration or secret management
- Error responses to clients MUST contain only safe, user-facing messages

## Language (YOU MUST FOLLOW)

Write the entire review in 日本語（Japanese）.

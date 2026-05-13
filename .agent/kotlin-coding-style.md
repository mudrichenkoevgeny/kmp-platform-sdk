---
description: Naming conventions, when subject rules, brace requirements, and FQN/Comments ban
globs: "**/*.kt"
alwaysApply: true
---

# Kotlin Coding Style

This document defines the mandatory Kotlin syntax and formatting standards for the `kmp-platform-sdk`. These rules ensure maximum readability and consistency across all multiplatform modules.

## 1. Core Syntax Constraints

### Fully Qualified Names (FQN)
- **Strict Ban:** Do not use FQN in expressions, type annotations, or generics.
- **Resolution:** Use `import` or `typealias`.
- **Exception:** Minimal FQN is allowed only when imports are technically impossible or cause unresolvable ambiguity (justify the reason in the commit message).

### Comments
- **Strict Ban:** Do not write or preserve comments in the code.
- **Self-Documentation:** Logic must be clear through expressive naming and clean structure. If a complex algorithm requires explanation, refactor it into smaller, well-named functions or use KDoc for public APIs.

## 2. Control Flow

### `when` Expressions
- **No Invocations in Subject:** Do not put function or suspend calls inside `when (...)` parentheses.
- **Preceding Variable:** Assign the return value to a `val` on the preceding line, then use `when` only on that variable (a plain reference).
- **Forbidden Pattern:** Do not use `when (val x = someFunction())`. Always extract the invocation to its own `val` first.

### Braces for `if` and `return`
- **Block Body Requirement:** Do not write single-line early exits like `if (condition) return`.
- **Mandatory Braces:** Always use a block body `{ ... }` for `if` statements, even if they only perform a `return` or `return@label`.
- **Formatting:** Put the `return` statement on its own line inside the braces.

## 3. Naming Conventions

### Outcome Variable Naming
- **Domain Focus:** Use concrete names tied to the domain or operation (e.g., `authSessionResult`), not vague placeholders like `res` or `data`.
- **Result Types:** When a variable holds the outcome of a step (especially `AppResult` or similar), include `Result` in the identifier when it improves clarity (e.g., `userProfileResult`).

### Resource IDs
- Always match the naming conventions defined in `localization.md` (e.g., `error_user_invalid_password`).

## 4. Expression vs Body Functions
- **Simple Mappers:** Use expression-body functions (`fun toDto() = ...`) for simple transformations and mappers.
- **Complex Logic:** Use block-body functions (`fun process() { ... }`) for anything involving multiple steps, branching, or lifecycle-sensitive operations to ensure readability.

## 5. Sealed Types and Enums
- **Exhaustiveness:** When using `when` on a `sealed class` or `enum`, do not provide an `else` branch. Handle all cases explicitly to ensure the compiler catches new variants.
- **State Naming:** Use clear, state-describing names for sealed UI states (e.g., `Authorized`, `MfaRequired`).

---
*Refer to `AGENTS.md` for the full list of project standards.*
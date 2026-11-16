---
description: Local test execution policies and SDK module testing setup
globs: "**/*Test*.kt"
alwaysApply: true
---

# Local Testing Standards — kmp-platform-sdk

This document outlines repository-internal test organization for the `kmp-platform-sdk` codebase.

## 1. SDK Test Environment
- **SDK Module Testing:** Test suites in `:testing` provide base screenshot rules, `AppErrorParserMock`, and Robolectric setup used by feature modules (`feature:clientuser`, `feature:managementuser`) and consumer apps.
- **Sample Integration Tests:** Host application integration tests reside in `sampleclient` and `samplemanagement`.

## 2. Konsist Architecture Suite & Reuse
- **Architecture Assertions:** `:testing` (`jvmMain`) exports `SdkArchitectureAssertions` providing reusable Konsist rules for layer boundaries, single declaration per file, Decompose contracts, and Compose preview completeness.
- **Consumer Execution:** Consumer apps and host modules run universal architecture checks in `jvmTest` by invoking:
  ```kotlin
  @Test
  fun `run all universal KMP SDK consumer architecture rules`() {
      SdkArchitectureAssertions.assertAllConsumerRules()
  }
  ```

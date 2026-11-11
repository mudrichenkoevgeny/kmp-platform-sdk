---
description: Local test execution policies and SDK module testing setup
globs: "**/*Test*.kt"
alwaysApply: true
---

# Local Testing Standards — kmp-platform-sdk

This document outlines repository-internal test organization for the `kmp-platform-sdk` codebase.

## 1. SDK Test Environment
- **SDK Module Testing:** Test suites in `:core:testing` provide base screenshot rules, `AppErrorParserMock`, and Robolectric setup used by feature modules (`feature:clientuser`, `feature:managementuser`) and consumer apps.
- **Sample Integration Tests:** Host application integration tests reside in `sampleclient` and `samplemanagement`.

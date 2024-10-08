### Changelog

All notable changes to this project will be documented in this file. Dates are displayed in UTC.

#### [0.0.1-RC4]

- Introduced an AttackProcessor, which will be invoked with attackMetaData as you start an attack
- An attackPredicate can now be supplied during bundle init, and can be used as is. 

#### [0.0.1-RC3]

- Introduced attackCriteria, with time and percentage bounds
  belong in this boundary
- Introduced CHANGELOG

#### [0.0.1-RC2]

- A bug of meeting target load on CPU Attacker has been fixed
- Added test cases for the properties objects and interceptable attacks

#### [0.0.1-RC1]

- First version of vaanar, including CPU, memory, exception and latency attacks
- Interface definitions to do customAttackers as requried.
- A dropwizard bundle to quickly set up as a sidecar
- An example demonstrating how-to
- README explaining design considerations, and getting started 
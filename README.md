# Vaanar

Vaanar is a chaos monkey application for dropwizard. Inspired
by [Chaos Engineering @ Netflix](https://github.com/Netflix/chaosmonkey)
and [Chaos Monkey for Spring Boot](https://github.com/codecentric/chaos-monkey-spring-boot). It borrows the CPU and
Memory attacks from [Chaos Monkey for Spring Boot](https://github.com/codecentric/chaos-monkey-spring-boot), with
simplified interfaces and easy to plugin systems

<p> Vaanar provides the following chaos attacks </p>

- CPU Attack
- Memory Attack
- Latency Attack
- Exception Attack
- Sigterm Attack
- Custom Attack - Define your own attack by implementing `CustomAttackerFactory`

### Getting Started

#### Define your AttackConfiguration to begin with

```
{
  "enableDestruction" : true,
  "attackProperties" : [{
    "name" : "testAttack",
    "type" : "CPU"
    ....
  }]
```

#### Initialize the VaanarBundle

```
class TestBundle<T extends Configuration> extends VaanarBundle<T> {
    @Override
    protected AttackConfiguration getAttackConfiguration(T configuration) {
        return configuration.getAttackConfiguration();
    }

    @Override
    protected Optional<CustomAttackerFactory> getAdditionalAttackers(T configuration, Environment environment) {
        return Optional.empty();
    }
}
```

### Maven Dependency

- The bom is available at

```
<dependency>
    <groupId>com.grookage.vaanar</groupId>
    <artifactId>vaanar-bom</artifactId>
    <versio>0.0.1-RC1</version>
</dependency>
```

#### Preparing a function for attack

There are two kinds of attacks.

- Attacks that can start when the application starts - not interpretable by nature
- Interpretable attacks - For this, Annotate the method with `@AttackFunction` with the appropriate attackName specified
  in the config

For example:

```
    @AttackFunction(name = "testAttack")
    private void attackableFunction(String args..) {
        //Some body
    }
```

LICENSE
-------

Copyright 2024 Koushik R <rkoushik.14@gmail.com>.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.



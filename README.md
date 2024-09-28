# Vaanar

Vaanar is a chaos monkey application for dropwizard. Inspired
by [Chaos Engineering @ Netflix](https://github.com/Netflix/chaosmonkey)
and [Chaos Monkey for Spring Boot](https://github.com/codecentric/chaos-monkey-spring-boot).

<p> Vaanar provides the following chaos attacks </p>

- CPU Attack
- Memory Attack
- Latency Attack
- Exception Attack
- Sigterm Attack
- Custom Attack - Define your own attack by implementing `CustomAttackerFactory`

### Maven Dependency

- The bom is available at

```
<dependency>
    <groupId>com.grookage.vaanar</groupId>
    <artifactId>vaanar-bom</artifactId>
    <versio>0.0.1-RC2</version>
</dependency>
```

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

VaanarBundle works in conjunction with guiceBundle, at the moment. So you'll need to create both bundles
and wire them together for AttackFunctionInterceptor to work.

##### Creating the VaanarBundle, in your dropwizard initialize method

```
final var vaanarBundle = new VaanarBundle<AppConfiguration>() {
            @Override
            protected AttackConfiguration getAttackConfiguration(AppConfiguration configuration) {
                return configuration.getAttackConfiguration();
            }

            @Override
            protected Optional<CustomAttackerFactory> getAdditionalAttackers(AppConfiguration configuration, Environment environment) {
                return Optional.empty();
            }
        };
        bootstrap.addBundle(vaanarBundle);
```

##### Next, create the guiceBundle with autoConfigEnabled. This requires you to bind the AttackInterceptor

```
 final var guiceBundle = GuiceBundle.builder()
                .enableAutoConfig(getClass().getPackage()
                        .getName())
                .modules(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bindInterceptor(Matchers.any(), Matchers.annotatedWith(AttackFunction.class), vaanarBundle.getAttackInterceptor());
                    }

                    @Provides
                    public VaanarEngine getVaanarEngine() {
                        return vaanarBundle.getVaanarEngine();
                    }
                })
                .build(Stage.PRODUCTION);
        bootstrap.addBundle(guiceBundle);
```

This will be further simplified in the future and many custom attacks shall be added.

#### Preparing a function for attack

There are two kinds of attacks.

- Attacks that can start when the application starts - not interceptable by nature. These when marked as enabled, in the
  configuration start as soon as the app starts
- Interceptable attacks - For this, Annotate the method with `@AttackFunction` with the appropriate attackName specified
  in the config. These are custom attacks, that can be leveraged by the clients as well.

For example:

```
    @AttackFunction(name = "testAttack")
    private void attackableFunction(String args..) {
        //Some body
    }
```

#### You can find the dropwizard server example in vaanar-example, with a latencyMonkey configured

Please use github issues for features and asks.

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



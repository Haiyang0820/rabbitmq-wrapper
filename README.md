# Introduction

RabbitMQ client support cluster connection and use pool connection with channel

# Dependency

- amqp-client

```
    <dependency>
       <groupId>com.rabbitmq</groupId>
       <artifactId>amqp-client</artifactId>
       <version>4.1.1</version>
     </dependency>
```

- commons-pool

```
    <dependency>
      <groupId>commons-pool</groupId>
      <artifactId>commons-pool</artifactId>
      <version>1.6</version>
    </dependency>
```

# Usage

#### Init config
```Java
    RBConfiguration rbConfiguration = new RBConfiguration();
    rbConfiguration.setListhost("localhost:5672;localhost:5673;localhost:5674"); // with cluster
    rbConfiguration.setVirtualhost("/"); 
    rbConfiguration.setUsername("username"); 
    rbConfiguration.setPassword("password"); 
``` 

#### Send message

```java
     try {
        RBManager.getInstance(rbConfiguration).setMessage("queueName","message");
        RBManager.getInstance(rbConfiguration).setMessage("exchangeName","routingKey","message")
    } catch (Exception e) {
        e.printStackTrace();
    }
```

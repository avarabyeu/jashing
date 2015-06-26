# jashing [![Build Status](https://travis-ci.org/avarabyeu/jashing.svg?branch=master)](https://travis-ci.org/avarabyeu/jashing) [![Maven central](https://maven-badges.herokuapp.com/maven-central/com.github.avarabyeu.jashing/jashing/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.avarabyeu.jashing/jashing)

Java port Of [Dashing](https://github.com/Shopify/dashing)


* [Maven Dependencies](#maven-dependencies)
* [Getting Started](#getting-started)
   * [As Simple As It's Possible](#as-simple-as-its-possible)
   * [Deploy to container](#deploy-to-container)
* [Extensions](#extensions)
   * [Jira](#jira)
   * [Jenkins](#jenkins)
   * [GitHub](#github)
   * [Git](#git)
   * [SVN](#svn)


## Maven Dependencies

Last stable version:
```xml
<dependency>
    <groupId>com.github.avarabyeu.jashing</groupId>
    <artifactId>jashing</artifactId>
    <version>X.X.X</version>
</dependency>
```

## Getting Started

### As Simple As It's Possible

#### Starting
Add Jashing into your classpath, create main class and add the following code:

```java

public interface MainClass {

    public static void main(String[] args) {
        Jashing.builder().withPort(8282).build().bootstrap();
    }
}

```

That's it! Jashing has started on port 8282 and ready to show you demo dashboard

## Deploy to container

You can easily deploy Jashing to Servlet container. Make sure Jashing in your and implement

```java
com.github.avarabyeu.jashing.core.JashingFilter
```

Also make sure that your marked your filter with Servlet's WebFilter annotation
```java
 @WebFilter(urlPatterns={"/*"})
```

## Extensions
For now, Jashing supports the following list of extensions:

* Jira
* Jenkins
* Git
* SVN
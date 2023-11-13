![IMG](https://img.shields.io/github/license/ProOmnes/PROreports)
![IMG](https://img.shields.io/github/last-commit/ProOmnes/PROreports)
![IMG](https://img.shields.io/github/issues/ProOmnes/PROreports)
![IMG](https://img.shields.io/github/issues-closed/ProOmnes/PROreports)
![IMG](https://img.shields.io/github/issues-pr/ProOmnes/PROreports)
# PROreports
An advanced report system for reporting players with a user interface for moderators.

## Features
- Create a report as a player with your own reason
- See all your sent reports
- Manage current reports and process them as a moderator
- Data storage with **Yaml, MySQL or MongoDB**
- The whole plugin is customizable

> Further functionalities and features are already being planned. Further information is available on the [Discord](https://discord.gg/vgwJtjaFXM) server.

## How to install
1. Download the plugin.
2. Put the .jar file in your 'plugins' folder.
3. If you want to use MySQL as a data provider, please download [DBLib](https://cloudburstmc.org/resources/dblib.12/download) and place it in your 'plugins' folder as well.
4. Restart or start the server.
5. Configure the plugin in config.yml and have fun using this plugin!

> If you have any questions about the installation, we are available on our [Discord](https://discord.gg/vgwJtjaFXM) server or here on GitHub.

## Commands & Permissions
| Command           | Usage                      | Permission                |
|-------------------|----------------------------|---------------------------|
| **report**        | /report <optional: player> | *none*                    |
| **myreports**     | /myreports                 | *none*                    |
| **reportmanager** | /reportmanager             | proreports.role.moderator |

> All commands can also be edited.

## API
### Maven Dependency
```xml
<repository>
    <id>proomnes-repository-snapshots</id>
    <name>ProOmnes Repository</name>
    <url>https://repo.proomnes.net/snapshots</url>
</repository>

<dependency>
  <groupId>net.proomnes.proreports-nk</groupId>
  <artifactId>PROreports-NK</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Usage examples
```java
        // Get access to all functions in the ReportService class
        final ReportService reportService = PROreportsAPI.getReportService();

        // Events provided by this plugin
        @EventHandler
        public void on(final ReportCloseEvent event) {
    
        }

        @EventHandler
        public void on(final ReportCreateEvent event) {

        }

        @EventHandler
        public void on(final ReportUpdateEvent event) {

        }
```

## Images
Here are some images of the plugin.
![Image 1](https://form-images-nk.proomnes.net/img/27dcc13a-7b1d-4a84-902b-18bf8c8e9220.png)
![Image 1](https://form-images-nk.proomnes.net/img/7c3a2ef4-503a-4134-aa70-a6a2795d718c.png)
![Image 1](https://form-images-nk.proomnes.net/img/7525c32b-59f0-4bea-9daf-2993f0ef4bf0.png)
![Image 1](https://form-images-nk.proomnes.net/img/ec05a7fa-1339-4547-89c7-2ec658382915.png)
___
# Feedback & Support
![IMG](https://img.shields.io/github/issues/ProOmnes/PROreports)
![IMG](https://img.shields.io/github/issues-closed/ProOmnes/PROreports)
![IMG](https://img.shields.io/github/issues-pr/ProOmnes/PROreports)

We are very happy to receive any constructive feedback.
If you have any problems with this plugin, we are happy to help you on [GitHub](https://github.com/ProOmnes/PROreports) or on our [Discord](https://discord.gg/vgwJtjaFXM) server!

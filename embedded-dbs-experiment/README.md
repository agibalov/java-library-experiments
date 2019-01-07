# embedded-dbs-experiment

Experiments with:

* [Wix Embedded MySql](https://github.com/wix/wix-embedded-mysql). Known issues: it needs `libaio.so.1` which is not installed on Ubuntu 16.04 by default. You have to install it manually like this: `sudo apt -y install libaio1`.
* [MariaDB4j](https://github.com/vorburger/MariaDB4j). Known issues: it doesn't work when server's timezone is EDT, so you have to `SET GLOBAL time_zone='+00:00';`.
* [Embedded PostgreSQL Server](https://github.com/yandex-qatools/postgresql-embedded).


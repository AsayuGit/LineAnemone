
# LineAnemone

A simple command line pcap browser


[![GPLv2 License](https://img.shields.io/badge/License-GPL%20v2-blue.svg)](https://opensource.org/licenses/)

## Features

- Pcap Display
- Follow TCP Stream
- Interactive Menu
- Extensive command line options

## Supported Protocols

- ETHERNET
- ARP
- IPv4
- IPv6
- ICMP (v4)
- DHCP
- TCP
- UDP
- QUICK
- HTTP
- DNS


## Usage

#### Syntax
```bash
  java -jar LineAnemone.jar [options] [pcap]
```

#### Exemple
```bash
  EG: java -jar LineAnemone.jar -f 0 http.pcap
```

#### Command line options
|  Option | Description                                        |
| :------ | :------------------------------------------------- |
| `-h`    | Display this help menu                             |
| `-d`    | Display the pcap                                   |
| `-f`    | Follow a TCP Stream (If available)                 |
| `-s`    | Show a single packet in more detail (If available) |
| `-q`    | Exit LineAnemone                                   |

#### Menu options
|  Option |   Alt         | Description                                        |
| :------ | :------------ | :------------------------------------------------- |
| `h`     | `help`        | Display this help menu                             |
| `d`     | `display`     | Display the pcap                                   |
| `f`     | `follow`      | Follow a TCP Stream (If available)                 |
| `s`     | `show`        | Show a single packet in more detail (If available) |
| `q`     | `quit / exit` | Exit LineAnemone                                   |

### CLI Examples

#### To open a pcap in interactive mode:
```bash
  java -jar LineAnemone.jar pcap/http.pcap
```

#### To follow the first tcp stream:
```bash
  java -jar LineAnemone.jar -f 0 pcap/http.pcap
```

#### To display the sixt packet:
```bash
  java -jar LineAnemone.jar -s 6 pcap/http.pcap
```

#### To do both and exit:
```bash
  java -jar LineAnemone.jar -f 0 -s 6 -q pcap/http.pcap
```

## Dependencies

In order to be able to build LineAnemone you will need make and a recent version of Java.

## How to Build

To build and run do :

```bash
  cd LineAnemone
  make
  java -jar LineAnemone.jar pcap/http.pcap
```
## Authors

- [@AsayuGit](https://github.com/AsayuGit)

## FAQ

#### What are the supported OS for LineAnemone ?

LineAnemone is build with Java so it should be able to run on any OS where the Jvm is installed.

# LineAnemone

Un navigateur de ficher pcap en ligne de commande


[![GPLv2 License](https://img.shields.io/badge/License-GPL%20v2-blue.svg)](https://opensource.org/licenses/)

## Fonctionalités

- Affichage contenu pcap
- Suivi flux tcp
- Menu Interactif
- Utilisable en ligne de commande

## Protocoles Supportés

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


## Utilisation

#### Syntax
```bash
  java -jar LineAnemone.jar [options] [pcap]
```

#### Exemple
```bash
  EG: java -jar LineAnemone.jar -f 0 http.pcap
```

#### Options en ligne de commande
|  Option | Description                                        |
| :------ | :------------------------------------------------- |
| `-h`    | Display this help menu                             |
| `-d`    | Display the pcap                                   |
| `-f`    | Follow a TCP Stream (If available)                 |
| `-s`    | Show a single packet in more detail (If available) |
| `-q`    | Exit LineAnemone                                   |

#### Options en mode interactif
|  Option |   Alt         | Description                                        |
| :------ | :------------ | :------------------------------------------------- |
| `h`     | `help`        | Display this help menu                             |
| `d`     | `display`     | Display the pcap                                   |
| `f`     | `follow`      | Follow a TCP Stream (If available)                 |
| `s`     | `show`        | Show a single packet in more detail (If available) |
| `q`     | `quit / exit` | Exit LineAnemone                                   |

### CLI Examples

#### Pour ouvrir un pcap en mode intéractif:
```bash
  java -jar LineAnemone.jar pcap/http.pcap
```

#### Pour suivre le premier flux tcp:
```bash
  java -jar LineAnemone.jar -f 0 pcap/http.pcap
```

#### Pour afficher le sixième packet:
```bash
  java -jar LineAnemone.jar -s 6 pcap/http.pcap
```

#### Pour faire les deux et sortir:
```bash
  java -jar LineAnemone.jar -f 0 -s 6 -q pcap/http.pcap
```

## Dépendences

Pour fonctionner LineAnemone a juste besoin d'une version recente de Java (Ex 21).

## Comment compiler ?

Pour compiler, executez :

```bash
  cd LineAnemone
  make
  java -jar LineAnemone.jar pcap/http.pcap
```
## Démarche

Pour réaliser ce projet et parser les ficher PCAP ma démarche auras été de consevoir une
classe par donnée manipulable dans le pcap, que ce soit le fichier en lui même mais
également les différents protocoles contenu au sein de ce dernier.

Chaque classe de protocole ignore tout du protocole parent ainsi que de l'enfant, ce
qui nous permet de ne pas dépendre de l'implémentation d'un protocole spécifique et de
se reposer plutot sur leus abstraction.

Chaque classe de protocole s'initialise d'elle meme en lui passant la section du fichier pcap qui content des données potentielles.

De façon a ne pas dupliquer de code pour des raisons innutiles, plusieurs classe représentes des concepts communs a plusieurs protocoles, tels que ContainerProtocol,
NetworkProtocol et TransportProtocol.

## Comment tester ?

des pcap de test sont disponible dans le dossier pcap, LineAnemode à été testé contre
eux lors de son développement, ainsi tout pcap additionel est le bienvenue.

### Exemples de tests :

```bash
  java -jar LineAnemone.jar pcap/http.pcap
```

```bash
  java -jar LineAnemone.jar pcap/dns-3.pcap
```

```bash
  java -jar LineAnemone.jar pcap/icmpPing.pcap
```

## Auteurs

- [@AsayuGit](https://github.com/AsayuGit)

## FAQ

#### Quels sont les OS supporté par LineAnemone ?

LineAnemone est programmé en Java et donc tourneras sur tout OS ou Java est installé.
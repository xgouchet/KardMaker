# KardMaker

A personal project used to quickly generate cards for board game prototypes.

More to come soon.

## Build

```shell
./gradlew :cli:shadowjar
```

## Run

To generate cards from a given `config.json` :

```shell
java -jar cli/build/libs/cli-all.jar config.json
```

### Command line options

| Short | Full         | Description                                                     |
|-------|--------------|-----------------------------------------------------------------|
| `-v`  | `--verbose`  | Verbose logs                                                    |
| `-d`  | `--debug`    | Draw debug lines on the generated images                        |
| `-b`  | `--no-bleed` | Draw images without the bleed content                           |
| `-f`  | `--folder`   | Treats the input path as a folder and find all json files in it |


# JOTT_compiler

A translator for the programming language Jott coded in Java.

## Building

Download the source code and unzip it. Then open a command line in the root folder (where this README file is).

Then run the following command:

```bash
javac -d bin -sourcepath src src/convert/Jott.java
```

## Usage

Open a command line in the bin folder and run one of the following commands to convert your Jott file to your language of choice:
```bash
# Jott
java convert/Jott <input_file.jott> <output_file.jott> Jott

# C
java convert/Jott <input_file.jott> <output_file.c> C

# Java
java convert/Jott <input_file.jott> <output_file.java> Java

# Python
java convert/Jott <input_file.jott> <output_file.py> Python
```

## Credits

* Aaron Berghash (amb8489)

* Connor Switenky (cs4331)

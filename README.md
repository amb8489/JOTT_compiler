# JOTT_compiler

A translator for the programming language Jott coded in Java.

## Building

Download the source code and unzip it. Then open a command line in the root folder (where this README file is).

Then run the following command:

```bash
javac -d bin -sourcepath src src/Jott.java
```

## Usage

Open a command line in the bin folder and run one of the following commands to convert your Jott file to your language of choice:
```bash
# Jott
java Jott <input_file.jott> <output_file.jott> Jott

# C
java Jott <input_file.jott> <output_file.c> C
# compiling & running C code
gcc -o <output_file> <output_file.c>
<output_file>

# Java
java Jott <input_file.jott> <output_file.java> Java
# compiling & running Java code
javac <output_file.java>
java <output_file>


# Python
java Jott <input_file.jott> <output_file.py> Python
# running Python code
python3 <output_file.py>
```










## Credits

* Aaron Berghash (amb8489)

* Connor Switenky (cs4331)

* Jake Peverly (jzp7326)

* Kaitlyn DeCola (kmd8594)

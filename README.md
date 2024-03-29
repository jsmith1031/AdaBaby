# AdaBaby
AdaBaby is a simplified Ada compiler made using coco framework

# Introduction
This simplified Ada95 compiler is built utilizing several frameworks to assist speedy construction, it has a simplified grammar with a custom tokenizer,
abstract symbol tree builder, tree walkers, and register allocator. 

# Scanner and Lexer

* **Ian Paterson** - Github: [ipaterson33](https://github.com/ipaterson33)
* **Jeffrey Smith** - Github: [jsmith1031](https://github.com/jsmith1031)
* [**ITEC 460 (Translator Design and Construction) Spring 2020**](https://www.radford.edu/nokie/classes/460/)

Email: 

[jsmith1031@radford.edu](mailto::jsmith1031@radford.edu)

[ipaterson@radford.edu](mailto::ipaterson@radford.edu)

#

## Sources
* [Coco/r for Java](http://www.ssw.uni-linz.ac.at/coco/)
* [ADAic](https://www.adaic.org/resources/add_content/docs/95style/html/sec_1/toc.html)
* [BNF of ADA](http://cui.unige.ch/isi/bnf/Ada95/BNFindex.html)
## Getting Started

From the Coco/r for Java section, download Coco.jar

Also download the Scanner.frame and Parser.frame

I also downloaded the Java.ATG and JavaParser.java for starting samples

Run the .atg file using coco.jar
>java -jar coco.jar AdaBaby.atg

Compile the parser.java
>javac JavaParser.java



>java compile test.adb

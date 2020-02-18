compile: compile.java Scanner.java Parser.java
	java -jar Coco.jar AdaBaby.atg
	javac compile.java Scanner.java Parser.java

run: 
	java compile test.adb
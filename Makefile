CC := javac

SRC :=  src/LineAnemone.java

OUT := out

PROJ := LineAnemone

build:
	$(CC) -sourcepath src -d $(OUT) $(SRC)
	jar cfe $(PROJ).jar $(PROJ) -C $(OUT) .

clean:
	rm -rf $(OUT) $(PROJ).jar

run:
	java -jar $(PROJ).jar
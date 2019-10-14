JC = javac

PATH_TO_FX=/home/vcarloto/Documents/practice/openjfx-11.0.2_linux-x64_bin-sdk/javafx-sdk-11.0.2/lib
JFLAGS = --module-path $(PATH_TO_FX) --add-modules javafx.controls,javafx.fxml
.SUFFIXES: .java .class


.java.class:
	$(JC)  $(JFLAGS) $*.java


CLASSES = \
		Server.java \
		Client.java \
		User.java \
		BoardController.java \
		BoardSelectionController.java \
		BlackBoard.java \
		BlackBoardInterface.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class

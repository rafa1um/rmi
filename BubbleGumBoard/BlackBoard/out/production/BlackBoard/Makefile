JC = javac

PATH_TO_FX=/home/jvrn3/Downloads/javafx-sdk-11.0.2/lib/
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

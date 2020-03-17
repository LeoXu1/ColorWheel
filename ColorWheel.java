import javafx.application.Application;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;

public class ColorWheel extends Application {
    int dir = 5;
    int angle = 0;
    int arcNum = 0;
    int numArcs = 3;
    int score = 0;
    int prevScore = 0;
    int level = 1;
    int prevLevel = 1;
    boolean started = false;
    boolean passed = false;
    Random random = new Random();
    Color[] colors = new Color[] {Color.BLUE, Color.RED, Color.YELLOW,
                                  Color.ORANGE, Color.PURPLE, Color.GREEN,
                                  Color.PINK, Color.BROWN, Color.INDIGO,
                                  Color.NAVY};
    ArrayList<Arc> arcs = new ArrayList<Arc>();
    Pane pane = new Pane();
    Rectangle pointer = new Rectangle(200, 195, 100, 10);
    PauseTransition pauseTransition = new PauseTransition();
    SequentialTransition timeline = new SequentialTransition();

    public void start(Stage stage) {
	VBox box = new VBox();
	HBox scoreBox = new HBox();
	Label scoreLabel = new Label("Score: ");
	Label scoreDisplay = new Label("0");
	Rotate rotate = new Rotate();
	rotate.setAngle(1);
	rotate.setPivotX(200);
	rotate.setPivotY(200);

	Scene scene = new Scene(box, 400, 400);
	scene.setOnKeyPressed(event -> {
		switch(event.getCode()) {
		case SPACE:
		    passed = false;
		    if (started == false) {
			started = true;
		    }
		    else {
			if (angle > arcNum * (360/numArcs) && angle < (arcNum + 1) * (360/numArcs)) {
			    score++;
			    scoreDisplay.setText(Integer.toString(score));
			}
			else {
			    timeline.stop();
			    Alert gameOver = new Alert(AlertType.INFORMATION);
			    gameOver.setHeaderText("Game Over!");
			    gameOver.setContentText("Score: "+score);
			    gameOver.show();
			}
			int lastNum = arcNum;
			arcNum = random.nextInt(numArcs);
			while (lastNum == arcNum) {
			    arcNum = random.nextInt(numArcs);
			}
			dir = dir * -1;
			pointer.setFill(colors[arcNum]);
		    }
		default:

		}
	    });
	setupLevel(3);
	EventHandler<ActionEvent> handler = event -> {
	    if (started) {
		angle = angle + dir;
		if (angle > 360) {
		    angle -= 360;
		}
		if (angle < 0) {
		    angle += 360;
		}
		rotate.setAngle(angle);
		pointer.getTransforms().setAll(rotate);
		if (angle > arcNum * (360/numArcs) && angle < (arcNum + 1) * (360/numArcs)) {
		    passed = true;
		}
		if (angle > (arcNum + 1) * (360/numArcs) && passed) {
		    timeline.stop();
		    Alert gameOver = new Alert(AlertType.INFORMATION);
		    gameOver.setHeaderText("Game Over!");
		    gameOver.setContentText("Score: "+score);
		    gameOver.show();
		}
	    }
	    if (score > 3 && level == 1) {
		level++;
		numArcs++;
		prevScore = score;
		prevLevel = level;
		timeline.stop();
		timeline = new SequentialTransition();
	        timeline.getChildren().add(pauseTransition);
		timeline.setCycleCount(Timeline.INDEFINITE);
		pointer = new Rectangle(200, 195, 100, 10);
		setupLevel(numArcs);
		timeline.play();
	    }
	    if (score > prevScore + numArcs * 2 && level == prevLevel) {
		level++;
                numArcs++;
                prevScore = score;
                prevLevel = level;
                timeline.stop();
                timeline = new SequentialTransition();
                timeline.getChildren().add(pauseTransition);
                timeline.setCycleCount(Timeline.INDEFINITE);
                pointer = new Rectangle(200, 195, 100, 10);
                setupLevel(numArcs);
                timeline.play();
	    }
	};
	pauseTransition.setDuration(Duration.seconds(0.05));
	pauseTransition.setOnFinished(handler);
	timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getChildren().setAll(pauseTransition);
	timeline.play();

	scoreBox.getChildren().addAll(scoreLabel, scoreDisplay);
        box.getChildren().addAll(scoreBox, pane);
	stage.setScene(scene);
	stage.sizeToScene();
	stage.show();
    }

    public void setupLevel(int num) {
	pane.getChildren().clear();
	arcs.clear();
	pointer.getTransforms().clear();
	pane.getChildren().add(pointer);
	numArcs = num;
	started = false;
	angle = 0;
	if (dir < 0) {
	    dir = dir * -1;
	}
	arcNum = random.nextInt(numArcs);
	pointer.setFill(colors[arcNum]);
	for (int i = 0; i < num; i++) {
            arcs.add(new Arc(200, 200, 100, 100, -i * (360/num), -(360/num)));
        }
        for (int i = 0; i < num; i++) {
            arcs.get(i).setFill(Color.TRANSPARENT);
            arcs.get(i).setType(ArcType.OPEN);
            arcs.get(i).setStroke(colors[i]);
	    arcs.get(i).setStrokeWidth(5);
        }
	pane.getChildren().addAll(arcs);
    }
}
    

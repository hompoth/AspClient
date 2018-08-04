// Mission: v Fix all errors
//	v		Handle no username/password error (display to user) search javafx popup/display
//	x		Use proper gameLoop - Josh v
//			Respond to pings with pongs (this should allow you to stay logged in) - Josh v
// Optional: v	Make the client display in a nicer way (move around text fields, etc)
//				Read from an ip/port file
//				Use the world class file for the full screen game to hold map data, player data, etc.
//					Create Player/NPC/Window/Etc classes for your full screen client
//				Make sure the socket connected before sending / reading data (Surround with try/catch and handle the error. Send to user)
// Advanced : Change this application to use FXML MVC architecture. (Very good learning material)
//			Go over a bunch of design patterns and try to clean up the code a much as possible.
//			You want there to be high cohesion with low coupling. (Look it up)

package client;

import client.World.ConnectionState;
import client.GetImage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class LoginClient extends Application{
    Game game;
    GameView gameView;
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane border = new BorderPane();
        GridPane grid = new GridPane();
        TextField usernameFld = new TextField();
        TextField passwordFld = new PasswordField();
        
        Button loginBtn = new Button();
        Button exitBtn = new Button();
    	
        primaryStage.setTitle("Connect to Server");
        primaryStage.setOnCloseRequest(e -> {
        	if (game != null && game.isRunning()) {
        		game.setConnectionState(ConnectionState.Disconnected);
        	}
            //Platform.exit(); // Add if using the stop method
            System.exit(0);
        });
        
        loginBtn.setText("Login");
        loginBtn.setPrefWidth(100);
       
        exitBtn.setText("Exit");
        exitBtn.setPrefWidth(100);
        exitBtn.setTranslateX(-100);
        
        usernameFld.setPrefWidth(200);
        passwordFld.setPrefWidth(200);
        
        EventHandler<ActionEvent> loginEventHandler = event -> {
			String username, password;
			username = usernameFld.getText();
			password = passwordFld.getText();
			run(username, password, primaryStage);
        };
        
        EventHandler<ActionEvent> exitEventHandler = event -> primaryStage.fireEvent(
        		new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST)
			);

        passwordFld.setOnAction(loginEventHandler);
        loginBtn.setOnAction(loginEventHandler);
        exitBtn.setOnAction(exitEventHandler);

        //TODO: User HBox and VBox for the following:
        //HBox hbox = new HBox();
        grid.add(new Label("Username: "), 1, 1);
        grid.add(new Label("Password: "), 1, 2);
        grid.add(usernameFld, 2, 1, 1, 1);
        grid.add(passwordFld, 2, 2, 1, 1);
        grid.add(loginBtn, 2, 3);
        grid.add(exitBtn, 3, 3);
        
        border.setBackground(new Background(new BackgroundImage(new Image("skins/Background.bmp"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        border.setBottom(grid);
        border.setPadding(new Insets(22));

        primaryStage.setScene(new Scene(border, 640, 480)); // TODO: Save this scene if you want to see it on quit.
        primaryStage.setResizable(false);
        primaryStage.show();

        // TODO: Create an options window to choose the size of the game (or fullscreen)
        // Center the canvas and make sure the canvas keeps the 640/480 ratio
        double width = 1600;
        double height = 1200;
        Group root = new Group();
        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, width, height, Color.BLACK);
		gameView = new GameView(primaryStage, scene, canvas.getGraphicsContext2D(), width, height);
    }

	private void run(String username, String password, Stage primaryStage) {
		// TODO: Make it so that two threads can't be run at the same time (Logging in twice)
		// For example, destroy this.g before adding a new one. The thread will kill itself in most cases so its not an issue yet.
		//this.game = new Game(username, password, "192.168.191.1", 2006, gameView);
		this.game = new Game(username, password, "xendria.access.ly", 2006, gameView);
		//this.game = new Game(username, password, "192.168.0.107", 2006, gameView);
        //game.start();
	}

}

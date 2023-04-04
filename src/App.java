import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXMLLoader;
import javafx.util.Duration;
import javafx.scene.layout.Pane;
import views.screen.EcoBikeMainScreenHandler;
import utils.Configs;

public class App extends Application {

	public void start(Stage initStage) throws Exception {
		try {
			Pane root = (Pane) FXMLLoader.load(getClass().getResource(Configs.SPLASH_SCREEN_PATH));
			Scene scene = new Scene(root);
			initStage.setScene(scene);
			initStage.show();
			SequentialTransition seqTransition = new SequentialTransition(root, new PauseTransition(Duration.seconds(1.5)));
			seqTransition.play();
			seqTransition.setOnFinished((e) -> {
				EcoBikeMainScreenHandler homeHandler = EcoBikeMainScreenHandler.getEcoBikeMainScreenHandler(initStage, null);
				homeHandler.show();
			});
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}

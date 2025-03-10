package app;

import java.io.IOException;

import app.db.daos.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author Philippe Duval
 * 
 *         This is our main class. It extends Application, which is a JavaFX
 *         class, and thus implements the inherited methods
 *
 */
public class App extends Application {

	private static Scene scene;
	
	// Lets add the main layout of our application as a static member, shall we ?
	// This will help us avoiding dodgy explicit casts
	private static BorderPane mainlayout;

	//tbf I wanted to remove the navbar without breaking the code, so I just commented most of its fxml
	@Override
	public void start(Stage stage) throws IOException {
		//db
		DatabaseManager.initializeDatabase();

		//icon
		stage.getIcons().add(new Image(App.class.getResourceAsStream("/app/view/contact.png")));
        // Nothing new here
		stage.setTitle("Java2Project : Contact Manager");
		// Load the main layout from file
		mainlayout = loadFXML("MainLayout");
		// Back to normal, except we use our newly defined member. Seems cumbersome, but
		// it will make sense in two seconds
		scene = new Scene(mainlayout, 640, 480);
		stage.setScene(scene);
		stage.show();
		// This is also new for this PW : you have to call the default view you want to
		// see when the application launches
		App.showView("PersonLayout");
	}

	// Here is the real game changer: let's change Parent with something more, let's
	// say, generic. We can do this as fxmlLoader.load() specifically returns a
	// generic object.
	// Doing this allows us to call loadFXML to populate whatever object type we
	// think relevant, not only Parent objects (here, we also are looking for Nodes
	// ones indeed)
	private static <T> T loadFXML(String fxml) throws IOException {
		// As we will use this method a lot, and we have all our view in a specific
		// package, let's put it there to save some typing :)
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/app/view/" + fxml + ".fxml"));
		return fxmlLoader.load();
	}

	public static void main(String[] args) {
		launch();
	}

	/**
	 * @param rootElement updates the center of our layout with the @rootElement
	 *                    passed in parameter
	 */
	public static void showView(String rootElement) {
		try {
			// We can only set the center of a borderPane, not a Parent, so we rely on
			// either an explicit cast or our better generics implementation to convert our
			// scene and modify it.
			mainlayout.setCenter(loadFXML(rootElement));
		} catch (IOException e) {
			// Chances are that the file is not found. Nothing we can do, really, but as
			// IOException is checked, it would require us to add nasty support all over our
			// code
			// Instead, a better practice is to convert this checked exception into an
			// unchecked exception, and let it bubble up to the main thread, killing the
			// JVM. We could also close the app, but it would be dangerous, as some
			// resources could be opened somewhere and not correctly closed...
			// BEWARE ! you should ALWAYS keep the stacktrace complete. using the original
			// exception as an argument allows that !
			throw new IllegalArgumentException(e);
		}
	}
}

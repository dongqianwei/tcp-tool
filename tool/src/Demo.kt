import javafx.application.Application
import javafx.stage.Stage
import javafx.fxml.FXMLLoader
import javafx.scene.Scene

fun main(args: Array<String>) {
    Application.launch(javaClass<App>())
}

class App: Application() {

    var controllor: Controllor? = null

    override fun start(primaryStage: Stage?) {
        val loader = FXMLLoader(this.getClass().getResource("demo.fxml"))
        primaryStage!!.setScene(Scene(loader.load()))
        controllor = loader.getController()
        primaryStage!!.show()
    }
}
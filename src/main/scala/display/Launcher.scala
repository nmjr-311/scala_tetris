package display

import java.io.IOException

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{NoDependencyResolver, FXMLView}

/**
  * Created by nmjr on 17/04/29.
  */
object Launcher extends JFXApp {
  val fileLocation: String = "window.fxml"
  val resource = getClass.getResource(fileLocation)

  if (resource == null) {
    throw new IOException(s"Cannot load resource: $fileLocation")
  }

  val root = FXMLView(resource, NoDependencyResolver)

  stage = new PrimaryStage() {
    title = "main"
    scene = new Scene(root)
  }
}

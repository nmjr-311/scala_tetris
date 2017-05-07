package data

/**
  * Created by nmjr on 17/04/29.
  */
sealed abstract class Key {
  override implicit def toString: String = this match {
    case Up => "Up"
    case Down => "Down"
    case Left => "Left"
    case Right => "Right"
    case Quit => "Quit"
    case _ => "Other"
  }
}
object Up extends Key
object Down extends Key
object Left extends Key
object Right extends Key
object Quit extends Key
object Other extends Key

object Key {
  def toKey(inputKey: Char) = inputKey match {
    case 'w' => Up
    case 'a' => Left
    case 'd' => Right
    case 's' => Down
  }
}
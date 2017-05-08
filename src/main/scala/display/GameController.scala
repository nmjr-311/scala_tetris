package display


import data._
import data.blocks.{Block, BlockNone}

import scala.util.Random
import scalafx.animation.{Animation, KeyFrame, Timeline}
import scalafx.beans.property.StringProperty
import scalafx.event.ActionEvent
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.{Label, TextArea, TitledPane}
import scalafx.scene.input.{KeyCode, KeyEvent, MouseEvent}
import scalafx.scene.paint.{Color, Paint}
import scalafx.util.Duration
import scalafxml.core.macros.sfxml

/**
  * Created by nmjr on 17/04/29.
  */
@sfxml
class GameController(private val canvas: Canvas,
                     private val debugDisplay: Label) {
  private val windowSize = 600
  private val gc = canvas.graphicsContext2D
  private val boardColSize = 15
  private val boardRowSize = 10
  private val blockSize : Double = 30.0
  private val boardRealColSize = boardColSize*blockSize
  private val boardRealRowSize = boardRowSize*blockSize
  canvas.setHeight(boardRealColSize)
  canvas.setWidth(boardRealRowSize)
  canvas.setLayoutX((windowSize - boardRealRowSize) / 2)
  canvas.setLayoutY((windowSize - boardRealColSize - 50) / 2)
  private val board = new Board(boardColSize, boardRowSize)
  private val boardController : BoardController =
    new BoardController(blockSize, boardColSize, boardRowSize, board)
  private var timeLine: Timeline = null
  private val keyFrame = KeyFrame(Duration.apply(1500), onFinished =
    _ => doOneIteration())
  private val blocks: Array[Block] = Block.getBlocks
  private var score: Int = 0
  private var isPlaying: Boolean = false

  def keyPressed(event: KeyEvent): Unit = {
    if (isPlaying) {
      val k: Key = event.code match {
        case KeyCode.Up | KeyCode.W => Up
        case KeyCode.Down | KeyCode.S => Down
        case KeyCode.Right | KeyCode.A => Right
        case KeyCode.Left | KeyCode.D => Left
        case KeyCode.Q => Quit
        case _ => Other
      }
      boardController.changeBlockPos(k)
      draw()
    }
  }

  def init: Unit = {
    board.clear()
    makeBlock
    timeLine = Timeline {Seq(keyFrame)}
    timeLine.cycleCount() = Animation.Indefinite
    score = 0
    isPlaying = true
    draw()
  }

  def start(e: MouseEvent) = {
    init
    draw()
    timeLine.play()
  }

  def drawBoard: Unit = {
    gc.setFill(Color.WhiteSmoke)
    gc.fillRect(0.0, 0.0, boardRealRowSize, boardRealColSize)
    gc.setFill(Color.DarkGray)
    (0 until boardColSize).toList.scanLeft(0.0)((b, _) =>
      b + blockSize).foreach(i =>
      gc.strokeLine(0.0, i, boardRealRowSize, i))
    (0 until boardRowSize).toList.scanLeft(0.0)((b, _) =>
      b + blockSize).foreach(i =>
      gc.strokeLine(i, 0.0, i, boardRealColSize))
  }

  def draw(): Unit = {
    drawBoard
    boardController.drawBoard(gc)
    boardController.drawCurrentBlock(gc)
    debugDisplay.text = score.toString
  }

  def doOneIteration():Unit = {
    val ret = boardController.oneIteration()
    score += ret._2 * 100
    if(ret._1)
      makeBlock()
    if(boardController.checkGameOver) {
      timeLine.stop()
      boardController.setCurrentBlock(new BlockNone)
      isPlaying = false
      println("finish!!")
      printf("your score is %d", score)
    }
    draw()
  }

  private def makeBlock(): Unit =
    boardController.setCurrentBlock(blocks(Random.nextInt(blocks.length)))

}

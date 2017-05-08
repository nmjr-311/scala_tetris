package display

import data.blocks.{Block, BlockNone}
import data._

import scala.util.Random
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.{Color, Paint}

/**
  * Created by nmjr on 17/05/06.
  */
class BoardController(private val blockSize: Double,
                     private val boardColSize: Int,
                     private val boardRowSize: Int,
                     private val board: Board,
                     private val boardColor: Paint = Color.Gray,
                     private val boardDefaultColor: Paint = Color.White) {
  private var currentBlock: currentBlock =
    new currentBlock(new BlockNone(),Color.Gray, (0, 0))

  def oneIteration(): (Boolean, Int) = {
    val fail = changeBlockPos(Down)
    val f = canMove()
    if (f || fail) {
      board.addBlockInBoard(currentBlock.calcBlockPos())
    }
    val lines = board.clearLines()
    (f || fail, lines)
  }

  def checkGameOver: Boolean = board.checkGameOver

  private def canMove(): Boolean =
    !board.checkBlockInBoard(currentBlock.calcBlockPos()) ||
      board.checkBlockTouchBoard(currentBlock.calcBlockPos())


  def setCurrentBlock(block: Block): Unit = {
    val r = Random.nextInt(255)
    val g = Random.nextInt(255)
    val b = Random.nextInt(255)
    currentBlock = new currentBlock(block, Color.rgb(r, g, b),
      (0, boardRowSize / 2 - block.size / 2))
  }

  private def draw(gc: GraphicsContext)(fillRange: List[(Int, Int)]) =
    fillRange.foreach(tuple =>
      gc.fillRect(tuple._2*blockSize, tuple._1*blockSize,
        blockSize-0.5, blockSize-0.5))

  def drawBoard(gc: GraphicsContext): Unit = {
    gc.setFill(boardColor)
    draw(gc)(board.calcBoardState())
  }

  def drawCurrentBlock(gc: GraphicsContext): Unit = {
    gc.setFill(currentBlock.getColor)
    draw(gc)(currentBlock.calcBlockPos())
  }

  def changeBlockPos(key: Key): Boolean = {
    val diff = key match {
      case Up => {
        changeAngle
        (0, 0)
      }
      case Down => (1, 0)
      case Left => (0, -1)
      case Right => (0, 1)
      case _ => (0, 0)
    }
    val cPos = currentBlock.getPos
    val nPos = addPos(cPos)(diff)
    currentBlock.updatePos(nPos)
    cPos == nPos
  }

  def changeAngle: Unit = {
    val bs = currentBlock.getbs
    val ret = currentBlock.changeAngle()
    currentBlock.setbs(ret)
    if (canMove())
      currentBlock.setbs(bs)
  }

  private def addPos(pos: (Int, Int))(diff: (Int, Int)): (Int, Int) = {
    val newPos = (pos._1 + diff._1, pos._2 + diff._2)
    val pos1 = currentBlock.calcBlockPos(newPos)
    if (board.checkBlockInBoard(pos1) &&
      !board.checkBlockTouchBoard(pos1))
      newPos
    else
      pos
  }

}

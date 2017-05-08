package data

import data.blocks._

import scala.annotation.tailrec
import scalafx.scene.paint.{Color, Paint}

/**
  * Created by nmjr on 17/04/29.
  */
class Board(private val boardColSize: Int,
           private val boardRowSize: Int) {
  val bs: Array[Array[Boolean]] = new Array[Array[Boolean]](boardColSize)
  (0 until bs.length).toList.foreach(i =>
    bs(i) = new Array[Boolean](boardRowSize))

  def calcBoardState(): List[(Int, Int)] =
    Board.blockToPosList((boardColSize-1, boardRowSize-1))(bs)

  def addBlockInBoard(ps : List[(Int, Int)]): Unit =
    ps.foreach(pos => {
     bs(pos._1)(pos._2) = true
    })

  def checkGameOver: Boolean = bs(0).exists(a => a)

  def printBoard: Unit = Board.printArray(bs)

  def checkBlockTouchBoard(ps: List[(Int, Int)]): Boolean =
    ps.exists(pos => bs(pos._1)(pos._2))

  def clearLines(): Int = {
    val ret = bs.foldRight((boardColSize-1, Nil): (Int, List[Int]))((as, b) =>
      if (as.forall(identity)) (b._1-1, b._1 :: b._2) else (b._1-1, b._2))._2
    ret.foreach(i => {
      clearOneRow(i)
    })
    if (!ret.isEmpty) {
      go(boardColSize-1)
    }
    @tailrec
    def go(i: Int): Unit = i match {
      case _ if i < 0 => ()
      case _ => {
        val l = ret.filter(_ > i).length
        if (l != 0) {
          for (j <- 0 until boardRowSize) {
            bs(i + l)(j) = bs(i)(j)
            bs(i)(j) = false
          }
        }
        go(i - 1)
      }
    }

    //printBoard
    ret.length
  }

  def clear(): Unit = {
    (0 until boardColSize).foreach(i => clearOneRow(i))
  }

  private def clearOneRow(i: Int): Unit =
    (0 until boardRowSize).foreach(j => bs(i)(j) = false)

  private def clearOneColumn(j: Int): Unit =
    (0 until boardColSize).foreach(i => bs(i)(j) = false)

  def checkBlockInBoard(ps: List[(Int, Int)]) =
    ps.forall(t => t._1 >= 0 && t._1 < boardColSize && t._2 >= 0 && t._2 < boardRowSize)
}

class currentBlock(private val block: Block,
                   private val color: Paint,
                   private var pos: (Int, Int)) {
  def getPos: (Int, Int) = pos
  private var currentBs = block.bs
  def getbs = currentBs
  def setbs(as: Array[Array[Boolean]]): Unit = {
    currentBs = as
  }

  def getColor: Paint = color

  def updatePos(pos: (Int, Int)): Unit = this.pos = pos
  //def calcBlockPos(): List[(Int, Int)] = Board.blockToPosList(pos)(block.bs)(identity)
  def calcBlockPos(p: (Int, Int) = pos) =
    Board.blockToPosList((p._1 + block.size - 1, p._2 + block.size - 1))(currentBs)

  def changeAngle(): Array[Array[Boolean]] = {
    val ret : Array[Array[Boolean]] = new Array[Array[Boolean]](block.size)
    for (i <- 0 until block.size) {
      ret(i) = new Array[Boolean](block.size)
      for (j <- 0 until block.size) {
        ret(i)(j) = currentBs(j)((block.size - 1)-i)
      }
    }
    ret
    //currentBs = ret
    //Board.printArray(ret)
  }

}

private object Board{

  def blockToPosList(initPos: (Int, Int))(as: Array[Array[Boolean]]): List[(Int, Int)]
  = as.foldRight((Nil: List[List[(Int, Int)]], initPos._1))((ll, b1) => {
    val _ll: List[(Int, Int)] =
      ll.foldRight((Nil: List[(Int, Int)], initPos._2))((l, b2) => {
        if (l) ((b1._2, b2._2) :: b2._1, b2._2-1)
        else (b2._1, b2._2-1)
      })._1
    ((_ll :: b1._1), b1._2-1)
  })._1.flatten

  def printArray(ass: Array[Array[Boolean]]): Unit = {
    printf("  ")
    for (j <- 0 until ass(0).length)
      printf("%2d", j)
    println()
    for (i <- 0 until ass.length) {
      printf("%2d:\t", i)
      for (j <- 0 until ass(0).length)
        printf("%c ", if (ass(i)(j)) 'x' else '.')
      println()
    }
  }
}

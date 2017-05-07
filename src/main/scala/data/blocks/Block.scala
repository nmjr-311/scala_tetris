package data.blocks

/**
  * Created by nmjr on 17/04/29.
  */
sealed abstract class Block {
  val size: Int = 4
  val bs: Array[Array[Boolean]]
}

class BlockO extends Block {
  val bs =
    Array(
      Array(false, false, false, false),
      Array(false, true, true, false)
      , Array(false, true, true, false)
      , Array(false, false, false, false))
}

class BlockL extends Block {
  val bs =
    Array(Array(false, true, false,false)
      ,    Array(false, true, false, false)
      ,    Array(false, true, true, false)
    , Array(false, false, false, false))
}

class BlockI extends Block {
  val bs = Array(Array(false,true,false,false)
                , Array(false,true,false,false)
    , Array(false,true,false,false)
                , Array(false,true,false,false))
}

class BlockS extends Block {
  val bs = Array(
     Array(false,true,false,false)
    , Array(false,true,true,false)
    , Array(false,false,true,false)
    ,Array(false,false,false,false)
    )
}

class BlockJ extends Block {
  val bs = Array(
     Array(false,false,true,false)
    , Array(false,false,true,false)
    , Array(false,true,true,false),
    Array(false,false,false,false)
    )
}

class BlockZ extends Block {
  val bs = Array(
    Array(false,false,true,false)
    , Array(false,true,true,false)
    , Array(false,true,false,false)
    ,  Array(false,false,false,false))
}

class BlockT extends Block {
  val bs = Array(
    Array(false,false,true,false)
    , Array(false,true,true,false)
    , Array(false,false,true,false)
  ,  Array(false,false,false,false))
}


class BlockNone extends Block {
  val bs = Array(Array(false))
}

object Block {
  def getBlocks: Array[Block] =
  Array[Block](new BlockI, new BlockO, new BlockL,
    new BlockS, new BlockL, new BlockZ, new BlockT)
}


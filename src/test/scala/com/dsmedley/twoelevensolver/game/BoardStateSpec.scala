package com.dsmedley.twoelevensolver.game

import org.scalatest._

class BoardStateSpec extends FlatSpec with Matchers {

  val board1 = new BoardState(List(
    List(0,0,0,0),
    List(0,0,0,0),
    List(0,1,0,0),
    List(0,0,0,0)
  ))
  val board2 = new BoardState(List(
    List(0,0,0,1),
    List(0,3,0,0),
    List(0,3,4,1),
    List(2,1,3,2)
  ))

  val board3 = new BoardState(List(
    List(0,0,2,0),
    List(1,1,2,0),
    List(1,3,0,0),
    List(3,3,3,0)
  ))

  "A board state" should "Report correct vacancy" in {
    board1.vacancy should be (15)
    board2.vacancy should be (7)
    board3.vacancy should be (7)
  }

  it should "Report correct compressibility" in {
    board1.compressibility should be (0)
    board2.compressibility should be (0)
    board3.compressibility should be (2)
  }

  it should "Report correct perpendicular compressibility" in {
    board1.perpendicularCompressibility should be (0)
    board2.perpendicularCompressibility should be (2)
    board3.perpendicularCompressibility should be (3)
  }

  it should "Rotate its content by 90 degrees clockwise" in {

    board1.rotate.raw should be (List(
      0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0
    ))

    board2.rotate.raw should be (List(
      2,0,0,0,1,3,3,0,3,4,0,0,2,1,0,1
    ))

    board3.rotate.raw should be (List(
      3,1,1,0,3,3,1,0,3,0,2,2,0,0,0,0
    ))
  }

  it should "Perform a collapse operation correctly" in {
    board1.collapse.raw should be (List(
      0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0
    ))

    board2.collapse.raw should be (List(
      1,0,0,0,3,0,0,0,3,4,1,0,2,1,3,2
    ))

    board3.collapse.raw should be (List(
      2,0,0,0,2,2,0,0,1,3,0,0,4,3,0,0
    ))
  }

  it should "Be constructable from a list of cell values" in {
    BoardState(board2.raw).raw should be (board2.raw)
  }

  it should "Not be constructed from a non-square list" in {
    an [Exception] should be thrownBy {
      BoardState(List(1,2,3))
    }
  }
}

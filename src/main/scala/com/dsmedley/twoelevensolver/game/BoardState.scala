package com.dsmedley.twoelevensolver.game

import scala.annotation.tailrec

/**
 * A representation of a particular state of the 2048 game
 *
 * Stores various metrics about the current state, along with the ability to advance
 * the state.
 *
 * Tiles are stored as integer powers of 2, with 0 being the special case of an empty cell.
 */
class BoardState(content: List[List[Int]]) {

  /**
   * The number of tiles along on side of the (square) board.
   */
  val degree: Int = content.length

  // Metrics about the board state

  /**
   * How many vacant spaces the board state has.
   */
  val vacancy: Int = content.map(_.count(_ == 0)).sum

  /**
   * Determine how many cell merges would occur if a row were compressed.
   *
   * @return The number of cell merges that would occur
   */
  private def rowCompressibility(row: List[Int]): Int = {
    // the accumulator here is total merges, last cell value and whether the last cell inspected
    // could merge. This way we count 2, 2, 2 as one merge and not two.
    row.filter(_ != 0).foldLeft[(Int, Int, Boolean)]((0, 0, false))(
      (acc, elem) => {
        if (elem == acc._2 && !acc._3) {
          (acc._1 + 1, elem, true)
        } else {
          (acc._1, elem, false)
        }
      }
    )._1
  }

  /**
   * If the game board was collapsed in this orientation, how many cells would be merged?
   */
  val compressibility = content.map(rowCompressibility).sum

  /**
   * If the game board was collapsed at a perpendicular orientation, how many cells would be merged?
   */
  val perpendicularCompressibility = content.transpose.map {
    column: List[Int] => rowCompressibility(column.reverse)
  }.sum

  /**
   * Perform the equivalent of a ninety-degree clockwise rotation on the board
   *
   * @return A new rotated board state
   */
  def rotate: BoardState = {
    new BoardState(content.transpose.map(_.reverse))
  }

  // Advance the board to the next state

  /**
   * Move through a row, merging together any applicable tiles
   *
   * Recursively grabs the first two elements and compares them, merging them in necessary
   * Assumes the row has been stripped of zero values
   *
   * @param row The row, stripped of zeros
   * @param result The accumulated result
   *
   * @return The resulting row
   */
  @tailrec
  private def collapseRow(row: List[Int], result: List[Int] = List()): List[Int] = {
    row match {
      case Nil => result  // Got an empty row
      case head::tail => {  // Acquire the first element
        tail match {
          case Nil => result :+ head  // This is the last element so append it
          case second::end =>  {
            if (head == second) {
              collapseRow(end, result :+ (head + 1))  // Combine and continue with the rest
            } else {
              collapseRow(tail, result :+ head)
            }
          }
        }
      }
    }
  }

  /**
   * Pad a row back to the correct size
   *
   * @param row The row
   *
   * @return The row with zeros padded on the end.
   */
  private def padRow(row: List[Int]): List[Int] = {
    degree - row.length match {
      case 0 => row
      case neg: Int if neg < 0 => throw new Exception("Row is larger than board!")
      case n: Int => row ++ List.fill(n)(0)
    }
  }

  /**
   * Perform a collapse (right-to-left) of cells on the board
   *
   * @return A new BoardState with the cells collapse
   */
  def collapse: BoardState = new BoardState(
    content map {
      row: List[Int] => padRow(collapseRow(row.filter(_ > 0)))
    }
  )

  /**
   * Stringify the contents to fit the board shape
   */
  override def toString: String = {
    content.map(_.mkString(", ")).mkString("\n")
  }

  val raw: List[Int] = content.flatten

  val maxValue: Int = raw.max
}

object BoardState {
  def apply(items: List[Int]): BoardState = {
    val degree = math.sqrt(items.length)
    if (degree % 1 != 0) {
      throw new Exception("Provided input sequence is not square!")
    }

    new BoardState(items.grouped(degree.floor.toInt).toList)
  }
}

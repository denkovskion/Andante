/*
 * MIT License
 *
 * Copyright (c) 2024 Ivan Denkovski
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package blog.art.chess.andante.position;

import blog.art.chess.andante.move.AndernachMove;
import blog.art.chess.andante.move.Capture;
import blog.art.chess.andante.move.CirceMove;
import blog.art.chess.andante.move.EnPassant;
import blog.art.chess.andante.move.Move;
import blog.art.chess.andante.move.PromotionCapture;
import blog.art.chess.andante.move.QuietMove;
import blog.art.chess.andante.move.VariantMove;
import blog.art.chess.andante.piece.Colour;
import blog.art.chess.andante.piece.Piece;
import blog.art.chess.andante.piece.orthodox.King;

public enum Condition {
  ANDERNACH {
    @Override
    public Move decorateMove(Board board, Move move) {
      Move baseMove;
      if (move instanceof VariantMove) {
        baseMove = ((VariantMove) move).findBaseMove();
      } else {
        baseMove = move;
      }
      if (baseMove instanceof Capture || baseMove instanceof PromotionCapture) {
        Piece piece = board.get(((QuietMove) baseMove).getOrigin());
        if (!(piece instanceof King)) {
          Square capture = ((QuietMove) baseMove).getTarget();
          Square change = ((QuietMove) baseMove).getTarget();
          Colour origin = piece.getColour();
          Colour target = board.get(capture).getColour();
          return new AndernachMove(move, change, origin, target);
        }
      } else if (baseMove instanceof EnPassant) {
        Piece piece = board.get(((EnPassant) baseMove).getOrigin());
        Square capture = ((EnPassant) baseMove).getStop();
        Square change = ((EnPassant) baseMove).getTarget();
        Colour origin = piece.getColour();
        Colour target = board.get(capture).getColour();
        return new AndernachMove(move, change, origin, target);
      }
      return move;
    }
  }, CIRCE {
    @Override
    public Move decorateMove(Board board, Move move) {
      Move baseMove;
      if (move instanceof VariantMove) {
        baseMove = ((VariantMove) move).findBaseMove();
      } else {
        baseMove = move;
      }
      if (baseMove instanceof Capture || baseMove instanceof PromotionCapture) {
        Square capture = ((QuietMove) baseMove).getTarget();
        Piece piece = board.get(capture);
        if (!(piece instanceof King)) {
          Square rebirth = board.findRebirthSquare(capture, piece.getClass(), piece.getColour());
          if (board.get(rebirth) == null) {
            return new CirceMove(move, capture, rebirth);
          }
        }
      } else if (baseMove instanceof EnPassant) {
        Square capture = ((EnPassant) baseMove).getStop();
        Piece piece = board.get(capture);
        Square rebirth = board.findRebirthSquare(capture, piece.getClass(), piece.getColour());
        if (board.get(rebirth) == null) {
          return new CirceMove(move, capture, rebirth);
        }
      }
      return move;
    }
  };

  public abstract Move decorateMove(Board board, Move move);
}

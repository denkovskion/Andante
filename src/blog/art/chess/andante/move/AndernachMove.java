/*
 * MIT License
 *
 * Copyright (c) 2025 Ivan Denkovski
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

package blog.art.chess.andante.move;

import blog.art.chess.andante.position.Position;
import java.util.Locale;
import java.util.StringJoiner;

public class AndernachMove extends FairyMove {

  protected final boolean castling;

  protected AndernachMove(NullMove baseMove, boolean castling) {
    super(baseMove);
    this.castling = castling;
  }

  public AndernachMove(Capture baseMove, boolean castling) {
    super(baseMove);
    this.castling = castling;
  }

  public AndernachMove(EnPassant baseMove, boolean castling) {
    super(baseMove);
    this.castling = castling;
  }

  public AndernachMove(PromotionCapture baseMove, boolean castling) {
    super(baseMove);
    this.castling = castling;
  }

  public AndernachMove(CirceMove baseMove, boolean castling) {
    super(baseMove);
    this.castling = castling;
  }

  public AndernachMove(AntiCirceMove baseMove, boolean castling) {
    super(baseMove);
    this.castling = castling;
  }

  @Override
  protected void preWrite(Position position, StringBuilder lanBuilder, Locale locale) {
    if (baseMove instanceof CirceMove circeMove) {
      circeMove.baseMove.preWrite(position, lanBuilder, locale);
      lanBuilder.append("(").append(
          position.getBoard().get(((QuietMove) circeMove.baseMove).origin).getColour().getOpposite()
              .getCode(locale)).append(";");
      if (circeMove.baseMove instanceof EnPassant enPassant) {
        lanBuilder.append(position.getBoard().get(enPassant.stop).getCode(locale));
      } else {
        lanBuilder.append(
            position.getBoard().get(((QuietMove) circeMove.baseMove).target).getCode(locale));
      }
      lanBuilder.append(position.getBoard().toCode(circeMove.rebirth)).append(")");
    } else if (baseMove instanceof AntiCirceMove antiCirceMove) {
      antiCirceMove.baseMove.preWrite(position, lanBuilder, locale);
      lanBuilder.append("(");
      if (antiCirceMove.baseMove instanceof PromotionCapture promotionCapture) {
        lanBuilder.append(position.getBox().peek(promotionCapture.section).getCode(locale))
            .append(position.getBoard().toCode(antiCirceMove.rebirth)).append(
                position.getBox().peek(promotionCapture.section).getColour().getOpposite()
                    .getCode(locale));
      } else {
        lanBuilder.append(
                position.getBoard().get(((QuietMove) antiCirceMove.baseMove).origin).getCode(locale))
            .append(position.getBoard().toCode(antiCirceMove.rebirth)).append(
                position.getBoard().get(((QuietMove) antiCirceMove.baseMove).origin).getColour()
                    .getOpposite().getCode(locale));
      }
      lanBuilder.append(")");
    } else {
      baseMove.preWrite(position, lanBuilder, locale);
      lanBuilder.append("(").append(
          position.getBoard().get(((QuietMove) baseMove).origin).getColour().getOpposite()
              .getCode(locale)).append(")");
    }
  }

  @Override
  protected void updatePieces(Position position) {
    baseMove.updatePieces(position);
    if (baseMove instanceof CirceMove circeMove) {
      position.getBoard().get(((QuietMove) circeMove.baseMove).target).toggleColour();
    } else if (baseMove instanceof AntiCirceMove antiCirceMove) {
      position.getBoard().get(antiCirceMove.rebirth).toggleColour();
    } else {
      position.getBoard().get(((QuietMove) baseMove).target).toggleColour();
    }
  }

  @Override
  protected void revertPieces(Position position) {
    if (baseMove instanceof CirceMove circeMove) {
      position.getBoard().get(((QuietMove) circeMove.baseMove).target).toggleColour();
    } else if (baseMove instanceof AntiCirceMove antiCirceMove) {
      position.getBoard().get(antiCirceMove.rebirth).toggleColour();
    } else {
      position.getBoard().get(((QuietMove) baseMove).target).toggleColour();
    }
    baseMove.revertPieces(position);
  }

  @Override
  protected void updateCastlings(Position position) {
    baseMove.updateCastlings(position);
    if (baseMove instanceof CirceMove circeMove) {
      if (castling) {
        position.getState().addCastling(((QuietMove) circeMove.baseMove).target);
      }
    } else if (baseMove instanceof AntiCirceMove antiCirceMove) {
      if (castling) {
        position.getState().addCastling(antiCirceMove.rebirth);
      } else {
        position.getState().removeCastling(antiCirceMove.rebirth);
      }
    } else {
      if (castling) {
        position.getState().addCastling(((QuietMove) baseMove).target);
      }
    }
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", AndernachMove.class.getSimpleName() + "[", "]").add(
        "baseMove=" + baseMove).add("castling=" + castling).toString();
  }
}

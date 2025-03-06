/*
 * MIT License
 *
 * Copyright (c) 2024-2025 Ivan Denkovski
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

package blog.art.chess.andante.parser;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

class Popeye {

  enum File {_a, _b, _c, _d, _e, _f, _g, _h}

  static final EnumMap<File, String> fileCodes = new EnumMap<>(
      Map.of(File._a, "a", File._b, "b", File._c, "c", File._d, "d", File._e, "e", File._f, "f",
          File._g, "g", File._h, "h"));

  enum Rank {_1, _2, _3, _4, _5, _6, _7, _8}

  static final EnumMap<Rank, String> rankCodes = new EnumMap<>(
      Map.of(Rank._1, "1", Rank._2, "2", Rank._3, "3", Rank._4, "4", Rank._5, "5", Rank._6, "6",
          Rank._7, "7", Rank._8, "8"));

  record Square(File file, Rank rank) {

  }

  enum PieceType {King, Queen, Rook, Bishop, Knight, Pawn, Grasshopper, Nightrider, Amazon}

  enum Colour {White, Black}

  record Piece(Square square, PieceType pieceType, Colour colour) {

  }

  enum StipulationType {Direct, Help, Self}

  static final EnumMap<StipulationType, String> stipulationTypeCodes = new EnumMap<>(
      Map.of(StipulationType.Direct, "", StipulationType.Help, "h", StipulationType.Self, "s"));

  enum Goal {Mate, Stalemate}

  static final EnumMap<Goal, String> goalCodes = new EnumMap<>(
      Map.of(Goal.Mate, "#", Goal.Stalemate, "="));

  record Stipulation(StipulationType stipulationType, Goal goal, int nMoves) {

  }

  enum Option {Try, Defence, SetPlay, NullMoves, WhiteToPlay, Variation, MoveNumbers, NoThreat, EnPassant, NoBoard, NoShortVariations, HalfDuplex, NoCastling}

  static class Options {

    private boolean tri;
    private int defence;
    private boolean setPlay;
    private boolean nullMoves;
    private boolean whiteToPlay;
    private boolean variation;
    private boolean moveNumbers;
    private boolean noThreat;
    private final List<Square> enPassant = new ArrayList<>();
    private boolean noBoard;
    private boolean noShortVariations;
    private boolean halfDuplex;
    private final List<Square> noCastling = new ArrayList<>();

    boolean isTry() {
      return tri;
    }

    void setTry() {
      this.tri = true;
    }

    int getDefence() {
      return defence;
    }

    void setDefence(int defence) {
      this.defence = defence;
    }

    boolean isNullMoves() {
      return nullMoves;
    }

    void setNullMoves() {
      this.nullMoves = true;
    }

    boolean isSetPlay() {
      return setPlay;
    }

    void setSetPlay() {
      this.setPlay = true;
    }

    boolean isWhiteToPlay() {
      return whiteToPlay;
    }

    void setWhiteToPlay() {
      this.whiteToPlay = true;
    }

    boolean isVariation() {
      return variation;
    }

    void setVariation() {
      this.variation = true;
    }

    boolean isMoveNumbers() {
      return moveNumbers;
    }

    void setMoveNumbers() {
      this.moveNumbers = true;
    }

    boolean isNoThreat() {
      return noThreat;
    }

    void setNoThreat() {
      this.noThreat = true;
    }

    List<Square> getEnPassant() {
      return enPassant;
    }

    boolean isNoBoard() {
      return noBoard;
    }

    void setNoBoard() {
      this.noBoard = true;
    }

    boolean isNoShortVariations() {
      return noShortVariations;
    }

    void setNoShortVariations() {
      this.noShortVariations = true;
    }

    boolean isHalfDuplex() {
      return halfDuplex;
    }

    void setHalfDuplex() {
      this.halfDuplex = true;
    }

    List<Square> getNoCastling() {
      return noCastling;
    }
  }

  enum Condition {Circe, NoCapture}

  static class Conditions {

    private boolean circe;
    private boolean noCapture;

    boolean isCirce() {
      return circe;
    }

    void setCirce() {
      this.circe = true;
    }

    boolean isNoCapture() {
      return noCapture;
    }

    void setNoCapture() {
      this.noCapture = true;
    }
  }

  enum Command {Remark, Condition, Option, Stipulation, Pieces}

  static class Problem {

    private final Conditions conditions = new Conditions();
    private final Options options = new Options();
    private Stipulation stipulation;
    private final List<Piece> pieces = new ArrayList<>();

    Conditions getConditions() {
      return conditions;
    }

    Options getOptions() {
      return options;
    }

    Stipulation getStipulation() {
      return stipulation;
    }

    void setStipulation(Stipulation stipulation) {
      this.stipulation = stipulation;
    }

    List<Piece> getPieces() {
      return pieces;
    }
  }

  enum Directive {BeginProblem, EndProblem, NextProblem}
}

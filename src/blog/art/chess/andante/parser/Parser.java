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

import blog.art.chess.andante.condition.CirceMoveFactory;
import blog.art.chess.andante.condition.MoveFactory;
import blog.art.chess.andante.piece.Colour;
import blog.art.chess.andante.piece.Piece;
import blog.art.chess.andante.piece.fairy.Amazon;
import blog.art.chess.andante.piece.fairy.Grasshopper;
import blog.art.chess.andante.piece.fairy.Nightrider;
import blog.art.chess.andante.piece.orthodox.Bishop;
import blog.art.chess.andante.piece.orthodox.King;
import blog.art.chess.andante.piece.orthodox.Knight;
import blog.art.chess.andante.piece.orthodox.Pawn;
import blog.art.chess.andante.piece.orthodox.Queen;
import blog.art.chess.andante.piece.orthodox.Rook;
import blog.art.chess.andante.position.Board;
import blog.art.chess.andante.position.Box;
import blog.art.chess.andante.position.DefaultBoard;
import blog.art.chess.andante.position.DefaultBox;
import blog.art.chess.andante.position.DefaultMemory;
import blog.art.chess.andante.position.DefaultState;
import blog.art.chess.andante.position.DefaultTable;
import blog.art.chess.andante.position.MailboxBoard;
import blog.art.chess.andante.position.Memory;
import blog.art.chess.andante.position.Position;
import blog.art.chess.andante.position.State;
import blog.art.chess.andante.position.Table;
import blog.art.chess.andante.problem.Aim;
import blog.art.chess.andante.problem.AnalysisOptions;
import blog.art.chess.andante.problem.BattlePlayOptions;
import blog.art.chess.andante.problem.Directmate;
import blog.art.chess.andante.problem.DisplayOptions;
import blog.art.chess.andante.problem.HelpPlayOptions;
import blog.art.chess.andante.problem.Helpmate;
import blog.art.chess.andante.problem.LogOptions;
import blog.art.chess.andante.problem.MateSearch;
import blog.art.chess.andante.problem.Perft;
import blog.art.chess.andante.problem.Problem;
import blog.art.chess.andante.problem.Selfmate;
import blog.art.chess.andante.problem.Task;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Parser {

  private final String inputFile;
  private Locale inputLanguage;

  public Parser(String inputFile) {
    this.inputFile = inputFile;
  }

  public List<Task> readAllTasks() {
    String line = null;
    String token = null;
    try (Reader reader = inputFile != null ? new FileReader(inputFile)
        : new InputStreamReader(System.in); Scanner scanner = new Scanner(reader)) {
      Pattern beginProblemPattern = Pattern.compile(
          Stream.of(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN).map(
                  locale -> ResourceBundle.getBundle("blog.art.chess.andante.parser.PopeyeKeywords",
                      locale)).map(bundle -> bundle.getString(Popeye.Directive.BeginProblem.name()))
              .collect(Collectors.joining("|")), Pattern.CASE_INSENSITIVE);
      if (scanner.hasNext(beginProblemPattern)) {
        String beginProblemToken = token = scanner.next(beginProblemPattern);
        ResourceBundle keywords = Stream.of(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN).map(
            locale -> ResourceBundle.getBundle("blog.art.chess.andante.parser.PopeyeKeywords",
                locale)).filter(bundle -> bundle.getString(Popeye.Directive.BeginProblem.name())
            .equalsIgnoreCase(beginProblemToken)).findAny().orElseThrow();
        inputLanguage = keywords.getLocale();
        ResourceBundle pieceTypeCodes = ResourceBundle.getBundle(
            "blog.art.chess.andante.piece.PieceCodes", inputLanguage);
        List<Popeye.Problem> problems = new ArrayList<>();
        while (true) {
          Popeye.Problem problem = new Popeye.Problem();
          Pattern commandPattern = Pattern.compile(
              Arrays.stream(Popeye.Command.values()).map(Enum::name).map(keywords::getString)
                  .collect(Collectors.joining("|")), Pattern.CASE_INSENSITIVE);
          while (scanner.hasNext(commandPattern)) {
            String commandToken = token = scanner.next(commandPattern);
            Popeye.Command command = Arrays.stream(Popeye.Command.values())
                .filter(value -> keywords.getString(value.name()).equalsIgnoreCase(commandToken))
                .findAny().orElseThrow();
            switch (command) {
              case Remark -> scanner.nextLine();
              case Condition -> {
                Pattern conditionPattern = Pattern.compile(
                    Arrays.stream(Popeye.Condition.values()).map(Enum::name)
                        .map(keywords::getString).collect(Collectors.joining("|")),
                    Pattern.CASE_INSENSITIVE);
                do {
                  String conditionToken = token = scanner.next(conditionPattern);
                  Popeye.Condition condition = Arrays.stream(Popeye.Condition.values()).filter(
                          value -> keywords.getString(value.name()).equalsIgnoreCase(conditionToken))
                      .findAny().orElseThrow();
                  if (condition == Popeye.Condition.Circe) {
                    problem.getConditions().setCirce();
                  }
                } while (scanner.hasNext(conditionPattern));
              }
              case Option -> {
                Pattern optionPattern = Pattern.compile(
                    Arrays.stream(Popeye.Option.values()).map(Enum::name).map(keywords::getString)
                        .collect(Collectors.joining("|")), Pattern.CASE_INSENSITIVE);
                do {
                  String optionToken = token = scanner.next(optionPattern);
                  Popeye.Option option = Arrays.stream(Popeye.Option.values()).filter(
                          value -> keywords.getString(value.name()).equalsIgnoreCase(optionToken))
                      .findAny().orElseThrow();
                  switch (option) {
                    case Try -> problem.getOptions().setTry();
                    case Defence -> {
                      Pattern defencePattern = Pattern.compile("[1-9]\\d*");
                      String defenceToken = token = scanner.next(defencePattern);
                      int defence = Integer.parseInt(defenceToken);
                      problem.getOptions().setDefence(defence);
                    }
                    case SetPlay -> problem.getOptions().setSetPlay();
                    case NullMoves -> problem.getOptions().setNullMoves();
                    case WhiteToPlay -> problem.getOptions().setWhiteToPlay();
                    case Variation -> problem.getOptions().setVariation();
                    case MoveNumbers -> problem.getOptions().setMoveNumbers();
                    case NoThreat -> problem.getOptions().setNoThreat();
                    case EnPassant -> {
                      Pattern enPassantPattern = Pattern.compile(String.format("((%s)(%s)){1,2}",
                          Arrays.stream(Popeye.File.values()).map(Popeye.fileCodes::get)
                              .collect(Collectors.joining("|")),
                          Arrays.stream(Popeye.Rank.values()).map(Popeye.rankCodes::get)
                              .collect(Collectors.joining("|"))), Pattern.CASE_INSENSITIVE);
                      String enPassantToken = token = scanner.next(enPassantPattern);
                      Pattern squarePattern = Pattern.compile(
                          String.format("(?<file>%s)(?<rank>%s)",
                              Arrays.stream(Popeye.File.values()).map(Popeye.fileCodes::get)
                                  .collect(Collectors.joining("|")),
                              Arrays.stream(Popeye.Rank.values()).map(Popeye.rankCodes::get)
                                  .collect(Collectors.joining("|"))), Pattern.CASE_INSENSITIVE);
                      Matcher squareMatcher = squarePattern.matcher(enPassantToken);
                      while (squareMatcher.find()) {
                        Popeye.File file = Arrays.stream(Popeye.File.values()).filter(
                                value -> Popeye.fileCodes.get(value)
                                    .equalsIgnoreCase(squareMatcher.group("file"))).findAny()
                            .orElseThrow();
                        Popeye.Rank rank = Arrays.stream(Popeye.Rank.values()).filter(
                                value -> Popeye.rankCodes.get(value)
                                    .equalsIgnoreCase(squareMatcher.group("rank"))).findAny()
                            .orElseThrow();
                        problem.getOptions().getEnPassant().add(new Popeye.Square(file, rank));
                      }
                    }
                    case NoBoard -> problem.getOptions().setNoBoard();
                    case NoShortVariations -> problem.getOptions().setNoShortVariations();
                    case HalfDuplex -> problem.getOptions().setHalfDuplex();
                    case NoCastling -> {
                      Pattern noCastlingPattern = Pattern.compile(String.format("((%s)(%s))+",
                          Arrays.stream(Popeye.File.values()).map(Popeye.fileCodes::get)
                              .collect(Collectors.joining("|")),
                          Arrays.stream(Popeye.Rank.values()).map(Popeye.rankCodes::get)
                              .collect(Collectors.joining("|"))), Pattern.CASE_INSENSITIVE);
                      String noCastlingToken = token = scanner.next(noCastlingPattern);
                      Pattern squarePattern = Pattern.compile(
                          String.format("(?<file>%s)(?<rank>%s)",
                              Arrays.stream(Popeye.File.values()).map(Popeye.fileCodes::get)
                                  .collect(Collectors.joining("|")),
                              Arrays.stream(Popeye.Rank.values()).map(Popeye.rankCodes::get)
                                  .collect(Collectors.joining("|"))), Pattern.CASE_INSENSITIVE);
                      Matcher squareMatcher = squarePattern.matcher(noCastlingToken);
                      while (squareMatcher.find()) {
                        Popeye.File file = Arrays.stream(Popeye.File.values()).filter(
                                value -> Popeye.fileCodes.get(value)
                                    .equalsIgnoreCase(squareMatcher.group("file"))).findAny()
                            .orElseThrow();
                        Popeye.Rank rank = Arrays.stream(Popeye.Rank.values()).filter(
                                value -> Popeye.rankCodes.get(value)
                                    .equalsIgnoreCase(squareMatcher.group("rank"))).findAny()
                            .orElseThrow();
                        problem.getOptions().getNoCastling().add(new Popeye.Square(file, rank));
                      }
                    }
                  }
                } while (scanner.hasNext(optionPattern));
              }
              case Stipulation -> {
                Pattern stipulationPattern = Pattern.compile(
                    String.format("(?<stipulationType>%s)(?<goal>%s)(?<nMoves>%s)",
                        Arrays.stream(Popeye.StipulationType.values())
                            .map(Popeye.stipulationTypeCodes::get).collect(Collectors.joining("|")),
                        Arrays.stream(Popeye.Goal.values()).map(Popeye.goalCodes::get)
                            .collect(Collectors.joining("|")), "[1-9]\\d*"),
                    Pattern.CASE_INSENSITIVE);
                String stipulationToken = token = scanner.next(stipulationPattern);
                Matcher stipulationMatcher = stipulationPattern.matcher(stipulationToken);
                if (stipulationMatcher.matches()) {
                  Popeye.StipulationType stipulationType = Arrays.stream(
                          Popeye.StipulationType.values()).filter(
                          value -> Popeye.stipulationTypeCodes.get(value)
                              .equalsIgnoreCase(stipulationMatcher.group("stipulationType"))).findAny()
                      .orElseThrow();
                  Popeye.Goal goal = Arrays.stream(Popeye.Goal.values()).filter(
                          value -> Popeye.goalCodes.get(value)
                              .equalsIgnoreCase(stipulationMatcher.group("goal"))).findAny()
                      .orElseThrow();
                  int nMoves = Integer.parseInt(stipulationMatcher.group("nMoves"));
                  problem.setStipulation(new Popeye.Stipulation(stipulationType, goal, nMoves));
                }
              }
              case Pieces -> {
                Pattern colourPattern = Pattern.compile(
                    Arrays.stream(Popeye.Colour.values()).map(Enum::name).map(keywords::getString)
                        .collect(Collectors.joining("|")), Pattern.CASE_INSENSITIVE);
                do {
                  String colourToken = token = scanner.next(colourPattern);
                  Popeye.Colour colour = Arrays.stream(Popeye.Colour.values()).filter(
                          value -> keywords.getString(value.name()).equalsIgnoreCase(colourToken))
                      .findAny().orElseThrow();
                  Pattern piecePattern = Pattern.compile(
                      String.format("(?<pieceType>%s)(?<squares>((%s)(%s))+)",
                          Arrays.stream(Popeye.PieceType.values())
                              .map(value -> pieceTypeCodes.getString(value.name()))
                              .collect(Collectors.joining("|")),
                          Arrays.stream(Popeye.File.values()).map(Popeye.fileCodes::get)
                              .collect(Collectors.joining("|")),
                          Arrays.stream(Popeye.Rank.values()).map(Popeye.rankCodes::get)
                              .collect(Collectors.joining("|"))), Pattern.CASE_INSENSITIVE);
                  do {
                    String pieceToken = token = scanner.next(piecePattern);
                    Matcher pieceMatcher = piecePattern.matcher(pieceToken);
                    if (pieceMatcher.matches()) {
                      Popeye.PieceType pieceType = Arrays.stream(Popeye.PieceType.values()).filter(
                              value -> pieceTypeCodes.getString(value.name())
                                  .equalsIgnoreCase(pieceMatcher.group("pieceType"))).findAny()
                          .orElseThrow();
                      Pattern squarePattern = Pattern.compile(
                          String.format("(?<file>%s)(?<rank>%s)",
                              Arrays.stream(Popeye.File.values()).map(Popeye.fileCodes::get)
                                  .collect(Collectors.joining("|")),
                              Arrays.stream(Popeye.Rank.values()).map(Popeye.rankCodes::get)
                                  .collect(Collectors.joining("|"))), Pattern.CASE_INSENSITIVE);
                      Matcher squareMatcher = squarePattern.matcher(pieceMatcher.group("squares"));
                      while (squareMatcher.find()) {
                        Popeye.File file = Arrays.stream(Popeye.File.values()).filter(
                                value -> Popeye.fileCodes.get(value)
                                    .equalsIgnoreCase(squareMatcher.group("file"))).findAny()
                            .orElseThrow();
                        Popeye.Rank rank = Arrays.stream(Popeye.Rank.values()).filter(
                                value -> Popeye.rankCodes.get(value)
                                    .equalsIgnoreCase(squareMatcher.group("rank"))).findAny()
                            .orElseThrow();
                        Popeye.Square square = new Popeye.Square(file, rank);
                        problem.getPieces().add(new Popeye.Piece(square, pieceType, colour));
                      }
                    }
                  } while (scanner.hasNext(piecePattern));
                } while (scanner.hasNext(colourPattern));
              }
            }
          }
          Pattern directivePattern = Pattern.compile(
              Stream.of(Popeye.Directive.EndProblem, Popeye.Directive.NextProblem).map(Enum::name)
                  .map(keywords::getString).collect(Collectors.joining("|")),
              Pattern.CASE_INSENSITIVE);
          String directiveToken = token = scanner.next(directivePattern);
          Popeye.Directive directive = Arrays.stream(Popeye.Directive.values())
              .filter(value -> keywords.getString(value.name()).equalsIgnoreCase(directiveToken))
              .findAny().orElseThrow();
          problems.add(problem);
          if (directive == Popeye.Directive.EndProblem) {
            break;
          }
        }
        return problems.stream().peek(this::validateProblem).peek(this::verifyProblem)
            .map(this::convertProblem).toList();
      } else {
        inputLanguage = Locale.ROOT;
        List<Model.Position> positions = new ArrayList<>();
        while (scanner.hasNextLine()) {
          line = scanner.nextLine();
          if (!line.isBlank()) {
            try (Scanner lineScanner = new Scanner(line.strip())) {
              Model.Position position = new Model.Position();
              lineScanner.useDelimiter("");
              int index = 0;
              while (index != 64) {
                if (index % 8 == 0 && index != 0) {
                  lineScanner.next("/");
                }
                Pattern digitPattern = Pattern.compile(
                    "[" + "12345678".substring(0, 8 - index % 8) + "]");
                if (lineScanner.hasNext(digitPattern)) {
                  String digitSymbol = lineScanner.next(digitPattern);
                  int digit = Integer.parseInt(digitSymbol);
                  index += digit;
                  if (index % 8 == 0) {
                    continue;
                  }
                }
                String pieceSymbols = "KQRBNPkqrbnp";
                Pattern piecePattern = Pattern.compile("[" + pieceSymbols + "]");
                String pieceSymbol = lineScanner.next(piecePattern);
                Model.Piece piece = Model.Piece.values()[pieceSymbols.indexOf(pieceSymbol)];
                position.getBoard().set(index, piece);
                index++;
              }
              lineScanner.next("\\s");
              lineScanner.reset();
              String colourSymbols = "wb";
              Pattern colourPattern = Pattern.compile("[" + colourSymbols + "]");
              String sideToMoveToken = lineScanner.next(colourPattern);
              Model.Colour sideToMove = Model.Colour.values()[colourSymbols.indexOf(
                  sideToMoveToken)];
              position.setSideToMove(sideToMove);
              String castlingSymbols = "KQkq";
              Pattern castlingPattern = Pattern.compile(
                  "\\b" + Arrays.stream(castlingSymbols.split(""))
                      .map(castlingSymbol -> castlingSymbol + "?").collect(Collectors.joining()));
              if (lineScanner.hasNext(castlingPattern)) {
                String castlingToken = lineScanner.next(castlingPattern);
                Arrays.stream(castlingToken.split("")).forEach(castlingSymbol -> {
                  Model.Castling castling = Model.Castling.values()[castlingSymbols.indexOf(
                      castlingSymbol)];
                  position.getCastlings().add(castling);
                });
              } else {
                lineScanner.next("-");
              }
              Pattern enPassantPattern = Pattern.compile("[a-h][36]");
              if (lineScanner.hasNext(enPassantPattern)) {
                String enPassantToken = lineScanner.next(enPassantPattern);
                int file = enPassantToken.charAt(0) - 'a' + 1;
                int rank = enPassantToken.charAt(1) - '1' + 1;
                position.setEnPassant(new Model.Square((8 - rank) * 8 + file - 1));
              } else {
                lineScanner.next("-");
              }
              List<String> opcodeCodes = List.of("acd", "dm");
              Pattern opcodePattern = Pattern.compile(String.join("|", opcodeCodes));
              String opcodeToken = lineScanner.next(opcodePattern);
              Model.Opcode opcode = Model.Opcode.values()[opcodeCodes.indexOf(opcodeToken)];
              Pattern operandPattern = Pattern.compile(switch (opcode) {
                case ACD -> "(?<operand>0|[1-9]\\d*);$";
                case DM -> "(?<operand>[1-9]\\d*);$";
              });
              String operandToken = lineScanner.next(operandPattern);
              Matcher operandMatcher = operandPattern.matcher(operandToken);
              if (operandMatcher.matches()) {
                int operand = Integer.parseInt(operandMatcher.group("operand"));
                position.setOperation(new Model.Operation(opcode, operand));
              }
              positions.add(position);
            }
          }
        }
        return positions.stream().peek(this::validatePosition).map(this::convertPosition).toList();
      }
    } catch (IllegalArgumentException | UnsupportedOperationException e) {
      System.err.println(Problem.logPrefix() + " " + e.getMessage());
    } catch (NoSuchElementException e) {
      System.err.println(
          Problem.logPrefix() + " Parse failure (" + (line != null ? "invalid line: \"" + line
              + "\"" : token != null ? "last valid token: \"" + token + "\"" : "unsupported format")
              + ").");
    } catch (IOException e) {
      System.err.println(
          Problem.logPrefix() + " Read failure (invalid file: \"" + inputFile + "\").");
    }
    return Collections.emptyList();
  }

  private void validateProblem(Popeye.Problem specification) {
    if (specification.getStipulation() == null) {
      throw new IllegalArgumentException("Problem conversion failure (missing stipulation).");
    }
    Arrays.stream(Popeye.Colour.values()).forEach(colour -> specification.getPieces().stream()
        .collect(Collectors.toMap(Popeye.Piece::square, Function.identity(),
            (oldValue, newValue) -> newValue)).values().stream()
        .filter(piece -> piece.colour() == colour && piece.pieceType() == Popeye.PieceType.King)
        .reduce((oldValue, newValue) -> {
          throw new IllegalArgumentException(
              "Problem conversion failure (too many " + colour.toString().toLowerCase()
                  + " kings).");
        }).orElseThrow(() -> new IllegalArgumentException(
            "Problem conversion failure (missing " + colour.toString().toLowerCase() + " king).")));
  }

  private void verifyProblem(Popeye.Problem specification) {
    specification.getOptions().getNoCastling().stream().filter(square -> switch (square.file()) {
      case _a, _e, _h -> false;
      case _b, _c, _d, _f, _g -> true;
    } || switch (square.rank()) {
      case _1, _8 -> false;
      case _2, _3, _4, _5, _6, _7 -> true;
    }).findAny().ifPresent(square -> {
      throw new UnsupportedOperationException(
          "Task creation failure (not accepted option: nocastling " + Popeye.fileCodes.get(
              square.file()) + Popeye.rankCodes.get(square.rank()) + ").");
    });
    specification.getOptions().getEnPassant().stream().peek(square -> {
      if (specification.getOptions().isSetPlay()) {
        throw new UnsupportedOperationException(
            "Task creation failure (not accepted option: enpassant w/ setplay).");
      }
    }).distinct().reduce((oldValue, newValue) -> {
      throw new UnsupportedOperationException(
          "Task creation failure (not accepted option: multiple enpassant).");
    }).filter(square -> {
      List<Popeye.Piece> pieces = specification.getPieces().stream().collect(
          Collectors.toMap(Popeye.Piece::square, Function.identity(),
              (oldValue, newValue) -> newValue)).values().stream().toList();
      Popeye.Colour sideToMove = switch (specification.getStipulation().stipulationType()) {
        case Direct, Self -> specification.getOptions().isHalfDuplex();
        case Help ->
            specification.getOptions().isHalfDuplex() == specification.getOptions().isWhiteToPlay();
      } ? Popeye.Colour.Black : Popeye.Colour.White;
      return switch (square.rank()) {
        case _1, _2, _4, _5, _7, _8 -> true;
        case _3 -> pieces.stream().anyMatch(piece -> piece.square().file() == square.file() && (
            piece.square().rank() == square.rank() || piece.square().rank() == Popeye.Rank._2))
            || pieces.stream().noneMatch(piece -> piece.square().file() == square.file()
            && piece.square().rank() == Popeye.Rank._4 && piece.colour() == Popeye.Colour.White
            && piece.pieceType() == Popeye.PieceType.Pawn) || sideToMove == Popeye.Colour.White;
        case _6 -> pieces.stream().anyMatch(piece -> piece.square().file() == square.file() && (
            piece.square().rank() == square.rank() || piece.square().rank() == Popeye.Rank._7))
            || pieces.stream().noneMatch(piece -> piece.square().file() == square.file()
            && piece.square().rank() == Popeye.Rank._5 && piece.colour() == Popeye.Colour.Black
            && piece.pieceType() == Popeye.PieceType.Pawn) || sideToMove == Popeye.Colour.Black;
      };
    }).ifPresent(square -> {
      throw new UnsupportedOperationException(
          "Task creation failure (not accepted option: enpassant " + Popeye.fileCodes.get(
              square.file()) + Popeye.rankCodes.get(square.rank()) + ").");
    });
  }

  private Task convertProblem(Popeye.Problem specification) {
    Board board = specification.getPieces().stream().map(Popeye.Piece::pieceType).allMatch(
        pieceType -> pieceType == Popeye.PieceType.King || pieceType == Popeye.PieceType.Queen
            || pieceType == Popeye.PieceType.Rook || pieceType == Popeye.PieceType.Bishop
            || pieceType == Popeye.PieceType.Knight || pieceType == Popeye.PieceType.Pawn)
        ? new MailboxBoard() : new DefaultBoard();
    specification.getPieces().forEach(piece -> board.put(
        board.getSquare(convertFile(piece.square().file()), convertRank(piece.square().rank())),
        convertPieceTypeAndColour(piece.pieceType(), piece.colour())));
    Box box = new DefaultBox();
    Popeye.PieceType[] promotionTypes = Stream.concat(
            Stream.of(Popeye.PieceType.Queen, Popeye.PieceType.Rook, Popeye.PieceType.Bishop,
                Popeye.PieceType.Knight),
            specification.getPieces().stream().map(Popeye.Piece::pieceType).filter(
                pieceType -> pieceType != Popeye.PieceType.King && pieceType != Popeye.PieceType.Pawn))
        .distinct().sorted().toArray(Popeye.PieceType[]::new);
    Arrays.stream(Popeye.Colour.values()).forEach(colour -> {
      int maxMove = switch (specification.getStipulation().stipulationType()) {
        case Direct -> switch (colour) {
          case White -> specification.getOptions().isHalfDuplex();
          case Black -> !specification.getOptions().isHalfDuplex();
        };
        case Help -> specification.getOptions().isWhiteToPlay() && switch (colour) {
          case White -> specification.getOptions().isHalfDuplex();
          case Black -> !specification.getOptions().isHalfDuplex();
        };
        case Self -> false;
      } ? specification.getStipulation().nMoves() : specification.getStipulation().nMoves() + 1;
      int nPawns = specification.getPieces().stream().collect(
              Collectors.toMap(Popeye.Piece::square, Function.identity(),
                  (oldValue, newValue) -> newValue)).values().stream()
          .filter(piece -> piece.colour() == colour && piece.pieceType() == Popeye.PieceType.Pawn)
          .mapToInt(piece -> 1).sum();
      int maxPromotion = Math.min(maxMove, nPawns);
      IntStream.range(0, promotionTypes.length).forEach(index -> IntStream.range(0, maxPromotion)
          .forEach(promotionNo -> box.push(box.getSection(convertColour(colour), index + 1),
              convertPieceTypeAndColour(promotionTypes[index], colour))));
    });
    Table table = new DefaultTable();
    Colour sideToMove = switch (specification.getStipulation().stipulationType()) {
      case Direct, Self -> specification.getOptions().isHalfDuplex();
      case Help ->
          specification.getOptions().isHalfDuplex() == specification.getOptions().isWhiteToPlay();
    } ? Colour.BLACK : Colour.WHITE;
    State state = new DefaultState();
    specification.getPieces().stream().collect(
            Collectors.toMap(Popeye.Piece::square, Function.identity(),
                (oldValue, newValue) -> newValue)).values().stream().filter(piece ->
            (piece.pieceType() == Popeye.PieceType.King && piece.square().file() == Popeye.File._e
                || piece.pieceType() == Popeye.PieceType.Rook && (
                piece.square().file() == Popeye.File._a || piece.square().file() == Popeye.File._h))
                && (piece.colour() == Popeye.Colour.White && piece.square().rank() == Popeye.Rank._1
                || piece.colour() == Popeye.Colour.Black && piece.square().rank() == Popeye.Rank._8))
        .map(Popeye.Piece::square)
        .filter(square -> !specification.getOptions().getNoCastling().contains(square)).forEach(
            square -> state.addCastling(
                board.getSquare(convertFile(square.file()), convertRank(square.rank()))));
    specification.getOptions().getEnPassant().forEach(square -> state.setEnPassant(
        board.getSquare(convertFile(square.file()), convertRank(square.rank()))));
    Memory memory = new DefaultMemory();
    MoveFactory moveFactory =
        specification.getConditions().isCirce() ? new CirceMoveFactory() : new MoveFactory() {
          @Override
          public String toString() {
            return "default";
          }
        };
    Position position = new Position(board, box, table, sideToMove, state, memory, moveFactory);
    Aim aim = switch (specification.getStipulation().goal()) {
      case Mate -> Aim.MATE;
      case Stalemate -> Aim.STALEMATE;
    };
    int nMoves = switch (specification.getStipulation().stipulationType()) {
      case Direct, Self -> false;
      case Help -> specification.getOptions().isWhiteToPlay();
    } ? specification.getStipulation().nMoves() - 1 : specification.getStipulation().nMoves();
    boolean halfMove = switch (specification.getStipulation().stipulationType()) {
      case Direct, Self -> false;
      case Help -> specification.getOptions().isWhiteToPlay();
    };
    Problem problem = switch (specification.getStipulation().stipulationType()) {
      case Direct -> new Directmate(position, aim, nMoves);
      case Self -> new Selfmate(position, aim, nMoves);
      case Help -> new Helpmate(position, aim, nMoves, halfMove);
    };
    boolean setPlay = specification.getOptions().isSetPlay();
    int nRefutations = Math.max(specification.getOptions().getDefence(),
        specification.getOptions().isTry() ? 1 : 0);
    boolean variations = specification.getOptions().isVariation();
    boolean threats =
        specification.getOptions().isVariation() && !specification.getOptions().isNoThreat();
    boolean shortVariations =
        specification.getOptions().isVariation() && !specification.getOptions()
            .isNoShortVariations();
    boolean tempoTries =
        specification.getOptions().isNullMoves() || specification.getOptions().isTry();
    AnalysisOptions analysisOptions = switch (specification.getStipulation().stipulationType()) {
      case Direct, Self ->
          new BattlePlayOptions(setPlay, nRefutations, variations, threats, shortVariations);
      case Help -> new HelpPlayOptions(setPlay, tempoTries);
    };
    Locale outputLanguage = inputLanguage;
    boolean internalModel = !specification.getOptions().isNoBoard();
    boolean internalProgress = specification.getOptions().isMoveNumbers();
    DisplayOptions displayOptions = new LogOptions(outputLanguage, internalModel, internalProgress);
    return new Task(problem, analysisOptions, displayOptions);
  }

  private Piece convertPieceTypeAndColour(Popeye.PieceType pieceType, Popeye.Colour colour) {
    return switch (pieceType) {
      case King -> new King(convertColour(colour));
      case Queen -> new Queen(convertColour(colour));
      case Rook -> new Rook(convertColour(colour));
      case Bishop -> new Bishop(convertColour(colour));
      case Knight -> new Knight(convertColour(colour));
      case Pawn -> new Pawn(convertColour(colour));
      case Grasshopper -> new Grasshopper(convertColour(colour));
      case Nightrider -> new Nightrider(convertColour(colour));
      case Amazon -> new Amazon(convertColour(colour));
    };
  }

  private Colour convertColour(Popeye.Colour colour) {
    return switch (colour) {
      case White -> Colour.WHITE;
      case Black -> Colour.BLACK;
    };
  }

  private int convertFile(Popeye.File file) {
    return switch (file) {
      case _a -> 1;
      case _b -> 2;
      case _c -> 3;
      case _d -> 4;
      case _e -> 5;
      case _f -> 6;
      case _g -> 7;
      case _h -> 8;
    };
  }

  private int convertRank(Popeye.Rank rank) {
    return switch (rank) {
      case _1 -> 1;
      case _2 -> 2;
      case _3 -> 3;
      case _4 -> 4;
      case _5 -> 5;
      case _6 -> 6;
      case _7 -> 7;
      case _8 -> 8;
    };
  }

  private void validatePosition(Model.Position specification) {
    if (!Stream.of(Model.Piece.WhiteKing, Model.Piece.BlackKing).allMatch(king ->
        specification.getBoard().stream().filter(piece -> piece == king).mapToInt(piece -> 1).sum()
            == 1)) {
      throw new IllegalArgumentException(
          "Position conversion failure (not accepted number of kings).");
    }
    if (!specification.getCastlings().stream().allMatch(castling -> switch (castling) {
      case WhiteShort, WhiteLong -> specification.getBoard().get(60) == Model.Piece.WhiteKing;
      case BlackShort, BlackLong -> specification.getBoard().get(4) == Model.Piece.BlackKing;
    } && switch (castling) {
      case WhiteShort -> specification.getBoard().get(63) == Model.Piece.WhiteRook;
      case WhiteLong -> specification.getBoard().get(56) == Model.Piece.WhiteRook;
      case BlackShort -> specification.getBoard().get(7) == Model.Piece.BlackRook;
      case BlackLong -> specification.getBoard().get(0) == Model.Piece.BlackRook;
    })) {
      throw new IllegalArgumentException(
          "Position conversion failure (not accepted castling rights).");
    }
    if (specification.getEnPassant() != null) {
      if (!switch (specification.getSideToMove()) {
        case White ->
            specification.getEnPassant().index() >= 16 && specification.getEnPassant().index() <= 23
                && specification.getBoard().get(specification.getEnPassant().index()) == null
                && specification.getBoard().get(specification.getEnPassant().index() - 8) == null
                && specification.getBoard().get(specification.getEnPassant().index() + 8)
                == Model.Piece.BlackPawn;
        case Black ->
            specification.getEnPassant().index() >= 40 && specification.getEnPassant().index() <= 47
                && specification.getBoard().get(specification.getEnPassant().index()) == null
                && specification.getBoard().get(specification.getEnPassant().index() + 8) == null
                && specification.getBoard().get(specification.getEnPassant().index() - 8)
                == Model.Piece.WhitePawn;
      }) {
        throw new IllegalArgumentException(
            "Position conversion failure (not accepted en passant square).");
      }
    }
  }

  private Task convertPosition(Model.Position specification) {
    Board board = new MailboxBoard();
    IntStream.range(0, 64).filter(index -> specification.getBoard().get(index) != null).forEach(
        index -> board.put(board.getSquare(index % 8 + 1, 8 - index / 8),
            convertPiece(specification.getBoard().get(index))));
    Box box = new DefaultBox();
    Arrays.stream(Model.Colour.values()).forEach(colour -> {
      int maxMove = switch (specification.getOperation().opcode()) {
        case ACD -> specification.getSideToMove() == colour ?
            (specification.getOperation().operand() + 1) / 2 + 1
            : specification.getOperation().operand() / 2 + 1;
        case DM ->
            specification.getSideToMove() == colour ? specification.getOperation().operand() + 1
                : specification.getOperation().operand();
      };
      int nPawns = specification.getBoard().stream().filter(piece -> piece == switch (colour) {
        case White -> Model.Piece.WhitePawn;
        case Black -> Model.Piece.BlackPawn;
      }).mapToInt(piece -> 1).sum();
      int maxPromotion = Math.min(maxMove, nPawns);
      Model.Piece[] promotions = (switch (colour) {
        case White ->
            Stream.of(Model.Piece.WhiteQueen, Model.Piece.WhiteRook, Model.Piece.WhiteBishop,
                Model.Piece.WhiteKnight);
        case Black ->
            Stream.of(Model.Piece.BlackQueen, Model.Piece.BlackRook, Model.Piece.BlackBishop,
                Model.Piece.BlackKnight);
      }).toArray(Model.Piece[]::new);
      IntStream.range(0, promotions.length).forEach(index -> IntStream.range(0, maxPromotion)
          .forEach(promotionNo -> box.push(box.getSection(convertColour(colour), index + 1),
              convertPiece(promotions[index]))));
    });
    Table table = new DefaultTable();
    Colour sideToMove = convertColour(specification.getSideToMove());
    State state = new DefaultState();
    specification.getCastlings().forEach(castling -> IntStream.of(switch (castling) {
      case WhiteShort, WhiteLong -> 60;
      case BlackShort, BlackLong -> 4;
    }, switch (castling) {
      case WhiteShort -> 63;
      case WhiteLong -> 56;
      case BlackShort -> 7;
      case BlackLong -> 0;
    }).forEach(index -> state.addCastling(board.getSquare(index % 8 + 1, 8 - index / 8))));
    if (specification.getEnPassant() != null) {
      state.setEnPassant(board.getSquare(specification.getEnPassant().index() % 8 + 1,
          8 - specification.getEnPassant().index() / 8));
    }
    Memory memory = new DefaultMemory();
    MoveFactory moveFactory = new MoveFactory() {
      @Override
      public String toString() {
        return "default";
      }
    };
    Position position = new Position(board, box, table, sideToMove, state, memory, moveFactory);
    int nMoves = switch (specification.getOperation().opcode()) {
      case ACD -> specification.getOperation().operand() / 2;
      case DM -> specification.getOperation().operand();
    };
    boolean halfMove = switch (specification.getOperation().opcode()) {
      case ACD -> specification.getOperation().operand() % 2 == 1;
      case DM -> false;
    };
    Problem problem = switch (specification.getOperation().opcode()) {
      case ACD -> new Perft(position, nMoves, halfMove);
      case DM -> new MateSearch(position, nMoves);
    };
    AnalysisOptions analysisOptions = new AnalysisOptions() {
      @Override
      public String toString() {
        return "default";
      }
    };
    DisplayOptions displayOptions = new DisplayOptions() {
      @Override
      public String toString() {
        return "default";
      }
    };
    return new Task(problem, analysisOptions, displayOptions);
  }

  private Piece convertPiece(Model.Piece piece) {
    return switch (piece) {
      case WhiteKing -> new King(Colour.WHITE);
      case WhiteQueen -> new Queen(Colour.WHITE);
      case WhiteRook -> new Rook(Colour.WHITE);
      case WhiteBishop -> new Bishop(Colour.WHITE);
      case WhiteKnight -> new Knight(Colour.WHITE);
      case WhitePawn -> new Pawn(Colour.WHITE);
      case BlackKing -> new King(Colour.BLACK);
      case BlackQueen -> new Queen(Colour.BLACK);
      case BlackRook -> new Rook(Colour.BLACK);
      case BlackBishop -> new Bishop(Colour.BLACK);
      case BlackKnight -> new Knight(Colour.BLACK);
      case BlackPawn -> new Pawn(Colour.BLACK);
    };
  }

  private Colour convertColour(Model.Colour colour) {
    return switch (colour) {
      case White -> Colour.WHITE;
      case Black -> Colour.BLACK;
    };
  }
}

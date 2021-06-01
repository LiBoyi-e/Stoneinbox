package rollingcubes.state;

import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;

import org.tinylog.Logger;

import java.util.Arrays;

/**
 * Class representing the state of the puzzle.
 */
public class RollingCubesState {

    /**
     * The array representing the initial configuration of the tray.
     */
    public static final int[][] INITIAL = {
            {1, 1, 1},
            {1, 0, 1},
            {1, 1, 1}
    };

    /**
     * The array representing a near-goal configuration of the tray.
     */
    public static final int[][] NEAR_GOAL = {
            {1, 0, 2},
            {3, 5, 2},
            {6, 1, 5}
    };

    /**
     * The array storing the current configuration of the tray.
     */
    private ReadOnlyObjectWrapper<Cube>[][] tray = new ReadOnlyObjectWrapper[3][3];

    /**
     * Indicates whether the puzzle is solved.
     */
    private ReadOnlyBooleanWrapper solved = new ReadOnlyBooleanWrapper();

    /**
     * The row of the empty space.
     */
    private int emptyRow;

    /**
     * The column of the empty space.
     */
    private int emptyCol;

    /**
     * Creates a {@code RollingCubesState} object representing the (original) initial state of the puzzle.
     */
    public RollingCubesState() {
        this(NEAR_GOAL);
    }

    /**
     * Creates a {@code RollingCubesState} object that is initialized it with the specified array.
     *
     * @param a an array of size 3&#xd7;3 representing the initial configuration of the tray
     * @throws IllegalArgumentException if the array does not represent a valid configuration of the tray
     */
    public RollingCubesState(int[][] a) {
        if (!isValidTray(a)) {
            throw new IllegalArgumentException();
        }
        initTray(a);
        solved.bind(
                new BooleanBinding() {
                    {
                        super.bind(Arrays.stream(tray).flatMap(Arrays::stream).toArray(Observable[]::new));
                    }

                    @Override
                    protected boolean computeValue() {
                        return checkSolved();
                    }
                }
        );
    }

    private boolean isValidTray(int[][] a) {
        if (a == null || a.length != 3) {
            return false;
        }
        boolean foundEmpty = false;
        for (int[] row : a) {
            if (row == null || row.length != 3) {
                return false;
            }
            for (int space : row) {
                if (space < 0 || space >= Cube.values().length) {
                    return false;
                }
                if (space == Cube.EMPTY.getValue()) {
                    if (foundEmpty) {
                        return false;
                    }
                    foundEmpty = true;
                }
            }
        }
        return foundEmpty;
    }

    private void initTray(int[][] a) {
        for (var i = 0; i < 3; i++) {
            for (var j = 0; j < 3; j++) {
                Cube cube;
                if ((cube = Cube.of(a[i][j])) == Cube.EMPTY) {
                    emptyRow = i;
                    emptyCol = j;
                }
                tray[i][j] = new ReadOnlyObjectWrapper<>(cube);
            }
        }
    }

    private boolean checkSolved() {
        for (var row : tray) {
            for (var cube : row) {
                if (cube.get() != Cube.CUBE6 && cube.get() != Cube.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * {@return the row of the empty space}
     */
    public int getEmptyRow() {
        return emptyRow;
    }

    /**
     * {@return the column of the empty space}
     */
    public int getEmptyCol() {
        return emptyCol;
    }

    /**
     * {@return {@code true} if the puzzle is solved, {@code false} otherwise}
     */
    public boolean isSolved() {
        return solved.get();
    }

    public ReadOnlyBooleanProperty solvedProperty() {
        return solved.getReadOnlyProperty();
    }

    /**
     * {@return the state of the position specified in the tray}
     *
     * @param row the row of a position
     * @param col the column of a position
     */
    public Cube getCube(int row, int col) {
        return tray[row][col].get();
    }

    public ReadOnlyObjectProperty<Cube> cubeProperty(int row, int col) {
        return tray[row][col].getReadOnlyProperty();
    }

    /**
     * {@return a read-only view of the tray}
     */
    public Cube[][] getTray() {
        var a = new Cube[3][3];
        for (var i = 0; i < 3; i++) {
            for (var j = 0; j < 3; j++) {
                a[i][j] = tray[i][j].get();
            }
        }
        return a;
    }

    /**
     * Returns whether the cube at the specified position can be rolled to the empty space.
     *
     * @param row the row of the cube to be rolled
     * @param col the column of the cube to be rolled
     * @return {@code true} if the cube at the specified position can be rolled to the empty space, {@code false} otherwise
     */
    public boolean canRollToEmptySpace(int row, int col) {
        return 0 <= row && row <= 2 && 0 <= col && col <= 2 &&
                Math.abs(emptyRow - row) + Math.abs(emptyCol - col) == 1;
    }

    /**
     * Returns the direction to which the cube at the specified position is rolled to the empty space.
     *
     * @param row the row of the cube to be rolled
     * @param col the column of the cube to be rolled
     * @return the direction to which the cube at the specified position is rolled to the empty space
     * @throws IllegalArgumentException if the cube at the specified position can not be rolled to the empty space
     */
    public Direction getRollDirection(int row, int col) {
        if (! canRollToEmptySpace(row, col)) {
            throw new IllegalArgumentException();
        }
        return Direction.of(emptyRow - row, emptyCol - col);
    }

    /**
     * Rolls the cube at the specified position to the empty space.
     *
     * @param row the row of the cube to be rolled
     * @param col the column of the cube to be rolled
     * @throws IllegalArgumentException if the cube at the specified position can not be rolled to the empty space
     */
    public void rollToEmptySpace(int row, int col) {
        var direction = getRollDirection(row, col);
        Logger.debug("Cube at ({},{}) is rolled to {}", row, col, direction);
        tray[emptyRow][emptyCol].set(tray[row][col].get().rollTo(direction));
        tray[row][col].set(Cube.EMPTY);
        emptyRow = row;
        emptyCol = col;
    }

    public String toString() {
        var sb = new StringBuilder();
        for (var row : tray) {
            for (var cube : row) {
                sb.append(cube.get()).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        var state = new RollingCubesState();
        System.out.println(state);
    }

}

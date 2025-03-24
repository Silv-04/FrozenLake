import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FrozenLake {
    int nbOfWins = 0;
    int totalGame = 1000;
    double proba = 0.05;
    int MAXINT = 1000000;

    // Renvoie la grille du fichier grid.txt sous forme d'une matrice
    public List<List<String>> getGrid() {
        List<List<String>> grid = new ArrayList<>();
        try {
            FileReader fr = new FileReader("grid.txt");
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while (line != null) {
                List<String> row = new ArrayList<>();
                for (char c : line.toCharArray()) {
                    row.add(String.valueOf(c));
                }
                grid.add(row);
                line = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grid;
    }

    // Renvoie les coordonnées d'une position souhaitée
    public int[] position(List<List<String>> grid, String position) {
        int x = 0;
        int y = 0;
        for (List<String> row : grid) {
            for (String cell : row) {
                if (cell.equals(position)) {
                    x = grid.indexOf(row);
                    y = row.indexOf(cell);
                }
            }
        }
        return new int[] { x, y };
    }

    // Renvoie toutes les positions possibles à partir d'une position donnée
    public List<String> availableActions(List<List<String>> grid, int[] position) {
        List<String> actions = new ArrayList<>();
        int x = position[0];
        int y = position[1];

        if (x > 0) {
            actions.add("UP");
        }
        if (x < grid.size() - 1) {
            actions.add("DOWN");
        }
        if (y > 0) {
            actions.add("LEFT");
        }
        if (y < grid.get(x).size() - 1) {
            actions.add("RIGHT");
        }
        return actions;
    }

    // Simule la glissade avec une probabilité de "proba"
    public String move(double rdm, String actionToDo, List<String> actions) {
        String action = actionToDo;
        if (actionToDo.equals("UP") || actionToDo.equals("DOWN")) {
            if (actions.contains("LEFT")) {
                if (actions.contains("RIGHT")) {
                    if (rdm < proba) {
                        action = "LEFT";
                    } else {
                        if (rdm > 1 - proba) {
                            action = "RIGHT";
                        }
                    }
                } else {
                    if (rdm < proba) {
                        return "LEFT";
                    }
                }
            } else {
                if (actions.contains("RIGHT")) {
                    if (rdm < proba) {
                        action = "RIGHT";
                    }
                }
            }
        } else {
            if (actions.contains("UP")) {
                if (actions.contains("DOWN")) {
                    if (rdm < proba) {
                        action = "UP";
                    } else {
                        if (rdm > 1 - proba) {
                            action = "DOWN";
                        }
                    }
                } else {
                    if (rdm < proba) {
                        return "UP";
                    }
                }
            } else {
                if (actions.contains("DOWN")) {
                    if (rdm < proba) {
                        action = "DOWN";
                    } else {
                        action = actionToDo;
                    }
                }
            }
        }
        return action;
    }

    // Simule un déplacement aléatoire en prenant en compte la glissade
    public int[] simulateOneStep(List<List<String>> grid, int[] position) {
        List<String> actions = availableActions(grid, position);

        int x = position[0];
        int y = position[1];

        String actionToDo = actions.get((int) (Math.random() * actions.size()));
        String action = move(Math.random(), actionToDo, actions);

        if (action.equals("UP")) {
            x--;
        }
        if (action.equals("DOWN")) {
            x++;
        }
        if (action.equals("LEFT")) {
            y--;
        }
        if (action.equals("RIGHT")) {
            y++;
        }
        return new int[] { x, y };
    }

    // Renvoie true si on est sur "G" le point d'arrivée
    public boolean isOver(List<List<String>> grid, int[] position) {
        int x = position[0];
        int y = position[1];

        if (grid.get(x).get(y).equals("G")) {
            return true;
        } else {
            return false;
        }
    }

    // Renvoie true si on tombe dans un trou "H"
    public boolean fellInHole(List<List<String>> grid, int[] position) {
        int x = position[0];
        int y = position[1];
        if (grid.get(x).get(y).equals("H")) {
            return true;
        } else {
            return false;
        }
    }

    // Calcule la distance de manhattan
    public int manhattanDistance(int[] playerPosition, int[] goalPosition) {
        return Math.abs(playerPosition[0] - goalPosition[0]) + Math.abs(playerPosition[1] - goalPosition[1]);
    }

    // Simule un déplacement en prenant en compte la distance de manhattan
    public int[] manhattanOneStep(List<List<String>> grid, int[] position) {
        List<String> actions = availableActions(grid, position);

        int x = position[0];
        int y = position[1];

        int[] goalPosition = position(grid, "G");

        int minDistUp = MAXINT;
        int minDistDown = MAXINT;
        int minDistLeft = MAXINT;
        int minDistRight = MAXINT;

        if (actions.contains("UP")) {
            if (!grid.get(x - 1).get(y).equals("H")) {
                minDistUp = manhattanDistance(new int[] { x - 1, y }, goalPosition);
            }
        }
        if (actions.contains("DOWN")) {
            if (!grid.get(x + 1).get(y).equals("H")) {
                minDistDown = manhattanDistance(new int[] { x + 1, y }, goalPosition);
            }
        }
        if (actions.contains("LEFT")) {
            if (!grid.get(x).get(y - 1).equals("H")) {
                minDistLeft = manhattanDistance(new int[] { x, y - 1 }, goalPosition);
            }
        }
        if (actions.contains("RIGHT")) {
            if (!grid.get(x).get(y + 1).equals("H")) {
                minDistRight = manhattanDistance(new int[] { x, y + 1 }, goalPosition);
            }
        }

        int minDist = Math.min(Math.min(minDistUp, minDistDown), Math.min(minDistLeft, minDistRight));
        if (minDist == minDistUp) {
            switch (move(Math.random(), "UP", actions)) {
                case "UP":
                    x--;
                    break;
                case "LEFT":
                    y--;
                    break;
                case "RIGHT":
                    y++;
                    break;
            }
        } else {
            if (minDist == minDistDown) {
                switch (move(Math.random(), "DOWN", actions)) {
                    case "DOWN":
                        x++;
                        break;
                    case "LEFT":
                        y--;
                        break;
                    case "RIGHT":
                        y++;
                        break;
                }
            } else {
                if (minDist == minDistLeft) {
                    switch (move(Math.random(), "LEFT", actions)) {
                        case "UP":
                            x--;
                            break;
                        case "DOWN":
                            x++;
                            break;
                        case "LEFT":
                            y--;
                            break;
                    }
                } else {
                    switch (move(Math.random(), "RIGHT", actions)) {
                        case "UP":
                            x--;
                            break;
                        case "DOWN":
                            x++;
                            break;
                        case "RIGHT":
                            y++;
                            break;
                    }
                }
            }
        }
        return new int[] { x, y };
    }

    // Lance la partie avec le mode souhaitée (NORMAL ou OPTIMIZED)
    public void play(String mode) {
        int[] position = position(getGrid(), "S");
        List<List<String>> grid = getGrid();

        int nbOfGame = 0;

        if (mode.equals("NORMAL")) {
            while (nbOfGame < totalGame) {
                if (!isOver(grid, position)) {
                    if (!fellInHole(grid, position)) {
                        position = simulateOneStep(grid, position);
                    } else {
                        position = position(grid, "S");
                        nbOfGame++;
                    }
                } else {
                    position = position(grid, "S");
                    nbOfGame++;
                    nbOfWins++;
                }
            }
        }
        if (mode.equals("OPTIMIZED")) {
            while (nbOfGame < totalGame) {
                if (!isOver(grid, position)) {
                    if (!fellInHole(grid, position)) {
                        position = manhattanOneStep(grid, position);
                    } else {
                        position = position(grid, "S");
                        nbOfGame++;
                    }
                } else {
                    position = position(grid, "S");
                    nbOfGame++;
                    nbOfWins++;
                }
            }
        }
        double winRate = ((double) nbOfWins / (double) totalGame) * 100;
        System.out.println("Number of wins: " + nbOfWins);
        System.out.println("Total game: " + totalGame);
        System.out.println("Win rate: " + (int) winRate + "%");
    }

    public static void main(String[] args) {
        FrozenLake fl = new FrozenLake();
        System.out.println("====NORMAL FROZEN LAKE====");
        fl.play("NORMAL");
        System.out.println("---------------------------");
        System.out.println("===OPTIMIZED FROZEN LAKE===");
        fl.play("OPTIMIZED");
    }
}
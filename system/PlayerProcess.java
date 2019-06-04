package project.system;

import java.util.concurrent.Callable;
import java.util.Scanner;

public class PlayerProcess implements Callable<int[]> {
    ProcessLock Lock;
    Player player;

    public PlayerProcess(Player player) {
        this.player = player;
    }

    public int[] call() {
        //busy waiting
        while (Lock.checkPlayerLock()) ;
        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        int[] op = new int[3];
        String input;
        String[] op_string;

        //Show current hand
        player.show(GameSystem.gs.getCardList());
        System.out.println("Left -> L, Right -> R, Up -> U, Down -> D, Guard -> G, Charge -> C");
        do {
            System.out.print("Choose order of Cards ; ");
            input = scanner.nextLine();
            op_string = input.split(" ");
            if (op_string.length > 3) {
                System.out.println("Wrong Length of Cards\n");
                continue;
            }
            if (op_string[0].equals(op_string[1]) || op_string[0].equals(op_string[2]) || op_string[1].equals(op_string[2])) {
                System.out.println("Cards can use only one time per turn");
                continue;
            }
            for (int i = 0; i < 3; ++i) {
                if (op_string[i].equals("L") || op_string[i].equals("l")) op[i] = 0;
                else if (op_string[i].equals("R") || op_string[i].equals("r")) op[i] = 1;
                else if (op_string[i].equals("U") || op_string[i].equals("u")) op[i] = 2;
                else if (op_string[i].equals("D") || op_string[i].equals("d")) op[i] = 3;
                else if (op_string[i].equals("G") || op_string[i].equals("g")) op[i] = 4;
                else if (op_string[i].equals("C") || op_string[i].equals("c")) op[i] = 5;
                else {
                    try {
                        op[i] = Integer.parseInt(op_string[i]);
                    } catch (NumberFormatException ex) {
                        System.out.println("Cannot convert to integer error");
                        break;
                    }
                    try {
                        if (player.hand[op[i]]) {
                            op[i] += 6;
                        } else {
                            System.out.println("Not in your hand error");
                            break;
                        }
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        System.out.println("Array out of bounds error");
                        break;
                    }
                }
                if (i == 2) done = true;
            }
        } while (!done);
        Lock.EndPlayerTurn();
        return op;
    }
}

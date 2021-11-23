import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Send your busters out into the fog to trap ghosts and bring them home!
 **/
class Player {
    static ArrayList<Integer> alliedBusters;
    static ArrayList<Integer> explorationStatus;
    static ArrayList<Integer> enemyBusters;
    static ArrayList<Integer> ghosts;
    //multipliers: the number of variables per 'item' in arrays, for easier scanning
    static final int alliedBustersMultiplier = 3;
    static final int explorationMultiplier = 3;
    static final int enemyBustersMultiplier = 4;
    static final int ghostMultiplier = 4;

    static int homeX;
    static int homeY;

    public static int calculateDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.floor(Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)));
    }

    public static int findLowest(ArrayList<Integer> arr) {
        int temp = arr.get(0);
        int tempindex = 0;
        for (int j = 1; j < arr.size(); j ++) {
            if (arr.get(j) < temp) {
                tempindex = j;
                temp = arr.get(j);
            }
        }
        return tempindex;
    }

    public static void DefaultMovement(int busterIndex, int bustersPerPlayer) {
        if (Math.abs(explorationStatus.get(busterIndex * explorationMultiplier) - alliedBusters.get(busterIndex * alliedBustersMultiplier)) < 550 &&
             Math.abs(explorationStatus.get(busterIndex * explorationMultiplier + 1) - alliedBusters.get(busterIndex * alliedBustersMultiplier + 1)) < 550)
             {setNewTargetPoint(busterIndex, bustersPerPlayer);}
        System.out.println("MOVE " + explorationStatus.get(busterIndex * explorationMultiplier) + " " + explorationStatus.get(busterIndex * explorationMultiplier + 1));
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int bustersPerPlayer = in.nextInt(); // the amount of busters you control
        int ghostCount = in.nextInt(); // the amount of ghosts on the map
        int myTeamId = in.nextInt(); // if this is 0, your base is on the top left of the map, if it is one, on the bottom right

        if (myTeamId == 0){
            homeX=0;
            homeY=0;
        }
        else if (myTeamId == 1){
            homeX=16000;
            homeY=9000;
        }

        explorationStatus = new ArrayList<Integer>(); // an array of targets and exploration level for every buster 
        for (int n = 0; n < explorationMultiplier * bustersPerPlayer; n++) { explorationStatus.add(0); } // dest x,y , exploration level
        // game loop
        while (true) {
            alliedBusters = new ArrayList<Integer>();
            enemyBusters = new ArrayList<Integer>();
            ghosts = new ArrayList<Integer>();
            int entities = in.nextInt(); // the number of busters and ghosts visible to you
            for (int i = 0, tempIndex = 0; i < entities; i++) {
                int entityId = in.nextInt(); // buster id or ghost id
                int x = in.nextInt();
                int y = in.nextInt(); // position of this buster / ghost
                int entityType = in.nextInt(); // the team id if it is a buster, -1 if it is a ghost.
                int state = in.nextInt(); // For busters: 0=idle, 1=carrying a ghost.
                int value = in.nextInt(); // For busters: Ghost id being carried. For ghosts: number of busters attempting to trap this ghost.

                if (entityType == myTeamId) {
                    alliedBusters.add(x);
                    alliedBusters.add(y);
                    alliedBusters.add(state);
                }

                else if (entityType == -1) {
                    ghosts.add(x);
                    ghosts.add(y);
                    ghosts.add(entityId);
                    ghosts.add(state);
                }

                else {
                    enemyBusters.add(x);
                    enemyBusters.add(y);
                    enemyBusters.add(entityId);
                    enemyBusters.add(state);
                }

            }
            for (int i = 0; i < bustersPerPlayer; i++) {
                // start
                if (explorationStatus.get(i * explorationMultiplier + 2) == 0) {setNewTargetPoint(i, bustersPerPlayer);}
                // if carrying a ghost, move to the base 
                if (alliedBusters.get(i * alliedBustersMultiplier + 2) == 1) {
                    System.err.println(i + " - has a ghost");
                    if (calculateDistance(homeX, homeY, alliedBusters.get(i*alliedBustersMultiplier), alliedBusters.get(i*alliedBustersMultiplier+1))<1601){System.out.println("RELEASE");} //calculating whether the buster is in the base
                    else {System.out.println("MOVE " + homeX + " " + homeY); //else, move to home
                    }
                }
                //if there's at least one ghost near the buster:
                else if (ghosts.size() > 0) {
                    ArrayList<Integer> distPerGhost = new ArrayList<Integer>();
                    for (int a = 0; a < ghosts.size(); a += ghostMultiplier) {
                        int distance = calculateDistance(alliedBusters.get(i * alliedBustersMultiplier), alliedBusters.get(i * alliedBustersMultiplier + 1), ghosts.get(a), ghosts.get(a + 1));
                        distPerGhost.add(distance);
                    }
                    //for (int k = 0; k < distPerGhost.size(); k++){System.err.println("Distance of" + i + " to ghost " + k + " -> " + distPerGhost.get(k));}
                    //buster actions according to the distance between him and ghost 
                    int targetGhost = findLowest(distPerGhost);
                    int targetGhostDistance = distPerGhost.get (targetGhost);
                    if (targetGhostDistance <= 1760 && targetGhostDistance >= 900) {System.out.println("BUST " +  ghosts.get(targetGhost * ghostMultiplier + 2));}
                    else if (targetGhostDistance > 1760) {
                        System.err.println(i + " is moving to ghost number " + ghosts.get(targetGhost * ghostMultiplier + 2));
                        System.out.println("MOVE " + ghosts.get(targetGhost * ghostMultiplier) + " " + ghosts.get(targetGhost * ghostMultiplier + 1));
                    }
                    else if (targetGhostDistance < 900) { // get back in order to be in capture range
                        //System.err.println("Too close to the ghost");
                        System.out.println("MOVE "
                        + (2 * alliedBusters.get(i * alliedBustersMultiplier) - ghosts.get(targetGhost * ghostMultiplier)) + " "
                        + (2 * alliedBusters.get(i * alliedBustersMultiplier) - ghosts.get(targetGhost * ghostMultiplier + 1)));
                    }
                }
                else { //if there's no ghosts around, do the default 
                    DefaultMovement(i, bustersPerPlayer);
                }

            }
        }
    }
    
    public static void setNewTargetPoint(int busterIndex, int bustersPerPlayer) {
        int r = -1080;
        int d = -1080;
        int exp = explorationStatus.get(busterIndex * explorationMultiplier + 2);
        if (exp==0){
            r = 7000;
            d = 90;
        }
        else if (exp==1){
            r = 9500;
            d = 65;
        }
        else if(exp==2){
            r = 12000;
            d = 45;
        }
        else{d=-1;}
        explorationStatus.set(busterIndex * explorationMultiplier + 2, explorationStatus.get(busterIndex * explorationMultiplier + 2) + 1);
        System.err.println("exploration lvl is now " + explorationStatus.get(busterIndex * explorationMultiplier + 2));
        double dRad =  Math.toRadians(d * (busterIndex + 1) / (bustersPerPlayer + 1));
        explorationStatus.set(busterIndex*explorationMultiplier, (int)Math.floor(Math.abs(homeX - r * Math.cos(dRad))));
        explorationStatus.set(busterIndex * explorationMultiplier + 1, (int) Math.floor(Math.abs(homeY - r * Math.sin(dRad))));
        if (r == -1) { //busters need to stay in the designated area 
            explorationStatus.set(busterIndex * explorationMultiplier,  15000 * (busterIndex % 2));
            explorationStatus.set(busterIndex * explorationMultiplier + 1,  8000 * ((busterIndex + 1) % 2));
        }
        System.err.println("target for " + busterIndex + ": "+ explorationStatus.get(busterIndex * explorationMultiplier) + ", " + explorationStatus.get(busterIndex * explorationMultiplier + 1));
    }
}
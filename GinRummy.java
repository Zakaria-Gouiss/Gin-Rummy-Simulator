
import java.util.Scanner;
import java.util.ArrayList;

public class GinRummy {

    public static void seePlayers(ArrayList<Player> pl) {
        for (Player p : pl) {
            System.out.println(p);
        }
    }

    public static void clearData(ArrayList<Player> pl) {
        for (Player player : pl) {
            player.resetStats();
        }
    }

    public static boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!(Character.isDigit(str.charAt(0)))) {
                return false;
            }
        }
        return true;
    }
    public static String listPlayers(ArrayList<Player> p)
    {
        String retString = "";
        for (int i = 0; i < p.size(); i++)
        {
            retString += ("Player " + (i+1) + ": " + p.get(i).getName() + "\n");
        }
        return retString;
    }
    public static void playRound(ArrayList<Player> players) {
        try 
        {
            Scanner in = new Scanner(System.in);
            System.out.println("Round starting/in progress:");
            char firstChar = 'A';
            while (!(firstChar == 'Y' || firstChar == 'N')) {
                System.out.print("Has cheating occured before/during the round? Enter 'Y' or 'N' : ");
                firstChar = in.next().trim().charAt(0);
            }
            boolean cheatingBolFound = false;
            String cheatingPlayer;
            String pointsInput = "a";
            int pointsToAdd;
            int cheatingPlayerIndex = 0;
            if (firstChar == 'Y') 
            {
                
                while (!cheatingBolFound)
                {
                    System.out.print("Who cheated? Enter a valid player: ");
                    cheatingPlayer = in.next();
                    for (int i = 0; i < players.size(); i++)
                    {
                        Player pl = players.get(i);
                        if (pl.getName().equals(cheatingPlayer))
                        {
                            cheatingBolFound = true;
                            cheatingPlayerIndex = i;
                            break;
                        }
                    }
                }
                while(!isNumber(pointsInput))
                {
                    System.out.print("How many points to add: ");
                    pointsInput = in.next();
                }
                pointsToAdd = Integer.parseInt(pointsInput);
                players.get(cheatingPlayerIndex).addPoints(pointsToAdd);
            }
            System.out.print("Who closed? ");
            System.out.println();
            String closer;
            boolean closed = false;
            int closerIndex = 0;
            while (!closed)
            {
                closer = in.next().trim();
                for (int i = 0; i < players.size(); i++) {
                    if (players.get(i).getName().equals(closer)) 
                    {
                        closed = true;
                        closer = players.get(i).getName();
                        closerIndex = i;
                        break;
                    }
                }
                if (closed == false) {
                    System.out.println("Player not found. Try again:");
                }
            }
            char validClose = 'A';
            char bareMin = 'A';
            while (!(validClose == 'Y' || validClose == 'N')) {
                System.out.print("Is this valid? Type 'Y' or 'N' ");
                validClose = in.next().trim().charAt(0);
            }
            if (validClose == 'N') {
                players.get(closerIndex).failedClose();
                players.get(closerIndex).updateScore();
            } else if (validClose == 'Y') {
                String parseBol = "";
                boolean isDouble;
                while (!(parseBol.equals("false") || parseBol.equals("true"))) {
                    System.out.print("Is the joker used? Type a boolean in lowercase: ");
                    parseBol = in.next().trim();
                }
                isDouble = Boolean.parseBoolean(parseBol);
                System.out.println();
                for (int i = 0; i < players.size(); i++)
                {
                    if (i == closerIndex)
                    {
                        continue;
                    }
                    System.out.println(players.get(i).getName() + ", score counting will now start.");
                    while (!(bareMin == 'Y' || bareMin == 'N')) {
                        System.out.print("Does this player have the bare minimum? Type 'Y' or 'N': ");
                        bareMin = in.next().charAt(0);
                    }
                    if (bareMin == 'Y') {
                        players.get(i).startCount();
                    } else if (bareMin == 'N') {
                        players.get(i).failedHand();
                    }
                    bareMin = 'A';
                }
                if (isDouble) {
                    for (int i = 0; i < players.size(); i++) {
                        if (i == closerIndex) {
                            continue;
                        }
                        players.get(i).doubleScore();
                    }
                }
                for (int i = 0; i < players.size(); i++) {
                    if (i == closerIndex) {
                        continue;
                    }
                    players.get(i).updateScore();
                }
            }
            System.out.println();
            seePlayers(players);
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getTotalScore() > 101) {
                    System.out.println("Player " + players.get(i).getName() + " has been eliminated.");
                    players.get(i).resetStats();
                    players.remove(i);
                    i--;
                }
            }
            if (players.size() == 1) {
                System.out.println(players.get(0).getName() + " has won the game!");
                System.out.println();
                seePlayers(players);
                System.out.println();
                System.out.println("The game has ended.");
                players.get(0).resetStats();
                System.exit(0);
            } else {
                System.out.println("Proceeding to next round.");
                for (Player player : players) {
                    player.resetCurrentRound();
                }
            }
            playRound(players);
            in.close();
            
        } catch (Exception e)
        {
            System.out.println("An error has occured:");
            System.out.println();
            e.printStackTrace();
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();      
            System.out.println("This error has caused the program to terminate. Please try to run this program again.");
            System.out.println();
        }
        
    }

    public static void main(String[] args) {
        try
        {
            Scanner in = new Scanner(System.in);
            System.out.print("Welcome! How many players are playing? ");
            String playerStringToParse = "a";
            while(!isNumber(playerStringToParse))
            {
                playerStringToParse = in.next();
                if (!isNumber(playerStringToParse))
                {
                    System.out.print("Enter a valid number: ");
                }
            }
            int totalPlayers = Integer.parseInt(playerStringToParse);
            ArrayList<Player> players = new ArrayList<Player>();
            for (int i = 0; i < totalPlayers; i++) {
                System.out.print("Enter player " + (i + 1) + "'s name: ");
                String playerName = in.next();
                while (true) 
                {
                    boolean duplicatePlayers = false;
                    for (Player player : players) {
                        if (player.getName().equals(playerName))
                        {
                            System.out.print("Player already exists, enter a different name: ");
                            playerName = in.next();
                            duplicatePlayers = true;
                            break;
                        }
                    }
                    if (!duplicatePlayers)
                    {
                        players.add(new Player(playerName));
                        break;
                    }
                }
                
            }
            System.out.println();
            clearData(players);
            System.out.println(listPlayers(players));
            System.out.print("Press 'Z' to start a game\n");
            char letter;
            do
            {
                letter = in.next().charAt(0);
            } while (letter != 'Z');
            System.out.println();
            playRound(players);
            in.close();    
        }
        catch (Exception e)
        {
            System.out.println("An error has occured:");
            System.out.println();
            e.printStackTrace();
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();      
            System.out.println("This error has caused the program to terminate. Please try to run this program again.");
            System.out.println();
        }
        
    }
}

class Player {

    private String name;
    private int totalScore;
    private int currentRound;
    private Scanner scnr = new Scanner(System.in);
    private static int nextID = 1;
    private int playerID;

    public Player(String name) {
        this.name = name;
        totalScore = 0;
        currentRound = 0;
        this.playerID = nextID++;
    }

    public void failedClose() {
        currentRound = 25;
    }

    public void updateScore() {
        totalScore += currentRound;
    }

    public void failedHand() {
        currentRound = 51;
    }

    @Override
    public String toString() {return String.format("Player info:%nPlayer name: %s%nCurrent round: %d%nTotal score for game: %d%n", name, currentRound, totalScore);}

    public String getName() {
        return this.name;
    }
    
    public void startCount() {
        currentRound = 0;
        String input;
        System.out.print("Counting has started. Press 'Q' to stop. ");
        while (true) {
            input = scnr.next();
            if (input.equals("Q")) {
                break;
            } else {
                if (isNumber(input)) {
                    currentRound += Integer.parseInt(input);
                } else {
                    System.out.print("Not a number, try again: ");
                }
            }
        }
        String extraJokers;
        while (true) {
            System.out.print("How many extra jokers do you have?: ");
            extraJokers = scnr.next();
            if (isNumber(extraJokers)) {break;}
        }
        int totalExtraJokers = Integer.parseInt(extraJokers);
        currentRound += (10 * totalExtraJokers);
        System.out.println();
    }

    public int getTotalScore() {
        return this.totalScore;
    }

    public void doubleScore() {
        currentRound = (2 * currentRound);
    }

    public void resetStats() {
        this.totalScore = 0;
        this.currentRound = 0;
    }

    public void resetCurrentRound() {
        currentRound = 0;
    }

    private static boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!(Character.isDigit(str.charAt(0)))) {
                return false;
            }
        }
        return true;
    }

    public void addPoints(int n)
    {
        totalScore += n;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null){return false;}
        if (this == obj){return true;}
        if (!(obj instanceof Player)){return false;}
        Player pl = (Player) obj;
        return (this.playerID == pl.playerID && this.name.equals(pl.name));
    }
}
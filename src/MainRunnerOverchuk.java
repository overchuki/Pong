public class MainRunnerOverchuk {

    //main class which deals with and stores scores for two player and one player pong
    private static int leftScore=0;
    private static int rightScore=0;
    private static int mainScore=0;

    //main method which creates the frame window
    public static void main(String[] args){
        new GuiOverchuk();
    }

    //adds a point to the main score, which refers to single player
    public void addPointToMain(){
        mainScore++;
    }

    //getter for main score
    public int getMainScore(){
        return mainScore;
    }

    //resets every score to zero
    public void resetPoints(){
        leftScore=0;
        rightScore=0;
        mainScore=0;
    }

    //adds a point to the player on the left
    public void addPointToLeft(){
        leftScore++;
    }

    //adds a point to the player on the right
    public void addPointToRight(){
        rightScore++;
    }

    //getter for the score of the player on the left
    public int getLeftScore(){
        return leftScore;
    }

    //getter for the score of the player on the right
    public int getRightScore(){
        return rightScore;
    }
}
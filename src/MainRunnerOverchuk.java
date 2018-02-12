public class MainRunnerOverchuk {

    private static int leftScore=0;
    private static int rightScore=0;
    private static int mainScore=0;

    public static void main(String[] args){

        new GuiOverchuk();
    }

    public void addPointToMain(){
        mainScore++;
    }

    public int getMainScore(){
        return mainScore;
    }

    public void resetPoints(){
        leftScore=0;
        rightScore=0;
        mainScore=0;
    }

    public void addPointToLeft(){
        leftScore++;
    }

    public void addPointToRight(){
        rightScore++;
    }

    public int getLeftScore(){
        return leftScore;
    }

    public int getRightScore(){
        return rightScore;
    }
}
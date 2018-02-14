public class BallOverchuk {

    //variables that store the current position of the code
    private int ballX;
    private int ballY;

    //constant for the radius of the ball
    private static final int RADIUS=20;

    //variables that store the velocity of the ball, both x and y
    private int xDir;
    private int yDir;

    //constructor which creates the ball at 0,0
    public BallOverchuk(){
        ballX=0;
        ballY=0;
    }

    //getter for the radius
    public int getRadius(){
        return RADIUS;
    }

    //inverts the x velocity by multiplying it by -1
    public void invertxDir(){
        xDir=xDir*(-1);
    }

    //inverts the y velocity by multiplying it by -1
    public void invertyDir(){
        yDir=yDir*(-1);
    }

    //increases speed by adding to the current velocity of x and y
    public void increaseSpeed(boolean xVal,boolean yVal){
        //if the x value needs to be increased, it happens so here
        if(xVal) {
            //if velocity is positive, adds 2 speed to it
            if (xDir > 0) {
                xDir+=2;
            } else {
                //else, subtracts 2 to increase speed
                xDir-=2;
            }
        }
        //if the y value needs to be increased, it follows the same process as above
        if(yVal) {
            if (yDir > 0) {
                yDir+=2;
            } else {
                yDir-=2;
            }
        }
    }

    //getter for the x position
    public int getBallX() {
        return ballX;
    }

    //setter for the x position
    public void setBallX(int x) {
        ballX = x;
    }

    //getter for the y position
    public int getBallY() {
        return ballY;
    }

    //setter for the y position
    public void setBallY(int y) {
        ballY = y;
    }

    //getter for the x velocity
    public int getxDir() {
        return xDir;
    }

    //setter for the x velocity
    public void setxDir(int x) {
        xDir = x;
    }

    //getter for the y velocity
    public int getyDir() {
        return yDir;
    }

    //setter for the y velocity
    public void setyDir(int y) {
        yDir = y;
    }
}
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class GuiOverchuk extends JFrame {

    //two timers for two and one player pong
    private Timer timer;
    private Timer timerOneP;

    //booleans used to prevent the paddles from going over the top and bottom of the screen
    private boolean leftTop=false;
    private boolean leftBottom=false;
    private boolean rightTop=false;
    private boolean rightBottom=false;

    //
    private boolean bottomLeft=false;
    private boolean bottomRight=false;

    boolean goingLeft;
    boolean goingRight;

    //boolean that determines if the game will have a light theme
    private boolean whiteTheme=false;

    //integers that determine the number of players and the number of balls
    private static int numberOfBalls=1;
    private static int numberOfPlayers=1;

    //last hit variables for both balls that prevent them from getting stuck in paddles and walls
    private static int lastHitOne = -1;
    private static int lastHitTwo = -1;

    //variables initialized in the graphics constructor that store the height and width of the window
    private static int windowWidth;
    private static int windowHeight;

    //constant for the paddle speed at which it moves
    private static final int PADDLE_SPEED = 15;

    //initializer used in the graphics class that determines some first time values
    private boolean firstTime = true;

    //classes for the graphics, both balls, and the main class for managing score
    private static GameGraphics graph;
    private static BallOverchuk ballOne;
    private static BallOverchuk ballTwo;
    private static MainRunnerOverchuk run = new MainRunnerOverchuk();

    //classes for the paddles on the left, right, and bottom of the screen
    private PaddleOverchuk left;
    private PaddleOverchuk right;
    private PaddleOnePOverchuk bottom;

    //arrays used as options in the initial joptionpanes
    private String[] options = {"One","Two"};
    private String[] themes = {"Dark","Light"};
    private String[] pause = {"Resume","Restart","Exit"};

    //constructor that initializes the frame, sets up all the basics, and initializes all the classes
    public GuiOverchuk() {
        //all the basics are set up
        setTitle("Welcome to Pong!");
        setBackground(Color.BLACK);
        setSize(1800, 1080);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //joptionpanes that determine the look of the game and what type of game it is going to be
        JOptionPane.showMessageDialog(null,"You are about to start a game of Pong!");
        numberOfPlayers=JOptionPane.showOptionDialog(null,"How many players for the game?","Choose one below",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,"One");
        numberOfPlayers++;
        numberOfBalls=JOptionPane.showOptionDialog(null,"How many balls for the game?","Choose one below",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,"One");
        numberOfBalls++;
        if(numberOfPlayers==1) {
            JOptionPane.showMessageDialog(null, "Use the arrow keys to move left and right.");
        }else if(numberOfPlayers==2){
            int choice = JOptionPane.showOptionDialog(null,"Would you like a dark or light theme?","Choose one below",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,themes,"One");
            switch(choice){
                case 0:
                    whiteTheme=false;
                    break;
                case 1:
                    whiteTheme=true;
                    break;
            }
            JOptionPane.showMessageDialog(null, "Player on the right uses arrow keys to move.\nPlayer on the left uses 'W' and 'S' to move.");
        }

        //all the classes are initialized with correct values based off of the window
        graph = new GameGraphics();
        add(graph);

        ballOne = new BallOverchuk();
        ballTwo = new BallOverchuk();

        left = new PaddleOverchuk(20, 992);
        right = new PaddleOverchuk(getWidth() - 80, 992);
        bottom = new PaddleOnePOverchuk(942,1768);

        //method that resets the ball, or balls, in the middle and gives them a random direction and speed
        setRandomDirectionAndPosition();

        //creates a timer with two different classes based on the number of players
        if(numberOfPlayers==2) {
            timer = new Timer("animation");
            timer.scheduleAtFixedRate(new Task(), 150, (1000 / 60));
        }else if(numberOfPlayers==1){
            timerOneP=new Timer("animation");
            timerOneP.scheduleAtFixedRate(new TaskOneP(),150,(1000/60));
        }

        //this adds a key listener in the constructor to listen for different keys and perform actions
        addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 38) {
                    //up arrow
                    //sets the speed of the right paddle negative so it moves up
                    right.setyDir(-PADDLE_SPEED);
                }else if (e.getKeyCode() == 40) {
                    //down arrow
                    //sets the speed of the right paddle positive so it moves down
                    right.setyDir(PADDLE_SPEED);
                }

                if (e.getKeyCode() == 87) {
                    //W key
                    //sets the speed of the left paddle negative so it moves up
                    left.setyDir(-PADDLE_SPEED);
                }else if (e.getKeyCode() == 83) {
                    //S key
                    //sets the speed of the left paddle positive so it moves down
                    left.setyDir(PADDLE_SPEED);
                }

                if(e.getKeyCode()==37){
                    //left arrow
                    //sets the speed of the bottom paddle negative so it moves left
                    bottom.setxDir(-PADDLE_SPEED);
                }else if(e.getKeyCode()==39){
                    //right arrow
                    //sets the speed of the bottom paddle positive so it moves right
                    bottom.setxDir(PADDLE_SPEED);
                }

                //escape key
                if(e.getKeyCode()==27){
                    //if there are two players, the two player timer is paused
                    if(numberOfPlayers==2) {
                        pause();
                        int choice = JOptionPane.showOptionDialog(null, "Game Paused", "Paused", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, pause, "Resume");
                        //switch for the three possible statements from the joptionpane, while the animation is paused
                        switch (choice) {
                            case 0:
                                resume();
                                break;
                            case 1:
                                setRandomDirectionAndPosition();
                                run.resetPoints();
                                resume();
                                break;
                            case 2:
                                System.exit(0);
                                break;
                        }
                        //resumes if the pane is closed
                        if (choice == JOptionPane.CLOSED_OPTION) {
                            resume();
                        }
                    }else{
                        //same idea as above to implement a pause menu in the single player pong
                        pauseOneP();
                        int choice = JOptionPane.showOptionDialog(null, "Game Paused", "Paused", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, pause, "Resume");
                        switch (choice) {
                            case 0:
                                resumeOneP();
                                break;
                            case 1:
                                setRandomDirectionAndPosition();
                                run.resetPoints();
                                resumeOneP();
                                break;
                            case 2:
                                System.exit(0);
                                break;
                        }
                        if (choice == JOptionPane.CLOSED_OPTION) {
                            resumeOneP();
                        }
                    }
                }
            }

            //key released function that cancels the movements of the paddles
            public void keyReleased(KeyEvent e) {
                //right paddle speed is set to zero once either arrow key is released
                if (e.getKeyCode() == 38) {
                    right.setyDir(0);
                }else if (e.getKeyCode() == 40) {
                    right.setyDir(0);
                }
                //left paddle speed is set to zero once either W or S is released
                if (e.getKeyCode() == 87) {
                    left.setyDir(0);
                }else if (e.getKeyCode() == 83) {
                    left.setyDir(0);
                }
                //bottom paddle speed is set to zero once either arrow key is released
                if(e.getKeyCode()==37){
                    bottom.setxDir(0);
                }else if(e.getKeyCode()==39){
                    bottom.setxDir(0);
                }
            }

            //required function in the keylistener that does not do anything
            public void keyTyped(KeyEvent e) {

            }
        });

        //sets the frame visible at the end of the constructor
        setVisible(true);
    }

    //pauses the two player timer
    public void pause() {
        timer.cancel();
    }

    //pauses the one player timer
    public void pauseOneP() {
        timerOneP.cancel();
    }

    //resumes the two player timer
    public void resume() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new Task(), 0, (1000/60) );
    }

    //resumes the one player timer
    public void resumeOneP() {
        timerOneP = new Timer();
        timerOneP.scheduleAtFixedRate(new TaskOneP(), 0, (1000/60) );
    }

    //method that resets everything except for score
    public static void setRandomDirectionAndPosition() {
        //last hit variables are reset since the ball hasn't hit anything yet
        lastHitOne = -1;
        lastHitTwo = -1;
        //random direction and speed is assigned to both balls, if there are two
        //the directions must always be opposite x directions since it would be unfair for both to go in the same direction
        //this sets x direction
        if (((Math.random() - 0.5) * 1) >= 0) {
            ballOne.setxDir((int) (4 + (Math.random() * 3)));
        } else {
            ballOne.setxDir((int) ((Math.random() * (-3)) - 4));
        }
        //this sets y direction
        if (((Math.random() - 0.5) * 1) < 0) {
            ballOne.setyDir((int) (4 + (Math.random() * 3)));
        } else {
            ballOne.setyDir((int) ((Math.random() * (-3)) - 4));
        }

        //set of if statements that make sure the balls don't all travel towards one paddle once they are reset
        if(numberOfBalls==2){
            //for two player pong, sets the x direction opposite of ball one, and also sets random y direction and speed
            if(numberOfPlayers==2) {
                ballTwo.setxDir(ballOne.getxDir() * (-1));
                if ((Math.random() - 0.5) * 1 >= 0) {
                    ballTwo.setyDir(ballOne.getyDir());
                } else {
                    ballTwo.setyDir(ballOne.getyDir() * (-1));
                }
            }else if(numberOfPlayers==1){
                //for single player, sets y direction opposite and x direction random
                ballTwo.setyDir(ballOne.getyDir() * (-1));
                if ((Math.random() - 0.5) * 1 >= 0) {
                    ballTwo.setxDir(ballOne.getyDir());
                } else {
                    ballTwo.setxDir(ballOne.getyDir() * (-1));
                }
            }
            //places ball two in the middle of the screen
            ballTwo.setBallX(windowWidth/2);
            ballTwo.setBallY(windowHeight/2);
        }

        //places ball one in the middle of the screen
        ballOne.setBallX(windowWidth / 2);
        ballOne.setBallY(windowHeight / 2);

        //repaints the screen
        graph.repaint();
    }

    //graphics class which draws everything on the screen
    private class GameGraphics extends Component {
        //paint method which draws everything on the screen
        public void paint(Graphics g) {
            //the first time in the paint method, place the balls in the middle
            if(firstTime){
                ballOne.setBallX(getWidth() / 2);
                ballOne.setBallY(getHeight() / 2);
                if(numberOfBalls==2) {
                    ballTwo.setBallX(getWidth() / 2);
                    ballTwo.setBallY(getHeight() / 2);
                }
                firstTime=false;
            }
            //creates a new graphics object and sets the window width and height accordingly
            Graphics2D g2 = (Graphics2D) g;
            windowHeight = getHeight();
            windowWidth = getWidth();

            //if there are two players, set the background color to black, unless the user has chosen the light theme
            if(numberOfPlayers==2) {
                g2.setColor(Color.black);
                if(whiteTheme){
                    g2.setColor(Color.white);
                }
            }else if(numberOfPlayers==1){
                //if there is one player, set the background according to the user's score
                if(run.getMainScore()<5) {
                    g2.setColor(Color.black);
                }else if(run.getMainScore()>=5&&run.getMainScore()<10){
                    g2.setColor(Color.blue);
                }else if(run.getMainScore()>=10&&run.getMainScore()<15){
                    g2.setColor(Color.red);
                }else if(run.getMainScore()>=15){
                    g2.setColor(Color.white);
                }
            }
            //creates the background rectangle with the above color
            g2.fillRect(0, 0, getWidth(), getHeight());

            //sets the color to white by default, but it the player's score is above 15 or the theme is light, set it to black
            g2.setColor(Color.WHITE);
            if((numberOfPlayers==1&&(run.getMainScore()>=15)||whiteTheme)){
                g2.setColor(Color.black);
            }
            //draws the first ball according to its current position
            g2.fillArc(ballOne.getBallX(), ballOne.getBallY(), ballOne.getRadius() * 2, ballOne.getRadius() * 2, 0, 360);

            //same as above, but for the second ball, if there is one
            if(numberOfBalls==2){
                g2.fillArc(ballTwo.getBallX(), ballTwo.getBallY(), ballTwo.getRadius() * 2, ballTwo.getRadius() * 2, 0, 360);
            }

            if(numberOfPlayers==2) {
                //draws the paddles on the left and right according to their positions and the color initialized above
                g2.fillRect(left.getxPos(), left.getyPos(), left.getWidth(), left.getHeight());
                g2.fillRect(right.getxPos(), right.getyPos(), right.getWidth(), right.getHeight());

                //draws the two scores in the top corners
                g2.setFont(new Font("Arial", Font.PLAIN, 80));
                g2.drawString(run.getLeftScore() + "", (getWidth() / 2) - 100, 80);
                g2.drawString(run.getRightScore() + "", (getWidth() / 2) + 55, 80);

                //draws the line in the middle to separate the two sides
                g2.fillRect((getWidth() / 2) - 5, 0, 10, getHeight());
            }else if(numberOfPlayers==1){
                //if there is one player, draw the paddle at the bottom and draw the score in the top right
                g2.fillRect(bottom.getxPos(),bottom.getyPos(),bottom.getWidth(),bottom.getHeight());
                g2.setFont(new Font("Arial", Font.PLAIN, 80));
                g2.drawString(run.getMainScore()+"",windowWidth-120,80);
            }
        }
    }

    //timer class for one player pong
    private class TaskOneP extends TimerTask{
        //main run method running at 60 fps
        public void run(){
            //repaints the components on the screen
            graph.repaint();

            //variables for both balls which determine its current position on the screen but in different places
            int bottomBallOne = ballOne.getBallY()+(ballOne.getRadius()*2);
            int topBallOne = ballOne.getBallY();
            int leftBallOne = ballOne.getBallX();
            int rightBallOne = ballOne.getBallX()+(ballOne.getRadius()*2);

            int bottomBallTwo = ballTwo.getBallY()+(ballTwo.getRadius()*2);
            int topBallTwo = ballTwo.getBallY();
            int leftBallTwo = ballTwo.getBallX();
            int rightBallTwo = ballTwo.getBallX()+(ballTwo.getRadius()*2);

            //changes the x and y coordinates of the first ball based on its direction
            ballOne.setBallX(ballOne.getBallX() + ballOne.getxDir());
            ballOne.setBallY(ballOne.getBallY() + ballOne.getyDir());

            //changes the x and y coordinates oif the second ball if there are two
            if(numberOfBalls==2){
                ballTwo.setBallX(ballTwo.getBallX() + ballTwo.getxDir());
                ballTwo.setBallY(ballTwo.getBallY() + ballTwo.getyDir());
            }

            //if the left of the paddle is at 0 or less than zero, it means that it is hitting the left wall
            //boolean that represents this is set accordingly
            if(bottom.getxPos()<=0){
                bottomLeft=true;
            }else{
                bottomLeft=false;
            }

            //if the right of the paddle is equal to or greater than the width, it means that it is hitting the right wall
            //boolean that represents this is set accordingly
            if(bottom.getxPos()+bottom.getWidth()>=windowWidth){
                bottomRight=true;
            }else{
                bottomRight=false;
            }

            //booleans the current movement of the paddle are set using the current velocities
            goingLeft=bottom.getxDir()<0;
            goingRight=bottom.getxDir()>0;

            //if the paddle has not hit any walls, hit the right wall but is moving left, or hit the left wall and is moving right
            //it can move in the direction currently set. This prevents the paddle from going off the screen
            if((!bottomRight&&!bottomLeft)||(bottomRight&&goingLeft)||(bottomLeft&&goingRight)) {
                bottom.setxPos(bottom.getxPos() + bottom.getxDir());
            }

            //if the left of the ball is left or equal to the left wall and it is going towards it invert the x direction
            //this bounces the ball off the wall and prevents it from getting stuck in the wall
            if(leftBallOne<=0&&(lastHitOne!=2&&ballOne.getxDir()<0)){
                ballOne.invertxDir();
                lastHitOne=2;
            }

            //same concept as above but with the right wall
            if(rightBallOne>=windowWidth&&(lastHitOne!=3&&ballOne.getxDir()>0)){
                ballOne.invertxDir();
                lastHitOne=3;
            }

            //same as above but for the top wall
            if(topBallOne<=0&&(lastHitOne!=1&&ballOne.getyDir()<0)){
                ballOne.invertyDir();
                lastHitOne = 1;
            }

            //this is for the paddle
            //if the bottom of the ball is equal to or past the paddle, but not all the way past it
            //and the left of the ball is right of the left of the paddle and the right of the ball is left than the right of the paddle
            //then invert the y direction and increase the single player score
            if((bottomBallOne>=bottom.getyPos()&&bottomBallOne<=bottom.getyPos()+bottom.getHeight())&&(leftBallOne>=bottom.getxPos()&&rightBallOne<=bottom.getxPos()+bottom.getWidth())){
                if(lastHitOne!=0&&ballOne.getyDir()>0){
                    ballOne.invertyDir();
                    run.addPointToMain();
                    lastHitOne=0;
                    //if the paddle is moving right, increase the x speed, if it is moving left, increase y speed, and if it is not moving, increase both
                    if(bottom.getxDir()>0){
                        ballOne.increaseSpeed(true,false);
                    }else if(bottom.getxDir()<0){
                        ballOne.increaseSpeed(false,true);
                    }else{
                        ballOne.increaseSpeed(true,true);
                    }
                }
            }

            //same if statements and methods inside of them, except now they are using the properties of the second ball and changing properties of the second ball
            if(leftBallTwo<=0&&(lastHitTwo!=2&&ballTwo.getxDir()<0)){
                ballTwo.invertxDir();
                lastHitTwo=2;
            }

            if(rightBallTwo>=windowWidth&&(lastHitTwo!=3&&ballTwo.getxDir()>0)){
                ballTwo.invertxDir();
                lastHitTwo=3;
            }

            if(topBallTwo<=0&&(lastHitTwo!=1&&ballTwo.getyDir()<0)){
                ballTwo.invertyDir();
                lastHitTwo = 1;
            }

            if((bottomBallTwo>=bottom.getyPos()&&bottomBallTwo<=bottom.getyPos()+bottom.getHeight())&&(leftBallTwo>=bottom.getxPos()&&rightBallTwo<=bottom.getxPos()+bottom.getWidth())){
                if(lastHitTwo!=0&&ballTwo.getyDir()>0){
                    ballTwo.invertyDir();
                    run.addPointToMain();
                    lastHitTwo=0;
                    if(bottom.getxDir()>0){
                        ballTwo.increaseSpeed(true,false);
                    }else if(bottom.getxDir()<0){
                        ballTwo.increaseSpeed(false,true);
                    }else{
                        ballTwo.increaseSpeed(true,true);
                    }
                }
            }

            //if either of the balls go below the screen, reset the points, as the user lost, and reset the ball, or balls back in the middle
            if(topBallOne>=windowHeight){
                run.resetPoints();
                setRandomDirectionAndPosition();
            }

            if(topBallTwo>=windowHeight){
                run.resetPoints();
                setRandomDirectionAndPosition();
            }
        }
    }

    //timer class for the two player pong
    private class Task extends TimerTask {
        //returns a boolean that determines if the passed in paddle is going up, or not going up
        public boolean goingUp(PaddleOverchuk p){
            if(p.getyDir()<0){
                return true;
            }else{
                return false;
            }
        }

        //determines the change for a paddle based off of the passed in number: 0 for left paddle, 1 for right paddle
        public void determineChange(int x){
            //booleans that determine the current position of the paddle passed in and its direction
            boolean top;
            boolean bottom;
            boolean up;
            boolean down;

            //same concept as it is in the above class, but since there are two panels, it is done inside a function
            if((goingUp(left)&&x==0)||(goingUp(right)&&x==1)){
                up=true;
                down=false;
            }else{
                down=true;
                up=false;
            }

            if(x==0){
                top=leftTop;
                bottom=leftBottom;
            }else{
                top=rightTop;
                bottom=rightBottom;
            }

            //this makes sure that the paddles stay inside the screen and can move without gettting stuck
            if((!top&&!bottom)||(top&&down)||(bottom&&up)) {
                if(x==0) {
                    left.setyPos(left.getyPos() + left.getyDir());
                }else{
                    right.setyPos(right.getyPos() + right.getyDir());
                }
            }
        }

        //this finds, and increases the speed for either ball, based off of the paddle it just hit and the direction that paddle was going
        public void findIncreaseSpeed(PaddleOverchuk p, int ball){
            //boolean that determines whether it is the first ball, if not, it is the second ball
            boolean bOne;
            if(ball == 1){
                bOne = true;
            }else{
                bOne = false;
            }
            //if the paddle is going down, increase the y speed
            if(p.getyDir()>0){
                if(bOne) {
                    ballOne.increaseSpeed(false, true);
                }else{
                    ballTwo.increaseSpeed(false,true);
                }
            }else if(p.getyDir()<0){
                //if the paddle is going up, increase the x speed
                if(bOne) {
                    ballOne.increaseSpeed(true, false);
                }else{
                    ballTwo.increaseSpeed(true,false);
                }
            }else{
                //if it stationary, increase both speeds
                if(bOne) {
                    ballOne.increaseSpeed(true, true);
                }else{
                    ballTwo.increaseSpeed(true,true);
                }
            }
        }

        //main run method running at 60 fps
        public void run() {
            //repaints the screen
            graph.repaint();

            //variables that represent the left and right of each ball
            int leftBallOne = ballOne.getBallX()-ballOne.getRadius();
            int rightBallOne = ballOne.getBallX()+ballOne.getRadius();

            int leftBallTwo = ballTwo.getBallX()-ballTwo.getRadius();
            int rightBallTwo = ballTwo.getBallX()+ballTwo.getRadius();

            //ball one and two are changed according to their current direction
            ballOne.setBallX(ballOne.getBallX() + ballOne.getxDir());
            ballOne.setBallY(ballOne.getBallY() + ballOne.getyDir());

            if(numberOfBalls==2){
                ballTwo.setBallX(ballTwo.getBallX() + ballTwo.getxDir());
                ballTwo.setBallY(ballTwo.getBallY() + ballTwo.getyDir());
            }

            //the booleans for whether the left and right paddles have ran into the top or bottom walls
            if(left.getyPos()<=0){
                leftTop=true;
            }else{
                leftTop=false;
            }

            if(left.getyPos()+left.getHeight()>=windowHeight){
                leftBottom=true;
            }else{
                leftBottom=false;
            }

            if(right.getyPos()<=0){
                rightTop=true;
            }else{
                rightTop=false;
            }

            if(right.getyPos()+right.getHeight()>=windowHeight){
                rightBottom=true;
            }else{
                rightBottom=false;
            }

            //using the method above, the paddles move according to their direction
            determineChange(0);
            determineChange(1);

            //if statement that inverts the x of the first ball if it hits the left paddle using the same basics as above
            if((leftBallOne+ballOne.getRadius()<=left.getxPos()+left.getWidth()&&leftBallOne+ballOne.getRadius()>=left.getxPos())&&(ballOne.getBallY()>=left.getyPos()&&ballOne.getBallY()<=left.getyPos()+left.getHeight())){
                if(lastHitOne!=0) {
                    ballOne.invertxDir();
                    findIncreaseSpeed(left,1);
                    lastHitOne = 0;
                }
            }

            //if statement that inverts the x of the second ball if it hits off the left paddle
            if(numberOfBalls==2&&(leftBallTwo+ballTwo.getRadius()<=left.getxPos()+left.getWidth()&&leftBallTwo+ballTwo.getRadius()>=left.getxPos())&&(ballTwo.getBallY()>=left.getyPos()&&ballTwo.getBallY()<=left.getyPos()+left.getHeight())){
                if(lastHitTwo!=0){
                    ballTwo.invertxDir();
                    findIncreaseSpeed(left,2);
                    lastHitTwo=0;
                }
            }

            //if statement that inverts the x of the first ball if it hits off the right paddle
            if((rightBallOne+ballOne.getRadius()>=right.getxPos()&&rightBallOne+ballOne.getRadius()<=right.getxPos()+right.getWidth())&&(ballOne.getBallY()>=right.getyPos()&&ballOne.getBallY()<=right.getyPos()+right.getHeight())){
                if(lastHitOne!=1) {
                    ballOne.invertxDir();
                    findIncreaseSpeed(right,1);
                    lastHitOne = 1;
                }
            }

            //if statement that inverts the x of the second ball if it hits off the right paddle
            if(numberOfBalls==2&&(rightBallTwo+ballTwo.getRadius()>=right.getxPos()&&rightBallTwo+ballTwo.getRadius()<=right.getxPos()+right.getWidth())&&(ballTwo.getBallY()>=right.getyPos()&&ballTwo.getBallY()<=right.getyPos()+right.getHeight())){
                if(lastHitTwo!=1){
                    ballTwo.invertxDir();
                    findIncreaseSpeed(right,2);
                    lastHitTwo=1;
                }
            }

            //if the ball goes past either the right or the left wall, add a point to the corresponding player and reset the balls back in the middle
            if(rightBallOne<0||(numberOfBalls==2&&rightBallTwo<0)){
                run.addPointToRight();
                setRandomDirectionAndPosition();
            }else if(leftBallOne>windowWidth||(numberOfBalls==2&&leftBallTwo>windowWidth)){
                run.addPointToLeft();
                setRandomDirectionAndPosition();
            }

            //if the second ball is equal or greater than the bottom wall or is less than or equal then the top, invert the y to bounce it off
            if(numberOfBalls==2) {
                if (ballTwo.getBallY() + (ballTwo.getRadius() * 2) >= windowHeight || ballTwo.getBallY() <= 0) {
                    ballTwo.invertyDir();
                }
            }

            //same as the second ball, the first ball will bounce off the bottom and top walls if it is equal to them or goes past them
            if (ballOne.getBallY() + (ballOne.getRadius() * 2) >= windowHeight || ballOne.getBallY() <= 0) {
                ballOne.invertyDir();
            }
        }
    }
}
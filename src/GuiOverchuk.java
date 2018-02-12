import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class GuiOverchuk extends JFrame {

    private Timer timer;
    private Timer timerOneP;

    private boolean leftTop=false;
    private boolean leftBottom=false;
    private boolean rightTop=false;
    private boolean rightBottom=false;

    private boolean bottomLeft=false;
    private boolean bottomRight=false;

    private boolean whiteTheme=false;

    private static int numberOfBalls=1;
    private static int numberOfPlayers=1;

    private static int lastHitOne = -1;
    private static int lastHitTwo = -1;

    private static int windowWidth;
    private static int windowHeight;

    private static final int PADDLE_SPEED = 15;

    private boolean firstTime = true;

    private static GameGraphics graph;
    private static BallOverchuk ballOne;
    private static BallOverchuk ballTwo;
    private static MainRunnerOverchuk run = new MainRunnerOverchuk();

    private PaddleOverchuk left;
    private PaddleOverchuk right;
    private PaddleOnePOverchuk bottom;

    private String[] options = {"One","Two"};
    private String[] themes = {"Dark","Light"};

    private String[] pause = {"Resume","Restart","Exit"};

    public GuiOverchuk() {
        setTitle("Welcome to Pong!");
        setBackground(Color.BLACK);
        setSize(1800, 1080);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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

        graph = new GameGraphics();
        add(graph);

        ballOne = new BallOverchuk();
        ballTwo = new BallOverchuk();

        left = new PaddleOverchuk(20, 992);
        right = new PaddleOverchuk(getWidth() - 80, 992);
        bottom = new PaddleOnePOverchuk(942,1768);

        setRandomDirectionAndPosition();

        if(numberOfPlayers==2) {
            timer = new Timer("animation");
            timer.scheduleAtFixedRate(new Task(), 150, (1000 / 60));
        }else if(numberOfPlayers==1){
            timerOneP=new Timer("animation");
            timerOneP.scheduleAtFixedRate(new TaskOneP(),150,(1000/60));
        }

        addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 38) {
                    right.setyDir(-PADDLE_SPEED);
                }else if (e.getKeyCode() == 40) {
                    right.setyDir(PADDLE_SPEED);
                }
                if (e.getKeyCode() == 87) {
                    left.setyDir(-PADDLE_SPEED);
                }else if (e.getKeyCode() == 83) {
                    left.setyDir(PADDLE_SPEED);
                }

                if(e.getKeyCode()==37){
                    bottom.setxDir(-PADDLE_SPEED);
                }else if(e.getKeyCode()==39){
                    bottom.setxDir(PADDLE_SPEED);
                }

                if(e.getKeyCode()==27){
                    if(numberOfPlayers==2) {
                        pause();
                        int choice = JOptionPane.showOptionDialog(null, "Game Paused", "Paused", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, pause, "Resume");
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
                        if (choice == JOptionPane.CLOSED_OPTION) {
                            resume();
                        }
                    }else{
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

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 38) {
                    right.setyDir(0);
                }else if (e.getKeyCode() == 40) {
                    right.setyDir(0);
                }
                if (e.getKeyCode() == 87) {
                    left.setyDir(0);
                }else if (e.getKeyCode() == 83) {
                    left.setyDir(0);
                }
                if(e.getKeyCode()==37){
                    bottom.setxDir(0);
                }else if(e.getKeyCode()==39){
                    bottom.setxDir(0);
                }
            }

            public void keyTyped(KeyEvent e) {

            }
        });

        setVisible(true);
    }

    public void pause() {
        timer.cancel();
    }

    public void pauseOneP() {
        timerOneP.cancel();
    }

    public void resume() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new Task(), 0, (1000/60) );
    }

    public void resumeOneP() {
        timerOneP = new Timer();
        timerOneP.scheduleAtFixedRate(new TaskOneP(), 0, (1000/60) );
    }

    public static void setRandomDirectionAndPosition() {
        lastHitOne = -1;
        lastHitTwo = -1;
        if (((Math.random() - 0.5) * 1) >= 0) {
            ballOne.setxDir((int) (4 + (Math.random() * 3)));
        } else {
            ballOne.setxDir((int) ((Math.random() * (-3)) - 4));
        }
        if (((Math.random() - 0.5) * 1) < 0) {
            ballOne.setyDir((int) (4 + (Math.random() * 3)));
        } else {
            ballOne.setyDir((int) ((Math.random() * (-3)) - 4));
        }

        if(numberOfBalls==2){
            if(numberOfPlayers==2) {
                ballTwo.setxDir(ballOne.getxDir() * (-1));
                if ((Math.random() - 0.5) * 1 >= 0) {
                    ballTwo.setyDir(ballOne.getyDir());
                } else {
                    ballTwo.setyDir(ballOne.getyDir() * (-1));
                }
            }else if(numberOfPlayers==1){
                ballTwo.setyDir(ballOne.getyDir() * (-1));
                if ((Math.random() - 0.5) * 1 >= 0) {
                    ballTwo.setxDir(ballOne.getyDir());
                } else {
                    ballTwo.setxDir(ballOne.getyDir() * (-1));
                }
            }
            ballTwo.setBallX(windowWidth/2);
            ballTwo.setBallY(windowHeight/2);
        }

        ballOne.setBallX(windowWidth / 2);
        ballOne.setBallY(windowHeight / 2);

        graph.repaint();
    }

    private class GameGraphics extends Component {
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            if (firstTime) {
                ballOne.setBallX(getWidth() / 2);
                ballOne.setBallY(getHeight() / 2);
                if(numberOfBalls==2) {
                    ballTwo.setBallX(getWidth() / 2);
                    ballTwo.setBallY(getHeight() / 2);
                }
                firstTime = false;
            }

            windowHeight = getHeight();
            windowWidth = getWidth();

            if(numberOfPlayers==2) {
                g2.setColor(Color.black);
                if(whiteTheme){
                    g2.setColor(Color.white);
                }
            }else if(numberOfPlayers==1){
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
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(Color.WHITE);
            if((numberOfPlayers==1&&run.getMainScore()>=15)||whiteTheme){
                g2.setColor(Color.black);
            }
            g2.fillArc(ballOne.getBallX(), ballOne.getBallY(), ballOne.getRadius() * 2, ballOne.getRadius() * 2, 0, 360);

            if(numberOfBalls==2){
                g2.setColor(Color.white);
                if((numberOfPlayers==1&&run.getMainScore()>=15)||whiteTheme){
                    g2.setColor(Color.BLACK);
                }
                g2.fillArc(ballTwo.getBallX(), ballTwo.getBallY(), ballTwo.getRadius() * 2, ballTwo.getRadius() * 2, 0, 360);
            }

            if(numberOfPlayers==2) {
                if(whiteTheme){
                    g2.setColor(Color.black);
                }
                g2.fillRect(left.getxPos(), left.getyPos(), left.getWidth(), left.getHeight());
                g2.fillRect(right.getxPos(), right.getyPos(), right.getWidth(), right.getHeight());

                g2.setFont(new Font("Arial", Font.PLAIN, 80));
                g2.drawString(run.getLeftScore() + "", (getWidth() / 2) - 100, 80);
                g2.drawString(run.getRightScore() + "", (getWidth() / 2) + 55, 80);

                g2.fillRect((getWidth() / 2) - 5, 0, 10, getHeight());
            }else if(numberOfPlayers==1){
                if(run.getMainScore()>=30){
                    g2.setColor(Color.black);
                }
                g2.fillRect(bottom.getxPos(),bottom.getyPos(),bottom.getWidth(),bottom.getHeight());
                g2.setFont(new Font("Arial", Font.PLAIN, 80));
                g2.drawString(run.getMainScore()+"",windowWidth-120,80);
            }
        }
    }

    private class TaskOneP extends TimerTask{
        public void run(){
            graph.repaint();

            int bottomBallOne = ballOne.getBallY()+(ballOne.getRadius()*2);
            int topBallOne = ballOne.getBallY();
            int leftBallOne = ballOne.getBallX();
            int rightBallOne = ballOne.getBallX()+(ballOne.getRadius()*2);

            int bottomBallTwo = ballTwo.getBallY()+(ballTwo.getRadius()*2);
            int topBallTwo = ballTwo.getBallY();
            int leftBallTwo = ballTwo.getBallX();
            int rightBallTwo = ballTwo.getBallX()+(ballTwo.getRadius()*2);

            ballOne.setBallX(ballOne.getBallX() + ballOne.getxDir());
            ballOne.setBallY(ballOne.getBallY() + ballOne.getyDir());

            if(numberOfBalls==2){
                ballTwo.setBallX(ballTwo.getBallX() + ballTwo.getxDir());
                ballTwo.setBallY(ballTwo.getBallY() + ballTwo.getyDir());
            }

            if(bottom.getxPos()<=0){
                bottomLeft=true;
            }else{
                bottomLeft=false;
            }

            if(bottom.getxPos()+bottom.getWidth()>=windowWidth){
                bottomRight=true;
            }else{
                bottomRight=false;
            }

            boolean goingLeft=bottom.getxDir()<0;
            boolean goingRight=bottom.getxDir()>0;

            if((!bottomRight&&!bottomLeft)||(bottomRight&&goingLeft)||(bottomLeft&&goingRight)) {
                bottom.setxPos(bottom.getxPos() + bottom.getxDir());
            }

            if(leftBallOne<=0&&(lastHitOne!=2&&ballOne.getxDir()<0)){
                ballOne.invertxDir();
                lastHitOne=2;
            }

            if(rightBallOne>=windowWidth&&(lastHitOne!=3&&ballOne.getxDir()>0)){
                ballOne.invertxDir();
                lastHitOne=3;
            }

            if(topBallOne<=0&&(lastHitOne!=1&&ballOne.getyDir()<0)){
                ballOne.invertyDir();
                lastHitOne = 1;
            }

            if((bottomBallOne>=bottom.getyPos()&&bottomBallOne<=bottom.getyPos()+bottom.getHeight())&&(leftBallOne>=bottom.getxPos()&&rightBallOne<=bottom.getxPos()+bottom.getWidth())){
                if(lastHitOne!=0&&ballOne.getyDir()>0){
                    ballOne.invertyDir();
                    run.addPointToMain();
                    lastHitOne=0;
                    if(bottom.getxDir()>0){
                        ballOne.increaseSpeed(true,false);
                    }else if(bottom.getxDir()<0){
                        ballOne.increaseSpeed(false,true);
                    }else{
                        ballOne.increaseSpeed(true,true);
                    }
                }
            }

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

    private class Task extends TimerTask {
        public boolean goingUp(PaddleOverchuk p){
            if(p.getyDir()<0){
                return true;
            }else{
                return false;
            }
        }

        public void determineChange(int x){
            boolean top;
            boolean bottom;
            boolean up;
            boolean down;

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

            if((!top&&!bottom)||(top&&down)||(bottom&&up)) {
                if(x==0) {
                    left.setyPos(left.getyPos() + left.getyDir());
                }else{
                    right.setyPos(right.getyPos() + right.getyDir());
                }
            }
        }

        public void findIncreaseSpeed(PaddleOverchuk p, int ball){
            boolean bOne;
            if(ball == 1){
                bOne = true;
            }else{
                bOne = false;
            }
            if(p.getyDir()>0){
                if(bOne) {
                    ballOne.increaseSpeed(false, true);
                }else{
                    ballTwo.increaseSpeed(false,true);
                }
            }else if(p.getyDir()<0){
                if(bOne) {
                    ballOne.increaseSpeed(true, false);
                }else{
                    ballTwo.increaseSpeed(true,false);
                }
            }else{
                if(bOne) {
                    ballOne.increaseSpeed(true, true);
                }else{
                    ballTwo.increaseSpeed(true,true);
                }
            }
        }

        public void run() {
            graph.repaint();

            int leftBallOne = ballOne.getBallX()-ballOne.getRadius();
            int rightBallOne = ballOne.getBallX()+ballOne.getRadius();

            int leftBallTwo = ballTwo.getBallX()-ballTwo.getRadius();
            int rightBallTwo = ballTwo.getBallX()+ballTwo.getRadius();

            ballOne.setBallX(ballOne.getBallX() + ballOne.getxDir());
            ballOne.setBallY(ballOne.getBallY() + ballOne.getyDir());

            if(numberOfBalls==2){
                ballTwo.setBallX(ballTwo.getBallX() + ballTwo.getxDir());
                ballTwo.setBallY(ballTwo.getBallY() + ballTwo.getyDir());
            }

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

            determineChange(0);
            determineChange(1);

            if((leftBallOne+ballOne.getRadius()<=left.getxPos()+left.getWidth()&&leftBallOne+ballOne.getRadius()>=left.getxPos())&&(ballOne.getBallY()>=left.getyPos()&&ballOne.getBallY()<=left.getyPos()+left.getHeight())){
                if(lastHitOne!=0) {
                    ballOne.invertxDir();
                    findIncreaseSpeed(left,1);
                    lastHitOne = 0;
                }
            }

            if(numberOfBalls==2&&(leftBallTwo+ballTwo.getRadius()<=left.getxPos()+left.getWidth()&&leftBallTwo+ballTwo.getRadius()>=left.getxPos())&&(ballTwo.getBallY()>=left.getyPos()&&ballTwo.getBallY()<=left.getyPos()+left.getHeight())){
                if(lastHitTwo!=0){
                    ballTwo.invertxDir();
                    findIncreaseSpeed(left,2);
                    lastHitTwo=0;
                }
            }

            if((rightBallOne+ballOne.getRadius()>=right.getxPos()&&rightBallOne+ballOne.getRadius()<=right.getxPos()+right.getWidth())&&(ballOne.getBallY()>=right.getyPos()&&ballOne.getBallY()<=right.getyPos()+right.getHeight())){
                if(lastHitOne!=1) {
                    ballOne.invertxDir();
                    findIncreaseSpeed(right,1);
                    lastHitOne = 1;
                }
            }

            if(numberOfBalls==2&&(rightBallTwo+ballTwo.getRadius()>=right.getxPos()&&rightBallTwo+ballTwo.getRadius()<=right.getxPos()+right.getWidth())&&(ballTwo.getBallY()>=right.getyPos()&&ballTwo.getBallY()<=right.getyPos()+right.getHeight())){
                if(lastHitTwo!=1){
                    ballTwo.invertxDir();
                    findIncreaseSpeed(right,2);
                    lastHitTwo=1;
                }
            }

            if(rightBallOne<0||(numberOfBalls==2&&rightBallTwo<0)){
                run.addPointToRight();
                setRandomDirectionAndPosition();
            }else if(leftBallOne>windowWidth||(numberOfBalls==2&&leftBallTwo>windowWidth)){
                run.addPointToLeft();
                setRandomDirectionAndPosition();
            }

            if(numberOfBalls==2) {
                if (ballTwo.getBallY() + (ballTwo.getRadius() * 2) >= windowHeight || ballTwo.getBallY() <= 0) {
                    ballTwo.invertyDir();
                }
            }

            if (ballOne.getBallY() + (ballOne.getRadius() * 2) >= windowHeight || ballOne.getBallY() <= 0) {
                ballOne.invertyDir();
            }
        }
    }
}
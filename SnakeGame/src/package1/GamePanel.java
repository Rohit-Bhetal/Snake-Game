package package1;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH=600;
    static final int SCREEN_HEIGHT=600;
    static final int UNIT_SIZE=25;
    static final int GAME_UNIT=(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    int DELAY=100;
    final int x[] =new int[GAME_UNIT];
    final int y[]=new int[GAME_UNIT];
    int bodyParts=6;
    int pointsEaten;
    int pointX;
    int pointY;
    char direction='R';
    boolean running =false;
    Timer timer;
    Random random;
    GamePanel(){
        random =new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }

    /* Grid lines
     * for (int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){
            g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
        }
     */

    public void startGame() {
        newPoint();
        running=true;
        timer=new Timer(DELAY,this);
        timer.start();
    }

    public void newPoint(){

        pointX=random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        pointY=random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if (running){
            
        g.setColor(Color.red);
        g.fillOval(pointX,pointY,UNIT_SIZE,UNIT_SIZE);

        for (int i=0;i<bodyParts;i++){
            if(i==0){
                g.setColor(Color.green);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
            else{
                g.setColor(new Color(50,100,42));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

        }
        g.setColor(Color.white);
        g.setFont(new Font(Font.MONOSPACED,Font.BOLD,45));
        FontMetrics metrics=getFontMetrics(g.getFont());
        g.drawString("Score: "+pointsEaten,(SCREEN_HEIGHT-metrics.stringWidth("Score: "+pointsEaten))-10,SCREEN_HEIGHT-2);
        }else{
            gameOver(g);
        }
        
    }
    public void move(){
        for(int i=bodyParts;i>0;i--){
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        switch(direction){
            case 'U':
                y[0]=y[0]-UNIT_SIZE;
                break;
            case 'D':
                y[0]=y[0]+UNIT_SIZE;
                break;
            case 'R':
                x[0]=x[0]+UNIT_SIZE;
                break;
            case 'L':
                x[0]=x[0]-UNIT_SIZE;
                break;
        }


    }
    public void checkPoints(){
        if ((x[0]==pointX)&& (y[0]==pointY)){
            bodyParts++;
            pointsEaten++;
            newPoint();
        }

    }
    public void checkCollision(){
        for (int i=bodyParts;i>0;i--){
            if((x[0]==x[i])&& (y[0]==y[i])){
                running=false;
            }
        }
        //boundary collusiion
        if(x[0]<0){
            running=false;
        }
        if(y[0]<0){
            running=false;
        }
        if(x[0]>SCREEN_WIDTH){
            running=false;
        }
        if(y[0]>SCREEN_HEIGHT){
            running=false;
        }

        if(!running){
            timer.stop();
            gameOver(getGraphics());
        }

    }
    public void gameOver(Graphics g){
        //Game Over text
        g.setColor(Color.white);
        g.setFont(new Font(Font.MONOSPACED,Font.BOLD,75));
        FontMetrics metrics=getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_HEIGHT-metrics.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);
        g.setColor(Color.green);
        g.setFont(new Font(Font.MONOSPACED,Font.BOLD,50));
        g.drawString("Score: "+pointsEaten,(SCREEN_HEIGHT-metrics.stringWidth("Score: "+pointsEaten))/2,(SCREEN_HEIGHT+150)/2);
        
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (running){
                move();
                checkPoints();
                checkCollision();

            }
            repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()) {
                case (KeyEvent.VK_LEFT):
                    if(direction!='R'){
                        direction='L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction!='L'){
                        direction='R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction!='D'){
                        direction='U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction!='U'){
                        direction='D';
                    }
                    break;

            }
            

        }
    }
    
}

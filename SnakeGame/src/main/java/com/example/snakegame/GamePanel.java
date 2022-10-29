package com.example.snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH=600;
    static final int SCREEN_HEIGHT=600;
    static final int UNIT_SIZE=25;
    static final int GAME_UNITS=(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;//How many units we can fit on screen
    static final int DELAY=75;//timer higher the delay slower the game
    final int x[]=new int[GAME_UNITS];//2 arrays X any Y this are going to hold all the co-ordinates of all body parts
    final int y[]=new int[GAME_UNITS];
    int bodyParts=6;//initial bodyparts
    int applesEaten;//initial value will be 0
    int appleX;//x co-ordinate of apple and will randomly appear after each appleEaten
    int appleY;
    char direction='R';//initial direction of snake
    boolean running =false;
    Timer timer;
    Random random;

    GamePanel(){
        random=new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT)); //size preferred for game panel
        this.setBackground(Color.BLACK);//background color
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }
    public void startGame(){
        newApple();//to create new apple for us
        running=true;
        timer=new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        if(running){
        for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {//for loop to draw lines on screen like grid
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }
        g.setColor(Color.RED);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);//this much large the apple is.

        //to draw the head and body parts of snake
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {//we are dealing with head of snake
                g.setColor(Color.CYAN);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            } else {//for dealing the body of the snake
                g.setColor(new Color(45, 180, 0));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

            }
        }
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free",Font.BOLD,40));
            FontMetrics metrics=getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten,(SCREEN_WIDTH- metrics.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());
      }
        else{
           gameOver(g);
        }
    }
    public void newApple(){
        appleX=random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;//random apple at x-axis
        appleY=random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){//movement of snake with help of this method
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
                case 'L':
                    x[0]=x[0]-UNIT_SIZE;
                    break;
                case 'R':
                    x[0]=x[0]+UNIT_SIZE;
                    break;
            }
    }
    public void checkApple(){
        if((x[0]==appleX)&&(y[0]==appleY)){
            bodyParts++;//after eating body will increase
            applesEaten++;//apples eaten score
            newApple();//new apple will generate
        }
    }
    public void checkCollisions(){
        //checks if heads collides with body if it collides then game will be over
        for(int i=bodyParts;i>0;i--){
            if((x[0]==x[i])&&(y[0]==y[i])){
                running=false;
            }
        }
        //check if head touches the left border
        if(x[0]<0){
            running =false;
        }
        //checks if head touches the right border
        if(x[0]>SCREEN_WIDTH){
            running=false;
        }
        //checks if head touches top border
        if(y[0]<0){
            running=false;
        }
        //checks if head touches bottom border
        if(y[0]>SCREEN_HEIGHT){
            running=false;
        }
        if(!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        //score
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free",Font.BOLD,40));
        FontMetrics metrics1=getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten,(SCREEN_WIDTH- metrics1.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());
         //Game over text
        g.setColor(Color.RED);
            g.setFont(new Font("Ink Free",Font.BOLD,75));
            FontMetrics metrics2=getFontMetrics(g.getFont());
            g.drawString("Game Over",(SCREEN_WIDTH- metrics2.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
                switch(e.getKeyCode()){
                    case KeyEvent.VK_LEFT:
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener{
    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private Image dot;
    private Image apple;
    private int appleX;
    private int appleY;
    private int[] x = new int[ALL_DOTS];
    private int[][] borderXY = new int[21][21];
    private int[] borderY = new int[21];
    private int[] y = new int[ALL_DOTS];
    private int dots;
    private Timer timer;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;
    JButton restart;

    public GameField(){
        this.setLayout(null);
        setBackground(Color.black);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);

    }

    public void initGame(){
        left = false;
        right = true;
        up = false;
        down = false;
        inGame = true;
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i*DOT_SIZE;
            y[i] = 48;
        }
        timer = new Timer(250,this);
        timer.start();
        createApple();


    }

    public void createApple(){

        appleX = new Random().nextInt(20)*DOT_SIZE;

        appleY = new Random().nextInt(20)*DOT_SIZE;
                if (appleX == 0 || (appleY== 0) || appleX == 320 || (appleY== 320)) {
                    createApple();
                }

    }

    public void loadImages(){
        ImageIcon iia = new ImageIcon("src/apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("src/snake.png");
        dot = iid.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int x = 0; x <= SIZE; x+=16) {
            g.fillRect(x,0,16,16);
            g.fillRect(x,320,16,16);
            g.setColor(Color.gray);
        }
        for (int y = 0; y <= SIZE; y+=16) {
            g.fillRect(0,y,16,16);
            g.fillRect(320,y,16,16);
            g.setColor(Color.gray);

        }
        if(inGame){
            int score = dots - 3;
            g.drawString("Your score: " + score, 32, 32);
            g.setColor(Color.white);
            g.drawImage(apple,appleX,appleY,this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot,x[i],y[i],this);

            }
        } else{
            timer.stop();
            int score = dots - 3;
            String str = "Game Over";
            g.setColor(Color.white);
            restart = new JButton();
            restart.setText("Restart game");
            restart.setBounds(90,170,150,50);
            restart.addActionListener(new restartButtonListener());
            this.add(restart);
            g.drawString(str,135,SIZE/2);
            g.setColor(Color.gray);
            g.drawString("Your score: " + score,130,260);
        }
    }

    public void move(){
        for (int i = dots; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(left){
            x[0] -= DOT_SIZE;
        }
        if(right){
            x[0] += DOT_SIZE;
        } if(up){
            y[0] -= DOT_SIZE;
        } if(down){
            y[0] += DOT_SIZE;
        }
    }

    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            dots++;
            createApple();
        }
    }

    public void checkCollisions(){
        for (int i = dots; i >0 ; i--) {
            if(i>4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
        }

        if(x[0]>304){
            inGame = false;
        }
        if(x[0]<16){
            inGame = false;
        }
        if(y[0]>304){
            inGame = false;
        }
        if(y[0]<16){
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame){
            checkApple();
            checkCollisions();
            move();

        }
        repaint();
    }

    public class restartButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            initGame();
            remove(restart);
            revalidate();
            repaint();
        }
    }

    class FieldKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_LEFT && !right){
                left = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_RIGHT && !left){
                right = true;
                up = false;
                down = false;
            }

            if(key == KeyEvent.VK_UP && !down){
                right = false;
                up = true;
                left = false;
            }
            if(key == KeyEvent.VK_DOWN && !up){
                right = false;
                down = true;
                left = false;
            }
        }
    }

}
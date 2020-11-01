import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Snake extends JPanel{

private boolean end = false;

private int score = 0;

private int motion = 0, previousMotion = 0;

private Point apple = new Point(35,25);

private final Point startPoint = new Point(12,25);

private Point head;

private ArrayList<Point> body = new ArrayList<Point>();

private ArrayList<Integer> motionQueue = new ArrayList<Integer>();

private Color[][] terrain = new Color[50][50];

private Color[] colour = new Color[]{
new Color(0,255,0), new Color(0,0,0), new Color(143,143,143),
new Color(238,238,238), new Color(255,0,0), new Color(150,150,150)};

private Color scoreColour = new Color(238,238,238);

private boolean collide(int x, int y){
if(terrain[head.y + y][head.x + x] != colour[1] && terrain[head.y + y][head.x + x] != colour[4]){
return true;
}
else{
return false;
}
}

private boolean selfCollide(int x, int y){
boolean b = false;
for(int n = body.size()-1; n > 0; n--){
if(head.y + y == body.get(n).y && head.x + x == body.get(n).x){
b = true;
}
}
return b;
}

private void eaten(){
if(head.x == apple.x && head.y == apple.y){
apple = new Point(inSquare(1,48), inSquare(1,48));

for(int n = 0; n < body.size(); n++){
if((apple.x == body.get(n).x && apple.y == body.get(n).y) || (apple.x == head.x && apple.y == head.y)){
apple = new Point(inSquare(1,48), inSquare(1,48));
}
else{
break;
}
}

terrain[apple.y][apple.x] = colour[4];
++score;
eat();
}
}

private void across(int move){
if(!collide(move, 0) && !selfCollide(move, 0) && end == false){
head = new Point(head.x + move, head.y);
}
else{
end = true;
}
}

private void pole(int move){
if(!collide(0, move) && !selfCollide(0, move) && end == false){
head = new Point(head.x, head.y + move);
}
else{
end = true;
}
}

private void eat(){
body.add(new Point(body.get(body.size()-1).x, body.get(body.size()-1).y));
}

private void spawn(){
head = startPoint;
body.add(new Point(head.x - 1, head.y));
motionQueue.add(0);
previousMotion = 0;
terrain[head.y][head.x] = colour[0];
terrain[body.get(0).y][body.get(0).x] = colour[0];
terrain[apple.y][apple.x] = colour[4];
}

private void clear(){
if(end == false){
terrain[body.get(0).y][body.get(0).x] = colour[1];
terrain[head.y][head.x] = colour[1];
for(int n = body.size()-1; n > 0; n--){
terrain[body.get(n).y][body.get(n).x] = colour[1];
body.get(n).y = body.get(n-1).y;
body.get(n).x = body.get(n-1).x;
}
body.get(0).y = head.y;
body.get(0).x = head.x;
}
}

private void snake(){
if(end == false){
for(int n = body.size()-1; n > 0; n--){
terrain[body.get(n).y][body.get(n).x] = colour[0];
}
terrain[body.get(0).y][body.get(0).x] = colour[0];
terrain[head.y][head.x] = colour[0];
repaint();
}
else{
scoreColour = new Color(0,0,0);
for(int n = body.size()-1; n > 0; n--){
terrain[body.get(n).y][body.get(n).x] = colour[5];
}
terrain[body.get(0).y][body.get(0).x] = colour[5];
terrain[head.y][head.x] = colour[5];
repaint();
}
}

private void reset(){
for(int row = 0; row < 50; row++){
for(int column = 0; column < 50; column++){
if(row == 0 || row == 49 || column == 0 || column == 49){
terrain[column][row] = colour[2];
}
else{
terrain[column][row] = colour[1];
}
}
}
}

private boolean allow(int before, int now){
if((before == (2) && now == (3))||(before == (1) && now == (4)) || (before == (3) && now == (2))||(before == (4) && now == (1))){
return false;
}
else{
return true;
}
}

@Override
public void paintComponent(Graphics g){
g.setColor(colour[3]);
g.fillRect(590,50,80,40);

g.setColor(scoreColour);
g.setFont(new Font("", Font.PLAIN, 13));
g.drawString("Score: " + score, 585, 65);

for(int row = 0; row < 50; row++){
for(int column = 0; column < 50; column++){
g.setColor(terrain[column][row]);
g.fillRect(row * 10 + 50, column * 10 + 50, 10, 10);
}
}
}

private int inSquare(int min, int max){
return (int)((Math.random() * (max + 1 - min)) + min);
}

public static void main(String[] args){
final Snake snake = new Snake();
snake.setSize(680,625);
snake.reset();
snake.spawn();
snake.clear();
snake.snake();

JPanel panel = new JPanel();
panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
panel.setLayout(new BorderLayout());
panel.setFocusable(true);
panel.requestFocus();
panel.add(snake);

panel.addKeyListener(new KeyListener(){
public void keyTyped(KeyEvent e){
}

public void keyPressed(KeyEvent e){
switch(e.getKeyCode()){
case KeyEvent.VK_UP:
if(snake.allow(snake.previousMotion,1)){
snake.motion = 1;
snake.motionQueue.add(snake.motion);
snake.previousMotion = snake.motion;
}
break;

case KeyEvent.VK_LEFT:
if(snake.allow(snake.previousMotion,2)){
snake.motion = 2;
snake.motionQueue.add(snake.motion);
snake.previousMotion = snake.motion;
}
break;

case KeyEvent.VK_RIGHT:
if(snake.allow(snake.previousMotion,3)){
snake.motion = 3;
snake.motionQueue.add(snake.motion);
snake.previousMotion = snake.motion;
}
break;

case KeyEvent.VK_DOWN:
if(snake.allow(snake.previousMotion,4)){
snake.motion = 4;
snake.motionQueue.add(snake.motion);
snake.previousMotion = snake.motion;
}
break;

case KeyEvent.VK_W:
if(snake.allow(snake.previousMotion,1)){
snake.motion = 1;
snake.motionQueue.add(snake.motion);
snake.previousMotion = snake.motion;
}
break;

case KeyEvent.VK_A:
if(snake.allow(snake.previousMotion,2)){
snake.motion = 2;
snake.motionQueue.add(snake.motion);
snake.previousMotion = snake.motion;
}
break;
case KeyEvent.VK_D:
if(snake.allow(snake.previousMotion,3)){
snake.motion = 3;
snake.motionQueue.add(snake.motion);
snake.previousMotion = snake.motion;
}
break;

case KeyEvent.VK_S:
if(snake.allow(snake.previousMotion,4)){
snake.motion = 4;
snake.motionQueue.add(snake.motion);
snake.previousMotion = snake.motion;
}
break;
}
}

public void keyReleased(KeyEvent e) {
}
});

JFrame frame = new JFrame("Snake");
frame.setTitle("Snake");
frame.setSize(680,625);
frame.add(panel, BorderLayout.CENTER);
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.setVisible(true);

new Thread() {
@Override public void run(){
while (true){
try {
if(snake.motionQueue.get(0) != 0){
switch(snake.motionQueue.get(0)){
case 1:
snake.clear(); snake.pole(-1); snake.eaten(); snake.snake();
if(snake.motionQueue.size() > 1){
snake.motionQueue.remove(snake.motionQueue.get(0));
}
break;

case 2:
snake.clear(); snake.across(-1); snake.eaten(); snake.snake();
if(snake.motionQueue.size() > 1){
snake.motionQueue.remove(snake.motionQueue.get(0));
}
break;

case 3:
snake.clear(); snake.across(1); snake.eaten(); snake.snake();
if(snake.motionQueue.size() > 1){
snake.motionQueue.remove(snake.motionQueue.get(0));
}
break;

case 4:
snake.clear(); snake.pole(1); snake.eaten(); snake.snake();
if(snake.motionQueue.size() > 1){
snake.motionQueue.remove(snake.motionQueue.get(0));
}
break;
}
}
else{
switch(snake.motion){
case 1:
snake.clear(); snake.pole(-1); snake.eaten(); snake.snake();
if(snake.motionQueue.size() > 1){
snake.motionQueue.remove(snake.motionQueue.get(0));
}
break;

case 2:
snake.clear(); snake.across(-1); snake.eaten(); snake.snake();
if(snake.motionQueue.size() > 1){
snake.motionQueue.remove(snake.motionQueue.get(0));
}
break;

case 3:
snake.clear(); snake.across(1); snake.eaten(); snake.snake();
if(snake.motionQueue.size() > 1){
snake.motionQueue.remove(snake.motionQueue.get(0));
}
break;

case 4:
snake.clear(); snake.pole(1); snake.eaten(); snake.snake();
if(snake.motionQueue.size() > 1){
snake.motionQueue.remove(snake.motionQueue.get(0));
}
break;
}
}
Thread.sleep(45);
}catch(InterruptedException e){}
}
}
}.start();
}
}

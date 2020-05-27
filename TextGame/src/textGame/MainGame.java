package textGame;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import javax.swing.JFrame;

import enums.AreaType;
import enums.DirectionType;
import enums.ItemType;
import enums.SButtonType;
import items.Item;

/*
 * main method that ties everything together
 */
public class MainGame extends Canvas implements Runnable
{
    private static final long serialVersionUID = 1L;
    boolean running, invOpen, daytime, logOpen;
    int width,height,updates,frames,dragOX, dragOY, logOffset, logs, moveSpacing, standardTextSize, unreadMessages;
    long lastTime, nowTime;
    
    ColorScheme black, green, blue, red, orange, veil;
    SButton inventoryb, areaHeaderb,adesb, logUp, logDown, playerb, logb;
    public Area location;
    Thread thread;
    JFrame frame;
    Mouse mouse;
    public WorldMap map;
    Item[] inventory;
    SButton[] inventorybs,logbs,dirbs;
    ArrayList<SButton> buttons;
    ArrayList<String> eventLog;
    
    public MainGame() {
        width = 1280;
        height = 720;
        final Dimension size = new Dimension(width, height);
        setPreferredSize(size);
        frame = new JFrame();
        mouse = new Mouse();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }
    
    /*
     * initializes buttons and variables
     */
    public void init() {
        lastTime = System.nanoTime();
        updates = 0;
        frames = 0;
        logOffset = -1;
        logs = 0;
        unreadMessages = 0;
        moveSpacing = height(6);
        standardTextSize = width(2);
        buttons = new ArrayList<SButton>();
        veil = new ColorScheme(Color.BLACK);
        daytime = true;
        logOpen = true;
        green = new ColorScheme(new Color(22, 89, 19));
        black = new ColorScheme(Color.BLACK);
        blue = new ColorScheme(new Color(0,40,110));
        red = new ColorScheme(new Color(150,15,5));
        orange = new ColorScheme(new Color(110,110,0));
        eventLog = new ArrayList<String>();
        logbs = new SButton[8];
        for (int i = 0; i < logbs.length;i++) {
        	logbs[i] = new SButton(SButtonType.DESCRIPTION, width(1), height(41-i*5), "", black, height(3), i);
        	logbs[i].setVisible(false);
        }
        invOpen = true;
        inventory = new Item[10];
        inventorybs = new SButton[inventory.length];
        for (int i = 0; i < inventory.length;i++) {
        	inventorybs[i] = new SButton(SButtonType.INVENTORY_ITEM, width(3), height(93-(i+1)*5),"", blue, standardTextSize,i);
        	inventorybs[i].setVisible(false);
        }
        logb = new SButton(SButtonType.LOG, width(1), height(1),"Event Log", red, standardTextSize,0);
        inventoryb = new SButton(SButtonType.INVENTORY, width(3), height(93),"Inventory (0/10)", red, standardTextSize,0);
        playerb = new SButton(SButtonType.PLAYER, width(45), height(87),"Player", red, standardTextSize,0);
        areaHeaderb = new SButton(SButtonType.AREA_HEADER, width(35), height(3),"", green, width(4),0);
        adesb = new SButton(SButtonType.DESCRIPTION, width(35), height(15), "", black, standardTextSize,0);
        dirbs = new SButton[DirectionType.values().length];
        for (int i = 0; i < dirbs.length; i++) {
        	dirbs[i] = new SButton(SButtonType.LOCATION_LINK, width(45), height(87)-moveSpacing,"", green, standardTextSize, i);
        }
        dirbs[DirectionType.EAST.ordinal()].setLocation(width(60), height(87));
        dirbs[DirectionType.SOUTH.ordinal()].setLocation(-1, height(87)+moveSpacing);
        dirbs[DirectionType.WEST.ordinal()].setLocation(width(23), height(87));
        logUp = new SButton(SButtonType.LOG_UP, width(15), height(1),"↑", red, standardTextSize, 0);
        logUp.setVisible(false);
        logDown = new SButton(SButtonType.LOG_DOWN, width(15), height(45),"↓", red, standardTextSize, 0);
        logDown.setVisible(false);
         
        map = new WorldMap();
        /*
         * render once because bounds calculations for buttons are done in render method
         */
        render();
        /*
         * starts the game in the tutorial
         */
        loadArea(map.getArea(AreaType.INFINITE_DARKNESS));
        /*
         * while loop will create effect of dark screen that slowly brightens
         */
        int lastUpdate = updates;
        while (location.getLighting()<250) {
        	tick();
        	if (updates-lastUpdate>5) {
        		lastUpdate = updates;
        		location.setLighting(location.getLighting()+1);
            	setLighting();
        	}
        }
    }
    
    /*
     * determines lighting for screen depending on area
     */
    public void setLighting() {
    	int lighting = location.getLighting();
    	if (lighting==-1) {
    		if (daytime) {
    			lighting = 250;
    		} else {
    			lighting = 50;
    		}
    	}
    	if (lighting<150 && location.hasItem(ItemType.LIT_TORCH)) lighting = 150;
    	veil.ghostColor(255-lighting);
    }
    
    /*
     * loads buttons for area
     * called when area changes state, or player moves to new area
     */
    public void loadArea(Area a) {
    	SButton b;
    	location = a;
    	setLighting();
    	while (buttons.size()>0) {
    		buttons.get(0).setDragged(false);
    		buttons.remove(0);
    	}
    	buttons.add(inventoryb);
    	buttons.add(playerb);
    	areaHeaderb.setText(a.getName());
    	buttons.add(areaHeaderb);
    	for (DirectionType dir:DirectionType.values()) {
    		DirectionLink dl = a.getDLink(dir); 
    		if (dl!=null) {
    			b = dirbs[dir.ordinal()];
    			b.setText(map.getArea(dl.getLink()).getName());
    			b.setVisible(true);
    			buttons.add(b);
    		}
    	}
    	render();
    	dirbs[DirectionType.EAST.ordinal()].placeRight(playerb, moveSpacing);
    	dirbs[DirectionType.WEST.ordinal()].placeLeft(playerb, moveSpacing);
    	buttons.add(adesb);
    	adesb.setText(a.getDes());
    	render();
    	for (int i = 0; i < a.getPeopleSize(); i++) {
    		Person p = a.getPerson(i);
    		b = new SButton(SButtonType.PERSON, width(40),0, p.getName(), orange, standardTextSize,i);
    		b.placeDown(buttons.get(buttons.size()-1), standardTextSize/2);
    		if (p.getConverted()) b.setColors(red);
    		buttons.add(b);
    		render();
    		SButton b2 = new SButton(SButtonType.DESCRIPTION, 0, b.getBounds().y, p.getIdleDes(), black, standardTextSize,0);
    		b2.placeRight(b, standardTextSize/2);
    		buttons.add(b2);
    		render();
    	}
    	for (int i = 0; i < a.getItemsSize();i++) {
    		Item item = a.getItem(i);
    		String lead = "There is a";
    		if (item.getName().charAt(item.getName().length()-1)=='s') lead = "There are";
    		b = new SButton(SButtonType.DESCRIPTION, width(40), 0, lead, black, standardTextSize,0);
    		b.placeDown(buttons.get(buttons.size()-1), standardTextSize/2);
    		buttons.add(b);
    		render();
        	SButton b2 = new SButton(SButtonType.AREA_ITEM, b.getXRight()+standardTextSize/2, b.getBounds().y, a.getItem(i).getName(), blue, standardTextSize,i);
        	b2.placeRight(b, standardTextSize/2);
    		buttons.add(b2);
    		render();
    	}
    	for (SButton s:inventorybs) buttons.add(s);
    	for (SButton s:logbs) buttons.add(s);
    	buttons.add(logDown);
    	buttons.add(logUp);
    	buttons.add(logb);
    }
    
    /*
     * Adds message for player to read in their event log.
     * splits up message so each line has no more than 40 words+spaces
     * TODO: split up message more accurately based on screen length
     */
    public void addLog(String s) {
    	logOffset = eventLog.size()+1;
    	logs++;
    	SButton temp = new SButton(SButtonType.DESCRIPTION, 0, 0,"("+logs+") "+s, black, standardTextSize,0);
    	String[] words = temp.getTextArray();
    	int letters = 0;
    	String add = "";
		for (int i = 0; i < words.length; i++) {
			int w = words[i].length()+1;
			if (letters+w>40 && i>0) {
				eventLog.add(add);
				letters = w;
				add = words[i]+" ";
			} else {
				add+=words[i]+" ";
				letters+=w;
			}
		}
		eventLog.add(add);
		if (!logOpen) unreadMessages++;
    	refreshLog();
    }
    
    /*
     * determines whether up/down arrows need to be visible and if there are new messages the player has not read
     */
    public void refreshLog() {
    	if (unreadMessages>0) {
    		if (logOpen) {
    			unreadMessages = 0;
    			logb.setText("Event Log");
    		} else {
    			logb.setText("Event Log("+unreadMessages+")");
    		}
    	}
    	for (SButton s:logbs) {
    		int pointer = logOffset-s.getPointer()-1;
    		s.setVisible(pointer>-1 && pointer < eventLog.size() && logOpen);
    		if (s.getVisible()) s.setText(eventLog.get(pointer));
    	}
    	logUp.setVisible(logOffset>logbs.length&&logOpen);
    	logDown.setVisible(logOffset<eventLog.size()&&logOpen);
    }
    
    public int getInvSize() {
    	int invSize = 0;
		while (inventory[invSize]!=null) invSize++;
		return invSize;
    }
    
    public void updateInvText() {
    	if (invOpen) {
    		inventoryb.setText("Inventory ("+getInvSize()+"/"+inventory.length+")");
    	} else {
    		inventoryb.setText("Inventory (x)");
    	}
    }
    
    /*
     * Adds an item to the player's inventory at the next available spot
     * adds message that player gained item
     * returns false if there was no room in the inventory for another item
     */
    public boolean invAddItem(Item item) {
    	for (int i = 0; i < inventory.length; i++) {
    		if (inventory[i]==null) {
    			inventory[i]=item;
    			inventorybs[i].setText(item.getName());
    			inventorybs[i].setVisible(invOpen);
    			addLog("Gained item: " + item.getName());
    			return true;
    		}
    	}
    	return false;
    }
    
    /*
     * removes an item from the player's inventory
     * moves all inventory items that were above it down 1 spot so that there are no holes
     */
    public void invRemove(int pointer) {
    	inventory[pointer] = null;
    	while (pointer<inventory.length-1) {
    		inventory[pointer] = inventory[pointer+1];
    		if (inventory[pointer]!=null) inventorybs[pointer].setText(inventory[pointer].getName());
    		pointer++;
    	}
    	inventory[pointer] = null;
    	inventorybs[pointer].setVisible(false);
    	for (SButton s:inventorybs) {
    		if (inventory[s.getPointer()]==null) {
    			s.setVisible(false);
    		} else {
    			s.setVisible(invOpen);
    			s.setDragged(false);
    			s.setLocation(s.getOrigin().x, s.getOrigin().y);
    		}
    	}
    }
    
    /*
     * returns width in pixels based on what percentage of the screen it should be
     * the idea is to specify all dimensions based on percentages so that the game looks the same if the player resizes the screen
     */
    public int width(final int percentage) {
        return width * percentage / 100;
    }
    
    public int height(final int percentage) {
        return height * percentage / 100;
    }
    
    public synchronized void start() {
        running = true;
        (thread = new Thread(this, "Display")).start();
    }
    
    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        init();
        while (running) {
            tick();
        }
    }
    
    /* 
     * Main game loop
     * should update() every 60 seconds
     * returns true if there was an update on this tick
     */
    
    public boolean tick() {
        boolean rtrn = false;
        nowTime = System.nanoTime();
        if (nowTime - lastTime > 16666666L) {
            lastTime = nowTime;
            update();
            rtrn = true;
        }
        render();
        return rtrn;
    }
    
    /*
     * game logic done here
     * executes button clicks from the player
     */
    public void update() {
        updates++;
        for (SButton b:buttons) {
        	if (b.getReturning()) b.returnOrigin();
        }
        if (mouse.pressed) {
        	for (SButton b:buttons) {
        		if (b.getVisible() && b.getBounds().contains(new Point(mouse.x, mouse.y))) {
        			b.grabbed();
        			dragOX = b.getBounds().x-mouse.x;
        			dragOY = b.getBounds().y-mouse.y;
        			break;
        		}
        	}
        	mouse.pressed = false;
        }
        if (mouse.dragged) {
        	for (SButton b:buttons) {
        		if (b.getDragged()) {
        			b.setLocation(mouse.x+dragOX, mouse.y+dragOY);
        			for (SButton b2:buttons) {
        				if (b.getBounds().intersects(b2.getBounds())) {
        					b2.setHighlighted(true);
        				} else {
        					b2.setHighlighted(false);
        				}
        			}
        		}
        	}
        	mouse.dragged = false;
        }
        if (mouse.released) {
        	for (int i = 0; i < buttons.size(); i++) {
        		SButton b = buttons.get(i);
        		if (b.getDragged()) {
        			if (b.getOrigin().x==b.getBounds().x && b.getOrigin().y == b.getBounds().y) {
        				b.click(this);
        			} else {
        				b.drop(this);
        			}
        		}
        	}
        	for (SButton b:buttons) {
        		if (b.getHighlighted()) b.setHighlighted(false);
        	}
        	mouse.released = false;
        }
    }
    
    /*
     * draws the game
     * if any buttons do not have their bounds calculated, this will calculate their bounds such that there is word wrapping with the right edge of the screen being a limit.
     */
    
    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        if(logOpen) {
        	g.setColor(black.getColor());
            g.drawRect(width(1), height(6), width(33), height(40));
        }
        for (SButton b:buttons) {
        	if (b.getVisible()) {
        		g.setColor(b.getColor());
        		g.setFont(new Font("TimesNewRoman", Font.PLAIN, b.getFontSize()));
        		FontMetrics fm = g.getFontMetrics();
        		int textW = (int) fm.getStringBounds(b.getText(), g).getWidth();
        		int textH = (int) fm.getStringBounds(b.getText(), g).getHeight();
        		int lines = 1;
        		if (textW+b.getBounds().x>width) {
        			textW = width-b.getBounds().x;
        			String[] words = b.getTextArray();
        			int x = b.getBounds().x;
        			int y = b.getBounds().y+textH;
        			for (int i = 0; i < words.length; i++) {
        				int w = (int) (fm.getStringBounds(words[i]+" ", g).getWidth());
        				if (x+w>width && i>0) {
        					lines++;
        					x = b.getBounds().x;
        					y+=textH;
        				}
        				g.drawString(words[i], x, y);
        				x+=w;
        			}
        		} else {
        			g.drawString(b.getText(), b.getBounds().x, b.getBounds().y+textH);
        		}
        		if (b.getNeedsBounds()) {
        			b.getBounds().width = textW;
        			b.getBounds().height = textH*lines;;
        		}
        		//g.drawRect(b.getBounds().x, b.getBounds().y, b.getBounds().width, b.getBounds().height);
        	}
        }
        g.setColor(veil.getSelected());
        g.fillRect(0, 0, width, height);
        
        g.dispose();
        bs.show();
        ++frames;
    }
    
    public static void main(String[] args) {
        final MainGame game = new MainGame();
        game.frame.setResizable(true);
        game.frame.setTitle("Game");
        game.frame.add(game);
        game.frame.pack();
        game.frame.setDefaultCloseOperation(3);
        game.frame.setLocationRelativeTo(null);
        game.frame.setVisible(true);
        game.start();
    }
}


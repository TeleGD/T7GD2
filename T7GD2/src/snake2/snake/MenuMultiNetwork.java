package snake2.snake;

import general.ui.Button;
import general.ui.ColorPicker;
import general.ui.TGDComponent;
import general.ui.TGDComponent.OnClickListener;
import general.ui.TextField;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import snake2.snake.network_tcp.Client;
import snake2.snake.network_tcp.DiscoverServerThread;
import snake2.snake.network_tcp.DiscoveryThread;
import snake2.snake.network_tcp.Serveur;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class MenuMultiNetwork implements Client.SocketListener {

    private int longueurJeu=(int)(World.longueur*0.8);

    public int hauteurMenu=(int)(World.hauteur/1.5);
    public int longueurMenu= World.longueur/2;
    public int debutx=(longueurJeu-longueurMenu)/2+longueurMenu/15;
    public int debuty=(World.hauteur-hauteurMenu)/2+hauteurMenu/10;
    public int debutdroiteansx=(longueurJeu+longueurMenu)/2-longueurMenu/10-longueurMenu/8;
    public int nJoueur=0;
    public int pas = World.hauteur/20;
    public int yn=debuty+pas;
    public int debutNom = longueurJeu/2 - longueurMenu/10;
    private Button boutonStart;
    public boolean enleve=false;
    private ColorPicker picker;
    private boolean affPicker=false;

    private DiscoverServerThread discoverServerThread;
    private String ipServer;
    private Serveur serveur;
    private TextField nomJoueursField;
    private Button choixCouleur;
    private Client client;

    private ArrayList<Snake> snakes =new ArrayList<>();
    private String nomsJoueurs;

    public MenuMultiNetwork() {

    }

    public void init(final GameContainer container, StateBasedGame game) throws SlickException {

        final Snake snake = findSnakeByIpAdress(World.ipAdress);

        nomJoueursField= new TextField(container , debutNom , yn , longueurMenu/3 , hauteurMenu/15 );
        nomJoueursField.setBackgroundColor(Color.black);
        nomJoueursField.setTextColor(snake.couleur);
        nomJoueursField.setPadding(5, 5, 0, 15);
        nomJoueursField.setOnlyFigures(false);
        nomJoueursField.setMaxNumberOfLetter(20);


        boutonStart = new Button("START",container,longueurJeu/2-longueurMenu/6,(World.hauteur+hauteurMenu)/2-8*hauteurMenu/75,longueurMenu/3,hauteurMenu/15);
        boutonStart.setBackgroundColor(Color.green);
        boutonStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(TGDComponent componenent) {
                World.setSnakes(snakes.toArray(new Snake[snakes.size()]));
            }});

        picker = new ColorPicker(container,0,0, World.longueur/5, World.hauteur/4);
        choixCouleur = new Button(container,longueurJeu/2+longueurMenu/4,yn,hauteurMenu/15,hauteurMenu/15);
        choixCouleur.setBackgroundColor(snake.couleur);
        choixCouleur.setBackgroundColorEntered(new Color(255,255,255,100));
        choixCouleur.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(TGDComponent componenent) {
                // TODO Auto-generated method stub
                picker.setY(choixCouleur.getY());
                picker.setColorSelected(snake.couleur);
                picker.setX(longueurJeu/2+longueurMenu/3);
                affPicker = true;
                picker.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(TGDComponent componenent) {
                        // TODO Auto-generated method stub
                        snake.couleur=picker.getColorSelected();

                        choixCouleur.setBackgroundColor(picker.getColorSelected());
                        nomJoueursField.setTextColor(picker.getColorSelected());
                        affPicker = false;
                    }});
            }});


    }

    public Snake findSnakeByIpAdress(String ipAdress){

        for(int i=0;i<snakes.size();i++){
            if(snakes.get(i).ipAdress.equals(ipAdress)){
                return snakes.get(i);
            }
        }
        return new Snake(Color.white,0,Input.KEY_RIGHT,Input.KEY_RIGHT,10,"default",2);

    }

    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        if (!enleve) {


            g.setColor(new Color(0,0,255));
            g.fillRect((longueurJeu-longueurMenu)/2, (World.hauteur-hauteurMenu)/2, longueurMenu, hauteurMenu);
            g.setColor(new Color(0,0,0));
            g.drawString("Nom : "+nJoueur, debutx, debuty);
            g.drawString("nombre de joueurs : "+nJoueur, debutx, debuty+40);


            for (int i = 0;i<snakes.size();i+=1) {
                yn = debuty+40 + i*pas;
                g.setColor(snakes.get(i).couleur);
                g.drawString("Nom Joueur n "+i+" : "+snakes.get(i).nom,debutx,yn+35);

                if (affPicker) {
                    picker.render(container, game, g);
                }

            }

            nomJoueursField.render(container, game, g);
            choixCouleur.render(container, game, g);

            if(World.isServer){
                boutonStart.render(container, game, g);
            }
        }
    }

    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if(World.isServer){
            boutonStart.update(container, game, delta);
        }
        nomJoueursField.update(container, game, delta);
        choixCouleur.update(container, game, delta);

    }



    public void keyPressed(int i, char c) {
        if(i == Input.KEY_S){

            if(World.isServer ==false){
                World.isServer = true;

                discoverServerThread = new DiscoverServerThread(5000,15);
                discoverServerThread.start();

                try {
                    ipServer = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                snakes.add(new Snake(Color.white,0,Input.KEY_RIGHT,Input.KEY_LEFT,10,nomJoueursField.getText(),2));

                serveur =new Serveur(8887);
                serveur.addOnClientConnectedListener(new Serveur.OnClientConnectedListener() {
                    @Override
                    public void onConnected(Socket socket) {

                    }

                    @Override
                    public void onDisconnected(Socket socket) {

                    }
                });
                serveur.addSocketListener(MenuMultiNetwork.this);
                serveur.start();
            }

        }else if(i == Input.KEY_C){

            World.isServer = false;

            DiscoveryThread thread = new DiscoveryThread();
            thread.addOnServerDetectedListener(new DiscoveryThread.OnServerDetectedListener() {
                @Override
                public void onServerDetected(String ipAdress) {
                    ipServer = ipAdress;
                    client= new Client(ipAdress,8887);

                    String message = "add_joueur;";
                    message += ipAdress+";";

                    Color c = choixCouleur.getBackgroundColor();
                    message += nomJoueursField.getText()+";"+c.getRed()+";"+c.getGreen()+";"+c.getBlue()+";"+c.getAlpha();


                    client.sendString(message);
                    client.sendString("get_connected_players");
                    client.addSocketListener(MenuMultiNetwork.this);
                }
            });

            thread.start();
        }
    }


    public void keyReleased(int i, char c) {

    }


    @Override
    public void onMessageSend(Socket socket, String message) {
        System.out.println("message send= "+message);

    }

    @Override
    public void onMessageReceived(Socket socket, String message) {
        System.out.println("message received= "+message);
        if(message.equals("get_connected_players")){

            message = "received_connected_players;"+snakes.size()+";";
            for(int i=0;i<snakes.size();i++){
                message += snakes.get(i).nom+";"+snakes.get(i).couleur.getRed()+";"+snakes.get(i).couleur.getGreen()+";"+snakes.get(i).couleur.getBlue()+";"+snakes.get(i).couleur.getAlpha()+";";
            }

            serveur.sendStringToAllClients(message);

        }else if(message.startsWith("received_connected_players")){

            //client

            String split[] = message.split(Pattern.quote(";"));

            nJoueur = Integer.parseInt(split[1]);
            snakes.removeAll(snakes);

            for(int i=0;i<nJoueur;i++){
                String nom =split[2+5*i];
                int r = Integer.parseInt(split[2+5*i+1]);
                int g = Integer.parseInt(split[2+5*i+2]);
                int b = Integer.parseInt(split[2+5*i+3]);
                int a = Integer.parseInt(split[2+5*i+4]);
                Color c = new Color(r,g,b,a);
                snakes.add(new Snake(c,12+12*i,Input.KEY_RIGHT,Input.KEY_LEFT,10,nom,10));

            }
        }else if(message.startsWith("add_joueur")){

            String[] split = message.split(Pattern.quote(";"));
            String adresse =split[1];
            String nom =split[2];
            int r = Integer.parseInt(split[3]);
            int g = Integer.parseInt(split[4]);
            int b = Integer.parseInt(split[5]);
            int a = Integer.parseInt(split[6]);
            Color c = new Color(r,g,b,a);


            //int xinit =(100-nJoueur)/(nJoueur+1) + *((100-nJoueur)/(nJoueur+1)+1);

            int size=  snakes.size();

            snakes.add(new Snake(c,snakes.size()*12+12,Input.KEY_RIGHT,Input.KEY_LEFT,10,nom,10));

        }
    }
}

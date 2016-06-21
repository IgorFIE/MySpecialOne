/**
 * Created by codecadet on 20/06/16.
 */
public class Game {

    Field field;
    //list for player

    Player player;
    public Game(){
        Field.init(100, 25);
    }

    public void start(){

        player = new Player();


        while(true) {

            Field.draw(player);
        }
        /*while(true){
            readFromServer();
        }*/

    }

    public void readFromServer(){

    }
}

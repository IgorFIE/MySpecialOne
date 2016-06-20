/**
 * Created by codecadet on 20/06/16.
 */
public class Game {

    Field field;
    //list for player

    public Game(){
        Field.init(100, 25);
    }

    public void start(){

        while(true){
            readFromServer();
        }

    }

    public void readFromServer(){

    }
}

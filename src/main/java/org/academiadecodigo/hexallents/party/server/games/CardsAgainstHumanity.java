package org.academiadecodigo.hexallents.party.server.games;

import org.academiadecodigo.hexallents.party.server.Score;
import org.academiadecodigo.hexallents.party.server.Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codecadet on 24/03/2018.
 */
public class CardsAgainstHumanity extends AbstractGame{

    private final int NUMBER_OF_WHITECARDS = 818;
    private final int NUMBER_OF_BLACKCARDS = 191;
    private BufferedReader whiteCardManager;
    private BufferedReader blackCardManager;
    private List<Integer> usedBCards;
    private List<Integer> usedWCards;



    public CardsAgainstHumanity(Score score, Server server, int rounds) {
        super(score, server, rounds);

    }

    @Override
    public void load() {
        File bCards = new File("src/main/resources/cards-against-humanity/BlackCards.txt");
        File wCards = new File("src/main/resources/cards-against-humanity/WhiteCards.txt");

        try {
            whiteCardManager = new BufferedReader(new FileReader(wCards));
            blackCardManager = new BufferedReader(new FileReader(bCards));
        }catch (IOException io){
            System.err.println("CREATING CARDS ERROR");
        }

        usedBCards = new ArrayList<>();
        usedWCards = new ArrayList<>();

    }

    @Override
    public void start() throws InterruptedException {

    }





    //Obtains a card by its type which is inferred by its maxNumber
    private String getWhiteCard() throws IOException {

        int number = (int) (Math.random()*NUMBER_OF_WHITECARDS);

                if (usedWCards.contains(number)){
                    return getWhiteCard();
                }
                usedWCards.add(number);

        return getCard(number, whiteCardManager);
    }

    private String getBlackCard(){

        int number = (int) (Math.random()*NUMBER_OF_BLACKCARDS);

        if (usedBCards.contains(number)){
            return getBlackCard();
        }
        usedBCards.add(number);

        return getCard(number, blackCardManager);
    }

    private String getCard(int cardIndex, BufferedReader in){
        try {
            while (cardIndex > 0) {
                in.readLine();
                cardIndex--;
            }
            return in.readLine();
        } catch (IOException io ){
            System.err.println("ERROR READING CARDS");
        }

    }

}
